package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_ONE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.transactionmodule.SalesSummaryListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class SalesTransactionsCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String deviceTypeId;
	List<SalesSummaryListViewModel> list;
	
	protected final Log logger = LogFactory.getLog(SalesTransactionsCommand.class);
	
	@Override
	public void execute() throws CommandException 
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SalesTransactionsCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		
		try
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors())
			{
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
				
				SalesSummaryListViewModel salesSummaryListViewModel = new SalesSummaryListViewModel();
				
				salesSummaryListViewModel.setTransactionDate(PortalDateUtils.formatDate(new Date(), "dd/MM/yyyy"));
				salesSummaryListViewModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
				
				searchBaseWrapper.setBasePersistableModel(salesSummaryListViewModel);
				searchBaseWrapper = commonCommandManager.fetchSalesTransactions(searchBaseWrapper);
				list = searchBaseWrapper.getCustomList().getResultsetList();
			}
			else
			{
				throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("fetchTransactionsCommand.generalErrorOccured", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new CommandException(this.getMessageSource().getMessage("fetchTransactionsCommand.generalErrorOccured", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SalesTransactionsCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SalesTransactionsCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();		
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SalesTransactionsCommand.prepare()");
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
			logger.debug("Start of SalesTransactionsCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SalesTransactionsCommand.validate()");
		}
		return validationErrors;
	}
	
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SalesTransactionsCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		if(list != null && list.size() > 0)
		{			
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_TRANS)
				.append(TAG_SYMBOL_CLOSE);
				
			Double sumOfAmounts = 0d ;
			
			for(SalesSummaryListViewModel localSalesSummaryListViewModel:list)
			{	
				strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_TRN)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_PRODUCT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(localSalesSummaryListViewModel.getName()))
					.append(TAG_SYMBOL_QUOTE)
					
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_FORMATED_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(localSalesSummaryListViewModel.getSalesAmount()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithZero(localSalesSummaryListViewModel.getSalesAmount()))
					.append(TAG_SYMBOL_QUOTE)
				
					.append(TAG_SYMBOL_CLOSE)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_TRN)
					.append(TAG_SYMBOL_CLOSE);
				
				
				sumOfAmounts += localSalesSummaryListViewModel.getSalesAmount() ;
					
			}
			
			
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_TRANS)
				.append(TAG_SYMBOL_CLOSE);
			
			
			
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(ATTR_TRN_DATEF)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append( PortalDateUtils.formatDate(new Date(), PortalDateUtils.SHORT_DATE_FORMAT) )
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
			.append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(Formatter.formatNumbers(sumOfAmounts))
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
			.append(CommandFieldConstants.KEY_TOTAL_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(replaceNullWithZero(sumOfAmounts))
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
			
			
		}
		else
		{
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_MESGS)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_MESG)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_LEVEL)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(ATTR_LEVEL_ONE)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				.append(this.getMessageSource().getMessage("fetchTransactionsCommand.noTransactionAvailable", null,null))
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_MESG)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_MESGS)
				.append(TAG_SYMBOL_CLOSE);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SalesTransactionsCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
