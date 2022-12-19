package com.inov8.integration.boss.controller;

import com.inov8.integration.pdu.BossPdu;

public class LimitEnhancementResponse extends BossPdu {
	private static final long serialVersionUID = -2077532714796193184L;
	
	private String transactionID;
	private String responseCode;
	private String responseCodeDescription;
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseCodeDescription() {
		return responseCodeDescription;
	}
	public void setResponseCodeDescription(String responseCodeDescription) {
		this.responseCodeDescription = responseCodeDescription;
	}
	
	
}
