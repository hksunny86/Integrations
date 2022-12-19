package com.inov8.microbank.server.service.workflow.credittransfer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.creditmodule.Inov8DistributorCreditManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;


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

public class OperatorToDistributorTransaction
    extends CreditTransferTransaction
{
  private Inov8DistributorCreditManager inov8DistributorCreditManager;
  private OperatorManager operatorManager;
  private DistributorContactManager distributorContactManager;
  private DistributorManager distributorManager;
  private SmsSender smsSender;




//  private TransactionManager transactionManager;


/**
   * Contains all the logic for validation
   *
   * @param wrapper
   * @return
   */
 public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws WorkFlowException
  {
    System.out.println("doValidate called in I8toDistributorTransaction");
    // all the validation logic goes here

    DistributorContactModel distributorContactModel = wrapper.getToDistributorContactModel();
    OperatorModel operatorModel = wrapper.getOperatorModel();

    // Validates the Distributor's requirements
    if (distributorContactModel != null)
    {
      if (!distributorContactModel.getActive())
      {
        throw new WorkFlowException( WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_ACTIVE );
      }
      if (!distributorContactModel.getHead() )
      {
        throw new WorkFlowException( WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_NATIONAL_MANAGER );
      }
    }
    else
    {
      throw new WorkFlowException( WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NULL );
    }

    // Validates the i8User's requirements
    if (operatorModel == null)
    {
      throw new WorkFlowException( WorkFlowErrorCodeConstants.OPERATOR_MODEL_NULL);
    }

    //  Validates the workflowWrapper's requirements
    if ( null==wrapper.getTransactionAmount() || wrapper.getTransactionAmount() <= 0  )
    {
       throw new WorkFlowException( WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
    }
    System.out.println("doValidate called in I8toDistributorTransaction");
    return wrapper;
  }
  /**
   * This method uses the Transaction Module to log the transaction in DB
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   */
  public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    System.out.println(
        "Use the transaction Module to log the transaction into the transaction tables");

    // Prepares the Transaction Wrapper object and sends it to the Transaction module( TransactionManager ) for
    // saving that transaction in the database

    txManager.saveTransaction(wrapper);

    return wrapper;
  }

  /**
   * This method transfers the credit from i8 to the Distributor Contact
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {

    // Updates the balance of the Distributor
	  TransactionModel txModel = wrapper.getTransactionModel();
	  BaseWrapper baseWrapper = new BaseWrapperImpl();


    // Updates the balance of the Operator
    double operatorBalance = wrapper.getOperatorModel().getBalance() -
        wrapper.getTransactionModel().getTransactionAmount();
    if (operatorBalance < 0.0)
    {
      // Log the failure reason
      throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.INSUFFICIENT_AMOUNT_TO_TRANSFER );
    }
    else
    {

        wrapper.getToDistributorContactModel().setBalance(wrapper.getToDistributorContactModel().getBalance()+
                txModel.getTransactionAmount());
    wrapper.getOperatorModel().setBalance(operatorBalance);
    wrapper.getOperatorModel().setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    wrapper.getOperatorModel().setUpdatedOn(new Date());
    wrapper.getTransactionModel().setDistributorId(wrapper.getToDistributorContactModel().getDistributorId());
    wrapper.getTransactionModel().setToDistContactMobNo(wrapper.getAppUserModel().getMobileNo());
    
    
     String txAmount = Formatter.formatNumbers(wrapper.getTransactionAmount());
    String message = new String();
    message="You have received credit of amount " 
		+ txAmount + " from " + wrapper.getOperatorModel().getName()/*wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName()*/ +
        ". Transaction Id is: "
        + wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode();
	
    ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
	
    SmsMessage smsMessage = new SmsMessage(wrapper.getAppUserModel().getMobileNo(),message);
    wrapper.getTransactionModel().setConfirmationMessage(message);
    wrapper.getTransactionModel().setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());
    
 
    this.inov8DistributorCreditManager.updateInov8DistributorContactBalance(wrapper);

    txManager.saveTransaction(wrapper); //save the transaction
    if(wrapper.getDeviceTypeModel().getDeviceTypeId() != DeviceTypeConstantsInterface.ALL_PAY)
    {

    	messageList.add(smsMessage);
    	wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
    }
    }
    // -----------------------------------------------------------------------

    return wrapper;
  }
  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
	  wrapper = super.doPreProcess(wrapper);
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

	  TransactionModel txModel = wrapper.getTransactionModel();
	  txModel.setTransactionAmount(wrapper.getTransactionAmount());
	  txModel.setTotalAmount(wrapper.getTransactionAmount());
          // Should be passed from Command module
       //   txModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
	  txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
	  
	 // wrapper.setToDistributorContactModel(new DistributorContactModel()) ;
  	 //wrapper.getToDistributorContactModel().setDistributorId( txModel.getToDistContactId() ) ;
	  
	  
	  txModel.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_REGISTER);
	  txModel.setDistributorNmContactIdDistributorContactModel(wrapper.getToDistributorContactModel());
	  txModel.setTotalCommissionAmount(0D);
	  txModel.setSupProcessingStatusId(1L);
	  txModel.setConfirmationMessage("ConfirmationMessage");
	  txModel.setTransactionTypeId(wrapper.getTransactionTypeModel().getTransactionTypeId());

	  
	  
	  
	  txModel.setToDistContactId( wrapper.getToDistributorContactModel().getDistributorContactId() );
          //txModel.setToDistContactMobNo( wrapper.getToDistributorContactAppUserModel().getMobileNo() );

          if( wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList() != null && ((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) != null )
          {
            AppUserModel toDistAppUser = (AppUserModel)((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) ;
            wrapper.getTransactionModel().setToDistContactName(toDistAppUser.getFirstName() + " " + toDistAppUser.getLastName()) ;
            wrapper.getTransactionModel().setNotificationMobileNo(toDistAppUser.getMobileNo());
            wrapper.setAppUserModel(toDistAppUser);
          }
	  return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreStart(wrapper);

    BaseWrapper baseWrapper = new BaseWrapperImpl();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    TransactionModel txModel = wrapper.getTransactionModel();
    baseWrapper.setBasePersistableModel( wrapper.getAppUserModel() ) ;
    baseWrapper = operatorManager.loadOperatorByAppUser(baseWrapper);
    wrapper.setOperatorModel((OperatorModel)baseWrapper.getBasePersistableModel());

    
     DistributorModel distributorModel = new DistributorModel();
	  distributorModel.setDistributorId(wrapper.getDistributorModel().getDistributorId());
	  searchBaseWrapper.setBasePersistableModel(distributorModel);
	  
	 if (!(distributorContactManager.isDistributorActive(searchBaseWrapper)))
	 {
	  if(!distributorContactManager.isDistributorContact(searchBaseWrapper))
	  {
		  searchBaseWrapper.setBasePersistableModel(distributorModel);
		  if (!distributorContactManager.isDistributorContactActive(searchBaseWrapper))
	      {
			  searchBaseWrapper.setBasePersistableModel(distributorModel);
			  if (distributorContactManager.isDistributorContactHead(searchBaseWrapper) )
		      {
		        throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_NATIONAL_MANAGER );
		      }
			 
	      }
		  else
		  {
			  throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_ACTIVE );
			  
		  }
	     
		  
	  }
	  else 
	  {
		  
		  throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NULL);
	  }
	  
	 }
	 
	 else 
	 {
		 
		 throw new FrameworkCheckedException("DistributorInActive" );
	 }
	
	 if ( null==wrapper.getTransactionAmount() || wrapper.getTransactionAmount() <= 0  )
		 
	    {
	       throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
	    }

	 DistributorContactModel distributorContactModel=distributorContactManager.findDistributorNationalManagerContact(searchBaseWrapper);
	  distributorContactModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	  distributorContactModel.setUpdatedOn(new Date());
	  wrapper.setToDistributorContactModel(distributorContactModel);
	  wrapper.getToDistributorContactModel().setDistributorIdDistributorModel(distributorModel) ;
	  //wrapper.setToDistributorContactModel(new DistributorContactModel()) ;
    //searchBaseWrapper.setBasePersistableModel(wrapper.getAppUserModel());
    //wrapper.setToDistributorContactModel( distributorContactManager.findDistributorContactByMobileNumber(searchBaseWrapper) );

//    AppUserModel tempAppUser = new AppUserModel();
//    tempAppUser.setMobileNo( wrapper.getToDistributorContactAppUserModel().getMobileNo() ) ;
//    searchBaseWrapper.setBasePersistableModel( tempAppUser ) ;
//    wrapper.setToDistributorContactModel( distributorContactManager.findDistributorContactByMobileNumber(searchBaseWrapper) );

    return wrapper;
  }

 
  public void setInov8DistributorCreditManager(Inov8DistributorCreditManager
                                               inov8DistributorCreditManager)
  {
    this.inov8DistributorCreditManager = inov8DistributorCreditManager;
  }

public void setOperatorManager(OperatorManager operatorManager) {
	this.operatorManager = operatorManager;
}

public void setDistributorContactManager(
		DistributorContactManager distributorContactManager) {
	this.distributorContactManager = distributorContactManager;
}

      public void setDistributorManager(DistributorManager distributorManager)
      {
        this.distributorManager = distributorManager;
      }

      public void setSmsSender(SmsSender smsSender)
      {
        this.smsSender = smsSender;
      }


}
