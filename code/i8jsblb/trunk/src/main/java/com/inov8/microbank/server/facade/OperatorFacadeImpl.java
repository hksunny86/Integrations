package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.operatormodule.OperatorBankInfoManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.operatormodule.OperatorUserManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class OperatorFacadeImpl implements OperatorFacade
{

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private OperatorManager operatorManager;
	private OperatorUserManager operatorUserManager;
	private OperatorBankInfoManager operatorBankInfoManager;

	public void setOperatorUserManager(OperatorUserManager operatorUserManager)
	{
		this.operatorUserManager = operatorUserManager;
	}

	/**
	 * loadOperator
	 *
	 * @param searchBaseWrapper BaseWrapper
	 * @return BaseWrapper
	 * @throws FrameworkCheckedException
	 *
	 */
	public BaseWrapper loadOperator(BaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.operatorManager.loadOperator(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return searchBaseWrapper;
	}

	/**
	 * updateOperator
	 *
	 * @param baseWrapper BaseWrapper
	 * @return BaseWrapper
	 * @throws FrameworkCheckedException
	 */
	public BaseWrapper updateOperator(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.operatorManager.updateOperator(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.UPDATE_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper loadOperatorByAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.operatorManager.loadOperatorByAppUser(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.UPDATE_ACTION);
		}
		return baseWrapper;
	}

	public void setOperatorManager(OperatorManager operatorManager)
	{
		this.operatorManager = operatorManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BaseWrapper createAppUserForOperator(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		try
		{
			this.operatorUserManager.createAppUserForOperator(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper createOperatorUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		try
		{
			this.operatorUserManager.createOperatorUser(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper loadOperatorUser(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{

		try
		{
			this.operatorUserManager.loadOperatorUser(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchOperatorUser(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			this.operatorUserManager.searchOperatorUser(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public BaseWrapper updateOperatorUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		try
		{
			this.operatorUserManager.updateOperatorUser(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.UPDATE_ACTION);
		}
		return baseWrapper;

	}

	public BaseWrapper getOperatorBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.operatorManager.getOperatorBankInfo(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;
	}
	
	public BaseWrapper loadOperatorBankInfo(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.operatorBankInfoManager
					.loadOperatorBankInfo(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);

		}
	}

	public SearchBaseWrapper loadOperatorBankInfo(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.operatorBankInfoManager
					.loadOperatorBankInfo(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);

		}
	}

	public void setOperatorBankInfoManager(
			OperatorBankInfoManager operatorBankInfoManager) {
		this.operatorBankInfoManager = operatorBankInfoManager;
	}

	public BaseWrapper updateOperatorBankInfo(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			this.operatorBankInfoManager.updateOperatorBankInfo(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.UPDATE_ACTION);
		}
		return baseWrapper;
	}

	public BaseWrapper createOperatorBankInfo(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			this.operatorBankInfoManager.createOperatorBankInfo(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchOperatorBankInfo(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			this.operatorBankInfoManager
					.searchOperatorBankInfo(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
	
		Long getAppUserPartnerGroupId;
		try
		    {
		       getAppUserPartnerGroupId = this.operatorUserManager.getAppUserPartnerGroupId(appUserId);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          FrameworkExceptionTranslator.FIND_ACTION);
		    }
		    return getAppUserPartnerGroupId;

	}
	

}
