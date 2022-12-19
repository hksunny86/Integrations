package com.inov8.microbank.server.service.bulkdisbursements;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author Fahad Tariq
 * @version 1.0
 */

public interface BulkDisbursementsManager {
	public BulkDisbursementsModel saveOrUpdateBulkDisbursement(BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException;
	public void createOrUpdateBulkDisbursements(List<BulkDisbursementsModel> dis) throws FrameworkCheckedException;
	public void createOrUpdateBulkPayments(List<BulkDisbursementsModel> bulkPaymentList) throws FrameworkCheckedException;
	void update(BulkDisbursementsModel model) throws FrameworkCheckedException;
	public void makeDebitCreditAccount(String sourceAccount, String targetAccount, Double totalDisbursableAmount, List<BulkDisbursementsModel> bulkDisbursementsModels, WorkFlowWrapper wrapper) throws FrameworkCheckedException;
	public void makeTransferFund(StakeholderBankInfoModel sourceAccount, BulkDisbursementsModel bulkDisbursementsModel, String notificationMessage, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchBulkDisbursements(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchBulkPayments(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadBulkDisbursement(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public BaseWrapper updateBulkDisbursement(BaseWrapper wrapper) throws FrameworkCheckedException;
	public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException;
	public void saveOrUpdateCollection(List<BulkDisbursementsModel> bulkDisbursementsModels) throws FrameworkCheckedException;
	public String postCoreFundTransfer(String fromAccountNumber, String toAccountNumber, Double amount, Long productId, WorkFlowWrapper wrapper) throws FrameworkCheckedException;
}
