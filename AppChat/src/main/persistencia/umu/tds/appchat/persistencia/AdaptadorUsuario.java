package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.Usuario;

import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import beans.Mensaje;

public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	
	private static final String USUARIO = "Usuario";
	private static final String NOMBRE = "nombre";
	private static final String TELEFONO = "telefono";
	private static final String PASSWORD = "password";

	private ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	public AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static AdaptadorUsuario getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuario();
		return unicaInstancia;
	}
	
	private Entidad usuarioToEntidad(Usuario usuario) {
		
		Entidad eUsuario = null;
		
		return eUsuario;
	}
	
	public void registrarUsuario(Usuario usuario) {
		Entidad usuario1 = new Entidad();
		usuario1.setNombre("Usuario");
		usuario1.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nif", "12345678A"))));
		servPersistencia.registrarEntidad(usuario1);
	}
	
	private Usuario entidadToUsuario(Entidad eUsuario) {

		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, PASSWORD);
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, TELEFONO);
		
		return null;
	}
	
	public Usuario recuperarUsuario(int codigo) {
		if (PoolDAO.getUnicaInstancia().contains(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObject(codigo);
		}

		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

		return entidadToUsuario(eUsuario);
	}
}
