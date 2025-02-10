package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class VentanaMain extends JFrame {
    private JPanel panelContactos;
    private JPanel panelChat;
    private JTextField campoBusqueda;
    private JTextArea areaTexto;
    private JList<String> listaContactos;

    public VentanaMain() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Cambiar el icono de la ventana
        Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/logo_icono.png");
        setIconImage(icon);

        // Configuración del panel principal
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(240, 248, 255)); // Fondo claro similar a VentanaLogin
        setContentPane(contentPane);

        // Barra superior
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(240, 248, 255));
        
        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzquierda.setBackground(new Color(240, 248, 255));
        campoBusqueda = new JTextField(20);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonContactos = new JButton("Contactos");
        JButton botonPremium = new JButton("Premium");

        panelIzquierda.add(campoBusqueda);
        panelIzquierda.add(botonBuscar);
        panelIzquierda.add(botonContactos);
        panelIzquierda.add(botonPremium);
        
        // Cargar imagen de perfil redondeada con tamaño fijo
        JLabel imagenPerfil = new JLabel();
        try {
            BufferedImage originalImage = ImageIO.read(new File("src/resources/profile1.jpg"));
            Image roundedImage = createRoundedImage(originalImage, 50);
            imagenPerfil.setIcon(new ImageIcon(roundedImage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.setBackground(new Color(240, 248, 255));
        panelDerecha.add(imagenPerfil);

        barraSuperior.add(panelIzquierda, BorderLayout.WEST);
        barraSuperior.add(panelDerecha, BorderLayout.EAST);
        
        contentPane.add(barraSuperior, BorderLayout.NORTH);

        // Panel izquierdo - Lista de contactos
        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(240, 248, 255));
        listaContactos = new JList<>(new DefaultListModel<>());
        panelContactos.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        panelContactos.setPreferredSize(new Dimension(250, getHeight()));
        contentPane.add(panelContactos, BorderLayout.WEST);

        // Panel derecho - Chat
        panelChat = new JPanel(new BorderLayout());
        panelChat.setBackground(Color.WHITE);
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollChat = new JScrollPane(chatArea);

        // Campo de entrada de texto
        areaTexto = new JTextArea(2, 30);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        JScrollPane scrollTexto = new JScrollPane(areaTexto);

        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.setBackground(new Color(0, 128, 128));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);

        JPanel panelEscribir = new JPanel(new BorderLayout());
        panelEscribir.setBackground(Color.WHITE);
        panelEscribir.add(scrollTexto, BorderLayout.CENTER);
        panelEscribir.add(botonEnviar, BorderLayout.EAST);

        panelChat.add(scrollChat, BorderLayout.CENTER);
        panelChat.add(panelEscribir, BorderLayout.SOUTH);
        contentPane.add(panelChat, BorderLayout.CENTER);
    }

    private Image createRoundedImage(BufferedImage image, int size) {
        BufferedImage roundedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(image, 0, 0, size, size, null);
        g2.dispose();
        return roundedImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaMain().setVisible(true);
        });
    }
}
