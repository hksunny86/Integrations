package com.inov8.microbank.server.service.workflow.credittransfer;


import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

/**
 *
 * <p>Title: DistributorToDistributorTransaction</p>
 *
 * <p>Description: Class implements the logic for credit transfer between a distributor contact
 * to another distributor contact </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class DistributorToDistributorTransaction
    extends CreditTransferTransaction
{
  private DistDistCreditManager distDistCreditManager;
  private DistributorContactManager distributorContactManager;
  private SmsSender smsSender;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;


  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

public DistributorToDistributorTransaction()
  {
  }

  /**
   * Credit transfer transaction takes place over here
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {
    //this.populateTransactionObject(wrapper);
    DistributorContactModel fromDistContactModel = wrapper.getFromDistributorContactModel();
    DistributorContactModel toDistContactModel = wrapper.getToDistributorContactModel();
    Double transactionAmount = wrapper.getTransactionModel().getTransactionAmount();
    
//    wrapper.getDistributorContactModel().getBalance();
//    wrapper.getTransactionModel().getTransactionAmount();

    // Updates the balance of 'To' Distributor
    BaseWrapper baseWrapper = new BaseWrapperImpl();
	
	baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
	
	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	switchWrapper.setWorkFlowWrapper(wrapper);
	switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
	abstractFinancialInstitution.creditTransfer(switchWrapper);
	
    
    /*toDistContactModel.setBalance(toDistContactModel.getBalance() +
                                  transactionAmount);

    // Updates the balance of 'From' Distributor
    double distributorBalance = fromDistContactModel.getBalance() -
        transactionAmount;
    if (distributorBalance < 0.0)
    {
      // Log the failure reason
      throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_AMOUNT_TO_TRANSFER);
    }*/
//    fromDistContactModel.setBalance(distributorBalance);
    wrapper.getTransactionModel().setFromDistContactIdDistributorContactModel(wrapper.getFromDistributorContactModel());
    wrapper.getTransactionModel().setToDistContactIdDistributorContactModel(wrapper.getToDistributorContactModel());
    wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getToDistributorContactAppUserModel().getMobileNo());
    wrapper.getTransactionModel().setDistributorId(wrapper.getToDistributorContactModel().getDistributorId());
    
    String txAmount = Formatter.formatNumbers(wrapper.getTransactionAmount());
    wrapper.getTransactionModel().setConfirmationMessage( SMSUtil.buildCreditTransferSMS( wrapper.getFromDistributorContactAppUserModel().getFirstName(), wrapper.getFromDistributorContactAppUserModel().getLastName(), wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), txAmount ));
    wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());

//    this.distDistCreditManager.updateDistToDistContactBalance(wrapper);

//    txManager.saveTransaction(wrapper); //save the transaction

//    SmsMessage smsMessage = new SmsMessage(wrapper.getToDistributorContactAppUserModel().getMobileNo(),wrapper.getTransactionModel().getConfirmationMessage());
//    smsSender.send(smsMessage );
    // -----------------------------------------------------------------------


    return wrapper;
  }

  /**
   * doValidate
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   */
protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception
  {



//    this.populateTransactionObject(wrapper);
    DistributorContactModel toDistributorContactModel = wrapper.getToDistributorContactModel();
    DistributorContactModel fromDistributorContactModel = wrapper.getFromDistributorContactModel();
    
    
    if(fromDistributorContactModel.getDistributorIdDistributorModel().getNational() && !toDistributorContactModel.getHead())
    {
    	throw new WorkFlowException(WorkFlowErrorCodeConstants.TO_FROM_DISTRIBUTOR_SAME_LEVEL);
    }

    if(!fromDistributorContactModel.getDistributorIdDistributorModel().getNational() && !this.distributorContactManager.isManagingContact( fromDistributorContactModel.getDistributorContactId(), toDistributorContactModel.getDistributorContactId() ) )
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.TO_FROM_DISTRIBUTOR_SAME_LEVEL);
    }

    // Validates the Distributor's requirements
    if (toDistributorContactModel != null)
    {
      if (!toDistributorContactModel.getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.TODISTRIBUTOR_NOT_ACTIVE);
      }
      if (!toDistributorContactModel.getDistributorIdDistributorModel().getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.TODIST_DISTRIBUTOR_INACTIVE);
      }
    }
    else
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.TODISTRIBUTOR_NULL);
    }

    if(wrapper.getDistributorNmContactModel() == null)
    {
    	throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_NM_FOR_DISTRIBUTOR);
    }

    if(wrapper.getUserDeviceAccountModel() == null )
	{
		throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
	}

    // Validates the From Distributor's requirements
    if (fromDistributorContactModel != null)
    {
      if (!fromDistributorContactModel.getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMDISTRIBUTOR_NOT_ACTIVE);
      }
    }
    else
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMDISTRIBUTOR_NULL);
    }

    if ((fromDistributorContactModel.getDistributorLevelId() >= toDistributorContactModel.getDistributorLevelId()) && !fromDistributorContactModel.getDistributorIdDistributorModel().getNational())
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.TO_FROM_DISTRIBUTOR_SAME_LEVEL);
    }

    //  Validates the workflowWrapper's requirements
    if ( wrapper.getTransactionAmount() <= 0 )
    {
       throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
    }
    return wrapper;
  }

  public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    // Prepares the Transaction Wrapper object and sends it to the Transaction module( TransactionManager ) for
    // saving that transaction in the database

    txManager.saveTransaction(wrapper);

    return wrapper;
  }

  public void setDistDistCreditManager(DistDistCreditManager
                                       distDistCreditManager)
  {
    this.distDistCreditManager = distDistCreditManager;
  }


  protected WorkFlowWrapper doCreditTransferStart(WorkFlowWrapper wrapper)
  {
    return wrapper;
  }

  protected WorkFlowWrapper doCreditTransferEnd(WorkFlowWrapper wrapper)
  {
    return wrapper;
  }

  /**
   * Populates the transaction object with all the necessary data to save it in the db.
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {
	 wrapper = super.doPreStart(wrapper);
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    DistributorContactModel tempDistributorContactModel ;
    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
    baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
   	wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
   	baseWrapper.setBasePersistableModel(wrapper.getReceivingSmartMoneyAccountModel());
   	baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
   	wrapper.setReceivingSmartMoneyAccountModel( (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel( wrapper.getFromDistributorContactAppUserModel() ) ;
    baseWrapper =  distributorContactManager.loadDistributorContactByAppUser(baseWrapper);
    wrapper.setFromDistributorContactModel((DistributorContactModel)baseWrapper.getBasePersistableModel());

    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
	userDeviceAccountsModel.setAppUserId(wrapper.getFromDistributorContactAppUserModel().getAppUserId());
	userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
	baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
	baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
	wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());

    searchBaseWrapper.setBasePersistableModel(wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel());
    DistributorContactModel nationalManager = distributorContactManager.findDistributorNationalManagerContact(searchBaseWrapper);
    wrapper.setDistributorNmContactModel(nationalManager);


    AppUserModel tempAppUser = new AppUserModel();
    tempAppUser.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
    tempAppUser.setMobileNo( wrapper.getToDistributorContactAppUserModel().getMobileNo() ) ;
    searchBaseWrapper.setBasePersistableModel( tempAppUser ) ;
    tempDistributorContactModel = distributorContactManager.findDistributorContactByMobileNumber(searchBaseWrapper);
    if( tempDistributorContactModel == null )
      throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DISTRIBUTOR_AGAINST_MOBILENO);
    wrapper.setToDistributorContactModel(tempDistributorContactModel);

    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreProcess(wrapper);
    TransactionModel transactionModel = wrapper.getTransactionModel();

    transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    transactionModel.setTransactionAmount(wrapper.getTransactionAmount());
    transactionModel.setTotalAmount(wrapper.getTransactionAmount());
    transactionModel.setTotalCommissionAmount(0d);
    transactionModel.setDiscountAmount(0d);
    transactionModel.setFromDistContactId(wrapper.getFromDistributorContactModel().getDistributorContactId());
    transactionModel.setToDistContactId(wrapper.getToDistributorContactModel().getDistributorContactId());
//    transactionModel.setDistributorIdDistributorModel(wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel());
    transactionModel.setDistributorNmContactId(wrapper.getDistributorNmContactModel().getDistributorContactId());

    transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
    transactionModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
    transactionModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

    transactionModel.setFromDistContactMobNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
    transactionModel.setSaleMobileNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
    transactionModel.setToDistContactMobNo(wrapper.getToDistributorContactAppUserModel().getMobileNo());
    transactionModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getPrimaryKey());
    transactionModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
    transactionModel.setFromDistContactName(wrapper.getFromDistributorContactAppUserModel().getFirstName()+ " "+ wrapper.getFromDistributorContactAppUserModel().getLastName());

    if( wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList() != null && ((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) != null )
    {
      AppUserModel toDistAppUser = (AppUserModel)((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) ;
      wrapper.getTransactionModel().setToDistContactName(toDistAppUser.getFirstName() + " " + toDistAppUser.getLastName()) ;
    }

    transactionModel.setDistributorId( wrapper.getFromDistributorContactModel().getDistributorId() );

    return wrapper;
  }

  public void setDistributorContactManager(DistributorContactManager distributorContactManager)
  {
    this.distributorContactManager = distributorContactManager;
  }

  public void setSmsSender(SmsSender smsSender)
  {
    this.smsSender = smsSender;
  }

public DistributorContactManager getDistributorContactManager() {
	return distributorContactManager;
}

public void setUserDeviceAccountsManager(
		UserDeviceAccountsManager userDeviceAccountsManager) {
	this.userDeviceAccountsManager = userDeviceAccountsManager;
}

public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
{
	this.smartMoneyAccountManager = smartMoneyAccountManager;
}




}
