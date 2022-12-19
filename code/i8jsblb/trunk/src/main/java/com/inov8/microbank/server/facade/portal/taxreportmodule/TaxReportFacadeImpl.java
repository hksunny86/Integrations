package com.inov8.microbank.server.facade.portal.taxreportmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.reportmodule.WHTStakeholderViewModel;
import com.inov8.microbank.common.model.tax.FedDetailedReportModel;
import com.inov8.microbank.common.model.tax.WhtDetailedReportModel;
import com.inov8.microbank.server.service.portal.taxreportmodule.TaxReportManager;

import java.util.List;

public class TaxReportFacadeImpl implements TaxReportFacade{

    private FrameworkExceptionTranslator frameworkExceptionTranslator;
    private TaxReportManager taxReportManager;

	@Override
	public SearchBaseWrapper searchAgentWHTReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return taxReportManager.searchAgentWHTReportView(wrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	
	@Override
	public SearchBaseWrapper searchAgentCustWHTReportView(
			SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try{
			return taxReportManager.searchAgentCustWHTReportView(wrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
		
	}

	@Override
	public SearchBaseWrapper searchTransactionwiseWHTReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return taxReportManager.searchTransactionwiseWHTReportView(wrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public CustomList<WHTStakeholderViewModel> searchWHTStakeholderView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException {
		
		try {
			
			return taxReportManager.searchWHTStakeholderView( searchBaseWrapper );
		
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}	
	
	@Override
	public SearchBaseWrapper searchFedBreakupDetailReportView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return taxReportManager.searchFedBreakupDetailReportView(searchBaseWrapper);
		} catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	public void setTaxReportManager(TaxReportManager taxReportManager) {
		this.taxReportManager = taxReportManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	@Override
	public SearchBaseWrapper searchCashTransReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return taxReportManager.searchCashTransReportView(wrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	@Override
	public List<FedDetailedReportModel> getFedDetails(SearchBaseWrapper searchBaseWrapper) {
			return taxReportManager.getFedDetails(searchBaseWrapper);
	}
	@Override
	public List<WhtDetailedReportModel> getWhtReportDetails(SearchBaseWrapper searchBaseWrapper) {
		return taxReportManager.getWhtReportDetails(searchBaseWrapper);
	}


	@Override
	public SearchBaseWrapper findTargetedWhtDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try{
			return taxReportManager.findTargetedWhtDetail(searchBaseWrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	

}
