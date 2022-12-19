package com.inov8.microbank.server.service.portal.transactiondetaili8module.allpay;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface AllPayTransactionDetailI8Manager
{
	SearchBaseWrapper searchTransactionDetailForI8(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
}
