package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * @author Abu Turab Munir
 * @Dated  April 12, 2016
 * @Description: purpose of the document is to check whether customer's mobile number and CNIC already exists in MicroBank or not, 
 *               and fetch the details from NADRA regarding the customer's CNIC, Mobile and Thumb Impression
 * 
 */

public class CustomerNadraVerificationCommand extends BaseCommand {
	protected final Log logger = LogFactory
			.getLog(CustomerNadraVerificationCommand.class);
	protected AppUserModel appUserModel;
	protected BaseWrapper preparedBaseWrapper;
	
	private String cMsisdn, cNic, cFingerIndex;
	private String  customerRegistrationStateId;
	private Date cnicExpiry;
	private CommonCommandManager commonCommandManager;
	private MessageSource messageSource;
	private boolean flagMsisdn = false, flagCNic = false;
	private Long appUserId1, appUserType;
	NadraIntegrationVO iVo = new NadraIntegrationVO();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private File thumbImpressionFile;
	private String thumbImpressionPath;
	private String initialDepositAllowed;
	private String imageRetakeAllowed;
	private String isMobileUpadetAllowed;
	private String templateType;
	private String template;
	private String isUpgradeAccount = "";
	
	public void prepare(BaseWrapper baseWrapper) {
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of CustomerNadraVerificationCommand.prepare()");
		
		this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
		this.cNic = getCommandParameter(baseWrapper, "CNIC");
		this.cFingerIndex = getCommandParameter(baseWrapper, "FINGER_INDEX");
		thumbImpressionPath = MessageUtil.getMessage("uploadDirectory") + File.separator + "AGENTmate_" + cMsisdn + "_Impression_Photo.jpg";
		this.template = getCommandParameter(baseWrapper, "FINGER_TEMPLATE");
		this.templateType =getCommandParameter(baseWrapper, "TEMPLATE_TYPE");
		this.isUpgradeAccount = this.getCommandParameter(baseWrapper,"IS_UPGRADE");
		
		iVo.setFranchiseeID(MessageUtil.getMessage("NadraFranchiseeID"));
		iVo.setUserName(MessageUtil.getMessage("NadraUserName"));
		
		AppUserModel agentAppUserModel = ThreadLocalAppUser.getAppUserModel();
		String areaName = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_SENDER_CITY);

		if(agentAppUserModel.getRetailerContactId() != null) {
			this.commonCommandManager = getCommonCommandManager();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setPrimaryKey(agentAppUserModel.getRetailerContactId());
			BaseWrapper wrapper = new BaseWrapperImpl();
			wrapper.setBasePersistableModel(retailerContactModel);
			try {
				wrapper = commonCommandManager.loadRetailerContact(wrapper);
				retailerContactModel = (RetailerContactModel) wrapper.getBasePersistableModel();
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
			final TaxRegimeModel taxRegimeModel = commonCommandManager.getTaxRegimeDAO().findByPrimaryKey(retailerContactModel.getTaxRegimeId());

			areaName = taxRegimeModel.getName();
		}

		iVo.setPassword(MessageUtil.getMessage("NadraPassword"));
		iVo.setAreaName(areaName);
		iVo.setCitizenNumber(cNic);
		iVo.setContactNo(cMsisdn);
		iVo.setFingerIndex(cFingerIndex);
		iVo.setFingerTemplate(template);
		iVo.setTemplateType(templateType);

		this.preparedBaseWrapper = baseWrapper;
		this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
		if (this.logger.isDebugEnabled())
			this.logger.debug("End of CustomerNadraVerificationCommand.prepare()");
	}

	@Override
	public void doValidate() throws CommandException
	{
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of CustomerNadraVerificationCommand.validate()");
		ValidationErrors validationErrors = new ValidationErrors();
		
		/*if(this.thumbImpressionPath == null || !"".equals(this.thumbImpressionPath)){
			this.thumbImpressionFile = new File(thumbImpressionPath);
			if(!thumbImpressionFile.exists())
			{
				validationErrors.getStringBuilder().append("\n");
				validationErrors.getStringBuilder().append("Could not found thumb impression file on specified path");
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.FILE_UPLOAD_ERROR,ErrorLevel.HIGH,new Throwable());
			}
		}*/
		
		validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "MSISDN");
		validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
		validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(this.template, validationErrors, "Template");
		validationErrors = ValidatorWrapper.doRequired(this.templateType, validationErrors, "Template Type");
		
		validationErrors = ValidatorWrapper.doValidateCNIC(this.cNic, validationErrors, "CNIC");
		
		if(validationErrors.hasValidationErrors())
		{	
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
		
		if (this.logger.isDebugEnabled())
			this.logger.debug("End of CustomerNadraVerificationCommand.validate()");
	}


	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}
	
	
	public void execute() throws CommandException {
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of CustomerNadraVerificationCommand.execute()");
		
		String retVal = null;
		this.commonCommandManager = getCommonCommandManager();
		
		messageSource = getMessageSource();

		Long error_code = ErrorCodes.COMMAND_EXECUTION_ERROR;

		try {
			initialDepositAllowed = "1"; //this.segmentModel.getInitialDepositRequired() ? "1" : "0";
			imageRetakeAllowed = "1"; //this.segmentModel.getRetakeImages() ? "1" : "0";
			isMobileUpadetAllowed = "1"; //this.segmentModel.getMobileUpdateAllowed() ? "1" : "0";
			flagMsisdn = isMobileNumUnique(this.cMsisdn);
			if (!flagMsisdn && isUpgradeAccount.equals(""))
			{
				// MSISDN is not Unique.
				appUserId1 = appUserModel.getAppUserId();
				appUserType = appUserModel.getAppUserTypeId();
				
				if (appUserType == UserTypeConstantsInterface.RETAILER.longValue()) { // Throw exception if MSISDN is of Retailers/Agents
					throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.MsisdnNotUnique",null, null));
				}
				else if(appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE) || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)) //added new check for decline to allow again for account opening May 02, 2016 - resending to server
				{
					flagMsisdn = true; //allow in decline/rejected state	
				}
			} 
			else 
			{ // MSISDN is Unique. Going to Check CNIC and Registration State.
				flagCNic = isCNICUnique(this.cNic);
				if(!flagCNic && isUpgradeAccount.equals(""))
				{
					appUserId1 = appUserModel.getAppUserId();
					appUserType = appUserModel.getAppUserTypeId();
					
					if (appUserType == UserTypeConstantsInterface.RETAILER.longValue()) { // Throw exception if MSISDN is of Retailers/Agents
						throw new FrameworkCheckedException(messageSource.getMessage("checkCustomerExistanceCommand.CnicNotUnique",null, null));
					}
					else if(appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE) || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)) //added new check for decline to allow again for account opening May 02, 2016 - resending to server
					{
						flagCNic = true; //allow in decline/rejected state	
					}
				}
				
			}
			
			if((flagCNic && flagMsisdn) || isUpgradeAccount.equals("1"))
			{
				iVo = this.getNadraIntegrationController().fingerPrintVerification(iVo);
				iVo.setContactNo(cMsisdn);
				if(null == iVo.getResponseCode() || !iVo.getResponseCode().equals("100"))
				{
					throw new FrameworkCheckedException(messageSource
					.getMessage(
							"NADRA.noRecordFound",null, null)); 
				}
			}
			else
			{
				throw new FrameworkCheckedException(
						messageSource
								.getMessage(
										"checkCustomerExistanceCommand.AlreadyRegistered",null, null));
			}
			
		} catch (FrameworkCheckedException ex) {
			error_code = ErrorCodes.INVALID_USER;
			retVal = ex.getMessage();
		} catch (Exception e) {
			retVal = e.getMessage();
		}

		if (retVal != null)
			throw new CommandException(retVal, error_code.longValue(),
					ErrorLevel.MEDIUM, new Throwable());
		if (this.logger.isDebugEnabled())
			this.logger.debug("End of CustomerNadraVerificationCommand.execute()");
	}

	public String response() {
		if (this.logger.isDebugEnabled())
			this.logger
					.debug("Start of CustomerNadraVerificationCommand.response()");
		return toXML();
	}

	private String toXML() {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of CustomerNadraVerificationCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE);
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.deviceTypeId).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
			
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CREG_STATE_ID)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.customerRegistrationStateId)
					.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getContactNo()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CNIC)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getCitizenNumber()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_BIRTH_PLACE)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getBirthPlace()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_RESPONSE)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getResponseCode()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
//
//					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
//					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
//					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
//					.append(XMLConstants.ATTR_CNIC_EXPIRY)
//					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
//					.append( this.cnicExpiry == null ? "" : this.dateFormat.format(this.cnicExpiry))
//					.append(TAG_SYMBOL_OPEN)
//					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
//					.append(TAG_SYMBOL_CLOSE);
//
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CNAME)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getFullName()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_MOTHER_MAIDEN)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getMotherName()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CNIC_EXPIRY)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getCardExpire()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CDOB)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getDateOfBirth()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_CNIC_STATUS)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getCardExpire()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_PRESENT_ADDR)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getPresentAddress()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_PERMANENT_ADDR)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.iVo.getPresentAddress()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_RETAKE_IMAGES)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(imageRetakeAllowed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					
					strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_MOBILE_UPDATE_ALLOWED)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(isMobileUpadetAllowed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE)
					
					.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_INITIAL_DEPOSIT_REQUIRED)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(initialDepositAllowed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE)
		
							.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
							.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
							.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
							.append("GENDER")
							.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
							.append(iVo.getGender()).append(TAG_SYMBOL_OPEN)
							.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
							.append(TAG_SYMBOL_CLOSE);;

		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("End of CustomerNadraVerificationCommand.toXML()");
		}

		return strBuilder.toString();
	}
				


	private boolean isMobileNumUnique(String mobileNo) {
		appUserModel = new AppUserModel();
		try {
			appUserModel = this.commonCommandManager
					.loadAppUserByMobileAndType(mobileNo);
		} catch (Exception e) {
			this.logger.debug("CommonCommandManager Exception: "
					+ e.getMessage());
			e.printStackTrace();
		}
		return appUserModel == null;
	}

	private boolean isCNICUnique(String cnic) {
		appUserModel = new AppUserModel();
		try {
			appUserModel = this.commonCommandManager
					.loadAppUserByCnicAndType(cnic);
		} catch (Exception e) {
			this.logger.debug("CommonCommandManager Exception: "
					+ e.getMessage());
			e.printStackTrace();
		}
		return appUserModel == null;
	}

	private NadraIntegrationController getNadraIntegrationController() {
		return HttpInvokerUtil.getHttpInvokerFactoryBean(NadraIntegrationController.class,
				MessageUtil.getMessage("NadraIntegrationURL"));
	}
	
	private void populateNadraResponse(NadraIntegrationVO iVo){
		this.iVo.setFullName("dummy name");
		this.iVo.setBirthPlace("1");
		this.iVo.setCardExpire("Not Expired");
		this.iVo.setCitizenNumber("3320202236570");
		this.iVo.setDateOfBirth("02-02-1980");
		this.iVo.setContactNo("03216879870");
		this.iVo.setFingerIndex("1");
		this.iVo.setMotherName("Mama");
		this.iVo.setResponseCode("100");
	}

	
}