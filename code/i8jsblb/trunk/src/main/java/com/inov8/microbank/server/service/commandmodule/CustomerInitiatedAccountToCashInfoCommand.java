package com.inov8.microbank.server.service.commandmodule;

import java.util.ArrayList;

import com.inov8.microbank.common.util.*;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;

public class CustomerInitiatedAccountToCashInfoCommand extends BaseCommand{

	private WorkFlowWrapper workflowWrapper;
	private String senderMobileNo, receiverMobileNo;
	private String receiverCNIC;
	private AppUserModel appUserModel,senderAppUserModel, receiverAppUserModel;
	private DeviceTypeModel deviceTypeModel;
	private ProductModel productModel;
	private CustomerModel customerModel;
	private Long senderSegmentId;
	private String txAmount, productId;
	private CommissionWrapper commissionWrapper;
	private CommissionAmountsHolder commissionAmountsHolder;
	
	private final Log logger = LogFactory.getLog(CustomerInitiatedAccountToCashInfoCommand.class);
	//DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	
	@Override
	public void execute() throws CommandException
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerInitiatedAccountToCashInfoCommand.execute()");
		}
		
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(Double.valueOf(txAmount));
		
		//create new workFlow wrapper 
		workflowWrapper = new WorkFlowWrapperImpl();
		
		workflowWrapper.setProductModel(productModel);
		workflowWrapper.setAppUserModel(appUserModel);
		workflowWrapper.setTransactionAmount(Double.valueOf(txAmount));
		workflowWrapper.setDeviceTypeModel(deviceTypeModel);
		workflowWrapper.setTransactionModel(transactionModel);
		workflowWrapper.setSegmentModel(new SegmentModel(senderSegmentId));
		
		try {

			//customerAppUserModel = commonCommandManager.loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
			if(appUserModel == null){
				throw new CommandException(MessageUtil.getMessage("22012"), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH);
			}

			else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
				CustomerModel customerModel = new CustomerModel();
				try {
					customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
				} catch (CommandException e) {
					e.printStackTrace();
				}

				if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
					logger.error("[CashDepositInfoCommand.execute] Upgrade Account L0 to L1 to perform Transaction.: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo);
					throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

				}
			}


			workflowWrapper.setTaxRegimeModel((TaxRegimeModel)customerModel.getTaxRegimeIdTaxRegimeModel().clone());
			workflowWrapper.setIsCustomerInitiatedTransaction(true);
			commissionWrapper = commonCommandManager.calculateCommission(workflowWrapper);
			commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

			SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
			sma.setCustomerId(appUserModel.getCustomerId());

			sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

			SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

			commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel , commissionAmountsHolder.getTotalAmount(), true);

		}catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
	}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerInitiatedAccountToCashInfoCommand.execute()");
		}
}		

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerInitiatedAccountToCashInfoCommand.prepare()");
		}
		
		String classNMethodName = getClass().getSimpleName() +".prepare(): ";
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		senderMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		receiverMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		receiverCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
		deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		txAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		
        BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
		
		deviceTypeModel = new DeviceTypeModel(StringUtil.isNullOrEmpty(deviceTypeId) ? DeviceTypeConstantsInterface.USSD : Long.valueOf(deviceTypeId));
		
	// initialize product model
	try{
		productModel = new ProductModel();
		productModel.setProductId(Long.parseLong(productId));
		bWrapper.setBasePersistableModel(productModel);
		bWrapper = commonCommandManager.loadProduct(bWrapper);
		productModel = (ProductModel)bWrapper.getBasePersistableModel();
    }
    catch(FrameworkCheckedException ex) {
    	logger.error(classNMethodName +" Product model/vo not found: " + ex.getStackTrace().toString());
    	if(logger.isDebugEnabled())
    		ex.printStackTrace();
    }
	
	// initialize sender customer  model
    try{
    	CustomerModel custModel = new CustomerModel();
    	custModel.setCustomerId(appUserModel.getCustomerId());
    	
    	bWrapper.setBasePersistableModel(custModel);
    	bWrapper = commonCommandManager.loadCustomer(bWrapper);

    	customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
    }
    catch(FrameworkCheckedException ex){
    	logger.error(classNMethodName + "Customer model not found: " + ex.getStackTrace());
    	if(logger.isDebugEnabled())
    		ex.printStackTrace();

    }
	
	try {
		senderAppUserModel = appUserModel;
		receiverAppUserModel = commonCommandManager.checkAppUserTypeAsWalkinCustoemr(receiverMobileNo);
		
		
		senderSegmentId = commonCommandManager.getCustomerSegmentIdByMobileNo(senderMobileNo);
		
	} 
	catch (Exception e) {
		e.printStackTrace();
	   }
	
	if(logger.isDebugEnabled())
	{
		logger.debug("End of CustomerInitiatedAccountToCashInfoCommand.prepare()");
	}
	
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerInitiatedAccountToCashInfoCommand.validate()");
		}
		
		String classNMethodName = getClass().getSimpleName() + ".validate(): ";
		ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		ValidatorWrapper.doRequired(receiverMobileNo,validationErrors,"Receiver Mobile No");
		ValidatorWrapper.doRequired(receiverCNIC,validationErrors,"Receiver CNIC");
		
		ValidatorWrapper.doRequired(productId, validationErrors, "Product");
		ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		ValidatorWrapper.doRequired(txAmount, validationErrors, "Transaction Amount");

        if(!validationErrors.hasValidationErrors()) {
            ValidatorWrapper.doInteger(productId,validationErrors,"Product");
            ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            ValidatorWrapper.doNumeric(txAmount,validationErrors,"Transaction Amount");
        }
        
        
        String error = "";
        if(!validationErrors.hasValidationErrors()) {
            if(null== productModel) {
    			error = "Product not found ";
            	logger.error(classNMethodName + error);
    			ValidatorWrapper.addError(validationErrors, error);
    		}
            
            else{
                Long integrationVOId = productModel.getProductIntgVoId();
                Long integrationModuleId = productModel.getProductIntgModuleInfoId();
            	
                if(null == integrationVOId || null == integrationModuleId) {
                	error = "Product integration module/vo not found";
                	logger.error(classNMethodName + error);
        			ValidatorWrapper.addError(validationErrors, error);
                }
            }
        }

/*        if(!validationErrors.hasValidationErrors()) {
    		if(senderMobileNo.equals(receiverMobileNo )) {
    			error = this.getMessageSource().getMessage("CUSTOMER.MONEYTRANSFER.SENDER_REC_SAME_MOB", null,null);
    			logger.error(classNMethodName + error);
    			ValidatorWrapper.addError(validationErrors, error);
    		}
        }*/

        if(!validationErrors.hasValidationErrors()) {
        	if(null == senderAppUserModel) {
    			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_NOT_REG", null,null);
    			logger.error(classNMethodName + error);
    			ValidatorWrapper.addError(validationErrors, error);
    		}else {
			try {
				 if(!validationErrors.hasValidationErrors()) {
					 validateBBCustomer(senderAppUserModel, "Sender - ");
				 }
				 if(!validationErrors.hasValidationErrors()) {
					 validsateSmartMoneyAccount(senderAppUserModel, "Sender - ");
				 }
			} 
			catch (FrameworkCheckedException e) {
				logger.error(classNMethodName + e.getMessage()); 
				ValidatorWrapper.addError(validationErrors, e.getMessage());
			}

		}
     }   	
		
		//validate product limits
		
        DistributorModel distributorModel = new DistributorModel();
        distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);
		
        if(!validationErrors.hasValidationErrors()) {
    		try {
    			commonCommandManager.checkProductLimit(senderSegmentId, productModel.getProductId(), "abc", 
    					Long.valueOf(deviceTypeId), Double.valueOf(txAmount), productModel, distributorModel, handlerModel);
    		}
    		catch (Exception e) {
    			logger.error(classNMethodName + e.getMessage()); 
    			ValidatorWrapper.addError(validationErrors, e.getMessage());
            	if(logger.isDebugEnabled())
            		e.printStackTrace();
    		}
        }
		
        if(!validationErrors.hasValidationErrors()) {
    		try{
    			if(commonCommandManager.loadAppUserByCnicAndType(receiverCNIC) != null){
    				error=this.getMessageSource().getMessage("MONEYTRANSFER.REC_NIC_ALREADY_REG", null,null);
    				logger.error(classNMethodName + error);
    				ValidatorWrapper.addError(validationErrors, error);
    			}
    			
    		}catch(FrameworkCheckedException fe){
    				fe.printStackTrace();
    		}
        }

        if(!validationErrors.hasValidationErrors()) {
        	if(null != receiverAppUserModel) {
    			error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_ALREAY_REG", null,null);
    			logger.error(classNMethodName + error);
    			ValidatorWrapper.addError(validationErrors, error);
    		}
        }
		
		if(!validationErrors.hasValidationErrors()) {
	        if(this.getCommonCommandManager().isCnicBlacklisted(receiverCNIC)) {
	                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
	                throw new CommandException(validationErrors.getErrors(),ErrorCodes.TERMINATE_EXECUTION_FLOW,ErrorLevel.MEDIUM,new Throwable());		                
	        }		           
	    }
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerInitiatedAccountToCashInfoCommand.validate()");
		}
		
		return validationErrors;
	}
	
	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerInitiatedAccountToCashInfoCommand.response()");
		}
		return toXML();
	}
	
	private String toXML()
	{
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();

			params.add(new LabelValueBean(CommandFieldConstants.KEY_PROD_ID, replaceNullWithEmpty(productId)));
			
			if(commissionAmountsHolder != null) {
				params.add(new LabelValueBean(CommandFieldConstants.KEY_COMM_AMOUNT, replaceNullWithEmpty(commissionAmountsHolder.getTotalCommissionAmount()+"")));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_PROCESS_AMNT, replaceNullWithEmpty(commissionAmountsHolder.getTransactionProcessingAmount()+"")));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_TOTAL_AMOUNT, replaceNullWithEmpty(commissionAmountsHolder.getTotalAmount()+"")));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_AMOUNT, replaceNullWithEmpty(commissionAmountsHolder.getTransactionAmount()+"")));
				params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
				}
	
		
		params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(receiverCNIC)));
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerInitiatedAccountToCashInfoCommand.response()");
		}
		return MiniXMLUtil.createInfoResponseXMLByParams(params);
	}
	
	protected void validsateSmartMoneyAccount(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
		searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
		
		if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && 
				searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(prefix + validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());
		}
		
		if(smartMoneyAccountModel.getName() == null){
			throw new CommandException(prefix + "SMA Model name is null", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());
		}
		
		if(!smartMoneyAccountModel.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())){
			throw new CommandException(prefix + this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	}
	
	protected void validateBBCustomer(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException{
		ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(customerAppUserModel);
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(prefix + validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		//Check User Device Accounts health
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.putObject(CommandFieldConstants.KEY_CUST_ERROR_PREFIX, prefix.substring(0, prefix.lastIndexOf(" -")));
		baseWrapper.setBasePersistableModel(customerAppUserModel);
		validationErrors = commonCommandManager.checkCustomerCredentials(baseWrapper);
		
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	}


}
