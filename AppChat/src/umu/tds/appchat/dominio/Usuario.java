package umu.tds.appchat.dominio;

public class Usuario {

	private String saludo;
	private Usuario usuario;
	private String movil;
	private String imagen; // TODO CAMBIAR
	
	public Usuario(String saludo, Usuario usuario, String movil, String imagen) {
		this.saludo = saludo;
		this.usuario = usuario;
		this.movil = movil;
		this.imagen = imagen;
	}

	public String getSaludo() {
		return saludo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getImagen() {
		return imagen;
	}
	
	public String getMovil() {
		return movil;
	}
}
