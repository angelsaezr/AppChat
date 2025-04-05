package umu.tds.appchat.dominio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Clase Usuario. Cada usuario tiene su lista de contactos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
/**
 * Representa un usuario dentro de la aplicación.
 * Contiene información personal, estado de cuenta y su lista de contactos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Usuario {

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Número de móvil del usuario.
     */
    private String movil;

    /**
     * Contraseña del usuario.
     */
    private String contraseña;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Ruta de la imagen de perfil del usuario.
     */
    private String imagen;

    /**
     * Indica si el usuario tiene cuenta premium.
     */
    private boolean isPremium;

    /**
     * Saludo personalizado del usuario.
     */
    private String saludo;

    /**
     * Fecha de nacimiento del usuario.
     */
    private LocalDate fechaNacimiento;

    /**
     * Lista de contactos asociados al usuario.
     */
    private List<Contacto> contactos;

    /**
     * Descuento aplicado al usuario si es premium.
     */
    private Optional<Descuento> descuento;

    /**
     * Código identificador del usuario.
     */
    private int codigo;

    /**
     * Constructor de la clase Usuario.
     *
     * @param nombre nombre del usuario
     * @param movil número de móvil
     * @param contraseña contraseña
     * @param imagen ruta de la imagen de perfil
     * @param saludo saludo personalizado
     * @param email correo electrónico
     * @param fechaNacimiento fecha de nacimiento
     */
    public Usuario(String nombre, String movil, String contraseña, String imagen, String saludo, String email, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.movil = movil;
        this.contraseña = contraseña;
        this.imagen = imagen;
        this.saludo = saludo;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.isPremium = false;
        this.contactos = new LinkedList<>();
        this.descuento = null; // Por defecto, el usuario no tiene descuento
        this.codigo = 0;
    }

    // Métodos Getters

    /**
     * Obtiene el nombre del usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el número de móvil del usuario.
     *
     * @return número de móvil
     */
    public String getMovil() {
        return movil;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return contraseña
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Obtiene la imagen de perfil del usuario.
     *
     * @return ruta de la imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Obtiene el saludo personalizado del usuario.
     *
     * @return saludo
     */
    public String getSaludo() {
        return saludo;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene la fecha de nacimiento del usuario.
     *
     * @return fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Indica si el usuario tiene cuenta premium.
     *
     * @return true si es premium, false en caso contrario
     */
    public boolean isPremium() {
        return isPremium;
    }

    /**
     * Devuelve una copia de la lista de contactos del usuario.
     * 
     * @return lista de contactos
     */
    public List<Contacto> getContactos() {
        return new LinkedList<Contacto>(contactos); // Devuelve una copia para evitar modificaciones externas
    }

    /**
     * Calcula el valor del descuento aplicado al usuario en base a la cantidad de mensajes enviados.
     * 
     * @return valor del descuento aplicado o 0.0 si no hay descuento
     */
    public double calDescuento() {
        return descuento
            .map(d -> d.getDescuento(getNumMensajesEnviados())) // Si hay descuento, aplica la función getDescuento
            .orElse(0.0); // Si no hay descuento, devuelve 0.0
    }

    /**
     * Devuelve la lista de mensajes asociados a un contacto específico.
     *
     * @param contacto el contacto del cual se desean obtener los mensajes
     * @return lista de mensajes del contacto, o lista vacía si no se encuentra
     */
    public List<Mensaje> getMensajesDeContacto(Contacto contacto) {
        return contactos.stream()
                .filter(c -> c.getNombre().equals(contacto.getNombre()))
                .findFirst()
                .map(Contacto::getMensajes)
                .orElseGet(LinkedList::new);
    }

    /**
     * Obtiene el número total de mensajes enviados por el usuario.
     *
     * @return número de mensajes enviados
     */
    private int getNumMensajesEnviados() {
        return contactos.stream()
                .flatMap(contacto -> contacto.getMensajes().stream()) // Obtiene todos los mensajes de los contactos
                .mapToInt(mensaje -> 1) // Convierte cada mensaje en un valor 1 para contar
                .sum(); // Suma todos los valores
    }

    /**
     * Obtiene el código identificador del usuario.
     *
     * @return código del usuario
     */
    public int getCodigo() {
        return codigo;
    }

    // Métodos modificadores

    /**
     * Establece la imagen de perfil del usuario.
     *
     * @param imagen nueva ruta de la imagen
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Establece un nuevo saludo personalizado.
     *
     * @param saludo nuevo saludo
     */
    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

    /**
     * Establece una nueva contraseña para el usuario.
     *
     * @param contraseña nueva contraseña
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Activa o desactiva el modo premium del usuario.
     *
     * @param premium true para activar premium, false para desactivarlo
     */
    public void setPremium(boolean premium) {
        this.isPremium = premium;
    }
	
    /**
     * Establece la lista de contactos del usuario.
     * Añade cada contacto recibido a la lista interna.
     *
     * @param contactos lista de contactos a establecer
     */
    public void setContactos(List<Contacto> contactos) {
        contactos.stream().forEach(c -> this.contactos.add(c));    
    }

    /**
     * Devuelve el descuento asignado al usuario, si existe.
     *
     * @return un {@code Optional} con el descuento, o vacío si no tiene
     */
    public Optional<Descuento> getDescuento() {
        return descuento;
    }

    /**
     * Establece un tipo de descuento específico para el usuario.
     *
     * @param tipoDescuento tipo de descuento a aplicar
     */
    public void setDescuento(TipoDescuento tipoDescuento) {
        this.descuento = Optional.of(DescuentoFactoria.crearDescuento(tipoDescuento));
    }

    /**
     * Establece el código identificador del usuario.
     *
     * @param codigo nuevo código del usuario
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    // Métodos para gestionar contactos

    /**
     * Añade un nuevo contacto al usuario si no está repetido.
     * Verifica que no haya conflictos de móvil en contactos individuales.
     *
     * @param contacto el contacto a añadir
     * @return true si fue añadido correctamente, false en caso de duplicados o errores
     */
    public boolean addContacto(Contacto contacto) {
        if (contacto == null || contactos.contains(contacto)) return false;

        // Solo comprobar móviles duplicados si el contacto es individual
        if (contacto instanceof ContactoIndividual) {
            ContactoIndividual nuevoCI = (ContactoIndividual) contacto;
            for (Contacto c : contactos) {
                if (c instanceof ContactoIndividual) {
                    if (((ContactoIndividual) c).getMovil().equals(nuevoCI.getMovil())) {
                        return false;
                    }
                }
            }
        }

        boolean esMismoMovil;

        if (contacto instanceof ContactoIndividual) {
            ContactoIndividual c = (ContactoIndividual) contacto;
            esMismoMovil = c.getUsuario().getMovil().equals(this.movil);
        } else if (contacto instanceof Grupo) {
            Grupo g = (Grupo) contacto;
            esMismoMovil = g.getMiembros().stream()
                    .map(ContactoIndividual::getUsuario)
                    .map(Usuario::getMovil)
                    .anyMatch(movil -> movil.equals(this.movil));
        } else {
            esMismoMovil = false;
        }

        // Si no es el mismo móvil y es un contacto individual, actualiza si ya existe
        if (!esMismoMovil && contacto instanceof ContactoIndividual) {
            ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;

            // Buscar y actualizar el contacto si ya existe
            boolean contactoActualizado = contactos.stream()
                .filter(c -> c instanceof ContactoIndividual) // Solo los ContactoIndividual
                .map(c -> (ContactoIndividual) c)
                .filter(cI -> cI.getUsuario().getMovil().equals(contactoIndividual.getMovil())) // Comparamos los móviles
                .peek(cI -> cI.setNombre(contacto.getNombre())) // Actualizamos el nombre
                .findFirst()
                .isPresent();

            if (contactoActualizado) {
                return true;
            }
        }

        return !esMismoMovil && contactos.add(contacto);
    }

    /**
     * Elimina un contacto de la lista de contactos del usuario.
     *
     * @param contacto el contacto a eliminar
     * @return true si el contacto fue eliminado, false si no se encontró o era null
     */
    public boolean removeContacto(Contacto contacto) {
        if (contacto == null)
            return false;
        return contactos.remove(contacto);
    }

    /**
     * Añade un mensaje a un contacto específico del usuario.
     *
     * @param receptor el contacto que recibirá el mensaje
     * @param mensaje el mensaje a agregar
     * @return true si el mensaje fue agregado correctamente, false si el contacto no existe
     */
    public boolean addMensaje(Contacto receptor, Mensaje mensaje) {
        return contactos.stream()
                .filter(contacto -> contacto.getNombre().equals(receptor.getNombre()))
                .findFirst()
                .map(contacto -> contacto.addMensaje(mensaje))
                .orElse(false);
    }

    /**
     * Busca y devuelve un contacto individual por número de móvil.
     *
     * @param movil número de móvil a buscar
     * @return contacto individual correspondiente, o null si no se encuentra
     */
    public ContactoIndividual getContactoIndividual(String movil) {
        return contactos.stream()
                .filter(contacto -> contacto instanceof ContactoIndividual)
                .map(contacto -> (ContactoIndividual) contacto)
                .filter(contactoIndividual -> contactoIndividual.getUsuario().getMovil().equals(movil))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene un grupo por su nombre.
     *
     * @param nombreGrupo nombre del grupo a buscar
     * @return Optional con el grupo si existe, vacío si no se encuentra
     */
    public Optional<Grupo> obtenerGrupo(String nombreGrupo) {
        return contactos.stream()
                .filter(c -> c instanceof Grupo)
                .map(c -> (Grupo) c)
                .filter(c -> c.getNombre().equals(nombreGrupo))
                .findFirst();
    }

    /**
     * Verifica si un contacto específico es miembro de un grupo dado.
     *
     * @param contacto nombre del contacto
     * @param grupo nombre del grupo
     * @return true si el contacto pertenece al grupo, false en caso contrario
     */
    public boolean esMiembroGrupo(String contacto, String grupo) {
        Optional<Grupo> grupo1 = obtenerGrupo(grupo);

        if (grupo1.isPresent()) {
            return grupo1.get().getMiembros().stream().anyMatch(c -> c.getNombre().equals(contacto));
        } else {
            return false;
        }
    }
}