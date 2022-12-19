package com.inov8.integration.channel.vision.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.enums.I8SBKeysOfCollectionEnum;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.i8sb.vo.CardDetailVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "MTI",
        "ProcessingCode",
        "TransmissionDatetime",
        "SystemsTraceAuditNumber",
        "TimeLocalTransaction",
        "DateLocalTransaction",
        "MerchantType",
        "CNIC",
        "CardDetails",
        "ResponseCode",
        "ResponseDescription"
})
public class VisionDebitCardResponse extends Response implements Serializable {

    @JsonProperty("MTI")
    private String mti;
    @JsonProperty("ProcessingCode")
    private String processingCode;
    @JsonProperty("TransmissionDatetime")
    private String transmissionDateTime;
    @JsonProperty("SystemsTraceAuditNumber")
    private String stan;
    @JsonProperty("TimeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("DateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("MerchantType")
    private String merchantType;
    @JsonProperty("CNIC")
    private String cnic;
    @JsonProperty("CardDetails")
    private List<CardDetail> cardDetails;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;


    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public List<CardDetail> getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(List<CardDetail> cardDetails) {
        this.cardDetails = cardDetails;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equalsIgnoreCase("00")){

            i8SBSwitchControllerResponseVO.setMTI(this.getMti());
            i8SBSwitchControllerResponseVO.setProcessingCode(this.getProcessingCode());
            i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.getTransmissionDateTime());
            i8SBSwitchControllerResponseVO.setTimeLocalTransaction(this.getTimeLocalTransaction());
            i8SBSwitchControllerResponseVO.setDateLocalTransaction(this.getDateLocalTransaction());
            i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
            i8SBSwitchControllerResponseVO.setCNIC(this.getCnic());

            if (this.getCardDetails()!=null){
            List<CardDetailVO> cardDetailVO = new ArrayList<>();
            List<CardDetail> cardDetailList = this.getCardDetails();
            cardDetailList.forEach( cardDetail  -> {

                CardDetailVO cardDetailVO1 = new CardDetailVO();

                cardDetailVO1.setCardType(cardDetail.getCardType());
                cardDetailVO1.setCardNo(cardDetail.getCardNo());
                cardDetailVO1.setCardTitle(cardDetail.getCardTitle());
                cardDetailVO1.setAccountNumber(cardDetail.getAccountNumber());
                cardDetailVO1.setCif(cardDetail.getCif());
                cardDetailVO1.setCardStatus(cardDetail.getCardStatus());
                cardDetailVO1.setCreationDate(cardDetail.getCreationDate());
                cardDetailVO.add(cardDetailVO1);

            });
            i8SBSwitchControllerResponseVO.setCardDetailList(cardDetailVO);
            }
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        }
        return i8SBSwitchControllerResponseVO;
    }
}
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CardType",
        "CardNo",
        "CardTitle",
        "AccountNumber",
        "CIF",
        "CardStatus",
        "CreationDate"
})
class CardDetail{
    @JsonProperty("CardType")
    private String cardType;
    @JsonProperty("CardNo")
    private String cardNo;
    @JsonProperty("CardTitle")
    private String cardTitle;
    @JsonProperty("AccountNumber")
    private String accountNumber;
    @JsonProperty("CIF")
    private String cif;
    @JsonProperty("CardStatus")
    private String cardStatus;
    @JsonProperty("CreationDate")
    private String creationDate;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}