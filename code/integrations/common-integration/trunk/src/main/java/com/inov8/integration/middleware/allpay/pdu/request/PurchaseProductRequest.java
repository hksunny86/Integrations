package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;

public class PurchaseProductRequest implements Serializable {

	private static final long serialVersionUID = 3561308515323587831L;

	private RequestHeader header;
	private String productId;
	private String denominationCode;
	private String quantity;

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

	public String getDenominationCode() {
		return denominationCode;
	}

	public void setDenominationCode(String denominationCode) {
		this.denominationCode = denominationCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
