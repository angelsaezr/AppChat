package umu.tds.appchat.controlador;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.RepositorioUsuarios;
import umu.tds.appchat.dominio.TipoMensaje;

/**
 * Clase AppChat que tiene todos los usuarios.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class AppChat {

    public static final AppChat INSTANCE = new AppChat();
    public static double COSTE_PREMIUM = 100.0;
    
    private RepositorioUsuarios repositorioUsuarios;
    private Usuario usuarioActual;

    private AppChat() {
        this.repositorioUsuarios = RepositorioUsuarios.INSTANCE;
    }
    
    public Usuario getUsuarioActual() {
		return usuarioActual;
	}

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    // Registrar un nuevo usuario
    public boolean registrarUsuario(String nombre, String movil, String contraseña, LocalDate fechaNacimiento, String imagen, String saludo, String email) {
        if (repositorioUsuarios.buscarUsuarioPorMovil(movil) != null) {
            return false; // Ya existe un usuario con ese número
        }
        if (imagen == "")
        	imagen = "src/main/resources/profile1.jpg";
        Usuario nuevoUsuario = new Usuario(nombre, movil, contraseña, imagen, saludo, email, fechaNacimiento);
        return repositorioUsuarios.addUsuario(nuevoUsuario);
    }

    // Iniciar sesión con móvil y contraseña
    public boolean login(String movil, String contraseña) {
        Usuario usuario = repositorioUsuarios.buscarUsuarioPorMovil(movil);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    // Agregar un contacto individual al usuario actual
    public ContactoIndividual agregarContacto(String nombre, String movil) {
        if (usuarioActual == null) return null;

        Usuario usuarioContacto = repositorioUsuarios.buscarUsuarioPorMovil(movil);
        if (usuarioContacto == null) return null;

        ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto);
        if (usuarioActual.addContacto(nuevoContacto)) return nuevoContacto;
        
        return null;
    }
    
    public Grupo agregarGrupo(String nombreGrupo, List<String> miembros, String imagenGrupo) {
    	if (usuarioActual == null) return null;
    	
    	List<ContactoIndividual> contactos = new LinkedList<ContactoIndividual>();
    	for(Contacto c : getContactosUsuarioActual()) {
    		if (c instanceof ContactoIndividual) {
    			for(String s : miembros) {
    	    		if (s.equals(c.getNombre())) {
    	    			ContactoIndividual cI = (ContactoIndividual) c;
    	    			Usuario usuarioContacto = repositorioUsuarios.buscarUsuarioPorMovil(cI.getMovil());
    	    			if (usuarioContacto == null || cI.getMovil().equals(usuarioActual.getMovil())) return null;
    	    			contactos.add(cI);
    	    		}
    	    	}
    		}
    	}
    	
    	Grupo nuevoGrupo = new Grupo(nombreGrupo, contactos, imagenGrupo);
    	if(usuarioActual.addContacto(nuevoGrupo)) return nuevoGrupo;
    	
    	return null;
    }

    // Enviar mensaje a un contacto individual
    public boolean enviarMensajeContacto(Contacto receptor, String texto, int emoticono) {
    	if (usuarioActual == null) return false;
    	
        // Intentar enviar el mensaje desde el usuario actual
        if (!usuarioActual.addMensaje(receptor, texto, emoticono, TipoMensaje.ENVIADO)) {
            return false;
        }

        if (receptor instanceof ContactoIndividual) {
            return enviarMensajeAContactoIndividual((ContactoIndividual) receptor, texto, emoticono);
        } else if (receptor instanceof Grupo) {
            return enviarMensajeAGrupo((Grupo) receptor, texto, emoticono);
        }

        return true;
    }

    // Método auxiliar para enviar mensaje a un usuario individual
    private boolean enviarMensajeAContactoIndividual(ContactoIndividual contactoReceptor, String texto, int emoticono) {
        Usuario usuarioReceptor = contactoReceptor.getUsuario();
        ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());

        if (usuarioReceptor.getContactoIndividual(usuarioActual.getMovil()) == null) { 
        	contactoSender = new ContactoIndividual("", usuarioActual);
        	usuarioReceptor.addContacto(contactoSender); // Si no lo tiene agregado, lo agrega sin nombre
        }

        return usuarioReceptor.addMensaje(contactoSender, texto, emoticono, TipoMensaje.RECIBIDO);
    }

    // Método auxiliar para enviar mensaje a un grupo
    private boolean enviarMensajeAGrupo(Grupo grupo, String texto, int emoticono) {
        return grupo.getMiembros().stream()
            .map(ContactoIndividual::getUsuario)
            .filter(usuarioReceptor -> !usuarioReceptor.equals(usuarioActual))
            .map(usuarioReceptor -> {
                ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());
                if (contactoSender == null) {
                	contactoSender = new ContactoIndividual("", usuarioActual);
                    usuarioReceptor.addContacto(contactoSender); // Si no lo tiene agregado, lo agrega sin nombre
                }
                return usuarioReceptor.addMensaje(contactoSender, texto, emoticono, TipoMensaje.RECIBIDO);
            })
            .allMatch(Boolean::booleanValue); // Devuelve true si TODOS los mensajes se envían con éxito
    }
    
    // Comprobar si el contacto es miembro del grupo
    public boolean esMiembroGrupo(String contacto, String grupo) {
		return usuarioActual.esMiembroGrupo(contacto, grupo);
	}
    
    public List<Mensaje> getMensajesDelContacto(Contacto contacto) {
    	return usuarioActual.getMensajesDeContacto(contacto);
    }
    
    public List<Contacto> getContactosUsuarioActual() {
    	return usuarioActual.getContactos();
	}
    
    public boolean activarPremium() {
    	if (usuarioActual == null) return false;
    	
		this.usuarioActual.setPremium(true);
		// TODO
		return true;
	}

	public boolean anularPremium() {
		if (usuarioActual == null) return false;
		
		this.usuarioActual.setPremium(false);
		// TODO
		return true;
	}

	public List<ContactoIndividual> getMiembrosGrupo(Grupo grupo) {
		return grupo.getMiembros();
	}
}
