package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

/**
 * Adaptador para la persistencia de objetos {@link ContactoIndividual} usando el patrón DAO.
 * Utiliza un servicio de persistencia abstracto para registrar y recuperar entidades.
 * Aplica patrón Singleton para asegurar una única instancia del adaptador.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class AdaptadorContactoIndividual implements IAdaptadorContactoIndividualDAO {

    /**
     * Nombre de la propiedad "nombre" usada en persistencia.
     */
    private static final String NOMBRE = "nombre";

    /**
     * Nombre de la propiedad "mensajes" usada en persistencia.
     */
    private static final String MENSAJES = "mensajes";

    /**
     * Nombre de la propiedad "usuario" usada en persistencia.
     */
    private static final String USUARIO = "usuario";

    /**
     * Servicio de persistencia que se encarga de interactuar con la capa de almacenamiento.
     */
    private ServicioPersistencia servPersistencia;

    /**
     * Instancia única del adaptador (Singleton).
     */
    private static AdaptadorContactoIndividual unicaInstancia = null;

    /**
     * Constructor que inicializa el servicio de persistencia desde la factoría.
     */
    public AdaptadorContactoIndividual() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Devuelve la instancia única del adaptador (Singleton).
     *
     * @return instancia única de {@code AdaptadorContactoIndividual}
     */
    public static AdaptadorContactoIndividual getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new AdaptadorContactoIndividual();
        return unicaInstancia;
    }

    /**
     * Registra un {@link ContactoIndividual} en el sistema de persistencia.
     * Se encarga de crear una entidad, registrar sus mensajes y asociarla con su usuario.
     * Si ya está registrado por código, no realiza ninguna acción.
     *
     * @param contacto el contacto individual a registrar
     */
	public void registrarContactoIndividual(ContactoIndividual contacto) {
		// Se comprueba que no está registrada la entidad que corresponde al código del objeto
		Optional<Entidad> eContactoIndividual = Optional.ofNullable(servPersistencia.recuperarEntidad(contacto.getCodigo()));
		if (eContactoIndividual.isPresent()) {
			return;
		}

		// TODO NO DEBERIA AÑADIRSE EL USUARIO OTRA VEZ AdaptadorUsuario.getUnicaInstancia().registrarUsuario(contacto.getUsuario());	

		List<Mensaje> mensajesRegistrados = new ArrayList<>();

		for (Mensaje m : contacto.getMensajes()) {
		    AdaptadorMensaje.getUnicaInstancia().registrarMensaje(m);
		    mensajesRegistrados.add(m);
		}
		
		// Se crea la entidad
		eContactoIndividual = Optional.of(new Entidad());
		eContactoIndividual.get().setNombre("contactoIndividual");

		// Se crean y añaden las propiedades a la entidad creada
		eContactoIndividual.get().setPropiedades(new ArrayList<>(Arrays.asList(
			    new Propiedad(NOMBRE, contacto.getNombre()),
			    new Propiedad(MENSAJES, getCodigos(mensajesRegistrados)),
			    new Propiedad(USUARIO, String.valueOf(contacto.getUsuario().getCodigo()))
			)));

		// Se registra la entidad y se asocia id al objeto almacenado
		eContactoIndividual = Optional.ofNullable(servPersistencia.registrarEntidad(eContactoIndividual.get()));
		contacto.setCodigo(eContactoIndividual.get().getId());
		System.out.println("ContactoIndvidual " + contacto.getNombre() + " registrado");
		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(contacto.getCodigo(), contacto);
	}

	/**
     * Recupera un {@link ContactoIndividual} desde la capa de persistencia usando su código.
     * Si el objeto ya está en el {@link PoolDAO}, lo devuelve directamente.
     * Si no está, lo reconstruye con sus propiedades y lo añade al pool.
     *
     * @param codigo código identificador del contacto individual
     * @return el objeto {@link ContactoIndividual} correspondiente
     */
	public ContactoIndividual recuperarContactoIndividual(int codigo) {
		// Si el objeto está en el pool se retorna
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		// Si no lo está se recupera entidad y las propiedades de campos de tipo primitivo
		Entidad eContactoIndividual = servPersistencia.recuperarEntidad(codigo);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContactoIndividual, NOMBRE);

		// Se recuperan los objetos referenciados, se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
		Usuario usuario = AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eContactoIndividual, USUARIO)));
		ContactoIndividual contactoIndividual = new ContactoIndividual(nombre, usuario);
		contactoIndividual.setCodigo(codigo);

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(codigo, contactoIndividual);

		// Se actualiza el objeto
		List<Mensaje> mensajes = getMensajes(servPersistencia.recuperarPropiedadEntidad(eContactoIndividual, MENSAJES));
		
		for (Mensaje mensaje : mensajes) {
			contactoIndividual.addMensaje(mensaje);
		}
	
		contactoIndividual.setUsuario(usuario);	
		System.out.println("ContactoIndvidual " + contactoIndividual.getNombre() + " recuperado");
		// Se retorna el objeto
		return contactoIndividual;
	}	
	
	/**
     * Recupera todos los contactos individuales almacenados en la capa de persistencia.
     * Cada entidad se reconstruye utilizando {@code recuperarContactoIndividual}.
     *
     * @return lista de todos los {@link ContactoIndividual} registrados
     */
	public List<ContactoIndividual> recuperarTodosLosContactosIndividuales() {
		return servPersistencia.recuperarEntidades("contactoIndividual").stream()
				.map(entidad -> recuperarContactoIndividual(entidad.getId()))
				.collect(Collectors.toList());
	}

	/**
     * Modifica los datos persistidos de un {@link ContactoIndividual}.
     * Actualiza las propiedades de nombre, mensajes y usuario en la entidad correspondiente.
     *
     * @param contacto el contacto individual con la información actualizada
     */
	public void modificarContactoIndividual(ContactoIndividual contacto) {
		//Se recupera entidad
		Entidad eContactoIndividual = servPersistencia.recuperarEntidad(contacto.getCodigo());
		
		//Se recorren sus propiedades y se actualiza su valor
		for (Propiedad prop : eContactoIndividual.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(contacto.getNombre());
			} else if (prop.getNombre().equals(MENSAJES)) {
				prop.setValor(getCodigos(contacto.getMensajes()));
			} else if (prop.getNombre().equals(USUARIO)) {
				prop.setValor(String.valueOf(contacto.getUsuario().getCodigo()));
			}
			
			servPersistencia.modificarEntidad(eContactoIndividual);
		}
	}

	/**
     * Elimina completamente un {@link ContactoIndividual} del sistema de persistencia.
     * Borra primero los mensajes asociados, luego la entidad, y finalmente lo quita del pool.
     *
     * @param contacto el contacto individual a eliminar
     */
	public void borrarContactoIndividual(ContactoIndividual contacto) {
		// TODO ES POSIBLE ELIMINAR UN CONTACTO INDIVIDUAL EN LA APP? CREO QUE NO
		// Se recupera entidad
		Entidad eContactoIndividual = servPersistencia.recuperarEntidad(contacto.getCodigo());

		// Se eliminan sus entidades agregadas
		contacto.getMensajes().forEach(AdaptadorMensaje.getUnicaInstancia()::borrarMensaje);

		// Se elimina la entidad
		servPersistencia.borrarEntidad(eContactoIndividual);

		// Si esta en el pool de objetos se elimina
		if (PoolDAO.getUnicaInstancia().contains(contacto.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObject(contacto.getCodigo());
		}
	}
	
	/**
     * Recupera una lista de objetos {@link Mensaje} a partir de un string con sus códigos separados por espacios.
     *
     * @param codigos cadena con los códigos de los mensajes
     * @return lista de mensajes recuperados
     */
	private List<Mensaje> getMensajes(String codigos) {
		return Arrays.stream(codigos.split(" "))
				.filter(codigo -> !codigo.isEmpty())
				.map(Integer::parseInt)
				.map(codigo -> AdaptadorMensaje.getUnicaInstancia().recuperarMensaje(codigo))
				.collect(Collectors.toList());
	}
	
	/**
     * Convierte una lista de mensajes a una cadena con sus códigos separados por espacios.
     * Este formato es compatible con la forma en que se almacenan en la capa de persistencia.
     *
     * @param mensajes lista de mensajes
     * @return string con los códigos separados por espacios
     */
	private String getCodigos(List<Mensaje> mensajes) {
		return mensajes.stream().map(m -> String.valueOf(m.getCodigo())) // Convertimos el código a String
				.collect(Collectors.joining(" ")); // Unimos los códigos con un espacio entre ellos, eficiente y buena
													// práctica en rendimiento
	}
}
