package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_RESPONSE_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PAYMENT_MODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_HELPLINE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_MOB_NO;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class RetailerToCustomerDiscreteProductSaleCommand extends BaseCommand 
{
	protected String productId;
	protected AppUserModel appUserModel;
	protected String mobileNo;
	protected String deviceTypeId;
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	
	protected final Log logger = LogFactory.getLog(RetailerToCustomerDiscreteProductSaleCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToCustomerDiscreteProductSaleCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		if(appUserModel.getRetailerContactId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationErrors.hasValidationErrors())
				{
					WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
					
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(mobileNo);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.RET_DISC_PRODUCT_SALE_TX);
						
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
					
					commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					transactionModel = workFlowWrapper.getTransactionModel();
					productModel = workFlowWrapper.getProductModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(Exception ex)
			{
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			throw new CommandException(this.getMessageSource().getMessage("retailerToCustomerProductSaleCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToCustomerDiscreteProductSaleCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToCustomerDiscreteProductSaleCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToCustomerDiscreteProductSaleCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToCustomerDiscreteProductSaleCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToCustomerDiscreteProductSaleCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToCustomerDiscreteProductSaleCommand.toXML()");
		}
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_MOB_NO)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TYPE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getTransactionTypeId())
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getCreatedOn())
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
			//.append(Formatter.formatDate(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TIMEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatTime(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_PRODUCT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(productModel.getName()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_SUPPLIER)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(productModel.getSupplierIdSupplierModel().getName()))
			.append(TAG_SYMBOL_QUOTE)
						
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PAYMENT_MODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_BANK_RESPONSE_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getBankResponseCode()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_FORMATED_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(transactionModel.getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithZero(transactionModel.getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE);
			
		if(productModel != null && productModel.getHelpLineNotificationMessageModel().getSmsMessageText() != "") 
		{
			strBuilder.append(TAG_SYMBOL_SPACE)
				.append(ATTR_TRN_HELPLINE)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithEmpty(productModel.getHelpLineNotificationMessageModel().getSmsMessageText()))
				.append(TAG_SYMBOL_QUOTE);		
		}
			
		strBuilder.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToCustomerDiscreteProductSaleCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
