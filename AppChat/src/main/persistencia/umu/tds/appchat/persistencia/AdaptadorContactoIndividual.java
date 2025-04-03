package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

public class AdaptadorContactoIndividual implements IAdaptadorContactoIndividualDAO {

	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	private static final String USUARIO = "usuario";

	private ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividual unicaInstancia = null;

	public AdaptadorContactoIndividual() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static AdaptadorContactoIndividual getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoIndividual();
		return unicaInstancia;
	}

	public void registrarContactoIndividual(ContactoIndividual contacto) {
		// Se comprueba que no está registrada la entidad que corresponde al código del objeto
		Optional<Entidad> eContactoIndividual = Optional.ofNullable(servPersistencia.recuperarEntidad(contacto.getCodigo()));
		if (eContactoIndividual.isPresent()) {
			return;
		}

		// Se registran sus objetos referenciados
		/*for (Mensaje m: contacto.getMensajes()) {
			AdaptadorMensaje.getUnicaInstancia().registrarMensaje(m);
		}*/
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

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(contacto.getCodigo(), contacto);
	}

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

		// Se retorna el objeto
		return contactoIndividual;
	}	
	
	public List<ContactoIndividual> recuperarTodosLosContactosIndividuales() {
		return servPersistencia.recuperarEntidades("contactoIndividual").stream()
				.map(entidad -> recuperarContactoIndividual(entidad.getId()))
				.collect(Collectors.toList());
	}

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

	public void borrarContactoIndividual(ContactoIndividual contacto) {
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
	
	private List<Mensaje> getMensajes(String codigos) {
		return Arrays.stream(codigos.split(" "))
				.filter(codigo -> !codigo.isEmpty())
				.map(Integer::parseInt)
				.map(codigo -> AdaptadorMensaje.getUnicaInstancia().recuperarMensaje(codigo))
				.collect(Collectors.toList());
	}
	
	private String getCodigos(List<Mensaje> mensajes) {
		return mensajes.stream().map(m -> String.valueOf(m.getCodigo())) // Convertimos el código a String
				.collect(Collectors.joining(" ")); // Unimos los códigos con un espacio entre ellos, eficiente y buena
													// práctica en rendimiento
	}
}
