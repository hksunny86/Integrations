package com.inov8.microbank.server.service.dailyjob;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportSummaryModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.CustomerBalanceReportConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.facade.ActionLogFacade;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;
import com.inov8.ola.util.EncryptionUtil;
/** 
 * Created By    : Hassan javaid <br>
 * Creation Date : JAN, 2014 12<p>
 * Purpose       : To populate the table "CUSTOMER_BALANCE_REPORT" with the decrypted Customer Opening/Closing balance.<p>
 * Updated By    : Naseer Ullah<br>
 * Updated Date  : APR, 2014 18 <br>
 * Comments      : <br>
 */
public class CustomerBalanceReportDecryptionScheduler
{
	private static final Logger LOGGER = Logger.getLogger( CustomerBalanceReportDecryptionScheduler.class );
	private MessageSource messageSource;
	private FinanceReportsFacacde financeReportsFacacde;
	private ActionLogFacade actionLogFacade;
		
	public CustomerBalanceReportDecryptionScheduler()
	{
		
	}
	
	 public void init()
	 {	
		LOGGER.info("*********** Executing CustomerBalanceReportDecryptionScheduler ***********");
		ActionLogModel actionLogModel = null;
		Long startTime= System.currentTimeMillis();
		try
		{
			String maxExecutionMinsStr = messageSource.getMessage(CustomerBalanceReportConstantsInterface.KEY_MAX_EXEC_MINS, null, null);
			String chunkSizeStr = messageSource.getMessage(CustomerBalanceReportConstantsInterface.KEY_CHUNK_SIZE, null, null);

			int maxExecutionMins = Integer.parseInt(maxExecutionMinsStr);
			Integer chunkSize = Integer.valueOf(chunkSizeStr);

			Calendar maxExecutionTime = GregorianCalendar.getInstance();
			maxExecutionTime.add(Calendar.MINUTE, maxExecutionMins);
			long maxExecutionTimeMillis = maxExecutionTime.getTimeInMillis();

			LOGGER.info("Chunk Size is " + chunkSize);
			LOGGER.info("Max Execution Time in minutes is " + maxExecutionMins);
			LOGGER.info("Scheduler will not fetch any records for processing after " + maxExecutionTime.getTime());

			List<CustomerBalanceReportViewModel> customerBalanceReportViewModelList = null;
			actionLogModel= new ActionLogModel();

			//Start Action Log
			BaseWrapper	actionLogBaseWrapper= new BaseWrapperImpl();
			actionLogBaseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_CREATE);
			actionLogBaseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.CUTOMER_BALANCE_DECRYPTION_USECASE_ID);
			actionLogBaseWrapper.putObject("appUser",PortalConstants.SCHEDULER_APP_USER_ID);
			actionLogBaseWrapper.putObject("deviceType", DeviceTypeConstantsInterface.BULK_DISBURSEMENT );
			actionLogBaseWrapper.putObject("userName","Balance Decryption Scheduler");
			actionLogModel= actionLogFacade.createCustomActionLogRequiresNewTransaction(actionLogBaseWrapper);

			int iterationCount = 0;

        	do
	        {
	        	//Yesterday records
        		LOGGER.info( "Going to fetch YESTERDAY records from CUSTOMER_BALANCE_REPORT_VIEW with DECRYPTION_SCHEDULER_STATUS=" + CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_INPROCESS + " and END_DAY_BALANCE not null" );
    	        customerBalanceReportViewModelList = searchCustomerBalanceReportView(CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_INPROCESS, Boolean.FALSE, chunkSize);
    	        if( customerBalanceReportViewModelList != null && !customerBalanceReportViewModelList.isEmpty() )
    	        {
    	        	LOGGER.info( "Iteration count is " + ++iterationCount);
    	        	LOGGER.info( customerBalanceReportViewModelList.size() + " records fetched from CUSTOMER_BALANCE_REPORT_VIEW.");
    	        	customerBalanceReportViewModelList = decryptProps( customerBalanceReportViewModelList );
    	        	List<CustomerBalanceReportSummaryModel> customerBalanceReportSummaryModelList = populateSummaryModel(customerBalanceReportViewModelList);
    	        	saveOrUpdateCustomerBalanceReportSummary(customerBalanceReportSummaryModelList, PortalConstants.ACTION_UPDATE, Boolean.FALSE, CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_INPROCESS, CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_COMPLETE);
    	        }
	        }
	        while( customerBalanceReportViewModelList.size() == chunkSize.intValue() );

        	do
        	{
        		//Today records
        		LOGGER.info( "Going to fetch TODAY records from CUSTOMER_BALANCE_REPORT_VIEW with DECRYPTION_SCHEDULER_STATUS=" + CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_NEW + " and END_DAY_BALANCE null" );
        		customerBalanceReportViewModelList = searchCustomerBalanceReportView(CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_NEW, Boolean.TRUE, chunkSize);
        		if( customerBalanceReportViewModelList != null && !customerBalanceReportViewModelList.isEmpty() )
        		{
        			LOGGER.info( "Iteration count is " + ++iterationCount);
        			LOGGER.info( customerBalanceReportViewModelList.size() + " records fetched from CUSTOMER_BALANCE_REPORT_VIEW.");
        			customerBalanceReportViewModelList = decryptProps( customerBalanceReportViewModelList );
        			List<CustomerBalanceReportSummaryModel> customerBalanceReportSummaryModelList = populateSummaryModel(customerBalanceReportViewModelList);
        			saveOrUpdateCustomerBalanceReportSummary(customerBalanceReportSummaryModelList, PortalConstants.ACTION_CREATE, Boolean.TRUE, CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_NEW, CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_INPROCESS);				
        		}
        	}
        	while( customerBalanceReportViewModelList.size() == chunkSize.intValue() );

	        do
	        {
	        	//Past records case: Usually Scheduler will not process these records once they are inserted in CUSTOMER_BALANCE_REPORT table 
	        	LOGGER.info( "Going to fetch PAST records from CUSTOMER_BALANCE_REPORT_VIEW with DECRYPTION_SCHEDULER_STATUS=" + CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_NEW + " and END_DAY_BALANCE not null" );
	        	customerBalanceReportViewModelList = searchCustomerBalanceReportView(CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_NEW, Boolean.FALSE, chunkSize);
	        	if( customerBalanceReportViewModelList != null && !customerBalanceReportViewModelList.isEmpty() )
	        	{
	        		LOGGER.info( "Iteration count is " + ++iterationCount);
	        		LOGGER.info( customerBalanceReportViewModelList.size() + " records fetched from CUSTOMER_BALANCE_REPORT_VIEW.");
	        		customerBalanceReportViewModelList = decryptProps( customerBalanceReportViewModelList );
	        		List<CustomerBalanceReportSummaryModel> customerBalanceReportSummaryModelList = populateSummaryModel(customerBalanceReportViewModelList);
					saveOrUpdateCustomerBalanceReportSummary(customerBalanceReportSummaryModelList, PortalConstants.ACTION_CREATE, Boolean.FALSE, CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_NEW, CustomerBalanceReportConstantsInterface.SCHEDULER_STATUS_COMPLETE);
	        	}
	        }
	        while( customerBalanceReportViewModelList.size() == chunkSize.intValue() && maxExecutionTimeMillis >= System.currentTimeMillis() );

		}
		catch(Exception e)
		{
			LOGGER.error( e.getMessage(), e );
		}
		//End Action Log
		this.actionLogAfterEnd(actionLogModel);
		
		Long endTime= System.currentTimeMillis();			 
		LOGGER.info("Total time consumed in seconds : "+ (endTime-startTime)/1000);

		LOGGER.info("*********** Completed CustomerBalanceReportDecryptionScheduler ***********");
	}

	 private List<CustomerBalanceReportViewModel> searchCustomerBalanceReportView(String schedulerStatus, Boolean isEndDayBalanceNull, Integer chunkSize) throws FrameworkCheckedException
	 {
		 List<CustomerBalanceReportViewModel> customerBalanceReportViewModelList = null;

		 SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		 searchBaseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_SCHEDULER_STATUS, schedulerStatus);
	     searchBaseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_IS_END_DAY_BALANCE_NULL, isEndDayBalanceNull);
	     searchBaseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_CHUNK_SIZE, chunkSize);
	     customerBalanceReportViewModelList = financeReportsFacacde.customSearchCustomerBalanceReportView( searchBaseWrapper );
	     return customerBalanceReportViewModelList;
	 }

	 private void saveOrUpdateCustomerBalanceReportSummary(List<CustomerBalanceReportSummaryModel> customerBalanceReportSummaryModelList, Long actionId,Boolean isEndDayBalanceNull, String oldStatus,  String newStatus ) throws FrameworkCheckedException
	 {
		 BaseWrapper baseWrapper= new BaseWrapperImpl();
		 baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
		 baseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_SCHEDULER_STATUS, oldStatus);
		 baseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_IS_END_DAY_BALANCE_NULL, isEndDayBalanceNull);
		 baseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_SCHEDULER_STATUS_UPDATED, newStatus);
		 baseWrapper.putObject(CustomerBalanceReportConstantsInterface.KEY_COLLECTION,(Serializable) customerBalanceReportSummaryModelList);
		 financeReportsFacacde.updateDailyCustomerBalanceSummary(baseWrapper);
	 }

	 private List<CustomerBalanceReportViewModel> decryptProps( List<CustomerBalanceReportViewModel> list )
     {
		 LOGGER.info("Decrypting balances");
	 	 String startDayBalance;
	 	 String endDayBalance;
	 	
        for( CustomerBalanceReportViewModel customerBalanceReportViewModel : list )
        {        	     	
        	try
        	{
        		startDayBalance = customerBalanceReportViewModel.getStartDayBalance();
    			if(null!=startDayBalance)
    			{
    				Float.valueOf(startDayBalance);
    			}
    			customerBalanceReportViewModel.setStartDayBalance(startDayBalance);
        	}
        	catch(Exception ex){
        		LOGGER.error("Opening Balance Dacryption Failed for User ID : "+customerBalanceReportViewModel.getUserId());
        		customerBalanceReportViewModel.setStartDayBalance(null);
        	}

        	try{   		
        		endDayBalance =	 customerBalanceReportViewModel.getEndDayBalance();
    			if(null!=endDayBalance){		
    				Float.valueOf(endDayBalance);
    			}
    			customerBalanceReportViewModel.setEndDayBalance(endDayBalance);
        	}
        	catch(Exception ex){
        		LOGGER.error("Closing Balance Dacryption Failed for User ID : "+customerBalanceReportViewModel.getUserId() );
        		customerBalanceReportViewModel.setEndDayBalance(null);
        	}
        }
	 
		LOGGER.info("END decrypting balances.");
        return list;
    }
	 
	 private List<CustomerBalanceReportSummaryModel> populateSummaryModel(List<CustomerBalanceReportViewModel> list){
		 
		 CustomerBalanceReportSummaryModel customerBalanceReportSummaryModel = null;
		 List<CustomerBalanceReportSummaryModel> CustomerBalanceReportSummaryModelList =  new ArrayList<CustomerBalanceReportSummaryModel>();
		 		 
		 for(CustomerBalanceReportViewModel customerBalanceReportViewModel : list ){
			 	
			 	customerBalanceReportSummaryModel= new CustomerBalanceReportSummaryModel();
			 
			 	customerBalanceReportSummaryModel.setAppUserId(customerBalanceReportViewModel.getAppUserId());
				customerBalanceReportSummaryModel.setUserId(customerBalanceReportViewModel.getUserId());
				customerBalanceReportSummaryModel.setAccountId(customerBalanceReportViewModel.getAccountId());
				customerBalanceReportSummaryModel.setAccountNumber(customerBalanceReportViewModel.getAccountNumber());
				customerBalanceReportSummaryModel.setBalance(customerBalanceReportViewModel.getBalance());
				customerBalanceReportSummaryModel.setStatusId(customerBalanceReportViewModel.getStatusId());
				customerBalanceReportSummaryModel.setFirstName(customerBalanceReportViewModel.getFirstName());
				customerBalanceReportSummaryModel.setMiddleName(customerBalanceReportViewModel.getMiddleName());
				customerBalanceReportSummaryModel.setLastName(customerBalanceReportViewModel.getLastName());
				customerBalanceReportSummaryModel.setAddress(customerBalanceReportViewModel.getAddress());
				customerBalanceReportSummaryModel.setCnic(customerBalanceReportViewModel.getCnic());
				customerBalanceReportSummaryModel.setFatherName(customerBalanceReportViewModel.getFatherName());
				customerBalanceReportSummaryModel.setActive(customerBalanceReportViewModel.getActive());
				customerBalanceReportSummaryModel.setDeleted(customerBalanceReportViewModel.getDeleted());
				customerBalanceReportSummaryModel.setLandlineNumber(customerBalanceReportViewModel.getLandlineNumber());
				customerBalanceReportSummaryModel.setMobileNumber(customerBalanceReportViewModel.getMobileNumber());
				customerBalanceReportSummaryModel.setDob(customerBalanceReportViewModel.getDob());
				customerBalanceReportSummaryModel.setBalanceDisbursed(customerBalanceReportViewModel.getBalanceDisbursed());
				customerBalanceReportSummaryModel.setBalanceReceived(customerBalanceReportViewModel.getBalanceReceived());
				customerBalanceReportSummaryModel.setEndDayBalance(customerBalanceReportViewModel.getEndDayBalance());
				customerBalanceReportSummaryModel.setStartDayBalance(customerBalanceReportViewModel.getStartDayBalance());
				customerBalanceReportSummaryModel.setStatsDate(customerBalanceReportViewModel.getStatsDate());
				customerBalanceReportSummaryModel.setDailyAccountStatsId(customerBalanceReportViewModel.getDailyAccountStatsId());
				
				CustomerBalanceReportSummaryModelList.add(customerBalanceReportSummaryModel);
		 
		 }
		 return CustomerBalanceReportSummaryModelList;	 
	 }

	private void actionLogAfterEnd(ActionLogModel actionLogModel)
    {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
        actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
        insertActionLogRequiresNewTransaction(actionLogModel);
    }

    private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel)
    {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        try
        {
            baseWrapper = this.actionLogFacade.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
        }
        catch (Exception ex)
        {
        	LOGGER.error("Exception occurred while processing", ex);
        }
        return actionLogModel;
    }

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public void setFinanceReportsFacacde(FinanceReportsFacacde financeReportsFacacde)
	{
		this.financeReportsFacacde = financeReportsFacacde;
	}

	public void setActionLogFacade(ActionLogFacade actionLogFacade)
	{
		this.actionLogFacade = actionLogFacade;
	}

}
