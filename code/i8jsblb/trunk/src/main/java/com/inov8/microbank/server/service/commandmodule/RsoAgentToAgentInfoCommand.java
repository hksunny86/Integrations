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
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class RsoAgentToAgentInfoCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected BaseWrapper preparedBaseWrapper;
	protected String accountId;
	double discountAmount;
	String receiverMSISDN;
	
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
//	double billAmount;
	Double txAmount = 0.0D;
	protected String customerCNIC;
	CommissionAmountsHolder commissionAmountsHolder;
	BillPaymentVO p ;
	
	
	
	protected final Log logger = LogFactory.getLog(AllPayBillInfoCommand.class);
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

					validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
					
					AppUserModel userModel = new AppUserModel();
					SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
					userModel.setMobileNo(receiverMSISDN);
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

						if(null == sma){
							throw new CommandException("Recipient Account is not Active.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}else{
							validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
						}

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
//									System.out.println();
									
									workFlowWrapper = commonCommandManager.getBillInfo(billSale, workFlowWrapper);
									BillPaymentVO billSaleVO = (BillPaymentVO)workFlowWrapper.getProductVO();
							
//									To calculate the commission

									transactionModel = new TransactionModel();
//									transactionModel.setTransactionAmount(billSaleVO.getCurrentBillAmount());					
									//SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
//									smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
//									baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
//									baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
//									smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
//									
//									if(smartMoneyAccountModel.getPaymentModeId() != null)
//									{
//										transactionModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
//									}
									/*if(productModel.getMinLimit() != null && 
											(transactionModel.getTransactionAmount() < productModel.getMinLimit()))
									{
										throw new CommandException("The amount provided is less than the minimum transaction limit.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									else if(productModel.getMaxLimit() != null && 
											(transactionModel.getTransactionAmount() > productModel.getMaxLimit()))
									{
										throw new CommandException("The amount provided is greater than the maximum transaction limit.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									*/		
									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.RSO_TO_RET_TX);
//									transactionModel.setTransactionAmount(txAmount);
									workFlowWrapper.setTransactionAmount(txAmount);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setFromRetailerContactAppUserModel( appUserModel );
									
									//pulling the customer segment for commission module changes
									SegmentModel segmentModel = new SegmentModel();
									segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
									workFlowWrapper.setSegmentModel(segmentModel);
									
									RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
								    workFlowWrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
									
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
									
									userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
//									discountAmount = productModel.getFixedDiscount() + (productModel.getPercentDiscount()/100) * commissionAmountsHolder.getTotalAmount();
//									if(userDeviceAccountsModel.getCommissioned() && discountAmount > commissionAmountsHolder.getTotalAmount())
//									{
//										throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.discountexceedsprice", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//									}
									//billAmount = billSaleVO.getBillAmount();
									p = billSaleVO;
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
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayBillInfoCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		receiverMSISDN = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		String amountParam = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		
		if(amountParam != null && !"".equals(amountParam)) {
			this.txAmount = Double.valueOf(amountParam);  
		}
		
		logger.info("[AgentToAgentInfoCommand.prepare] Logged in AppUserID:" + appUserModel.getAppUserId() + ". ProductID:" + productId + ". Receiver Mobile No:" + receiverMSISDN);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AgentToAgentInfoCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayBillInfoCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
	//	validationErrors = ValidatorWrapper.doRequired(customerCode,validationErrors,"Customer Code");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(receiverMSISDN,validationErrors,"MSISDN");
//		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayBillInfoCommand.validate()");
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
			logger.debug("Start of AllPayBillInfoCommand.toXML()");
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
				.append(TAG_SYMBOL_CLOSE)
				
				.append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())))
				
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
				.append(CommandFieldConstants.KEY_TX_PROCESS_AMNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))
			
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE);
			
			
			/////////////////////////adding customer CNIC////////////////////
			
			
			if(null != customerCNIC && !"".equals(customerCNIC)){
				
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_CNIC)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)		
				.append(customerCNIC)		
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE);
				
			}
			
			
			
			
			//////////////////////////////////////////////////////////////////
			
			
			
			
			
			
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
				strBuilder .append( p.responseXML() );
				/*String billingMonthStr=((UtilityBillVO)p).getBillingMonth();
				
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
				.append(TAG_SYMBOL_CLOSE);*/
			}else{
				strBuilder .append( p.responseXML() );
			}
				
//				.append(TAG_SYMBOL_OPEN)
//				.append(TAG_PARAM)
//				.append(TAG_SYMBOL_SPACE)
//				.append(ATTR_PARAM_NAME)
//				.append(TAG_SYMBOL_EQUAL)
//				.append(TAG_SYMBOL_QUOTE)
//				.append(CommandFieldConstants.KEY_BILL_AMOUNT)
//				.append(TAG_SYMBOL_QUOTE)
//				.append(TAG_SYMBOL_CLOSE)
//				
//				.append(billAmount)
//				
//				.append(TAG_SYMBOL_OPEN)
//				.append(TAG_SYMBOL_SLASH)
//				.append(TAG_PARAM)
//				.append(TAG_SYMBOL_CLOSE)
			     strBuilder.append(TAG_SYMBOL_OPEN)
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
				.append(TAG_SYMBOL_CLOSE)
				
				.append(Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount()))
				
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
				.append(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount()))
			
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)
				
//				.append(TAG_SYMBOL_OPEN)
//				.append(TAG_PARAM)
//				.append(TAG_SYMBOL_SPACE)
//				.append(ATTR_PARAM_NAME)
//				.append(TAG_SYMBOL_EQUAL)
//				.append(TAG_SYMBOL_QUOTE)
//				.append(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT)
//				.append(TAG_SYMBOL_QUOTE)
//				.append(TAG_SYMBOL_CLOSE)
//				
//				.append(Formatter.formatNumbers(billAmount))
//				
//				.append(TAG_SYMBOL_OPEN)
//				.append(TAG_SYMBOL_SLASH)
//				.append(TAG_PARAM)
//				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				
				.append(Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount()))
			
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
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayBillInfoCommand.toXML()");
		}
		return strBuilder.toString();
	}
	
}
