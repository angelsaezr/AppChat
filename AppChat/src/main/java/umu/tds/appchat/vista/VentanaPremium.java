package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.TipoDescuento;

/**
 * Ventana para seleccionar el tipo de descuento para la suscripción Premium.
 * Permite al usuario elegir entre un descuento por fecha o por mensajes,
 * y muestra el precio final a pagar después de aplicar el descuento.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaPremium extends JDialog {
    private JButton btnAceptar, btnCancelar;

    /**
     * Crea la ventana de selección de descuento para el usuario.
     * Permite seleccionar entre un descuento por fecha o por mensajes y calcula
     * el precio final a pagar después de aplicar el descuento.
     * 
     * @param ventanaMain La ventana principal desde la cual se abre esta ventana de selección de descuento.
     */
    public VentanaPremium(VentanaMain ventanaMain) {
        super(ventanaMain, "Seleccionar Descuento Premium", true);
        setSize(400, 160);
        setLocationRelativeTo(ventanaMain);
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblDescuento, gbc);

        JComboBox<String> comboDescuentos = new JComboBox<>(new String[]{"Descuento por Fecha", "Descuento por Mensajes"});
        comboDescuentos.setPreferredSize(new Dimension(180, 25));
        gbc.gridx = 1;
        panel.add(comboDescuentos, gbc);

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
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;  // Ocupar ambas columnas
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);

        add(panel);

        // Acción de los botones
        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> {
            // Obtener el tipo de descuento seleccionado
            String tipoDescuento = (String) comboDescuentos.getSelectedItem();
            TipoDescuento tipo = tipoDescuento.equals("Descuento por Fecha") ? TipoDescuento.FECHA : TipoDescuento.MENSAJE;
            AppChat.getInstance().activarPremium(tipo);
            double descuentoAplicado = AppChat.getInstance().getDescuento();
            double precioFinal = AppChat.COSTE_PREMIUM * (1 - descuentoAplicado);
            
            // Mostrar el diálogo con el precio final
            mostrarDialogoPrecio(precioFinal);
            
            dispose();
            ventanaMain.setPremium();
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
        comboDescuentos.addKeyListener(enterKeyListener);
    }

    /**
     * Muestra un JDialog con el precio final a pagar después del descuento.
     * @param precioFinal Cantidad a pagar con descuento aplicado.
     */
    private void mostrarDialogoPrecio(double precioFinal) {
        JDialog dialogo = new JDialog(this, "Confirmación de Pago", true);
        dialogo.setSize(300, 170);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JTextArea mensaje = new JTextArea("\n¡Enhorabuena! Eres usuario premium.\n\nCantidad anual a pagar: " + String.format("%.2f", precioFinal) + "€\n\n");
        mensaje.setEditable(false);
        mensaje.setOpaque(false);
        mensaje.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mensaje.setLineWrap(true);
        mensaje.setWrapStyleWord(true);
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        dialogo.add(mensaje, BorderLayout.CENTER);


        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(0, 128, 128));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(ev -> dialogo.dispose());
        
        // Agrega KeyListener para detectar la tecla Enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnCerrar.doClick(); // Simula el clic en el botón
                }
            }
        };
        
        // Asigna el KeyListener a los campos de entrada
        mensaje.addKeyListener(enterKeyListener);

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        dialogo.add(panelBoton, BorderLayout.SOUTH);

        dialogo.setVisible(true);
    }
}