package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.ApiCityModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.integration.dispenser.CashToCashDispenser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CNICToCNICInfoCommand extends FundTransferInfoCommand {

	private List <ApiCityModel> apiCityModelList=new ArrayList<>();
	private boolean isValidSenderNIC = true;
	private boolean isValidSenderMobile = true;
	private boolean isValidReceiverNIC = true;
	private boolean isValidReceiverMobile = true;

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		super.prepare(baseWrapper);

		Long[] appUserTypes = {UserTypeConstantsInterface.CUSTOMER,UserTypeConstantsInterface.HANDLER,UserTypeConstantsInterface.RETAILER};
		senderMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		receiverMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE);
		senderCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		receiverCNIC = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);

		try {
			receiverAppUserModel = getCommonCommandManager().loadAppUserByCnicAndType(receiverCNIC);
			if(receiverAppUserModel != null)
				isValidReceiverNIC = false;
			receiverAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(receiverMobileNo,appUserTypes);
			if(receiverAppUserModel != null)
				isValidReceiverMobile = false;

			senderAppUserModel = getCommonCommandManager().loadAppUserByCnicAndType(senderCNIC);
			if(senderAppUserModel != null)
				isValidSenderNIC = false;
			senderAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(senderMobileNo,appUserTypes);
			if(senderAppUserModel != null)
				isValidSenderMobile = false;
			/*senderAppUserModelList = ccManager.getAppUserManager().findAppUserByCnicAndMobile(senderMobileNo , senderCNIC);
			receiverAppUserModelList = ccManager.getAppUserManager().findAppUserByCnicAndMobile(receiverMobileNo , receiverCNIC);*/
			//no registered user should exists with sender mobile no
			//senderAppUserModel = ccManager.checkAppUserTypeAsWalkinCustoemr(senderMobileNo);

			//no registered user should exists with receiver mobile no
			//receiverAppUserModel = ccManager.checkAppUserTypeAsWalkinCustoemr(receiverMobileNo);

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

		String classNMethodName = getClass().getSimpleName()+ ".validate(): ";
		ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		ValidatorWrapper.doRequired(receiverMobileNo,validationErrors,"Receiver Mobile No");
		ValidatorWrapper.doRequired(senderCNIC,validationErrors,"Sender CNIC");
		ValidatorWrapper.doRequired(receiverCNIC,validationErrors,"Receiver CNIC");
		boolean isSenderReceiverSame = false;
		String error = "";
		if(senderMobileNo.equals(receiverMobileNo )) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_REC_SAME_MOB", null, null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}

		if(senderCNIC.equals(receiverCNIC )) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_REC_SAME_NIC", null, null);
			isSenderReceiverSame = true;
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

		if(!isValidSenderNIC)
		{
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_NIC_ALREADY_REG", null,null);
			ValidatorWrapper.addError(validationErrors, error);
		}
		if(!isValidSenderMobile)
		{
			error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_ALREADY_REG", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		if(!isValidReceiverNIC)
		{
			error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_NIC_ALREADY_REG", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		if(!isValidReceiverMobile)
		{
			error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_ALREAY_REG", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		/*if(null != senderAppUserModel || null != receiverAppUserModel){
			if(null != senderAppUserModel){
				error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_ALREADY_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			if(null != receiverAppUserModel){
				error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_ALREAY_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			//error = "Sender/Receiver is already registered as BB user.";

		}*/


		try{
			/*if(ccManager.loadAppUserByCnicAndType(senderCNIC) != null && !isSenderReceiverSame){
				error=this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_NIC_ALREADY_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			if(ccManager.loadAppUserByCnicAndType(receiverCNIC) != null && !isSenderReceiverSame){
				error=this.getMessageSource().getMessage("MONEYTRANSFER.REC_NIC_ALREADY_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}*/
			/**
			 * [JSBLMFS-177] Restrict C2C transaction at leg1 if mobile number of recipient is different but CNIC is same.
			 */
			if(!validationErrors.hasValidationErrors() && ccManager.checkP2PTransactionsOnCNIC(receiverCNIC,receiverMobileNo)){
				error=this.getMessageSource().getMessage("MONEYTRANSFER.EXISTING_P2P_WITH_DIFF_MOB", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
		}catch(FrameworkCheckedException fe){
			fe.printStackTrace();
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		try{
			super.execute();

			workflowWrapper.setDeviceTypeModel(deviceTypeModel);
			workflowWrapper.setSmartMoneyAccountModel(agentSMAModel);
			workflowWrapper.setRecipientWalkinCustomerModel(getWalkinCustomerModel(new WalkinCustomerModel(receiverCNIC, receiverMobileNo)));
			workflowWrapper.setSenderWalkinCustomerModel(getWalkinCustomerModel(new WalkinCustomerModel(senderCNIC, senderMobileNo)));

			//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
			//getCommonCommandManager().createOrUpdateWalkinCustomer(senderCNIC, senderMobileNo, null);
			//getCommonCommandManager().createOrUpdateWalkinCustomer(receiverCNIC, receiverMobileNo, null);


/*			if(workflowWrapper.getSenderAppUserModel()!=null) {
				getCommonCommandManager().createNewWalkinCustomer(senderCNIC, senderMobileNo, null);
			}
			if(workflowWrapper.getRecipientCustomerModel()!=null) {
				getCommonCommandManager().createNewWalkinCustomer(receiverCNIC, receiverMobileNo, null);
			}*/
			CashToCashDispenser dispenser = loadCashToCashDispenser(workflowWrapper);

			dispenser.getBillInfo(workflowWrapper);
			LinkedHashMap<String, SortingOrder> map=new LinkedHashMap<>();
			map.put("name",SortingOrder.ASC);
			apiCityModelList=getCommonCommandManager().getApiCityDAO().findAll(map).getResultsetList();

		}catch(Exception ex){
			logger.error("Exception @ CNICToCNICInfoCommand.execute...", ex);
			handleException(ex);
		}
	}

	@Override
	public String response() {

		List<LabelValueBean> params = super.getResponse();
		String response="";
		StringBuilder strBuilder = new StringBuilder();
		params.add(new LabelValueBean(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_S_W_CNIC, replaceNullWithEmpty(senderCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(receiverCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_IS_BVS_REQUIRED, "1"/*(String)workflowWrapper.getObject(CommandFieldConstants.IS_SENDER_BVS_REQUIRED)*/));
		//params.add(new LabelValueBean("Cities", "Lahore"));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_SENDER_CITY, replaceNullWithEmpty(retailerContactModel.getAreaIdAreaModel().getName())));


		response = MiniXMLUtil.createInfoResponseXMLByParams(params);

		if(apiCityModelList!=null && apiCityModelList.size()>0) {
			strBuilder.append(TAG_SYMBOL_OPEN).append("cities").append(TAG_SYMBOL_CLOSE);
			for (ApiCityModel apiModel:apiCityModelList
				 ) {
				strBuilder.append(TAG_SYMBOL_OPEN).append("city").append(TAG_SYMBOL_SPACE);
				strBuilder.append(ATTR_PARAM_NAME).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append(apiModel.getName()).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
				strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append("city").append(TAG_SYMBOL_CLOSE);
			}

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append("cities").append(TAG_SYMBOL_CLOSE);
			response = response + strBuilder.toString();
		}
			return response;
	}

	private CashToCashDispenser loadCashToCashDispenser(WorkFlowWrapper workFlowWrapper) throws CommandException {
		CashToCashDispenser cashToCashDispenser = null;

		try {
			cashToCashDispenser = (CashToCashDispenser) getCommonCommandManager().loadProductDispense(workFlowWrapper);
		} catch (FrameworkCheckedException e) {
			logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return cashToCashDispenser;
	}

	private WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel walkinCustomerModel) throws CommandException {

		try {
			getCommonCommandManager().createUpdateWalkinCustomerIfExists(walkinCustomerModel.getCnic(), walkinCustomerModel.getMobileNumber(), null);
		} catch (FrameworkCheckedException e) {
			logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		return walkinCustomerModel;
	}

}
