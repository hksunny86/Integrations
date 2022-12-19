package com.inov8.microbank.server.service.portal.financereportsmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;

public interface FinanceReportsManager
{
	SearchBaseWrapper searchAgentClosingBalanceView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchCustomerBalanceReportView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchCommissionSummaryReportView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	
	SearchBaseWrapper searchCustomerBalanceSummaryReport(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	BaseWrapper updateDailyCustomerBalanceSummary(BaseWrapper newbaseWrapper) throws FrameworkCheckedException;

	List<CustomerBalanceReportViewModel> customSearchCustomerBalanceReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	
	SearchBaseWrapper searchDailyCustomerBalanceReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchBBSettlementAccountsReport(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchCustomerClosingBalanceView(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
	
	/**
	 * @author AtifHussain
	 * @param searchBaseWrapper
	 * @return
	 * @throws FrameworkCheckedException
	 */
	SearchBaseWrapper searchSettlementClosingBalanceView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
