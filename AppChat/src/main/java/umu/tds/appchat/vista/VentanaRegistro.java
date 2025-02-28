package umu.tds.appchat.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import umu.tds.appchat.controlador.Controlador;

/**
 * Ventana para registrarse.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaRegistro extends JDialog {
    private JPanel contentPane, panelSeleccionarImagen, panelBotones, panelImagen;
    private JTextField txtUsuario, txtEmail, txtMovil;
    private JTextArea txtSaludo;
    private JPasswordField txtPassword, txtRepitePassword;
    private JDateChooser dateChooser;
    private JButton btnRegistrar, btnCancelar, btnSeleccionarImagen;
    private JLabel lblImagenSeleccionada;
    private File imagenSeleccionada;

	public VentanaRegistro(JFrame parent) {
        super(parent, "Registro de Usuario", true);
        setSize(400, 650);
        setLocationRelativeTo(parent);
        this.setResizable(false);
        
        // Configuración del panel principal
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(240, 248, 255));
        setContentPane(contentPane);

        // Campos de entrada
        txtUsuario = createTextField("Usuario *");
        txtPassword = createPasswordField("Contraseña *");
        txtRepitePassword = createPasswordField("Repite la contraseña *");
        txtEmail = createTextField("Email *");
        txtMovil = createTextField("Móvil *");
        
        JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento            ");
        lblFechaNacimiento.setAlignmentX(Component.RIGHT_ALIGNMENT);
        contentPane.add(lblFechaNacimiento);
        
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(300, 35));
        dateChooser.setMaximumSize(new Dimension(300, 35));
        dateChooser.setMinimumSize(new Dimension(300, 35));
        dateChooser.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        dateChooser.setBackground(Color.WHITE);
        dateChooser.getCalendarButton().setBackground(new Color(0, 128, 128));
        dateChooser.getCalendarButton().setForeground(Color.WHITE);
        dateChooser.setFocusable(false);
        dateChooser.setDateFormatString("dd MMM yyyy");
        dateChooser.getJCalendar().setPreferredSize(new Dimension(300, 200));
        
        // Deshabilitar edición manual
        JTextField dateEditor = ((JTextField) dateChooser.getDateEditor().getUiComponent());
        dateEditor.setEditable(false);
        dateEditor.setBackground(Color.WHITE);
        
        contentPane.add(dateChooser);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        
        txtSaludo = new JTextArea("Hi there...", 3, 15);
        txtSaludo.setPreferredSize(new Dimension(300, 70));
        txtSaludo.setMaximumSize(new Dimension(300, 70));
        txtSaludo.setMinimumSize(new Dimension(300, 70));
        txtSaludo.setBorder(BorderFactory.createTitledBorder("Saludo inicial"));
        txtSaludo.setBackground(Color.WHITE);
        txtSaludo.setForeground(Color.DARK_GRAY);
        txtSaludo.setWrapStyleWord(true);
        txtSaludo.setLineWrap(true);
        contentPane.add(txtSaludo);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        
        panelSeleccionarImagen = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelSeleccionarImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSeleccionarImagen.setBackground(new Color(240, 248, 255));
        
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setPreferredSize(new Dimension(300, 25));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarImagen.setBackground(new Color(0, 128, 128));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setBorderPainted(false);
        btnSeleccionarImagen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	List<File> archivos = new PanelArrastraImagen(parent).showDialog();
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
        panelImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelImagen.setBackground(new Color(240, 248, 255));
        panelImagen.add(lblImagenSeleccionada);
        contentPane.add(panelImagen);
        
        panelSeleccionarImagen.add(btnSeleccionarImagen);
        contentPane.add(panelSeleccionarImagen);

        // Botones
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panelBotones.setBackground(new Color(240, 248, 255));
        
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setBackground(new Color(0, 128, 128));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.addActionListener(this::validarCampos);
        panelBotones.add(btnRegistrar);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setBorderPainted(false);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(Box.createRigidArea(new Dimension(10, 0)));
        panelBotones.add(btnCancelar);
        
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(panelBotones);
    }

    private JTextField createTextField(String title) {
        JTextField textField = new JTextField(15);
        textField.setPreferredSize(new Dimension(300, 40));
        textField.setMaximumSize(new Dimension(300, 40));
        textField.setMinimumSize(new Dimension(300, 40));
        textField.setBorder(BorderFactory.createTitledBorder(title));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.DARK_GRAY);
        contentPane.add(textField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        return textField;
    }

    private JPasswordField createPasswordField(String title) {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setMinimumSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createTitledBorder(title));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.DARK_GRAY);
        contentPane.add(passwordField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        return passwordField;
    }
    
    private void validarCampos(ActionEvent e) {
        if (txtUsuario.getText().isEmpty() || txtEmail.getText().isEmpty() || txtMovil.getText().isEmpty() || new String(txtPassword.getPassword()).isEmpty() || new String(txtRepitePassword.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellena todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (new String(txtPassword.getPassword()).equals(new String(txtRepitePassword.getPassword()))) {
        	JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Controlador.INSTANCE.registrarUsuario(txtUsuario.getText(), txtMovil.getText(), new String(txtPassword.getPassword()), dateChooser.getDate(), imagenSeleccionada, txtSaludo.getText(), txtEmail.getText());
        JOptionPane.showMessageDialog(this, "Registro exitoso.", "Información", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}