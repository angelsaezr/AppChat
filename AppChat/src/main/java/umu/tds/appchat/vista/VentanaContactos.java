package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana con la tabla de contactos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaContactos extends JDialog {
    private JTable table;
    private ContactTableModel tableModel;
    private JButton btnAceptar;

	public VentanaContactos(Frame parent) {
        super(parent, "Contactos", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        this.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Crear el modelo de la tabla
        tableModel = new ContactTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 128, 128));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);

        panelBotones.add(btnAceptar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);

        btnAceptar.addActionListener(e -> dispose());
        
        // Agrega KeyListener a la ventana y la tabla
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAceptar.doClick(); // Simula el clic en el botón
                    e.consume(); // Evita que el evento se propague
                }
            }
        };

        // Agrega el KeyListener a la ventana
        this.addKeyListener(enterKeyListener);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        // Pone el KeyListener a la tabla
        table.addKeyListener(enterKeyListener);

    }

    // Clase interna para el modelo de la tabla
    private static class ContactTableModel extends AbstractTableModel {
        private final String[] nombresColumna = {"Nombre", "Teléfono", "Saludo"};
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
        	return data.add(new Object[] {nombre, movil, saludo});
        }
    }
    
    public boolean addContactoIndividual(String nombre, String movil, String saludo) {
    	return tableModel.add(nombre, movil, saludo);
    }
}
