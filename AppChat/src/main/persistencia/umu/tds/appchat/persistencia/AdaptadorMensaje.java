package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import beans.Entidad;
import beans.Propiedad;
import beans.Mensaje;

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

	public void modificarMensaje() {

	}

	public void borrarMensaje(Mensaje mensaje) {
		
	}
}
