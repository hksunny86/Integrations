package com.inov8.microbank.server.facade.ussdmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.ussdmodule.UssdMenuMappingManager;

public class UssdMenuMappingFacadeImpl implements UssdMenuMappingFacade{
	private UssdMenuMappingManager ussdMenuMappingManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	public BaseWrapper findMenuMapping(BaseWrapper param) throws FrameworkCheckedException {
		BaseWrapper retVal=null;
		try
	    {
			retVal=this.ussdMenuMappingManager.findMenuMapping(param);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
		return retVal;
	}
	public UssdMenuMappingManager getUssdMenuMappingManager() {
		return ussdMenuMappingManager;
	}
	public void setUssdMenuMappingManager(
			UssdMenuMappingManager ussdMenuMappingManager) {
		this.ussdMenuMappingManager = ussdMenuMappingManager;
	}
	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
