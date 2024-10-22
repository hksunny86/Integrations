package com.inov8.integration.channel.refferalCustomer.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class DynamicReffrelRequest extends Request {

    private String msisdn;
    private String productId;
    private  String referralCode;
    private String campaignCode;
    private String transactionId;
    private String transactionAmount;
    private String transactionDate;


    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMsisdn(i8SBSwitchControllerRequestVO.getMsisdn());
        this.setProductId(i8SBSwitchControllerRequestVO.getProductCode());
        this.setCampaignCode(i8SBSwitchControllerRequestVO.getOrderRefId());
        this.setReferralCode(i8SBSwitchControllerRequestVO.getIdCode());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setTransactionDate(i8SBSwitchControllerRequestVO.getTransactionDate());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
