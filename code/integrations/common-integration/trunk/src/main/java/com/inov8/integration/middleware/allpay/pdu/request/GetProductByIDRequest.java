package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;

public class GetProductByIDRequest implements Serializable {

	private static final long serialVersionUID = 7093910730491115231L;

	private RequestHeader header;
	private String productId;

	public RequestHeader getHeader() {
		return header;
	}

	public void setHeader(RequestHeader header) {
		this.header = header;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
