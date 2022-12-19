package com.inov8.hsm.pdu.response;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class GeneratePINBlockResponse extends BasePDU {

	private static final long serialVersionUID = -5285310022047911920L;

	private String responseCode;
	private String pinBlock;

	public GeneratePINBlockResponse(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getPinBlock() {
		return pinBlock;
	}

	public void setPinBlock(String pinBlock) {
		this.pinBlock = pinBlock;
	}


}
