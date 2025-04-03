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

public class AdaptadorGrupo implements IAdaptadorGrupoDAO {

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

	}

	public Grupo recuperarGrupo(int codigo) {
		return null;
		
	}
	
	public List<Grupo> recuperarTodosLosContactosIndividuales() {
		return servPersistencia.recuperarEntidades("grupo").stream()
				.map(entidad -> recuperarGrupo(entidad.getId()))
				.collect(Collectors.toList());
	}

	public void modificarGrupo(Grupo grupo) {

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
	
	private String getCodigosContactosIndiv(List<Contacto> contactos) {
		return contactos.stream()
				.map(c -> String.valueOf(c.getCodigo())) // Convertimos el código a String
				.collect(Collectors.joining(" ")); // Unimos los códigos con un espacio entre ellos, eficiente y buena
													// práctica en rendimiento
	}
}
