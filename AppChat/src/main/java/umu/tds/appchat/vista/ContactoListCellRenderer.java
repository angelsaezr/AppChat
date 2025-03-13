package umu.tds.appchat.vista;

import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel donde se seleccionan los chats.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
    private JPanel panelTexto;
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

		lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSaludo.setFont(new Font("Segoe UI", Font.ITALIC, 12));

		panelTexto = new JPanel(new GridLayout(3, 1)); // Para organizar los textos verticalmente
		panelTexto.setBackground(Color.WHITE);
		panelTexto.add(lblNombre);
		panelTexto.add(lblTelefono);
		panelTexto.add(lblSaludo);

		add(lblImagen, BorderLayout.WEST);  // Imagen a la izquierda
		add(panelTexto, BorderLayout.CENTER);  // Texto a la derecha
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Contacto> listacontactos, Contacto contacto, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Configuración de la imagen
		String fotoUsuario = contacto.getImagen();
		try {
	        Image imagenOriginal;
	        if (fotoUsuario.startsWith("http")) {
	            // Cargar imagen desde URL externa
				URI uri = URI.create(fotoUsuario);  // Crear un objeto URI a partir del String
				URL url = uri.toURL();  // Convertir URI en un objeto URL
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
		lblNombre.setText(AppChat.INSTANCE.getNombreContacto(contacto));
		
		ContactoIndividual c;
		if (contacto instanceof ContactoIndividual) {
			c = (ContactoIndividual) contacto;
			lblTelefono.setText("Tel: " + c.getMovil());
			lblSaludo.setText(c.getSaludo());
		} else {
			Grupo g = (Grupo) contacto;
			List<String> miembros = AppChat.INSTANCE.getMiembrosGrupo(g);

			// Usamos `joining(", ")` para unir los nombres
			String miembrosTexto = miembros.stream()
			    .collect(Collectors.joining(", "));

			lblTelefono.setText("Miembros: " + miembrosTexto);
			lblSaludo.setText("");
		}
		
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240,242,245,255)));
		
		// Configuración de colores para selección
		if (isSelected) {
		    setBackground(new Color(240,242,245,255));
		    panelTexto.setBackground(new Color(240,242,245,255));
        } else {
            setBackground(Color.WHITE);
            panelTexto.setBackground(Color.WHITE);
        }

		setOpaque(true); // Para que el fondo se muestre correctamente
		return this;
	}
}
