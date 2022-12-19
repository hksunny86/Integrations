package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bkr on 4/18/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPointsVO {

    private String isRedeemAllowed;
    private String isTopupAllowed;
    private String isAllMerchantRequired;
    private String merchantId;
    private String merchantName;
    private String spendRupees;
    private String getPoints;
    private String amountSpent;
    private String pointsEarned;
    private String worthOfPoints;
    private String transactionDate;
    private String cardBalanceExpiry;
    private String merchantColor;
    private String merchantLogo;
    private String merchantLayout;
    private String userMobileNumber;


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(String amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(String pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public String getWorthOfPoints() {
        return worthOfPoints;
    }

    public void setWorthOfPoints(String worthOfPoints) {
        this.worthOfPoints = worthOfPoints;
    }

    public String gettransactionDate() {
        return transactionDate;
    }

    public void settransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCardBalanceExpiry() {
        return cardBalanceExpiry;
    }

    public void setCardBalanceExpiry(String cardBalanceExpiry) {
        this.cardBalanceExpiry = cardBalanceExpiry;
    }

    public String isRedeemAllowed() {
        return isRedeemAllowed;
    }

    public void setRedeemAllowed(String redeemAllowed) {
        isRedeemAllowed = redeemAllowed;
    }

    public String isTopupAllowed() {
        return isTopupAllowed;
    }

    public void setTopupAllowed(String topupAllowed) {
        isTopupAllowed = topupAllowed;
    }

    public String getMerchantColor() {
        return merchantColor;
    }

    public void setMerchantColor(String merchantColor) {
        this.merchantColor = merchantColor;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getMerchantLayout() {
        return merchantLayout;
    }

    public void setMerchantLayout(String merchantLayout) {
        this.merchantLayout = merchantLayout;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }


    public String getSpendRupees() {
        return spendRupees;
    }

    public void setSpendRupees(String spendRupees) {
        this.spendRupees = spendRupees;
    }

    public String getGetPoints() {
        return getPoints;
    }

    public void setGetPoints(String getPoints) {
        this.getPoints = getPoints;
    }

    public String isAllMerchantRequired() {
        return isAllMerchantRequired;
    }

    public void setAllMerchantRequired(String allMerchantRequired) {
        isAllMerchantRequired = allMerchantRequired;
    }
}
