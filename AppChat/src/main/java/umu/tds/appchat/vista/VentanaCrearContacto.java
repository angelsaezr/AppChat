package umu.tds.appchat.vista;

import javax.swing.*;

import umu.tds.appchat.controlador.AppChat;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Ventana para añadir o crear contactos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaCrearContacto extends JDialog {
    private JTextField nameField;
    private JTextField phoneField;
    private JButton btnAceptar, btnCancelar;

	public VentanaCrearContacto(VentanaMain ventanaMain) {
        super(ventanaMain, "Crear Contacto", true);
        setSize(400, 180);
        setLocationRelativeTo(ventanaMain);
        setLayout(new GridBagLayout());
        this.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // Etiqueta y campo de Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblTelefono, gbc);

        phoneField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

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
        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> {
        	if(nameField.getText().isBlank() || phoneField.getText().isBlank())
        		JOptionPane.showMessageDialog(this, "Es obligatorio rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        	else {
        		if(AppChat.INSTANCE.agregarContacto(nameField.getText(), phoneField.getText()) != null)
        			JOptionPane.showMessageDialog(this, "El contacto no está registrado en el sistema", "Error", JOptionPane.ERROR_MESSAGE);
        		else {
        			JOptionPane.showMessageDialog(this, "Contacto agregado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
        			ventanaMain.actualizarListaContactos();
        			dispose();
        		}
        	}
        });
        
        // Agrega KeyListener para detectar la tecla Enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAceptar.doClick(); // Simula el clic en el botón
                }
            }
        };
        
        // Asigna el KeyListener a los campos de entrada
        nameField.addKeyListener(enterKeyListener);
        phoneField.addKeyListener(enterKeyListener);
    }
}
