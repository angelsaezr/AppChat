package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.ContactoIndividual;

/**
 * Interfaz DAO para la gestión de objetos {@link ContactoIndividual} en la capa de persistencia.
 * Define las operaciones básicas de registro, recuperación, modificación y eliminación.
 * 
 * Esta interfaz será implementada por clases que interactúan con sistemas de almacenamiento persistente.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public interface IAdaptadorContactoIndividualDAO {

    /**
     * Registra un nuevo contacto individual en la base de datos.
     *
     * @param contacto el contacto a registrar
     */
    public void registrarContactoIndividual(ContactoIndividual contacto);

    /**
     * Recupera un contacto individual a partir de su código identificador.
     *
     * @param codigo código único del contacto
     * @return el contacto individual correspondiente
     */
    public ContactoIndividual recuperarContactoIndividual(int codigo);

    /**
     * Recupera todos los contactos individuales almacenados.
     *
     * @return lista de contactos individuales
     */
    public List<ContactoIndividual> recuperarTodosLosContactosIndividuales();

    /**
     * Modifica los datos de un contacto individual existente en la base de datos.
     *
     * @param contacto el contacto con datos actualizados
     */
    public void modificarContactoIndividual(ContactoIndividual contacto);

    /**
     * Elimina un contacto individual de la base de datos.
     *
     * @param contacto el contacto a eliminar
     */
    //public void borrarContactoIndividual(ContactoIndividual contacto);
}