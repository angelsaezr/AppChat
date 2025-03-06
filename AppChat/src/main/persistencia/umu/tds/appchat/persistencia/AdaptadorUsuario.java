package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.Usuario;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	
	private ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	public AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public void registrarUsuario(Usuario usuario) {

	}
	
	public static AdaptadorUsuario getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuario();
		return unicaInstancia;
	}
	
	
}
