package com.inov8.export;

import com.inov8.export.common.ConnectionManager;
import com.inov8.export.common.MessageSource;
import com.inov8.export.model.ExportRequestModel;
import com.inov8.export.service.ProcessExportRequest;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExportFileJobController {

	static Logger log = Logger.getLogger(ExportFileJobController.class);
	static Connection con;
	static ConnectionManager cm;
	

	public static void main(String[] args) throws Exception{	
	
		con =null;
		
		//Loading properties
		MessageSource ms = new MessageSource();
		
		//Search for Connection if not available
		while (con==null) {
			try {
				cm = new ConnectionManager();
			} catch (Exception e) {
				log.error("Unable to find DB Connection");
				TimeUnit.MINUTES.sleep(Long.parseLong(MessageSource.getPoperties("execution.rate").trim()));
			}
			
			if(null!=cm)
				con = cm.getConnection();
		}
		
		log.info("Starting File Export Job");		
		String exportRequestsSql = "SELECT * FROM EXPORT_REQUEST WHERE STATUS_ID =?";
		
		
		while (true) {
			//Loop starts
			log.info("***File Export Job- Searching New Export Request***");
			
			try {
				PreparedStatement pst = con.prepareStatement(exportRequestsSql);
				pst.setLong(1, 1);
				ResultSet rs = pst.executeQuery();
				List<ExportRequestModel> exportRequestModelList = new ArrayList<ExportRequestModel>();
				while (rs.next()) {
					ExportRequestModel exportModel = new ExportRequestModel();
					exportModel.setExportRequestId(rs.getLong("EXPORT_REQUEST_ID"));
					exportModel.setReportId(rs.getLong("REPORT_ID"));
					exportModel.setReportView(rs.getString("EXPORT_VIEW"));
					exportModel.setExportQuery(rs.getString("QUERY"));
					exportModel.setTotalsQuery(rs.getString("QUERY_ROWS"));
					exportModel.setStatusId(rs.getLong("STATUS_ID"));
					exportModel.setUsername(rs.getString("USER_NAME"));			
					exportModel.setCreatedon(rs.getTimestamp("CREATED_ON"));
					exportModel.setCreatedBy(rs.getLong("CREATED_BY"));
					exportModel.setUpdatedBy(rs.getLong("UPDATED_BY"));
					exportModel.setUpdatedOn(rs.getTimestamp("UPDATED_ON"));
					exportModel.setVersionNo(rs.getLong("VERSION_NO"));
					exportModel.setEmail(rs.getString("EMAIL"));
					exportModel.setPackageCall(rs.getString("PACKAGE_CALL"));
					exportModel.setFromDate(rs.getTimestamp("FROM_DATE"));
					exportModel.setToDate(rs.getTimestamp("TO_DATE"));
					exportModel.setAccountId(rs.getLong("ACCOUNT_ID"));
					exportModel.setDateType(rs.getString("DATE_TYPE"));
				
					exportRequestModelList.add(exportModel);

				}
				
				log.info("***File Export Job*** "+exportRequestModelList.size()+" Export Requests found");
				
				for (ExportRequestModel exportRequestModel : exportRequestModelList) {

					log.info("Pushing Export Request for Processing for Request ID : "
							+ exportRequestModel.getExportRequestId());
					ProcessExportRequest processExportRequest = new ProcessExportRequest(exportRequestModel);
					new Thread(processExportRequest).start();
				}

				TimeUnit.MINUTES.sleep(Long.parseLong(MessageSource.getPoperties("execution.rate").trim()));
			
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				con=null;
				cm=null;
				while (con==null) {
					try {
						cm = new ConnectionManager();
					} catch (Exception ex) {
						log.error("Unable to find DB Connection");
						TimeUnit.MINUTES.sleep(Long.parseLong(MessageSource.getPoperties("execution.rate").trim()));
					}
					
					if(null!=cm)
						con = cm.getConnection();
				}	
			}	

		}
						
	}
}
