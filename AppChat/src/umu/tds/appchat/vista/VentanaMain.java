package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.formdev.flatlaf.FlatLightLaf;

@SuppressWarnings("serial")
public class VentanaMain extends JFrame {
    private JPanel panelContactos;
    private JPanel panelChat;
    private JTextArea areaTexto;
    private JList<String> listaContactos;
    JButton botonBuscar, botonContactos;

    public VentanaMain() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Aplicar FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cambiar el icono de la ventana
        Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/logo_icono.png");
        setIconImage(icon);

        // Configuración del panel principal
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(255, 255, 255)); // Colores originales
        setContentPane(contentPane);

        // Barra superior con diseño moderno y centrado vertical
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(240, 248, 255));
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 60));

        JPanel panelIzquierda = new JPanel(null); // Usar diseño nulo para centrar manualmente
        panelIzquierda.setBackground(new Color(240, 248, 255));
        
        botonBuscar = new JButton("🔍 Buscar Mensajes");
        botonBuscar.setBackground(new Color(0, 128, 128));
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setBounds(10, 15, 140, 30);
        
        botonContactos = new JButton("👥  Contactos");
        botonContactos.setBackground(new Color(0, 128, 128));
        botonContactos.setForeground(Color.WHITE);
        botonContactos.setBounds(155, 15, 110, 30);
        botonContactos.addActionListener(e -> {
            JPanel panelContactos = new VentanaContactos();
            getContentPane().removeAll();
            getContentPane().add(panelContactos, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        JButton botonCrearContacto = new JButton("➕ Crear Contacto");
        botonCrearContacto.setBackground(new Color(0, 128, 128));
        botonCrearContacto.setForeground(Color.WHITE);
        botonCrearContacto.setBounds(270, 15, 130, 30);
        
        JButton botonCrearGrupo = new JButton("📁 Crear Grupo");
        botonCrearGrupo.setBackground(new Color(0, 128, 128));
        botonCrearGrupo.setForeground(Color.WHITE);
        botonCrearGrupo.setBounds(405, 15, 115, 30);
        
        JButton botonPremium = new JButton("⭐  Premium");
        botonPremium.setBackground(new Color(0, 128, 128));
        botonPremium.setForeground(Color.WHITE);
        botonPremium.setBounds(525, 15, 100, 30);
        
        JButton botonCerrarSesion = new JButton("🚪 Cerrar Sesión");
        botonCerrarSesion.setBackground(new Color(255, 69, 0));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setBounds(630, 15, 110, 30);
        
        botonPremium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new PremiumDialog(VentanaMain.this).setVisible(true);
            }
        });

        panelIzquierda.setPreferredSize(new Dimension(800, 60));
        panelIzquierda.add(botonBuscar);
        panelIzquierda.add(botonContactos);
        panelIzquierda.add(botonCrearContacto);
        panelIzquierda.add(botonCrearGrupo);
        panelIzquierda.add(botonPremium);
        panelIzquierda.add(botonCerrarSesion);

        // Cargar imagen de perfil redondeada con tamaño fijo
        JLabel imagenPerfil = new JLabel();
        try {
            BufferedImage originalImage = ImageIO.read(new File("src/resources/profile1.jpg"));
            Image roundedImage = createRoundedImage(originalImage, 50);
            imagenPerfil.setIcon(new ImageIcon(roundedImage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panelDerecha = new JPanel(new GridBagLayout()); // Centrado vertical
        panelDerecha.setBackground(new Color(240, 248, 255));
        panelDerecha.add(imagenPerfil);

        barraSuperior.add(panelIzquierda, BorderLayout.WEST);
        barraSuperior.add(panelDerecha, BorderLayout.EAST);

        contentPane.add(barraSuperior, BorderLayout.NORTH);

        // Panel izquierdo - Lista de contactos con borde sutil
        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(245, 245, 245));
        panelContactos.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        listaContactos = new JList<>(new DefaultListModel<>());
        panelContactos.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        panelContactos.setPreferredSize(new Dimension(270, getHeight()));
        contentPane.add(panelContactos, BorderLayout.WEST);

        // Panel derecho - Chat con diseño minimalista
        panelChat = new JPanel(new BorderLayout());
        panelChat.setBackground(Color.WHITE);
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollChat = new JScrollPane(chatArea);

        // Campo de entrada de texto
        areaTexto = new JTextArea(2, 30);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollTexto = new JScrollPane(areaTexto);

        JButton botonEnviar = new JButton("📨  Enviar");
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
