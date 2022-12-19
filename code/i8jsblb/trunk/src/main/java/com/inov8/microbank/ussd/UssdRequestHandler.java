package com.inov8.microbank.ussd;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_DEVICE_TYPE_ID;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.webapp.action.allpayweb.formbean.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.viewer.Command;
import org.springframework.context.MessageSource;

import com.ibm.icu.util.Calendar;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.vo.ussd.UserState;
import com.inov8.microbank.mfs.MfsRequestHandler;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.ussdmodule.UssdMenuManager;
import com.inov8.microbank.server.service.ussdmodule.UssdUserStateManager;
import com.inov8.microbank.server.webservice.bean.USSDInputDTO;
import com.inov8.microbank.server.webservice.bean.USSDOutputDTO;
import com.thoughtworks.xstream.XStream;

public class UssdRequestHandler {
	protected final Log logger = LogFactory.getLog(getClass());
	private CommonCommandManager commonCommandManager;
	private MfsRequestHandler mfsReqHandler;
	private UssdMenuManager ussdMenuManager;
	private CommandManager commandManager;
	private ActionLogManager actionLogManager;
	private MessageSource messageSource;
	private UssdUserStateManager ussdUserStateManager;
	private CommissionRateDAO commissionRateDAO;


	public String checkLogin(String msnisdn, HttpServletRequest request, USSDXMLUtils ussdXMLUtils) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.checkLogin Start");
		}
		String loginRetVal = null;
		MfsRequestWrapper sessionWrapper = (MfsRequestWrapper) request.getSession(false).getAttribute(MfsRequestWrapper.KEY_MFS_REQUEST_MAP);
		if (null == sessionWrapper) {
			//if (logger.isDebugEnabled())
			logger.debug(">>>>>>>>> SessionWrapper is null, creating and storing in HttpSession");

			AppUserModel appUserModel = this.getCommonCommandManager().loadAppUserByMobileByQuery(msnisdn);
			if(appUserModel == null)
			{
//					logger.info("++++++++++++ Unregistered Customer - "+msnisdn+" ++++++++++++");
				loginRetVal = "<msg id=\"-1\"><errors><error code=\"9001\" level=\"2\">Dear Customer in order to use Timepey, kindly visit Zong Customer Service Centre, Franchise or any AKBL branch.</error></errors></msg>";
				return loginRetVal;
			}

			sessionWrapper = new MfsRequestWrapper();
			request.getSession(false).setAttribute(MfsRequestWrapper.KEY_MFS_REQUEST_MAP, sessionWrapper);
		}

		String xml = ussdXMLUtils.prepareLoginXmlMessage("45", msnisdn, DeviceTypeConstantsInterface.USSD + "");


		try {
			loginRetVal = mfsReqHandler.handleRequest(xml, sessionWrapper);

		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.checkLogin End");
		}
		return loginRetVal;
	}
	public boolean isValidUserType(AppUserModel appUserModel) {
		return null != appUserModel
				&& (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue() || appUserModel.getAppUserTypeId()
				.longValue() == UserTypeConstantsInterface.RETAILER.longValue());
	}
	public I8SBSwitchControllerResponseVO validateUser(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO,Long appUserTypeID) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.validateUser Start");
		}
		USSDOutputDTO ret = null;//new USSDOutputDTO();

		// validating User Mobile No and User Channel of USSD.
		try{
			AppUserModel appUserModel = this.getCommonCommandManager().loadAppUserByMobileByQuery(i8SBSwitchControllerRequestVO.getMobileNumber());

			SearchBaseWrapper appUserWrapper = new SearchBaseWrapperImpl();
			appUserModel.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
			appUserModel.setAppUserId(appUserTypeID);

			appUserWrapper.setBasePersistableModel(appUserModel);
			appUserWrapper =  this.getCommonCommandManager().loadAppUserByMobileNumberAndType(appUserWrapper);

			appUserModel = (AppUserModel)appUserWrapper.getBasePersistableModel();

			if(appUserModel == null || !appUserModel.getAppUserTypeId().equals(appUserTypeID))
			{
				logger.info("[User Validation] App User does not exist");
				i8SBSwitchControllerResponseVO.setuSSDResponseString("App User does not exist...");
				i8SBSwitchControllerResponseVO.setuSSDAction("end");
				i8SBSwitchControllerResponseVO.setError("true");
				return i8SBSwitchControllerResponseVO;
			}
			else {

				i8SBSwitchControllerResponseVO.setError("false");
				String mobileNo = appUserModel.getMobileNo();
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);

				ValidationErrors validationErrors = this.getCommonCommandManager().checkUserCredentials(baseWrapper);
				if (validationErrors.hasValidationErrors()) {
					i8SBSwitchControllerResponseVO.setuSSDResponseString(validationErrors.getErrors());
					i8SBSwitchControllerResponseVO.setuSSDAction("end");
					i8SBSwitchControllerResponseVO.setError("true");
					i8SBSwitchControllerResponseVO.setuSSDResponseString(validationErrors.getErrors());
					logger.error("[User Validation] Error "+ validationErrors.getErrors());
				}
				else if(!checkIsUSSDEnabled(appUserModel,appUserTypeID))
				{
					i8SBSwitchControllerResponseVO.setuSSDResponseString("USSD Channel is disbabled for your account");
					i8SBSwitchControllerResponseVO.setuSSDAction("end");
					i8SBSwitchControllerResponseVO.setError("true");
					logger.error("[User Validation] USSD Channel is disbabled");
				}
				else
				{
					// User is fully verified..
					logger.info("[User Validation] User is successfuly validated");
					i8SBSwitchControllerResponseVO.setError("false");
					ThreadLocalAppUser.setAppUserModel(appUserModel);
					baseWrapper = this.commonCommandManager.loadUserDeviceAccountByMobileNumber(baseWrapper);
					UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
					ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return i8SBSwitchControllerResponseVO;
	}

	private boolean checkIsUSSDEnabled(AppUserModel appUserModel,Long appUserTypeID) throws FrameworkCheckedException {

		if(appUserTypeID == UserTypeConstantsInterface.RETAILER)
		{
			BaseWrapper retailerContactWrapper = new BaseWrapperImpl();
			Long retailerContactID = appUserModel.getRetailerContactId();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setPrimaryKey(retailerContactID);
			retailerContactWrapper.setBasePersistableModel(retailerContactModel);
			retailerContactModel = (RetailerContactModel) commonCommandManager.loadRetailerContact(retailerContactWrapper).getBasePersistableModel();

			if(!retailerContactModel.getAgentUssdEnabled())
			{
				return false;
			}
			return  true;
		}
		else if(appUserTypeID != null && appUserTypeID.equals(UserTypeConstantsInterface.CUSTOMER))
		{
			CustomerModel customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
			if(customerModel != null && customerModel.getCustomerUSSDEnabled() != null && !customerModel.getCustomerUSSDEnabled())
				return false;
			else
				return true;
		}

		return true;
	}

	public boolean isFirstCall(String message,String msisdn) {
		return null!=msisdn && null != message && message.equals(USSDConstantsInterface.STAR_200_HASH_MSG_VALUE);
	}
	public void loadCatalog(UserState userState, UssdMenuModel menuModel){
		String xmlResponse=null;
		try {
			BaseWrapper baseWrapper=new BaseWrapperImpl();
			baseWrapper.putObject( KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
			xmlResponse=commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_GET_LATEST_CATALOG);
			System.out.println(xmlResponse);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String executeCommand(UserState userState, UssdMenuModel menuModel,I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.executeCommand Start");
		}
		String retVal=null;

		if(CommandConstants.BALANCE_CHECK_AGENT.equals(menuModel.getCommandCode().toUpperCase())){
			// TODO: execute BALANCE_CHECK_AGENT command
			XStream xstream = new XStream();
			ActionLogModel actionLogModel = new ActionLogModel();
			try
			{

//				this.populateUserStateWithBankInfo(userState, commandManager);
				String response = "";
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject( CommandFieldConstants.KEY_ALLPAY_ID, ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId()) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_BB_ACC_ID, userState.getAccId() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_PIN, userState.getPin() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_ACCOUNT_TYPES, userState.getAccountTypeId() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_ENCRYPTION_TYPE,1) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_APP_ID,CommandConstants.CONSUMER_APP) ;


				logger.info("[UssdRequestHanler.execute] BALANCE_CHECK_AGENT Tracing ***** \n DEVICE TYPE ** "+
						baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID)+"\n AGENT MOBILE NUMBER ** "+
						userState.getUserMsisdn() +"\n****************" );


				actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(baseWrapper), XPathConstants.actionLogInputXMLLocationSteps));
				this.actionLogBeforeStart(actionLogModel);
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
				actionLogModel.setCommandId(new Long(CommandFieldConstants.CMD_CHK_ACC_BAL).longValue());
				actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
				actionLogModel.setOutputXml(retVal);
				this.actionLogAfterEnd(actionLogModel);

				String BAL =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='BAL']");
				userState.setAgentBalance(BAL);
				userState.setTransactionAmount(BAL);
				logger.info("[UssdRequestHanler.execute] Completing execution of BALANCE_CHECK_AGENT. Details: ***** \n DEVICE TYPE ** "+
						baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID)+"\n AGENT MOBILE NUMBER ** "+
						userState.getUserMsisdn() +"\n****************" );


			}
			catch(Exception e)
			{
				if(e instanceof CommandException)
				{
					retVal = e.getMessage();
					i8SBSwitchControllerResponseVO.setError("true");
				}
				else if (e instanceof WorkFlowException)
				{
					logger.error(e);
					retVal = e.getMessage();
					i8SBSwitchControllerResponseVO.setError("true");
				}
				else
				{
					retVal=MessageUtil.getMessage("MINI.GeneralError");
					i8SBSwitchControllerResponseVO.setError("true");
				}
			}
		}
		else if(CommandConstants.BB_MINI_STATEMENT.equals(menuModel.getCommandCode().toUpperCase()))
		{
			// Agent Mini Statement

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject( CommandFieldConstants.KEY_ALLPAY_ID, ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId()) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_BB_ACC_ID, userState.getAccId() ) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_PIN, userState.getPin() ) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_ACCOUNT_TYPES, 1 ) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_ENCRYPTION_TYPE,1) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_ST_TX_NO,1) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_END_TX_NO,5) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_U_ID, ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId()) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_PAYMENT_MODE,0) ;

			logger.info("[UssdRequestHanler.execute] Mini Statement Agent ***** \n DEVICE TYPE ** "+
					baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID)+"\n AGENT MOBILE NUMBER ** "+
					userState.getUserMsisdn() +"\n****************" );


			try {
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_MINISTATEMENT_AGENT);

				logger.info("[USSDRequesthandler] Mini Statemnt Command Result "+ retVal);



			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.BB_CD_INFO.equals(menuModel.getCommandCode().toUpperCase()))
		{
			// Agent Cash In

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID,ProductConstantsInterface.CASH_DEPOSIT);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT,userState.getTransferAmount());

			logger.info("[UssdRequestHanler.execute] CashIn Customer ***** \n DEVICE TYPE ** "+
					baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID)+"\n AGENT MOBILE NUMBER ** "+
					userState.getUserMsisdn() +"\n****************" );


			try {

				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND);

				// Generating the OTP
				MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
				miniTransactionModel.setAppUserId(userState.getCustomerAppUserModel().getAppUserId());
				miniTransactionModel.setMobileNo(userState.getMsisdn());
				miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_CASH_DEPOSIT));
				miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
				logger.info("[USSDRequestHandler]Going to Generate OTP..");
				this.getCommonCommandManager().getMiniTransactionManager().generateOTP(miniTransactionModel, "otpSms", "Cash Deposit");

				String CMOB =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='CMOB']");
				String TXAM =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='TXAM']");
				String CAMTF =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='CAMTF']");
				String TAMT =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='TAMT']");
				String CNAME  =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='NAME']");

				userState.setCustomerName(CNAME);
				userState.setMsisdn(CMOB);
				userState.setDeductionAmount(CAMTF);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);
				userState.setProductID(ProductConstantsInterface.CASH_DEPOSIT);
			}
			catch(Exception e)
			{
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.BB_CD.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent Cash In

			AppUserModel customerModel = null;

			try {
				customerModel = commonCommandManager.loadAppUserByMobileAndType(userState.getMsisdn(), new Long[]{2L});
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}

			String customerCNIC = customerModel.getNic();
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CASH_DEPOSIT);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, customerCNIC);
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, Double.toString(userState.getAmount()));
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, userState.getTotalAmount());

			logger.info("[UssdRequestHanler.execute] CashIn Customer ***** \n DEVICE TYPE ** " +
					baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID) + "\n AGENT MOBILE NUMBER ** " +
					userState.getUserMsisdn() + "\n****************");


			try {
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CASH_DEPOSIT);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");
				String fedAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TPAMF");
				String comAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@CAMTF");

				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);
				userState.setFedAmount(fedAmount);
				userState.setCommissionAmount(comAmount);
			}
			catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.BB_CASH_OUT_INFO.equals(menuModel.getCommandCode().toUpperCase()))
		{
			// Agent CashOut

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID,ProductConstantsInterface.CASH_WITHDRAWAL);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT,userState.getTransferAmount());

			logger.info("[UssdRequestHanler.execute] CashOut Customer ***** \n DEVICE TYPE ** "+
					baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID)+"\n AGENT MOBILE NUMBER ** "+
					userState.getUserMsisdn() +"\n****************" );


			try {
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CASH_OUT_INFO);

				// Generating the OTP
				MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
				miniTransactionModel.setAppUserId(userState.getCustomerAppUserModel().getAppUserId());
				miniTransactionModel.setMobileNo(userState.getMsisdn());
				miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_CASH_DEPOSIT));
				miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
				logger.info("[USSDRequestHandler]Going to Generate OTP..");
				this.getCommonCommandManager().getMiniTransactionManager().generateOTP(miniTransactionModel, "otpSms", "Cash Withdrawl");

				String CMOB =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='CMOB']");
				String TXAM =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='TXAM']");
				String CAMTF =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='CAMTF']");
				String TAMT =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='TAMT']");
				String CNAME  =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='CNAME']");
				String CNIC  =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='CNIC']");

				userState.setCustomerName(CNAME);
				userState.setCustomerCNIC(CNIC);
				userState.setMsisdn(CMOB);
				userState.setDeductionAmount(CAMTF);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);
				userState.setProductID(ProductConstantsInterface.CASH_WITHDRAWAL);
			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.BB_CASH_OUT.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent Cash out


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CASH_WITHDRAWAL);
			baseWrapper.putObject(CommandFieldConstants.KEY_IS_USSD,true);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, userState.getCustomerCNIC());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, Double.toString(userState.getAmount()));
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, userState.getTotalAmount());

			logger.info("[UssdRequestHanler.execute] CashOut Customer ***** \n DEVICE TYPE ** " +
					baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID) + "\n AGENT MOBILE NUMBER ** " +
					userState.getUserMsisdn() + "\n****************");


			try {
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CASH_OUT);
				logger.info("[USSDRequesthandler] Cashout Command Result "+ retVal);
				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@ID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");
				String fedAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TPAMF");
				String comAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@CAMTF");

				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);
				userState.setFedAmount(fedAmount);
				userState.setCommissionAmount(comAmount);

			} catch (Exception e) {
				i8SBSwitchControllerResponseVO.setError("true");
				retVal = e.getMessage();
				logger.error(e);
			}
		}

		else if(CommandConstants.CHANGE_PIN_AGENT.equals(menuModel.getCommandCode().toUpperCase())) {


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, userState.getNewPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, userState.getConfirmNewPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);

			try {
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_VERIFLY_PIN_CHANGE);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.CHANGE_LOGIN_PIN_AGENT.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent Login Pin Change:


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getLoginPIN());
			baseWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, userState.getNewPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, userState.getConfirmNewPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);




			try {
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_GNI_CHG_PIN);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.CHALLAN_INFO.equals(menuModel.getCommandCode().toUpperCase())) {
			// VRG CHallan INFO Command


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, userState.getChallanID());
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getProductID());
			baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
			baseWrapper.putObject(CommandFieldConstants.KEY_ALLPAY_ID, ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
			baseWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER,userState.getMsisdn());


			try {

				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_COLLECTION_PAYMENT_INFO_COMMAND);
				logger.info("[USSDRequesthandler] CollectionPaymentInfo Command Result "+ retVal);

				String BAMT  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMT']");
				String BAMTF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMTF']");
				String BPAID  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BPAID']");
				String DUEDATE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='DUEDATE']");
				String DUEDATEF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='DUEDATEF']");
				String ISOVERDUE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='ISOVERDUE']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");

				userState.setUtilityBillAmount(TAMT);
				userState.setIsBillOverDue(ISOVERDUE);
				userState.setValidDate(DUEDATE);
				userState.setIsBillPaid(BPAID);
				userState.setDeductionAmount(TPAM);
				userState.setTransactionProcessingAmount(TPAM);
				userState.setTransactionAmount(BAMT);
				userState.setTotalAmount(TAMT);

				if (userState.getIsBillPaid().equals("1")) {
					i8SBSwitchControllerResponseVO.setError("true");
					retVal = "Your Challan has already been Paid.";
				}

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.CHALLAN_DEPOSIT.equals(menuModel.getCommandCode().toUpperCase())) {
			// VRG CHallan Deposit Command

			BaseWrapper baseWrapper = new BaseWrapperImpl();


			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, userState.getChallanID());
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getProductID());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getTransactionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM,userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT,userState.getTotalAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute CollectionPayment Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_COLLECTION_PAYMENT_COMMAND);
				logger.info("[USSDRequesthandler] CollectionPayment Command Result "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");
				//String fedAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TPAMF");
				String comAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@CAMTF");
				// only commission amount is being provided by VRG
				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);
				userState.setFedAmount(comAmount);
				userState.setCommissionAmount(comAmount);
			}
			catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.TRANSFER_IN_INFO.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent TRANSFER IN INFO CMD:


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.	TRANSFER_IN);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getTransferAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, userState.getBankId());


			try {

				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_TRANSFER_IN_INFO);

				logger.info("[USSDRequesthandler] TransferIn Info Command Result "+ retVal);
				String CoreAccountID =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='COREACID']");
				String TXAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TXAM']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");

				userState.setCoreAccountID(CoreAccountID);
				userState.setDeductionAmount(CAMT);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);

				logger.info("[USSDRequestHandler] Result Parsed Successfully: Total  Amount to be transfered is: "+ userState.getTotalAmount());

			}
			catch(Exception e)
			{
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}


		else if(CommandConstants.TRANSFER_IN_CMD.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent TRANSFER In  CMD:

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.TRANSFER_IN);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE,1);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_BB_ACC_ID,userState.getAccId());
			baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO,userState.getCoreAccountID());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getTransferAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, userState.getBankId());
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, userState.getTotalAmount());


			try {
				logger.info("[USSDRequestHandler] Going to Execute TransferIN Command");
				ThreadLocalActionLog.setActionLogId(1L);
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_TRANSFER_IN);
				logger.info("[USSDRequestHandler] TransferIN Command Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String billDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String billTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");

				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(billDate+":"+billTime);
				userState.setTransactionAmount(TXAMF);

			}
			catch(Exception e)
			{
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}


		else if(CommandConstants.TRANSFER_OUT_INFO.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent TRANSFER OUT INFO CMD:

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.TRANSFER_OUT);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getTransferAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, userState.getBankId());


			try {
				logger.info("[USSDRequestHandler] Going to Execute TransferOutInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_TRANSFER_OUT_INFO);
				logger.info("[USSDRequestHandler] TransferOutInfo Command Result: "+ retVal);

				String CoreAccountID =MiniXMLUtil.getTagTextValue(retVal, "//params/param[@name='COREACID']");
				String CoreAccountTitle = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='COREACTL']");
				String TXAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TXAM']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");

				userState.setCoreAccountID(CoreAccountID);
				userState.setCoreAccountTitle(CoreAccountTitle);
				userState.setDeductionAmount(CAMT);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.TRANSFER_OUT_CMD.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent TRANSFER OUT INFO CMD:


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.TRANSFER_OUT);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE,1);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_BB_ACC_ID,userState.getAccId());
			baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO,userState.getCoreAccountID());
			baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE,userState.getCoreAccountTitle());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getTransferAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, userState.getBankId());
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, userState.getTotalAmount());


			try {
				logger.info("[USSDRequestHandler] Going to Execute TransferOut Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_TRANSFER_OUT);
				logger.info("[USSDRequestHandler] TransferOut Command Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String billDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String billTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");

				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(billDate+":"+billTime);
				userState.setTransactionAmount(TXAMF);

			} catch(Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.AG_TRAN_INFO.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent TRANSFER OUT INFO CMD:

			AppUserModel appUserModel = null;
			UserDeviceAccountsModel userDeviceAccountsModel = null;
			try {
				appUserModel = commonCommandManager.loadAppUserByQuery(userState.getReceiverAccountNo(),3L);

				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper =   commonCommandManager.loadUserDeviceAccountByMobileNumber(baseWrapper);

				userDeviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,userDeviceAccountsModel.getDeviceTypeId());
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE,userState.getReceiverAccountNo());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getTransferAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute AgentoAgentInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.AGENT_TO_AGENT_INFO);
				logger.info("[USSDRequestHandler] AgenttoAgentInfo Command Result: "+ retVal);

				String TXAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TXAM']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");
				String RAMOB = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='RAMOB']");
				String RACNIC = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='RACNIC']");
				String RANAME = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='RANAME']");

				userState.setDeductionAmount(CAMT);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setWalkinReceiverCNIC(RACNIC);
				userState.setRecipientName(RANAME);
				userState.setTransactionAmount(TXAM);
				userState.setMsisdn(RAMOB);

			} catch (Exception e) {
				logger.error(e);
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
			}
		}

		else if(CommandConstants.AG_TRAN_CMD.equals(menuModel.getCommandCode().toUpperCase())) {
			// Agent TRANSFER CMD:

			AppUserModel appUserModel = null;
			UserDeviceAccountsModel userDeviceAccountsModel = null;
			try {
				appUserModel = commonCommandManager.loadAppUserByQuery(userState.getReceiverAccountNo(),3L);
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper =   commonCommandManager.loadUserDeviceAccountByMobileNumber(baseWrapper);

				userDeviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
			} catch (Exception e) {
				logger.error(e);
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,userDeviceAccountsModel.getDeviceTypeId());
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE,userState.getReceiverAccountNo());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getTransferAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, userState.getTotalAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute AgentoAgent Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_R2R_TRANS);
				logger.info("[USSDRequestHandler] AgenttoAgent Command Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transferDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATE");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");

				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(transferDate);
				userState.setTransactionAmount(TXAMF);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}
		else if(CommandConstants.BILL_PAY_INFO_COMMAND_CODE.equals(menuModel.getCommandCode().toUpperCase())) {

			if(userState.getBillPaymentConsumerNumber() == null || userState.getBillPaymentConsumerNumber().equals(""))
				userState.setBillPaymentConsumerNumber(userState.getMsisdn());

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getUtilityCompanyID());
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CSCD,userState.getBillPaymentConsumerNumber());
			baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE,PaymentModeConstants.PAYMENT_FROM_CASH);
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID,userState.getBankId());
			baseWrapper.putObject(CommandFieldConstants.KEY_CNIC,userState.getCustomerCNIC());

			try {
				logger.info("[USSDRequestHandler] Going to Execute BillPaymentInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_INFO);
				logger.info("[USSDRequestHandler] BillPaymentInfo Command Result: "+ retVal);
				String BAMT  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMT']");
				String BAMTF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMTF']");
				String LBAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='LBAMT']");
				String LBAMTF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='LBAMTF']");
				String BPAID  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BPAID']");
				String CNIC = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CNIC']");
				String DUEDATE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='DUEDATE']");
				String ISOVERDUE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='ISOVERDUE']");
				String companyName = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='PNAME']");

				double actualBillAmount;
				double paidBillAmount = -1;
				if(!userState.isPrepaidLoad()) {
					if (userState.getTransferAmount() != null)
						paidBillAmount = Double.parseDouble(userState.getTransferAmount());

					if (ISOVERDUE.equals("0")) {

						actualBillAmount = Double.parseDouble(BAMT);
					} else {
						actualBillAmount = Double.parseDouble(LBAMT);
					}
					userState.setIsBillPaid(BPAID);
					if (userState.getIsBillPaid().equals("1")) {
						i8SBSwitchControllerResponseVO.setError("true");
						retVal = "Your Bill has already been Paid.";
					}

					if (paidBillAmount != -1) {
						if (paidBillAmount > actualBillAmount) {
							i8SBSwitchControllerResponseVO.setError("true");
							retVal = "The Amount you have entered is greater then actual Bill Amount which is {" + actualBillAmount + "}";
						}
					}
				}

				if(userState.getTransferAmount() != null) {
					userState.setUtilityBillAmount(userState.getTransferAmount());
					userState.setTransactionAmount(userState.getTransferAmount());
				}else
				{
					userState.setUtilityBillAmount(BAMT);
					userState.setTransactionAmount(BAMT);
				}
				userState.setIsBillOverDue(ISOVERDUE);
				userState.setValidDate(DUEDATE);
				userState.setIsBillPaid(BPAID);
			} catch (Exception e)
			{
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}
		else if(CommandConstants.BILL_PAY_COMMAND_CODE.equals(menuModel.getCommandCode().toUpperCase())) {



			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getUtilityCompanyID());
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants. CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER,userState.getBillPaymentConsumerNumber());
			baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE,PaymentModeConstants.PAYMENT_FROM_CASH);
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID,userState.getBankId());
			baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, userState.getUtilityBillAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getUtilityBillAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute BillPaymentInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
				logger.info("[USSDRequestHandler] BillPaymentInfo Command Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String billDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String billTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");

				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(billDate+":"+billTime);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}

		else if(CommandConstants.WALLET_TO_CNIC_INFO.equals(menuModel.getCommandCode().toUpperCase())) {

			logger.info("Inside Wallet2CNICINFO");
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_CASH_CI);
			baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_R_W_CNIC,userState.getWalkinReceiverCNIC());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());

			try {
				logger.info("[USSDRequestHandler] Going to Execute Wallet2CNICInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2CNICINFO);
				logger.info("[USSDRequestHandler] Wallet2CNICInfo Result: "+ retVal);

				String TXAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TXAM']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");

				userState.setCommissionAmount(CAMT);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);
				userState.setTransactionProcessingAmount(TPAM);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}
		else if(CommandConstants.WALLET_TO_CNIC_CMD.equals(menuModel.getCommandCode().toUpperCase())) {



			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_CASH_CI);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_R_W_CNIC,userState.getWalkinReceiverCNIC());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT,userState.getCommissionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM,userState.getTransactionProcessingAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT,userState.getTotalAmount());



			try {
				logger.info("[USSDRequestHandler] Going to Execute Wallet2CNICCMD Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2CNICCMD);
				logger.info("[USSDRequestHandler] Wallet2CNICCMD Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String customerBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String transactionTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");

				userState.setTransactionID(transactionID);
				userState.setCustomerBalance(customerBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}

		else if(CommandConstants.WALLET2_WALLET_INFO.equals(menuModel.getCommandCode().toUpperCase())) {


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_ACT_CI);
			baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());


			try {
				logger.info("[USSDRequestHandler] Going to Execute Wallet2WalletInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2WALLETINFO);
				logger.info("[USSDRequestHandler] Wallet2WalletInfo Result: "+ retVal);

				String TXAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TXAM']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");

				userState.setCommissionAmount(CAMT);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);
				userState.setTransactionProcessingAmount(TPAM);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}

		else if(CommandConstants.WALLET2_WALLET_CMD.equals(menuModel.getCommandCode().toUpperCase())) {


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_ACT_CI);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT,userState.getCommissionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM,userState.getTransactionProcessingAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT,userState.getTotalAmount());



			try {
				logger.info("[USSDRequestHandler] Going to Execute Wallet2Wallet Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2WALLETCMD);
				logger.info("[USSDRequestHandler] Wallet2Wallet Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String customerBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String transactionTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");

				userState.setTransactionID(transactionID);
				userState.setCustomerBalance(customerBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}
		else if(CommandConstants.BB2_CROE_INFO.equals(menuModel.getCommandCode().toUpperCase())) {


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT);
			baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO,userState.getCoreAccountID());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());


			try {
				logger.info("[USSDRequestHandler] Going to Execute BB2COREINFO Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BB2COREINFO);
				logger.info("[USSDRequestHandler] BB2COREINFO Result: "+ retVal);

				String TXAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TXAM']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");
				String COREACCT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='COREACTL']");

				userState.setCommissionAmount(CAMT);
				userState.setAmount(Double.parseDouble(TXAM));
				userState.setTotalAmount(TAMT);
				userState.setTransactionAmount(TAMT);
				userState.setTransactionProcessingAmount(TPAM);
				userState.setCoreAccountTitle(COREACCT);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}

		else if(CommandConstants.BB2_CORE_CMD.equals(menuModel.getCommandCode().toUpperCase())) {


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, userState.getPin());
			baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1);
			baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO,userState.getCoreAccountID());
			baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE,userState.getCoreAccountTitle());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_CAMT,userState.getCommissionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM,userState.getTransactionProcessingAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT,userState.getTotalAmount());



			try {
				logger.info("[USSDRequestHandler] Going to Execute BB2CORE Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BBTOCORECMD);
				logger.info("[USSDRequestHandler] BB2CORE Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String customerBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String transactionTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");

				userState.setTransactionID(transactionID);
				userState.setCustomerBalance(customerBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}

		else if(CommandConstants.CUSTOMER_CHALLAN_INFO.equals(menuModel.getCommandCode().toUpperCase())) {
			// Customer VRG CHallan INFO Command


			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, userState.getChallanID());
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getProductID());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getMsisdn());


			try {

				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CUSTOMER_COLLECTION_PAYMENT_INFO);
				logger.info("[USSDRequesthandler] Customer CollectionPaymentInfo Command Result "+ retVal);

				String BAMT  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMT']");
				String BAMTF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMTF']");
				String BPAID  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BPAID']");
				String LBAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='LBAMT']");
				String DUEDATE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='DUEDATE']");
				String DUEDATEF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='DUEDATEF']");
				String ISOVERDUE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='ISOVERDUE']");
				String TAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TAMT']");
				String TPAM = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='TPAM']");
				String CAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CAMT']");

				if (ISOVERDUE.equals("0")) {
					userState.setUtilityBillAmount(BAMT);
					userState.setTransactionAmount(BAMT);
				}
				else {
					userState.setUtilityBillAmount(LBAMT);
					userState.setTransactionAmount(LBAMT);
				}

				userState.setIsBillOverDue(ISOVERDUE);
				userState.setValidDate(DUEDATE);
				userState.setIsBillPaid(BPAID);
				userState.setDeductionAmount(TPAM);
				userState.setTransactionProcessingAmount(TPAM);
				userState.setCommissionAmount(CAMT);
				userState.setTransactionAmount(TAMT);

				if (userState.getIsBillPaid().equals("1")) {
					i8SBSwitchControllerResponseVO.setError("true");
					retVal = "Your Challan has already been Paid.";
				}

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.CUSTOMER_CHALLAN_DEPOSIT.equals(menuModel.getCommandCode().toUpperCase())) {
			// VRG CHallan Deposit Command

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getMsisdn());
			baseWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, userState.getChallanID());
			baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, userState.getChallanID());
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getProductID());
			baseWrapper.putObject(CommandFieldConstants.KEY_TXAM,userState.getUtilityBillAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT,userState.getUtilityBillAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TPAM,userState.getDeductionAmount());
			baseWrapper.putObject(CommandFieldConstants.KEY_TAMT,userState.getTransactionAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute Customer CollectionPayment Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CUSTOMER_COLLECTION_PAYMENT_COMMAND);
				logger.info("[USSDRequesthandler] Customer CollectionPayment Command Result "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String AgentBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String transactionDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String TXAMF = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TXAMF");
				//String fedAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TPAMF");
				String comAmount = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@CAMTF");
				// only commission amount is being provided by VRG
				userState.setTransactionID(transactionID);
				userState.setAgentBalance(AgentBalance);
				userState.setTransactionDate(transactionDate);
				userState.setTransactionAmount(TXAMF);
				userState.setFedAmount(comAmount);
				userState.setCommissionAmount(comAmount);
			}
			catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}
		}

		else if(CommandConstants.CUSTOMER_BILL_PAYMENT_INFO.equals(menuModel.getCommandCode().toUpperCase())) {

			if(userState.getBillPaymentConsumerNumber() == null || userState.getBillPaymentConsumerNumber().equals(""))
				userState.setBillPaymentConsumerNumber(userState.getMsisdn());

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getUtilityCompanyID());
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CSCD,userState.getBillPaymentConsumerNumber());
			baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE,PaymentModeConstants.PAYMENT_FROM_CASH);
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID,userState.getBankId());
			baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT,userState.getUtilityBillAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute Customer BillPaymentInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS_INQUIRY);
				logger.info("[USSDRequestHandler] Customer BillPaymentInfo Command Result: "+ retVal);
				String BAMT  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMT']");
				String BAMTF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BAMTF']");
				String LBAMT = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='LBAMT']");
				String LBAMTF = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='LBAMTF']");
				String BPAID  = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='BPAID']");
				String CNIC = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='CNIC']");
				String DUEDATE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='DUEDATE']");
				String ISOVERDUE = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='ISOVERDUE']");
				String companyName = MiniXMLUtil.getTagTextValue(retVal,"//params/param[@name='PNAME']");

				double actualBillAmount;
				double paidBillAmount = -1;
				if(!userState.isPrepaidLoad()) {
					if (userState.getTransferAmount() != null)
						paidBillAmount = Double.parseDouble(userState.getTransferAmount());

					if (ISOVERDUE.equals("0")) {

						actualBillAmount = Double.parseDouble(BAMT);
					} else {
						actualBillAmount = Double.parseDouble(LBAMT);
					}
					userState.setIsBillPaid(BPAID);
					if (userState.getIsBillPaid().equals("1")) {
						i8SBSwitchControllerResponseVO.setError("true");
						retVal = "Your Bill has already been Paid.";
					}

					if (paidBillAmount != -1) {
						if (paidBillAmount > actualBillAmount) {
							i8SBSwitchControllerResponseVO.setError("true");
							retVal = "The Amount you have entered is greater then actual Bill Amount which is {" + actualBillAmount + "}";
						}
					}
				}

               if(userState.getTransferAmount() != null) {
				   userState.setUtilityBillAmount(userState.getTransferAmount());
				   userState.setTransactionAmount(userState.getTransferAmount());
			   }else
			   {
				   userState.setUtilityBillAmount(BAMT);
				   userState.setTransactionAmount(BAMT);
			   }
				userState.setIsBillOverDue(ISOVERDUE);
				userState.setValidDate(DUEDATE);
				userState.setIsBillPaid(BPAID);


			} catch (Exception e)
			{
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}
		else if(CommandConstants.CUSTOMER_BILL_PAYMENT_CMD.equals(menuModel.getCommandCode().toUpperCase())) {



			BaseWrapper baseWrapper = new BaseWrapperImpl();

			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.USSD);
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, userState.getUtilityCompanyID());
			baseWrapper.putObject( CommandFieldConstants.KEY_PIN, userState.getPin() ) ;
			baseWrapper.putObject( CommandFieldConstants.KEY_ENCRYPTION_TYPE, 1) ;
			baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,userState.getUserMsisdn());
			baseWrapper.putObject(CommandFieldConstants.KEY_CSCD,userState.getBillPaymentConsumerNumber());
			baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE,PaymentModeConstants.PAYMENT_FROM_CASH);
			baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID,userState.getBankId());
			baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT,userState.getUtilityBillAmount());

			try {
				logger.info("[USSDRequestHandler] Going to Execute BillPaymentInfo Command");
				retVal = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS);
				logger.info("[USSDRequestHandler] BillPaymentInfo Command Result: "+ retVal);

				String transactionID = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TRXID");
				String customerBalance = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@BALF");
				String billDate = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@DATEF");
				String billTime = MiniXMLUtil.getTagTextValue(retVal,"//trans/trn/@TIMEF");

				userState.setTransactionID(transactionID);
				userState.setCustomerBalance(customerBalance);
				userState.setTransactionDate(billDate+":"+billTime);

			} catch (Exception e) {
				retVal = e.getMessage();
				i8SBSwitchControllerResponseVO.setError("true");
				logger.error(e);
			}

		}


		return retVal;
	}

	public void replacePlaceHolders(UserState userState, UssdMenuModel menuModel) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.replacePlaceHolders Start");
		}
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat stf=new SimpleDateFormat("h:mm a");


		if (menuModel.getMenuString().contains("<A_MSISDN>")) {
			if(userState.getMsisdn()!=null && !"".equals(userState.getMsisdn())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<A_MSISDN>", userState.getMsisdn()));
			}
		}
		if(menuModel.getMenuString().contains("<C_MSISDN>")){
			if(userState.getMsisdn()!=null && !"".equals(userState.getMsisdn())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<C_MSISDN>", userState.getMsisdn()));
			}
		}
		if(menuModel.getMenuString().contains("<MSISDN>")){
			if(userState.getMsisdn()!=null && !"".equals(userState.getMsisdn())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<MSISDN>", userState.getMsisdn()));
			}
		}
		if(menuModel.getMenuString().contains("<RA_MOB>")){
			if(userState.getReceiverAccountNo()!=null && !"".equals(userState.getReceiverAccountNo())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<RA_MOB>", userState.getReceiverAccountNo()));
			}
		}
		if(menuModel.getMenuString().contains("<W_NO>")){
			if(userState.getMsisdn()!=null && !"".equals(userState.getMsisdn())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<W_NO>", userState.getMsisdn()));
			}
		}

		if(menuModel.getMenuString().contains("<CORE_ACC_NO>")){
			if(userState.getCoreAccountID()!=null && !"".equals(userState.getCoreAccountID())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<CORE_ACC_NO>", userState.getCoreAccountID()));
			}
		}

		if(menuModel.getMenuString().contains("<B_MSISDIN>")){
			if(userState.getMsisdn()!=null && !"".equals(userState.getMsisdn())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<B_MSISDIN>", userState.getMsisdn()));
			}
		}

		if (menuModel.getMenuString().contains("<A_N>")){
			if(userState.getAgentName()!=null && !"".equals(userState.getAgentName())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<A_N>",
						userState.getAgentName()));
			}
		}
		if (menuModel.getMenuString().contains("<C_N>")){
			if(userState.getCustomerName()!=null && !"".equals(userState.getCustomerName())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<C_N>",
						userState.getCustomerName()));
			}
		}
		if (menuModel.getMenuString().contains("<T_A>")){
			menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<T_A>", String.valueOf(userState.getAmount())));

		}
		if (menuModel.getMenuString().contains("<DED_AMT>")){
			if(userState.getDeductionAmount()!=null && !"".equals(userState.getDeductionAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<DED_AMT>", userState.getDeductionAmount()));
			}else if(userState.getTPAM()!=null && !"".equals(userState.getTPAM())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<DED_AMT>", userState.getTPAM()));
			}

		}
		if (menuModel.getMenuString().contains("<FED_AMT>")){
			if(userState.getFedAmount()!=null && !"".equals(userState.getFedAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<FED_AMT>", userState.getFedAmount()));
			}

		}
		if (menuModel.getMenuString().contains("<T_C>")){
			if(userState.getTransactionCode()!=null && !"".equals(userState.getTransactionCode())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<T_C>", userState.getTransactionCode()));
			}

		}
		if (menuModel.getMenuString().contains("<T_ID>")){
			if(userState.getTransactionID()!=null && !"".equals(userState.getTransactionID())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<T_ID>", userState.getTransactionID()));
			}

		}
		if(menuModel.getMenuString().contains("<D>")){
			menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<D>",sdf.format(Calendar.getInstance().getTime()) ));
		}
		if(menuModel.getMenuString().contains("<T>")){
			menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<T>",stf.format(Calendar.getInstance().getTime()) ));
		}
		if(menuModel.getMenuString().contains("<T_D>")){
			if(userState.getTransactionDate()!=null && !"".equals(userState.getTransactionDate())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<T_D>",userState.getTransactionDate() ));
			}
		}
		if(menuModel.getMenuString().contains("<T_T>")){
		}
		if(userState.getTransactionTime()!=null && !"".equals(userState.getTransactionTime())){
			menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<T_T>",userState.getTransactionTime() ));
		}
		if(menuModel.getMenuString().contains("<V_D>")){
			if(userState.getValidDate()!=null && !"".equals(userState.getValidDate())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<V_D>",userState.getValidDate() ));
			}
		}
		if(menuModel.getMenuString().contains("<V_T>")){
			if(userState.getValidTime()!=null && !"".equals(userState.getValidTime())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<V_T>",userState.getValidTime()));
			}
		}
		if(menuModel.getMenuString().contains("<A_BAL>")){
			if(userState.getAgentBalance()!=null && !"".equals(userState.getAgentBalance())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<A_BAL>",userState.getAgentBalance() ));
			}
		}
		if(menuModel.getMenuString().contains("<C_BAL>")){
			if(userState.getCustomerBalance()!=null && !"".equals(userState.getCustomerBalance())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<C_BAL>",userState.getCustomerBalance()));
			}
		}
		if(menuModel.getMenuString().contains("<U_B_T>")){
			/*if(userState.getUtilityBillType()!=null && !"".equals(userState.getUtilityBillType())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_B_T>",userState.getUtilityBillType()));
			}*/
			if(userState.getUtilityCompanyName()!=null && !"".equals(userState.getUtilityCompanyName())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_B_T>",userState.getUtilityCompanyName()));
			}
		}
		if(menuModel.getMenuString().contains("<U_C_N>")){
			if(userState.getUtilityCompanyID()!=null && !"".equals(userState.getUtilityCompanyID())){
				String companyName=null;
				if(null!=UtilityCompanyEnum.lookup(userState.getUtilityCompanyID())){
					companyName=UtilityCompanyEnum.lookup(userState.getUtilityCompanyID()).name();
				}else if(null!=InternetCompanyEnum.lookup(userState.getUtilityCompanyID())){
					companyName=InternetCompanyEnum.lookup(userState.getUtilityCompanyID()).name();
				}
				companyName = companyName.replace("_BILL", "");
				companyName = companyName.replace("_", " ");
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_C_N>",companyName));
			}
		}
		if(menuModel.getMenuString().contains("<U_B_M>")){
			if(userState.getUtilityBillMonth()!=null && !"".equals(userState.getUtilityBillMonth())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_B_M>",userState.getUtilityBillMonth()));
			}
		}
		if(menuModel.getMenuString().contains("<U_B_A>")){
			if(userState.getBAMT()!=null && !"".equals(userState.getBAMT())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_B_A>",userState.getBAMT()));
			}
		}
		if(menuModel.getMenuString().contains("<U_D>")){
			if(userState.getValidDate()!=null && !"".equals(userState.getValidDate())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_D>",userState.getValidDate()));
			}
		}
		if(menuModel.getMenuString().contains("<U_S>")){
			if(userState.getUtilitySubscriber()!=null && !"".equals(userState.getUtilitySubscriber())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<U_S>",userState.getUtilitySubscriber()));
			}
		}
		if(menuModel.getMenuString().contains("<CON_NO>")){
			if(userState.getBillPaymentConsumerNumber()!=null && !"".equals(userState.getBillPaymentConsumerNumber())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<CON_NO>",userState.getBillPaymentConsumerNumber()));
			}
		}
		if(menuModel.getMenuString().contains("<C_CNIC>")){
			if(userState.getCustomerCNIC()!=null && !"".equals(userState.getCustomerCNIC())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<C_CNIC>",userState.getCustomerCNIC()));
			}
		}
		if(menuModel.getMenuString().contains("<R_CNIC>")){
			if(userState.getWalkinReceiverCNIC()!=null && !"".equals(userState.getWalkinReceiverCNIC())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<R_CNIC>",userState.getWalkinReceiverCNIC()));
			}
		}
		if(menuModel.getMenuString().contains("<S_CNIC>")){
			if(userState.getWalkinSenderCNIC()!=null && !"".equals(userState.getWalkinSenderCNIC())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<S_CNIC>",userState.getWalkinSenderCNIC()));
			}
		}
		if(menuModel.getMenuString().contains("<R_MSISDN>")){
			if(userState.getWalkinReceiverMSISDN()!=null && !"".equals(userState.getWalkinReceiverMSISDN())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<R_MSISDN>",userState.getWalkinReceiverMSISDN()));
			}
		}
		if(menuModel.getMenuString().contains("<S_MSISDN>")){
			if(userState.getWalkinSenderMSISDN()!=null && !"".equals(userState.getWalkinSenderMSISDN())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<S_MSISDN>",userState.getWalkinSenderMSISDN()));
			}
		}


		if(menuModel.getMenuString().contains("<TAMT>")){
			if(userState.getCwTotalAmountForCustomer() !=null && !"".equals(userState.getCwTotalAmountForCustomer())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<TAMT>",userState.getCwTotalAmountForCustomer()));
			}else if(userState.getTAMT() !=null && !"".equals(userState.getTAMT())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<TAMT>",userState.getTAMT()));
			}
		}

		if(menuModel.getMenuString().contains("<TRX_AMT>")){
			if(userState.getTransactionAmount() !=null && !"".equals(userState.getTransactionAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<TRX_AMT>",userState.getTransactionAmount()));
			}
		}

		if(menuModel.getMenuString().contains("<TRX_FEE>")){
			if(userState.getFedAmount() !=null && !"".equals(userState.getFedAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<TRX_FEE>",userState.getFedAmount()));
			}
		}

		if(menuModel.getMenuString().contains("<TRX_FEE>")){
			if(userState.getTransactionProcessingAmount() !=null && !"".equals(userState.getTransactionProcessingAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<TRX_FEE>",userState.getTransactionProcessingAmount()));
			}
		}
		if(menuModel.getMenuString().contains("<TRX_COM>")){
			if(userState.getCommissionAmount() !=null && !"".equals(userState.getCommissionAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<TRX_COM>",userState.getCommissionAmount()));
			}
		}

		if(menuModel.getMenuString().contains("<EC_ID>")){
			if(userState.getChallanID() !=null && !"".equals(userState.getChallanID())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<EC_ID>",userState.getChallanID()));
			}
		}
		if (menuModel.getMenuString().contains("<CUST_ID>")){
			if(null!=userState.getCustomerId() && !"".equals(userState.getCustomerId())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<CUST_ID>", userState.getCustomerId()));
			}
		}

		if(menuModel.getMenuString().contains("<AMT>")){
			if(userState.getTotalAmount() !=null && !"".equals(userState.getTotalAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<AMT>",userState.getTotalAmount()));
			}
		}
		if(menuModel.getMenuString().contains("<MIN_AMNT>")){
			if(userState.getMinAmount() !=null && !"".equals(userState.getMinAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<MIN_AMNT>",userState.getMinAmount()));
			}
		}

		if(menuModel.getMenuString().contains("<DUE_DATE>")){
			if(userState.getValidDate() !=null && !"".equals(userState.getValidDate())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<DUE_DATE>",userState.getValidDate()));
			}
		}

		if(menuModel.getMenuString().contains("<C_C_B>")){
			if(userState.getCreditCardAmount() !=null && !"".equals(userState.getCreditCardAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<C_C_B>",userState.getCreditCardAmount()));
			}
		}

		if(menuModel.getMenuString().contains("<CREDIT_CARD>")){
			if(userState.getCreditCardNumber() !=null && !"".equals(userState.getCreditCardNumber())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<CREDIT_CARD>",userState.getCreditCardNumber()));
			}
		}


		if(menuModel.getMenuString().contains("<B_A>")){
			if(userState.getUtilityBillAmount() !=null && !"".equals(userState.getUtilityBillAmount())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<B_A>",userState.getUtilityBillAmount()));
			}
		}

		if(menuModel.getMenuString().contains("<B_COMP>")){
			if(userState.getUtilityCompanyName() !=null && !"".equals(userState.getUtilityCompanyName())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<B_COMP>",userState.getUtilityCompanyName()));
			}
		}
//
		if(menuModel.getMenuString().contains("<B_S>")){
			if(userState.getIsBillPaid() !=null && !"".equals(userState.getIsBillPaid())){

				String billPaid = null;
				if(userState.getIsBillPaid().equals("0"))
					billPaid = "Not-Paid";
				else
					billPaid = "Paid";
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<B_S>",billPaid));
			}
		}

		if(menuModel.getMenuString().contains("<ACCNO>")){
			if(userState.getWalkinReceiverCNIC() !=null && !"".equals(userState.getWalkinReceiverCNIC())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<ACCNO>",userState.getWalkinReceiverCNIC()));
			}
		}

		if(menuModel.getMenuString().contains("<NAME>")){
			if(userState.getRecipientName() !=null && !"".equals(userState.getRecipientName())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<NAME>",userState.getRecipientName()));
			}
		}

		if(menuModel.getMenuString().contains("<B_ACC_NO>")){
			if(userState.getCoreAccountID() !=null && !"".equals(userState.getCoreAccountID())){
				menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<B_ACC_NO>",userState.getCoreAccountID()));
			}
		}

        if(menuModel.getMenuString().contains("<GOV_PAY_TYPE>")){
            if(userState.getGovPaymentType() !=null && !"".equals(userState.getGovPaymentType())){
                menuModel.setMenuString(menuModel.getMenuString().replaceFirst("<GOV_PAY_TYPE>",userState.getGovPaymentType()));
            }
        }

		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.replacePlaceHolders End");
		}
	}
	public BaseWrapper findMenu(Long screenCode) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findMenu Start");
		}
		BaseWrapper searchBaseWrapper = new BaseWrapperImpl();
		// prepare model to menu
		UssdMenuModel ussdMenuModel = new UssdMenuModel();
		ussdMenuModel.setScreenCode(screenCode);
		searchBaseWrapper.setBasePersistableModel(ussdMenuModel);

		try {
			searchBaseWrapper = ussdMenuManager.findMenu(searchBaseWrapper);
		} catch (Exception e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findMenu End");
		}
		return searchBaseWrapper;
	}
	public BaseWrapper findPreviousMenu(Long screenOutputCode,Long appUserTypeID, Long previousScreenCode) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findPreviousMenu Start");
		}
		BaseWrapper retVal=null;
		try {
			int menuCount=ussdMenuManager.findPreviousMenuCount(screenOutputCode, appUserTypeID);
			if( menuCount > 1){
				retVal = new BaseWrapperImpl();
				UssdMenuModel ussdMenuModel = new UssdMenuModel();
				ussdMenuModel.setScreenCode(previousScreenCode);
				retVal.setBasePersistableModel(ussdMenuModel);
				retVal = ussdMenuManager.findMenu(retVal);
			}else{
				retVal=ussdMenuManager.findPreviousMenu(screenOutputCode, appUserTypeID);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findPreviousMenu End");
		}
		return retVal;
	}
	public BaseWrapper findPreviousMenu(UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findPreviousMenu Start");
		}
		BaseWrapper searchBaseWrapper = new BaseWrapperImpl();
		// prepare model to get previous menu
		UssdMenuModel ussdMenuModel = new UssdMenuModel();
		ussdMenuModel.setScreenCode(userState.getScreenCode());
		searchBaseWrapper.setBasePersistableModel(ussdMenuModel);

		try {
			searchBaseWrapper = ussdMenuManager.findMenu(searchBaseWrapper);
		} catch (Exception e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findPreviousMenu End");
		}
		return searchBaseWrapper;
	}
	public BaseWrapper findChangePinMenu(long screenCodeForChagePinMenu) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findChangePinMenu Start");
		}
		BaseWrapper searchBaseWrapper = new BaseWrapperImpl();

		UssdMenuModel ussdMenuModel = new UssdMenuModel();
		ussdMenuModel.setScreenCode(screenCodeForChagePinMenu);
		searchBaseWrapper.setBasePersistableModel(ussdMenuModel);
		try {

			searchBaseWrapper = ussdMenuManager.findMenu(searchBaseWrapper);
		} catch (Exception e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findChangePinMenu End");
		}
		return searchBaseWrapper;
	}

	public BaseWrapper findNextMenu(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState, Long appUserTypeID,boolean isFirstCall) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findNextMenu Start");
		}
		BaseWrapper searchBaseWrapper = new BaseWrapperImpl();
		UssdMenuMappingModel mappingModel = getMenuMappingModel(i8SBSwitchControllerRequestVO, userState, appUserTypeID);
		searchBaseWrapper.setBasePersistableModel(mappingModel);
		try {
			searchBaseWrapper = ussdMenuManager.findNextMenu(searchBaseWrapper);
		} catch (Exception e) {
			logger.error(e);

		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.findNextMenu End");
		}
		return searchBaseWrapper;
	}

	private UssdMenuMappingModel getMenuMappingModel(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState, Long appUserTypeID) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.getMenuMappingModel Start");
		}
		UssdMenuMappingModel mappingModel = new UssdMenuMappingModel();
		mappingModel.setScreenCodeInput(userState.getScreenCode());
		mappingModel.setOptions(i8SBSwitchControllerRequestVO.getuSSDRequestString());
		mappingModel.setAppUserTypeId(appUserTypeID);
		logger.debug(userState.getScreenCode()+"-----------"+mappingModel.getOptions()+"-------------------------"+appUserTypeID);

		if(logger.isDebugEnabled()){
			logger.debug("UssdRequestHandler.getMenuMappingModel End");
		}
		return mappingModel;
	}
	public String validateGeneralInput(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		return (new UssdValidator().validateGeneralInput(i8SBSwitchControllerRequestVO, userState));
	}
	public String validateCustomInput(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState, ActionLogManager actionLogManager,I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
		return (new UssdValidator().validateCustomInput(i8SBSwitchControllerRequestVO,commonCommandManager, userState,commandManager, actionLogManager,i8SBSwitchControllerResponseVO));
	}


	public MfsRequestHandler getMfsReqHandler() {
		return mfsReqHandler;
	}
	public void setMfsReqHandler(MfsRequestHandler mfsReqHandler) {
		this.mfsReqHandler = mfsReqHandler;
	}
	public CommonCommandManager getCommonCommandManager() {
		return commonCommandManager;
	}
	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}
	public UssdMenuManager getUssdMenuManager() {
		return ussdMenuManager;
	}
	public void setUssdMenuManager(UssdMenuManager ussdMenuManager) {
		this.ussdMenuManager = ussdMenuManager;
	}
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	private void actionLogBeforeStart(ActionLogModel actionLogModel)
	{


		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp( new java.util.Date().getTime() ));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if(actionLogModel.getActionLogId() != null)
		{
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}
	private void actionLogAfterEnd(ActionLogModel actionLogModel)
	{
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp( new java.util.Date().getTime() ));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}

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

	private String removeParams(String xmlStr)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLoginCommand.removeParams()");
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
			logger.debug("End of MfsLoginCommand.removeParams()");
		}
		return xmlStr;
	}
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void populateUserStateWithBankInfo(UserState userState)throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		logger.info(" AppUserModel" +ThreadLocalAppUser.getAppUserModel());
		logger.info(" UserDeviceAccountsModel" +ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel());
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			baseWrapper.putObject(KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
		}
		else
		{
			baseWrapper.putObject(KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
		}
//		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
		String bankInfo = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_GT_ACC_INFO);

		String bankId = MiniXMLUtil.getTagTextValue(bankInfo, "//banks/bank/@id");
		String accId = MiniXMLUtil.getTagTextValue(bankInfo, "//banks/bank/acc/@id");
		if(bankId == null || accId ==null || "".equals(bankId) || "".equals(accId)){
			throw new CommandException(this.getMessageSource().getMessage("accountNotLinked", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}else{
			String pinChangeRequired=MiniXMLUtil.getTagTextValue(bankInfo, "//banks/bank/acc/@pinChReq");
			userState.setBankId(bankId);
			userState.setAccId(accId);
			userState.setChangePinRequired("1".equals(pinChangeRequired));
		}

	}
	public UssdUserStateManager getUssdUserStateManager() {
		return ussdUserStateManager;
	}
	public void setUssdUserStateManager(UssdUserStateManager ussdUserStateManager) {
		this.ussdUserStateManager = ussdUserStateManager;
	}
	public CommissionRateDAO getCommissionRateDAO() {
		return commissionRateDAO;
	}
	public void setCommissionRateDAO(CommissionRateDAO commissionRateDAO) {
		this.commissionRateDAO = commissionRateDAO;
	}

}
