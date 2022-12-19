package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyModuleManager;

/**
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */

public class VeriflyFacadeImpl implements VeriflyFacade
{

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private VeriflyModuleManager veriflyModuleManager ;


	public VeriflyFacadeImpl()
	{
	}


	public BaseWrapper createOrUpdateVerifly(BaseWrapper baseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.veriflyModuleManager.createOrUpdateVerifly(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchVerifly(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.veriflyModuleManager.searchVerifly(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}


	public BaseWrapper loadVerifly(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.veriflyModuleManager.loadVerifly(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return baseWrapper;
	}


	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


	public void setVeriflyModuleManager(VeriflyModuleManager veriflyModuleManager)
	{
		this.veriflyModuleManager = veriflyModuleManager;
	}

}
