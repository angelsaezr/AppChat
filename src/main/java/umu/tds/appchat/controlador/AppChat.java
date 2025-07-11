package umu.tds.appchat.controlador;

import java.io.File;

import java.time.LocalDate;
import java.util.List;

import umu.tds.appchat.dominio.Usuario;
import umu.tds.appchat.persistencia.DAOException;
import umu.tds.appchat.persistencia.FactoriaDAO;
import umu.tds.appchat.persistencia.IAdaptadorContactoIndividualDAO;
import umu.tds.appchat.persistencia.IAdaptadorGrupoDAO;
import umu.tds.appchat.persistencia.IAdaptadorMensajeDAO;
import umu.tds.appchat.persistencia.IAdaptadorUsuarioDAO;
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

    /**
     * Activa el modo premium para el usuario actual con un tipo de descuento especificado.
     *
     * @param tipo el tipo de descuento a aplicar
     * @return true si se activó correctamente, false si no hay usuario actual
     */
    public double activarPremium(TipoDescuento tipo) {
        usuarioActual.activarPremium(tipo);
        adaptadorDescuento.registrarDescuento(DescuentoFactoria.crearDescuento(tipo));
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return usuarioActual.calcularDescuento();
    }

    /**
     * Anula el modo premium del usuario actual.
     *
     * @return true si se desactivó correctamente, false si no hay usuario actual
     */
    public boolean anularPremium() {
    	Descuento descuento = this.usuarioActual.getDescuento().get();
        this.usuarioActual.anularPremium();
        adaptadorDescuento.borrarDescuento(descuento);
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
     * @param rutaImagen ruta de la imagen del grupo
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
     * Envía un mensaje a un contacto individual o a un grupo.
     * <p>
     * Dependiendo del tipo del contacto receptor, este método se encarga de enviar un mensaje,
     * registrar el mensaje enviado, simular la recepción del mensaje por el destinatario (para
     * mantener la consistencia en ambas direcciones), y actualizar los datos correspondientes en
     * los adaptadores de datos.
     * </p>
     *
     * @param receptor   El contacto destinatario del mensaje. Puede ser un {@link ContactoIndividual} o un {@link Grupo}.
     * @param texto      El texto del mensaje a enviar.
     * @param emoticono  El identificador del emoticono asociado al mensaje.
     * @return {@code true} si el mensaje fue enviado correctamente.
     */
    public boolean enviarMensajeContacto(Contacto receptor, String texto, int emoticono) {
    	Mensaje mensajeEnviado;
    	if (receptor instanceof ContactoIndividual) {
    		ContactoIndividual contactoReceptor = (ContactoIndividual) receptor;
    		mensajeEnviado = this.usuarioActual.enviarMensajeAContactoIndividual(contactoReceptor, texto, emoticono, TipoMensaje.ENVIADO);
    		adaptadorMensaje.registrarMensaje(mensajeEnviado);
    		
    		añadirMensajeRecibido(contactoReceptor, texto, emoticono);
    		
            adaptadorContactoIndividual.modificarContactoIndividual(contactoReceptor);
    	} else if (receptor instanceof Grupo) {
    		Grupo grupoReceptor = (Grupo) receptor;
    		mensajeEnviado = this.usuarioActual.enviarMensajeAGrupo(grupoReceptor, texto, emoticono, TipoMensaje.ENVIADO);
    		adaptadorMensaje.registrarMensaje(mensajeEnviado);
    		
    		for(ContactoIndividual contactoReceptor : grupoReceptor.getMiembros()) {
    			añadirMensajeRecibido(contactoReceptor, texto, emoticono);
    		}
    		
    		adaptadorGrupo.modificarGrupo(grupoReceptor);
    	}

        return true;
    }
    
    /**
     * Simula la recepción de un mensaje por parte de un contacto individual.
     * <p>
     * Si el contacto del remitente no existe en la agenda del usuario receptor, se crea automáticamente.
     * Luego se genera un mensaje con tipo {@code RECIBIDO}, se registra y se actualizan los datos del
     * contacto en los adaptadores correspondientes.
     * </p>
     *
     * @param contactoReceptor El contacto individual que recibe el mensaje.
     * @param texto            El contenido del mensaje recibido.
     * @param emoticono        El identificador del emoticono del mensaje.
     */
    private void añadirMensajeRecibido(ContactoIndividual contactoReceptor, String texto, int emoticono) {
    	Usuario usuarioReceptor = contactoReceptor.getUsuario();
        ContactoIndividual contactoSender = usuarioReceptor.getContactoIndividual(usuarioActual.getMovil());
        if (contactoSender == null) {
        	contactoSender = usuarioReceptor.addContactoIndividual("$"+usuarioActual.getMovil(), usuarioActual);
            adaptadorContactoIndividual.registrarContactoIndividual(contactoSender);
            adaptadorUsuario.modificarUsuario(usuarioReceptor);
        }
        
        Mensaje mensajeRecibido = usuarioReceptor.enviarMensajeAContactoIndividual(contactoSender, texto, emoticono, TipoMensaje.RECIBIDO);
        
        adaptadorMensaje.registrarMensaje(mensajeRecibido);
        adaptadorContactoIndividual.modificarContactoIndividual(contactoSender);
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
     * @param nuevosMiembros lista de nuevos miembros
     * @return true si se actualizaron correctamente los miembros del grupo, false si hubo algún error
     */
    public boolean actualizarMiembrosGrupo(Grupo grupo, List<ContactoIndividual> nuevosMiembros) {
        grupo.actualizarMiembrosGrupo(nuevosMiembros);
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
