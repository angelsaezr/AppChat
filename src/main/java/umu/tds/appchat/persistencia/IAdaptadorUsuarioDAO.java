package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Usuario;

/**
 * Interfaz DAO para la gestión de objetos {@link Usuario} en la capa de persistencia.
 * Define operaciones estándar para registrar, recuperar, modificar y eliminar usuarios.
 * 
 * Esta interfaz será implementada por adaptadores concretos que interactúan con
 * el sistema de almacenamiento persistente de la aplicación.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public interface IAdaptadorUsuarioDAO {

    /**
     * Registra un usuario en la base de datos.
     *
     * @param usuario el usuario a registrar
     */
    public void registrarUsuario(Usuario usuario);

    /**
     * Recupera un usuario desde la base de datos mediante su código único.
     *
     * @param codigo código identificador del usuario
     * @return el usuario correspondiente
     */
    public Usuario recuperarUsuario(int codigo);

    /**
     * Recupera todos los usuarios almacenados en la base de datos.
     *
     * @return lista de usuarios registrados
     */
    public List<Usuario> recuperarTodosLosUsuarios();

    /**
     * Modifica los datos de un usuario existente en la base de datos.
     *
     * @param usuario el usuario con los datos actualizados
     */
    public void modificarUsuario(Usuario usuario);

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param usuario el usuario a eliminar
     */
    //public void borrarUsuario(Usuario usuario);
}