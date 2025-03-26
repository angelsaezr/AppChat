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

public class AdaptadorMensaje implements IAdaptadorMensajeDAO {

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

	}

	public Mensaje recuperarMensaje(int codigo) {
		return null;
		
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
