import java.util.*;

/** Clase que modeliza el patron de cuadrados */
class Pattern{
    private MyPoint centre;
    private Square principal;
    private Pattern p1,p2,p3,p4;

    /** Crea un nuevo patrón centrado en p, con el cuadrado principal del tamaño indicado. Da la
	posibilidad de crear todo el patron o solo el cuadrado principal */
    public Pattern(MyPoint p, int size, boolean generateAll){
	centre = p;
	principal = new Square(p,size);
	if((size > 1) && generateAll){
	    p1 = new Pattern(principal.getTopLeftCorner(), (int) principal.getSize() / 2, true);	
	    p2 = new Pattern(principal.getTopRightCorner(), (int) principal.getSize() / 2, true);	
	    p3 = new Pattern(principal.getBottomLeftCorner(), (int) principal.getSize() / 2, true);
	    p4 = new Pattern(principal.getBottomRightCorner(), (int) principal.getSize() / 2, true);
	}
	
    }

    /** Inizializa el subpatron de la región especificada */
    public void initializeSubRegion(Region r){
	if(principal.getSize() > 1){
	    switch(r){
	    case C1:
	    case E1:
		p1 = new Pattern(principal.getTopLeftCorner(), (int) principal.getSize() / 2, false);
		break;
	    case C2:
	    case E2:
		p2 = new Pattern(principal.getTopRightCorner(), (int) principal.getSize() / 2, false);
		break;
	    case C3:
	    case E3:
		p3 = new Pattern(principal.getBottomLeftCorner(), (int) principal.getSize() / 2, false);
		break;
	    case C4:
	    case E4:
		p4 = new Pattern(principal.getBottomRightCorner(), (int) principal.getSize() / 2, false);
		break;
	    }
	}
    }
    /** Devuelve el cuadrado principal */
    public Square getPrincipal(){
	return principal;
    }

    /** Devuelve el subpatron de la región especificada */
    public Pattern getRegion(Region r){
	switch(r){
	case C1:
	case E1: return p1;
	case C2:
	case E2: return p2;
	case C3:
	case E3: return p3;
	case C4:
	case E4: return p4;
	default: return null;
	}
    }

    /** Devuelve el número de cuadrados que contienen al punto p. Utiliza un parámetro acumulador. */
    public int count(MyPoint p, int acumulator){
	Region reg = principal.where(p);
	if (reg.in()){
	    acumulator++;
	}
	Pattern sub = getRegion(reg);
	if(sub == null){
	    initializeSubRegion(reg);
	    sub = getRegion(reg);
	}
	if (sub != null){
	    return sub.count(p,acumulator);
	} else {
	    return acumulator;
	}
    }
	
	/** Devuelve una lista con los cuadrados calculados */
	public List<Square> getSquares(List<Square> list){
	list.add(principal);
	if(p1 != null)
		p1.getSquares(list);
	if(p2 != null)
		p2.getSquares(list);
	if(p3 != null)
		p3.getSquares(list);
	if(p4 != null)
		p4.getSquares(list);
	return list;
	}

	/** Devuelve el tamaño del cuadrado principal */
    public int getSquareSize(){
	return principal.getSize();
    }
}
