package umu.tds.appchat.persistencia;

/**
 * Excepción personalizada que representa errores en operaciones DAO
 * (Data Access Object) relacionadas con la persistencia de datos.
 * 
 * Se utiliza para encapsular fallos durante el acceso o manipulación
 * de entidades persistidas.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
@SuppressWarnings("serial")
public class DAOException extends Exception {

    /**
     * Crea una nueva excepción DAO con el mensaje especificado.
     *
     * @param mensaje descripción del error ocurrido
     */
    public DAOException(String mensaje) {
        super(mensaje);
    }
}

