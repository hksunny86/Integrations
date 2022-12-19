package com.inov8.microbank.server.service.integration.vo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;

public class CreditAdviceVO implements ProductVO {
	
	
    protected String mobileNo;
    protected Double amount;
    protected String accountNumber;
    protected Long appUserId;
    protected Long customerId;
    protected Double balance;
    private String accountTitle;
	

	@Override
	public void setResponseCode(String responseCode) {

	}

	@Override
	public String getResponseCode() {
		return null;
	}

	@Override
	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_MOB_NO ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_MOB_NO ).toString().trim();
		      ((CreditAdviceVO)productVO).setMobileNo( mobileNo );
		}

	
		if( baseWrapper.getObject( CommandFieldConstants.KEY_AMOUNT ) != null )
		{
		    try
			{
				((CreditAdviceVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_AMOUNT ).toString().trim()) );
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		
		return (ProductVO)productVO;
	}

	@Override
	public void validateVO(ProductVO productVO)throws FrameworkCheckedException {
		if( ((CreditAdviceVO)productVO).getMobileNo() == null )
			throw new FrameworkCheckedException("Mobile number is not provided.");
		if( ((CreditAdviceVO)productVO).getAmount() == null )
			throw new FrameworkCheckedException("Amount is not provided.");
		
		ValidationErrors validationErrors = new ValidationErrors(); 
		validationErrors = ValidatorWrapper.doNumeric(((CreditAdviceVO)productVO).getMobileNo(), validationErrors, "Mobile number");
		if( validationErrors.hasValidationErrors() )
		{
			throw new FrameworkCheckedException("Mobile number is invalid.");
		}

	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
