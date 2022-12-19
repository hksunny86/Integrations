package com.inov8.microbank.disbursement.job;

import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.disbursement.service.BulkDisbursementsFacade;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

import com.inov8.microbank.server.service.statuscheckmodule.StatusCheckManager;
import org.apache.log4j.Logger;
//import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@SuppressWarnings("all")
public class AcHolderDisbursementScheduler{
	private static Logger logger = Logger.getLogger(AcHolderDisbursementScheduler.class);

	private BulkDisbursementsFacade bulkDisbursementsFacade;
	private StatusCheckManager statusCheckManager;

	public void startExecution() throws JobExecutionException {
		logger.info("********** STARTING - BULK DISBURSEMENT (to Acc Holder) *****************");
		
		try {
			statusCheckManager.checkActiveMQStatus();
		} catch (Exception e) {
			throw new JobExecutionException("Error on checking Credit Queue status check. Cannot start Scheduler processing.");
		}

		Date currentDateTime = new Date(System.currentTimeMillis());
		
		try {
			bulkDisbursementsFacade.makeCoreFundTransfer(ServiceConstantsInterface.BULK_DISB_ACC_HOLDER, currentDateTime);
		} catch (Exception e) {
			logger.error("Exception occurred while AcHolderDisbursementScheduler.makeCoreFundTransfer --> Message:"+e.getMessage(),e);
		}
		
		
		try {
			bulkDisbursementsFacade.makeBLBFundTransfer(ServiceConstantsInterface.BULK_DISB_ACC_HOLDER, currentDateTime);
		} catch (Exception e) {
			logger.error("Exception occurred while AcHolderDisbursementScheduler.makeBLBFundTransfer --> Message:"+e.getMessage(),e);
		}
		logger.info("********** ENDING - BULK DISBURSEMENT (to Acc Holder) *****************");
		
	}

	public void setBulkDisbursementsFacade(BulkDisbursementsFacade bulkDisbursementsFacade) {
		this.bulkDisbursementsFacade = bulkDisbursementsFacade;
	}

	public void setStatusCheckManager(StatusCheckManager statusCheckManager) {
		this.statusCheckManager = statusCheckManager;
	}
}