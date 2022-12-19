package com.inov8.microbank.server.dao.portal.ola;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.ola.CustomerBbStatementViewModel;

import java.util.List;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:37:37 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface CustomerBbStatementViewDao extends BaseDAO<CustomerBbStatementViewModel, Long>
{

    public List<CustomerBbStatementViewModel> searchBBStatementViewByPaymentModeId(CustomerBbStatementViewModel customerBbStatementViewModel, SearchBaseWrapper searchBaseWrapper);
}
