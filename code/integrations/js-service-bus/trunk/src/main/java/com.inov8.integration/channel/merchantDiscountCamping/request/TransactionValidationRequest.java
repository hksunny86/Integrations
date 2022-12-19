package com.inov8.integration.channel.merchantDiscountCamping.request;

import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class TransactionValidationRequest extends Request{

    private String appUserId;
    private String cardNo;
    private String cnic;
    private String merchantName;
    private String mobileNo;
    private String password;
    private String rrn;
    private String segmentId;
    private String stan;
    private String transactionAmount;
    private String transactionName;
    private String transactionType;
    private String username;

    private String merchantUserName = PropertyReader.getProperty("merchant.camping.userName");
    private String mechantCampingPassword = PropertyReader.getProperty("merchant.camping.password");


    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setAppUserId(i8SBSwitchControllerRequestVO.getUserId());
        this.setCardNo(i8SBSwitchControllerRequestVO.getCardNumber());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setMerchantName(i8SBSwitchControllerRequestVO.getMerchantName());
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setPassword(mechantCampingPassword);
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setSegmentId(i8SBSwitchControllerRequestVO.getSegmentId());
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setTransactionName(i8SBSwitchControllerRequestVO.getTransactionCodeDesc());
        this.setTransactionType(i8SBSwitchControllerRequestVO.getTransactionType());
        this.setUsername(merchantUserName);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
