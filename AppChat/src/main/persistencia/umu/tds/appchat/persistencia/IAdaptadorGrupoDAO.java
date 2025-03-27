package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Grupo;

public interface IAdaptadorGrupoDAO {
	public void registrarGrupo(Grupo grupo);
	public Grupo recuperarGrupo(int codigo);
	public List<Grupo> recuperarTodosLosContactosIndividuales();
	public void modificarGrupo(Grupo grupo);
	public void borrarGrupo(Grupo grupo);
}
