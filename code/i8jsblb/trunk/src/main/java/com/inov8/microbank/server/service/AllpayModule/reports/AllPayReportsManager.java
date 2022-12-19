package com.inov8.microbank.server.service.AllpayModule.reports;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface AllPayReportsManager {
	SearchBaseWrapper searchRetailerTransactions(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	SearchBaseWrapper searchRetailerBillSummaries(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	SearchBaseWrapper searchRetailerHeadTransactions(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	SearchBaseWrapper searchDistributorHeadTransactions(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	
	//---second version of reports
	SearchBaseWrapper searchDistHeadSummary(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	SearchBaseWrapper searchRegionalHeadSummary(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	SearchBaseWrapper searchRetSummary(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
}
