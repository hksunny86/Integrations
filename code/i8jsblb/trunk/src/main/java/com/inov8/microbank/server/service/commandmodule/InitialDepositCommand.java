/**
 * 
 */
package com.inov8.microbank.server.service.commandmodule;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

/**
 * @author Atieq Rehman
 * 
 * <p>Initial Deposit Command</p>
 *
 */
public class InitialDepositCommand extends BaseCommand {

	private AppUserModel appUserModel, customerAppUserModel;
	private BaseWrapper baseWrapper;
	
	private String customerMobileNo, agentMobileNo, txAmount;
	private String productId, encryptionType, agentPIN;
	private RetailerContactModel retailerContactModel;
	private ProductModel productModel;
	private CustomerModel customerModel;
	private CashInVO initialDeposit;
	private TransactionModel transactionModel;
	private boolean isBvsAccount;
	private final Log logger = LogFactory.getLog(InitialDepositCommand.class);
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		String classNMethodName = "[InitialDepositCommand.prepare] ";
		if(logger.isDebugEnabled()){
			logger.debug("Start of " + classNMethodName);
		}
	      
		this.baseWrapper = baseWrapper;  
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);

        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        agentPIN = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);

        customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        txAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        
        if (null != getCommandParameter(baseWrapper, "IS_BVS_ACCOUNT") && !"".equals(getCommandParameter(baseWrapper, "IS_BVS_ACCOUNT"))) {
            this.isBvsAccount = getCommandParameter(baseWrapper, "IS_BVS_ACCOUNT").equals("1") ? true : false;
        } else {
            this.isBvsAccount = false;
        }
       
    	
		try{
        	BaseWrapper bWrapper = new BaseWrapperImpl();
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
			bWrapper.setBasePersistableModel(productModel);
			bWrapper = getCommonCommandManager().loadProduct(bWrapper);
			productModel = (ProductModel)bWrapper.getBasePersistableModel();
        }
        catch(Exception ex){
        	logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace().toString());
        }
		
        try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        }
        catch(Exception ex){
        	logger.error(classNMethodName + "Retailer contact model not found: " + ex.getStackTrace());
        }
        
        try{
        	customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(customerMobileNo);
			customerAppUserModel = getCommonCommandManager().loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
			
			if(customerAppUserModel != null) {
				customerModel = new CustomerModel();
				customerModel.setCustomerId(customerAppUserModel.getCustomerId());
				
				BaseWrapper bWrapper = new BaseWrapperImpl();
				bWrapper = new BaseWrapperImpl();
				bWrapper.setBasePersistableModel(customerModel);
				bWrapper = getCommonCommandManager().loadCustomer(bWrapper);
				customerModel = (CustomerModel)bWrapper.getBasePersistableModel();
			}
        }
        catch(Exception ex){
        	logger.error(classNMethodName + "Customer App user/model not found: " + ex.getStackTrace());
        }
        
        if(logger.isDebugEnabled()) {
			logger.debug("End of " + classNMethodName);
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		String classNMethodName = "[InitialDepositCommand.validate] ";
		String inputParams = "Logged in AppUserID: " + 
				ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Customer Mobile No: " + customerMobileNo;
	
		if(logger.isDebugEnabled()) {
			logger.debug("Start of " + classNMethodName);
		}
		
		ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
		ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");

		ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");
		ValidatorWrapper.doRequired(agentPIN, validationErrors, "Product Id");

		ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
		ValidatorWrapper.doRequired(txAmount, validationErrors, "Initial Deposit");
			
		if(!validationErrors.hasValidationErrors()) 
			ValidatorWrapper.doNumeric(txAmount,validationErrors,"Initial Deposit");
		
		if(agentMobileNo.equals(customerMobileNo )) 
			ValidatorWrapper.addError(validationErrors, "Agent and Customer mobile nos. cannot be same.");
		
		if(null== productModel) {
			logger.error(classNMethodName + "Product model not found: " + inputParams);
			ValidatorWrapper.addError(validationErrors, "Product not found.");
		}
		
		if(null == retailerContactModel) {
			logger.error(classNMethodName + "Retailer Contact Model is null. " + inputParams);
			ValidatorWrapper.addError(validationErrors, "Retailer Contact not found.");
		}
		
		if(null == customerAppUserModel) {
			logger.error(classNMethodName + "Customer App User not found: " + inputParams);
			ValidatorWrapper.addError(validationErrors, "Customer not found.");
		}
		else {
			try {
				ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(customerAppUserModel);
				
				if(userValidation.hasValidationErrors()) {
					logger.error(classNMethodName + " Customer validation failed"); 
					ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
				}
			}
			catch (FrameworkCheckedException e) {
				logger.error(classNMethodName + e.getMessage()); 
				ValidatorWrapper.addError(validationErrors, e.getMessage());
			}
		}
		
		if(null == customerModel) {
			logger.error(classNMethodName + "Customer model not found: " + inputParams);
			ValidatorWrapper.addError(validationErrors, "Customer not found.");
		}

		try {
			ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);
		
			if(userValidation.hasValidationErrors()) {
				logger.error(classNMethodName + " User validation failed."); 
				ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
			}
		} catch (FrameworkCheckedException e) {
			logger.error(classNMethodName + e.getMessage()); 
			ValidatorWrapper.addError(validationErrors, e.getMessage());
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("End of " + classNMethodName);
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		String classNMethodName = "[InitialDepositCommand.execute] ";
		String exceptionMessage = "Exception Occured for Logged in AppUser";
		String inputParams = " ID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Mobile No:" + customerMobileNo ;
		String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
		
		if (this.isBvsAccount) {
			workFlowWrapper.putObject("isBvsAccount", isBvsAccount);			
		}
		try{
//			AccountInfoModel accountInfoModel = new AccountInfoModel();
//			accountInfoModel.setOldPin(agentPIN);
//			workFlowWrapper.setAccountInfoModel(accountInfoModel);
			workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
				
			// check Product Limits
			getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(), 
					Long.valueOf(deviceTypeId), Double.parseDouble(txAmount), productModel, null, workFlowWrapper.getHandlerModel());

			String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
			// Check Velocity Conditions
			checkVelocityCondition(userId);
			
			
			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setOldPin(agentPIN);
			workFlowWrapper.setAccountInfoModel(accountInfoModel);

			
			ProductVO productVo = getCommonCommandManager().loadProductVO(baseWrapper);
			
			if(productVo == null) {
				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
			workFlowWrapper.setCustomerModel(customerModel);
			workFlowWrapper.setSegmentModel(new SegmentModel());
			workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());
			workFlowWrapper.setToSegmentId(customerModel.getSegmentId());
			
			workFlowWrapper.setProductVO(productVo);
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setAppUserModel(appUserModel);
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
			
			//Agent smart money account
			SmartMoneyAccountModel agentSMAModel = loadSmartMoneyAccountModel(UserTypeConstantsInterface.RETAILER);
//			verifyPIN(agentSMAModel, agentPIN);

			workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);

			//Customer smart money account
			SmartMoneyAccountModel customerSMAModel = loadSmartMoneyAccountModel(UserTypeConstantsInterface.CUSTOMER);
			workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMAModel);
			
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.INITIAL_DEPOSIT_TX);
			workFlowWrapper.setRetailerContactModel(retailerContactModel);
			workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
			
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);

			logger.info(classNMethodName + " Going to execute Transaction flow. " + inputParams);

			workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);
		
			transactionModel = workFlowWrapper.getTransactionModel();
			
			initialDeposit = (CashInVO) workFlowWrapper.getProductVO();
			initialDeposit.setTransactionProcessingAmount(workFlowWrapper.getTxProcessingAmount());
			initialDeposit.setTotalAmount(workFlowWrapper.getTotalAmount());
			initialDeposit.setCustomerBalance(((CashInVO)workFlowWrapper.getProductVO()).getCustomerBalance());
			initialDeposit.setAgentBalance(((CashInVO)workFlowWrapper.getProductVO()).getAgentBalance());
		}
		catch(CommandException e) {
			logger.error(genericExceptionMessage + e.getMessage()); 
			throw e;
		}
		catch(WorkFlowException we) {
			logger.error(genericExceptionMessage + we.getMessage()); 
			throw new CommandException(we.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,we);
		}
		catch (ClassCastException cce) {							
			logger.error(genericExceptionMessage + cce.getMessage()); 
			throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,cce);
		}
		catch(Exception ex) {
			logger.error(genericExceptionMessage + ex.getMessage()); 
			
			if(ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1) {
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			else{
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}	
	}

	@Override
	public String response() {
		List<LabelValueBean> lvbs = new ArrayList<LabelValueBean>();
		
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));
		
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode().toString()));
		
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic()));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CNAME, customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName()));
		
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, transactionModel.getCreatedOn().toString()));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(transactionModel.getCreatedOn())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount().toString())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAM, initialDeposit.getTransactionProcessingAmount().toString()));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(initialDeposit.getTransactionProcessingAmount())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMT, String.valueOf(transactionModel.getTotalAmount().doubleValue())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));

		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, Formatter.formatNumbers(initialDeposit.getCustomerBalance())));

		return MiniXMLUtil.createResponseXMLByParams(lvbs);
	}
	
	private void checkVelocityCondition(String userId) throws FrameworkCheckedException {
		BaseWrapper vWrapper = new BaseWrapperImpl();
		vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
		vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
		vWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
		vWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
		vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(txAmount));
		vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, customerModel.getSegmentId());
		vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
		vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//		vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
		getCommonCommandManager().checkVelocityCondition(vWrapper);
	}
	
	private SmartMoneyAccountModel loadSmartMoneyAccountModel(Long userType) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SmartMoneyAccountModel smaModel =  new SmartMoneyAccountModel();
		
		if(UserTypeConstantsInterface.RETAILER.longValue() == userType.longValue()) 
			smaModel.setRetailerContactId(appUserModel.getRetailerContactId());

		else if(UserTypeConstantsInterface.CUSTOMER.longValue() == userType.longValue()) 
			smaModel.setCustomerId(customerAppUserModel.getCustomerId());
		
		smaModel.setActive(Boolean.TRUE);
		searchBaseWrapper.setBasePersistableModel(smaModel);
		
		searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
		
		@SuppressWarnings("unchecked")
		CustomList<SmartMoneyAccountModel> smaList = searchBaseWrapper.getCustomList();
		if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
			return smaList.getResultsetList().get(0);
		}
		
		return null;
	}
	
	
}
