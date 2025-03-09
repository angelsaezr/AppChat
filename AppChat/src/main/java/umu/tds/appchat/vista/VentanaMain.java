package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import com.formdev.flatlaf.FlatLightLaf;

import umu.tds.appchat.controlador.Controlador;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.TipoMensaje;
import umu.tds.appchat.utils.Utils;

/**
 * Ventana principal de la aplicacion.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaMain extends JFrame {
    private JPanel panelContactos;
    private JPanel panelChat;
    private JTextArea areaTexto;
    private JList<Contacto> listaContactos;
    private Contacto contactoSeleccionado;
    private JButton botonBuscar, botonContactos, botonCrearContacto, botonCrearGrupo, botonPremium, botonCerrarSesion, botonEnviar, botonEmoticonos;
    private JLabel imagenPerfil;
    private JPanel barraSuperior, panelIzquierda, panelDerecha, panelAreaTexto, panelEnviar, panelEscribir, panelEmoticonos;
    private DefaultListModel<Contacto> modeloLista;
    
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
        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/resources/logo_icono.png");
        setIconImage(icon);

        // Configuración del panel principal
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(255, 255, 255)); // Colores originales
        setContentPane(contentPane);

        // Barra superior con diseño moderno y centrado vertical
        barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(240, 248, 255));
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 60));

        panelIzquierda = new JPanel(null); // Usar diseño nulo para centrar manualmente
        panelIzquierda.setBackground(new Color(240, 248, 255));
        
        botonBuscar = new JButton("🔍 Buscar Mensajes");
        botonBuscar.setBackground(new Color(0, 128, 128));
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setBounds(10, 15, 140, 30);
        botonBuscar.setBorderPainted(false);
        botonBuscar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VentanaBuscarMensaje(VentanaMain.this).setVisible(true);
            }
        });
        
        botonContactos = new JButton("👥  Contactos");
        botonContactos.setBackground(new Color(0, 128, 128));
        botonContactos.setForeground(Color.WHITE);
        botonContactos.setBounds(155, 15, 110, 30);
        botonContactos.setBorderPainted(false);
        botonContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VentanaContactos tablaContactos = new VentanaContactos(VentanaMain.this);
                Controlador.INSTANCE.getContactosUsuarioActual().stream()
                	.filter(ContactoIndividual.class::isInstance) // Filtrar solo ContactoIndividual
                	.map(ContactoIndividual.class::cast) // Hacer el casting a ContactoIndividual
                	.filter(cI -> !cI.getNombre().isBlank()) // Filtrar contactos con nombre no vacío
                	.forEach(cI -> tablaContactos.addContactoIndividual(cI.getNombre(), cI.getMovil(), cI.getSaludo())); // Agregar a la tabla

                tablaContactos.setVisible(true);
            }
        });

        botonCrearContacto = new JButton("➕ Crear Contacto");
        botonCrearContacto.setBackground(new Color(0, 128, 128));
        botonCrearContacto.setForeground(Color.WHITE);
        botonCrearContacto.setBounds(270, 15, 130, 30);
        botonCrearContacto.setBorderPainted(false);
        botonCrearContacto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VentanaCrearContacto(VentanaMain.this).setVisible(true);
            }
        });
        
        botonCrearGrupo = new JButton("📁 Crear Grupo");
        botonCrearGrupo.setBackground(new Color(0, 128, 128));
        botonCrearGrupo.setForeground(Color.WHITE);
        botonCrearGrupo.setBounds(405, 15, 115, 30);
        botonCrearGrupo.setBorderPainted(false);
        botonCrearGrupo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VentanaCrearGrupo(VentanaMain.this).setVisible(true);
            }
        });
        
        botonPremium = new JButton("⭐  Premium");
        botonPremium.setBackground(new Color(0, 128, 128));
        botonPremium.setForeground(Color.WHITE);
        botonPremium.setBounds(525, 15, 100, 30);
        botonPremium.setBorderPainted(false);
        botonPremium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VentanaPremium(VentanaMain.this).setVisible(true);
            }
        });
        
        botonCerrarSesion = new JButton("🚪 Cerrar Sesión");
        botonCerrarSesion.setBackground(new Color(255, 69, 0));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setBounds(630, 15, 110, 30);
        botonCerrarSesion.setBorderPainted(false);
        botonCerrarSesion.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    VentanaMain.this,
                    "¿Seguro que quieres cerrar sesión?",
                    "Cerrar sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Crea un JDialog sin botones
                JDialog dialogo = new JDialog(VentanaMain.this, "Cerrar sesión", false);
                JLabel mensaje = new JLabel("Cerrando sesión...", SwingConstants.CENTER);
                mensaje.setFont(new Font("Segoe UI", Font.BOLD, 14));
                dialogo.add(mensaje);

                dialogo.setSize(200, 100);
                dialogo.setLocationRelativeTo(VentanaMain.this);
                dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                dialogo.setVisible(true);

                // Temporizador para cerrar el mensaje y redirigir a la ventana de login
                Timer timer = new Timer(2000, event -> {
                    dialogo.dispose();
                    new VentanaLogin().setVisible(true);
                    dispose(); // Cierra la ventana actual
                });

                timer.setRepeats(false);
                timer.start();
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
        imagenPerfil = new JLabel();
        String fotoUsuario = Controlador.INSTANCE.getImagenPerfil();
        Image imagenOriginal;
        if (fotoUsuario != "") {
        	try {
    	        if (fotoUsuario.startsWith("http")) {
    	            // Cargar imagen desde URL externa
    				URI uri = URI.create(fotoUsuario);  // Crear un objeto URI a partir del String
    				URL url = uri.toURL();  // Convertir URI en un objeto URL
    	            imagenOriginal = ImageIO.read(url);
    	        } else {
    	            // Cargar imagen desde recursos locales
    	            File file = new File(fotoUsuario);
    	            imagenOriginal = ImageIO.read(file);
    	        }
    	        
    	        if (imagenOriginal != null) {
    	            Image imagenRedondeada = Utils.createRoundedImage(imagenOriginal, 50);
    	            ImageIcon iconoEscalado = new ImageIcon(imagenRedondeada);
    	            imagenPerfil.setIcon(iconoEscalado);
    	        }
    	    } catch (IOException e) {
    	        System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
    	        e.printStackTrace();
    	    }
        } else {
        	File file = new File("src/main/resources/profile1.jpg");
            try {
				imagenOriginal = ImageIO.read(file);
				Image imagenRedondeada = Utils.createRoundedImage(imagenOriginal, 50);
	            ImageIcon iconoEscalado = new ImageIcon(imagenRedondeada);
	            imagenPerfil.setIcon(iconoEscalado);
			} catch (IOException e1) {
				System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
				e1.printStackTrace();
			}
        }
        

        panelDerecha = new JPanel(new GridBagLayout()); // Centrado vertical
        panelDerecha.setBackground(new Color(240, 248, 255));
        panelDerecha.add(imagenPerfil);

        barraSuperior.add(panelIzquierda, BorderLayout.WEST);
        barraSuperior.add(panelDerecha, BorderLayout.EAST);

        contentPane.add(barraSuperior, BorderLayout.NORTH);

        // Panel izquierdo - Lista de contactos con borde sutil
        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(245, 245, 245));
        panelContactos.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        
        modeloLista = new DefaultListModel<>();
        
        actualizarListaContactos();
        listaContactos = new JList<>(modeloLista);
        listaContactos.setCellRenderer(new ContactoListCellRenderer());
        
        listaContactos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Para evitar eventos múltiples en una sola selección
                contactoSeleccionado = listaContactos.getSelectedValue();
                actualizarChat(); // Método para mostrar los mensajes del contacto seleccionado
            }
        });

        
        panelContactos.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        panelContactos.setPreferredSize(new Dimension(270, getHeight()));
        contentPane.add(panelContactos, BorderLayout.WEST);
        
        // Panel derecho - Chat
        panelChat = new JPanel(new BorderLayout());
        panelChat.setBackground(Color.WHITE);
        ChatPanel chatPanel = new ChatPanel();
        //chatPanel.addMessage("Hola, ¿cómo estás?", "Yo", true);
        //chatPanel.addMessage("Bien, gracias. ¿Y tú?", "Contacto", false);
        //chatPanel.addEmoticon(3, "Yo", true);
        JScrollPane scrollChat = new JScrollPane(chatPanel);
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChat.setBorder(BorderFactory.createEmptyBorder());

        // Campo de entrada de texto
        areaTexto = new JTextArea(1, 30);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaTexto.setMargin(new Insets(10, 10, 10, 10));
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
        	public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        	public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        	public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });

        botonEnviar = new JButton("📨  Enviar");
        botonEnviar.setBackground(new Color(0, 128, 128));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);
        botonEnviar.setBorderPainted(false);
        botonEnviar.setPreferredSize(new Dimension(80,30));
        
        botonEnviar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enviarMensaje();
            }
        });
        
        botonEmoticonos = new JButton("😊");
        botonEmoticonos.setBackground(new Color(0, 128, 128));
        botonEmoticonos.setForeground(Color.WHITE);
        botonEmoticonos.setFocusPainted(false);
        botonEmoticonos.setBorderPainted(false);
        botonEmoticonos.setPreferredSize(new Dimension(30, 30));
        
        botonEmoticonos.addActionListener(e -> {
            Point location = botonEmoticonos.getLocationOnScreen();
            EmoticonosDialog popup = new EmoticonosDialog(VentanaMain.this, contactoSeleccionado);
            popup.setLocation(location.x, location.y - 130);
            popup.setVisible(true);
        });
        
        panelAreaTexto = new JPanel(new BorderLayout());
        panelEnviar = new JPanel(new FlowLayout());
        panelAreaTexto.add(areaTexto, BorderLayout.CENTER);
        panelEnviar.add(botonEnviar);
        panelEmoticonos = new JPanel(new FlowLayout());
        panelEmoticonos.add(botonEmoticonos);
        
        panelEscribir = new JPanel(new BorderLayout());
        panelEscribir.setBackground(Color.WHITE);
        panelEscribir.add(panelAreaTexto, BorderLayout.CENTER);
        panelEscribir.add(panelEnviar, BorderLayout.EAST);
        panelEscribir.add(panelEmoticonos, BorderLayout.WEST);

        panelChat.add(scrollChat, BorderLayout.CENTER);
        contentPane.add(panelChat, BorderLayout.CENTER);
    }

    // Método para ajustar el tamaño del JTextArea dinámicamente
	private void ajustarTamañoAreaTexto() {
    	int lineas = areaTexto.getLineCount();
    	FontMetrics fm = areaTexto.getFontMetrics(areaTexto.getFont());
    	int alturaLinea = fm.getHeight();
    	int altura = alturaLinea * lineas + 20; // Ajusta el valor según el tamaño de fuente
    	if (altura > 550)
    		altura = 550;
    	areaTexto.setPreferredSize(new Dimension(300, altura));
    	areaTexto.revalidate();
    }

    
    public void actualizarChat() {
        if (contactoSeleccionado != null) {
            panelChat.removeAll(); // Limpiar el panel antes de cargar el nuevo chat
            ChatPanel chatPanel = new ChatPanel();
            
            Controlador.INSTANCE.getMensajes(contactoSeleccionado).stream()
            .forEach(mensaje -> {
                boolean esEnviado = mensaje.getTipo() == TipoMensaje.ENVIADO;
                String nombre = !esEnviado ? contactoSeleccionado.getNombre() : Controlador.INSTANCE.getUsuarioActual().getNombre();
                
                if(!esEnviado && contactoSeleccionado instanceof ContactoIndividual && nombre.equals("")) {
                	ContactoIndividual c = (ContactoIndividual) contactoSeleccionado;
                	nombre = c.getMovil();
                }
                
                chatPanel.addMensaje(nombre, mensaje.getTexto(), mensaje.getEmoticono(), esEnviado);
            });


            JScrollPane scrollChat = new JScrollPane(chatPanel);
            scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollChat.setBorder(BorderFactory.createEmptyBorder());

            panelChat.add(scrollChat, BorderLayout.CENTER);
            panelChat.add(panelEscribir, BorderLayout.SOUTH);
            panelChat.revalidate();
            panelChat.repaint();
            
            SwingUtilities.invokeLater(() -> 
            	scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum())
        );
        }
    }
    
    public void actualizarListaContactos() {
        modeloLista.clear();
        List<Contacto> contactos = Controlador.INSTANCE.getContactosUsuarioActual();
        contactos.forEach(modeloLista::addElement);
    }
    
    private void enviarMensaje() {
    	String texto = areaTexto.getText().trim();
        if (!texto.isEmpty()) {
            // Dividir el texto en líneas usando el salto de línea (\n) como delimitador
            String[] lineas = texto.split("\n");
            
            // Enviar un mensaje por cada línea
            for (String linea : lineas) {
                Controlador.INSTANCE.enviarMensajeContacto(contactoSeleccionado, linea.trim(), -1);
            }
            areaTexto.setText("");
        	actualizarChat();
        }
    }
    
    public void setContactoSeleccionado(Contacto contacto) {
    	contactoSeleccionado = contacto;
    	actualizarChat();
    }

}
