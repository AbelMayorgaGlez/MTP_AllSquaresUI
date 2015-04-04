import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

/** Clase que representa el cuadro de diálogo Opciones */
public class OpcionesDialog extends JDialog{
    private JLabel lGrosor,lColorLineas, lColorRelleno;
    private JCheckBox usarRelleno;
    private JTextFieldDouble campoSize;
    private float MAX_GROSOR = 5.0f;
    private JButton btnColorLineas, btnColorRelleno, btnAceptar,btnCancelar;
    private Color colorLineas, colorRelleno;
    private JTextField muestraColorLineas, muestraColorRelleno;
    private Rejilla rej;

    /** Constructor de clase */
    public OpcionesDialog(JFrame parent, boolean modal,Rejilla rejilla,float grosor,Color linea, Color fondo, boolean rellenar){
	super(parent, modal);
	initComponents(rejilla,grosor,linea,fondo, rellenar);
    }
    
    /** Inicializa los componentes del cuadro de diálogo */
    private void initComponents(Rejilla rejilla,float grosor, Color colorDeLinea, Color colorDeFondo, boolean rellenar){
	rej = rejilla;
	colorLineas = colorDeLinea;
	colorRelleno = colorDeFondo;

	setSize(400,250);
	setResizable(false);
	getContentPane().setLayout(null);

	lGrosor = new JLabel("Grosor de línea:");
	getContentPane().add(lGrosor);
	lGrosor.setBounds(15,5,120,30);
	
	campoSize = new JTextFieldDouble();
	campoSize.setText("" + grosor);
	getContentPane().add(campoSize);
	campoSize.setBounds(150,5,100,30);
	campoSize.setHorizontalAlignment(SwingConstants.RIGHT);

	/* Se selecciona todo el texto al hacer clic en el campo de texto */
	campoSize.addFocusListener(new FocusAdapter(){
		public void focusGained(FocusEvent evt){
		    ((JTextFieldDouble)evt.getSource()).selectAll();
		}
	    });

	
	lColorLineas = new JLabel("Color de línea:");
	getContentPane().add(lColorLineas);
	lColorLineas.setBounds(15,45,120,30);

	muestraColorLineas = new JTextField();
	getContentPane().add(muestraColorLineas);
	muestraColorLineas.setBounds(150,45,100,30);
	muestraColorLineas.setEditable(false);
	muestraColorLineas.setHorizontalAlignment(SwingConstants.RIGHT);
	cambiarCampoColor(muestraColorLineas,colorLineas);

	/* Action listener aplicado a los botones de selección de color */
	ActionListener al = new ActionListener(){
		/* Dependiendo del botón pulsado, el color seleccionado se asigna a las líneas o al relleno */
		public void actionPerformed(ActionEvent evt){
		    if(evt.getSource() == btnColorLineas){
			colorLineas = JColorChooser.showDialog(null,"Color de líneas",colorLineas);
			cambiarCampoColor(muestraColorLineas,colorLineas);
		    } else {
			colorRelleno = JColorChooser.showDialog(null,"Color de relleno",colorRelleno);
			cambiarCampoColor(muestraColorRelleno,colorRelleno);
		    }
		}
	    };

	btnColorLineas = new JButton("Color...");
	getContentPane().add(btnColorLineas);
	btnColorLineas.setBounds(275,45,100,30);
	btnColorLineas.addActionListener(al);

	usarRelleno = new JCheckBox("Usar relleno",rellenar);
	getContentPane().add(usarRelleno);
	usarRelleno.setBounds(15,95,120,30);
	
	/* Hace que se active o desactive el botón de selección de color de relleno */
	usarRelleno.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent evt){
		    btnColorRelleno.setEnabled(((JCheckBox)evt.getSource()).isSelected());
		}
	    });
	
	lColorRelleno = new JLabel("Color de relleno:");
	getContentPane().add(lColorRelleno);
	lColorRelleno.setBounds(15,125,120,30);

	muestraColorRelleno = new JTextField();
	getContentPane().add(muestraColorRelleno);
	muestraColorRelleno.setBounds(150,125,100,30);
	muestraColorRelleno.setEditable(false);
	muestraColorRelleno.setHorizontalAlignment(SwingConstants.RIGHT);
	cambiarCampoColor(muestraColorRelleno,colorRelleno);
	
	btnColorRelleno = new JButton("Color...");
	getContentPane().add(btnColorRelleno);
	btnColorRelleno.setBounds(275,125,100,30);
	btnColorRelleno.addActionListener(al);
	btnColorRelleno.setEnabled(false);

	btnAceptar = new JButton("Aceptar");
	getContentPane().add(btnAceptar);
	btnAceptar.setBounds(70,180,100,30);
	btnAceptar.addActionListener(new ActionListener(){
		/* Recopila los datos, y si son correctos los envía a la rejilla para que realice los cambios oportunos.
		   Si no, muestra un mensaje de error */
		public void actionPerformed(ActionEvent evt){
		    try{
			float grosor = Float.parseFloat(campoSize.getText());
			if (grosor > MAX_GROSOR){
			    JOptionPane.showMessageDialog(null,"El grosor es demasiado grande para que se visualice bien","Precaución",JOptionPane.WARNING_MESSAGE);
			} else {
			    rej.cambiarPropiedades(grosor,colorLineas,colorRelleno, usarRelleno.isSelected());
			    dispose();
			}
		    } catch (NumberFormatException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
		    }
		}
	    });
	/* El botón aceptar es el botón por defecto. Se selecciona pulsando ENTER */
	getRootPane().setDefaultButton(btnAceptar);

	btnCancelar = new JButton("Cancelar");
	getContentPane().add(btnCancelar);
	btnCancelar.setBounds(230,180,100,30);
	btnCancelar.addActionListener(new ActionListener(){
		/* Simplemente destruye el cuadro de diálogo */
		public void actionPerformed(ActionEvent evt){
		    dispose();
		}
	    });	

	setVisible(true);
    }

    /** Actualiza el campo de texto correspondiente para que muestre el color seleccionado */
    private void cambiarCampoColor(JTextField campo, Color color){
	    campo.setText("(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
    }

}
