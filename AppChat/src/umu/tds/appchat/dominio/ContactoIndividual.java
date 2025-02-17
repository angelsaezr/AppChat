package umu.tds.appchat.dominio;

public class ContactoIndividual {

	private String nombre;
	private Usuario usuario;
	
	public ContactoIndividual(String nombre, Usuario usuario) {
		this.nombre = nombre;
		this.usuario = usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
}
