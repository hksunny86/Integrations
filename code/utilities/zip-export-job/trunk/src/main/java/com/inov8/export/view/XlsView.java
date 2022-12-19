package com.inov8.export.view;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.extremecomponents.table.view.ExportViewUtils;

/**
 * Created by atieq.rehman on 3/30/2018.
 */
public class XlsView {

    protected static int DEFAULT_CELL_WIDTH = 10 * 240;

    void createCell(Sheet sheet, Row row, CellStyle style, int cellType, int cellnum, Object cellValue) {
        Cell cell = row.createCell(cellnum);
        cell.setCellStyle(style);
        cell.setCellType(cellType);

        String value = cellValue != null ? cellValue.toString() : "";
        value = ExportViewUtils.parseXLS(value);
        cell.setCellValue(value);

        if (cellType == 1) {
            int valWidth = value.length() == 0 ? DEFAULT_CELL_WIDTH : value.length() * 240;

            if (valWidth <= DEFAULT_CELL_WIDTH)
                valWidth = DEFAULT_CELL_WIDTH;

            sheet.setColumnWidth(cell.getColumnIndex(), valWidth);
        } else {
            sheet.setColumnWidth(cell.getColumnIndex(), DEFAULT_CELL_WIDTH);
        }
    }
}
