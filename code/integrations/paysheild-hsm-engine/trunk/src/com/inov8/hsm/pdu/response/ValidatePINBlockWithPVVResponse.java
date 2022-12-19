package com.inov8.hsm.pdu.response;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class ValidatePINBlockWithPVVResponse extends BasePDU {


	private static final long serialVersionUID = 2297628885304541638L;
	
	private String responseCode;

	public ValidatePINBlockWithPVVResponse(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}


}
