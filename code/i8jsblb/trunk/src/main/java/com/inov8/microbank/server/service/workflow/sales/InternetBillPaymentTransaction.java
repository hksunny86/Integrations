package com.inov8.microbank.server.service.workflow.sales;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ReasonConstants;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
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
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.P2PVO;
import com.inov8.microbank.server.service.integration.vo.RetailPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;

/**
 * 
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */

public class InternetBillPaymentTransaction extends SalesTransaction
{

	protected final Log log = LogFactory.getLog(getClass());
	
	private MessageSource messageSource;

	SupplierManager supplierManager;

	CommissionManager commissionManager;

	SettlementManager settlementManager;

	private ProductDispenseController productDispenseController;

	private SmartMoneyAccountManager smartMoneyAccountManager;

	private CustTransManager customerManager;

	private SmsSender smsSender;

	private ProductManager productManager;

	private AppUserManager appUserManager;

	BillPaymentProductDispenser billSaleProductDispenser;

	private VeriflyManagerService veriflyController;

	private SwitchController switchController;

	private UserDeviceAccountsManager userDeviceAccountsManager;

	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private SupplierBankInfoManager supplierBankInfoManager;

	private OperatorManager operatorManager;
	
	private RetailerContactManager retailerContactManager;

	private FinancialIntegrationManager financialIntegrationManager;
	
	private AbstractFinancialInstitution phoenixFinancialInstitution;

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

	public InternetBillPaymentTransaction()
	{
	}

	/**
	 * Pulls the bill information from the supplier system
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of BillSaleTransaction...");
			logger.debug("Loading ProductDispenser...");
		}
		
		billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Fetching Bill Info through Product Dispenser...");
		}

		wrapper = billSaleProductDispenser.getBillInfo(wrapper);		
		
		return wrapper;
	}

	/**
	 * Validate input from the user against the information pulled from the
	 * supplier
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		wrapper = billSaleProductDispenser.verify(wrapper);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of BillSaleTransaction...");
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
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception
	{
		// ------ Calculate the commission
		// ------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		//----------------------Maqsood Shahzad -----------------------
		
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
		{
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(custModel);
			bWrapper = customerManager.loadCustomer(bWrapper);
			if(null != bWrapper.getBasePersistableModel())
			{
				custModel = (CustomerModel) bWrapper.getBasePersistableModel();
				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
			}
		}
		
		
		
		//--------------------------------------------------------------
		
		
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
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
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		 java.text.DecimalFormat fourDForm = new java.text.DecimalFormat("#.####");
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of BillSaleTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		BillPaymentVO productVO = (BillPaymentVO) workFlowWrapper.getProductVO();

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
		/*if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
		{
			if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
			}
		}*/

		if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of BillSaleTransaction...");
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
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of BillSaleTransaction");
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
			if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);
			}
			if(null != ThreadLocalAppUser.getAppUserModel().getCustomerId()) // for CW. Threadlocal user is agent hence this if
			{
				if (!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
				{

					throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
				}
			}
			else
			{
				if (!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(wrapper.getCustomerAppUserModel().getCustomerId().toString()))
				{

					throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
				}
			}
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

		// ----------------------- Validates the Supplier Bank Info requirements
		// -----------------
		if (wrapper.getSupplierBankInfoModel() == null || wrapper.getSupplierBankInfoModel().getAccountNo() == null
				|| "".equals(wrapper.getSupplierBankInfoModel().getAccountNo()))
		{

			logger.error("Either SupplierBankInfo or account No is null");
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
		}
		// ---------------------------------------------------------------------------------------

		// ----------------------- Validates the operator Bank Info requirements
		// -----------------
		if (wrapper.getOperatorBankInfoModel() == null)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
		}
		if (wrapper.getOperatorPayingBankInfoModel() == null)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
		}
		// ---------------------------------------------------------------------------------------

		// ------------------------- Validates the iNPUT's requirements
		// -----------------------------------
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
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of BillSaleTransaction");
		}
		return wrapper;
	}

	/**
	 * Method respponsible for processing ther Bill Sale transaction
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
	{
		TransactionDetailModel txDetailModel = new TransactionDetailModel();

		/**
		 * pull the bill information from the supplier, calculate commissions on
		 * the bill and return in to iPos for the customer to accept or reject
		 */
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Inside doSale(WorkFlowWrapper wrapper) of BillSaleTransaction..");
			}
			wrapper = this.getBillInfo(wrapper);
			// this.validateBillInfo(wrapper); //validate bill info against the
			// one coming from iPos to see if they match

			CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
			CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
					CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
//			this.validateCommission(commissionWrapper, wrapper); // validate
			// commission
			// against
			// the one
			// calculated
			// against
			// the bill
			// and the
			// one
			// coming
			// from iPos

			//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
			if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
				commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
				commissionAmounts.setFranchise1CommissionAmount(0.0d);
			}
			
			wrapper.setCommissionAmountsHolder(commissionAmounts);

			wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
			wrapper.getTransactionModel().setCreatedOn(new Date());
			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
			txDetailModel.setConsumerNo(((BillPaymentVO) wrapper.getProductVO()).getConsumerNo());
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
			

			wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));

			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
			String txAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount());
			String seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());

			txDetailModel.setSettled(false);
			wrapper.setTransactionDetailModel(txDetailModel);
			wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

			if (!wrapper.getTransactionDetailModel().getConsumerNo().equalsIgnoreCase(""))
				wrapper.getTransactionModel().setConfirmationMessage(
						SMSUtil.buildBillSaleSMS(wrapper.getInstruction().getSmsMessageText(), wrapper.getProductModel().getName(), txAmount, seviceChargesAmount,
								wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), wrapper.getTransactionDetailModel().getConsumerNo()));

			else
				wrapper.getTransactionModel().setConfirmationMessage(
						SMSUtil.buildVariableProductSMS(wrapper.getInstruction().getSmsMessageText(), txAmount, wrapper.getTransactionModel()
								.getTransactionCodeIdTransactionCodeModel().getCode(), wrapper.getProductModel().getHelpLineNotificationMessageModel()
								.getSmsMessageText()));

			if (logger.isDebugEnabled())
			{
				logger.debug("Saving Transaction in DB....");
			}

			txManager.saveTransaction(wrapper);

			if (logger.isDebugEnabled())
			{
				logger.debug("Going to settle commissions using SettlementManager....");
			}

			this.settlementManager.settleCommission(commissionWrapper, wrapper);

			if (logger.isDebugEnabled())
			{
				logger.debug("Going to settle Bank Payment using SettlementManager....");
			}
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(wrapper);
//			if (wrapper.getUserDeviceAccountModel().getCommissioned())
//			{
//
//				switchWrapper.setDiscountAmount(wrapper.getProductModel().getFixedDiscount() + (wrapper.getProductModel().getPercentDiscount() / 100)
//						* wrapper.getTransactionModel().getTotalAmount());
//				logger.debug("User is commissioned so we need to set the discount which is " + switchWrapper.getDiscountAmount());
//				switchWrapper.setCommissioned(true);
//			}
			
//			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50006L){
//				
//				wrapper = billSaleProductDispenser.doSale(wrapper);
//				
//			}
//			
//			SwitchWrapper phoenixSwitchWrapper = wrapper.getSwitchWrapper();
			double balance = 0 ;

			try
			{
				logger.info("[BillSaleTransaction.doSale] Going to Debit Customer Account in OLA." + 
							" Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" Consumer No:" + txDetailModel.getConsumerNo() + 
							" Trx ID:" + wrapper.getTransactionCodeModel().getCode());
				switchWrapper = abstractFinancialInstitution.transaction(switchWrapper);
			}
			
			catch(CommandException ce)
			{
				throw new CommandException(ce.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			}
			
			
			
//			if ( wrapper.getProductModel().getProductId() == 50006 || wrapper.getProductModel().getProductId() == 50004 )
			{
				/*
				AppUserModel tempAppUser = ThreadLocalAppUser.getAppUserModel(); // smart money account is that of customer but the logged in user is the retailer
				if(null != wrapper.getCustomerAppUserModel())
				{
					ThreadLocalAppUser.setAppUserModel(wrapper.getCustomerAppUserModel());
				}
				SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
				TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
				
				switchWrapperTemp.setWorkFlowWrapper(wrapper);
				switchWrapperTemp.setTransactionTransactionModel(wrapper.getTransactionModel());
				switchWrapperTemp.setBasePersistableModel( wrapper.getSmartMoneyAccountModel() ) ;
				
				switchWrapperTemp = abstractFinancialInstitution.checkBalance(switchWrapperTemp);
//				balance = switchWrapperTemp.getBalance() ;
				
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				ThreadLocalAppUser.setAppUserModel(tempAppUser);
			*/
			}

			//			this.settlementManager.settleBankPayment(commissionWrapper, wrapper);
				
				
//			
			
			wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
			balance = switchWrapper.getBalance();
			txDetailModel.setSettled(true);
			txManager.saveTransaction(wrapper);
			if(wrapper.getProductModel().getProductId() == 50006L){
				switchWrapper.setAgentBalance(((RetailPaymentVO)wrapper.getProductVO()).getBalance());
				wrapper.setSwitchWrapper(switchWrapper);
				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getPrimaryKey()+"");
				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(StringUtil.replaceString(switchWrapper.getAccountInfoModel().getAccountNo(), 5, "*"));
            	switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId()+"");
            	
				
			}else if(wrapper.getProductModel().getProductId() == 50000L){
				wrapper.setSwitchWrapper(switchWrapper);
				wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
				wrapper.setOLASwitchWrapper(switchWrapper);
				wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel)switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel());
			}else if(InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))){
				wrapper.setSwitchWrapper(switchWrapper);
//				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getPrimaryKey()+"");
//				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(StringUtil.replaceString(switchWrapper.getAccountInfoModel().getAccountNo(), 5, "*"));
//            	switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId()+"");
				wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
				wrapper.getTransactionModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());
            	wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel)switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel());
				
			}
			
			
			//Maqsood Shahzad -- need to set the account info model in workflowWrapper
			
//			wrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
			//------------------------------------------------------------------------
			
			ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
			productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
			productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );
			
			List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

			if( list != null && list.size() > 0 )
			{
				productDeviceFlowModel = list.get(0);	
				wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
			}
			
			
			
			// -----------------------------------------------------------------------

			this.settleAmount(wrapper); // settle all amounts to the respective
			// stakeholders
			wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstantsInterface.COMPLETED ) ;
			txManager.saveTransaction(wrapper); // save the transaction
			if (logger.isDebugEnabled())
			{
				logger.debug("Executing Bill Sale on BillSaleProductDispenser....");
			}
			// Calls the integration party
		
			
//			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() != 50006L){
				
				wrapper = billSaleProductDispenser.doSale(wrapper);
//			}
			
			
			
			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50006L){
				
				abstractFinancialInstitution.settleCashWithdrawalCommission(wrapper.getSwitchWrapper());
				wrapper.setOLASwitchWrapper(wrapper.getSwitchWrapper());
				wrapper.getSwitchWrapper().setAgentBalance(((RetailPaymentVO)wrapper.getProductVO()).getBalance());
				wrapper.getTransactionModel().setSmartMoneyAccountId(wrapper.getSwitchWrapper().getBasePersistableModel().getPrimaryKey());
				wrapper.getTransactionModel().setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName()+" "+ThreadLocalAppUser.getAppUserModel().getLastName());
				wrapper.getTransactionModel().setProcessingSwitchId(wrapper.getSwitchWrapper().getSwitchSwitchModel().getSwitchId());
				
				
			}
			else if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50000L){
				
//				phoenixFinancialInstitution.settleP2PCommission(wrapper.getSwitchWrapper());
				abstractFinancialInstitution.settleP2PCommission(wrapper.getSwitchWrapper());
			}
			
			
			
						
			if (wrapper.getDeviceTypeModel().getDeviceTypeId().longValue() != DeviceTypeConstantsInterface.ALL_PAY.longValue())
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Sending SMS to subscriber....");
				}

				if (!(wrapper.getCustomerAppUserModel().getMobileNo().trim().equals(wrapper.getAppUserModel().getMobileNo())) || wrapper.getProductModel().getProductId() == 50006)
				{
					
					
					// CASHOUT
					if( wrapper.getProductModel().getProductId() == 50006 )
					{
						String sms = this.getMessageSource().getMessage(
								"USSD.AgentPayCashSMS",
								new Object[] { 
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
										wrapper.getCustomerAppUserModel().getMobileNo(),
										wrapper.getCustomerAppUserModel().getFirstName() +" "+wrapper.getCustomerAppUserModel().getLastName(),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT)	+ " "+
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
										wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount(),
										wrapper.getTransactionCodeModel().getCode()}, 
										null);
								
						//send sms to agent
						this.smsSender.send(new SmsMessage(ThreadLocalAppUser.getAppUserModel().getMobileNo(), sms));
						wrapper.getTransactionDetailModel().setCustomField4(sms);
						//send sms to customer
						sms = this.getMessageSource().getMessage(
								"USSD.CustomerPayCashSMS",
								new Object[] {
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
										ThreadLocalAppUser.getAppUserModel().getMobileNo(),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
										Formatter.formatDouble(balance), 
										wrapper.getTransactionCodeModel().getCode()
										
										}, null);
						this.smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), sms));
						/*this.smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), "You have successfully performed Cash Withdrawal of" +
								" Rs. "+wrapper.getTotalAmount()+" through agent "+ThreadLocalAppUser.getAppUserModel().getFirstName()+" "+ThreadLocalAppUser.getAppUserModel().getLastName()+" "+ThreadLocalAppUser.getAppUserModel().getMobileNo()+
						        " on "+PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT)+" at "+PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT)+
						        ". After deduction of Charges Rs. "+wrapper.getTransactionModel().getTotalCommissionAmount()+" and FED Rs. "+wrapper.getTransactionModel().getTotalCommissionAmount()+
						        " your a/c balance is Rs. "+Formatter.formatNumbers(balance)+" TxID "+wrapper.getTransactionCodeModel().getCode()));
							*/
							
							
							
							
//							"You have successfully cashed out " + ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() 
//						+ ", " + wrapper.getAppUserModel().getMobileNo() + " for  Rs " + Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()) + ".\n\nTX ID: " 
//						+ wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() + "\nDate Time: " + 
//						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT) 
//						+ " " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT)
//						+ "\n\nYour new Balance is: " + ((RetailPaymentVO)wrapper.getProductVO()).getBalance()
//						+ "\n\nHelpline XXX-XXX-XXX" ;
						
						
						
						
						wrapper.getTransactionModel().setConfirmationMessage(sms) ;
					}
					
					// P2P Transfer................................
					else if ( productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
							productDeviceFlowModel.getDeviceFlowId().longValue() == 18 )
					{
						
						switchWrapper.setBasePersistableModel( wrapper.getSmartMoneyAccountModel() ) ;
						switchWrapper = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);
						balance = switchWrapper.getBalance() ;
						 
						String cust1SMS = this.getMessageSource().getMessage(
								"USSD.Customer1A2ASMS",
								new Object[] { 
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
										((P2PVO)wrapper.getProductVO()).getCustomerName(),
										((P2PVO)wrapper.getProductVO()).getMobileNo(),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
										balance,
										wrapper.getTransactionCodeModel().getCode()
										
										}, null);
						
						this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), cust1SMS));
						
						wrapper.getTransactionDetailModel().setCustomField4(cust1SMS);
						
						String cust2SMS = this.getMessageSource().getMessage(
								"USSD.Customer2A2ASMS",
								new Object[] { 
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
										wrapper.getAppUserModel().getFirstName(),
										wrapper.getAppUserModel().getMobileNo(),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
										((P2PVO)wrapper.getProductVO()).getBalance(),
										wrapper.getTransactionCodeModel().getCode()
										
										}, null);
						
						
						this.smsSender.send(new SmsMessage(((P2PVO)wrapper.getProductVO()).getMobileNo(), cust2SMS));
						
						wrapper.getTransactionModel().setConfirmationMessage(cust2SMS) ;
						
					}
					// Retail Payment
					else if ( productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
							productDeviceFlowModel.getDeviceFlowId().longValue() == 19 )
					{
						
						String sms = "Dear Customer, Thank You for using our mobile money transfer service. Your retail payment of " + 
						Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()) + " has been made to " + ((RetailPaymentVO)wrapper.getProductVO()).getCustomerName() 
						+ ", " + ((RetailPaymentVO)wrapper.getProductVO()).getMobileNo()  + ". "
							+ "For more information please call." ;
						
						sms = "Retail Payment Successful\n\nAmount: " + Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()) + "\nRetailer: " + 
						((RetailPaymentVO)wrapper.getProductVO()).getCustomerName() + "\nRec #: " + ((RetailPaymentVO)wrapper.getProductVO()).getMobileNo() 
						+ "\nDate Time: " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT) 
						+ " " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT) + "\nTX ID: "
						+ wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() + "\n\nYour new Balance is: " + balance
						+ "\n\nHelpline XXX-XXX-XXX" ;
						this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), sms));
						
						sms = "Dear Retailer, you have received a payment of " + 
						Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()) + " from " + wrapper.getAppUserModel().getFirstName() + " " + 
						wrapper.getAppUserModel().getLastName() + ", " + wrapper.getAppUserModel().getMobileNo()  + "/ a.c. " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() + " for a retail payment."
							+ " For more information please call." ;
						
						sms = "You have received retail payment of Rs " + Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount())
						+ " from "+ ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() 
						+ ", " + wrapper.getAppUserModel().getMobileNo() + ".\n\nTX ID: " 
						+ wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() + "\nDate Time: " + 
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT) 
						+ " " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT)
						+ "\n\nYour new Balance is: " + ((RetailPaymentVO)wrapper.getProductVO()).getBalance()
						+ "\n\nHelpline XXX-XXX-XXX" ;
						this.smsSender.send(new SmsMessage(((RetailPaymentVO)wrapper.getProductVO()).getMobileNo(), sms));
						
						wrapper.getTransactionModel().setConfirmationMessage(sms) ;
					}
					else
					{

						smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
					}

				}else if(wrapper!=null && wrapper.getProductModel()!=null && InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))){
					//Utility Bill Payment
					if(wrapper.getAppUserModel()!=null && wrapper.getAppUserModel().getAppUserTypeId().longValue() == 2L){
						//Customer Utility Bill Payment
						String companyName = InternetCompanyEnum.lookup(String.valueOf(wrapper.getProductModel().getProductId())).name();
						companyName = companyName.replace("_BILL", "");
						companyName = companyName.replace("_", " ");
						
					String custSMS = this.getMessageSource().getMessage(
							"USSD.CustomerBillPaymentSMS",
							new Object[] { 
									Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
									companyName,
									((BillPaymentVO)(wrapper.getProductVO())).getConsumerNo(),
									PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
									PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
									wrapper.getTransactionCodeModel().getCode()
									}, null);
					this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), custSMS));
					}else if(wrapper.getAppUserModel()!=null && wrapper.getAppUserModel().getAppUserTypeId().longValue() == 3L){
						//Agent Utility Bill payment
						String companyName = InternetCompanyEnum.lookup(String.valueOf(wrapper.getProductModel().getProductId())).name();
						companyName = companyName.replace("_BILL", "");
						companyName = companyName.replace("_", " ");
						String agentSMS = this.getMessageSource().getMessage(
								"USSD.CustomerBillPaymentSMS",
								new Object[] { 
										Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
										companyName,
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
										PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
										wrapper.getTransactionCodeModel().getCode()
										}, null);
						this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
					}
				}
				else
				{
					if (!wrapper.getProductModel().getInstructionIdNotificationMessageModel().getSmsMessageText().contains("SKMT"))
					{
						if( wrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD )
				   		  	{
//								this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(),  wrapper.getProductModel().getName() + " Bill Paid\n\nID: " + ((BillPaymentVO)wrapper.getProductVO()).getConsumerNo() + "\nAmount:  " + 
//										wrapper.getTotalAmount() + "\n"
//										+  "Date Time: " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT) 
//										+ " " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT) +  "\n\nTX ID: " + wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() 
//										+ "\n\nYour Balance is: Rs " + balance  + "\n\nHelpline XXX-XXX-XXX"));
							
							
							
							
							
							
							
							
							
							
							
							
							
							
				   		  	}
						else
							smsSender.sendDelayed(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
					}

				}
			}
			
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of BillSaleTransaction.");
		}

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of BillSaleTransaction....");
		}

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

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

		wrapper.setCustomerModel(new CustomerModel());
		wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());

		// --Setting instruction and success Message
		NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
		baseWrapper.setBasePersistableModel(notificationMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		// wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
		// NotificationMessageModel) baseWrapper.getBasePersistableModel());
		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
		baseWrapper.setBasePersistableModel(successMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		// Populate the Customer model from DB
		baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
		baseWrapper = customerManager.loadCustomer(baseWrapper);
		wrapper.setCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());

		// Populate the Smart Money Account from DB
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		
		
		//populate retailer model from DB
		
		if(wrapper.getProductModel().getProductId().longValue() == 50006L)
		{
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
			baseWrapper.setBasePersistableModel(retailerContactModel);
			baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
			retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
			wrapper.setRetailerContactModel(retailerContactModel);
		}
		
		// Populate Supplier Bank Info Model
		SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
		supplierBankInfoModel.setSupplierId(wrapper.getProductModel().getSupplierId());
		supplierBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		baseWrapper.setBasePersistableModel(supplierBankInfoModel);
		supplierBankInfoModel = (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper).getBasePersistableModel();
		wrapper.setSupplierBankInfoModel(supplierBankInfoModel);

		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();

		if (supplierBankInfoModel != null)
		{
			// Populate Operator's Paying Bank Info Model
			operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
			operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
			operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
			baseWrapper.setBasePersistableModel(operatorBankInfoModel);
			wrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		}

		// Populate Operator Bank Info Model
		// OperatorBankInfoModel operatorBankInfoModel = new
		// OperatorBankInfoModel();
		operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
		operatorBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		operatorBankInfoModel.setBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		baseWrapper.setBasePersistableModel(operatorBankInfoModel);
		wrapper.setOperatorBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of BillSaleTransaction....");
		}
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of BillSaleTransaction....");
		}
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
		if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId().longValue() == 50006L)
		{
			txModel.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		}
		else
		{
			txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		}

		// Customer model of transaction is populated
		if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId().longValue() == 50000L)
		{
			txModel.setCustomerIdCustomerModel(new CustomerModel());
			txModel.getCustomerIdCustomerModel().setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
		}
		

		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Customer mobile No
		if(wrapper.getProductModel().getProductId().longValue() != 50006L)
		{
			txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		}
		
		txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		if(wrapper.getProductModel().getProductId().longValue() == 50000L)
		{
			txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
		}
		else
		{
			txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		}

		// Populate processing Bank Id
		txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
//		txModel.setSaleMobileNo(wrapper.getCustomerModel().getMobileNo());
		if(wrapper.getProductModel().getProductId().longValue() == 50006L)
		{
			txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
			txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
			txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
			txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		}
		
		wrapper.setTransactionModel(txModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of BillSaleTransaction....");
		}

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doEnd(WorkFlowWrapper wrapper) of BillSaleTransaction....");
		}

		String sms = SMSUtil.buildBillPaymentSMSForUser(wrapper);

		if (logger.isDebugEnabled())
		{
			logger.debug("Sending out SMS to MWallet user....");
		}
		if (wrapper.getDeviceTypeModel().getDeviceTypeId().longValue() != DeviceTypeConstantsInterface.ALL_PAY.longValue()
				&& ( wrapper.getProductDeviceFlowModel() != null && wrapper.getProductDeviceFlowModel().getDeviceFlowId() != null && 
						(wrapper.getProductDeviceFlowModel().getDeviceFlowId().longValue() != 18 && wrapper.getProductModel().getProductId() != 50006 &&
								wrapper.getProductDeviceFlowModel().getDeviceFlowId().longValue() != 19	) )
		)
		{

			if( wrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD )
   		  	{
				this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), "Lesco Bill Paid\n\nID: 001256733484848\nAmount:  1230\n"
						+  "Due Date: 10/10/10\nDate Time: 12:12pm 10/10/10\n\nTX ID: TX123456\n\nYour Balance is: Rs 1000\n\nHelpline XXX-XXX-XXX"));
   		  	}
			 else
				 this.smsSender.sendDelayed(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), sms));

		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doEnd(WorkFlowWrapper wrapper) of BillSaleTransaction....");
		}
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

	public void setSettlementManager(SettlementManager settlementManager)
	{
		this.settlementManager = settlementManager;
	}

	public void setCustomerManager(CustTransManager customerManager)
	{
		this.customerManager = customerManager;
	}

	public void setProductDispenseController(ProductDispenseController productDispenseController)
	{
		this.productDispenseController = productDispenseController;
	}

	public void setProductManager(ProductManager productManager)
	{
		this.productManager = productManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
	{
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public void setSwitchController(SwitchController switchController)
	{
		this.switchController = switchController;
	}

	public void setVeriflyController(VeriflyManagerService veriflyController)
	{
		this.veriflyController = veriflyController;
	}

	public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager)
	{
		this.supplierBankInfoManager = supplierBankInfoManager;
	}

	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of BillSaleTransaction....");
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
			logger.debug("Ending getTransactionProcessingCharges of BillSaleTransaction....");
		}
		return transProcessingAmount;
	}

	public void setOperatorManager(OperatorManager operatorManager)
	{
		this.operatorManager = operatorManager;
	}

	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception
	{
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception
	{
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper) throws Exception
	{

		//		if (wrapper.getFinancialTransactionsMileStones().isBillPaid())
		//		{
		//			try
		//			{
		//				billSaleProductDispenser.rollback(wrapper);
		//			}
		//			catch (Exception e)
		//			{
		//				log.warn("Exception occured during Bill Payment rollback : " + e.getStackTrace());
		//			}
		//
		//		}
		//		if (wrapper.getFinancialTransactionsMileStones().getIsCommissionsSettled().size() != 0)
		//		{
		//			try
		//			{
		//				this.settlementManager.rollbackCommissionSettlement(wrapper);
		//			}
		//			catch (Exception e)
		//			{
		//				log.warn("Exception occured during Commission Settlement rollback : " + e.getStackTrace());
		//			}
		//		}
		//		if (wrapper.getFinancialTransactionsMileStones().isCustomerBankAccountDebitted())
		//		{
		//			try
		//			{
		//				this.settlementManager.rollbackBankPayment(wrapper);
		//			}
		//			catch (Exception e)
		//			{
		//				log.warn("Exception occured during Bank Settlement rollback : " + e.getStackTrace());
		//			}
		//		}
		return wrapper;
	}
	
	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		
		if ( InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
				&& wrapper.getBillPaymentIntegrationVO() == null 
				&& wrapper.getOLASwitchWrapper() != null) { // bill advice is not sent then credit customer account again.
			SwitchWrapper switchWrapper= wrapper.getOLASwitchWrapper();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			switchWrapper.getOlavo().setReasonId(ReasonConstants.REVERSE_BILL_PAYMENT);
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

			logger.info("[BillSaleTransaction.rollback] Bill Advice was NOT sent therefore going to Credit Customer Account in OLA." + 
					" Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
					" Consumer No:" + wrapper.getTransactionDetailModel().getConsumerNo() + 
					" Trx ID:" + wrapper.getTransactionCodeModel().getCode());
			
			switchWrapper = abstractFinancialInstitution.debitWithoutPin(switchWrapper);
		}
	
		return wrapper;
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

	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}

	

}
