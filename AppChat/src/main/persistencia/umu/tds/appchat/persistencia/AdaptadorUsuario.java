package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {

	private static final String NOMBRE = "nombre";
	private static final String MOVIL = "movil";
	private static final String CONTRASEÑA = "contraseña";
	private static final String EMAIL = "email";
	private static final String IMAGEN = "imagen";
	private static final String IS_PREMIUM = "isPremium";
	private static final String SALUDO = "saludo";
	private static final String FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String DESCUENTO = "descuento";
	private static final String CONTACTOS_INDIV = "contactosIndv";
	private static final String GRUPOS = "grupos";

	private ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	public AdaptadorUsuario() {
		this.servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	// Singleton
	public static AdaptadorUsuario getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuario();
		return unicaInstancia;
	}

	public void registrarUsuario(Usuario usuario) {
		// Se comprueba que no está registrada la entidad que corresponde al código del objeto

		Optional<Entidad> eUsuario = Optional.ofNullable(servPersistencia.recuperarEntidad(usuario.getCodigo()));
		if (eUsuario.isPresent()) {
			return;
		}

		// Se registran sus objetos referenciados
		for (Contacto contacto : usuario.getContactos()) {
			if (contacto instanceof ContactoIndividual) {
				AdaptadorContactoIndividual.getUnicaInstancia().registrarContactoIndividual((ContactoIndividual) contacto);
			} else if (contacto instanceof Grupo) {
				AdaptadorGrupo.getUnicaInstancia().registrarGrupo((Grupo) contacto);
			}
		}

		// Se crea la entidad
		eUsuario = Optional.of(new Entidad());
		eUsuario.get().setNombre("usuario");

		// Cogemos la lista de contactos y grupos
		List<Contacto> contactosIndiv = usuario.getContactos().stream()
				.filter(c -> c instanceof ContactoIndividual)
				.collect(Collectors.toList());
		List<Contacto> grupos = usuario.getContactos().stream()
				.filter(c -> c instanceof Grupo)
				.collect(Collectors.toList());

		// Se crean y añaden las propiedades a la entidad creada
		eUsuario.get().setPropiedades(new ArrayList<>(Arrays.asList(
						new Propiedad(NOMBRE, usuario.getNombre()),
						new Propiedad(MOVIL, usuario.getMovil()),
						new Propiedad(CONTRASEÑA, usuario.getContraseña()),
						new Propiedad(EMAIL, usuario.getEmail()),
						new Propiedad(IMAGEN, usuario.getImagen()),
						new Propiedad(IS_PREMIUM, String.valueOf(usuario.isPremium())),
						new Propiedad(SALUDO, usuario.getSaludo()),
						new Propiedad(FECHA_NACIMIENTO,usuario.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
						/*new Propiedad(DESCUENTO, String.valueOf(usuario.getDescuento())),TODO	*/
						new Propiedad(CONTACTOS_INDIV, getCodigos(contactosIndiv)),
						new Propiedad(GRUPOS, getCodigos(grupos)))));

		// Se registra la entidad y se asocia id al objeto almacenado
		eUsuario = Optional.ofNullable(servPersistencia.registrarEntidad(eUsuario.get()));
		usuario.setCodigo(eUsuario.get().getId());

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(usuario.getCodigo(), usuario);
	}

	public Usuario recuperarUsuario(int codigo) {
		// Si el objeto está en el pool se retorna
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		// Si no lo está se recupera entidad y las propiedades de campos de tipo primitivo
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String movil = servPersistencia.recuperarPropiedadEntidad(eUsuario, MOVIL);
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTRASEÑA);
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
		String imagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, IMAGEN);
		boolean isPremium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, IS_PREMIUM));
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, SALUDO);
		LocalDate fechaNacimiento = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// String descuento = servPersistencia.recuperarPropiedadEntidad(eUsuario, DESCUENTO);

		// Se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
		Usuario usuario = new Usuario(nombre, movil, contraseña, imagen, saludo, email, fechaNacimiento);
		usuario.setPremium(isPremium);
		//usuario.setDescuento(null); // TODO
		usuario.setCodigo(codigo);
		
		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(codigo, usuario);

		// Se recuperan los objetos referenciados y se actualiza el objeto
		List<Contacto> contactos = getContactosIndividuales(servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTACTOS_INDIV));
		List<Contacto> grupos = getGrupos(servPersistencia.recuperarPropiedadEntidad(eUsuario, GRUPOS));
		contactos.addAll(grupos); // Se añade los grupos a la lista de contactos individuales para agruparlos todos
		usuario.setContactos(contactos);
		// Se retorna el objeto
		return usuario;
	}

	public List<Usuario> recuperarTodosLosUsuarios() {
		return servPersistencia.recuperarEntidades("usuario").stream()
				.map(entidad -> recuperarUsuario(entidad.getId()))
				.collect(Collectors.toList());
	}

	public void modificarUsuario(Usuario usuario) {
		//Se recupera entidad
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
				
		List<Contacto> contactosIndiv = usuario.getContactos().stream().filter(c -> c instanceof ContactoIndividual).collect(Collectors.toList());
		List<Contacto> grupos = usuario.getContactos().stream().filter(c -> c instanceof Grupo).collect(Collectors.toList());
				
		//Se recorren sus propiedades y se actualiza su valor
		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(usuario.getNombre());
			} else if (prop.getNombre().equals(MOVIL)) {
				prop.setValor(usuario.getMovil());
			} else if (prop.getNombre().equals(CONTRASEÑA)) {
				prop.setValor(usuario.getContraseña());
			} else if (prop.getNombre().equals(EMAIL)) {
				prop.setValor(usuario.getEmail());
			} else if (prop.getNombre().equals(IMAGEN)) {
				prop.setValor(usuario.getImagen());
			} else if (prop.getNombre().equals(IS_PREMIUM)) {
				prop.setValor(String.valueOf(usuario.isPremium()));
			} else if (prop.getNombre().equals(SALUDO)) {
				prop.setValor(usuario.getSaludo());
			} else if (prop.getNombre().equals(FECHA_NACIMIENTO)) {
				prop.setValor(usuario.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				// TODO DESCUENTO
			} else if (prop.getNombre().equals(CONTACTOS_INDIV)) {
				prop.setValor(getCodigos(contactosIndiv));
			} else if (prop.getNombre().equals(GRUPOS)) {
				prop.setValor(getCodigos(grupos));
			}
			
			servPersistencia.modificarEntidad(eUsuario);
		}
	}

	// TODO ES POSIBLE ELIMINAR UN USUARIO EN LA APP? CREO QUE NO
	public void borrarUsuario(Usuario usuario) {
		// Se recupera entidad
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		// Se eliminan sus entidades agregadas
		for (Contacto contacto : usuario.getContactos()) {
			if (contacto instanceof ContactoIndividual) {
				AdaptadorContactoIndividual.getUnicaInstancia().borrarContactoIndividual((ContactoIndividual) contacto);
			} else {
				AdaptadorGrupo.getUnicaInstancia().borrarGrupo((Grupo) contacto);
			}
		}

		// Se elimina la entidad
		servPersistencia.borrarEntidad(eUsuario);

		// Si esta en el pool de objetos se elimina
		if (PoolDAO.getUnicaInstancia().contains(usuario.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObject(usuario.getCodigo());
		}
	}

	private List<Contacto> getContactosIndividuales(String codigos) {
		return Arrays.stream(codigos.split(" ")) // Dividimos la cadena en un array usando el espacio como separador
				.filter(codigo -> !codigo.isEmpty()) // Filtramos códigos vacíos
				.map(Integer::parseInt) // Convertimos los valores de String a Integer
				.map(codigo -> AdaptadorContactoIndividual.getUnicaInstancia().recuperarContactoIndividual(codigo))
				.collect(Collectors.toList());
	}

	private List<Contacto> getGrupos(String codigos) {
		return Arrays.stream(codigos.split(" "))
				.filter(codigo -> !codigo.isEmpty())
				.map(Integer::parseInt)
				.map(codigo -> AdaptadorGrupo.getUnicaInstancia().recuperarGrupo(codigo))
				.collect(Collectors.toList());
	}

	private String getCodigos(List<Contacto> contactos) {
		return contactos.stream()
				.map(c -> String.valueOf(c.getCodigo())) // Convertimos el código a String
				.collect(Collectors.joining(" ")); // Unimos los códigos con un espacio entre ellos, eficiente y buena
													// práctica en rendimiento
	}

}
