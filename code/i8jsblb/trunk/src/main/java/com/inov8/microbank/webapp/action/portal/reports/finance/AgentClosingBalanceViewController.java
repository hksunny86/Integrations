package com.inov8.microbank.webapp.action.portal.reports.finance;

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
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.financereportsmodule.AgentClosingBalanceViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.ExtendedAgentClosingBalanceViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;

public class AgentClosingBalanceViewController extends BaseFormSearchController
{
    //Autowired
    private FinanceReportsFacacde financeReportsFacacde;

	public AgentClosingBalanceViewController()
	{
		 super.setCommandName("extendedAgentClosingBalanceViewModel");
		 super.setCommandClass( ExtendedAgentClosingBalanceViewModel.class );
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	@Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>( );
	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

        ExtendedAgentClosingBalanceViewModel extendedAgentClosingBalanceView = (ExtendedAgentClosingBalanceViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "statsDate",
		    extendedAgentClosingBalanceView.getStartDate(), extendedAgentClosingBalanceView.getEndDate() );

		searchBaseWrapper.setBasePersistableModel( (AgentClosingBalanceViewModel) extendedAgentClosingBalanceView );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        //sorting order 
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put( "statsDate", SortingOrder.DESC );
		}
		
		if(sortingOrderMap.containsKey("endDayBalance")) {

			SortingOrder sortOrder = (SortingOrder) sortingOrderMap.get("endDayBalance");
			sortingOrderMap.remove("endDayBalance");

			sortingOrderMap.put( "statsDate", SortingOrder.DESC );
			sortingOrderMap.put( "endDayBalance", sortOrder );
		
		} else if(sortingOrderMap.containsKey("startDayBalance")) {

			SortingOrder sortOrder = (SortingOrder) sortingOrderMap.get("startDayBalance");
			sortingOrderMap.remove("startDayBalance");

			sortingOrderMap.put( "statsDate", SortingOrder.DESC );
			sortingOrderMap.put( "startDayBalance", sortOrder );
		}
		
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		searchBaseWrapper = financeReportsFacacde.searchAgentClosingBalanceView( searchBaseWrapper );
		List<AgentClosingBalanceViewModel> list = null;
		if( searchBaseWrapper.getCustomList() != null )
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView( getSuccessView(), "agentClosingBalanceViewModelList", list );
	}

	public void setFinanceReportsFacacde( FinanceReportsFacacde financeReportsFacacde )
    {
        this.financeReportsFacacde = financeReportsFacacde;
    }

}
