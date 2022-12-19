package com.inov8.integration.pdu.response;

import com.inov8.integration.pdu.BasePDU;

public class BillPaymentMarkingAdviceResponse extends BasePDU {
	private PhoenixResponseHeader header = new PhoenixResponseHeader();

	public BillPaymentMarkingAdviceResponse() {
		this.fields.addAll(header.getHeaderFields());
	}

	public PhoenixResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixResponseHeader header) {
		this.header = header;
	}
}
