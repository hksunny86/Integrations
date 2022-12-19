package com.inov8.microbank.server.facade.portal.forgotpinmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.forgotpinmodule.ForgotpinManager;

public class ForgotPinFacadeImp implements ForgotPinFacade
{

	private ForgotpinManager forgotPinManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchForgotPinUser(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
	   try
	    {
		   return forgotPinManager.searchForgotPinUser(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	}

	public BaseWrapper updateForgotPin(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
	   try
	    {
	      		  
		   return forgotPinManager.updateForgotPin(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    		  FrameworkExceptionTranslator.FIND_ACTION);
	    }
	}
	
	public void setForgotPinManager(ForgotpinManager forgotPinManager) {
		this.forgotPinManager = forgotPinManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BaseWrapper updateForgotPassword(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		
		 try
		    {
		      		  
			   return forgotPinManager.updateForgotPassword(baseWrapper);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		    		  FrameworkExceptionTranslator.FIND_ACTION);
		    }
	}

}
