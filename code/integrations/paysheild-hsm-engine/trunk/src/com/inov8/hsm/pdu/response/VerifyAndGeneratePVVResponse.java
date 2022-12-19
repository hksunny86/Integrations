package com.inov8.hsm.pdu.response;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class VerifyAndGeneratePVVResponse extends BasePDU {

	private static final long serialVersionUID = 5023680838994764882L;
	
	private String responseCode;
	private String PVV;

	public VerifyAndGeneratePVVResponse(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getPVV() {
		return PVV;
	}

	public void setPVV(String pVV) {
		PVV = pVV;
	}

}
