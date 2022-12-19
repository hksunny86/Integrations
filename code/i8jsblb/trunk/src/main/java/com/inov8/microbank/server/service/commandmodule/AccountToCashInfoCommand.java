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
import com.inov8.microbank.server.service.integration.dispenser.AccountToCashDispenser;
import com.inov8.microbank.server.service.integration.vo.AccountToCashVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;



public class AccountToCashInfoCommand extends BaseCommand
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
	AccountToCashVO p ;
	
	protected String customerMobileNumber;
	String recepientWalkinCNIC;
	String recepientWalkinMobile;
//	protected List<AppUserModel> receiverAppUserModelList;
	
	protected final Log logger = LogFactory.getLog(AccountToCashInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AcctToCashInfoCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
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
				     SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				     searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				     
				     searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				     
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
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setWalkInCustomerCNIC(recepientWalkinCNIC);
									AccountToCashDispenser acctToCashDispenser = (AccountToCashDispenser)commonCommandManager.loadProductDispense(workFlowWrapper);
									workFlowWrapper.setProductVO(productVO);
									workFlowWrapper = commonCommandManager.getBillInfo(acctToCashDispenser, workFlowWrapper);
									AccountToCashVO acctToCashVO = (AccountToCashVO)workFlowWrapper.getProductVO();
							
//									To calculate the commission

									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(acctToCashVO.getCurrentBillAmount());					
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
									
									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									
									//pulling the customer segment for commission module changes
									/*AppUserModel customerAppUserModel = ThreadLocalAppUser.getAppUserModel();
									CustomerModel custModel = customerAppUserModel.getCustomerIdCustomerModel();
									SegmentModel segmentModel = new SegmentModel();
									segmentModel.setSegmentId(custModel.getSegmentId());
									
									workFlowWrapper.setSegmentModel(segmentModel);*/
									AppUserModel customerAppUserModel = new AppUserModel();
									customerAppUserModel.setMobileNo(customerMobileNumber);
									customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
									SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
									sBaseWrapper.setBasePersistableModel(customerAppUserModel);
									sBaseWrapper = commonCommandManager.loadAppUserByMobileNumberAndType(sBaseWrapper);
									CustomerModel custModel = new CustomerModel();
									BaseWrapper bWrapper = new BaseWrapperImpl();
									
									if(null != sBaseWrapper.getBasePersistableModel())
									{
										customerAppUserModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();
										custModel.setCustomerId(customerAppUserModel.getCustomerId());
										bWrapper.setBasePersistableModel(custModel);
										bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
										workFlowWrapper.setSegmentModel(((CustomerModel)bWrapper.getBasePersistableModel()).getSegmentIdSegmentModel());
										customerCNIC = customerAppUserModel.getNic();
									}
									else
									{
										SegmentModel segmentModel = new SegmentModel();
										segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
										workFlowWrapper.setSegmentModel(segmentModel);
									}
									
									commonCommandManager.checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(),productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), billAmount, productModel, null, workFlowWrapper.getHandlerModel());		
									
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
									p = acctToCashVO;
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
				if(StringUtil.isFailureReasonId(wex.getMessage())){
					throw new CommandException(MessageUtil.getMessage(wex.getMessage()),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
				}else{
					throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
				}
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
			logger.debug("End of AcctToCashInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AcctToCashInfoCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        recepientWalkinCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC);
    	recepientWalkinMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN);
        
		
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		//receiverAppUserModelList = this.getCommonCommandManager().getAppUserManager().findAppUserByCnicAndMobile(recepientWalkinMobile , recepientWalkinCNIC);
//		try{
//		
//			if( ( accountId == null || "".equals(accountId) ) 
//	                && 
//					         ( bankId != null && !("".equals(bankId)) ) )
//			{
//				CommonCommandManager commonCommandManager = this.getCommonCommandManager();
//				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//
//				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
//				smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
//				smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
//				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
//				
//				searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
//				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
//			
//				ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
//				
//				if(!validationErrors.hasValidationErrors())
//				{
//					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
//				}
//				else
//					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//			}
//			else
//				if( ( accountId == null || "".equals(accountId) ) 
//		                && 
//				         ( bankId == null || "".equals(bankId) ) )
//			{
//				throw new CommandException("AccountId is null and BankId is also null",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//			}
//			
//		}
//		catch(Exception ex)
//		{
//			if(logger.isErrorEnabled())
//			{
//				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
//			}
//			ex.printStackTrace();
//		}

		/**
		 * ------------------------End of Change------------------------------
		 */

		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AcctToCashInfoCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AcctToCashInfoCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(recepientWalkinCNIC,validationErrors,"Recipient CNIC");
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
	//	validationErrors = ValidatorWrapper.doRequired(customerCode,validationErrors,"Customer Code");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
//		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doValidateCNIC(recepientWalkinCNIC,validationErrors,"Recipient CNIC");
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}

		if(logger.isDebugEnabled())
		{
			logger.debug("End of AcctToCashInfoCommand.validate()");
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
			logger.debug("Start of AcctToCashInfoCommand.toXML()");
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
			
			
			
			
			
			
			/*if(UtilityCompanyEnum.contains(productId)){
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
			}else{*/
				strBuilder .append( p.responseXML() );
//			}
				
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
			logger.debug("End of AcctToCashInfoCommand.toXML()");
		}
		return strBuilder.toString();
	}
	
}
