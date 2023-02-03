package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("CreditCard")
public class CreditCard implements Serializable {

    private final static long serialVersionUID = 1L;

    private String cardNumber;
    private String title;
    private String cardType;
    private String parentCardNumber;
    private String parentCardTitle;
    private String cardStatus;
    private String cardIssueDate;
    private String cardExpiryDate;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getParentCardNumber() {
        return parentCardNumber;
    }

    public void setParentCardNumber(String parentCardNumber) {
        this.parentCardNumber = parentCardNumber;
    }

    public String getParentCardTitle() {
        return parentCardTitle;
    }

    public void setParentCardTitle(String parentCardTitle) {
        this.parentCardTitle = parentCardTitle;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardIssueDate() {
        return cardIssueDate;
    }

    public void setCardIssueDate(String cardIssueDate) {
        this.cardIssueDate = cardIssueDate;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }
}