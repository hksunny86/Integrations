package com.inov8.microbank.webapp.action.portal.ola;

import static com.inov8.microbank.common.util.PortalDateUtils.FORMAT_DAY_MONTH_YEAR_COMPLETE;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;
import com.inov8.microbank.common.util.BBStatementReportConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.bankmodule.BankManager;
import com.inov8.ola.server.facade.AccountFacade;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class SettlementBbStatementViewController extends BaseFormSearchController
{
    //Autowired
    private PortalOlaFacade portalOlaFacade;
    //Autowired
    private AccountFacade accountFacade;
    //Autowired
    private BankManager bankManager;

    public SettlementBbStatementViewController()
    {
        setCommandClass( SettlementBbStatementViewModel.class );
        setCommandName( "settlementBbStatementViewModel" );
    }

    @Override
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    {
        super.initBinder(request, binder);
        CommonUtils.bindCustomDateEditor(binder);
    }

    @Override
    protected Object formBackingObject( HttpServletRequest request ) throws Exception
    {
		String accountId = ServletRequestUtils.getStringParameter(request, "accountId");
		String accountNumber = ServletRequestUtils.getStringParameter(request, "accountNumber");
		String accountTittle = ServletRequestUtils.getStringParameter(request, "accountTitle");
		Long accId = 0L;
		if (null != accountId && accountId.trim().length() > 0){
			accId = Long.parseLong(accountId);
	    }
		
    	SettlementBbStatementViewModel settlementBbStatementViewModel = new SettlementBbStatementViewModel( new DateRangeHolderModel() );
    	settlementBbStatementViewModel.setAccountId(accId);
    	settlementBbStatementViewModel.setAccountNumber(accountNumber);
    	settlementBbStatementViewModel.setAccountTittle(accountTittle);
        return settlementBbStatementViewModel;
    }

    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        return null;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        Map<String, Object> modelMap = new HashMap<>( 3 ); 
        List<SettlementBbStatementViewModel> settlementBbStatementViewModelList = null;

       
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SettlementBbStatementViewModel settlementBbStatementViewModel = (SettlementBbStatementViewModel) model;
            searchBaseWrapper.setBasePersistableModel( settlementBbStatementViewModel );

            DateRangeHolderModel dateRangeHolderModel = settlementBbStatementViewModel.getDateRangeHolderModel();
            dateRangeHolderModel.setDatePropertyName( "transactionTime" );
            searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

            if( sortingOrderMap.isEmpty() )
            {
                sortingOrderMap.put( "transactionTime", SortingOrder.ASC );
            }
            searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

            searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
        try
        {
            searchBaseWrapper = portalOlaFacade.searchSettlementBbStatementView( searchBaseWrapper );
            pagingHelperModel.setTotalRecordsCount(searchBaseWrapper.getCustomList().getResultsetList().size());

            CustomList<SettlementBbStatementViewModel> customList = searchBaseWrapper.getCustomList();
            if( customList != null )
            {
                settlementBbStatementViewModelList = customList.getResultsetList();
                if( null != settlementBbStatementViewModelList && !settlementBbStatementViewModelList.isEmpty() )
                {
                    if( pagingHelperModel.isFirstPage() || pagingHelperModel.getPageNo() == null )
                    {
                        SettlementBbStatementViewModel firstBbStatementViewModel = settlementBbStatementViewModelList.get( 0 );
                        double openingBalance = calculateOpeningBalance( firstBbStatementViewModel );
                        
                        /// Balance set to be used in BB Statement Export
                        request.setAttribute(BBStatementReportConstants.openingBalance, openingBalance);
                    }

                    if( pagingHelperModel.isLastPage() || pagingHelperModel.getPageNo() == null )
                    {
                        SettlementBbStatementViewModel lastBbStatementViewModel = settlementBbStatementViewModelList.get( settlementBbStatementViewModelList.size() - 1 );
                      /// Balance set to be used in BB Statement Export
                        request.setAttribute(BBStatementReportConstants.closingBalance, lastBbStatementViewModel.getBalanceAfterTransaction());
                    
                    }
                }
            }
            
            if(customList == null || customList.getResultsetList().size() == 0) {

            	Double openingClosingBalance = portalOlaFacade.getBalanceByDate(dateRangeHolderModel.getFromDate(), settlementBbStatementViewModel.getAccountId());
            	
                request.setAttribute(BBStatementReportConstants.openingBalance, openingClosingBalance);
                request.setAttribute(BBStatementReportConstants.closingBalance, openingClosingBalance);
            }
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        loadReportHeaderInfo(request,settlementBbStatementViewModel,dateRangeHolderModel);
        modelMap.put( "settlementBbStatementViewModelList", settlementBbStatementViewModelList );
        return new ModelAndView( getFormView(), modelMap );
    }
    @SuppressWarnings( "unchecked" )
    private void loadReportHeaderInfo(HttpServletRequest request,SettlementBbStatementViewModel settlementBbStatementViewModel, DateRangeHolderModel dateRangeHolderModel) throws Exception
	{ 	
	 	Map<String, String> reportHeaderMap = new LinkedHashMap<String, String>();
	 			 	
        String issueDate = null;
        String statementDateRange = null;
        String branchName1 = null;
        String branchName2 = null;   
        String currency=BBStatementReportConstants.currency;

        if( dateRangeHolderModel != null )
        {
            Date fromDate = dateRangeHolderModel.getFromDate();
            Date toDate = dateRangeHolderModel.getToDate();
            if( fromDate != null && toDate != null )
            {
            	statementDateRange = PortalDateUtils.formatDate( fromDate, FORMAT_DAY_MONTH_YEAR_COMPLETE ) + " to " + PortalDateUtils.formatDate( toDate, FORMAT_DAY_MONTH_YEAR_COMPLETE);
            }
        }
                
        Date currentDate= new Date();
        issueDate= PortalDateUtils.formatDate(currentDate, FORMAT_DAY_MONTH_YEAR_COMPLETE);
        
        //Load Bank
        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setPrimaryKey(CommissionConstantsInterface.BANK_ID);
        baseWrapperBank.setBasePersistableModel(bankModel);
        baseWrapperBank = this.bankManager.loadBank(baseWrapperBank);
        bankModel= (BankModel) baseWrapperBank.getBasePersistableModel();
        
        if(bankModel!=null){
        	 
        	if(bankModel.getAddress1()==null)
        		branchName1="";
        	else 
        		branchName1=bankModel.getAddress1();
        	
        	if(bankModel.getAddress2()==null)
        		branchName2="";
        	else 
        		branchName2=bankModel.getAddress2();
        	
        	if(bankModel.getCity()!=null)
        		 branchName2=branchName2.concat(","+bankModel.getCity());
        }
       	             
        reportHeaderMap.put(BBStatementReportConstants.issueDateKey,issueDate);
        reportHeaderMap.put(BBStatementReportConstants.statementDateRangeKey,statementDateRange );
        reportHeaderMap.put(BBStatementReportConstants.branchNameKey1,branchName1) ;
        reportHeaderMap.put(BBStatementReportConstants.branchNameKey2,branchName2) ;
        reportHeaderMap.put(BBStatementReportConstants.settlementAccTitle,settlementBbStatementViewModel.getAccountTittle()) ;
        reportHeaderMap.put(BBStatementReportConstants.settlementAccNumber,settlementBbStatementViewModel.getAccountNumber()) ;
        reportHeaderMap.put(BBStatementReportConstants.currencyKey,currency); 
        
        request.setAttribute( BBStatementReportConstants.mapkey, reportHeaderMap );
	}

    private double calculateOpeningBalance( SettlementBbStatementViewModel bbStatementViewModel )
    {
        double openingBalance = 0.0;
        if( bbStatementViewModel.getDebitAmount() != null &&  bbStatementViewModel.getDebitAmount() > 0 )
        {
            openingBalance = bbStatementViewModel.getBalanceAfterTransaction().doubleValue() + bbStatementViewModel.getDebitAmount().doubleValue();
        }
        else if ( bbStatementViewModel.getCreditAmount() != null &&  bbStatementViewModel.getCreditAmount() > 0)
        {
            openingBalance = bbStatementViewModel.getBalanceAfterTransaction().doubleValue() - bbStatementViewModel.getCreditAmount().doubleValue();
        }
        return openingBalance;
    }

    public void setPortalOlaFacade( PortalOlaFacade portalOlaFacade )
    {
        this.portalOlaFacade = portalOlaFacade;
    }

    public void setAccountFacade( AccountFacade accountFacade )
    {
        this.accountFacade = accountFacade;
    }

	public void setBankManager(BankManager bankManager) {
		this.bankManager = bankManager;
	}

}
