package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AgentTransferRuleModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.TaxValueBean;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AgentToAgentTransferVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class AgentToAgentInfoCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected BaseWrapper preparedBaseWrapper;
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
	CommissionAmountsHolder commissionAmountsHolder;

	String senderAgentMobile;
	String recipientAgentMobile;
	String txAmount;
	RetailerContactModel fromRetailerContactModel;

	
	protected final Log logger = LogFactory.getLog(AgentToAgentInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	AgentToAgentTransferVO p;
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AgentToAgentInfoCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		CommissionWrapper commissionWrapper;
		baseWrapper = new BaseWrapperImpl();
		
		
		if(appUserModel.getRetailerContactId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
					
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel.setDeleted(false);
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					baseWrapper = commonCommandManager.loadOLASmartMoneyAccount(baseWrapper);
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

					baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
					validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
					
					AppUserModel userModel = new AppUserModel();
					SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
					userModel.setMobileNo(recipientAgentMobile);
					userModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					searchBaseWrapper.setBasePersistableModel(userModel);
					searchBaseWrapper = commonCommandManager.loadAppUserByMobileNumberAndType(searchBaseWrapper);
					if(null != searchBaseWrapper.getBasePersistableModel())
					{
						userModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
						sma.setRetailerContactId(userModel.getRetailerContactId());
						sma.setDeleted(false);
						baseWrapper.setBasePersistableModel(sma);

						baseWrapper = commonCommandManager.loadOLASmartMoneyAccount(baseWrapper);
						sma = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

//						validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
						if(!validationErrors.hasValidationErrors()){
							//Check Recipient Agent Account for Blocked / Deactivated / Closed Status
							validationErrors = commonCommandManager.checkRecipientAgentCredentials(userModel, Long.valueOf(deviceTypeId));
						}
					}else{
						throw new CommandException(this.getMessageSource().getMessage("invalid.mobile", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					if(!validationErrors.hasValidationErrors())
					{
						if(smartMoneyAccountModel.getName() != null)
						{
							if(smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString()))
							{
					
								productModel = new ProductModel();
								TransactionModel transactionModel = null;
								productModel.setProductId(Long.parseLong(productId));
								baseWrapper.setBasePersistableModel(productModel);
								baseWrapper = commonCommandManager.loadProduct(baseWrapper);
								productModel = (ProductModel)baseWrapper.getBasePersistableModel();
								
								commonCommandManager.checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble(txAmount), productModel, null, workFlowWrapper.getHandlerModel());								
								
								Long senderAppUserId = appUserModel.getAppUserId(); //Agent appUserId
								if(handlerAppUserModel != null && handlerAppUserModel.getAppUserId() != null){
									senderAppUserId = handlerAppUserModel.getAppUserId(); //Handler appUserId
								}
								AgentTransferRuleModel ruleModel = commonCommandManager.findAgentTransferRule(Long.valueOf(deviceTypeId),Double.parseDouble(txAmount), senderAppUserId, userModel.getAppUserId());
								workFlowWrapper.setAgentTransferRuleModel(ruleModel);
								
								if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) 
								{
									ProductVO productVO = commonCommandManager.loadProductVO(preparedBaseWrapper);
									if(productVO == null)
									{
										throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									
									productVO.populateVO(productVO, preparedBaseWrapper);
									
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setSmartMoneyAccountModel(sma);
									workFlowWrapper.setToRetailerContactAppUserModel(userModel);
									BillPaymentProductDispenser billSale = (BillPaymentProductDispenser)commonCommandManager.loadProductDispense(workFlowWrapper);
									workFlowWrapper.setProductVO(productVO);
									
									workFlowWrapper = commonCommandManager.getBillInfo(billSale, workFlowWrapper);
									AgentToAgentTransferVO agentToAgentVO = (AgentToAgentTransferVO)workFlowWrapper.getProductVO();
							
//									To calculate the commission

									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(Double.parseDouble(txAmount));
									
									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.RET_TO_RET_TX);
									workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setFromRetailerContactAppUserModel( appUserModel );
									
									SegmentModel segmentModel = new SegmentModel();
									segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
									workFlowWrapper.setSegmentModel(segmentModel);
									
									workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
									workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
									
								    workFlowWrapper.setTaxRegimeModel(fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel());
									
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
									
									agentToAgentVO.setAgentName(workFlowWrapper.getToRetailerContactAppUserModel().getFirstName() + " "+ workFlowWrapper.getToRetailerContactAppUserModel().getLastName());
									agentToAgentVO.setAgentCNIC(workFlowWrapper.getToRetailerContactAppUserModel().getNic());
									
									p = agentToAgentVO;
								}							
							}
							else
							{
								throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
						else
						{
							throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(WorkFlowException wex)
			{
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch (ClassCastException e)
			{							
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
			}
			catch(Exception ex)
			{
				logger.error(ex.getMessage(),ex);
				if(ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1)
				{
					throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
				}
				else
				{
					throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
				}
			}
		}
		else
		{
			throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AgentToAgentInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		senderAgentMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		recipientAgentMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[AgentToAgentInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}

		logger.info("[AgentToAgentInfoCommand.prepare] Logged in AppUserID:" + appUserModel.getAppUserId() + ". ProductID:" + productId + ". Receiver Agent Mobile No:" + recipientAgentMobile);
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(senderAgentMobile,validationErrors,"Agent mobile No");
		validationErrors = ValidatorWrapper.doRequired(recipientAgentMobile,validationErrors,"Recipient agent mobile No");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Amount");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doInteger(senderAgentMobile, validationErrors, "Agent mobile no");
			validationErrors = ValidatorWrapper.doInteger(recipientAgentMobile, validationErrors, "Recipient agent mobile no");
			validationErrors = ValidatorWrapper.doNumeric(txAmount,validationErrors,"Amount");
		}
		
		if(!validationErrors.hasValidationErrors()){
			if(senderAgentMobile.equals(recipientAgentMobile)){
				validationErrors.getStringBuilder().append("Your Mobile No cannot be used as Recipient agent mobile.");
			}
		}
		
		if(!validationErrors.hasValidationErrors()){
			if(appUserModel.getMobileNo().equals(recipientAgentMobile)){
				validationErrors.getStringBuilder().append("Own account transfer is not allowed.");
			}
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
		StringBuilder strBuilder = new StringBuilder();
		
		if(commissionAmountsHolder != null){
			
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE, recipientAgentMobile));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECIPIENT_AGENT_CNIC, p.getAgentCNIC()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECIPIENT_AGENT_NAME, p.getAgentName()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}
		
		return strBuilder.toString();
	}
	
}
