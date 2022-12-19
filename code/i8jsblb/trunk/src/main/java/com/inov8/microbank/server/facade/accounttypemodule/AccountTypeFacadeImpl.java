package com.inov8.microbank.server.facade.accounttypemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.server.service.accounttypemodule.AccountTypeManager;


public class AccountTypeFacadeImpl implements AccountTypeFacade{
	
	private AccountTypeManager accountTypeManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public SearchBaseWrapper loadAccountType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	
		try
	    {
	      this.accountTypeManager.loadAccountType(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}
	
	public BaseWrapper loadAccountType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      this.accountTypeManager.loadAccountType(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return baseWrapper;
	}
	
	public List<PaymentModeModel> searchPaymentModeModel(Long accountTypeId) throws FrameworkCheckedException {
		 List<PaymentModeModel> paymentModeModelList = null;
		try
	    {
			paymentModeModelList  =this.accountTypeManager.searchPaymentModeModel(accountTypeId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    
	    return paymentModeModelList;
	}	
	
	public void setAccountTypeManager(AccountTypeManager accountTypeManager) {
		this.accountTypeManager = accountTypeManager;
	}
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


}
