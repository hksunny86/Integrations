package com.inov8.integration.loyalty;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bkr on 4/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailPointsVO implements Serializable {

    private String transactionDate;
    private String transactionAmount;
    private String pointsEarned;
    private String worthOfPoints;
    private String userMobileNumber;
    private String merchantId;
    private String isTotalRequired;
    private String sessionId;
    private String brandId;
    private String loyaltyConfigDesc;
    private String paymentMethodId;
    private String initiatorAppUserId;
    private String transactionCode;
	private String sharedSession;
	private String redeemPoints;
	private String balance;
	private String remainingPoints;

	public String getSharedSession() {
		return sharedSession;
	}

	public void setSharedSession(String sharedSession) {
		this.sharedSession = sharedSession;
	}

	public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
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

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String isTotalRequired() {
        return isTotalRequired;
    }

    public void setTotalRequired(String totalRequired) {
        isTotalRequired = totalRequired;
    }

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the brandId
	 */
	public String getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the loyaltyConfigDesc
	 */
	public String getLoyaltyConfigDesc() {
		return loyaltyConfigDesc;
	}

	/**
	 * @param loyaltyConfigDesc the loyaltyConfigDesc to set
	 */
	public void setLoyaltyConfigDesc(String loyaltyConfigDesc) {
		this.loyaltyConfigDesc = loyaltyConfigDesc;
	}

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param paymentMethodId the paymentMethodId to set
	 */
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * @return the initiatorAppUserId
	 */
	public String getInitiatorAppUserId() {
		return initiatorAppUserId;
	}

	/**
	 * @param initiatorAppUserId the initiatorAppUserId to set
	 */
	public void setInitiatorAppUserId(String initiatorAppUserId) {
		this.initiatorAppUserId = initiatorAppUserId;
	}

	/**
	 * @return the transactionCode
	 */
	public String getTransactionCode() {
		return transactionCode;
	}

	/**
	 * @param transactionCode the transactionCode to set
	 */
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getRedeemPoints() {
		return redeemPoints;
	}

	public void setRedeemPoints(String redeemPoints) {
		this.redeemPoints = redeemPoints;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRemainingPoints() {
		return remainingPoints;
	}

	public void setRemainingPoints(String remainingPoints) {
		this.remainingPoints = remainingPoints;
	}
}
