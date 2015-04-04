import java.io.*;

/** Clase que modeliza un punto en el plano*/
class MyPoint implements Serializable{
	private int x,y;
	
	/** Constructor de clase. Crea un punto en el origen de coordenadas */
	public MyPoint(){
	}

	/** Constructor de clase. Crea un punto con las coordenadas pasadas */
	public MyPoint(int x, int y){
	this.x = x;
	this.y = y;
	}

	/** Devuelve la abscisa */
	public int getX(){
	return x;
	}

	/** Devuelve la ordenada */
	public int getY(){
	return y;
	}

	/** Establece la abscisa */
	public void setX(int x){
	this.x = x;
	}

	/** Establece la ordenada */
	public void setY(int y){
	this.y = y;
	}	

	/** Compara dos puntos por la abscisa */
	public int compareXTo(MyPoint p){
	int pX = p.getX();
	if(x == pX){
		return 0;
	} else if(x > pX){
		return 1;
	} else {
		return -1;
	}
	}

	/** Compara dos puntos por la ordenada */
	public int compareYTo(MyPoint p){
	int pY = p.getY();
	if(y == pY){
		return 0;
	} else if(y > pY){
		return 1;
	} else {
		return -1;
	}
	}

	/** Compara dos puntos. Devuelve 1 si el punto pasado tiene menor abscisa y ordenada que el punto actual, 
		-1 en el caso de que sea al reves, y 0 en cualquier otro caso */
	public int compareTo(MyPoint p){
	if((compareXTo(p) > 0) && (compareYTo(p) > 0)){
		return 1;
	} else if((compareXTo(p) < 0) && (compareYTo(p) < 0)){
		return -1;
	} else {
		return 0;
	}
	}

    /** Devuelve un String que representa al punto */
    public String toString(){
	return "(" + x + "," + y + ")";
    }
}
