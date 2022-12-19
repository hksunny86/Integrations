package com.inov8.microbank.server.service.workflow.credittransfer;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			November 2008  			
 * Description:				
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;



public class DistibutorCreditRechargeTx
    extends CreditTransferTransaction
{
  protected final Log log = LogFactory.getLog(getClass());
  private TransactionModuleManager transactionManager;

  private AppUserManager appUserManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private NotificationMessageManager notificationMessageManager;
  private VeriflyManagerService veriflyController;
  private SwitchController switchController ;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private DistributorContactManager distributorContactManager;
  private OperatorManager operatorManager;
  


  public DistibutorCreditRechargeTx()
  {
  }

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of DistibutorCreditRechargeTx...");
		}
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		wrapper = super.doPreStart(wrapper);

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		
		// Populate the OLA Smart Money Account from DB
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		sma.setDistributorContactId( ThreadLocalAppUser.getAppUserModel().getDistributorContactId() ) ;
		baseWrapper.setBasePersistableModel(sma);
		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
		wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

	    //Populate Operator Bank Info Model
	    OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
	    operatorBankInfoModel.setOperatorId( PortalConstants.REF_DATA_OPERATOR );
	    operatorBankInfoModel.setPaymentModeId( wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId() );
	    operatorBankInfoModel.setBankId( wrapper.getOlaSmartMoneyAccountModel().getBankId() ) ;
	    baseWrapper.setBasePersistableModel( operatorBankInfoModel );

	    wrapper.setOperatorBankInfoModel( (OperatorBankInfoModel)this.operatorManager.getOperatorBankInfo( baseWrapper ).getBasePersistableModel() ) ;

		
		// Populate the Smart Money Account from DB
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
		
		DistributorContactModel distributorContact = new DistributorContactModel() ;
		distributorContact.setDistributorContactId( ThreadLocalAppUser.getAppUserModel().getDistributorContactId() ) ;
		baseWrapper.setBasePersistableModel( distributorContact ) ;
	    baseWrapper =  distributorContactManager.loadDistributorContact(baseWrapper);
	    wrapper.setFromDistributorContactModel((DistributorContactModel)baseWrapper.getBasePersistableModel());

		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of DistibutorCreditRechargeTx...");
		}
		return wrapper;
	}

  @Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of DistibutorCreditRechargeTx...");
		}
		wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		// txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTotalAmount(wrapper.getTransactionAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());


		txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());

		// Populate processing Bank Id
		txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		
		//	Populate processing Bank Id
		txModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.IN_PROGRESS ) ;
		
		txModel.setFromDistContactIdDistributorContactModel(wrapper.getFromDistributorContactModel());
		txModel.setFromDistContactMobNo( ThreadLocalAppUser.getAppUserModel().getMobileNo() ) ;
		txModel.setDistributorId(wrapper.getFromDistributorContactModel().getDistributorId());
		txModel.setFromDistContactName( ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() );
		wrapper.setTransactionModel(txModel);
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of DistibutorCreditRechargeTx...");
		}
		return wrapper;
	}
  

  public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
  {
    // ------------------------Validates the Customer's requirements -----------------------------------
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of DistibutorCreditRechargeTx...");
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

	  if (wrapper.getSmartMoneyAccountModel() != null)
	  {
		  if (!wrapper.getSmartMoneyAccountModel().getActive())
		  {
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.INACTIVE_OR_NO_SMA);
		  }
		  if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
		  {
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);
		  }
	  }
	  else
	  {
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.INACTIVE_OR_NO_SMA);
	  }

	  if(wrapper.getUserDeviceAccountModel() == null )
	  {
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
	  }

	  //  Validates the workflowWrapper's requirements
	  if ( wrapper.getTransactionAmount() <= 0 )
	  {
		  
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
	  }	 
	  
	  // Validates the From Distributor's COntact requirements
	    if (wrapper.getFromDistributorContactModel() != null)
	    {
	      if (!wrapper.getFromDistributorContactModel().getActive())
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.DIST_CONTACT_INACTIVE);
	      }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMDISTRIBUTOR_NULL);
	    }
	    
	    // Validates the From Distributor's requirements
	    if (wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel() != null)
	    {
	      if (!wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel().getActive())
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.DIST_INACTIVE);
	      }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMDISTRIBUTOR_NULL);
	    }

	  if(logger.isDebugEnabled())
	  {
		  logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of DistibutorCreditRechargeTx...");
	  }
	  return wrapper;
  }

  /**
   * doVariableProductSale
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   *
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside doVariableProductSale(Work`FlowWrapper wrapper) of DistibutorCreditRechargeTx...");
		}


      wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
    
      wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
      
      if( wrapper.getCustomerAccount() != null && wrapper.getCustomerAccount().getNumber() != null )
    	  wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));
      
      
      if(logger.isDebugEnabled())
  	  {
    	  logger.debug("Saving Transaction to DB...");
  	  }
      
      wrapper.getTransactionModel().setConfirmationMessage( SMSUtil.buildCreditTransferSMS( wrapper.getFromDistributorContactAppUserModel().getFirstName(), 
    		  wrapper.getFromDistributorContactAppUserModel().getLastName(), wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), 
    		  String.valueOf(wrapper.getTransactionModel().getTotalAmount())));
      
      wrapper.getTransactionModel().setNotificationMobileNo( wrapper.getAppUserModel().getMobileNo() ) ;
      
      System.out.println("$$$$$$$$$$$$$$$ " + wrapper.getTransactionModel().getTotalAmount());
      
      txManager.saveTransaction(wrapper);

      
	
      BaseWrapper baseWrapper = new BaseWrapperImpl();
		
      baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());		
      AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
      SwitchWrapper switchWrapper = new SwitchWrapperImpl();
      switchWrapper.setWorkFlowWrapper(wrapper);
      switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
      abstractFinancialInstitution.transaction(switchWrapper);
      
      
      // Debit the OLA Account -----------------------------------------------------------------------------------      
      baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());		
      abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
      switchWrapper = new SwitchWrapperImpl();
      switchWrapper.setWorkFlowWrapper(wrapper);
      switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
      abstractFinancialInstitution.debit(switchWrapper);
      //-----------------------------------------------------------------------------------------------------------
      
      wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
      txManager.saveTransaction(wrapper);
//      if(logger.isDebugEnabled())
//      {
//       logger.debug("Sending SMS to subscriber...");
//      }
//      
//      smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(),wrapper.getTransactionModel().getConfirmationMessage()));
    
    // -----------------------------------------------------------------------

    return wrapper;
  }

  public void setTransactionManager(TransactionModuleManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }

  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setNotificationMessageManager(NotificationMessageManager
                                            notificationMessageManager)
  {
    this.notificationMessageManager = notificationMessageManager;
  }

  public void setSwitchController(SwitchController switchController)
  {
    this.switchController = switchController;
  }

  public void setVeriflyController(VeriflyManagerService veriflyController)
  {
    this.veriflyController = veriflyController;
  }

  protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    return wrapper;
  }

  public void setUserDeviceAccountsManager(
  		UserDeviceAccountsManager userDeviceAccountsManager) {
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
		return wrapper;
	}

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	
	public void setDistributorContactManager(DistributorContactManager distributorContactManager)
	{
		this.distributorContactManager = distributorContactManager;
	}

	public void setOperatorManager(OperatorManager operatorManager)
	{
		this.operatorManager = operatorManager;
	}


}
