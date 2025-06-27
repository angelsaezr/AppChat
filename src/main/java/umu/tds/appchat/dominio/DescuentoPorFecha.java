package umu.tds.appchat.dominio;

import java.time.LocalDate;

/**
 * Implementación de {@link Descuento} que aplica un descuento si la fecha actual
 * se encuentra dentro de un rango específico.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class DescuentoPorFecha implements Descuento {
	/**
     * Código identificador del descuento.
     */
    private int codigo;

    /**
     * Fecha de inicio del periodo en que se aplica el descuento.
     */
    private LocalDate fechaInicio;

    /**
     * Fecha de fin del periodo en que se aplica el descuento.
     */
    private LocalDate fechaFin;

    /**
     * Porcentaje de descuento a aplicar.
     */
    private double porcentajeDescuento;

    /**
     * Crea un descuento por fecha con un periodo y porcentaje determinados.
     *
     * @param fechaInicio fecha de inicio del descuento
     * @param fechaFin fecha de fin del descuento
     * @param porcentajeDescuento porcentaje a aplicar si la fecha actual está dentro del rango
     */
    public DescuentoPorFecha(LocalDate fechaInicio, LocalDate fechaFin, double porcentajeDescuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeDescuento = porcentajeDescuento;
        this.codigo = 0;
    }

    /**
     * Retorna el porcentaje de descuento si la fecha actual está dentro del rango válido.
     *
     * @param numMensajes no se utiliza en esta implementación
     * @return el porcentaje de descuento si aplica, o 0.0 en caso contrario
     */
    @Override
    public double getDescuento(int numMensajes) {
        LocalDate hoy = LocalDate.now();
        if (!hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin)) {
            return porcentajeDescuento;
        }
        return 0.0;
    }
    
    /**
     * Obtiene el código identificador del descuento.
     *
     * @return código del descuento
     */
    @Override
    public int getCodigo() {
    	return codigo;
    }
    
    /**
     * Establece el código identificador del descuento.
     *
     * @param codigo nuevo código del descuento
     */
    public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
    
    /**
     * Devuelve la fecha y hora de inicio del descuento.
     *
     * @return fecha y hora de inicio
     */
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	
	/**
     * Devuelve la fecha y hora de inicio del descuento.
     *
     * @return fecha y hora de inicio
     */
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	
	/**
     * Devuelve el porcentaje de descuento.
     *
     * @return porcentaje de descuento
     */
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}
}