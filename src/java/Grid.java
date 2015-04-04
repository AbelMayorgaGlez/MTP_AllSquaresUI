import java.util.*;

/** Clase que modeliza la rejilla principal en la que va encuadrada el patrón. */
class Grid{
    private static final int DEFAULT_SIZE = 1024;
    private final int size;
    private final int MAX_SQUARE_SIZE;
    private final MyPoint centre;
    private Pattern allSquares;

    /** Crea una nueva rejilla, especificando el tamaño del cuadrado central y la opción de crear el patrón entero. */
    public Grid(int squareSize, boolean generateAll) throws SquareSizeException{
	this(DEFAULT_SIZE, squareSize, generateAll);
    }

    /** Crea una nueva rejilla del tamaño indicado, especificando el tamaño del cuadrado central y la opción de crear el patrón entero. */
    public Grid(int gridSize, int squareSize, boolean generateAll) throws SquareSizeException{
	if (gridSize > 1){
	    size = gridSize;
	} else {
	    throw new SquareSizeException();
	}
	centre = new MyPoint(gridSize,gridSize);
	MAX_SQUARE_SIZE = (int) gridSize / 2;
	if((squareSize <= MAX_SQUARE_SIZE) && (squareSize >= 1)){
	    allSquares = new Pattern(centre,squareSize, generateAll);
	} else {
	    throw new SquareSizeException();
	}
    }
    
    /** Devuelve el número de cuadrados que contienen al punto p */
    public int count(MyPoint p){
	return allSquares.count(p,0);
    }

	/** Devuelve una lista con todos los cuadrados calculados */
	public List<Square> getSquares(){
	return allSquares.getSquares(new ArrayList<Square>());
	}

    /** Devuelve el tamaño de la rejilla */
    public int getSize(){
	return size;
    }

    /** Devuelve el tamaño del cuadrado principal */
    public int getSquareSize(){
	return allSquares.getSquareSize();
    }
}
