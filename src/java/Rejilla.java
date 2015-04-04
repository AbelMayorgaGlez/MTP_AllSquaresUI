import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

/** Clase que representa el espacio de trabajo sobre el que se dibuja y se colocan las etiquetas */
public class Rejilla extends JLayeredPane{
    private Grid rej;
    private int size; /*Tamaño*/
    private int totalSize; /*Tamaño del lado */

    /* Puntos necesarios para calcular el dibujo */
    private MyPoint origen; /*El punto superior izquierdo de la rejilla que se ve en la pantalla */
    private MyPoint limite; /* El punto inferior derecho de la rejilla que se ve en la pantalla */
    private MyPoint puntoDibujo; /* Punto de la pantalla sobre el que se situa el origen de coordenadas de la rejilla */

    private java.util.List<Square> cuadrados; /*Lista de cuadrados*/
    private Map<MyPoint,Integer> puntos = new HashMap<MyPoint,Integer>(); /*Tabla de los puntos calculados */
    private int deltaX = 0, deltaY = 0; /*Desplazamiento desde el punto central de la reticula*/
    private float zoom = 1.0f, MIN_ZOOM = 0.25f, MAX_ZOOM = 5.0f; /* Zoom y límites */
    private AllSquaresGUI contenidoEn;
    private Color colorDeFondo = Color.WHITE, colorDeLinea = Color.BLACK;
    private float grosorDeLinea = 1.0f;
    private boolean rellenarCuadrados = false;
    private Stroke formato = new BasicStroke(grosorDeLinea); /*Formato de las lineas */
    private MouseListener etiquetasML;
 
    /** Constructor de clase */
    public Rejilla(int rejSize,int cuaSize, AllSquaresGUI in){
	try{
	    contenidoEn = in;
	    size = rejSize;
	    totalSize = size*2+1;
	    setLayout(null);

	    rej = new Grid(rejSize,cuaSize,true);

	    origen = new MyPoint();
	    limite = new MyPoint();
	    cuadrados = rej.getSquares();
	    calcularPuntos(contenidoEn.getWidth()-20,contenidoEn.getHeight()-83);
	    
	    /* Al no ser las capas opacas, se puede ver el dibujo */
	    setOpaque(false);

	    /* MouseListener para las etiquetas. Hace que vengan al frente al hacer clic sobre ellas */
	    etiquetasML = new MouseAdapter(){
		    public void mouseClicked(MouseEvent evt){
			JLabel etiqueta = (JLabel) evt.getSource();
			moveToFront(etiqueta);
		    }
		};
	    
	} catch (Exception e){
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}
    }

    /** Calcula los puntos necesarios tomando el ancho y el alto del componente*/
    public void calcularPuntos(){
	calcularPuntos(getWidth(),getHeight());
    }

    /** Calcula los puntos necesarios tomando el ancho y el alto dados */
    public void calcularPuntos(int ancho, int alto){
	int oX = (int) ancho/2 - (int)(size * zoom) - (int)(deltaX * zoom);
	int oY = (int) alto/2 - (int)(size * zoom) - (int)(deltaY * zoom);

	puntoDibujo = new MyPoint(oX,oY);
	if(oX < 0){
	    origen.setX((int)(-oX/zoom));
	} else {
	    origen.setX(0);
	}

	if(oY < 0){
	    origen.setY((int)(-oY/zoom));
	} else {
	    origen.setY(0);
	}
	
	int lX = (int)((ancho - oX)/zoom);
	int lY = (int)((alto - oY)/zoom);

	if(lX > totalSize){
	    limite.setX(totalSize);
	} else {
	    limite.setX(lX);
	}

	if(lY > totalSize){
	    limite.setY(totalSize);
	} else {
	    limite.setY(lY);
	}
    }
    
    /** Dibuja el componente */
    public void paintComponent(Graphics g){
	Graphics2D g2D = (Graphics2D) g;
	g2D.setStroke(formato);
	MyPoint topLeft, bottomRight;
	int lado;
	g2D.setColor(Color.WHITE);
	g2D.fillRect(puntoDibujo.getX(),puntoDibujo.getY(), (int)(totalSize*zoom), (int)(totalSize*zoom));
	g2D.setColor(colorDeLinea);
	for(Square cua : cuadrados){
	    topLeft = cua.getTopLeftCorner();
	    bottomRight = cua.getBottomRightCorner();
	    /* Un cuadrado se dibuja si su esquina superior izquierda o su esquina inferior derecha se ven en la pantalla */
	    if((bottomRight.compareTo(origen) > 0) && (topLeft.compareTo(limite) < 0)){
		lado = (int)((cua.getSize()*2+1)*zoom);
		if(rellenarCuadrados){
		    g2D.setColor(colorDeFondo);
		    g2D.fillRect(puntoDibujo.getX()+(int)(topLeft.getX()*zoom),puntoDibujo.getY()+(int)(topLeft.getY()*zoom),lado,lado);
		    g2D.setColor(colorDeLinea);
		}
		g2D.drawRect(puntoDibujo.getX()+(int)(topLeft.getX()*zoom),puntoDibujo.getY()+(int)(topLeft.getY()*zoom),lado,lado);
	    }
	}

	/* Se quitan todas las etiquetas y se vuelven a poner en la posición correspondiente */
       	quitarEtiquetas();
	int pX,pY;
	Set<MyPoint> conjuntoDePuntos = puntos.keySet();
	for(MyPoint p : conjuntoDePuntos){
	    if(puntoAdmisible(p)){
		pX = puntoDibujo.getX()+(int)(p.getX()*zoom);
		pY = puntoDibujo.getY()+(int)(p.getY()*zoom);
		g2D.drawLine(pX,pY,pX,pY);
	       	ponerEtiqueta(p,pX+5,pY-25,puntos.get(p));
	    }	
	}
    }

    /** Desplaza el dibujo en sentido horizontal */
    public void moveX(int x){
	deltaX = x - size;
	calcularPuntos();
	repaint();
    }

    /** Desplaza el dibujo en sentido vertical */
    public void moveY(int y){
	deltaY = y - size;
	calcularPuntos();
	repaint();
    }

    /** Calcula el nuevo zoom */
    public void darZoom(int deltaZ){
	if((zoom >= 1.0) || (deltaZ < 0) || ((deltaZ > 0) && (zoom > MIN_ZOOM))){
	    if(zoom > 1.0){
		if((zoom - deltaZ >= 1) && (zoom - deltaZ <= MAX_ZOOM)){
		    zoom -= deltaZ;
		}
	    } else if (zoom == 1.0){
		if((deltaZ < 0) && (deltaZ > -(MAX_ZOOM - 1))){
		    zoom -= deltaZ;
		} else {
		    zoom /= 2;
		}
	    } else {
		if(deltaZ < 0){
		    zoom *= 2;
		} else if (zoom > MIN_ZOOM){
		    zoom /= 2;
		}
	    }
	    calcularPuntos();
	    repaint();
	}
	
    }

    /** Muestra el diálogo de opciones */
    public void mostrarOpciones(){
	new OpcionesDialog(contenidoEn,true,this,grosorDeLinea,colorDeLinea,colorDeFondo,rellenarCuadrados);
    }

    /** Calcula un nuevo punto */
    public void ponerPunto(double x, double y){
	MyPoint punto = traducirPunto(x,y);
	if (puntoAdmisible(punto)){
	    puntos.put(punto,rej.count(punto));
	    repaint();
	}
    }

    /** Si el punto está dentro de la parte visible de la rejilla, entonces es admisible */
    public boolean puntoAdmisible(MyPoint punto){
	return ((origen.compareTo(punto) < 0) && (limite.compareTo(punto) > 0));	    
	}

    /** Traduce el punto dado en coordenadas absolutas a las coordenadas de la rejilla */
    public MyPoint traducirPunto(double x, double y){
	return new MyPoint((int)((x-puntoDibujo.getX())/zoom),(int) ((y - puntoDibujo.getY())/zoom));
    }
    
    /** Cambia las propiedades del dibujo */
    public void cambiarPropiedades(float grosor, Color lineas, Color fondo, boolean relleno){
	grosorDeLinea = grosor;
	formato = new BasicStroke(grosorDeLinea);
	colorDeLinea = lineas;
	colorDeFondo = fondo;
	rellenarCuadrados = relleno;
	repaint();
    }

    /** Devuelve el tamaño de la rejilla */
    public int darSize(){
	return rej.getSize();
    }

    /** Devuelve el tamaño del cuadrado principal */
    public int darCuaSize(){
	return rej.getSquareSize();
    }

    /** Devuelve la tabla de puntos */
    public Map<MyPoint,Integer> darPuntos(){
	return puntos;
    }
    
    /** Pone los puntos pasados */
    public void ponerPuntos(Map<MyPoint,Integer> puntos){
	this.puntos = puntos;
    } 
    /** Quita todas las etiquetas */
    public void quitarEtiquetas(){
	removeAll();
    }

    /** Pone una nueva etiqueta */
    public void ponerEtiqueta(MyPoint p,int pX, int pY, int numCuadrados){
	JLabel etiqueta = new JLabel();
	etiqueta.setText(p.toString() + ": " + numCuadrados + (numCuadrados == 1 ? " cuadrado" : " cuadrados"));
	add(etiqueta);
	if (pY<10){
	    pY = 10;
	}
	if((getWidth()-pX) < 175){
	    pX -=175 - (getWidth() - pX);
	}
	etiqueta.setBounds(pX, pY,170,20);
	etiqueta.setOpaque(true);
	etiqueta.setBackground(Color.WHITE);
	etiqueta.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));

	etiqueta.addMouseListener(etiquetasML);
	
    }

}
