package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;

import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

/**
 * Adaptador que gestiona la persistencia de objetos {@link Mensaje} mediante el patrón DAO.
 * Utiliza un {@link ServicioPersistencia} para registrar, recuperar, modificar y eliminar entidades.
 * Implementa el patrón Singleton para garantizar una única instancia del adaptador.
 * 
 * Las propiedades que maneja incluyen texto, emoticono, tipo y fecha/hora de envío del mensaje.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class AdaptadorMensaje implements IAdaptadorMensajeDAO {

    /**
     * Propiedad correspondiente al texto del mensaje.
     */
    private static final String TEXTO = "texto";

    /**
     * Propiedad correspondiente al código del emoticono.
     */
    private static final String EMOTICONO = "emoticono";

    /**
     * Propiedad correspondiente al tipo del mensaje (enviado o recibido).
     */
    private static final String TIPO = "tipo";

    /**
     * Propiedad correspondiente a la fecha y hora de envío del mensaje.
     */
    private static final String FECHA_HORA_ENVIO = "fechaHoraEnvio";

    /**
     * Servicio de persistencia utilizado para interactuar con la base de datos.
     */
    private ServicioPersistencia servPersistencia;

    /**
     * Instancia única del adaptador (Singleton).
     */
    private static AdaptadorMensaje unicaInstancia = null;

    /**
     * Constructor privado que inicializa el adaptador con el servicio de persistencia.
     */
    public AdaptadorMensaje() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Devuelve la instancia única del adaptador de mensajes.
     *
     * @return instancia única de {@link AdaptadorMensaje}
     */
    public static AdaptadorMensaje getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new AdaptadorMensaje();
        return unicaInstancia;
    }
	
    /**
     * Registra un objeto {@link Mensaje} en la capa de persistencia.
     * Si el mensaje ya está registrado (según su código), no realiza ninguna acción.
     * Se crea una nueva entidad con las propiedades del mensaje y se agrega al {@link PoolDAO}.
     *
     * @param mensaje el mensaje a registrar
     */
	public void registrarMensaje(Mensaje mensaje) {
		
		// Se comprueba que no está registrada la entidad que corresponde al código del objeto
		Optional<Entidad> eMensaje = Optional.ofNullable(servPersistencia.recuperarEntidad(mensaje.getCodigo()));
		if (eMensaje.isPresent()) {
			return;
		}

		// Se registran sus objetos referenciados
		

		// Se crea la entidad
		eMensaje = Optional.of(new Entidad());
		eMensaje.get().setNombre("mensaje");

		// Se crean y añaden las propiedades a la entidad creada
		eMensaje.get().setPropiedades(
							new ArrayList<Propiedad>(Arrays.asList(
					        new Propiedad(TEXTO, mensaje.getTexto()),
					        new Propiedad(EMOTICONO, Integer.toString(mensaje.getEmoticono())),
					        new Propiedad(TIPO, String.valueOf(mensaje.getTipo())),
					        new Propiedad(FECHA_HORA_ENVIO, String.valueOf(mensaje.getFechaHoraEnvio())))));

		// Se registra la entidad y se asocia id al objeto almacenado
		eMensaje = Optional.ofNullable(servPersistencia.registrarEntidad(eMensaje.get()));		
		mensaje.setCodigo(eMensaje.get().getId());

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(mensaje.getCodigo(), mensaje);
	}

	/**
     * Recupera un objeto {@link Mensaje} desde la capa de persistencia usando su código.
     * Si el objeto ya existe en el {@link PoolDAO}, lo retorna directamente.
     * Si no, reconstruye el objeto con sus propiedades y lo añade al pool.
     *
     * @param codigo identificador único del mensaje
     * @return el mensaje recuperado o reconstruido
     */
	public Mensaje recuperarMensaje(int codigo) {
		// Si el objeto está en el pool se retorna
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (Mensaje) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		// Si no lo está se recupera entidad y las propiedades de campos de tipo primitivo
		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
		
		//int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
		String emoticonoStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO);
		int emoticono = (emoticonoStr != null && !emoticonoStr.isEmpty()) ? Integer.parseInt(emoticonoStr) : 0;

		//TipoMensaje tipo = TipoMensaje.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO));
		String tipoStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO);
		TipoMensaje tipo = (tipoStr != null) ? TipoMensaje.valueOf(tipoStr) : TipoMensaje.RECIBIDO; // TODO NO ES RECIBIDO

		//LocalDateTime fechaHoraEnvio = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, FECHA_HORA_ENVIO));
		String fechaStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, FECHA_HORA_ENVIO);
		// TODO REVISAR ESTO
		LocalDateTime fechaHoraEnvio = (fechaStr != null && !fechaStr.isEmpty())
		    ? LocalDateTime.parse(fechaStr)
		    : LocalDateTime.now();
		
		
		// Se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
		Mensaje mensaje = new Mensaje(texto, emoticono, tipo);
		mensaje.setCodigo(codigo);

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(codigo, mensaje);

		// Se recuperan los objetos referenciados y se actualiza el objeto
		
		
		// Se retorna el objeto
		return mensaje;
		
	}
	
	/**
     * Recupera todos los objetos {@link Mensaje} almacenados en la base de datos.
     * Cada entidad se reconstruye utilizando {@code recuperarMensaje}.
     *
     * @return lista de todos los mensajes registrados
     */
    public List<Mensaje> recuperarTodosLosMensajes() {
        return servPersistencia.recuperarEntidades("mensaje").stream()
                .map(entidad -> recuperarMensaje(entidad.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Elimina un mensaje completamente del sistema de persistencia.
     * También lo elimina del {@link PoolDAO} si está presente.
     *
     * @param mensaje el mensaje a eliminar
     */
	public void borrarMensaje(Mensaje mensaje) {
		// Se recupera entidad
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());

		// Se elimina la entidad
		servPersistencia.borrarEntidad(eMensaje);

		// Si esta en el pool de objetos se elimina
		if (PoolDAO.getUnicaInstancia().contains(mensaje.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObject(mensaje.getCodigo());
		}
	}
}