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

	public void modificarGrupo() {

	}

	public void borrarGrupo(Grupo grupo) {
		
	}
}
