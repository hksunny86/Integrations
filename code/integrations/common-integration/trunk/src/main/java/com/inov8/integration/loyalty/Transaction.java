package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bkr on 8/9/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private String transactionCode;
    private String dateFormatted;
    private String description;
    private String amountFormatted;
    private String loyaltyPoints;
    private String retailerId;
    private String retailerName;
    private String timeFormatted;
    private String processingStatus;


    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public void setAmountFormatted(String amountFormatted) {
        this.amountFormatted = amountFormatted;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

	/**
	 * @return the timeFormatted
	 */
	public String getTimeFormatted() {
		return timeFormatted;
	}

	/**
	 * @param timeFormatted the timeFormatted to set
	 */
	public void setTimeFormatted(String timeFormatted) {
		this.timeFormatted = timeFormatted;
	}

	/**
	 * @return the processingStatus
	 */
	public String getProcessingStatus() {
		return processingStatus;
	}

	/**
	 * @param processingStatus the processingStatus to set
	 */
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
}
