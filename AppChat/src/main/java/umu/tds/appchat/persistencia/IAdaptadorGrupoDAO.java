package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Grupo;

/**
 * Interfaz DAO para la gestión de objetos {@link Grupo} en la capa de persistencia.
 * Define operaciones básicas para registrar, recuperar, modificar y eliminar grupos.
 * 
 * Esta interfaz será implementada por clases encargadas de interactuar con la base de datos
 * u otro sistema de almacenamiento persistente.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public interface IAdaptadorGrupoDAO {

    /**
     * Registra un nuevo grupo en la base de datos.
     *
     * @param grupo el grupo a registrar
     */
    public void registrarGrupo(Grupo grupo);

    /**
     * Recupera un grupo desde la base de datos a partir de su código identificador.
     *
     * @param codigo código único del grupo
     * @return el grupo correspondiente
     */
    public Grupo recuperarGrupo(int codigo);

    /**
     * Recupera todos los grupos almacenados en la base de datos.
     * 
     * @return lista de grupos
     */
    public List<Grupo> recuperarTodosLosContactosIndividuales();

    /**
     * Modifica los datos de un grupo existente en la base de datos.
     *
     * @param grupo el grupo con los datos actualizados
     */
    public void modificarGrupo(Grupo grupo);

    /**
     * Elimina un grupo de la base de datos.
     *
     * @param grupo el grupo a eliminar
     */
    public void borrarGrupo(Grupo grupo);
}