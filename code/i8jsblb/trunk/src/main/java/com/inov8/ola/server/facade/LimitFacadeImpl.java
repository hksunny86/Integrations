package com.inov8.ola.server.facade;

/** 	
 * @author 					Usman Ashraf
 * Project Name: 			OLA
 * Creation Date: 			April 2012  			
 * Description:				
 */


import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.BlinkCustomerLimitModel;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.ola.server.service.limit.LimitManager;



public class LimitFacadeImpl implements LimitFacade
{
		
	private LimitManager limitManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	
	public LimitModel getLimitByTransactionType(Long transactionTypeId, Long limitTypeId,Long customerAccountTypeId)throws Exception{
		LimitModel limitModel = null;
		try
	    {
			
			limitModel = this.limitManager.getLimitByTransactionType(transactionTypeId,limitTypeId,customerAccountTypeId);
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
		    throw ex ;
	    }
	    return limitModel;
	}


	@Override
	public BlinkDefaultLimitModel getBlinkDefaultLimitByTransactionType(Long transactionTypeId, Long limitTypeId, Long customerAccountTypeId) throws Exception {
		BlinkDefaultLimitModel blinkDefaultLimitModel = null;
		try
		{

			blinkDefaultLimitModel = this.limitManager.getBlinkDefaultLimitByTransactionType(transactionTypeId,limitTypeId,customerAccountTypeId);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex ;
		}
		return blinkDefaultLimitModel;
	}

	@Override
	public BlinkCustomerLimitModel getBlinkCustomerLimitByTransactionType(Long transactionTypeId, Long limitTypeId, Long customerAccountTypeId,Long accountId) throws Exception {
		BlinkCustomerLimitModel limitModel = null;
		try
		{

			limitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(transactionTypeId,limitTypeId,customerAccountTypeId ,accountId);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex ;
		}
		return limitModel;
	}

	@Override
	public List<BlinkCustomerLimitModel> getBlinkCustomerLimitByTransactionTypeByCustomerId(Long customerId) throws FrameworkCheckedException {
		List<BlinkCustomerLimitModel> limitsList = null;
		try
		{

			limitsList = this.limitManager.getBlinkCustomerLimitByTransactionTypeByCustomerId(customerId);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex ;
		}
		return limitsList;
	}

	public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException{
		List<LimitModel> limitsList = null;
		try
	    {
			
			limitsList = this.limitManager.getLimitsByCustomerAccountType(customerAccountTypeId);
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
		    throw ex ;
	    }
	    return limitsList;
	}
	
	public boolean updateLimitsList(List<LimitModel> limitsList) throws FrameworkCheckedException{
		boolean saved = false;
		try
	    {
			saved = this.limitManager.updateLimitsList(limitsList);
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
		    throw ex ;
	    }
		
	    return saved;
	}
	
	public void setLimitManager(LimitManager limitManager) {
		this.limitManager = limitManager;
	}
	
	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	@Override
	public BaseWrapper saveOrUpdateLimitRule(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return this.limitManager.saveOrUpdateLimitRule(baseWrapper);
		}
		catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public LimitRuleModel loadLimitRuleModel(Long limitRuleId)
			throws FrameworkCheckedException {
		try
	    {
			return this.limitManager.loadLimitRuleModel(limitRuleId);		
	    }
		catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public SearchBaseWrapper searchLimitRule(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		try
        {
	        return limitManager.searchLimitRule(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	
}

