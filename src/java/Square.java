
/** Clase que modeliza un cuadrado */
class Square{
    private final MyPoint centre;
    private final int size;

    /** Crea un cuadrado centrado en el punto y del tamaño indicado */
    public Square(MyPoint p, int siz){
	centre = p;
	size = siz;
    }

    /** Devuelve el punto central del cuadrado */
    public MyPoint getCentre(){
	return centre;
    }

    /** Devuelve el tamaño del cuadrado */
    public int getSize(){
	return size;
    }

    /** Devuelve la esquina superior izquierda del cuadrado */
    public MyPoint getTopLeftCorner(){
	return new MyPoint(centre.getX() - size,centre.getY() - size);
    }

    /** Devuelve la esquina superior derecha del cuadrado */
    public MyPoint getTopRightCorner(){
	return new MyPoint(centre.getX() + size,centre.getY() - size);
    }

    /** Devuelve la esquina inferior izquierda del cuadrado */
    public MyPoint getBottomLeftCorner(){
	return new MyPoint(centre.getX() - size,centre.getY() + size);
    }

    /** Devuelve la esquina inferior derecha del cuadrado */
    public MyPoint getBottomRightCorner(){
	return new MyPoint(centre.getX() + size,centre.getY() + size);
    }

    /** Devuelve la region del cuadrado en la que se encuentra el punto especificado */
    public Region where(MyPoint p){

	int diffX = (int)(centre.getX() - p.getX());
	int absX = Math.abs(diffX);
	int diffY = (int)(centre.getY() - p.getY());
	int absY = Math.abs(diffY);

	if((absX > 2*size) || (absY > 2*size)){
		return Region.NO;
	    }
	if((size % 2) == 0){
	    if(diffX == 0){
		if(absY <= size){
		    return Region.IV;
		} else {
		    return Region.OV;
		}
	    }
	    if (diffY == 0){
		if(absX <= size){
		    return Region.IH;
		} else {
		    return Region.OH;
		}
	    }
	} else {
	    if(absX <= 1){
		if(absY <= size){
		    return Region.IV;
		} else {
		    return Region.OV;
		}
	    }
	    if (absY <= 1){
		if(absX <= size){
		    return Region.IH;
		} else {
		    return Region.OH;
		}
	    }
	}
	if(diffX > 0){
	    if(diffY > 0){
		if((diffX > size) || (diffY > size)){
		    return Region.E1;
		} else {
		    return Region.C1;
		}
	    } else {
		if ((diffX > size) || (diffY < -size)){
		    return Region.E3;
		} else {
		    return Region.C3;
		}
	    }
	} else {
	    if(diffY > 0){
		if ((diffX < -size) || (diffY > size)){
		    return Region.E2;
		} else {
		    return Region.C2;
		}
	    } else {
		if((diffX < -size) || (diffY < -size)){
		    return Region.E4;
		} else {
		    return Region.C4;
		}
	    }
	}	    
    }
}
