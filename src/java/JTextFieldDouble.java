import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/** Clase que representa un campo de texto en el que solo se pueden introducir números double positivos */
public class JTextFieldDouble extends JTextField{
    /** Devuelve el modelo personalizado */
    protected Document createDefaultModel(){
	return new PlainDocumentDouble();
    }

    /** Modelo personalizado que solo permite números double positivos */
    protected class PlainDocumentDouble extends PlainDocument{
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
	    char[] fuente = str.toCharArray();
	    char[] resultado = new char[fuente.length];
	    int j = 0;
	    
	    for (int i = 0; i < fuente.length; i++){

		if ((fuente[i] >= '0' && fuente[i] <= '9') || ((fuente[i] == '.') && (getText(0,getLength()).indexOf('.') == -1))){
		    resultado[j++] = fuente[i];
		} else {
		    Toolkit.getDefaultToolkit().beep();
		}
	    }
	    super.insertString(offs, new String(resultado,0,j),a);
	}
    }
}
