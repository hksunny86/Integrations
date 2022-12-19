package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 10/29/2015.
 */
public class ResendProductPurchaseSMSRequest implements Serializable {

    private RequestHeader header;
    private String transactionId;


    public RequestHeader getHeader() {
        return header;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
