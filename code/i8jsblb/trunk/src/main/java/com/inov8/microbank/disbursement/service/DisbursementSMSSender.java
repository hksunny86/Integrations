package com.inov8.microbank.disbursement.service;

import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.disbursement.dao.BulkDisbursementDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisbursementSMSSender extends Thread {

	private SmsSender smsSender;
	private Long disbursementFileInfoId;
	private BulkDisbursementDAO bulkDisbursementDAO;

	private final static Log logger = LogFactory.getLog(DisbursementSMSSender.class);

	public DisbursementSMSSender(SmsSender smsSender, BulkDisbursementDAO bulkDisbursementDAO, Long disbursementFileInfoId) {
		this.smsSender = smsSender;
		this.bulkDisbursementDAO = bulkDisbursementDAO;
		this.disbursementFileInfoId = disbursementFileInfoId;
	}

	public void run() {

		List<Object[]> disbursementList = bulkDisbursementDAO.loadDisbursementsForSMS(disbursementFileInfoId);
		if(CollectionUtils.isEmpty(disbursementList)) {
			logger.info("No Records available for SMS, Disbursement File Info Id : " + disbursementFileInfoId );

			return;
		}

		ArrayList<SmsMessage> sms = new ArrayList<>(disbursementList.size());
		String mobileNo = null;
		String batchNumber = null;
		for(Object[] bulkDisbursementsModel : disbursementList) {
			try {
				mobileNo = (String)bulkDisbursementsModel[0];
				Double amount = (Double)bulkDisbursementsModel[1];
				Date paymentDate = (Date)bulkDisbursementsModel[2];
				batchNumber = (String)bulkDisbursementsModel[3];
				Object[] params = { mobileNo, amount, PortalDateUtils.formatDateDefault(paymentDate)};
				String salaryDisbursementNotification = MessageUtil.getMessage("DISBURSEMENT.UPLOAD", params);

				sms.add(new SmsMessage(mobileNo, salaryDisbursementNotification));
			}

			catch (Exception e) {
				logger.error("Error in preparing SMS to "+ mobileNo + " for batch : " + batchNumber);
			}
		}

		try {
			smsSender.send(sms);
		}

		catch (Exception e) {
			logger.error("Error in sms sending. Error Message : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
