package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import tds.BubbleText;

/**
 * Panel personalizado que representa la interfaz del área de conversación (chat).
 * Contiene un contenedor vertical de burbujas de mensajes y un scroll automático.
 * Permite añadir mensajes de texto y emoticonos de forma visual.
 * 
 * Utiliza {@link BubbleText} para renderizar cada mensaje.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class ChatPanel extends JPanel {

    private JPanel chatContainer;
    private JScrollPane scrollPane;

    /**
     * Crea un nuevo {@code ChatPanel} con diseño vertical y estilo visual predeterminado.
     */
    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(new Color(240, 240, 240));
        chatContainer.setSize(400, 100);
        chatContainer.setPreferredSize(new Dimension(400, 0));

        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Añade un mensaje al chat, compuesto de texto, emoticono o ambos.
     *
     * @param sender nombre del remitente
     * @param text texto del mensaje
     * @param emoticono identificador del emoticono (-1 si no hay)
     * @param isSent true si el mensaje fue enviado por el usuario, false si fue recibido
     */
    public void addMensaje(String sender, String text, int emoticono, boolean isSent) {
        Color bubbleColor = isSent ? new Color(173, 216, 230) : new Color(200, 200, 200);
        int type = isSent ? BubbleText.SENT : BubbleText.RECEIVED;

        if (text != null && !text.isBlank()) {
            agregarBurbuja(new BubbleText(chatContainer, text, bubbleColor, sender, type, 14));
        }
        if (emoticono != -1) {
            agregarBurbuja(new BubbleText(chatContainer, emoticono, bubbleColor, sender, type, 12));
        }
    }

    /**
     * Añade un emoticono como burbuja individual al chat.
     *
     * @param emojiId identificador del emoticono
     * @param sender nombre del remitente
     * @param isSent true si el mensaje fue enviado por el usuario, false si fue recibido
     */
    public void addEmoticon(int emojiId, String sender, boolean isSent) {
        Color bubbleColor = isSent ? new Color(173, 216, 230) : new Color(200, 200, 200);
        int type = isSent ? BubbleText.SENT : BubbleText.RECEIVED;

        agregarBurbuja(new BubbleText(chatContainer, emojiId, bubbleColor, sender, type, 14));
    }

    private void agregarBurbuja(BubbleText bubble) {
        chatContainer.add(bubble);

        // Ajustar el tamaño preferido para permitir el crecimiento del chatContainer
        chatContainer.setPreferredSize(new Dimension(chatContainer.getWidth(),
                (int) chatContainer.getPreferredSize().getHeight() + bubble.getHeight()));
        chatContainer.revalidate();
        chatContainer.repaint();
    }
}