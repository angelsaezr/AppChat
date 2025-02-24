package umu.tds.appchat.controlador;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Mensaje;

/**
 * Clase controlador para delegar acciones.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Controlador {
    public static final Controlador INSTANCE = new Controlador();
    
    private final AppChat appChat;

    private Controlador() {
        this.appChat = AppChat.INSTANCE;
    }
    
    public Usuario getUsuarioActual() {
        return appChat.getUsuarioActual();
    }
    
    public String getImagenPerfil() {
    	return appChat.getUsuarioActual().getImagen();
    }

    public boolean registrarUsuario(String nombre, String movil, String contraseña, Date fechaNacimiento, File imagen, String saludo, String email) {
    	String rutaImagen = "";
    	if (imagen != null)
    		rutaImagen = imagen.getAbsolutePath();
    	LocalDate fechaNacimientoLocal = LocalDate.now();
    	if (fechaNacimiento != null)
    		fechaNacimientoLocal = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return appChat.registrarUsuario(nombre, movil, contraseña, fechaNacimientoLocal, rutaImagen, saludo, email);
    }

    public boolean login(String movil, String contraseña) {
        return appChat.login(movil, contraseña);
    }

    public ContactoIndividual agregarContacto(String nombre, String movil) {
        return appChat.agregarContacto(nombre, movil);
    }

    public boolean enviarMensajeContacto(ContactoIndividual receptor, String texto, int emoticono) {
        return appChat.enviarMensajeContacto(receptor, texto, emoticono);
    }
    
    public boolean esMiembroGrupo(String contacto, String grupo) {
		return appChat.esMiembroGrupo(contacto, grupo);
	}
    
    public List<Mensaje> getMensajes(Contacto contacto) {
        return appChat.getMensajesDelContacto(contacto);
    }
    
    public List<Contacto> getContactosUsuarioActual() {
        return appChat.getContactosUsuarioActual();
    }
}
