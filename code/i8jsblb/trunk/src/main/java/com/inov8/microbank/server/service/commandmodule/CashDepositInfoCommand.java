package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.CashInProductDispenser;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class CashDepositInfoCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected BaseWrapper preparedBaseWrapper;
	
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
	CommissionAmountsHolder commissionAmountsHolder;
	CashInVO p ;
	
	protected String customerMobileNumber;
	protected String customerCNIC;
	protected String customerName;
	String txAmount;
	RetailerContactModel fromRetailerContactModel;
	
	protected final Log logger = LogFactory.getLog(CashDepositInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayBillInfoCommand.execute()");
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
				//validate customer mobile no.
				Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
				AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(customerMobileNumber, appUserTypeIds);
				if(null == customerAppUserModel || customerAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER) {
					logger.error("[CashDepositInfoCommand.execute] Customer App User not found: " + customerMobileNumber);
					throw new CommandException(this.getMessageSource().getMessage(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED, null, null), Long.valueOf(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED), ErrorLevel.MEDIUM, null);

				}
				else if (customerAppUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER){
					logger.error("[CashDepositInfoCommand.execute] Invalid Customer Account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerMobileNumber); 
					throw new CommandException(MessageUtil.getMessage("invalid.mobile"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());


				}else if (customerAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
					baseWrapper.setBasePersistableModel(customerAppUserModel);
					ValidationErrors userValidation = getCommonCommandManager().checkCustomerCredentials(baseWrapper);
					CustomerModel customerModel = new CustomerModel();
					try {
						customerModel = commonCommandManager.getCustomerModelById(customerAppUserModel.getCustomerId());
					} catch (CommandException e) {
						e.printStackTrace();
					}

					if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
						logger.error("[CashDepositInfoCommand.execute] Upgrade Account L0 to L1 to perform Transaction.: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerMobileNumber);
						throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

					}

					if(userValidation.hasValidationErrors()) {
						throw new CommandException(userValidation.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				
				//Validate Agent 
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
								
								if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) 
								{
									ProductVO productVO = commonCommandManager.loadProductVO(preparedBaseWrapper);
									if(productVO == null)
									{
										throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									
									productVO.populateVO(productVO, preparedBaseWrapper);
									
									workFlowWrapper.setProductModel(productModel);
									/*CashInProductDispenser billSale = (CashInProductDispenser)commonCommandManager.loadProductDispense(workFlowWrapper);
									workFlowWrapper.setProductVO(productVO);
									workFlowWrapper = commonCommandManager.getBillInfo(billSale, workFlowWrapper);
									CashInVO cashInVO = (CashInVO)workFlowWrapper.getProductVO();*/
							
									customerCNIC = customerAppUserModel.getNic();
									customerName = customerAppUserModel.getFirstName()+ " " + customerAppUserModel.getLastName();
									
//									To calculate the commission

									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(Double.valueOf(txAmount));

									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BILL_SALE_TX);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setFromRetailerContactAppUserModel( appUserModel );
									
									
									//pulling the customer segment for commission module changes
									long segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNumber);
									SegmentModel segmentModel = new SegmentModel();
									segmentModel.setSegmentId(segmentId);
									workFlowWrapper.setSegmentModel(segmentModel);
									workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
									workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
									
									commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), transactionModel.getTransactionAmount(), productModel, null,workFlowWrapper.getHandlerModel());
									
									/*BaseWrapper bWrapper = new BaseWrapperImpl();
									RetailerContactModel retailerContactModel = new RetailerContactModel();
									retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
									bWrapper.setBasePersistableModel(retailerContactModel);

									try{
										bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);

										this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

									}catch(Exception ex){
										logger.error("[AccountToAccountInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
									}*/
									
									workFlowWrapper.setTaxRegimeModel((TaxRegimeModel)fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel().clone());
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
									
									userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
									
									//p = cashInVO;
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
			}catch(CommandException e) {
				logger.error(e.getMessage()); 
				throw e;
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
			logger.debug("End of AllPayBillInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		preparedBaseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		/*if (appUserModel == null) {
			appUserModel = new AppUserModel();
			appUserModel.setMobileNo(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE));
			try {
				appUserModel = this.getCommonCommandManager().loadAppUserByMobileByQuery(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE));
				ThreadLocalAppUser.setAppUserModel(appUserModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        
        try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
        	logger.error("[CashDepositInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }

	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException{
		
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(customerMobileNumber,validationErrors,"Mobile No");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Amount");
		
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Amount");
		}

        //HRA validation check
        if(!validationErrors.hasValidationErrors()) {
            commonCommandManager.validateHRA(customerMobileNumber);
        }

        return validationErrors;
	}
	
	@Override
	public String response() 
	{
		return toXML();
	}
	
	private String toXML(){
		StringBuilder strBuilder = new StringBuilder();
		if(commissionAmountsHolder != null){
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerMobileNumber)));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, replaceNullWithEmpty(customerName)));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(customerCNIC)));
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
