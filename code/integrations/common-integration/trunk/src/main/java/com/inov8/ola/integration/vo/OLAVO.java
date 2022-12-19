package com.inov8.ola.integration.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

import com.inov8.framework.common.model.SortingOrder;




public class OLAVO implements Serializable
{
	
	private Double balance ;
	private String authCode;
    private String mfsId;
	private String microbankTransactionCode;
	private String payingAccNo;
	private String receivingAccNo;
	private String responseCode;
	private String transactionTypeId;
	private Date transactionDateTime;
	private String address;
	private String mobileNumber;
	private Long accountHolderId;
	private String firstName;
	private String lastName;
	private String middleName;
	private String cnic;
	private String fatherName;
	private Boolean active;
	private Boolean deleted;
	private String landlineNumber;
	private Date dob;
	private Long reasonId;
	private Long statusId ;
	private Long accountId ;
	private String statusName;
	
	private Long balanceDisbursed;
	private Long balanceReceived;
	private String endDayBalance;
	private String startDayBalance;
	private Date statsDate;
	private Long dailyAccountStatsId;
	
	private Date statsStartDate;
	private Date statsEndDate;
	private boolean isBillPayment;
	private Long customerAccountTypeId;
	
	private LinkedHashMap<String, SortingOrder> sortingOrderMap ;
	
	
	
	
	public Date getStatsStartDate()
	{
		return statsStartDate;
	}

	public void setStatsStartDate(Date statsStartDate)
	{
		this.statsStartDate = statsStartDate;
	}

	public Date getStatsEndDate()
	{
		return statsEndDate;
	}

	public void setStatsEndDate(Date statsEndDate)
	{
		this.statsEndDate = statsEndDate;
	}

	public Long getAccountHolderId()
	{
		return accountHolderId;
	}
	
	public void setAccountHolderId(Long accountHolderId)
	{
		this.accountHolderId = accountHolderId;
	}
	
	public Boolean getActive()
	{
		return active;
	}
	
	public void setActive(Boolean active)
	{
		this.active = active;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getAuthCode()
	{
		return authCode;
	}
	
	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}
	
	public Double getBalance()
	{
		return balance;
	}
	
	public void setBalance(Double balance)
	{
		this.balance = balance;
	}
	
	public String getCnic()
	{
		return cnic;
	}
	
	public void setCnic(String cnic)
	{
		this.cnic = cnic;
	}
	
	public Boolean getDeleted()
	{
		return deleted;
	}
	
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}
	
	public Date getDob()
	{
		return dob;
	}
	
	public void setDob(Date dob)
	{
		this.dob = dob;
	}
	
	public String getFatherName()
	{
		return fatherName;
	}
	
	public void setFatherName(String fatherName)
	{
		this.fatherName = fatherName;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getMfsId()
	{
		return mfsId;
	}
	
	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
	}
	
	public String getMicrobankTransactionCode()
	{
		return microbankTransactionCode;
	}
	
	public void setMicrobankTransactionCode(String microbankTransactionCode)
	{
		this.microbankTransactionCode = microbankTransactionCode;
	}
	
	public String getLandlineNumber()
	{
		return landlineNumber;
	}
	
	public void setLandlineNumber(String landlineNumber)
	{
		this.landlineNumber = landlineNumber;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getMiddleName()
	{
		return middleName;
	}
	
	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}
	
	public String getMobileNumber()
	{
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}
	
	public String getPayingAccNo()
	{
		return payingAccNo;
	}
	
	public void setPayingAccNo(String payingAccNo)
	{
		this.payingAccNo = payingAccNo;
	}
	
	public Long getReasonId()
	{
		return reasonId;
	}
	
	public void setReasonId(Long reasonId)
	{
		this.reasonId = reasonId;
	}
	
	public String getReceivingAccNo()
	{
		return receivingAccNo;
	}
	
	public void setReceivingAccNo(String receivingAccNo)
	{
		this.receivingAccNo = receivingAccNo;
	}
	
	public String getResponseCode()
	{
		return responseCode;
	}
	
	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode;
	}
	
	public Date getTransactionDateTime()
	{
		return transactionDateTime;
	}
	
	public void setTransactionDateTime(Date transactionDateTime)
	{
		this.transactionDateTime = transactionDateTime;
	}
	
	public String getTransactionTypeId()
	{
		return transactionTypeId;
	}
	
	public void setTransactionTypeId(String transactionTypeId)
	{
		this.transactionTypeId = transactionTypeId;
	}

	
	public Long getStatusId()
	{
		return statusId;
	}

	
	public void setStatusId(Long statusId)
	{
		this.statusId = statusId;
	}

	
	public Long getAccountId()
	{
		return accountId;
	}

	
	public void setAccountId(Long accountId)
	{
		this.accountId = accountId;
	}

	
	public LinkedHashMap<String, SortingOrder> getSortingOrderMap()
	{
		return sortingOrderMap;
	}

	
	public void setSortingOrderMap(LinkedHashMap<String, SortingOrder> sortingOrderMap)
	{
		this.sortingOrderMap = sortingOrderMap;
	}

	
	public String getStatusName()
	{
		return statusName;
	}

	
	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	public Long getBalanceDisbursed()
	{
		return balanceDisbursed;
	}

	public void setBalanceDisbursed(Long balanceDisbursed)
	{
		this.balanceDisbursed = balanceDisbursed;
	}

	public Long getBalanceReceived()
	{
		return balanceReceived;
	}

	public void setBalanceReceived(Long balanceReceived)
	{
		this.balanceReceived = balanceReceived;
	}

	public String getEndDayBalance()
	{
		return endDayBalance;
	}

	public void setEndDayBalance(String endDayBalance)
	{
		this.endDayBalance = endDayBalance;
	}

	public String getStartDayBalance()
	{
		return startDayBalance;
	}

	public void setStartDayBalance(String startDayBalance)
	{
		this.startDayBalance = startDayBalance;
	}

	public Date getStatsDate()
	{
		return statsDate;
	}

	public void setStatsDate(Date statsDate)
	{
		this.statsDate = statsDate;
	}

	public Long getDailyAccountStatsId()
	{
		return dailyAccountStatsId;
	}

	public void setDailyAccountStatsId(Long dailyAccountStatsId)
	{
		this.dailyAccountStatsId = dailyAccountStatsId;
	}

	public boolean getIsBillPayment() {
		return isBillPayment;
	}

	public void setIsBillPayment(boolean isBillPayment) {
		this.isBillPayment = isBillPayment;
	}

	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}

	}
