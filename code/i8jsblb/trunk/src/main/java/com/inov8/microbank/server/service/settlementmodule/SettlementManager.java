package com.inov8.microbank.server.service.settlementmodule;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public interface SettlementManager
{
	public SwitchWrapper settleBankPayment(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws Exception;

	public void settleCreditPayment(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

	public void settleCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws Exception;

	public WorkFlowWrapper rollbackBankPayment(WorkFlowWrapper workFlowWrapper) throws Exception;

	public WorkFlowWrapper rollbackCommissionSettlement(WorkFlowWrapper workFlowWrapper) throws Exception;
	
	public void settleAllPayCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, Exception;

	public CommissionTransactionModel saveCommissionTransactionMoel(CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException, Exception;
	
	public SettlementTransactionModel saveSettlementTransactionModel(SettlementTransactionModel settlementTransactionModel) throws Exception;
	
	public Long getDistributorBankInfoId(Long distributerId) throws FrameworkCheckedException;
	
	public Long getStakeholderBankInfoId(Long accountTypeId) throws FrameworkCheckedException;
	
	public List<SettlementTransactionModel> getSettlementTransactionModelList(Long stakeholderBankInfoId, Boolean isCreditEntry, Date createdOn);

	public void prepareDataForDayEndSettlement(WorkFlowWrapper wrapper) throws Exception;

	public void settleHierarchyCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

	public void updateCommissionTransactionSettled(Long transactionDetailId) throws FrameworkCheckedException;

}
