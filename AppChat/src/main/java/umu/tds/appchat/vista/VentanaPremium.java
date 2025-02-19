package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class VentanaPremium extends JDialog {
	private JButton btnAceptar, btnCancelar;

    @SuppressWarnings("unused")
	public VentanaPremium(JFrame parent) {
        super(parent, "Seleccionar Descuento Premium", true);
        setSize(400, 160);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        this.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y ComboBox
        JLabel lblDescuento = new JLabel("Selecciona un descuento:");
        lblDescuento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblDescuento, gbc);

        JComboBox<String> comboDescuentos = new JComboBox<>(new String[]{"Descuento Mayores", "Descuento Estudiantes", "Descuento Familiar"});
        comboDescuentos.setPreferredSize(new Dimension(180, 25));
        gbc.gridx = 1;
        panel.add(comboDescuentos, gbc);

        // Etiqueta de cantidad a pagar (centrada horizontalmente)
        JLabel lblCantidad = new JLabel("Cantidad a pagar: 99,75€", SwingConstants.CENTER);
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;  // Ocupar ambas columnas
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblCantidad, gbc);

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
        panel.add(panelBotones, gbc);

        add(panel);

        // Acción de los botones
        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Descuento aplicado: " + comboDescuentos.getSelectedItem());
            dispose();
        });
    }
}
