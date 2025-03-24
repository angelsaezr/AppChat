package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import com.formdev.flatlaf.FlatLightLaf;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.dominio.TipoMensaje;
import umu.tds.appchat.utils.Utils;

/**
 * Ventana principal de la aplicación.
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
    private JButton botonBuscar, botonContactos, botonCrearContacto, botonCrearGrupo, botonPremium, botonCerrarSesion, botonEnviar, botonEmoticonos, botonEditarContacto;
    private JLabel imagenPerfil, imagenContactoSeleccionado, nombreContactoSeleccionado, starLabel;
    private JPanel barraSuperior, panelIzquierda, panelDerecha, panelAreaTexto, panelEnviar, panelEscribir, panelEmoticonos, panelContactoSeleccionado, panelEditarContacto;
    private DefaultListModel<Contacto> modeloLista;
    private File imagenSeleccionada;
    private EmoticonosDialog popup;
    
	public VentanaMain() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
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
        contentPane.setBackground(new Color(255, 255, 255));
        setContentPane(contentPane);

        // Barra superior
        barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(240, 248, 255));
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 60));

        panelIzquierda = new JPanel(null); // Usar diseño nulo para centrar manualmente
        panelIzquierda.setBackground(new Color(240, 248, 255));
        panelIzquierda.setPreferredSize(new Dimension(800, 60));
        panelDerecha = new JPanel(new GridBagLayout());
        panelDerecha.setBackground(new Color(240, 248, 255));
        
        botonBuscar = crearBotonSuperior("🔍 Buscar Mensajes", new Color(0, 128, 128), 10, 15, 140, 30,
				() -> new VentanaBuscarMensaje(VentanaMain.this).setVisible(true));
        
        botonContactos = crearBotonSuperior("👥  Contactos", new Color(0, 128, 128), 155, 15, 110, 30, () -> {
            VentanaContactos tablaContactos = new VentanaContactos(VentanaMain.this);
            AppChat.getInstance().getContactosUsuarioActual().stream()
                .filter(ContactoIndividual.class::isInstance)
                .map(ContactoIndividual.class::cast)
                .filter(cI -> !cI.getNombre().isBlank())
                .forEach(cI -> tablaContactos.addContactoIndividual(cI.getNombre(), cI.getMovil(), cI.getSaludo()));
            
            AppChat.getInstance().getContactosUsuarioActual().stream()
                .filter(Grupo.class::isInstance)
                .map(Grupo.class::cast)
                .forEach(g -> {
                    String miembros = g.getMiembros().stream()
                        .map(ContactoIndividual::getNombre)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Sin miembros");
                    tablaContactos.addGrupo(g.getNombre(), miembros);
                });
            
            tablaContactos.setVisible(true);
        });

        botonCrearContacto = crearBotonSuperior("➕ Crear Contacto", new Color(0, 128, 128), 270, 15, 130, 30,
        		() ->  new VentanaCrearContacto(VentanaMain.this).setVisible(true));
        
        botonCrearGrupo = crearBotonSuperior("📁 Crear Grupo", new Color(0, 128, 128), 405, 15, 115, 30,
        		() -> new VentanaCrearGrupo(VentanaMain.this).setVisible(true));	
        
        botonCerrarSesion = crearBotonSuperior("🚪 Cerrar Sesión", new Color(255, 69, 0), 630, 15, 110, 30, () -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                VentanaMain.this,
                "¿Seguro que quieres cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                JDialog dialogo = new JDialog(VentanaMain.this, "Cerrar sesión", false);
                JLabel mensaje = new JLabel("Cerrando sesión...", SwingConstants.CENTER);
                mensaje.setFont(new Font("Segoe UI", Font.BOLD, 14));
                dialogo.add(mensaje);
                dialogo.setSize(200, 100);
                dialogo.setLocationRelativeTo(VentanaMain.this);
                dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                dialogo.setVisible(true);

                Timer timer = new Timer(2000, event -> {
                    dialogo.dispose();
                    new VentanaLogin().setVisible(true);
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        
        if(AppChat.getInstance().isPremium()) setPremium();
        else removePremium();
        
        panelIzquierda.add(botonBuscar);
        panelIzquierda.add(botonContactos);
        panelIzquierda.add(botonCrearContacto);
        panelIzquierda.add(botonCrearGrupo);
        panelIzquierda.add(botonCerrarSesion);

        // Cargar imagen de perfil redondeada con tamaño fijo
        imagenPerfil = new JLabel();
        actualizarImagenPerfil();
        
        imagenPerfil.setToolTipText("Perfil");
	    imagenPerfil.setOpaque(true);
	    Color colorOriginal = imagenPerfil.getBackground();

	    imagenPerfil.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            new VentanaEditarPerfil(VentanaMain.this).setVisible(true);
	        }
	
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            imagenPerfil.setBackground(new Color(220, 220, 220)); // Gris claro
	            imagenPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        }
	
	        @Override
	        public void mouseExited(MouseEvent e) {
	            imagenPerfil.setBackground(colorOriginal); // Restaura el color original
	            imagenPerfil.setCursor(Cursor.getDefaultCursor());
	        }
	    });
       
        panelDerecha.add(imagenPerfil);

        barraSuperior.add(panelIzquierda, BorderLayout.WEST);
        barraSuperior.add(panelDerecha, BorderLayout.EAST);

        contentPane.add(barraSuperior, BorderLayout.NORTH);

        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(245, 245, 245));
        
        modeloLista = new DefaultListModel<>();
        
        actualizarListaContactos();
        listaContactos = new JList<>(modeloLista);
        listaContactos.setCellRenderer(new ContactoListCellRenderer());
        listaContactos.setFocusable(false);
        
        listaContactos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Para evitar eventos múltiples en una sola selección
                setContactoSeleccionado(listaContactos.getSelectedValue());
            }
        });
        
        panelContactos.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        panelContactos.setPreferredSize(new Dimension(270, getHeight()));
        contentPane.add(panelContactos, BorderLayout.WEST);
        
        // Panel derecho - Chat
        panelChat = new JPanel(new BorderLayout());
        panelChat.setBackground(Color.WHITE);
        ChatPanel chatPanel = new ChatPanel();
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
            if (popup != null && popup.isVisible()) {
                popup.setVisible(false); // Oculta el diálogo si ya está visible
            } else {
                Point location = botonEmoticonos.getLocationOnScreen();
                popup = new EmoticonosDialog(VentanaMain.this, contactoSeleccionado);
                popup.setLocation(location.x, location.y - 130);
                popup.setVisible(true);
                
                // Agregar MouseListeners para ocultar el popup
                VentanaMain.this.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentMoved(ComponentEvent e) {
                    	if (popup != null && popup.isVisible()) {
                            popup.setVisible(false);
                            VentanaMain.this.removeComponentListener(this); // Eliminar el listener después de ocultarlo
                        }
                    }
                    @Override
                    public void componentResized(ComponentEvent e) {
                    	if (popup != null && popup.isVisible()) {
                            popup.setVisible(false);
                            VentanaMain.this.removeComponentListener(this); // Eliminar el listener después de ocultarlo
                        }
                    }
                });
                WindowStateListener stateListener = new WindowStateListener() {
                    @Override
                    public void windowStateChanged(WindowEvent ev) {
                        if ((ev.getNewState() & Frame.ICONIFIED) != 0 || (ev.getNewState() & Frame.MAXIMIZED_BOTH) != 0) {
                            if (popup != null && popup.isVisible()) {
                                popup.setVisible(false);
                                VentanaMain.this.removeWindowStateListener(this); // Eliminar el listener después de ocultarlo
                            }
                        }
                    }
                };
                VentanaMain.this.addWindowStateListener(stateListener);

            }
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
        
        imagenContactoSeleccionado = new JLabel();
        
        nombreContactoSeleccionado = new JLabel();
        nombreContactoSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelContactoSeleccionado = new JPanel(new BorderLayout(10, 0));
        
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
    	if (popup != null && popup.isVisible()) {
            popup.setVisible(false); // Oculta el diálogo si ya está visible
        }
    	panelChat.setVisible(false);
        if (contactoSeleccionado != null) {
            panelChat.removeAll();
            panelContactoSeleccionado.removeAll();
            
            ChatPanel chatPanel = new ChatPanel();
            
            AppChat.getInstance().getMensajesDelContacto(contactoSeleccionado).stream()
            .forEach(mensaje -> {
                boolean esEnviado = mensaje.getTipo() == TipoMensaje.ENVIADO;
                String nombre = !esEnviado ? contactoSeleccionado.getNombre() : AppChat.getInstance().getNombreUsuarioActual();
                
                if(!esEnviado && contactoSeleccionado instanceof ContactoIndividual && nombre.equals("")) {
                	ContactoIndividual c = (ContactoIndividual) contactoSeleccionado;
                	nombre = c.getMovil();
                }
                
                chatPanel.addMensaje(nombre, mensaje.getTexto(), mensaje.getEmoticono(), esEnviado);
            });


            JScrollPane scrollChat = new JScrollPane(chatPanel);
            scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollChat.setBorder(BorderFactory.createEmptyBorder());
            
            String fotoUsuario = contactoSeleccionado.getImagen();
    		try {
    	        Image imagenOriginal;
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
    	            imagenContactoSeleccionado.setIcon(iconoEscalado);
    	        }
    	    } catch (IOException e) {
    	        System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
    	        e.printStackTrace();
    	    }
            nombreContactoSeleccionado.setText(AppChat.getInstance().getNombreContacto(contactoSeleccionado));
            
            panelContactoSeleccionado = new JPanel(new BorderLayout(10, 0));
            panelContactoSeleccionado.setBackground(Color.WHITE);
            panelContactoSeleccionado.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(196,196,196,196)));
            panelContactoSeleccionado.add(imagenContactoSeleccionado, BorderLayout.WEST);
            panelContactoSeleccionado.add(nombreContactoSeleccionado, BorderLayout.CENTER);
            
            if (contactoSeleccionado instanceof Grupo) {
            	botonEditarContacto = new JButton("Cambiar Imagen");
                botonEditarContacto.setPreferredSize(new Dimension(120, 40));
                botonEditarContacto.setBackground(new Color(0, 128, 128));
                botonEditarContacto.setForeground(Color.WHITE);
                botonEditarContacto.setFocusPainted(false);
                botonEditarContacto.setBorderPainted(false);
                botonEditarContacto.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    	List<File> archivos = new PanelArrastraImagen(VentanaMain.this).showDialog();
                        if (!archivos.isEmpty()) {
                            imagenSeleccionada = archivos.get(0);
                            AppChat.getInstance().cambiarImagenGrupo((Grupo)contactoSeleccionado, imagenSeleccionada);
                            setContactoSeleccionado(contactoSeleccionado);
                        }
                    }
                });
                
                panelEditarContacto = new JPanel(new FlowLayout());
                panelEditarContacto.setBackground(Color.WHITE);
                panelEditarContacto.add(botonEditarContacto);
                
            	panelContactoSeleccionado.add(panelEditarContacto, BorderLayout.EAST);
            	panelContactoSeleccionado.add(panelEditarContacto, BorderLayout.EAST);
            	
            	JButton botonEditarMiembros = new JButton("Editar Miembros");
                botonEditarMiembros.setPreferredSize(new Dimension(120, 40));
                botonEditarMiembros.setBackground(new Color(0, 128, 128));
                botonEditarMiembros.setForeground(Color.WHITE);
                botonEditarMiembros.setFocusPainted(false);
                botonEditarMiembros.setBorderPainted(false);
                botonEditarMiembros.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new VentanaEditarMiembrosGrupo(VentanaMain.this, (Grupo) contactoSeleccionado).setVisible(true);
                    }
                });
                
                panelEditarContacto.add(botonEditarMiembros);
            	
            	JButton botonEliminarGrupo = new JButton("Eliminar Grupo");
            	   botonEliminarGrupo.setPreferredSize(new Dimension(120, 40));
            	   botonEliminarGrupo.setBackground(new Color(255, 69, 0));
            	   botonEliminarGrupo.setForeground(Color.WHITE);
            	   botonEliminarGrupo.setFocusPainted(false);
            	   botonEliminarGrupo.setBorderPainted(false);
            	   botonEliminarGrupo.addMouseListener(new MouseAdapter() {
            		   @Override
            	       public void mouseClicked(MouseEvent e) {
            	           int confirmacion = JOptionPane.showConfirmDialog(
            	               VentanaMain.this,
            	               "¿Seguro que quieres eliminar este grupo?",
            	               "Eliminar Grupo",
            	               JOptionPane.YES_NO_OPTION,
            	               JOptionPane.WARNING_MESSAGE
            	           );

            	           if (confirmacion == JOptionPane.YES_OPTION) {
            	               AppChat.getInstance().eliminarGrupo((Grupo) contactoSeleccionado);
            	               actualizarListaContactos();
            	               panelChat.removeAll();
            	               panelChat.revalidate();
            	               panelChat.repaint();
            	               
            	               JOptionPane.showMessageDialog(VentanaMain.this, "Grupo eliminado correctamente.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            	           }
            	       }
            	   });
            	
            	   panelEditarContacto.add(botonEliminarGrupo);
            	   
            } else if (contactoSeleccionado instanceof ContactoIndividual && contactoSeleccionado.getNombre().equals("")) {
            	botonEditarContacto = new JButton("Asignar nombre");
                botonEditarContacto.setPreferredSize(new Dimension(115, 40));
                botonEditarContacto.setBackground(new Color(0, 128, 128));
                botonEditarContacto.setForeground(Color.WHITE);
                botonEditarContacto.setFocusPainted(false);
                botonEditarContacto.setBorderPainted(false);
                botonEditarContacto.addActionListener(e -> {
                    new VentanaAsignarNombre(this, contactoSeleccionado).setVisible(true);
                });
                
                panelEditarContacto = new JPanel(new FlowLayout());
                panelEditarContacto.setBackground(Color.WHITE);
                panelEditarContacto.add(botonEditarContacto);
                
            	panelContactoSeleccionado.add(panelEditarContacto, BorderLayout.EAST);
            	panelContactoSeleccionado.add(panelEditarContacto, BorderLayout.EAST);
            }
            
            
            panelChat.add(panelContactoSeleccionado, BorderLayout.NORTH);
            panelChat.add(scrollChat, BorderLayout.CENTER);
            panelChat.add(panelEscribir, BorderLayout.SOUTH);
            panelChat.revalidate();
            panelChat.repaint();
            
            SwingUtilities.invokeLater(() -> 
            	scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum())
            );
            panelChat.setVisible(true);
        }
    }
    
    public void actualizarListaContactos() {
        modeloLista.clear();
        List<Contacto> contactos = AppChat.getInstance().getContactosUsuarioActual();
        contactos.forEach(modeloLista::addElement);
    }
    
    public void actualizarImagenPerfil() {
    	String fotoUsuario = AppChat.getInstance().getImagenPerfil();
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
        imagenPerfil.revalidate();
        imagenPerfil.repaint();
	}
    
    private void enviarMensaje() {
    	String texto = areaTexto.getText().trim();
        if (!texto.isEmpty()) {
            // Dividir el texto en líneas usando el salto de línea (\n) como delimitador
            String[] lineas = texto.split("\n");
            
            // Enviar un mensaje por cada línea
            for (String linea : lineas) {
                AppChat.getInstance().enviarMensajeContacto(contactoSeleccionado, linea.trim(), -1);
            }
            areaTexto.setText("");
        	actualizarChat();
        }
    }
    
    public void setContactoSeleccionado(Contacto contacto) {
    	contactoSeleccionado = contacto;
    	listaContactos.setSelectedValue(contacto, true);
    	actualizarChat();
    }
    
    // Edita VentanaMain para que sea la de un usuario premium
    public void setPremium() {
        if(botonPremium != null) panelIzquierda.remove(botonPremium);

        botonPremium = crearBotonSuperior("⭐  Premium", new Color(204, 153, 0), 525, 15, 100, 30, () -> {
            new VentanaPremiumAplicado(VentanaMain.this).setVisible(true);
        });

        panelIzquierda.add(botonPremium);

        // Carga imagen de estrella junto al icono de usuario (a la izquierda)
        try {
            File file = new File("src/main/resources/premium.png");
            Image starImage = ImageIO.read(file);
            ImageIcon starIcon = new ImageIcon(starImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH));

            starLabel = new JLabel(starIcon);
            starLabel.setToolTipText("Eres usuario premium"); // Agrega tooltip al pasar el ratón

            panelDerecha.add(starLabel, 0); // Agrega la estrella antes de la imagen del usuario
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen premium: " + e.getMessage());
        }

        // Refrescar la interfaz
        panelIzquierda.revalidate();
        panelIzquierda.repaint();
        panelDerecha.revalidate();
        panelDerecha.repaint();
    }

    
    // Edita VentanaMain para que sea la de un usuario NO premium
    public void removePremium() {
    	if(botonPremium != null) panelIzquierda.remove(botonPremium);
    	if(starLabel != null) panelDerecha.remove(starLabel);
    	
    	botonPremium = crearBotonSuperior("⭐  Premium", new Color(0, 128, 128), 525, 15, 100, 30,
        		() -> new VentanaPremium(VentanaMain.this).setVisible(true));
    	panelIzquierda.add(botonPremium);
    	
    	panelIzquierda.revalidate();
        panelIzquierda.repaint();
        panelDerecha.revalidate();
        panelDerecha.repaint();
    }
    
    private JButton crearBotonSuperior(String texto, Color colorFondo, int x, int y, int ancho, int alto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setBounds(x, y, ancho, alto);
        boton.setBorderPainted(false);
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });
        return boton;
    }
}
