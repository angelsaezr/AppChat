package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Mensaje;

public interface IAdaptadorMensajeDAO {
	public void registrarMensaje(Mensaje mensaje);
	public Mensaje recuperarMensaje(int codigo);
	public List<Mensaje> recuperarTodosLosMensajes();
	public void modificarMensaje(Mensaje mensaje);
	public void borrarMensaje(Mensaje mensaje);
}
