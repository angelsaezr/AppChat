package umu.tds.appchat.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import umu.tds.appchat.dominio.Mensaje;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportPDF {

    public static void crearPDF(String contacto, List<Mensaje> historial, String rutaArchivo) {
        try {
            Document pdfDoc = new Document();
            PdfWriter.getInstance(pdfDoc, new FileOutputStream(rutaArchivo));
            pdfDoc.open();

            // Título del documento
            Font estiloTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph encabezado = new Paragraph("Conversación con " + contacto, estiloTitulo);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            pdfDoc.add(encabezado);
            pdfDoc.add(new Paragraph("\n")); // Espacio extra

            // Estilos de texto
            Font estiloMensaje = new Font(Font.FontFamily.HELVETICA, 12);
            Font estiloFecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Mensaje mensaje : historial) {
                String autor = contacto; // Se asume que el mensaje tiene un método getAutor() que devuelve un objeto con getNombre()
                String contenido = mensaje.getTexto();     // Contenido del mensaje
                String fechaHora = mensaje.getFechaHoraEnvio().format(formatoFecha); // Se asume que el mensaje tiene un atributo LocalDateTime llamado fechaHora

                Paragraph lineaAutor = new Paragraph(autor + ":", estiloMensaje);
                lineaAutor.setSpacingBefore(5f);
                pdfDoc.add(lineaAutor);

                Paragraph lineaContenido = new Paragraph(contenido, estiloMensaje);
                pdfDoc.add(lineaContenido);

                Paragraph lineaFecha = new Paragraph(fechaHora, estiloFecha);
                pdfDoc.add(lineaFecha);
            }

            pdfDoc.close();

        } catch (Exception ex) {
            throw new RuntimeException("No se pudo generar el PDF de la conversación", ex);
        }
    }
}
