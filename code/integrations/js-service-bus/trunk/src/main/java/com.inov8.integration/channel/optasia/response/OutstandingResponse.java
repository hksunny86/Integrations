package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.optasiaVO.ChargeAdjustments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityValue",
        "identityType",
        "origSource",
        "receivedTimestamp",
        "outstandingPerCurrency",
        "thirdPartyData"
})
public class OutstandingResponse extends Response implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(OutstandingResponse.class.getSimpleName());

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("outstandingPerCurrency")
    private List<OutstandingPerCurrency> outstandingPerCurrency;
    @JsonProperty("thirdPartyData")
    private List<ThirdPartyData> thirdPartyData;
    private String responseCode;
    private String responseDescription;
    private Map<String, List<?>> collectionOfList = new HashMap();
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;

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

    @JsonProperty("identityValue")
    public String getIdentityValue() {
        return identityValue;
    }

    @JsonProperty("identityValue")
    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    @JsonProperty("identityType")
    public String getIdentityType() {
        return identityType;
    }

    @JsonProperty("identityType")
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    @JsonProperty("origSource")
    public String getOrigSource() {
        return origSource;
    }

    @JsonProperty("origSource")
    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    @JsonProperty("receivedTimestamp")
    public String getReceivedTimestamp() {
        return receivedTimestamp;
    }

    @JsonProperty("receivedTimestamp")
    public void setReceivedTimestamp(String receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    @JsonProperty("outstandingPerCurrency")
    public List<OutstandingPerCurrency> getOutstandingPerCurrency() {
        return outstandingPerCurrency;
    }

    @JsonProperty("outstandingPerCurrency")
    public void setOutstandingPerCurrency(List<OutstandingPerCurrency> outstandingPerCurrency) {
        this.outstandingPerCurrency = outstandingPerCurrency;
    }

    @JsonProperty("thirdPartyData")
    public List<ThirdPartyData> getThirdPartyData() {
        return thirdPartyData;
    }

    @JsonProperty("thirdPartyData")
    public void setThirdPartyData(List<ThirdPartyData> thirdPartyData) {
        this.thirdPartyData = thirdPartyData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {


        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        } else {
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
            i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
        }
        i8SBSwitchControllerResponseVO.setCode(this.getCode());
        i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());
        if (outstandingPerCurrency != null) {
            com.inov8.integration.webservice.optasiaVO.OutstandingPerCurrency outstandingPerCurrency;
            List<com.inov8.integration.webservice.optasiaVO.OutstandingPerCurrency> outstandingPerCurrencyList = new ArrayList<>();

            com.inov8.integration.webservice.optasiaVO.Charge charge;
            List<com.inov8.integration.webservice.optasiaVO.Charge> chargeList = new ArrayList<>();

            List<Charge> chargeList1 = new ArrayList<>();

            for (int i = 0; i < this.getOutstandingPerCurrency().size(); i++) {

                outstandingPerCurrency = new com.inov8.integration.webservice.optasiaVO.OutstandingPerCurrency();

                charge = new com.inov8.integration.webservice.optasiaVO.Charge();

                outstandingPerCurrency.setCurrencyCode(this.getOutstandingPerCurrency().get(i).getCurrencyCode());
                outstandingPerCurrency.setAvailableCreditLimit(this.getOutstandingPerCurrency().get(i).getAvailableCreditLimit());
                outstandingPerCurrency.setDynamicCreditLimit(this.getOutstandingPerCurrency().get(i).getDynamicCreditLimit());
                outstandingPerCurrency.setNumOutstandingLoans(this.getOutstandingPerCurrency().get(i).getNumOutstandingLoans());
                outstandingPerCurrency.setTotalGross(this.getOutstandingPerCurrency().get(i).getTotalGross());
                outstandingPerCurrency.setTotalPrincipal(this.getOutstandingPerCurrency().get(i).getTotalPrincipal());
                outstandingPerCurrency.setTotalSetupFees(this.getOutstandingPerCurrency().get(i).getTotalSetupFees());
                outstandingPerCurrency.setTotalInterest(this.getOutstandingPerCurrency().get(i).getTotalInterest());
                outstandingPerCurrency.setTotalInterestVAT(this.getOutstandingPerCurrency().get(i).getTotalInterestVAT());
                outstandingPerCurrency.setTotalCharges(this.getOutstandingPerCurrency().get(i).getTotalCharges());
                outstandingPerCurrency.setTotalChargesVAT(this.getOutstandingPerCurrency().get(i).getTotalChargesVAT());
                chargeList1 = this.getOutstandingPerCurrency().get(i).getCharges();
                if (chargeList1 != null) {
                    for (Charge value : chargeList1) {
                        charge = new com.inov8.integration.webservice.optasiaVO.Charge();
                        charge.setCharge(value.getCharge());
                        charge.setChargeName(value.getChargeName());
                        charge.setChargeVAT(value.getChargeVAT());
                        chargeList.add(charge);
                    }
                }

                outstandingPerCurrency.setCharges(chargeList);
                outstandingPerCurrency.setTotalPendingLoans(this.getOutstandingPerCurrency().get(i).getTotalPendingLoans());
                outstandingPerCurrency.setTotalPendingRecoveries(this.getOutstandingPerCurrency().get(i).getTotalPendingRecoveries());
                outstandingPerCurrencyList.add(outstandingPerCurrency);
                collectionOfList.put("OutstandingPerCurrency", outstandingPerCurrencyList);
                i8SBSwitchControllerResponseVO.setTotalGross(this.getOutstandingPerCurrency().get(0).getTotalGross());
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
            }
        }

        if (thirdPartyData != null) {
            com.inov8.integration.webservice.optasiaVO.Account account;
            List<com.inov8.integration.webservice.optasiaVO.Account> accountList = new ArrayList<>();

            com.inov8.integration.webservice.optasiaVO.ThirdPartyData thirdPartyData;
            List<com.inov8.integration.webservice.optasiaVO.ThirdPartyData> thirdPartyDataList = new ArrayList<>();

            List<Account> accountList1;
            List<ThirdPartyData> thirdPartyDataList1 = this.getThirdPartyData();

            for (int i = 0; i < thirdPartyDataList1.size(); i++) {

                thirdPartyData = new com.inov8.integration.webservice.optasiaVO.ThirdPartyData();

                thirdPartyData.setActivationDate(thirdPartyDataList1.get(i).getActivationDate());
                thirdPartyData.setExpirationDate(thirdPartyDataList1.get(i).getExpirationDate());
                thirdPartyData.setStatus(thirdPartyDataList1.get(i).getStatus());
                accountList1 = this.getThirdPartyData().get(i).getAccounts();

                if (accountList1 != null) {
                    for (Account value : accountList1) {
                        account = new com.inov8.integration.webservice.optasiaVO.Account();
                        account.setAccountType(value.getAccountType());
                        account.setAccountID(value.getAccountID());
                        account.setAccountBalance(value.getAccountBalance());
                        account.setAccountCurrencyCode(value.getAccountCurrencyCode());
                        accountList.add(account);
                    }
                }
                thirdPartyData.setAccounts(accountList);
                thirdPartyDataList.add(thirdPartyData);
                collectionOfList.put("thirdPartyData", thirdPartyDataList);

            }
        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyCode",
        "availableCreditLimit",
        "dynamicCreditLimit",
        "numOutstandingLoans",
        "totalGross",
        "totalPrincipal",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "charges",
        "totalPendingLoans",
        "totalPendingRecoveries"
})
class OutstandingPerCurrency implements Serializable {

    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("availableCreditLimit")
    private Float availableCreditLimit;
    @JsonProperty("dynamicCreditLimit")
    private Float dynamicCreditLimit;
    @JsonProperty("numOutstandingLoans")
    private String numOutstandingLoans;
    @JsonProperty("totalGross")
    private String totalGross;
    @JsonProperty("totalPrincipal")
    private String totalPrincipal;
    @JsonProperty("totalSetupFees")
    private String totalSetupFees;
    @JsonProperty("totalInterest")
    private String totalInterest;
    @JsonProperty("totalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("totalCharges")
    private String totalCharges;
    @JsonProperty("totalChargesVAT")
    private String totalChargesVAT;
    @JsonProperty("charges")
    private List<Charge> charges;
    @JsonProperty("totalPendingLoans")
    private String totalPendingLoans;
    @JsonProperty("totalPendingRecoveries")
    private String totalPendingRecoveries;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("availableCreditLimit")
    public Float getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    @JsonProperty("availableCreditLimit")
    public void setAvailableCreditLimit(Float availableCreditLimit) {
        this.availableCreditLimit = availableCreditLimit;
    }

    @JsonProperty("dynamicCreditLimit")
    public Float getDynamicCreditLimit() {
        return dynamicCreditLimit;
    }

    @JsonProperty("dynamicCreditLimit")
    public void setDynamicCreditLimit(Float dynamicCreditLimit) {
        this.dynamicCreditLimit = dynamicCreditLimit;
    }


    @JsonProperty("numOutstandingLoans")
    public String getNumOutstandingLoans() {
        return numOutstandingLoans;
    }

    @JsonProperty("numOutstandingLoans")
    public void setNumOutstandingLoans(String numOutstandingLoans) {
        this.numOutstandingLoans = numOutstandingLoans;
    }

    @JsonProperty("totalGross")
    public String getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalPrincipal")
    public String getTotalPrincipal() {
        return totalPrincipal;
    }

    @JsonProperty("totalPrincipal")
    public void setTotalPrincipal(String totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    @JsonProperty("totalSetupFees")
    public String getTotalSetupFees() {
        return totalSetupFees;
    }

    @JsonProperty("totalSetupFees")
    public void setTotalSetupFees(String totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    @JsonProperty("totalInterest")
    public String getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public String getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(String totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public String getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public String getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(String totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    @JsonProperty("charges")
    public List<Charge> getCharges() {
        return charges;
    }

    @JsonProperty("charges")
    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    @JsonProperty("totalPendingLoans")
    public String getTotalPendingLoans() {
        return totalPendingLoans;
    }

    @JsonProperty("totalPendingLoans")
    public void setTotalPendingLoans(String totalPendingLoans) {
        this.totalPendingLoans = totalPendingLoans;
    }

    @JsonProperty("totalPendingRecoveries")
    public String getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    @JsonProperty("totalPendingRecoveries")
    public void setTotalPendingRecoveries(String totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "charge",
        "chargeVAT"
})
class Charge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("charge")
    private Float charge;
    @JsonProperty("chargeVAT")
    private Float chargeVAT;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("charge")
    public Float getCharge() {
        return charge;
    }

    @JsonProperty("charge")
    public void setCharge(Float charge) {
        this.charge = charge;
    }

    @JsonProperty("chargeVAT")
    public Float getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(Float chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accountType",
        "accountID",
        "accountBalance",
        "accountCurrencyCode"
})
class Account {

    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("accountID")
    private String accountID;
    @JsonProperty("accountBalance")
    private String accountBalance;
    @JsonProperty("accountCurrencyCode")
    private String accountCurrencyCode;

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("accountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("accountID")
    public String getAccountID() {
        return accountID;
    }

    @JsonProperty("accountID")
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    @JsonProperty("accountBalance")
    public String getAccountBalance() {
        return accountBalance;
    }

    @JsonProperty("accountBalance")
    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    @JsonProperty("accountCurrencyCode")
    public String getAccountCurrencyCode() {
        return accountCurrencyCode;
    }

    @JsonProperty("accountCurrencyCode")
    public void setAccountCurrencyCode(String accountCurrencyCode) {
        this.accountCurrencyCode = accountCurrencyCode;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "activationDate",
        "expirationDate",
        "status",
        "accounts"
})
class ThirdPartyData {

    @JsonProperty("activationDate")
    private String activationDate;
    @JsonProperty("expirationDate")
    private String expirationDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("accounts")
    private List<Account> accounts;

    @JsonProperty("activationDate")
    public String getActivationDate() {
        return activationDate;
    }

    @JsonProperty("activationDate")
    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    @JsonProperty("expirationDate")
    public String getExpirationDate() {
        return expirationDate;
    }

    @JsonProperty("expirationDate")
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("accounts")
    public List<Account> getAccounts() {
        return accounts;
    }

    @JsonProperty("accounts")
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}