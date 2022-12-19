package com.inov8.integration.middleware.allpay.pdu.response;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 10/29/2015.
 */
@XmlType(name = "ProductPurchaseInquiry")
public class ProductPurchaseInquiryResponse implements Serializable{

    private ResponseHeader header;

    private String productId;
    private String denominationCode;
    private String value;
    private String quantity;

    public ProductPurchaseInquiryResponse() {
        header = new ResponseHeader();
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
