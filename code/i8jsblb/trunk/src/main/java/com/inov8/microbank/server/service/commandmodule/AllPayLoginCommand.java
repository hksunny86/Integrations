package com.inov8.microbank.server.service.commandmodule;

/**
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			December 2008  			
 * Description:				
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.app.vo.AppVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AdsModel;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

import java.util.*;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class AllPayLoginCommand extends BaseCommand
{
	protected String userId;
	protected String pin;
	protected boolean impcr;
	protected String appId;
	protected String appVersionNo;
	protected String catVersionNo;
	protected String deviceTypeId;
	protected String isRooted;

	protected String udid;
	protected String os;
	protected String osVersion;
	protected String model;
	protected String vendor;
	protected String network;
	protected String userType;
	protected String deviceCloudId;

	private BaseWrapper baseWrapper;

	String tickerString;
	String appVersion;
	String bankAccInfo="";
	String catalogDetail;
	String loginResponse = "";
	String applicationName;
	String balanceResponse = "";
	//String lastTransactionInfo = "" ;
	protected int tillBalanceRequired;
	String encryption_type;
//	double balance = 0.0;

	private AccountInfoModel accountInfoModel;

	protected final Log logger = LogFactory.getLog(AllPayLoginCommand.class);

	private Long sTime = DateTimeUtils.currentTimeMillis();

	@Override
	public void execute() throws CommandException
	{
		try
		{
			CommandManager commandManager = this.getCommandManager();

			//verify app version
			if(DeviceTypeConstantsInterface.ALL_PAY.longValue() == new Long(deviceTypeId).longValue()) {

				AppVO appVO = getCommonCommandManager().getAppManager().validateAppVersion(this.baseWrapper);
				appVersion = MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_APP_USAGE_LVL, appVO.getAppUsageLevel());
				appVersion += MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_APP_VER, appVO.getAppVersion());
				baseWrapper.putObject("APP_VERSION", appVersion);
			}

			/*
			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, userId);
			if(!(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == new Long(deviceTypeId).longValue()))
			{
				baseWrapper.putObject(CommandFieldConstants.KEY_APP_VER, appVersionNo);
				baseWrapper.putObject(CommandFieldConstants.KEY_APP_NAME, applicationName);
				baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
				appVersion = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_APP_VER);
				appVersion = removeParams(appVersion);
			}
			*/
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, userId);
			baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);

			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, this.encryption_type);
			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			baseWrapper.putObject(CommandFieldConstants.KEY_IS_ROOTED, isRooted);

			baseWrapper.putObject(CommandFieldConstants.KEY_UDID, udid);
			baseWrapper.putObject(CommandFieldConstants.KEY_OS, os);
			baseWrapper.putObject(CommandFieldConstants.KEY_OS_VERSION, osVersion);
			baseWrapper.putObject(CommandFieldConstants.KEY_MODEL, model);
			baseWrapper.putObject(CommandFieldConstants.KEY_VENDOR, vendor);
			baseWrapper.putObject(CommandFieldConstants.KEY_NETWORK, network);
			baseWrapper.putObject(CommandFieldConstants.KEY_USER_TYPE, userType);
			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_CLOUD_ID, deviceCloudId);

			if(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() != new Long(deviceTypeId).longValue()) {	//skip login for Agent Web Application
				Long sTime = DateTimeUtils.currentTimeMillis();
				loginResponse = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_LOGIN);
				logger.info("LoginCommand response in AllPayLoginCommand.execute() :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - sTime));
				loginResponse = removeParams(loginResponse);
			}

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);

			if((DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() != new Long(deviceTypeId).longValue()))
			{
				tickerString = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_GT_TK_STR);
				tickerString = removeParams(tickerString);
			}



//				if((DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == new Long(deviceTypeId).longValue()))
//				{
//					baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
//				}
//				else
//				{
//					baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
//				}
			bankAccInfo = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_GT_ACC_INFO);


			// Check added for not to load catalog
			if(!(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == new Long(deviceTypeId).longValue()) && DeviceTypeConstantsInterface.ATM != Long.parseLong(deviceTypeId))
			{
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
				baseWrapper.putObject(CommandFieldConstants.KEY_CAT_VER, catVersionNo);
				catalogDetail = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_UPD_CAT);
			}

			// To get the information of last transaction
			/*baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			baseWrapper.putObject(CommandFieldConstants.KEY_ST_TX_NO, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_END_TX_NO, 1);*/
			/*lastTransactionInfo = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_GT_TRANS);

			if( lastTransactionInfo.indexOf("No transaction(s) found.") >= 0 )
				lastTransactionInfo = "" ;*/

			accountInfoModel = this.getAccountInfoModel();
			baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL,accountInfoModel);
			if(DeviceTypeConstantsInterface.ALL_PAY.longValue() == new Long(deviceTypeId).longValue()) {//Check for mobile app
				/*
				 *  Coded added by Jawwad ..
				 *  Following code gets the current OLA balance of the Retailer/Customer
				 */

				SmartMoneyAccountModel sma = getDefaultOLAAccount();

				if( sma != null )
					logger.info( "SMA account id : " + sma.getPrimaryKey() );

				if( sma != null )
				{
					try
					{
						this.impcr = sma.getChangePinRequired();
						baseWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, sma);
						baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
						if(UserTypeConstantsInterface.HANDLER.longValue() != ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue()) {
							balanceResponse = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_BAL);
							balanceResponse = removeParams(balanceResponse); // TODO ACCOUNT BALANCE
						}
					}
					catch (FrameworkCheckedException ex)
					{
						if(ex.getMessage().equalsIgnoreCase("Record does not exist."))
						{
							logger.error("Exception in AllPayLoginCommand.execute() :: " + ex);
						}
					}
				}
			}
			else
			{
				this.checkTillBalanceRequired(getCommonCommandManager());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			if(ex instanceof CommandException)
			{
				CommandException cmdEx = (CommandException)ex;
				throw cmdEx;
			}
			else
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayLoginCommand.execute()");
		}
	}

	private BaseWrapper checkTillBalanceRequired(CommonCommandManager commonCommandManager) throws CommandException
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AgentOpeningBalModel agentOpeningBalModel = new AgentOpeningBalModel();

		logger.info("AllPayLoginCommand.checkTillBalanceRequired() userId : "+userId +" "+calendar.getTime());

		agentOpeningBalModel.setAgentId(userId);
		agentOpeningBalModel.setBalDate(calendar.getTime());
		searchBaseWrapper.setBasePersistableModel(agentOpeningBalModel);

		try
		{
			searchBaseWrapper = commonCommandManager.checkTillBalanceRequired(searchBaseWrapper);
			if(null != searchBaseWrapper.getCustomList() && null != searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() >0)
			{
				List<AgentOpeningBalModel> list = searchBaseWrapper.getCustomList().getResultsetList();

				agentOpeningBalModel = list.get(0);

				if(agentOpeningBalModel != null && agentOpeningBalModel.getAgentId().equals(userId)) {

					if(null == agentOpeningBalModel.getOpeningAccountBalance())
					{
						switchWrapper = commonCommandManager.checkAgentBalance();
						double balance = switchWrapper.getBalance();
						agentOpeningBalModel.setOpeningAccountBalance(balance);
						agentOpeningBalModel.setRunningAccountBalance(balance);
						if(null != switchWrapper.getAccountInfoModel() && null == agentOpeningBalModel.getAccountNumber())
						{
							agentOpeningBalModel.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
						}
						baseWrapper.setBasePersistableModel(agentOpeningBalModel);
						baseWrapper = commonCommandManager.saveTillBalance(baseWrapper);
						tillBalanceRequired = 0;

					}
					else
					{
						tillBalanceRequired = 0;
					}
				} else {
					tillBalanceRequired = 1;
				}
			}
			else
			{
				tillBalanceRequired = 1;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CommandException("Your Core Banking Account cannot be accessed at the moment. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, e);
		}
		return baseWrapper;
	}

	private AccountInfoModel getAccountInfoModel() throws FrameworkCheckedException
	{
		AccountInfoModel accountInfoModel = null;
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		if(appUserModel != null)
		{
			Long customerId;
			if(appUserModel.getCustomerId() != null)
				customerId =  appUserModel.getCustomerId();
			else
				customerId = appUserModel.getAppUserId();

			try {

				accountInfoModel = commonCommandManager.getAccountInfoModel(customerId,PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("Error in AllpayLoginCommenad.getAccountiNfoModel() :: " + e);
			}
		}
		return accountInfoModel;
	}

	private SmartMoneyAccountModel getDefaultOLAAccount() throws FrameworkCheckedException
	{
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;

		Long customerId = ThreadLocalAppUser.getAppUserModel().getCustomerId() ;
		Long retailerContactId = ThreadLocalAppUser.getAppUserModel().getRetailerContactId() ;
		Long distributorContactId = ThreadLocalAppUser.getAppUserModel().getDistributorContactId() ;
		Long handlerId = ThreadLocalAppUser.getAppUserModel().getHandlerId();

		if( customerId != null )
			sma.setCustomerId(customerId);
		else if( retailerContactId != null )
			sma.setRetailerContactId(retailerContactId);
		else if( distributorContactId != null )
			sma.setDistributorContactId(distributorContactId);
		else if (handlerId != null && ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {
			sma.setHandlerId(handlerId);
		}

		if(handlerId != null && handlerId > 0) {

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(sma);
			searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

			if(searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() == 1) {
				sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
			}

		} else if(customerId != null && customerId > 0){
			sma.setDefAccount(true);
			sma.setDeleted(false);

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(sma);
			searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
			if(searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() >= 1) {
				sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
			}
		} else {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(sma) ;
			sma = (SmartMoneyAccountModel)this.getCommonCommandManager().loadOLASmartMoneyAccount(baseWrapper).getBasePersistableModel() ;
		}


		return sma;
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		this.baseWrapper = baseWrapper;
		userId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		appId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_ID);
		appVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_VER);
		catVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAT_VER);
		applicationName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_NAME);
		isRooted = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_ROOTED);

		udid = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_UDID);
		os = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OS);
		osVersion = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OS_VERSION);
		model = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MODEL);
		vendor = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_VENDOR);
		network = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NETWORK);
		userType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_USER_TYPE);
		deviceCloudId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_CLOUD_ID);

		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayLoginCommand.prepare()");
		}
	}

	@Override
	public String response()
	{
		return toXML();
	}

	@Override
	public void doValidate() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayLoginCommand.doValidate()");
		}
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(userId,validationErrors,"User Id");

		if(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() != new Long(deviceTypeId).longValue()) {
			ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
			ValidatorWrapper.doRequired(userType,validationErrors,"User Type");
//			App Id Mandatory check removed - for empty App Id Force app download is required
//			if(!StringUtil.isNullOrEmpty(userType) && userType.equals(UserTypeConstantsInterface.CUSTOMER.toString()) ){
//				ValidatorWrapper.doRequired(appId,validationErrors,"Application Id");
//			}
//			ValidatorWrapper.doRequired(appId,validationErrors,"Application Id");
			ValidatorWrapper.doRequired(appVersionNo,validationErrors,"Application Version No");
			ValidatorWrapper.doRequired(catVersionNo,validationErrors,"Catalog Version No");
			ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");
		}

		if(!validationErrors.hasValidationErrors() && !(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == new Long(deviceTypeId).longValue()))
		{
			byte enc_type = new Byte(encryption_type).byteValue();
			pin = this.decryptPin(pin, enc_type);
		}

		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(userId,validationErrors,"User Id");
		}

		if(!validationErrors.hasValidationErrors() && !StringUtil.isNullOrEmpty(appId) ){
			if(appId.equals(CommandConstants.CONSUMER_APP)) {
				validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.KEY_MOBILE_LENGTH,validationErrors,"Mobile Number");
			}else if(appId.equals(CommandConstants.AGENT_MATE)){
				validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.ALLPAY_USERID_LENGTH,validationErrors,"User Id");
			}
		}

		if(!validationErrors.hasValidationErrors() && !(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == new Long(deviceTypeId).longValue()))
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}

		if(validationErrors.hasValidationErrors())
		{

			throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayLoginCommand.doValidate() ");
		}
	}


	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayLoginCommand.toXML()");
		}
		String mobileNumberParam = CommandFieldConstants.KEY_AGENT_MOB_NO;
		String isMigrated = "1";
		if(accountInfoModel != null && accountInfoModel.getIsMigrated() != null)
			isMigrated = accountInfoModel.getIsMigrated().toString();
		if(UserTypeConstantsInterface.CUSTOMER.longValue() == ThreadLocalAppUser.getAppUserModel().getAppUserTypeId()){
			mobileNumberParam = CommandFieldConstants.KEY_MOB_NO;
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE)
				.append(loginResponse)
				.append(tickerString)
				.append(appVersion)
				.append(balanceResponse)
				/*Adding parameter for till balance report functionality*/
				/*.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_BAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append("483335.0")	// TODO remove hardcode.
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)*/
				/*End Adding parameter for till balance functionality*/


				/*.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_FORMATED_BAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(Formatter.formatNumbers(483335.0))	// TODO remove hardcode.
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE)*/

				.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE)
				.append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(CommandFieldConstants.KEY_IS_MPIN_CHNG_REQ)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				.append(convertBooleanToBit(this.impcr))
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
				.append(mobileNumberParam)
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE)
				.append(ThreadLocalAppUser.getAppUserModel().getMobileNo())
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)

				.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ADTYPE, MessageUtil.getMessage("adType")))
				.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VIDEO_LINKS, MessageUtil.getMessage("videoLink")))

				.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
				.append("IS_MIGRATED")
				.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
				.append(isMigrated).append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)

				.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
				.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
				.append("CNIC")
				.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
				.append(ThreadLocalAppUser.getAppUserModel().getNic()).append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
				.append(TAG_SYMBOL_CLOSE)

				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE);

		if(!(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == new Long(deviceTypeId).longValue()))
		{
			strBuilder.append(catalogDetail);
		}
		strBuilder.append(bankAccInfo);
		//.append(lastTransactionInfo);
		strBuilder.append(prepareAdsXml());

		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayLoginCommand.toXML()");
		}

		logger.info("Total Time taken by AllPayLoginCommand :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - sTime));

		return strBuilder.toString();
	}

	private String prepareAdsXml() {
		StringBuilder adsXml = new StringBuilder();
		try {
			if (MessageUtil.getBooleanMessage("adsRequired")) {
				AdsModel adsModel = new AdsModel();
				adsModel.setActive(Boolean.TRUE);
				List<AdsModel> adsList = commonCommandManager.findAds(adsModel);///////only show home ads

				String ads = prepareAdsXmlString(adsList);
				if (!ads.equals("")) {
					adsXml.append(ads);
				}
			}
		}catch(Exception e){
			logger.error("Error while preparing ads XML... ", e);
		}
		return adsXml.toString();
	}

	private String prepareAdsXmlString(List<AdsModel> adsList) {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ADS, true));

		Map<String, Object> attributes;
		for (AdsModel adModel : adsList) {
			attributes = new LinkedHashMap<>();
			attributes.put(XMLConstants.ATTR_NAME, adModel.getImageName());
			attributes.put(XMLConstants.ATTR_TYPE, StringEscapeUtils.escapeXml(adModel.getAdLocationId().toString()));
			strBuilder.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_AD, attributes, true));
		}

		strBuilder.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ADS, false));

		return strBuilder.toString();
	}

	private String removeParams(String xmlStr)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayLoginCommand.removeParams()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE);
		if(xmlStr != null && !xmlStr.equals("") && xmlStr.contains(strBuilder.toString()))
		{
			xmlStr = xmlStr.replaceAll(strBuilder.toString(), "");
			StringBuilder strBuilderParams = new StringBuilder();
			strBuilderParams.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAMS)
					.append(TAG_SYMBOL_CLOSE);
			xmlStr = xmlStr.replaceAll(strBuilderParams.toString(), "");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayLoginCommand.removeParams()");
		}
		return xmlStr;
	}
	public String convertBooleanToBit(boolean convertableFlag)
	{
		String convertedFlag;
		if(convertableFlag)
		{
			convertedFlag = "1";
		}
		else
		{
			convertedFlag = "0";
		}
		return convertedFlag;
	}

}
