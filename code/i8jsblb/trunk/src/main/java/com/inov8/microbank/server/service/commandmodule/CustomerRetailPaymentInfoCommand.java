package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.AgentRetailPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;



public class CustomerRetailPaymentInfoCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected BaseWrapper preparedBaseWrapper;
	protected String accountId;
	double discountAmount;
	
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
	double billAmount;
	protected String customerCNIC;
	CommissionAmountsHolder commissionAmountsHolder;
	CustomerModel customerModel;
	AgentRetailPaymentVO p ;
	
	protected String customerMobileNumber;
	private String receiverMobileNumber;
	private String receiverName;
	
	protected final Log logger = LogFactory.getLog(CustomerRetailPaymentInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");

	protected String txAmount;
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerRetPaymentCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		CommissionWrapper commissionWrapper;
		baseWrapper = new BaseWrapperImpl();
		
		
		if(appUserModel.getCustomerId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				     
				     smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
				     smartMoneyAccountModel.setDeleted(false);
					smartMoneyAccountModel.setDefAccount(true);
					smartMoneyAccountModel.setActive(true);
				     SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				     searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				     
				     searchBaseWrapper = commonCommandManager.loadSMAExactMatch(searchBaseWrapper);
				     
				     List<SmartMoneyAccountModel> searchedList = searchBaseWrapper.getCustomList().getResultsetList();
				     
				     smartMoneyAccountModel = (SmartMoneyAccountModel) searchedList.get(0);

				     baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				     
				     validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

					if(!validationErrors.hasValidationErrors())
					{
						if(smartMoneyAccountModel.getName() != null)
						{
							if(smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString()))
							{


								AppUserModel retailerAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(receiverMobileNumber);
								if (retailerAppUserModel == null){

									throw new CommandException(MessageUtil.getMessage("invalid.mobile"),Long.valueOf(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED),ErrorLevel.MEDIUM,new Throwable());

								}else if(retailerAppUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.RETAILER){
									logger.error("[CashDepositInfoCommand.execute] Invalid Retailer Account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
									throw new CommandException(MessageUtil.getMessage("invalid.mobile"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

								}else{
									validationErrors = commonCommandManager.checkActiveAppUser(retailerAppUserModel);
									if(validationErrors.hasValidationErrors()){
										throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									receiverName=retailerAppUserModel.getFullName();

									//Check User Device Accounts health
									BaseWrapper baseWrapper = new BaseWrapperImpl();
									baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
									baseWrapper.setBasePersistableModel(retailerAppUserModel);
									validationErrors = getCommonCommandManager().checkRecipientAgentCredentials(retailerAppUserModel,DeviceTypeConstantsInterface.MOBILE);

									if(validationErrors.hasValidationErrors()) {
										throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}

									//check Customer SMA
									searchBaseWrapper = new SearchBaseWrapperImpl();
									SmartMoneyAccountModel customerSMA =  new SmartMoneyAccountModel();
									customerSMA.setRetailerContactId(retailerAppUserModel.getRetailerContactId());
									customerSMA.setActive(true);
									customerSMA.setDefAccount(true);
									searchBaseWrapper.setBasePersistableModel(customerSMA);

									searchBaseWrapper = commonCommandManager.loadSMAExactMatch(searchBaseWrapper);
									if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
										customerSMA = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
									}

									//baseWrapper.setBasePersistableModel(customerSMA);
									if(!smartMoneyAccountModel.getActive())
									{
										validationErrors.getStringBuilder().append("Account is not Active");
									}
									if(!validationErrors.hasValidationErrors()) {
										if(customerSMA.getName() != null) {
											if(!customerSMA.getRetailerContactId().toString().equals(retailerAppUserModel.getRetailerContactId().toString())) {
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
									AgentRetailPaymentVO retPaymentVO = (AgentRetailPaymentVO) workFlowWrapper.getProductVO();

//									To calculate the commission

									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(retPaymentVO.getTransactionAmount());

									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_RET_PAYMENT_TX);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setCustomerModel(customerModel);

									long segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNumber);
									DistributorModel distributorModel = new DistributorModel();
									distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);

									commonCommandManager.checkProductLimit(segmentId,productModel.getProductId(), ThreadLocalAppUser.getAppUserModel().getMobileNo(), Long.valueOf(deviceTypeId), transactionModel.getTransactionAmount(), productModel, distributorModel, workFlowWrapper.getHandlerModel());

									workFlowWrapper.setToSegmentId(segmentId);

									SegmentModel segmentModel = new SegmentModel();
									segmentModel.setSegmentId(segmentId);
									workFlowWrapper.setSegmentModel(segmentModel);

									workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
									workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(Long.valueOf( deviceTypeId)));

									workFlowWrapper.setIsCustomerInitiatedTransaction(true);
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
									userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

									commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel , commissionAmountsHolder.getTotalAmount(), true);

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
				if(ex.getMessage().indexOf("JTA") != -1)
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
		receiverMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		preparedBaseWrapper = baseWrapper;
		BaseWrapper bWrapper = new BaseWrapperImpl();
		CustomerModel customerModel = new CustomerModel();
		customerModel.setCustomerId( appUserModel.getCustomerId() );
		bWrapper.setBasePersistableModel(customerModel);

		try{
			bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);

			this.customerModel = (CustomerModel) bWrapper.getBasePersistableModel();

		}catch(Exception ex){
			logger.error("[AccountToAccountInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}

	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		ValidatorWrapper.doRequired(receiverMobileNumber, validationErrors,"Recipient Retailer's Id");
		ValidatorWrapper.doRequired(txAmount, validationErrors,"Transaction amount");
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(receiverMobileNumber,validationErrors,"Agent Mobie");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");

		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
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
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_AGENT_MOBILE, replaceNullWithEmpty(receiverMobileNumber)));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECIPIENT_AGENT_NAME, replaceNullWithEmpty(receiverName)));
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
