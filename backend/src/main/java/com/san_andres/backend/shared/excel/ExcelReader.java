package com.san_andres.backend.shared.excel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReader {

    public List<List<String>> read(MultipartFile file) throws Exception {

        List<List<String>> rows = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {

            if (row.getRowNum() == 0) {
                continue;
            }

            List<String> columns = new ArrayList<>();

            for (Cell cell : row) {

                String value = getValue(cell);
                columns.add(value);

            }

            rows.add(columns);

        }

        workbook.close();

        return rows;
    }

    private String getValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell)
                .trim();
    }
}