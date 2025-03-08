package umu.tds.appchat.vista;

import javax.swing.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import umu.tds.appchat.controlador.Controlador;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.TipoMensaje;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class VentanaBuscarMensaje extends JDialog {
    private JTextField textFieldTexto, textFieldTelefono, textFieldContacto;
    private JButton btnBuscar;
    private JPanel panelResultados;

    public VentanaBuscarMensaje(JFrame parent) {
        super(parent, "Buscar Mensajes", true);
        setSize(600, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        textFieldTexto = new JTextField(15);
        textFieldTelefono = new JTextField(15);
        textFieldContacto = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBuscar.setBackground(new Color(0, 128, 128));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        
        gbc.gridy = 0;
        panelBusqueda.add(new JLabel("Texto"), gbc);
        gbc.gridx = 1;
        panelBusqueda.add(textFieldTexto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelBusqueda.add(new JLabel("Teléfono"), gbc);
        gbc.gridx = 1;
        panelBusqueda.add(textFieldTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelBusqueda.add(new JLabel("Contacto"), gbc);
        gbc.gridx = 1;
        panelBusqueda.add(textFieldContacto, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panelBusqueda.add(btnBuscar, gbc);

        add(panelBusqueda, BorderLayout.NORTH);

        // Panel de resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelResultados);
        add(scrollPane, BorderLayout.CENTER);

        // Acción del botón buscar (simulación de mensajes)
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarResultados(textFieldTexto.getText(), textFieldTelefono.getText(), textFieldContacto.getText());
            }
        });
    }

    private void mostrarResultados(String texto, String movil, String contacto) {
        panelResultados.removeAll();

        List<JPanel> resultados = Controlador.INSTANCE.getContactosUsuarioActual().stream()
            .filter(c -> esContactoRelevante(c, movil, contacto)) // Filtra contactos relevantes
            .flatMap(c -> Controlador.INSTANCE.getMensajes(c).stream()
                .filter(m -> texto.isBlank() || m.getTexto().toLowerCase().contains(texto.toLowerCase())) // Búsqueda flexible
                .filter(m -> !m.getTexto().isBlank()) // Evita mensajes vacíos
                .map(m -> Map.entry(m, c)) // Guarda mensaje y contacto asociado
            )
            .sorted(Comparator.comparing((Map.Entry<Mensaje, Contacto> entry) -> entry.getKey().getFechaHoraEnvio())
                .reversed()) // Ordena de más reciente a más antiguo
            .map(entry -> crearPanelMensaje(
                entry.getKey().getTipo() == TipoMensaje.ENVIADO 
                    ? Controlador.INSTANCE.getUsuarioActual().getNombre() 
                    : getNombreContacto(entry.getValue()),
                entry.getKey().getTipo() == TipoMensaje.ENVIADO 
                    ? getNombreContacto(entry.getValue())
                    : Controlador.INSTANCE.getUsuarioActual().getNombre(),
                entry.getKey().getTexto()
            ))
            .toList();

        resultados.forEach(panelResultados::add);

        panelResultados.revalidate();
        panelResultados.repaint();
    }




    // Método auxiliar para verificar si un contacto es relevante según móvil o nombre
    private boolean esContactoRelevante(Contacto c, String movil, String nombre) {
        if (!movil.isBlank()) {
            if (c instanceof ContactoIndividual) {
                return ((ContactoIndividual) c).getMovil().equals(movil);
            } else if (c instanceof Grupo) {
                return ((Grupo) c).getMiembros().stream()
                    .anyMatch(miembro -> miembro.getMovil().equals(movil));
            }
        }
        if (!nombre.isBlank()) {
            if (c.getNombre().equals(nombre)) return true;
            if (c instanceof Grupo) {
                return ((Grupo) c).getMiembros().stream()
                    .anyMatch(miembro -> miembro.getNombre().equals(nombre));
            }
            return false;
        }
        return true; // Si no hay filtros, el contacto es relevante
    }

    // Método auxiliar para obtener el nombre del contacto
    private String getNombreContacto(Contacto c) {
        return c.getNombre().isBlank() && c instanceof ContactoIndividual 
            ? ((ContactoIndividual) c).getMovil() 
            : c.getNombre();
    }


    private JPanel crearPanelMensaje(String emisor, String receptor, String mensaje) {
        JPanel panelMensaje = new JPanel(new BorderLayout());
        panelMensaje.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        panelMensaje.setPreferredSize(new Dimension(500, 80));
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(240, 248, 255));
        panelSuperior.add(new JLabel("  Emisor: " + emisor, JLabel.LEFT), BorderLayout.WEST);
        panelSuperior.add(new JLabel("Receptor: " + receptor + "  ", JLabel.RIGHT), BorderLayout.EAST);
        
        JTextArea textAreaMensaje = new JTextArea(mensaje);
        textAreaMensaje.setEditable(false);
        textAreaMensaje.setWrapStyleWord(true);
        textAreaMensaje.setLineWrap(true);
        textAreaMensaje.setBackground(Color.WHITE);
        
        panelMensaje.add(panelSuperior, BorderLayout.NORTH);
        panelMensaje.add(textAreaMensaje, BorderLayout.CENTER);
        
        return panelMensaje;
    }
}
