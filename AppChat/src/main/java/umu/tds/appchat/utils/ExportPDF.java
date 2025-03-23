package umu.tds.appchat.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dominio.Contacto;
import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.Mensaje;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportPDF {

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
                encabezado = new Paragraph("Conversación entre " + contacto.getNombre() + " y " + AppChat.getInstance().getUsuarioActual().getNombre(), estiloTitulo);
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

            String nombreUsuarioActual = AppChat.getInstance().getUsuarioActual().getNombre();

            for (Mensaje mensaje : historial) {
                boolean esUsuario = /* TODO mensaje.getAutor().getNombre().equals(nombreUsuarioActual)*/ true;
                String autor = contacto.getNombre();
                String contenido = mensaje.getTexto();
                String fechaHora = mensaje.getFechaHoraEnvio().format(formatoFecha);

                // Contenedor con fondo como burbuja
                PdfPTable contenedor = new PdfPTable(1);
                contenedor.setWidthPercentage(70);
                if (esUsuario) {
                    contenedor.setHorizontalAlignment(Element.ALIGN_RIGHT);
                } else {
                    contenedor.setHorizontalAlignment(Element.ALIGN_LEFT);
                }

                // Autor (opcional si quieres mostrarlo en cada mensaje)
                PdfPCell celdaAutor = new PdfPCell(new Phrase(autor, estiloMensaje));
                celdaAutor.setBorder(Rectangle.NO_BORDER);
                celdaAutor.setBackgroundColor(esUsuario ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
                contenedor.addCell(celdaAutor);

                // Mensaje
                PdfPCell celdaMensaje = new PdfPCell(new Phrase(contenido, estiloMensaje));
                celdaMensaje.setPadding(8f);
                celdaMensaje.setBackgroundColor(esUsuario ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
                celdaMensaje.setBorderColor(BaseColor.LIGHT_GRAY);
                contenedor.addCell(celdaMensaje);

                // Fecha
                PdfPCell celdaFecha = new PdfPCell(new Phrase(fechaHora, estiloFecha));
                celdaFecha.setBorder(Rectangle.NO_BORDER);
                celdaFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaFecha.setBackgroundColor(esUsuario ? new BaseColor(220, 248, 198) : new BaseColor(240, 240, 240));
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
