package umu.tds.appchat.persistencia;

public abstract class FactoriaDAO {
	private static FactoriaDAO INSTANCE;

	public static final String DAO_TDS = "umu.tds.appchat.persistencia.TDSFactoriaDAO";

	public static FactoriaDAO getUnicaInstancia(String nombre) throws DAOException {
		if (INSTANCE == null)
			try {
				INSTANCE = (FactoriaDAO) Class.forName(nombre).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		return INSTANCE;
	}

	public static FactoriaDAO getUnicaInstancia() throws DAOException {
		return getUnicaInstancia(FactoriaDAO.DAO_TDS);
	}

	protected FactoriaDAO() {}
	
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	public abstract IAdaptadorMensajeDAO getMensajeDAO();
	public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();
	public abstract IAdaptadorGrupoDAO getGrupoDAO();
}
