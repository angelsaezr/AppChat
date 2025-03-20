package umu.tds.appchat.vista;

import javax.imageio.ImageIO;
import javax.swing.*;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.utils.Utils;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * Ventana para editar el perfil del usuario (imagen y saludo).
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaEditarPerfil extends JDialog {
    private JLabel lblImagenPerfil, lblSaludo;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private JTextField txtSaludo;
    private File imagenSeleccionada;

    public VentanaEditarPerfil(VentanaMain ventanaMain) {
        super(ventanaMain, "Editar Perfil", true);
        setSize(400, 300);
        setLocationRelativeTo(ventanaMain);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Imagen de perfil
     // Imagen de perfil
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setPreferredSize(new Dimension(100, 100));
        lblImagenPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        //lblImagenPerfil.setOpaque(true);
        //lblImagenPerfil.setBackground(Color.LIGHT_GRAY);
        //lblImagenPerfil.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
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
    	            Image imagenRedondeada = Utils.createRoundedImage(imagenOriginal, 100);
    	            ImageIcon iconoEscalado = new ImageIcon(imagenRedondeada);
    	            lblImagenPerfil.setIcon(iconoEscalado);
    	        }
    	    } catch (IOException e) {
    	        System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
    	        e.printStackTrace();
    	    }
        } else {
        	File file = new File("src/main/resources/profile1.jpg");
            try {
				imagenOriginal = ImageIO.read(file);
				Image imagenRedondeada = Utils.createRoundedImage(imagenOriginal, 100);
	            ImageIcon iconoEscalado = new ImageIcon(imagenRedondeada);
	            lblImagenPerfil.setIcon(iconoEscalado);
			} catch (IOException e1) {
				System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
				e1.printStackTrace();
			}
        }
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(lblImagenPerfil, gbc);

        btnSeleccionarImagen = new JButton("Cambiar Imagen");
        btnSeleccionarImagen.setBackground(new Color(0, 128, 128));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setBorderPainted(false);
        btnSeleccionarImagen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	List<File> archivos = new PanelArrastraImagen(ventanaMain).showDialog();
                if (!archivos.isEmpty()) {
                    imagenSeleccionada = archivos.get(0);
                    try {
						Image imagenOriginal = ImageIO.read(imagenSeleccionada);
						Image imagenRedondeada = Utils.createRoundedImage(imagenOriginal, 100);
	    	            ImageIcon iconoEscalado = new ImageIcon(imagenRedondeada);
	    	            lblImagenPerfil.setIcon(iconoEscalado);
	    	            lblImagenPerfil.revalidate();
	    	            lblImagenPerfil.repaint();
					} catch (IOException e1) {
						System.err.println("No se pudo cargar la imagen: " + imagenSeleccionada.getAbsolutePath());
						e1.printStackTrace();
					}
                }
            }
        });
        
        gbc.gridy = 1;
        panelPrincipal.add(btnSeleccionarImagen, gbc);

        // Saludo
        lblSaludo = new JLabel("Saludo:");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelPrincipal.add(lblSaludo, gbc);

        txtSaludo = new JTextField(AppChat.getInstance().getSaludo());
        txtSaludo.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panelPrincipal.add(txtSaludo, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0, 128, 128));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(e -> {
        	if(!txtSaludo.getText().isBlank()) {
        		if (imagenSeleccionada != null) AppChat.getInstance().cambiarImagenPerfil(imagenSeleccionada);
            	AppChat.getInstance().cambiarSaludo(txtSaludo.getText());
            	JOptionPane.showMessageDialog(this, "Perfil editado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            	ventanaMain.actualizarImagenPerfil();
            	dispose();
        	} else
        		JOptionPane.showMessageDialog(this, "Es obligatorio tener un saludo.", "Error", JOptionPane.ERROR_MESSAGE);
        });

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(e -> {
            dispose();
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
