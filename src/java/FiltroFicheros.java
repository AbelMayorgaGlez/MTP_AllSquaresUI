import javax.swing.filechooser.FileFilter;
import java.io.File;

/** Clase utilizada para filtra archivos con extensión ".asq"*/
public class FiltroFicheros extends FileFilter{

    /** Devuelve true si se acepta el fichero,
	es decir, si es un directorio o un archivo ".asq" */
    public boolean accept(File f){
	if(f.isDirectory()){
	    return true;
	}

	String nombre = f.getName().toLowerCase();
	if(nombre != null){
	    if(nombre.endsWith(".asq")){
		return true;
	    }
	}
	return false;
    }

    /** Devuelve la descripción del filtro */
    public String getDescription(){
	return "Ficheros AllSquares";
    }
}
