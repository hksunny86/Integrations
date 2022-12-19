package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.util.*;

import java.util.List;

public class CNICToBLBInfoCommand extends FundTransferInfoCommand {

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		super.prepare(baseWrapper);
		
		senderMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		senderCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		receiverMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		
		try {
			//no registered user should exists with sender mobile no
			senderAppUserModel = ccManager.checkAppUserTypeAsWalkinCustoemr(senderMobileNo);
			//senderAppUserModelList = ccManager.getAppUserManager().findAppUserByCnicAndMobile(senderMobileNo , senderCNIC);
			receiverAppUserModel = ccManager.loadAppUserByQuery(receiverMobileNo, UserTypeConstantsInterface.CUSTOMER);
			receiverCNIC = (receiverAppUserModel != null) ? receiverAppUserModel.getNic() : "";
			senderSegmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
//			productVO = ccManager.loadProductVO(baseWrapper);
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
		ValidatorWrapper.doRequired(senderCNIC,validationErrors,"Sender CNIC");
		ValidatorWrapper.doValidateCNIC(senderCNIC,validationErrors,"Sender CNIC");
		ValidatorWrapper.doRequired(receiverMobileNo,validationErrors,"Receiver Mobile No");
		
		String error = "";
		try{
			if(ccManager.loadAppUserByCnicAndType(senderCNIC) != null){
				error=this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_NIC_ALREADY_REG", null, null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			
		}catch(FrameworkCheckedException fe){
				fe.printStackTrace();
		}
		if(senderMobileNo.equals(receiverMobileNo )) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_REC_SAME_MOB", null, null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		if(agentMobileNo.equals(senderMobileNo)) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_SENDER", null, null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		if(agentMobileNo.equals(receiverMobileNo)) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_RECEIVER", null, null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}

		if(null != senderAppUserModel) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_ALREADY_REG", null, null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		if(null == receiverAppUserModel){
			error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_NOT_REG", null, null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		else {
			/*if(senderAppUserModelList.size() >=1) {
				if (senderAppUserModelList.size() > 1 || !senderMobileNo.equals(senderAppUserModelList.get(0).getMobileNo()) || !senderCNIC.equals(senderAppUserModelList.get(0).getNic())) {
					//For specific error message on pairing by atiq butt
					boolean cn=false,mb=false;
					for(int i=0;i<senderAppUserModelList.size();i++){
						if(!senderMobileNo.equals(senderAppUserModelList.get(i).getMobileNo()) && senderCNIC.equals(senderAppUserModelList.get(i).getNic())){
							cn=true;
						}
						if(senderMobileNo.equals(senderAppUserModelList.get(i).getMobileNo()) && !senderCNIC.equals(senderAppUserModelList.get(i).getNic())){
							mb=true;
						}
					}
					if(cn && mb){
						error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_CNIC_ALREADY_PAIR", null, null);
					}
					else if(cn){
						error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_CNIC_ALREADY_PAIR", null, null);
					}
					else{
						error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_ALREADY_PAIR", null, null);
					}

					logger.error(classNMethodName + error);
					ValidatorWrapper.addError(validationErrors, error);
				}
			}*/
			try {
				validateBBCustomer(receiverAppUserModel, "Receiver - ");
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
		super.execute();
		workflowWrapper.setSmartMoneyAccountModel(agentSMAModel);
		try {
			//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
			getCommonCommandManager().createOrUpdateWalkinCustomer(senderCNIC, senderMobileNo, null);

			workflowWrapper.setTransactionAmount(Double.valueOf(txAmount));
			getCommonCommandManager().verifyWalkinCustomerThroughputLimits(workflowWrapper, TransactionTypeConstantsInterface.OLA_DEBIT, senderCNIC);
			WalkinCustomerModel senderWalkinModel = new WalkinCustomerModel();
			senderWalkinModel.setCnic(senderCNIC);
			senderWalkinModel.setMobileNumber(senderMobileNo);
			workflowWrapper.setSenderWalkinCustomerModel(senderWalkinModel);
		} catch(FrameworkCheckedException e) {
			e.printStackTrace();
			handleException(e);
		}catch(WorkFlowException wex){
			wex.printStackTrace();
			if(StringUtil.isFailureReasonId(wex.getMessage())){
            	throw new CommandException(MessageUtil.getMessage(wex.getMessage()),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
            }else{
            	throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
            }
		}catch (Exception ex){
			ex.printStackTrace();
			handleException(ex);
		}
	}
	
	@Override
	public String response() {
		
		List<LabelValueBean> params = super.getResponse();
		
		params.add(new LabelValueBean(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_S_W_CNIC, replaceNullWithEmpty(senderCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(receiverCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_IS_BVS_REQUIRED, (String) workflowWrapper.getObject(CommandFieldConstants.IS_SENDER_BVS_REQUIRED)));

		return MiniXMLUtil.createInfoResponseXMLByParams(params);
	}
}
