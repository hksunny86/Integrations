package com.inov8.integration.middleware.allpay.pdu.response;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "GetSupplierProducts")
public class GetProductsResponse implements Serializable {

	private static final long serialVersionUID = 8601151494056958652L;

	private ResponseHeader header;
	private List<Product> products;

    public GetProductsResponse(){
        header = new ResponseHeader();
    }

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public ResponseHeader getHeader() {
		return header;
	}

	public void setHeader(ResponseHeader header) {
		this.header = header;
	}

}
