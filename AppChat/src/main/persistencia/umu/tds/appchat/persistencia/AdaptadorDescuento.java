package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;

import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.time.LocalDate;
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
 * @author Ángel
 * @author Francisco Javier
 */
public class AdaptadorDescuento implements IAdaptadorDescuentoDAO {
	/**
     * Propiedad correspondiente al tipo del mensaje (enviado o recibido).
     */
    private static final String TIPO = "tipo";
    
    /**
     * Propiedad correspondiente a la fecha de inicio del descuento.
     */
    private static final String FECHA_INICIO = "fechaInicio";
    
    /**
     * Propiedad correspondiente a la fecha de fin del descuento.
     */
    private static final String FECHA_FIN = "fechaFin";
    
    /**
     * Propiedad correspondiente al umbral de mensajes.
     */
    private static final String UMBRAL_MENSAJES = "umbralMensajes";
    
    /**
     * Propiedad correspondiente al porcentaje de descuento.
     */
    private static final String PORCENTAJE_DESCUENTO = "porcentajeDescuento";
	
    /**
     * Servicio de persistencia utilizado para interactuar con la base de datos.
     */
    private ServicioPersistencia servPersistencia;

    /**
     * Instancia única del adaptador (Singleton).
     */
    private static AdaptadorDescuento unicaInstancia = null;

    /**
     * Constructor privado que inicializa el adaptador con el servicio de persistencia.
     */
    public AdaptadorDescuento() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Devuelve la instancia única del adaptador de descuentos.
     *
     * @return instancia única de {@link AdaptadorDescuento}
     */
    public static AdaptadorDescuento getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new AdaptadorDescuento();
        return unicaInstancia;
    }
	
    /**
     * Registra un objeto {@link Descuento} en la capa de persistencia.
     * Si el descuento ya está registrado (según su código), no realiza ninguna acción.
     * Se crea una nueva entidad con las propiedades del descuento y se agrega al {@link PoolDAO}.
     *
     * @param descuento el descuento a registrar
     */
	public void registrarDescuento(Descuento descuento) {
		// Si ya tiene código, no registrar de nuevo
	    if (descuento.getCodigo() != 0) {
	        return;
	    }
	    
		// Se comprueba que no está registrada la entidad que corresponde al código del objeto
		Optional<Entidad> eDescuento = Optional.ofNullable(servPersistencia.recuperarEntidad(descuento.getCodigo()));
		if (eDescuento.isPresent()) {
			return;
		}

		// Se crea la entidad
		eDescuento = Optional.of(new Entidad());
		eDescuento.get().setNombre("descuento");

		// Se crean y añaden las propiedades a la entidad creada
		if(descuento instanceof DescuentoPorFecha) {
			DescuentoPorFecha dF = (DescuentoPorFecha) descuento;
			eDescuento.get().setPropiedades(
					new ArrayList<Propiedad>(Arrays.asList(
					new Propiedad(TIPO, "descuentoPorFecha"),
			        new Propiedad(FECHA_INICIO, String.valueOf(dF.getFechaInicio())),
			        new Propiedad(FECHA_FIN, String.valueOf(dF.getFechaFin())),
			        new Propiedad(PORCENTAJE_DESCUENTO, String.valueOf(dF.getPorcentajeDescuento())))));
		} else if(descuento instanceof DescuentoPorMensaje) {
			DescuentoPorMensaje dM = (DescuentoPorMensaje) descuento;
			eDescuento.get().setPropiedades(
					new ArrayList<Propiedad>(Arrays.asList(
					new Propiedad(TIPO, "descuentoPorMensaje"),
			        new Propiedad(UMBRAL_MENSAJES, String.valueOf(dM.getUmbralMensajes())),
			        new Propiedad(PORCENTAJE_DESCUENTO, String.valueOf(dM.getPorcentajeDescuento())))));
		}

		// Se registra la entidad y se asocia id al objeto almacenado
		eDescuento = Optional.ofNullable(servPersistencia.registrarEntidad(eDescuento.get()));		
		descuento.setCodigo(eDescuento.get().getId());
		
		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(descuento.getCodigo(), descuento);
	}

	/**
     * Recupera un objeto {@link Descuento} desde la capa de persistencia usando su código.
     * Si el objeto ya existe en el {@link PoolDAO}, lo retorna directamente.
     * Si no, reconstruye el objeto con sus propiedades y lo añade al pool.
     *
     * @param codigo identificador único del descuento
     * @return el descuento recuperado o reconstruido
     */
	public Descuento recuperarDescuento(int codigo) {
		Descuento descuento;
		
		// Si el objeto está en el pool se retorna
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (Descuento) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		// Si no lo está se recupera entidad y las propiedades de campos de tipo primitivo
		Entidad eDescuento = servPersistencia.recuperarEntidad(codigo);
		String tipo = servPersistencia.recuperarPropiedadEntidad(eDescuento, TIPO);
		if (tipo.equals("descuentoPorFecha")) {
			String fechaInicioStr = servPersistencia.recuperarPropiedadEntidad(eDescuento, FECHA_INICIO);
			LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
			String fechaFinStr = servPersistencia.recuperarPropiedadEntidad(eDescuento, FECHA_INICIO);
			LocalDate fechaFin = LocalDate.parse(fechaFinStr);
			String porcentajeDescuentoStr = servPersistencia.recuperarPropiedadEntidad(eDescuento, PORCENTAJE_DESCUENTO);
			double porcentajeDescuento = (porcentajeDescuentoStr != null && !porcentajeDescuentoStr.isEmpty()) ? Double.parseDouble(porcentajeDescuentoStr) : 0;
			
			// Se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
			descuento = new DescuentoPorFecha(fechaInicio, fechaFin, porcentajeDescuento);
			descuento.setCodigo(codigo);
		} else if (tipo.equals("descuentoPorMensaje")) {
			String umbralMensajesStr = servPersistencia.recuperarPropiedadEntidad(eDescuento, UMBRAL_MENSAJES);
			int umbralMensajes = (umbralMensajesStr != null && !umbralMensajesStr.isEmpty()) ? Integer.parseInt(umbralMensajesStr) : 0;
			String porcentajeDescuentoStr = servPersistencia.recuperarPropiedadEntidad(eDescuento, PORCENTAJE_DESCUENTO);
			double porcentajeDescuento = (porcentajeDescuentoStr != null && !porcentajeDescuentoStr.isEmpty()) ? Double.parseDouble(porcentajeDescuentoStr) : 0;
			
			// Se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
			descuento = new DescuentoPorMensaje(umbralMensajes, porcentajeDescuento);
			descuento.setCodigo(codigo);
		} else {
			return null;
		}

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(codigo, descuento);

		// Se recuperan los objetos referenciados y se actualiza el objeto
		
		// Se retorna el objeto
		return descuento;
		
	}
	
	/**
     * Recupera todos los objetos {@link Descuento} almacenados en la base de datos.
     * Cada entidad se reconstruye utilizando {@code recuperarDescuento}.
     *
     * @return lista de todos los descuentos registrados
     */
    public List<Descuento> recuperarTodosLosDescuentos() {
        return servPersistencia.recuperarEntidades("descuento").stream()
                .map(entidad -> recuperarDescuento(entidad.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Elimina un descuento completamente del sistema de persistencia.
     * También lo elimina del {@link PoolDAO} si está presente.
     *
     * @param descuento el descuento a eliminar
     */
	public void borrarDescuento(Descuento descuento) {
		// Se recupera entidad
		Entidad eDescuento = servPersistencia.recuperarEntidad(descuento.getCodigo());

		// Se elimina la entidad
		servPersistencia.borrarEntidad(eDescuento);

		// Si esta en el pool de objetos se elimina
		if (PoolDAO.getUnicaInstancia().contains(descuento.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObject(descuento.getCodigo());
		}
	}
}