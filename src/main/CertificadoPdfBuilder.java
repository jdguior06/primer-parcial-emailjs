package main;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CertificadoPdfBuilder {

    private static final BaseColor VERDE    = new BaseColor(26, 92, 42);
    private static final BaseColor GRIS     = new BaseColor(80, 80, 80);

    /**
     * datos: [0]=certId  [1]=estudiante  [2]=nroDoc  [3]=tipoCurso
     *        [4]=nota    [5]=estado      [6]=fechaEmision  [7]=instructor
     *        [8]=fechaIni  [9]=fechaFin
     */
    public static byte[] generar(String[] datos) throws DocumentException, IOException {
        Document doc = new Document(PageSize.LETTER, 90f, 90f, 72f, 72f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        doc.open();

        // Cargar membrete como fondo de página (src/resources/membrete.jpg)
        try (InputStream is = CertificadoPdfBuilder.class.getResourceAsStream("/resources/membrete.jpg")) {
            if (is != null) {
                Image img = Image.getInstance(leerBytes(is));
                img.scaleAbsolute(doc.getPageSize().getWidth(), doc.getPageSize().getHeight());
                img.setAbsolutePosition(0, 0);
                writer.getDirectContentUnder().addImage(img);
            }
        }

        // Fuentes (Times Roman — estilo más formal para certificados)
        Font fTitulo   = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD,   VERDE);
        Font fNombre   = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD,   BaseColor.BLACK);
        Font fNormal   = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
        Font fPequena  = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, GRIS);

        boolean aprobado = "aprobado".equalsIgnoreCase(datos[5]);

        // Zona de texto: subida para no pisar la zona de firma del membrete.
        // Página Letter = 612 x 792 pt.
        // lly=340 deja ~340pt libres en la parte inferior para la firma/sello.
        // ury=645 mantiene distancia respecto al encabezado del ACB (~150pt desde arriba).
        PdfContentByte cb = writer.getDirectContent();
        ColumnText ct = new ColumnText(cb);
        ct.setSimpleColumn(90f, 340f, 522f, 645f);

        // Título
        Paragraph pTitulo = new Paragraph(
                aprobado ? "CERTIFICADO DE APROBACIÓN" : "CERTIFICADO DE CALIFICACIÓN",
                fTitulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        pTitulo.setSpacingAfter(10f);
        ct.addElement(pTitulo);

        // Línea decorativa verde
        ct.addElement(new Chunk(new LineSeparator(1.5f, 65f, VERDE, Element.ALIGN_CENTER, -2f)));
        ct.addElement(new Paragraph("\n", fPequena));

        // Introducción
        Paragraph pIntro = new Paragraph("Se certifica que el/la estudiante:", fNormal);
        pIntro.setAlignment(Element.ALIGN_CENTER);
        pIntro.setSpacingAfter(8f);
        ct.addElement(pIntro);

        // Nombre completo del estudiante
        Paragraph pNombre = new Paragraph(datos[1].toUpperCase(), fNombre);
        pNombre.setAlignment(Element.ALIGN_CENTER);
        pNombre.setSpacingAfter(4f);
        ct.addElement(pNombre);

        // CI
        Paragraph pCi = new Paragraph("C.I.: " + datos[2], fNormal);
        pCi.setAlignment(Element.ALIGN_CENTER);
        pCi.setSpacingAfter(16f);
        ct.addElement(pCi);

        // Texto del curso
        Paragraph pCursoLabel = new Paragraph("ha completado el curso de:", fNormal);
        pCursoLabel.setAlignment(Element.ALIGN_CENTER);
        pCursoLabel.setSpacingAfter(6f);
        ct.addElement(pCursoLabel);

        Paragraph pCurso = new Paragraph(datos[3].toUpperCase(), fNombre);
        pCurso.setAlignment(Element.ALIGN_CENTER);
        pCurso.setSpacingAfter(16f);
        ct.addElement(pCurso);

        // Nota y calificación
        String notaTexto = aprobado
                ? "con una nota de " + datos[4] + " puntos, obteniendo la calificación de APROBADO/A."
                : "con una nota de " + datos[4] + " puntos — calificación: " + datos[5].toUpperCase() + ".";
        Paragraph pNota = new Paragraph(notaTexto, fNormal);
        pNota.setAlignment(Element.ALIGN_CENTER);
        pNota.setSpacingAfter(12f);
        ct.addElement(pNota);

        // Período del curso
        if (datos[8] != null && datos[9] != null && !"-".equals(datos[8])) {
            Paragraph pPeriodo = new Paragraph(
                    "Período del curso: " + datos[8] + "  al  " + datos[9], fPequena);
            pPeriodo.setAlignment(Element.ALIGN_CENTER);
            pPeriodo.setSpacingAfter(4f);
            ct.addElement(pPeriodo);
        }

        // Instructor
        if (datos[7] != null) {
            Paragraph pInst = new Paragraph("Instructor: " + datos[7], fPequena);
            pInst.setAlignment(Element.ALIGN_CENTER);
            pInst.setSpacingAfter(4f);
            ct.addElement(pInst);
        }

        // Fecha de emisión y referencia
        Paragraph pFecha = new Paragraph(
                "Fecha de emisión: " + datos[6] + "   ·   Santa Cruz, Bolivia", fPequena);
        pFecha.setAlignment(Element.ALIGN_CENTER);
        pFecha.setSpacingBefore(6f);
        pFecha.setSpacingAfter(4f);
        ct.addElement(pFecha);

        Paragraph pRef = new Paragraph("Ref. Certificado N° " + datos[0], fPequena);
        pRef.setAlignment(Element.ALIGN_CENTER);
        ct.addElement(pRef);

        ct.go();
        doc.close();
        return out.toByteArray();
    }

    private static byte[] leerBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int n;
        while ((n = is.read(chunk)) != -1) buf.write(chunk, 0, n);
        return buf.toByteArray();
    }
}
