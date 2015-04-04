import javax.swing.*;
import java.awt.event.*;

/** Clase que representa el cuadro de diálogo Nuevo */
public class NuevoDialog extends JDialog{
    private JLabel lRejilla,lCuadrado;
    private JTextFieldEnteroPositivo jTFRejilla,jTFCuadrado;
    private JButton btnAceptar,btnCancelar;
    private AllSquaresGUI padre;

    /** Constructor de clase */
    public NuevoDialog(JFrame parent, boolean modal){
	super(parent,modal);
	padre = (AllSquaresGUI)parent;
	initComponents();
    }

    /** Inicia los componentes del cuadro de diálogo*/
    private void initComponents(){
	setSize(390,210);
	setResizable(false);
	getContentPane().setLayout(null);

	lRejilla = new JLabel("Tamaño de la rejilla:");
	getContentPane().add(lRejilla);
	lRejilla.setBounds(30,30,200,30);

	lCuadrado = new JLabel("Tamaño del cuadrado principal:");
	getContentPane().add(lCuadrado);
	lCuadrado.setBounds(30,90,200,30);

	jTFRejilla = new JTextFieldEnteroPositivo();
	jTFRejilla.setText("1024");
	getContentPane().add(jTFRejilla);
	jTFRejilla.setBounds(260,30,100,30);

	jTFCuadrado = new JTextFieldEnteroPositivo();
	jTFCuadrado.setText("512");
	getContentPane().add(jTFCuadrado);
	jTFCuadrado.setBounds(260,90,100,30);

	FocusListener fl = new FocusAdapter(){
		/* Selecciona todo el texto al obtener el foco */
		public void focusGained(FocusEvent evt){
		    ((JTextFieldEnteroPositivo)evt.getSource()).selectAll();
		}
	    };

	jTFRejilla.addFocusListener(fl);
	jTFCuadrado.addFocusListener(fl);

	btnAceptar = new JButton("Aceptar");
	getContentPane().add(btnAceptar);
	btnAceptar.setBounds(85,140,100,30);
	btnAceptar.addActionListener(new ActionListener(){
		/* Envía los datos introducidos si estos se pueden transformar a números. Si no, muestra un mensaje de error*/
		public void actionPerformed(ActionEvent evt){
		    try{
			mandarResultados(Integer.parseInt(jTFRejilla.getText()),Integer.parseInt(jTFCuadrado.getText()));
		    } catch (NumberFormatException e){
			JOptionPane.showMessageDialog(null, e.getMessage());		       
		    }
		}
	    });
	/* El botón por defecto es el botón aceptar */
	getRootPane().setDefaultButton(btnAceptar);

	btnCancelar = new JButton("Cancelar");
	getContentPane().add(btnCancelar);
	btnCancelar.setBounds(205,140,100,30);
	btnCancelar.addActionListener(new ActionListener(){
		/* Destruye el cuadro de diálogo */
		public void actionPerformed(ActionEvent evt){
		    dispose();
		}
	    });
	
	setVisible(true);
    }

    /** Comprueba los datos y si son correctos, los envía y destruye el cuadro de diálogo */
    private void mandarResultados(int rejSize,int cuaSize){
	if(cuaSize > (rejSize / 2)) {
	    JOptionPane.showMessageDialog(this,"El tamaño del cuadrado debe ser menor o igual a la mitad del tamaño de la rejilla","Error",JOptionPane.ERROR_MESSAGE);
	    jTFCuadrado.requestFocus();
	} else if(cuaSize == 0){
	    JOptionPane.showMessageDialog(this,"El tamaño del cuadrado debe ser mayor que cero","Error",JOptionPane.ERROR_MESSAGE);
	    jTFCuadrado.requestFocus();
	} else {
	    padre.nuevaRejilla(rejSize,cuaSize);
	    padre.setTitle("Sin Título - AllSquares");
	    padre.setCambios(true);
	    dispose();
	}
    }
}
