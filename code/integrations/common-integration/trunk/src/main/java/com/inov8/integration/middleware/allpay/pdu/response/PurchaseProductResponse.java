package com.inov8.integration.middleware.allpay.pdu.response;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@XmlType(name = "PurchaseProduct")
public class PurchaseProductResponse implements Serializable {

	private static final long serialVersionUID = -1678838128951029442L;

	private ResponseHeader header;

	private String productName;
	private String serialNo;
	private String pin;
	private String charges;
	private Date expiryDate;

    public PurchaseProductResponse() {
        header = new ResponseHeader();
    }

    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public ResponseHeader getHeader() {
		return header;
	}

	public void setHeader(ResponseHeader header) {
		this.header = header;
	}

}
