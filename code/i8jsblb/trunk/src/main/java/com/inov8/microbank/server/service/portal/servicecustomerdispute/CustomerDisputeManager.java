package com.inov8.microbank.server.service.portal.servicecustomerdispute;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface CustomerDisputeManager
{
	SearchBaseWrapper searchCustomerDisputeTransactionForBillPayment(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper createIssue(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
	
	BaseWrapper searchMNOSupplier(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
	
    BaseWrapper isValidMfsId(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
	
}
