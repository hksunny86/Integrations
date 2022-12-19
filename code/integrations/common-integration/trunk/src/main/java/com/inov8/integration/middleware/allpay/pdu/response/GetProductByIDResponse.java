package com.inov8.integration.middleware.allpay.pdu.response;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetProductByID")
public class GetProductByIDResponse implements Serializable {

	private static final long serialVersionUID = -1570395637549625476L;

	private ResponseHeader header;
	private Product product;

    public GetProductByIDResponse() {
        header = new ResponseHeader();
    }

    public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ResponseHeader getHeader() {
		return header;
	}

	public void setHeader(ResponseHeader header) {
		this.header = header;
	}

}
