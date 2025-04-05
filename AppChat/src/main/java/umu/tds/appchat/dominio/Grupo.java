package umu.tds.appchat.dominio;

import java.util.List;

/**
 * Representa un grupo de contactos en la aplicación.
 * Hereda de {@link Contacto} y contiene una lista de miembros individuales.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Grupo extends Contacto {

    /**
     * Ruta de la imagen del grupo.
     */
    private String urlImagen;

    /**
     * Lista de miembros que pertenecen al grupo.
     */
    private List<ContactoIndividual> miembros;

    /**
     * Constructor que crea un grupo con nombre, miembros y una imagen personalizada.
     *
     * @param nombreGrupo nombre del grupo
     * @param miembros lista de miembros del grupo
     * @param imagenGrupo ruta de la imagen del grupo
     */
    public Grupo(String nombreGrupo, List<ContactoIndividual> miembros, String imagenGrupo) {
        super(nombreGrupo);
        this.miembros = miembros;
        if(imagenGrupo != "")
            urlImagen = imagenGrupo;
        else
            urlImagen = "src/main/resources/profile1.jpg";
    }

    /**
     * Constructor que crea un grupo con nombre y miembros, usando la imagen por defecto.
     *
     * @param nombreGrupo nombre del grupo
     * @param miembros lista de miembros del grupo
     */
    public Grupo(String nombreGrupo, List<ContactoIndividual> miembros) {
        this(nombreGrupo, miembros, "");
    }

    /**
     * Devuelve la lista de miembros del grupo.
     *
     * @return lista de contactos individuales que forman parte del grupo
     */
    public List<ContactoIndividual> getMiembros() {
        return miembros;
    }

    /**
     * Devuelve la imagen del grupo.
     *
     * @return ruta de la imagen del grupo
     */
    public String getImagen() {
        return urlImagen;
    }

    /**
     * Establece una nueva imagen para el grupo.
     *
     * @param imagen nueva ruta de imagen del grupo
     */
    public void setImagen(String imagen) {
        urlImagen = imagen;
    }

    /**
     * Añade un nuevo miembro al grupo si aún no pertenece.
     *
     * @param miembro contacto individual a añadir
     * @return true si fue añadido, false si ya era miembro
     */
    public boolean addMiembro(ContactoIndividual miembro) {
        if (this.miembros.contains(miembro)) {
            return false;
        }
        this.miembros.add(miembro);
        return true;
    }

    /**
     * Elimina un miembro del grupo si pertenece a él.
     *
     * @param miembro contacto individual a eliminar
     * @return true si fue eliminado, false si no estaba en el grupo
     */
    public boolean removeMiembro(ContactoIndividual miembro) {
        if (this.miembros.contains(miembro)) {
            this.miembros.remove(miembro);
            return true;
        }
        return false;
    }

    /**
     * Elimina todos los miembros del grupo.
     *
     * @return true si se eliminaron, false si la lista ya estaba vacía
     */
    public boolean removeAllMiembros() {
        if (this.miembros != null && !this.miembros.isEmpty()) {
            this.miembros.clear();
            return true;
        }
        return false;
    }
}