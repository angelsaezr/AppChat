package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class VentanaCrearGrupo extends JDialog {
    private JTextField groupNameField;
    private JList<String> contactList;
    private DefaultListModel<String> contactListModel;
    private JButton btnAceptar, btnCancelar;

    @SuppressWarnings("unused")
	public VentanaCrearGrupo(Frame parent) {
        super(parent, "Crear Grupo", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        this.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de Nombre del Grupo
        JLabel lblNombre = new JLabel("Nombre del Grupo:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        groupNameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(groupNameField, gbc);

        // Lista de contactos
        JLabel lblContactos = new JLabel("Seleccionar Contactos:");
        lblContactos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblContactos, gbc);

        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        contactList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(contactList);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 128, 128));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);

        add(panel);

        // Acción de los botones
        btnCancelar.addActionListener(e -> setVisible(false));
        btnAceptar.addActionListener(e -> setVisible(false));
    }
}
