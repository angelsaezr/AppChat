package umu.tds.appchat.vista;

import javax.swing.*;

import umu.tds.appchat.controlador.AppChat;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Diálogo para crear un nuevo contacto en la aplicación.
 * Permite al usuario ingresar el nombre y el teléfono del nuevo contacto.
 * Incluye botones para confirmar o cancelar la creación del contacto.
 * 
 * El diálogo valida que ambos campos estén llenos antes de agregar el contacto.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaCrearContacto extends JDialog {
    /**
     * Campo de texto para ingresar el nombre del nuevo contacto.
     */
    private JTextField nameField;

    /**
     * Campo de texto para ingresar el número de teléfono del nuevo contacto.
     */
    private JTextField phoneField;

    /**
     * Botón para confirmar la creación del contacto.
     */
    private JButton btnAceptar;

    /**
     * Botón para cancelar la operación y cerrar el diálogo.
     */
    private JButton btnCancelar;

    /**
     * Crea e inicializa el diálogo para la creación de un nuevo contacto.
     * 
     * @param ventanaMain la ventana principal que invoca este diálogo
     */
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // Etiqueta y campo de Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
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
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
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
        		if(AppChat.getInstance().agregarContactoIndividual(nameField.getText(), phoneField.getText()) != null) {
        			JOptionPane.showMessageDialog(this, "Contacto agregado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
    				ventanaMain.actualizarListaContactos();
    				dispose();
        		} else {
        			JOptionPane.showMessageDialog(this, "El contacto no está registrado en el sistema o ya está agregado", "Error", JOptionPane.ERROR_MESSAGE);
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