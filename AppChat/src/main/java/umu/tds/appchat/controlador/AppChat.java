package umu.tds.appchat.controlador;

import java.time.LocalDate;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.RepositorioUsuarios;

public class AppChat {

    public static final AppChat INSTANCE = new AppChat();
    
    private RepositorioUsuarios repositorioUsuarios;
    private Usuario usuarioActual;

    private AppChat() {
        this.repositorioUsuarios = RepositorioUsuarios.INSTANCE;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    // Registrar un nuevo usuario
    public boolean registrarUsuario(String nombre, String movil, String contraseña, LocalDate fechaNacimiento, String imagen, String saludo) {
        if (repositorioUsuarios.getUsuario(movil) != null) {
            return false; // Ya existe un usuario con ese número
        }

        Usuario nuevoUsuario = new Usuario(nombre, movil, contraseña, imagen, saludo);
        return repositorioUsuarios.addUsuario(nuevoUsuario);
    }

    // Iniciar sesión con móvil y contraseña
    public boolean login(String movil, String contraseña) {
        Usuario usuario = repositorioUsuarios.getUsuario(movil);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    // Agregar un contacto individual al usuario actual
    public ContactoIndividual agregarContacto(String nombre, String movil) {
        if (usuarioActual == null) return null;

        Usuario usuarioContacto = repositorioUsuarios.getUsuario(movil);
        if (usuarioContacto == null) return null;

        ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto);
        if (usuarioActual.addContacto(nuevoContacto)) {
            return nuevoContacto;
        }
        return null;
    }

    // Enviar mensaje a un contacto individual
    public boolean enviarMensajeContacto(ContactoIndividual receptor, String texto, int emoticono, int tipo) {
        if (usuarioActual == null || receptor == null) return false;
        return usuarioActual.addMensaje(receptor, texto, emoticono, tipo);
    }
}
