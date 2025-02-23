package umu.tds.appchat.dominio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

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
	private Descuento descuento;

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
		return new LinkedList<>(contactos);
	} // Devuelve una copia para evitar modificaciones externas

	public Descuento getDescuento() {
		return descuento;
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

	public void setDescuento(Descuento descuento) {
		this.descuento = descuento;
	}

	// Métodos para gestionar contactos
	public boolean addContacto(Contacto contacto) {
		if (contacto == null || contactos.contains(contacto))
			return false;
		return contactos.add(contacto);
	}

	public boolean removeContacto(Contacto contacto) {
		if (contacto == null)
			return false;
		return contactos.remove(contacto);
	}

	// Método para calcular el descuento aplicado al usuario
	public double calcularDescuento(int numMensajes) {
		if (descuento != null) {
			return descuento.getDescuento(numMensajes);
		}
		return 0.0;
	}
	
	public boolean addMensaje(Contacto receptor, String texto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje;
		if(texto != null)
			mensaje = new Mensaje(texto, tipo);
		else
			mensaje = new Mensaje(emoticono, tipo);
		for(Contacto c : contactos) {
			if (c.getNombre().equals(receptor.getNombre()))
				return c.addMensaje(mensaje);
		}
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

}
