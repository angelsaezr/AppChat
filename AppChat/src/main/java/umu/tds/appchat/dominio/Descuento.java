package umu.tds.appchat.dominio;

/**
 * Interfaz que define el comportamiento para aplicar descuentos a usuarios.
 * Implementaciones concretas determinarán el porcentaje de descuento
 * en función del número de mensajes enviados.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public interface Descuento {

    /**
     * Calcula el porcentaje de descuento a aplicar según la cantidad de mensajes enviados.
     *
     * @param numMensajes número de mensajes enviados por el usuario
     * @return porcentaje de descuento aplicado
     */
    double getDescuento(int numMensajes);
}
