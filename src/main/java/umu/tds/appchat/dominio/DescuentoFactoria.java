package umu.tds.appchat.dominio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factoria para la creación de objetos que implementan la interfaz {@link Descuento}.
 * Asocia tipos de descuento con sus respectivas implementaciones a través de {@link Supplier}.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class DescuentoFactoria {

    /**
     * Mapa que asocia cada tipo de descuento con un proveedor de instancias de {@link Descuento}.
     */
    private static final Map<TipoDescuento, Supplier<Descuento>> proveedores = new HashMap<>();

    static {
        // Se registran los tipos de descuento con un Supplier vacío (requieren parámetros al instanciar)
        proveedores.put(TipoDescuento.FECHA, () -> new DescuentoPorFecha(LocalDate.now(), LocalDate.now().plusDays(7), 0.10));
        proveedores.put(TipoDescuento.MENSAJE, () -> new DescuentoPorMensaje(10, 0.05));
    }

    /**
     * Crea una instancia de {@link Descuento} según el tipo especificado.
     *
     * @param tipo el tipo de descuento deseado
     * @return instancia correspondiente de {@link Descuento}
     * @throws IllegalArgumentException si el tipo no está registrado
     */
    public static Descuento crearDescuento(TipoDescuento tipo) {
        Supplier<Descuento> supplier = proveedores.get(tipo);
        if (supplier == null) {
            throw new IllegalArgumentException("Tipo de descuento desconocido: " + tipo.toString());
        }
        return supplier.get();
    }
}