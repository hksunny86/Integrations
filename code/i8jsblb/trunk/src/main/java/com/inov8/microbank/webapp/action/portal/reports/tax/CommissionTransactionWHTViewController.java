package com.inov8.microbank.webapp.action.portal.reports.tax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.reportmodule.CommissionTransactionWHListViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.facade.portal.taxreportmodule.TaxReportFacade;

public class CommissionTransactionWHTViewController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TaxReportFacade taxReportFacade;

	public CommissionTransactionWHTViewController()
	{
		 super.setCommandName("commissionTransactionWHListViewModel");
		 super.setCommandClass(CommissionTransactionWHListViewModel.class);
	}
	
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	
	@SuppressWarnings( "unchecked" )
    protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		
	    return referenceDataMap;
	}

    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
	{
    	CommissionTransactionWHListViewModel commissionTransactionWHListViewModel = (CommissionTransactionWHListViewModel) model;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<CommissionTransactionWHListViewModel> list = new ArrayList<CommissionTransactionWHListViewModel>();
		
        
        searchBaseWrapper.setBasePersistableModel( commissionTransactionWHListViewModel );
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "transactionDate",
				commissionTransactionWHListViewModel.getStartDate(), commissionTransactionWHListViewModel.getEndDate() );
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		//sorting order 
		if(sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("transactionDate", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper = taxReportFacade.searchTransactionwiseWHTReportView( searchBaseWrapper );

		if(searchBaseWrapper.getCustomList() != null )
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		else
			searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
		
		String successView = StringUtil.trimExtension( request.getServletPath() );
		return new ModelAndView( "reports/tax/"+successView, "commissionTransactionWHListViewModelList", list );
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setTaxReportFacade(TaxReportFacade taxReportFacade) {
		this.taxReportFacade = taxReportFacade;
	}


	
}
