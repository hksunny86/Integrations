package com.inov8.microbank.common.util;

import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class SMSUtil
{
  public SMSUtil()
  {
  }

  public static String buildSMS(String message, String firstArg, String secondArg, String transCode)
  {
    message = message.replaceFirst( "[?]", firstArg )
        .replaceFirst( "[?]", secondArg )
        .replaceFirst( "[?]", transCode ) ;

    return message ;
  }
  
  public static String buildDiscreteProductSMS(String message, String productName, String firstArg, String secondArg, String thirdArg)
  {
	  UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
	  if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
	  {
		  message = message.replaceFirst( "[?]", productName )
		  .replaceFirst("[?]", firstArg)
		  .replaceFirst( "[?]", secondArg ).replaceFirst("[?]", thirdArg).replaceFirst("[?]", ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE);
	  }
	  else
	  {
		  message = message.replaceFirst( "[?]", productName )
		  .replaceFirst("[?]", firstArg)
		  .replaceFirst( "[?]", secondArg ).replaceFirst("[?]", thirdArg);
	  }
        

    return message ;
  }
  
  public static String buildRetailerDiscreteProductSMS(WorkFlowWrapper wrapper)
  {
	  String sms = "";
	  Object[] args = {Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductUnitModel().getSerialNo(),wrapper.getProductUnitModel().getPin(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
	  sms = MessageUtil.getMessage("retailerdiscretesale.sms", args);
	  return sms;
  }
  public static String buildRetailerVariableProductSMS(WorkFlowWrapper wrapper)
  {
	  String sms = "";
	  Object[] args = {Formatter.formatNumbers(wrapper.getTransactionAmount()),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
	  sms = MessageUtil.getMessage("retailervariablesale.sms", args);
	  return sms;
  }
  
  
  public static String buildDiscreteProductSMSForUser(WorkFlowWrapper wrapper)
  {
	  
	  
	  String sms = "";
	  if(null != wrapper.getTransactionModel() && null != wrapper.getTransactionModel().getBankResponseCode() && !wrapper.getTransactionModel().getBankResponseCode().equals(""))
	  {
		  if(null != wrapper.getUserDeviceAccountModel() && wrapper.getUserDeviceAccountModel().getCommissioned())
		  {
			  Object[] args = {wrapper.getProductModel().getName(),wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"\nAuth: " + wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE};
			  sms = MessageUtil.getMessage("discretesale.usersms", args);
		  }
		  else
		  {
			  Object[] args = {wrapper.getProductModel().getName(),wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"\nAuth: " + wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
			  sms = MessageUtil.getMessage("discretesale.usersms", args);
		  }
	  }
	  else
	  {
		  if(null != wrapper.getUserDeviceAccountModel() && wrapper.getUserDeviceAccountModel().getCommissioned())
		  {
			  Object[] args = {wrapper.getProductModel().getName(),wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"",wrapper.getTransactionCodeModel().getCode(),ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE};
			  sms = MessageUtil.getMessage("discretesale.usersms", args);
		  }
		  else
		  {
			  Object[] args = {wrapper.getProductModel().getName(),wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"",wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
			  sms = MessageUtil.getMessage("discretesale.usersms", args);
		  }
	  }
		

    return sms ;
  }
  public static String buildVariableProductSMSForUser(WorkFlowWrapper wrapper)
  {
	  
	  String sms = "";
	  
	  if(wrapper.getProductModel().getInstructionIdNotificationMessageModel().getSmsMessageText().contains("SKMT"))
	  {
		  Object[] args = {Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getBankIdBankModel().getName(),wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
		  sms = MessageUtil.getMessage("variablesale.skmtsms", args);
	  }
	  else if(null != wrapper.getTransactionModel() && null != wrapper.getTransactionModel().getBankResponseCode() && !wrapper.getTransactionModel().getBankResponseCode().equals(""))
	  {
		  if(null != wrapper.getUserDeviceAccountModel() && wrapper.getUserDeviceAccountModel().getCommissioned())
		  {
			  Object[] args = {wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"\nAuth: " + wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE};
			  sms = MessageUtil.getMessage("variablesale.usersms", args);
		  }
		  else
		  {
			  Object[] args = {wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"\nAuth: " + wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
			  sms = MessageUtil.getMessage("variablesale.usersms", args);
		  }
	  }
	  else
	  {
		  if(null != wrapper.getUserDeviceAccountModel() && wrapper.getUserDeviceAccountModel().getCommissioned())
		  {
			  Object[] args = {wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"",wrapper.getTransactionCodeModel().getCode(),ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE};
			  sms = MessageUtil.getMessage("variablesale.usersms", args);
		  }
		  else
		  {
			  Object[] args = {wrapper.getCustomerAppUserModel().getMobileNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"",wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
			  sms = MessageUtil.getMessage("variablesale.usersms", args);
		  }
	  }

    return sms ;
  }
  
  public static String buildBillPaymentSMSForUser(WorkFlowWrapper wrapper)
  {
	  
	  String sms = "";
	  
	  if(wrapper.getProductModel().getInstructionIdNotificationMessageModel().getSmsMessageText().contains("SKMT"))
	  {
		  Object[] args = {Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getBankIdBankModel().getName(),wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
		  sms = MessageUtil.getMessage("variablesale.skmtsms", args);
	  }
	  else if(null != wrapper.getTransactionModel() && null != wrapper.getTransactionModel().getBankResponseCode() && !wrapper.getTransactionModel().getBankResponseCode().equals(""))
	  {
		  if(null != wrapper.getUserDeviceAccountModel() && wrapper.getUserDeviceAccountModel().getCommissioned())
		  {
			  
			  Object[] args = {wrapper.getProductModel().getSupplierIdSupplierModel().getName(),wrapper.getTransactionDetailModel().getConsumerNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount()),Formatter.formatNumbers(wrapper.getTxProcessingAmount()),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"\nAuth Code: "+wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE};
			  sms = MessageUtil.getMessage("billsale.usersms", args);
		  }
		  else
		  {
			  Object[] args = {wrapper.getProductModel().getSupplierIdSupplierModel().getName(),wrapper.getTransactionDetailModel().getConsumerNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount()),Formatter.formatNumbers(wrapper.getTxProcessingAmount()),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"\nAuth Code: "+wrapper.getTransactionModel().getBankResponseCode(),wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
			  sms = MessageUtil.getMessage("billsale.usersms", args);
		  }
	  }
	  else
	  {
		  if(null != wrapper.getUserDeviceAccountModel() && wrapper.getUserDeviceAccountModel().getCommissioned())
		  {
			  Object[] args = {wrapper.getProductModel().getSupplierIdSupplierModel().getName(),wrapper.getTransactionDetailModel().getConsumerNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount()),Formatter.formatNumbers(wrapper.getTxProcessingAmount()),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"",wrapper.getTransactionCodeModel().getCode(),ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE};
			  sms = MessageUtil.getMessage("billsale.usersms", args);
		  }
		  else
		  {
			  Object[] args = {wrapper.getProductModel().getSupplierIdSupplierModel().getName(),wrapper.getTransactionDetailModel().getConsumerNo(),Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount()),Formatter.formatNumbers(wrapper.getTxProcessingAmount()),Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount()),wrapper.getTransactionModel().getUpdatedOn(),wrapper.getSmartMoneyAccountModel().getName(),"",wrapper.getTransactionCodeModel().getCode(),wrapper.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()};
			  sms = MessageUtil.getMessage("billsale.usersms", args);
		  }
	  }
	  

    return sms ;
  }

  
  

  
  
  
  public static String buildVariableProductSMS(String message, String firstArg, String secondArg, String thirdArg )
  {
	  UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
	  if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
	  {
	  
		  message = message.replaceFirst( "[?]", firstArg )
		  .replaceFirst( "[?]", secondArg ).replaceFirst("[?]", ApplicationNamesConstantsInterface.MICROBANK_HELP_LINE);
	  }
	  else
	  {
		  message = message.replaceFirst( "[?]", firstArg )
		  .replaceFirst( "[?]", secondArg ).replaceFirst("[?]", thirdArg);
	  }

    return message ;
  }
  
  public static String buildBillSaleSMS(String message, String firstArg, String secondArg, String thirdArg , String fourthArg,String fifthArg)
  {
	  UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
	  if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
	  {
    message = message.replaceFirst( "[?]", firstArg )
        .replaceFirst( "[?]", secondArg )
        .replaceFirst( "[?]", thirdArg ).replaceFirst("[?]", fourthArg).replaceFirst("[?]", fifthArg);
        
	  }
	  else
	  {
		  message = message.replaceFirst( "[?]", firstArg )
	        .replaceFirst( "[?]", secondArg )
	        .replaceFirst( "[?]", thirdArg ).replaceFirst("[?]", fourthArg).replaceFirst("[?]", fifthArg);
		  
	  }

    return message ;
  }

  public static String buildCreditTransferSMS(String mfsId, String lastName, String transCode, String amount)
  {
	  
    String message = "i8 credit of PKR "
            + amount + " has been transferred to your account by " + mfsId;


    return message ;
  }

  public static String buildProductSaleSMS(String pin, String serial, String transCode, String instructions)
  {
	  String sms = "";

		  Object[] args = {pin,serial,transCode,(instructions == null ? "" : instructions )};
		  sms = MessageUtil.getMessage("productsale.sms", args);

    return sms ;
  }

  public static String buildBillSaleSMS( String transCode, String instructions, String totalAmount)
  {
    String message = "Your bill has been paid. "
        + "Transaction Amount is: " + totalAmount
        + ". Transaction Code is: " + transCode
        + ". " + (instructions == null ? "" : instructions ) ;

    return message ;
  }



}
