package com.inov8.microbank.server.dao.salesmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.salesmodule.SalesTeamComissionViewModel;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface SalesTeamCommissionDAO extends BaseDAO<SalesTeamComissionViewModel, Long>
{
	SalesTeamComissionViewModel loadSalesTeamComissionView(String transactionCode);

	CustomList<SalesTeamComissionViewModel> loadSalesTeamCommissionViewByCriteria(String transactionCode);
}
