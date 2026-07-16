package com.san_andres.backend.infrastructure.persistence.adapters;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Company;
import com.san_andres.backend.domain.port.repositories.PdfGeneratorPort;
import com.san_andres.backend.domain.port.usecases.CompanyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class OpenPdfAdapter implements PdfGeneratorPort {

    private final CompanyUseCase companyUseCase;
    private static final String SUBTITULO_EMPRESA = "Sistema de Gestión Educativa";
    private static final Color COLOR_PRINCIPAL = new Color(16, 38, 74);
    private static final Color COLOR_PRINCIPAL_OSCURO = new Color(10, 26, 53);
    private static final Color COLOR_DORADO = new Color(196, 160, 75);
    private static final Color COLOR_DORADO_SUAVE = new Color(224, 199, 138);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(115, 126, 148);
    private static final Color COLOR_FILA_PAR = new Color(244, 246, 250);
    private static final Color COLOR_BORDE = new Color(224, 229, 238);
    private static final Color COLOR_BORDE_TABLA = new Color(206, 214, 228);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS_OSCURO = new Color(38, 42, 54);

    private static final float BAND_HEIGHT = 100f;
    private static final float PAGE_WIDTH = PageSize.A4.getWidth();
    private static final float PAGE_HEIGHT = PageSize.A4.getHeight();


    private Company getCompany() {
        return companyUseCase.findById("COMP0001");
    }

    @Override
    public byte[] generateList(String title, List<String> headers, List<List<String>> rows) {

        Company company = getCompany();

        return generateList(title, headers, rows, company.getName(), cargarLogoDesdeUrl(company.getLogo()));
    }


    @Override
    public byte[] generateListId(String title, List<String> headers, List<List<String>> rows) {

        Company company = getCompany();

        return generateConstancia(title, headers, rows, company.getName(), cargarLogoDesdeUrl(company.getLogo()));
    }


    public byte[] generateList(String title, List<String> headers, 
                               List<List<String>> rows, String nombreEmpresa, byte[] logoBytes) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 42, 42, BAND_HEIGHT + 34, 60);

            PdfWriter writer = PdfWriter.getInstance(document, out);
            FooterEvent footerEvent = new FooterEvent(nombreEmpresa, logoBytes);
            writer.setPageEvent(footerEvent);

            document.open();

            dibujarBandaMarca(writer.getDirectContentUnder(), nombreEmpresa, logoBytes);
            dibujarMarcaDeAgua(writer.getDirectContentUnder(), logoBytes);

            // ---------- Fuentes ----------
            Font fontEyebrow = new Font(Font.HELVETICA, 8.5f, Font.BOLD, COLOR_DORADO.darker());
            Font fontTitulo = new Font(Font.HELVETICA, 19, Font.BOLD, GRIS_OSCURO);
            Font fontFecha = new Font(Font.HELVETICA, 9, Font.ITALIC, COLOR_TEXTO_SECUNDARIO);
            Font fontHeader = new Font(Font.HELVETICA, 8.5f, Font.BOLD, BLANCO);
            Font fontCelda = new Font(Font.HELVETICA, 9.5f, Font.NORMAL, GRIS_OSCURO);
            Font fontResumen = new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_TEXTO_SECUNDARIO);

            // ---------- Encabezado editorial (eyebrow + título + fecha) ----------
            Paragraph eyebrow = new Paragraph("REPORTE OFICIAL", fontEyebrow);
            eyebrow.setSpacingAfter(4f);
            document.add(eyebrow);

            Paragraph titleParagraph = new Paragraph(title, fontTitulo);
            titleParagraph.setSpacingAfter(4f);
            document.add(titleParagraph);

            String fechaGeneracion = "Generado el " +
                    new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, hh:mm a", new Locale("es", "PE"))
                            .format(new Date());

            Paragraph dateParagraph = new Paragraph(fechaGeneracion, fontFecha);
            dateParagraph.setSpacingAfter(6f);
            document.add(dateParagraph);

            // Línea divisoria fina dorada bajo el encabezado, antes de la tabla
            document.add(construirLineaDivisoria());

            Paragraph resumen = new Paragraph("Total de registros: " + rows.size(), fontResumen);
            resumen.setSpacingBefore(10f);
            resumen.setSpacingAfter(10f);
            document.add(resumen);

            // ---------- Tabla de datos ----------
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);
            table.setHeaderRows(1);
            table.setSpacingBefore(0f);

            float[] columnWidths = new float[headers.size()];
            Arrays.fill(columnWidths, 1f);
            table.setWidths(columnWidths);

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(
                        new Phrase(header.toUpperCase(new Locale("es", "PE")), fontHeader)
                );
                headerCell.setBackgroundColor(COLOR_PRINCIPAL);
                headerCell.setBorder(Rectangle.BOTTOM);
                headerCell.setBorderColor(COLOR_DORADO);
                headerCell.setBorderWidth(1.75f);
                headerCell.setPaddingTop(10f);
                headerCell.setPaddingBottom(10f);
                headerCell.setPaddingLeft(9f);
                headerCell.setPaddingRight(9f);
                headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(headerCell);
            }

            int rowIndex = 0;
            for (List<String> row : rows) {
                Color rowColor = (rowIndex % 2 == 0) ? BLANCO : COLOR_FILA_PAR;

                for (String value : row) {
                    PdfPCell cell = new PdfPCell(
                            new Phrase(value != null ? value : "—", fontCelda)
                    );
                    cell.setBackgroundColor(rowColor);
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setBorderColor(COLOR_BORDE_TABLA);
                    cell.setBorderWidth(0.75f);
                    cell.setPaddingTop(8.5f);
                    cell.setPaddingBottom(8.5f);
                    cell.setPaddingLeft(9f);
                    cell.setPaddingRight(9f);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                }
                rowIndex++;
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }


    public byte[] generateConstancia(String title, List<String> headers,
                                     List<List<String>> rows, String nombreEmpresa, byte[] logoBytes
    ) {
        if (rows == null || rows.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Se requiere al menos un registro (fila) con los datos de la persona para generar la constancia.");
        }
        List<String> datos = rows.get(0);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 42, 42, BAND_HEIGHT + 34, 60);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            FooterEvent footerEvent = new FooterEvent(nombreEmpresa, logoBytes);
            writer.setPageEvent(footerEvent);

            document.open();

            dibujarBandaMarca(writer.getDirectContentUnder(), nombreEmpresa, logoBytes);
            dibujarMarcaDeAgua(writer.getDirectContentUnder(), logoBytes);

            // ---------- Fuentes ----------
            Font fontEyebrow = new Font(Font.HELVETICA, 8.5f, Font.BOLD, COLOR_DORADO.darker());
            Font fontTituloConstancia = new Font(Font.HELVETICA, 21, Font.BOLD, GRIS_OSCURO);
            Font fontCodigo = new Font(Font.HELVETICA, 9, Font.ITALIC, COLOR_TEXTO_SECUNDARIO);
            Font fontCuerpo = new Font(Font.HELVETICA, 10.5f, Font.NORMAL, GRIS_OSCURO);
            Font fontLabel = new Font(Font.HELVETICA, 8.5f, Font.BOLD, COLOR_TEXTO_SECUNDARIO);
            Font fontValor = new Font(Font.HELVETICA, 11, Font.NORMAL, GRIS_OSCURO);
            Font fontFirmaNombre = new Font(Font.HELVETICA, 10, Font.BOLD, GRIS_OSCURO);
            Font fontFirmaCargo = new Font(Font.HELVETICA, 8.5f, Font.NORMAL, COLOR_TEXTO_SECUNDARIO);

            String codigo = generarCodigoConstancia();

            // ---------- Encabezado del documento ----------
            Paragraph eyebrow = new Paragraph("DOCUMENTO OFICIAL", fontEyebrow);
            eyebrow.setAlignment(Element.ALIGN_CENTER);
            eyebrow.setSpacingAfter(6f);
            document.add(eyebrow);

            String tituloTexto = (title != null && !title.isBlank())
                    ? title.toUpperCase(new Locale("es", "PE"))
                    : "CONSTANCIA";
            Paragraph tituloConstancia = new Paragraph(tituloTexto, fontTituloConstancia);
            tituloConstancia.setAlignment(Element.ALIGN_CENTER);
            tituloConstancia.setSpacingAfter(4f);
            document.add(tituloConstancia);

            Paragraph codigoParrafo = new Paragraph("N.° " + codigo, fontCodigo);
            codigoParrafo.setAlignment(Element.ALIGN_CENTER);
            codigoParrafo.setSpacingAfter(8f);
            document.add(codigoParrafo);

            document.add(construirLineaCentradaDorada());

            // ---------- Párrafo introductorio ----------
            Paragraph introduccion = new Paragraph(
                    "La Dirección de la institución educativa " + nombreEmpresa + " hace constar que, según los " +
                            "registros que obran en el sistema de gestión educativa, la persona identificada a " +
                            "continuación cuenta con la siguiente información:",
                    fontCuerpo);
            introduccion.setAlignment(Element.ALIGN_JUSTIFIED);
            introduccion.setSpacingBefore(22f);
            introduccion.setSpacingAfter(20f);
            document.add(introduccion);

            // ---------- Ficha de datos de la persona ----------
            document.add(construirBloqueDatos(headers, datos, fontLabel, fontValor));

            // ---------- Párrafo de cierre ----------
            Paragraph cierre = new Paragraph(
                    "Se expide la presente constancia a solicitud de la parte interesada, para los fines que " +
                            "estime convenientes.",
                    fontCuerpo);
            cierre.setAlignment(Element.ALIGN_JUSTIFIED);
            cierre.setSpacingBefore(24f);
            cierre.setSpacingAfter(34f);
            document.add(cierre);

            String lugarFecha = "Lima, " +
                    new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "PE")).format(new Date());
            Paragraph fechaParrafo = new Paragraph(lugarFecha, fontCuerpo);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);
            fechaParrafo.setSpacingAfter(48f);
            document.add(fechaParrafo);

            // ---------- Bloque de firma ----------
            document.add(construirBloqueFirma(nombreEmpresa, fontFirmaNombre, fontFirmaCargo));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando constancia PDF", e);
        }
    }

    private PdfPTable construirLineaDivisoria() {
        PdfPTable linea = new PdfPTable(2);
        try {
            linea.setWidths(new float[]{6f, 94f});
            linea.setWidthPercentage(100);
        } catch (DocumentException ignored) {
        }

        PdfPCell dorada = new PdfPCell();
        dorada.setFixedHeight(2.2f);
        dorada.setBackgroundColor(COLOR_DORADO);
        dorada.setBorder(Rectangle.NO_BORDER);

        PdfPCell gris = new PdfPCell();
        gris.setFixedHeight(2.2f);
        gris.setBackgroundColor(COLOR_BORDE);
        gris.setBorder(Rectangle.NO_BORDER);

        linea.addCell(dorada);
        linea.addCell(gris);
        return linea;
    }

    private String generarCodigoConstancia() {
        String anio = new SimpleDateFormat("yyyy").format(new Date());
        long sufijo = System.currentTimeMillis() % 1_000_000L;
        return String.format(new Locale("es", "PE"), "%s-%06d", anio, sufijo);
    }

    private PdfPTable construirLineaCentradaDorada() throws DocumentException {
        PdfPTable linea = new PdfPTable(3);
        linea.setWidths(new float[]{42f, 16f, 42f});
        linea.setWidthPercentage(100);

        PdfPCell vacioIzq = new PdfPCell();
        vacioIzq.setBorder(Rectangle.NO_BORDER);
        vacioIzq.setFixedHeight(2f);

        PdfPCell dorada = new PdfPCell();
        dorada.setBackgroundColor(COLOR_DORADO);
        dorada.setBorder(Rectangle.NO_BORDER);
        dorada.setFixedHeight(2f);

        PdfPCell vacioDer = new PdfPCell();
        vacioDer.setBorder(Rectangle.NO_BORDER);
        vacioDer.setFixedHeight(2f);

        linea.addCell(vacioIzq);
        linea.addCell(dorada);
        linea.addCell(vacioDer);
        return linea;
    }


    private PdfPTable construirBloqueDatos(
            List<String> headers, List<String> datos, Font fontLabel, Font fontValor
    ) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{38f, 62f});
        tabla.setSpacingBefore(0f);

        int n = Math.min(headers.size(), datos.size());
        for (int i = 0; i < n; i++) {
            String label = headers.get(i);
            String valor = datos.get(i) != null ? datos.get(i) : "—";

            PdfPCell labelCell = new PdfPCell(
                    new Phrase(label.toUpperCase(new Locale("es", "PE")), fontLabel));
            labelCell.setBackgroundColor(COLOR_FILA_PAR);
            labelCell.setBorder(Rectangle.BOTTOM);
            labelCell.setBorderColor(COLOR_BORDE_TABLA);
            labelCell.setBorderWidth(0.75f);
            labelCell.setPaddingTop(11f);
            labelCell.setPaddingBottom(11f);
            labelCell.setPaddingLeft(12f);
            labelCell.setPaddingRight(8f);
            labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(labelCell);

            PdfPCell valorCell = new PdfPCell(new Phrase(valor, fontValor));
            valorCell.setBorder(Rectangle.BOTTOM);
            valorCell.setBorderColor(COLOR_BORDE_TABLA);
            valorCell.setBorderWidth(0.75f);
            valorCell.setPaddingTop(11f);
            valorCell.setPaddingBottom(11f);
            valorCell.setPaddingLeft(12f);
            valorCell.setPaddingRight(12f);
            valorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(valorCell);
        }
        return tabla;
    }

    private PdfPTable construirBloqueFirma(String nombreEmpresa, Font fontNombre, Font fontCargo)
            throws DocumentException {
        PdfPTable contenedor = new PdfPTable(3);
        contenedor.setWidths(new float[]{25f, 50f, 25f});
        contenedor.setWidthPercentage(100);

        PdfPCell vacioIzq = new PdfPCell();
        vacioIzq.setBorder(Rectangle.NO_BORDER);

        Paragraph nombreCargo = new Paragraph("Dirección Académica", fontNombre);
        nombreCargo.setAlignment(Element.ALIGN_CENTER);
        Paragraph institucion = new Paragraph(nombreEmpresa, fontCargo);
        institucion.setAlignment(Element.ALIGN_CENTER);

        PdfPCell firmaContenido = new PdfPCell();
        firmaContenido.setBorder(Rectangle.TOP);
        firmaContenido.setBorderColor(GRIS_OSCURO);
        firmaContenido.setBorderWidth(0.75f);
        firmaContenido.setPaddingTop(6f);
        firmaContenido.setUseAscender(true);
        firmaContenido.addElement(nombreCargo);
        firmaContenido.addElement(institucion);

        PdfPCell vacioDer = new PdfPCell();
        vacioDer.setBorder(Rectangle.NO_BORDER);

        contenedor.addCell(vacioIzq);
        contenedor.addCell(firmaContenido);
        contenedor.addCell(vacioDer);
        return contenedor;
    }

    private void dibujarBandaMarca(PdfContentByte cb, String nombreEmpresa, byte[] logoBytes)
            throws DocumentException {
        cb.saveState();
        int franjas = 40;
        float alturaFranja = BAND_HEIGHT / franjas;
        for (int i = 0; i < franjas; i++) {
            float t = i / (float) franjas;
            Color color = interpolarColor(COLOR_PRINCIPAL_OSCURO, COLOR_PRINCIPAL, t);
            cb.setColorFill(color);
            float y = PAGE_HEIGHT - BAND_HEIGHT + i * alturaFranja;
            cb.rectangle(0, y, PAGE_WIDTH, alturaFranja + 0.5f);
            cb.fill();
        }
        cb.restoreState();
        cb.saveState();
        cb.setColorFill(COLOR_DORADO);
        cb.rectangle(0, PAGE_HEIGHT - BAND_HEIGHT - 3f, PAGE_WIDTH, 3f);
        cb.fill();
        cb.restoreState();

        float textX = 42f;

        if (logoBytes != null) {
            try {
                float discoRadio = 30f;
                float discoCx = 42f + discoRadio;
                float discoCy = PAGE_HEIGHT - BAND_HEIGHT / 2f;

                // 1. Disco blanco de fondo
                cb.saveState();
                cb.setColorFill(BLANCO);
                cb.circle(discoCx, discoCy, discoRadio);
                cb.fill();
                cb.restoreState();

                // 2. Recortar (clip) la imagen en forma circular
                cb.saveState();
                cb.circle(discoCx, discoCy, discoRadio - 2f); // -2f deja un pequeño margen/borde blanco visible
                cb.clip();
                cb.newPath(); // IMPORTANTE: limpia el path actual después de aplicar el clip

                Image logo = Image.getInstance(logoBytes);
                // Aseguramos que el logo cubra bien el círculo (un poco más grande que el diámetro
                // para que no queden bordes blancos raros del propio PNG, ya que el clip lo recorta)
                float logoSize = (discoRadio - 2f) * 2f * 1.15f;
                logo.scaleToFit(logoSize, logoSize);
                float logoX = discoCx - logo.getScaledWidth() / 2f;
                float logoY = discoCy - logo.getScaledHeight() / 2f;
                logo.setAbsolutePosition(logoX, logoY);
                cb.addImage(logo);
                cb.restoreState();

                textX = 42f + discoRadio * 2f + 18f;
            } catch (Exception ignored) {
                // Si el logo no se puede decodificar, seguimos solo con el texto.
            }
        }

        Font fontEmpresa = new Font(Font.HELVETICA, 17, Font.BOLD, BLANCO);
        Font fontSubtitulo = new Font(Font.HELVETICA, 9.5f, Font.NORMAL, COLOR_DORADO_SUAVE);

        ColumnText.showTextAligned(
                cb, Element.ALIGN_LEFT,
                new Phrase(nombreEmpresa, fontEmpresa),
                textX, PAGE_HEIGHT - BAND_HEIGHT / 2f + 6f, 0
        );

        ColumnText.showTextAligned(
                cb, Element.ALIGN_LEFT,
                new Phrase(SUBTITULO_EMPRESA, fontSubtitulo),
                textX, PAGE_HEIGHT - BAND_HEIGHT / 2f - 11f, 0
        );
    }

    private void dibujarMarcaDeAgua(PdfContentByte cb, byte[] logoBytes) {
        if (logoBytes == null) {
            return;
        }
        try {
            Image logo = Image.getInstance(logoBytes);
            float size = 260f;
            logo.scaleToFit(size, size);

            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.045f);

            cb.saveState();
            cb.setGState(gs);
            float x = (PAGE_WIDTH - logo.getScaledWidth()) / 2f;
            float y = (PAGE_HEIGHT - BAND_HEIGHT - logo.getScaledHeight()) / 2f;
            logo.setAbsolutePosition(x, y);
            cb.addImage(logo);
            cb.restoreState();
        } catch (Exception ignored) {
            // Si falla, simplemente no se dibuja marca de agua.
        }
    }

    private static Color interpolarColor(Color desde, Color hasta, float t) {
        int r = (int) (desde.getRed() + (hasta.getRed() - desde.getRed()) * t);
        int g = (int) (desde.getGreen() + (hasta.getGreen() - desde.getGreen()) * t);
        int b = (int) (desde.getBlue() + (hasta.getBlue() - desde.getBlue()) * t);
        return new Color(r, g, b);
    }

    private byte[] cargarLogoDesdeUrl(String url) {
        try {
            java.net.URL logoUrl = java.net.URI.create(url).toURL();
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) logoUrl.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != java.net.HttpURLConnection.HTTP_OK) {
                return null;
            }

            try (var is = conn.getInputStream()) {
                return is.readAllBytes();
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static class FooterEvent extends PdfPageEventHelper {

        private final String nombreEmpresa;
        private final byte[] logoBytes;
        private final Font fontFooter = new Font(Font.HELVETICA, 8, Font.NORMAL, COLOR_TEXTO_SECUNDARIO);
        private final Font fontFooterMuted = new Font(Font.HELVETICA, 7.5f, Font.ITALIC, COLOR_TEXTO_SECUNDARIO);
        private final BaseFont baseFontFooter;
        private PdfTemplate totalPagesTemplate;

        FooterEvent(String nombreEmpresa, byte[] logoBytes) {
            this.nombreEmpresa = nombreEmpresa;
            this.logoBytes = logoBytes;
            try {
                this.baseFontFooter = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                throw new ResourceNotFoundException("No se pudo crear la fuente base para el footer del PDF");
            }
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPagesTemplate = writer.getDirectContent().createTemplate(40, 20);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            float footerY = 40f;

            cb.saveState();
            cb.setColorStroke(COLOR_BORDE);
            cb.setLineWidth(0.75f);
            cb.moveTo(42f, footerY + 16f);
            cb.lineTo(PAGE_WIDTH - 42f, footerY + 16f);
            cb.stroke();
            cb.restoreState();

            float leftX = 42f;

            if (logoBytes != null) {
                try {
                    Image logo = Image.getInstance(logoBytes);
                    float logoSize = 14f;
                    logo.scaleToFit(logoSize, logoSize);
                    logo.setAbsolutePosition(leftX, footerY - 1f);
                    cb.addImage(logo);
                    leftX += logoSize + 6f;
                } catch (Exception ignored) {
                    // sin logo, solo texto
                }
            }

            ColumnText.showTextAligned(
                    cb, Element.ALIGN_LEFT,
                    new Phrase(nombreEmpresa, fontFooter),
                    leftX, footerY, 0
            );

            ColumnText.showTextAligned(
                    cb, Element.ALIGN_CENTER,
                    new Phrase("Documento generado automáticamente — uso interno", fontFooterMuted),
                    PAGE_WIDTH / 2f, footerY, 0
            );

            ColumnText.showTextAligned(
                    cb, Element.ALIGN_RIGHT,
                    new Phrase("Página " + writer.getPageNumber() + " de ", fontFooter),
                    PAGE_WIDTH - 42f - 20f, footerY, 0
            );
            cb.addTemplate(totalPagesTemplate, PAGE_WIDTH - 42f - 18f, footerY - 2f);
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            totalPagesTemplate.beginText();
            totalPagesTemplate.setFontAndSize(baseFontFooter, 8);
            totalPagesTemplate.setColorFill(COLOR_TEXTO_SECUNDARIO);
            totalPagesTemplate.setTextMatrix(0, 2);
            totalPagesTemplate.showText(String.valueOf(writer.getPageNumber()));
            totalPagesTemplate.endText();
        }
    }
}