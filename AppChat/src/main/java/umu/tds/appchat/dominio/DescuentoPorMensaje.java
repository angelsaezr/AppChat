package umu.tds.appchat.dominio;

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
