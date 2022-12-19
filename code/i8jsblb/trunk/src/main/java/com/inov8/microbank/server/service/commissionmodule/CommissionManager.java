package com.inov8.microbank.server.service.commissionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.UnsettledAgentCommModel;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Syed Ahmad Bilal
 * @version 1.0
 */
public interface CommissionManager
{
	public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper getCommissionTransactionModel(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException;
	public Boolean removeCommissionTransactionModel(CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException;
	public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper getCommissionRateData(CommissionRateModel commissionRateModel);
	public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] params, TransactionProductEnum productEnum, Integer legNumber);
	public CommissionAmountsHolder loadCommissionDetails(Long transactionDetailId) throws FrameworkCheckedException;
	public CommissionAmountsHolder loadCommissionDetailsUnsettled(Long transactionDetailId) throws FrameworkCheckedException;
	public void calculateHierarchyCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;
	public void makeAgent2CommissionSettlement(WorkFlowWrapper wrapper) throws Exception;
	public void saveUnsettledCommission(UnsettledAgentCommModel model, Long agentAppUserId) throws FrameworkCheckedException;
	public SearchBaseWrapper searchUnsettledAgentCommission(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	
}
