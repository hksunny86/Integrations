package com.inov8.microbank.server.service.consumercommandmodule;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MSG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.ResponseCodeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;



public class VerifyCustomerSelfRegistetrationCommand extends BaseCommand{
	
	private final Log logger = LogFactory.getLog(VerifyCustomerSelfRegistetrationCommand.class);

	private String mobileNO;
	private String cnic;
	private String isUpgrade;
	private String responseMessage;
	private WebServiceVO webServiceVO;
	private Long errorCode;
	private String registrationState;

	private AppUserModel appUserModel;
	private CustomerModel customerModel;
	
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {

		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VerifyCustomerSelfRegistetrationCommand.prepare()");
		}

		isUpgrade = getCommandParameter(baseWrapper, "IS_UPGRADE");
		mobileNO = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        cnic = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        
        if(logger.isDebugEnabled())
		{
			logger.debug("End of VerifyCustomerSelfRegistetrationCommand.prepare()");
		}
        
	}

	@Override
	public void doValidate() throws CommandException{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VerifyCustomerSelfRegistetrationCommand.doValidate()");
		}
		
		ValidationErrors validationErrors = new ValidationErrors();

		if(!isUpgrade.equals("1"))
			ValidatorWrapper.doRequired(mobileNO, validationErrors, "Mobile No");
        ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
        
        boolean isValid = CommonUtils.isValidCnic(cnic);
        if(!validationErrors.hasValidationErrors()){
        	if(isValid == false){
        		 validationErrors.getStringBuilder().append("Invalid CNIC");
                 throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
        	}
		}
		
		if(validationErrors.hasValidationErrors()){
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());
		}
		
		   if(logger.isDebugEnabled())
			{
				logger.debug("End of VerifyCustomerSelfRegistetrationCommand.doValidate()");
			}
	}
	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		
		return new ValidationErrors();
	}

	@Override
	public void execute() throws CommandException {

		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VerifyCustomerSelfRegistetrationCommand.execute()");
		}


		webServiceVO = new WebServiceVO();
		if(!isUpgrade.equals("1"))
		{
			webServiceVO.setMobileNo(mobileNO);
			webServiceVO.setCnicNo(cnic);
		}

		int pendingTransactionCount=0;
		if(!isUpgrade.equals("1"))
		{
			try {
				pendingTransactionCount = this.commonCommandManager.countCustomerPendingTrx(cnic);
			} catch (FrameworkCheckedException e) {
				throw new CommandException(e.getLocalizedMessage() ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, null );
			}
			if(pendingTransactionCount > 0){
				webServiceVO.setResponseCode(FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
			}else{
				try {
					pendingTransactionCount	=	this.commonCommandManager.countCustomerPendingTrxByMobile(mobileNO);
				} catch (FrameworkCheckedException e) {
					throw new CommandException(e.getLocalizedMessage() ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, null );
				}
				if(pendingTransactionCount > 0){
					webServiceVO.setResponseCode(FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
				}else{
					webServiceVO = this.commonCommandManager.getFonePayManager().verifyNewCustomer(webServiceVO);
				}
			}

			if(webServiceVO.getResponseCode() != "00" && webServiceVO.getCustomerStatus()==null){
				errorCode =Long.valueOf(webServiceVO.getResponseCode());
				String msg = MessageUtil.getMessage("fonepay.error."+webServiceVO.getResponseCode());
				throw new CommandException(msg,errorCode, ErrorLevel.MEDIUM, new Throwable());
			}
		}
		else
		{
			errorCode = ResponseCodeConstants.ACCOUNT_NOT_EXIST;
			AppUserModel appModel = ThreadLocalAppUser.getAppUserModel();
			this.mobileNO = appModel.getMobileNo();
			if(!appModel.getNic().equals(this.cnic))
				throw new CommandException("You can update your own Account only",errorCode, ErrorLevel.MEDIUM, new Throwable());
			try {
				Long[] appUserTypes = {AppConstants.CONSUMER_APP};
				appUserModel = this.commonCommandManager.loadAppUserByCNICAndAccountType(this.cnic,appUserTypes);
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
				if(appUserModel != null && appUserModel.getCustomerId() != null )
				{
					this.mobileNO = appUserModel.getMobileNo();
					customerModel = this.commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
				}
				else
				{
					//responseMessage = "No Customer exists against this CNIC.";
					throw new CommandException("No Customer exists against this CNIC.",errorCode, ErrorLevel.MEDIUM, new Throwable());
				}

				if(appUserModel == null || customerModel == null)
				{
					throw new CommandException("No Customer exists against this CNIC.",errorCode, ErrorLevel.MEDIUM, new Throwable());
				}

				if(!customerModel .getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0))
				{
					throw new CommandException("Customer L1 account is already exists in the system .",errorCode, ErrorLevel.MEDIUM, new Throwable());
				}

				if(!appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED))
				{
					throw new CommandException("Customer Registration State is not valid.",errorCode,ErrorLevel.MEDIUM, new Throwable());
				}

				if(appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED))
					registrationState = "Approved";
				responseMessage = "verified";
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VerifyCustomerSelfRegistetrationCommand.execute()");
		}
		
	}

	@Override
	public String response() {
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VerifyCustomerSelfRegistetrationCommand.response()");
		}
		
		StringBuilder response = new StringBuilder();
		
		if(webServiceVO.getCustomerStatus() != null){
			response.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE).
			append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_NAME,webServiceVO.getFirstName()+" "+webServiceVO.getLastName()));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CDOB,webServiceVO.getDateOfBirth()));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC_EXPIRY,webServiceVO.getCnicExpiry()));
			
			if(webServiceVO.getCustomerStatus().equals("4")){
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_PHOTO,"1"));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO,"1"));
				
			}else if(webServiceVO.getCustomerStatus().equals("3")){
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_PHOTO,"0"));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO,"1"));
				
			}else{
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_PHOTO,"1"));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO,"0"));
				
			}
		}
		else if(isUpgrade.equals("1"))
		{
			response.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
					.append(TAG_SYMBOL_CLOSE);
			if(responseMessage != null )
			{

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_CNIC)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(this.cnic).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(XMLConstants.ATTR_CNIC_EXPIRY)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(appUserModel.getNicExpiryDate()).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(appUserModel.getMobileNo()).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_CREG_STATE)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(registrationState)
						.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_CREG_STATE_ID)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(appUserModel.getRegistrationStateId())
						.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_CNAME)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(customerModel.getName()).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);
			}
			/*else
			{
				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(this.deviceTypeId).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);

				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_CNIC)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(this.cnic).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);
				response.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append("IS_UPGRADE")
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
						.append(this.isUpgrade).append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE);


			}*/
		}
		else{
			 response.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE).
			 append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_DEVICE_TYPE_ID,this.deviceTypeId));
			 
		}
		
		response.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VerifyCustomerSelfRegistetrationCommand.response()");
		}
		
		return response.toString();
		
	}
	
	

}
