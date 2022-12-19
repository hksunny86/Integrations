package com.inov8.microbank.server.service.portal.productcustomerdispute;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface CustomerDisputeProductManager
{
	SearchBaseWrapper searchCustomerDisputeTransactionForVariableDiscrete(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper createIssue(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
	
	BaseWrapper searchMNOSupplier(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

}
