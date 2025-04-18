package umu.tds.appchat.vista;

import javax.swing.*;

import java.util.List;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.TipoMensaje;
import umu.tds.appchat.dominio.Usuario;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Diálogo de búsqueda de mensajes dentro del historial de conversaciones.
 * Permite filtrar por texto, teléfono o nombre de contacto y muestra los resultados en una lista interactiva.
 * 
 * Los resultados permiten seleccionar rápidamente el contacto relacionado con el mensaje encontrado.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaBuscarMensaje extends JDialog {
    /**
     * Campo de texto para ingresar el contenido del mensaje a buscar.
     */
    private JTextField textFieldTexto;

    /**
     * Campo de texto para ingresar el número de teléfono asociado al mensaje a buscar.
     */
    private JTextField textFieldTelefono;

    /**
     * Campo de texto para ingresar el nombre del contacto relacionado con el mensaje a buscar.
     */
    private JTextField textFieldContacto;

    /**
     * Botón para ejecutar la acción de búsqueda con los criterios introducidos.
     */
    private JButton btnBuscar;

    /**
     * Panel donde se muestran los resultados de la búsqueda.
     */
    private JPanel panelResultados;

    /**
     * Referencia a la ventana principal desde la que se lanza este diálogo.
     */
    private VentanaMain ventanaMain;
    
    /**
     * Crea e inicializa la ventana de búsqueda de mensajes.
     *
     * @param ventanaMain la ventana principal que lanza este diálogo
     */
    public VentanaBuscarMensaje(VentanaMain ventanaMain) {
        super(ventanaMain, "Buscar Mensajes", true);
        this.ventanaMain = ventanaMain;
        setSize(600, 600);
        setLocationRelativeTo(ventanaMain);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        textFieldTexto = new JTextField(15);
        textFieldTelefono = new JTextField(15);
        textFieldContacto = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBuscar.setBackground(new Color(0, 128, 128));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        
        gbc.gridy = 0;
        panelBusqueda.add(new JLabel("Texto"), gbc);
        gbc.gridx = 1;
        panelBusqueda.add(textFieldTexto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelBusqueda.add(new JLabel("Teléfono"), gbc);
        gbc.gridx = 1;
        panelBusqueda.add(textFieldTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelBusqueda.add(new JLabel("Contacto"), gbc);
        gbc.gridx = 1;
        panelBusqueda.add(textFieldContacto, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panelBusqueda.add(btnBuscar, gbc);

        add(panelBusqueda, BorderLayout.NORTH);

        // Panel de resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelResultados);
        add(scrollPane, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> mostrarResultados());
        
        
        // Agrega KeyListener para detectar la tecla Enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnBuscar.doClick(); // Simula el clic en el botón
                }
            }
        };
        
        // Asigna el KeyListener a los campos de entrada
        textFieldTexto.addKeyListener(enterKeyListener);
        textFieldTelefono.addKeyListener(enterKeyListener);
        textFieldContacto.addKeyListener(enterKeyListener);
    }

    private void mostrarResultados() {
    	panelResultados.revalidate();
        panelResultados.repaint();
    	
        panelResultados.removeAll();
        
        if (textFieldTexto.getText().isBlank() && textFieldTelefono.getText().isBlank() && textFieldContacto.getText().isBlank()) {
    	    JOptionPane.showMessageDialog(this, "Debe introducir al menos un criterio de búsqueda.", "Error", JOptionPane.ERROR_MESSAGE);
    	    return;
    	}

        List<Mensaje> mensajes = AppChat.getInstance().buscarMensajes(
            textFieldTexto.getText(), 
            textFieldTelefono.getText(), 
            textFieldContacto.getText()
        );
        
        if (mensajes.size() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron mensajes con los criterios proporcionados.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Usuario usuarioActual = AppChat.getInstance().getUsuarioActual();
        
        List<JPanel> resultados = mensajes.stream()
            .map(m -> {
                Contacto contacto = usuarioActual.getContactos().stream()
                    .filter(c -> c.getMensajes().contains(m))
                    .findFirst().orElse(null);

                String emisor = (m.getTipo() == TipoMensaje.ENVIADO) 
                    ? usuarioActual.getNombre()
                    : contacto.getNombreContacto();
                String receptor = (m.getTipo() == TipoMensaje.ENVIADO) 
                    ? contacto.getNombreContacto() 
                    : usuarioActual.getNombre();
                
                return crearPanelMensaje(emisor, receptor, m.getTexto(), contacto);
            })
            .toList();

        resultados.forEach(panelResultados::add);
    }

    private JPanel crearPanelMensaje(String emisor, String receptor, String mensaje, Contacto contacto) {
        JPanel panelMensaje = new JPanel(new BorderLayout());
        panelMensaje.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        panelMensaje.setPreferredSize(new Dimension(500, 80));
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(240, 248, 255));
        panelSuperior.add(new JLabel("  Emisor: " + emisor, JLabel.LEFT), BorderLayout.WEST);
        panelSuperior.add(new JLabel("Receptor: " + receptor + "  ", JLabel.RIGHT), BorderLayout.EAST);
        
        panelSuperior.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventanaMain.setContactoSeleccionado(contacto);
                dispose();
            }
        });
        
        JTextArea textAreaMensaje = new JTextArea(mensaje);
        textAreaMensaje.setEditable(false);
        textAreaMensaje.setWrapStyleWord(true);
        textAreaMensaje.setLineWrap(true);
        textAreaMensaje.setBackground(Color.WHITE);
        
        textAreaMensaje.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventanaMain.setContactoSeleccionado(contacto);
                dispose();
            }
        });
        
        panelMensaje.add(panelSuperior, BorderLayout.NORTH);
        panelMensaje.add(textAreaMensaje, BorderLayout.CENTER);
        
        panelMensaje.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventanaMain.setContactoSeleccionado(contacto);
                dispose();
            }
        });
        
        return panelMensaje;
    }
}