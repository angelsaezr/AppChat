package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dominio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import beans.Entidad;
import beans.Propiedad;
import beans.Mensaje;

public class AdaptadorContactoIndividual implements IAdaptadorContactoIndividualDAO {
	
	private ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividual unicaInstancia = null;
	
	public AdaptadorContactoIndividual() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public static AdaptadorContactoIndividual getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoIndividual();
		return unicaInstancia;
	}
	
	public void registrarContactoIndividual(ContactoIndividual contacto) {

	}

	public ContactoIndividual recuperarContactoIndividual(int codigo) {
		return null;
		
	}

	public void modificarContactoIndividual() {

	}

	public void borrarContactoIndividual(ContactoIndividual contacto) {
		
	}
}
