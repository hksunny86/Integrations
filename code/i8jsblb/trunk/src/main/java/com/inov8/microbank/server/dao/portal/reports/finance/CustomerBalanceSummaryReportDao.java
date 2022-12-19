package com.inov8.microbank.server.dao.portal.reports.finance;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportSummaryModel;

public interface CustomerBalanceSummaryReportDao extends BaseDAO<CustomerBalanceReportSummaryModel, Long> {
		
	 void updateDailyCustomerBalanceSummary(List<CustomerBalanceReportSummaryModel> list, Long actionId)throws FrameworkCheckedException;
}
