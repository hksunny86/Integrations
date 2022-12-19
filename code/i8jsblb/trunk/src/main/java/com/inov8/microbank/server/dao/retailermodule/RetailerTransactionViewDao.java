package com.inov8.microbank.server.dao.retailermodule;


import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.RetailerTransactionViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 30, 2013 4:10:13 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface RetailerTransactionViewDao extends BaseDAO<RetailerTransactionViewModel, Long>
{
    SearchBaseWrapper fetchRegionalRetailActivitySummary( SearchBaseWrapper searchBaseWrapper ) throws DataAccessException;
}
