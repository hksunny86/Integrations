package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Atieq ur Rehman
 * @Modify by : Turab
 * <p>
 * 		This is abstract class used as parent for different Fund Transfer Info Commands. </br>
 * 		It prepares & validates basic properties, executes common logic and generate response for </br>
 * 	</p>
 * 
 * <li>BLB to BLB</li>
 * <li>BLB to CNIC </li>
 * <li>CNIC to BLB </li>
 * <li>CNIC to CNIC</li>
 * 
 */
public abstract class FundTransferInfoCommand extends BaseCommand {

	protected AppUserModel appUserModel;
	protected RetailerContactModel retailerContactModel;
	protected ProductModel productModel;
//	protected ProductVO productVO;
	protected DeviceTypeModel deviceTypeModel;
	protected CommissionWrapper commissionWrapper;
	protected CommissionAmountsHolder commissionAmountsHolder;
	protected SmartMoneyAccountModel agentSMAModel;
	protected WorkFlowWrapper workflowWrapper;
/*	protected List<AppUserModel> senderAppUserModelList, receiverAppUserModelList;*/
	protected AppUserModel senderAppUserModel, receiverAppUserModel;
	protected CommonCommandManager ccManager;
	protected final Log logger = LogFactory.getLog(getClass());

	protected String agentMobileNo, pin, txAmount, productId;
	protected String senderMobileNo, receiverMobileNo;
	protected String senderCNIC, receiverCNIC;
	protected Long senderSegmentId;
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		String classNMethodName = getClass().getSimpleName() +".prepare(): ";
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
	    
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        txAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        pin = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		
        ccManager = getCommonCommandManager();
        BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);

		//initialize device type model
		deviceTypeModel = new DeviceTypeModel(StringUtil.isNullOrEmpty(deviceTypeId) ? DeviceTypeConstantsInterface.USSD : Long.valueOf(deviceTypeId));

		// initialize product model
		try{
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
			bWrapper.setBasePersistableModel(productModel);
			bWrapper = ccManager.loadProduct(bWrapper);
			productModel = (ProductModel)bWrapper.getBasePersistableModel();
	        //productVO = ccManager.loadProductVO(bWrapper); commented by Turab; this should be done on command level not in base level
        }
        catch(FrameworkCheckedException ex) {
        	logger.error(classNMethodName +" Product model/vo not found: " + ex.getStackTrace().toString());
        	if(logger.isDebugEnabled())
        		ex.printStackTrace();
        }
		
		// initialize retailer contact model
        try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	bWrapper.setBasePersistableModel(retContactModel);
        	bWrapper = ccManager.loadRetailerContact(bWrapper);

        	retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        }
        catch(FrameworkCheckedException ex){
        	logger.error(classNMethodName + "Retailer contact model not found: " + ex.getStackTrace());
        	if(logger.isDebugEnabled())
        		ex.printStackTrace();

        }
        
        //initialize agent's SMA model
        try{
        	agentSMAModel = new SmartMoneyAccountModel();
	        agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
	        agentSMAModel.setDeleted(false);
	        bWrapper.setBasePersistableModel(agentSMAModel);
			
	        bWrapper = ccManager.loadOLASmartMoneyAccount(bWrapper);
			agentSMAModel = (SmartMoneyAccountModel)bWrapper.getBasePersistableModel();
        }
        catch(FrameworkCheckedException ex) {
        	logger.error(classNMethodName + "Invalid Agent SMA Account: " + ex.getStackTrace());
        	if(logger.isDebugEnabled())
        		ex.printStackTrace();
        }
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		String classNMethodName = getClass().getSimpleName() +".validate(): ";
		
		ValidatorWrapper.doRequired(productId, validationErrors, "Product");
		ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile Number");
		ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		//ValidatorWrapper.doRequired(pin, validationErrors, "Bank Pin");
		ValidatorWrapper.doRequired(txAmount, validationErrors, "Transaction Amount");

        if(!validationErrors.hasValidationErrors()) {
            ValidatorWrapper.doInteger(productId,validationErrors,"Product");
            ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            ValidatorWrapper.doNumeric(txAmount,validationErrors,"Transaction Amount");
        }
        
        String error = "";
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
            
            /*if(null == productVO) {
            	error =  "Product VO not found";
            	logger.error(classNMethodName + error);
    			ValidatorWrapper.addError(validationErrors, error);
            }*/
        }
        
		if(null == retailerContactModel) {
			error = "Retailer Contact Model is null. ";
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		try {
			//validate agent's app user
			ValidationErrors userValidation = ccManager.checkActiveAppUser(appUserModel);
		
			if(userValidation.hasValidationErrors()) {
				logger.error(classNMethodName + " App user validation failed."); 
				ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
			}
			
			// validate agent SMA Account
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(agentSMAModel);
			baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
			ValidationErrors ves = ccManager.checkSmartMoneyAccount(baseWrapper);
			
			if(ves.hasValidationErrors()) {
				logger.error(classNMethodName + ves.getErrors()); 
				ValidatorWrapper.addError(validationErrors, ves.getErrors());
			}
		} 
		
		catch (FrameworkCheckedException e) {
			logger.error(classNMethodName + e.getMessage()); 
			ValidatorWrapper.addError(validationErrors, e.getMessage());
		}

		//validate product limits
		try {
			getCommonCommandManager().checkProductLimit(senderSegmentId, productModel.getProductId(), appUserModel.getMobileNo(), 
					Long.valueOf(deviceTypeId), Double.valueOf(txAmount), productModel, null, handlerModel);
		}
		catch (Exception e) {
			logger.error(classNMethodName + e.getMessage()); 
			ValidatorWrapper.addError(validationErrors, e.getMessage());
        	if(logger.isDebugEnabled())
        		e.printStackTrace();
		}								
		
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		
		//create new workFlow wrapper 
		workflowWrapper = new WorkFlowWrapperImpl();
		workflowWrapper.setHandlerModel(handlerModel);
		workflowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		workflowWrapper.setProductModel(productModel);
		//workflowWrapper.setProductVO(productVO);
		workflowWrapper.setAppUserModel(appUserModel);
		workflowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		workflowWrapper.setTransactionAmount(Double.valueOf(txAmount));
		workflowWrapper.setDeviceTypeModel(deviceTypeModel);

		//turab
		TransactionModel transactionModel = new TransactionModel();
		//transactionModel.setTransactionAmount(a2aVO.getTransactionAmount());					
		
		if(agentSMAModel.getPaymentModeId() != null)
		{
			transactionModel.setPaymentModeId(agentSMAModel.getPaymentModeId());
			transactionModel.setTransactionAmount(Double.valueOf(txAmount));
			workflowWrapper.setTransactionModel(transactionModel);
		}
		//end  by turab
		
		workflowWrapper.setFromRetailerContactModel(retailerContactModel);
		workflowWrapper.setRetailerContactModel(retailerContactModel);
		workflowWrapper.setSegmentModel(new SegmentModel(senderSegmentId));
		
		try {
			
			workflowWrapper.setTaxRegimeModel((TaxRegimeModel)retailerContactModel.getTaxRegimeIdTaxRegimeModel().clone());
			commissionWrapper = ccManager.calculateCommission(workflowWrapper);
			commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		} 
		
		catch (FrameworkCheckedException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
	}

	
	public List<LabelValueBean> getResponse() {
		if(DeviceTypeConstantsInterface.USSD.toString().equals(deviceTypeId)) {
			return new ArrayList<LabelValueBean>();
		}
		
		else{
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
			return params;
		}
	}
	
	protected void validateBBCustomer(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException{
		ValidationErrors validationErrors = ccManager.checkActiveAppUser(customerAppUserModel);
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(prefix + validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		//Check User Device Accounts health
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.putObject(CommandFieldConstants.KEY_CUST_ERROR_PREFIX, prefix.substring(0, prefix.lastIndexOf(" -")));
		baseWrapper.setBasePersistableModel(customerAppUserModel);
		validationErrors = ccManager.checkCustomerCredentials(baseWrapper);
		
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	}
	
	protected void validsateSmartMoneyAccount(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
		searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		searchBaseWrapper = ccManager.loadSmartMoneyAccount(searchBaseWrapper);
		
		if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && 
				searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		ValidationErrors validationErrors = ccManager.checkSmartMoneyAccount(baseWrapper);
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
