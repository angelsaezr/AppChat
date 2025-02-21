package umu.tds.appchat.dominio;

import java.time.LocalDate;

public class DescuentoPorFecha implements Descuento {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeDescuento;

    public DescuentoPorFecha(LocalDate fechaInicio, LocalDate fechaFin, double porcentajeDescuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double getDescuento(int numMensajes) {
        LocalDate hoy = LocalDate.now();
        if (!hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin)) {
            return porcentajeDescuento;
        }
        return 0.0;
    }
}
