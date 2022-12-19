package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.commandmodulelist.CommandListViewManager;


public class CommandListViewFacadeImpl implements CommandListViewFacade {

	private CommandListViewManager commandListViewManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchCommandListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      
			this.commandListViewManager.searchCommandListView(searchBaseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;
	}

	public void setCommandListViewManager(
			CommandListViewManager commandListViewManager) {
		this.commandListViewManager = commandListViewManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
