package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class GetSupplierInfoCommand extends BaseCommand 
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
	CommissionAmountsHolder commissionAmountsHolder;
	BillPaymentVO p ;
	protected String pin;
	protected AbstractFinancialInstitution abstractFinancialInstitution;
	protected final Log logger = LogFactory.getLog(GetSupplierInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private Boolean isInclusiveCharges;
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetSupplierInfoCommand.execute()");
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
					SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

					validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

					if(!validationErrors.hasValidationErrors())
					{
						if(smartMoneyAccountModel.getName() != null)
						{
							if(smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString()))
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
									BillPaymentProductDispenser billSale = (BillPaymentProductDispenser)commonCommandManager.loadProductDispense(workFlowWrapper);
									workFlowWrapper.setProductVO(productVO);
									workFlowWrapper = commonCommandManager.getBillInfo(billSale, workFlowWrapper);
									
									BillPaymentVO billSaleVO = (BillPaymentVO)workFlowWrapper.getProductVO();
									
									//check balance of the customer for this transaction
//									if(workFlowWrapper.getProductModel().getProductId() == 50006 && ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER)
//									{
//										SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//										switchWrapper.putObject(CommandFieldConstants.KEY_PIN, preparedBaseWrapper.getObject(CommandFieldConstants.KEY_PIN));
//										switchWrapper.setWorkFlowWrapper(workFlowWrapper);
//										AbstractFinancialInstitution financialInt = (AbstractFinancialInstitution) preparedBaseWrapper.getObject(CommandFieldConstants.KEY_FINANCIAL_INSTITUTION);
//										financialInt.checkBalance(switchWrapper);
//									}
									
									//---------------------------------------------------
									
									
							
//									To calculate the commission

									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(billSaleVO.getCurrentBillAmount());					
									//SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
									smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
									baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
									baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
									smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
									
									if(smartMoneyAccountModel.getPaymentModeId() != null)
									{
										transactionModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
									}
												
									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BILL_SALE_TX);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									//Maqsood Shahzad - due to commission changes loading customer along with the segment
									CustomerModel custModel = new CustomerModel();
									custModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
									BaseWrapper bWrapper = new BaseWrapperImpl();
									bWrapper.setBasePersistableModel(custModel);
									bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
									if(null != bWrapper.getBasePersistableModel())
									{
										custModel = (CustomerModel) bWrapper.getBasePersistableModel();
										workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
									}
									
									RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
								    workFlowWrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
									
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

									isInclusiveCharges = commissionAmountsHolder.getIsInclusiveCharges();
									
									userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
//									discountAmount = productModel.getFixedDiscount() + (productModel.getPercentDiscount()/100) * commissionAmountsHolder.getTotalAmount();
//									if(userDeviceAccountsModel.getCommissioned() && discountAmount > commissionAmountsHolder.getTotalAmount())
//									{
//										throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.discountexceedsprice", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//									}
									billAmount = billSaleVO.getBillAmount();
									p = billSaleVO;
									
									logger.info("[GetSupplierInfoCommand.execute] Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Consumer No: " + (p == null ? " NULL. " : p.getConsumerNo()));
								}							
							}
							else
							{
								logger.error("[GetSupplierInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo())); 
								throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
						else
						{
							logger.error("[GetSupplierInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo())); 
							throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						logger.error("[GetSupplierInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo())); 
						throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					logger.error("[GetSupplierInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo())); 
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(CommandException e)
			{
				logger.error("[GetSupplierInfoCommand.execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo()) + " Exception Message:" + e.getMessage()); 
				throw e;
			}
			catch(WorkFlowException wex)
			{
				logger.error("[GetSupplierInfoCommand.execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo()) + " Exception Message:" + wex.getMessage()); 
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch (ClassCastException e)
			{							
				logger.error("[GetSupplierInfoCommand.execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo()) + " Exception Message:" + e.getMessage()); 
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
			}
			catch(Exception ex)
			{
				logger.error("[GetSupplierInfoCommand.execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo()) + " Exception Message:" + ex.getMessage()); 
				ex.printStackTrace();
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
			logger.error("[GetSupplierInfoCommand.execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId  + " ConsumerNo:" + (p == null ? " NULL. " : p.getConsumerNo())); 
			throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetSupplierInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetSupplierInfoCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);

		logger.info("[GetSupplierInfoCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + " AccountId: " + accountId + 
    			" deviceTypeId: " + deviceTypeId/* + 
    			" customerMobileNumber: " + customerMobileNumber*/);
    
		/**
		 * 
		 *   Change by Sheraz Ahmed on June 30th, 2008
		 *   
		 *   To get the accountId according to CustomerId and BankId basis in case of BANK
		 *   Because
		 *         in BANK case only BankId is available not accountId.
		 *         
		 */
		
		/**
		 * ------------------------Start of Change------------------------------
		 */
		
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		
		try{
		
			if( ( accountId == null || "".equals(accountId) ) 
	                && 
					         ( bankId != null && !("".equals(bankId)) ) )
			{
				CommonCommandManager commonCommandManager = this.getCommonCommandManager();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
				smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
				smartMoneyAccountModel.setActive(true);
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			
				ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors())
				{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				}
				else
				{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					
				}
			}
			else
				if( ( accountId == null || "".equals(accountId) ) 
		                && 
				         ( bankId == null || "".equals(bankId) ) )
			{
				throw new CommandException("AccountId is null and BankId is also null",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
		}
		catch(Exception ex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error("[GetSupplierInfoCommand.prepare] Exception occured for AppUserID:" + appUserModel.getAppUserId() +". Exception Details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
//			ex.printStackTrace();
		}

		/**
		 * ------------------------End of Change------------------------------
		 */

		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetSupplierInfoCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetSupplierInfoCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
	//	validationErrors = ValidatorWrapper.doRequired(customerCode,validationErrors,"Customer Code");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetSupplierInfoCommand.validate()");
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
			logger.debug("Start of GetSupplierInfoCommand.toXML()");
		}
		
		StringBuilder strBuilder = new StringBuilder();
		if(commissionAmountsHolder != null)
		{
			
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_COMM_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
				/*if("50006".equals(productId)){
					strBuilder.append(replaceNullWithZero((commissionAmountsHolder.getAgent1CommissionAmount())));
				}else{
					strBuilder.append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())));
				}*/
				strBuilder.append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())));
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_TX_PROCESS_AMNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
				strBuilder.append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()));
				/*if("50006".equals(productId)){
					strBuilder.append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()));
				}else{
					strBuilder.append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()));
				}*/
				
			
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE);
				if(UtilityCompanyEnum.contains(productId)){
					Date dueDate=((UtilityBillVO)p).getDueDate();
					strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_BILL_DUE_DATE)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_CLOSE)		
					.append(dtf.print(dueDate.getTime()))		
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
					String billingMonthStr=((UtilityBillVO)p).getBillingMonth();
					
					strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_BILL_DATE)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_CLOSE)		
					.append(billingMonthStr)		
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
				}else{
					strBuilder .append( p.responseXML() );
				}
				
				strBuilder .append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_BILL_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
				strBuilder.append(billAmount);
				/*if("50006".equals(productId)){
					strBuilder.append(replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()));
				}else{
					strBuilder.append(billAmount);
				}*/
				
				
				
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_TOTAL_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))
			
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
							
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_ACC_ID)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(accountId)
				
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				
				
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
				/*if("50006".equals(productId)){
					strBuilder.append(replaceNullWithZero((commissionAmountsHolder.getAgent1CommissionAmount())));
				}else{
					strBuilder.append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())));
				}*/
				strBuilder.append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())));
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))
			
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
				/*if("50006".equals(productId)){
					strBuilder.append(replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()));
				}else{
					strBuilder.append(replaceNullWithZero(billAmount));
				}*/
				
				strBuilder.append(replaceNullWithZero(billAmount));
				
				
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))
			
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE);
				
				
				
				
			
			if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
			{
				
				userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_DISCOUNT_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(replaceNullWithZero((discountAmount)))
				
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_FORMATED_DISCOUNT_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(Formatter.formatNumbers(discountAmount))
			
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE);
			}
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append("REASON_ID")
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			
			.append(((isInclusiveCharges) ? 4 : 5))
		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);		
			
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetSupplierInfoCommand.toXML()");
		}
		return strBuilder.toString();
	}
	
}
