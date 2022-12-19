package com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.util.ReportConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.ola.util.EncryptionUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hibernate.Session;
import org.springframework.context.MessageSource;

import javax.persistence.Column;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TransactionDataDownloadHibernateDAO extends BaseHibernateDAO<BasePersistableModel, Long, TransactionDataDownloadHibernateDAO> implements TransactionDataDownloadDAO {

	private MessageSource messageSource;

	/**
	 * @param _class
	 * @param entity
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private Map<String, Object> getValueMap(Class _class, Object entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	
    	Map<String, Object> columnValueMap = new HashMap<String, Object>(0);	
    	
    	for (Method method : _class.getDeclaredMethods()) {

        Annotation[] annotations = method.getDeclaredAnnotations();
        
        if(annotations.length>0 && method.getName().startsWith("get")) {
        	
        	for(Annotation annotation : annotations) {
        		
        	    if(annotation instanceof Column) {
        	    	
        	        Column myAnnotation = (Column) annotation;
        	        
        	        Object obj = method.invoke(entity);
        	        
        	        if(obj instanceof Boolean) {
        	        	
        	        	Boolean value = (Boolean) obj;
        	        	
        	        	obj = (value ? 1 : 0);
        	        }
        	        
        	        if(obj != null) {
            	        
            	        columnValueMap.put(myAnnotation.name(), obj);
            	        
        	        }
        	    }
        	}
        }
        	
   		}    	
    	
    	return columnValueMap;
    }
    
    
    /**
     * @param reportId
     * @param dateRangeHolderModel
     * @param basePersistableModel
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private String createQuery(String reportId, DateRangeHolderModel dateRangeHolderModel, BasePersistableModel basePersistableModel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	
    	String query = null;
    	Date startDate = null;				
    	Date endDate = null;
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		
    	Map<String, Object> columnValueMap = null;;
    	
    	if(ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT.equals(reportId)) {

    		query = this.messageSource.getMessage(ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT_QUERY, null, null);
    		columnValueMap = getValueMap(basePersistableModel.getClass().getSuperclass(), basePersistableModel);
    	
    	} else if(ReportConstants.REPORT_BB_CUSTOMER_REPORT.equals(reportId)) {

    		query = this.messageSource.getMessage(ReportConstants.REPORT_BB_CUSTOMER_REPORT_QUERY, null, null);
    		columnValueMap = getValueMap(basePersistableModel.getClass(), basePersistableModel);
    	}    	
    	
    	if(dateRangeHolderModel != null) {
    	    startDate = dateRangeHolderModel.getFromDate();				
    	    endDate = dateRangeHolderModel.getToDate();
    	}
    	
    	Boolean isWhereClause = Boolean.FALSE;
    	
    	StringBuilder queryBuiler = new StringBuilder(query);
    	queryBuiler.append(" WHERE ");

    	Set<String> set = columnValueMap.keySet();
    	
    	Iterator<String> iterator = set.iterator();
    	
    	while(iterator.hasNext()) {
    		
    		String column = iterator.next();
    		Object value = columnValueMap.get(column);

        	if(isWhereClause) {
        		queryBuiler.append(" AND ");
        	}        		
    		
    		if(value instanceof Number) {

    			queryBuiler.append(column +" = "+value);
            	isWhereClause = Boolean.TRUE;        		
            	
    		} else if(value instanceof String) {

    			queryBuiler.append(" LOWER("+column+") LIKE '"+value+"%'");
            	isWhereClause = Boolean.TRUE;
    		}

    		if(!isWhereClause) {
    			isWhereClause = Boolean.TRUE;
    		}
    	}
    	
    	if(startDate != null) {

        	if(isWhereClause) {
        		queryBuiler.append(" AND");
        	}
        	
        	String start_date = format.format(startDate);
        	
	       	queryBuiler.append(" (1=1) AND CREATED_ON >= TO_TIMESTAMP('"+start_date+"', 'DD-Mon-YYYY HH24:MI')");
        	isWhereClause = Boolean.TRUE;
    	}
    	
    	if(endDate != null) {

        	if(isWhereClause) {
        		queryBuiler.append(" AND");
        	}

        	String end_date = format.format(endDate);
        	
        	queryBuiler.append(" (1=1) AND CREATED_ON <= TO_TIMESTAMP('"+end_date+"', 'DD-Mon-YYYY HH24:MI')");
        	isWhereClause = Boolean.TRUE;
    	}        	

    	if(startDate != null) {
    		queryBuiler.append(" ORDER BY CREATED_ON DESC");
    	}
    	
    	logger.info("\n"+queryBuiler.toString());

    	return queryBuiler.toString();
    }
    
    
    /**
     * @param reportId
     * @param headers
     * @param decryptColumns
     */
    private void prepareReportParams(String reportId, StringBuilder headers, List<Integer> decryptColumns) {

    	String reportHeader = null;
    	
    	if(ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT.equals(reportId)) {

    		reportHeader = this.messageSource.getMessage(ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT_HEADERS, null, null);  		
    		
    	} else if(ReportConstants.REPORT_BB_CUSTOMER_REPORT.equals(reportId)) {

    		reportHeader = this.messageSource.getMessage(ReportConstants.REPORT_BB_CUSTOMER_REPORT_HEADERS, null, null);
    	/*	decryptColumns.add(11);
    		decryptColumns.add(10);
    		decryptColumns.add(8);*/
    	}    	
    	
    	headers.append(reportHeader);
    }
    
    
    /* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate.TransactionDataDownloadDAO#createZipFile(com.inov8.framework.common.wrapper.SearchBaseWrapper)
	 */
    @Override
	public String createZipFile(SearchBaseWrapper searchBaseWrapper) throws Exception {

    	String fetchSize = this.messageSource.getMessage("report.fetch.size", null, null);
    	String reportType = ((String) searchBaseWrapper.getObject("reportType")).toLowerCase();
    	String reportId = (String) searchBaseWrapper.getObject("reportId");
    	StringBuilder headers = new StringBuilder();
    	List<Integer> decryptColumns = new ArrayList<Integer>(0);

    	prepareReportParams(reportId, headers, decryptColumns);
    	
		String fileName = reportId+"_"+String.valueOf(System.currentTimeMillis());
    	String path = (String) searchBaseWrapper.getObject("fileURL")+"//";
    	
		new Thread(new TransactionDataDeleteThread(path)).start();
    	
    	String fullPath = path + fileName +"."+ reportType;    		
    	File file = new File(fullPath);

    	logger.info("\n:- Report Type/ID : "+reportType+" / "+reportId);

    	Session session = null;
        Connection connection = null;
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	String query = this.createQuery(reportId, searchBaseWrapper.getDateRangeHolderModel(), searchBaseWrapper.getBasePersistableModel());    	
    	
    	try {

        	session = super.getHibernateTemplate().getSessionFactory().openSession();
        	connection = session.connection();
    		preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFetchSize(Integer.valueOf(fetchSize));
    		resultSet = preparedStatement.executeQuery();

            StringBuilder content = new StringBuilder();
            
            if(reportType.equalsIgnoreCase("csv")) {
            	
            	content.append(headers.toString()+"\n");
            	
            	String fileData = generateCSV(resultSet, content, decryptColumns);
            		
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw); 
                    
                if(fileData.length() > 0){
                   	bw.write(content.toString());
                }
                    
                bw.close();
                fw.close();
            	
                logger.info("\n:- Generated CSV File : "+file.getName());
                
            } else if(reportType.equalsIgnoreCase("xls")) {

            	HSSFWorkbook workbook = generateXLS(resultSet, headers.toString().split(","), decryptColumns);
                    
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.flush();
                out.close();
                workbook = null;
                logger.info("\n:- Generated XLS File : "+file.getName());
            		
            } else if(reportType.equalsIgnoreCase("xlsx")) {
            	            	
            		org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = generateXLSX(resultSet, headers.toString().split(","), decryptColumns);
                    
                    FileOutputStream out = new FileOutputStream(file);
                    workbook.write(out);
                    out.flush();
                    out.close();
                    
                    workbook.dispose();
                    logger.info("\n:- Generated XLSX File : "+file.getName());
            }
                        
            fileName += ".zip";

            convertZIP(file, (path + fileName));
    		
    	} catch (Exception e) {
    		fileName = null;
    		logger.error("Error in TransactionDataDowloadHibernateDAO.createZipFile() :: " + e);
    		
    	} finally {
    		
    	    try {
    	    	
    	    	if (resultSet != null) {
    	    		logger.info("Closing RS");
    	    		resultSet.close();
    	    	}
    	    	
    	    	if (preparedStatement != null) {
    	    		logger.info("Closing PS");
    	    		preparedStatement.close();
    	    	}
    	    	
    	    	if (connection != null) {
    	    		logger.info("Closing C");
    	    		connection.close();
    	    	}
    	    	
    	    	if (session != null) {
    	    		logger.info("Closing S");
    	    		session.close();
    	    	}
    	    
    	    } catch (Exception e) {
    	    	logger.warn("Closeing Session : Exception ");
				logger.error("Error in TransactionDataDowloadHibernateDAO.createZipFile() :: " + e);
    	    }
    	}
                    
    	return fileName;
    }
    
	
	/**
	 * @param resultSet
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	private String generateCSV(ResultSet resultSet, StringBuilder content, List<Integer> decryptColumns) throws Exception {

    	logger.info("\n:- generateCSV(...)");
		int columnCount = resultSet.getMetaData().getColumnCount();

		while(resultSet.next()) {
		
	   		for(int x=1; x<=columnCount; x++) {
	
				String columnData = resultSet.getString(x);
    			
    			if(decryptColumns.contains(Integer.valueOf(x))) {
    				    				
    				if(StringUtil.isNumeric(columnData)) {

        				columnData = EncryptionUtil.decryptAccountNo(columnData);
        				
    				} else {
        				
//        				columnData = EncryptionUtil.decryptPin(columnData);
    				}
    			}				
				
				content.append(StringUtil.isNullOrEmpty(columnData) ? "" : columnData);
				
				if(x != (columnCount)) {
	    			content.append(",");
				} else {
	    			content.append("\n");
				}
	   		}
		}
		
		return content.toString();
	}
    
   
	/**
	 * @param resultSet
	 * @param headers
	 * @return
	 * @throws Exception 
	 */
	private org.apache.poi.xssf.streaming.SXSSFWorkbook generateXLSX(ResultSet resultSet, String[] headers, List<Integer> decryptColumns) throws Exception {

    	logger.info("\n:- generateXLSX(...)");
		int columnCount = resultSet.getMetaData().getColumnCount();
		
		SXSSFWorkbook  workbook = new SXSSFWorkbook();
		
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet();
		
		org.apache.poi.ss.usermodel.Row row = sheet.createRow(0);
		
		int i = 0;
		
		for(String header : headers) {

			org.apache.poi.ss.usermodel.Cell cell = row.createCell((short)i);
			cell.setCellValue(header);
			i++;
		}
		
		i = 1;
		
        while (resultSet.next()) {           		
    		
    		row = sheet.createRow(i);

    		for(int x=1; x<=columnCount; x++) {
    			
    			String columnData = resultSet.getString(x);
    			
    			if(decryptColumns.contains(Integer.valueOf(x))) {
    				    				
    				if(StringUtil.isNumeric(columnData)) {

        				columnData = EncryptionUtil.decryptAccountNo(columnData);
        				
    				} else {
        				
//        				columnData = EncryptionUtil.decryptPin(columnData);
    				}
    			}
 
    			org.apache.poi.ss.usermodel.Cell cell = row.createCell((short)x-1);
    			cell.setCellValue(StringUtil.isNullOrEmpty(columnData) ? "" : columnData);
    		}
    		 
            if(i % 100 == 0) {
            	
            	org.apache.poi.xssf.streaming.SXSSFSheet _sheet = (org.apache.poi.xssf.streaming.SXSSFSheet) sheet; 
            	
                 _sheet.flushRows(100);
            }    		
    		
    		i++;
    	}
        
		return workbook;
	}
    
   
	/**
	 * @param resultSet
	 * @param headers
	 * @return
	 * @throws Exception 
	 */
	private HSSFWorkbook generateXLS(ResultSet resultSet, String[] headers, List<Integer> decryptColumns) throws Exception {

    	logger.info("\n:- generateXLS(...)");
		int columnCount = resultSet.getMetaData().getColumnCount();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("REPORT I");
		
		HSSFRow row = sheet.createRow(0);
		
		int i = 0;
		
		for(String header : headers) {

			HSSFCell cell = row.createCell((short)i);
			cell.setCellValue(header);
			i++;
		}
				
		i = 1;
		
        while (resultSet.next()) {           		
    		
    		row = sheet.createRow(i);

    		for(int x=1; x<=columnCount; x++) {

    			String columnData = resultSet.getString(x);
    			
    			if(decryptColumns.contains(Integer.valueOf(x))) {
    				    				
    				if(StringUtil.isNumeric(columnData)) {

        				columnData = EncryptionUtil.decryptAccountNo(columnData);
        				
    				} else {
        				
//        				columnData = EncryptionUtil.decryptPin(columnData);
    				}
    			}
    			
    			HSSFCell cell = row.createCell((short)(x-1));
    			cell.setCellValue(StringUtil.isNullOrEmpty(columnData) ? "" : columnData);
    		}
    		
    		i++;
    	}
        
		return workbook;
	}
	

    /**
     * @param inputFile
     * @param zipFilePath
     */
    private void convertZIP(File inputFile, String zipFilePath) {
    	
		long start = System.currentTimeMillis();
        
    	try {

            // Wrap a FileOutputStream around a ZipOutputStream to store the zip stream to a file. Note that this is not absolutely necessary
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            // a ZipEntry represents a file entry in the zip archive We name the ZipEntry after the original file's name
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream fileInputStream = new FileInputStream(inputFile);
            byte[] buf = new byte[1024];
            int bytesRead;

            // Read the input file by chucks of 1024 bytes and write the read bytes to the zip stream
            
            while((bytesRead = fileInputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, bytesRead);
            }

            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();

            zipOutputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("\n:- Converted File in Zip : "+inputFile.getName()+ " "+ ((System.currentTimeMillis() - start)/1000) + "s");
    }
	
    public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}