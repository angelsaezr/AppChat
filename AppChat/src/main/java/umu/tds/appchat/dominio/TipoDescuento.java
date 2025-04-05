package umu.tds.appchat.dominio;

/**
 * Enumeración que representa los tipos de descuento disponibles en la aplicación.
 * Se utiliza para seleccionar la estrategia correspondiente desde la factoría.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public enum TipoDescuento {
    /**
     * Descuento basado en un rango de fechas.
     */
    FECHA,

    /**
     * Descuento basado en la cantidad de mensajes enviados.
     */
    MENSAJE;
}