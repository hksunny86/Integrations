package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * 
 * @author AtifHu
 *
 */
public class ReceiveCashInfoCommand extends BaseCommand
{
	protected final Log logger = LogFactory.getLog(ReceiveCashInfoCommand.class);
	protected String productId, recvMobileNumber, recvCnic, transactionId, agentMobileNumber, isRegistered, senderMobileNumber;
	protected AppUserModel appUserModel;
	protected BaseWrapper preparedBaseWrapper;
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	protected TransactionDetailMasterModel txDetailMasterModel;
	protected ProductModel productModel;
	protected BaseWrapper baseWrapper;
	protected String successMessage;
	protected String txProcessingAmount;
	private String walkInCustomerCNIC;
	
	private String recBvsFlag = "";

	@Override
	public void execute() throws CommandException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Start of ReceiveCashInfoCommand.execute()");
		}

		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		baseWrapper = new BaseWrapperImpl();

		try
		{
			ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);

			if (!validationErrors.hasValidationErrors())
			{
				TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
				transactionCodeModel.setCode(transactionId);
				workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
				txDetailMasterModel = new TransactionDetailMasterModel();
				txDetailMasterModel.setTransactionCode(transactionId);
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(txDetailMasterModel);
				searchBaseWrapper = getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
				txDetailMasterModel = (TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
				senderMobileNumber	=	txDetailMasterModel.getSaleMobileNo();
				//txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
                
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
                workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                workFlowWrapper.setTransactionAmount(txDetailMasterModel.getTransactionAmount());
                workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(Long.parseLong(deviceTypeId)));

			} 
			else
			{
				throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			}
			
			
            // this method calculate limits including all in process transactions as well,
            // in case of leg 2 this shouldn't include in process transactions, so this flag is used to by pass in process transaction
            workFlowWrapper.setProductModel(productModel);
			//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
            //commonCommandManager.createOrUpdateWalkinCustomer(recvCnic, recvMobileNumber, null);
			//commonCommandManager.createNewWalkinCustomer(recvCnic, recvMobileNumber, null);
            workFlowWrapper.putObject("EXCLUDE_INPROCESS_TX", "1");
            
            Boolean limitApplicableForBulkdisbursement=true;
            if(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == productModel.getServiceId().longValue()) {
            	BulkDisbursementsModel bulkDisbursementsModel = new BulkDisbursementsModel();
                bulkDisbursementsModel.setTransactionCode(txDetailMasterModel.getTransactionCode());
                List<BasePersistableModel> list = commonCommandManager.findBasePersistableModel(bulkDisbursementsModel);
                if (list != null && !list.isEmpty()) {
                	bulkDisbursementsModel = (BulkDisbursementsModel) list.get(0);
                	limitApplicableForBulkdisbursement = bulkDisbursementsModel.getLimitApplicable();
                }
            }

			recBvsFlag = "1";
//			if(!limitApplicableForBulkdisbursement){
//				recBvsFlag ="0";
//			}else{
//				commonCommandManager.verifyWalkinCustomerThroughputLimits(workFlowWrapper, TransactionTypeConstantsInterface.OLA_CREDIT, txDetailMasterModel.getRecipientCnic());
//				recBvsFlag = (String) workFlowWrapper.getObject(CommandFieldConstants.IS_RECEIVER_BVS_REQUIRED);
//			}

		}catch(WorkFlowException wex)
		   {
		    logger.error("ReceiveCashInfoCommand Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId+ " Exception Message:" + wex.getMessage()); 
		    if(StringUtil.isFailureReasonId(wex.getMessage())){
		       throw new CommandException(MessageUtil.getMessage(wex.getMessage()),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
		  }else{
		        throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
		     }
		   } catch (Exception ex)
		{
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}

		if (logger.isDebugEnabled())
		{
			logger.debug("End of ReceiveCashInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Start of ReceiveCashInfoCommand.prepare()");
		}

		preparedBaseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();

		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		agentMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		recvMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE);
		recvCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
		walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);

		if (logger.isDebugEnabled())
		{
			logger.debug("End of ReceiveCashInfoCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNumber, validationErrors, "Agent Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(recvMobileNumber, validationErrors, "Recipient Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(recvCnic, validationErrors, "Recipient Cnic");
		validationErrors = ValidatorWrapper.doRequired(transactionId, validationErrors, "Transaction Id");
		
		CommonCommandManager commonCommandManager	=	this.getCommonCommandManager();
		
		try
		{
			TransactionDetailMasterModel transactionDetailMasterModel=new TransactionDetailMasterModel();
			transactionDetailMasterModel.setTransactionCode(transactionId);
			transactionDetailMasterModel.setRecipientMobileNo(recvMobileNumber);
			transactionDetailMasterModel.setRecipientCnic(recvCnic);
	//		transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
			
			SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);
			searchBaseWrapper=commonCommandManager.loadTransactionDetailMaster(searchBaseWrapper);
			
			
			
			//***************************************************************************************************************************
			//									Check if sender or receiver cnic is blacklisted
			//***************************************************************************************************************************
			if (!validationErrors.hasValidationErrors()) {
				if (this.getCommonCommandManager().isCnicBlacklisted(recvCnic)) {
					validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
					throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
				}
			}
			//***************************************************************************************************************************


			if(searchBaseWrapper.getBasePersistableModel()!=null)
			{
				transactionDetailMasterModel	=	(TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(new ProductModel(transactionDetailMasterModel.getProductId()));				
				commonCommandManager.loadProduct(baseWrapper);
				this.productModel = (ProductModel) baseWrapper.getBasePersistableModel();			
				
				if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.COMPLETED.longValue()){
					throw new CommandException(MessageUtil.getMessage("RECEIVE_CASH_ERROR_COMPLETED"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}else if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.PENDING_ACTION_AUTH.longValue()){
					throw new CommandException(MessageUtil.getMessage("RECEIVE_CASH_ERROR_PENDING_ACTION_AUTH"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}else if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.UNCLAIMED.longValue()){
					throw new CommandException(MessageUtil.getMessage("RECEIVE_CASH_ERROR_UNCLAIMED"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}else if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.REVERSED.longValue()){
					throw new CommandException(MessageUtil.getMessage("RECEIVE_CASH_ERROR_REVERSED"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}else if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue()){
					throw new CommandException(MessageUtil.getMessage("RECEIVE_CASH_ERROR_REVERSE_COMPLETED"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}else if(transactionDetailMasterModel.getSupProcessingStatusId() != SupplierProcessingStatusConstants.IN_PROGRESS.longValue()){
					throw new CommandException(MessageUtil.getMessage("RECEIVE_CASH_ERROR_OTHER"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}
				
				if(transactionDetailMasterModel.getUpdateP2PFlag() != null && transactionDetailMasterModel.getUpdateP2PFlag()){
					throw new CommandException(MessageUtil.getMessage("UPDATE_P2P_PENDING_AUTH_ERROR"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}

				if(transactionDetailMasterModel.getReversalFlag() != null && transactionDetailMasterModel.getReversalFlag()){
					throw new CommandException(MessageUtil.getMessage("REVERSAL_INPROCESS_ERROR"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}

			}
			else
			{
				throw new CommandException("Transaction ID does not exists against the Recipient CNIC/mobile number.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			ex.printStackTrace();
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}
		
		
		isRegistered="0";
		
		try
		{
			if(commonCommandManager.isCustomerAlreadyRegistered(recvCnic))
			{
				isRegistered="1";
			}
		} 
		catch (FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}
		
		return validationErrors;
	}

	@Override
	public String response()
	{
		return toXML();
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("End of ReceiveCashInfoCommand.toXML()");
		}
		
		Boolean isInclusiveChargesIncluded = txDetailMasterModel.getThirdPartyCheck();
		isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;	

		Double TPAM = txDetailMasterModel.getInclusiveCharges();	
		
		if(isInclusiveChargesIncluded) {
			TPAM = null;
		}		
		
		TPAM = (TPAM == null) ? 0D : TPAM;
		
		Double TXAM = txDetailMasterModel.getTransactionAmount();
		TXAM -= TPAM;		
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, replaceNullWithEmpty(senderMobileNumber)));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_S_W_CNIC, replaceNullWithEmpty(txDetailMasterModel.getSenderCnic())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, replaceNullWithEmpty(recvMobileNumber)));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(recvCnic)));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_ID, replaceNullWithEmpty(txDetailMasterModel.getTransactionCode())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(txDetailMasterModel.getCreatedOn())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(txDetailMasterModel.getCreatedOn())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(txDetailMasterModel.getInclusiveCharges())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(replaceNullWithZero(txDetailMasterModel.getInclusiveCharges()))));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, ""+replaceNullWithZero(TPAM)));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(replaceNullWithZero(TPAM))));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, ""+replaceNullWithZero(TXAM)));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(replaceNullWithZero(TXAM))));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAM, ""+replaceNullWithZero(TXAM)));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(TXAM))));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, String.valueOf(txDetailMasterModel.getProductId().longValue())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.IS_REG, isRegistered));
		
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_SERVICE_ID, String.valueOf(productModel.getServiceId())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_BVS_REQUIRED, recBvsFlag));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PAYMENT_TYPE, "Money Transfer"));


		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of ReceiveCashInfoCommand.toXML()");
		}
		
		return strBuilder.toString();
	}
}
