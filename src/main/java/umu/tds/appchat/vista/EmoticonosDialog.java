package umu.tds.appchat.vista;

import javax.swing.*;

import tds.BubbleText;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;

/**
 * Diálogo flotante que muestra una selección de emoticonos para enviar en el chat.
 * Al seleccionar uno, se envía automáticamente al contacto receptor y se actualiza la vista.
 * 
 * Se muestra sin decoraciones y con disposición de 2 filas y 4 columnas de emojis.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class EmoticonosDialog extends JDialog {
    /**
     * Panel que contiene los botones con los emoticonos disponibles para enviar.
     */
    private JPanel panelEmojis;

    /**
     * Crea un nuevo diálogo de emoticonos asociado a una ventana principal y a un contacto receptor.
     *
     * @param ventanaMain la ventana principal que invoca el diálogo
     * @param contactoReceptor el contacto al que se le enviará el emoticono seleccionado
     */
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