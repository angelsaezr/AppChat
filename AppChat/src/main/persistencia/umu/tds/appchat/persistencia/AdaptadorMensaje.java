package umu.tds.appchat.persistencia;

public class AdaptadorMensaje implements IAdaptadorMensajeDAO {

	private static AdaptadorMensaje unicaInstancia = null;
	
	public static AdaptadorMensaje getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorMensaje();
		return unicaInstancia;
	}
}
