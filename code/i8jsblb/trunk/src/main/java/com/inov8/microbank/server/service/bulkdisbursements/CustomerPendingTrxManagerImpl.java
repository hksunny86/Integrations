package com.inov8.microbank.server.service.bulkdisbursements;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerPendingTrxManagerImpl implements CustomerPendingTrxManager{
	private static Logger logger = Logger.getLogger(BulkDisbursementsManagerImpl.class);

	private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private TransactionModuleManager transactionManager;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private WorkFlowExceptionTranslator workflowExceptionTranslator;
	private SettlementManager settlementManager;
	private ActionLogManager actionLogManager;
	private RetailerContactManager	retailerContactManager;
	private CommissionManager commissionManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;

	private TransactionDetailMasterDAO transactionDetailMasterDAO;

	
	public void setTransactionDetailMasterDAO(
			TransactionDetailMasterDAO transactionDetailMasterDAO) {
		this.transactionDetailMasterDAO = transactionDetailMasterDAO;
	}

	public WorkFlowWrapper makeCustomerTrxByTransactionCode(String transactionId, String cnicCustomer) throws FrameworkCheckedException{

		logger.info("[CustomerPendingTrxManagerImpl.makeCustomerTrxByTransactionCode] Going to settle transaction of customer CNIC by transaction Id : "+transactionId);
		
		try
		{
			TransactionDetailMasterModel  txDetailMasterModel= this.transactionDetailMasterDAO.findCustomerTrxByTransactionCode(transactionId);
			if(txDetailMasterModel.getReversalFlag() != null && txDetailMasterModel.getReversalFlag()){
				throw new FrameworkCheckedException(MessageUtil.getMessage("REVERSAL_INPROCESS_ERROR"));
			}

			//ActionLog Start
			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

			
			//Load Customer AppUserModel 
			AppUserModel customerAppUserModel =new AppUserModel();
			customerAppUserModel.setNic(cnicCustomer);
			customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);
			
			if(customerAppUserModel == null || customerAppUserModel.getAppUserId() == null){
				throw new FrameworkCheckedException("No customer found against given CNIC: "+cnicCustomer);
			}
			
			
			CustomerModel customerModel = customerAppUserModel.getCustomerIdCustomerModel();
			appUserManager.updateCustomerFirstDebitCredit(customerModel, customerAppUserModel.getRegistrationStateId(), false, null);
			
			//Customer SMA
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
			sma.setCustomerId(customerAppUserModel.getCustomerId());
			CustomList<SmartMoneyAccountModel> smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(sma);
			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) {
				sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
			}else{
				throw new FrameworkCheckedException("Unable to load Smart Money Account Info against given CNIC: "+cnicCustomer);
			}
			
			BaseWrapper bWrapper = new BaseWrapperImpl();

			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setAppUserId(customerAppUserModel.getAppUserId());
			userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
			bWrapper.setBasePersistableModel(userDeviceAccountsModel);
			bWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(bWrapper);
			UserDeviceAccountsModel customerUDAModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
			if(customerUDAModel == null || customerUDAModel.getUserDeviceAccountsId() == null){
				throw new FrameworkCheckedException("Unable to load User Device Account Info against given CNIC: "+cnicCustomer);
			}
			
			AppUserModel	agentAppUser =	UserUtils.getCurrentUser();
			
			SmartMoneyAccountModel agentSMA = smartMoneyAccountManager.loadSmartMoneyAccountModel(agentAppUser);
			if (null == agentSMA) {
				throw new FrameworkCheckedException("Unable to load Agent Smart Money Account Info of : "+agentAppUser.getUsername());
			}
			
			ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
			
			logger.info("****** Going to transfer from Sundry to Customer A/C for TransactionId:"+txDetailMasterModel.getTransactionId() + " -- TransactionCode:"+txDetailMasterModel.getTransactionCode());
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
			workFlowWrapper.setSmartMoneyAccountModel(agentSMA);
			workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
			workFlowWrapper.setOlaSmartMoneyAccountModel(sma);
			workFlowWrapper.setCustomerModel(customerModel);
			workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
			workFlowWrapper.setAppUserModel(ThreadLocalAppUser.getAppUserModel());

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactModel retailerContact = new RetailerContactModel();
			retailerContact.setRetailerContactId(agentAppUser.getRetailerContactId());
			searchBaseWrapper.setBasePersistableModel( retailerContact );
			searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
			RetailerContactModel retailerContactModel = (RetailerContactModel)searchBaseWrapper.getBasePersistableModel();
			workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
			workFlowWrapper.setRetailerContactModel(retailerContactModel);
			
			ProductModel productModel = new ProductModel();
			productModel.setProductId(txDetailMasterModel.getProductId());
			workFlowWrapper.setProductModel(productModel);
			
			
	        searchBaseWrapper = new SearchBaseWrapperImpl();
	        TransactionModel transactionModel = new TransactionModel();
	        transactionModel.setTransactionId(txDetailMasterModel.getTransactionId());
	        searchBaseWrapper.setBasePersistableModel(transactionModel);
	        searchBaseWrapper = this.transactionManager.loadTransaction(searchBaseWrapper);    

	        if(searchBaseWrapper.getBasePersistableModel() != null) 
	        {
	        	transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
	        	workFlowWrapper.setTransactionModel(transactionModel);
	        	workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());

		        List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transactionModel.getTransactionIdTransactionDetailModelList();
		        if(txdetails != null && !txdetails.isEmpty() )
		        {
		        	workFlowWrapper.setTransactionDetailModel(txdetails.get(0));
		        }
		        else
		        {
		        	logger.error("[CustomerPendingTrxManagerImpl.settleCustomerPendingTrx] Unable to load TransactionDetailModel for transactionId:"+txDetailMasterModel.getTransactionId());
					throw new FrameworkCheckedException("Unable to load TransactionDetailModel for transactionId:"+txDetailMasterModel.getTransactionId());
		        }
	        }
	        else
	        {
	        	logger.error("[CustomerPendingTrxManagerImpl.settleCustomerPendingTrx] Unable to load TransactionModel for transactionId:"+txDetailMasterModel.getTransactionId());
				throw new FrameworkCheckedException("Unable to load TransactionModel for transactionId:"+txDetailMasterModel.getTransactionId());
	        }
			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setBasePersistableModel(sma);
//		        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
	        switchWrapper.setBankId( txDetailMasterModel.getBankId() );
	        switchWrapper.setPaymentModeId( txDetailMasterModel.getPaymentModeId() );

			try{
				workFlowWrapper.setCurrentSupProcessingStatusId(txDetailMasterModel.getSupProcessingStatusId());
				workFlowWrapper.setLeg2Transaction(true);
				
				CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(txDetailMasterModel.getTransactionDetailId());
				workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);

				commissionManager.makeAgent2CommissionSettlement(workFlowWrapper);
				
				settlementManager.updateCommissionTransactionSettled(txDetailMasterModel.getTransactionDetailId());

				switchWrapper = olaVeriflyFinancialInstitution.settleCustomerPendingTrx(switchWrapper);
				workFlowWrapper.setOLASwitchWrapper(switchWrapper);
			}catch(WorkFlowException ex){
				throw translateException(ex);
			}

			// update old recipientAccountNo to customer account no
			String recipientAccountNo = switchWrapper.getToAccountNo();
			workFlowWrapper.getTransactionDetailModel().setCustomField2(recipientAccountNo);
			
			
			
			String brandName = MessageUtil.getMessage("jsbl.brandName");
			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(3);
			Date dateNow = Calendar.getInstance().getTime();
			double customerBalance	= switchWrapper.getOlavo().getToBalanceAfterTransaction();
			double agentBalance	= switchWrapper.getOlavo().getAgentBalanceAfterTransaction();
			double incCharges = txDetailMasterModel.getInclusiveCharges();
			
			if(ProductConstantsInterface.BULK_PAYMENT.longValue() != workFlowWrapper.getProductModel().getProductId().longValue()){
				
				String senderSms = MessageUtil.getMessage(
						"USSD.PendingTransaction.Sender",
						new Object[]{brandName, 
								workFlowWrapper.getTransactionCodeModel().getCode(),
								Formatter.formatDouble(txDetailMasterModel.getTransactionAmount()),
								cnicCustomer, 
								PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_TIME_FORMAT),
								PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_DATE_FORMAT)
						});
				messageList.add(new SmsMessage(txDetailMasterModel.getSaleMobileNo(), senderSms));
				transactionModel.setConfirmationMessage(senderSms);
				workFlowWrapper.getTransactionDetailModel().setCustomField8(senderSms);
			}
			
			String agent2Sms = MessageUtil.getMessage(
					"USSD.PendingTransaction.Agent",
					new Object[]{
							brandName,
							workFlowWrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(txDetailMasterModel.getTransactionAmount()),
							PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_DATE_FORMAT),
							Formatter.formatDouble(incCharges),
							Formatter.formatDouble(agentBalance)
					});
			messageList.add(new SmsMessage(workFlowWrapper.getAppUserModel().getMobileNo(), agent2Sms));
			workFlowWrapper.getTransactionDetailModel().setCustomField4(agent2Sms);
			
			String customerSms = MessageUtil.getMessage(
					"USSD.PendingTransaction.Receiver",
					new Object[]{
							brandName,
							workFlowWrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(txDetailMasterModel.getTransactionAmount()),
							PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_DATE_FORMAT),
							brandName+" Agent",
							Formatter.formatDouble(incCharges),
							Formatter.formatDouble(customerBalance)
					});
			messageList.add(new SmsMessage(workFlowWrapper.getCustomerModel().getMobileNo(), customerSms));
			workFlowWrapper.getTransactionDetailModel().setCustomField10(customerSms);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			
			transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
			transactionModel.setToRetContactId(agentAppUser.getRetailerContactId());
			baseWrapper.setBasePersistableModel(transactionModel);
			transactionManager.updateTransaction(baseWrapper);
			
			
			txDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
			baseWrapper.setBasePersistableModel(txDetailMasterModel);
			
			String agent2Id = null;
			if(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel() != null && ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() != null){
				agent2Id = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
			}
			
			
			txDetailMasterModel.setRecipientMfsId(customerUDAModel.getUserId());
			txDetailMasterModel.setRecipientAccountNo(recipientAccountNo);
			txDetailMasterModel.setRecipientAccountNick(sma.getName());
			txDetailMasterModel.setAgent2Id(agent2Id);
			
			transactionDetailMasterManager.updateTransactionDetailMasterForLeg2(txDetailMasterModel);
			
/*			Long typeSpecificCustomerPoolAcc = this.settlementManager.getStakeholderBankInfoId(workFlowWrapper.getCustomerModel().getCustomerAccountTypeId());

			saveSettlementTransactionEntry(txDetailMasterModel.getTransactionId(),
					txDetailMasterModel.getProductId(),
					switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().getActualBillableAmount(),
					PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
					PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID
					);

			saveSettlementTransactionEntry(txDetailMasterModel.getTransactionId(),
					txDetailMasterModel.getProductId(),
					switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().getActualBillableAmount(),
					PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
					PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID
					);

			saveSettlementTransactionEntry(txDetailMasterModel.getTransactionId(),
					txDetailMasterModel.getProductId(),
					switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().getActualBillableAmount(),
					PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
					typeSpecificCustomerPoolAcc
					);
			
			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(3);
		
			String brandName = MessageUtil.getMessage("jsbl.brandName");
			Double agentBalance = ((CashToCashVO)(workFlowWrapper.getProductVO())).getBalance();
			
			Object[] senderAgentArguments = new Object[] {
					brandName,
					workFlowWrapper.getTransactionCodeModel().getCode(),
					PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{6}
					PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{5}
				};
			
			Object[] agentArguments = new Object[] {
					brandName,
					workFlowWrapper.getTransactionCodeModel().getCode(),
					PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
					PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
					agentBalance
				};
			
			Object[] receiverArguments = new Object[] {
					brandName,
					workFlowWrapper.getTransactionCodeModel().getCode(),
					PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
					PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
				};
			
			String smsTextToAgent = MessageUtil.getMessage("USSD.PendingTransaction.Sender", agentArguments);
			String smsTextToSender = MessageUtil.getMessage("USSD.PendingTransaction.Agent", senderAgentArguments);
			String smsTextToReceiver = MessageUtil.getMessage("USSD.PendingTransaction.Receiver", receiverArguments);
			
			messageList.add(new SmsMessage(agentMobile, smsTextToAgent));
			messageList.add(new SmsMessage(recvMobile, smsTextToReceiver));
			messageList.add(new SmsMessage(senderMobile, smsTextToSender));
			workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

*/
			settlementManager.prepareDataForDayEndSettlement(workFlowWrapper);

			//ActionLog End
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

			logger.info("[CustomerCashManagerImpl.settleCustomerPendingTrx] Successful completion...");
			
			return workFlowWrapper;

		}catch (Exception e){
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
		
	}
	
	private WorkFlowException translateException(WorkFlowException ex){
		String errorMessage = ex.getMessage();
		WorkFlowException outputException = null;
		try{
			if(errorMessage != null && errorMessage.length() == 4 && StringUtil.isInteger(errorMessage)){
				outputException = this.workflowExceptionTranslator.translateWorkFlowException(ex,this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
			}
		}catch(Exception e){
			logger.error("Exception while translating the exception...",e);
		}
		
		if(outputException != null){
			return outputException;
		}else{
			return ex;
		}

	}
	
	
	private void actionLogBeforeStart(ActionLogModel actionLogModel) {

		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if (actionLogModel.getActionLogId() != null) {
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel) {
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		return actionLogModel;
	}

	private void saveSettlementTransactionEntry(Long transactionId,Long productId,Double amount,Long fromAccId,Long toAccId) throws Exception{
		
		SettlementTransactionModel settlementModel = new SettlementTransactionModel();
		settlementModel.setTransactionID(transactionId);
		settlementModel.setProductID(productId);
		settlementModel.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		settlementModel.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);
		settlementModel.setFromBankInfoID(fromAccId);
		settlementModel.setToBankInfoID(toAccId);
		settlementModel.setAmount(amount);
		
		this.settlementManager.saveSettlementTransactionModel(settlementModel);
	}
	
	@Override
	public int countCustomerPendingTrx(String customerCNIC) throws FrameworkCheckedException
	{
		try
		{
			if(StringUtil.isNullOrEmpty(customerCNIC))
			{
				throw new FrameworkCheckedException("Customer CNIC is empty");
			}
			
			List<TransactionDetailMasterModel> trxList = this.transactionDetailMasterDAO.findCustomerPendingTrxByCNIC(customerCNIC);
			
			if(trxList==null)
			{
				return 0;
			}
			else
			{
				return trxList.size();
			}
		}
		catch(Exception e )
		{
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
	}

	@Override
	public int countCustomerPendingTrxByMobile(String recipientMobileNo) throws FrameworkCheckedException
	{
		try
		{
			if(StringUtil.isNullOrEmpty(recipientMobileNo))
			{
				throw new FrameworkCheckedException("Customer Mobile Number is empty");
			}

			List<TransactionDetailMasterModel> trxList = this.transactionDetailMasterDAO.findCustomerPendingTrxByMobile(recipientMobileNo);

			if(trxList==null)
			{
				return 0;
			}
			else
			{
				return trxList.size();
			}
		}
		catch(Exception e )
		{
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
	}

	public void setOlaVeriflyFinancialInstitution(
			AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}
	
	public void setWorkflowExceptionTranslator(WorkFlowExceptionTranslator workflowExceptionTranslator) {
		this.workflowExceptionTranslator = workflowExceptionTranslator;
	}
	
	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager)
	{
		this.retailerContactManager = retailerContactManager;
	}
	
	public void setCommissionManager(CommissionManager commissionManager){
		this.commissionManager = commissionManager;
	}
	
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager)
	{
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

}
