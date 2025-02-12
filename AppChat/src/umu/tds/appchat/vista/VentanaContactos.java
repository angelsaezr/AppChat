package umu.tds.appchat.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaContactos extends JPanel {
    public VentanaContactos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Panel Izquierdo - Lista de Contactos
        JPanel panelListaContactos = new JPanel(new BorderLayout());
        panelListaContactos.setBackground(Color.WHITE);
        panelListaContactos.setBorder(BorderFactory.createTitledBorder("Lista de Contactos"));
        
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        //modeloLista.addElement("contacto1");
        //modeloLista.addElement("grupo2");
        
        JList<String> listaContactos = new JList<>(modeloLista);
        panelListaContactos.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        
        JButton btnAgregarContacto = new JButton("Añadir Contacto");
        btnAgregarContacto.setBackground(new Color(0, 128, 128));
        btnAgregarContacto.setForeground(Color.WHITE);
        panelListaContactos.add(btnAgregarContacto, BorderLayout.SOUTH);
        
        // Panel Derecho - Grupo Seleccionado
        JPanel panelGrupo = new JPanel(new BorderLayout());
        panelGrupo.setBackground(Color.WHITE);
        panelGrupo.setBorder(BorderFactory.createTitledBorder("Grupo"));
        
        DefaultListModel<String> modeloGrupo = new DefaultListModel<>();
        //modeloGrupo.addElement("contacto4");
        //modeloGrupo.addElement("contacto5");
        
        JList<String> listaGrupo = new JList<>(modeloGrupo);
        panelGrupo.add(new JScrollPane(listaGrupo), BorderLayout.CENTER);
        
        JButton btnAgregarGrupo = new JButton("Añadir Grupo");
        btnAgregarGrupo.setBackground(new Color(0, 128, 128));
        btnAgregarGrupo.setForeground(Color.WHITE);
        panelGrupo.add(btnAgregarGrupo, BorderLayout.SOUTH);
        
        // Agregar Paneles al Panel Principal
        add(panelListaContactos, BorderLayout.WEST);
        add(panelGrupo, BorderLayout.EAST);
    }
}
