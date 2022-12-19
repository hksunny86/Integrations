package com.inov8.export.service;

import com.inov8.export.common.ConnectionManager;
import com.inov8.export.common.MailEngine;
import com.inov8.export.model.ExportInformationModel;
import com.inov8.export.model.ExportRequestModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.extremecomponents.table.core.TableConstants;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;

public class ProcessExportRequest implements Runnable {
	
	static Logger log = Logger.getLogger(ProcessExportRequest.class.getName());
	
	ExportRequestModel exportRequestModel;
	Connection con;
	
	public ProcessExportRequest(ExportRequestModel exportRequestModel) throws Exception {
		this.exportRequestModel = exportRequestModel;
		con = new ConnectionManager().getConnection();
	}

	public void run() {
		PreparedStatement pst3 = null;
		try 
		{
			log.info("*** ProcessExportReuest- Starts Processing Export Request ID:"+exportRequestModel.getExportRequestId()+ "****");

			//update request status
			String updateRequestsStatusSql = "UPDATE EXPORT_REQUEST SET STATUS_ID =? WHERE EXPORT_REQUEST_ID =?";
			pst3 = con.prepareStatement(updateRequestsStatusSql);
			pst3.setLong(1,2);
			pst3.setLong(2,exportRequestModel.getExportRequestId());
			
			int i = pst3.executeUpdate();

			ExportInformationModel exportInfoModel = getExportInfoModel();
			Statement statement = con.createStatement(); 
			
			if(null!=exportRequestModel.getPackageCall())
			{
				String sqlcall = exportRequestModel.getPackageCall();
				
				String fromDate = "to_date('" + DateFormatUtils.format(exportRequestModel.getFromDate(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
	            String toDate = "to_date('" + DateFormatUtils.format(exportRequestModel.getToDate(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
	            
	            sqlcall = StringUtils.replaceOnce(sqlcall,"?",fromDate);
	            sqlcall = StringUtils.replaceOnce(sqlcall,"?",toDate);
				if(exportRequestModel.getAccountId() != null) {
					sqlcall = StringUtils.replaceOnce(sqlcall, "?", String.valueOf(exportRequestModel.getAccountId()));
				}
	            sqlcall = StringUtils.replaceOnce(sqlcall,"?","'"+exportRequestModel.getDateType()+"'");

				log.info("*** Setting Dates - "+sqlcall+" ***");
				statement.execute(sqlcall);
			}
			
			
			Long startTime= System.currentTimeMillis();
	    	log.info("******Start Time for Query "+ new Date()+"******\n"+exportRequestModel.getExportQuery());
		
			ResultSet exportResultSet = statement.executeQuery(exportRequestModel.getExportQuery());
			
			ResultSet totalsResultSet = null;
			Statement statement2=null; 
			
			if(exportInfoModel.getPropsFormatsMap() != null && exportInfoModel.getPropsFormatsMap().values().contains(TableConstants.CURRENCY))
			{	            
				
				statement2= con.createStatement(); 
				
				if(null!=exportRequestModel.getPackageCall())
				{
					String sqlcall = exportRequestModel.getPackageCall();
					
					String fromDate = "to_date('" + DateFormatUtils.format(exportRequestModel.getFromDate(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
		            String toDate = "to_date('" + DateFormatUtils.format(exportRequestModel.getToDate(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
		            
		            
		            sqlcall = StringUtils.replaceOnce(sqlcall,"?",fromDate);
		            sqlcall = StringUtils.replaceOnce(sqlcall,"?",toDate);
					if(exportRequestModel.getAccountId() != null) {
						sqlcall = StringUtils.replaceOnce(sqlcall, "?", String.valueOf(exportRequestModel.getAccountId()));
					}
					
					log.info("*** Setting Dates - "+sqlcall+" ***");
					statement2.execute(sqlcall);
				}
				
				totalsResultSet = statement2.executeQuery(exportRequestModel.getTotalsQuery());
			}
			log.info("*****End Time for Query "+ new Date()+"*****");
	    	Long endTime= System.currentTimeMillis();			 
			log.info("*****Total Query time consumed in seconds : "+ (endTime-startTime)/1000+"*****");
			
			startTime= System.currentTimeMillis();
			log.info("Start Time for Export "+ new Date());

			if(exportRequestModel.getReportId() == 2L)
			{

				exportInfoModel.setCnicCellColumnsIndexes("3");
				exportInfoModel.setDobCellColumnsIndexes("5");
				exportInfoModel.setAccountCellColumnsIndexes("6");
				exportInfoModel.setBalaceCellColumnsIndexes("7");
			}
			String filePath = ExportViewEnum.valueOf(exportRequestModel.getReportView().toUpperCase())
					.export(exportRequestModel, exportInfoModel, exportResultSet, totalsResultSet, con);
			
	        log.info("End Time for Export "+ new Date());
	        endTime= System.currentTimeMillis();	        
			log.info("Total File Writing time consumed for writing records in seconds : "+ (endTime-startTime)/1000);
			
			//update request status
			pst3.setLong(1,0);
			pst3.setLong(2,exportRequestModel.getExportRequestId());
			
			i = pst3.executeUpdate();
			
			///closing resources 
			
			exportResultSet.close();
			statement.close();
			
			if(null!=totalsResultSet && (!totalsResultSet.isClosed())){
				totalsResultSet.close();
				statement.close();
			}
			pst3.close();			

			startTime= System.currentTimeMillis();
			String zipFileName = makeZip(filePath);
			
			endTime= System.currentTimeMillis();			 
			log.info("Total Zip making time consumed in seconds : "+ (endTime-startTime)/1000);	
			
			
			if(null!=exportRequestModel.getEmail()){
				MailEngine me = new MailEngine();			
				me.sendEmail(zipFileName+" is ready to download",exportRequestModel.getEmail());
			}
		
		} catch (Exception e) {

			try {
				if(pst3 != null) {
					pst3.setLong(1, 1);
					pst3.setLong(2, exportRequestModel.getExportRequestId());
					pst3.executeUpdate();
				}
			}catch (Exception e1) {
				log.error("Unable to update status in case of error");
				e1.printStackTrace();
			}
			log.error(e,e);
		}

		finally {
			try {
				if(null!=con && (!con.isClosed())){
					con.close();
				}
			} catch (SQLException e) {
				log.error("Unable to close connection",e);
			}
		}
	}

	private ExportInformationModel getExportInfoModel()
			throws SQLException, IOException, JsonParseException,JsonMappingException 
			{
		ExportInformationModel exportInfoModel1 =null;
		
		String exportMetaDataQuery = "SELECT METADATA FROM REPORTS_METADATA WHERE REPORT_ID=?";	
		PreparedStatement pst = con.prepareStatement(exportMetaDataQuery);
		pst.setLong(1,exportRequestModel.getReportId());
		ResultSet rs = pst.executeQuery();
		
		if(rs.next()){
			String reportMataData =  rs.getString(1);
			ObjectMapper mapper = new ObjectMapper();	            
			exportInfoModel1= mapper.readValue(reportMataData, ExportInformationModel.class);
		}
		
		pst.close();
		rs.close();
		return exportInfoModel1;
	}
	
	 private String makeZip(String filePath)
	    {
	        String zipFileName = null;
	        try{
	            log.info(" Compressing File " + filePath);
	            String gzipFilePath = filePath+".gz";
	            String zipFilePath;
	            if(filePath.endsWith("xlsx"))
	            {
	                zipFilePath = filePath.substring(0,filePath.length()-5)+".zip";
	            }
	            else
	            {
	                zipFilePath = filePath.substring(0,filePath.length()-4)+".zip";
	            }
	            String zipCommand = "sudo gzip " + filePath;
	            String mvCommand = "mv " + gzipFilePath + " " + zipFilePath;
	            log.info(" Executing command> " + zipCommand);
	            Process zipProcess = Runtime.getRuntime().exec(zipCommand);
	            zipProcess.waitFor();
	            log.info(" Executing command> " + mvCommand);
	            Process renameProcess = Runtime.getRuntime().exec(mvCommand);//rename .gz to .zip
	            renameProcess.waitFor();
	            log.info("Zip Done.");
	            zipFileName = Paths.get(zipFilePath).getFileName().toString();
	      
	        }catch(Exception e){
	        	log.error(e.getMessage());
	            e.printStackTrace();
	        }
	        return zipFileName;
	    }
}
