package com.inov8.integration.pdu.request;

import com.inov8.integration.pdu.BasePDU;

public class AcquirerReversalAdvice extends BasePDU {
	private int pduLength = 239;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();

	public AcquirerReversalAdvice() {
		this.fields.addAll(header.getFields());
	}

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}
}
