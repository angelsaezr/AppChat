package umu.tds.appchat.vista;

import javax.swing.*;

import tds.BubbleText;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;

/**
 * Ventana para seleccionar y enviar emoticonos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class EmoticonosDialog extends JDialog {
    private JPanel panelEmojis;

    public EmoticonosDialog(VentanaMain ventanaMain, Contacto contactoReceptor) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        setSize(new Dimension(200, 130));
        setUndecorated(true);
        panelEmojis = new JPanel(new GridLayout(2, 4, 5, 5));
        panelEmojis.setBackground(new Color(240, 248, 255));
        this.setResizable(false);

        for (int i = 0; i < 8; i++) {
            JButton emojiButton = new JButton(BubbleText.getEmoji(i));
            emojiButton.setBackground(Color.WHITE);
            emojiButton.setBorderPainted(false);
            emojiButton.setFocusPainted(false);
            emojiButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            final int index = i;
            emojiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AppChat.getInstance().enviarMensajeContacto(contactoReceptor, "", index);
                    setVisible(false);
                    dispose();
                    ventanaMain.actualizarChat();
                }
            });
            panelEmojis.add(emojiButton);
        }
        add(panelEmojis, BorderLayout.CENTER);
    }
}
