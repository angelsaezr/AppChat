package umu.tds.appchat.dominio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
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

            // TODO

            pdfDoc.close();
            
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo generar el PDF de la conversación", ex);
        }
    }
    
    /*
    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("mi_primer_pdf.pdf"));
            document.open();
            document.add(new Paragraph("¡Hola, este es mi primer PDF generado con iText!"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    */
}

