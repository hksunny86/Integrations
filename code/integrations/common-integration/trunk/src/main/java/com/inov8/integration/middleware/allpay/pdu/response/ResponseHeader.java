package com.inov8.integration.middleware.allpay.pdu.response;

import java.io.Serializable;

public class ResponseHeader implements Serializable{

	private static final long serialVersionUID = -5039629266235571012L;

	private String responseCode;
	private String responseDescription;
	private String referenceNo;
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	
	
}
