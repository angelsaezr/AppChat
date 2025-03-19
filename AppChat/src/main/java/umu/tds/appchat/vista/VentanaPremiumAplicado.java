package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.TipoDescuento;

/**
 * Ventana para gestionar el estado premium del usuario.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaPremiumAplicado extends JDialog {
    private JButton btnExportarPDF, btnAnularPremium;

    public VentanaPremiumAplicado(JFrame parent) {
        super(parent, "Gestionar Premium", true);
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

        // Panel de botones
        JPanel panelBotones = new JPanel();

        btnExportarPDF = crearBoton("Exportar PDF", new Color(70, 130, 180));  // Azul oscuro
        btnAnularPremium = crearBoton("Anular Premium", new Color(220, 20, 60)); // Rojo oscuro

        // Agregar botones al panel
        panelBotones.add(btnExportarPDF);
        panelBotones.add(btnAnularPremium);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);

        add(panel);

        btnExportarPDF.addActionListener(e -> {
            // Abrir la ventana de exportación de PDF
            //VentanaExportarPDF ventanaExportar = new VentanaExportarPDF(VentanaPremiumAplicado.this);
            //ventanaExportar.setVisible(true);
        });

        btnAnularPremium.addActionListener(e -> {
            // Anular la suscripción premium del usuario
            AppChat.getInstance().anularPremium();
            JOptionPane.showMessageDialog(this, "Tu suscripción premium ha sido anulada.", "Premium Anulado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        // Agregar KeyListener para detectar la tecla Enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // TODO btnAceptar.doClick(); // Simula el clic en el botón
                }
            }
        };

     
    }

    /**
     * Crea un botón con el estilo adecuado.
     * @param texto Texto del botón
     * @param color Color de fondo
     * @return JButton estilizado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        return boton;
    }
}
