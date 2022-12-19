package com.inov8.microbank.server.facade;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;

public class SettlementFacadeImpl implements SettlementFacade
{
	private SettlementManager settlementManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public FrameworkExceptionTranslator getFrameworkExceptionTranslator()
	{
		return frameworkExceptionTranslator;
	}

	public SettlementManager getSettlementManager()
	{
		return settlementManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setSettlementManager(SettlementManager settlementManager)
	{
		this.settlementManager = settlementManager;
	}

	public SwitchWrapper settleBankPayment(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws Exception
	{
		return settlementManager.settleBankPayment(commissionWrapper, workFlowWrapper);
	}

	public void settleCreditPayment(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		settlementManager.settleCreditPayment(commissionWrapper, workFlowWrapper);
	}

	public void settleCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws Exception, WorkFlowException
	{
		try
		{
			this.settlementManager.settleCommission(commissionWrapper, workFlowWrapper);
		}
		catch (WorkFlowException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw ex;
		}

	}

	public WorkFlowWrapper rollbackBankPayment(WorkFlowWrapper workFlowWrapper) throws Exception
	{
		// TODO Auto-generated method stub
		return this.settlementManager.rollbackBankPayment(workFlowWrapper);
	}

	public WorkFlowWrapper rollbackCommissionSettlement(WorkFlowWrapper workFlowWrapper) throws Exception
	{
		return this.settlementManager.rollbackCommissionSettlement(workFlowWrapper);

	}

	public void settleAllPayCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, Exception
	{
		try
		{
			this.settlementManager.settleAllPayCommission(commissionWrapper, workFlowWrapper);
		}
		catch (WorkFlowException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw ex;
		}		
	}

	public CommissionTransactionModel saveCommissionTransactionMoel(CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException, Exception {
		
		try
		{
			commissionTxModel = this.settlementManager.saveCommissionTransactionMoel(commissionTxModel);
		}
		catch (WorkFlowException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
		return commissionTxModel;
	}

	@Override
	public SettlementTransactionModel saveSettlementTransactionModel(
			SettlementTransactionModel settlementTransactionModel)
			throws Exception {
		
		return this.settlementManager.saveSettlementTransactionModel(settlementTransactionModel);
	}

	@Override
	public Long getDistributorBankInfoId(Long distributerId)
			throws FrameworkCheckedException {
		
		return this.settlementManager.getDistributorBankInfoId(distributerId);
	}


	@Override
	public Long getStakeholderBankInfoId(Long accountTypeId) throws FrameworkCheckedException {
		return settlementManager.getStakeholderBankInfoId(accountTypeId);
	}

	@Override
	public List<SettlementTransactionModel> getSettlementTransactionModelList(Long stakeholderBankInfoId, Boolean isCreditEntry, Date createdOn) {
		return settlementManager.getSettlementTransactionModelList(stakeholderBankInfoId, isCreditEntry, createdOn);
	}
	
	@Override
	public void prepareDataForDayEndSettlement(WorkFlowWrapper wrapper) throws Exception {
		settlementManager.prepareDataForDayEndSettlement(wrapper);
	}

	@Override
	public void settleHierarchyCommission(CommissionWrapper commissionWrapper,
			WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		this.settlementManager.settleHierarchyCommission(commissionWrapper, workFlowWrapper);
		
	}

	@Override
	public void updateCommissionTransactionSettled(Long transactionDetailId) throws FrameworkCheckedException {
		settlementManager.updateCommissionTransactionSettled(transactionDetailId);
	}

}
