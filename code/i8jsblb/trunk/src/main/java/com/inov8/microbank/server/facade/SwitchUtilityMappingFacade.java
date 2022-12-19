package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.switchutilitymappingmodule.SwitchUtilityMappingManager;

public class SwitchUtilityMappingFacade implements SwitchUtilityMappingManager
{

	private SwitchUtilityMappingManager switchUtilityMappingManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
		
	
	public SearchBaseWrapper findUtilityCompanyCodeByExample(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			return this.switchUtilityMappingManager.findUtilityCompanyCodeByExample(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
		
	}

	public BaseWrapper findUtilityCompanyCodeByExample(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.switchUtilityMappingManager.findUtilityCompanyCodeByExample(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setSwitchUtilityMappingManager(SwitchUtilityMappingManager switchUtilityMappingManager)
	{
		this.switchUtilityMappingManager = switchUtilityMappingManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
