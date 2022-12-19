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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class SearchTransactionCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String txCode;
	protected String deviceTypeId;
	FetchTransactionListViewModel fetchTransactionListViewModel;

	protected final Log logger = LogFactory.getLog(SearchTransactionCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SearchTransactionCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
			ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationErrors.hasValidationErrors())
			{
				fetchTransactionListViewModel = new FetchTransactionListViewModel();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				fetchTransactionListViewModel.setTranCode(txCode);
							
				if(appUserModel.getCustomerId() != null)
				{
					fetchTransactionListViewModel.setCustomerId(appUserModel.getCustomerId());
				}
				else if(appUserModel.getRetailerContactId() != null)
				{
					fetchTransactionListViewModel.setFromRetContactId(appUserModel.getRetailerContactId());
				}
				else if(appUserModel.getDistributorContactId() != null)
				{
					fetchTransactionListViewModel.setFromDistContactId(appUserModel.getDistributorContactId());
				}
				else
				{
					throw new CommandException(this.getMessageSource().getMessage("transactionsCommand.appUserTypeDoesNotExist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				searchBaseWrapper.setBasePersistableModel(fetchTransactionListViewModel);
				searchBaseWrapper = commonCommandManager.searchTransaction(searchBaseWrapper);
				List<FetchTransactionListViewModel> list = searchBaseWrapper.getCustomList().getResultsetList();
				if(list != null && list.size() > 0)
				{
					fetchTransactionListViewModel = list.get(0);
				}
				else
				{
					throw new CommandException(this.getMessageSource().getMessage("searchTransactionCommand.invalidTransactionCode", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());				
				}
			}
			else
			{
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("fetchTransactionsCommand.generalErrorOccured", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("fetchTransactionsCommand.generalErrorOccured", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SearchTransactionCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SearchTransactionCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		txCode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SearchTransactionCommand.prepare()");
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
			logger.debug("Start of SearchTransactionCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(txCode,validationErrors,"Tx Code");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SearchTransactionCommand.validate()");
		}
		return validationErrors;
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SearchTransactionCommand.toXML()");
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
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getTranCode())) 
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_MOB_NO)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getNotificationMobileNo()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TYPE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(fetchTransactionListViewModel.getTransactionTypeId())
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(fetchTransactionListViewModel.getTranDate())
			.append(TAG_SYMBOL_QUOTE)
						
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(PortalDateUtils.formatDate(fetchTransactionListViewModel.getTranDate(), PortalDateUtils.LONG_DATE_FORMAT))
			//.append(Formatter.formatDate(fetchTransactionListViewModel.getTranDate()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TIMEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatTime(fetchTransactionListViewModel.getTranDate()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PAYMENT_MODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getName()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_PRODUCT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getProductName()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_SUPPLIER)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getSupplierName()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_BANK_RESPONSE_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getBankResponseCode()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_FORMATED_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(fetchTransactionListViewModel.getTranAmount()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithZero(fetchTransactionListViewModel.getTranAmount()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_HELPLINE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(fetchTransactionListViewModel.getHelpLine()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			
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
			logger.debug("End of SearchTransactionCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
