package com.inov8.microbank.server.dao.portal.ola;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:37:37 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface SettlementBbStatementViewDao extends BaseDAO<SettlementBbStatementViewModel, Long>
{
	public String getBalanceByDate(Date calendar, Long accountId) throws Exception;
	CustomList<SettlementBbStatementViewModel> searchSettlementbbStatement(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

}