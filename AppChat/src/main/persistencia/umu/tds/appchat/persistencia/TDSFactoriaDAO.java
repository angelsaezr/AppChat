package umu.tds.appchat.persistencia;

/**
 * Implementación concreta de la {@link FactoriaDAO} que utiliza adaptadores
 * basados en la tecnología TDS (Tiny Data Store) para la persistencia.
 * 
 * Proporciona instancias únicas de cada adaptador DAO mediante el patrón Singleton.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class TDSFactoriaDAO extends FactoriaDAO {

    /**
     * Constructor por defecto.
     */
    public TDSFactoriaDAO() {}

    /**
     * Devuelve la instancia única del adaptador DAO de usuarios.
     *
     * @return adaptador de usuarios
     */
    @Override
    public IAdaptadorUsuarioDAO getUsuarioDAO() {
        return AdaptadorUsuario.getUnicaInstancia();
    }

    /**
     * Devuelve la instancia única del adaptador DAO de contactos individuales.
     *
     * @return adaptador de contactos individuales
     */
    @Override
    public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
        return AdaptadorContactoIndividual.getUnicaInstancia();
    }

    /**
     * Devuelve la instancia única del adaptador DAO de grupos.
     *
     * @return adaptador de grupos
     */
    @Override
    public IAdaptadorGrupoDAO getGrupoDAO() {
        return AdaptadorGrupo.getUnicaInstancia();
    }

    /**
     * Devuelve la instancia única del adaptador DAO de mensajes.
     *
     * @return adaptador de mensajes
     */
    @Override
    public IAdaptadorMensajeDAO getMensajeDAO() {
        return AdaptadorMensaje.getUnicaInstancia();
    }
}