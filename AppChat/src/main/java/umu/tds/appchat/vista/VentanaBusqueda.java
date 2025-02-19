package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class VentanaBusqueda extends JDialog {
    private JTextField textField;
    private JTextField phoneField;
    private JTextField contactField;
    private JButton btnBuscar, btnCancelar;

    public VentanaBusqueda(Frame parent) {
        super(parent, "Buscar Mensajes", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        this.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo de texto
        JLabel lblTexto = new JLabel("Texto:");
        lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTexto, gbc);

        textField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(textField, gbc);

        // Campo de teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblTelefono, gbc);

        phoneField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        // Campo de contacto
        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblContacto, gbc);

        contactField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(contactField, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0, 128, 128));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);

        panelBotones.add(btnBuscar);
        panelBotones.add(btnCancelar);
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);

        add(panel);

        // Acción de los botones
        btnCancelar.addActionListener(e -> setVisible(false));
        btnBuscar.addActionListener(e -> setVisible(false));
    }
}

@SuppressWarnings("serial")
class VentanaBusquedaResultado extends JDialog {
    public VentanaBusquedaResultado(Frame parent, List<String> results) {
        super(parent, "Resultados de Búsqueda", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        this.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JList<String> resultList = new JList<>(results.toArray(new String[0]));
        panel.add(new JScrollPane(resultList), BorderLayout.CENTER);

        JButton closeButton = new JButton("Cerrar");
        closeButton.setBackground(new Color(255, 69, 0));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> setVisible(false));

        panel.add(closeButton, BorderLayout.SOUTH);
        add(panel);
    }
}