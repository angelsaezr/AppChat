package umu.tds.appchat.controlador;

import java.io.File;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
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

    public boolean agregarContacto(String nombre, String movil) {
        ContactoIndividual nuevoContacto = appChat.agregarContacto(nombre, movil);
        if(nuevoContacto != null) return true;
        return false;
    }
    
    public boolean agregarGrupo(String nombreGrupo, List<String> miembros, File imagenGrupo) {
    	String rutaImagen = "";
    	if (imagenGrupo != null)
    		rutaImagen = imagenGrupo.getAbsolutePath();
    	
    	Grupo nuevoGrupo = appChat.agregarGrupo(nombreGrupo, miembros, rutaImagen);
    	if(nuevoGrupo != null) return true;
    	return false;
    }

    public boolean enviarMensajeContacto(Contacto receptor, String texto, int emoticono) {
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
    
    public boolean activarPremium() {
		return appChat.activarPremium();
	}

	public boolean anularPremium() {
		return appChat.anularPremium();
	}

	public List<String> getMiembrosGrupo(Grupo grupo) {
	    return appChat.getMiembrosGrupo(grupo).stream()
	        .map(ContactoIndividual::getNombre) // Obtener el nombre de cada contacto
	        .collect(Collectors.toList()); // Convertir a lista
	}
	
	public List<Mensaje> buscarMensajes(String texto, String movil, String contacto) {
	    String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
	            .replaceAll("\\p{M}", "")
	            .toLowerCase();

	    return getContactosUsuarioActual().stream()
	        .filter(c -> esContactoRelevante(c, movil, contacto)) // Filtra contactos relevantes
	        .flatMap(c -> getMensajes(c).stream()
	            .filter(m -> textoNormalizado.isBlank() || 
	                Normalizer.normalize(m.getTexto(), Normalizer.Form.NFD)
	                    .replaceAll("\\p{M}", "")
	                    .toLowerCase()
	                    .contains(textoNormalizado)) // Búsqueda sin tildes y flexible
	            .filter(m -> !m.getTexto().isBlank()) // Evita mensajes vacíos
	        )
	        .sorted(Comparator.comparing(Mensaje::getFechaHoraEnvio).reversed()) // Ordena de más reciente a antiguo
	        .collect(Collectors.toList());
	}

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
	    return true; // Si no hay filtros, se considera relevante
	}
	
	// Método auxiliar para obtener el nombre del contacto
    public String getNombreContacto(Contacto c) {
        return c.getNombre().isBlank() && c instanceof ContactoIndividual 
            ? ((ContactoIndividual) c).getMovil() 
            : c.getNombre();
    }
}
