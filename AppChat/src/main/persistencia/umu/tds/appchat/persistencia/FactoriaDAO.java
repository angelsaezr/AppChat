package umu.tds.appchat.persistencia;

/**
 * Fábrica abstracta de objetos DAO (Data Access Object).
 * Utiliza el patrón Factory y Singleton para proporcionar acceso a las implementaciones de persistencia.
 * Esta clase define métodos abstractos para obtener adaptadores DAO específicos.
 * 
 * La implementación concreta se determina dinámicamente a través de reflexión.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public abstract class FactoriaDAO {

    /**
     * Instancia única de la factoría (patrón Singleton).
     */
    private static FactoriaDAO INSTANCE;

    /**
     * Ruta por defecto de la implementación DAO basada en TDS.
     */
    public static final String DAO_TDS = "umu.tds.appchat.persistencia.TDSFactoriaDAO";

    /**
     * Devuelve la instancia única de la factoría, instanciada a partir del nombre de clase indicado.
     * 
     * @param nombre nombre completo de la clase que implementa {@code FactoriaDAO}
     * @return instancia única de {@code FactoriaDAO}
     * @throws DAOException si ocurre un error al crear la instancia
     */
    public static FactoriaDAO getUnicaInstancia(String nombre) throws DAOException {
        if (INSTANCE == null)
            try {
                INSTANCE = (FactoriaDAO) Class.forName(nombre).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new DAOException(e.getMessage());
            }
        return INSTANCE;
    }

    /**
     * Devuelve la instancia única de la factoría utilizando la implementación por defecto.
     * 
     * @return instancia única de {@code FactoriaDAO}
     * @throws DAOException si ocurre un error al crear la instancia
     */
    public static FactoriaDAO getUnicaInstancia() throws DAOException {
        return getUnicaInstancia(FactoriaDAO.DAO_TDS);
    }

    /**
     * Constructor protegido para evitar instanciación externa.
     */
    protected FactoriaDAO() {}

    /**
     * Devuelve el adaptador DAO para usuarios.
     *
     * @return instancia de {@link IAdaptadorUsuarioDAO}
     */
    public abstract IAdaptadorUsuarioDAO getUsuarioDAO();

    /**
     * Devuelve el adaptador DAO para mensajes.
     *
     * @return instancia de {@link IAdaptadorMensajeDAO}
     */
    public abstract IAdaptadorMensajeDAO getMensajeDAO();

    /**
     * Devuelve el adaptador DAO para contactos individuales.
     *
     * @return instancia de {@link IAdaptadorContactoIndividualDAO}
     */
    public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();

    /**
     * Devuelve el adaptador DAO para grupos.
     *
     * @return instancia de {@link IAdaptadorGrupoDAO}
     */
    public abstract IAdaptadorGrupoDAO getGrupoDAO();
    
    /**
     * Devuelve el adaptador DAO para descuentos.
     *
     * @return instancia de {@link IAdaptadorGrupoDAO}
     */
    public abstract IAdaptadorDescuentoDAO getDescuentoDAO();
}