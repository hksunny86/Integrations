package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.UnsettledAgentCommModel;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;

public class AllPayCommissionFacadeImpl implements AllPayCommissionFacade
{

	private CommissionManager allPayCommissionManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public AllPayCommissionFacadeImpl()
	{
	}

	public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		try
		{
			return allPayCommissionManager.calculateCommission(workFlowWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}



	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	
	public void setAllPayCommissionManager(CommissionManager allPayCommissionManager)
	{
		this.allPayCommissionManager = allPayCommissionManager;
	}

	public SearchBaseWrapper getCommissionTransactionModel(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	public Boolean removeCommissionTransactionModel(CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	

	@Override
	public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return allPayCommissionManager.loadAgentCommission(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	@Override
	public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] params, TransactionProductEnum productEnum, Integer legNumber) {

		return allPayCommissionManager.updateCommissionTransaction(isSettled, isPosted, params, productEnum, legNumber);
	}

	@Override
	public SearchBaseWrapper getCommissionRateData(CommissionRateModel commissionRateModel) {
		// TODO Auto-generated method stub
		return allPayCommissionManager.getCommissionRateData(commissionRateModel);
	}

	@Override
	public CommissionAmountsHolder loadCommissionDetails(Long transactionDetailId) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}
	
	@Override
	public CommissionAmountsHolder loadCommissionDetailsUnsettled(Long transactionDetailId) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	@Override
	public void calculateHierarchyCommission(WorkFlowWrapper workFlowWrapper)
			throws FrameworkCheckedException {
		this.allPayCommissionManager.calculateHierarchyCommission(workFlowWrapper);
		
	}

	@Override
	public void makeAgent2CommissionSettlement(WorkFlowWrapper wrapper) throws Exception {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	@Override
	public void saveUnsettledCommission(UnsettledAgentCommModel model, Long agentAppUserId) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");		
	}

	@Override
	public SearchBaseWrapper searchUnsettledAgentCommission(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");		
	}

}
