package umu.tds.appchat.dominio;

/**
 * Representa un contacto individual en la aplicación.
 * Está asociado a un objeto {@link Usuario} y hereda de {@link Contacto}.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class ContactoIndividual extends Contacto {

    /**
     * Usuario asociado a este contacto individual.
     */
    private Usuario usuario;

    /**
     * Crea un nuevo contacto individual con nombre y usuario asociado.
     *
     * @param nombre nombre del contacto
     * @param usuario objeto Usuario vinculado a este contacto
     */
    public ContactoIndividual(String nombre, Usuario usuario) {
        super(nombre);
        this.usuario = usuario;
    }

    /**
     * Obtiene el usuario asociado al contacto.
     *
     * @return usuario vinculado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Asigna un nuevo usuario al contacto.
     *
     * @param usuario nuevo usuario a asociar
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve la imagen del usuario asociado.
     *
     * @return imagen de perfil del usuario
     */
    public String getImagen() {
        return usuario.getImagen();
    }

    /**
     * Devuelve el número de móvil del usuario asociado.
     *
     * @return número de móvil del usuario
     */
    public String getMovil() {
        return usuario.getMovil();
    }

    /**
     * Devuelve el saludo personalizado del usuario asociado.
     *
     * @return saludo del usuario
     */
    public String getSaludo() {
        return usuario.getSaludo();
    }
}
