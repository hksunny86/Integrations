package com.inov8.microbank.server.service.switchmodule.iris.model;

import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 12/15/2015.
 */
public class CardVO implements Serializable {
    private static final long serialVersionUID = 6106246076421396342L;

    private String cardNo;
    private String cardExpiry;
    private String cardType;
    private String cardStatus;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}