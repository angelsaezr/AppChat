package umu.tds.appchat.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        this.descuento = Optional.empty(); // Por defecto, el usuario no tiene descuento
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
    public double calcularDescuento() {
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
        return descuento == null ? Optional.empty() : descuento;
    }

    /**
     * Establece un tipo de descuento específico para el usuario y lo convierte en premium.
     *
     * @param tipoDescuento tipo de descuento a aplicar
     * @return el descuento del usuario
     */
    public Descuento activarPremium(TipoDescuento tipoDescuento) {
    	this.isPremium = true;
        this.descuento = Optional.of(DescuentoFactoria.crearDescuento(tipoDescuento));
        return this.descuento.get();
    }
    
    /**
     * El usuario cancela la suscripción premium y establece el descuento a null
     *
     */
    public void anularPremium() {
    	this.isPremium = false;
    	this.descuento = Optional.empty();
    }
    
    /**
     * Establece descuento específico para el usuario.
     *
     * @param descuento descuento a aplicar
     */
    public void setDescuento(Descuento descuento) {
    	this.isPremium = true;
        this.descuento = Optional.ofNullable(descuento);
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
     * @param nombre el nombre con el que se agrega el contacto
     * @param usuarioContacto el usuario que se está agregando como contacto
     * @return nuevoContacto si fue añadido correctamente, null en caso de duplicados o errores
     */
    public ContactoIndividual addContactoIndividual(String nombre, Usuario usuarioContacto) {
    	ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto);
        boolean movilDuplicado = contactos.stream()
        	.filter(c -> c instanceof ContactoIndividual)
        	.map(c -> (ContactoIndividual) c)
        	.anyMatch(cI -> cI.getMovil().equals(nuevoContacto.getMovil()));

       	if (movilDuplicado || usuarioContacto.getMovil().equals(this.movil)) return null;

       	contactos.add(nuevoContacto);

        return nuevoContacto;
    }
    
    /**
     * Añade un nuevo grupo al usuario.
     *
     * @param nombreGrupo el nombre con el que se agrega el grupo
     * @param miembros lista de miembros del grupo
     * @param rutaImagen ruta de la imagen del grupo
     * @return nuevoGrupo grupo añadido
     */
    public Grupo addGrupo(String nombreGrupo, List<ContactoIndividual> miembros, String rutaImagen) {
    	Grupo nuevoGrupo = new Grupo(nombreGrupo, miembros, rutaImagen);

       	contactos.add(nuevoGrupo);

        return nuevoGrupo;
    }

    /**
     * Elimina un contacto de la lista de contactos del usuario.
     *
     * @param contacto el contacto a eliminar
     * @return true si el contacto fue eliminado, false si no se encontró o era null
     */
    public boolean removeContacto(Contacto contacto) {
        return contactos.remove(contacto);
    }

    /**
     * Añade un mensaje al chat con un contacto específico del usuario.
     *
     * @param receptor el contacto individual del chat en el que se desea agregar el mensaje
     * @param texto contenido del mensaje
     * @param emoticono código del emoticono adjunto
     * @param tipo enviado o recibido
     * @return el mensaje añadido al chat
     */
    public Mensaje enviarMensajeAContactoIndividual(ContactoIndividual receptor, String texto, int emoticono, TipoMensaje tipo) {
    	Mensaje mensaje = new Mensaje(texto, emoticono, tipo, LocalDateTime.now());
    	ContactoIndividual receptorEnLista = (ContactoIndividual)contactos.stream()
    			.filter(contacto -> contacto.getNombre().equals(receptor.getNombre()))
    			.findFirst()
    			.get();
    	receptorEnLista.addMensaje(mensaje);
        return mensaje;
    }
    
    /**
     * Añade un mensaje al chat con un grupo del usuario.
     *
     * @param grupo grupo donde se envia el mensaje
     * @param texto contenido del mensaje
     * @param emoticono código del emoticono adjunto
     * @param tipo enviado o recibido
     * @return el mensaje añadido al chat del grupo
     */
    public Mensaje enviarMensajeAGrupo(Grupo grupo, String texto, int emoticono, TipoMensaje tipo) {
    	Mensaje mensaje = new Mensaje(texto, emoticono, tipo, LocalDateTime.now());
    	Grupo grupoEnLista = (Grupo)contactos.stream()
    			.filter(contacto -> contacto.getNombre().equals(grupo.getNombre()))
    			.findFirst()
    			.get();
    	grupoEnLista.addMensaje(mensaje);
        return mensaje;
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