package com.inov8.microbank.webapp.action.allpaymodule.reports;

import static com.inov8.microbank.common.util.PortalDateUtils.FORMAT_DAY_MONTH_YEAR_COMPLETE;

import java.util.ArrayList;
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
import com.inov8.microbank.common.model.portal.ola.AgentBBStatementViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.AddressTypeConstants;
import com.inov8.microbank.common.util.BBStatementReportConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.bankmodule.BankManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.util.EncryptionUtil;

public class AgentBBStatementSearchController extends BaseFormSearchController
{
    //Autowired
    private PortalOlaFacade portalOlaFacade;
    private AllpayRetailerAccountManager allpayRetailerAccountManager;
    private RetailerContactManager retailerContactManager;
    private AppUserManager appUserManager;
    private BankManager bankManager;

    public AgentBBStatementSearchController()
    {
        setCommandName("agentBBStatementViewModel");
        setCommandClass(AgentBBStatementViewModel.class);
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
        AgentBBStatementViewModel agentBBStatementViewModel = new AgentBBStatementViewModel( new DateRangeHolderModel() );
        return agentBBStatementViewModel;
    }

    @Override
    protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
    {
        return null;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        Map<String, Object> modelMap = new HashMap<>( 3 );
        List<AgentBBStatementViewModel> agentBBStatementViewModelList = null;

        AgentBBStatementViewModel agentBBStatementViewModel = (AgentBBStatementViewModel) model;

        String appUserIdEncrypted = ServletRequestUtils.getRequiredStringParameter(request, "appUserId");
        String appUserIdDecrypted = EncryptionUtil.decryptWithDES( appUserIdEncrypted );
        Long appUserId = null;
        if(null != appUserIdDecrypted && !"".equals(appUserIdDecrypted)){
            appUserId = Long.valueOf( appUserIdDecrypted );
            agentBBStatementViewModel.setAppUserId( appUserId );
        }


        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel( agentBBStatementViewModel );

        DateRangeHolderModel dateRangeHolderModel = agentBBStatementViewModel.getDateRangeHolderModel();
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
            searchBaseWrapper = portalOlaFacade.searchAgentBBStatementView(searchBaseWrapper);

            CustomList<AgentBBStatementViewModel> customList = searchBaseWrapper.getCustomList();
            if( customList != null )
            {
                agentBBStatementViewModelList = customList.getResultsetList();
                if( null != agentBBStatementViewModelList && !agentBBStatementViewModelList.isEmpty() )
                {
                    if( pagingHelperModel.isFirstPage() || pagingHelperModel.getPageNo() == null )
                    {
                        AgentBBStatementViewModel firstBbStatementViewModel = agentBBStatementViewModelList.get( 0 );
                        double openingBalance = calculateOpeningBalance( firstBbStatementViewModel );
                        /// Balance set to be used in BB Statement Export
                        request.setAttribute(BBStatementReportConstants.openingBalance, openingBalance);
                    }
                    if( pagingHelperModel.isLastPage() || pagingHelperModel.getPageNo() == null )
                    {
                        AgentBBStatementViewModel lastBbStatementViewModel = agentBBStatementViewModelList.get( agentBBStatementViewModelList.size() - 1 );
                        /// Balance set to be used in BB Statement Export
                        request.setAttribute(BBStatementReportConstants.closingBalance, lastBbStatementViewModel.getBalanceAfterTransaction());
                    }
                }
                else
                {
                    double balance=0.0;
                    DateRangeHolderModel tempDateRangeHolderModel = new DateRangeHolderModel();
                    tempDateRangeHolderModel.setDatePropertyName( "transactionTime" );
                    tempDateRangeHolderModel.setToDate(dateRangeHolderModel.getFromDate());
                    tempDateRangeHolderModel.setFromDate(null);
                    searchBaseWrapper.setDateRangeHolderModel( tempDateRangeHolderModel );
                    searchBaseWrapper.setPagingHelperModel(null);
                    searchBaseWrapper = portalOlaFacade.searchAgentBBStatementView(searchBaseWrapper);
                    customList = searchBaseWrapper.getCustomList();
                    List<AgentBBStatementViewModel> tempAgentBbStatementViewModelList= customList.getResultsetList();
                    if( null != tempAgentBbStatementViewModelList && !tempAgentBbStatementViewModelList.isEmpty() )
                    {
                        AgentBBStatementViewModel bbStatementViewModel = tempAgentBbStatementViewModelList.get(tempAgentBbStatementViewModelList.size() - 1  );
                        balance = bbStatementViewModel.getBalanceAfterTransaction();
                    }
                    else
                    {
                        DateRangeHolderModel tempDateRangeHolderModel2 = new DateRangeHolderModel();
                        tempDateRangeHolderModel2.setDatePropertyName( "transactionTime" );
                        tempDateRangeHolderModel2.setFromDate(dateRangeHolderModel.getToDate());
                        tempDateRangeHolderModel2.setToDate(null);
                        searchBaseWrapper.setDateRangeHolderModel( tempDateRangeHolderModel2 );
                        searchBaseWrapper.setPagingHelperModel(null);
                        searchBaseWrapper = portalOlaFacade.searchAgentBBStatementView( searchBaseWrapper );
                        customList = searchBaseWrapper.getCustomList();
                        List<AgentBBStatementViewModel> tempAgentBbStatementViewModelList1= customList.getResultsetList();
                        if( null != tempAgentBbStatementViewModelList1 && !tempAgentBbStatementViewModelList1.isEmpty() )
                        {
                            AgentBBStatementViewModel bbStatementViewModel = tempAgentBbStatementViewModelList1.get(0);
                            balance = calculateOpeningBalance( bbStatementViewModel);

                        }
                        else
                        {
                            // balance = this.mfsAccountManager.getMfsAccountBalance(appUserId);
                        }
                    }
                    request.setAttribute(BBStatementReportConstants.openingBalance, balance);
                    request.setAttribute(BBStatementReportConstants.closingBalance, balance);
                    pagingHelperModel.setTotalRecordsCount(0);
                }
            }
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        if(appUserId != null){
            loadReportHeaderInfo(request,appUserId.toString(),dateRangeHolderModel);
        }else{
            return new ModelAndView( "redirect:searchAgent.jsf?infoMessage=Please select an agent to view its BB Statement Report" );
        }
        modelMap.put( "agentBBStatementViewModelList", agentBBStatementViewModelList );
        return new ModelAndView( getFormView(), modelMap );
    }
    @SuppressWarnings( "unchecked" )
    private void loadReportHeaderInfo(HttpServletRequest request, String appUserId, DateRangeHolderModel dateRangeHolderModel) throws Exception
    {
        Map<String, String> reportHeaderMap = new LinkedHashMap<String, String>();

        String issueDate = null;
        String statementDateRange = null;
        String branchName1 = null;
        String branchName2 = null;
        String customerName= null;
        String address1 ="";
        String address2 ="";
        String address3 ="";
        String zongMsisdn=null;
        String customerId=null;
        String currency=BBStatementReportConstants.currency;
        String accountLevel=null;
        String businessName=null;

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
                branchName1=bankModel.getAddress1().concat(",");

            if(bankModel.getAddress2()==null)
                branchName2="";
            else
                branchName2=bankModel.getAddress2();

            if(bankModel.getCity()!=null)
                branchName2=bankModel.getCity();
        }

        if (null != appUserId && appUserId.trim().length() > 0)
        {

            AppUserModel appUserModel = this.appUserManager.getUser(appUserId);

            zongMsisdn = appUserModel.getMobileNo();


            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            RetailerContactModel retailerContactModel = new RetailerContactModel();
            retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
            searchBaseWrapper.setBasePersistableModel(retailerContactModel);
            searchBaseWrapper = this.retailerContactManager.loadRetailerContact(searchBaseWrapper);
            retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();

            if(retailerContactModel != null){
                UserDeviceAccountsModel deviceAccountModel = this.allpayRetailerAccountManager.getUserDeviceAccountsModel(appUserId);
                if(deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null){
                    customerId=deviceAccountModel.getUserId();
                }
                customerName=retailerContactModel.getName();
                businessName=retailerContactModel.getBusinessName();

                if(retailerContactModel.getRelationOlaCustomerAccountTypeModel()!=null){
                    accountLevel=retailerContactModel.getRelationOlaCustomerAccountTypeModel().getName();
                }
                ///////////////Retailer Address
                Collection<RetailerContactAddressesModel> retailerContactAddresses = retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList();


                if(retailerContactAddresses != null && retailerContactAddresses.size() > 0){
                    for(RetailerContactAddressesModel retAdd : retailerContactAddresses)
                    {
                        if(retAdd.getApplicantTypeId()==null && retAdd.getApplicantDetailId()==null
                                && retAdd.getAddressTypeId()==AddressTypeConstants.CORRESSPONDANCE_ADDRESS.longValue())
                        {
                            AddressModel addressModel = retAdd.getAddressIdAddressModel();

                            if(addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()){
                                address1 = address1 + addressModel.getStreetAddress();
                            }
                            if(addressModel.getCityId() != null ){
                                address2= addressModel.getCityIdCityModel().getName();
                            }
                        }

                    }
                }

            }

        }

        reportHeaderMap.put( BBStatementReportConstants.issueDateKey,issueDate);
        reportHeaderMap.put( BBStatementReportConstants.statementDateRangeKey,statementDateRange );
        reportHeaderMap.put(BBStatementReportConstants.branchNameKey1,branchName1) ;
        reportHeaderMap.put(BBStatementReportConstants.branchNameKey2,branchName2) ;
        reportHeaderMap.put(BBStatementReportConstants.customerNameKey,customerName );
        reportHeaderMap.put(BBStatementReportConstants.addressKey1,address1);
        reportHeaderMap.put(BBStatementReportConstants.addressKey2,address2);
        reportHeaderMap.put(BBStatementReportConstants.addressKey3,address3);
        reportHeaderMap.put( BBStatementReportConstants.zongMsisdnKey,zongMsisdn);
        reportHeaderMap.put( BBStatementReportConstants.AgentIdKey,customerId);
        reportHeaderMap.put( BBStatementReportConstants.currencyKey,currency);
        reportHeaderMap.put( BBStatementReportConstants.accountLevelKey,accountLevel);
        reportHeaderMap.put(BBStatementReportConstants.businessNameKey, businessName);

        request.setAttribute( BBStatementReportConstants.mapkey, reportHeaderMap );
    }

    private double calculateOpeningBalance( AgentBBStatementViewModel bbStatementViewModel )
    {
        double openingBalance = 0.0;
        if( bbStatementViewModel.getDebitAmount() != null &&  bbStatementViewModel.getDebitAmount() > 0)
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

    public void setAllpayRetailerAccountManager(
            AllpayRetailerAccountManager allpayRetailerAccountManager) {
        this.allpayRetailerAccountManager = allpayRetailerAccountManager;
    }

    public void setBankManager(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setRetailerContactManager(
            RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

}