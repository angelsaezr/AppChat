package umu.tds.appchat.dominio;

/**
 * Implementación de {@link Descuento} que aplica un descuento
 * si el usuario ha enviado una cantidad mínima de mensajes.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class DescuentoPorMensaje implements Descuento {

    /**
     * Umbral mínimo de mensajes enviados para aplicar el descuento.
     */
    private int umbralMensajes;

    /**
     * Porcentaje de descuento a aplicar si se supera el umbral.
     */
    private double porcentajeDescuento;

    /**
     * Crea un descuento basado en la cantidad de mensajes enviados.
     *
     * @param umbralMensajes número mínimo de mensajes requeridos
     * @param porcentajeDescuento porcentaje de descuento a aplicar
     */
    public DescuentoPorMensaje(int umbralMensajes, double porcentajeDescuento) {
        this.umbralMensajes = umbralMensajes;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Devuelve el porcentaje de descuento si se ha alcanzado el umbral de mensajes.
     *
     * @param numMensajes número de mensajes enviados
     * @return porcentaje de descuento si se cumple el umbral, o 0.0 si no se cumple
     */
    @Override
    public double getDescuento(int numMensajes) {
        return numMensajes >= umbralMensajes ? porcentajeDescuento : 0.0;
    }
}