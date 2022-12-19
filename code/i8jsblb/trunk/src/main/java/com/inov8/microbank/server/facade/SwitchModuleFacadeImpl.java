package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchFinderManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;

/**
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */

public class SwitchModuleFacadeImpl implements SwitchModuleFacade
{

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	private SwitchModuleManager switchManager;

	private SwitchFinderManager switchFinderManager;

	public SwitchModuleFacadeImpl()
	{
	}

	public BaseWrapper loadSwitchFinder(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.switchFinderManager.loadSwitchFinder(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper createOrUpdateSwitchFinder(BaseWrapper baseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.switchFinderManager.createOrUpdateSwitchFinder(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchSwitchFinder(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.switchFinderManager.searchSwitchFinder(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public SwitchWrapper getSwitchClassPath(SwitchWrapper switchWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.switchManager.getSwitchClassPath(switchWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return switchWrapper;
	}

	public BaseWrapper loadSwitch(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.switchManager.loadSwitch(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper createOrUpdateSwitch(BaseWrapper baseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.switchManager.createOrUpdateSwitch(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchSwitch(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.switchManager.searchSwitch(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return searchBaseWrapper;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setSwitchManager(SwitchModuleManager switchManager)
	{
		this.switchManager = switchManager;
	}

	public void setSwitchFinderManager(SwitchFinderManager switchFinderManager)
	{
		this.switchFinderManager = switchFinderManager;
	}
}
