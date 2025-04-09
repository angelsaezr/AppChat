package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dominio.Descuento;

/**
 * Interfaz DAO para la gestión de objetos {@link Mensaje} en la capa de persistencia.
 * Define las operaciones necesarias para registrar, recuperar y eliminar descuentos.
 * 
 * Esta interfaz debe ser implementada por adaptadores que interactúen con el sistema de almacenamiento.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public interface IAdaptadorDescuentoDAO {

    /**
     * Registra un descuento en la base de datos.
     *
     * @param descuento el descuento a registrar
     */
    public void registrarDescuento(Descuento descuento);

    /**
     * Recupera un descuento desde la base de datos a partir de su código identificador.
     *
     * @param codigo código único del descuento
     * @return el descuento correspondiente
     */
    public Descuento recuperarDescuento(int codigo);

    /**
     * Recupera todos los descuentos almacenados en la base de datos.
     *
     * @return lista de descuentos registrados
     */
    public List<Descuento> recuperarTodosLosDescuentos();

    /**
     * Elimina un descuento del sistema de persistencia.
     *
     * @param descuento el descuento a eliminar
     */
    public void borrarDescuento(Descuento descuento);
}