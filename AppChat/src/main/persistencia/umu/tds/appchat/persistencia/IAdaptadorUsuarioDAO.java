package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Usuario;

public interface IAdaptadorUsuarioDAO {
	public void registrarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int codigo);
	public List<Usuario> recuperarTodosLosUsuarios();
	public void modificarUsuario(Usuario usuario);
	public void borrarUsuario(Usuario usuario);
}
