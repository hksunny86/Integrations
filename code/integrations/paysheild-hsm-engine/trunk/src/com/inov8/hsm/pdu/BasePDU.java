package com.inov8.hsm.pdu;

import java.io.Serializable;

public class BasePDU implements Serializable {
	
	private static final long serialVersionUID = 7105234427190145032L;
	
	private byte[] rawPdu;
	private BaseHeader header;

	public byte[] getRawPdu() {
		return rawPdu;
	}

	public void setRawPdu(byte[] rawPdu) {
		this.rawPdu = rawPdu;
	}

	public BaseHeader getHeader() {
		return header;
	}

	public void setHeader(BaseHeader header) {
		this.header = header;
	}


}
