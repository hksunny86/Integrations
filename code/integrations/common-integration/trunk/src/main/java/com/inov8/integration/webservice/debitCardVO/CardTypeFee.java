package com.inov8.integration.webservice.debitCardVO;

import java.io.Serializable;

public class CardTypeFee implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    private String cardTypeId;
    private String cardName;
    private String cardFee;

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardFee() {
        return cardFee;
    }

    public void setCardFee(String cardFee) {
        this.cardFee = cardFee;
    }
}
