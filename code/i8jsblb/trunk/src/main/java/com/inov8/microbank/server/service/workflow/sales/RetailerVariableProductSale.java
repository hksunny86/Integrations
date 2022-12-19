package com.inov8.microbank.server.service.workflow.sales;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductCatalogDetailModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
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
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.dispenser.AllTecosTopupDispenser;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;

/**
 *f
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */


public class RetailerVariableProductSale
    extends VariableProductSaleTransaction
{
  protected final Log log = LogFactory.getLog(getClass());	
  private ProductManager productManager;
  private CommissionManager commissionManager;
  private SettlementManager settlementManager;
  private TransactionModuleManager transactionManager;
  private CustTransManager customerManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private ProductDispenseController productDispenseController ;
  private NotificationMessageManager notificationMessageManager;
  private RetailerContactManager retailerContactManager;
  private SmsSender smsSender;
  private ShipmentManager shipmentManager;
  private XStream xstream;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private AllTecosTopupDispenser allTecosTopupDispenser;

  public RetailerVariableProductSale()
  {
  }

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
      throws Exception
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;

    wrapper = super.doPreStart(wrapper);

    // Populate the Product model from DB
    baseWrapper.setBasePersistableModel(wrapper.getProductModel());
    baseWrapper = productManager.loadProduct(baseWrapper);
    wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

    // Populate Retailer Contact model from DB
    RetailerContactModel retailerContact = new RetailerContactModel();
    retailerContact.setRetailerContactId( wrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
    searchBaseWrapper.setBasePersistableModel( retailerContact );
    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
    wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

    // Populate Product's notification messages
    
    AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
    wrapper.setAppUserModel(appUserModel);

      NotificationMessageModel notificationMessage = new NotificationMessageModel();
      notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
      baseWrapper.setBasePersistableModel(notificationMessage);
      baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
      wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

//      wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
//          NotificationMessageModel) baseWrapper.getBasePersistableModel());
      NotificationMessageModel successMessage = new NotificationMessageModel();
      successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
      baseWrapper.setBasePersistableModel(successMessage);
      baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
      wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());


    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
    userDeviceAccountsModel.setAppUserId(wrapper.getFromRetailerContactAppUserModel().getAppUserId());
    userDeviceAccountsModel.setDeviceTypeId( wrapper.getDeviceTypeModel().getDeviceTypeId() );
    baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
    baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
    wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());
    
    //  Populate the OLA Smart Money Account from DB
	SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
	sma.setRetailerContactId( ThreadLocalAppUser.getAppUserModel().getRetailerContactId() ) ;
	baseWrapper.setBasePersistableModel(sma);
	baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
	wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());


    wrapper.setPaymentModeModel( new PaymentModeModel() ) ;
    wrapper.getPaymentModeModel().setPrimaryKey(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreProcess(wrapper);

    TransactionModel txModel = wrapper.getTransactionModel();

    txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

    //		txModel.setTransactionAmount(wrapper.getTransactionAmount());
    txModel.setTransactionAmount(wrapper.getTransactionAmount());
    txModel.setTotalAmount(wrapper.getTransactionAmount());
    txModel.setTotalCommissionAmount(0d) ;
    txModel.setDiscountAmount(0d);

    // Transaction Type model of transaction is populated
    txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

    // Sets the Device type
    txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());

    // Payment mode model of transaction is populated
    txModel.setPaymentModeId(wrapper.getPaymentModeModel().getPaymentModeId());

    txModel.setFromRetContactId( wrapper.getFromRetailerContactModel().getRetailerContactId() );
    
    
    txModel.setFromRetContactMobNo( wrapper.getFromRetailerContactAppUserModel().getMobileNo() );
    txModel.setProcessingBankId(wrapper.getOlaSmartMoneyAccountModel().getBankId());
    txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
    txModel.setSaleMobileNo( wrapper.getFromRetailerContactAppUserModel().getMobileNo() );
    txModel.setFromRetContactName(wrapper.getFromRetailerContactAppUserModel().getFirstName() + " " + wrapper.getFromRetailerContactAppUserModel().getLastName());

    txModel.setRetailerId( wrapper.getFromRetailerContactModel().getRetailerId() );
    wrapper.setTransactionModel(txModel);

    return wrapper;
  }


  /**
   * doValidate
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   * @todo Implement this
   *   com.inov8.microbank.server.workflow.transaction.SalesTransaction method
   */
  public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
  {
     //	 --------------------- Validates the Retailer's requirements -----------------------------
	    if (wrapper.getFromRetailerContactModel() != null)
	    {
	      if (!wrapper.getFromRetailerContactModel().getActive())
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE);
	      }
//              if( wrapper.getFromRetailerContactModel().getBalance().doubleValue() < wrapper.getTransactionAmount().doubleValue() )
//              {
//                throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_RETAILER_BALANCE);
//              }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
	    }

//	    // ------------------------Validates the Customer's requirements -----------------------------------
	      if ("".equals(wrapper.getCustomerAppUserModel().getMobileNo()))
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
	      }

	    //	--------------------------------  Validates the Product's requirements ---------------------------------
	    if ( wrapper.getProductModel() != null )
	    {
	      if ( !wrapper.getProductModel().getActive()  )
	      {
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE );
	      }
              if ( wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_VARIABLE.longValue()  )
              {
                throw new WorkFlowException( WorkFlowErrorCodeConstants.NOT_VARIABLE_PRODUCT );
              }

	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL );
	    }

	    //	-----------------------  Validates the Supplier's requirements ---------------------------------------------
	    if ( wrapper.getProductModel().getSupplierIdSupplierModel() != null )
	    {
	      if ( !wrapper.getProductModel().getSupplierIdSupplierModel().getActive()  )
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE );
	      }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NULL);
	    }

	    //  Validates the workflowWrapper's requirements
	    if ( wrapper.getTransactionAmount() <= 0 )
	    {
	       throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
	    }
            if(wrapper.getUserDeviceAccountModel() == null )
            {
              throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
            }

            //	-----------------------  Checks Is Product exist in Retailer's catalog -------------------------
            List<ProductCatalogDetailModel> catalogProducts = (List)wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getProductCatalogueIdProductCatalogModel().getProductCatalogIdProductCatalogDetailModelList() ;
            boolean isFound = false ;
            for( ProductCatalogDetailModel productCatalogDetailModel : catalogProducts )
            {
              if( productCatalogDetailModel.getProductId() != null && productCatalogDetailModel.getProductId().equals(wrapper.getProductModel().getProductId()))
              {
                isFound = true ;
                break;
              }
            }
            if( !isFound )
            {
              throw new WorkFlowException(WorkFlowErrorCodeConstants.PROD_NOT_IN_CATALOG);
            }

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

	    return wrapper;
  }

  /**
   * doVariableProductSale
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @todo Implement this
   *   com.inov8.microbank.server.workflow.transaction.VariableProductSaleTransaction
   *   method
   */
  public WorkFlowWrapper doVariableProductSale(WorkFlowWrapper wrapper) throws
      Exception
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    TransactionDetailModel txDetailModel = new TransactionDetailModel();
    
    CommissionAmountsHolder commissionAmountHolder = new CommissionAmountsHolder();
    commissionAmountHolder.setTotalAmount(wrapper.getTransactionModel().getTransactionAmount());
    commissionAmountHolder.setTransactionProcessingAmount(0D);
    commissionAmountHolder.setTransactionAmount(wrapper.getTransactionModel().getTransactionAmount());
    wrapper.setCommissionAmountsHolder(commissionAmountHolder) ;
    
    // ------ Calculate the commission ------------------------------------------------------
    CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
    commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
    commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
    commissionWrapper.setTransactionTypeModel(wrapper.getTransactionModel().getTransactionTypeIdTransactionTypeModel());

    
    RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
    
    commissionWrapper = this.commissionManager.calculateCommission(wrapper);
    
    // --------------------------------------------------------------------------------------

    // -------- Validate commission ---------------------------------------------------------
    commissionAmountHolder = (CommissionAmountsHolder)
        commissionWrapper.getCommissionWrapperHashMap().get( CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER );
    
    wrapper.setCommissionAmountsHolder(commissionAmountHolder);
    wrapper.getTransactionModel().setTotalAmount(commissionAmountHolder.getTotalAmount());
//    validateCommission(wrapper.getCommissionAmountsHolder(), productCommission);

    wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmountHolder.getTotalCommissionAmount());
	wrapper.getTransactionModel().setTransactionAmount(commissionAmountHolder.getTransactionAmount());
	
	txDetailModel.setActualBillableAmount(commissionAmountHolder.getBillingOrganizationAmount());
    
    baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());		
    AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		

    wrapper.setAccountInfoModel(new AccountInfoModel());
    wrapper.setSmartMoneyAccountModel(wrapper.getOlaSmartMoneyAccountModel());
    
    SwitchWrapper switchWrapper = new SwitchWrapperImpl();
    switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel()) ;
    switchWrapper.setWorkFlowWrapper(wrapper);
    wrapper.setSwitchWrapper(switchWrapper);	
    
    switchWrapper = abstractFinancialInstitution.transaction(switchWrapper);
    wrapper.setSwitchWrapper(switchWrapper);
    
    
    if( wrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD )
    {
    	wrapper = allTecosTopupDispenser.doSale(wrapper);
    }
    else
    {
	    ProductDispenser productDispense = (ProductDispenser)this.productDispenseController.loadProductDispenser( wrapper ) ;
	    wrapper = productDispense.verify( wrapper ) ;
	    wrapper = productDispense.doSale( wrapper ) ;
	    wrapper.setProductDispenser(productDispense);
    }
    



    wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
    txDetailModel.setConsumerNo(((BillPaymentVO) wrapper.getProductVO()).getConsumerNo());
//    @txDetailModel.setActualBillableAmount(productCommission.getTotalAmount());
    
    
//    @txDetailModel.setProductTopupAmount( productCommission.getTotalAmount() - productCommission.getTotalCommissionAmount() );
    txDetailModel.setProductTopupAmount( wrapper.getTransactionModel().getTransactionAmount() );
    
    txDetailModel.setProductIdProductModel(wrapper.getProductModel());
    wrapper.getTransactionModel().setNotificationMobileNo( wrapper.getCustomerAppUserModel().getMobileNo() );


    String txAmount = Formatter.formatNumbers(wrapper.getTransactionAmount());
    String seviceChargesAmount=Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() -
			wrapper.getTransactionModel().getTransactionAmount());
    
    if( StringUtils.countMatches(wrapper.getProductModel().getInstructionIdNotificationMessageModel().getSmsMessageText(), "?") == 2 )
    	wrapper.getTransactionModel().setConfirmationMessage(SMSUtil.buildVariableProductSMS(wrapper.getInstruction().getSmsMessageText(), 
    			txAmount, wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
    			wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()));
    else if( StringUtils.countMatches(wrapper.getProductModel().getInstructionIdNotificationMessageModel().getSmsMessageText(), "?") == 5 )
    	wrapper.getTransactionModel().setConfirmationMessage(SMSUtil.buildBillSaleSMS(wrapper.getInstruction().getSmsMessageText(), 
    			wrapper.getProductModel().getName(), txAmount, seviceChargesAmount,wrapper.getTransactionModel()
				.getTransactionCodeIdTransactionCodeModel().getCode(),((BillPaymentVO)wrapper.getProductVO()).getConsumerNo()));
    


    txDetailModel.setSettled(false);
    wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
    txManager.saveTransaction(wrapper);

    // --------------- Updates the Balance of Retailer Contact --------------------------------------
    wrapper.getFromRetailerContactModel().setBalance( wrapper.getFromRetailerContactModel().getBalance() - wrapper.getTransactionAmount() );
    wrapper.getFromRetailerContactModel().setUpdatedBy( wrapper.getFromRetailerContactAppUserModel().getAppUserId() ) ;
    wrapper.getFromRetailerContactModel().setUpdatedOn( new Date() ) ;
    baseWrapper.setBasePersistableModel( wrapper.getFromRetailerContactModel() );
    baseWrapper = this.retailerContactManager.createOrUpdateRetailerContact( baseWrapper ) ;
    wrapper.setFromRetailerContactModel( (RetailerContactModel)baseWrapper.getBasePersistableModel() ) ;
    // ----------------------------------------------------------------------------------------------

    // -------- Settle the Commission ---------------------------------------
    this.settlementManager.settleAllPayCommission(commissionWrapper, wrapper);
    // -------------------------------------------------------------------------

    txDetailModel.setSettled(true);
    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
    // ------------------------------------------------------------------------
    
    /*if(!(wrapper.getCustomerAppUserModel().getMobileNo().trim().equals(wrapper.getAppUserModel().getMobileNo())))
    {
    	smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
    }
    else
    {
    	smsSender.sendDelayed(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
    }
    */

    return wrapper;
  }

  public void validateCommission(CommissionAmountsHolder commissionHolder,
                                 CommissionAmountsHolder
                                 calculatedCommissionHolder) throws Exception
  {
    if (commissionHolder.getTotalCommissionAmount().doubleValue() !=
        calculatedCommissionHolder.getTotalCommissionAmount().doubleValue() )
    {
      throw new WorkFlowException(
          "Calculated commission and the one from external interface, dont matched");
    }
  }

  public void setCommissionManager(CommissionManager commissionManager)
  {
    this.commissionManager = commissionManager;
  }

  public void setSettlementManager(SettlementManager settlementManager)
  {
    this.settlementManager = settlementManager;
  }

  public void setTransactionManager(TransactionModuleManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }

  public void setCustomerManager(CustTransManager customerManager)
  {
    this.customerManager = customerManager;
  }

  public void setNotificationMessageManager(NotificationMessageManager
                                            notificationMessageManager)
  {
    this.notificationMessageManager = notificationMessageManager;
  }

  public void setProductDispenseController(ProductDispenseController
                                           productDispenseController)
  {
    this.productDispenseController = productDispenseController;
  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setXstream(XStream xstream)
  {
    this.xstream = xstream;
  }

  public void setRetailerContactManager(RetailerContactManager
                                        retailerContactManager)
  {
    this.retailerContactManager = retailerContactManager;
  }

  public void setShipmentManager(ShipmentManager shipmentManager)
  {
    this.shipmentManager = shipmentManager;
  }

  public void setUserDeviceAccountsManager(UserDeviceAccountsManager
                                           userDeviceAccountsManager)
  {
    this.userDeviceAccountsManager = userDeviceAccountsManager;
  }

  protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    return wrapper;
  }


public void setSmsSender(SmsSender smsSender)
{
	this.smsSender = smsSender;
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
	if (wrapper.getFinancialTransactionsMileStones()
			.getIsCommissionsSettled().size() != 0) {
		try
		{
			this.settlementManager.rollbackCommissionSettlement(wrapper);
		}
		catch(Exception e)
		{
			log.warn("Exception occured during Commission Settlement rollback : "+e.getStackTrace());
		}
	}
	if (wrapper.getFinancialTransactionsMileStones().isProductDispensed())
	{
		if(null != wrapper.getProductDispenser())
		{
			try
			{
				wrapper.getProductDispenser().rollback(wrapper);
			}
			catch(Exception e)
			{
				log.warn("Exception occured during Product Dispense rollback : "+e.getStackTrace());
			}
		}
	}

	return wrapper;
}


	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		
		if( wrapper.getSmartMoneyAccountModel() != null )
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
		    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());		
		    AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
				
		    SwitchWrapper switchWrapper = wrapper.getSwitchWrapper();
		    wrapper.setAccountInfoModel(new AccountInfoModel());
		    wrapper.setSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());		    
						    
		    switchWrapper = abstractFinancialInstitution.rollback(switchWrapper);
		    wrapper = switchWrapper.getWorkFlowWrapper() ;
		}
		
		return wrapper;
	}

	public void setAllTecosTopupDispenser(
			AllTecosTopupDispenser allTecosTopupDispenser) {
		this.allTecosTopupDispenser = allTecosTopupDispenser;
	}
}
