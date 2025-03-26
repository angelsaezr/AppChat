package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.ContactoIndividual;

public interface IAdaptadorContactoIndividualDAO {
	public void registrarContactoIndividual(ContactoIndividual contacto);
	public ContactoIndividual recuperarContactoIndividual(int codigo);
	public List<ContactoIndividual> recuperarTodosLosContactosIndividuales();
	public void modificarContactoIndividual(ContactoIndividual contacto);
	public void borrarContactoIndividual(ContactoIndividual contacto);
}
