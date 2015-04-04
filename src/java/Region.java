/** Enumeración de regiones:
    IV: banda vertical central dentro del cuadrado.
    OV: banda vertical central fuera del cuadrado.
    IH: banda horizontal central dentro del cuadrado.
    OH: banda horizontal central fuera del cuadrado.
    C1: cuadrante 1 dentro del cuadrado.
    E1: cuadrante 1 fuera del cuadrado.
    C2: cuadrante 2 dentro del cuadrado.
    E2: cuadrante 2 fuera del cuadrado.
    C3: cuadrante 3 dentro del cuadrado.
    E3: cuadrante 3 fuera del cuadrado.
    C4: cuadrante 4 dentro del cuadrado.
    E4: cuadrante 4 fuera del cuadrado.*/
    enum Region{
	NO,IV,IH,OV,OH,C1,E1,C2,E2,C3,E3,C4,E4;

		/** Devuelve true si es una zona interior */
	    public boolean in(){
	    switch(this){
	    case IV:
	    case IH:
	    case C1:
	    case C2:
	    case C3:
	    case C4:
		return true;
	    default: return false;
	    }
	}
    }
