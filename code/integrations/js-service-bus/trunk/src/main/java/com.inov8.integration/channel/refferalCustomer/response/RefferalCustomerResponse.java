package com.inov8.integration.channel.refferalCustomer.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefferalCustomerResponse extends Response implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("Data")
    public ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        HashMap<String, ArrayList<com.inov8.integration.i8sb.vo.Data>> list = new HashMap<>();
        ArrayList<com.inov8.integration.i8sb.vo.Data> data2 = new ArrayList<>();

        if (this.getData() != null) {
            List<Data> associatedAccountList = this.getData();
            associatedAccountList.forEach(data -> {
                com.inov8.integration.i8sb.vo.Data associatedAccounts = new com.inov8.integration.i8sb.vo.Data();
                associatedAccounts.setSender_mobile(data.getSender_mobile());
                associatedAccounts.setReceiver_mobile(data.getReceiver_mobile());
                associatedAccounts.setReferral_Date(data.getReferral_Date());
                associatedAccounts.setReceiver_Referral_Name(data.getReceiver_Referral_Name());
                associatedAccounts.setReceiver_Status(data.getReceiver_Status());
                associatedAccounts.setReceiver_Tranx_Amount(data.getReceiver_Tranx_Amount());
                associatedAccounts.setDeleted(data.getDeleted());
                associatedAccounts.setCreditPayment_Amount(data.getCreditPayment_Amount());
                associatedAccounts.setReceiver_Debit_Amount(data.getReceiver_Debit_Amount());
                associatedAccounts.setTranx_Type(data.getTranx_Type());
                associatedAccounts.setTranx_Date(data.getTranx_Date());
                associatedAccounts.setTranx_From_Referre_Account(data.getTranx_From_Referre_Account());
                associatedAccounts.setTranx_To_Account_By_Referre(data.getTranx_To_Account_By_Referre());
                associatedAccounts.setReceiver_Signup_Name(data.getReceiver_Signup_Name());
                associatedAccounts.setSignup_Date(data.getSignup_Date());
                data2.add(associatedAccounts);
            });
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("successFull");
            list.put("asa", data2);
            i8SBSwitchControllerResponseVO.setCollectionOfList(list);
        }
        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("Sender_mobile")
    public String sender_mobile;
    @JsonProperty("Receiver_mobile")
    public String receiver_mobile;
    @JsonProperty("Referral_Date")
    public String referral_Date;
    @JsonProperty("Receiver_Referral_Name")
    public String receiver_Referral_Name;
    @JsonProperty("Receiver_Status")
    public String receiver_Status;
    @JsonProperty("Receiver_Tranx_Amount")
    public String receiver_Tranx_Amount;
    @JsonProperty("Deleted")
    public String deleted;
    @JsonProperty("CreditPayment_Amount")
    public String creditPayment_Amount;
    @JsonProperty("Receiver_Debit_Amount")
    public String receiver_Debit_Amount;
    @JsonProperty("Tranx_Type")
    public String tranx_Type;
    @JsonProperty("Tranx_Date")
    public String tranx_Date;
    @JsonProperty("Tranx_From_Referre_Account")
    public String tranx_From_Referre_Account;
    @JsonProperty("Tranx_To_Account_By_Referre")
    public String tranx_To_Account_By_Referre;
    @JsonProperty("Receiver_Signup_Name")
    public String receiver_Signup_Name;
    @JsonProperty("Signup_Date")
    public String signup_Date;

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getReferral_Date() {
        return referral_Date;
    }

    public void setReferral_Date(String referral_Date) {
        this.referral_Date = referral_Date;
    }

    public String getReceiver_Referral_Name() {
        return receiver_Referral_Name;
    }

    public void setReceiver_Referral_Name(String receiver_Referral_Name) {
        this.receiver_Referral_Name = receiver_Referral_Name;
    }

    public String getReceiver_Status() {
        return receiver_Status;
    }

    public void setReceiver_Status(String receiver_Status) {
        this.receiver_Status = receiver_Status;
    }

    public String getReceiver_Tranx_Amount() {
        return receiver_Tranx_Amount;
    }

    public void setReceiver_Tranx_Amount(String receiver_Tranx_Amount) {
        this.receiver_Tranx_Amount = receiver_Tranx_Amount;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCreditPayment_Amount() {
        return creditPayment_Amount;
    }

    public void setCreditPayment_Amount(String creditPayment_Amount) {
        this.creditPayment_Amount = creditPayment_Amount;
    }

    public String getReceiver_Debit_Amount() {
        return receiver_Debit_Amount;
    }

    public void setReceiver_Debit_Amount(String receiver_Debit_Amount) {
        this.receiver_Debit_Amount = receiver_Debit_Amount;
    }

    public String getTranx_Type() {
        return tranx_Type;
    }

    public void setTranx_Type(String tranx_Type) {
        this.tranx_Type = tranx_Type;
    }

    public String getTranx_Date() {
        return tranx_Date;
    }

    public void setTranx_Date(String tranx_Date) {
        this.tranx_Date = tranx_Date;
    }

    public String getTranx_From_Referre_Account() {
        return tranx_From_Referre_Account;
    }

    public void setTranx_From_Referre_Account(String tranx_From_Referre_Account) {
        this.tranx_From_Referre_Account = tranx_From_Referre_Account;
    }

    public String getTranx_To_Account_By_Referre() {
        return tranx_To_Account_By_Referre;
    }

    public void setTranx_To_Account_By_Referre(String tranx_To_Account_By_Referre) {
        this.tranx_To_Account_By_Referre = tranx_To_Account_By_Referre;
    }

    public String getReceiver_Signup_Name() {
        return receiver_Signup_Name;
    }

    public void setReceiver_Signup_Name(String receiver_Signup_Name) {
        this.receiver_Signup_Name = receiver_Signup_Name;
    }

    public String getSignup_Date() {
        return signup_Date;
    }

    public void setSignup_Date(String signup_Date) {
        this.signup_Date = signup_Date;
    }
}