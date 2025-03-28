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
public class Usuario {

	private String nombre;
	private String movil;
	private String contraseña;
	private String email;
	private String imagen;
	private boolean isPremium;
	private String saludo;
	private LocalDate fechaNacimiento;
	private List<Contacto> contactos;
	private Optional<Descuento> descuento;
	private int codigo;

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
	public String getNombre() {
		return nombre;
	}

	public String getMovil() {
		return movil;
	}

	public String getContraseña() {
		return contraseña;
	}

	public String getImagen() {
		return imagen;
	}

	public String getSaludo() {
		return saludo;
	}
	
	public String getEmail() {
		return email;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public boolean isPremium() {
		return isPremium;
	}

	public List<Contacto> getContactos() {
		System.out.println(this.contactos + "ytytytytyt");
		return new LinkedList<Contacto>(contactos);
	} // Devuelve una copia para evitar modificaciones externas

	public double calDescuento() {
	    return descuento
	        .map(d -> d.getDescuento(getNumMensajesEnviados())) // Si hay descuento, aplica la función getDescuento
	        .orElse(0.0); // Si no hay descuento, devuelve 0.0
	}
	
	public List<Mensaje> getMensajesDeContacto(Contacto contacto) {
	    return contactos.stream()
	            .filter(c -> c.getNombre().equals(contacto.getNombre()))
	            .findFirst()
	            .map(Contacto::getMensajes)
	            .orElseGet(LinkedList::new);
	}
	
	private int getNumMensajesEnviados() {
	    return contactos.stream()
	            .flatMap(contacto -> contacto.getMensajes().stream()) // Obtiene todos los mensajes de los contactos
	            .mapToInt(mensaje -> 1) // Convierte cada mensaje en un valor 1 para contar
	            .sum(); // Suma todos los valores
	}
	
	public int getCodigo() {
		return codigo;
	}

	// Métodos modificadores
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setPremium(boolean premium) {
		this.isPremium = premium;
	}
	
	public void setContactos(List<Contacto> contactos) {
		contactos.stream().forEach(c -> this.contactos.add(c));	
	}
	
	public Optional<Descuento> getDescuento() {
		return descuento;
	}

	public void setDescuento(TipoDescuento tipoDescuento) {
	    this.descuento = Optional.of(DescuentoFactoria.crearDescuento(tipoDescuento));
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	// Métodos para gestionar contactos
	public boolean addContacto(Contacto contacto) {
	    if (contacto == null || contactos.contains(contacto)) return false;
	    
	    for (Contacto c: contactos) {
        	if (c instanceof ContactoIndividual) {
        		if (((ContactoIndividual) c).getMovil().equals(((ContactoIndividual) contacto).getMovil())) {
        			return false;
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
	    
	    if (!esMismoMovil && contacto instanceof ContactoIndividual) {
	        ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
	        // Buscar y actualizar el contacto si ya existe
	        boolean contactoActualizado = contactos.stream()
	            .filter(c -> c instanceof ContactoIndividual)  // Solo los ContactoIndividual
	            .map(c -> (ContactoIndividual) c)
	            .filter(cI -> cI.getUsuario().getMovil().equals(contactoIndividual.getMovil()))  // Comparamos los números de móvil
	            .peek(cI -> cI.setNombre(contacto.getNombre()))  // Actualizamos el nombre
	            .findFirst()
	            .isPresent();
	        if (contactoActualizado) {
	            return true;
	        }
	    }
	    return !esMismoMovil && contactos.add(contacto);
	}


	public boolean removeContacto(Contacto contacto) {
		if (contacto == null)
			return false;
		return contactos.remove(contacto);
	}
	
	public boolean addMensaje(Contacto receptor, String texto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = new Mensaje(texto, emoticono, tipo);
		return contactos.stream()
				.filter(contacto -> contacto.getNombre().equals(receptor.getNombre()))
				.findFirst()
				.map(contacto -> contacto.addMensaje(mensaje))
				.orElse(false);
	}
	
	public ContactoIndividual getContactoIndividual(String movil) {
		return contactos.stream()
		        .filter(contacto -> contacto instanceof ContactoIndividual)
		        .map(contacto -> (ContactoIndividual) contacto)
		        .filter(contactoIndividual -> contactoIndividual.getUsuario().getMovil().equals(movil))
		        .findFirst()
		        .orElse(null);
	}
	
	public Optional<Grupo> obtenerGrupo(String nombreGrupo) {
		return contactos.stream().filter(c -> c instanceof Grupo).map(c -> (Grupo) c)
				.filter(c -> c.getNombre().equals(nombreGrupo)).findFirst();
	}
	
	public boolean esMiembroGrupo(String contacto, String grupo) {		
		Optional<Grupo> grupo1 = obtenerGrupo(grupo);
		
		if (grupo1.isPresent()) {
			return grupo1.get().getMiembros().stream().anyMatch(c -> c.getNombre().equals(contacto));
		} else {
			return false;
		}
	}

}
