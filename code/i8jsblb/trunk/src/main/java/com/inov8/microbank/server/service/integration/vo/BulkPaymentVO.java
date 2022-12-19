package com.inov8.microbank.server.service.integration.vo;

import java.io.Serializable;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;

public class BulkPaymentVO implements ProductVO, Serializable{
	
  private Double txAmount;
  private String transactionCode;
  private Long appUserId;
  private String recipientWalkinMobile;
  private String recipientWalkinCNIC;
  private Double balance;
  
  public String getResponseCode(){	
	return null;
  }

  public ProductVO populateVO( ProductVO bulkPaymentVO, BaseWrapper baseWrapper )
  {
    
    if( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC ) != null ){
    	((BulkPaymentVO)bulkPaymentVO).setRecipientWalkinCNIC( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC ).toString().trim() );
    }

    if( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN ) != null ){
    	((BulkPaymentVO)bulkPaymentVO).setRecipientWalkinMobile( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN ).toString().trim() );
    }

    if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null )
	{
	    try
		{
			((BulkPaymentVO)bulkPaymentVO).setTxAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
	}

    return (ProductVO)bulkPaymentVO;
  }

  public void setResponseCode(String responseCode)
  {
  }

  public void validateVO(ProductVO productVO) throws FrameworkCheckedException
  {
  }

public String getTransactionCode()
{
	return transactionCode;
}


public void setTransactionCode(String transactionCode)
{
	this.transactionCode = transactionCode;
}

public Long getAppUserId()
{
	return appUserId;
}

public void setAppUserId(Long appUserId)
{
	this.appUserId = appUserId;
}

public Double getTxAmount() {
	return txAmount;
}

public void setTxAmount(Double txAmount) {
	this.txAmount = txAmount;
}

public String getRecipientWalkinMobile() {
	return recipientWalkinMobile;
}

public void setRecipientWalkinMobile(String recipientWalkinMobile) {
	this.recipientWalkinMobile = recipientWalkinMobile;
}

public String getRecipientWalkinCNIC() {
	return recipientWalkinCNIC;
}

public void setRecipientWalkinCNIC(String recipientWalkinCNIC) {
	this.recipientWalkinCNIC = recipientWalkinCNIC;
}

public Double getBalance() {
	return balance;
}

public void setBalance(Double balance) {
	this.balance = balance;
}

}