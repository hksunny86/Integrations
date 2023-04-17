package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * 
 * @author AtifHu
 *
 */
public class PendingTransactionPaymentCommand extends BaseCommand
{
	protected final Log logger = LogFactory.getLog(PendingTransactionPaymentCommand.class);

	protected String pin, encryptionType, oneTimePin, trxId, agentMobile, recvMobile, recvCnic, senderMobile, senderCnic, productId;
	private double agentBalance	=	0.0;
	protected AppUserModel appUserModel;
	protected BaseWrapper preparedBaseWrapper;
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	protected TransactionModel transactionModel;
	protected ProductModel productModel;
	protected BaseWrapper baseWrapper;
	protected String successMessage;
	protected MiniTransactionModel	miniTransactionModel;
	protected TransactionDetailMasterModel	txDetailMasterModel;
	@Override
	public void execute() throws CommandException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Start of PendingTransactionPaymentCommand.execute()");
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
				transactionCodeModel.setCode(trxId);
				
				BaseWrapper bWrapper = new BaseWrapperImpl();
				bWrapper.setBasePersistableModel(transactionCodeModel);
				bWrapper = commonCommandManager.loadTransactionCodeByCode(bWrapper);
				transactionCodeModel = ((TransactionCodeModel)(bWrapper.getBasePersistableModel()));

				miniTransactionModel = new MiniTransactionModel();
				miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
				miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);

				SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
				wrapper.setBasePersistableModel(miniTransactionModel);
				wrapper = commonCommandManager.loadMiniTransaction(wrapper);
				
				if(null != wrapper.getCustomList() && null != wrapper.getCustomList().getResultsetList() && wrapper.getCustomList().getResultsetList().size() > 0)
				{
					miniTransactionModel = (MiniTransactionModel)wrapper.getCustomList().getResultsetList().get(0);
					
					if(miniTransactionModel.getMiniTransactionStateId().longValue() != MiniTransactionStateConstant.PIN_SENT) 
					{
						throw new CommandException("This transaction is either already claimed or expired.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
					}
					if(!miniTransactionModel.getOneTimePin().equals(EncoderUtils.encodeToSha(oneTimePin)))
					{
						throw new CommandException("Invalid Transaction Code Provided.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
					}
				}
				else
				{
					throw new CommandException("This transaction is either already claimed or expired.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
				}

				workFlowWrapper =commonCommandManager.makeCustomerTrxByTransactionCode(this.trxId, this.recvCnic);
				commonCommandManager.loadAndForwardAccountToQueue(this.trxId);

				transactionModel=workFlowWrapper.getTransactionModel();
				txDetailMasterModel=workFlowWrapper.getTransactionDetailMasterModel();
				productModel=workFlowWrapper.getProductModel();
				/**
				 * Added by Atif
				 */

				agentBalance	=	workFlowWrapper.getOLASwitchWrapper().getOlavo().getAgentBalanceAfterTransaction();
				/*double customerBalance	=	workFlowWrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction();

				String brandName = MessageUtil.getMessage("jsbl.brandName");
				ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

				double incCharges = workFlowWrapper.getCommissionAmountsHolder().getTotalInclusiveAmount();

				Date dateNow	= Calendar.getInstance().getTime();

				String agentSms = MessageUtil.getMessage(
						"USSD.PendingTransaction.Agent",
						new Object[]{
								brandName,
								workFlowWrapper.getTransactionCodeModel().getCode(),
								Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
								PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_TIME_FORMAT),
								PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_DATE_FORMAT),
								Formatter.formatDouble(incCharges),
								Formatter.formatDouble(agentBalance)
						});

				messageList.add(new SmsMessage(workFlowWrapper.getAppUserModel().getMobileNo(), agentSms));

				String customerSms = MessageUtil.getMessage(
						"USSD.PendingTransaction.Receiver",
						new Object[]{
								brandName,
								workFlowWrapper.getTransactionCodeModel().getCode(),
								Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
								PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_TIME_FORMAT),
								PortalDateUtils.formatDate(dateNow, PortalDateUtils.SHORT_DATE_FORMAT),
								brandName+" Agent",
								Formatter.formatDouble(incCharges),
								Formatter.formatDouble(customerBalance)
						});

				messageList.add(new SmsMessage(workFlowWrapper.getCustomerModel().getMobileNo(), customerSms));
				workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);*/
				commonCommandManager.sendSMS(workFlowWrapper);
			}
			else
			{
				throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			}
		} 
		catch (Exception ex)
		{
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}

		if (logger.isDebugEnabled())
		{
			logger.debug("End of PendingTransactionPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Start of PendingTransactionPaymentCommand.prepare()");
		}

		preparedBaseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();

		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		encryptionType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		oneTimePin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ONE_TIME_PIN);
		agentMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
		trxId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		recvMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE);
		recvCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
		senderMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		senderCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		productId= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);

		if (logger.isDebugEnabled())
		{
			logger.debug("End of PendingTransactionPaymentCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "PIN");
		validationErrors = ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");
		validationErrors = ValidatorWrapper.doRequired(oneTimePin, validationErrors, "One time PIN");
		validationErrors = ValidatorWrapper.doRequired(agentMobile, validationErrors, "Agent Mobile");
		validationErrors = ValidatorWrapper.doRequired(trxId, validationErrors, "Transaction Id");
		validationErrors = ValidatorWrapper.doRequired(recvMobile, validationErrors, "Recipient Mobile");
		validationErrors = ValidatorWrapper.doRequired(recvCnic, validationErrors, "Recipient Cnic");
		validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");

		if(Long.parseLong(productId)!=ProductConstantsInterface.BULK_PAYMENT) {
			validationErrors = ValidatorWrapper.doRequired(senderMobile, validationErrors, "Sender Mobile");
			validationErrors = ValidatorWrapper.doRequired(senderCnic, validationErrors, "Sender Cnic");
		}

		CommonCommandManager commonCommandManager	=	this.getCommonCommandManager();

		try
		{
			TransactionDetailMasterModel transactionDetailMasterModel=new TransactionDetailMasterModel();
			transactionDetailMasterModel.setTransactionCode(trxId);
			transactionDetailMasterModel.setRecipientMobileNo(recvMobile);
			transactionDetailMasterModel.setRecipientCnic(recvCnic);
			//		transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);

			SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);
			searchBaseWrapper=commonCommandManager.loadTransactionDetailMaster(searchBaseWrapper);

			if(searchBaseWrapper.getBasePersistableModel()!=null)
			{
				transactionDetailMasterModel	=	(TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
				if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.COMPLETED.longValue()){
					throw new CommandException("Transaction ID already claimed.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}
				if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.FAILED.longValue()){
					throw new CommandException("Transaction ID is invalid.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
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
			logger.debug("Start of PendingTransactionPaymentCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		
		Double totalAmount = txDetailMasterModel.getTransactionAmount() - txDetailMasterModel.getInclusiveCharges();

		if(txDetailMasterModel.getThirdPartyCheck() != null && txDetailMasterModel.getThirdPartyCheck()){
			totalAmount = txDetailMasterModel.getTransactionAmount();
		}
				
		Double totalCommission = Double.valueOf(transactionModel.getTransactionAmount());
		strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_TRANS)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_TRN)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRXID)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_RWCNIC)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(recvCnic)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_RWMOB)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(recvMobile)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_SWMOB)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(senderMobile)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_SWCNIC)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(senderCnic)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_DATEF)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TIMEF)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(Formatter.formatTime(transactionModel.getCreatedOn()))
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PROD)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithEmpty(productModel.getName()))
				.append(TAG_SYMBOL_QUOTE)
					
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TPAM)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithZero(totalCommission))
				.append(TAG_SYMBOL_QUOTE)
				
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TPAMF)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(Formatter.formatNumbers(totalCommission))
				.append(TAG_SYMBOL_QUOTE)
				
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TAMT)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithZero(totalAmount))
				.append(TAG_SYMBOL_QUOTE)
				
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TAMTF)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(Formatter.formatNumbers(totalAmount))
				.append(TAG_SYMBOL_QUOTE)
				
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TXAM)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithZero(transactionModel.getTransactionAmount()))
				.append(TAG_SYMBOL_QUOTE)
				
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TXAMF)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(Formatter.formatNumbers(transactionModel.getTransactionAmount()))
				.append(TAG_SYMBOL_QUOTE)

			.append(TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_BALF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(agentBalance))
			.append(TAG_SYMBOL_QUOTE);
		
			strBuilder.append(TAG_SYMBOL_CLOSE);
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE);
			if(logger.isDebugEnabled())
			{
				logger.debug("End of PendingTransactionPaymentCommand.toXML()");
			}
		return strBuilder.toString();
	}
}