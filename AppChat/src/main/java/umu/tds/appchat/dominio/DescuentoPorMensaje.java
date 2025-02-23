package umu.tds.appchat.dominio;

/**
 * Clase DescuentoPorMensaje.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class DescuentoPorMensaje implements Descuento {
    private int umbralMensajes;
    private double porcentajeDescuento;

    public DescuentoPorMensaje(int umbralMensajes, double porcentajeDescuento) {
        this.umbralMensajes = umbralMensajes;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double getDescuento(int numMensajes) {
        return numMensajes >= umbralMensajes ? porcentajeDescuento : 0.0;
    }
}
