package com.inov8.integration.channel.APIGEE.response.CardDetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.AssociatedAccounts;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AccountNumber"
})
class AssociatedAccount {

    @JsonProperty("AccountNumber")
    private String accountNumber;

    public AssociatedAccount() { }

    public AssociatedAccount(String accountNumber) {
        super();
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() { return accountNumber; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

}
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "MTI",
        "ProcessingCode",
        "TransmissionDatetime",
        "SystemsTraceAuditNumber",
        "TimeLocalTransaction",
        "DateLocalTransaction",
        "MerchantType",
        "ResponseCode",
        "CIF",
        "DebitCardNumber",
        "MaskedCardNumber",
        "CardStatus",
        "CardStatusCode",
        "AssociatedAccounts"
})
public class CardDetailResponse extends Response{

    @JsonProperty("MTI")
    private String MTI;
    @JsonProperty("ProcessingCode")
    private String processingCode;
    @JsonProperty("TransmissionDatetime")
    private String transmissionDatetime;
    @JsonProperty("SystemsTraceAuditNumber")
    private String systemsTraceAuditNumber;
    @JsonProperty("TimeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("DateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("MerchantType")
    private String merchantType;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("CIF")
    private String CIF;
    @JsonProperty("DebitCardNumber")
    private String debitCardNumber;
    @JsonProperty("MaskedCardNumber")
    private String maskedCardNumber;
    @JsonProperty("CardStatus")
    private String cardStatus;
    @JsonProperty("CardStatusCode")
    private String cardStatusCode;
    @JsonProperty("AssociatedAccounts")
    private List<AssociatedAccount> associatedAccounts;


    public CardDetailResponse() { }

    public CardDetailResponse(String mTI, String processingCode, String transmissionDatetime, String systemsTraceAuditNumber, String timeLocalTransaction, String dateLocalTransaction, String merchantType, String responseCode, String cIF, String debitCardNumber, String maskedCardNumber, String cardStatus, String cardStatusCode, List<AssociatedAccount> associatedAccounts) {
        super();
        this.MTI = mTI;
        this.processingCode = processingCode;
        this.transmissionDatetime = transmissionDatetime;
        this.systemsTraceAuditNumber = systemsTraceAuditNumber;
        this.timeLocalTransaction = timeLocalTransaction;
        this.dateLocalTransaction = dateLocalTransaction;
        this.merchantType = merchantType;
        this.responseCode = responseCode;
        this.CIF = cIF;
        this.debitCardNumber = debitCardNumber;
        this.maskedCardNumber = maskedCardNumber;
        this.cardStatus = cardStatus;
        this.cardStatusCode = cardStatusCode;
        this.associatedAccounts=associatedAccounts;
    }

    public String getMTI() { return MTI; }

    public void setMTI(String mTI) { this.MTI = mTI; }

    public String getProcessingCode() { return processingCode; }

    public void setProcessingCode(String processingCode) { this.processingCode = processingCode; }

    public String getTransmissionDatetime() { return transmissionDatetime; }

    public void setTransmissionDatetime(String transmissionDatetime) { this.transmissionDatetime = transmissionDatetime; }

    public String getSystemsTraceAuditNumber() { return systemsTraceAuditNumber; }

    public void setSystemsTraceAuditNumber(String systemsTraceAuditNumber) { this.systemsTraceAuditNumber = systemsTraceAuditNumber; }

    public String getTimeLocalTransaction() { return timeLocalTransaction; }

    public void setTimeLocalTransaction(String timeLocalTransaction) { this.timeLocalTransaction = timeLocalTransaction; }

    public String getDateLocalTransaction() { return dateLocalTransaction; }

    public void setDateLocalTransaction(String dateLocalTransaction) { this.dateLocalTransaction = dateLocalTransaction; }

    public String getMerchantType() { return merchantType; }

    public void setMerchantType(String merchantType) { this.merchantType = merchantType; }

    public String getResponseCode() { return responseCode; }

    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

    public String getCIF() { return CIF; }

    public void setCIF(String cIF) { this.CIF = cIF; }

    public String getDebitCardNumber() { return debitCardNumber; }

    public void setDebitCardNumber(String debitCardNumber) { this.debitCardNumber = debitCardNumber; }

    public String getMaskedCardNumber() { return maskedCardNumber; }

    public void setMaskedCardNumber(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }

    public String getCardStatus() { return cardStatus; }

    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }

    public String getCardStatusCode() { return cardStatusCode; }

    public void setCardStatusCode(String cardStatusCode) { this.cardStatusCode = cardStatusCode; }

    public List<AssociatedAccount> getAssociatedAccounts() { return associatedAccounts; }

    public void setAssociatedAccounts(List<AssociatedAccount> associatedAccounts) { this.associatedAccounts = associatedAccounts; }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        List<AssociatedAccounts> list= new ArrayList<>();

        if (this.getResponseCode().equalsIgnoreCase("00")){
            i8SBSwitchControllerResponseVO.setMTI(this.getMTI());
            i8SBSwitchControllerResponseVO.setProcessingCode(this.getProcessingCode());
            i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.getTransmissionDatetime());
            i8SBSwitchControllerResponseVO.setSystemsTraceAuditNumber(this.getSystemsTraceAuditNumber());
            i8SBSwitchControllerResponseVO.setTimeLocalTransaction(this.getTimeLocalTransaction());
            i8SBSwitchControllerResponseVO.setDateLocalTransaction(this.getDateLocalTransaction());
            i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setCIF(this.getCIF());
            i8SBSwitchControllerResponseVO.setDebitCardNumber(this.getDebitCardNumber());
            i8SBSwitchControllerResponseVO.setCardStatus(this.getCardStatus());
            i8SBSwitchControllerResponseVO.setCardStatusCode(this.getCardStatusCode());
            if (this.getAssociatedAccounts() != null){
                List<AssociatedAccount> associatedAccountList = this.getAssociatedAccounts();
                associatedAccountList.forEach(data -> {
                    AssociatedAccounts associatedAccounts =new AssociatedAccounts();
                    associatedAccounts.setAccountNumber(data.getAccountNumber());
                    list.add(associatedAccounts);
                });
                i8SBSwitchControllerResponseVO.setAssociatedAccounts(list);
            }
        }

        return i8SBSwitchControllerResponseVO;
    }
}