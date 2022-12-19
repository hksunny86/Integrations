package com.inov8.microbank.server.service.switchmodule.iris.model;

import java.io.Serializable;

public class CreditCardAccount implements Serializable
{
	private static final long	serialVersionUID	= -4988803568095282472L;

	private String				cardNumber;
	private String				cardExpiry;
	private String				reserved1;
	private String				closingBalance;
	private String				dueDate;
	private String				minPaymentDue;
	private String				currentBalance;
	private String				rewardPointsEarned;
	private String				reserved2;
	private String				availableCreditLimit;
	private String				customerName;
	private String				reserved3;

	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public String getCardExpiry()
	{
		return cardExpiry;
	}

	public void setCardExpiry(String cardExpiry)
	{
		this.cardExpiry = cardExpiry;
	}

	public String getReserved1()
	{
		return reserved1;
	}

	public void setReserved1(String reserved1)
	{
		this.reserved1 = reserved1;
	}

	public String getClosingBalance()
	{
		return closingBalance;
	}

	public void setClosingBalance(String closingBalance)
	{
		this.closingBalance = closingBalance;
	}

	public String getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(String dueDate)
	{
		this.dueDate = dueDate;
	}

	public String getMinPaymentDue()
	{
		return minPaymentDue;
	}

	public void setMinPaymentDue(String minPaymentDue)
	{
		this.minPaymentDue = minPaymentDue;
	}

	public String getCurrentBalance()
	{
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance)
	{
		this.currentBalance = currentBalance;
	}

	public String getRewardPointsEarned()
	{
		return rewardPointsEarned;
	}

	public void setRewardPointsEarned(String rewardPointsEarned)
	{
		this.rewardPointsEarned = rewardPointsEarned;
	}

	public String getReserved2()
	{
		return reserved2;
	}

	public void setReserved2(String reserved2)
	{
		this.reserved2 = reserved2;
	}

	public String getAvailableCreditLimit()
	{
		return availableCreditLimit;
	}

	public void setAvailableCreditLimit(String availableCreditLimit)
	{
		this.availableCreditLimit = availableCreditLimit;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getReserved3()
	{
		return reserved3;
	}

	public void setReserved3(String reserved3)
	{
		this.reserved3 = reserved3;
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}
}