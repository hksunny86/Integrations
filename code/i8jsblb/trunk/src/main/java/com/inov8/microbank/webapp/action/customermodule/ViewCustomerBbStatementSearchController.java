package com.inov8.microbank.webapp.action.customermodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.ola.CustomerBbStatementViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.bankmodule.BankManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.inov8.microbank.common.util.PortalDateUtils.FORMAT_DAY_MONTH_YEAR_COMPLETE;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 22, 2013 6:55:17 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ViewCustomerBbStatementSearchController extends BaseFormSearchController
{
	//Autowired
    private PortalOlaFacade portalOlaFacade;
	private MfsAccountManager mfsAccountManager;
	private BankManager bankManager;
	private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;

	public ViewCustomerBbStatementSearchController()
	{
		setCommandName("customerBbStatementViewModel");
		setCommandClass(CustomerBbStatementViewModel.class);
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
	    CustomerBbStatementViewModel customerBbStatementViewModel = new CustomerBbStatementViewModel( new DateRangeHolderModel() );
        return customerBbStatementViewModel;
    }

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		List<OlaCustomerAccountTypeModel> accountTypeList = new ArrayList();
		String appUserIdEncrypted = ServletRequestUtils.getRequiredStringParameter(request, "appUserId");
		Long appUserId = Long.parseLong(appUserIdEncrypted);
		AppUserModel appUserModel = mfsAccountManager.getAppUserModelByPrimaryKey(appUserId);
		CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
		List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeList = olaCustomerAccountTypeDao.loadCustomerACTypes(new Long[] {customerModel.getCustomerAccountTypeId()});
		OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = olaCustomerAccountTypeList.get(0);
		olaCustomerAccountTypeModel.setName("BLB");
		AccountModel accountModel = mfsAccountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(),customerModel.getCustomerAccountTypeId(),
				OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
		if(accountModel == null)
			accountModel = mfsAccountManager.getLastClosedAccountModel(appUserModel.getNic(),customerModel.getCustomerAccountTypeId());
		if(accountModel != null)
			accountTypeList.add(olaCustomerAccountTypeModel);

		olaCustomerAccountTypeList = olaCustomerAccountTypeDao.loadCustomerACTypes(new Long[] {CustomerAccountTypeConstants.HRA});
		accountModel = mfsAccountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(),CustomerAccountTypeConstants.HRA,
				OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
		if(accountModel == null)
			accountModel = mfsAccountManager.getLastClosedAccountModel(appUserModel.getNic(),CustomerAccountTypeConstants.HRA);

		olaCustomerAccountTypeModel = olaCustomerAccountTypeList.get(0);

		if(accountModel != null)
			accountTypeList.add(olaCustomerAccountTypeModel);

		Map referenceDataMap = new HashMap();

		referenceDataMap.put("accountTypeList",accountTypeList);

		return referenceDataMap;
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
	        bankModel.setPrimaryKey(BankConstantsInterface.ASKARI_BANK_ID);
	        baseWrapperBank.setBasePersistableModel(bankModel);
	        baseWrapperBank = this.bankManager.loadBank(baseWrapperBank);
	        bankModel= (BankModel) baseWrapperBank.getBasePersistableModel();
	        
	        if(bankModel!=null){
	        	 
	        	if(bankModel.getAddress1()==null)
	        		branchName1="";
	        	else 
	        		branchName1=bankModel.getAddress1();
	  
	        	
	        	if(bankModel.getCity()!=null)
	        		 branchName2=bankModel.getCity();
	        }
	       	        
	        if (null != appUserId && appUserId.trim().length() > 0)
		    {
		     	
		      BaseWrapper baseWrapper = new BaseWrapperImpl();
		      AppUserModel appUserModel = new AppUserModel();
//	          appUserModel.setAppUserId(Long.valueOf(EncryptionUtil.decryptForAppUserId( appUserId)));
				appUserModel.setAppUserId(Long.valueOf(appUserId));
	          baseWrapper.setBasePersistableModel(appUserModel);
		      baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
		      
		      appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
		     

		      zongMsisdn = appUserModel.getMobileNo();		         
		      CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();

		      if(customerModel != null){		    	 
//			      UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(Long.valueOf(EncryptionUtil.decryptForAppUserId( appUserId)),DeviceTypeConstantsInterface.MOBILE);
				  UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(Long.valueOf(appUserId),DeviceTypeConstantsInterface.MOBILE);

				  if(deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null){
		    		  customerId=deviceAccountModel.getUserId(); 
		    	  }			     
		    	  customerName=customerModel.getName();		    			    	  		   
		    	 
		    	  if(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel()!=null){
		    		  accountLevel=customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName();
		    	  }
				  String accountType = ServletRequestUtils.getRequiredStringParameter(request, "paymentModeId");
				  if(!accountType.equals("4"))
					  accountType = "BLB";
				  if(accountType.equals("4"))
					  accountLevel = "HRA";
		    	  // Populating Address Fields
		    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
		    	  StringBuilder sb = new StringBuilder(); 
		    	  if(customerAddresses != null && customerAddresses.size() > 0){
		    		  for(CustomerAddressesModel custAdd : customerAddresses){
		    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
		    			  if(custAdd.getAddressTypeId() == 1 && custAdd.getApplicantTypeId()==1){
		    				  
		    				 
		    				  if(addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()){
		    					  address1=  addressModel.getFullAddress();
		    				  }
		    				  else  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  address1 = addressModel.getHouseNo(); 
		    				  }
		    				  
		    				  if(addressModel.getCityId() != null){
		    					  address2 = addressModel.getCityIdCityModel().getName();
		    					 
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
	        reportHeaderMap.put( BBStatementReportConstants.customerIdKey,customerId);
	        reportHeaderMap.put( BBStatementReportConstants.currencyKey,currency); 
	        reportHeaderMap.put( BBStatementReportConstants.accountLevelKey,accountLevel);
	        
	        request.setAttribute( BBStatementReportConstants.mapkey, reportHeaderMap );
		}
	
	@SuppressWarnings( "unchecked" )
    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        Map<String, Object> modelMap = new HashMap<>( 3 ); 
        List<CustomerBbStatementViewModel> list = null;
        CustomerBbStatementViewModel customerBbStatementViewModel = (CustomerBbStatementViewModel) model;

        String appUserIdEncrypted = ServletRequestUtils.getRequiredStringParameter(request, "appUserId");
        String appUserIdDecrypted =  appUserIdEncrypted ;
		Long appUserId = null;
        if(null != appUserIdDecrypted && !"".equals(appUserIdDecrypted)){
        appUserId = Long.valueOf(appUserIdEncrypted);
        customerBbStatementViewModel.setAppUserId(appUserId);
        }

		String accountType = ServletRequestUtils.getRequiredStringParameter(request, "paymentModeId");
        Long customerAccountTypeId = null;
		if(!accountType.equals("") && !accountType.equals("4"))
			accountType = "BLB";
		else if(accountType.equals("4"))
		{
			accountType = "HRA";
			customerAccountTypeId = CustomerAccountTypeConstants.HRA;
		}
		Long paymentModeId = null ;
		Boolean isHRA = false;
		AppUserModel appUserModel = this.mfsAccountManager.getAppUserModelByPrimaryKey(appUserId);
		CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
		AccountModel accountModel = null;
		if(accountType.equals("BLB")) {
			paymentModeId = customerModel.getCustomerAccountTypeId();
			customerAccountTypeId = customerModel.getCustomerAccountTypeId();
		}
		else if(accountType.equals("HRA"))
		{
			paymentModeId = CustomerAccountTypeConstants.HRA;
			isHRA = true;
		}

		SmartMoneyAccountModel closedSMA = null;
		if(isHRA)
		{
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
			sma.setCustomerId(appUserModel.getCustomerId());
			sma.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
			sma.setActive(Boolean.TRUE);

			SmartMoneyAccountModel smartMoneyAccountModel = this.mfsAccountManager.getSmartMoneyAccountByExample(sma);

			if(smartMoneyAccountModel == null)
			{
				List<SmartMoneyAccountModel> smaList = this.mfsAccountManager.getLastClosedSMAAccount(sma);
				if(smaList != null && !smaList.isEmpty())
				{
					smartMoneyAccountModel = smaList.get(0);
					closedSMA = smartMoneyAccountModel;
				}
			}
		}

		if (isHRA && closedSMA != null)
			accountModel = this.mfsAccountManager.getLastClosedAccountModel(appUserModel.getNic(),paymentModeId);
		else if(isHRA && closedSMA == null)
			accountModel = this.mfsAccountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(),paymentModeId,
					OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
		else
			accountModel = this.mfsAccountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(),paymentModeId,
					OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
		if(accountModel == null)
			accountModel = mfsAccountManager.getLastClosedAccountModel(appUserModel.getNic(),customerAccountTypeId);
		if(accountModel != null)
			customerBbStatementViewModel.setAccountId(accountModel.getAccountId());
		if(paymentModeId != null)
			customerBbStatementViewModel.setPaymentModeId(paymentModeId);

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel( customerBbStatementViewModel );

        DateRangeHolderModel dateRangeHolderModel = customerBbStatementViewModel.getDateRangeHolderModel();
        dateRangeHolderModel.setDatePropertyName( "transactionTime" );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
        
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "transactionTime", SortingOrder.ASC );
        }
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		try
		{
			list = portalOlaFacade.searchBBStatementViewByPaymentModeId(customerBbStatementViewModel,searchBaseWrapper);
			if(list != null && !list.isEmpty())
			{
				if( pagingHelperModel.isFirstPage() || pagingHelperModel.getPageNo() == null )
				{
					CustomerBbStatementViewModel firstBbStatementViewModel = list.get(0);
					double openingBalance = calculateOpeningBalance( firstBbStatementViewModel );
					/// Balance set to be used in BB Statement Export
					request.setAttribute(BBStatementReportConstants.openingBalance, openingBalance);
				}
				CustomerBbStatementViewModel lastBbStatementViewModel = list.get( list.size() - 1 );
				/// Balance set to be used in BB Statement Export
				request.setAttribute(BBStatementReportConstants.closingBalance, lastBbStatementViewModel.getBalanceAfterTransaction());
				pagingHelperModel.setTotalRecordsCount(list.size());
			}
			if(pagingHelperModel.getTotalRecordsCount() == null){
				pagingHelperModel.setTotalRecordsCount(0);
				double balance =0.0;
				CustomList<CustomerBbStatementViewModel> customerBbStatementViewModelCustomList = new CustomList<CustomerBbStatementViewModel>();
				DateRangeHolderModel tempDateRangeHolderModel = new DateRangeHolderModel();
				tempDateRangeHolderModel.setDatePropertyName("transactionTime");
				tempDateRangeHolderModel.setToDate(dateRangeHolderModel.getFromDate());
				tempDateRangeHolderModel.setFromDate(null);
				searchBaseWrapper.setDateRangeHolderModel(tempDateRangeHolderModel);
				searchBaseWrapper.setPagingHelperModel(null);
				searchBaseWrapper = portalOlaFacade.searchCustomerBbStatementView(searchBaseWrapper);
				customerBbStatementViewModelCustomList = searchBaseWrapper.getCustomList();
				list = customerBbStatementViewModelCustomList.getResultsetList();
				if(list != null && !list.isEmpty()){
						balance = list.get(list.size()-1).getBalanceAfterTransaction();
				}
				else{


					tempDateRangeHolderModel.setFromDate(dateRangeHolderModel.getToDate());
					tempDateRangeHolderModel.setToDate(null);
					searchBaseWrapper.setDateRangeHolderModel(tempDateRangeHolderModel);
					searchBaseWrapper = portalOlaFacade.searchCustomerBbStatementView(searchBaseWrapper);
					customerBbStatementViewModelCustomList = searchBaseWrapper.getCustomList();
					list =customerBbStatementViewModelCustomList.getResultsetList();
					if(list != null && !list.isEmpty()){
						balance = calculateOpeningBalance(list.get(0));
					}
					else{
						balance = this.mfsAccountManager.getMfsAccountBalance(customerBbStatementViewModel.getAppUserId(),customerBbStatementViewModel.getPaymentModeId());
					}
				}


				request.setAttribute(BBStatementReportConstants.openingBalance,balance);
				request.setAttribute(BBStatementReportConstants.closingBalance,balance);
			}
		}
		catch (Exception e)
		{
			log.error( "Error in ViewCustomerBbStatementSearchController.onSearch() :: " + e.getMessage(), e );
		}
        if(appUserId != null ){
        	loadReportHeaderInfo(request,appUserId.toString(),dateRangeHolderModel);
        }else{
        	return new ModelAndView( "redirect:p_pgsearchuserinfo.html?infoMessage=Please select a customer to view its BB Statement Report");
        }
        modelMap.put( "customerBbStatementViewModelList", list );
		modelMap.put( "isHRA", isHRA );
        return new ModelAndView( getFormView(), modelMap );
    }

    private double calculateOpeningBalance( CustomerBbStatementViewModel bbStatementViewModel )
    {
        double openingBalance = bbStatementViewModel.getBalanceAfterTransaction().doubleValue();
        
        if( bbStatementViewModel.getDebitAmount() != null && bbStatementViewModel.getDebitAmount() > 0D )
        {
        	openingBalance += bbStatementViewModel.getDebitAmount().doubleValue();
        }
        else if ( bbStatementViewModel.getCreditAmount() != null && bbStatementViewModel.getCreditAmount() > 0D )
        {
            openingBalance -= bbStatementViewModel.getCreditAmount().doubleValue();
        }

        return openingBalance;
    }
   
    public void setPortalOlaFacade( PortalOlaFacade portalOlaFacade )
    {
        this.portalOlaFacade = portalOlaFacade;
    }

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setBankManager(BankManager bankManager) {
		this.bankManager = bankManager;
	}

	public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
		this.olaCustomerAccountTypeDao = olaCustomerAccountTypeDao;
	}
}
