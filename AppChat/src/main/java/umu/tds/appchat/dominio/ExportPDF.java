package umu.tds.appchat.dominio;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class ExportPDF {
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
}

