package com.inov8.microbank.server.service.portal.taxreportmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.reportmodule.*;
import com.inov8.microbank.common.model.tax.FedDetailedReportModel;
import com.inov8.microbank.common.model.tax.TargetedWhtDetailReportModel;
import com.inov8.microbank.common.model.tax.WhtDetailedReportModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.model.portal.reportmodule.CashTransReportViewModel;
import com.inov8.microbank.common.model.portal.reportmodule.CommissionTransactionWHListViewModel;
import com.inov8.microbank.common.model.portal.reportmodule.FedBreakupDetailListViewModel;
import com.inov8.microbank.common.model.portal.reportmodule.WHTAgentReportViewModel;
import com.inov8.microbank.common.model.portal.reportmodule.WHTStakeholderViewModel;
import com.inov8.microbank.common.model.tax.WHTSummaryViewModel;
import com.inov8.microbank.server.dao.portal.reports.finance.CashTransReportViewDAO;
import com.inov8.microbank.server.dao.portal.reports.tax.AgentCustWHTSummaryDAO;
import com.inov8.microbank.server.dao.portal.reports.tax.CommissionTransactionWhtViewDAO;
import com.inov8.microbank.server.dao.portal.reports.tax.FedBreakupDetailListViewDAO;
import com.inov8.microbank.server.dao.portal.reports.tax.WHTAgentReportViewDAO;
import com.inov8.microbank.server.dao.portal.reports.tax.WHTStakeholderViewDao;
import com.inov8.microbank.tax.dao.FedDetailedReportDAO;
import com.inov8.microbank.tax.dao.TargetedWhtDetailViewDAO;
import com.inov8.microbank.tax.dao.WhtDetailedReportDAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class TaxReportManagerImpl implements TaxReportManager {

    private WHTAgentReportViewDAO wHTAgentReportViewDAO;
    private WHTStakeholderViewDao whtStakeholderViewDao;
    private FedBreakupDetailListViewDAO fedBreakupDetailListViewDAO;
    private CashTransReportViewDAO cashTransReportViewDAO;
    private AgentCustWHTSummaryDAO agentCustWHTSummaryDAO;
	private FedDetailedReportDAO fedDetailedReportDAO;
	private WhtDetailedReportDAO whtDetailedReportDAO;
	@Autowired
	private TargetedWhtDetailViewDAO targetedWhtDetailViewDAO;

    private CommissionTransactionWhtViewDAO commissionTransactionWhtViewDAO;

    @Override
    public SearchBaseWrapper searchAgentWHTReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        WHTAgentReportViewModel model = (WHTAgentReportViewModel) wrapper.getBasePersistableModel();
        List<WHTAgentReportViewModel> list = wHTAgentReportViewDAO.loadAgentWHTReport(model, 
        										wrapper.getPagingHelperModel(),
        										wrapper.getSortingOrderMap(),
        										wrapper.getDateRangeHolderModel());
        
        CustomList<WHTAgentReportViewModel> customList = new CustomList<WHTAgentReportViewModel>();
        customList.setResultsetList(list);
        wrapper.setCustomList( customList );
        return wrapper;
    }
    

    @Override
    public SearchBaseWrapper searchAgentCustWHTReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException{

    	WHTSummaryViewModel model = (WHTSummaryViewModel) wrapper.getBasePersistableModel();

        List<WHTSummaryViewModel> list = agentCustWHTSummaryDAO.loadAgentCustWHTReport(model,
				wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(),
				wrapper.getDateRangeHolderModel());

		CustomList<WHTSummaryViewModel> customList = new CustomList<WHTSummaryViewModel>();
		customList.setResultsetList(list);
		wrapper.setCustomList( customList );
		return wrapper;
    }

    @Override
	@SuppressWarnings("unchecked")
	public SearchBaseWrapper searchTransactionwiseWHTReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        CommissionTransactionWHListViewModel model = (CommissionTransactionWHListViewModel) wrapper.getBasePersistableModel();
        CustomList list =  commissionTransactionWhtViewDAO.findByExample(model, 
        										wrapper.getPagingHelperModel(),
        										wrapper.getSortingOrderMap(),
        										wrapper.getDateRangeHolderModel());
        
        wrapper.setCustomList( list );
        return wrapper;
    }

    
    @Override
	public synchronized CustomList<WHTStakeholderViewModel> searchWHTStakeholderView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException {
			
		WHTStakeholderViewModel model = (WHTStakeholderViewModel) wrapper.getBasePersistableModel();
			
		CustomList<WHTStakeholderViewModel> customList = null;
			
		try {
				
			customList = whtStakeholderViewDao.searchWHTStakeholderView( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return customList;
	}
    
	@Override
	public SearchBaseWrapper searchFedBreakupDetailReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException
	{
		FedBreakupDetailListViewModel model = (FedBreakupDetailListViewModel) wrapper.getBasePersistableModel();
		CustomList<FedBreakupDetailListViewModel> list = fedBreakupDetailListViewDAO.findByExample(model, wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel());
		wrapper.setCustomList(list);
		return wrapper;
	}
    
    @Override
    public SearchBaseWrapper searchCashTransReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
    	CashTransReportViewModel model = (CashTransReportViewModel) wrapper.getBasePersistableModel();
    	CustomList<CashTransReportViewModel> list = cashTransReportViewDAO.findByExample(model, 
    				wrapper.getPagingHelperModel(),
    				wrapper.getSortingOrderMap(),
    				wrapper.getDateRangeHolderModel());
    	
        wrapper.setCustomList( list );
        return wrapper;
    }

	@Override
	public List<FedDetailedReportModel> getFedDetails(SearchBaseWrapper searchBaseWrapper){

		return fedDetailedReportDAO.getFilteredData(searchBaseWrapper);
	}
	@Override
	public List<WhtDetailedReportModel> getWhtReportDetails(SearchBaseWrapper searchBaseWrapper){

		return whtDetailedReportDAO.getFilteredWhtData(searchBaseWrapper);
	}

	@Override
	public SearchBaseWrapper findTargetedWhtDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		TargetedWhtDetailReportModel model = (TargetedWhtDetailReportModel) searchBaseWrapper.getBasePersistableModel();
    	List<TargetedWhtDetailReportModel> list = targetedWhtDetailViewDAO.loadTargetedWHTDetail(model, 
    			searchBaseWrapper.getPagingHelperModel(),
    			searchBaseWrapper.getSortingOrderMap(),
    			searchBaseWrapper.getDateRangeHolderModel());
    	
    	CustomList<TargetedWhtDetailReportModel> customList = new CustomList<TargetedWhtDetailReportModel>();
    	if(list !=null){
    		customList.setResultsetList(list);
    		searchBaseWrapper.setCustomList( customList );
    	}else{
    		searchBaseWrapper.setCustomList(null);
    	}
		
    	
    	
        return searchBaseWrapper;
	}

    
	public void setwHTAgentReportViewDAO(WHTAgentReportViewDAO wHTAgentReportViewDAO) {
		this.wHTAgentReportViewDAO = wHTAgentReportViewDAO;
	}
	public void setWhtStakeholderViewDao(WHTStakeholderViewDao whtStakeholderViewDao) {
		this.whtStakeholderViewDao = whtStakeholderViewDao;
	}	
	public void setCommissionTransactionWhtViewDAO(
			CommissionTransactionWhtViewDAO commissionTransactionWhtViewDAO) {
		this.commissionTransactionWhtViewDAO = commissionTransactionWhtViewDAO;
	}

	public void setFedBreakupDetailListViewDAO(FedBreakupDetailListViewDAO fedBreakupDetailListViewDAO)
	{
		this.fedBreakupDetailListViewDAO = fedBreakupDetailListViewDAO;
	}

	public void setCashTransReportViewDAO(CashTransReportViewDAO cashTransReportViewDAO) {
		this.cashTransReportViewDAO = cashTransReportViewDAO;
	}


	public void setAgentCustWHTSummaryDAO(
			AgentCustWHTSummaryDAO agentCustWHTSummaryDAO) {
		this.agentCustWHTSummaryDAO = agentCustWHTSummaryDAO;
	}




	public void setFedDetailedReportDAO(FedDetailedReportDAO fedDetailedReportDAO) {
		this.fedDetailedReportDAO = fedDetailedReportDAO;
	}

	public void setWhtDetailedReportDAO(WhtDetailedReportDAO whtDetailedReportDAO) {
		this.whtDetailedReportDAO = whtDetailedReportDAO;
	}


	public void setTargetedWhtDetailViewDAO(
			TargetedWhtDetailViewDAO targetedWhtDetailViewDAO) {
		this.targetedWhtDetailViewDAO = targetedWhtDetailViewDAO;
	}
}
