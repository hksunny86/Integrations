package com.inov8.integration.pdu.response;

import com.inov8.integration.pdu.BasePDU;

public class LogonResponse extends BasePDU {
	private int pduLength = 239;
	private PhoenixResponseHeader header = new PhoenixResponseHeader();

	public LogonResponse() {
		this.fields.addAll(header.getHeaderFields());
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
	}
}
