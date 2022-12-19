package com.inov8.microbank.server.service.commandmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;

public class BLBToBLBInfoCommand extends FundTransferInfoCommand {

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		super.prepare(baseWrapper);
		
		senderMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		receiverMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		
		try {
			senderAppUserModel = ccManager.loadAppUserByQuery(senderMobileNo, UserTypeConstantsInterface.CUSTOMER);
			receiverAppUserModel = ccManager.loadAppUserByQuery(receiverMobileNo, UserTypeConstantsInterface.CUSTOMER);
			//productVO = ccManager.loadProductVO(baseWrapper);
			senderSegmentId = ccManager.getCustomerSegmentIdByMobileNo(senderMobileNo);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		super.validate(validationErrors);
		
		String classNMethodName = getClass().getSimpleName() + ".validate(): ";
		ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		ValidatorWrapper.doRequired(receiverMobileNo,validationErrors,"Receiver Mobile No");
		
		String error = "";
		if(senderMobileNo.equals(receiverMobileNo )) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_REC_SAME_MOB", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		if(agentMobileNo.equals(senderMobileNo)) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_SENDER", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		if(agentMobileNo.equals(receiverMobileNo)) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_RECEIVER", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}

		if(null == senderAppUserModel || null == receiverAppUserModel){
			if(null == senderAppUserModel){
				error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_NOT_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			if(null == receiverAppUserModel){
				error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_NOT_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			
		}
		
		else {
			try {
				validateBBCustomer(senderAppUserModel, "Sender - ");
				validsateSmartMoneyAccount(senderAppUserModel, "Sender - ");

//				validateBBCustomer(receiverAppUserModel, "Receiver - ");
			} 
			catch (FrameworkCheckedException e) {
				logger.error(classNMethodName + e.getMessage()); 
				ValidatorWrapper.addError(validationErrors, e.getMessage());
			}
		}

        //HRA validation check
        if(!validationErrors.hasValidationErrors()) {
            commonCommandManager.validateHRA(receiverMobileNo);
        }

        return validationErrors;
	}
	
	@Override
	public void execute() throws CommandException {
		try {
			ccManager.checkCustomerBalance(senderMobileNo, Double.valueOf(txAmount));
		} catch (WorkFlowException wex) {
			wex.printStackTrace();
			throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
		super.execute();
	}
	
	@Override
	public String response() {
		
		List<LabelValueBean> params = super.getResponse(); 
		
		params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
		
		return MiniXMLUtil.createInfoResponseXMLByParams(params);
	}
}
