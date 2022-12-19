package com.inov8.export.view;

import com.inov8.export.common.EncryptionUtil;
import com.inov8.export.model.ExportInformationModel;
import com.inov8.export.model.ExportRequestModel;
import com.inov8.export.service.MultiSheetViewFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.view.XlsView;
import org.extremecomponents.table.view.XlsxView;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by NaseerUl on 9/19/2016.
 */
public class CustomXlsxView {

    public String export(ExportRequestModel exportRequestModel, ExportInformationModel exportInfoModel,
                         ResultSet rowSet, ResultSet totalsRowSet, Connection connection) throws Exception {
        int columnCount = rowSet.getMetaData().getColumnCount();

        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {
            Map<String, CellStyle> styles = initStyles(workbook, XlsxView.DEFAULT_FONT_HEIGHT);

            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("Export Workbook");
            PrintSetup ps = sheet.getPrintSetup();

            Set<Integer> balanceColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getBalaceCellColumnsIndexes());
            Set<Integer> accountNoColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getAccountCellColumnsIndexes());
            Set<Integer> cnicColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getCnicCellColumnsIndexes());
            Set<Integer> dobColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getDobCellColumnsIndexes());

            sheet.setAutobreaks(true);
            ps.setFitHeight((short) 1);
            ps.setFitWidth((short) 1);

            Long reportId = exportInfoModel.getReportId();
            boolean multiSheetExport = reportId != null && (reportId == 48 || reportId == 49);

            Row row = sheet.createRow(0);
            int rowIdx = 0;
            String columns[] = exportInfoModel.getColumnsTitles().split(",");
            for (int colIdx=1; colIdx <= columnCount; colIdx++) {

                String title = columns[colIdx - 1];
                if (colIdx == columnCount && ((reportId == 48 || reportId == 49))) {
                    // Last column is expected to be having id of reference record so to skip header for this column
                    break;
                }

                Cell cell = row.createCell(rowIdx);
                cell.setCellStyle(styles.get("titleStyle"));
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(title);
                int valWidth = title.length() * XlsView.WIDTH_MULT;
                sheet.setColumnWidth(cell.getColumnIndex(), valWidth);
                rowIdx++;
            }

            rowIdx = 1;
            List<Long> ids = new ArrayList<>();
            String[] props = exportInfoModel.getColumnsProps().split(",");

            while (rowSet.next()) {
                row = sheet.createRow(rowIdx);
                for (int colIdx = 1; colIdx <= columnCount; colIdx++) {
                    String value = rowSet.getString(colIdx);
                    value = null == value ? "" : value;
                    // last column is expected to be having desired id [not a good way as we dont want to display ids on interface]
                    if (colIdx == columnCount) {
                        if ((reportId == 48 || reportId == 49) && !value.equals("")) {
                            ids.add(Long.valueOf(value));
                            break;
                        }
                    }

                    Cell cell = row.createCell(colIdx - 1);
                    if (TableConstants.CURRENCY.equals(exportInfoModel.getFormat(props[colIdx - 1]))) {
                        cell.setCellStyle(styles.get("numericStyle"));
                    } else {
                        cell.setCellStyle(styles.get("textStyle"));
                    }

                    if (null != balanceColumnsIndexesSet && balanceColumnsIndexesSet.contains(colIdx)) {
                        if (!value.trim().isEmpty()) {
                            value = EncryptionUtil.decryptPin(value);
                        }
                    } else if (null != accountNoColumnsIndexesSet && accountNoColumnsIndexesSet.contains(colIdx)) {
                        if (!value.trim().isEmpty()) {
                            value = EncryptionUtil.decryptAccountNo(value);
                        }
                    }
                    else if (null != cnicColumnsIndexesSet && cnicColumnsIndexesSet.contains(colIdx)) {
                            if (!value.trim().isEmpty()) {
                                value = EncryptionUtil.decryptPin(value);
                            }
                        }

                        else if (null != dobColumnsIndexesSet && dobColumnsIndexesSet.contains(colIdx)) {
                        if (!value.trim().isEmpty()) {
                            value = EncryptionUtil.decryptPin(value);
                        }
                    }

                    if (StringUtils.contains(value, "<br/>")) {
                        value = StringUtils.replace(value, "<br/>", " ");
                    }

                    cell.setCellValue(value);
                    int valWidth = value.length() * XlsxView.WIDTH_MULT;
                    if (valWidth > sheet.getColumnWidth(cell.getColumnIndex())) {
                        sheet.setColumnWidth(cell.getColumnIndex(), valWidth);
                    }
                }

                if (rowIdx % 100 == 0) {
                    sheet.flushRows(100);
                }

                rowIdx++;
            }

            if (totalsRowSet != null && totalsRowSet.next()) {
                row = sheet.createRow(rowIdx);
                for (int colIdx = 1; colIdx <= columnCount; colIdx++) {
                    String value = totalsRowSet.getString(colIdx);
                    Cell cell = row.createCell((colIdx - 1));
                    cell.setCellStyle(styles.get("numericStyle_Totals"));
                    if (colIdx == 1) {
                        cell.setCellValue("Total:" + value);
                    } else {
                        cell.setCellValue(value);
                    }
                }
            }

            if (multiSheetExport) {
                generateDetailSheets(connection, workbook, styles, exportRequestModel, ids);
            }

        } catch (Exception e) {
            throw new Exception("Unable to generate file. Error: " + e.getMessage());
        }

        File zipReportsDir = new File(exportInfoModel.getExportDirPath() + File.separator + exportRequestModel.getUsername());
        Path path = Paths.get(exportInfoModel.getExportDirPath() + File.separator + exportRequestModel.getUsername());
        boolean pathExists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        if (!pathExists)
            Files.createDirectory(path);

        String xlsFileName = exportInfoModel.getExportFileName() + "_" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + "_XLSX.xlsx";
        File file = new File(zipReportsDir, xlsFileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Unable to write file. Error: " + ex.getMessage());
        }

        return file.getPath();
    }

    private void generateDetailSheets(Connection connection, SXSSFWorkbook workbook, Map<String, CellStyle> styles, ExportRequestModel exportRequestModel, List<Long> ids) {
        MultiSheetViewFactory factory = new MultiSheetViewFactory(connection);
        factory.generateDetailSheets(exportRequestModel, workbook, styles, ids);
    }

    private Map<String, CellStyle> initStyles(SXSSFWorkbook wb, short fontHeight) {
        Map<String, CellStyle> result = new HashMap<>();
        CellStyle titleStyle = wb.createCellStyle();
        CellStyle textStyle = wb.createCellStyle();
        CellStyle numericStyle = wb.createCellStyle();
        // Add to export totals
        CellStyle numericStyle_Totals = wb.createCellStyle();

        result.put("titleStyle", titleStyle);
        result.put("textStyle", textStyle);
        result.put("numericStyle", numericStyle);
        result.put("numericStyle_Totals", numericStyle_Totals);

        // Global fonts
        Font font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);

        Font fontBold = wb.createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontBold.setColor(HSSFColor.BLACK.index);
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setFontHeightInPoints(fontHeight);

        // Standard Numeric Style
        numericStyle.setFont(font);
        numericStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        numericStyle.setLocked(false);

        // Title Style
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(CellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderTop(CellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // Standard Text Style
        textStyle.setFont(font);
        textStyle.setWrapText(true);
        textStyle.setLocked(false);

        // Numeric Style Total
        numericStyle_Totals.setFont(fontBold);
        numericStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        numericStyle_Totals.setFillPattern(CellStyle.SOLID_FOREGROUND);
        numericStyle_Totals.setBorderBottom(CellStyle.BORDER_THIN);
        numericStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setBorderTop(CellStyle.BORDER_THIN);
        numericStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setAlignment(CellStyle.ALIGN_RIGHT);
        numericStyle_Totals.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        return result;
    }

    private Set<Integer> prepareColumnsIndexesSet(String columnsIndexesStr) {
        Set<Integer> columnsIndexesSet = null;
        if (!GenericValidator.isBlankOrNull(columnsIndexesStr)) {
            String[] columnsIndexes = columnsIndexesStr.split(",");
            columnsIndexesSet = new HashSet<>(columnsIndexes.length);
            for (String columnIndex : columnsIndexes) {
                columnsIndexesSet.add(Integer.valueOf(columnIndex));
            }
        }
        return columnsIndexesSet;
    }
}
