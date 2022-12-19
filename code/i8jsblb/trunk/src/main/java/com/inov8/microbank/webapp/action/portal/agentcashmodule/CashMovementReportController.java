package com.inov8.microbank.webapp.action.portal.agentcashmodule;

import static com.inov8.microbank.common.util.PortalDateUtils.FORMAT_DAY_MONTH_YEAR_COMPLETE;

import java.util.Date;
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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.portal.agentcashmodule.AgentCashViewModel;
import com.inov8.microbank.common.model.portal.agentcashmodule.ExtendedAgentCashViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.CashBankReconciliationReportConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.facade.TillBalanceFacade;
import com.inov8.microbank.server.facade.portal.agentcashmodule.AgentCashFacade;

public class CashMovementReportController extends BaseFormSearchController
{
	private AgentCashFacade agentCashFacade;
	//Autowired
	private TillBalanceFacade tillBalanceFacade;
	//Autowired
	private RetailerFacade retailerFacade;

	public CashMovementReportController()
	{
		 super.setCommandName("extendedAgentCashViewModel");
		 super.setCommandClass(ExtendedAgentCashViewModel.class);
	}
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception{
	    return null;
	}

    @SuppressWarnings( "unchecked" )
    private void loadReportHeaderInfo( HttpServletRequest request, ExtendedAgentCashViewModel extendedAgentCashViewModel, List<AgentCashViewModel> agentCashViewModels ) throws Exception
	{
        Map<String, String> reportHeaderMap = new LinkedHashMap<String, String>();
        String distributorName = null;
        String retailerName = null;
        String regionAndArea = null;
        String userNameAndId = null;
        String dateRangeFormatted = null;

        //Load Agent Account Number, Opening Balance etc
	    AgentOpeningBalModel agentOpeningBalModel = new AgentOpeningBalModel();
	    agentOpeningBalModel.setAgentId( extendedAgentCashViewModel.getAgentId() );
	    agentOpeningBalModel.setMsisdn( extendedAgentCashViewModel.getMsisdn() );

	    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    searchBaseWrapper.setBasePersistableModel( agentOpeningBalModel );

	    LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>( 1 );
	    sortingOrderMap.put( "createdOn", SortingOrder.ASC );
	    searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

	    DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedAgentCashViewModel.getStartDate(), extendedAgentCashViewModel.getEndDate() );
	    searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
	    searchBaseWrapper = tillBalanceFacade.loadAgentOpeningBalance( searchBaseWrapper );
	    CustomList<AgentOpeningBalModel> agentOpeningBalCustomList = searchBaseWrapper.getCustomList();

	    String accountNumber = null;
	    Double openingAccountBalance = 0.0;
	    Double dayEndClosingBalance = 0.0;
	    if( null != agentOpeningBalCustomList )
	    {
	        List<AgentOpeningBalModel> agentOpeningBalModels = agentOpeningBalCustomList.getResultsetList();
	        if( agentOpeningBalModels != null && !agentOpeningBalModels.isEmpty() )
	        {
	            AgentOpeningBalModel agentOpeningBalance = agentOpeningBalModels.get( 0 );
	            accountNumber = agentOpeningBalance.getAccountNumber();
	            openingAccountBalance = CommonUtils.getDoubleOrDefaultValue( agentOpeningBalance.getOpeningAccountBalance() );

	            dayEndClosingBalance = CommonUtils.getDoubleOrDefaultValue( agentOpeningBalModels.get( agentOpeningBalModels.size() - 1 ).getRunningAccountBalance() );//last row
	        }
	    }
	    request.setAttribute( CashBankReconciliationReportConstants.KEY_BALANCE_OPENING_BANK_DAY_START, openingAccountBalance );
	    request.setAttribute( CashBankReconciliationReportConstants.KEY_BALANCE_CLOSING_BANK_DAY_END, dayEndClosingBalance );

	    //Load Retailer info
	    SearchBaseWrapper retailerSearchWrapper = new SearchBaseWrapperImpl();

        RetailerContactListViewModel retailerContactListViewModel = new RetailerContactListViewModel();
        retailerContactListViewModel.setAllpayId( extendedAgentCashViewModel.getAgentId() );
        retailerContactListViewModel.setMobileNo( extendedAgentCashViewModel.getMsisdn() );

        retailerSearchWrapper.setBasePersistableModel( retailerContactListViewModel );

        retailerSearchWrapper = this.retailerFacade.findExactRetailerContactListViewModel(retailerSearchWrapper);
        CustomList<RetailerContactListViewModel> retailerContactCustomList = retailerSearchWrapper.getCustomList();
        if( retailerContactCustomList != null )
        {
            List<RetailerContactListViewModel> retailerContacts = retailerContactCustomList.getResultsetList();
            if( retailerContacts != null && !retailerContacts.isEmpty() )
            {
                RetailerContactListViewModel retailerContact = retailerContacts.get( 0 );
                distributorName = retailerContact.getDistributorName();
                retailerName = retailerContact.getRetailerName();

                String region = retailerContact.getRegionName();
                region = region == null ? region : region.trim();
                String area = retailerContact.getAreaName();
                area = area == null ? area : area.trim();
                regionAndArea = region + "(" + area + ")";
                userNameAndId = retailerContact.getUsername() + "(" + retailerContact.getAllpayId() + ")";
            }
        }

        if( dateRangeHolderModel != null )
        {
            Date fromDate = dateRangeHolderModel.getFromDate();
            Date toDate = dateRangeHolderModel.getToDate();
            if( fromDate != null && toDate != null )
            {
                dateRangeFormatted = PortalDateUtils.formatDate( dateRangeHolderModel.getFromDate(), FORMAT_DAY_MONTH_YEAR_COMPLETE ) + " to " + PortalDateUtils.formatDate( dateRangeHolderModel.getToDate(), FORMAT_DAY_MONTH_YEAR_COMPLETE );
            }
        }
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_AGENT_NETWORK, distributorName );
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_REPORT_TITLE, "Physical Cash and Bank A/C Movement Report" );
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_ACCOUNT_NUMBER, accountNumber ) ;
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_FRANCHISE_NAME, retailerName );
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_REGION_AND_AREA, regionAndArea );
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_USER_NAME_AND_ID, userNameAndId );
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_DATE_RANGE, dateRangeFormatted );
        reportHeaderMap.put( CashBankReconciliationReportConstants.LABEL_REPORT_DATE, PortalDateUtils.currentFormattedDate( FORMAT_DAY_MONTH_YEAR_COMPLETE ) ); 
        request.setAttribute( CashBankReconciliationReportConstants.KEY_REPORT_HEADER_MAP, reportHeaderMap );

        Double bankBalance = 0.0;
        Double cashBalance = 0.0;

        if( agentCashViewModels != null && !agentCashViewModels.isEmpty() )
        {
            AgentCashViewModel lastAgentCashViewModel = agentCashViewModels.get( agentCashViewModels.size() - 1 );

            bankBalance = CommonUtils.getDoubleOrDefaultValue( lastAgentCashViewModel.getBankBalance() );
            cashBalance = CommonUtils.getDoubleOrDefaultValue( lastAgentCashViewModel.getCashBalance() );
        }
        request.setAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_BANK_BALANCE, bankBalance );
        request.setAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_BALANCE, cashBalance );
        request.setAttribute( CashBankReconciliationReportConstants.KEY_TOTAL_CASH_AND_BANK, bankBalance + cashBalance );
	}

	@SuppressWarnings( "unchecked" )
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		ExtendedAgentCashViewModel extendedAgentCashViewModel  = (ExtendedAgentCashViewModel) model;
		AgentCashViewModel agentCashViewModel = (AgentCashViewModel) extendedAgentCashViewModel;
		//If both Agent ID and MSISDN are missing then return 0 records
		if(StringUtil.isNullOrEmpty(agentCashViewModel.getAgentId())
				&& StringUtil.isNullOrEmpty(agentCashViewModel.getMsisdn())){
			
			pagingHelperModel.setTotalRecordsCount(0);
			return new ModelAndView(getSuccessView(),"agentCashViewModelList",null);
		
		}
		
		searchBaseWrapper.setBasePersistableModel( (AgentCashViewModel) extendedAgentCashViewModel );
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "transactionDate", 
											extendedAgentCashViewModel.getStartDate(),
											extendedAgentCashViewModel.getEndDate());

		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
		if( sortingOrderMap.isEmpty() ){
			sortingOrderMap.put("transactionDateOnly", SortingOrder.ASC);
			sortingOrderMap.put("createdOn", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
		searchBaseWrapper = this.agentCashFacade.searchAgentCashMovement(searchBaseWrapper);
		List<AgentCashViewModel> agentCashViewModels = searchBaseWrapper.getCustomList().getResultsetList();
		loadReportHeaderInfo( httpServletRequest, extendedAgentCashViewModel, agentCashViewModels );

		return new ModelAndView( getSuccessView(), "agentCashViewModelList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setAgentCashFacade( AgentCashFacade agentCashFacade){
		this.agentCashFacade = agentCashFacade;
	}

	public void setTillBalanceFacade( TillBalanceFacade tillBalanceFacade )
    {
        this.tillBalanceFacade = tillBalanceFacade;
    }

	public void setRetailerFacade(RetailerFacade retailerFacade)
	{
		this.retailerFacade = retailerFacade;
	}

}
