package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

/**
 * Adaptador para la persistencia de objetos {@link Grupo} mediante el patrón DAO.
 * Utiliza {@link ServicioPersistencia} para registrar, recuperar y modificar entidades.
 * Aplica el patrón Singleton para garantizar una única instancia.
 * 
 * Gestiona las propiedades persistidas: nombre, mensajes, imagen y miembros del grupo.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class AdaptadorGrupo implements IAdaptadorGrupoDAO {

    /**
     * Nombre de la propiedad "nombre" utilizada en persistencia.
     */
    private static final String NOMBRE = "nombre";

    /**
     * Nombre de la propiedad "mensajes" utilizada en persistencia.
     */
    private static final String MENSAJES = "mensajes";

    /**
     * Nombre de la propiedad "imagen" utilizada en persistencia.
     */
    private static final String IMAGEN = "imagen";

    /**
     * Nombre de la propiedad "miembros" utilizada en persistencia.
     */
    private static final String MIEMBROS = "miembros";

    /**
     * Servicio de persistencia utilizado para las operaciones DAO.
     */
    private ServicioPersistencia servPersistencia;

    /**
     * Instancia única del adaptador (patrón Singleton).
     */
    private static AdaptadorGrupo unicaInstancia = null;

    /**
     * Constructor privado que inicializa el servicio de persistencia desde la factoría.
     */
    public AdaptadorGrupo() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Devuelve la instancia única del adaptador para grupos.
     *
     * @return instancia única de {@link AdaptadorGrupo}
     */
    public static AdaptadorGrupo getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new AdaptadorGrupo();
        return unicaInstancia;
    }
	
    /**
     * Registra un objeto {@link Grupo} en el sistema de persistencia.
     * Si el grupo ya está registrado (según su código), no se realiza ninguna acción.
     * También registra sus miembros y mensajes asociados si aún no están persistidos.
     *
     * @param grupo el grupo que se desea registrar
     */
	public void registrarGrupo(Grupo grupo) {
		// Se comprueba que no está registrada la entidad que corresponde al código del objeto

		Optional<Entidad> eGrupo = Optional.ofNullable(servPersistencia.recuperarEntidad(grupo.getCodigo()));
		if (eGrupo.isPresent()) {
			return;
		}

		// Se registran sus objetos referenciados
		AdaptadorContactoIndividual adaptadorContacto = AdaptadorContactoIndividual.getUnicaInstancia();
		for (ContactoIndividual miembro : grupo.getMiembros()) {
		    adaptadorContacto.registrarContactoIndividual(miembro);
		}

		AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
		for (Mensaje mensaje : grupo.getMensajes()) {
		    adaptadorMensaje.registrarMensaje(mensaje);
		}

		// Se crea la entidad
		eGrupo = Optional.of(new Entidad());
		eGrupo.get().setNombre("grupo");

		// Se crean y añaden las propiedades a la entidad creada
		eGrupo.get().setPropiedades(new ArrayList<>(Arrays.asList(
						new Propiedad(NOMBRE, grupo.getNombre()),
						new Propiedad(MENSAJES, getCodigosMensajes(grupo.getMensajes())),
						new Propiedad(IMAGEN, grupo.getImagen()),
						new Propiedad(MIEMBROS, getCodigosContactosIndiv(grupo.getMiembros())),
						new Propiedad(IMAGEN, grupo.getImagen()))));

		// Se registra la entidad y se asocia id al objeto almacenado
		eGrupo = Optional.ofNullable(servPersistencia.registrarEntidad(eGrupo.get()));
		grupo.setCodigo(eGrupo.get().getId());

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(grupo.getCodigo(), grupo);
	}

	 /**
     * Recupera un grupo desde la persistencia usando su código identificador.
     * Si el objeto ya existe en el {@link PoolDAO}, lo devuelve directamente.
     * 
     * @param codigo código único del grupo
     * @return el objeto {@link Grupo} recuperado
     */
	public Grupo recuperarGrupo(int codigo) {
		// Si el objeto está en el pool se retorna
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (Grupo) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		// Si no lo está se recupera entidad y las propiedades de campos de tipo primitivo
		Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
		String imagen = servPersistencia.recuperarPropiedadEntidad(eGrupo, IMAGEN);

		// Se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
		Grupo grupo = new Grupo(nombre, new LinkedList<ContactoIndividual>(), imagen);
		grupo.setCodigo(codigo);
		
		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(codigo, grupo);

		// Se recuperan los objetos referenciados y se actualiza el objeto
		String mensajes = servPersistencia.recuperarPropiedadEntidad(eGrupo, MENSAJES);
		getMensajes(mensajes).forEach(m -> {
		    grupo.addMensaje(m);
		});
		
		String miembros = servPersistencia.recuperarPropiedadEntidad(eGrupo, MIEMBROS);
		getMiembros(miembros).forEach(m -> {
		    grupo.addMiembro(m);
		});
		
		// Se retorna el objeto
		return grupo;
		
	}
	
	/**
     * Recupera todos los objetos {@link Grupo} almacenados en la base de datos.
     * Utiliza {@code recuperarGrupo} para reconstruir cada grupo desde su entidad.
     *
     * @return lista de todos los grupos registrados
     */
	public List<Grupo> recuperarTodosLosContactosIndividuales() {
		return servPersistencia.recuperarEntidades("grupo").stream()
				.map(entidad -> recuperarGrupo(entidad.getId()))
				.collect(Collectors.toList());
	}

	/**
     * Modifica los datos persistidos de un {@link Grupo}.
     * Actualiza las propiedades de nombre, mensajes, imagen y miembros en la entidad correspondiente.
     *
     * @param grupo el grupo con información actualizada
     */
	public void modificarGrupo(Grupo grupo) {
		//Se recupera entidad
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
				
		//Se recorren sus propiedades y se actualiza su valor
		for (Propiedad prop : eGrupo.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombre());
			} else if (prop.getNombre().equals(MENSAJES)) {
				prop.setValor(getCodigosMensajes(grupo.getMensajes()));
			} else if (prop.getNombre().equals(IMAGEN)) {
				prop.setValor(grupo.getImagen());
			} else if (prop.getNombre().equals(MIEMBROS)) {
				prop.setValor(getCodigosContactosIndiv(grupo.getMiembros()));
			}
			
			servPersistencia.modificarEntidad(eGrupo);
		}
	}

	/**
     * Elimina completamente un grupo del sistema de persistencia.
     * También elimina todos los mensajes asociados al grupo y lo elimina del {@link PoolDAO} si es necesario.
     *
     * @param grupo el grupo a eliminar
     */
	public void borrarGrupo(Grupo grupo) {
		// Se recupera entidad
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());

		// Se eliminan sus entidades agregadas
		for (Mensaje mensaje : grupo.getMensajes()) {
		    AdaptadorMensaje.getUnicaInstancia().borrarMensaje(mensaje);
		}

		// Se elimina la entidad
		servPersistencia.borrarEntidad(eGrupo);

		// Si esta en el pool de objetos se elimina
		if (PoolDAO.getUnicaInstancia().contains(grupo.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObject(grupo.getCodigo());
		}		
	}
	
    /**
     * Recupera una lista de objetos {@link Mensaje} a partir de una cadena con sus códigos separados por espacios.
     *
     * @param codigos cadena con los identificadores de los mensajes
     * @return lista de mensajes correspondientes a los códigos
     */
    private List<Mensaje> getMensajes(String codigos) {
        return Arrays.stream(codigos.split(" "))
                .filter(codigo -> !codigo.isEmpty())
                .map(Integer::parseInt)
                .map(codigo -> AdaptadorMensaje.getUnicaInstancia().recuperarMensaje(codigo))
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de mensajes en una cadena con sus códigos separados por espacios.
     * Este formato se utiliza en la persistencia para representar relaciones.
     *
     * @param mensajes lista de mensajes
     * @return cadena con los códigos separados por espacios
     */
    private String getCodigosMensajes(List<Mensaje> mensajes) {
        return mensajes.stream()
                .map(m -> String.valueOf(m.getCodigo()))
                .collect(Collectors.joining(" "));
    }

    /**
     * Recupera una lista de {@link ContactoIndividual} a partir de una cadena con sus códigos separados por espacios.
     *
     * @param codigos cadena con los identificadores de los contactos individuales
     * @return lista de contactos individuales correspondientes
     */
    private List<ContactoIndividual> getMiembros(String codigos) {
        return Arrays.stream(codigos.split(" "))
                .filter(codigo -> !codigo.isEmpty())
                .map(Integer::parseInt)
                .map(codigo -> AdaptadorContactoIndividual.getUnicaInstancia().recuperarContactoIndividual(codigo))
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de contactos individuales en una cadena con sus códigos separados por espacios.
     *
     * @param contactosIndiv lista de contactos individuales
     * @return cadena con los códigos separados por espacios
     */
    private String getCodigosContactosIndiv(List<ContactoIndividual> contactosIndiv) {
        return contactosIndiv.stream()
                .map(c -> String.valueOf(c.getCodigo()))
                .collect(Collectors.joining(" "));
    }
}