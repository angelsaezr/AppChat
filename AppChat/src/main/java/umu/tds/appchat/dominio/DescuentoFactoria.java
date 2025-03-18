package umu.tds.appchat.dominio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DescuentoFactoria {
    private static final Map<TipoDescuento, Supplier<Descuento>> proveedores = new HashMap<>();

    static {
        // Se registran los tipos de descuento con un Supplier vacío (requieren parámetros al instanciar)
        proveedores.put(TipoDescuento.FECHA, () -> new DescuentoPorFecha(LocalDate.now(), LocalDate.now().plusDays(7), 0.10));
        proveedores.put(TipoDescuento.MENSAJE, () -> new DescuentoPorMensaje(10, 0.05));
    }

    public static Descuento crearDescuento(TipoDescuento tipo) {
        Supplier<Descuento> supplier = proveedores.get(tipo);
        if (supplier == null) {
        	throw new IllegalArgumentException("Tipo de descuento desconocido: " + tipo.toString());
        }
        return supplier.get();
    }
}
