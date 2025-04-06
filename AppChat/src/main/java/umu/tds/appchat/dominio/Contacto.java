package umu.tds.appchat.dominio;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase contacto. Un contacto puede ser individual o un grupo.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
/**
 * Clase abstracta que representa un contacto en la aplicación.
 * Puede ser un contacto individual o un grupo, y almacena mensajes asociados.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public abstract class Contacto {

    /**
     * Nombre del contacto.
     */
    private String nombre;

    /**
     * Lista de mensajes asociados al contacto.
     */
    private List<Mensaje> mensajes;

    /**
     * Código identificador del contacto.
     */
    private int codigo;

    /**
     * Constructor que inicializa un contacto con su nombre.
     *
     * @param nombre nombre del contacto
     */
    public Contacto(String nombre) {
        this.nombre = nombre;
        this.mensajes = new LinkedList<Mensaje>();
        this.codigo = 0;
    }

    /**
     * Obtiene el nombre del contacto.
     *
     * @return nombre del contacto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del contacto.
     *
     * @param nombre nuevo nombre del contacto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve una nueva lista con los mensajes asociados al contacto.
     *
     * @return lista de mensajes
     */
    public List<Mensaje> getMensajes() {
        return new LinkedList<Mensaje>(mensajes);
    }

    /**
     * Obtiene el código identificador del contacto.
     *
     * @return código del contacto
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código identificador del contacto.
     *
     * @param codigo nuevo código del contacto
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Añade un mensaje a la lista de mensajes del contacto.
     *
     * @param mensaje mensaje a añadir
     * @return true si se añadió correctamente, false en caso contrario
     */
    public boolean addMensaje(Mensaje mensaje) {
        return mensajes.add(mensaje);
    }

    /**
     * Devuelve la imagen asociada al contacto.
     *
     * @return ruta o identificador de la imagen del contacto
     */
    public abstract String getImagen();
}