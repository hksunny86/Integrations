package com.inov8.microbank.common.util;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.PreferencesConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.ExportViewUtils;
import org.extremecomponents.table.view.XlsView;

public class ViewCustomerBbStatementXlsView extends XlsView {
	
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
	        wb.setSheetName(0, "Export Workbook");

	        styles = initStyles(wb);
	        ps = sheet.getPrintSetup();

	        sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	        addReportHeader(model);
	        createHeader(model);
	    }
	 private void addReportHeader( TableModel model )
	    {
	        rownum = 0;
	        cellnum = 0;
	        Map<String, String> reportHeaderMap = (Map<String, String>) model.getContext().getRequestAttribute(BBStatementReportConstants.mapkey);
	        Double openingBalance = (Double) model.getContext().getRequestAttribute(BBStatementReportConstants.openingBalance);
	        DecimalFormat df = new DecimalFormat("#.##");
	        if( reportHeaderMap != null && !reportHeaderMap.isEmpty() )
	        {		
	      	
                HSSFRow hssfRow = sheet.createRow( rownum );
                HSSFCell issueDateCell = hssfRow.createCell( (short)3 );	               
                setCellEncoding( issueDateCell );
                issueDateCell.setCellType( HSSFCell.CELL_TYPE_STRING );
               
                issueDateCell.setCellValue("Issued on :"+reportHeaderMap.get(BBStatementReportConstants.issueDateKey) );
               
                /////Row 2
                rownum++; cellnum = 0;
                
                hssfRow = sheet.createRow( rownum );
                HSSFCell branchNameCell = hssfRow.createCell( (short) 0 );               
                setCellEncoding( branchNameCell );
                branchNameCell.setCellStyle( (HSSFCellStyle) styles.get("boldStyle") );      
                branchNameCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                branchNameCell.setCellValue("BRANCH NAME");
               
                HSSFCell periodCell  = hssfRow.createCell((short)3);
                setCellEncoding( periodCell ); 
                periodCell.setCellStyle( (HSSFCellStyle) styles.get("boldStyle") );        
                periodCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                periodCell.setCellValue("Statement Period");
                
                ////////////////Row 3/////
                rownum++;
                
                hssfRow = sheet.createRow( rownum );
                HSSFCell branchNameValueCell = hssfRow.createCell( (short) 0 );
                setCellEncoding( branchNameValueCell );
                branchNameValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                branchNameValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.branchNameKey1));
                
                HSSFCell periodValueCell  = hssfRow.createCell((short)3);
                setCellEncoding( periodCell );	               
                periodValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                periodValueCell.setCellValue("From : "+reportHeaderMap.get(BBStatementReportConstants.statementDateRangeKey));
                
                ////////////////Row 4/////
                rownum++;
                
                hssfRow = sheet.createRow( rownum );
                HSSFCell branchName2ValueCell = hssfRow.createCell( (short) 0 );
                setCellEncoding( branchName2ValueCell );
                branchName2ValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                branchName2ValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.branchNameKey2));
                
               ///////////////////Row 5//////////// 
                rownum+=2;
                
                hssfRow = sheet.createRow( rownum );
                HSSFCell nameCell = hssfRow.createCell( (short) 0 );
                setCellEncoding( nameCell );
                nameCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                nameCell.setCellStyle( (HSSFCellStyle) styles.get("boldStyle") );
                nameCell.setCellValue("Name : ");
                
                HSSFCell nameValueCell = hssfRow.createCell( (short) 1 );
                setCellEncoding( nameValueCell );
                nameValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                nameValueCell.setCellStyle( (HSSFCellStyle) styles.get("boldStyle") );
                nameValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.customerNameKey));
                
                HSSFCell accountNoCell  = hssfRow.createCell((short)3);
                setCellEncoding( accountNoCell );	               
                accountNoCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                accountNoCell.setCellValue("BB Mobile Account #");
               
                HSSFCell accountNoValueCell  = hssfRow.createCell((short)5);
                setCellEncoding( accountNoValueCell );	               
                accountNoValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                accountNoValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.zongMsisdnKey));

                //////////Row 6
                
                rownum++;
                
                hssfRow = sheet.createRow( rownum );
                HSSFCell addressCell = hssfRow.createCell( (short) 0 );
                setCellEncoding( addressCell );
                addressCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                addressCell.setCellValue("Address : ");
                
                HSSFCell addressValueCell1 = hssfRow.createCell( (short) 1 );
                setCellEncoding( addressValueCell1 );
                addressValueCell1.setCellType( HSSFCell.CELL_TYPE_STRING );
                addressValueCell1.setCellValue(reportHeaderMap.get(BBStatementReportConstants.addressKey1));
                
                if(null!=reportHeaderMap.get(BBStatementReportConstants.customerIdKey)){
	                HSSFCell customerIdCell  = hssfRow.createCell((short)3);
	                setCellEncoding( customerIdCell );	               
	                customerIdCell.setCellType( HSSFCell.CELL_TYPE_STRING );
	                customerIdCell.setCellValue("Customer ID #");
	               
	                HSSFCell customerIdValueCell  = hssfRow.createCell((short)5);
	                setCellEncoding( customerIdValueCell );	               
	                customerIdValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
	                customerIdValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.customerIdKey));
                }
                else{
                	
                	HSSFCell customerIdCell  = hssfRow.createCell((short)3);
	                setCellEncoding( customerIdCell );	               
	                customerIdCell.setCellType( HSSFCell.CELL_TYPE_STRING );
	                customerIdCell.setCellValue("Agent ID #");
	               
	                HSSFCell customerIdValueCell  = hssfRow.createCell((short)5);
	                setCellEncoding( customerIdValueCell );	               
	                customerIdValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
	                customerIdValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.AgentIdKey));
                }
	            ///Row 7
                rownum++;
                hssfRow = sheet.createRow( rownum );
                
                HSSFCell addressValueCell2 = hssfRow.createCell( (short) 1 );
                setCellEncoding( addressValueCell2 );
                addressValueCell2.setCellType( HSSFCell.CELL_TYPE_STRING );
                addressValueCell2.setCellValue(reportHeaderMap.get(BBStatementReportConstants.addressKey2));
                
                HSSFCell currencyCell  = hssfRow.createCell((short)3);
                setCellEncoding( currencyCell );	               
                currencyCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                currencyCell.setCellValue("Currency");
               
                HSSFCell currencyValueCell  = hssfRow.createCell((short)5);
                setCellEncoding( currencyValueCell );	               
                currencyValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                currencyValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.currencyKey));
                
                ///Row 8
                rownum++;
                hssfRow = sheet.createRow( rownum );
                
                HSSFCell addressValueCell3 = hssfRow.createCell( (short) 1 );
                setCellEncoding( addressValueCell3 );
                addressValueCell3.setCellType( HSSFCell.CELL_TYPE_STRING );
                addressValueCell3.setCellValue(reportHeaderMap.get(BBStatementReportConstants.addressKey3));
                
                HSSFCell accountLevelCell  = hssfRow.createCell((short)3);
                setCellEncoding( accountLevelCell );	               
                accountLevelCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                accountLevelCell.setCellValue("Account Type");
               
                HSSFCell accountLevelValueCell  = hssfRow.createCell((short)5);
                setCellEncoding( accountLevelValueCell );	               
                accountLevelValueCell.setCellType( HSSFCell.CELL_TYPE_STRING );
                accountLevelValueCell.setCellValue(reportHeaderMap.get(BBStatementReportConstants.accountLevelKey));
                                
              
                ///Closing Balance
                //String openingBalance= (String) model.getContext().getRequestAttribute(BBStatementReportConstants.openingBalance);
                rownum+=2;
                hssfRow = sheet.createRow( rownum );
                              
                HSSFCell openingBalanceCell  = hssfRow.createCell((short)4);
                setCellEncoding( openingBalanceCell );
                openingBalanceCell.setCellStyle( (HSSFCellStyle) styles.get("boldStyle") );
                openingBalanceCell.setCellType( HSSFCell.CELL_TYPE_STRING );          
                openingBalanceCell.setCellValue("Opening Balance : "+df.format(openingBalance));
                
                sheet.addMergedRegion( new Region( 1, (short)3, 1, (short) (4) ) );
                sheet.addMergedRegion( new Region( 10, (short)4, 10, (short) (5)) );
 
    
                //rownum++;
               
	        }     
	        
	        
	    }
	 private void setCellEncoding(HSSFCell cell) {
	        if (encoding.equalsIgnoreCase("UTF")) {
	            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	        } else if (encoding.equalsIgnoreCase("UNICODE")) {
	            //cell.setEncoding(HSSFCell.ENCODING_COMPRESSED_UNICODE);
	        }
	    }
	 
	 protected void createHeader(TableModel model) {
		 	rownum++;
	        cellnum = 0;
	        HSSFRow row = sheet.createRow(rownum);

	        List columns = model.getColumnHandler().getHeaderColumns();
	        for (Iterator iter = columns.iterator(); iter.hasNext();) {
	            Column column = (Column) iter.next();
	            String title = column.getCellDisplay();
	            HSSFCell hssfCell = row.createCell(cellnum);

	            //setCellEncoding(hssfCell);

	            hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            hssfCell.setCellValue(title);
	            int valWidth = (title + "").length() * WIDTH_MULT;
	            sheet.setColumnWidth(hssfCell.getCellNum(), (short) valWidth);

	            cellnum++;
	        }
	    }
	  public void body(TableModel model, Column column) {
	        if (column.isFirstColumn()) {
	            rownum++;
	            cellnum = 0;
	            hssfRow = sheet.createRow(rownum);
	        }

	        String value = ExportViewUtils.parseXLS(column.getCellDisplay());
	        	       
	        if (StringUtils.contains(value,"<br/>")) {
	            value = StringUtils.replace(value, "<br/>"," ");
	        }  
	        
	        
	        HSSFCell hssfCell = hssfRow.createCell(cellnum);

	        //setCellEncoding(hssfCell);

	        if (column.isEscapeAutoFormat()) {
	            writeToCellAsText(hssfCell, value, "");
	        } else {
	            writeToCellFormatted(hssfCell, value, "");
	        }
	        cellnum++;
	    }
	 public Object afterBody(TableModel model) {
		 Double closingBalance = (Double) model.getContext().getRequestAttribute(BBStatementReportConstants.closingBalance);
		 DecimalFormat df = new DecimalFormat("#.##");
		 sheet.setColumnWidth((short)3, (short)(15*WIDTH_MULT));
		 sheet.setColumnWidth((short)4, (short)(15*WIDTH_MULT));
		 sheet.setColumnWidth((short)5, (short)(15*WIDTH_MULT)); 
		 
		 rownum++;
         sheet.addMergedRegion( new Region( rownum, (short)4, rownum, (short) (5) ) );
         hssfRow = sheet.createRow( rownum );
                       
         HSSFCell closingBalanceCell  = hssfRow.createCell((short)4);
         setCellEncoding( closingBalanceCell );
         closingBalanceCell.setCellStyle( (HSSFCellStyle) styles.get("boldStyle") );
         closingBalanceCell.setCellType( HSSFCell.CELL_TYPE_STRING );          
         closingBalanceCell.setCellValue("Closing Balance : "+df.format(closingBalance));
         
       
		 	
		 	if (model.getLimit().getTotalRows() != 0) {
	            totals(model);
	        }
	        //sheet.setProtect(true);
	        return wb;
	    }

}
