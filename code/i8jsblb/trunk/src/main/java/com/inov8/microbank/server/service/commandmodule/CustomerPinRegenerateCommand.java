package com.inov8.microbank.server.service.commandmodule;
/***********************************************************/
/************* Author: Abu Turab ***************************/
/************* Dated : March 19, 2018 **********************/
/***********************************************************/

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

public class CustomerPinRegenerateCommand extends BaseCommand 
{
	protected String deviceTypeId;
	protected String customerMobileNumber;
	String veriflyErrorMessage;
	boolean errorMessagesFlag;

	protected final Log logger = LogFactory.getLog(CustomerPinRegenerateCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerPinRegenerateCommand.execute()");
		}

		try {
			IvrRequestDTO ivrRequestDTO = new IvrRequestDTO();
			ivrRequestDTO.setCustomerMobileNo(customerMobileNumber);
			ivrRequestDTO.setProductId(Long.parseLong(CommandFieldConstants.REGENERATE_PIN_IVR));
			//this.getCommonCommandManager().makeIVRCallForPinRegenerate(ivrRequestDTO);
			this.getCommonCommandManager().initiateUserGeneratedPinIvrCall(ivrRequestDTO);
		} catch (Exception e) {
			logger.error(e);
			errorMessagesFlag = true;
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerPinRegenerateCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerPinRegenerateCommand.prepare()");
		}
		customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerPinRegenerateCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerPinRegenerateCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(customerMobileNumber, validationErrors, "Mobile Number");

		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerPinRegenerateCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerPinRegenerateCommand.toXML()");
		}
		String message="An IVR call has been routed to your number to set the MPIN";
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();

		if(errorMessagesFlag){
			message = "There were some error in routing the IVR call\n please retry later";
		}

		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerPinRegenerateCommand.toXML()");
		}
		return MiniXMLUtil.createMessageXML(message);
	}
}
