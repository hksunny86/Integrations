package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.servicetypemodule.ServiceTypeManager;


public class ServiceTypeFacadeImpl implements ServiceTypeFacade {

	private ServiceTypeManager serviceTypeManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	
	public SearchBaseWrapper searchServiceType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      
			this.serviceTypeManager.searchServiceType(searchBaseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;

	}
	
	
	public BaseWrapper loadServiceType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.serviceTypeManager.loadServiceType(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	

	public BaseWrapper updateServiceType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.serviceTypeManager.updateServiceType(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}


	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


	public void setServiceTypeManager(ServiceTypeManager serviceTypeManager) {
		this.serviceTypeManager = serviceTypeManager;
	}




}
