package com.inov8.microbank.server.service.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Apr 26, 2013 3:37:57 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface CommissionTransactionViewManager
{
    List<CommissionTransactionViewModel> searchCommissionTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
	List<CommissionTransactionViewModel> searchCommissionTransactionView(CommissionTransactionViewModel commissionTransactionViewModel) throws FrameworkCheckedException;
}
