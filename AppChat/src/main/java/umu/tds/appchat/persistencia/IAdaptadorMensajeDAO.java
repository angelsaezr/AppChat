package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Mensaje;

/**
 * Interfaz DAO para la gestión de objetos {@link Mensaje} en la capa de persistencia.
 * Define las operaciones necesarias para registrar, recuperar y eliminar mensajes.
 * 
 * Esta interfaz debe ser implementada por adaptadores que interactúen con el sistema de almacenamiento.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public interface IAdaptadorMensajeDAO {

    /**
     * Registra un mensaje en la base de datos.
     *
     * @param mensaje el mensaje a registrar
     */
    public void registrarMensaje(Mensaje mensaje);

    /**
     * Recupera un mensaje desde la base de datos a partir de su código identificador.
     *
     * @param codigo código único del mensaje
     * @return el mensaje correspondiente
     */
    public Mensaje recuperarMensaje(int codigo);

    /**
     * Recupera todos los mensajes almacenados en la base de datos.
     *
     * @return lista de mensajes registrados
     */
    public List<Mensaje> recuperarTodosLosMensajes();

    /**
     * Elimina un mensaje del sistema de persistencia.
     *
     * @param mensaje el mensaje a eliminar
     */
    public void borrarMensaje(Mensaje mensaje);
}