package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.tickermodule.TickerManager;

public class TickerFacadeImpl implements TickerFacade {
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	private TickerManager tickerManager;

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setTickerManager(TickerManager tickerManager) {
		this.tickerManager = tickerManager;
	}

	
	
	public SearchBaseWrapper searchTicker(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			this.tickerManager.searchTicker(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							this.frameworkExceptionTranslator.FIND_ACTION);

		}

		return searchBaseWrapper;
	}
    
	
	
	public BaseWrapper loadDefaultTicker(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try{
			baseWrapper = this.tickerManager.loadDefaultTicker(baseWrapper);
		}
		catch(Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper createTickerUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		try
	    {
	      this.tickerManager.createTickerUser(
	          baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	    }
	    return baseWrapper;


	}

	public SearchBaseWrapper loadTickerUser(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			this.tickerManager.loadTickerUser(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);

		}

		return searchBaseWrapper;

	}

	public SearchBaseWrapper searchTickerUser(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {

		try {
			this.tickerManager.searchTickerUser(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
		// TODO Auto-generated method stub

	}

	public BaseWrapper updateTickerUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		  try
		    {
		      this.tickerManager.updateTickerUser(
		          baseWrapper);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		    }
		    return baseWrapper;
		
	}

}
