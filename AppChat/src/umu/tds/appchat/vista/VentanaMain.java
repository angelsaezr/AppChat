package umu.tds.appchat.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class VentanaMain extends JFrame {
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaMain frame = new VentanaMain();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public VentanaMain() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menuFile.add(exitItem);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(200, 100, 80, 25);
        contentPane.add(lblUsername);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(290, 100, 150, 25);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(200, 140, 80, 25);
        contentPane.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(290, 140, 150, 25);
        contentPane.add(txtPassword);
        
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(200, 180, 110, 30);
        contentPane.add(btnLogin);
        
        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new VentanaSignUp();
                dispose();
            }
        });
        btnSignUp.setBounds(330, 180, 110, 30);
        contentPane.add(btnSignUp);
    }
}
