package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;

public class PurchaseReversalRequest implements Serializable {

	private static final long serialVersionUID = -5173766657748720495L;

	private RequestHeader header;
	private String transactionId;

	public RequestHeader getHeader() {
		return header;
	}

	public void setHeader(RequestHeader header) {
		this.header = header;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
