package umu.tds.appchat.controlador;

import java.io.File;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.persistencia.DAOException;
import umu.tds.appchat.persistencia.FactoriaDAO;
import umu.tds.appchat.persistencia.IAdaptadorContactoIndividualDAO;
import umu.tds.appchat.persistencia.IAdaptadorGrupoDAO;
import umu.tds.appchat.persistencia.IAdaptadorMensajeDAO;
import umu.tds.appchat.persistencia.IAdaptadorUsuarioDAO;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.RepositorioUsuarios;
import umu.tds.appchat.dominio.TipoDescuento;
import umu.tds.appchat.dominio.TipoMensaje;

/**
 * Controlador.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class AppChat {
	private static AppChat INSTANCE;
	
    public static double COSTE_PREMIUM = 100.0;
    
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    private IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;
    private IAdaptadorGrupoDAO adaptadorGrupo;
    private IAdaptadorMensajeDAO adaptadorMensaje;
    
    private RepositorioUsuarios repositorioUsuarios;
    private Usuario usuarioActual;

    private AppChat() {
        inicializarAdaptadores();
        inicializarReposiorios();
    }
    
    public static AppChat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppChat();
        }
        return INSTANCE;
    }
    
    private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getUnicaInstancia();	
			adaptadorUsuario = factoria.getUsuarioDAO();
			adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
			adaptadorGrupo = factoria.getGrupoDAO();
			adaptadorMensaje = factoria.getMensajeDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
    
    private void inicializarReposiorios() {
    	this.repositorioUsuarios = RepositorioUsuarios.INSTANCE;
    }
    
    public Usuario getUsuarioActual() {
		return usuarioActual;
	}
    
    public String getImagenPerfil() {
		return usuarioActual.getImagen();
	}
    
    public String getNombreUsuarioActual() {
    	return usuarioActual.getNombre();
    }
    
    public String getSaludo() {
		return usuarioActual.getSaludo();
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
    
    public List<String> getMiembrosGrupo(Grupo grupo) {
		return grupo.getMiembros().stream()
		        .map(ContactoIndividual::getNombre) // Obtener el nombre de cada contacto
		        .collect(Collectors.toList()); // Convertir a lista
	}
    
    public boolean activarPremium(TipoDescuento tipo) {
    	if (usuarioActual == null) return false;
    	
		usuarioActual.setPremium(true);
		usuarioActual.setDescuento(tipo);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}
    
    public boolean anularPremium() {
		if (usuarioActual == null) return false;
		
		this.usuarioActual.setPremium(false);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}

    // Registrar un nuevo usuario
    public boolean registrarUsuario(String nombre, String movil, String contraseña, LocalDate fechaNacimiento, String imagen, String saludo, String email) {
        if (repositorioUsuarios.buscarUsuarioPorMovil(movil) != null) {
            return false; // Ya existe un usuario con ese número
        }
        if (imagen == "")
        	imagen = "src/main/resources/profile1.jpg";
        Usuario nuevoUsuario = new Usuario(nombre, movil, contraseña, imagen, saludo, email, fechaNacimiento);
        adaptadorUsuario.registrarUsuario(nuevoUsuario);
        repositorioUsuarios.addUsuario(nuevoUsuario);
        return true;
    }

    // Iniciar sesión con móvil y contraseña
    public boolean login(String movil, String contraseña) {
        Usuario usuario = repositorioUsuarios.buscarUsuarioPorMovil(movil);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            this.usuarioActual = usuario;
            cargarMensajesNoAgregados();
            return true;
        }
        return false;
    }
    
    public void cargarMensajesNoAgregados() {
        List<Mensaje> todosLosMensajes = adaptadorMensaje.recuperarTodosLosMensajes();
        List<Usuario> todosLosUsuarios = repositorioUsuarios.getUsuarios();

        for (Usuario u: todosLosUsuarios) {
        	if (u.equals(usuarioActual)) break;
        	for (Contacto contacto : u.getContactos()) {
        		//if (usuarioActual.getContactos().contains(contacto)) break;
        		if (((ContactoIndividual) contacto).getMovil().equals(this.usuarioActual.getMovil()) && !contacto.getMensajes().isEmpty() && contacto != null) {
        			ContactoIndividual co = agregarContacto("", u.getMovil());
        			System.out.println("Agregado con móvil: " + u.getMovil());
        			System.out.println("Número de contactos del usuario actual: " + usuarioActual.getContactos().size());
        			if (co != null) adaptadorContactoIndividual.registrarContactoIndividual(co);
        		}
        	}
        	//&& !esContactoAgregado(contacto)
        
        	// poner si ya lo tiene agregado que no lo agregue
        	// poner si entra una segunda vez y no lo ha agregado, que no vuelva a agregar
        	// al entrear con el otro usuario, sale el mensaje como si lo hubiera enviado el, error
        	
        }
        adaptadorUsuario.modificarUsuario(usuarioActual);
        
    }
    
    /*public void cargarMensajesNoAgregados() {
        List<Usuario> todosLosUsuarios = adaptadorUsuario.recuperarTodosLosUsuarios();

        for (Usuario u : todosLosUsuarios) {
            if (u.equals(usuarioActual)) continue;

            for (Contacto contacto : u.getContactos()) {
                if (!(contacto instanceof ContactoIndividual)) continue;

                ContactoIndividual cInd = (ContactoIndividual) contacto;

                // Si este contacto apunta al usuario actual
                if (cInd.getMovil().equals(usuarioActual.getMovil())) {

                    // Verificamos si ya está agregado en la lista de contactos del usuario actual
                    boolean yaAgregado = usuarioActual.getContactos().stream()
                            .filter(c -> c instanceof ContactoIndividual)
                            .map(c -> ((ContactoIndividual) c).getMovil())
                            .anyMatch(movil -> movil.equals(u.getMovil()));

                    if (!yaAgregado && !cInd.getMensajes().isEmpty()) {
                        // Agregar al usuario actual un contacto para este emisor
                        agregarContacto("", u.getMovil());
                        System.out.println("📩 Agregado contacto no registrado con móvil: " + u.getMovil());
                    }
                }
            }
        }

        adaptadorUsuario.modificarUsuario(usuarioActual);
    }*/
    
    /*public void cargarMensajesNoAgregados() {
        List<Mensaje> todosLosMensajes = adaptadorMensaje.recuperarTodosLosMensajes();
        List<Usuario> emisoresNoAgregados = new LinkedList<>();

        for (Mensaje mensaje : todosLosMensajes) {
            if (mensaje.getTipo() == TipoMensaje.RECIBIDO) {
                for (Contacto c : usuarioActual.getContactos()) {
                    if (c instanceof ContactoIndividual) {
                        ContactoIndividual contacto = (ContactoIndividual) c;

                        // Si este contacto tiene el mensaje recibido
                        if (contacto.getMensajes().contains(mensaje)) {
                            Usuario emisor = contacto.getUsuario();

                            // ¿Este emisor ya está agregado por el usuario actual?
                            boolean yaAgregado = usuarioActual.getContactos().stream()
                                .filter(ci -> ci instanceof ContactoIndividual)
                                .map(ci -> ((ContactoIndividual) ci).getMovil())
                                .anyMatch(movil -> movil.equals(emisor.getMovil()));

                            if (!yaAgregado && !emisoresNoAgregados.contains(emisor)) {
                                emisoresNoAgregados.add(emisor);
                                adaptadorUsuario.modificarUsuario(usuarioActual);
                            }
                        }
                    }
                }
            }
        }
    }*/


    // Agregar un contacto individual al usuario actual
    public ContactoIndividual agregarContacto(String nombre, String movil) {
        if (usuarioActual == null) return null;

        Usuario usuarioContacto = repositorioUsuarios.buscarUsuarioPorMovil(movil);
        if (usuarioContacto == null) return null;

        ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto);
        if (usuarioActual.addContacto(nuevoContacto)) {
        	adaptadorContactoIndividual.registrarContactoIndividual(nuevoContacto);
        	adaptadorUsuario.modificarUsuario(usuarioActual);
        	return nuevoContacto;
        }
        return null;
    }
    
    public Grupo agregarGrupo(String nombreGrupo, List<String> miembros, File imagenGrupo) {
    	if (usuarioActual == null) return null;
    	
    	String rutaImagen = "";
    	if (imagenGrupo != null)
    		rutaImagen = imagenGrupo.getAbsolutePath();
    	else
    		rutaImagen = "src/main/resources/grupo2.jpg";
    	
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
    	
    	Grupo nuevoGrupo = new Grupo(nombreGrupo, contactos, rutaImagen);
    	adaptadorGrupo.registrarGrupo(nuevoGrupo);
    	usuarioActual.addContacto(nuevoGrupo);
    	adaptadorUsuario.modificarUsuario(usuarioActual);
    	return nuevoGrupo;
    	
    	// TODO return null;
    }

    // Enviar mensaje a un contacto individual
    public boolean enviarMensajeContacto(Contacto receptor, String texto, int emoticono) {
        if (usuarioActual == null) return false;

        // Crea y registrar mensaje
        Mensaje mensaje = new Mensaje(texto, emoticono, TipoMensaje.ENVIADO);

        // Intenta enviarlo desde el usuario actual
        if (!usuarioActual.addMensaje(receptor, mensaje)) {
            return false;
        }

        if (receptor instanceof ContactoIndividual) {
            return enviarMensajeAContactoIndividual((ContactoIndividual) receptor, mensaje);
        } else if (receptor instanceof Grupo) {
            return enviarMensajeAGrupo((Grupo) receptor, mensaje);
        }

        return true;
    }


    // Método auxiliar para enviar mensaje a un usuario individual
    private boolean enviarMensajeAContactoIndividual(ContactoIndividual contactoReceptor, Mensaje mensaje) {
        Usuario usuarioReceptor = contactoReceptor.getUsuario();
        ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());

        if (contactoSender == null) {
            contactoSender = new ContactoIndividual("", usuarioActual);
            usuarioReceptor.addContacto(contactoSender);
        }

        usuarioReceptor.addMensaje(contactoSender, mensaje);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContactoIndividual.modificarContactoIndividual(contactoReceptor);

        return true;
    }


    // Método auxiliar para enviar mensaje a un grupo
    private boolean enviarMensajeAGrupo(Grupo grupo, Mensaje mensajeOriginal) {
        return grupo.getMiembros().stream()
            .map(ContactoIndividual::getUsuario)
            .filter(usuarioReceptor -> !usuarioReceptor.equals(usuarioActual))
            .map(usuarioReceptor -> {
                ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());
                if (contactoSender == null) {
                    contactoSender = new ContactoIndividual("", usuarioActual);
                    usuarioReceptor.addContacto(contactoSender);
                }

                // Crea una copia del mensaje para cada usuario
                Mensaje copiaMensaje = new Mensaje(mensajeOriginal.getTexto(), mensajeOriginal.getEmoticono(), TipoMensaje.RECIBIDO);
                usuarioReceptor.addMensaje(contactoSender, copiaMensaje);
                adaptadorMensaje.registrarMensaje(mensajeOriginal);
                adaptadorGrupo.modificarGrupo(grupo);
                return true;
            })
            .allMatch(Boolean::booleanValue);
    }

    
    public List<Mensaje> buscarMensajes(String texto, String movil, String contacto) {
        // Normaliza el texto de búsqueda para eliminar tildes y convertir a minúsculas
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        return getContactosUsuarioActual().stream()
            .filter(c -> esContactoRelevante(c, movil, contacto)) // Filtra contactos relevantes
            .flatMap(c -> {
                List<Mensaje> mensajes = getMensajesDelContacto(c); // Obtiene los mensajes del contacto
                return mensajes.stream()
                    .filter(m -> textoNormalizado.isBlank() || 
                        Normalizer.normalize(m.getTexto(), Normalizer.Form.NFD)
                            .replaceAll("\\p{M}", "")
                            .toLowerCase()
                            .contains(textoNormalizado)) // Búsqueda sin tildes y flexible
                    .filter(m -> !m.getTexto().isBlank()) // Evita mensajes vacíos
                    .sorted(Comparator.comparing(Mensaje::getFechaHoraEnvio).reversed()); // Ordena de más reciente a antiguo
            })
            .collect(Collectors.toList()); // Recoge los resultados en una lista
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
            String nombreNormalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();

            String nombreContactoNormalizado = Normalizer.normalize(c.getNombre(), Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();

            if (nombreContactoNormalizado.contains(nombreNormalizado)) return true;

            if (c instanceof Grupo) {
                return ((Grupo) c).getMiembros().stream().anyMatch(miembro -> {
                    String nombreMiembroNormalizado = Normalizer.normalize(miembro.getNombre(), Normalizer.Form.NFD)
                            .replaceAll("\\p{M}", "")
                            .toLowerCase();
                    return nombreMiembroNormalizado.contains(nombreNormalizado);
                });
            }
            return false;
        }
        return true; // Si no hay filtros, se considera relevante
    }


	public boolean asignarNombre(String nombre, Contacto contacto) {
		contacto.setNombre(nombre);
		adaptadorContactoIndividual.modificarContactoIndividual((ContactoIndividual) contacto);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}

	public boolean cambiarImagenGrupo(Grupo contactoSeleccionado, File imagenGrupo) {
		if (imagenGrupo == null)
    		return false;
		
		String rutaImagen = "";
    	rutaImagen = imagenGrupo.getAbsolutePath();
		contactoSeleccionado.setImagen(rutaImagen);
		adaptadorGrupo.modificarGrupo(contactoSeleccionado);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}
	
	public boolean cambiarImagenPerfil(File imagen) {
		String rutaImagen = "";
    	if (imagen != null)
    		rutaImagen = imagen.getAbsolutePath();
		usuarioActual.setImagen(rutaImagen);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}
	
	public void cambiarSaludo(String text) {
		usuarioActual.setSaludo(text);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	public String getNombreContacto(Contacto c) {
        return c.getNombre().isBlank() && c instanceof ContactoIndividual 
            ? ((ContactoIndividual) c).getMovil() 
            : c.getNombre();
    }
	
	public boolean esContactoAgregado(Contacto contacto) {
		if (contacto.getNombre() == "")
			return false;
		return true;
	}

	public double getDescuento() {
		return usuarioActual.calDescuento();
	}
	
	public boolean isPremium() {
		return usuarioActual.isPremium();
	}
	
	public boolean eliminarGrupo(Grupo grupo) {
		if (usuarioActual.removeContacto(grupo)) {
			adaptadorGrupo.borrarGrupo(grupo);
			adaptadorUsuario.modificarUsuario(usuarioActual);
			return true;
		}
		return false;
	}
		
	public boolean actualizarMiembrosGrupo(Grupo grupo, List<String> nuevosMiembros) {
	    if (usuarioActual == null || grupo == null || nuevosMiembros == null)
	        return false;
	    
	    if (nuevosMiembros.isEmpty()) return false; // El grupo no puede quedar vacío
	    
	    List<ContactoIndividual> nuevosMiembrosLista = new LinkedList<>();
	    for (String movil : nuevosMiembros) {
	        ContactoIndividual contacto = this.usuarioActual.getContactoIndividual(movil);
	        if (contacto != null) {
	            nuevosMiembrosLista.add(contacto);
	        }
	    }
	    
	    grupo.removeAllMiembros();
	    for (ContactoIndividual nuevoMiembro : nuevosMiembrosLista) {
	        grupo.addMiembro(nuevoMiembro);
	    }
	    
	    adaptadorGrupo.modificarGrupo(grupo);
	    adaptadorUsuario.modificarUsuario(usuarioActual);
	    
	    return true;
	}
	
	public boolean existeUsuario(String movil) {
		return repositorioUsuarios.existeUsuario(movil);
	}
}
