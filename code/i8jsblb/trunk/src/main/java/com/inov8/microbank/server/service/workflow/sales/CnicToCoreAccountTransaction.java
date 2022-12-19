package com.inov8.microbank.server.service.workflow.sales;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;

public class CnicToCoreAccountTransaction extends SalesTransaction
{

	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	SupplierManager supplierManager;
	CommissionManager commissionManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private ProductManager productManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private FinancialIntegrationManager financialIntegrationManager;

	public void setGenericDAO(GenericDao genericDAO)
	{
		this.genericDAO = genericDAO;
	}

	public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager)
	{
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager)
	{
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public CnicToCoreAccountTransaction()
	{
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
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception
	{
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
	    wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		
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
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of CnicToCoreAccountTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		BBToCoreVO productVO = (BBToCoreVO) workFlowWrapper.getProductVO();

		if (!Double.valueOf(Formatter.formatDouble(productVO.getCurrentBillAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getBillAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue())).equals(  Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		/*if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}*/
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of CnicToCoreAccountTransaction...");
		}

	}

	/**
	 * This method calls the settlement module to settle the payment amounts
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper)
	{
		return wrapper;
	}

	/**
	 * This method is responsible for inserting the data into the transaction
	 * tables
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper)
	{
		return wrapper;
	}

	/**
	 * This method is responsible for updating the supplier with the information
	 * for example updating LESCO system with all the bill collections
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper)
	{
		return wrapper;
	}

	/**
	 * Validates the user input
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws Exception
	 */

	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
	{

		// ------------------------Validates the Customer's requirements
		// -----------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction");
		}
		if (wrapper.getAppUserModel() != null)
		{
			if ("".equals(wrapper.getAppUserModel().getMobileNo()))
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
		}
		if (wrapper.getUserDeviceAccountModel() == null)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}

		if (wrapper.getSmartMoneyAccountModel() != null)
		{
			if (!wrapper.getSmartMoneyAccountModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
			}
/*			if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);
			}
*/
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
		}

		// -------------------------------- Validates the Product's requirements
		// ---------------------------------
		if (wrapper.getProductModel() != null)
		{
			if (!wrapper.getProductModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}

		// -------------------------------- Validates the Product's requirements ---------------------------------------------------		
		if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue())
		{
			if (!wrapper.getProductModel().getActive())
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
			}
		}

		// ----------------------- Validates the Supplier's requirements
		// ---------------------------------------------
		if (wrapper.getProductModel().getSupplierIdSupplierModel() != null)
		{
			if (!wrapper.getProductModel().getSupplierIdSupplierModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NULL);
		}

		// ------------------------- Validates the iNPUT's requirements
			
		if (wrapper.getBillAmount() < 0)
		{
			
			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
		}
		if (wrapper.getTotalAmount() < 0)
		{
			
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
		}
		if (wrapper.getTotalCommissionAmount() < 0)
		{
			
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
		}
		if (wrapper.getTxProcessingAmount() < 0)
		{
			
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
		}

		// ----------------------- Validates the PaymentMode's requirements
		// --------------------------------------
		if (wrapper.getPaymentModeModel() != null)
		{
			if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
			{

				throw new WorkFlowException("PaymentModeID is not supplied.");
			}
		}

		// ----------------------- Validates the Service's requirements
		// ---------------------------
		if (wrapper.getProductModel().getServiceIdServiceModel() != null)
		{
			if (!wrapper.getProductModel().getServiceIdServiceModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}

		if (logger.isDebugEnabled())
		{

		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction");
		}
		return wrapper;
	}

	/**
	 * Method respponsible for processing ther Account To Account transaction
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
	{
		TransactionDetailModel txDetailModel = new TransactionDetailModel();

		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doSale(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction..");
		}

		CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		
		// validate commission against the one calculated against the bill and the on coming from client agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		this.validateCommission(commissionWrapper, wrapper); 
		
		wrapper.setCommissionAmountsHolder(commissionAmounts);
	
		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
		wrapper.getTransactionModel().setCreatedOn(new Date());
		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		txDetailModel.setConsumerNo(((BBToCoreVO) wrapper.getProductVO()).getConsumerNo());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		wrapper.getTransactionModel().setFromRetContactId(wrapper.getRetailerContactModel().getRetailerContactId());
		wrapper.getTransactionModel().setFromRetContactName(wrapper.getRetailerContactModel().getName());
		wrapper.getTransactionModel().setBankAccountNo(null);
		txDetailModel.setCustomField11(""+((BBToCoreVO)wrapper.getProductVO()).getAccountNumber());
		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getSenderAppUserModel().getMobileNo());
		
		
		txDetailModel.setSettled(false);
		wrapper.setTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().setConfirmationMessage(" _ ");

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		double senderBalance = 0 ;
		
		logger.info("[CnicToCoreAccountTransaction.doSale] Going to transfer money from Sender to Receiver Customer Account in OLA." + 
				" Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
				" Consumer No:" + txDetailModel.getConsumerNo() + 
				" Trx ID:" + wrapper.getTransactionCodeModel().getCode());
		/*switchWrapper.setFtOrder(1); // First FT
		switchWrapper = abstractFinancialInstitution.debitCreditAccount(switchWrapper);*/
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;

		wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
		senderBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction(); // Sender balance
		
		//set Sender Walkin Customer details in transaction_details table
		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().getCustomField11());
		wrapper.getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getName());
		wrapper.getTransactionDetailModel().setCustomField6(wrapper.getSenderAppUserModel().getMobileNo());
		wrapper.getTransactionDetailModel().setCustomField7(wrapper.getSenderAppUserModel().getNic());
		wrapper.getTransactionDetailModel().setCustomField5(""+wrapper.getWalkInCustomerMob());
		txDetailModel.setSettled(true);
		txManager.saveTransaction(wrapper);
		wrapper.setSwitchWrapper(switchWrapper);
		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

		baseWrapper = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
		baseWrapper.setBasePersistableModel(bankModel);

		AbstractFinancialInstitution abstractFinancialInstitutionRDV = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
		SwitchWrapper switchWrapper2 = new SwitchWrapperImpl();
		switchWrapper2.setWorkFlowWrapper(wrapper) ;
		switchWrapper2.setBankId(CommissionConstantsInterface.BANK_ID);
		switchWrapper2.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);			
		switchWrapper2.setCustomerAccount(wrapper.getCustomerAccount());
		switchWrapper2.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper2.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		
// 		Disabled Title Fetch on JSBL request [19-Oct-2016]
//		switchWrapper2 = abstractFinancialInstitution.titleFetch(switchWrapper2);			
		
		switchWrapper2.setBasePersistableModel(wrapper.getSmartMoneyAccountModel()) ;
		switchWrapper2.setInclusiveChargesApplied(switchWrapper.getInclusiveChargesApplied()); // to be used in CreditAdvice

		logger.info("[CNICToCoreAccountTransaction.doSale] Going to make FT at T24 from Agent A/C to Recipient A/C." + 
				" LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
				" Trx ID:" + wrapper.getTransactionCodeModel().getCode());
		
		switchWrapper2 = abstractFinancialInstitutionRDV.creditAccountAdvice(switchWrapper2) ;
		wrapper.setOlaSwitchWrapper_2(switchWrapper2);
		wrapper.setMiddlewareSwitchWrapper(switchWrapper2); // for day end O.F. settlement of Core FT
		
		
		ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
		productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
		productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );

		List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

		if( list != null && list.size() > 0 )
		{
			productDeviceFlowModel = list.get(0);	
			wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
		}


		this.settleAmount(wrapper); // settle all amounts to the respective stakeholders

		wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;

		((BBToCoreVO)wrapper.getProductVO()).setSenderBalance(senderBalance);
		String brandName = MessageUtil.getMessage("jsbl.brandName");
		
//		{0}\nTrx ID {1}\nYou have sent Rs.{2}\nat {3}\non {4}\ninto JSBL A/C {5}.\nFee is Rs.{6} incl FED.
		String custSMS = this.getMessageSource().getMessage(
				"WalkinCustomerCnicToT24SMS",
				new Object[] { 
						brandName,
						wrapper.getTransactionCodeModel().getCode(),
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
						((BBToCoreVO)wrapper.getProductVO()).getAccountNumber(),
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),

				}, null);

		wrapper.getTransactionModel().setConfirmationMessage(custSMS);
		wrapper.getTransactionDetailModel().setCustomField8(custSMS);

		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		messageList.add(new SmsMessage(wrapper.getSenderAppUserModel().getMobileNo(), custSMS));
		
		switchWrapper.getWorkFlowWrapper().setP2PRecepient(false);

		if(wrapper.getHandlerModel() == null  ||
				(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToAgent())){

	//		{0}\nTrx ID {1}\nYou have transferred Rs.{2}\n at {3}\non {4}\ninto JSBL A/C {5}.\nFee is Rs.{6}\nincl FED\nAvl Bal is Rs.{7}
			String agentSMS = this.getMessageSource().getMessage(
					"AgentCnicToT24SMS",
					new Object[] { 
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),//{0}
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{5}//{6}
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{4}
							((BBToCoreVO)wrapper.getProductVO()).getAccountNumber(),//{1}
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),//{2}
							senderBalance,//{3}
	
					}, null);
	
			wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
			messageList.add(new SmsMessage(((BBToCoreVO)wrapper.getProductVO()).getMobileNo(), agentSMS));
		}

		if(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToHandler()){

			String handlerSMS = this.getMessageSource().getMessage(
					"AgentCnicToT24SMS",
					new Object[] { 
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),//{0}
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{5}//{6}
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{4}
							((BBToCoreVO)wrapper.getProductVO()).getAccountNumber(),//{1}
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),//{2}
							senderBalance,//{3}
	
					}, null);
	
//			wrapper.getTransactionDetailModel().setCustomField4(handlerSMS);
			messageList.add(new SmsMessage(wrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));

		}
		// Set Handler Detail in Transaction and Transaction Detail Master
		if(wrapper.getHandlerModel() != null){
				wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
				wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
				wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
				wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
		}
		
		if(!StringUtil.isNullOrEmpty(wrapper.getWalkInCustomerMob())){
			//{0}\nTrx ID {1}\nYou have received Rs. {2} at {3} on {4} into JSBL A/c {5} from {6}
			String receiverSMS = this.getMessageSource().getMessage(
					"RecipientBBToT24SMS",
					new Object[] { 
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							((BBToCoreVO)wrapper.getProductVO()).getAccountNumber(),
							wrapper.getSenderAppUserModel().getMobileNo()
						}, null);
	
			messageList.add(new SmsMessage(wrapper.getWalkInCustomerMob(), receiverSMS));
//			wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
		}

		SwitchWrapper swWrapper = new SwitchWrapperImpl();
		swWrapper.setWorkFlowWrapper(wrapper) ;
		swWrapper = abstractFinancialInstitution.saveWalkInLedgerEntry(swWrapper);

		txManager.saveTransaction(wrapper);
		this.settlementManager.settleCommission(commissionWrapper, wrapper);
		wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction....");
		}

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		wrapper = super.doPreStart(wrapper);

		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

		// --Setting instruction and success Message
		NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
		baseWrapper.setBasePersistableModel(notificationMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
		baseWrapper.setBasePersistableModel(successMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		// Populate the AGENT Smart Money Account from DB
		/*baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());*/
		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		

		// No need to Populate the SENDER Smart Money Account from DB
/*		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		sma.setCustomerId(wrapper.getSenderAppUserModel().getCustomerId());
		CustomList<SmartMoneyAccountModel> smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(sma);
		if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) {
			sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
			wrapper.setOlaSmartMoneyAccountModel(sma);
			wrapper.setSenderSmartMoneyAccountModel(sma);
		}
*/		
		
		// Populate Handler's Retailer Contact model from DB
		if(wrapper.getHandlerModel() != null){
//			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
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
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction....");
		}
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
	{
		logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction....");
		
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
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Customer model of transaction is populated
//			txModel.setCustomerIdCustomerModel(new CustomerModel());
//			txModel.getCustomerIdCustomerModel().setCustomerId(wrapper.getCustomerModel().getCustomerId());
		
		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(null);

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		txModel.setCustomerMobileNo(wrapper.getSenderAppUserModel().getMobileNo());
		txModel.setSaleMobileNo(wrapper.getSenderAppUserModel().getMobileNo());
		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());//TODO: need to load upon login

		// Populate processing Bank Id
		txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		
		wrapper.setTransactionModel(txModel);
	
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CnicToCoreAccountTransaction....");
		}

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception
	{
		return wrapper;

	}

	public void setSupplierManager(SupplierManager supplierManager)
	{
		this.supplierManager = supplierManager;
	}

	public void setCommissionManager(CommissionManager commissionManager)
	{
		this.commissionManager = commissionManager;
	}

	public void setCustomerManager(CustTransManager customerManager)
	{
		this.customerManager = customerManager;
	}

	public void setProductManager(ProductManager productManager)
	{
		this.productManager = productManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
	{
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of CnicToCoreAccountTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList)
		{
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.EXCLUSIVE_CHARGES.longValue())
			{
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					transProcessingAmount += commissionRateModel.getRate();
				else
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;

				//transProcessingAmount += commissionRateModel.getRate();
				//System.out.println( "  !!!!!!!!!!!!!!!!!!1 " + transProcessingAmount );
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending getTransactionProcessingCharges of CnicToCoreAccountTransaction....");
		}
		return transProcessingAmount;
	}

	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception
	{
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception
	{
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper) throws Exception
	{
		return wrapper;
	}
	
	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		logger.info("[CnicToCoreAccountTransaction.rollback] rollback called..."); 
		/*try{
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			wrapper.getTransactionModel().setTransactionId(null);
			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
			txManager.saveTransaction(wrapper);
		}catch(Exception ex){
			logger.error("Unable to save Transaction details in case of rollback: \n"+ ex.getStackTrace());
		}
		
		if(null != wrapper.getOLASwitchWrapper()){
			logger.info("[CnicToCoreAccountTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
			SmartMoneyAccountModel senderCustomerSMA = wrapper.getSmartMoneyAccountModel();
			SmartMoneyAccountModel recipientCustomerSMA = wrapper.getRecipientSmartMoneyAccountModel();
			wrapper.setSmartMoneyAccountModel(recipientCustomerSMA);
			wrapper.setRecipientSmartMoneyAccountModel(senderCustomerSMA);
			wrapper.setOlaSmartMoneyAccountModel(senderCustomerSMA);
			
			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,true));
			if(null != wrapper.getOlaSwitchWrapper_2()){
				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,false));
			}
		}*/
		logger.info("[CnicToCoreAccountTransaction.rollback] rollback complete..."); 
		return wrapper;
	}
	
	/*
	 * This method swaps sender and recipient OLA Accounts for reversal FT
	 */
	private SwitchWrapper swapAccounts(WorkFlowWrapper wrapper,boolean isFirstFT) throws Exception{
		
		SwitchWrapper olaSwitchWrapper = isFirstFT?wrapper.getOLASwitchWrapper():wrapper.getOlaSwitchWrapper_2();
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

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


}
