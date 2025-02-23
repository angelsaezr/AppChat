package umu.tds.appchat.dominio;

import java.util.List;

/**
 * Clase Grupo. Un grupo es un tipo de Contacto.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Grupo extends Contacto {
	private String urlImagen;
	private List<ContactoIndividual> miembros;
	
	public Grupo(String nombreGrupo, List<ContactoIndividual> miembros, String imagenGrupo) {
		super(nombreGrupo);
		this.miembros = miembros;
		urlImagen = imagenGrupo;
	}
	
	public Grupo(String nombreGrupo, List<ContactoIndividual> miembros) {
		this(nombreGrupo, miembros, "");
	}

	public List<ContactoIndividual> getMiembros() {
		return miembros;
	}

	public String getImagen() {
		return urlImagen;
	}

	public void setImagen(String imagen) {
		urlImagen = imagen;
	}
	
	public boolean addMiembro(ContactoIndividual miembro) {
		return this.miembros.add(miembro);
	}
	
	public boolean removeMiembro(ContactoIndividual miembro) {
		return this.miembros.remove(miembro);
	}
}