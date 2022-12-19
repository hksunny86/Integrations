package com.inov8.microbank.server.facade.currencycodemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.currencycodemodule.CurrencyCodeManager;

public class CurrencyCodeFacadeImpl implements CurrencyCodeFacade {

	private CurrencyCodeManager currencyCodeManager ;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public SearchBaseWrapper loadCurrencyCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	
		try
	    {
	      this.currencyCodeManager.loadCurrencyCode(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}
	public void setCurrencyCodeManager(CurrencyCodeManager currencyCodeManager) {
		this.currencyCodeManager = currencyCodeManager;
	}
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
