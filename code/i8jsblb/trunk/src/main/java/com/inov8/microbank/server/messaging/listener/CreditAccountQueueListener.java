package com.inov8.microbank.server.messaging.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.ola.integration.vo.OLAVO;

public class CreditAccountQueueListener implements MessageListener {

	private static Log logger = LogFactory.getLog(CreditAccountQueueListener.class);

	private String delay;
	private OLAManager olaManager;

	@Override
	public void onMessage(Message message) {

		OLAVO olaVO = null;

		try {

			olaVO = (OLAVO) ((ObjectMessage) message).getObject();

			if(olaVO.getLedgerId().longValue() == -99L){
				logger.info("[ActiveMQ-Health-Check] going to ignore the message...");
				return;
			}

			Thread.sleep(Long.valueOf(delay));
			logger.info("CreditAccountQueueListener for Ledger-Id :: " + olaVO.getLedgerId());
			logger.info("Starting execution for Transaction ID: " + olaVO.getMicrobankTransactionCode());
			olaManager.makeTxForQueue(olaVO);
			logger.info("Completed execution for Transaction ID: " + olaVO.getMicrobankTransactionCode());

		} catch (Exception ex) {
			logger.error("Exception occured @ CreditAccountQueueListener... failed to perform Transaction for Ledger_Id :" + ((olaVO != null)?olaVO.getLedgerId():""));
			logger.error(ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public void setOlaManager(OLAManager olaManager) {
		this.olaManager = olaManager;
	}
}