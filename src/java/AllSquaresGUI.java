/*Informe de cambios:
Versión 2.5:
-Ahora los elementos del menú tienen icono.
Versión 2.4:
-Ahora cuando se hace clic en una etiqueta, esta se mueve al frente para poder mostrarse sin que otra la solape, tapando al resto de etiquetas.
-El tamaño de los archivos creados se ha reducido notablemente. Ahora solo se guarda el tamaño de la rejilla, el tamaño del cuadrado principal y los puntos calculados, en vez de guardar el patrón completo.
-Ahora, si se conoce el fichero sobre el que se está trabajando, no se pide confirmación para sobreescribirlo.
-Ahora, si se modifica el archivo actual, se indica en la barra de título anteponiendo un asterisco al nombre del fichero.
Versión 2.3:
-Añadido límites superior para el zoom.
-Añadido límite superior para el grosor de línea.
-Añadido diversos mensajes de error.
Versión 2.2:
-Ahora el punto límite del dibujo se calcula correctamente, evitando así dibujar cosas que no se visualizan.
-Solo Windows: Ahora el dibujo se muestra correctamente al redimensionar la ventana.
-Solucionado un error mediante el cual era posible que el zoom fuese 0 o incluso negativo.
-Ahora el límite inferior del zoom es 0.25
-Ahora, el dibujo y las etiquetas se muestran en un JLayeredPane, evitando asi que estas últimas tapen los menus.
-Ahora los cambios en la interfaz gráfica los realiza el hilo despachador de eventos.
Versión 2.1:
-Añadido un filtro para archivos con extensión ".asq".
-Ahora a los archivos guardados sin extensión se les adjunta la extensión ".asq".
-Cambiada la forma con la que se calculan los puntos límite del dibujo para evitar cálculos innecesarios.
Versión 2.0:
-Ahora la rejilla se muestra centrada en la pantalla.
-Ya no se reconocen los puntos externos a la rejilla.
-Ahora el título de los archivos se muestra en la ventana.
-Ahora el zoom sigue el mismo sentido normal.
-Reparado error en las barras de desplazamiento que impedia la visualización de rejillas de tamaño menor que 5.
-Reparado error en las barras de desplazamiento que impedia la correcta visualización de un patrón grande o muy ampliado.
-Depurado el diálogo Opciones.
Versión 1.1:
-Solucionado problema por el cual la rejilla no se borraba correctamente.
-Cambiada la forma de almacenar los puntos para no calcular el número de cuadrados que lo contienen continuamente.
-Añadido cuadro de dialogo de confirmación al realizar acciones que borran el trabajo actual.
-Cambiado el comportamiento de la ventana principal. Ahora pide confirmación para cerrar si se han producido cambios en el trabajo actual.
-Añadidos mensajes de error.
-Depurado el comportamiendo del menu Ayuda.
Versión 1.0:
-Añadido panel de información y mensaje de ayuda.
Versión 0.9:
-Añadidas las funcionalidades de Cargar/Salvar archivo.
Versión 0.8:
-Añadido cuadro de diálogo Opciones.
-Añadida funcionalidad para cambiar el ancho de las líneas, el color de las mismas y la posibilidad de rellenar los cuadrados con color.
-Depurado el cuadro de diálogo nuevo, que por alguna razón no se mostraba bien al empaquetarse en jar.
Versión 0.7:
-Añadido cuadro de diálogo Nuevo.
-Rediseñadas las barras de desplazamiento para que sean acordes al tamaño de la rejilla.
-Cambiado el comportamiento del menú para que ciertos elementos se desactiven cuando no hay rejilla sobre la que trabajar.
-Implementado el menú Limpiar.
-Se ha añadido un borde a las etiquetas para hacerlas más visibles si se solapan.
Versión 0.6:
-Depuración del dibujado de etiquetas para que no solapen el menú y las barras de desplazamiento.
Versión 0.5:
-Cálculo de los puntos que han de dibujarse y los que no.
-Añadido un GlassPane sobre el que colocar las etiquetas.
Versión 0.4:
-Dibujo de puntos haciendo clic con el ratón.
Versión 0.3:
-Cálculo de los cuadrados que han de dibujarse y los que no.
-Añadidas dos barras de desplazamiento para moverse por el dibujo.
-Añadida la opción de hacer zoom con la rueda del ratón.
Versión 0.2:
-Añadido el JPanel con la rejilla.
-Dibujo de cuadrados.
Versión 0.1:
-Creación del menú.
-Implementado el menú Salir.
*/


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/** Clase que representa la ventana principal */
public class AllSquaresGUI extends JFrame{
    
    private float version = 2.5f;
    private String ayudaMsg = "Interfaz gráfica para AllSquares Versión " + version + "\nHecho por Abel Mayorga González" +
	    "\n\nSeleccionar Archivo-Nuevo para crear una nueva rejilla."+
	    "\nSeleccionar Archivo-Cargar para cargar una rejilla previamente guardada.";

    private final int SALIR = 1, BORRAR_REJILLA = 2, NUEVA_REJILLA = 3, CARGAR_REJILLA = 4;

    private JMenuBar barraDeMenus;
    private JMenu menuArchivo, menuEdicion, menuAyuda;
    private JMenuItem menuItemNuevo, menuItemSalvar, menuItemCargar, menuItemOpciones, menuItemSalir, menuItemLimpiar;
    private Rejilla rej = null;
    private JScrollBar despH = null, despV = null;
    private boolean hayRejilla = false;
    private boolean hayCambios = false;
    private JPanel panelDeTrabajo,panelDeInformacion;
    private JLabel lTamRejilla, lTamCuadrado, lCursor;
    private MouseListener etiquetasML;
    private String rutaFichero = null, titulo = null;

    /** Constructor de clase */
    public AllSquaresGUI(){
	setSize(400,400);
	setExtendedState(JFrame.MAXIMIZED_BOTH);
	setTitle("All Squares");
	initComponents();
    }

    /** Inicializa los componentes */
    private void initComponents(){
	/* Cambio del comportamiento de la ventana. Hacer clic en el icono de salir
	   realiza lo mismo que hacer clic en el menu salir */
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent evt){
		    menuItemSalir.doClick();
		}
	    });

	/* Al redimensionar la ventana, se recalculan los puntos de la rejilla */
	addComponentListener(new ComponentAdapter(){
		public void componentResized(ComponentEvent evt){
		    if(rej != null){
			rej.calcularPuntos();
			repaint();
		    }
		}
	    });
	
        barraDeMenus = new JMenuBar();
	setJMenuBar(barraDeMenus);

	menuArchivo = new JMenu("Archivo");
	menuArchivo.setMnemonic('A');
	barraDeMenus.add(menuArchivo);

	menuItemNuevo = new JMenuItem("Nuevo", new ImageIcon(getClass().getResource("/iconos/Nuevo.png")));
	menuItemNuevo.setMnemonic('N');
	menuArchivo.add(menuItemNuevo);
	menuItemNuevo.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    if(hayCambios){
			mostrarGuardarSiONo(NUEVA_REJILLA);
		    } else {
			mostrarNuevoDialogo();
		    }
		}
	    });

	menuItemSalvar = new JMenuItem("Salvar",new ImageIcon(getClass().getResource("/iconos/Salvar.png")));
	menuItemSalvar.setMnemonic('S');
	menuArchivo.add(menuItemSalvar);
	menuItemSalvar.setEnabled(false);
	menuItemSalvar.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    guardar();
		}
	    });
	
	menuItemCargar = new JMenuItem("Cargar",new ImageIcon(getClass().getResource("/iconos/Cargar.png")));
	menuItemCargar.setMnemonic('C');
	menuArchivo.add(menuItemCargar);
	menuItemCargar.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    if(hayCambios){
			mostrarGuardarSiONo(CARGAR_REJILLA);
		    } else {
			cargar();
		    }
		}
	    });

	menuArchivo.addSeparator();

	menuItemOpciones = new JMenuItem("Opciones",new ImageIcon(getClass().getResource("/iconos/Opciones.png")));
	menuItemOpciones.setMnemonic('O');
	menuArchivo.add(menuItemOpciones);
	menuItemOpciones.setEnabled(false);
	menuItemOpciones.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    mostrarOpciones();
		}
	    });

	menuArchivo.addSeparator();

	menuItemSalir = new JMenuItem("Salir",new ImageIcon(getClass().getResource("/iconos/Salir.png")));
	menuItemSalir.setMnemonic('S');
	menuArchivo.add(menuItemSalir);
	menuItemSalir.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    if(hayCambios){
			mostrarGuardarSiONo(SALIR);
		    } else {
			System.exit(0);
		    }
		}
	    });

	menuEdicion = new JMenu("Edicion");
	menuEdicion.setMnemonic('E');
	barraDeMenus.add(menuEdicion);
	menuEdicion.setEnabled(false);

	menuItemLimpiar = new JMenuItem("Limpiar",new ImageIcon(getClass().getResource("/iconos/Limpiar.png")));
	menuItemLimpiar.setMnemonic('L');
	menuEdicion.add(menuItemLimpiar);
	menuItemLimpiar.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    if(hayCambios){
			mostrarGuardarSiONo(BORRAR_REJILLA);
		    } else {
			borrarRejilla();
		    }
		}
	    });

	menuAyuda = new JMenu("Ayuda");
	menuAyuda.setMnemonic('H');
	barraDeMenus.add(menuAyuda);
	/* Nota: No se por qué pero con ActionListener no funcionaba */
       	menuAyuda.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent evt){
		    menuAyuda.setSelected(false);
		    mostrarAyuda();
		}
	    });
   

	panelDeTrabajo = new JPanel();
	getContentPane().add(panelDeTrabajo);
	panelDeTrabajo.setLayout(new BorderLayout());

	panelDeInformacion = new JPanel();
	getContentPane().add(BorderLayout.SOUTH,panelDeInformacion);
	panelDeInformacion.setLayout(new GridLayout(1,3));
	
	lTamRejilla = new JLabel();
	panelDeInformacion.add(lTamRejilla);
	lTamRejilla.setHorizontalAlignment(SwingConstants.CENTER);
	lTamRejilla.setBackground(Color.WHITE);
	lTamRejilla.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));

	lTamCuadrado = new JLabel();
	panelDeInformacion.add(lTamCuadrado);
	lTamCuadrado.setHorizontalAlignment(SwingConstants.CENTER);
	lTamCuadrado.setBackground(Color.WHITE);
	lTamCuadrado.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));

	lCursor = new JLabel();
	panelDeInformacion.add(lCursor);
	lCursor.setHorizontalAlignment(SwingConstants.CENTER);
	lCursor.setBackground(Color.WHITE);
	lCursor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));
    }

    /** Establece si hay cambios en el trabajo actual */
    public void setCambios(boolean c){
	if(!hayCambios && c){
	    setTitle("*" + getTitle());
	}
	hayCambios = c;
    }

    /** Carga un fichero */
    public void cargar(){
	JFileChooser dialogo = new JFileChooser();
	dialogo.addChoosableFileFilter(new FiltroFicheros());
	dialogo.setFileFilter(dialogo.getChoosableFileFilters()[1]);
	int retVal = dialogo.showOpenDialog(this);
	if(retVal == JFileChooser.APPROVE_OPTION){
	    rutaFichero = dialogo.getSelectedFile().getAbsolutePath();
	    titulo = dialogo.getSelectedFile().getName();
	    try{
		FileInputStream fileStream = new FileInputStream(rutaFichero);
		ObjectInputStream os = new ObjectInputStream(fileStream);
		int size = (Integer) os.readObject();
		int cuaSize = (Integer) os.readObject();
		Map<MyPoint,Integer> puntos = (Map<MyPoint,Integer>) os.readObject();
		os.close();
		nuevaRejilla(size, cuaSize);
		rej.ponerPuntos(puntos);
		setTitle(titulo + " - AllSquares");
	    } catch (Exception e){
		JOptionPane.showMessageDialog(this, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    /** Guarda el trabajo actual */
    public void guardar(){
	if(rutaFichero == null){
	    JFileChooser dialogo = new JFileChooser();
	    dialogo.addChoosableFileFilter(new FiltroFicheros());
	    dialogo.setFileFilter(dialogo.getChoosableFileFilters()[1]);
	    int retVal = dialogo.showSaveDialog(this);
	    if(retVal == JFileChooser.APPROVE_OPTION){
		rutaFichero = dialogo.getSelectedFile().getAbsolutePath();
		titulo = dialogo.getSelectedFile().getName();
		if (titulo.indexOf('.') == -1){
		    titulo = titulo + ".asq";
		    rutaFichero = rutaFichero + ".asq";
		}
	    }
	}
	if(rutaFichero != null){
	    try{
		FileOutputStream fileStream = new FileOutputStream(rutaFichero);
		ObjectOutputStream os = new ObjectOutputStream(fileStream);
		os.writeObject(rej.darSize());
		os.writeObject(rej.darCuaSize());
		os.writeObject(rej.darPuntos());
		os.close();
		hayCambios = false;
		setTitle(titulo + " - AllSquares");
	    } catch (Exception e){
		JOptionPane.showMessageDialog(this, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	    }							   
	}
    }

    /** Muestra las opciones */
    public void mostrarOpciones(){
	rej.mostrarOpciones();
    }

    /** Muestra un mensaje de confirmación, y despues realiza la acción correspondiente a su argumento
	arg = SALIR : sale del programa;
	arg = BORRAR_REJILLA : borra el trabajo actual;
	arg = NUEVA_REJILLA : muestra el diálogo Nuevo;
	arg = CARGAR_REJILLA : muestra el diálogo Cargar; */
    public void mostrarGuardarSiONo(int flag){
	int opcion = JOptionPane.showConfirmDialog(this,"Se han producido cambios. ¿Desea guardar?","¿Desea guardar?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
	if(opcion == JOptionPane.YES_OPTION){
	    menuItemSalvar.doClick();
	    if (hayCambios){
		return;
	    }
	}
	if((opcion != JOptionPane.CANCEL_OPTION) && (opcion != JOptionPane.CLOSED_OPTION)){
	    if(flag == SALIR){
		System.exit(0);
	    } else if(flag == BORRAR_REJILLA){
		rutaFichero = null;
		titulo = null;
		borrarRejilla();
	    } else if(flag == NUEVA_REJILLA){
		mostrarNuevoDialogo();
	    } else if(flag == CARGAR_REJILLA){
		cargar();
	    }	   
	}
    }
	       
    /** Muestra el mensaje de ayuda */
    public void mostrarAyuda(){
	JOptionPane.showMessageDialog(this, ayudaMsg, "Ayuda", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/iconos/Ayuda.png")));
    }

    /** Crea una nueva rejilla con las dimensiones pasdas */
    public void nuevaRejilla(int rejSize, int cuaSize){
	borrarRejilla();
	rej = new Rejilla(rejSize,cuaSize,this);
	panelDeTrabajo.add(rej);
	lTamRejilla.setText("Tamaño de la rejilla: " + rejSize);
	lTamCuadrado.setText("Tamaño del cuadrado principal: " + cuaSize);
	
	/* Cambia el zoom */
	rej.addMouseWheelListener(new MouseWheelListener(){
		public void mouseWheelMoved(MouseWheelEvent evt){
		    rej.darZoom(evt.getWheelRotation());
		}
	    });

	/* Marca los puntos */
	rej.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent evt){
		    if(evt.getButton() == MouseEvent.BUTTON1){
			rej.ponerPunto(evt.getPoint().getX(),evt.getPoint().getY());
			setCambios(true);
		    }
		}
	    });

	/* Muestra el punto sobre el que está el cursor en el panel de información */
	rej.addMouseMotionListener(new MouseMotionAdapter(){
		public void mouseMoved(MouseEvent evt){
		    MyPoint punto = rej.traducirPunto(evt.getPoint().getX(),evt.getPoint().getY());
		    if(rej.puntoAdmisible(punto)){
			lCursor.setText("Posición: " + punto);
		    } else {
			lCursor.setText("");
		    }
		}
	    });


	
	despH = new JScrollBar(JScrollBar.HORIZONTAL,rejSize,(int) rejSize/50 ,0,rejSize*2+1);
	panelDeTrabajo.add(BorderLayout.SOUTH,despH);
	despH.addAdjustmentListener(new AdjustmentListener(){
		public void adjustmentValueChanged(AdjustmentEvent evt){
		    rej.moveX(evt.getValue());
		}
	    });

	despV = new JScrollBar(JScrollBar.VERTICAL,rejSize,(int) rejSize/50,0,rejSize*2+1);
	panelDeTrabajo.add(BorderLayout.EAST,despV);
	despV.addAdjustmentListener(new AdjustmentListener(){
		public void adjustmentValueChanged(AdjustmentEvent evt){
		    rej.moveY(evt.getValue());
		}
	    });
	
	/* Se activan los menús desactivados */
	menuItemSalvar.setEnabled(true);
	menuItemOpciones.setEnabled(true);
	menuEdicion.setEnabled(true);
	hayRejilla = true;
	repaint();
    }

    /** Borra la rejilla actual y desactiva los menús correspondientes */
    public void borrarRejilla(){
	if(hayRejilla){
	    panelDeTrabajo.remove(rej);
	    rej = null;
	    panelDeTrabajo.remove(despH);
	    despH = null;
	    panelDeTrabajo.remove(despV);
	    despV = null;;
	    lTamRejilla.setText("");
	    lTamCuadrado.setText("");
	    lCursor.setText("");
	    menuItemSalvar.setEnabled(false);
	    menuItemOpciones.setEnabled(false);
	    menuEdicion.setEnabled(false);
	    repaint();
	    hayRejilla = false;
	    hayCambios = false;
	    setTitle("All Squares");
	}
    }	
    
    /** Muesra el diálogo Nuevo */
    public void mostrarNuevoDialogo(){
	new NuevoDialog(this,true);
    }

    public static void main(String[] args){
	try{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e){
	    System.out.println("No se pudo establecer el aspecto deseado: " + e);
	}
	SwingUtilities.invokeLater(new Runnable(){
		public void run(){
		    new AllSquaresGUI().setVisible(true);
		}
	    });
    }
}
