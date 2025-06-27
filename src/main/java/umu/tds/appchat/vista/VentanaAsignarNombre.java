package umu.tds.appchat.vista;

import javax.swing.*;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Diálogo que permite asignar un nombre personalizado a un contacto.
 * Proporciona una interfaz sencilla con un campo de texto y botones de confirmación/cancelación.
 * 
 * Valida que el campo no esté vacío y notifica si la operación fue exitosa o fallida.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaAsignarNombre extends JDialog {
    /**
     * Campo de texto donde el usuario escribe el nuevo nombre para el contacto.
     */
    private JTextField nameField;

    /**
     * Botón para confirmar la asignación del nombre al contacto.
     */
    private JButton btnAceptar;

    /**
     * Botón para cancelar la operación y cerrar el diálogo.
     */
    private JButton btnCancelar;

    /**
     * Crea e inicializa el diálogo para asignar un nombre a un contacto dado.
     *
     * @param ventanaMain la ventana principal que invoca el diálogo
     * @param contacto el contacto al que se le asignará un nuevo nombre
     */
	public VentanaAsignarNombre(VentanaMain ventanaMain, Contacto contacto) {
        super(ventanaMain, "Asignar nombre", true);
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
        JLabel lblNombre = new JLabel("Asignar nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

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
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);

        add(panel);

        // Acción de los botones
        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> {
        	if(nameField.getText().isBlank())
        		JOptionPane.showMessageDialog(this, "Es obligatorio rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        	else {
        		if(!AppChat.getInstance().asignarNombre(nameField.getText(), contacto))
        			JOptionPane.showMessageDialog(this, "El contacto no está registrado en el sistema", "Error", JOptionPane.ERROR_MESSAGE);
        		else {
        			JOptionPane.showMessageDialog(this, "Nombre asignado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
        			ventanaMain.setContactoSeleccionado(contacto);
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
    }
}