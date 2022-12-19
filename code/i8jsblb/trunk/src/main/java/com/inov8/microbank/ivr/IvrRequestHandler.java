package com.inov8.microbank.ivr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.NadraCompanyEnum;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalEncryptionType;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.mfs.MfsRequestHandler;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.webservice.bean.IvrResponseDTO;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.microbank.server.webserviceclient.ivr.IvrWebService;
import com.inov8.microbank.server.webserviceclient.ivr.IvrWebServiceImplService;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import com.thoughtworks.xstream.XStream;



public class IvrRequestHandler {
	protected final Log logger = LogFactory.getLog(getClass());
	private CommonCommandManager commonCommandManager;
	private MfsRequestHandler mfsReqHandler;
	private CommandManager commandManager;
	private ActionLogManager actionLogManager;
	private MessageSource messageSource;
	private static final String PIN_RETRY_COUNT_EXHAUSTED = "9999";
	private static final String INCORRECT_PIN = "7022";
	private static final int MAX_RETRY_ATTEMPTS = 3;
	private static final String IVR_CALL_SUCCESS = "success";
	private static final String SUCCESS_RESPONSE = "0000";
	private static final String GENERAL_ERROR_RESPONSE = "9001";
	private static final String MINI_STATEMENT_PRODUCT_ID = "122";
	private static final String CHANGE_PIN_PRODUCT_ID = "123";
	private static final String CHECK_BALANCE_PRODUCT_ID = CommandFieldConstants.CMD_CHK_ACC_BAL;
	private static final String CMD_CUSTOMER_CREATE_PIN = CommandFieldConstants.CMD_CUSTOMER_CREATE_PIN;
	private static final String CMD_AGENT_CREATE_PIN = CommandFieldConstants.CMD_AGENT_CREATE_PIN;
	private static final String CMD_HANDLER_CREATE_PIN = CommandFieldConstants.CMD_HANDLER_CREATE_PIN;
	private static final Object NEW_PIN_PRODUCT_ID = "125";
	private static final Object REGEN_PIN_PRODUCT_ID = "126";
    private static final Object CUST_NEW_PIN_PRODUCT_ID = "134";
	IvrWebServiceImplService service;
	
	
	public void makeIvrRequest(IvrRequestDTO ivrRequestDTO) throws FrameworkCheckedException, Exception{
		try {
			
			loadRequiredBeans();
			IvrWebService ivrWebService = service.getIvrWebServicePort();
			logger.info("[IvrRequestHandler.makeIvrRequest] Hitting IVR web service. Transaction ID:" + ivrRequestDTO.getTransactionId() + " Customer Mobile:" + ivrRequestDTO.getCustomerMobileNo() + " Agent ID:" + ivrRequestDTO.getAgentId() + " Retry count: " + ivrRequestDTO.getRetryCount());
			String response = ivrWebService.handleRequest(ivrRequestDTO);
			logger.info("[IvrRequestHandler.makeIvrRequest] Response from IVR web service:" + response + " Transaction ID:" + ivrRequestDTO.getTransactionId());
			if(response != null && IVR_CALL_SUCCESS.equalsIgnoreCase(response)){
				//no action reqirued.
				
			}else{
				if (ivrRequestDTO != null && ivrRequestDTO.getProductId() != null && ! ivrRequestDTO.getProductId().toString().equals(CHECK_BALANCE_PRODUCT_ID) 
						&& ! ivrRequestDTO.getProductId().toString().equals(MINI_STATEMENT_PRODUCT_ID)
						&& ! ivrRequestDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID)
						&& ! ivrRequestDTO.getProductId().toString().equals(REGEN_PIN_PRODUCT_ID)
						&& ! ivrRequestDTO.getProductId().toString().equals(NEW_PIN_PRODUCT_ID)
                        && ! ivrRequestDTO.getProductId().toString().equals(CUST_NEW_PIN_PRODUCT_ID)
                        ) {
					//update transaction with status IVR_AUTHORIZATION_ABORTED
					updateTransactionStatus(ivrRequestDTO.getTransactionId(), SupplierProcessingStatusConstants.IVR_VALIDATION_ABORTED);
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			//update transaction with status IVR_AUTHORIZATION_ABORTED
			if (ivrRequestDTO != null && ivrRequestDTO.getRetryCount() == 0 &&  ! ivrRequestDTO.getProductId().toString().equals(CHECK_BALANCE_PRODUCT_ID) 
					&& ! ivrRequestDTO.getProductId().toString().equals(MINI_STATEMENT_PRODUCT_ID)
					&& ! ivrRequestDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID)
					&& ! ivrRequestDTO.getProductId().toString().equals(REGEN_PIN_PRODUCT_ID)
					&& ! ivrRequestDTO.getProductId().toString().equals(NEW_PIN_PRODUCT_ID)
                    && ! ivrRequestDTO.getProductId().toString().equals(CUST_NEW_PIN_PRODUCT_ID)) {
				updateTransactionStatus(ivrRequestDTO.getTransactionId(), SupplierProcessingStatusConstants.IVR_VALIDATION_ABORTED);
			}
		}
		
	}
	
	
	public IvrRequestDTO handleIvrResponse(IvrResponseDTO responseDTO){
		loadRequiredBeans();
		byte enc_type = new Byte(XMLConstants.ENCRYPTION_TYPE_AES).byteValue();
		ThreadLocalEncryptionType.setEncryptionType(enc_type);
		responseDTO.getPin();
		logger.info("[IvrRequestHandler.handleIvrResponse] Processing Response from IVR System. Transaction ID:" + responseDTO.getTransactionId() + " Retry count: " + responseDTO.getRetryCount());
		String agentMobile = responseDTO.getAgentMobileNo();
		String customerMobile = responseDTO.getCustomerMobileNo();
		String handlerMobile = responseDTO.getHandlerMobileNo();
		AppUserModel customerAppUserModel = new AppUserModel();
		AppUserModel agentAppUserModel = null;
		AppUserModel handlerAppUserModel = null;
		IvrRequestDTO requestDTO = populateIVRRequestDTO(responseDTO);
		
		try {
			if (agentMobile != null) {//can be null in case of Check balance, change pin or mini statement.
				
				if(responseDTO.getProductId().longValue() == ProductConstantsInterface.TELLER_CASH_OUT.longValue()) {
					agentAppUserModel = loadAppUserByMobile(agentMobile, UserTypeConstantsInterface.BANK);
				}
				if (agentAppUserModel == null) {
					agentAppUserModel = loadAppUserByMobile(agentMobile, UserTypeConstantsInterface.RETAILER);
				}
				if (agentAppUserModel == null) { //added by mudassir, on demand of turab by approval omar butt, due to shortage of time
					agentAppUserModel = loadAppUserByMobile(agentMobile, UserTypeConstantsInterface.HANDLER);
				}
			}
			if (handlerMobile != null) {
				handlerAppUserModel = loadAppUserByMobile(handlerMobile, UserTypeConstantsInterface.HANDLER);
			}
			
			customerAppUserModel = loadAppUserByMobile(customerMobile, UserTypeConstantsInterface.CUSTOMER);
			ThreadLocalAppUser.setAppUserModel(customerAppUserModel);//set in threadlocal to resolve issues in verifly calls where threadloacal might be used
			
			if(responseDTO.getProductId() != null
                    && (responseDTO.getProductId().toString().equals(NEW_PIN_PRODUCT_ID)
                        || responseDTO.getProductId().toString().equals(CUST_NEW_PIN_PRODUCT_ID)
                        || responseDTO.getProductId().toString().equals(REGEN_PIN_PRODUCT_ID))
                    && StringUtils.isNotEmpty(responseDTO.getNewPin())){
			try {
//				customerAppUserModel = loadAppUserByMobile(customerMobile, UserTypeConstantsInterface.CUSTOMER);
//				ThreadLocalAppUser.setAppUserModel(customerAppUserModel);
				if(agentMobile != null && !"".equals(agentMobile)){
					ThreadLocalAppUser.setAppUserModel(agentAppUserModel);
				}else if(handlerMobile != null && !"".equals(handlerMobile)){
					ThreadLocalAppUser.setAppUserModel(handlerAppUserModel);
				}else{
					ThreadLocalAppUser.setAppUserModel(customerAppUserModel);
				}
				String plainPIN = responseDTO.getNewPin();
				if (validatePlainPin(plainPIN)) {
					this.insertActionLog(responseDTO, actionLogManager);
					responseDTO.setNewPin(plainPIN); //plain pin will be sent and PVV will be updated according to this PIN
					String response = processTransaction(responseDTO);
					
					if (StringUtils.isEmpty(response)) {
						requestDTO.setTransactionResponse(SUCCESS_RESPONSE);
					}else{
						requestDTO.setTransactionResponse(GENERAL_ERROR_RESPONSE);
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
						messageList.add(new SmsMessage(requestDTO.getCustomerMobileNo(), this.getMessageSource().getMessage("GENERAL_ERROR_IVR", null,null)));
						baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
						commonCommandManager.sendSMS(baseWrapper);
					}
					
					
					
				}else{
					requestDTO.setTransactionResponse(INCORRECT_PIN);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return requestDTO;
		} else if (responseDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID) && StringUtils.isNotEmpty(responseDTO.getNewPin())) {
				//Process request for Change Pin. existing Pin validation already performed.
				String plainPIN = responseDTO.getPin();
				String newPin = responseDTO.getNewPin();
				if (validatePlainPin(plainPIN) && validatePlainPin(newPin)) {
//					logger.debug("[IvrRequestHandler.handleIvrResponse] Current Pin " + responseDTO.getPin() + " New Pin: " + responseDTO.getNewPin());
					String pin = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, plainPIN);
					newPin = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, responseDTO.getNewPin());
					this.insertActionLog(responseDTO, actionLogManager);
					responseDTO.setPin(pin);
					responseDTO.setNewPin(newPin);
					String response = processTransaction(responseDTO);
					
					if (StringUtils.isEmpty(response)) {
						requestDTO.setTransactionResponse(SUCCESS_RESPONSE);
					}else{
						requestDTO.setTransactionResponse(GENERAL_ERROR_RESPONSE);
					}
					
					
					
				}else{
					requestDTO.setTransactionResponse(INCORRECT_PIN);
				}

				
			}else {
			
				//validate customer pin
				String pinValidationResponse = validatePin(responseDTO, customerAppUserModel);
				
				//Pin Successfully validated
				if (StringUtils.isEmpty(pinValidationResponse)) {
					requestDTO.setIsPinAuthenticated(true);
					if (agentMobile == null) {//is null when check balance and mini statement requests 
						ThreadLocalAppUser.setAppUserModel(customerAppUserModel);
					}else{
						ThreadLocalAppUser.setAppUserModel(agentAppUserModel);
					}
					
					String response = null;
					if (responseDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID) && StringUtils.isEmpty(responseDTO.getNewPin())) {
						//Skin processing if Change pin request as new pin is not yet provided by customer.
					}else{
						response = processTransaction(responseDTO);
					}
					
					if (StringUtils.isEmpty(response)) {
						requestDTO.setTransactionResponse(SUCCESS_RESPONSE);
					}else{
						requestDTO.setTransactionResponse(response);
					}
					
				}else{//Invalid pin entered.
					
					if(pinValidationResponse.equals(INCORRECT_PIN)){
						requestDTO.setTransactionResponse(INCORRECT_PIN);
						
						if (responseDTO.getRetryCount().intValue() == MAX_RETRY_ATTEMPTS) {
							requestDTO.setTransactionResponse(PIN_RETRY_COUNT_EXHAUSTED);
							
							if (! responseDTO.getProductId().toString().equals(CHECK_BALANCE_PRODUCT_ID) 
									&& ! responseDTO.getProductId().toString().equals(MINI_STATEMENT_PRODUCT_ID)
									&& ! responseDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID)) {
								updateTransactionStatus(responseDTO.getTransactionId(), SupplierProcessingStatusConstants.FAILED);
								
							}
							
							requestDTO.setIsCredentialsExpired(true);
							requestDTO.setIsPinAuthenticated(false);
						}
						
						requestDTO.setRetryCount(responseDTO.getRetryCount());
					}
				}
			}
			
			
			
			
		} catch (Exception e) {
			
			if (requestDTO != null && requestDTO.getProductId() != null 
					&& ! requestDTO.getProductId().toString().equals(CHECK_BALANCE_PRODUCT_ID) 
					&& ! requestDTO.getProductId().toString().equals(MINI_STATEMENT_PRODUCT_ID)
					&& ! requestDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID)
					&& ! requestDTO.getProductId().toString().equals(REGEN_PIN_PRODUCT_ID)
					&& ! requestDTO.getProductId().toString().equals(NEW_PIN_PRODUCT_ID)
                    && ! requestDTO.getProductId().toString().equals(CUST_NEW_PIN_PRODUCT_ID)) {
				try {
					updateTransactionStatus(responseDTO.getTransactionId(), SupplierProcessingStatusConstants.FAILED);
				} catch (FrameworkCheckedException e1) {
					logger.error("[IvrRequestHandler.handleIvrResponse] Error in updating Transaction Status to Failed. Transaction ID:" + responseDTO.getTransactionId() + " Exception:" + e1.getMessage());
				}
			}
			
			if (requestDTO.getTransactionResponse() == null) {
				requestDTO.setTransactionResponse(GENERAL_ERROR_RESPONSE);
			}
			logger.error("[IvrRequestHandler.handleIvrResponse] Exception occured while Processing Response. Transaction ID:" + responseDTO.getTransactionId() + " Retry count: " + responseDTO.getRetryCount());
			e.printStackTrace();
		
		}finally{
			// Remove the appUserModel and other models from ThreadLocal
			ThreadLocalAppUser.remove();
			ThreadLocalActionLog.remove();
			ThreadLocalUserDeviceAccounts.remove();
			ThreadLocalEncryptionType.remove();
			
		}
		logger.info("[IvrRequestHandler.handleIvrResponse] Sending Response:" + requestDTO.getTransactionResponse() + " back to IVR System. Transaction ID:" + responseDTO.getTransactionId() + " Retry count: " + responseDTO.getRetryCount());
		
		return requestDTO;
	}
	
	private AppUserModel loadAppUserByMobile(String mobileNo, Long appUserTypeId) throws Exception{
		AppUserModel appUserModel = new AppUserModel();
		appUserModel = commonCommandManager.loadAppUserByQuery(mobileNo, appUserTypeId);
		
		return appUserModel;
	}
	public String validatePin(IvrResponseDTO responseDTO, AppUserModel customerAppUserModel) {
		
		if (ThreadLocalAppUser.getAppUserModel() != null) {
			logger.info("[IvrRequestHandler.validatePin] Going to validate Pin. AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		}else{
			logger.info("[IvrRequestHandler.validatePin] Going to validate Pin. Customer Mobile No:" + responseDTO.getCustomerMobileNo());
		}
		this.insertActionLog(responseDTO, actionLogManager);
		
		String plainPin = responseDTO.getPin();
		Integer retryCount = responseDTO.getRetryCount();
		
		String retVal=null;
//		String pin = MfsWebUtil.encryptPin(plainPin);
		String pin = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, plainPin); 
			try
			{
				Boolean notError = this.validatePlainPin(plainPin) && this.verifyPIN(pin, customerAppUserModel);
				if(!notError)
				{
					logger.info("[IvrRequestHandler.validatePin] Invalid Pin. Transaction ID:" + responseDTO.getTransactionId());
					
					retryCount += 1;
					responseDTO.setRetryCount(retryCount);
					retVal= INCORRECT_PIN;
					if(retryCount == MAX_RETRY_ATTEMPTS){
						UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel(); 
						userDeviceAccountsModel.setAppUserId(customerAppUserModel.getAppUserId());
						userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
						SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
						sbWrapper.setBasePersistableModel(userDeviceAccountsModel);
						
						sbWrapper = commonCommandManager.loadUserDeviceAccounts(sbWrapper);
						if(null != sbWrapper.getBasePersistableModel())
						{
							if (sbWrapper.getCustomList() != null && sbWrapper.getCustomList().getResultsetList().size() > 0) {
								userDeviceAccountsModel = (UserDeviceAccountsModel) sbWrapper.getCustomList().getResultsetList().get(0);
								userDeviceAccountsModel.setCredentialsExpired(true);
								BaseWrapper baseWrapper = new BaseWrapperImpl();
								baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
								
								logger.info("[IvrRequestHandler.validatePin] Blocking Account after retry count exhausted. Mobile No:" + customerAppUserModel.getMobileNo() + " Transaction ID:" + responseDTO.getTransactionId());
								
								commonCommandManager.updateUserDeviceAccounts(baseWrapper);
							}else{
								retVal= GENERAL_ERROR_RESPONSE;
								logger.error("[IvrRequestHandler.validatePin] No Entry found in User Device Accounts. Mobile No: " + customerAppUserModel.getAppUserId());
							}
						}else{
							logger.error("[IvrRequestHandler.validatePin] No Entry found in User Device Accounts. Mobile No: " + customerAppUserModel.getAppUserId());
							retVal= GENERAL_ERROR_RESPONSE;
						}

//						retVal= PIN_RETRY_COUNT_EXHAUSTED;
					}
					
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.error("[IvrRequestHandler.validatePin] Incorrect Pin Entered. LoggedIn AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + e.getMessage());
				retVal= INCORRECT_PIN;
			}
			
			if(logger.isDebugEnabled()){
				logger.debug("IvrRequestHandler.validatePin End");
			}
		return retVal;
	}
	
	
	private Boolean verifyPIN(String pin, AppUserModel customerAppUserModel)throws Exception
	{
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		sma.setCustomerId(customerAppUserModel.getCustomerId());
		
		logger.info("[IvrRequestHandler.verifyPIN] AppUserID: " + customerAppUserModel.getAppUserId());
		
		sma.setDeleted(false);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(sma);
		searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
		if(null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
		{
			sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setCustomerId(customerAppUserModel.getCustomerId());
		
		accountInfoModel.setAccountNick(sma.getName());
		accountInfoModel.setOldPin(pin);
		
		LogModel logModel = new LogModel();
		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//		logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		
		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		veriflyBaseWrapper.setLogModel(logModel);
		
		veriflyBaseWrapper.setBasePersistableModel(sma);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(sma);
		veriflyBaseWrapper.setBasePersistableModel(null);
		VeriflyManager veriflyManager = commonCommandManager.loadVeriflyManagerByAccountId(baseWrapper);
//		veriflyManager.verifyPIN(veriflyBaseWrapper);
		
		veriflyBaseWrapper = commonCommandManager.verifyVeriflyPin(veriflyManager, veriflyBaseWrapper);
		
		/*if(veriflyBaseWrapper.isErrorStatus()){
			setThreadLocalAccountInfoModel(userState, veriflyBaseWrapper.getAccountInfoModel());
		}*/
		if(logger.isDebugEnabled()){
			logger.debug("IvrRequestHandler.verifyPIN End");
		}
		return veriflyBaseWrapper.isErrorStatus();
		
	}
	
	private void loadRequiredBeans(){
		if (commonCommandManager == null) {
			commonCommandManager = getCommonCommandManager();
		}
		
		if (commandManager == null) {
			commandManager = getCommandManager();
		}
	}
	
	private boolean validatePlainPin(String plainPIN) {
		return plainPIN != null && !"".equals(plainPIN)&& StringUtil.isInteger(plainPIN) && plainPIN.length() == 4;
	}
	
	private void insertActionLog(IvrResponseDTO responseDTO, ActionLogManager actionLogManager)
	{
		String encPin = responseDTO.getPin();
		String newEncPin = responseDTO.getNewPin();
		responseDTO.setPin("****");
		responseDTO.setNewPin("****");
		
		XStream xstream = new XStream();
		ActionLogModel actionLogModel = new ActionLogModel();
		
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(responseDTO), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel, actionLogManager);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		responseDTO.setPin(encPin);
		responseDTO.setNewPin(newEncPin);
	}
	
	
	private void actionLogBeforeStart(ActionLogModel actionLogModel, ActionLogManager actionLogManager)
	{
		
		
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp( new java.util.Date().getTime() ));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel, actionLogManager);
		if(actionLogModel.getActionLogId() != null)
		{
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel, ActionLogManager actionLogManager)
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try
		{
			baseWrapper = actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel)baseWrapper.getBasePersistableModel();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();

		}
		return actionLogModel;
	}
	
	private TransactionModel loadTransactionModelByTransactionCode(String transactionCode) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TransactionModel txModel = new TransactionModel();
		TransactionCodeModel txCodeModel = new TransactionCodeModel();
		txCodeModel.setCode(transactionCode);
		baseWrapper.setBasePersistableModel(txCodeModel);
		baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
		
		txCodeModel = (TransactionCodeModel)baseWrapper.getBasePersistableModel();
		
		if (txCodeModel != null) {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(txCodeModel);
			searchBaseWrapper = commonCommandManager.loadTransactionByTransactionCode(searchBaseWrapper);
			
			txModel = (TransactionModel)searchBaseWrapper.getBasePersistableModel();
		}
		
		return txModel;
		
		
	}
	private CommonCommandManager getCommonCommandManager() {
		ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		CommonCommandManager commonCommandManager = (CommonCommandManager) webApplicationContext.getBean("commonCommandManager");
		return commonCommandManager;
	}

	private CommandManager getCommandManager() {
		ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		CommandManager commandManager = (CommandManager) webApplicationContext.getBean("cmdManager");
		return commandManager;
	}
	
	private void updateTransactionStatus(String transactionId, Long statusId) throws FrameworkCheckedException{
		TransactionModel txModel = loadTransactionModelByTransactionCode(transactionId);
		List<Long> transactionIds = new ArrayList<Long>();
		transactionIds.add(txModel.getTransactionId());
		commonCommandManager.updateTransactionProcessingStatus(statusId, transactionIds);
		
	}

	private IvrRequestDTO populateIVRRequestDTO(IvrResponseDTO responseDTO){
		IvrRequestDTO dto = new IvrRequestDTO();
		dto.setAgentId(responseDTO.getAgentId());
		dto.setAgentMobileNo(responseDTO.getAgentMobileNo());
		dto.setHandlerMobileNo(responseDTO.getHandlerMobileNo());
		dto.setAmount(responseDTO.getAmount());
		dto.setCharges(responseDTO.getCharges());
		dto.setCustomerMobileNo(responseDTO.getCustomerMobileNo());
		dto.setProductId(responseDTO.getProductId());
		dto.setTransactionId(responseDTO.getTransactionId());
		dto.setPin(responseDTO.getPin());
		dto.setIsPinAuthenticated(false);
		dto.setRetryCount(responseDTO.getRetryCount());
		dto.setPin(responseDTO.getPin());
		
		return dto;
	}
	
	
	private String processTransaction(IvrResponseDTO responseDTO){
		String response = "";
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, responseDTO.getProductId()) ;
		baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALL_PAY ) ;
		baseWrapper.putObject( CommandFieldConstants.KEY_AGENT_MOBILE, responseDTO.getAgentMobileNo() ) ;
		baseWrapper.putObject( CommandFieldConstants.KEY_TX_CODE, responseDTO.getTransactionId() ) ;
		baseWrapper.putObject( CommandFieldConstants.KEY_IVR_RESPONSE, Boolean.TRUE ) ;
		baseWrapper.putObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE, responseDTO.getCustomerMobileNo() ) ;
		baseWrapper.putObject( CommandFieldConstants.KEY_ENCRYPTION_TYPE, XMLConstants.ENCRYPTION_TYPE_AES) ;
		
		try{
			
			if (responseDTO.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH) {
				
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_ACCOUNT_TO_CASH);
				System.out.println("Response after execution" + response);
			}else if (responseDTO.getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT) {
				
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_ACCOUNT_TO_ACCOUNT);
				
			}else if (responseDTO.getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {
				
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_BB_TO_CORE_AC);
				
			}else if (responseDTO.getProductId().longValue() == ProductConstantsInterface.CASH_WITHDRAWAL) {
				
				baseWrapper.putObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE, responseDTO.getCustomerMobileNo() ) ;
				response = commandManager.executeCommand(baseWrapper, "116");//FIXME use constant
				
			}else if (responseDTO.getProductId().longValue() == ProductConstantsInterface.RETAIL_PAYMENT) {
			
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_AGENT_RETAIL_PAYMENT);
				
			}else if (responseDTO.getProductId().toString().equals(CHECK_BALANCE_PRODUCT_ID)) {
				
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
				
			}else if (responseDTO.getProductId().toString().equals(MINI_STATEMENT_PRODUCT_ID)) {
				
				response = commandManager.executeCommand(baseWrapper, MINI_STATEMENT_PRODUCT_ID);
			
			}else if (responseDTO.getProductId().toString().equals("50035")) {
				
				response = commandManager.executeCommand(baseWrapper, "138");
			
			}else if (responseDTO.getProductId().toString().equals(CHANGE_PIN_PRODUCT_ID)) {
				
				baseWrapper.putObject( CommandFieldConstants.KEY_PIN, responseDTO.getPin()) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_NEW_PIN, responseDTO.getNewPin() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_CONF_PIN, responseDTO.getNewPin() ) ;
				response = commandManager.executeCommand(baseWrapper, CHANGE_PIN_PRODUCT_ID);
			
			}else if (responseDTO.getProductId().toString().equals(NEW_PIN_PRODUCT_ID)
                    || responseDTO.getProductId().toString().equals(CUST_NEW_PIN_PRODUCT_ID)
                    || responseDTO.getProductId().toString().equals(REGEN_PIN_PRODUCT_ID)) {
				baseWrapper.putObject( CommandFieldConstants.KEY_PIN, responseDTO.getPin()) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_NEW_PIN, responseDTO.getNewPin() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_CONF_PIN, responseDTO.getNewPin() ) ;
				
				if(null != responseDTO.getHandlerMobileNo() && !"".equals(responseDTO.getHandlerMobileNo())){
					baseWrapper.putObject( CommandFieldConstants.KEY_HANDLER_MOB_NO, responseDTO.getHandlerMobileNo() ) ;
					response = commandManager.executeCommand(baseWrapper, CMD_HANDLER_CREATE_PIN);
				}else if(null != responseDTO.getAgentMobileNo() && !"".equals(responseDTO.getAgentMobileNo())){
						baseWrapper.putObject( CommandFieldConstants.KEY_AGENT_MOB_NO, responseDTO.getAgentMobileNo() ) ;
						response = commandManager.executeCommand(baseWrapper, CMD_AGENT_CREATE_PIN);
				}else{
					baseWrapper.putObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE, responseDTO.getCustomerMobileNo() ) ;
					response = commandManager.executeCommand(baseWrapper, CMD_CUSTOMER_CREATE_PIN);
				}
			
			}else if (UtilityCompanyEnum.contains(responseDTO.getProductId().toString())
				|| InternetCompanyEnum.contains(responseDTO.getProductId().toString())
						|| NadraCompanyEnum.contains(responseDTO.getProductId().toString())){
				
				baseWrapper.putObject( CommandFieldConstants.KEY_PAYMENT_TYPE, "0" ) ;
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
			}
			
		}catch(Exception e) {
			if(e instanceof CommandException)
			{
				Long errCode = ((CommandException) e).getErrorCode();
				if (errCode != null) {
					response = errCode.toString();
				}else{
					response = GENERAL_ERROR_RESPONSE;
				}
			}
			else
			{	
				logger.error(e);
				response = GENERAL_ERROR_RESPONSE;
			}
		}

		
		return response;
	}
	
	public MfsRequestHandler getMfsReqHandler() {
		return mfsReqHandler;
	}
	
	
	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}
	
	public void setMfsReqHandler(MfsRequestHandler mfsReqHandler) {
		this.mfsReqHandler = mfsReqHandler;
	}
	
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public IvrRequestHandler() {
	}


	public void setService(IvrWebServiceImplService service) {
		this.service = service;
	}
	
}
