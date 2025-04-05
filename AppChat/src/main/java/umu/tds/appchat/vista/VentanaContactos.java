package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import umu.tds.appchat.controlador.AppChat;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Diálogo para mostrar y seleccionar entre los contactos individuales y los grupos del usuario.
 * Permite agregar contactos o grupos mediante tablas interactivas.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaContactos extends JDialog {
	private JTable tableContactos;
	private JTable tableGrupos;
	private ContactTableModel tableModelContactos;
	private GroupTableModel tableModelGrupos;
	private JButton btnAceptar;

	 /**
     * Crea e inicializa el diálogo para mostrar los contactos y grupos del usuario.
     * 
     * @param ventanaMain la ventana principal que invoca este diálogo
     */
	public VentanaContactos(VentanaMain ventanaMain) {
		super(ventanaMain, "Contactos", true);
		setSize(600, 600);
		setLocationRelativeTo(ventanaMain);
		setLayout(new BorderLayout());
		this.setResizable(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(245, 245, 245));

		// Tablas para contactos individuales y grupos
		tableModelContactos = new ContactTableModel();
		tableContactos = new JTable(tableModelContactos);
		JScrollPane scrollContactos = new JScrollPane(tableContactos);
		
		// Agrega evento de clic a la tabla de contactos individuales
		tableContactos.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = tableContactos.getSelectedRow();
		        if (row != -1) {
		            String nombre = (String) tableModelContactos.getValueAt(row, 0);
		            
		            AppChat.getInstance().getContactosUsuarioActual().stream()
		                .filter(contacto -> contacto.getNombre().equals(nombre))
		                .findFirst()
		                .ifPresent(contacto -> {
		                    ventanaMain.setContactoSeleccionado(contacto);
		                    dispose();
		                });
		        }
		    }
		});

		tableModelGrupos = new GroupTableModel();
		tableGrupos = new JTable(tableModelGrupos);
		JScrollPane scrollGrupos = new JScrollPane(tableGrupos);
		
		// Agrega evento de clic a la tabla de grupos
		tableGrupos.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = tableGrupos.getSelectedRow();
		        if (row != -1) {
		            String nombre = (String) tableModelGrupos.getValueAt(row, 0);
		            
		            AppChat.getInstance().getContactosUsuarioActual().stream()
		                .filter(contacto -> contacto.getNombre().equals(nombre))
		                .findFirst()
		                .ifPresent(contacto -> {
		                    ventanaMain.setContactoSeleccionado(contacto);
		                    dispose();
		                });
		        }
		    }
		});

		// Panel para las tablas
		JPanel panelTablas = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        gbc.gridx = 0;
		panelTablas.add(new JLabel("Contactos Individuales:"), gbc);
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
		panelTablas.add(scrollContactos, gbc);
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
		panelTablas.add(new JLabel("Grupos:"), gbc);
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
		panelTablas.add(scrollGrupos, gbc);

		panel.add(panelTablas, BorderLayout.CENTER);

		// Panel de botones
		JPanel panelBotones = new JPanel();
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBackground(new Color(0, 128, 128));
		btnAceptar.setForeground(Color.WHITE);
		btnAceptar.setFocusPainted(false);
		btnAceptar.setBorderPainted(false);

		panelBotones.add(btnAceptar);
		panel.add(panelBotones, BorderLayout.SOUTH);

		add(panel);

		btnAceptar.addActionListener(e -> dispose());
		
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
        tableContactos.addKeyListener(enterKeyListener);
        tableGrupos.addKeyListener(enterKeyListener);
	}

	/**
     * Clase interna para gestionar el modelo de tabla de los contactos individuales.
     */
	private static class ContactTableModel extends AbstractTableModel {
		private final String[] nombresColumna = { "Nombre", "Teléfono", "Saludo" };
		private final List<Object[]> data;

		public ContactTableModel() {
			data = new ArrayList<>();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return nombresColumna.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex)[columnIndex];
		}

		@Override
		public String getColumnName(int column) {
			return nombresColumna[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false; // La tabla no será editable
		}

		public boolean add(String nombre, String movil, String saludo) {
			return data.add(new Object[] { nombre, movil, saludo });
		}
	}

	/**
     * Clase interna para gestionar el modelo de tabla de los grupos.
     */
	private static class GroupTableModel extends AbstractTableModel {
		private final String[] nombresColumna = { "Nombre", "Miembros" };
		private final List<Object[]> data;

		public GroupTableModel() {
			data = new ArrayList<>();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return nombresColumna.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex)[columnIndex];
		}

		@Override
		public String getColumnName(int column) {
			return nombresColumna[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public boolean add(String nombre, String miembros) {
			return data.add(new Object[] { nombre, miembros });
		}
	}

	/**
     * Añade un contacto individual a la tabla de contactos.
     *
     * @param nombre el nombre del contacto
     * @param movil el número de teléfono del contacto
     * @param saludo el saludo del contacto
     * @return true si el contacto fue añadido correctamente
     */
	public boolean addContactoIndividual(String nombre, String movil, String saludo) {
		return tableModelContactos.add(nombre, movil, saludo);
	}

	/**
     * Añade un grupo a la tabla de grupos.
     *
     * @param nombre el nombre del grupo
     * @param miembros los miembros del grupo
     * @return true si el grupo fue añadido correctamente
     */
	public boolean addGrupo(String nombre, String miembros) {
		return tableModelGrupos.add(nombre, miembros);
	}
}