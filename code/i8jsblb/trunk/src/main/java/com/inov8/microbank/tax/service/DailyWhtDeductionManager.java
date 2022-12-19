package com.inov8.microbank.tax.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.tax.model.*;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;

import java.util.Date;
import java.util.List;

public interface DailyWhtDeductionManager {

	BaseWrapper saveDailyWhtDeduction(BaseWrapper baseWrapper) throws Exception;
	BaseWrapper saveDailyWhtDeductionRequiresNewTransaction(BaseWrapper baseWrapper) throws Exception;
	List<DailyWhtDeductionModel> loadUnsettledWhtDeductionList(Date toDate) throws Exception;
	CustomList<WHTTrxViewModel> loadWHTTrx(SearchBaseWrapper wrapper) throws Exception;
	List<DateWiseUserWHTAmountViewModel> loadWithholdingUsersList(WHTConfigModel cashWithdrawalWHTConfigModel ,WHTConfigModel transferWHTConfigModel) throws Exception;
	void updateTransactionCode() ;
	
	void saveDailyWhtDeduction(List<DailyWhtDeductionModel> dailyWhtDeductionModelList) throws Exception;
	void saveDailyWhtDeductionRequiresNewTransaction(List<DailyWhtDeductionModel> dailyWhtDeductionModelList) throws Exception;
}