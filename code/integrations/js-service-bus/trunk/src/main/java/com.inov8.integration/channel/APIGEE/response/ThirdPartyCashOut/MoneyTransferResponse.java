package com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.json.JsonArray;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class MoneyTransferResponse extends Response {
    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
    @JsonProperty("data")
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        if(null!=this.getData()) {
            i8SBSwitchControllerResponseVO.setAccountType(this.getData().get(0).getTransactionType());
            i8SBSwitchControllerResponseVO.setTransactionFees(this.getData().get(0).getFeeAmount());
            i8SBSwitchControllerResponseVO.setAvailableLimit(this.getData().get(0).getAvailableAmount());
            i8SBSwitchControllerResponseVO.setSessionId(this.getData().get(0).getTransactionRefrence());
            i8SBSwitchControllerResponseVO.setMobilePhone(this.getData().get(0).getMobileNumber());
            i8SBSwitchControllerResponseVO.setSTAN(this.getData().get(0).getStan());
            i8SBSwitchControllerResponseVO.setCustomerAccNumber(this.getData().get(0).getKeyAccRef());
            i8SBSwitchControllerResponseVO.setWalletId(this.getData().get(0).getWalletId());
            i8SBSwitchControllerResponseVO.setAmountPaid(this.getData().get(0).getPrincipalAmount());
            i8SBSwitchControllerResponseVO.setTaxpaid(this.getData().get(0).getTaxAmount());
            i8SBSwitchControllerResponseVO.setAccountTitle(this.getData().get(0).getUserName());
        }
        return i8SBSwitchControllerResponseVO;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        @JsonProperty("transactionType")
        private String transactionType;
        @JsonProperty("feeAmount")
        private String feeAmount;
        @JsonProperty("walletId")
        private String walletId;
        @JsonProperty("availableAmount")
        private String availableAmount;
        @JsonProperty("transactionReference")
        private String transactionRefrence;
        @JsonProperty("mobileNumber")
        private String mobileNumber;
        @JsonProperty("STAN")
        private String stan;
        @JsonProperty("KEY_ACC_REF")
        private String keyAccRef;
        @JsonProperty("taxAmount")
        private String taxAmount;
        @JsonProperty("principalAmount")
        private String principalAmount;
        @JsonProperty("username")
        private String userName;


        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getFeeAmount() {
            return feeAmount;
        }

        public void setFeeAmount(String feeAmount) {
            this.feeAmount = feeAmount;
        }

        public String getWalletId() {
            return walletId;
        }

        public void setWalletId(String walletId) {
            this.walletId = walletId;
        }

        public String getAvailableAmount() {
            return availableAmount;
        }

        public void setAvailableAmount(String availableAmount) {
            this.availableAmount = availableAmount;
        }

        public String getTransactionRefrence() {
            return transactionRefrence;
        }

        public void setTransactionRefrence(String transactionRefrence) {
            this.transactionRefrence = transactionRefrence;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getStan() {
            return stan;
        }

        public void setStan(String stan) {
            this.stan = stan;
        }

        public String getKeyAccRef() {
            return keyAccRef;
        }

        public void setKeyAccRef(String keyAccRef) {
            this.keyAccRef = keyAccRef;
        }

        public String getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(String taxAmount) {
            this.taxAmount = taxAmount;
        }

        public String getPrincipalAmount() {
            return principalAmount;
        }

        public void setPrincipalAmount(String principalAmount) {
            this.principalAmount = principalAmount;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
