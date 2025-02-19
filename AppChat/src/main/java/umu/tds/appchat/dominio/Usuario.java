package umu.tds.appchat.dominio;

public class Usuario {

	private String saludo;
	private String nombre;
	private String movil;
	private String imagen; // TODO CAMBIAR
	
	public Usuario(String nombre, String movil, String saludo, String imagen) {
		this.saludo = saludo;
		this.nombre = nombre;
		this.movil = movil;
		this.imagen = imagen;
	}

	public String getSaludo() {
		return saludo;
	}

	public String getUsuario() {
		return nombre;
	}

	public String getImagen() {
		return imagen;
	}
	
	public String getMovil() {
		return movil;
	}
}
