package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.dominio.Usuario;

/**
 * Diálogo para editar los miembros de un grupo existente.
 * Permite agregar o quitar contactos de un grupo y actualiza la lista de miembros.
 * También valida que el grupo no quede vacío antes de proceder con la modificación.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaEditarMiembrosGrupo extends JDialog {
    /**
     * Lista de contactos disponibles para agregar al grupo.
     */
    private JList<String> contactList;

    /**
     * Lista de miembros actuales del grupo.
     */
    private JList<String> groupList;

    /**
     * Modelo de datos para la lista de contactos disponibles.
     */
    private DefaultListModel<String> contactListModel;

    /**
     * Modelo de datos para la lista de miembros del grupo.
     */
    private DefaultListModel<String> groupListModel;

    /**
     * Botón para confirmar los cambios realizados en los miembros del grupo.
     */
    private JButton btnAceptar;

    /**
     * Botón para cancelar los cambios y cerrar el diálogo.
     */
    private JButton btnCancelar;

    /**
     * Botón para agregar contactos seleccionados al grupo.
     */
    private JButton btnAdd;

    /**
     * Botón para quitar miembros seleccionados del grupo.
     */
    private JButton btnRemove;

    /**
     * Crea e inicializa el diálogo para editar los miembros de un grupo.
     * Permite al usuario mover contactos entre las listas de contactos disponibles y miembros del grupo.
     * 
     * @param ventanaMain la ventana principal que invoca este diálogo
     * @param grupo el grupo cuyas membresías serán editadas
     */
    public VentanaEditarMiembrosGrupo(VentanaMain ventanaMain, Grupo grupo) {
        super(ventanaMain, "Editar Miembros", true);
        setSize(500, 300);
        setLocationRelativeTo(ventanaMain);
        setLayout(new GridBagLayout());
        this.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Listas
        contactListModel = new DefaultListModel<>();
        groupListModel = new DefaultListModel<>();
        
        Usuario usuarioActual = AppChat.getInstance().getUsuarioActual();
        List<Contacto> listaContactos = usuarioActual.getContactos();
        listaContactos.stream()
        	.filter(c -> c instanceof ContactoIndividual)
        	.map(c -> (ContactoIndividual) c)
        	.forEach(c -> {
        		String contactoInfo = c.getNombre() + " (" + c.getMovil() + ")";
        		if (grupo.getMiembros().contains(c)) {
        			groupListModel.addElement(contactoInfo);
        		} else if (!c.getNombre().startsWith("$")) {
        			contactListModel.addElement(contactoInfo);
        		}
        	});


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

        JPanel panelListas = new JPanel(new GridBagLayout());
        panelListas.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelListas.add(new JLabel("Contactos disponibles:"), gbc);
        gbc.gridx = 2;
        panelListas.add(new JLabel("Miembros del grupo:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
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

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(panelListas, gbc);

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 128, 128));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setBorderPainted(false);
        btnAceptar.setFocusPainted(false);

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(groupListModel.isEmpty()) {
                	JOptionPane.showMessageDialog(VentanaEditarMiembrosGrupo.this, "El grupo no puede quedar vacío, ¿quizás quieres eliminarlo?", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                
            	List<ContactoIndividual> nuevosMiembros = new LinkedList<>();
                for (int i = 0; i < groupListModel.size(); i++) {
                    String contactoInfo = groupListModel.get(i);
                    String movil = contactoInfo.substring(contactoInfo.indexOf("(") + 1, contactoInfo.indexOf(")"));
                    // Buscar en los miembros originales del grupo por móvil
                    for (Contacto c : AppChat.getInstance().getUsuarioActual().getContactos()) {
                    	if (c instanceof ContactoIndividual) {
                    		ContactoIndividual cI = (ContactoIndividual) c;
                    		if (cI.getMovil().equals(movil)) {
                                nuevosMiembros.add(cI);
                            }
                    	}
                    }
                }
            	
                if (AppChat.getInstance().actualizarMiembrosGrupo(grupo, nuevosMiembros)) {
                    JOptionPane.showMessageDialog(VentanaEditarMiembrosGrupo.this, "Grupo actualizado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    ventanaMain.actualizarListaContactos();
                    ventanaMain.setContactoSeleccionado(grupo);
                    dispose();
                } else {
                	JOptionPane.showMessageDialog(VentanaEditarMiembrosGrupo.this, "Error al editar los miembros del grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                	dispose();
                }
            }
        });

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        gbc.gridy = 2;
        panel.add(panelBotones, gbc);
        add(panel);
        
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
        contactList.addKeyListener(enterKeyListener);
        groupList.addKeyListener(enterKeyListener);
        btnAdd.addKeyListener(enterKeyListener);
        btnRemove.addKeyListener(enterKeyListener);
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