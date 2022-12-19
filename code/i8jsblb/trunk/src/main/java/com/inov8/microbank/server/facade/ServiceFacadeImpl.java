package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.service.servicemodule.ServiceManager;

public class ServiceFacadeImpl 
	implements ServiceFacade	
{
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private ServiceManager serviceManager;
	
	
	public BaseWrapper loadService(BaseWrapper baseWrapper) throws
    FrameworkCheckedException
{
  try
  {
    this.serviceManager.loadService(baseWrapper);
  }
  catch (Exception ex)
  {
    throw this.frameworkExceptionTranslator.translate(ex,
        this.frameworkExceptionTranslator.
        FIND_BY_PRIMARY_KEY_ACTION);
  }
  return baseWrapper;
}


	public BaseWrapper createOrUpdateService(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		try
		  {
		    this.serviceManager.createOrUpdateService(baseWrapper);
		  }
		  catch (Exception ex)
		  {
		    throw this.frameworkExceptionTranslator.translate(ex,
		        this.frameworkExceptionTranslator.
		        FIND_BY_PRIMARY_KEY_ACTION);
		  }
		  return baseWrapper;
	}
	
	public SearchBaseWrapper searchService(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		try
		  {
		    this.serviceManager.searchService(searchBaseWrapper);
		  }
		  catch (Exception ex)
		  {
		    throw this.frameworkExceptionTranslator.translate(ex,
		        this.frameworkExceptionTranslator.
		        FIND_BY_PRIMARY_KEY_ACTION);
		  }
		  return searchBaseWrapper;
	}

	public List<LabelValueBean> getServiceLabels(Long... pk) {
		return serviceManager.getServiceLabels(pk);
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}


}
