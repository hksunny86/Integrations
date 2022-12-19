package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.SmsSender;

/**
 * @author Kashif
 *
 */
public class BulkDisbursementSMSSender extends Thread {

	private SmsSender smsSender;
	private MessageSource messageSource;
	private List<BulkDisbursementsModel> disbursementList;
	
	private final static Log logger = LogFactory.getLog(BulkDisbursementSMSSender.class);
	
	
	public BulkDisbursementSMSSender(SmsSender smsSender, MessageSource messageSource, List<BulkDisbursementsModel> disbursementList) {
		
		this.smsSender = smsSender;
		this.disbursementList = disbursementList;
		this.messageSource  = messageSource;
	}
	
	
	public void run() {
		
//		logger.info("Start Sending SMS for Bulk Disbursement ");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		for(BulkDisbursementsModel bulkDisbursementsModel : disbursementList) {
			
			Object[] params = { bulkDisbursementsModel.getMobileNo(),
								Formatter.formatDouble(bulkDisbursementsModel.getAmount()),
								dateFormat.format(bulkDisbursementsModel.getPaymentDate()),
								bulkDisbursementsModel.getDescription() };
			
			String salaryDisbursementNotification = messageSource.getMessage("SALARY.DISBURSEMENT.UPLOAD", params, null);
						
			try {
				
				smsSender.send(new SmsMessage(bulkDisbursementsModel.getMobileNo(), salaryDisbursementNotification));
				
				logger.info("Sent Message to "+ bulkDisbursementsModel.getMobileNo());
				
			} catch (FrameworkCheckedException e) {

				logger.info("Error Sending Message to "+ bulkDisbursementsModel.getMobileNo());
				logger.error(e);
			}
		}

		disbursementList = null;
		
//		logger.info("End Sending SMS for Bulk Disbursement ");
	}
}
