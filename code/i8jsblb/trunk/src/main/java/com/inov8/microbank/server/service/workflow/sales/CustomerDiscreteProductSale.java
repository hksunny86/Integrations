package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.creditmodule.Inov8CustomerCreditManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * Company: i8
 * </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class CustomerDiscreteProductSale extends DiscreteProductSaleTransaction
{
	
	private CommissionManager commissionManager;

	private SmartMoneyAccountManager smartMoneyAccountManager;

	private CustTransManager customerManager;

	private SettlementManager settlementManager;

	private SmsSender smsSender;

	private ProductManager productManager;

	private XStream xstream;

	private ProductUnitManager productUnitManager;

	private Inov8CustomerCreditManager inov8CustomerCreditManager;

	private OperatorManager operatorManager;

	private FailureLogManager failureLogManager;

	private AppUserManager appUserManager;

	private ProductDispenseController productDispenseController;

	private NotificationMessageManager notificationMessageManager;

	private VeriflyManagerService veriflyController;

	private SwitchController switchController;
	
	private FinancialIntegrationManager financialIntegrationManager;
	

	private UserDeviceAccountsManager userDeviceAccountsManager;
  private SupplierBankInfoManager supplierBankInfoManager;

  public CustomerDiscreteProductSale()
	{
	}

	/**
	 * doDiscreteProductSaleTransaction
	 *
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper doDiscreteProductSaleTransaction(WorkFlowWrapper wrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doDiscreteProductSaleTransaction(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
          TransactionModel txModel = wrapper.getTransactionModel();
          TransactionDetailModel txDetailModel = new TransactionDetailModel();
          wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

		// =============================== Sell PRODUCT
		// ===============================
		ProductModel productModel = wrapper.getProductModel();
		//ProductUnitModel productUnitModel = new ProductUnitModel();
		//
		// productUnitModel.setProductIdProductModel(wrapper.getProductModel());
		// wrapper.setBasePersistableModel(productUnitModel);
		// wrapper = productManager.sellDiscreteProduct(wrapper);
		// productUnitModel = (ProductUnitModel)
		// wrapper.getBasePersistableModel();
		ProductDispenser productDispense = this.productDispenseController.loadProductDispenser(wrapper);
		if(logger.isDebugEnabled())
		{
			logger.debug("Executing sale on ProductDispenser.....");
		}
		

		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setProductModel(wrapper.getProductModel());
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionModel().getTransactionTypeIdTransactionTypeModel());
		if(logger.isDebugEnabled())
		{
			logger.debug("Going to calculate commission using CommissionManager");
		}
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = commissionManager.calculateCommission(wrapper);

		CommissionAmountsHolder productCommission = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
				.get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		txModel.setTotalCommissionAmount(productCommission.getTotalCommissionAmount());
		txModel.setTransactionAmount(productCommission.getTransactionAmount());

//		txDetailModel.setActualBillableAmount(productCommission.getTotalAmount());
		txDetailModel.setProductCostPrice(productModel.getCostPrice());
		txDetailModel.setProductUnitPrice(productModel.getUnitPrice());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		
		wrapper.setCommissionAmountsHolder(productCommission);
		
		//txDetailModel.setProductUnitIdProductUnitModel(productUnitModel);

		// This should be changed once there could be more items in the
		// transaction model
		wrapper.getTransactionModel().setTotalAmount(productCommission.getTotalAmount());
		wrapper.getTransactionModel().setTotalCommissionAmount(productCommission.getTotalCommissionAmount());
		if(wrapper.getUserDeviceAccountModel().getCommissioned())
		{
			
			wrapper.setDiscountAmount(productModel.getFixedDiscount() + (productModel.getPercentDiscount()/100)* productCommission.getTotalAmount());
			wrapper.getTransactionModel().setDiscountAmount(wrapper.getDiscountAmount());
			if(null != wrapper.getDiscountAmount() && wrapper.getDiscountAmount() > wrapper.getTransactionModel().getTotalAmount())
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
			}
		}
		
		

		txDetailModel.setSettled(false);
                wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

		wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		
//		Change by Sheraz

//		if( wrapper.getCustomerAppUserModel().getMobileNo() == null )
			wrapper.getTransactionModel().setNotificationMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
//		else
//			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		
		String txAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount());
//		wrapper.getTransactionModel().setConfirmationMessage( SMSUtil.buildDiscreteProductSMS(wrapper.getInstruction().getSmsMessageText(), wrapper.getProductModel().getName(),wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), productUnitModel.getSerialNo(),productUnitModel.getPin() ,wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText())) ;
		
		 
		wrapper.getTransactionModel().setSupProcessingStatusId(1L);
		wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));
		if(logger.isDebugEnabled())
		{
			logger.debug("Saving Transaction in DB...");
		}
		
		wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED ) ;
		
		wrapper.getTransactionModel().setConfirmationMessage(" ") ;
        //txManager.saveTransaction(wrapper);

		// -------- Settle the Commission
		// -----------------------------------------
        
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		if(wrapper.getUserDeviceAccountModel().getCommissioned())
	    {
	    
	    	switchWrapper.setDiscountAmount(productModel.getFixedDiscount() + (productModel.getPercentDiscount()/100) * wrapper.getTransactionModel().getTotalAmount());
	    	logger.debug("User is commissioined so we need to set the discount which is "+ switchWrapper.getDiscountAmount());
	    	switchWrapper.setCommissioned(true);
	    }
		
		abstractFinancialInstitution.transaction(switchWrapper);
		
		try
		{
			wrapper = productDispense.doSale(wrapper); 
			wrapper.setProductDispenser(productDispense);
//		productUnitModel = (ProductUnitModel) wrapper.getBasePersistableModel();
//		wrapper.setProductUnitModel(productUnitModel);
			
			wrapper.getTransactionModel().setConfirmationMessage( SMSUtil.buildDiscreteProductSMS(wrapper.getInstruction().getSmsMessageText(), 
					wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
					"", "", "") );
			
			txDetailModel.setSettled(true);

			
			wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;
		}
		catch (Exception e)
		{			
			e.printStackTrace();			
			wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED ) ;
			
			wrapper.getTransactionModel().setConfirmationMessage(" ") ;
	        txManager.transactionRequiresNewTransaction(wrapper);
	        
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.SERVICE_DOWN ) ;

		}
		
		txManager.saveTransaction(wrapper);
		
		if(logger.isDebugEnabled())
    	{
        	logger.debug("Going to settle Commission using SettlementManager");
    	}
		this.settlementManager.settleCommission(commissionWrapper, wrapper);
		// -------------------------------------------------------------------------
		// Added after moving settlemnt code to Settlement module
		if(logger.isDebugEnabled())
		{
			logger.debug("Going to settle Bank Payment using SettlementManager");
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doDiscreteProductSaleTransaction(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}

		// -----------------------------------------------------------------------

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
		  logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
          wrapper = super.doPreStart(wrapper);
          BaseWrapper baseWrapper = new BaseWrapperImpl();
          SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
          // Populate the Product model from DB
          baseWrapper.setBasePersistableModel(wrapper.getProductModel());
          baseWrapper = productManager.loadProduct(baseWrapper);
          wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());
          UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
          userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
          userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
          baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
          baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
          wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());

          wrapper.setCustomerModel(new CustomerModel());
          wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());

          // Populate Product's notification messages
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

          // Populate the Customer model from DB
          searchBaseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
          searchBaseWrapper = customerManager.loadCustomer(searchBaseWrapper);
          wrapper.setCustomerModel((CustomerModel) searchBaseWrapper.getBasePersistableModel());

          // Populate the Smart Money Account from DB
          baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
          baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
          wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

          // Populate Supplier Bank Info Model
          SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
          supplierBankInfoModel.setSupplierId( wrapper.getProductModel().getSupplierId() );
          supplierBankInfoModel.setPaymentModeId( wrapper.getSmartMoneyAccountModel().getPaymentModeId() );
          baseWrapper.setBasePersistableModel( supplierBankInfoModel );
          wrapper.setSupplierBankInfoModel( (SupplierBankInfoModel)this.supplierBankInfoManager.loadSupplierBankInfo( baseWrapper ).getBasePersistableModel() ) ;

          //   Populate Operator Bank Info Model
          OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
          operatorBankInfoModel.setOperatorId( PortalConstants.REF_DATA_OPERATOR );
          operatorBankInfoModel.setPaymentModeId( wrapper.getSmartMoneyAccountModel().getPaymentModeId() );
          operatorBankInfoModel.setBankId( wrapper.getSmartMoneyAccountModel().getBankId() ) ;
          baseWrapper.setBasePersistableModel( operatorBankInfoModel );
          wrapper.setOperatorBankInfoModel( (OperatorBankInfoModel)this.operatorManager.getOperatorBankInfo( baseWrapper ).getBasePersistableModel() ) ;
          if(logger.isDebugEnabled())
          {
        	 logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		  }
          return wrapper;
        }

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		wrapper = super.doPreProcess(wrapper);
		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		// txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTransactionAmount(wrapper.getProductModel().getUnitPrice());
		txModel.setTotalAmount(wrapper.getTotalAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Customer model of transaction is populated
		txModel.setCustomerIdCustomerModel(wrapper.getCustomerModel());

		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

		
		
		// Customer mobile No
		txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        txModel.setSaleMobileNo( ThreadLocalAppUser.getAppUserModel().getMobileNo() );
                
        // Populate processing Bank Id
        txModel.setProcessingBankId( wrapper.getSmartMoneyAccountModel().getBankId() ) ;

		wrapper.setTransactionModel(txModel);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		return wrapper;
	}

	/**
	 * doPostDiscreteProductSaleTransaction
	 *
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside logTransaction(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		this.txManager.saveTransaction(wrapper);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending logTransaction(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		return wrapper;
	}

	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
	{
		// ------------------------Validates the Customer's requirements
		// -----------------------------------
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		if (wrapper.getCustomerModel() != null)
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

		if( wrapper.getSmartMoneyAccountModel() != null )
		{
			if (!wrapper.getSmartMoneyAccountModel().getActive())
			{
		
				throw new WorkFlowException(WorkFlowErrorCodeConstants.
						CUST_SMARTMONEY_INACTIVE);
			}
			if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
			{
		
				throw new WorkFlowException(WorkFlowErrorCodeConstants.
						CUST_SMARTMONEY_PIN_CHG_REQ);
			}
			if(!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
			{
		
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
			}
		}
		else
		{
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
		}


		// -------------------------------- Validates the Product's requirements
		// ---------------------------------
		if(wrapper.getUserDeviceAccountModel() == null )
		{
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}
		if (wrapper.getProductModel() != null)
		{
			if (!wrapper.getProductModel().getActive())
			{
		
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
			}
			if(wrapper.getProductModel().getInstructionId() == null)
			{
		
				throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_INSTRUCTION_FOR_PRODUCT);
			}
                        if ( wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_DISCRETE.longValue()  )
                        {
        
                          throw new WorkFlowException( WorkFlowErrorCodeConstants.NOT_DISCRETE_PRODUCT );
                        }
		}
		else
		{
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
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

                // ----------------------- Validates the Supplier Bank Info requirements -----------------
                if( wrapper.getSupplierBankInfoModel() == null ||
                    wrapper.getSupplierBankInfoModel().getAccountNo() == null ||
                    "".equals(wrapper.getSupplierBankInfoModel().getAccountNo()))
                {

                  throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
                }
                // ---------------------------------------------------------------------------------------
                
                // ----------------------- Validates the operator Bank Info requirements -----------------
                if( wrapper.getOperatorBankInfoModel() == null )
                {

                  throw new WorkFlowException(WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
                }
                // ---------------------------------------------------------------------------------------

                // ----------------------- Validates the Service's requirements ---------------------------
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

		// Validates the workflowWrapper's requirements
		// if (wrapper.getTransactionAmount() <= 0)
		// {
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
		// }
                
               
    if(logger.isDebugEnabled())
	{
       logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
	}
       return wrapper;
	}
	@Override
	protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper)throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doEnd(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		
//		String sms = 
//		SMSUtil.buildDiscreteProductSMS(wrapper.getInstruction().getSmsMessageText(), 
//				wrapper.getProductVO().getResponseCode(), wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
//				"", "", "") ;
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Sending SMS to user...");
		}
		this.smsSender.sendDelayed(new SmsMessage(wrapper.getAppUserModel().getMobileNo(),
				wrapper.getTransactionModel().getConfirmationMessage()
				));
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doEnd(WorkFlowWrapper wrapper) of CustomerDiscreteProductSale...");
		}
		return wrapper;
		
	}
	
	

	public void setCommissionManager(CommissionManager commissionManager)
	{
		this.commissionManager = commissionManager;
	}

	public void setSettlementManager(SettlementManager settlementManager)
	{
		this.settlementManager = settlementManager;
	}

	public void setProductUnitManager(ProductUnitManager productUnitManager)
	{
		this.productUnitManager = productUnitManager;
	}

	public void setInov8CustomerCreditManager(Inov8CustomerCreditManager inov8CustomerCreditManager)
	{
		this.inov8CustomerCreditManager = inov8CustomerCreditManager;
	}

	public void setOperatorManager(OperatorManager operatorManager)
	{
		this.operatorManager = operatorManager;
	}

	public void setFailureLogManager(FailureLogManager failureLogManager)
	{
		this.failureLogManager = failureLogManager;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public void setXstream(XStream xstream)
	{
		this.xstream = xstream;
	}

	public void setCustomerManager(CustTransManager customerManager)
	{
		this.customerManager = customerManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
	{
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setProductManager(ProductManager productManager)
	{
		this.productManager = productManager;
	}

	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}

	public void setProductDispenseController(ProductDispenseController productDispenseController)
	{
		this.productDispenseController = productDispenseController;
	}

	public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager)
	{
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setVeriflyController(VeriflyManagerService veriflyController)
	{
		this.veriflyController = veriflyController;
	}

	public void setSwitchController(SwitchController switchController)
	{
		this.switchController = switchController;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

  public void setSupplierBankInfoManager(SupplierBankInfoManager
                                         supplierBankInfoManager)
  {
    this.supplierBankInfoManager = supplierBankInfoManager;
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
protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper)throws Exception
{
//	if(wrapper.getFinancialTransactionsMileStones().isProductDispensed())
//	{
//		try
//		{
//			wrapper.getProductDispenser().rollback(wrapper);
//		}
//		catch(Exception e)
//		{
//			logger.warn("Exception occured during Product Dispense rollback : "+e.getStackTrace());
//		}
//	}
//	if (wrapper.getFinancialTransactionsMileStones()
//			.getIsCommissionsSettled().size() != 0) {
//		try{
//			this.settlementManager.rollbackCommissionSettlement(wrapper);
//		}
//		catch(Exception e)
//		{
//			logger.warn("Exception occured during Commission Settlement rollback : "+e.getStackTrace());
//		}
//	}
//	if (wrapper.getFinancialTransactionsMileStones()
//			.isCustomerBankAccountDebitted()) {
//		try
//		{
//			this.settlementManager.rollbackBankPayment(wrapper);
//		}
//		catch(Exception e)
//		{
//			logger.warn("Exception occured during Bank Settlement rollback : "+e.getStackTrace());
//		}
//	}

	// TODO Auto-generated method stub
	return wrapper;
}

public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}
}
