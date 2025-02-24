package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import tds.BubbleText;

/**
 * Clase del panel donde se intercambian mensajes.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
    private JPanel chatContainer;
    private JScrollPane scrollPane;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(new Color(240, 240, 240)); // Fondo uniforme con el panel principal
        chatContainer.setSize(400,700);
        chatContainer.setMinimumSize(new Dimension(400,700));
        chatContainer.setMaximumSize(new Dimension(400,700));
        chatContainer.setPreferredSize(new Dimension(400,700));
        
        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMensaje(String sender, String text, int emoticono, boolean isSent) {
        Color bubbleColor = isSent ? new Color(173, 216, 230) : new Color(200, 200, 200); // Azul claro para enviados, gris para recibidos
        int type = isSent ? BubbleText.SENT : BubbleText.RECEIVED;
        
        BubbleText bubble;
        if (text != null && text != "") {
        	bubble = new BubbleText(chatContainer, text, bubbleColor, sender, type, 14);
        	chatContainer.add(bubble);
            chatContainer.revalidate();
            chatContainer.repaint();
            chatContainer.scrollRectToVisible(chatContainer.getBounds());
            // Desplazar automáticamente al último mensaje
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
        }
        if (emoticono != -1) {
        	bubble = new BubbleText(chatContainer, emoticono, bubbleColor, sender, type, 12);
        	chatContainer.add(bubble);
            chatContainer.revalidate();
            chatContainer.repaint();
            chatContainer.scrollRectToVisible(chatContainer.getBounds());
            // Desplazar automáticamente al último mensaje
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
        }
        return;
    }

    public void addEmoticon(int emojiId, String sender, boolean isSent) {
        Color bubbleColor = isSent ? new Color(173, 216, 230) : new Color(200, 200, 200);
        int type = isSent ? BubbleText.SENT : BubbleText.RECEIVED;
        
        BubbleText bubble = new BubbleText(chatContainer, emojiId, bubbleColor, sender, type, 14);
        chatContainer.add(bubble);
        chatContainer.revalidate();
        chatContainer.repaint();
        
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }
}
