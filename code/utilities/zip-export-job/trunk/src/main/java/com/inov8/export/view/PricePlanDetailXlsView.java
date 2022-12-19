package com.inov8.export.view;

import com.inov8.export.vo.PricePlanDetailVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.extremecomponents.table.view.ExportViewUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by atieq.rehman on 3/28/2018.
 */
public class PricePlanDetailXlsView extends XlsView {

    public void generateDetailSheets(Workbook workbook, Map<String, CellStyle> styles, Map<Long, List<PricePlanDetailVO>> data) {

        for (Map.Entry<Long, List<PricePlanDetailVO>> e : data.entrySet()) {
            List<PricePlanDetailVO> detail = data.get(e.getKey());

            Sheet sheet = workbook.createSheet(detail.get(0).getTitle());
            createHeader(sheet, styles, detail.get(0));

            int rowNum = 8;

            for (PricePlanDetailVO column : detail) {
                Row row = sheet.createRow(rowNum++);
                int cellNum = 1;
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum++, column.getSlabFrom());
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum++, column.getSlabTo());
                createCell(sheet, row, styles.get("numericStyle"), 1, cellNum++, column.getFixed());
                createCell(sheet, row, styles.get("numericStyle"), 1, cellNum++, column.getInclusive());
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum++, column.getMinValue());
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum++, column.getMaxValue());
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum, column.getFee());
            }
        }
    }

    void createHeader(Sheet sheet, Map<String, CellStyle> styles, PricePlanDetailVO vo) {

        Row row = sheet.createRow(1);
        createCell(sheet, row, styles.get("textStyle"), 1, 1, "Title");
        createCell(sheet, row, styles.get("textStyle"), 1, 2, vo.getTitle());
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 7));

        row = sheet.createRow(3);
        createCell(sheet, row, styles.get("textStyle"), 1, 1, "Plan Type");
        createCell(sheet, row, styles.get("textStyle"), 1, 2, vo.getPlanType());
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));

        createCell(sheet, row, styles.get("textStyle"), 1, 6, "Usage Type");
        createCell(sheet, row, styles.get("textStyle"), 1, 7, vo.getUsageType());

        row = sheet.createRow(5);
        createCell(sheet, row, styles.get("textStyle"), 1, 1, "Tx Activation No.");
        createCell(sheet, row, styles.get("textStyle"), 1, 2, vo.getActivationFrom());

        createCell(sheet, row, styles.get("textStyle"), 1, 6, "Max. Tx. Amount");
        createCell(sheet, row, styles.get("textStyle"), 1, 7, vo.getMaxTxAmount());

        row = sheet.createRow(7);
        List<String> columns = Arrays.asList("Slab From", "Slab To", "Price Structure", "Inc./Exc.", "Min Value", "Max Value", "Fee Levied");

        int cellNum = 1;
        for (String str : columns) {
            createCell(sheet, row, styles.get("titleStyle"), 1, cellNum ++, str);
        }
    }
}
