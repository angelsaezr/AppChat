package umu.tds.appchat.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Mensaje;
import umu.tds.appchat.dominio.TipoMensaje;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import tds.BubbleText;

/**
 * Clase encargada de generar un documento PDF con el historial de conversación de un contacto.
 * Utiliza la biblioteca iText para crear un diseño estructurado, con burbujas de mensaje,
 * autor, fecha y emoticonos en caso de existir.
 * 
 * Incluye logo, título, y formato diferenciado entre mensajes enviados y recibidos.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class ExportPDF {

	 /**
     * Crea un archivo PDF con el historial de conversación del contacto especificado.
     *
     * @param contacto el contacto del que se mostrará la conversación (individual o grupo)
     * @param historial lista de mensajes del contacto
     * @param rutaArchivo ruta del archivo PDF a generar
     * @throws RuntimeException si ocurre algún error durante la creación del PDF
     */
    public static void crearPDF(Contacto contacto, List<Mensaje> historial, String rutaArchivo) {
        try {
            Document pdfDoc = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream(rutaArchivo));
            pdfDoc.open();
            
            // Agrega el logo
            try {
                Image logo = Image.getInstance("src/main/resources/logo_login.png");
                logo.scaleToFit(100, 100);
                logo.setAlignment(Image.ALIGN_CENTER);
                pdfDoc.add(logo);
                pdfDoc.add(Chunk.NEWLINE); // Espacio debajo del logo
            } catch (Exception e) {
                System.err.println("No se pudo cargar el logo: " + e.getMessage());
            }


            // Título del documento
            Font estiloTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph encabezado;
            if (contacto instanceof ContactoIndividual) {
                encabezado = new Paragraph("Conversación entre " + AppChat.getInstance().getNombreContacto(contacto) + " y " + AppChat.getInstance().getNombreUsuarioActual(), estiloTitulo);
            } else {
                encabezado = new Paragraph("Conversación del grupo \"" + contacto.getNombre() + "\"", estiloTitulo);
            }
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setSpacingAfter(20f);
            pdfDoc.add(encabezado);

            // Estilos
            Font estiloMensaje = new Font(Font.FontFamily.HELVETICA, 12);
            Font estiloFecha = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.DARK_GRAY);
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Mensaje mensaje : historial) {
                String autor;
                String contenido = mensaje.getTexto();
                String fechaHora = mensaje.getFechaHoraEnvio().format(formatoFecha);

                // Contenedor con fondo como burbuja
                PdfPTable contenedor = new PdfPTable(1);
                contenedor.setWidthPercentage(70);
                if (mensaje.getTipo() == TipoMensaje.ENVIADO) {
                    contenedor.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    autor = AppChat.getInstance().getNombreUsuarioActual();
                } else {
                    contenedor.setHorizontalAlignment(Element.ALIGN_LEFT);
                    autor = AppChat.getInstance().getNombreContacto(contacto);
                }

                // Autor (opcional si quieres mostrarlo en cada mensaje)
                PdfPCell celdaAutor = new PdfPCell(new Phrase(autor, estiloMensaje));
                celdaAutor.setBorder(Rectangle.NO_BORDER);
                celdaAutor.setBackgroundColor(mensaje.getTipo() == TipoMensaje.ENVIADO ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
                contenedor.addCell(celdaAutor);

                // Mensaje
                PdfPCell celdaMensaje = new PdfPCell(new Phrase(contenido, estiloMensaje));
                PdfPCell celdaEmoticono = new PdfPCell();
                if (mensaje.getEmoticono() != -1) {
                	try {
                        // Obtener el emoticono como java.awt.Image
                        java.awt.Image emojiAwtImage = BubbleText.getEmoji(mensaje.getEmoticono()).getImage();
                        
                        // Convertir a com.itextpdf.text.Image
                        com.itextpdf.text.Image emojiImage = com.itextpdf.text.Image.getInstance(emojiAwtImage, null);
                        
                        // Escalar la imagen si es necesario
                        emojiImage.scaleToFit(20, 20);  // Ajusta el tamaño según sea necesario
                        
                        // Añadir la imagen al PdfPCell
                        celdaEmoticono.addElement(emojiImage);
                    } catch (Exception e) {
                        System.err.println("Error al cargar el emoticono: " + e.getMessage());
                    }
                }
                celdaMensaje.setPadding(8f);
                celdaMensaje.setBackgroundColor(mensaje.getTipo() == TipoMensaje.ENVIADO ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
                celdaMensaje.setBorderColor(BaseColor.LIGHT_GRAY);
                celdaEmoticono.setPadding(8f);
                celdaEmoticono.setBackgroundColor(mensaje.getTipo() == TipoMensaje.ENVIADO ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
                celdaEmoticono.setBorderColor(BaseColor.LIGHT_GRAY);
                contenedor.addCell(celdaMensaje);
                contenedor.addCell(celdaEmoticono);

                // Fecha
                PdfPCell celdaFecha = new PdfPCell(new Phrase(fechaHora, estiloFecha));
                celdaFecha.setBorder(Rectangle.NO_BORDER);
                celdaFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaFecha.setBackgroundColor(mensaje.getTipo() == TipoMensaje.ENVIADO ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
                contenedor.addCell(celdaFecha);

                // Espacio entre mensajes
                contenedor.setSpacingAfter(10f);
                pdfDoc.add(contenedor);
            }

            pdfDoc.close();
            writer.close();

        } catch (Exception ex) {
            throw new RuntimeException("No se pudo generar el PDF de la conversación", ex);
        }
    }
}
