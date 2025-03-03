package umu.tds.appchat.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import umu.tds.appchat.controlador.Controlador;

/**
 * Ventana de inicio de sesion.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class VentanaLogin extends JFrame {
    private JPanel contentPane;
    private JTextField txtPhone;
    private JPasswordField txtPassword;

    public VentanaLogin() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setLocationRelativeTo(null);
        this.setResizable(false);
        
        // Aplicar FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Cambiar el icono de la ventana
        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/resources/logo_icono.png");
        setIconImage(icon);

        // Configuración del panel principal
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(240, 248, 255));
        setContentPane(contentPane);

        // Agregar el logo en la parte superior
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/main/resources/logo_login.png"); // Ruta del logo
        logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(logoLabel);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        // Título
        JLabel titleLabel = new JLabel("Bienvenido a AppChat");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 128, 128)); // Color similar al logo
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(titleLabel);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        // Campo de texto para el teléfono
        txtPhone = new JTextField(15);
        txtPhone.setPreferredSize(new Dimension(300, 40));
        txtPhone.setMaximumSize(new Dimension(300, 40));
        txtPhone.setMinimumSize(new Dimension(300, 40));
        txtPhone.setBorder(BorderFactory.createTitledBorder("Número de teléfono"));
        txtPhone.setBackground(Color.WHITE);
        txtPhone.setForeground(Color.DARK_GRAY);
        contentPane.add(txtPhone);

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        // Campo de texto para la contraseña
        txtPassword = new JPasswordField(15);
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setMaximumSize(new Dimension(300, 40));
        txtPassword.setMinimumSize(new Dimension(300, 40));
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(Color.DARK_GRAY);
        contentPane.add(txtPassword);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón de iniciar sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(0, 128, 128)); // Color similar al logo
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener((ActionEvent e) -> {
            if (txtPhone.getText().trim().isBlank() || new String(txtPassword.getPassword()).trim().isBlank()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean exito = Controlador.INSTANCE.login(txtPhone.getText().trim(), new String(txtPassword.getPassword()).trim());
                if (exito) {
                	JOptionPane.showMessageDialog(this, "Sesión iniciada", "Información", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new VentanaMain().setVisible(true); // Abre VentanaMain
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(loginButton);
        
        // Agrega KeyListener para detectar la tecla Enter en txtPhone y txtPassword
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick(); // Simula el clic en el botón de inicio de sesión
                }
            }
        };
        
        // Asigna el KeyListener a los campos de entrada
        txtPhone.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        // Enlace de registro
        JLabel registerLabel = new JLabel("¿No tienes cuenta? Regístrate");
        registerLabel.setForeground(Color.DARK_GRAY);
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linkLabel = new JLabel("aquí");
        linkLabel.setForeground(new Color(255, 69, 0)); // Color similar al logo
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.setFont(linkLabel.getFont().deriveFont(Font.BOLD));
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VentanaRegistro(VentanaLogin.this).setVisible(true);
            }
        });

        JPanel linkPanel = new JPanel();
        linkPanel.setBackground(new Color(240, 248, 255)); // Mismo fondo que la ventana
        linkPanel.add(registerLabel);
        linkPanel.add(linkLabel);
        contentPane.add(linkPanel);
    }
}
