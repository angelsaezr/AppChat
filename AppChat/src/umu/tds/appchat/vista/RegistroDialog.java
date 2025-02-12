package umu.tds.appchat.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class RegistroDialog extends JDialog {
    private JPanel contentPane, panelSeleccionarImagen, panelBotones;
    private JTextField txtUsuario, txtMovil, txtSaludo;
    private JPasswordField txtPassword, txtRepitePassword;
    private JDateChooser dateChooser;
    private JButton btnRegistrar, btnCancelar, btnSeleccionarImagen;

    public RegistroDialog(JFrame parent) {
        super(parent, "Registro de Usuario", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        
        // Configuración del panel principal
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(240, 248, 255));
        setContentPane(contentPane);

        // Campos de entrada
        txtUsuario = createTextField("Usuario:");
        txtPassword = createPasswordField("Contraseña:");
        txtRepitePassword = createPasswordField("Repite Contraseña:");
        txtMovil = createTextField("Móvil:");
        
        JLabel lblFechaNacimiento = new JLabel("Fecha de Nacimiento:        ");
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
        
        txtSaludo = createTextField("Saludo Inicial:");
        
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
        if (txtUsuario.getText().isEmpty() || txtMovil.getText().isEmpty() || dateChooser.getDate() == null || txtSaludo.getText().isEmpty() || 
            new String(txtPassword.getPassword()).isEmpty() || new String(txtRepitePassword.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Registro exitoso.", "Información", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}