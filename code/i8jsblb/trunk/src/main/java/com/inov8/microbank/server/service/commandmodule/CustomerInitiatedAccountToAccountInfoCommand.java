package com.inov8.microbank.server.service.commandmodule;

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
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.ola.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;


public class CustomerInitiatedAccountToAccountInfoCommand extends BaseCommand
{
	private final Log logger = LogFactory.getLog(CustomerInitiatedAccountToAccountInfoCommand.class);
	private String productId,txAmount;
	private String receiverMobileNo,senderMobileNo;
	private AppUserModel receiverAppUserModel,senderAppUserModel,appUserModel;
	private Long senderSegmentId;
	private ProductModel productModel;
	private WorkFlowWrapper workflowWrapper;
	private DeviceTypeModel deviceTypeModel;
	private CustomerModel customerModel;
	private CommissionWrapper commissionWrapper;
	private CommissionAmountsHolder commissionAmountsHolder;
	private String receiverTitle;
	

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		String classNMethodName = getClass().getSimpleName() +".prepare(): ";
		logger.info("Start of "+classNMethodName);
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		receiverMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		senderMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		txAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		deviceTypeModel = new DeviceTypeModel(StringUtil.isNullOrEmpty(deviceTypeId) ? DeviceTypeConstantsInterface.USSD : Long.valueOf(deviceTypeId));
		
        BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
		
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
			receiverAppUserModel = commonCommandManager.loadAppUserByQuery(receiverMobileNo, UserTypeConstantsInterface.CUSTOMER);
			receiverTitle = receiverAppUserModel.getFirstName() + " " + receiverAppUserModel.getLastName();

			senderSegmentId = commonCommandManager.getCustomerSegmentIdByMobileNo(senderMobileNo);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
			logger.info("End of "+classNMethodName);
	}
	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		String classNMethodName = getClass().getSimpleName() + ".validate(): ";
		logger.info("Start of "+classNMethodName);
		
		ValidatorWrapper.doRequired(receiverMobileNo,validationErrors,"Receiver Mobile No");
		ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		ValidatorWrapper.doRequired(txAmount, validationErrors, "Transaction Amount");
		
        if(!validationErrors.hasValidationErrors()) {
            ValidatorWrapper.doInteger(productId,validationErrors,"Product");
            ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            ValidatorWrapper.doNumeric(txAmount,validationErrors,"Transaction Amount");
        }
		
		String error = "";
		if(!validationErrors.hasValidationErrors()){
			if(senderMobileNo.equals(receiverMobileNo )) {
				error = this.getMessageSource().getMessage("CUSTOMER.MONEYTRANSFER.SENDER_REC_SAME_MOB", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
		}
		if(!validationErrors.hasValidationErrors()){
	        if(null== customerModel) {
				error = "Customer not found ";
	        	logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
		}
		if(!validationErrors.hasValidationErrors()){
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

		
        DistributorModel distributorModel = new DistributorModel();
        distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);
		//validate product limits
        if(!validationErrors.hasValidationErrors()){
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

        if(!validationErrors.hasValidationErrors()){
    		if(null == senderAppUserModel || null == receiverAppUserModel){
    				if(null == senderAppUserModel){
        				error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_NOT_REG", null,null);
        				logger.error(classNMethodName + error);
        				ValidatorWrapper.addError(validationErrors, error);
        			}
    			if(!validationErrors.hasValidationErrors()){
    				if(null == receiverAppUserModel){
        				error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_NOT_REG", null,null);
        				logger.error(classNMethodName + error);
        				ValidatorWrapper.addError(validationErrors, error);
        			}
    			}
    		}else {
        			try {
        				if(!validationErrors.hasValidationErrors()){
        					validateBBCustomer(senderAppUserModel, "Sender - ");
        				}
        				if(!validationErrors.hasValidationErrors()){
        					validsateSmartMoneyAccount(senderAppUserModel, "Sender - ");
        				}
//        				if(!validationErrors.hasValidationErrors()){
//        					validateBBCustomer(receiverAppUserModel, "Receiver - ");
//        				}
        			} 
        			catch (FrameworkCheckedException e) {
        				logger.error(classNMethodName + e.getMessage()); 
        				ValidatorWrapper.addError(validationErrors, e.getMessage());
        			}
    			
    		}
        }

        //HRA validation check
        if(!validationErrors.hasValidationErrors()) {
            commonCommandManager.validateHRA(receiverMobileNo);
        }

        logger.info("End of "+classNMethodName);
		
		return validationErrors;
	}
	
	@Override
	public void execute() throws CommandException {
		
		String classNMethodName = getClass().getSimpleName() + ".execute(): ";
		
		logger.info("Start of "+classNMethodName);

		//create new workFlow wrapper
				workflowWrapper = new WorkFlowWrapperImpl();
				workflowWrapper.setProductModel(productModel);
				workflowWrapper.setAppUserModel(appUserModel);
				workflowWrapper.setTransactionAmount(Double.valueOf(txAmount));
				workflowWrapper.setDeviceTypeModel(deviceTypeModel);

				TransactionModel transactionModel = new TransactionModel();
				
				transactionModel.setTransactionAmount(Double.valueOf(txAmount));
				workflowWrapper.setTransactionModel(transactionModel);
				workflowWrapper.setSegmentModel(new SegmentModel(senderSegmentId));
				
				try {
					
					workflowWrapper.setTaxRegimeModel((TaxRegimeModel)customerModel.getTaxRegimeIdTaxRegimeModel().clone());
					workflowWrapper.setIsCustomerInitiatedTransaction(true);
					commissionWrapper = commonCommandManager.calculateCommission(workflowWrapper);
					commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

					SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
					sma.setCustomerId(appUserModel.getCustomerId());

					sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

					SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

//					commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);

				} 
				
				catch (FrameworkCheckedException e) {
					e.printStackTrace();
					throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
				} catch (Exception e) {
					e.printStackTrace();
				}

		logger.info("End of "+classNMethodName);
	}
	
	@Override
	public String response() {
		
		String classNMethodName = getClass().getSimpleName() + ".response(): ";
		
		logger.info("Start of "+classNMethodName);
		
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
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECIEVER_ACCOUNT_TITLE, receiverTitle));

		logger.info("End of "+classNMethodName);
		
		return MiniXMLUtil.createInfoResponseXMLByParams(params);
	}
	
	private void validateBBCustomer(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException{
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
	
	private void validsateSmartMoneyAccount(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException {
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
}
