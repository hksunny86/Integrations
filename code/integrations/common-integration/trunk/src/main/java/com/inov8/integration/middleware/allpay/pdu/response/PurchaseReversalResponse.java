package com.inov8.integration.middleware.allpay.pdu.response;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "PurchaseReversal")
public class PurchaseReversalResponse implements Serializable {

	private static final long serialVersionUID = -3857068178859135780L;

	private ResponseHeader header;

    public PurchaseReversalResponse() {
        header = new ResponseHeader();
    }

    public ResponseHeader getHeader() {
		return header;
	}

	public void setHeader(ResponseHeader header) {
		this.header = header;
	}
	
	
	
}
