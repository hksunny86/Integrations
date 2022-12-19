package com.inov8.microbank.server.service.commandmodule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.integration.vo.OLAVO;

public class TitleFetchCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(TitleFetchCommand.class);
	private AppUserModel appUserModel = null;
	private String mobileNo;
	private String amount;
	private String deviceTypeId;
	private String response;
	private String stan;
	private String rrn;
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		logger.debug("Start of TitleFetchCommand.prepare()");
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		stan = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ST_TX_NO);
		rrn = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RRN);
		
		try {
			appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);
			if(appUserModel != null){
				logger.debug("[CreditAdviceCommand.execute] setting AppUserId:"+appUserModel.getAppUserId()+" in ThreadLocal");
				ThreadLocalAppUser.setAppUserModel(appUserModel);				
			}
		} catch (Exception e) {
			logger.error("[CreditAdviceCommand.execute] Unable to Load AppUserModel by mobile: " + mobileNo + "\nSetting Default appUserModel in ThreadLocal. appUserId:" + PortalConstants.SCHEDULER_APP_USER_ID+"\n",e);
		}
		
		logger.debug("End of TitleFetchCommand.prepare()");
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)throws CommandException {
		
		logger.debug("Start of TitleFetchCommand.validate()");
	
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
		validationErrors = ValidatorWrapper.doRequired(amount, validationErrors, "Amount");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(mobileNo,validationErrors,"Mobile No");
			validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Amount");
		}
		
		logger.debug("End of TitleFetchCommand.validate()");
		
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		logger.debug("Start of TitleFetchCommand.execute()");
		try {
			validateAppUserType(appUserModel);
			
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()){
                commonCommandManager.validateHRA(mobileNo);
            }

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()){				
				smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
			}else{
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			}
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		
			ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
			
			if(validationErrors.hasValidationErrors()){
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);	
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.IBFT.longValue());
			baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
			baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, amount);
			ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
			if(productVo == null){
				logger.error("[TitleFetchCommand.execute()] Unable to load productVo. mobileNo:" + mobileNo);
				throw new CommandException("Unable to load productVo", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
			}
			
			BaseWrapper wrapper = commonCommandManager.loadProduct(baseWrapper);
			ProductModel productModel = (ProductModel)wrapper.getBasePersistableModel();
			if(productModel == null){
				logger.error("[TitleFetchCommand.execute()] Unable to load productModel. mobileNo:" + mobileNo);
				throw new CommandException("Unable to load productModel", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
			}

			Long segmentId = null;
			if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()){	
				segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(mobileNo);				
			}else{
				segmentId = null;
			}
			commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), mobileNo, Long.valueOf(deviceTypeId), Double.valueOf(amount), productModel, null, null);
			
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);					
			AbstractFinancialInstitution olaFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setTransactionAmount(Double.valueOf(amount));
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
			switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			
			switchWrapper.putObject(CommandFieldConstants.KEY_RRN, rrn);
			switchWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
			switchWrapper.putObject(CommandFieldConstants.KEY_PRODUCT_ID, ProductConstantsInterface.IBFT);

			switchWrapper = (SwitchWrapper)olaFinancialInstitution.getAccountTitle(switchWrapper);
			
			OLAVO olavo = switchWrapper.getOlavo();
			StringBuilder responseBuilder = new StringBuilder();
			if(olavo != null){
				responseBuilder.append(olavo.getResponseCode());
				responseBuilder.append(",");
				responseBuilder.append(Formatter.formatDouble(olavo.getBalance()));
				responseBuilder.append(",");
				responseBuilder.append(olavo.getFirstName());
				responseBuilder.append(",");
				responseBuilder.append(olavo.getLastName());
				response = responseBuilder.toString();
			}else{
				logger.error("[TitleFetchCommand.execute()] olavo is null from olaFinancialInstitution.getAccountTitle() for mobileNo:" + mobileNo);
				throw new CommandException("olavo is null from olaFinancialInstitution.getAccountTitle()", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
			}
			
		}catch (Exception e){
			logger.error("[TitleFetchCommand.execute()] Exception occured for mobileNo:" + mobileNo + "\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
	}

	private void validateAppUserType(AppUserModel appUserModel) throws CommandException{
		if(appUserModel == null  || appUserModel.getAppUserTypeId() == null 
				|| (appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER
						&& appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.RETAILER)){
			
			logger.error("[TitleFetchCommand.validateAppUserType] Invalid User... mobileNo:" + mobileNo);
			throw new CommandException("Invalid User", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, null);
			
		}
	}
	
	@Override
	public String response() {
		return response;
	}
	
}
