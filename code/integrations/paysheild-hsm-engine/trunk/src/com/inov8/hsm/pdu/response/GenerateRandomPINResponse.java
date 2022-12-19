package com.inov8.hsm.pdu.response;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class GenerateRandomPINResponse extends BasePDU {

	private static final long serialVersionUID = -5285310022047911920L;

	private String responseCode;
	private String PIN;

	public GenerateRandomPINResponse(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String pIN) {
		PIN = pIN;
	}

}
