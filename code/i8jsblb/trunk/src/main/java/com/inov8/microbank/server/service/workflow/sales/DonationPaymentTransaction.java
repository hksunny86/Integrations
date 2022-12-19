package com.inov8.microbank.server.service.workflow.sales;

import static com.inov8.microbank.common.util.ServiceTypeConstantsInterface.SERVICE_TYPE_DONATION_CUSTOMER_PAYMENT;
import static com.inov8.microbank.common.util.ServiceTypeConstantsInterface.SERVICE_TYPE_DONATION_RETAILER_PAYMENT;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
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
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.DonationPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

public class DonationPaymentTransaction extends SalesTransaction {
	
		private final Log logger = LogFactory.getLog(getClass());
		
		private MessageSource messageSource;
		private CommissionManager commissionManager;
		private SettlementManager settlementManager;
		private ProductDispenseController productDispenseController;
		private CustTransManager customerManager;
		private SmsSender smsSender;
		private ProductManager productManager;
		private UserDeviceAccountsManager userDeviceAccountsManager;
		private NotificationMessageManager notificationMessageManager;
		private SupplierBankInfoManager supplierBankInfoManager;
		private RetailerContactManager retailerContactManager;	
		private StakeholderBankInfoManager stakeholderBankInfoManager = null;
		private String rrnPrefix;
	
		private AbstractFinancialInstitution phoenixFinancialInstitution;
		private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
		
		private DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern("dd/MM/yyyy");
		private DateTimeFormatter timeFormatter =  DateTimeFormat.forPattern("h:mm a");
		private Long INCLUSIVE_CHARGES = 4L;
		private String REASON_ID = "reasonId";

		/**
		 * Pulls the bill information from the supplier system
		 * 
		 * @param WorkFlowWrapper
		 * @return WorkFlowWrapper
		 */
		public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception {
			if (logger.isDebugEnabled()) {
				
				logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of DonationPaymentTransaction...");
				logger.debug("Loading ProductDispenser...");
			}
			
			BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
			
			logger.debug("Fetching Bill Info through Product Dispenser...");
			
			wrapper = billSaleProductDispenser.getBillInfo(wrapper);
	
			return wrapper;
		}
	
		/**
		 * Validate input from the user against the information pulled from the
		 * supplier
		 * 
		 * @param workFlowWrapper
		 * @return WorkFlowWrapper
		 */
		public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
	
			throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
		}

		/**
		 * This method calls the commission module to calculate the commission on
		 * this product and transaction.The wrapper should have product,payment mode
		 * and principal amount that is passed onto the commission module
		 * 
		 * @param  WorkFlowWrapper
		 * @return WorkFlowWrapper
		 */
		public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws Exception {
	 
			if (logger.isDebugEnabled()) {
				logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of DonationPaymentTransaction...");
			}
			
			if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
				CustomerModel customerModel = new CustomerModel();
				customerModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(customerModel);
				baseWrapper = customerManager.loadCustomer(baseWrapper);
				
				if(null != baseWrapper.getBasePersistableModel()) {
					
					customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
					workFlowWrapper.setCustomerModel(customerModel);
					workFlowWrapper.setSegmentModel(customerModel.getSegmentIdSegmentModel());
				}
				
			} else {
				
				SegmentModel segmentModel = new SegmentModel();
				segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
				workFlowWrapper.setSegmentModel(segmentModel);
			}
			
			CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
			commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
			commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
			commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
			commissionWrapper.setProductModel(workFlowWrapper.getProductModel());
			
	//		if(null != workFlowWrapper.getCustomerModel()) {
	//			
	//			SegmentModel segmentModel=new SegmentModel();
	//			segmentModel=workFlowWrapper.getCustomerModel().getSegmentIdSegmentModel();
	//			workFlowWrapper.setSegmentModel(segmentModel);
	//		}
			
			
			RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
		    workFlowWrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
			
			commissionWrapper = this.commissionManager.calculateCommission(workFlowWrapper);
	
			if (logger.isDebugEnabled()) {
				
				logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of DonationPaymentTransaction...");
			}
			
			return commissionWrapper;
		}

		/**
		 * 
		 * @param CommissionAmountsHolder
		 * @param calculatedCommissionHolder
		 * @throws FrameworkCheckedException
		 */
		public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
			if (logger.isDebugEnabled()) {
				logger.debug("Inside validateCommission of DonationPaymentTransaction...");
			}
			
			CommissionAmountsHolder commissionHolder = 
					(CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			
			UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
			
			DonationPaymentVO productVO = (DonationPaymentVO) workFlowWrapper.getProductVO();
	
			if (productVO.getCurrentBillAmount().doubleValue() != workFlowWrapper.getBillAmount().doubleValue()) {
	
				throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
			}
			
			if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue()) {
	
				throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
			}
			
			if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue()) {
	
				throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
			}
			
			if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
				
				if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount()) {
	
					throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
				}
			}
	
			if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue()) {
	
				throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
			}
			
			if (logger.isDebugEnabled()) {
	
				logger.debug("Ending validateCommission of DonationPaymentTransaction...");
			}
		}

		/**
		 * This method calls the settlement module to settle the payment amounts
		 * 
		 * @param WorkFlowWrapper
		 * @return WorkFlowWrapper
		 */
		public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
			
			return wrapper;
		}
	
		/**
		 * This method is responsible for inserting the data into the transaction
		 * tables
		 * 
		 * @param WorkFlowWrapper
		 * @return WorkFlowWrapper
		 */
		public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
			
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
		public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
			
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
	
		public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
			
		    if (wrapper.getFromRetailerContactModel() != null && 
		    		wrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
		    	
		      if (!wrapper.getFromRetailerContactModel().getActive()) {
		    	  
		        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE);
		      }
		      
		    } else if(wrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
		      
		    	throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
		    }
	
			if (wrapper.getAppUserModel() != null) {
				
				if (StringUtil.isNullOrEmpty(wrapper.getAppUserModel().getMobileNo())) {
					
					throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
				}
				
			} else {
				
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
			}
			
			if (wrapper.getUserDeviceAccountModel() == null) {
				
				throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
			}
	
			if (wrapper.getOlaSmartMoneyAccountModel() != null) {
				
				if (!wrapper.getOlaSmartMoneyAccountModel().getActive()) {
					
					throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
				}			
				
			} else {
				
				if(wrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
				}
			}
	
			if (wrapper.getProductModel() != null) {
				
				if (!wrapper.getProductModel().getActive()) {
			
					throw new WorkFlowException( WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
				}
				
			} else {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
			}
			
	 		if (wrapper.getProductModel().getServiceIdServiceModel().getServiceId().longValue() != SERVICE_TYPE_DONATION_RETAILER_PAYMENT.longValue() ||
	 				wrapper.getProductModel().getServiceIdServiceModel().getServiceId().longValue() != SERVICE_TYPE_DONATION_CUSTOMER_PAYMENT.longValue()) {
				
	 			if (!wrapper.getProductModel().getActive())  {
	 				
					throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
				}
			}
	 
			if (wrapper.getProductModel().getSupplierIdSupplierModel() != null) {
				
				if (!wrapper.getProductModel().getSupplierIdSupplierModel().getActive()) {
			
					throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);
				}
				
			} else {
			
				throw new WorkFlowException( WorkFlowErrorCodeConstants.SUPPLIER_NULL);
			}
	
			if (wrapper.getSupplierBankInfoModel() == null || StringUtil.isNullOrEmpty(wrapper.getSupplierBankInfoModel().getAccountNo())) {
			
				throw new WorkFlowException( WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
			}
	 
			if (wrapper.getBillAmount() < 0) {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
			}
			
			if (wrapper.getTotalAmount() < 0) {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
			}
			
			if (wrapper.getTotalCommissionAmount() < 0) {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
			}
			
			if (wrapper.getTxProcessingAmount() < 0) {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
			}
	
			if (wrapper.getPaymentModeModel() != null) {
				
				if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0) {
			
					throw new WorkFlowException("Payment Mode ID is not supplied.");
				}
			}
	
			if (wrapper.getProductModel().getServiceIdServiceModel() != null) {
				
				if (!wrapper.getProductModel().getServiceIdServiceModel().getActive()) {
			
					throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);
				}
				
			} else {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
			}
			
			return wrapper;
		}

	
	
		/**
		 * @param workFlowWrapper
		 * @return
		 * @throws Exception
		 */
		@Override
		public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws Exception {
			
			Long appUserTypeId = workFlowWrapper.getAppUserModel().getAppUserTypeId();
			
			if(appUserTypeId.longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
			    workFlowWrapper = this.getBillInfo(workFlowWrapper);
				return retailerDoSale(workFlowWrapper);
				
			} else if(appUserTypeId.longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
	
				return customerDoSale(workFlowWrapper);
			}
		
			return workFlowWrapper;
		}
	

		/**
		 * @param workFlowWrapper
		 * @return
		 * @throws Exception
		 * @description Payment/collection amount (excluding agent commission if any) will be moved to supplier core bank account
		 */
		private WorkFlowWrapper retailerDoSale(WorkFlowWrapper workFlowWrapper) throws Exception {
		    
		    BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(workFlowWrapper);
		    billSaleProductDispenser.verify(workFlowWrapper);
	 					
			Date currentDate = new Date();
			
	 		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
	 		
	 		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
				
			transactionModel.setConfirmationMessage(" _ ");
			transactionModel.setNotificationMobileNo(workFlowWrapper.getAppUserModel().getMobileNo());
	
			transactionModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED );
				
			if(!StringUtil.isNullOrEmpty(workFlowWrapper.getWalkInCustomerMob())) {
					
				transactionDetailModel.setCustomField6(workFlowWrapper.getWalkInCustomerMob());
				transactionModel.setNotificationMobileNo(workFlowWrapper.getWalkInCustomerMob());
			}
				
			if(!StringUtil.isNullOrEmpty(workFlowWrapper.getWalkInCustomerCNIC())) {
				
				transactionDetailModel.setCustomField7(workFlowWrapper.getWalkInCustomerCNIC());
			}
				
			if(!StringUtil.isNullOrEmpty((String)workFlowWrapper.getObject(CommandFieldConstants.KEY_CSCD))) {
				
				transactionDetailModel.setConsumerNo((String)workFlowWrapper.getObject(CommandFieldConstants.KEY_CSCD));
			
			} else {
				
				transactionDetailModel.setConsumerNo(workFlowWrapper.getWalkInCustomerMob());
			} 

			workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
			workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
			txManager.transactionRequiresNewTransaction(workFlowWrapper);
		
			CommissionAmountsHolder commissionAmounts = getCommissionAmountsHolder(workFlowWrapper);
				
			//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
			if(null != workFlowWrapper.getFromRetailerContactModel() && workFlowWrapper.getFromRetailerContactModel().getHead()){
				commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
				commissionAmounts.setFranchise1CommissionAmount(0.0d);
			}
			
			transactionModel.setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			transactionModel.setTotalAmount(commissionAmounts.getTotalAmount());
			
			transactionDetailModel.setActualBillableAmount(commissionAmounts.getTotalAmount());
			transactionDetailModel.setProductIdProductModel(workFlowWrapper.getProductModel());
	
			transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
			workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
	
			SmartMoneyAccountModel smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
	
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setTransactionTransactionModel(transactionModel);
	
			Double balance = 0.0 ;
	
			TransactionModel transactionModelTemp = null;
			
			CHECK_BALANCE : {
				
				transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel();
					
				SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
					
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
				switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
				switchWrapperTemp.setTransactionTransactionModel(transactionModel);
				switchWrapperTemp.setBasePersistableModel( smartMoneyAccountModel );					
							
				logger.debug("[DonationPaymentTransaction.dosale] Checking Agent Balance. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
			
				switchWrapperTemp = phoenixFinancialInstitution.checkBalance(switchWrapperTemp);
								
				transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapperTemp.getSwitchSwitchModel());
								
				if(switchWrapperTemp.getBalance() < transactionModelTemp.getTotalAmount()) {
									
					String response = this.getMessageSource().getMessage("MINI.InsufficientBalance", null, null) ; 
									
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);		 
				}
								
				balance = switchWrapperTemp.getBalance();				
	
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
					
				switchWrapper.setAccountInfoModel(switchWrapperTemp.getAccountInfoModel());
				
				switchWrapper.setFromAccountNo(switchWrapperTemp.getAccountInfoModel().getAccountNo());
				
				transactionModel.setBankAccountNo(StringUtil.replaceString(switchWrapperTemp.getAccountInfoModel().getAccountNo(), 5, "*"));
				
			}
				
			//The following code is for Phoenix implementation
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
			switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
									
			switchWrapper.setToAccountNo(workFlowWrapper.getSupplierBankInfoModel().getAccountNo());

			Double billAmount = workFlowWrapper.getBillAmount();
			Double transactionAmount = billAmount;
			
			if(null!=workFlowWrapper.getObject(REASON_ID) && ((Long) workFlowWrapper.getObject(REASON_ID)).longValue() == INCLUSIVE_CHARGES.longValue()) {
			
				transactionAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - 
									   switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount();
			}
			
			switchWrapper.getWorkFlowWrapper().getTransactionModel().setTransactionAmount(transactionAmount);
			switchWrapper.setTransactionAmount(transactionAmount);
	
			Double reconAmount = 0.0D;
			
			if(smartMoneyAccountModel.getPaymentModeId() != null && 
					smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue()) {
	
				this.sendSMS(workFlowWrapper, transactionModel);	
									
				logger.debug("[DonationPaymentTransaction.dosale] Debit Agent Account and Credit Supplier's Core Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
	
				switchWrapper = phoenixFinancialInstitution.debitCreditAccount(switchWrapper);
				
				balance = balance-switchWrapper.getTransactionAmount();
	
				reconAmount = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() -
						switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount();
			}
			
			logger.info("Settle Donation Transaction Commission/Recon.");
			switchWrapper.getWorkFlowWrapper().setTotalCommissionAmount(reconAmount);
			switchWrapper = olaVeriflyFinancialInstitution.settleDonationTransactionCommission(switchWrapper);
			
			transactionModelTemp.setTransactionAmount(transactionAmount);
			switchWrapper.setBalance(balance);
			switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp) ;
				
			workFlowWrapper.setSwitchWrapper(switchWrapper);
			workFlowWrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
	
			transactionDetailModel.setSettled(Boolean.TRUE);
				
			if(workFlowWrapper.getFirstFTIntegrationVO() != null) {
					
				String bankResponseCode = workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
					
				if(bankResponseCode != null) {
					transactionModel.setBankResponseCode(bankResponseCode);
				}
			}			

			workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
			workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
			txManager.saveTransaction(workFlowWrapper);
				
			if (logger.isDebugEnabled()) {
				logger.debug("Going to settle commissions using SettlementManager....");
			}
	
			this.settlementManager.settleCommission(workFlowWrapper.getCommissionWrapper(), workFlowWrapper);
				
				
			if (logger.isDebugEnabled()) {
				logger.debug("Going to settle Bank Payment using SettlementManager....");
			}
				
			this.settleAmount(workFlowWrapper); // settle all amounts to the respective
				
			transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
				
			if(workFlowWrapper.getFirstFTIntegrationVO() != null) {
					
				String responseCode = workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
					
				if(responseCode != null) {
						
					transactionModel.setBankResponseCode(responseCode);
				}
			}	
			
			billSaleProductDispenser.doSale(workFlowWrapper);
			
			
				
			workFlowWrapper.getTransactionModel().setTransactionAmount(billAmount);
			workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
			workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
			
			txManager.saveTransaction(workFlowWrapper); // save the transaction
				
			return workFlowWrapper;
		}

		
		/**
		 * @param workFlowWrapper
		 * @return
		 * @throws Exception
		 * @see Payment/Collection amount will be moved from Customer Branchless Account to Sipplier Branchless Account.
		 */
		private WorkFlowWrapper customerDoSale(WorkFlowWrapper workFlowWrapper) throws Exception {
				
			Date currentDate = new Date();

			Long reasonId = ((Long) workFlowWrapper.getObject(REASON_ID)).longValue();
			
			SmartMoneyAccountModel customerSmartMoneyAccountModel = null;
	 		
			TransactionModel _transactionModel = null;
			
	 		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
	 		
	 		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
	
				
			transactionModel.setConfirmationMessage(" _ ");
			transactionModel.setNotificationMobileNo(workFlowWrapper.getAppUserModel().getMobileNo());
	
			transactionModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED );
			transactionDetailModel.setConsumerNo((String)workFlowWrapper.getObject(CommandFieldConstants.KEY_CSCD));

			workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
			workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
			
			txManager.transactionRequiresNewTransaction(workFlowWrapper);
		
			CommissionAmountsHolder commissionAmounts = getCommissionAmountsHolder(workFlowWrapper);
				
			transactionModel.setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			transactionModel.setTotalAmount(commissionAmounts.getTotalAmount());
			
			transactionDetailModel.setActualBillableAmount(commissionAmounts.getTotalAmount());
			transactionDetailModel.setProductIdProductModel(workFlowWrapper.getProductModel());
			transactionDetailModel.setSettled(Boolean.FALSE);
			
	//		transactionModel.setBankAccountNo(StringUtil.replaceString(workFlowWrapper.getCustomerAccount().getNumber(), 5, "*"));
	
			transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
			workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
	
			customerSmartMoneyAccountModel = workFlowWrapper.getOlaSmartMoneyAccountModel();
			baseWrapper.setBasePersistableModel(customerSmartMoneyAccountModel);
			workFlowWrapper.setSmartMoneyAccountModel(customerSmartMoneyAccountModel);
	
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setTransactionTransactionModel(transactionModel);
	
			Double customerBalance = 0.0 ;
				
			CHECK_BALANCE : {
				
				SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
						
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
				switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
				switchWrapperTemp.setTransactionTransactionModel(transactionModel);
				switchWrapperTemp.setBasePersistableModel( customerSmartMoneyAccountModel );
				switchWrapperTemp.setSmartMoneyAccountModel(customerSmartMoneyAccountModel);
							
				logger.debug("[DonationPaymentTransaction.dosale] Checking Customer Balance. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
		
				switchWrapperTemp = olaVeriflyFinancialInstitution.checkBalance(switchWrapperTemp);
							
				transactionModel.setProcessingSwitchIdSwitchModel(switchWrapperTemp.getSwitchSwitchModel());
							
				if(switchWrapperTemp.getBalance() < transactionModel.getTotalAmount()) {
								
					String response = this.getMessageSource().getMessage("MINI.InsufficientBalance", null, null) ; 
								
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);		 
				}
							
				customerBalance = switchWrapperTemp.getBalance();
	
				switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel) ;
					
				transactionModel.setBankAccountNo(StringUtil.replaceString(switchWrapperTemp.getAccountInfoModel().getAccountNo(), 5, "*"));
			}
							
			//The following code is for Phoenix implementation
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
			switchWrapper.setBasePersistableModel(customerSmartMoneyAccountModel);
			
			switchWrapper.setToAccountNo(workFlowWrapper.getSupplierBankInfoModel().getAccountNo());
			
			
			Double billAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount();
			Double transactionAmount = billAmount;
			
			if(reasonId.longValue() == INCLUSIVE_CHARGES.longValue()) {
			
				transactionAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - 
								switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount();
			}
			
			switchWrapper.setTransactionAmount(transactionAmount);

			switchWrapper.setBalance(customerBalance);
			workFlowWrapper.setSwitchWrapper(switchWrapper);
			
		
			DEBIT_CREDIT_BLOCK : {
				
				logger.info("[DonationPaymentTransaction.dosale] Debit Agent Account and Credit Product Sundry Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " FT Amount: " + switchWrapper.getTransactionAmount());
										
				_transactionModel = (TransactionModel) org.apache.commons.beanutils.BeanUtils.cloneBean(transactionModel);
				
				this.sendSMS(workFlowWrapper, _transactionModel);				
					
				logger.info("Debit Customer Account.");
					
				switchWrapper = olaVeriflyFinancialInstitution.transaction(switchWrapper);
				
				customerBalance = switchWrapper.getBalance();
				
				workFlowWrapper.getTransactionModel().setTransactionAmount(transactionAmount);
				
				_transactionModel.setTransactionAmount(transactionAmount);
				
				switchWrapper.setTransactionAmount(transactionAmount);
				
				switchWrapper.setWorkFlowWrapper(workFlowWrapper);

				logger.info("Credit Sundry Account.");
				
				switchWrapper = olaVeriflyFinancialInstitution.donationTransaction(switchWrapper);
					
				workFlowWrapper.setSwitchWrapper(switchWrapper);

				logger.info("Settle Donation Transaction Commission/Recon.");
				
				/* Charges-Commission will be parked in Recon Settlement Account in Branch less Banking  */

				Double reconAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount();
				
				switchWrapper.getWorkFlowWrapper().setTotalCommissionAmount(reconAmount);
				
				switchWrapper = olaVeriflyFinancialInstitution.settleDonationTransactionCommission(switchWrapper);
				
				switchWrapper.setBalance(customerBalance);
					
			}

			transactionDetailModel.setSettled(Boolean.TRUE);
			
			if(workFlowWrapper.getFirstFTIntegrationVO() != null) {
				
				String bankResponseCode = workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
				
				if(bankResponseCode != null) {
					
					_transactionModel.setBankResponseCode(bankResponseCode);
				}
			}			
			
			switchWrapper.getWorkFlowWrapper().setTransactionModel(_transactionModel) ;
				
			workFlowWrapper.setSwitchWrapper(switchWrapper);
			workFlowWrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
			workFlowWrapper.setTransactionModel(_transactionModel);

			workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
			workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
			
			txManager.saveTransaction(workFlowWrapper);
			
			this.settlementManager.settleCommission(workFlowWrapper.getCommissionWrapper(), workFlowWrapper);
			
			this.settleAmount(workFlowWrapper); // settle all amounts to the respective
			
			_transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.BANK_SETTLEMENT_PENDING);
			
			if(workFlowWrapper.getFirstFTIntegrationVO() != null) {
				
				String responseCode = workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
				
				if(responseCode != null) {
					
					_transactionModel.setBankResponseCode(responseCode);
				}
			}			
			
			workFlowWrapper.getTransactionModel().setTransactionAmount(billAmount);

			workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
			workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
			
			txManager.saveTransaction(workFlowWrapper); // save the transaction
			
			return workFlowWrapper;
		}
	
	
		/**
		 * @param workFlowWrapper
		 * @return
		 * @throws Exception
		 */
		private CommissionAmountsHolder getCommissionAmountsHolder(WorkFlowWrapper workFlowWrapper) throws Exception {
			
		CommissionWrapper commissionWrapper = this.calculateCommission(workFlowWrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			/*
			if(null != workFlowWrapper.getFromRetailerContactModel() && 
					workFlowWrapper.getFromRetailerContactModel().getHead()) {
				//	ToDo
				//	commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
				//	commissionAmounts.setFranchise1CommissionAmount(0.0d);
					
					workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
			}
			*/

	
			workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
			
			workFlowWrapper.setCommissionWrapper(commissionWrapper);
			
			return commissionAmounts;
		}
	
	
		/**
		 * @param workFlowWrapper
		 * @param transactionModel
		 */
		private void sendSMS(WorkFlowWrapper workFlowWrapper, TransactionModel transactionModel) {
			
			CREATE_SEND_SMS : {
				
				String commissionAmount = null;
				long userType = workFlowWrapper.getAppUserModel().getAppUserTypeId().longValue();		

				Double totalCommissionAmount = transactionModel.getTotalCommissionAmount();
				Double transactionAmount = transactionModel.getTransactionAmount();

				if((null!=workFlowWrapper.getObject(REASON_ID) && ((Long) workFlowWrapper.getObject(REASON_ID)).longValue() == INCLUSIVE_CHARGES.longValue())) {//4
					
					totalCommissionAmount = 0.0D;
					transactionAmount = transactionModel.getTotalAmount();
				}
				
				commissionAmount = (totalCommissionAmount == 0.0D) ? formatDouble(totalCommissionAmount, "0.00") : formatDouble(totalCommissionAmount, "#.00");
				
				String dateTime = dateTimeFormatter.print(new DateTime()) +" "+ timeFormatter.print(new LocalTime());
					
				try {
						
					if(userType == UserTypeConstantsInterface.RETAILER.longValue()) {
							
						Object[] agentParameters = new Object[] { 
								workFlowWrapper.getProductModel().getName(), 
								Formatter.formatDouble(transactionAmount),
								transactionModel.getNotificationMobileNo(),
								dateTime, workFlowWrapper.getTransactionCodeModel().getCode(),
								commissionAmount};
							
						Object[] walkinCustomerSMS = new Object[] { 
								workFlowWrapper.getProductModel().getName(), 
								Formatter.formatDouble(transactionAmount),
								transactionModel.getFromRetContactMobNo(),
								dateTime, workFlowWrapper.getTransactionCodeModel().getCode(),
								commissionAmount};
							
						String agentSMS = this.getMessageSource().getMessage("USSD.DonationPaymentAgentNotification", agentParameters, null);
							
						String walkinSMS = this.getMessageSource().getMessage("USSD.DonationPaymentWCNotification", walkinCustomerSMS, null);
							
						workFlowWrapper.getTransactionDetailModel().setCustomField4(agentSMS);
						workFlowWrapper.getTransactionDetailModel().setCustomField8(walkinSMS);
						
						logger.debug("[DonationPaymentTransaction.dosale] SENDING AGENT SMS @ MOBILE : " + workFlowWrapper.getAppUserModel().getMobileNo());
						smsSender.send(new SmsMessage(workFlowWrapper.getAppUserModel().getMobileNo(), agentSMS));
							
						logger.debug("[DonationPaymentTransaction.dosale] SENDING WALIKIN CUSTOMER SMS @ MOBILE : " + workFlowWrapper.getWalkInCustomerMob());
						smsSender.send(new SmsMessage(workFlowWrapper.getWalkInCustomerMob(), walkinSMS));
	
						transactionModel.setConfirmationMessage(walkinSMS);
							
					} else if(userType == UserTypeConstantsInterface.CUSTOMER.longValue()) {						
							
						Object[] customerParameters = new Object[] { 
								workFlowWrapper.getProductModel().getName(),
								Formatter.formatDouble(transactionModel.getTransactionAmount()),
								dateTime,
								workFlowWrapper.getTransactionCodeModel().getCode(),
								commissionAmount,
								Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getBalance()-transactionModel.getTotalAmount())};			
							
						String customerSMS = this.getMessageSource().getMessage("CUSTOMER.SMS.DonationPaymentNotification", customerParameters, null);	
							
						transactionModel.setConfirmationMessage(customerSMS);		
						
						logger.debug("[DonationPaymentTransaction.dosale] SENDING CUSTOMER SMS @ MOBILE : " + workFlowWrapper.getAppUserModel().getMobileNo());
						smsSender.send(new SmsMessage(workFlowWrapper.getAppUserModel().getMobileNo(), customerSMS));				
					}
						
					
				} catch (FrameworkCheckedException e) {
					logger.error(e);
				}
					
			}		
		}
	

		/* (non-Javadoc)
		 * @see com.inov8.microbank.server.service.workflow.controller.TransactionProcessor#doPreStart(com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper)
		 */
		@Override
		protected WorkFlowWrapper doPreStart(WorkFlowWrapper workFlowWrapper) throws Exception {
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			workFlowWrapper = super.doPreStart(workFlowWrapper);

			baseWrapper.setBasePersistableModel(workFlowWrapper.getProductModel());
			baseWrapper = productManager.loadProduct(baseWrapper);
			
			ProductModel productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			
			workFlowWrapper.setProductModel(productModel);
	
			workFlowWrapper.setPaymentModeModel(new PaymentModeModel());	
						
			Long paymentModeId = null;
			
			if(workFlowWrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
			
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			    RetailerContactModel retailerContact = new RetailerContactModel();
			    retailerContact.setRetailerContactId( workFlowWrapper.getAppUserModel().getRetailerContactId() );
			    
			    searchBaseWrapper.setBasePersistableModel( retailerContact );
			    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
			    
			    workFlowWrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());
				workFlowWrapper.getPaymentModeModel().setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
				
				paymentModeId = PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT;
				
			} else {
	
				workFlowWrapper.getPaymentModeModel().setPaymentModeId(workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
				paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
			}

			// Populate Supplier Bank Info Model
			SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
			supplierBankInfoModel.setSupplierId(workFlowWrapper.getProductModel().getSupplierId());
			supplierBankInfoModel.setPaymentModeId(paymentModeId);
			baseWrapper.setBasePersistableModel(supplierBankInfoModel);
			supplierBankInfoModel = (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper).getBasePersistableModel();
			
			workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);
	
			// --Setting instruction and success Message
			NotificationMessageModel notificationMessage = new NotificationMessageModel();
			notificationMessage.setNotificationMessageId(workFlowWrapper.getProductModel().getInstructionId());
			baseWrapper.setBasePersistableModel(notificationMessage);
			baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
			workFlowWrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());
	
			// wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
			// NotificationMessageModel) baseWrapper.getBasePersistableModel());
			NotificationMessageModel successMessage = new NotificationMessageModel();
			successMessage.setNotificationMessageId(workFlowWrapper.getProductModel().getSuccessMessageId());
			baseWrapper.setBasePersistableModel(successMessage);
			baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
			workFlowWrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());
	
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setAppUserId(workFlowWrapper.getAppUserModel().getAppUserId());
			userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
			baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
			
//			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//			StakeholderBankInfoModel sundaryStakeholderBankInfoModel = new StakeholderBankInfoModel();
//			sundaryStakeholderBankInfoModel.setPrimaryKey(DONATION_COLLECTION_SUNDRY_ACCOUNT_ID);
//			searchBaseWrapper.setBasePersistableModel(sundaryStakeholderBankInfoModel);
//			sundaryStakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
//
//			workFlowWrapper.putObject("DONATION_COLLECTION_SUNDRY_ACCOUNT_ID", sundaryStakeholderBankInfoModel);
			// storing sundry account to Supplier bank.
			
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of DonationPaymentTransaction....");
						
			return workFlowWrapper;
		}
		

		/* (non-Javadoc)
		 * @see com.inov8.microbank.server.service.workflow.controller.TransactionProcessor#doPreProcess(com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper)
		 */
		@Override
		protected WorkFlowWrapper doPreProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
	 
			workFlowWrapper = super.doPreProcess(workFlowWrapper);
	
			TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
	
			transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			
			if(workFlowWrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
	
				transactionModel.setRetailerId(workFlowWrapper.getFromRetailerContactModel().getRetailerId());
				transactionModel.setDistributorId(workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
				transactionModel.setSmartMoneyAccountId(workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
				transactionModel.setProcessingBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
				transactionModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
				transactionModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
				transactionModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;
				transactionModel.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
				
			} else if(workFlowWrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
				transactionModel.setPaymentModeId(workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
	
				transactionModel.setCustomerMobileNo(workFlowWrapper.getAppUserModel().getMobileNo());
				
				transactionModel.setSmartMoneyAccountId(workFlowWrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
				
				transactionModel.setProcessingBankId(workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
				
				transactionModel.setCustomerMobileNo(workFlowWrapper.getWalkInCustomerMob());
			}		
			
			transactionModel.setTransactionAmount(workFlowWrapper.getBillAmount());
			transactionModel.setTotalAmount(workFlowWrapper.getBillAmount());
			transactionModel.setTotalCommissionAmount(0d);
			transactionModel.setDiscountAmount(0d);
			transactionModel.setTransactionTypeIdTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
	
			transactionModel.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
			transactionModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
			transactionModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
	
			workFlowWrapper.setTransactionModel(transactionModel);
	
			return workFlowWrapper;
		}



		/**
		 * @param commissionWrapper
		 * @param workFlowWrapper
		 * @return
		 */
		public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
	
			Double transProcessingAmount = 0D;
	
			List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
	
			for (CommissionRateModel commissionRateModel : resultSetList) {
				
				if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue()) {
					
					if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue()) {
						
						transProcessingAmount += commissionRateModel.getRate();
						
					} else {
						
						transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
					}
				}
			}
			
			return transProcessingAmount;
		}
		

		/* (non-Javadoc)
		 * @see com.inov8.microbank.server.service.workflow.controller.TransactionControllerImpl#rollback(com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper)
		 */
		@Override
		public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception {
			
			String code = wrapper.getTransactionCodeModel().getCode();
			
			logger.debug("[DonationPaymentTransaction.rollback] Rolling back transaction with ID : "+code);
			
			if(null != wrapper.getFirstFTIntegrationVO()) {
				
				String rrn = wrapper.getFirstFTIntegrationVO().getRetrievalReferenceNumber();
				rrn = rrnPrefix + rrn;
				rrnPrefix = null;
				
				logger.debug("[DonationPaymentTransaction.rollback] FT was done so trying to roll back with RRN  : "+rrn+" and trx code : "+code);
				
				PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO();
				phoenixIntegrationMessageVO.setRetrievalReferenceNumber( rrn );
				phoenixIntegrationMessageVO.setChannelId("MR0001");
				
				SwitchWrapper switchWrapper = wrapper.getFirstFTSwitchWrapper();
				switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
				String fromAccount = new String(switchWrapper.getFromAccountNo());
				String toAccount = new String(switchWrapper.getToAccountNo());
				
				//reverse accounts for posted transaction report...
				switchWrapper.setFromAccountNo(toAccount);
				switchWrapper.setToAccountNo(fromAccount);
				
				logger.debug("[DonationPaymentTransaction.rollback] Hitting Phoenix to rollback with RRN : "+rrn+" and trx code : "+code);
				switchWrapper = phoenixFinancialInstitution.reverseFundTransfer(switchWrapper);
			}
			  if(null != wrapper.getProductVO() && null != wrapper.getProductVO().getResponseCode())
	            {
	                ProductDispenser productDispense = this.productDispenseController.loadProductDispenser( wrapper ) ;
	                productDispense.rollback(wrapper);
	            }
			
		
			return wrapper;
		}
		
		
		@Override
		protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception {
			return wrapper;
		}

		@Override
		protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception {
			return wrapper;
		}
		
		/**
		 * @param param
		 * @param expression
		 * @return
		 */
		private String formatDouble(Double param, String expression){
			
			DecimalFormat decimalFormat = new DecimalFormat(expression);
			String retVal=null;
			if(param!=null){
				retVal=decimalFormat.format(param);
			}
			return retVal;
		}		
	
		public void setMessageSource(MessageSource messageSource) {
			this.messageSource = messageSource;
		}
		public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
			this.phoenixFinancialInstitution = phoenixFinancialInstitution;
		}	
		public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
			this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
		}		
		public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
			this.notificationMessageManager = notificationMessageManager;
		}	
		public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
			this.userDeviceAccountsManager = userDeviceAccountsManager;
		}
		public MessageSource getMessageSource() {
			return messageSource;
		}
		public void setCommissionManager(CommissionManager commissionManager) {
			this.commissionManager = commissionManager;
		}
		public void setSettlementManager(SettlementManager settlementManager) {
			this.settlementManager = settlementManager;
		}
		public void setCustomerManager(CustTransManager customerManager) {
			this.customerManager = customerManager;
		}
		public void setProductDispenseController(ProductDispenseController productDispenseController) {
			this.productDispenseController = productDispenseController;
		}
		public void setProductManager(ProductManager productManager) {
			this.productManager = productManager;
		}
		public void setSmsSender(SmsSender smsSender) {
			this.smsSender = smsSender;
		}
		public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
			this.supplierBankInfoManager = supplierBankInfoManager;
		}	
		public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
			this.retailerContactManager = retailerContactManager;
		}	
		public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
			this.stakeholderBankInfoManager = stakeholderBankInfoManager;
		}
}