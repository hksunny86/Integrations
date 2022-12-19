package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_CVV;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_ID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_IS_DEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_MOD;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_MPIN;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_NICK;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_P2P_KEY;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_PGP_KEY;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_PN_CH_REQ;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_TPIN;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_ID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_IS_BANK;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_IS_BANK_PIN_REQ;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_BANK_NAME;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PIN_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_ACC;
import static com.inov8.microbank.common.util.XMLConstants.TAG_BANK;
import static com.inov8.microbank.common.util.XMLConstants.TAG_BANKS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.FinancialInstitutionConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class GetBankAccountInfoCommand extends BaseCommand
{
	protected AppUserModel appUserModel;
	protected String deviceTypeId;
	protected String appVersionNo;
	List<SmAcctInfoListViewModel> smAcctInfoList;
	boolean toXMLFlag = false;

	protected final Log logger = LogFactory.getLog(GetBankAccountInfoCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetBankAccountInfoCommand.execute()");
		}
		
//		if(appUserModel.getCustomerId() != null)
		{
			toXMLFlag = true;
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			try
			{
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationError.hasValidationErrors())
				{
					if(appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.HANDLER))
					{
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						HandlerModel handlerModel = new HandlerModel();
						handlerModel.setHandlerId(appUserModel.getHandlerId());
						baseWrapper.setBasePersistableModel(handlerModel);
						this.getCommonCommandManager().loadHandler(baseWrapper);
						handlerModel = (HandlerModel)baseWrapper.getBasePersistableModel();
						this.appUserModel = this.getCommonCommandManager().loadAppUserByRetailerContractId(handlerModel.getRetailerContactId());
					}
//					LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap();
					SmAcctInfoListViewModel sMAcctInfoListViewModel = new SmAcctInfoListViewModel();
					
					
					if( appUserModel.getCustomerId() != null  )
						sMAcctInfoListViewModel.setCustomerId(appUserModel.getCustomerId());
					else if( appUserModel.getRetailerContactId() != null  )
						sMAcctInfoListViewModel.setRetailerContactId(appUserModel.getRetailerContactId());
					else if( appUserModel.getDistributorContactId() != null  )
						sMAcctInfoListViewModel.setDistributorContactId(appUserModel.getDistributorContactId());
					
					sMAcctInfoListViewModel.setDefAccount(Boolean.TRUE);
//					sortingOrderMap.put("name", SortingOrder.ASC);
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					searchBaseWrapper.setBasePersistableModel(sMAcctInfoListViewModel);
//					searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
					searchBaseWrapper = commonCommandManager.loadSmartMoneyAccountInfo(searchBaseWrapper);
					smAcctInfoList = searchBaseWrapper.getCustomList().getResultsetList();
					
					if( appUserModel.getRetailerContactId() != null || appUserModel.getDistributorContactId() != null )
					{
						Iterator<SmAcctInfoListViewModel> iterator = smAcctInfoList.iterator() ;
						
						while( iterator.hasNext() )
						{
							SmAcctInfoListViewModel smAccount = iterator.next() ;
							
							if( smAccount.getFinancialIntegrationId().longValue() == FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION.longValue() )
							{
//								iterator.remove();
							}							
						}						
					}
					
				}
				else
				{
					throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetBankAccountInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetBankAccountInfoCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		appVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_VER);
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetBankAccountInfoCommand.prepare()");
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
			logger.debug("Start of GetBankAccountInfoCommand.validate()");
		}
		
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetBankAccountInfoCommand.validate()");
		}
		return validationErrors;
	}

	private String toXML()
	{
		SortedMap<String, List> smAcctInfoListViewModelMap = new TreeMap<String, List>( preprocessData(smAcctInfoList) );
		boolean firstAccount = true ;
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetBankAccountInfoCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		
		if(toXMLFlag)
		{
			Set<String> set = smAcctInfoListViewModelMap.keySet();			
			Iterator<String> smAcctInfoListViewModelIte = set.iterator() ;
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_BANKS)
			.append(TAG_SYMBOL_CLOSE);
			
			while( smAcctInfoListViewModelIte.hasNext() )
			{
				String keyCode = smAcctInfoListViewModelIte.next() ;				
				List<SmAcctInfoListViewModel> accountsList = smAcctInfoListViewModelMap.get(keyCode) ;
				Iterator<SmAcctInfoListViewModel> accountsIte = accountsList.iterator() ;
				
				firstAccount = true ;
				
				while( accountsIte.hasNext() )
				{
					SmAcctInfoListViewModel localSmAcctInfoListViewModel = accountsIte.next() ;
					
					if( firstAccount )
					{
						/**********/
						strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_BANK)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_BANK_ID)
						
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(localSmAcctInfoListViewModel.getBankId())// need to be changed
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						
						.append(ATTR_IS_BANK)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsBank()))// need to be changed
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE);
						
						
						
						
						strBuilder.append(ATTR_PARAM_BANK_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(localSmAcctInfoListViewModel.getBankName())// need to be changed
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						
						.append(ATTR_PIN_LEVEL)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getPinLevel()))// need to be changed
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						
						.append(ATTR_ACC_PGP_KEY)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(localSmAcctInfoListViewModel.getKey())// need to be changed
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE);
						
						
						/*if( localSmAcctInfoListViewModel.getBankName().toUpperCase().indexOf("OLA") != -1 || localSmAcctInfoListViewModel.getBankName().toUpperCase().indexOf("BANK") != -1 ||  
								localSmAcctInfoListViewModel.getBankName().toUpperCase().indexOf("KASB") != -1
								|| localSmAcctInfoListViewModel.getBankName().toUpperCase().indexOf("Bank Account") != -1)
						{
							strBuilder.append(ATTR_ACC_P2P_KEY)
							.append(TAG_SYMBOL_EQUAL)
							.append(TAG_SYMBOL_QUOTE)
							.append(1)// need to be changed
							.append(TAG_SYMBOL_QUOTE)
							.append(TAG_SYMBOL_SPACE) ;
						}*/
						
						strBuilder.append(ATTR_ACC_MOD)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(localSmAcctInfoListViewModel.getMod())// need to be changed
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						
						.append(TAG_SYMBOL_CLOSE);
						
						
						//Fetch the BANK accounts if retailer or distributor is going to login
						if( (appUserModel.getRetailerContactId() != null || appUserModel.getDistributorContactId() != null) 
								&& localSmAcctInfoListViewModel.getIsBank() )
						{
							try
							{
								BaseWrapper baseWrapper = new BaseWrapperImpl();
								baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
								baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, localSmAcctInfoListViewModel.getBankId());
								baseWrapper.putObject(CommandFieldConstants.KEY_APP_VER, appVersionNo);
								
								String accountsFromBankXML = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CUSTOMER_ACCOUNT_RELATIONSHIP_INQUIRY);
								
								
								strBuilder.append(accountsFromBankXML);
							}
							catch(Exception e)
							{
								e.printStackTrace();							
							}
						}
						
						/**********************************/
						
						firstAccount = false ;						
					}

					if( !localSmAcctInfoListViewModel.getIsBank() )
					{
						strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_ACC)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_ID)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(localSmAcctInfoListViewModel.getSmartMoneyAccountId())
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_NICK)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(escapeXml(localSmAcctInfoListViewModel.getName()))
						.append(TAG_SYMBOL_QUOTE);
					
					
						if(localSmAcctInfoListViewModel.getVeriflyId() != null && 
								localSmAcctInfoListViewModel.getFinancialIntegrationId().longValue() != FinancialInstitutionConstants.PHOENIX_FINANCIAL_INSTITUTION.longValue() )
						{
							strBuilder.append(TAG_SYMBOL_SPACE)							
							
							.append(ATTR_IS_BANK_PIN_REQ)
							.append(TAG_SYMBOL_EQUAL)
							.append(TAG_SYMBOL_QUOTE)
							.append(this.convertBooleanToBit(true))
							.append(TAG_SYMBOL_QUOTE)
							
							.append(TAG_SYMBOL_SPACE);							
						}
						else
						{
							strBuilder.append(TAG_SYMBOL_SPACE)
							
							.append(ATTR_IS_BANK_PIN_REQ)
							.append(TAG_SYMBOL_EQUAL)
							.append(TAG_SYMBOL_QUOTE)
							.append(this.convertBooleanToBit(false))
							.append(TAG_SYMBOL_QUOTE);
						}
							strBuilder.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_IS_DEF)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getDefAccount()))
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_PN_CH_REQ)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getChangePinRequired()))
						.append(TAG_SYMBOL_QUOTE)
					
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_CVV)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsCvvRequired()))
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_TPIN)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsTpinRequired()))
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_ACC_MPIN)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsMpinRequired()))
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_SYMBOL_CLOSE);

						
					}
					///////////*****************************
				}
				
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_BANK)
				.append(TAG_SYMBOL_CLOSE);
				
			}
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_BANKS)
			.append(TAG_SYMBOL_CLOSE);
			
		}
				
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetBankAccountInfoCommand.toXML()");
		}
		return strBuilder.toString();
	}
	
	/**
	 * This method will group the accounts on the basis of Bank .... place them in a hashmap for each bank
	 * @param smAcctInfoList
	 * @return
	 */
	private HashMap<String, List> preprocessData(List<SmAcctInfoListViewModel> smAcctInfoList)
	{
		if(null != smAcctInfoList && !smAcctInfoList.isEmpty())
		{
			Iterator<SmAcctInfoListViewModel> smAcctInfoListIte = smAcctInfoList.iterator() ;
			HashMap<String, List> smAcctInfoListViewModelMap = new HashMap<String, List>();		
		
			while( smAcctInfoListIte.hasNext() )
			{
				SmAcctInfoListViewModel smAcctInfoListViewModel = smAcctInfoListIte.next();
				if( smAcctInfoListViewModelMap.containsKey(smAcctInfoListViewModel.getBankName()))
				{
					List<SmAcctInfoListViewModel> smAcctInfoModelList = smAcctInfoListViewModelMap.get(smAcctInfoListViewModel.getBankName());
					smAcctInfoModelList.add( smAcctInfoListViewModel );
					smAcctInfoListViewModelMap.put(smAcctInfoListViewModel.getBankName(), smAcctInfoModelList);
				}
				else
				{
					List<SmAcctInfoListViewModel> smAcctInfoModelList = new ArrayList<SmAcctInfoListViewModel>();
					smAcctInfoModelList.add( smAcctInfoListViewModel ) ;
					smAcctInfoListViewModelMap.put(smAcctInfoListViewModel.getBankName(), smAcctInfoModelList);				
				}			
			}	
		
		return smAcctInfoListViewModelMap;
		}
		return new HashMap<String, List>();
	}

}
