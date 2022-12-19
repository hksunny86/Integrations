package com.inov8.microbank.server.dao.dailyjob;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;

public interface SettlementTransactionDAO extends BaseDAO <BasePersistableModel, Long> {

	public List<SettlementTransactionModel> getSettlementTransactionModelList(SettlementTransactionModel settlementTransactionModel);
	public Boolean updateSettlementTransaction(Object[] transactionIDs, Long settlementSchedulerID, Long status);
	public List<SettlementTransactionModel> getPendingSettlementTransactionList(StakeholderBankInfoModel stakeholderBankInfoModel, Date _startTime, Boolean isCredit);
}
