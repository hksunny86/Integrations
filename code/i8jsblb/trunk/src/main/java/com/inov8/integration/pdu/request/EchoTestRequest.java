package com.inov8.integration.pdu.request;

import com.inov8.integration.pdu.BasePDU;

public class EchoTestRequest extends BasePDU {
	int pduLength = 239;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();

	public EchoTestRequest() {
		this.fields.addAll(header.getFields());
	}

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}
}
