package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.io.Serializable;

public class MfsDebitCardModel implements Serializable,Cloneable{

    private static final long serialVersionUID = -2780818066040920852L;
    private String mobileNo;
    private String cnic;
    private String embossingName;
    private String nadraName;
    private String mailingAddress;
    private String cardStatus;
    private String cardState;
    private Double cardFee;
    private String cardNumber;
    private Long cardStateId;
    private Long cardStatusId;
    private Long cardProductCodeId;
    private Long usecaseId;
    private Long appUserId;
    private String mfsId;
    private String name;
    private Long actionId;
    private String segmentName;
    private String channelName;
    private String cardProductType;
    private String cardTypeCode;
    private String firstName;
    private String lastName;
    private String withAuthFlag;
    private Long segmentId;
    private Long mailingAddressId;
    private String city;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileNo() {return mobileNo;}

    public void setMobileNo(String mobileNo) {this.mobileNo = mobileNo;}

    public String getCnic() {return cnic;}

    public void setCnic(String cnic) {this.cnic = cnic;}

    public String getEmbossingName() {return embossingName;}

    public void setEmbossingName(String embossingName) {this.embossingName = embossingName;}

    public String getNadraName() {return nadraName;}

    public void setNadraName(String nadraName) {this.nadraName = nadraName;}

    public String getMailingAddress() {return mailingAddress;}

    public void setMailingAddress(String mailingAddress) {this.mailingAddress = mailingAddress;}

    public String getCardStatus() {return cardStatus;}

    public void setCardStatus(String cardStatus) {this.cardStatus = cardStatus;}

    public String getCardState() {return cardState;}

    public void setCardState(String cardState) {this.cardState = cardState;}

    public Double getCardFee() {return cardFee;}

    public void setCardFee(Double cardFee) {this.cardFee = cardFee;}

    public String getCardNumber() {return cardNumber;}

    public void setCardNumber(String cardNumber) {this.cardNumber = cardNumber;}

    public Long getCardStateId() {return cardStateId;}

    public void setCardStateId(Long cardStateId) {this.cardStateId = cardStateId;}

    public Long getCardStatusId() {return cardStatusId;}

    public void setCardStatusId(Long cardStatusId) {this.cardStatusId = cardStatusId;}

    public Long getUsecaseId() {
        return usecaseId;
    }

    public void setUsecaseId(Long usecaseId) {
        this.usecaseId = usecaseId;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getMfsId() {
        return mfsId;
    }

    public void setMfsId(String mfsId) {
        this.mfsId = mfsId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final String MFS_DEBIT_CARD_MODEL_KEY = "mfsDebitCardModelKey";

    public String getSegmentName() {return segmentName;}

    public void setSegmentName(String segmentName) {this.segmentName = segmentName;}

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCardProductType() {
        return cardProductType;
    }

    public void setCardProductType(String cardProductType) {
        this.cardProductType = cardProductType;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public Long getMailingAddressId() {
        return mailingAddressId;
    }

    public void setMailingAddressId(Long mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }

    public Long getCardProductCodeId() {return cardProductCodeId;}

    public void setCardProductCodeId(Long cardProductCodeId) {this.cardProductCodeId = cardProductCodeId;}

    public String getWithAuthFlag() {
        return withAuthFlag;
    }

    public void setWithAuthFlag(String withAuthFlag) {
        this.withAuthFlag = withAuthFlag;
    }
}
