package com.inov8.integration.vo;

import java.io.Serializable;

public class CardType implements Serializable {
    private String cardProductTypeId;
    private String cardName;


    public String getCardProductTypeId() {
        return cardProductTypeId;
    }

    public void setCardProductTypeId(String cardProductTypeId) {
        this.cardProductTypeId = cardProductTypeId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
