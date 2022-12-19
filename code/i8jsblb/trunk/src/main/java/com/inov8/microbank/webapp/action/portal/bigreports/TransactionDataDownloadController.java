package com.inov8.microbank.webapp.action.portal.bigreports;

import static com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR;
import static com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_BB_CUS_ACC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.util.ReportConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class TransactionDataDownloadController extends AjaxController {

	private static Log logger = LogFactory.getLog(TransactionDataDownloadController.class);
	private TransactionDetailI8Manager transactionDetailI8Manager;
	public static final String REPORT_FOLDER = "zip_reports";


	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override	
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String contextPath = getURLWithContextPath(request);
		String reportType = request.getParameter("reportType");
		String reportId = request.getParameter("reportId");
		String fileURL = super.getServletContext().getRealPath("WEB-INF");
		
		fileURL = fileURL.replace("WEB-INF", REPORT_FOLDER);
		
		ReportCriteriaSessionObject reportCriteriaSessionObject = null;

		if(!StringUtil.isNullOrEmpty(reportId)) {
			
			if(reportId.equals(ReportConstants.REPORT_BB_CUSTOMER_REPORT)) {

				reportCriteriaSessionObject = (ReportCriteriaSessionObject) request.getSession().getAttribute(REPORT_CRITERIA_SESSION_OBJ_BB_CUS_ACC);
				
			} else if(reportId.equals(ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT)) {
				
				reportCriteriaSessionObject = (ReportCriteriaSessionObject) request.getSession().getAttribute(REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR);
			}
		}
		
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		try {

			SearchBaseWrapper searchBaseWrapper = reportCriteriaSessionObject.getSearchBaseWrapper();		
			searchBaseWrapper.putObject("fileURL", fileURL);						
			searchBaseWrapper.putObject("reportType", reportType);											
			searchBaseWrapper.putObject(ReportConstants.REPORT_ID, reportId);						
			
			long start = System.currentTimeMillis();
			
			String fileName = this.transactionDetailI8Manager.createZipFile(searchBaseWrapper);
			
			reportCriteriaSessionObject.setFileName(fileName);
//			request.getSession().setAttribute(REPORT_CRITERIA_SESSION_OBJ, reportCriteriaSessionObject); 		todo
			
			ajaxXmlBuilder.addItem("fileURL", contextPath+fileName);

			logger.info("\n\n:- "+fileName+" Created "+ ((System.currentTimeMillis() - start)/1000) + ".s\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
		}
			
		
		return ajaxXmlBuilder.toString();
	}
	
	public String getURLWithContextPath(HttpServletRequest request) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(request.getScheme());
		stringBuilder.append("://");
		stringBuilder.append(request.getServerName());
		stringBuilder.append(":");
		stringBuilder.append(request.getServerPort());
		stringBuilder.append(request.getContextPath());
		
		logger.info(stringBuilder.toString());
		
		stringBuilder.append("/");
		stringBuilder.append(REPORT_FOLDER);
		stringBuilder.append("/");
		
		return stringBuilder.toString();
	}

	public void setTransactionDetailI8Manager(TransactionDetailI8Manager transactionDetailI8Manager) {
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}	

}