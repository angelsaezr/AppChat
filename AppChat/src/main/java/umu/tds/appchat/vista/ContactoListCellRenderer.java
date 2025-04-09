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
 * Renderizador personalizado para objetos {@link Contacto} en listas.
 * Muestra la imagen, nombre, número de teléfono y saludo de cada contacto en formato visual.
 * Soporta tanto contactos individuales como grupos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
    /**
     * Panel que contiene las etiquetas de texto (nombre, teléfono y saludo).
     */
    private JPanel panelTexto;

    /**
     * Etiqueta que muestra la imagen del contacto.
     */
    private JLabel lblImagen;

    /**
     * Etiqueta que muestra el nombre del contacto.
     */
    private JLabel lblNombre;

    /**
     * Etiqueta que muestra el número de teléfono o los miembros del grupo.
     */
    private JLabel lblTelefono;

    /**
     * Etiqueta que muestra el saludo del contacto, si es un contacto individual.
     */
    private JLabel lblSaludo;

	/**
     * Crea un nuevo renderizador de celdas para una lista de contactos.
     * Inicializa los componentes visuales como etiquetas e imagen.
     */
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

	 /**
     * Devuelve el componente visual que representa cada celda de la lista.
     *
     * @param listacontactos la lista donde se renderiza el componente
     * @param contacto el contacto a representar
     * @param index índice del elemento en la lista
     * @param isSelected indica si la celda está seleccionada
     * @param cellHasFocus indica si la celda tiene el foco
     * @return componente visual configurado para representar al contacto
     */
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
		lblNombre.setText(AppChat.getInstance().getNombreContacto(contacto));
		
		ContactoIndividual c;
		if (contacto instanceof ContactoIndividual) {
			c = (ContactoIndividual) contacto;
			lblTelefono.setText("Tel: " + c.getMovil());
			lblSaludo.setText(c.getSaludo());
		} else {
			Grupo g = (Grupo) contacto;
			List<String> miembros = AppChat.getInstance().getMiembrosGrupo(g);

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