package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.auditlogmodule.AuditLogListViewManager;


public class AuditLogListViewFacadeImpl implements AuditLogListViewFacade {

	private AuditLogListViewManager auditLogListViewManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public BaseWrapper loadAuditLog(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try{
			baseWrapper = this.auditLogListViewManager.loadAuditLog(baseWrapper);
		}
		catch(Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}
	
	public SearchBaseWrapper searchAuditLogListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      
	      this.auditLogListViewManager.searchAuditLogListView(searchBaseWrapper);
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;
	}

	public void setAuditLogListViewManager(
			AuditLogListViewManager auditLogListViewManager) {
		this.auditLogListViewManager = auditLogListViewManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


}
