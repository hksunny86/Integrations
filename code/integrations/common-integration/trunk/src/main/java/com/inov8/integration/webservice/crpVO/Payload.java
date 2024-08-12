package com.inov8.integration.webservice.crpVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String customerType;
    private String customerOccupation;
    private String isBeneficialOwner;
    private String customerSourceOfFunds;
    private String purposeOfAccount;
    private String internalRiskAssessmentFound;
    private String isDirector;
    private String otherNationalities;
    private String customerCountryID;
    private String productAndServices;
    private String channel;
    private String tradingCountries;
    private String customerPermanentAddress;
    private String customerMailingAddress;
    private String customerExpectedAggregateDebitPerMonth;
    private String customerExpectedAggregateCreditPerMonth;
    private String customerCreditCountPerMonth;
    private String customerDebitCountPerMonth;
    private String score;
    private String rating;
    private String crpDate;
    private String nextCrpDate;
    private String lastKycDate;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getIsBeneficialOwner() {
        return isBeneficialOwner;
    }

    public void setIsBeneficialOwner(String isBeneficialOwner) {
        this.isBeneficialOwner = isBeneficialOwner;
    }

    public String getCustomerSourceOfFunds() {
        return customerSourceOfFunds;
    }

    public void setCustomerSourceOfFunds(String customerSourceOfFunds) {
        this.customerSourceOfFunds = customerSourceOfFunds;
    }

    public String getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    public String getInternalRiskAssessmentFound() {
        return internalRiskAssessmentFound;
    }

    public void setInternalRiskAssessmentFound(String internalRiskAssessmentFound) {
        this.internalRiskAssessmentFound = internalRiskAssessmentFound;
    }

    public String getIsDirector() {
        return isDirector;
    }

    public void setIsDirector(String isDirector) {
        this.isDirector = isDirector;
    }

    public String getOtherNationalities() {
        return otherNationalities;
    }

    public void setOtherNationalities(String otherNationalities) {
        this.otherNationalities = otherNationalities;
    }

    public String getCustomerCountryID() {
        return customerCountryID;
    }

    public void setCustomerCountryID(String customerCountryID) {
        this.customerCountryID = customerCountryID;
    }

    public String getProductAndServices() {
        return productAndServices;
    }

    public void setProductAndServices(String productAndServices) {
        this.productAndServices = productAndServices;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTradingCountries() {
        return tradingCountries;
    }

    public void setTradingCountries(String tradingCountries) {
        this.tradingCountries = tradingCountries;
    }

    public String getCustomerPermanentAddress() {
        return customerPermanentAddress;
    }

    public void setCustomerPermanentAddress(String customerPermanentAddress) {
        this.customerPermanentAddress = customerPermanentAddress;
    }

    public String getCustomerMailingAddress() {
        return customerMailingAddress;
    }

    public void setCustomerMailingAddress(String customerMailingAddress) {
        this.customerMailingAddress = customerMailingAddress;
    }

    public String getCustomerExpectedAggregateDebitPerMonth() {
        return customerExpectedAggregateDebitPerMonth;
    }

    public void setCustomerExpectedAggregateDebitPerMonth(String customerExpectedAggregateDebitPerMonth) {
        this.customerExpectedAggregateDebitPerMonth = customerExpectedAggregateDebitPerMonth;
    }

    public String getCustomerExpectedAggregateCreditPerMonth() {
        return customerExpectedAggregateCreditPerMonth;
    }

    public void setCustomerExpectedAggregateCreditPerMonth(String customerExpectedAggregateCreditPerMonth) {
        this.customerExpectedAggregateCreditPerMonth = customerExpectedAggregateCreditPerMonth;
    }

    public String getCustomerCreditCountPerMonth() {
        return customerCreditCountPerMonth;
    }

    public void setCustomerCreditCountPerMonth(String customerCreditCountPerMonth) {
        this.customerCreditCountPerMonth = customerCreditCountPerMonth;
    }

    public String getCustomerDebitCountPerMonth() {
        return customerDebitCountPerMonth;
    }

    public void setCustomerDebitCountPerMonth(String customerDebitCountPerMonth) {
        this.customerDebitCountPerMonth = customerDebitCountPerMonth;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCrpDate() {
        return crpDate;
    }

    public void setCrpDate(String crpDate) {
        this.crpDate = crpDate;
    }

    public String getNextCrpDate() {
        return nextCrpDate;
    }

    public void setNextCrpDate(String nextCrpDate) {
        this.nextCrpDate = nextCrpDate;
    }

    public String getLastKycDate() {
        return lastKycDate;
    }

    public void setLastKycDate(String lastKycDate) {
        this.lastKycDate = lastKycDate;
    }
}
