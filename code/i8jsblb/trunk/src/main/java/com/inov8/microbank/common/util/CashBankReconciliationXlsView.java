package com.inov8.microbank.common.util;

/*
 * Copyright 2004 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.calc.CalcResult;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.core.PreferencesConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.ExportViewUtils;
import org.extremecomponents.table.view.View;
import org.extremecomponents.util.ExtremeUtils;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 24, 2012 3:45:09 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CashBankReconciliationXlsView implements View
{
    private static Log logger = LogFactory.getLog(CashBankReconciliationXlsView.class);
    public static final int WIDTH_MULT = 240; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    public static final short DEFAULT_FONT_HEIGHT = 8;
    public static final double NON_NUMERIC = -.99999;
    public static final String DEFAULT_MONEY_FORMAT = "$###,###,##0.00";
    public static final String DEFAULT_PERCENT_FORMAT = "##0.0%";
    public static final String NBSP = "&nbsp;";

    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFPrintSetup ps;
    private Map<String,HSSFCellStyle> styles;
    private short rownum;
    private short cellnum;
    private HSSFRow hssfRow;
    private String moneyFormat;
    private String percentFormat;
    private String encoding;

    public CashBankReconciliationXlsView()
    {
    }

    /**
     * Populates report header information i.e. on top of data grid
     * @param model
     */
    private void addReportHeader( TableModel model )
    {
        rownum = 0;
        cellnum = 0;
        Map<String, String> reportHeaderMap = (Map<String, String>) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_REPORT_HEADER_MAP );
        if( reportHeaderMap != null && !reportHeaderMap.isEmpty() )
        {
            for( String label : reportHeaderMap.keySet() )
            {
                cellnum = 0;

                HSSFRow hssfRow = sheet.createRow( rownum );
                HSSFCell labelHssfCell = hssfRow.createCell( cellnum );
                setCellEncoding( labelHssfCell );

                labelHssfCell.setCellStyle( styles.get("boldStyle") );
                labelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                labelHssfCell.setCellValue( label );

                sheet.addMergedRegion( new Region( rownum, (short)cellnum, rownum, (short) (cellnum + 1) ) );

                cellnum += 2;
                HSSFCell valueHssfCell = hssfRow.createCell( cellnum );
                setCellEncoding( valueHssfCell );

                valueHssfCell.setCellStyle(  styles.get("textStyle") );
                valueHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                valueHssfCell.setCellValue( reportHeaderMap.get( label ) );
                sheet.addMergedRegion( new Region( rownum, (short)cellnum, rownum, (short) (cellnum + 2) ) );

                rownum++;
            }
            
        }
        
    }

    /**
     * Populates report footer information i.e. on bottom of data grid
     * @param model
     */
    private void addReportFooter( TableModel model, String totalComm )
    {
        Double dayStartOpeningBalance = (Double) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_BALANCE_OPENING_BANK_DAY_START );
        if( dayStartOpeningBalance == null )
        {
            dayStartOpeningBalance = 0.0;
        }
        Double dayEndClosingBalance = (Double) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_BALANCE_CLOSING_BANK_DAY_END );
        if( dayEndClosingBalance == null )
        {
            dayEndClosingBalance = 0.0;
        }

        rownum+=2;
        cellnum = 0;

        //Bank Opening Balance with Commission
        HSSFRow openingBalWithCommHssfRow = sheet.createRow( rownum );
        HSSFCell openingBalWithCommHssfCell = openingBalWithCommHssfRow.createCell( cellnum );
        setCellEncoding( openingBalWithCommHssfCell );

        openingBalWithCommHssfCell.setCellStyle( styles.get("boldStyle") );
        openingBalWithCommHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        openingBalWithCommHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_BALANCE_OPENING_BANK_WITH_COMMISSION );

        sheet.addMergedRegion( new Region( rownum, (short)cellnum, rownum, (short) (cellnum + 2) ) );

        cellnum += 4;
        HSSFCell openingBalWithCommValueHssfCell = openingBalWithCommHssfRow.createCell( cellnum );
        setCellEncoding( openingBalWithCommValueHssfCell );

        openingBalWithCommValueHssfCell.setCellStyle( styles.get("numericStyle") );
        openingBalWithCommValueHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        openingBalWithCommValueHssfCell.setCellValue( dayStartOpeningBalance );
        rownum++;

        //Bank Opening Balance at day start
        cellnum = 0;
        HSSFRow openingBalDayStartHssfRow = sheet.createRow( rownum );
        HSSFCell openingBalDayStartLabelHssfCell = openingBalDayStartHssfRow.createCell( cellnum );
        setCellEncoding( openingBalDayStartLabelHssfCell );

        openingBalDayStartLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        openingBalDayStartLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        openingBalDayStartLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_BALANCE_OPENING_BANK_DAY_START );

        sheet.addMergedRegion( new Region( rownum, (short)cellnum, rownum, (short) (cellnum + 2) ) );

        cellnum += 4;
        HSSFCell openingBalDayStartValHssfCell = openingBalDayStartHssfRow.createCell( cellnum );
        setCellEncoding( openingBalDayStartValHssfCell );

        openingBalDayStartValHssfCell.setCellStyle( styles.get("numericStyle") );
        openingBalDayStartValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        openingBalDayStartValHssfCell.setCellValue( dayStartOpeningBalance );
        rownum++;

        //Bank Opening Balance Difference
        cellnum = 2;
        HSSFRow differenceLabelHssfRow = sheet.createRow( rownum );
        HSSFCell differenceLabelHssfCell = differenceLabelHssfRow.createCell( cellnum );
        setCellEncoding( differenceLabelHssfCell );

        differenceLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        differenceLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        differenceLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_DIFFERENCE );

        cellnum += 2;
        HSSFCell differenceValHssfCell = differenceLabelHssfRow.createCell( cellnum );
        setCellEncoding( differenceValHssfCell );

        differenceValHssfCell.setCellStyle( styles.get("numericStyle") );
        differenceValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        differenceValHssfCell.setCellValue( dayStartOpeningBalance - dayStartOpeningBalance );

        rownum+=2;
        cellnum = 0;

        //Bank Closing Balance with Commission
        HSSFRow closingBalWithCommHssfRow = sheet.createRow( rownum );
        HSSFCell closingBalWithCommHssfCell = closingBalWithCommHssfRow.createCell( cellnum );
        setCellEncoding( closingBalWithCommHssfCell );

        closingBalWithCommHssfCell.setCellStyle( styles.get("boldStyle") );
        closingBalWithCommHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        closingBalWithCommHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_BALANCE_CLOSING_BANK_WITH_COMMISSION );

        sheet.addMergedRegion( new Region( rownum, (short)cellnum, rownum, (short) (cellnum + 2) ) );

        cellnum += 4;
        HSSFCell closingBalWithCommValueHssfCell = closingBalWithCommHssfRow.createCell( cellnum );
        setCellEncoding( closingBalWithCommValueHssfCell );

        closingBalWithCommValueHssfCell.setCellStyle( styles.get("numericStyle") );
        closingBalWithCommValueHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        Double totalBankBalWithComm = (Double) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_BANK_BALANCE );
        if( totalBankBalWithComm == null )
        {
            totalBankBalWithComm = 0.0;   
        }
        if( totalComm == null )
        {
            totalComm = "0.0";
        }
        totalBankBalWithComm += Double.valueOf( totalComm );
        closingBalWithCommValueHssfCell.setCellValue( totalBankBalWithComm );
        rownum++;

        //Bank Closing Balance at day end
        cellnum = 0;
        HSSFRow closingBalDayStartHssfRow = sheet.createRow( rownum );
        HSSFCell closingBalDayEndLabelHssfCell = closingBalDayStartHssfRow.createCell( cellnum );
        setCellEncoding( closingBalDayEndLabelHssfCell );

        closingBalDayEndLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        closingBalDayEndLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        closingBalDayEndLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_BALANCE_CLOSING_BANK_DAY_END );

        sheet.addMergedRegion( new Region( rownum, (short)cellnum, rownum, (short) (cellnum + 2) ) );

        cellnum += 4;
        HSSFCell closingBalDayEndValHssfCell = closingBalDayStartHssfRow.createCell( cellnum );
        setCellEncoding( closingBalDayEndValHssfCell );

        closingBalDayEndValHssfCell.setCellStyle( styles.get("numericStyle") );
        closingBalDayEndValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        closingBalDayEndValHssfCell.setCellValue( totalBankBalWithComm );
        rownum++;

        //Bank Closing Balance Difference
        cellnum = 2;
        HSSFRow closingBalDiffHssfRow = sheet.createRow( rownum );
        HSSFCell closingBalDiffLabelHssfCell = closingBalDiffHssfRow.createCell( cellnum );
        setCellEncoding( closingBalDiffLabelHssfCell );

        closingBalDiffLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        closingBalDiffLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        closingBalDiffLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_DIFFERENCE );

        cellnum += 2;
        HSSFCell closingBalDiffValHssfCell = closingBalDiffHssfRow.createCell( cellnum );
        setCellEncoding( closingBalDiffValHssfCell );

        closingBalDiffValHssfCell.setCellStyle( styles.get("numericStyle") );
        closingBalDiffValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        closingBalDiffValHssfCell.setCellValue( totalBankBalWithComm - totalBankBalWithComm );

        //Physical Cash
        rownum+=2;
        cellnum = 2;
        HSSFRow physicalCashHssfRow = sheet.createRow( rownum );
        HSSFCell physicalCashLabelHssfCell = physicalCashHssfRow.createCell( cellnum );
        setCellEncoding( physicalCashLabelHssfCell );

        physicalCashLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        physicalCashLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        physicalCashLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_PHYSICAL_CASH );

        cellnum += 2;
        HSSFCell physicalCashValHssfCell = physicalCashHssfRow.createCell( cellnum );
        setCellEncoding( physicalCashValHssfCell );

        physicalCashValHssfCell.setCellStyle( styles.get("numericStyle") );
        physicalCashValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        physicalCashValHssfCell.setCellValue( (Double) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_BALANCE ) );

        //Bank Balance
        rownum++;
        cellnum = 2;
        HSSFRow bankBalanceHSSFRow = sheet.createRow( rownum );
        HSSFCell bankBalanceLabelHssfCell = bankBalanceHSSFRow.createCell( cellnum );
        setCellEncoding( bankBalanceLabelHssfCell );

        bankBalanceLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        bankBalanceLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        bankBalanceLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_BANK_BALANCE );

        cellnum += 2;
        HSSFCell bankBalanceValHssfCell = bankBalanceHSSFRow.createCell( cellnum );
        setCellEncoding( bankBalanceValHssfCell );

        bankBalanceValHssfCell.setCellStyle( styles.get("numericStyle") );
        bankBalanceValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        bankBalanceValHssfCell.setCellValue( (Double) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_BANK_BALANCE ) );

        //Commission
        rownum++;
        cellnum = 2;
        HSSFRow commissionHSSFRow = sheet.createRow( rownum );
        HSSFCell commissionLabelHssfCell = commissionHSSFRow.createCell( cellnum );
        setCellEncoding( commissionLabelHssfCell );

        commissionLabelHssfCell.setCellStyle( styles.get("boldStyle") );
        commissionLabelHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        commissionLabelHssfCell.setCellValue( CashBankReconciliationReportConstants.LABEL_COMMISSION );

        cellnum += 2;
        HSSFCell commissionValHssfCell = commissionHSSFRow.createCell( cellnum );
        setCellEncoding( commissionValHssfCell );

        commissionValHssfCell.setCellStyle( styles.get("numericStyle") );
        commissionValHssfCell.setCellType( HSSFCell.CELL_TYPE_STRING );
        if( totalComm != null && !totalComm.isEmpty() )
        {
            commissionValHssfCell.setCellValue( totalComm );
        }
        else
        {
            commissionValHssfCell.setCellValue( 0.0 );
        }
        
    }

    public void beforeBody(TableModel model) {
        logger.debug("XlsView.init()");
        
        moneyFormat = model.getPreferences().getPreference(PreferencesConstants.TABLE_EXPORTABLE + "format.money");
        if (StringUtils.isEmpty(moneyFormat)) {
            moneyFormat = DEFAULT_MONEY_FORMAT;
        }
        percentFormat = model.getPreferences().getPreference(PreferencesConstants.TABLE_EXPORTABLE + "format.percent");
        if (StringUtils.isEmpty(percentFormat)) {
            percentFormat = DEFAULT_PERCENT_FORMAT;
        }

        encoding = model.getExportHandler().getCurrentExport().getEncoding();

        wb = new HSSFWorkbook();
        sheet = wb.createSheet();

        if (encoding.equalsIgnoreCase("UTF")) {
            wb.setSheetName(0, "Export Workbook");//, HSSFWorkbook.ENCODING_UTF_16);
        } else if (encoding.equalsIgnoreCase("UNICODE")) {
            wb.setSheetName(0, "Export Workbook");//, HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
        }

        styles = initStyles(wb);
        ps = sheet.getPrintSetup();

        sheet.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);

        addReportHeader( model );
        createHeader(model);
    }

    public void body(TableModel model, Column column) {
        if (column.isFirstColumn()) {
            rownum++;
            cellnum = 0;
            hssfRow = sheet.createRow(rownum);
        }

        String value = ExportViewUtils.parseXLS(column.getCellDisplay());

        HSSFCell hssfCell = hssfRow.createCell(cellnum);

        setCellEncoding(hssfCell);

        if (column.isEscapeAutoFormat()) {
            writeToCellAsText(hssfCell, value, "");
        } else {
            writeToCellFormatted(hssfCell, value, "");
        }
        cellnum++;
    }

    public Object afterBody(TableModel model) {
        String totalComm = null;
        if (model.getLimit().getTotalRows() != 0) {
            totalComm = totals(model);
        }

        addReportFooter( model, totalComm );
        return wb;
    }

    private void createHeader(TableModel model) {
        rownum++;
        cellnum = 0;
        HSSFRow row = sheet.createRow(rownum);

        List columns = model.getColumnHandler().getHeaderColumns();
        for (Iterator iter = columns.iterator(); iter.hasNext();) {
            Column column = (Column) iter.next();
            String title = column.getCellDisplay();
            HSSFCell hssfCell = row.createCell(cellnum);

            setCellEncoding(hssfCell);

            hssfCell.setCellStyle( styles.get("titleStyle"));
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue(title);
            int valWidth = (title + "").length() * WIDTH_MULT;
            sheet.setColumnWidth(hssfCell.getCellNum(), (short) valWidth);

            cellnum++;
        }
    }

    private void writeToCellAsText(HSSFCell cell, String value, String styleModifier) {
        // format text
        if (value.trim().equals(NBSP)) {
            value = "";
        }
        cell.setCellStyle( styles.get("textStyle" + styleModifier));
        fixWidthAndPopulate(cell, NON_NUMERIC, value);
    }

    private void writeToCellFormatted(HSSFCell cell, String value, String styleModifier) {
        double numeric = NON_NUMERIC;

        try {
            numeric = Double.parseDouble(value);
        } catch (Exception e) {
            numeric = NON_NUMERIC;
        }

        if (value.startsWith("$") || value.endsWith("%") || value.startsWith("($")) {
            boolean moneyFlag = (value.startsWith("$") || value.startsWith("($"));
            boolean percentFlag = value.endsWith("%");

            value = StringUtils.replace(value, "$", "");
            value = StringUtils.replace(value, "%", "");
            value = StringUtils.replace(value, ",", "");
            value = StringUtils.replace(value, "(", "-");
            value = StringUtils.replace(value, ")", "");

            try {
                numeric = Double.parseDouble(value);
            } catch (Exception e) {
                numeric = NON_NUMERIC;
            }

            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            if (moneyFlag) {
                // format money
                cell.setCellStyle( styles.get("moneyStyle" + styleModifier));
            } else if (percentFlag) {
                // format percent
                numeric = numeric / 100;
                cell.setCellStyle( styles.get("percentStyle" + styleModifier));
            }
        } else if (numeric != NON_NUMERIC) {
            // format numeric
            cell.setCellStyle( styles.get("numericStyle" + styleModifier));
        } else {
            // format text
            if (value.trim().equals(NBSP)) {
                value = "";
            }
            cell.setCellStyle( styles.get("textStyle" + styleModifier));
        }

        fixWidthAndPopulate(cell, numeric, value);
    }

    private void fixWidthAndPopulate(HSSFCell cell, double numeric, String value) {
        int valWidth = 0;

        if (numeric != NON_NUMERIC) {
            cell.setCellValue(numeric);
            valWidth = (cell.getNumericCellValue() + "$,.").length() * WIDTH_MULT;
        } else {
            cell.setCellValue(value);
            valWidth = (cell.getStringCellValue() + "").length() * WIDTH_MULT;

            if (valWidth < (WIDTH_MULT * MIN_CHARS)) {
                valWidth = WIDTH_MULT * MIN_CHARS;
            }
        }

        if (valWidth > sheet.getColumnWidth(cell.getCellNum())) {
            sheet.setColumnWidth(cell.getCellNum(), (short) valWidth);
        }
    }

    private Map<String,HSSFCellStyle> initStyles(HSSFWorkbook wb) {
        return initStyles(wb, DEFAULT_FONT_HEIGHT);
    }

    private Map<String,HSSFCellStyle> initStyles(HSSFWorkbook wb, short fontHeight) {
        Map<String,HSSFCellStyle> result = new HashMap<String,HSSFCellStyle>();
        HSSFCellStyle titleStyle = wb.createCellStyle();
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFCellStyle boldStyle = wb.createCellStyle();
        HSSFCellStyle numericStyle = wb.createCellStyle();
        HSSFCellStyle numericStyleBold = wb.createCellStyle();
        HSSFCellStyle moneyStyle = wb.createCellStyle();
        HSSFCellStyle moneyStyleBold = wb.createCellStyle();
        HSSFCellStyle percentStyle = wb.createCellStyle();
        HSSFCellStyle percentStyleBold = wb.createCellStyle();

        // Add to export totals
        HSSFCellStyle moneyStyle_Totals = wb.createCellStyle();
        HSSFCellStyle naStyle_Totals = wb.createCellStyle();
        HSSFCellStyle numericStyle_Totals = wb.createCellStyle();
        HSSFCellStyle percentStyle_Totals = wb.createCellStyle();
        HSSFCellStyle textStyle_Totals = wb.createCellStyle();

        result.put("titleStyle", titleStyle);
        result.put("textStyle", textStyle);
        result.put("boldStyle", boldStyle);
        result.put("numericStyle", numericStyle);
        result.put("numericStyleBold", numericStyleBold);
        result.put("moneyStyle", moneyStyle);
        result.put("moneyStyleBold", moneyStyleBold);
        result.put("percentStyle", percentStyle);
        result.put("percentStyleBold", percentStyleBold);

        // Add to export totals
        result.put("moneyStyle_Totals", moneyStyle_Totals);
        result.put("naStyle_Totals", naStyle_Totals);
        result.put("numericStyle_Totals", numericStyle_Totals);
        result.put("percentStyle_Totals", percentStyle_Totals);
        result.put("textStyle_Totals", textStyle_Totals);

        HSSFDataFormat format = wb.createDataFormat();

        // Global fonts
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);

        HSSFFont fontBold = wb.createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontBold.setColor(HSSFColor.BLACK.index);
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setFontHeightInPoints(fontHeight);

        // Money Style
        moneyStyle.setFont(font);
        moneyStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyle.setDataFormat(format.getFormat(moneyFormat));

        // Money Style Bold
        moneyStyleBold.setFont(fontBold);
        moneyStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyleBold.setDataFormat(format.getFormat(moneyFormat));

        // Percent Style
        percentStyle.setFont(font);
        percentStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        percentStyle.setDataFormat(format.getFormat(percentFormat));

        // Percent Style Bold
        percentStyleBold.setFont(fontBold);
        percentStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        percentStyleBold.setDataFormat(format.getFormat(percentFormat));

        // Standard Numeric Style
        numericStyle.setFont(font);
        numericStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Standard Numeric Style Bold
        numericStyleBold.setFont(fontBold);
        numericStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Title Style
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Standard Text Style
        textStyle.setFont(font);
        textStyle.setWrapText(true);

        // Standard Text Style
        boldStyle.setFont(fontBold);
        boldStyle.setWrapText(true);

        // Money Style Total
        moneyStyle_Totals.setFont(fontBold);
        moneyStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        moneyStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        moneyStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        moneyStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        moneyStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        moneyStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        moneyStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        moneyStyle_Totals.setDataFormat(format.getFormat(moneyFormat));

        // n/a Style Total
        naStyle_Totals.setFont(fontBold);
        naStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        naStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        naStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        naStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        naStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        naStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        naStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        naStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Numeric Style Total
        numericStyle_Totals.setFont(fontBold);
        numericStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        numericStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        numericStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numericStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Percent Style Total
        percentStyle_Totals.setFont(fontBold);
        percentStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        percentStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        percentStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        percentStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        percentStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        percentStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        percentStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        percentStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        percentStyle_Totals.setDataFormat(format.getFormat(percentFormat));

        // Text Style Total
        textStyle_Totals.setFont(fontBold);
        textStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        textStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        textStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        textStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return result;
    }

    // Add to export totals
    public String totals(TableModel model) {
        String totalComm = null;
        Column firstCalcColumn = model.getColumnHandler().getFirstCalcColumn();
        if (firstCalcColumn != null) {
            int rows = firstCalcColumn.getCalc().length;
            for (int i = 0; i < rows; i++) {
                rownum++;
                HSSFRow row = sheet.createRow(rownum);
                cellnum = 0;
                for (Iterator iter = model.getColumnHandler().getColumns().iterator(); iter.hasNext();) {
                    Column column = (Column) iter.next();
                    if (column.isFirstColumn()) {
                        String calcTitle = CalcUtils.getFirstCalcColumnTitleByPosition(model, i);
                        HSSFCell cell = row.createCell(cellnum);
                        setCellEncoding(cell);
                        if (column.isEscapeAutoFormat()) {
                            writeToCellAsText(cell, calcTitle, "_Totals");
                        } else {
                            writeToCellFormatted(cell, calcTitle, "_Totals");
                        }
                        cellnum++;
                        continue;
                    }

                    if (column.isCalculated()) {
                        Number value = null;
                        if( CashBankReconciliationReportConstants.KEY_TOTAL_BANK_BALANCE.equals( column.getTitle() ) )
                        {
                            value = (Number) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_BANK_BALANCE );
                        }
                        else if( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_BALANCE.equals( column.getTitle() ) )
                        {
                            value = (Number) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_BALANCE );
                        }
                        else if( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_AND_BANK.equals( column.getTitle() ) )
                        {
                            value = (Number) model.getContext().getRequestAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_AND_BANK );
                        }
                        else
                        {
                            CalcResult calcResult = CalcUtils.getCalcResultsByPosition(model, column, i);
                            value = calcResult.getValue();
                        }
                        HSSFCell cell = row.createCell(cellnum);
                        setCellEncoding(cell);
                        if (value != null)
                            if (column.isEscapeAutoFormat()) {
                                writeToCellAsText(cell, value.toString(), "_Totals");
                            } else {
                                writeToCellFormatted(cell, ExtremeUtils.formatNumber(column.getFormat(), value, model.getLocale()), "_Totals");
                                if( CashBankReconciliationReportConstants.COL_COMMISSION_AFTER_TAX.equals( column.getTitle() ) )
                                {
                                   totalComm = ExtremeUtils.formatNumber(column.getFormat(), value, model.getLocale() );
                                }
                            }
                        else {
                            cell.setCellStyle( styles.get("naStyle_Totals"));
                            cell.setCellValue("n/a");
                        }
                        cellnum++;
                    } else {
                        HSSFCell cell = row.createCell(cellnum);
                        setCellEncoding(cell);
                        writeToCellFormatted(cell, "", "_Totals");
                        cellnum++;
                    }
                }
            }
        }
        return totalComm;
    }


    //add to set Cell encoding
    private void setCellEncoding(HSSFCell cell) {
        if (encoding.equalsIgnoreCase("UTF")) {
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
        } else if (encoding.equalsIgnoreCase("UNICODE")) {
            //cell.setEncoding(HSSFCell.ENCODING_COMPRESSED_UNICODE);
        }
    }

}
