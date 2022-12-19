package com.inov8.microbank.server.facade.portal.taxreportmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.taxreportmodule.TaxReportManager;


public interface TaxReportFacade extends TaxReportManager {

	public abstract SearchBaseWrapper searchTransactionwiseWHTReportView(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchFedBreakupDetailReportView(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException;

}
