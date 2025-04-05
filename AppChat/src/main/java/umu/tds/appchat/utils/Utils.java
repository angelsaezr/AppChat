package umu.tds.appchat.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase utilidades.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
/**
 * Clase de utilidades generales para el manejo de rutas de recursos
 * y procesamiento gráfico de imágenes dentro de la aplicación.
 * 
 * Contiene métodos para convertir rutas de archivos a rutas compatibles
 * con {@code getResource()} y para generar imágenes redondeadas.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Utils {

    /**
     * Convierte una ruta absoluta de un archivo en {@code src/main/resources}
     * a una ruta relativa compatible con {@code getResource()}.
     *
     * @param archivoImagen archivo cuya ruta se va a convertir
     * @return ruta relativa con formato adecuado para {@code getResource()}
     */
    public static String getRutaResourceFromFile(File archivoImagen) {
        // Define la ruta base del proyecto que debe apuntar a "src/main/resources"
        Path rutaBase = Paths.get("src/main/resources").toAbsolutePath();

        // Obtén la ruta absoluta del archivo
        Path rutaArchivo = archivoImagen.toPath().toAbsolutePath();

        // Calcula la ruta relativa desde "src/main/resources" hasta el archivo
        Path rutaRelativa = rutaBase.relativize(rutaArchivo);

        // Devuelve la ruta en formato compatible con getResource()
        return "/" + rutaRelativa.toString().replace("\\", "/");
    }

    /**
     * Convierte una cadena con ruta absoluta que contiene {@code src\\main\\resources\\}
     * a una ruta relativa para acceder con {@code getResource()}.
     *
     * @param source ruta original como cadena
     * @return ruta relativa transformada o vacía si no coincide
     */
    public static String getRutaResourceFromString(String source) {
        String target = "";
        if (source.contains("src\\main\\resources\\")) {
            target = source.substring(source.indexOf("src\\main\\resources\\") + "src\\main\\resources\\".length());
            // Cambia las barras de Windows (\) por barras de URL (/)
            target = "/" + target.replace("\\", "/");
        }
        return target;
    }

    /**
     * Crea una imagen redondeada (en forma de círculo) a partir de una imagen original.
     *
     * @param image imagen original
     * @param size tamaño del círculo (ancho y alto)
     * @return imagen redondeada
     */
    public static Image createRoundedImage(Image image, int size) {
        BufferedImage roundedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(image, 0, 0, size, size, null);
        g2.dispose();
        return roundedImage;
    }
}