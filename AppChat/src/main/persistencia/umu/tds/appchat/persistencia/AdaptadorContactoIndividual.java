package umu.tds.appchat.persistencia;

public class AdaptadorContactoIndividual implements IAdaptadorContactoIndividualDAO {
	
	private static AdaptadorContactoIndividual unicaInstancia = null;
	
	public static AdaptadorContactoIndividual getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoIndividual();
		return unicaInstancia;
	}
}
