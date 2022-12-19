package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.util.List;

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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.AgentRetailPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class AgentRetailPaymentInfoCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected BaseWrapper preparedBaseWrapper;
	
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
	String transactionAmount;
	protected String customerCNIC;
	CommissionAmountsHolder commissionAmountsHolder;
	AgentRetailPaymentVO p ;
	RetailerContactModel retailerContactModel;

	protected String customerMobileNumber;
	
	protected final Log logger = LogFactory.getLog(AgentRetailPaymentInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	
	@Override
	public void execute() throws CommandException
	{
		
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
				     SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				     searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				     
				     searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				     
				     List<SmartMoneyAccountModel> searchedList = searchBaseWrapper.getCustomList().getResultsetList();
				     
				     smartMoneyAccountModel = (SmartMoneyAccountModel) searchedList.get(0);

				     baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				     
				     baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
				     validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

					if(!validationErrors.hasValidationErrors())
					{
						if(smartMoneyAccountModel.getName() != null)
						{
							if(smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString()))
							{
								//Customer Validation
								AppUserModel customerAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(customerMobileNumber);
								if (customerAppUserModel == null){
									
									throw new CommandException(this.getMessageSource().getMessage(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED, null,null),Long.valueOf(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED),ErrorLevel.MEDIUM,new Throwable());
								
								}else if(customerAppUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER){
									logger.error("[CashDepositInfoCommand.execute] Invalid Customer Account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
									throw new CommandException(MessageUtil.getMessage("invalid.mobile"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

								}else{
									validationErrors = commonCommandManager.checkActiveAppUser(customerAppUserModel);
									if(validationErrors.hasValidationErrors()){
										throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}

									//Check User Device Accounts health
									BaseWrapper baseWrapper = new BaseWrapperImpl();
									baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
									baseWrapper.setBasePersistableModel(customerAppUserModel);
									validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);
									
									if(validationErrors.hasValidationErrors()) {
										throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									
									//check Customer SMA
									searchBaseWrapper = new SearchBaseWrapperImpl();
									SmartMoneyAccountModel customerSMA =  new SmartMoneyAccountModel();
									customerSMA.setCustomerId(customerAppUserModel.getCustomerId());
									searchBaseWrapper.setBasePersistableModel(customerSMA);
									
									searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
									if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
										customerSMA = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
									}
									
									baseWrapper.setBasePersistableModel(customerSMA);
									validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
								
									if(!validationErrors.hasValidationErrors()) {
										if(customerSMA.getName() != null) {
											if(!customerSMA.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())) {
												logger.error("[AgentRetailPaymentInfoCommand.execute] Invalid Smart Money account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ", Customer Mobile No:" + customerMobileNumber); 
												throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
											}
										}else {
											logger.error("[AgentRetailPaymentInfoCommand.execute] Throwing Exception - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ", Customer Mobile No:" + customerMobileNumber); 
											throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
										}
									}else {
										logger.error("[AgentRetailPaymentInfoCommand.execute] Throwing Exception - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ ", Customer Mobile No:" + customerMobileNumber); 
										throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									
								}
								
								productModel = new ProductModel();
								TransactionModel transactionModel = null;
								productModel.setProductId(Long.parseLong(productId));
								baseWrapper.setBasePersistableModel(productModel);
								baseWrapper = commonCommandManager.loadProduct(baseWrapper);
								productModel = (ProductModel)baseWrapper.getBasePersistableModel();
								
								if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) 
								{
									ProductVO productVO = commonCommandManager.loadProductVO(preparedBaseWrapper);
									if(productVO == null)
									{
										throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									
									productVO.populateVO(productVO, preparedBaseWrapper);
									workFlowWrapper.setProductVO(productVO);
									
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setProductModel(productModel);
//									CustomerRetailPaymentDispenser dispenser = (CustomerRetailPaymentDispenser)commonCommandManager.loadProductDispense(workFlowWrapper);
//									workFlowWrapper = commonCommandManager.getBillInfo(dispenser, workFlowWrapper);
									AgentRetailPaymentVO retPaymentVO = (AgentRetailPaymentVO)workFlowWrapper.getProductVO();
							
//									To calculate the commission

									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(retPaymentVO.getTransactionAmount());					
									
									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_RET_PAYMENT_TX);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setRetailerContactModel(retailerContactModel);

									long segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNumber);
									
									commonCommandManager.checkProductLimit(segmentId,productModel.getProductId(), ThreadLocalAppUser.getAppUserModel().getMobileNo(), Long.valueOf(deviceTypeId), transactionModel.getTransactionAmount(), productModel, null, workFlowWrapper.getHandlerModel());

									workFlowWrapper.setToSegmentId(segmentId);
									
									SegmentModel segmentModel = new SegmentModel();
									segmentModel.setSegmentId(segmentId);
									workFlowWrapper.setSegmentModel(segmentModel);
									
								    workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
								    
								    
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
									userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
									
									commonCommandManager.checkCustomerBalance(customerMobileNumber, commissionAmountsHolder.getTotalAmount());

									p = retPaymentVO;
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
			catch(CommandException ce)
			{
				throw ce;
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
				if(ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1)
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
			logger.debug("End of CustomerRetPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[AccountToAccountInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}

	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(customerMobileNumber,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(transactionAmount,validationErrors,"Amount");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(transactionAmount,validationErrors,"Amount");
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
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerMobileNumber)));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}
		
		return strBuilder.toString();
	}
	
}
