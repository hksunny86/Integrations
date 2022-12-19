package com.inov8.microbank.server.dao.portal.reports.finance;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 11, 2013 4:23:40 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface CustomerBalanceReportViewDao extends BaseDAO<CustomerBalanceReportViewModel, Long>
{
	List<CustomerBalanceReportViewModel> customSearchCustomerBalanceReportView(String decryptionSchedulerStatus, Boolean isEndDayBalanceNull, Integer chunkSize) throws FrameworkCheckedException;
	CustomList<CustomerBalanceReportViewModel> searchDailyCustomerBalanceReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException; 
}
