package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // Acción del botón buscar (simulación de mensajes)
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarResultados();
            }
        });
    }

    private void mostrarResultados() {
        panelResultados.removeAll();
        
        // Mensajes de prueba
        String[][] mensajes = {
            {"Usuario1", "Usuario2", "Hola, ¿cómo estás?"},
            {"Usuario2", "Usuario1", "Bien, ¿y tú?"},
            {"Usuario3", "Usuario1", "Nos vemos mañana a las 5"},
            {"Manolo", "Javi", "Holaaa"},
            {"Usuario2", "Usuario1", "Bien, ¿y tú?"},
            {"Usuario3", "Usuario1", "Nos vemos mañana a las 5"},
            {"Manolo", "Javi", "Holaaa"}
        };
        
        for (String[] mensaje : mensajes) {
            panelResultados.add(crearPanelMensaje(mensaje[0], mensaje[1], mensaje[2]));
        }
        
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
