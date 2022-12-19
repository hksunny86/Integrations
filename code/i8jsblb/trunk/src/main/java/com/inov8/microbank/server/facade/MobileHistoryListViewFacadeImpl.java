package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.mobilehistorymodule.MobileHistoryListViewManager;

public class MobileHistoryListViewFacadeImpl implements MobileHistoryListViewFacade {

	private MobileHistoryListViewManager mobileHistoryListViewManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator; 
	public SearchBaseWrapper searchMobileHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      
			this.mobileHistoryListViewManager.searchMobileHistory(searchBaseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;
	}
	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
	public void setMobileHistoryListViewManager(
			MobileHistoryListViewManager mobileHistoryListViewManager) {
		this.mobileHistoryListViewManager = mobileHistoryListViewManager;
	}
	

}
