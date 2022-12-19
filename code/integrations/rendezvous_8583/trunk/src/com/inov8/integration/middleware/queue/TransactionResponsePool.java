package com.inov8.integration.middleware.queue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.PDUWrapper;

@Component
public class TransactionResponsePool {
	private Map<String, PDUWrapper> responsePool = new ConcurrentHashMap<String, PDUWrapper>();
	private static Logger logger = LoggerFactory.getLogger(TransactionResponsePool.class.getSimpleName());

	public void put(PDUWrapper pduWrapper) {
		if (pduWrapper != null) {
			responsePool.put(pduWrapper.getRRNKey(), pduWrapper);
			logger.debug("INSERTING RESPOSNE IN RESPONSE POOL WITH RRN: " + pduWrapper.getRRNKey());
			logger.debug("RESPONSE POOL SIZE: " + responsePool.size());
		}
	}

	public BasePDU get(String RRNkey) {
		//logger.debug("Searching Response Pool with RRN: " + RRNkey);
		BasePDU basePDU = null;
		if (RRNkey != null) {
			PDUWrapper pduWrapper = this.responsePool.get(RRNkey);
			if (pduWrapper != null) {
				basePDU = pduWrapper.getBasePDU();
			}
		} else {
			logger.error("INVALID RRN KEY PROVIDED");
			throw new IllegalArgumentException("INVALID ARGUMENT PROVIDED.");
		}
		return basePDU;
	}

	public boolean remove(String RRNkey) {
		boolean status = false;
		if (RRNkey != null) {
			PDUWrapper pduWrapper = this.responsePool.remove(RRNkey);
			if (pduWrapper != null) {
				status = true;
				logger.debug("RRN REMOVED FROM POOL: " + RRNkey);
			}
		}
		return status;
	}

}