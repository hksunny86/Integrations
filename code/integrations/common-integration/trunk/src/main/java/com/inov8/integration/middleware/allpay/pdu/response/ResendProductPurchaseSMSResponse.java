package com.inov8.integration.middleware.allpay.pdu.response;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 10/29/2015.
 */
@XmlType(name = "ResendProductPurchaseSMS")
public class ResendProductPurchaseSMSResponse implements Serializable{

    private ResponseHeader header;

    public ResendProductPurchaseSMSResponse() {
        header = new ResponseHeader();
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }
}
