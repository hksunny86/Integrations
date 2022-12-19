/**
 * 
 */
package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ValidationErrors;

/**
 * @author rizwanur.rehman
 *
 */
public class GenerateEncryptedPinCommand extends BaseCommand
{
	protected String pin;
	protected String deviceTypeId;	

	
	@Override
	public void execute() throws CommandException
	{
		pin = this.encryptPin(pin);
	}

	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
	}


	@Override
	public String response()
	{
		return toXML();
	}


	@Override
	public void doValidate() throws CommandException
	{
		
	}

	private String toXML()
	{
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_ENCRYPTED_PIN)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				.append(pin)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE);
		
		return strBuilder.toString();
	}


	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
