package com.inov8.integration.pdu.response;

import com.inov8.integration.pdu.BasePDU;

public class AcquirerReversalAdviceResponse extends BasePDU {
	int pduLength = 239;
	private PhoenixResponseHeader header = new PhoenixResponseHeader();

	public AcquirerReversalAdviceResponse() {
		this.fields.addAll(header.getHeaderFields());

	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
	}

}
