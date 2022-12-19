package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.retailertypemodule.RetailerTypeManager;


public class RetailerTypeFacadeImpl implements RetailerTypeFacade {

	
	private RetailerTypeManager retailerTypeManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public SearchBaseWrapper searchRetailerType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.retailerTypeManager.searchRetailerType(searchBaseWrapper);
					
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;

	}

	
	public BaseWrapper loadRetailerType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.retailerTypeManager.loadRetailerType(baseWrapper);
					
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	
	public BaseWrapper updateRetailerType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.retailerTypeManager.updateRetailerType(baseWrapper);
					
	    
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


	public void setRetailerTypeManager(RetailerTypeManager retailerTypeManager) {
		this.retailerTypeManager = retailerTypeManager;
	}


}
