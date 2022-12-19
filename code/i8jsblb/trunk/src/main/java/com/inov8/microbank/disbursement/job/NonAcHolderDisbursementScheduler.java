package com.inov8.microbank.disbursement.job;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.disbursement.service.BulkDisbursementsFacade;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

import com.inov8.microbank.server.service.statuscheckmodule.StatusCheckManager;
import org.apache.log4j.Logger;
//import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



@SuppressWarnings("all")
public class NonAcHolderDisbursementScheduler{

	private static Logger logger = Logger.getLogger(NonAcHolderDisbursementScheduler.class);
	private BulkDisbursementsFacade bulkDisbursementsFacade;
	private StatusCheckManager statusCheckManager;

	public void startExecution() throws JobExecutionException {
		logger.info("********** STARTING - BULK DISBURSEMENT (to CNIC) *****************");
		
		try {
			statusCheckManager.checkActiveMQStatus();
		} catch (Exception e) {
			throw new JobExecutionException("Error on checking Credit Queue status check. Cannot start Scheduler processing.");
		}

		Date currentDateTime = new Date(System.currentTimeMillis());
		long start = System.currentTimeMillis();
		
		logger.info("\n-Started NonAcHolderDisbursementScheduler on " + currentDateTime);
		
		ThreadLocalAppUser.setAppUserModel(new AppUserModel(2L));

		try {
			logger.info("##Starting Core Transations ");
			bulkDisbursementsFacade.makeCoreFundTransfer(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER, currentDateTime);
		} catch (Exception e) {
			logger.error("Exception occurred while NonAcHolderDisbursementScheduler.makeCoreFundTransfer --> Message:"+e.getMessage(),e);
		}

		try {
			logger.info("##Starting BLB Transations ");
			bulkDisbursementsFacade.makeBLBFundTransfer(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER, currentDateTime);
		} catch (Exception e) {
			logger.error("Exception occurred while NonAcHolderDisbursementScheduler.makeCoreFundTransfer --> Message:"+e.getMessage(),e);
		}

		logger.info("\n:-Ended Disbursement Scheduler Took : "+ ((System.currentTimeMillis() - start)/1000) + " second(s).");
		logger.info("********** Ending - BULK DISBURSEMENT (to CNIC) *****************");
	}
 	
	public void setBulkDisbursementsFacade(BulkDisbursementsFacade bulkDisbursementsFacade) {
		this.bulkDisbursementsFacade = bulkDisbursementsFacade;
	}

	public void setStatusCheckManager(StatusCheckManager statusCheckManager) {
		this.statusCheckManager = statusCheckManager;
	}
}