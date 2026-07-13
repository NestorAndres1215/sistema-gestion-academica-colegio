package com.san_andres.backend.infrastructure.reports;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReader {


    public List<List<String>> read(MultipartFile file) throws Exception {

        List<List<String>> rows = new ArrayList<>();

        Workbook workbook =
                WorkbookFactory.create(file.getInputStream());


        Sheet sheet = workbook.getSheetAt(0);
        System.out.println("HOJA ACTUAL: " + sheet.getSheetName());
        System.out.println("ULTIMA FILA: " + sheet.getLastRowNum());
        System.out.println("PRIMERA FILA: " + sheet.getFirstRowNum());

        for (Row row : sheet) {

            System.out.println(
                    "ROW NUMERO: " + row.getRowNum() +
                            " CELDAS: " + row.getPhysicalNumberOfCells()
            );
            // saltar cabecera
            if(row.getRowNum() == 0){
                continue;
            }


            List<String> columns = new ArrayList<>();


            for(Cell cell : row){

                String value = getValue(cell);

                System.out.println(
                        "Fila: " + row.getRowNum()
                                + " Columna: " + cell.getColumnIndex()
                                + " Valor: [" + value + "]"
                );

                columns.add(value);

            }


            System.out.println("ROW COMPLETA: " + columns);

            rows.add(columns);

        }


        workbook.close();


        System.out.println("TOTAL FILAS LEIDAS: " + rows.size());


        return rows;
    }

    private String getValue(Cell cell){

        if(cell == null){
            return "";
        }

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell)
                .trim();
    }
}