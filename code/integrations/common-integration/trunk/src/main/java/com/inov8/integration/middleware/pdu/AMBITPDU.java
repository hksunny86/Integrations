package com.inov8.integration.middleware.pdu;

import java.io.Serializable;

public class AMBITPDU implements Serializable {

	private static final long serialVersionUID = 7105234427190145032L;

	private String rawPdu;
	private AMBITHeader header;

	public String getRawPdu() {
		return rawPdu;
	}

	public void setRawPdu(String rawPdu) {
		this.rawPdu = rawPdu;
	}
	
	public AMBITHeader getHeader() {
		return header;
	}

	public void setHeader(AMBITHeader header) {
		this.header = header;
	}

}
