package umu.tds.appchat.vista;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.utils.ExportPDF;

@SuppressWarnings("serial")
public class VentanaExportarPDF extends JDialog {
    private JComboBox<Contacto> comboContactos;
    private JButton btnGuardar, btnCancelar;

    public VentanaExportarPDF(JFrame parent) {
        super(parent, "Exportar Conversación a PDF", true);
        setSize(450, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel de selección de contacto
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelSeleccion.add(new JLabel("Seleccionar un contacto para exportar mensajes:"));

        List<Contacto> contactos = AppChat.getInstance().getContactosUsuarioActual();
        comboContactos = new JComboBox<>(contactos.toArray(new Contacto[0]));
        comboContactos.setPreferredSize(new Dimension(250, 30));
        panelSeleccion.add(comboContactos);

        add(panelSeleccion, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Exportar");
        btnCancelar = new JButton("Cancelar");

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
                        List<Mensaje> mensajes = AppChat.getInstance().getMensajesDelContacto(contacto);
                        ExportPDF.crearPDF(contacto.getNombre(), mensajes, ruta);
                        
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
    }
}
