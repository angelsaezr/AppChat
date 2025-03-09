package umu.tds.appchat.vista;

import javax.swing.*;

import java.util.List;

import umu.tds.appchat.controlador.Controlador;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.TipoMensaje;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class VentanaBuscarMensaje extends JDialog {
    private JTextField textFieldTexto, textFieldTelefono, textFieldContacto;
    private JButton btnBuscar;
    private JPanel panelResultados;

    public VentanaBuscarMensaje(JFrame parent) {
        super(parent, "Buscar Mensajes", true);
        setSize(600, 600);
        setLocationRelativeTo(parent);
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
        panelResultados.removeAll();

        List<Mensaje> mensajes = Controlador.INSTANCE.buscarMensajes(
            textFieldTexto.getText(), 
            textFieldTelefono.getText(), 
            textFieldContacto.getText()
        );

        List<JPanel> resultados = mensajes.stream()
            .map(m -> {
                Contacto contacto = Controlador.INSTANCE.getContactosUsuarioActual().stream()
                    .filter(c -> Controlador.INSTANCE.getMensajes(c).contains(m))
                    .findFirst().orElse(null);

                String emisor = (m.getTipo() == TipoMensaje.ENVIADO) 
                    ? Controlador.INSTANCE.getUsuarioActual().getNombre() 
                    : Controlador.INSTANCE.getNombreContacto(contacto);
                String receptor = (m.getTipo() == TipoMensaje.ENVIADO) 
                    ? Controlador.INSTANCE.getNombreContacto(contacto) 
                    : Controlador.INSTANCE.getUsuarioActual().getNombre();

                return crearPanelMensaje(emisor, receptor, m.getTexto());
            })
            .toList();

        resultados.forEach(panelResultados::add);

        panelResultados.revalidate();
        panelResultados.repaint();
    }


    private JPanel crearPanelMensaje(String emisor, String receptor, String mensaje) {
        JPanel panelMensaje = new JPanel(new BorderLayout());
        panelMensaje.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        panelMensaje.setPreferredSize(new Dimension(500, 80));
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(240, 248, 255));
        panelSuperior.add(new JLabel("  Emisor: " + emisor, JLabel.LEFT), BorderLayout.WEST);
        panelSuperior.add(new JLabel("Receptor: " + receptor + "  ", JLabel.RIGHT), BorderLayout.EAST);
        
        JTextArea textAreaMensaje = new JTextArea(mensaje);
        textAreaMensaje.setEditable(false);
        textAreaMensaje.setWrapStyleWord(true);
        textAreaMensaje.setLineWrap(true);
        textAreaMensaje.setBackground(Color.WHITE);
        
        panelMensaje.add(panelSuperior, BorderLayout.NORTH);
        panelMensaje.add(textAreaMensaje, BorderLayout.CENTER);
        
        return panelMensaje;
    }
}
