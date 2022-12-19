package com.inov8.microbank.server.facade.allpaymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.AllpayModule.AllpayCommissionRatesManager;

public class AllpayCommissionRatesFacadeImpl implements
		AllpayCommissionRatesFacade {
	private FrameworkExceptionTranslator frameworkExceptionTranslator; 
	private AllpayCommissionRatesManager allpayCommissionRatesManager;

	public BaseWrapper createCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			 this.allpayCommissionRatesManager.createCommissionRates(baseWrapper);
		} catch (Exception ex) {			
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper loadCommissionRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			 this.allpayCommissionRatesManager.loadCommissionRates(searchBaseWrapper);
		} catch (Exception ex) {			
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.FIND_ACTION
			          );
		}
		return searchBaseWrapper;
	}

	public BaseWrapper loadCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			 this.allpayCommissionRatesManager.loadCommissionRates(baseWrapper);
		} catch (Exception ex) {			
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.FIND_ACTION
			          );
		}
		return baseWrapper;
		}

	public SearchBaseWrapper searchCommissionRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			 this.allpayCommissionRatesManager.searchCommissionRates(searchBaseWrapper);
		} catch (Exception ex) {			
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.FIND_ACTION
			          );
		}
		return searchBaseWrapper;
	}
	
	

	public SearchBaseWrapper searchAllPayCommissionRate(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.allpayCommissionRatesManager.searchAllPayCommissionRate(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public BaseWrapper updateCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			 this.allpayCommissionRatesManager.loadCommissionRates(baseWrapper);
		} catch (Exception ex) {			
			throw this.frameworkExceptionTranslator.translate(ex,
			          this.frameworkExceptionTranslator.FIND_ACTION
			          );
		}
		return baseWrapper;
	}

	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public AllpayCommissionRatesManager getAllpayCommissionRatesManager() {
		return allpayCommissionRatesManager;
	}

	public void setAllpayCommissionRatesManager(
			AllpayCommissionRatesManager allpayCommissionRatesManager) {
		this.allpayCommissionRatesManager = allpayCommissionRatesManager;
	}}
