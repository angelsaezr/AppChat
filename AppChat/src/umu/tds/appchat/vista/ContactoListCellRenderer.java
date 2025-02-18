package umu.tds.appchat.vista;

import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<ContactoIndividual> {
	private static final Border SELECCIONADO = BorderFactory.createLineBorder(Color.BLUE, 2);
    private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private JLabel lblImagen;
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JLabel lblSaludo;

	public ContactoListCellRenderer() {
		setLayout(new BorderLayout(10, 10)); // Espaciado entre imagen y texto

		lblImagen = new JLabel();
		lblNombre = new JLabel();
		lblTelefono = new JLabel();
		lblSaludo = new JLabel();

		lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
		lblTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
		lblSaludo.setFont(new Font("Arial", Font.ITALIC, 12));

		JPanel panelTexto = new JPanel(new GridLayout(3, 1)); // Para organizar los textos verticalmente
		panelTexto.add(lblNombre);
		panelTexto.add(lblTelefono);
		panelTexto.add(lblSaludo);

		add(lblImagen, BorderLayout.WEST);  // Imagen a la izquierda
		add(panelTexto, BorderLayout.CENTER);  // Texto a la derecha
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends ContactoIndividual> listacontactos, ContactoIndividual contacto, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Configuración de la imagen
		String fotoUsuario = contacto.getUsuario().getImagen();
		try {
	        Image imagenOriginal;
	        if (fotoUsuario.startsWith("http")) {
	            // Cargar imagen desde URL externa
	            URL url = new URL(fotoUsuario);
	            imagenOriginal = ImageIO.read(url);
	        } else {
	            // Cargar imagen desde recursos locales
	            File file = new File(fotoUsuario);
	            imagenOriginal = ImageIO.read(file);
	        }
	        
	        if (imagenOriginal != null) {
	            Image imagenRedondeada = Utils.createRoundedImage(imagenOriginal, 50);
	            ImageIcon iconoEscalado = new ImageIcon(imagenRedondeada);
	            lblImagen.setIcon(iconoEscalado);
	        }
	    } catch (IOException e) {
	        System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
	        e.printStackTrace();
	    }
		// Configuración del texto
		lblNombre.setText(contacto.getNombre());
		lblTelefono.setText("Tel: " + contacto.getUsuario().getMovil());
		lblSaludo.setText(contacto.getUsuario().getSaludo());

		// Configuración de colores para selección
		if (isSelected) {
            setBackground(new Color(184, 207, 229)); // Color de fondo para selección
            setBorder(SELECCIONADO); // Borde azul para mostrar selección
        } else {
            setBackground(Color.WHITE); // Fondo blanco cuando no está seleccionado
            setBorder(NO_SELECCIONADO); // Sin borde cuando no está seleccionado
        }

		setOpaque(true); // Para que el fondo se muestre correctamente
		return this;
	}
}
