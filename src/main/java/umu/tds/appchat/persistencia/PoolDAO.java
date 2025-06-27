package umu.tds.appchat.persistencia;

import java.util.Hashtable;

/**
 * Clase que implementa un pool de objetos en memoria para evitar múltiples instancias
 * del mismo objeto recuperado desde la base de datos.
 * 
 * Actúa como una caché en la capa DAO, permitiendo almacenar y reutilizar objetos persistidos.
 * Se implementa como Singleton.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class PoolDAO {

    /**
     * Tabla hash que almacena los objetos por su ID único.
     */
    private Hashtable<Integer, Object> pool;

    /**
     * Instancia única del pool (Singleton).
     */
    private static PoolDAO unicaInstancia;

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private PoolDAO() {
        pool = new Hashtable<Integer, Object>();
    }

    /**
     * Devuelve la instancia única del {@code PoolDAO}.
     *
     * @return instancia Singleton del pool
     */
    public static PoolDAO getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new PoolDAO();
        return unicaInstancia;
    }

    /**
     * Recupera un objeto del pool a partir de su identificador.
     *
     * @param id identificador del objeto
     * @return el objeto correspondiente, o null si no existe
     */
    public Object getObject(int id) {
        return pool.get(id);
    }

    /**
     * Añade un objeto al pool con su identificador correspondiente.
     *
     * @param id identificador del objeto
     * @param object el objeto a almacenar
     */
    public void addObject(int id, Object object) {
        pool.put(id, object);
    }

    /**
     * Verifica si existe un objeto con el ID dado en el pool.
     *
     * @param id identificador del objeto
     * @return true si el objeto está presente, false en caso contrario
     */
    public boolean contains(int id) {
        return pool.containsKey(id);
    }

    /**
     * Elimina un objeto del pool a partir de su ID.
     *
     * @param id identificador del objeto a eliminar
     */
    public void removeObject(int id) {
        pool.remove(id);
    }
}