package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import umu.tds.appchat.controlador.AppChat;

/**
 * Ventana que permite gestionar la suscripción premium del usuario.
 * El usuario puede exportar sus conversaciones a PDF o anular su suscripción premium.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaPremiumAplicado extends JDialog {
    private JButton btnExportarPDF, btnAnularPremium;

    /**
     * Crea la ventana para gestionar la suscripción premium.
     * Permite al usuario exportar su conversación a PDF o anular su suscripción premium.
     * 
     * @param ventanaMain La ventana principal desde la cual se abre esta ventana de gestión.
     */
    public VentanaPremiumAplicado(VentanaMain ventanaMain) {
        super(ventanaMain, "Gestionar Premium", true);
        setSize(400, 200);
        setLocationRelativeTo(ventanaMain);
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

        btnExportarPDF = crearBoton("Exportar PDF", new Color(0, 128, 128));
        btnAnularPremium = crearBoton("Anular Premium", new Color(255, 69, 0));

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
            VentanaExportarPDF ventanaExportar = new VentanaExportarPDF(ventanaMain);
            ventanaExportar.setVisible(true);
        });

        btnAnularPremium.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres anular tu suscripción premium?\nPerderás todas las ventajas.",
                "Confirmar Anulación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Anula la suscripción premium del usuario
                AppChat.getInstance().anularPremium();
                ventanaMain.removePremium();
                JOptionPane.showMessageDialog(
                    this,
                    "Tu suscripción premium ha sido anulada con éxito.",
                    "Premium Anulado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            }
        });

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
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        return boton;
    }
}