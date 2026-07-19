package com.san_andres.backend.report.infrastructure.adapter.output.excel;

import com.san_andres.backend.report.application.dto.request.TemplateRequest;
import com.san_andres.backend.report.domain.output.ExcelGeneratorPort;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class ApachePoiExcelAdapter implements ExcelGeneratorPort {

    private static final String NOMBRE_EMPRESA = "Colegio San Andrés";
    private static final Color COLOR_PRINCIPAL = new Color(16, 38, 74);
    private static final Color COLOR_DORADO = new Color(196, 160, 75);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(115, 126, 148);
    private static final Color COLOR_FILA_PAR = new Color(244, 246, 250);
    private static final Color COLOR_BORDE = new Color(206, 214, 228);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS_OSCURO = new Color(38, 42, 54);

    @Override
    public byte[] generateList(String title, List<String> headers, List<List<String>> rows) {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte");
            sheet.setDefaultColumnWidth(20);

            CellStyle eyebrowStyle = crearEstiloEyebrow(workbook);
            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle dateStyle = crearEstiloFecha(workbook);
            CellStyle headerStyle = crearEstiloHeader(workbook);
            CellStyle cellStyleImpar = crearEstiloCelda(workbook, BLANCO);
            CellStyle cellStylePar = crearEstiloCelda(workbook, COLOR_FILA_PAR);
            CellStyle resumenStyle = crearEstiloResumen(workbook);

            int rowIdx = 0;

            Row eyebrowRow = sheet.createRow(rowIdx++);
            eyebrowRow.setHeightInPoints(18);
            Cell eyebrowCell = eyebrowRow.createCell(0);
            eyebrowCell.setCellValue("REPORTE OFICIAL — " + NOMBRE_EMPRESA.toUpperCase(new Locale("es", "PE")));
            eyebrowCell.setCellStyle(eyebrowStyle);
            mergeRow(sheet, rowIdx - 1, headers.size());

            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.setHeightInPoints(28);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(titleStyle);
            mergeRow(sheet, rowIdx - 1, headers.size());

            String fecha = "Generado el " +
                    new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, hh:mm a", new Locale("es", "PE"))
                            .format(new Date());
            Row dateRow = sheet.createRow(rowIdx++);
            dateRow.setHeightInPoints(16);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(fecha);
            dateCell.setCellStyle(dateStyle);
            mergeRow(sheet, rowIdx - 1, headers.size());

            Row resumenRow = sheet.createRow(rowIdx++);
            resumenRow.setHeightInPoints(16);
            Cell resumenCell = resumenRow.createCell(0);
            resumenCell.setCellValue("Total de registros: " + rows.size());
            resumenCell.setCellStyle(resumenStyle);
            mergeRow(sheet, rowIdx - 1, headers.size());

            rowIdx++;

            int headerRowIdx = rowIdx;
            Row headerRow = sheet.createRow(rowIdx++);
            headerRow.setHeightInPoints(22);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i).toUpperCase(new Locale("es", "PE")));
                cell.setCellStyle(headerStyle);
            }

            int dataStartRow = rowIdx;
            int visualIndex = 0;
            for (List<String> data : rows) {
                Row row = sheet.createRow(rowIdx++);
                row.setHeightInPoints(18);
                CellStyle style = (visualIndex % 2 == 0) ? cellStyleImpar : cellStylePar;

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.createCell(i);
                    String value = (i < data.size() && data.get(i) != null) ? data.get(i) : "—";
                    cell.setCellValue(value);
                    cell.setCellStyle(style);
                }
                visualIndex++;
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512);
            }

            if (!rows.isEmpty()) {
                sheet.setAutoFilter(new CellRangeAddress(headerRowIdx, headerRowIdx, 0, headers.size() - 1));
            }

            sheet.createFreezePane(0, dataStartRow);
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    private CellStyle crearEstiloEyebrow(Workbook wb) {
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);
        font.setColor(rgbColor(wb, COLOR_DORADO.darker()));

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private CellStyle crearEstiloTitulo(Workbook wb) {
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        font.setColor(rgbColor(wb, GRIS_OSCURO));

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private CellStyle crearEstiloFecha(Workbook wb) {
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setItalic(true);
        font.setColor(rgbColor(wb, COLOR_TEXTO_SECUNDARIO));

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        return style;
    }

    private CellStyle crearEstiloResumen(Workbook wb) {
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setColor(rgbColor(wb, COLOR_TEXTO_SECUNDARIO));

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        return style;
    }

    private CellStyle crearEstiloHeader(Workbook wb) {
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        font.setColor(rgbColor(wb, BLANCO));

        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(rgbColor(wb, COLOR_PRINCIPAL));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBottomBorderColor(rgbColor(wb, COLOR_DORADO));

        return style;
    }

    private CellStyle crearEstiloCelda(Workbook wb, Color fondo) {
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(rgbColor(wb, GRIS_OSCURO));

        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(rgbColor(wb, fondo));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(rgbColor(wb, COLOR_BORDE));

        return style;
    }

    private XSSFColor rgbColor(Workbook wb, Color color) {
        return new XSSFColor(color, null);
    }

    private void mergeRow(Sheet sheet, int rowIdx, int columnCount) {
        if (columnCount > 1) {
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, columnCount - 1));
        }
    }

    @Override
    public byte[] generateTemplate(TemplateRequest request) {

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Plantilla");
            XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();

            XSSFFont headerFont = (XSSFFont) workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontHeightInPoints((short) 11);

            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(new XSSFColor(new Color(16, 38, 74), null));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(24);

            for (int i = 0; i < request.getHeaders().size(); i++) {

                Cell cell = headerRow.createCell(i);
                cell.setCellValue(request.getHeaders().get(i));
                cell.setCellStyle(headerStyle);

                sheet.setColumnWidth(i, 20 * 256);
            }

            AreaReference reference = new AreaReference(
                    new CellReference(0, 0),
                    new CellReference(1, request.getHeaders().size() - 1),
                    SpreadsheetVersion.EXCEL2007);

            XSSFTable table = sheet.createTable(reference);
            table.setName("TablaPlantilla");
            table.setDisplayName("TablaPlantilla");

            CTTable ctTable = table.getCTTable();
            ctTable.setId(1);
            ctTable.setTotalsRowShown(false);

            CTTableStyleInfo style = ctTable.addNewTableStyleInfo();
            style.setName("TableStyleMedium2");
            style.setShowRowStripes(true);
            style.setShowColumnStripes(false);

            sheet.createFreezePane(0, 1);

            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando la plantilla.", e);
        }
    }

}