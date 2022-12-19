package com.inov8.integration.channel.merchantDiscountCamping.request;

import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.DateFormatEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;

import java.util.Date;

public class TransactionUpdateRequest extends Request {

    private String appUserId;
    private String balanceAfterTransaction;
    private String microTransactionCode;
    private String password;
    private String reversalDate;
    private String rrn;
    private String stan;
    private String transactionDate;
    private String transactionStatus;
    private String username;
    private String transactionAmount;
    private String transactionType;

    private String merchantUserName = PropertyReader.getProperty("merchant.camping.userName");
    private String mechantCampingPassword = PropertyReader.getProperty("merchant.camping.password");

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setAppUserId(i8SBSwitchControllerRequestVO.getUserId());
        this.setBalanceAfterTransaction(i8SBSwitchControllerRequestVO.getAvailableBalance());
        this.setMicroTransactionCode(i8SBSwitchControllerRequestVO.getTransactionCode());
        this.setPassword(mechantCampingPassword);
        this.setReversalDate(DateUtil.formatCurrentDate(DateFormatEnum.EXPIRY_DATE.getValue()));
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setTransactionDate(DateUtil.formatCurrentDate(DateFormatEnum.EXPIRY_DATE.getValue()));
        this.setTransactionStatus(i8SBSwitchControllerRequestVO.getStatus());
        this.setTransactionType(i8SBSwitchControllerRequestVO.getTransactionType());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setUsername(merchantUserName);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(String balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getMicroTransactionCode() {
        return microTransactionCode;
    }

    public void setMicroTransactionCode(String microTransactionCode) {
        this.microTransactionCode = microTransactionCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReversalDate() {
        return reversalDate;
    }

    public void setReversalDate(String reversalDate) {
        this.reversalDate = reversalDate;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
