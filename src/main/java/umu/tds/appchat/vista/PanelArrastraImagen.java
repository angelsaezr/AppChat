package umu.tds.appchat.vista;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import java.awt.Font;
import java.awt.Color;

/**
 * Diálogo personalizado para seleccionar o arrastrar una imagen desde el sistema de archivos.
 * Permite previsualizar la imagen cargada y confirmar o cancelar la acción.
 * Está pensado para usarse en contextos como la personalización de perfiles o grupos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class PanelArrastraImagen extends JDialog {
	
    /**
     * Panel principal del contenido del diálogo.
     */
    private JPanel contentPane = new JPanel();

    /**
     * Lista de archivos que el usuario ha subido o seleccionado.
     */
    private List<File> archivosSubidos = new ArrayList<>();

    /**
     * Etiqueta que muestra la ruta del archivo subido o seleccionado.
     */
    private JLabel lblArchivoSubido;

    /**
     * Botón para aceptar la imagen seleccionada.
     */
    private JButton btnAceptar;

    /**
     * Botón para cancelar la acción y cerrar el diálogo.
     */
    private JButton btnCancelar;

    /**
     * Crea e inicializa el diálogo de arrastre de imagen, asociado a una ventana principal.
     *
     * @param owner la ventana principal sobre la cual se mostrará el diálogo
     */
    public PanelArrastraImagen(JFrame owner) {
        super(owner, "Agregar fotos", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        this.setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPane, BorderLayout.CENTER);
        contentPane.setLayout(new BorderLayout());
        
        JEditorPane editorPane = new JEditorPane();
        editorPane.setBackground(Color.WHITE);
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");  
        editorPane.setText("<html><body style='font-family:Segoe UI; text-align:center;'>"
                + "<h2 style='color:#008080;'>Agregar Foto</h2>"
                + "<p>Puedes arrastrar el fichero aquí</p></body></html>");
        contentPane.add(editorPane, BorderLayout.NORTH);
        
        JLabel imagenLabel = new JLabel();
        imagenLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(imagenLabel, BorderLayout.CENTER);
        
        editorPane.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>) evt.getTransferable().
                            getTransferData(DataFlavor.javaFileListFlavor);
                    
                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        archivosSubidos.add(file);
                        
                        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        imagenLabel.setIcon(new ImageIcon(img));
                        
                        lblArchivoSubido.setText(file.getAbsolutePath());
                        lblArchivoSubido.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        lblArchivoSubido = new JLabel();
        lblArchivoSubido.setForeground(new Color(80, 80, 80));
        lblArchivoSubido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblArchivoSubido.setVisible(false);
        contentPane.add(lblArchivoSubido, BorderLayout.SOUTH);
        
        JButton botonElegir = new JButton("Seleccionar de tu ordenador");
        botonElegir.setForeground(Color.WHITE);
        botonElegir.setBackground(new Color(0, 128, 128));
        botonElegir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonElegir.setFocusPainted(false);
        botonElegir.setBorderPainted(false);
        botonElegir.addActionListener(ev -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                archivosSubidos.add(file);
                
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imagenLabel.setIcon(new ImageIcon(img));
                
                lblArchivoSubido.setText(file.getAbsolutePath());
                lblArchivoSubido.setVisible(true);
            }
        });
        contentPane.add(botonElegir, BorderLayout.SOUTH);
        
        // Panel de botones Aceptar y Cancelar
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(240, 248, 255));
        
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 128, 128));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);
        btnAceptar.addActionListener(ev -> dispose());
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(ev -> {
            archivosSubidos.clear();
            dispose();
        });
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
        setLocationRelativeTo(owner);
    }
    
    /**
     * Muestra el diálogo y devuelve la lista de archivos que el usuario ha cargado o seleccionado.
     *
     * @return lista de archivos seleccionados por el usuario
     */
    public List<File> showDialog() {
        this.setVisible(true);
        return archivosSubidos;
    }
}