/**
 * 
 */
package com.inov8.microbank.common.util;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Export;
import org.extremecomponents.table.calc.CalcResult;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.View;
import org.extremecomponents.util.ExtremeUtils;

/**
 * Project Name: 			Microbank	
 * @author Hassan Javaid
 * Creation Date: 			April 04, 2014
 * Creation Time: 			9:56:49 AM
 * Description:				
 */

public class CustomerBBStatementPDFView implements View {
    public final static String FONT = "exportPdf.font";
    public final static String HEADER_BACKGROUND_COLOR = "headerBackgroundColor";
    public final static String HEADER_TITLE = "headerTitle";
    public final static String HEADER_COLOR = "headerColor";

    private StringBuffer xlsfo = new StringBuffer();
    private String font;

    public CustomerBBStatementPDFView() {
    }

    public void beforeBody(TableModel model) {
        this.font = model.getPreferences().getPreference(FONT);

        xlsfo.append(startRoot());
        xlsfo.append(regionBefore(model));
        xlsfo.append(regionAfter());
        addReportHeader(model);
        xlsfo.append(columnDefinitions(model));
        xlsfo.append(header(model));
        xlsfo.append(" <fo:table-body> ");
    }

    public void body(TableModel model, Column column) {
        if (column.isFirstColumn()) {
            xlsfo.append(" <fo:table-row> ");
        }

        String value=column.getCellDisplay();
        if (StringUtils.contains(value,"<br/>")) {
            value = StringUtils.replace(value, "<br/>"," ");
        }  
        	
	        xlsfo.append(" <fo:table-cell border=\"solid silver .5px\" display-align=\"center\" padding=\"2pt\"> ");
	        xlsfo.append(" <fo:block" + getFont() + ">" + addAlignment(value, model.getColumnHandler().columnCount()) + "</fo:block> ");
	        xlsfo.append(" </fo:table-cell> ");
        if (column.isLastColumn()) {
            xlsfo.append(" </fo:table-row> ");
        }
    }

    /**
     * Splits the word in required characters if no space is found to avoid overlapping in table column
     * @param value
     * @return
     */
    private String addAlignment(String value, double columnCount)
    {
    	if(value==null || value.length()==0)
    		return value;
    	String ret = StringUtil.wrapString(value, (int)(80/columnCount), null, true);
		return ret;

/*    	
    	int count = (int)columnCount;
    	switch (count)
    	{
    		case 1: return StringUtil.wrapString(value, 80, null, true);
    		case 2: return StringUtil.wrapString(value, 40, null, true);
    			
    	}
    	
    	else if(columnCount>4 && columnCount<=6)
    		return StringUtil.wrapString(value, 21, null, true);

    	else if(columnCount>6 && columnCount<=8)
    		return StringUtil.wrapString(value, 17, null, true);

    	else if(columnCount>8 && columnCount<=10)
    		return StringUtil.wrapString(value, 13, null, true);

    	else if(columnCount>10 && columnCount<=12)
    		return StringUtil.wrapString(value, 9, null, true);
    	
    	else if(columnCount>12 && columnCount<=14)
    		return StringUtil.wrapString(value, 7, null, true);

    	else if(columnCount>14 && columnCount<=16)
    		return StringUtil.wrapString(value, 4, null, true);

    	else 
    		return StringUtil.wrapString(value, 3, null, true);
  */  	
/*    	
    	else if(columnCount>5 && columnCount<=8)
    		return StringUtil.wrapString(value, 19, null, true);
    	
   	 	else if(columnCount==9)
    		 return StringUtil.wrapString(value, 15, null, true);
    	
    	 else if(columnCount>9 && columnCount<12)
    		 return StringUtil.wrapString(value, 10, null, true);
    	
    	 else 
    		 return StringUtil.wrapString(value, 7, null, true);
*/    		 
    }
    
    public Object afterBody(TableModel model) {
		 Double closingBalance = (Double) model.getContext().getRequestAttribute(BBStatementReportConstants.closingBalance);
		 DecimalFormat df = new DecimalFormat("#.##");
    	if (model.getLimit().getTotalRows() != 0) {
            xlsfo.append(totals(model));
        }
        xlsfo.append(" </fo:table-body> ");
        xlsfo.append(" </fo:table> ");
        //For Printing Closing Balance after Table body
        xlsfo.append(" <fo:block  white-space-collapse=\"false\"" + getFont() + "> 																					<fo:inline font-weight=\"bold\" font-size=\"75%\">Closing Balance :  "+ closingBalance +" </fo:inline></fo:block> ");

        xlsfo.append(endRoot());
        return xlsfo.toString();
    }

    public String startRoot() {
        StringBuffer sb = new StringBuffer();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        sb.append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");

        sb.append(" <fo:layout-master-set> ");
        sb.append(" <fo:simple-page-master master-name=\"simple\" ");
        sb.append(" page-height=\"8.5in\" ");
        sb.append(" page-width=\"11in\" ");
        sb.append(" margin-top=\".5in\" ");
        sb.append(" margin-bottom=\".25in\" ");
        sb.append(" margin-left=\".5in\" ");
        sb.append(" margin-right=\".5in\"> ");
        sb.append(" <fo:region-body margin-top=\".5in\" margin-bottom=\".25in\"/> ");
        sb.append(" <fo:region-before extent=\".5in\"/> ");
        sb.append(" <fo:region-after extent=\".25in\"/> ");
        sb.append(" </fo:simple-page-master> ");
        sb.append(" </fo:layout-master-set> ");

        sb.append(" <fo:page-sequence master-reference=\"simple\" initial-page-number=\"1\"> ");

        return sb.toString();
    }

    public String regionBefore(TableModel model) {
        StringBuffer sb = new StringBuffer();

        Export export = model.getExportHandler().getCurrentExport();

        String headerBackgroundColor = export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

        sb.append(" <fo:static-content flow-name=\"xsl-region-before\"> ");

        String title = export.getAttributeAsString(HEADER_TITLE);

        sb.append(" <fo:block space-after.optimum=\"15pt\" color=\"" + headerBackgroundColor + "\" font-size=\"17pt\" font-family=\"" + getHeadFont() + "'Times'\">" + title + "</fo:block> ");
        //sb.append(" <fo:block><fo:external-graphic content-width=\"50pt\" content-height=\"50pt\" src=\"http://localhost:8080/i8Microbank/images/login_01.jpg\"/></fo:block>");
        sb.append(" </fo:static-content> ");

        return sb.toString();
    }

    public String regionAfter() {
        StringBuffer sb = new StringBuffer();

        sb.append(" <fo:static-content flow-name=\"xsl-region-after\" display-align=\"after\"> ");

        sb.append(" <fo:block text-align=\"end\">Page <fo:page-number/></fo:block> ");

        sb.append(" </fo:static-content> ");

        return sb.toString();
    }

    public String columnDefinitions(TableModel model) {
        StringBuffer sb = new StringBuffer();

       // sb.append(" <fo:flow flow-name=\"xsl-region-body\"> ");

        sb.append(" <fo:block" + getFont() + ">");

        sb.append(" <fo:table table-layout=\"fixed\" font-size=\"10pt\"> ");

        double columnCount = model.getColumnHandler().columnCount();

        double colwidth = 10 / columnCount;

        for (int i = 1; i <= columnCount; i++) {
            sb.append(" <fo:table-column column-number=\"" + i + "\" column-width=\"" + colwidth + "in\"/> ");
        }

        return sb.toString();
    }
    private void addReportHeader( TableModel model )
    {
        Map<String, String> reportHeaderMap = (Map<String, String>) model.getContext().getRequestAttribute(BBStatementReportConstants.mapkey);
        if( reportHeaderMap != null && !reportHeaderMap.isEmpty() )
        {		
        	xlsfo.append(" <fo:flow flow-name=\"xsl-region-body\"> ");
        	
        	xlsfo.append(" <fo:block>");

        	xlsfo.append(" <fo:table  border=\"0.0pt solid black\" table-layout=\"fixed\" font-size=\"10pt\"> ");
        	       	
        	xlsfo.append(" <fo:table-column column-width=\"30mm\"/> ");
        	xlsfo.append(" <fo:table-column column-width=\"100mm\"/> ");
        	xlsfo.append(" <fo:table-column column-width=\"50mm\"/> ");
        	xlsfo.append(" <fo:table-column column-width=\"50mm\"/> ");
 
        	xlsfo.append(" <fo:table-body> ");

        	////Row 1        	
        	String issueDateCell="Issued on :  "+reportHeaderMap.get(BBStatementReportConstants.issueDateKey);
        	xlsfo.append(" <fo:table-row> ");
        	
        	xlsfo.append(" <fo:table-cell > ");
        	xlsfo.append(" </fo:table-cell> ");
  	       
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">" + issueDateCell + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
      
	        
	       
           xlsfo.append(" </fo:table-row> ");
       	
 
            /////Row 2
          
           
  	       
  	     xlsfo.append(" <fo:table-row> ");
        	
        	xlsfo.append(" <fo:table-cell > ");
        	xlsfo.append(" <fo:block font-weight=\"bold\"" + getFont() + ">BRANCH NAME</fo:block> ");
        	xlsfo.append(" </fo:table-cell> ");
  	       
           xlsfo.append(" <fo:table-cell > ");      
 	       xlsfo.append(" </fo:table-cell> ");
 	       
 	       xlsfo.append(" <fo:table-cell > ");
 	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" <fo:table-cell > ");
           xlsfo.append(" <fo:block font-weight=\"bold\"" + getFont() + ">Statement Period</fo:block> ");
 	       xlsfo.append(" </fo:table-cell> ");
      
 	        
 	       
           xlsfo.append(" </fo:table-row> ");

  	       
           
            ////////////////Row 3/////
            String branchNameValueCell=reportHeaderMap.get(BBStatementReportConstants.branchNameKey1);
            String periodValueCell=reportHeaderMap.get(BBStatementReportConstants.statementDateRangeKey);
            
                                                    	     	  

     	   xlsfo.append(" <fo:table-row> ");
       	
       	xlsfo.append(" <fo:table-cell number-columns-spanned=\"2\"> ");
       	xlsfo.append(" <fo:block" + getFont() + ">" + branchNameValueCell + "</fo:block> ");
       	xlsfo.append(" </fo:table-cell> ");
 	       
          xlsfo.append(" <fo:table-cell > ");      
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell> ");
	       xlsfo.append(" <fo:block" + getFont() + ">" + periodValueCell + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
          
          xlsfo.append(" <fo:table-cell > ");    
	       xlsfo.append(" </fo:table-cell> ");

          xlsfo.append(" </fo:table-row> ");
 
     	    ////////////////Row 4/////
     	   String branchName2ValueCell=reportHeaderMap.get(BBStatementReportConstants.branchNameKey2);
 
     	   xlsfo.append(" <fo:table-row> ");
        	
        	xlsfo.append(" <fo:table-cell number-columns-spanned=\"2\"> ");
            xlsfo.append(" <fo:block " + getFont() + ">" +branchName2ValueCell+ "</fo:block> ");
        	xlsfo.append(" </fo:table-cell> ");
  	       
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");

           xlsfo.append(" </fo:table-row> ");

           //////////////// Empty Row////////////////
           xlsfo.append(" <fo:table-row> ");
           
           xlsfo.append(" <fo:table-cell number-columns-spanned=\"2\"> ");
       	xlsfo.append(" </fo:table-cell> ");
 	       
          xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block " + getFont() + ">.</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
          
          xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" </fo:table-row> ");

                    ///////////////////Row 5//////////// 
           String nameValueCell=reportHeaderMap.get(BBStatementReportConstants.customerNameKey);
           String accountNoValueCell=reportHeaderMap.get(BBStatementReportConstants.zongMsisdnKey);
 
           xlsfo.append(" <fo:table-row> ");
        	
        	xlsfo.append(" <fo:table-cell > ");
        	 xlsfo.append(" <fo:block font-weight=\"bold\"" + getFont() + ">Name :</fo:block> ");
        	xlsfo.append(" </fo:table-cell> ");
  	       
           xlsfo.append(" <fo:table-cell > ");
           xlsfo.append(" <fo:block" + getFont() + ">" + nameValueCell + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">BB Mobile Account #</fo:block>");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">" + accountNoValueCell + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");

           xlsfo.append(" </fo:table-row> ");
 
            	    //////////Row 6
            
           String addressValueCell1=reportHeaderMap.get(BBStatementReportConstants.addressKey1);
           String customerIdValueCell=reportHeaderMap.get(BBStatementReportConstants.customerIdKey);
           String agentIdValueCell=reportHeaderMap.get(BBStatementReportConstants.AgentIdKey);
                                                             	     	  
    	   
			xlsfo.append(" <fo:table-row> ");
			xlsfo.append(" <fo:table-cell > ");
			xlsfo.append(" <fo:block" + getFont() + ">Address : </fo:block> ");
			xlsfo.append(" </fo:table-cell> ");
			xlsfo.append(" <fo:table-cell > ");
	           xlsfo.append(" <fo:block" + getFont() + ">" + addressValueCell1 + "</fo:block> ");
		       xlsfo.append(" </fo:table-cell> ");
		       
		   if(null!=customerIdValueCell){
		       xlsfo.append(" <fo:table-cell > ");
		       xlsfo.append(" <fo:block" + getFont() + ">Customer ID #</fo:block>");
		       xlsfo.append(" </fo:table-cell> ");
	           
	           xlsfo.append(" <fo:table-cell > ");
		       xlsfo.append(" <fo:block" + getFont() + ">" + customerIdValueCell + "</fo:block> ");
		       xlsfo.append(" </fo:table-cell> ");
		   }
		   else{
			   xlsfo.append(" <fo:table-cell > ");
		       xlsfo.append(" <fo:block" + getFont() + ">Agent ID #</fo:block>");
		       xlsfo.append(" </fo:table-cell> ");
	           
	           xlsfo.append(" <fo:table-cell > ");
		       xlsfo.append(" <fo:block" + getFont() + ">" + agentIdValueCell + "</fo:block> ");
		       xlsfo.append(" </fo:table-cell> ");
			   
		   }
 
           xlsfo.append(" </fo:table-row> ");

            ///Row 7
           String addressValueCell2=reportHeaderMap.get(BBStatementReportConstants.addressKey2);
           String currencyValueCell=reportHeaderMap.get(BBStatementReportConstants.currencyKey);


  xlsfo.append(" <fo:table-row> ");
        	
        	xlsfo.append(" <fo:table-cell > ");
        	xlsfo.append(" </fo:table-cell> ");
  	       
           xlsfo.append(" <fo:table-cell > ");
           xlsfo.append(" <fo:block" + getFont() + ">" + addressValueCell2 + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">Currency</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">" + currencyValueCell + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
      
	        
	       
           xlsfo.append(" </fo:table-row> ");
    	    
    	    
    	    
    	    
            ///Row 8
           
           String addressValueCell3=reportHeaderMap.get(BBStatementReportConstants.addressKey3);
           String accountLevelValueCell=reportHeaderMap.get(BBStatementReportConstants.accountLevelKey);


    	    
    	    xlsfo.append(" <fo:table-row> ");
        	
        	xlsfo.append(" <fo:table-cell > ");
        	xlsfo.append(" </fo:table-cell> ");
  	       
           xlsfo.append(" <fo:table-cell > ");
           xlsfo.append(" <fo:block" + getFont() + ">" + addressValueCell3 + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">Account Type</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block" + getFont() + ">" + accountLevelValueCell + "</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
      
	        
	       
           xlsfo.append(" </fo:table-row> ");
           //////////////// Empty Row////////////////
           xlsfo.append(" <fo:table-row> ");
           
           xlsfo.append(" <fo:table-cell number-columns-spanned=\"2\"> ");
       	xlsfo.append(" </fo:table-cell> ");
 	       
          xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block " + getFont() + ">.</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
          
          xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" </fo:table-row> ");
           //////////////// Empty Row////////////////                          
           xlsfo.append(" <fo:table-row> ");
           
           xlsfo.append(" <fo:table-cell number-columns-spanned=\"2\"> ");
       	xlsfo.append(" </fo:table-cell> ");
 	       
          xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" </fo:table-cell> ");
	       
	       xlsfo.append(" <fo:table-cell > ");
	       xlsfo.append(" <fo:block " + getFont() + "></fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
          
          xlsfo.append(" <fo:table-cell > ");
          xlsfo.append(" <fo:block " + getFont() + ">.</fo:block> ");
	       xlsfo.append(" </fo:table-cell> ");
           
           xlsfo.append(" </fo:table-row> ");
    	    xlsfo.append(" </fo:table-body> ");
            xlsfo.append("</fo:table>"); 

            Double openingBalance = (Double) model.getContext().getRequestAttribute(BBStatementReportConstants.openingBalance);
	        DecimalFormat df = new DecimalFormat("#.##");
            xlsfo.append(" <fo:block  white-space-collapse=\"false\"" + getFont() + "> 																					<fo:inline font-weight=\"bold\" font-size=\"75%\">Opening Balance :  "+ openingBalance +" </fo:inline></fo:block> ");            
        	xlsfo.append(" </fo:block>");
        }
        
    }

    public String header(TableModel model) {
        StringBuffer sb = new StringBuffer();

        Export export = model.getExportHandler().getCurrentExport();
        String headerColor = export.getAttributeAsString(HEADER_COLOR);
        String headerBackgroundColor = export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

        sb.append(" <fo:table-header background-color=\"" + headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");

        sb.append(" <fo:table-row> ");

        List columns = model.getColumnHandler().getHeaderColumns();
        for (Iterator iter = columns.iterator(); iter.hasNext();) {
            Column column = (Column) iter.next();
            String title = column.getCellDisplay();
            sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"2pt\"> ");
            sb.append(" <fo:block" + getFont() + ">" + addAlignment(title, model.getColumnHandler().columnCount()) + "</fo:block> ");
            sb.append(" </fo:table-cell> ");
        }

        sb.append(" </fo:table-row> ");

        sb.append(" </fo:table-header> ");

        return sb.toString();
    }

    public String endRoot() {
        StringBuffer sb = new StringBuffer();
       
        sb.append(" </fo:block> ");

        sb.append(" </fo:flow> ");

        sb.append(" </fo:page-sequence> ");

        sb.append(" </fo:root> ");

        return sb.toString();
    }

    protected String getFont() {
        return font == null ? "" : " font-family=\"" + font + "\"";
    }

    protected String getHeadFont() {
        return font == null ? "" : font + ",";
    }

    /**
     * TWEST - New Method that answers a StringBuffer containing the totals
     * information. If no totals exist on the model answer an empty buffer.
     * 
     * The totals row will be given the same style as the header row.
     * 
     * @param model
     * @return StringBuffer containing the complete fo statement for totals
     */
    public StringBuffer totals(TableModel model) {

        StringBuffer sb = new StringBuffer();
        Export export = model.getExportHandler().getCurrentExport();
        String headerColor = export.getAttributeAsString(HEADER_COLOR);
        String headerBackgroundColor = export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

        Column firstCalcColumn = model.getColumnHandler().getFirstCalcColumn();

        if (firstCalcColumn != null) {
            int rows = firstCalcColumn.getCalc().length;
            for (int i = 0; i < rows; i++) {
                sb.append("<fo:table-row>");
                for (Iterator iter = model.getColumnHandler().getColumns().iterator(); iter.hasNext();) {
                    Column column = (Column) iter.next();
                    if (column.isFirstColumn()) {
                        String calcTitle = CalcUtils.getFirstCalcColumnTitleByPosition(model, i);
                        sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
                        sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\">");
                        sb.append(" <fo:block " + getFont() + ">" + calcTitle);
                        sb.append(" </fo:block></fo:table-cell> ");
                        continue;
                    }
                    if (column.isCalculated()) {
                        sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
                        sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");
                        sb.append(" <fo:block " + getFont() + ">");
                        CalcResult calcResult = CalcUtils.getCalcResultsByPosition(model, column, i);
                        Number value = calcResult.getValue();
                        if (value != null) {
                            sb.append(ExtremeUtils.formatNumber(column.getFormat(), value, model.getLocale()));
                        } else {
                            sb.append("n/a");
                        }
                        sb.append("</fo:block> ");
                    } else {
                        sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
                        sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");
                        sb.append(" <fo:block " + getFont() + ">");
                        sb.append(" ");
                        sb.append("</fo:block> ");
                    }
                    sb.append(" </fo:table-cell> ");
                }
                sb.append("</fo:table-row>");

            }
        }
        return sb;
    }
}
