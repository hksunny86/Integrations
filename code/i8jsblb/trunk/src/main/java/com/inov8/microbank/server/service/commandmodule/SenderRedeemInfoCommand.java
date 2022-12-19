package com.inov8.microbank.server.service.commandmodule;

import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.TransactionReversalConstants;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

/**
 * @author Abu Turab
 * @Description this command is info command for sender redeem request, Cash2Cash only
 * @date		27-04-2015
 *
 */
public class SenderRedeemInfoCommand extends FundTransferInfoCommand {

	protected String	trxId;
	BaseWrapper baseWrapper;
	protected String trxDate, trxTime, msisdn, full_name, MOBN, CMOBN;
	protected Double txAmount;
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		agentMobileNo 	= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
		trxId			= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		senderMobileNo	= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		senderCNIC		= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		deviceTypeId 	= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId 		= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		
	}
	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		String classNMethodName = getClass().getSimpleName() + ".validate(): ";
		ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		ValidatorWrapper.doRequired(senderCNIC,validationErrors,"Sender CNIC");
		ValidatorWrapper.doRequired(trxId,validationErrors,"Transaction ID");
		ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		//***************************************************************************************************************************
		//									Check if sender or receiver cnic is blacklisted
		//***************************************************************************************************************************		
		if(this.getCommonCommandManager().isCnicBlacklisted(senderCNIC)) {
            validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
            throw new CommandException(validationErrors.getErrors(),ErrorCodes.TERMINATE_EXECUTION_FLOW,ErrorLevel.MEDIUM,new Throwable());		                
		}
		//***************************************************************************************************************************

		String error = "";
		if(agentMobileNo.equals(senderMobileNo)) {
			error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_CANT_BE_SENDER", null,null);
			logger.error(classNMethodName + error);
			ValidatorWrapper.addError(validationErrors, error);
		}
		
		return validationErrors;
	}
	
	@Override
	public void execute() throws CommandException {
		String retVal = null;
		DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		baseWrapper = new BaseWrapperImpl();
		
		try
		{
			SearchBaseWrapper	sBaesWrapper = new SearchBaseWrapperImpl();
			TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel(false);
			txDetailMasterModel.setTransactionCode(trxId);			//trxId transaction code
			txDetailMasterModel.setSenderCnic(senderCNIC);			//sender walkin cnic
			txDetailMasterModel.setSaleMobileNo(senderMobileNo); 	//sender walkin mobile number
			txDetailMasterModel.setProductId(ProductConstantsInterface.CASH_TRANSFER); 					//Cash2Cash
			txDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSED); 	//status must be reversed
			sBaesWrapper.setBasePersistableModel(txDetailMasterModel);
			sBaesWrapper = commonCommandManager.loadTransactionDetailMaster(sBaesWrapper);
			txDetailMasterModel = (TransactionDetailMasterModel) sBaesWrapper.getBasePersistableModel();
			//check if this is cash to cash transaction and status is REVERSED 
			 if(txDetailMasterModel != null ) {	
					if(txDetailMasterModel.getUpdateP2PFlag() != null && txDetailMasterModel.getUpdateP2PFlag()){
						retVal = MessageUtil.getMessage("UPDATE_P2P_PENDING_AUTH_ERROR");
					}
					
					Integer redemptionType = txDetailMasterModel.getRedemptionType();
					if(redemptionType == null){
						logger.error("Redemption Type missing in TransactionDetailMaster for Transaction ID:"+txDetailMasterModel.getTransactionCode());
						throw new CommandException(this.getMessageSource().getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR, null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					if(redemptionType.intValue() == TransactionReversalConstants.REDEMPTION_TYPE_PARTIAL){
						//Partial Redemption
						double inclusiveChargesApplied = txDetailMasterModel.getInclusiveCharges();
						if(txDetailMasterModel.getThirdPartyCheck() != null && txDetailMasterModel.getThirdPartyCheck()){
							inclusiveChargesApplied = 0D;
						}
						txAmount = txDetailMasterModel.getTransactionAmount() - inclusiveChargesApplied;
					}else{
						//Full Redemption
						txAmount = txDetailMasterModel.getTotalAmount();			
					}

					
					trxDate = dtf.print(txDetailMasterModel.getCreatedOn().getTime());
					trxTime = tf.print(txDetailMasterModel.getCreatedOn().getTime());
					receiverMobileNo = txDetailMasterModel.getRecipientMobileNo();
					receiverCNIC	 = txDetailMasterModel.getRecipientCnic();
			}
			 else{//this is not a valid Cash to Cash transaction for reversal redeem
					 retVal = "Please provide valid data to proceed with Sender Redeem transaction";

			 }
		 	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = "The transaction could not be fetched\n";
		}

		if( retVal != null )
		 {
			 throw new CommandException(retVal,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		 }
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of PayCashInfoCommand.execute()");
		}
	}
	
	@Override
	public String response() {
		List<LabelValueBean> params = super.getResponse(); 
		params.add(new LabelValueBean(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_S_W_CNIC, replaceNullWithEmpty(senderCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(receiverCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, replaceNullWithEmpty(trxId)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, replaceNullWithEmpty(trxDate)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, replaceNullWithEmpty(trxTime)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(txAmount)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TAMT, txAmount.toString()));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_IS_BVS_REQUIRED, "1"));
		
	return MiniXMLUtil.createInfoResponseXMLByParams(params);
	}
}