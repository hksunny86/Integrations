package com.inov8.microbank.server.facade.ussdmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.ussdmodule.UssdMenuManager;

public class UssdMenuFacadeImpl implements UssdMenuFacade{
	private UssdMenuManager ussdMenuManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public BaseWrapper findNextMenu(BaseWrapper param) throws FrameworkCheckedException{
		BaseWrapper retVal=null;
		try
	    {
			retVal=this.ussdMenuManager.findNextMenu(param);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
		return retVal;
	}
	public UssdMenuManager getUssdMenuManager() {
		return ussdMenuManager;
	}
	public void setUssdMenuManager(UssdMenuManager ussdMenuManager) {
		this.ussdMenuManager = ussdMenuManager;
	}
	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
	public BaseWrapper findMenu(BaseWrapper param)
			throws FrameworkCheckedException {
		BaseWrapper retVal=null;
		try
	    {
			retVal=this.ussdMenuManager.findMenu(param);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
		return retVal;
	}
	public BaseWrapper findPreviousMenu(Long screenOutputCode, Long appUserTypeID) throws FrameworkCheckedException {
		BaseWrapper retVal=null;
		try
	    {
			retVal=this.ussdMenuManager.findPreviousMenu(screenOutputCode, appUserTypeID);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
		return retVal;
	}
	public int findPreviousMenuCount(Long screenOutputCode, Long appUserTypeID) throws FrameworkCheckedException {
		int retVal=0;
		try
	    {
			retVal=this.ussdMenuManager.findPreviousMenuCount(screenOutputCode, appUserTypeID);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
		return retVal;
	}

}
