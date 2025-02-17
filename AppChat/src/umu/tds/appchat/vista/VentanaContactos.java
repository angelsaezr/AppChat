package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class VentanaContactos extends JDialog {
    private JTable table;
    private ContactTableModel tableModel;

    public VentanaContactos(Frame parent) {
        super(parent, "Lista contactos", true);
        setLayout(new BorderLayout());

        // Crear el modelo de la tabla
        tableModel = new ContactTableModel();
        table = new JTable(tableModel);

        // Agregar la tabla con scroll
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");

        // Acciones de los botones
        okButton.addActionListener(e -> dispose());

        // Agregar botones al panel
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    // Clase interna para el modelo de la tabla
    // TODO Comprobar
    private static class ContactTableModel extends AbstractTableModel {
        private final String[] nombresColumna = {"Nombre", "Teléfono", "Saludo"};
        private final List<Object[]> data;

        public ContactTableModel() {
            data = new ArrayList<>();
            data.add(new Object[]{"Angel", "660392750", "En el gimnasio"});
            data.add(new Object[]{"Javi", "696305617", "Durmiendo"});
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
    }
}


