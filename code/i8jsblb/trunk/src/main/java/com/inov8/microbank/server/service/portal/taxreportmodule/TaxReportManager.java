package com.inov8.microbank.server.service.portal.taxreportmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.reportmodule.WHTStakeholderViewModel;
import com.inov8.microbank.common.model.tax.FedDetailedReportModel;
import com.inov8.microbank.common.model.tax.WhtDetailedReportModel;

import java.util.List;

public interface TaxReportManager {
    
	public SearchBaseWrapper searchAgentWHTReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchAgentCustWHTReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException;

	public abstract SearchBaseWrapper searchTransactionwiseWHTReportView(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException;
	public CustomList<WHTStakeholderViewModel> searchWHTStakeholderView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

	public SearchBaseWrapper searchFedBreakupDetailReportView(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException;
    
	public SearchBaseWrapper searchCashTransReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	public List<FedDetailedReportModel> getFedDetails(SearchBaseWrapper searchBaseWrapper);
	public List<WhtDetailedReportModel> getWhtReportDetails(SearchBaseWrapper searchBaseWrapper);
	public SearchBaseWrapper findTargetedWhtDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

}
