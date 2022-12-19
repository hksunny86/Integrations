/**
 *
 */
package com.inov8.microbank.mfs;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeCommandModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.util.MfsRequestWrapper.MfsRequestInformation;
import com.inov8.microbank.demographics.model.DemographicsModel;
import com.inov8.microbank.mfs.jme.messaging.parser.MessageParser;
import com.inov8.microbank.mfs.jme.messaging.parser.ParsingException;
import com.inov8.microbank.mfs.jme.messaging.parser.XmlParserFactory;
import com.inov8.microbank.mfs.jme.model.Message;
import com.inov8.microbank.server.dao.handlermodule.HandlerDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeCommandManager;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import static com.inov8.microbank.common.util.CommandFieldConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * @author imran.sarwar Creation Time: Oct 6, 2006 3:55:10 PM
 */
public class MfsRequestHandler
{

	/** Logger for this class */
	protected final Log logger = LogFactory.getLog(getClass());
	protected CommandManager cmdManager = null; // new MockCommandManager();
	private DeviceTypeCommandManager deviceTypeCommandManager;
	private ActionLogManager actionLogManager;
	private MessageSource messageSource;
	private boolean canResponseBeNull = false;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

	//************************************************************************
	private ProductCatalogManager productCatalogManager;
	private HandlerDAO handlerDAO;
	//************************************************************************

	@SuppressWarnings("unchecked")
	public String handleRequest(String xml, MfsRequestWrapper sessionWrapper)
	{
		logger.info("Start of MfsRequestHandler.handleRequest()....");
		String retXML = null;
		String curCmd = "";
		String deviceTypeId = "";
		String fingerTemplate = "";
		ActionLogModel actionLogModel = new ActionLogModel();
		AppUserModel userModel = null;
		UserDeviceAccountsModel userDeviceAccountsModel = null;
		boolean commandDeviceTypeValidityFlag = false;
		boolean isDoubleSubmission = false;
		MfsRequestInformation requestInformation = null;
		// TODO: log here
		logger.info("Before Action Logging in MfsRequestHandler.handleRequest()....");
		try
		{
			//Insert ActionLog in the DB
			MessageParser parser = XmlParserFactory.getParser(xml);
			BaseWrapper wrapper = parser.parse(xml);
//			actionLogModel.setInputXml(StringUtil.replacePinWithAsterics(xml));

//			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
			curCmd = (String) wrapper.getObject(KEY_CURR_COMMAND);

//			if(curCmd != null && (curCmd.equals("252") || curCmd.equals("229"))) {
//				fingerTemplate = (String) wrapper.getObject(KEY_FINGER_TEMPLATE);
//				fingerTemplate = "*****";
//				logger.info("Masked Finger Template: " +fingerTemplate);
//				wrapper.putObject(KEY_FINGER_TEMPLATE, fingerTemplate);
//
//				xml = StringUtils.replace(xml, StringUtils.substringBetween(xml.substring
//						(xml.indexOf(CommandFieldConstants.KEY_FINGER_TEMPLATE)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN), fingerTemplate);
//				}

			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
			actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
			actionLogModel.setStartTime(new Timestamp( new java.util.Date().getTime() ));
			actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
			if(actionLogModel.getActionLogId() != null)
			{
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			}

			//ActionLog Inserted in the DB9

//			MessageParser parser = XmlParserFactory.getParser(xml);
//			BaseWrapper wrapper = parser.parse(xml);

			requestInformation = isDoubleSubmission(curCmd, (String)wrapper.getObject(ATTR_REQ_TIME), sessionWrapper);
			if(null != requestInformation)
				isDoubleSubmission = requestInformation.isRepeatingRequest();
			
			if(!isDoubleSubmission)
			{
				if(wrapper.getObject(KEY_DEVICE_TYPE_ID) != null)
					deviceTypeId = (String) wrapper.getObject(KEY_DEVICE_TYPE_ID);

				ValidationErrors validationErrors = new ValidationErrors();
				validationErrors = ValidatorWrapper.doRequired(curCmd, validationErrors, "Command Id");
				if (!validationErrors.hasValidationErrors())
					validationErrors = ValidatorWrapper.doInteger(curCmd, validationErrors, "Command Id");

				validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type Id");
				if (!validationErrors.hasValidationErrors())
					validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type Id");

				if (validationErrors.hasValidationErrors())
				{
					retXML = prepareErrorMessage(new Message(ErrorCodes.UNKNOWN_ERROR, ErrorLevel.HIGH, validationErrors
							.getStringBuilder().toString()));
				}

				DeviceTypeCommandModel deviceTypeCommandModel = new DeviceTypeCommandModel();

				if (retXML == null)
				{
					deviceTypeCommandModel.setDeviceTypeId(new Long(deviceTypeId).longValue());
					deviceTypeCommandModel.setCommandId(new Long(curCmd).longValue());
					wrapper.setBasePersistableModel(deviceTypeCommandModel);
					CustomList<DeviceTypeCommandModel> customList = deviceTypeCommandManager.loadDeviceTypeCommand(wrapper);
					if (null != customList)
					{
						List<DeviceTypeCommandModel> list = customList.getResultsetList();
						if (list.isEmpty())
							retXML = prepareErrorMessage(new Message(ErrorCodes.UNKNOWN_ERROR, ErrorLevel.HIGH,
									this.messageSource.getMessage("mfsRequestHandler.invalidCommand", null,null)));
						else
							deviceTypeCommandModel = list.get(0);
					}
				}


				if(retXML == null){
					commandDeviceTypeValidityFlag = true;
				}

				// Get the appUserModel set by the MfsSupportFilter. In case
				// of login this will be null

				userModel = ThreadLocalAppUser.getAppUserModel();
				userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
				if (retXML == null){
					if (deviceTypeCommandModel.getLoginRequired() && userModel == null)
//						retXML = prepareErrorMessage(new Message(ErrorCodes.INVALID_LOGIN, ErrorLevel.HIGH,
//								"Please login to access this feature."));
						retXML = prepareErrorMessage(new Message(ErrorCodes.INVALID_LOGIN, ErrorLevel.HIGH,
								this.messageSource.getMessage("mfsRequestHandler.sessionExpired", null,null)));
						
				}

				//********************************************************************************************************
				if(retXML == null) {
					int curCommandId = Integer.parseInt(curCmd);
					if(userDeviceAccountsModel != null
							&& curCommandId != Integer.parseInt(RECEIVE_CASH_COMMAND_COMMAND)
							&& curCommandId != Integer.parseInt(CommandFieldConstants.CMD_VERIFY_PIN)
							&& curCommandId != Integer.parseInt(CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_LEG2)
							&& curCommandId != Integer.parseInt(CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_LEG2_INFO)
					) {
						retXML = checkCurrentProductExistInCatalog(curCmd, xml, retXML, userDeviceAccountsModel);
					}

					if(deviceTypeCommandModel.getLoginRequired() && userModel == null)
						retXML = prepareErrorMessage(new Message(ErrorCodes.INVALID_LOGIN, ErrorLevel.HIGH, this.messageSource.getMessage("mfsRequestHandler.sessionExpired", null, null)));

				}
				//********************************************************************************************************
				if (retXML == null)
				{

					if(Integer.parseInt(curCmd) == Integer.parseInt(CMD_MFS_LOGIN) || Integer.parseInt(curCmd) == Integer.parseInt(CommandFieldConstants.CMD_ALL_PAY_LOGIN)) {  

						String UID = null;
						if(wrapper.getObject(CommandFieldConstants.KEY_U_ID) != null)
							UID = (String)wrapper.getObject(CommandFieldConstants.KEY_U_ID);
						if(StringUtil.isNullOrEmpty(UID)) {
							logger.error("USER ID "+UID);
							throw new CommandException(messageSource.getMessage("agentRegisterationNotApproved", null, null), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
						}
					}
					if(curCmd != null && curCmd.equals("33")){
                        String UDID = null;
                        if(wrapper.getObject(CommandFieldConstants.KEY_UDID) != null)
                            UDID = (String) wrapper.getObject(CommandFieldConstants.KEY_UDID);
						MfsRequestInformation uDidInformation = new MfsRequestInformation();
						uDidInformation.setUserDeviceId(UDID);
						sessionWrapper.setRequestInformation("DEMO_MAP",uDidInformation);
					}
					else if(curCmd != null && !curCmd.equals("128") && !curCmd.equals("178")
							&& ThreadLocalAppUser.getAppUserModel() != null && sessionWrapper.getRequestInformation("DEMO_MAP") != null){
						//Concurrent Session Work Around
						String UDID = sessionWrapper.getRequestInformation("DEMO_MAP").getUserDeviceId();
						DemographicsModel demographicsModel = cmdManager.getCommonCommandManager().getDemographicsManager().loadDemographics(
								ThreadLocalAppUser.getAppUserModel().getAppUserId());
						if(!userModel.getMobileNo().equals("03463564149")) {
							if ((demographicsModel != null && UDID != null && !UDID.equals(demographicsModel.getUdid()))
									|| (demographicsModel != null && demographicsModel.getUdid() == null)) {
								retXML = prepareErrorMessage(new Message(ErrorCodes.INVALID_PIN, ErrorLevel.HIGH,
										"You are already logged in at Different Location.", null, null));
							}
						}
					}
					if(retXML == null || (retXML != null && retXML.equals("")))
						retXML = prepareXmlMessage(cmdManager.executeCommand(wrapper), curCmd);

					if (CMD_MFS_LOGIN.equals(curCmd))
					{
						userModel = ThreadLocalAppUser.getAppUserModel();
						userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
						if (userModel == null)
							retXML = prepareErrorMessage(new Message(0111, ErrorLevel.HIGH,
									this.messageSource.getMessage("mfsRequestHandler.unknownError", null,null)));
					}
				}
			}
			else // double submission detected
			{
				if(logger.isDebugEnabled())
					logger.debug(">>>>>>>>>>>> Double submission detected");

			}

		}
		catch (FrameworkCheckedException fCExp)
		{
			//logger.error("Exception thrown by CommandManager", );
			logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(fCExp));
			retXML = prepareErrorFromExcep((CommandException) fCExp);
		}
		catch (ParsingException pEx)
		{
			logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(pEx));
			retXML = prepareErrorMessage(new Message(ErrorCodes.INVALID_INPUT, ErrorLevel.HIGH,
					this.messageSource.getMessage("mfsRequestHandler.invalidInput", null,null)));
		}
		catch (Exception ex)
		{
			logger.error("[MfsRequestHandler.handleRequest] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + ex.getMessage() + ". Detailed Exception: " + ex);
			ex.printStackTrace();
			retXML = prepareErrorMessage(new Message(ErrorCodes.UNKNOWN_ERROR, ErrorLevel.HIGH,
					this.messageSource.getMessage("mfsRequestHandler.unknownError", null,null)));
		}
		finally
		{
			if(!isDoubleSubmission)
			{
				if(!canResponseBeNull)
				{
					//updateMfsRequestCacheInformation(retXML, curCmd, sessionWrapper);
					if(null != requestInformation)
					{
						if(logger.isDebugEnabled())
							logger.debug(">>>>>>>> Response XML saved in MfsRequestInformation");				
						requestInformation.setResponseXml(retXML);
					}
				}
				if(userDeviceAccountsModel == null)
					userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
				if(userModel == null)
					userModel = ThreadLocalAppUser.getAppUserModel();

				MessageParser parser = XmlParserFactory.getParser(xml);
				BaseWrapper wrapper = parser.parse(xml);
				if(curCmd != null && (curCmd.equals("252") || curCmd.equals("229") || curCmd.equals("250") || curCmd.equals("255"))
						|| curCmd.equals("264") || curCmd.equals("270")) {
					fingerTemplate = (String) wrapper.getObject(KEY_FINGER_TEMPLATE);
					fingerTemplate = "*****";
					logger.info("Masked Finger Template: " +fingerTemplate);
					wrapper.putObject(KEY_FINGER_TEMPLATE, fingerTemplate);

					xml = StringUtils.replace(xml, StringUtils.substringBetween(xml.substring
							(xml.indexOf(CommandFieldConstants.KEY_FINGER_TEMPLATE)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN), fingerTemplate);
				}
				actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
				actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(retXML, XPathConstants.actionLogOutputXMLLocationSteps));
				if(commandDeviceTypeValidityFlag)
				{
					actionLogModel.setCommandId(Long.parseLong(curCmd));
					actionLogModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
				}
				if(userModel != null)
				{
					actionLogModel.setAppUserId(userModel.getAppUserId());
					actionLogModel.setUserName(userModel.getUsername());
				}
				if(userDeviceAccountsModel != null)
				{
					actionLogModel.setUserDeviceAccountsId(userDeviceAccountsModel.getUserDeviceAccountsId());
					actionLogModel.setDeviceUserId(userDeviceAccountsModel.getUserId());
				}
			}
			else
				{
					if(canResponseBeNull || requestInformation==null)
						retXML = "";
					else
					{
						retXML = requestInformation.getResponseXml();						
						String cacheKey = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId()+curCmd;
						long sleepInterval = 5000, totalInterval = 0;
						
						while( retXML == null || retXML.equalsIgnoreCase("") )
						{								
							logger.debug("MfsRequestHandler -----> Doubly submitted request::: Going to Sleep");
							synchronized(this)
							{
								try
								{
									this.wait(sleepInterval);
									totalInterval += sleepInterval;
								}
								catch (InterruptedException e)
								{
									logger.error("MfsRequestHandler -----> Doubly submitted request :: " + e);
								}									
							}
							logger.error("MfsRequestHandler -----> Doubly submitted request::: Back from Sleep");
							
							if( totalInterval >= 100000 )
							{
								logger.error("Double submission detected but didn't get the ReponseXML");
								retXML = prepareErrorMessage(new Message(ErrorCodes.UNKNOWN_ERROR, ErrorLevel.HIGH,
										this.messageSource.getMessage("mfsRequestHandler.unknownError", null,null)));
								break;
							}
							
							requestInformation = (MfsRequestInformation)sessionWrapper.getRequestInformation(cacheKey);
							retXML = requestInformation.getResponseXml() ;
						}
						
						if(logger.isDebugEnabled())
							logger.debug(">>>>>>>> Response XML returned from existing MfsRequestInformation as a result of double submission");							
					}
					
					actionLogModel.setOutputXml("Double submission Detected, sending response from previous request: "+ retXML);
				}
			actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
			actionLogModel.setEndTime(new Timestamp( new java.util.Date().getTime() ));
			insertActionLogRequiresNewTransaction(actionLogModel);
//			logger.info("[ MfsRequestHandler ] Going to Remove ThreadLocalAccountInfo objects.");
			ThreadLocalAccountInfo.remove();//remove verifly accountInfo from Threadlocal 
		}


		
			
			
			
		return retXML;
	}

//**********************************************************************************************************************************
	//=========================================================
	//              Work For Product Catalog
	//=========================================================
	private String checkCurrentProductExistInCatalog(String curCmd, String xml, String retXML, UserDeviceAccountsModel userDeviceAccountsModel) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
	{
		if(!xml.contains("PID") && !(curCmd.equals("121"))) return retXML;

		String productId = "";
		if(curCmd.equals("121")){
			productId = ProductConstantsInterface.ACCOUNT_OPENING.toString();
		}else{
			productId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.PRODUCT_ID_NODEREF);
		}
		if(StringUtil.isNullOrEmpty(productId)){
			return retXML;
		}

		if(productId.equals("2510801")){
			return retXML;
		}
		Boolean isExist = productCatalogManager.isProductExistInCatalog(userDeviceAccountsModel.getProdCatalogId(), Long.parseLong(productId));
		if(!isExist) {
			retXML = prepareErrorMessage(new Message(ErrorCodes.CATALOG_ERROR, ErrorLevel.HIGH, this.messageSource.getMessage("product.catalog.updated", null, null)));
		}
		return retXML;
	}
//***********************************************************************************************************************************

	public String prepareXmlMessage(String xml, String actId)
	{
		StringBuilder msg = new StringBuilder();
		msg
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MSG)
				.append(TAG_SYMBOL_SPACE)
			.append(ATTR_MSG_ID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(StringUtil.trimToEmpty(actId))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE);
		// append actual xml message
		msg.append(xml);

		// append end of msg tag
		msg
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_CLOSE);

		return msg.toString();
	}

	public String prepareErrorFromExcep(CommandException cmdExp)
	{
		if(cmdExp.getSecondIndexLabel() != null && !cmdExp.getSecondIndexLabel().equals(""))
			return prepareErrorMessage(new Message(cmdExp.getErrorCode(), cmdExp.getLevel(), cmdExp.getMessage(),cmdExp.getIndexLabel(),cmdExp.getSecondIndexLabel()));
		else
			return prepareErrorMessage(new Message(cmdExp.getErrorCode(), cmdExp.getLevel(), cmdExp.getMessage(),cmdExp.getIndexLabel()));
	}

	public String prepareErrorMessage(Message error)
	{
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		StringBuilder xml = new StringBuilder();
		xml
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_ERRORS)
			.append(TAG_SYMBOL_CLOSE);
		if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
		{
			xml.append(MessageParsingUtils.parseMessageForIpos(error.toXml(true)));
		}
		else
		{
			xml.append(error.toXml(true));
		}

		xml
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_ERRORS)
			.append(TAG_SYMBOL_CLOSE);

		return prepareXmlMessage(xml.toString(), ErrorCodes.ERROR_MESSAGE_CODE);
//		StringBuilder xml = new StringBuilder();
//		xml
//			.append(TAG_SYMBOL_OPEN)
//			.append(TAG_ERRORS)
//			.append(TAG_SYMBOL_CLOSE)
//			.append(MessageParsingUtils.parseMessageForIpos(error.toXml(true)));
//
//		xml
//			.append(TAG_SYMBOL_OPEN_SLASH)
//			.append(TAG_ERRORS)
//			.append(TAG_SYMBOL_CLOSE);
//
//		return prepareXmlMessage(xml.toString(), ErrorCodes.ERROR_MESSAGE_CODE);
	}

	protected MfsRequestInformation isDoubleSubmission(String currentCommand, String reqTime, 
			MfsRequestWrapper userSessionWrapper)
	{
		MfsRequestInformation reqInformation = null;
		
		UserDeviceAccountsModel usModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		if(usModel!=null && (null!=currentCommand && !"".equals(currentCommand)))
		{
			String cacheKey = usModel.getUserId()+currentCommand;
			try
			{
				Long currentRequestTime = Long.parseLong(reqTime, 10);
				if(userSessionWrapper != null)
					reqInformation = (MfsRequestInformation)userSessionWrapper.getRequestInformation(cacheKey);
				if(null!=reqInformation)
				{
					Long prevRequestTime = reqInformation.getRequestTime();
					if(currentRequestTime.equals(prevRequestTime))
					{
						reqInformation.setRepeatingRequest(true);
					}
					else
					{
						reqInformation.setRequestTime(currentRequestTime);
						reqInformation.setRepeatingRequest(false);
						reqInformation.setResponseXml("");
					}
				}
				else
				{
					reqInformation = new MfsRequestInformation(currentRequestTime, false);
					userSessionWrapper.setRequestInformation(cacheKey, reqInformation);
				}
			}
			catch(NumberFormatException nEx)
			{
				logger.error("Exception while converting time to long", nEx);
			}
		}
		
		return reqInformation;
	}
	
	/*
	private void updateMfsRequestCacheInformation(String responseXML, String currentCommand, 
			MfsRequestWrapper userSessionWrapper)
	{
		UserDeviceAccountsModel usModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		if(usModel!=null && (null!=currentCommand && !"".equals(currentCommand)))
		{
			String cacheKey = usModel.getUserId()+ currentCommand;
			MfsRequestInformation reqInformation = userSessionWrapper.getRequestInformation(cacheKey);
			if(null!=reqInformation)
			{
				reqInformation.responseXml = responseXML;
				if(logger.isDebugEnabled())
					logger.debug(">>>>>>>> MfsRequestInformation updated in cache");				
			}
			else
			{
				if(logger.isDebugEnabled())
					logger.debug(">>>>>>>>>> MfsRequestInformation not required to be updated");
			}
		}
	}
	*/
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel)
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try
		{
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel)baseWrapper.getBasePersistableModel();
		}
		catch(Exception ex)
		{
			logger.error("Exception occurred while processing",ex);

		}
		return actionLogModel;
	}

	
	public void setCmdManager(CommandManager cmdManager)
	{
		this.cmdManager = cmdManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public void setDeviceTypeCommandManager(DeviceTypeCommandManager deviceTypeCommandManager)
	{
		this.deviceTypeCommandManager = deviceTypeCommandManager;
	}

	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
	
	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	/**
	 * @param canResponseBeNull the canResponseBeNull to set
	 */
	public void setCanResponseBeNull(boolean canResponseBeNull)
	{
		this.canResponseBeNull = canResponseBeNull;
	}

	public void setProductCatalogManager(ProductCatalogManager productCatalogManager) {
		this.productCatalogManager = productCatalogManager;
	}
}
