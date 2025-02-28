package umu.tds.appchat.dominio;

/**
 * Clase contacto individual.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class ContactoIndividual extends Contacto {
	private Usuario usuario;

	public ContactoIndividual(String nombre, Usuario usuario) {
		super(nombre);
		this.usuario = usuario;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public String getImagen() {
		return usuario.getImagen();
	}
	
	public String getMovil() {
		return usuario.getMovil();
	}
	
	public String getSaludo() {
		return usuario.getSaludo();
	}
}