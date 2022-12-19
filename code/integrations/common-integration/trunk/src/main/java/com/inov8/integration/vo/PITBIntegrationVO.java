package com.inov8.integration.vo;

import com.inov8.integration.middleware.pdu.Beneficiary;
import com.inov8.integration.middleware.pdu.BeneficiaryRegistration;
import com.inov8.integration.middleware.pdu.PermanentCardBlock;
import com.inov8.integration.middleware.pdu.Transaction;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZeeshanAh1 on 12/7/2015.
 */
public class PITBIntegrationVO implements Serializable {

    private String responseCode;
    private String requestStatus;
    private String message;
    private String fromDate;
    private String toDate;
    private List<PermanentCardBlock> permanentCardBlockList;
    private List<BeneficiaryRegistration> beneficiaryRegistrationList;
    private List<Transaction> transactionList;
    private List<Beneficiary> beneficiaries;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestStatus() { return requestStatus; }

    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getFromDate() { return fromDate; }

    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }

    public void setToDate(String toDate) { this.toDate = toDate; }

    public List<BeneficiaryRegistration> getBeneficiaryRegistrationList() {
        return beneficiaryRegistrationList;
    }

    public void setBeneficiaryRegistrationList(List<BeneficiaryRegistration> beneficiaryRegistrationList) { this.beneficiaryRegistrationList = beneficiaryRegistrationList; }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<PermanentCardBlock> getPermanentCardBlockList() {
        return permanentCardBlockList;
    }

    public void setPermanentCardBlockList(List<PermanentCardBlock> permanentCardBlockList) { this.permanentCardBlockList = permanentCardBlockList; }

    public List<Beneficiary> getBeneficiaries() { return beneficiaries; }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) { this.beneficiaries = beneficiaries; }
}
