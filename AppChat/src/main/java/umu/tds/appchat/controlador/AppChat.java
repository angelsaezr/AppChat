package umu.tds.appchat.controlador;

import java.io.File;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.persistencia.DAOException;
import umu.tds.appchat.persistencia.FactoriaDAO;
import umu.tds.appchat.persistencia.IAdaptadorContactoIndividualDAO;
import umu.tds.appchat.persistencia.IAdaptadorGrupoDAO;
import umu.tds.appchat.persistencia.IAdaptadorMensajeDAO;
import umu.tds.appchat.persistencia.IAdaptadorUsuarioDAO;
import umu.tds.appchat.utils.Utils;
import umu.tds.appchat.persistencia.IAdaptadorDescuentoDAO;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Descuento;
import umu.tds.appchat.dominio.DescuentoFactoria;
import umu.tds.appchat.dominio.Grupo;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.RepositorioUsuarios;
import umu.tds.appchat.dominio.TipoDescuento;
import umu.tds.appchat.dominio.TipoMensaje;

/**
 * Clase Controladora.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class AppChat {
	/**
	 * Instancia única de la clase AppChat (patrón Singleton).
	 */
	private static AppChat INSTANCE;

	/**
	 * Coste del servicio premium para los usuarios.
	 */
	public static double COSTE_PREMIUM = 100.0;

	/**
	 * Adaptador para operaciones de persistencia sobre usuarios.
	 */
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	/**
	 * Adaptador para operaciones de persistencia sobre contactos individuales.
	 */
	private IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;

	/**
	 * Adaptador para operaciones de persistencia sobre grupos.
	 */
	private IAdaptadorGrupoDAO adaptadorGrupo;

	/**
	 * Adaptador para operaciones de persistencia sobre mensajes.
	 */
	private IAdaptadorMensajeDAO adaptadorMensaje;
	
	/**
	 * Adaptador para operaciones de persistencia sobre descuentos.
	 */
	private IAdaptadorDescuentoDAO adaptadorDescuento;

	/**
	 * Repositorio de usuarios en memoria.
	 */
	private RepositorioUsuarios repositorioUsuarios;

	/**
	 * Usuario que ha iniciado sesión actualmente en la aplicación.
	 */
	private Usuario usuarioActual;

    /**
     * Constructor privado de la clase AppChat.
     * Inicializa adaptadores y repositorios.
     */
    private AppChat() {
        inicializarAdaptadores();
        inicializarReposiorios();
    }

    /**
     * Devuelve la instancia única de AppChat.
     * Si no existe, se crea una nueva.
     *
     * @return instancia única de AppChat
     */
    public static AppChat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppChat();
        }
        return INSTANCE;
    }

    /**
     * Inicializa los adaptadores de acceso a datos mediante la factoría DAO.
     */
    private void inicializarAdaptadores() {
        FactoriaDAO factoria = null;
        try {
            factoria = FactoriaDAO.getUnicaInstancia();    
            adaptadorUsuario = factoria.getUsuarioDAO();
            adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
            adaptadorGrupo = factoria.getGrupoDAO();
            adaptadorMensaje = factoria.getMensajeDAO();
            adaptadorDescuento = factoria.getDescuentoDAO();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa los repositorios necesarios para el funcionamiento de la aplicación.
     */
    private void inicializarReposiorios() {
        this.repositorioUsuarios = RepositorioUsuarios.INSTANCE;
    }

    /**
     * Obtiene el usuario actualmente activo en la aplicación.
     *
     * @return el usuario actual
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    // TODO cargar appchar ponerlo para tutoria
    // TODO lo de asignar nombre 

    /**
     * Activa el modo premium para el usuario actual con un tipo de descuento especificado.
     *
     * @param tipo el tipo de descuento a aplicar
     * @return true si se activó correctamente, false si no hay usuario actual
     */
    public boolean activarPremium(TipoDescuento tipo) {
        usuarioActual.activarPremium(tipo);
        
        adaptadorDescuento.registrarDescuento(DescuentoFactoria.crearDescuento(tipo));
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return true;
    }

    /**
     * Anula el modo premium del usuario actual.
     *
     * @return true si se desactivó correctamente, false si no hay usuario actual
     */
    public boolean anularPremium() {
        this.usuarioActual.anularPremium();
        Descuento d = this.usuarioActual.getDescuento().get();
        adaptadorDescuento.borrarDescuento(d);
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return true;
    }

    /**
     * Registrar un nuevo usuario.
     *
     * @param nombre nombre del usuario
     * @param movil número de móvil del usuario
     * @param contraseña contraseña del usuario
     * @param fechaNacimiento fecha de nacimiento del usuario
     * @param imagen ruta de la imagen de perfil
     * @param saludo saludo personalizado del usuario
     * @param email correo electrónico del usuario
     * @return true si el usuario fue registrado correctamente, false si ya existe uno con el mismo móvil
     */
    public boolean registrarUsuario(String nombre, String movil, String contraseña, LocalDate fechaNacimiento, String imagen, String saludo, String email) {
        if (repositorioUsuarios.buscarUsuarioPorMovil(movil) != null) {
            return false; // Ya existe un usuario con ese número
        }
        Usuario nuevoUsuario = repositorioUsuarios.addUsuario(nombre, movil, contraseña, fechaNacimiento, imagen, saludo, email);
        if(nuevoUsuario != null) {
        	adaptadorUsuario.registrarUsuario(nuevoUsuario);
        	return true;
        }
        return false;
    }

    /**
     * Iniciar sesión con móvil y contraseña.
     *
     * @param movil número de móvil del usuario
     * @param contraseña contraseña del usuario
     * @return true si las credenciales son correctas, false en caso contrario
     */
    public boolean login(String movil, String contraseña) {
        Usuario usuario = repositorioUsuarios.buscarUsuarioPorMovil(movil);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    /**
     * Agregar un contacto individual al usuario actual.
     *
     * @param nombre nombre del contacto
     * @param movil número de móvil del contacto
     * @return el contacto individual creado, o null si no fue posible agregarlo
     */
    public ContactoIndividual agregarContactoIndividual(String nombre, String movil) {
        Usuario usuarioContacto = repositorioUsuarios.buscarUsuarioPorMovil(movil);
        if (usuarioContacto == null) return null;
        ContactoIndividual nuevoContacto = usuarioActual.addContactoIndividual(nombre, usuarioContacto);
        if (nuevoContacto != null) {
            adaptadorContactoIndividual.registrarContactoIndividual(nuevoContacto);
            adaptadorUsuario.modificarUsuario(usuarioActual);
        }
        return nuevoContacto;
    }

    /**
     * Agrega un nuevo grupo con miembros seleccionados e imagen de grupo.
     *
     * @param nombreGrupo nombre del grupo a crear
     * @param miembros lista de nombres de los miembros
     * @param imagenGrupo archivo de imagen del grupo (puede ser null)
     * @return el grupo creado, o null si no fue posible crear el grupo
     */
    public Grupo agregarGrupo(String nombreGrupo, List<ContactoIndividual> miembros, String rutaImagen) {
    	Grupo nuevoGrupo = usuarioActual.addGrupo(nombreGrupo, miembros, rutaImagen);
    	if (nuevoGrupo != null) {
    		adaptadorGrupo.registrarGrupo(nuevoGrupo);
            adaptadorUsuario.modificarUsuario(usuarioActual);
    	}
        return nuevoGrupo;
    }

    /**
     * Enviar mensaje a un contacto individual.
     *
     * @param receptor el contacto o grupo al que se desea enviar el mensaje
     * @param texto contenido del mensaje
     * @param emoticono código del emoticono adjunto
     * @return true si el mensaje fue enviado correctamente, false en caso contrario
     */
    public boolean enviarMensajeContacto(Contacto receptor, String texto, int emoticono) {
        // TODO patron creador, cambiar
        // Crea y registrar mensaje
        Mensaje mensaje = new Mensaje(texto, emoticono, TipoMensaje.ENVIADO, LocalDateTime.now());

        // Intenta enviarlo desde el usuario actual
        if (!usuarioActual.addMensaje(receptor, mensaje)) {
            return false;
        }
        adaptadorMensaje.registrarMensaje(mensaje);

        if (receptor instanceof ContactoIndividual) {
            return enviarMensajeAContactoIndividual((ContactoIndividual) receptor, mensaje);
        } else if (receptor instanceof Grupo) {
            return enviarMensajeAGrupo((Grupo) receptor, mensaje);
        }

        return true;
    }


    /**
     * Método auxiliar para enviar mensaje a un usuario individual.
     *
     * @param contactoReceptor el contacto individual que recibe el mensaje
     * @param mensaje el mensaje a enviar
     * @return true si el mensaje se envió correctamente
     */
    private boolean enviarMensajeAContactoIndividual(ContactoIndividual contactoReceptor, Mensaje mensajeOriginal) {
        Usuario usuarioReceptor = contactoReceptor.getUsuario();
        ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());

        if (contactoSender == null) {
        	contactoSender = usuarioReceptor.addContactoIndividual("$"+usuarioActual.getMovil(), usuarioActual);
            adaptadorContactoIndividual.registrarContactoIndividual(contactoSender);
            adaptadorUsuario.modificarUsuario(usuarioReceptor);
        }
        // TODO creador, logica bien
        Mensaje mensaje2 = new Mensaje(mensajeOriginal.getTexto(), mensajeOriginal.getEmoticono(), TipoMensaje.RECIBIDO, LocalDateTime.now());
        usuarioReceptor.addMensaje(contactoSender, mensaje2);
        
        adaptadorMensaje.registrarMensaje(mensaje2);
        adaptadorContactoIndividual.modificarContactoIndividual(contactoReceptor);
        adaptadorContactoIndividual.modificarContactoIndividual(contactoSender);

        return true;
    }

    /**
     * Método auxiliar para enviar mensaje a un grupo.
     *
     * @param grupo el grupo al que se enviará el mensaje
     * @param mensajeOriginal el mensaje a enviar
     * @return true si el mensaje fue enviado a todos los miembros correctamente
     */
    private boolean enviarMensajeAGrupo(Grupo grupo, Mensaje mensajeOriginal) {
    	grupo.getMiembros().stream()
        .map(ContactoIndividual::getUsuario)
        .filter(usuarioReceptor -> !usuarioReceptor.equals(usuarioActual))
        .forEach(usuarioReceptor -> {
            ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());
            if (contactoSender == null) {
            	contactoSender = usuarioReceptor.addContactoIndividual("$" + usuarioActual.getMovil(), usuarioActual);
                adaptadorContactoIndividual.registrarContactoIndividual(contactoSender);
                adaptadorUsuario.modificarUsuario(usuarioReceptor);
            }
            
            Mensaje mensaje2 = new Mensaje(
                mensajeOriginal.getTexto(),
                mensajeOriginal.getEmoticono(),
                TipoMensaje.RECIBIDO,
                LocalDateTime.now()
            );

            usuarioReceptor.addMensaje(contactoSender, mensaje2);
            adaptadorMensaje.registrarMensaje(mensaje2);
            adaptadorContactoIndividual.modificarContactoIndividual(contactoSender);
        });

    	adaptadorGrupo.modificarGrupo(grupo);
    	return true;
    }

    /**
     * Busca mensajes que contengan el texto especificado y pertenezcan a un contacto y móvil determinados.
     *
     * @param texto texto a buscar dentro de los mensajes
     * @param movil número de móvil asociado al contacto
     * @param contacto nombre del contacto
     * @return lista de mensajes que coinciden con los criterios de búsqueda
     */
    public List<Mensaje> buscarMensajes(String texto, String movil, String contacto) {
        // Normaliza el texto de búsqueda para eliminar tildes y convertir a minúsculas
        String textoNormalizado = Utils.normalizarTexto(texto);

        return usuarioActual.getContactos().stream()
            .filter(c -> contactoCumpleFiltros(c, movil, contacto)) // Filtra contactos relevantes
            .flatMap(c -> {
                List<Mensaje> mensajes = c.getMensajes(); // Obtiene los mensajes del contacto
                return mensajes.stream()
                    .filter(m -> textoNormalizado.isBlank() || 
                    		Utils.normalizarTexto(m.getTexto())
                            .contains(textoNormalizado)) // Búsqueda sin tildes y flexible
                    .filter(m -> !m.getTexto().isBlank()) // Evita mensajes vacíos
                    .sorted(Comparator.comparing(Mensaje::getFechaHoraEnvio).reversed()); // Ordena de más reciente a antiguo
            })
            .collect(Collectors.toList()); // Recoge los resultados en una lista
    }

    /**
     * Determina si un contacto es relevante según el móvil o el nombre proporcionado.
     *
     * @param c el contacto a evaluar
     * @param movil número de móvil para filtrar
     * @param nombre nombre para filtrar
     * @return true si el contacto cumple con los criterios de búsqueda, false en caso contrario
     */
    private boolean contactoCumpleFiltros(Contacto c, String movil, String nombre) {
    	// TODO en dominio
    	if (!movil.isBlank()) {
            if (c instanceof ContactoIndividual) {
                return ((ContactoIndividual) c).getMovil().equals(movil);
            } else if (c instanceof Grupo) {
                return ((Grupo) c).getMiembros().stream()
                    .anyMatch(miembro -> miembro.getMovil().equals(movil));
            }
        }
        if (!nombre.isBlank()) {
            String nombreNormalizado = Utils.normalizarTexto(nombre);

            String nombreContactoNormalizado = Utils.normalizarTexto(c.getNombre());

            if (nombreContactoNormalizado.contains(nombreNormalizado)) return true;

            if (c instanceof Grupo) {
                return ((Grupo) c).getMiembros().stream().anyMatch(miembro -> {
                    String nombreMiembroNormalizado = Utils.normalizarTexto(miembro.getNombre());
                    return nombreMiembroNormalizado.contains(nombreNormalizado);
                });
            }
            return false;
        }
        return true; // Si no hay filtros, se considera relevante
    }

    /**
     * Asigna un nuevo nombre a un contacto individual.
     *
     * @param nombre nuevo nombre a asignar
     * @param contacto el contacto al que se le asignará el nombre
     * @return true si el nombre se asignó correctamente
     */
    public boolean asignarNombre(String nombre, Contacto contacto) {
        contacto.setNombre(nombre);
        adaptadorContactoIndividual.modificarContactoIndividual((ContactoIndividual) contacto);
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return true;
    }

    /**
     * Cambia la imagen de un grupo específico.
     *
     * @param contactoSeleccionado el grupo al que se le cambiará la imagen
     * @param imagenGrupo archivo de imagen a asignar al grupo
     * @return true si la imagen fue cambiada exitosamente, false si no se proporcionó imagen
     */
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
	
    /**
     * Cambia la imagen de perfil del usuario actual.
     *
     * @param imagen archivo de imagen a establecer como imagen de perfil
     * @return true si se cambió correctamente
     */
    public boolean cambiarImagenPerfil(File imagen) {
        String rutaImagen = "";
        if (imagen != null)
            rutaImagen = imagen.getAbsolutePath();
        usuarioActual.setImagen(rutaImagen);
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return true;
    }

    /**
     * Cambia el saludo del usuario actual.
     *
     * @param text nuevo saludo a establecer
     */
    public void cambiarSaludo(String text) {
        usuarioActual.setSaludo(text);
        adaptadorUsuario.modificarUsuario(usuarioActual);
    }

    /**
     * Verifica si el contacto ha sido agregado por el usuario.
     *
     * @param contacto el contacto a verificar
     * @return true si el contacto tiene nombre asignado, false si no
     */
    public boolean esContactoAgregado(Contacto contacto) {
        if (contacto.getNombre() == "")
            return false;
        return true;
    }

    /**
     * Verifica si el usuario actual es premium.
     *
     * @return true si el usuario es premium, false en caso contrario
     */
    public boolean isPremium() {
        return usuarioActual.isPremium();
    }

    /**
     * Elimina un grupo de los contactos del usuario actual.
     *
     * @param grupo el grupo a eliminar
     * @return true si el grupo fue eliminado exitosamente, false en caso contrario
     */
    public boolean eliminarGrupo(Grupo grupo) {
        if (usuarioActual.removeContacto(grupo)) {
            adaptadorGrupo.borrarGrupo(grupo);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            return true;
        }
        return false;
    }
		
    /**
     * Actualiza la lista de miembros de un grupo con nuevos contactos seleccionados.
     *
     * @param grupo el grupo a actualizar
     * @param nuevosMiembros lista de números de móvil correspondientes a los nuevos miembros
     * @return true si se actualizaron correctamente los miembros del grupo, false si hubo algún error
     */
    public boolean actualizarMiembrosGrupo(Grupo grupo, List<String> nuevosMiembros) {
        if (usuarioActual == null || grupo == null || nuevosMiembros == null)
            return false;

        if (nuevosMiembros.isEmpty()) return false; // El grupo no puede quedar vacío

        List<ContactoIndividual> nuevosMiembrosLista = nuevosMiembros.stream()
            .map(usuarioActual::getContactoIndividual)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        grupo.removeAllMiembros();

        nuevosMiembrosLista.forEach(grupo::addMiembro);

        adaptadorGrupo.modificarGrupo(grupo);
        adaptadorUsuario.modificarUsuario(usuarioActual);

        return true;
    }

    /**
     * Verifica si existe un usuario con el número de móvil especificado.
     *
     * @param movil número de móvil a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existeUsuario(String movil) {
        return repositorioUsuarios.existeUsuario(movil);
    }
}
