package com.inov8.microbank.server.dao.portal.transactiondetail;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionCashReversalViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 27, 2012 1:51:41 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface TransactionCashReversalViewDao extends BaseDAO<TransactionCashReversalViewModel, Long>
{
	List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException;
}
