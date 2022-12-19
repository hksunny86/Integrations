package com.inov8.microbank.disbursement.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.TaxRegimeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.disbursement.vo.DisbursementVO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.tax.service.TaxManager;

/**
 * @author Kashif
 *
 */

public class NonAcHolderDisbursementThread implements Runnable {

	private static Logger logger = Logger.getLogger(NonAcHolderDisbursementThread.class);
	private Integer threadNumber = null;
	private BulkDisbursementsManager bulkDisbursementsManager;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
	private List<DisbursementVO> disbursementVOList = null;
	private CountDownLatch startSignal;
	private CountDownLatch doneSignal;
	private SmsSender smsSender;
	private WorkFlowWrapper workFlowWrapper = null;
	private TaxManager taxManager;
	
	public NonAcHolderDisbursementThread(Integer threadNumber, BulkDisbursementsManager bulkDisbursementsManager,
			CreditAccountQueingPreProcessor creditAccountQueingPreProcessor, List<DisbursementVO> disbursementVOList,
										 WorkFlowWrapper workFlowWrapper, SmsSender smsSender, CountDownLatch startSignal, CountDownLatch doneSignal,
										 TaxManager taxManager) {
		
		this.threadNumber = threadNumber;
		this.bulkDisbursementsManager = bulkDisbursementsManager;
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
		this.disbursementVOList = disbursementVOList;
		this.workFlowWrapper = workFlowWrapper;
		this.smsSender = smsSender;
		this.startSignal = startSignal;
	    this.doneSignal = doneSignal;
        this.taxManager = taxManager;
	}
 
	@Override
	public void run() {
		//startSignal.await();
		logger.debug("Thread Started # " + threadNumber + " Records to Perform : " + disbursementVOList.size());

		ArrayList<SmsMessage> sms = new ArrayList<>(disbursementVOList.size());
		
        TaxRegimeModel taxRegimeModel = null;
		try {
			taxRegimeModel = taxManager.searchTaxRegimeById(TaxRegimeConstants.FEDERAL);
		} catch (FrameworkCheckedException e1) {
			logger.error("[NonAcHolderDisbursementThread] Exception occurred while searchTaxRegimeById: 3", e1);
		}
		
		if(taxRegimeModel == null || taxRegimeModel.getTaxRegimeId() == null){
			logger.error("[NonAcHolderDisbursementThread] TaxRegime for ID: 3 not loaded... So this thread is gonna be stop...");
			return;
		}
        
        for(DisbursementVO disbursementVO : disbursementVOList) {
			try {

				long start = System.currentTimeMillis();

				Double txAmount = disbursementVO.getAmount();
				if(txAmount != null && txAmount > 0.0D) {
					WorkFlowWrapper wrapper = workFlowWrapper.cloneForDisbursement();
					wrapper.setTaxRegimeModel(taxRegimeModel);
					
					wrapper = bulkDisbursementsManager.nonAcHolderFundTransfer(disbursementVO, wrapper);
					creditAccountQueingPreProcessor.startProcessing(wrapper);

					SmsMessage smsMessage = (SmsMessage) wrapper.getObject(CommandFieldConstants.KEY_SMS_MESSAGE);
					if(smsMessage != null)
						sms.add(smsMessage);

					logger.info("Disbursement Id " + disbursementVO.getDisbursementId() + " Tx Code : " + wrapper.getTransactionCodeModel().getCode() +
					" Time taken " + (System.currentTimeMillis() - start)/1000.0d  +" seconds.");
				}
			}

			catch (Exception e) {
				logger.error("[NonAcHolderDisbursementThread] Exception occurred:");
				e.printStackTrace();
			}
		}

		try{
			if(CollectionUtils.isNotEmpty(sms)) {
				logger.info("Sending SMS for successful transactions. Count : " + sms.size());
				smsSender.send(sms);
			}

		}

		catch (Exception e) {
			logger.error(e);
		}

		//doneSignal.countDown();
	}

	public void setTaxManager(TaxManager taxManager) {
		this.taxManager = taxManager;
	}
}