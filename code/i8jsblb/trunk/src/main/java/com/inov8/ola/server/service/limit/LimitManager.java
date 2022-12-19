package com.inov8.ola.server.service.limit;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.BlinkCustomerLimitModel;
import com.inov8.microbank.common.model.LimitRuleModel;

/** 	
 * @author 					Usman Ashraf

 * Project Name: 			OLA
 * Creation Date: 			April 2012 			
 * Description:				
 */




public interface LimitManager 
{
			
	public LimitModel getLimitByTransactionType(Long transactionTypeId, Long limitTypeId,Long customerAccountTypeId)throws Exception;
	public BlinkDefaultLimitModel getBlinkDefaultLimitByTransactionType(Long transactionTypeId, Long limitTypeId, Long customerAccountTypeId)throws Exception;

	public BlinkCustomerLimitModel getBlinkCustomerLimitByTransactionType(Long transactionTypeId, Long limitTypeId, Long customerAccountTypeId,Long accountId)throws Exception;
	public List<BlinkCustomerLimitModel> getBlinkCustomerLimitByTransactionTypeByCustomerId(Long customerId)throws FrameworkCheckedException;

	public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException;
	public boolean updateLimitsList(List<LimitModel> limitsList) throws FrameworkCheckedException;
	BaseWrapper saveOrUpdateLimitRule(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	LimitRuleModel loadLimitRuleModel(Long limitRuleId)throws FrameworkCheckedException;
	SearchBaseWrapper searchLimitRule(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
}
