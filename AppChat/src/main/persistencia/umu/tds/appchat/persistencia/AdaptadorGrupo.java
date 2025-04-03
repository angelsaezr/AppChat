package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

public class AdaptadorGrupo implements IAdaptadorGrupoDAO {

	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	private static final String IMAGEN = "imagen";
	private static final String MIEMBROS = "miembros";
	
	private ServicioPersistencia servPersistencia;
	private static AdaptadorGrupo unicaInstancia = null;
	
	public AdaptadorGrupo() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public static AdaptadorGrupo getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorGrupo();
		return unicaInstancia;
	}
	
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
	
	public List<Grupo> recuperarTodosLosContactosIndividuales() {
		return servPersistencia.recuperarEntidades("grupo").stream()
				.map(entidad -> recuperarGrupo(entidad.getId()))
				.collect(Collectors.toList());
	}

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

	public void borrarGrupo(Grupo grupo) {
		
	}
	
	
	private List<Mensaje> getMensajes(String codigos) {
		return Arrays.stream(codigos.split(" "))
				.filter(codigo -> !codigo.isEmpty())
				.map(Integer::parseInt)
				.map(codigo -> AdaptadorMensaje.getUnicaInstancia().recuperarMensaje(codigo))
				.collect(Collectors.toList());
	}
	
	private String getCodigosMensajes(List<Mensaje> mensajes) {
		return mensajes.stream()
				.map(m -> String.valueOf(m.getCodigo())) // Convertimos el código a String
				.collect(Collectors.joining(" ")); // Unimos los códigos con un espacio entre ellos, eficiente y buena
													// práctica en rendimiento
	}
	
	private List<ContactoIndividual> getMiembros(String codigos) {
		return Arrays.stream(codigos.split(" "))
				.filter(codigo -> !codigo.isEmpty())
				.map(Integer::parseInt)
				.map(codigo -> AdaptadorContactoIndividual.getUnicaInstancia().recuperarContactoIndividual(codigo))
				.collect(Collectors.toList());
	}
	
	private String getCodigosContactosIndiv(List<ContactoIndividual> contactosIndiv) {
		return contactosIndiv.stream()
				.map(c -> String.valueOf(c.getCodigo())) // Convertimos el código a String
				.collect(Collectors.joining(" ")); // Unimos los códigos con un espacio entre ellos, eficiente y buena
													// práctica en rendimiento
	}
}
