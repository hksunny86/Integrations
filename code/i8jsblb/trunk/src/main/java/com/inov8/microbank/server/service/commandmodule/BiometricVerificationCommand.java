package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BiometricVerificationCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(BiometricVerificationCommand.class);
	protected BaseWrapper preparedBaseWrapper;
	NadraIntegrationVO iVo;

	private String bvsMsdn, nonBVSMsdn, bvsCNIC, nonBvsCNIC, areaName, txAmt, remittanceType;
	private String templateType, template, cFingerIndex;

	public void prepare(BaseWrapper baseWrapper) {
		this.bvsMsdn = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BVS_MSISDN);
		this.bvsCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BVS_CNIC);
		this.nonBvsCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
		this.nonBVSMsdn = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		this.cFingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
		this.deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		this.template = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
		this.templateType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
		this.areaName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AREA_NAME);
		this.txAmt = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		this.remittanceType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_REMITTANCE_TYPE);

		this.preparedBaseWrapper = baseWrapper;
		if (this.logger.isDebugEnabled())
			this.logger.debug("End of BiometricVerificationCommand.prepare()");
	}

	@Override
	public void doValidate() throws CommandException
	{
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of BiometricVerificationCommand.validate()");
		ValidationErrors validationErrors = new ValidationErrors();

		validationErrors = ValidatorWrapper.doRequired(this.bvsCNIC, validationErrors, "CNIC to be Verify");
		validationErrors = ValidatorWrapper.doRequired(this.nonBvsCNIC, validationErrors, "CNIC");
		validationErrors = ValidatorWrapper.doRequired(this.bvsMsdn, validationErrors, "MSISDN");
		validationErrors = ValidatorWrapper.doRequired(this.cFingerIndex, validationErrors, "Finger Index");
		validationErrors = ValidatorWrapper.doRequired(this.template, validationErrors, "Template");
		validationErrors = ValidatorWrapper.doRequired(this.templateType, validationErrors, "Template");
		validationErrors = ValidatorWrapper.doRequired(this.areaName, validationErrors, "Area Name");

		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());
		}

		if (this.logger.isDebugEnabled())
			this.logger.debug("End of BiometricVerificationCommand.validate()");
	}


	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		return new ValidationErrors();
	}


	public void execute() throws CommandException {
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of BiometricVerificationCommand.execute()");

		iVo = new NadraIntegrationVO();
		iVo.setFranchiseeID(MessageUtil.getMessage("NadraFranchiseeID"));
		iVo.setUserName(MessageUtil.getMessage("NadraUserName"));
		iVo.setPassword(MessageUtil.getMessage("NadraPassword"));
		iVo.setCitizenNumber(bvsCNIC);
		iVo.setSecondaryCitizenNumber(nonBvsCNIC);
		iVo.setContactNo(bvsMsdn);
		iVo.setSecondaryContactNo(nonBVSMsdn);
		iVo.setTemplateType(templateType);
		iVo.setFingerTemplate(template);
		iVo.setAreaName(areaName);
		iVo.setFingerIndex(cFingerIndex);
		iVo.setRemittanceAmount(txAmt);
		iVo.setRemittanceType(remittanceType);
		//---------- to be decided
		iVo.setAccountNumber("423423");		//dummy, should be discussed

//		this.commonCommandManager = getCommonCommandManager();
		try {
			iVo = AllPayWebUtil.getNadraIntegrationController().otcFingerPrintVerification(iVo);
			if(!StringUtil.isNullOrEmpty(iVo.getResponseCode()) && !iVo.getResponseCode().equals("100")){
				throw new CommandException("Fingerprints does not exist in citizen database", ErrorCodes.INVALID_USER_ACCOUNT,
						ErrorLevel.MEDIUM, new Throwable());
			}
		}catch (Exception e) {
			if(e instanceof CommandException){
				throw (CommandException)e;
			}

			e.printStackTrace();
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR.longValue(),
					ErrorLevel.MEDIUM, new Throwable());
		}

		if (this.logger.isDebugEnabled())
			this.logger.debug("End of BiometricVerificationCommand.execute()");
	}

	public String response() {
		if (this.logger.isDebugEnabled())
			this.logger
					.debug("Start of BiometricVerificationCommand.response()");
		return null;
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