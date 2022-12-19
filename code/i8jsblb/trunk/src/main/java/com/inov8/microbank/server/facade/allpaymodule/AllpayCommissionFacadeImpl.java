package com.inov8.microbank.server.facade.allpaymodule;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			December 2008  			
 * Description:				
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.AllpayModule.AllpayCommissionTransactionManager;

public class AllpayCommissionFacadeImpl implements AllpayCommissionFacade
{

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private AllpayCommissionTransactionManager allpayCommissionTransactionManager;

	public BaseWrapper loadCommissionTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.allpayCommissionTransactionManager.loadCommissionTransaction(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper updateCommissionTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.allpayCommissionTransactionManager.updateCommissionTransaction(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.UPDATE_ACTION);
		}
		return baseWrapper;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setAllpayCommissionTransactionManager(AllpayCommissionTransactionManager allpayCommissionTransactionManager)
	{
		this.allpayCommissionTransactionManager = allpayCommissionTransactionManager;
	}
}
