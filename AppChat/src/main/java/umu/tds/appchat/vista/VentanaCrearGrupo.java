package umu.tds.appchat.vista;

import javax.swing.*;

import java.util.LinkedList;
import java.util.List;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Diálogo para crear un nuevo grupo de contactos. 
 * Permite al usuario seleccionar contactos para agregar al grupo, 
 * asignar un nombre y una imagen al grupo.
 * 
 * Los contactos se pueden mover entre las listas disponibles y seleccionados, 
 * y se valida que todos los campos estén correctamente rellenados antes de crear el grupo.
 * 
 * @author Ángel
 * @author Francisco Javier
 */

@SuppressWarnings("serial")
public class VentanaCrearGrupo extends JDialog {
	/**
     * Ruta de la imagen por defecto.
     */
	private String IMAGEN_POR_DEFECTO = "src/main/resources/grupo2.jpg";
    /**
     * Campo de texto donde se introduce el nombre del nuevo grupo.
     */
    private JTextField groupNameField;

    /**
     * Lista que muestra los contactos disponibles para agregar al grupo.
     */
    private JList<String> contactList;

    /**
     * Lista que muestra los contactos ya seleccionados para el grupo.
     */
    private JList<String> groupList;

    /**
     * Lista de contactos del usuario actual, utilizada como fuente para la creación del grupo.
     */
    private List<Contacto> listaContactos;

    /**
     * Modelo de la lista de contactos disponibles.
     */
    private DefaultListModel<String> contactListModel;

    /**
     * Modelo de la lista de contactos seleccionados para el grupo.
     */
    private DefaultListModel<String> groupListModel;

    /**
     * Botón para confirmar la creación del grupo.
     */
    private JButton btnAceptar;

    /**
     * Botón para cancelar la operación y cerrar el diálogo.
     */
    private JButton btnCancelar;

    /**
     * Botón para mover contactos desde la lista de disponibles a la de seleccionados.
     */
    private JButton btnAdd;

    /**
     * Botón para remover contactos de la lista de seleccionados y devolverlos a la lista de disponibles.
     */
    private JButton btnRemove;

    /**
     * Botón para seleccionar una imagen desde el sistema de archivos para el grupo.
     */
    private JButton btnSeleccionarImagen;

    /**
     * Etiqueta que muestra una vista previa de la imagen seleccionada para el grupo.
     */
    private JLabel lblImagenSeleccionada;

    /**
     * Archivo de imagen seleccionado como avatar del grupo.
     */
    private File imagenSeleccionada;

    /**
     * Crea e inicializa el diálogo para crear un nuevo grupo.
     * Permite al usuario seleccionar contactos y asignarles un nombre y una imagen.
     * 
     * @param ventanaMain la ventana principal que invoca este diálogo
     */
    public VentanaCrearGrupo(VentanaMain ventanaMain) {
        super(ventanaMain, "Crear Grupo", true);
        setSize(500, 500);
        setLocationRelativeTo(ventanaMain);
        setLayout(new GridBagLayout());
        this.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Nombre del Grupo
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre del Grupo:"), gbc);
        groupNameField = new JTextField(33);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(groupNameField, gbc);

        // Listas
        contactListModel = new DefaultListModel<>();
        groupListModel = new DefaultListModel<>();
        listaContactos = AppChat.getInstance().getContactosUsuarioActual();
        listaContactos.stream()
        	.filter(c -> c instanceof ContactoIndividual)
        	.filter(c -> AppChat.getInstance().esContactoAgregado(c))
        	.map(c -> AppChat.getInstance().getNombreContacto(c) + " (" + ((ContactoIndividual) c).getMovil() + ")")
        	.forEach(contactListModel::addElement);


        contactList = new JList<>(contactListModel);
        groupList = new JList<>(groupListModel);
        contactList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        groupList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollContactList = new JScrollPane(contactList);
        JScrollPane scrollGroupList = new JScrollPane(groupList);
        scrollContactList.setPreferredSize(new Dimension(150, 150));
        scrollGroupList.setPreferredSize(new Dimension(150, 150));

        // Botones para mover contactos
        btnAdd = new JButton(">>");
        btnRemove = new JButton("<<");
        btnAdd.addActionListener(e -> moverSeleccionados(contactList, contactListModel, groupListModel));
        btnRemove.addActionListener(e -> moverSeleccionados(groupList, groupListModel, contactListModel));
        btnAdd.setPreferredSize(new Dimension(50, 30));
        btnRemove.setPreferredSize(new Dimension(50, 30));

        // Panel de listas y botones
        JPanel panelListas = new JPanel(new GridBagLayout());
        panelListas.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = 2;
        panelListas.add(new JLabel("Contactos disponibles:"), gbc);
        gbc.gridx = 2;
        panelListas.add(new JLabel("Contactos seleccionados:"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panelListas.add(scrollContactList, gbc);

        gbc.gridx = 1;
        JPanel panelBotonesMover = new JPanel(new GridBagLayout());
        panelBotonesMover.setOpaque(false);
        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.insets = new Insets(5, 0, 5, 0);
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 0;
        panelBotonesMover.add(btnAdd, gbcBotones);
        gbcBotones.gridy = 1;
        panelBotonesMover.add(btnRemove, gbcBotones);
        panelListas.add(panelBotonesMover, gbc);

        gbc.gridx = 2;
        panelListas.add(scrollGroupList, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(panelListas, gbc);

        // Panel de selección de imagen
        JPanel panelImagen = new JPanel(new GridBagLayout());
        panelImagen.setOpaque(false);

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setPreferredSize(new Dimension(300, 25));
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
                    ImageIcon icon = new ImageIcon(imagenSeleccionada.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblImagenSeleccionada.setIcon(new ImageIcon(img));
                    lblImagenSeleccionada.setVisible(true);
                }
            }
        });
        
        lblImagenSeleccionada = new JLabel();
        lblImagenSeleccionada.setHorizontalAlignment(JLabel.CENTER);
        lblImagenSeleccionada.setVisible(false);
        
        gbc.gridy = 4;
        panelImagen.add(lblImagenSeleccionada, gbc);
        gbc.gridy = 5;
        panelImagen.add(btnSeleccionarImagen, gbc);
        
        gbc.gridy = 3;
        panel.add(panelImagen, gbc);

        // Botones Aceptar y Cancelar
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 128, 128));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setBorderPainted(false);
        btnAceptar.setFocusPainted(false);
        
        btnAceptar.addActionListener(e -> {
        	if(groupNameField.getText().isBlank() || groupListModel.isEmpty())
        		JOptionPane.showMessageDialog(this, "Es obligatorio rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        	else {
        		List<ContactoIndividual> miembros = new LinkedList<>();
                
        		for (int i = 0; i < groupListModel.getSize(); i++) {
                    String entrada = groupListModel.get(i);
                    // Extrae el número de teléfono entre paréntesis
                    int inicio = entrada.indexOf('(');
                    int fin = entrada.indexOf(')');
                    if (inicio != -1 && fin != -1 && inicio < fin) {
                        String movil = entrada.substring(inicio + 1, fin);
                        for (Contacto c : listaContactos) {
                            if (c instanceof ContactoIndividual) {
                                ContactoIndividual ci = (ContactoIndividual) c;
                                if (ci.getMovil().equals(movil)) {
                                    miembros.add(ci);
                                    break;
                                }
                            }
                        }
                    }
                }
        		
                String rutaImagen = "";
                if (imagenSeleccionada != null)
                    rutaImagen = imagenSeleccionada.getAbsolutePath();
                else
                    rutaImagen = IMAGEN_POR_DEFECTO;
        		if(AppChat.getInstance().agregarGrupo(groupNameField.getText(), miembros, rutaImagen) == null)
        			JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
        		else {
        			JOptionPane.showMessageDialog(this, "Grupo creado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
        			ventanaMain.actualizarListaContactos();
        			dispose();
        		}
        	}
        });
        
        // Agrega KeyListener para detectar la tecla Enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAceptar.doClick(); // Simula el clic en el botón
                }
            }
        };
        
        // Asigna el KeyListener a los campos de entrada
        groupNameField.addKeyListener(enterKeyListener);
        contactList.addKeyListener(enterKeyListener);
        groupList.addKeyListener(enterKeyListener);
        btnAdd.addKeyListener(enterKeyListener);
        btnRemove.addKeyListener(enterKeyListener);
        btnSeleccionarImagen.addKeyListener(enterKeyListener);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        gbc.gridy = 4;
        panel.add(panelBotones, gbc);
        add(panel);
    }

    private void moverSeleccionados(JList<String> origen, DefaultListModel<String> modeloOrigen, DefaultListModel<String> modeloDestino) {
        List<String> seleccionados = origen.getSelectedValuesList();
        seleccionados.stream()
        	.forEach(s -> {
            modeloDestino.addElement(s);
            modeloOrigen.removeElement(s);
        });

    }
}