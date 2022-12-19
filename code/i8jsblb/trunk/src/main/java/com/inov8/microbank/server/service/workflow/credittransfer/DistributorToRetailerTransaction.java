package com.inov8.microbank.server.service.workflow.credittransfer;

import java.util.ArrayList;
import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.creditmodule.DistRetCreditManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

/**
 *
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

public class DistributorToRetailerTransaction
    extends CreditTransferTransaction
{
  private DistRetCreditManager distRetCreditManager;
  private DistributorContactManager distributorContactManager;
  private RetailerContactManager retailerContactManager;
  private SmsSender smsSender;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;

  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
{
	this.smartMoneyAccountManager = smartMoneyAccountManager;
}

public DistributorToRetailerTransaction()
  {
  }

  /**
   * creditTransfer
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   *
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {
	  
	RetailerContactModel toRetailerContactModel = wrapper.getToRetailerContactModel();
	DistributorContactModel fromDistContactModel = wrapper.getFromDistributorContactModel();
    wrapper.getTransactionModel().setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
    BaseWrapper baseWrapper = new BaseWrapperImpl();
	baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	switchWrapper.setWorkFlowWrapper(wrapper);
	switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
	abstractFinancialInstitution.creditTransfer(switchWrapper);
    wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    String txAmount = Formatter.formatNumbers(wrapper.getTransactionAmount());
    wrapper.getTransactionModel().setConfirmationMessage( SMSUtil.buildCreditTransferSMS( wrapper.getFromDistributorContactAppUserModel().getFirstName(), wrapper.getFromDistributorContactAppUserModel().getLastName(), wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), txAmount )) ;
    wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
    wrapper.getTransactionModel().setFromDistContactId(fromDistContactModel.getDistributorContactId());
    wrapper.getTransactionModel().setToRetContactId(toRetailerContactModel.getRetailerContactId());
    wrapper.getTransactionModel().setDistributorId(wrapper.getFromDistributorContactModel().getDistributorId());
    wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());

    txManager.saveTransaction(wrapper); //save the transaction
    
	ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

    SmsMessage smsMessage = new SmsMessage(wrapper.getToRetailerContactAppUserModel().getMobileNo(),wrapper.getTransactionModel().getConfirmationMessage());
    if(wrapper.getDeviceTypeModel().getDeviceTypeId() != DeviceTypeConstantsInterface.ALL_PAY)
    {
    	messageList.add(smsMessage) ;    // -----------------------------------------------------------------------
    }


    return wrapper;
  }

  protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception
  {

    // Validates the Distributor's requirements
    if (wrapper.getDistributorContactModel() != null)
    {
      if (!wrapper.getDistributorContactModel().getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_ACTIVE);
      }
    }
    else
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NULL);
    }

    // Validates the Retailer's requirements
    if (wrapper.getToRetailerContactModel() != null)
    {
      if (!wrapper.getToRetailerContactModel().getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.TORETAILER_NOT_ACTIVE);
      }
      if (!wrapper.getToRetailerContactModel().getHead())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_NOT_HEAD);
      }
      if (!wrapper.getToRetailerContactModel().getRetailerIdRetailerModel().getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.TORET_RETAILER_INACTIVE);
      }
    }
    else
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_RETAILER_AGAINST_MOBILENO);
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

    if(!wrapper.getDistributorContactModel().getDistributorId().equals(wrapper.getToRetailerContactModel().getRelationRetailerIdRetailerModel().getDistributorId()))
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.DIST_RETAIL_DIFF_DISTRIBUTOR);
    }

    return wrapper;
  }

  public void setDistRetCreditManager(DistRetCreditManager distRetCreditManager)
  {
    this.distRetCreditManager = distRetCreditManager;
  }

  protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
	txManager.saveTransaction(wrapper);
    return wrapper;
  }
  public void setDistributorContactManager(
			DistributorContactManager distributorContactManager) {
		this.distributorContactManager = distributorContactManager;
	}

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreStart(wrapper);
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

    baseWrapper.setBasePersistableModel( wrapper.getFromDistributorContactAppUserModel() ) ;
    baseWrapper = (BaseWrapper) distributorContactManager.loadDistributorContactByAppUser(baseWrapper) ;
    wrapper.setDistributorContactModel((DistributorContactModel)baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
    baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
   	wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
   	baseWrapper.setBasePersistableModel(wrapper.getReceivingSmartMoneyAccountModel());
   	baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
   	wrapper.setReceivingSmartMoneyAccountModel( (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel( wrapper.getFromDistributorContactAppUserModel() ) ;
    baseWrapper =  distributorContactManager.loadDistributorContactByAppUser(baseWrapper);
    wrapper.setFromDistributorContactModel((DistributorContactModel)baseWrapper.getBasePersistableModel());

    wrapper.getToRetailerContactAppUserModel().setAppUserTypeId( UserTypeConstantsInterface.RETAILER );
    searchBaseWrapper.setBasePersistableModel(wrapper.getToRetailerContactAppUserModel());
    wrapper.setToRetailerContactModel( retailerContactManager.findRetailerContactByMobileNumber(searchBaseWrapper) );

    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
    userDeviceAccountsModel.setAppUserId(wrapper.getFromDistributorContactAppUserModel().getAppUserId());
    userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
    baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
    baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
    wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());

    searchBaseWrapper.setBasePersistableModel(wrapper.getDistributorContactModel().getDistributorIdDistributorModel());
    DistributorContactModel nationalManager = distributorContactManager.findDistributorNationalManagerContact(searchBaseWrapper);
    wrapper.setDistributorNmContactModel(nationalManager);

    return wrapper;
  }


@Override
protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
{
	wrapper = super.doPreProcess(wrapper);
	// populate the transaction object from the workflow wrapper
	RetailerContactModel retailerContactModel = wrapper.getToRetailerContactModel();
	DistributorContactModel distributorContactModel = wrapper.getDistributorContactModel();

	wrapper.getTransactionModel().setToRetContactId(retailerContactModel.getRetailerContactId());
	wrapper.getTransactionModel().setFromDistContactId(distributorContactModel.getDistributorContactId());
	wrapper.getTransactionModel().setTotalAmount(wrapper.getTransactionAmount());
	wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
	wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
	wrapper.getTransactionModel().setFromDistContactMobNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
        wrapper.getTransactionModel().setSaleMobileNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
        wrapper.getTransactionModel().setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
        wrapper.getTransactionModel().setTotalCommissionAmount(0D);
        wrapper.getTransactionModel().setDiscountAmount(0D);
        wrapper.getTransactionModel().setDeviceTypeId( wrapper.getDeviceTypeModel().getDeviceTypeId() );
        wrapper.getTransactionModel().setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getPrimaryKey());
        wrapper.getTransactionModel().setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());

        wrapper.getTransactionModel().setDistributorNmContactId(wrapper.getDistributorNmContactModel().getDistributorContactId());

//        wrapper.getTransactionModel().setDistributorNmContactId( distributorContactModel.getManagingContactId() );
        wrapper.getTransactionModel().setDistributorId( distributorContactModel.getDistributorId() );

        if( retailerContactModel.getRetailerContactIdAppUserModelList() != null && ((List)retailerContactModel.getRetailerContactIdAppUserModelList()).get(0) != null )
        {
          AppUserModel retailerAppUser = (AppUserModel)((List)retailerContactModel.getRetailerContactIdAppUserModelList()).get(0) ;
          wrapper.getTransactionModel().setToRetContactName(retailerAppUser.getFirstName() + " " + retailerAppUser.getLastName()) ;
        }

        wrapper.getTransactionModel().setFromDistContactName(wrapper.getFromDistributorContactAppUserModel().getFirstName()+ " " + wrapper.getFromDistributorContactAppUserModel().getLastName());
	return wrapper;
      }

      public void setRetailerContactManager(RetailerContactManager retailerContactManager)
      {
        this.retailerContactManager = retailerContactManager;
      }

      public void setSmsSender(SmsSender smsSender)
      {
        this.smsSender = smsSender;
      }

  public void setUserDeviceAccountsManager(UserDeviceAccountsManager
                                           userDeviceAccountsManager)
  {
    this.userDeviceAccountsManager = userDeviceAccountsManager;
  }

}
