package com.inov8.microbank.server.service.workflow.credittransfer;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.CashToCashVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Title: DistributorToDistributorTransaction
 * </p>
 * 
 * <p>
 * Description: Class implements the logic for credit transfer between a
 * distributor contact to another distributor contact
 * </p>
 * 
 * @author Kashif Bashir
 * @version 1.0
 */

public class CashToCashTransaction extends CreditTransferTransaction {
	private DistributorContactManager distributorContactManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private FinancialIntegrationManager financialIntegrationManager;
	private NotificationMessageManager notificationMessageManager;
	private CustTransManager customerManager;
	private CommissionManager commissionManager;
	private ProductDispenseController productDispenseController;

	private MfsAccountManager mfsAccountManager;
	private WalkinCustomerModel recepientWalkinCustomerModel;
	private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
	private BillPaymentProductDispenser cashToCashDispenser;
	private MessageSource messageSource;
	private SmartMoneyAccountManager smartMoneyAccountManager;

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	private WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside loadProductDispenser(WorkFlowWrapper wrapper) of CashToCashTransaction...");
			logger.debug("Loading ProductDispenser...");
		}

		cashToCashDispenser = (BillPaymentProductDispenser) this.productDispenseController
				.loadProductDispenser(wrapper);

		if (logger.isDebugEnabled()) {
			logger.debug("Fetching Bill Info through Product Dispenser...");
		}

		wrapper = cashToCashDispenser.getBillInfo(wrapper);

		return wrapper;
	}

	/**
	 * Credit transfer transaction takes place over here
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @param switchWrapper 
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws Exception {

		Date nowDate = new Date();
		wrapper = this.getBillInfo(wrapper);

		CommissionWrapper commissionWrapper = wrapper.getCommissionWrapper();// this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper
				.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){ 
			commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
			commissionAmounts.setFranchise1CommissionAmount(0.0d);
		}
		
		this.validateCommission(commissionWrapper, wrapper); // validate


		//Removed after otp validation removed
		//this.createMiniTransactionModel(wrapper); //Save Mini Transaction

//		this.createMiniTransactionModel(wrapper); JSBL no Mini transaction required
		
		wrapper.setCommissionAmountsHolder(commissionAmounts);

		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
		wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());
		wrapper.getTransactionModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());
		wrapper.getTransactionModel().setCreatedBy(UserUtils.getCurrentUser().getPrimaryKey());
		wrapper.getTransactionModel().setUpdatedBy(UserUtils.getCurrentUser().getPrimaryKey());
		wrapper.getTransactionModel().setCreatedOn(nowDate);
		wrapper.getTransactionModel().setUpdatedOn(nowDate);

		wrapper.getTransactionModel().setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());
		wrapper.getTransactionModel().setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
		wrapper.getTransactionModel().setDistributorId(wrapper.getDistributorModel().getDistributorId());
		wrapper.getTransactionModel().setFromRetContactId(wrapper.getAppUserModel().getRetailerContactId());

		wrapper.getTransactionModel().setFromRetContactMobNo(wrapper.getAppUserModel().getMobileNo());
		wrapper.getTransactionModel().setMfsId(getMfsId(wrapper.getAppUserModel().getAppUserId()));
		wrapper.getTransactionModel().setFromRetContactName(wrapper.getAppUserModel().getFirstName()+" "+wrapper.getAppUserModel().getLastName());
		wrapper.getTransactionModel().setBankResponseCode(null);
		wrapper.getTransactionModel().setProcessingSwitchId(null);
		wrapper.getTransactionModel().setBankAccountNo(null);
		wrapper.getTransactionModel().setDiscountAmount(0.0D);
		wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
		
		TransactionDetailModel txDetailModel = new TransactionDetailModel();
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		txDetailModel.setCustomField5(wrapper.getRecipientWalkinCustomerModel().getMobileNumber());
		txDetailModel.setCustomField6(wrapper.getWalkInCustomerMob());
		txDetailModel.setCustomField7(wrapper.getSenderWalkinCustomerModel().getCnic());		
		txDetailModel.setCustomField9(wrapper.getRecipientWalkinCustomerModel().getCnic());
		txDetailModel.setSettled(Boolean.FALSE);
		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		wrapper.setTransactionDetailModel(txDetailModel);



		wrapper.getTransactionDetailMasterModel().setSendingRegion(wrapper.getRetailerModel().getRegionModel().getRegionId());
		wrapper.getTransactionDetailMasterModel().setSendingRegionName(wrapper.getRetailerModel().getRegionModel().getRegionName());

		wrapper.getTransactionDetailMasterModel().setSenderAreaId(wrapper.getRetailerContactModel().getAreaId());
		wrapper.getTransactionDetailMasterModel().setSenderAreaName(wrapper.getRetailerContactModel().getAreaName());

		wrapper.getTransactionDetailMasterModel().setSenderDistributorId(wrapper.getDistributorModel().getDistributorId());
		wrapper.getTransactionDetailMasterModel().setSenderDistributorName(wrapper.getDistributorModel().getName());
		if (null != wrapper.getDistributorModel().getMnoId()) {
			wrapper.getTransactionDetailMasterModel().setSenderServiceOPId(wrapper.getDistributorModel().getMnoId());
			wrapper.getTransactionDetailMasterModel().setSenderServiceOPName(wrapper.getDistributorModel().getMnoModel().getName());
//			wrapper.getTransactionDetailMasterModel().setSenderServiceOPName(wrapper.getDistributorModel().getMnoId());
		}

		// Set Handler Detail in Transaction and Transaction Detail Master
		if(wrapper.getHandlerModel() != null){
				wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
				wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
				wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
				wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
		}

		txManager.saveTransaction(wrapper);
		
		// Walk in customer ledger entry
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper) ;

		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		switchWrapper = abstractFinancialInstitution.saveWalkinCustomerLedgerEntry(switchWrapper);
		
		this.settlementManager.settleCommission(commissionWrapper, wrapper);

		switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// Agent SMA

		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;

		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

		double agentBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();
		((CashToCashVO)(wrapper.getProductVO())).setBalance(agentBalance);
	    
//		SwitchWrapper switchWrapper2 = new SwitchWrapperImpl();
//		switchWrapper2.setWorkFlowWrapper(wrapper);
//		switchWrapper2.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// Agent SMA
//		switchWrapper2.setFtOrder(2);
//		switchWrapper2.setSkipAccountInfoLoading(true);
//		
//		logger.info("[CashToCashTransaction.doCreditTransfer()] Going to transfer funds from Cash-to-Cash Sundry to Commission Recon Account. Transaction ID: " + switchWrapper2.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
//		switchWrapper2 = abstractFinancialInstitution.debitCreditAccount(switchWrapper2);
//		
//		wrapper.setOlaSwitchWrapper_2(switchWrapper2); //setting the switchWrapper2 for rollback
//
//		
//		if(wrapper.getCommissionAmountsHolder().getFranchise1CommissionAmount() > 0.0d){
//			SwitchWrapper switchWrapper3 = new SwitchWrapperImpl();
//			switchWrapper3.setWorkFlowWrapper(wrapper);
//			switchWrapper3.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// Agent SMA
//			switchWrapper3.setFtOrder(3);
//			switchWrapper3.setSkipAccountInfoLoading(true);
//			
//			logger.info("[CashToCashTransaction.doCreditTransfer()] Going to transfer funds from Cash-to-Cash Sundry to Franchise Account. Transaction ID: " + switchWrapper2.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
//			switchWrapper3 = abstractFinancialInstitution.debitCreditAccount(switchWrapper3);
//			
//			wrapper.setOlaSwitchWrapper_3(switchWrapper3); //setting the switchWrapper3 for rollback
//		}
//		
//		txDetailModel.setVersionNo(Integer.valueOf(0));
		txDetailModel.setCustomField1(null);
//		txDetailModel.setCustomField2(switchWrapper.getToAccountNo());
		// this amount is to be disbursed to customer at the time of account opening
		txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount() - switchWrapper.getInclusiveChargesApplied());

		this.sendSMS(wrapper);
		
		txDetailModel.setSettled(Boolean.FALSE);
		
		txManager.saveTransaction(wrapper);

		((CashToCashVO) (wrapper.getProductVO())).setTotalAmount(commissionAmounts.getTotalAmount());
		((CashToCashVO) (wrapper.getProductVO())).setTxAmount(commissionAmounts.getTransactionAmount());
		((CashToCashVO) (wrapper.getProductVO())).setCommissionAmount(commissionAmounts.getTransactionProcessingAmount());

		return wrapper;

	}

	
	private void sendSMS(WorkFlowWrapper workFlowWrapper) throws Exception {

		Double trxAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount();
		Double serviceCharges = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
		Double inclusiveChargesApplied = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getOLASwitchWrapper().getInclusiveChargesApplied()); //Inclusive Charges Applied
		Double agentBalance = ((CashToCashVO)(workFlowWrapper.getProductVO())).getBalance();
		String brandName = null;
		if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(Long.valueOf(50028L))) {
			brandName = MessageUtil.getMessage("sco.brandName");
		} else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}

		
//		{0}\nTrx ID {1}\nYou have sent Rs.{2}\nto {3}\nat {4}\non {5}.\nFee is Rs.{6} (incl FED {7})
		Object[] customerSmsArguments = new Object[] {
				brandName,
				workFlowWrapper.getTransactionCodeModel().getCode(),//{3}	
				Formatter.formatDouble(trxAmount),//{0}
				workFlowWrapper.getRecipientWalkinCustomerModel().getCnic(),
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{6}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{5}
				Formatter.formatDouble(serviceCharges),//{1}
//				Formatter.formatDouble(serviceCharges),//{1}
//				Formatter.formatDouble(trxAmount + serviceCharges),//{2}
				//workFlowWrapper.getOneTimePin(),
			};
		
//		{0}\nTrx ID {1}\nYou have sent Rs.{2}\nat {3}\non {4}.\n Fee is Rs.{5} incl FED\nAvl Bal:Rs.{6}
		Object[] agentSmsArguments = new Object[] {
				brandName,
				workFlowWrapper.getTransactionCodeModel().getCode(),
				Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
				Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
				agentBalance
			};
		
//		{0}\nTrx ID {1}\nRs.{2}sent by {3}\nat {4}\non {5}.\nPls collect funds from nearest {6} Agent.\nPls bring Original CNIC
		Object[] recipientWalkinSmsArguments = new Object[] {
				brandName,
				workFlowWrapper.getTransactionCodeModel().getCode(),
				Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount() - inclusiveChargesApplied),
				workFlowWrapper.getSenderWalkinCustomerModel().getCnic(),
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
				brandName
			};

		try {
			
			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

			//sending SMS to Walkin Customer Recepient.			
			String smsText = this.getMessageSource().getMessage("USSD.CustomerCashToCashSMS", customerSmsArguments, null);
			messageList.add(new SmsMessage(workFlowWrapper.getWalkInCustomerMob(), smsText));
			workFlowWrapper.getTransactionDetailModel().setCustomField8(smsText.substring(0, smsText.length()-5)+" *****"); //masked as code was being saved in transaction_detail's custom_field8
			
			if(workFlowWrapper.getHandlerModel() == null  || (workFlowWrapper.getHandlerModel() != null && workFlowWrapper.getHandlerModel().getSmsToAgent())){
				//sending SMS to Agent.
				smsText = this.getMessageSource().getMessage("USSD.AgentCashToCashSMS", agentSmsArguments, null);	
				messageList.add(new SmsMessage(workFlowWrapper.getAppUserModel().getMobileNo(), smsText));
				workFlowWrapper.getTransactionDetailModel().setCustomField4(smsText);
			}

			if(workFlowWrapper.getHandlerModel() != null && workFlowWrapper.getHandlerModel().getSmsToHandler()){
				smsText = this.getMessageSource().getMessage("USSD.AgentCashToCashSMS", agentSmsArguments, null);	
				messageList.add(new SmsMessage(workFlowWrapper.getHandlerAppUserModel().getMobileNo(), smsText));
			}

			//sending SMS to Walkin Customer Recipient.
			smsText = this.getMessageSource().getMessage("USSD.WalkinRecipientCashToCashLeg1SMS", recipientWalkinSmsArguments, null);	
			messageList.add(new SmsMessage(workFlowWrapper.getRecipientWalkinCustomerModel().getMobileNumber(), smsText));
			workFlowWrapper.getTransactionDetailModel().setCustomField10(smsText);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
		}
			
	}
	
	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper)
			throws Exception {
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper)
			throws Exception {
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception {
		
//		String code = wrapper.getTransactionCodeModel().getCode();
//		logger.info("[CashToCashTransaction.rollback] Rolling back C2C transaction with  ID: " + code);
//		try{
//			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
//			wrapper.getTransactionModel().setTransactionId(null);
//			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
//			txManager.saveTransaction(wrapper);
//		}catch(Exception ex){
//			logger.error("Unable to save CashToCashTransaction details in case of rollback: \n"+ ex.getStackTrace());
//		}
		
//		if(null != wrapper.getOLASwitchWrapper()){
//			logger.info("[CashToCashTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
//			BaseWrapper baseWrapper = new BaseWrapperImpl();
//			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
//			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
//			
//			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,1));
//			if(null != wrapper.getOlaSwitchWrapper_2()){
//				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,2));
//			}
//			if(null != wrapper.getOlaSwitchWrapper_3()){
//				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,3));
//			}
//		}
		logger.info("[CashToCashTransaction.rollback] rollback complete..."); 
		
		return wrapper;
	}

	/*
	 * This method swaps sender and recipient OLA Accounts for reversal FT
	 */
	private SwitchWrapper swapAccounts(WorkFlowWrapper wrapper,int ftOrder) throws Exception{
		
		SwitchWrapper olaSwitchWrapper = wrapper.getOLASwitchWrapper();
		
		if(ftOrder == 2){
			olaSwitchWrapper = wrapper.getOlaSwitchWrapper_2();
		}else if(ftOrder == 3){
			olaSwitchWrapper = wrapper.getOlaSwitchWrapper_3();
		}
		
		OLAVO olaVO = olaSwitchWrapper.getOlavo();
		String fromAccountNo = olaSwitchWrapper.getFromAccountNo();
		String toAccountNo = olaSwitchWrapper.getToAccountNo();
		olaSwitchWrapper.setFromAccountNo(toAccountNo);
		olaSwitchWrapper.setToAccountNo(fromAccountNo);
		olaVO.setPayingAccNo(toAccountNo);
		olaVO.setReceivingAccNo(fromAccountNo);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
		switchWrapper.setBankId(wrapper.getOlaSmartMoneyAccountModel().getBankId());
		switchWrapper.setWorkFlowWrapper(wrapper);

		return switchWrapper;
	}
	private String getMfsId(Long appUserId) throws FrameworkCheckedException {
		
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(appUserId);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		
		String userId = null;
		
		try {
			baseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
						
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			
			userId = userDeviceAccountsModel.getUserId();		
			
		} catch (FrameworkCheckedException e) {

			throw new FrameworkCheckedException(e.getMessage());
		}
		
		return userId;		
	}
	
	private Double checkBalance(WorkFlowWrapper workFlowWrapper, SwitchWrapper switchWrapper, 
				AbstractFinancialInstitution financialInstitution) throws FrameworkCheckedException, Exception {

		SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();

		switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC,	ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
		switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
		switchWrapperTemp.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
		switchWrapperTemp.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapperTemp.getWorkFlowWrapper().setCashToCashLeg1(true);
		
		if (workFlowWrapper.getProductModel().getProductId() == 50011L) {
			workFlowWrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
			workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);

			switchWrapperTemp = financialInstitution.checkBalanceWithoutPin(switchWrapperTemp);
		}

		return switchWrapperTemp.getBalance();
	}

	 private void createMiniTransactionModel(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

		 try {
			
			WalkinCustomerModel walkinSenderModel = workFlowWrapper.getSenderWalkinCustomerModel();
			TransactionCodeModel transactionCodeModel = workFlowWrapper.getTransactionCodeModel();
			MiniTransactionModel miniTransactionModel = workFlowWrapper.getMiniTransactionModel();
			
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setNic(walkinSenderModel.getCnic());
			appUserModel.setMobileNo(walkinSenderModel.getMobileNumber());
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(appUserModel);
			
			logger.info("[CashToCashTransaction.createMiniTransactionModel] Loading appUser of Sender Walkin Customer with CNIC:" + walkinSenderModel.getCnic() + ". Mobile No:" + walkinSenderModel.getMobileNumber() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			searchBaseWrapper =  mfsAccountManager.loadAppUserByMobileNumberAndType(searchBaseWrapper);
			appUserModel = (AppUserModel)searchBaseWrapper.getBasePersistableModel();
			if (appUserModel == null) {
				logger.error("[CashToCashTransaction.createMiniTransactionModel] No record found for Sender Walkin Customer with CNIC:" + walkinSenderModel.getCnic() + ". Mobile No:" + walkinSenderModel.getMobileNumber() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				throw new WorkFlowException(WorkFlowErrorCodeConstants.UNKNOWN_ERROR_MSG);
			} 
			
			miniTransactionModel.setAppUserId(appUserModel.getAppUserId()) ;
			
			miniTransactionModel.setTransactionCodeIdTransactionCodeModel(transactionCodeModel);
			miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
			miniTransactionModel.setTimeDate(new Date()) ;
	
			miniTransactionModel.setTAMT(workFlowWrapper.getTransactionAmount()) ;
			miniTransactionModel.setCAMT(workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount()) ;//TODO put commissionAmount variable here
			miniTransactionModel.setTPAM(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()) ;
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
			
			baseWrapper = txManager.saveMiniTransaction(baseWrapper) ;

		 } catch (Exception e) {
			 e.printStackTrace();
			 throw new WorkFlowException(e.getMessage());
		 }
	}


	/**
	 * doValidate
	 * 
	 * @param wrapper
	 * @return WorkFlowWrapper
	 * @throws Exception
	 */
	protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

		// this.populateTransactionObject(wrapper);
		DistributorContactModel toDistributorContactModel = wrapper.getToDistributorContactModel();

		/*
		 * if(fromDistributorContactModel.getDistributorIdDistributorModel().
		 * getNational() && !toDistributorContactModel.getHead()) { throw new
		 * WorkFlowException
		 * (WorkFlowErrorCodeConstants.TO_FROM_DISTRIBUTOR_SAME_LEVEL); }
		 */

		/*
		 * // Validates the Distributor's requirements if
		 * (toDistributorContactModel != null) { if
		 * (!toDistributorContactModel.getActive()) { throw new
		 * WorkFlowException
		 * (WorkFlowErrorCodeConstants.TODISTRIBUTOR_NOT_ACTIVE); } if
		 * (!toDistributorContactModel
		 * .getDistributorIdDistributorModel().getActive()) { throw new
		 * WorkFlowException
		 * (WorkFlowErrorCodeConstants.TODIST_DISTRIBUTOR_INACTIVE); } } else {
		 * throw new
		 * WorkFlowException(WorkFlowErrorCodeConstants.TODISTRIBUTOR_NULL); }
		 * 
		 * if(wrapper.getDistributorNmContactModel() == null) { throw new
		 * WorkFlowException(WorkFlowErrorCodeConstants.NO_NM_FOR_DISTRIBUTOR);
		 * }
		 */

		if (wrapper.getUserDeviceAccountModel() == null) {
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}

		// Validates the workflowWrapper's requirements
		if (wrapper.getTransactionAmount() <= 0) {
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
		}
		
		return wrapper;
	}

	public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {

		return wrapper;
	}

	protected WorkFlowWrapper doCreditTransferStart(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	protected WorkFlowWrapper doCreditTransferEnd(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * Populates the transaction object with all the necessary data to save it
	 * in the db.
	 * 
	 * @param wrapper
	 * @return WorkFlowWrapper
	 */
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		wrapper = super.doPreStart(wrapper);

		UserDeviceAccountsModel userDeviceAccountsModel = getUserDeviceAccountsModel(wrapper.getAppUserModel().getAppUserId());
		wrapper.setUserDeviceAccountModel(userDeviceAccountsModel);

		// wrapper.setCustomerModel(new CustomerModel());
		// wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());

		// --Setting instruction and success Message
		NotificationMessageModel notificationMessage = getNotificationMessageModel(wrapper.getProductModel().getInstructionId());
		wrapper.setInstruction(notificationMessage);

		NotificationMessageModel successMessage = getNotificationMessageModelBySuccessId(wrapper.getProductModel().getSuccessMessageId());
		wrapper.setSuccessMessage(successMessage);

		// Populate the Customer model from DB
		// baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
		// baseWrapper = customerManager.loadCustomer(baseWrapper);
		// wrapper.setCustomerModel((CustomerModel)
		// baseWrapper.getBasePersistableModel());

		// Populate the Smart Money Account from DB
		// baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		// baseWrapper =
		// smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
		// wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel)
		// baseWrapper.getBasePersistableModel());

		PaymentModeModel paymentModeModel = new PaymentModeModel();
		paymentModeModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		wrapper.setPaymentModeModel(paymentModeModel);

		if( wrapper.getTransactionDetailMasterModel() != null){
			if(wrapper.getObject(CommandFieldConstants.KEY_MAN_OT_PIN) != null) {
				String isManualOTPinStr = wrapper.getObject(CommandFieldConstants.KEY_MAN_OT_PIN).toString();
				if (StringUtils.isNotEmpty(isManualOTPinStr)) {
					wrapper.getTransactionDetailMasterModel().setIsManualOTPin(true);
				}
			}
		}
		// Added by mudassir - Populate the walk in customer Smart Money Account
		// from DB
		// WalkinCustomerModel walkinCustomerModel =
		// mfsAccountManager.getWalkinCustomerModel(wrapper.getRecipientWalkinCustomerModel());
		// wrapper.setRecipientWalkinCustomerModel(walkinCustomerModel);

		// SmartMoneyAccountModel smartMoneyAccountModel =
		// wrapper.getRecipientWalkinSmartMoneyAccountModel();
		// smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
		// //
		// smartMoneyAccountModel.setWalkinCustomerIdWalkinCustomerModel(walkinCustomerModel);
		// SmartMoneyAccountModel walkinRecipeientSMA =
		// mfsAccountManager.getSmartMoneyAccount(smartMoneyAccountModel);
		//
		// wrapper.setRecipientWalkinSmartMoneyAccountModel(walkinRecipeientSMA);
		

		// Populate Handler's Retailer Contact model from DB
		if(wrapper.getHandlerModel() != null){
//			searchBaseWrapper = new SearchBaseWrapperImpl();
//			RetailerContactModel retailerContact = new RetailerContactModel();
//			retailerContact.setRetailerContactId( wrapper.getHandlerModel().getRetailerContactId() );
//			searchBaseWrapper.setBasePersistableModel( retailerContact );
//			searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
//			wrapper.setHandlerRetContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

			// Populate the Handler OLA Smart Money Account from DB
			SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(wrapper.getHandlerModel().getHandlerId());
			wrapper.setHandlerSMAModel(sma);

			// Set Handler User Device Account Model
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
		
		wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		txModel.setTransactionAmount(wrapper.getBillAmount());
		txModel.setTotalAmount(wrapper.getTransactionAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
		txModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
//		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		txModel.setSmartMoneyAccountId(null);

		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());

		txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());


		wrapper.setTransactionModel(txModel);
		if (logger.isDebugEnabled()) {
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of AccountToCashTransaction....");
		}

		return wrapper;
	}

	/**
	 * This method calls the commission module to calculate the commission on
	 * this product and transaction.The wrapper should have product,payment mode
	 * and principal amount that is passed onto the commission module
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
		// ------ Calculate the commission
		// ------------------------------------------------------
		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		// ----------------------Maqsood Shahzad -----------------------

		if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(custModel);
			bWrapper = customerManager.loadCustomer(bWrapper);
			if (null != bWrapper.getBasePersistableModel()) {
				custModel = (CustomerModel) bWrapper.getBasePersistableModel();
				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
			}
		}

		// --------------------------------------------------------------

		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled()) {
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		return commissionWrapper;
	}

	/**
	 * 
	 * @param commissionHolder
	 *            CommissionAmountsHolder
	 * @param calculatedCommissionHolder
	 *            CommissionAmountsHolder
	 * @throws FrameworkCheckedException
	 */
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		java.text.DecimalFormat fourDForm = new java.text.DecimalFormat("#.####");
	
		if (logger.isDebugEnabled()) {
			logger.debug("Inside validateCommission of BillSaleTransaction...");
		}
		
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts	.getUserDeviceAccountsModel();
		CashToCashVO productVO = (CashToCashVO) workFlowWrapper.getProductVO();

		// if
		// (!Double.valueOf(Formatter.formatDouble(productVO.getTxAmount().doubleValue())).equals(
		// Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTransactionAmount().doubleValue()))))
		// {
		//
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		// }
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue())).
				equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue())))) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue())).
				equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue())))) {

			// throw new
			// WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
			if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount()) {

				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
			}
		}

		// if
		// (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper,
		// workFlowWrapper).doubleValue())).equals(
		// Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
		// {
		//
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		// }
		if (logger.isDebugEnabled()) {

			logger.debug("Ending validateCommission of BillSaleTransaction...");
		}

	}

	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside getTransactionProcessingCharges of BillSaleTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = 
			(List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) {
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.EXCLUSIVE_CHARGES	.longValue()) {
				
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					
					transProcessingAmount += commissionRateModel.getRate();
				
				else
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Ending getTransactionProcessingCharges of BillSaleTransaction....");
		}
		
		return transProcessingAmount;
	}

	private UserDeviceAccountsModel getUserDeviceAccountsModel(Long appUserId) throws CommandException {

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(appUserId);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

		try {

			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);

		} catch (FrameworkCheckedException e) {

			if (logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();

	}

	private NotificationMessageModel getNotificationMessageModelBySuccessId(Long successMessageId) throws CommandException {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(successMessageId);
		baseWrapper.setBasePersistableModel(successMessage);

		try {

			baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);

		} catch (FrameworkCheckedException e) {

			if (logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (NotificationMessageModel) baseWrapper.getBasePersistableModel();
	}

	private NotificationMessageModel getNotificationMessageModel(Long productInstructionId) throws CommandException {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(productInstructionId);
		baseWrapper.setBasePersistableModel(notificationMessage);

		try {

			baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);

		} catch (FrameworkCheckedException e) {

			if (logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (NotificationMessageModel) baseWrapper.getBasePersistableModel();
	}

	public void setDistributorContactManager(DistributorContactManager distributorContactManager) {
		this.distributorContactManager = distributorContactManager;
	}

	public DistributorContactManager getDistributorContactManager() {
		return distributorContactManager;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}

	public void setProductDispenseController(ProductDispenseController productDispenseController) {
		this.productDispenseController = productDispenseController;
	}

	public WalkinCustomerModel getRecepientWalkinCustomerModel() {
		return recepientWalkinCustomerModel;
	}

	public void setRecepientWalkinCustomerModel(WalkinCustomerModel recepientWalkinCustomerModel) {
		this.recepientWalkinCustomerModel = recepientWalkinCustomerModel;
	}

	public SmartMoneyAccountModel getRecepientWalkinSmartMoneyAccountModel() {
		return recepientWalkinSmartMoneyAccountModel;
	}

	public void setRecepientWalkinSmartMoneyAccountModel(SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel) {
		this.recepientWalkinSmartMoneyAccountModel = recepientWalkinSmartMoneyAccountModel;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}


}