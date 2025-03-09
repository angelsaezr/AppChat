package umu.tds.appchat.dominio;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase contacto. Un contacto puede ser individual o un grupo.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public abstract class Contacto {
	private String nombre;
	private List<Mensaje> mensajes;
	private int codigo;

	public Contacto(String nombre) {
		this.nombre = nombre;
		this.mensajes = new LinkedList<Mensaje>();
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Mensaje> getMensajes() {
		return new LinkedList<Mensaje>(mensajes);
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public boolean addMensaje(Mensaje mensaje) {
		return mensajes.add(mensaje);
	}

	public abstract String getImagen();
}