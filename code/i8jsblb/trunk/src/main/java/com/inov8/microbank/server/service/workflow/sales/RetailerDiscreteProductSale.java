package com.inov8.microbank.server.service.workflow.sales;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductCatalogDetailModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.thoughtworks.xstream.XStream;


/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class RetailerDiscreteProductSale
    extends DiscreteProductSaleTransaction
{
  protected final Log log = LogFactory.getLog(getClass());
  private CustTransManager customerManager;
  private SettlementManager settlementManager;
  private CommissionManager commissionManager;
  private SmsSender smsSender;
  private ProductManager productManager;
  private XStream xstream;
  private ProductDispenseController productDispenseController ;
  private AppUserManager appUserManager;
  private RetailerContactManager retailerContactManager;
  private NotificationMessageManager notificationMessageManager;
  private UserDeviceAccountsManager userDeviceAccountsManager;


public RetailerDiscreteProductSale()
  {
  }

  /**
   * doDiscreteProductSaleTransaction
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @todo Implement this
   * com.inov8.microbank.server.workflow.transaction.DiscreteProductSaleTransaction
   * method
   */
  public WorkFlowWrapper doDiscreteProductSaleTransaction(WorkFlowWrapper
      wrapper)throws Exception
  {
    AuditLogModel failureLogModel = new AuditLogModel();
    BaseWrapper baseWrapper = new BaseWrapperImpl();

//    failureLogModel.setModuleName(ModuleNameConstantsInterface.RETAILER_TO_CUSTOMER_TRANSACTION);
    wrapper.setFailureLogModel(failureLogModel);
    TransactionModel txModel = wrapper.getTransactionModel();
    failureLogModel.setTransactionCodeId(txModel.getTransactionCodeId());
    TransactionDetailModel txDetailModel = new TransactionDetailModel();
    //===============================save the customer & User========================
//    wrapper = customerManager.saveCustomerAndUser(wrapper);
    CustomerModel customerModel = (CustomerModel) wrapper.getBasePersistableModel();
    //===============================Sell product===============================
    ProductUnitModel productUnitModel = new ProductUnitModel();
    ProductModel productModel = wrapper.getProductModel();

//    productUnitModel.setProductIdProductModel(wrapper.getProductModel());
//    wrapper.setBasePersistableModel(productUnitModel);
//    wrapper = productManager.sellDiscreteProduct(wrapper);
//    productUnitModel = (ProductUnitModel) wrapper.getBasePersistableModel();

    ProductDispenser productDispense = this.productDispenseController.loadProductDispenser( wrapper ) ;
    wrapper = productDispense.doSale( wrapper ) ;
    wrapper.setProductDispenser(productDispense);
    productUnitModel = (ProductUnitModel) wrapper.getBasePersistableModel();
    wrapper.setProductUnitModel(productUnitModel);
    CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
    
    RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
    
    commissionWrapper = commissionManager.calculateCommission(wrapper);
    CommissionAmountsHolder productCommission =
    (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
    txModel.setTotalCommissionAmount(productCommission.getTotalCommissionAmount());
    txModel.setTransactionAmount(productCommission.getTransactionAmount());
    txModel.setTotalAmount( productCommission.getTotalAmount() );
//    txDetailModel.setActualBillableAmount(productCommission.getTotalAmount());
    txDetailModel.setProductCostPrice(productModel.getCostPrice());
    txDetailModel.setProductUnitPrice(productModel.getUnitPrice());
    txDetailModel.setProductIdProductModel(wrapper.getProductModel());
    txDetailModel.setProductUnitId( productUnitModel.getProductUnitId() );
    //======================Settle Payment=============================

    txDetailModel.setSettled(false);
    wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
    wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
    wrapper.setTransactionDetailModel(txDetailModel);
    String txAmount = Formatter.formatNumbers(wrapper.getTransactionAmount());
    wrapper.getTransactionModel().setConfirmationMessage(SMSUtil.buildRetailerDiscreteProductSMS(wrapper));

    txManager.saveTransaction(wrapper);

    // --------------- Updates the Balance of Retailer Contact --------------------------------------
    wrapper.getFromRetailerContactModel().setBalance( wrapper.getFromRetailerContactModel().getBalance() - wrapper.getProductModel().getUnitPrice() );
    wrapper.getFromRetailerContactModel().setUpdatedBy( wrapper.getFromRetailerContactAppUserModel().getAppUserId() ) ;
    wrapper.getFromRetailerContactModel().setUpdatedOn( new Date() ) ;
    baseWrapper.setBasePersistableModel( wrapper.getFromRetailerContactModel() );
    baseWrapper = this.retailerContactManager.createOrUpdateRetailerContact( baseWrapper ) ;
    wrapper.setFromRetailerContactModel( (RetailerContactModel)baseWrapper.getBasePersistableModel() ) ;
    // ----------------------------------------------------------------------------------------------

    // -------- Settle the Commission ---------------------------------------
    this.settlementManager.settleCommission(commissionWrapper, wrapper);
    // -------------------------------------------------------------------------

    txDetailModel.setSettled(true);
    txManager.saveTransaction(wrapper);
    //======================Send sms to customer===============================
    if(!(wrapper.getCustomerAppUserModel().getMobileNo().trim().equals(wrapper.getAppUserModel().getMobileNo())))
    {
    	smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage())) ;
        SmsMessage message = new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage());
        message.setMessageText(wrapper.getTransactionModel().getConfirmationMessage());
        message.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        message.setMessageType("ZINDIGI");
        message.setTitle("Transaction Confirmation");
        this.smsSender.pushNotification(message);
    }
    else
    {
    	smsSender.sendDelayed(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage())) ;
    }

    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {

    wrapper = super.doPreStart(wrapper);
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

    baseWrapper.setBasePersistableModel(wrapper.getProductModel());
    baseWrapper = productManager.loadProduct(baseWrapper);
    wrapper.setProductModel((ProductModel)baseWrapper.getBasePersistableModel());

    RetailerContactModel retailerContact = new RetailerContactModel();
    retailerContact.setRetailerContactId( wrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
    searchBaseWrapper.setBasePersistableModel(retailerContact);
    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);

    wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());


    // Populate Product's notification messages

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
//      wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
//          NotificationMessageModel) baseWrapper.getBasePersistableModel());


    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
    userDeviceAccountsModel.setAppUserId(wrapper.getFromRetailerContactAppUserModel().getAppUserId());
    userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
    baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
    baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
    wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());


    PaymentModeModel paymentModeModel = new PaymentModeModel();
    paymentModeModel.setPrimaryKey(PaymentModeConstantsInterface.CASH);
    wrapper.setPaymentModeModel(paymentModeModel);

    wrapper.setTransactionAmount( wrapper.getProductModel().getUnitPrice() );

    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreProcess(wrapper);
    TransactionModel txModel = wrapper.getTransactionModel();
    txModel.setTransactionAmount(wrapper.getTransactionAmount());
    txModel.setTotalAmount(wrapper.getTotalAmount());
    txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

    txModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

    txModel.setFromRetContactIdRetailerContactModel(wrapper.getFromRetailerContactModel());
    txModel.setPaymentModeId(PaymentModeConstantsInterface.CASH);

    txModel.setTransactionAmount(wrapper.getProductModel().getUnitPrice());
    txModel.setTotalAmount(wrapper.getProductModel().getUnitPrice());
    txModel.setTotalCommissionAmount(0d) ;
    txModel.setFromRetContactMobNo( wrapper.getFromRetailerContactAppUserModel().getMobileNo() );
    txModel.setSaleMobileNo( wrapper.getFromRetailerContactAppUserModel().getMobileNo() );

    txModel.setNotificationMobileNo( wrapper.getCustomerAppUserModel().getMobileNo() );
    txModel.setRetailerId( wrapper.getFromRetailerContactModel().getRetailerId() );

    wrapper.setTransactionModel(txModel);

    wrapper.getTransactionModel().setFromRetContactName( wrapper.getFromRetailerContactAppUserModel().getFirstName() + " " + wrapper.getFromRetailerContactAppUserModel().getLastName() );

    return wrapper;
  }
  protected WorkFlowWrapper doPreEnd(WorkFlowWrapper wrapper)throws Exception
  {
//	  smsSender.send(wrapper.getCustomerAppUserModel().getMobileNo(),"Your pin is: "+wrapper.getProductUnitModel().getPin()+" and serial no is : "+wrapper.getProductUnitModel().getSerialNo() + " " + wrapper.getProductModel().getInstruction());
	  return wrapper;
  }

  public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
  {
	    // --------------------- Validates the Retailer's requirements -----------------------------
	    if (wrapper.getFromRetailerContactModel() != null)
	    {
	      if (!wrapper.getFromRetailerContactModel().getActive())
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE );
	      }
              if( wrapper.getFromRetailerContactModel().getBalance() < wrapper.getProductModel().getUnitPrice() )
              {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_RETAILER_BALANCE);
              }

	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
	    }

	    // ------------------------Validates the Customer's requirements -----------------------------------
	    if (wrapper.getCustomerAppUserModel() != null)
	    {
	      if ("".equals(wrapper.getCustomerAppUserModel().getMobileNo()))
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
	      }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
	    }

	    //	--------------------------------  Validates the Product's requirements ---------------------------------
	    if ( wrapper.getProductModel() != null )
	    {
	      if ( !wrapper.getProductModel().getActive()  )
	      {
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE );
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




            if(wrapper.getUserDeviceAccountModel() == null )
            {
              throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
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


	    //  Validates the workflowWrapper's requirements
	    if ( wrapper.getTransactionAmount() <= 0 )
	    {
	       throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
	    }

            //	-----------------------  Checks Is Product exist in Retailer's catalog -------------------------
            List<ProductCatalogDetailModel> catalogProducts = (List)wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getProductCatalogueIdProductCatalogModel().getProductCatalogIdProductCatalogDetailModelList() ;
            boolean isFound = false ;
            for(ProductCatalogDetailModel productCatalogDetailModel : catalogProducts)
            {
              if(productCatalogDetailModel.getProductId() != null && productCatalogDetailModel.getProductId().equals(wrapper.getProductModel().getProductId()))
              {
                isFound = true ;
                break;
              }
            }
            if( !isFound )
            {
              throw new WorkFlowException(WorkFlowErrorCodeConstants.PROD_NOT_IN_CATALOG);
            }

	  return wrapper;
  }


  public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    return wrapper;
  }
  public void setCustomerManager(CustTransManager customerManager)
  {
    this.customerManager = customerManager;
  }

  public void setSettlementManager(SettlementManager settlementManager)
  {
    this.settlementManager = settlementManager;
  }

  public void setCommissionManager(CommissionManager commissionManager)
  {
    this.commissionManager = commissionManager;
  }
  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setXstream(XStream xstream)
  {
    this.xstream = xstream;
  }

  public void setSmsSender(SmsSender smsSender)
  {
    this.smsSender = smsSender;
  }

  public void setProductDispenseController(ProductDispenseController
                                           productDispenseController)
  {
    this.productDispenseController = productDispenseController;
  }
  public void setRetailerContactManager(RetailerContactManager retailerContactManager)
  {
  	this.retailerContactManager = retailerContactManager;
  }

  public void setNotificationMessageManager(NotificationMessageManager
                                            notificationMessageManager)
  {
    this.notificationMessageManager = notificationMessageManager;
  }

  public void setUserDeviceAccountsManager(UserDeviceAccountsManager
                                           userDeviceAccountsManager)
  {
    this.userDeviceAccountsManager = userDeviceAccountsManager;
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
	// TODO Auto-generated method stub
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
	if(wrapper.getFinancialTransactionsMileStones().isProductDispensed())
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

	return wrapper;
}

}
