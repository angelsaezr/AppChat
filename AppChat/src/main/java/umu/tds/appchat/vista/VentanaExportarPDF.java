package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.utils.ExportPDF;

/**
 * Diálogo para exportar una conversación a un archivo PDF.
 * Permite al usuario seleccionar un contacto y guardar los mensajes de la conversación en un archivo PDF.
 * 
 * Valida que el usuario seleccione un contacto y elija una ubicación para guardar el archivo.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaExportarPDF extends JDialog {
    /**
     * Desplegable para seleccionar el contacto cuya conversación se desea exportar.
     */
    private JComboBox<Contacto> comboContactos;

    /**
     * Botón para iniciar la exportación de la conversación seleccionada a un archivo PDF.
     */
    private JButton btnGuardar;

    /**
     * Botón para cancelar la operación y cerrar el diálogo.
     */
    private JButton btnCancelar;

    /**
     * Crea e inicializa el diálogo para exportar los mensajes de un contacto a un archivo PDF.
     * Permite al usuario seleccionar un contacto y elegir una ubicación para guardar el archivo.
     * 
     * @param parent la ventana principal que invoca este diálogo
     */
    public VentanaExportarPDF(JFrame parent) {
        super(parent, "Exportar Conversación a PDF", true);
        setSize(450, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel de selección de contacto
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelSeleccion.add(new JLabel("Seleccionar un contacto para exportar mensajes:"));
        
        Usuario usuarioActual = AppChat.getInstance().getUsuarioActual();
        List<Contacto> contactos = usuarioActual.getContactos();
        List<Contacto> contactosAgregados = new ArrayList<>();

        contactos.stream()
        	.filter(c -> !(c instanceof ContactoIndividual) || !c.getNombre().startsWith("$"))
        	.forEach(contactosAgregados::add);

        comboContactos = new JComboBox<Contacto>(contactosAgregados.toArray(new Contacto[0]));
        comboContactos.setPreferredSize(new Dimension(250, 30));
        comboContactos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Contacto) {
                    Contacto c = (Contacto) value;
                    String texto = c.getNombreContacto();
                    if (c instanceof ContactoIndividual) {
                        texto += " (" + ((ContactoIndividual) c).getMovil() + ")";
                    }
                    setText(texto);
                }
                return this;
            }
        });
        
        panelSeleccion.add(comboContactos);

        add(panelSeleccion, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0)); // más separación del borde inferior

        btnGuardar = new JButton("Exportar");
        btnGuardar.setBackground(new Color(0, 128, 128));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);


        // Acción al hacer clic en "Exportar"
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Contacto contacto = (Contacto) comboContactos.getSelectedItem();
                if (contacto == null) {
                    JOptionPane.showMessageDialog(VentanaExportarPDF.this, "Debes seleccionar un contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar como");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf"));
                int userSelection = fileChooser.showSaveDialog(VentanaExportarPDF.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String ruta = fileToSave.getAbsolutePath();

                    if (!ruta.toLowerCase().endsWith(".pdf")) {
                        ruta += ".pdf";
                    }

                    try {
                        List<Mensaje> mensajes = contacto.getMensajes();
                        ExportPDF.crearPDF(contacto, mensajes, ruta);
                        
                        JOptionPane.showMessageDialog(
                            VentanaExportarPDF.this,
                            "Conversación exportada correctamente.\n\nArchivo guardado en:\n" + ruta,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                            VentanaExportarPDF.this,
                            "Error al exportar el PDF: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        ex.printStackTrace();
                    }

                }
            }
        });

        // Acción al hacer clic en "Cancelar"
        btnCancelar.addActionListener(e -> dispose());
        
        // Agrega KeyListener para detectar la tecla Enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnGuardar.doClick(); // Simula el clic en el botón
                }
            }
        };
        
        // Asigna el KeyListener a los campos de entrada
        comboContactos.addKeyListener(enterKeyListener);
    }
}