package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

public class AdaptadorMensaje implements IAdaptadorMensajeDAO {
	
	private static final String TEXTO = "texto";
	private static final String EMOTICONO = "emoticono";
	private static final String TIPO = "tipo";
	private static final String FECHA_HORA_ENVIO = "fechaHoraEnvio";

	private ServicioPersistencia servPersistencia;
	private static AdaptadorMensaje unicaInstancia = null;
	
	public AdaptadorMensaje() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public static AdaptadorMensaje getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorMensaje();
		return unicaInstancia;
	}
	
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
		System.out.println("codigo que se guarda en mensaje: " + mensaje.getCodigo());

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(mensaje.getCodigo(), mensaje);
	}

	public Mensaje recuperarMensaje(int codigo) {
		// Si el objeto está en el pool se retorna
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (Mensaje) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		// Si no lo está se recupera entidad y las propiedades de campos de tipo primitivo
		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
		System.out.println("EL TEXTO DEL MENSAJE RECUPERADO ES: " + texto);
		
		//int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
		String emoticonoStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO);
		int emoticono = (emoticonoStr != null && !emoticonoStr.isEmpty()) ? Integer.parseInt(emoticonoStr) : 0;

		//TipoMensaje tipo = TipoMensaje.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO));
		String tipoStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO);
		TipoMensaje tipo = (tipoStr != null) ? TipoMensaje.valueOf(tipoStr) : TipoMensaje.RECIBIDO; // TODO NO ES RECIBIDO

		//LocalDateTime fechaHoraEnvio = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, FECHA_HORA_ENVIO));
		String fechaStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, FECHA_HORA_ENVIO);
		LocalDateTime fechaHoraEnvio = (fechaStr != null && !fechaStr.isEmpty())
		    ? LocalDateTime.parse(fechaStr)
		    : LocalDateTime.now();
		
		
		// Se crea el objeto, se inicializa con propiedades anteriores y se añade al pool si es necesario
		Mensaje mensaje = new Mensaje(texto, emoticono, tipo);
		mensaje.setCodigo(codigo);

		// Se añade al pool
		PoolDAO.getUnicaInstancia().addObject(codigo, mensaje);

		// Se recuperan los objetos referenciados y se actualiza el objeto
		
		System.out.println("El mensaje recuperado essssssssssssss: " + mensaje.getTexto());
		// Se retorna el objeto
		return mensaje;
		
	}
	
	public List<Mensaje> recuperarTodosLosMensajes() {
		return servPersistencia.recuperarEntidades("mensaje").stream()
				.map(entidad -> recuperarMensaje(entidad.getId()))
				.collect(Collectors.toList());
	}

	public void modificarMensaje(Mensaje mensaje) {

	}

	public void borrarMensaje(Mensaje mensaje) {
		
	}
}
