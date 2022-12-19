package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;

public class GetProductsRequest implements Serializable {

	private static final long serialVersionUID = 8198772026041342061L;
	
	private RequestHeader header;

	public RequestHeader getHeader() {
		return header;
	}

	public void setHeader(RequestHeader header) {
		this.header = header;
	}

}
