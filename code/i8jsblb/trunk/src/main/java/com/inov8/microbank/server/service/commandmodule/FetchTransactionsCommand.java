package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_RESPONSE_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_ONE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PAYMENT_MODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_HELPLINE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_MOB_NO;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel;
import com.inov8.microbank.common.util.CommandConstants;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class FetchTransactionsCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String startingTxNumber;
	protected String endingTxNumber;
	protected String deviceTypeId;
	List<FetchTransactionListViewModel> list;
	
	protected final Log logger = LogFactory.getLog(FetchTransactionsCommand.class);
	
	@Override
	public void execute() throws CommandException 
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of FetchTransactionsCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		
		try
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors())
			{
				FetchTransactionListViewModel fetchTransactionListViewModel = new FetchTransactionListViewModel();
				
				int pageSize = getPageSize(startingTxNumber, endingTxNumber);
				PagingHelperModel pagingHelperModel = new PagingHelperModel();
				pagingHelperModel.setPageSize(pageSize);
				pagingHelperModel.setPageNo(getPageNo(Integer.parseInt(endingTxNumber),pageSize));
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
				
				LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap();
				sortingOrderMap.put("tranDate", SortingOrder.DESC);
				searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
				
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
				searchBaseWrapper = commonCommandManager.fetchTransactions(searchBaseWrapper);
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
			throw new CommandException(this.getMessageSource().getMessage("fetchTransactionsCommand.generalErrorOccured", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of FetchTransactionsCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of FetchTransactionsCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		startingTxNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ST_TX_NO);
		endingTxNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_END_TX_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of FetchTransactionsCommand.prepare()");
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
			logger.debug("Start of FetchTransactionsCommand.validate()");
		}
		
		if( startingTxNumber == null || startingTxNumber.equals("") )
			startingTxNumber = CommandFieldConstants.DEFAULT_STARTING_TRANS_NO ;
		if( endingTxNumber == null || endingTxNumber.equals("") )
			endingTxNumber = CommandFieldConstants.DEFAULT_ENDING_TRANS_NO ;
			
		validationErrors = ValidatorWrapper.doRequired(startingTxNumber,validationErrors,"Starting Tx Number");
		validationErrors = ValidatorWrapper.doRequired(endingTxNumber,validationErrors,"Ending Tx Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(startingTxNumber,validationErrors,"Starting Tx Number");
			validationErrors = ValidatorWrapper.doNumeric(endingTxNumber,validationErrors,"Ending Tx Number");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doCompareStartEnd(startingTxNumber,endingTxNumber, validationErrors,"Starting Transaction Number","Ending Transaction Number");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of FetchTransactionsCommand.validate()");
		}
		return validationErrors;
	}
	
	
	private int getPageSize(String startingTxNo,String endingTxNo)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of FetchTransactionsCommand.getPageSize()");
		}
		int pageSize;
		if(Integer.parseInt(startingTxNo) == Integer.parseInt(endingTxNo))
		{
			pageSize = 1;
		}
		else if(Integer.parseInt(startingTxNo) == 1)
		{
			pageSize = Integer.parseInt(endingTxNo);
		}
		else
		{
			pageSize = Integer.parseInt(endingTxNo) - Integer.parseInt(startingTxNo);
			pageSize = pageSize + 1;
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of FetchTransactionsCommand.getPageSize()");
		}
		return pageSize; 
	}
	
	
	private int getPageNo(int endingTxNo, int pageSize)
	{
		int pageNo = endingTxNo/pageSize;
		return pageNo;
	}
	
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of FetchTransactionsCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		if(list != null && list.size() > 0)
		{			
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_TRANS)
				.append(TAG_SYMBOL_CLOSE);
					
			for(FetchTransactionListViewModel localFetTranModel:list)
			{	
				strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_TRN)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_CODE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(localFetTranModel.getTranCode())) 
					.append(TAG_SYMBOL_QUOTE)
					

					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_TYPE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(localFetTranModel.getTransactionTypeId())
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_DATE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(localFetTranModel.getTranDate())
					.append(TAG_SYMBOL_QUOTE)
								
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_DATEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(PortalDateUtils.formatDate(localFetTranModel.getTranDate(), PortalDateUtils.SHORT_DATE_FORMAT))
					//.append(Formatter.formatDate(localFetTranModel.getTranDate()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_TIMEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatTime(localFetTranModel.getTranDate()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_PAYMENT_MODE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)					
					.append(escapeXml(replaceNullWithEmpty(localFetTranModel.getName())))
					.append(TAG_SYMBOL_QUOTE);
					
					if( localFetTranModel.getTransactionTypeId().longValue() == TransactionTypeConstantsInterface.RET_CREDIT_RECHARGE_TX.longValue()
							|| localFetTranModel.getTransactionTypeId().longValue() == TransactionTypeConstantsInterface.DIST_CREDIT_RECHARGE_TX.longValue())
					{
						strBuilder
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_TRN_PRODUCT)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandConstants.i8_CREDIT_RECHARGE)
						.append(TAG_SYMBOL_QUOTE);
					}
					else if( localFetTranModel.getTransactionTypeId().longValue() == TransactionTypeConstantsInterface.DIST_TO_DIST_TX.longValue()
							|| localFetTranModel.getTransactionTypeId().longValue() == TransactionTypeConstantsInterface.DIST_TO_RET_TX.longValue()
							|| localFetTranModel.getTransactionTypeId().longValue() == TransactionTypeConstantsInterface.RET_TO_RET_TX.longValue())
					{
						strBuilder
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_TRN_PRODUCT)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandConstants.AGENT_TO_AGENT_TRANSFER_NAME)
						.append(TAG_SYMBOL_QUOTE);
					}
					else
					{
						strBuilder							
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_TRN_PRODUCT)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(replaceNullWithEmpty(localFetTranModel.getProductName()))
						.append(TAG_SYMBOL_QUOTE);
					}
				
				strBuilder
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_SUPPLIER)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(localFetTranModel.getSupplierName()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_BANK_RESPONSE_CODE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(localFetTranModel.getBankResponseCode()))
					.append(TAG_SYMBOL_QUOTE);
					
//					if( localFetTranModel.getSuppResponseCode() != null && !localFetTranModel.getSuppResponseCode().
//							equalsIgnoreCase(""))
//					{
//						strBuilder.append(TAG_SYMBOL_SPACE)
//						.append(ATTR_TRACK_ID)
//						.append(TAG_SYMBOL_EQUAL)
//						.append(TAG_SYMBOL_QUOTE)
//						.append(replaceNullWithEmpty(localFetTranModel.getSuppResponseCode()))
//						.append(TAG_SYMBOL_QUOTE);
//					}
					//else
					{
						strBuilder.append(TAG_SYMBOL_SPACE)
						.append(ATTR_TRN_HELPLINE)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(replaceNullWithEmpty(localFetTranModel.getHelpLine()))
						.append(TAG_SYMBOL_QUOTE);						
					}
					
					strBuilder.append(TAG_SYMBOL_SPACE)
					.append(ATTR_FORMATED_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(localFetTranModel.getTranAmount()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
						.append(ATTR_TRN_MOB_NO)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(replaceNullWithEmpty(localFetTranModel.getNotificationMobileNo()))
						.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithZero(localFetTranModel.getTranAmount()))
					.append(TAG_SYMBOL_QUOTE)
					
					
		
					.append(TAG_SYMBOL_CLOSE)
				
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_TRN)
					.append(TAG_SYMBOL_CLOSE);
			}
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_TRANS)
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
			logger.debug("End of FetchTransactionsCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
