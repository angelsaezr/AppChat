package umu.tds.appchat.persistencia;

public class AdaptadorGrupo implements IAdaptadorGrupoDAO {

	private static AdaptadorGrupo unicaInstancia = null;
	
	public static AdaptadorGrupo getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorGrupo();
		return unicaInstancia;
	}
}
