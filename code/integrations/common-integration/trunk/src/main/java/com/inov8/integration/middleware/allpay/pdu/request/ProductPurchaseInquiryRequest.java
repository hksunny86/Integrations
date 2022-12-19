package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 10/29/2015.
 */
public class ProductPurchaseInquiryRequest implements Serializable{

    private RequestHeader header;
    private String productId;
    private String denominationCode;
    private String value;
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
