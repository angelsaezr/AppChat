package umu.tds.vista;

import java.awt.*;
import javax.swing.*;

public class VentanaSignUp extends JFrame{

    public VentanaSignUp() {
    	setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    	
    	JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menuFile.add(exitItem);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);
    	
    	JPanel panel = new JPanel();
    	getContentPane().add(panel, BorderLayout.CENTER);
    	
    	JButton btnNewButton = new JButton("New button");
    	panel.add(btnNewButton);
    	
    }
}
