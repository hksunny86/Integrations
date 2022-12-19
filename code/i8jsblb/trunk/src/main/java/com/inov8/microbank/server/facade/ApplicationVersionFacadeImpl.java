package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.applicationversionmodule.ApplicationVersionManager;


public class ApplicationVersionFacadeImpl implements ApplicationVersionFacade
{

	private ApplicationVersionManager applicationVersionManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public BaseWrapper createApplicationVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.applicationVersionManager.createApplicationVersion(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper loadApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.applicationVersionManager.loadApplicationVersion(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	    }
	    return searchBaseWrapper;
	}

	public BaseWrapper loadApplicationVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.applicationVersionManager.loadApplicationVersion(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	    }
	    return baseWrapper;
	}

	public SearchBaseWrapper searchApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.applicationVersionManager.searchApplicationVersion(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}

	public BaseWrapper updateApplicationVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.applicationVersionManager.updateApplicationVersion(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
	    return baseWrapper;
	}

	public void setApplicationVersionManager(ApplicationVersionManager applicationVersionManager)
	{
		this.applicationVersionManager = applicationVersionManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
