package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionShSharesViewModel;
import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;
import com.inov8.microbank.common.vo.product.CommissionShSharesVO;
import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;

public class CommissionStakeholderFacadeImpl implements CommissionStakeholderFacade
{
	
	private CommissionStakeholderManager commissionStakeholderManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	@Override
	public BaseWrapper createCommissionStakeholder(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.commissionStakeholderManager.createCommissionStakeholder(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
	}

	@Override
	public SearchBaseWrapper loadCommissionStakeholder(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.commissionStakeholderManager.loadCommissionStakeholder(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator. FIND_BY_PRIMARY_KEY_ACTION );
	    }
	    return searchBaseWrapper;
	}
	
	@Override
	public List<CommissionShSharesViewModel> loadCommissionStakeholderSharesViewList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
	    {
		    return this.commissionStakeholderManager.loadCommissionStakeholderSharesViewList(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	    }
	}
	
	@Override
	public List<CommissionShSharesModel>  loadCommissionShSharesList(CommissionShSharesModel vo) throws FrameworkCheckedException
	{
		try
	    {
		    return this.commissionStakeholderManager.loadCommissionShSharesList(vo);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	    }
	}

	@Override
	public SearchBaseWrapper searchCommissionStakeholder(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.commissionStakeholderManager.searchCommissionStakeholder(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}

	@Override
	public BaseWrapper updateCommissionStakeholder(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
	    {
	      this.commissionStakeholderManager.updateCommissionStakeholder(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.INSERT_ACTION);
	    }
	    return baseWrapper;
	}
	
	public void setCommissionStakeholderManager(CommissionStakeholderManager commissionStakeholderManager)
	{
		this.commissionStakeholderManager = commissionStakeholderManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	@Override
	public SearchBaseWrapper searchCommissionStakeholderAccounts(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		try{
			 this.commissionStakeholderManager.searchCommissionStakeholderAccounts(searchBaseWrapper);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	    }
		
		return searchBaseWrapper;
	}

	@Override
	public BaseWrapper createCommissionStakeholderAccounts(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
		{
			this.commissionStakeholderManager.createCommissionStakeholderAccounts(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
	}
	
	public boolean saveCommissionShShares(List<CommissionShSharesModel> commissionShSharesList) throws FrameworkCheckedException{
		boolean success = false;
		try
		{
			success = this.commissionStakeholderManager.saveCommissionShShares(commissionShSharesList);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
		
		return success;
	}
	
	
	public List<CommissionShSharesModel> loadCommissionShSharesList(SearchBaseWrapper wrapper) throws FrameworkCheckedException{
		List<CommissionShSharesModel> list = null;
		try{
			 list = this.commissionStakeholderManager.loadCommissionShSharesList(wrapper);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	    }
		
		return list;
	}
	
	public List<CommissionShSharesDefaultModel> loadDefaultCommissionShSharesList(Long productId) throws FrameworkCheckedException
	{
		List<CommissionShSharesDefaultModel> list = null;
		try{
			 list = this.commissionStakeholderManager.loadDefaultCommissionShSharesList(productId);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	    }
		
		return list;
	}
	
	public List<CommissionStakeholderModel> loadCommissionStakeholdersList(SearchBaseWrapper wrapper) throws FrameworkCheckedException{
		List<CommissionStakeholderModel> list = null;
		try{
			 list = this.commissionStakeholderManager.loadCommissionStakeholdersList(wrapper);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	    }
		
		return list;
	}
	
	public boolean removeCommissionShSharesByStakeholderIds(List<Long> removeSharesList) throws FrameworkCheckedException{
		boolean removed = false;
		try{
			removed = this.commissionStakeholderManager.removeCommissionShSharesByStakeholderIds(removeSharesList);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.DELETE_ACTION);
	    }
		return removed;
	}
	
	public boolean removeCommissionShSharesByShShareIds(List<Long> removeSharesList) throws FrameworkCheckedException{
		boolean removed = false;
		try{
			removed = this.commissionStakeholderManager.removeCommissionShSharesByShShareIds(removeSharesList);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.DELETE_ACTION);
	    }
		return removed;
	}
	
	public BaseWrapper saveCommissionStakeholderAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return this.commissionStakeholderManager.saveCommissionStakeholderAccount(baseWrapper);
	}

	@Override
	public SearchBaseWrapper searchCommissionStakeholderAccountsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {	
		return this.commissionStakeholderManager.searchCommissionStakeholderAccountsByCriteria(searchBaseWrapper);
	}
	
}
