package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.handlermodule.HandlerDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.facade.ProductFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public final class AllPayWebResponseDataPopulator implements ApplicationContextAware {

	protected static final String USER_DEVICE_ACCOUNT_DAO_BEAN = "userDeviceAccountsDAO";
	protected static final String MFS_AC_MGR_BEAN = "mfsAccountManager";
	protected static final String PRODUCT_FACADE_BEAN = "productFacade";
	protected static final String COMMAND_MANAGER_BEAN = "commonCommandManager";
	protected static final String APP_USER_SEC_DAO_BEAN = "userDao";
	
	private static final Logger logger = Logger.getLogger(AllPayWebResponseDataPopulator.class);
	private static ApplicationContext applicationContext = null;

	
	public static void setDefaultParams(AllPayRequestWrapper request) {
		
		HttpSession httpSession = request.getSession();
				
		String[] ACID = {(String) httpSession.getAttribute(CommandFieldConstants.KEY_ACC_ID)};
		String[] BAID = {(String) httpSession.getAttribute(CommandFieldConstants.KEY_BANK_ID)};

		request.addParameter(CommandFieldConstants.KEY_ACC_ID, ACID);
		request.addParameter(CommandFieldConstants.KEY_BANK_ID, BAID);
		
		logger.info("[AllPayWebResponseDataPopulator.setDefaultParams] Setting Request Parameter for AppUserID:" + (ThreadLocalAppUser.getAppUserModel() == null ? " is NULL" : ThreadLocalAppUser.getAppUserModel().getAppUserId()) + ". Params:" + CommandFieldConstants.KEY_ACC_ID +" = " +request.getParameter(CommandFieldConstants.KEY_ACC_ID) +
				 	" " + CommandFieldConstants.KEY_BANK_ID +" = " +request.getParameter(CommandFieldConstants.KEY_BANK_ID));
		
		setUserInfo(request);
	}
		
	
	protected static void setUserInfo(AllPayRequestWrapper request) {

		HttpSession httpSession = request.getSession();
		
		String[] UID = {(String) httpSession.getAttribute(CommandFieldConstants.KEY_U_ID)};
		
		request.addParameter(CommandFieldConstants.KEY_U_ID, UID);
		request.addParameter(CommandFieldConstants.KEY_ALLPAY_ID, UID);
		
		logger.info("[AllPayWebResponseDataPopulator.setUserInfo] Setting User Info Request Parameter for AppUserID:" + (ThreadLocalAppUser.getAppUserModel() == null ? " is NULL" : ThreadLocalAppUser.getAppUserModel().getAppUserId()) + ". Params:" + CommandFieldConstants.KEY_U_ID +" = " +request.getParameter(CommandFieldConstants.KEY_U_ID) + 	
					" " + CommandFieldConstants.KEY_ALLPAY_ID +" = " +request.getParameter(CommandFieldConstants.KEY_ALLPAY_ID));	
	}

	public void populateAccountHolderInfo( HttpServletRequest request )
	{
		request.setAttribute( CommandFieldConstants.KEY_CNIC, request.getParameter(CommandFieldConstants.KEY_CUSTOMER_CNIC ) );
		request.setAttribute( CommandFieldConstants.KEY_MOB_NO, request.getParameter(CommandFieldConstants.KEY_MOB_NO ) );
		request.setAttribute( CommandFieldConstants.KEY_RP_NAME, request.getParameter(CommandFieldConstants.KEY_RP_NAME ) );		
	}

	public void populateTransactionInfo( HttpServletRequest request )
	{
		request.setAttribute( CommandFieldConstants.KEY_CAMT, request.getParameter(CommandFieldConstants.KEY_CAMT ) );
		request.setAttribute( CommandFieldConstants.KEY_TPAM, request.getParameter(CommandFieldConstants.KEY_TPAM ) );
		request.setAttribute( CommandFieldConstants.KEY_TAMT, request.getParameter(CommandFieldConstants.KEY_TAMT ) );
		request.setAttribute( CommandFieldConstants.KEY_CAMTF, request.getParameter(CommandFieldConstants.KEY_CAMTF ) );
		request.setAttribute( CommandFieldConstants.KEY_TPAMF, request.getParameter(CommandFieldConstants.KEY_TPAMF ) );
		request.setAttribute( CommandFieldConstants.KEY_TAMTF, request.getParameter(CommandFieldConstants.KEY_TAMTF ) );
		request.setAttribute( CommandFieldConstants.KEY_TXAM, request.getParameter(CommandFieldConstants.KEY_TXAM ) );		
	}
	
	/**
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		
		return applicationContext.getBean(beanName);
	}
	

	public final static Integer INVALID_BANK_PIN = 0;
	protected final static Integer VALID_BANK_PIN = 1;
	public final static Integer BLOCKED_BANK_PIN = 2;
	
	public Integer isValidBankPinTryCount(HttpServletRequest request) {		

		Integer STATUS = INVALID_BANK_PIN;
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		
		Boolean isValid = isValidBankCount(requestWrapper);
		
		if(!isValid) {

			STATUS = INVALID_BANK_PIN;
			
			String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
			String deviceTypeId = requestWrapper.getParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID);
			
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setUserId(UID);
			userDeviceAccountsModel.setDeviceTypeId(Long.valueOf(deviceTypeId));

			CustomList<UserDeviceAccountsModel> customList = getUserDeviceAccountsDAO().findByExample(userDeviceAccountsModel);

			if(customList != null && !customList.getResultsetList().isEmpty()) 	{
				
				userDeviceAccountsModel = customList.getResultsetList().get(0);
				userDeviceAccountsModel.setCredentialsExpired(Boolean.TRUE);
				
				getUserDeviceAccountsDAO().saveOrUpdate(userDeviceAccountsModel);
				
				requestWrapper.addNullParameter(CommandFieldConstants.KEY_U_ID);
				STATUS = BLOCKED_BANK_PIN;
			}				
		}
		
		return STATUS;
	}

	public RetailerContactModel getRetaielrContactModelById(Long id) throws FrameworkCheckedException
	{
		RetailerContactDAO retailerContactDAO = (RetailerContactDAO) getBean("retailerContactDAO");
		return  retailerContactDAO.findByPrimaryKey(id);
	}

    public RetailerContactModel getRetaielrContactModelByHandlerId(Long id) throws FrameworkCheckedException
    {
        HandlerDAO handlerDAO = (HandlerDAO) getBean("handlerDAO");
        HandlerModel handlerModel = handlerDAO.findByPrimaryKey(id);
        RetailerContactModel retailerContactModel = handlerModel.getRetailerContactIdRetailerContactModel();
        return retailerContactModel;
    }

    public String getAreaName(AppUserModel appUserModel){
        String areaName = "punjab";
        Long retailerContactId = null;
        try {
            if(appUserModel != null) {
                if (UserTypeConstantsInterface.RETAILER.longValue() == appUserModel.getAppUserTypeId()) {
                    retailerContactId = appUserModel.getRetailerContactId();
                    RetailerContactModel retailerContactModel = this.getRetaielrContactModelById(retailerContactId);
                    if (retailerContactModel != null && retailerContactModel.getTaxRegimeIdTaxRegimeModel() != null) {
                        areaName = retailerContactModel.getTaxRegimeIdTaxRegimeModel().getName();
                    }
                } else if (UserTypeConstantsInterface.HANDLER.longValue() == appUserModel.getAppUserTypeId()) {
                    RetailerContactModel retailerContactModel = this.getRetaielrContactModelByHandlerId(appUserModel.getHandlerId());
                    if (retailerContactModel != null && retailerContactModel.getTaxRegimeIdTaxRegimeModel() != null) {
                        areaName = retailerContactModel.getTaxRegimeIdTaxRegimeModel().getName();
                    }
                }
            }else{
                logger.error("[AllPayWebResponseDataPopulator.getAreaName] appUserModel is null. Returning default area:" + areaName);
            }
        } catch (FrameworkCheckedException e) {
            logger.error("[AllPayWebResponseDataPopulator.getAreaName] Unable to fetch area name against appUserId:"+appUserModel.getAppUserId(), e);
        }
        return areaName;
    }

    /**
     * @param request
     * @return
     */
	private Boolean isValidBankCount(AllPayRequestWrapper request) {
		
		String errors = "";
    	String INVALID_BANK_PIN_COUNT = "INVALID_BANK_PIN_COUNT";
		String INVALID_BANK_PIN_MSG = "In-valid MPIN, please enter valid MPIN.";
		Boolean isValidBankPinTryCount = Boolean.TRUE;
		HttpSession session = request.getSession();
		Integer pinTryCount = 0;
		
		if(session.getAttribute(INVALID_BANK_PIN_COUNT) != null) {
			
			pinTryCount = (Integer) session.getAttribute(INVALID_BANK_PIN_COUNT); 
		}
		
		if(request.getAttribute("errors") != null) {
		
			errors = (String) request.getAttribute("errors");
		}
		
		if(INVALID_BANK_PIN_MSG.equalsIgnoreCase(errors.trim())) {
						
			if(pinTryCount > 0) {
								
				if(pinTryCount >= 2) {
					
					isValidBankPinTryCount = Boolean.FALSE;
					
					request.setAttribute("errors", "Maximum retry limit reached. Your Account has been blocked.");
					session.invalidate();
					
					logger.info("isValidBankPinTryCount = " +pinTryCount+ " Blocking Pin = "+isValidBankPinTryCount+" Session Invalidate Now");
					
				} else {
					
					session.setAttribute(INVALID_BANK_PIN_COUNT, ++pinTryCount);
				}
				
			} else {
				
				session.setAttribute(INVALID_BANK_PIN_COUNT, ++pinTryCount);
			}	
			
			logger.info("isValidBankPinTryCount = " +pinTryCount);	
			
		} else if((pinTryCount > 0) && session != null) {
			
			session.setAttribute(INVALID_BANK_PIN_COUNT, 0);
		}
		
		logger.info("isValidBankPinTryCount = " +isValidBankPinTryCount);	
		
		return isValidBankPinTryCount;
	}	


	/**
	 * @param request
	 * @param mobileNumber
	 * @return
	 */
	protected Boolean isValidCustomer(HttpServletRequest request, String mobileNumber) {
		
		Boolean isValidCustomer = Boolean.TRUE;
		
		UserDeviceAccountsModel userDeviceAccountsModel = null;

		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNumber);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		
		try {
			
			MfsAccountManager mfsAccountManager = (MfsAccountManager) getBean(MFS_AC_MGR_BEAN);
			
			appUserModel = mfsAccountManager.getAppUserModel(appUserModel);
			
			if(appUserModel != null) {
				
				userDeviceAccountsModel = mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.USSD);
				
			} else {

				isValidCustomer = Boolean.FALSE;
				request.setAttribute("errors", "No Customer Account found against Mobile # "+mobileNumber);
			}
			
			if(userDeviceAccountsModel != null && appUserModel != null) {
							
				StringBuilder errors = new StringBuilder("Customer Account has been ");
				
				if(appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()){
					errors.append("Closed");
					isValidCustomer = Boolean.FALSE;
				}
				
				if(!userDeviceAccountsModel.getAccountEnabled()){
					errors.append("De-activated");
					isValidCustomer = Boolean.FALSE;
				} 
				
				if(userDeviceAccountsModel.getAccountExpired()){
					
					if(!isValidCustomer) {
						errors.append(", ");
					}
					
					errors.append("Expired");
					
					isValidCustomer = Boolean.FALSE;
				} 
				
				if(userDeviceAccountsModel.getAccountLocked()){
					
					if(!isValidCustomer) {
						errors.append(", ");
					}
					
					errors.append("Locked");
					
					isValidCustomer = Boolean.FALSE;
				} 
				
				if(userDeviceAccountsModel.getCredentialsExpired()){
					
					if(!isValidCustomer) {
						errors.append(", and ");
					}
					
					errors.append("blocked due Invalid Credentials ");
					
					isValidCustomer = Boolean.FALSE;
				} 
				
				if(!isValidCustomer) {
					request.setAttribute("errors", errors);
				}
			}
			
		} catch (FrameworkCheckedException e) {

			logger.error(e.getLocalizedMessage());
		}
		
		return isValidCustomer;
	}	
	
	
	/**
	 * @param controller
	 * @param request
	 * @return
	 */
	public String getErrorType(HttpServletRequest request) {

		RequestContext requestContext = new RequestContext(request);
		String errorString = (String) request.getAttribute("errors");
		String errorType = AllPayWebConstant.BLANK_SPACE.getValue();
		
		if(isValidString(errorString)) {
			
			errorString = errorString.trim();
			
			String inValidPINMessage = requestContext.getMessage("6033").trim();
			String incorrectTransPIN = requestContext.getMessage("MINI.IncorrectTransPIN").trim(); 
			String limitExceeds_CrDay = requestContext.getMessage("8061").trim();
			String limitExceeds_DbDay = requestContext.getMessage("8062").trim();
			String limitExceeds_CrMonth = requestContext.getMessage("8063").trim();
			String limitExceeds_DbMonth = requestContext.getMessage("8064").trim();
			String limitExceeds_DbYear = requestContext.getMessage("8065").trim();
			String limitExceeds_CrYear = requestContext.getMessage("8066").trim();
			String limitExceeds_Mx_Blnc = requestContext.getMessage("8067").trim();
			String limitExceeds_Mx_Blnc_Recipient = requestContext.getMessage("8068").trim();
			String wc_throughputLimit_not_defiend_system = requestContext.getMessage("8069").trim();
			String wc_throughput_Monthly_not_Veridfied = requestContext.getMessage("8070").trim();
			String limitExceeds_Mx_Blnc_DbMonth = requestContext.getMessage("8071").trim();
			String limitExceeds_Mx_C2C_Tr_Limit = requestContext.getMessage("CashToCashCommand.maxTransactionLimit").trim();
			String limitExceeds_Mn_C2C_Tr_Limit = requestContext.getMessage("CashToCashCommand.minTransactionLimit").trim();

			String serviceUnAvailable_A = requestContext.getMessage("6074").trim();
			String serviceUnAvailable_B = requestContext.getMessage("6076").trim();
			String serviceUnAvailable_C = requestContext.getMessage("command.unexpectedError").trim();
			String serviceUnAvailable_D = requestContext.getMessage("mfsRequestHandler.unknownError").trim();
			
			String[] limits =  {limitExceeds_CrDay, limitExceeds_DbDay, limitExceeds_CrMonth, limitExceeds_DbMonth, 
								limitExceeds_DbYear, limitExceeds_DbYear, limitExceeds_CrYear, limitExceeds_Mx_Blnc,
								limitExceeds_Mx_Blnc_Recipient, wc_throughputLimit_not_defiend_system, wc_throughput_Monthly_not_Veridfied,
								limitExceeds_Mx_Blnc_DbMonth, limitExceeds_Mx_C2C_Tr_Limit, limitExceeds_Mn_C2C_Tr_Limit};
			
			String[] serviceUnAvailable =  {serviceUnAvailable_A, serviceUnAvailable_B, serviceUnAvailable_C, serviceUnAvailable_D};
						
			if(( isValidString(inValidPINMessage) && inValidPINMessage.equalsIgnoreCase( errorString )) 
					|| (isValidString(incorrectTransPIN) && incorrectTransPIN.equalsIgnoreCase( errorString )) ) {
				
				errorType = AllPayWebConstant.INVALID_BANK_PIN.getValue();
				
			} else if(Arrays.asList(limits).contains(errorString)) {
				
				errorType = AllPayWebConstant.LIMIT_EXCEED.getValue();	
				
			} else if(Arrays.asList(serviceUnAvailable).contains(errorString)) {
				
				errorType = AllPayWebConstant.SERVICE_UNAVAILABLE.getValue();		
			}
		}
		
		return errorType;
	}
	

	/**
	 * @param string
	 * @return
	 */
	public static Boolean isValidString(String string) {
		
		if(AllPayWebConstant.NULL_OBJECT.getValue() != string && 
				!AllPayWebConstant.BLANK_SPACE.getValue().equals(string)) {
			
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	
	/**
	 * @return
	 */
	private UserDeviceAccountsDAO userDeviceAccountsDAO = null;
	
	protected UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
		
		if(userDeviceAccountsDAO == null) {
			
			userDeviceAccountsDAO = (UserDeviceAccountsDAO) getBean(USER_DEVICE_ACCOUNT_DAO_BEAN);
		}
		
		return userDeviceAccountsDAO;
	}
	
	public AppUserModel getAppUserModel(String userId) {
	
		AppUserModel appUserIdAppUserModel = null;
		
		UserDeviceAccountsModel example = new UserDeviceAccountsModel();
		example.setUserId(userId);

		UserDeviceAccountsModel userDeviceAccountsModel = getUserDeviceAccountsModel(example);
		
		if(userDeviceAccountsModel != null) {
			appUserIdAppUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
		}
		
		logger.info("getAppUserModel(.) for ThreadLocalAppUser : "+appUserIdAppUserModel);
		
		return appUserIdAppUserModel;
	}
	
	
	/**
	 * @param example
	 * @return
	 */
	public UserDeviceAccountsModel getUserDeviceAccountsModel(UserDeviceAccountsModel example) {

		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        exampleHolder.setEnableLike(Boolean.FALSE);	
		
        CustomList<UserDeviceAccountsModel> customList = getUserDeviceAccountsDAO().findByExample(example,null,null,exampleHolder);
        
		UserDeviceAccountsModel userDeviceAccountsModel = null;
		
		if(customList != null && customList.getResultsetList()!= null && !customList.getResultsetList().isEmpty()) {

			userDeviceAccountsModel = customList.getResultsetList().get(0);
		}		
		
		return userDeviceAccountsModel;
	}
	
	/**
	 * @param productId
	 * @return
	 * @throws FrameworkCheckedException
	 */
	protected ProductModel getProductModel(Long productId) {
		
		ProductModel productModel = new ProductModel();
		productModel.setProductId(productId);
		productModel.setPrimaryKey(productId);
		
		ProductFacade productFacade = (ProductFacade) getBean(PRODUCT_FACADE_BEAN);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(productModel);

		try {
			
			baseWrapper = productFacade.loadProduct(baseWrapper);
			productModel = (ProductModel) baseWrapper.getBasePersistableModel();			
			return productModel;
			
		} catch (FrameworkCheckedException e) {

			logger.error(e.getLocalizedMessage());
		}
		
		return null;
	}
	
	/**
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText) {
		
		return Encryption.encrypt(plainText);
	}
	
	/**
	 * @param cipherText
	 * @return
	 */
	public static String decrypt(String cipherText) {
		
		return Encryption.decrypt(cipherText);
	}
	
	
	private SmartMoneyAccountDAO smartMoneyAccountDAO = null;
	
	public SmartMoneyAccountModel getSmartMoneyAccountModel(SmartMoneyAccountModel _smartMoneyAccountModel) {

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		
		if(smartMoneyAccountDAO == null) {
			
			smartMoneyAccountDAO = (SmartMoneyAccountDAO) getBean("smartMoneyAccountDAO");
		}
		
		CustomList<SmartMoneyAccountModel> customList = smartMoneyAccountDAO.findByExample(_smartMoneyAccountModel);
		
		if(customList != null && customList.getResultsetList() != null && !customList.getResultsetList().isEmpty()) {
			smartMoneyAccountModel = customList.getResultsetList().get(0);
			logger.info("[AllPayWebResponseDataPopulator.getSmartMoneyAccountModel] SmartMoneyAccount List Size:" + customList.getResultsetList().size() + ". Loading SmartMoneyAccountID:" + smartMoneyAccountModel.getSmartMoneyAccountId() + ". Nick:" + smartMoneyAccountModel.getName());
		}
		
		return smartMoneyAccountModel;
	}
	
	
	/**
	 * @return
	 */
	private CommonCommandManager commonCommandManager = null;
	
	protected CommonCommandManager getCommonCommandManager() {
		
		if(commonCommandManager == null) {
			commonCommandManager = (CommonCommandManager) getBean(COMMAND_MANAGER_BEAN);			
		}
		
		return commonCommandManager;
	}
	
	
	/**
	 * @param appUserModel
	 * @param bankPin
	 * @param transactionCodeModel
	 * @param productModel
	 * @param fetchTitle
	 * @throws FrameworkCheckedException
	 */
	public void verifyPIN(AppUserModel appUserModel, String bankPin, TransactionCodeModel transactionCodeModel, ProductModel productModel, Boolean fetchTitle) throws FrameworkCheckedException {
		
		logger.info("Agent Web > Verify Bank PIN > APP USER " + appUserModel.getUsername() + ". RetailerContactId:" + appUserModel.getRetailerContactId());
		
		SmartMoneyAccountModel exampleInstance = new SmartMoneyAccountModel();
		exampleInstance.setDeleted(Boolean.FALSE);
		exampleInstance.setActive(Boolean.TRUE);
		if(UserTypeConstantsInterface.HANDLER.longValue() == appUserModel.getAppUserTypeId()){
            exampleInstance.setHandlerId(appUserModel.getHandlerId());
        }else{
            exampleInstance.setRetailerContactId(appUserModel.getRetailerContactId());
        }

		SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountModel(exampleInstance);
		
	    BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
	    baseWrapperTemp.setBasePersistableModel(smartMoneyAccountModel);
	    
		SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
	    switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
	    
		try {
			
			AbstractFinancialInstitution abstractFinancialInstitution = getCommonCommandManager().loadFinancialInstitution(baseWrapperTemp);
		    switchWrapper.putObject(CommandFieldConstants.KEY_PIN , bankPin) ;
			switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper) ;

			logger.info("Agent Web > Bank PIN Verified" + (switchWrapper != null));
		}catch(CommandException e){
			throw new FrameworkCheckedException(e.getMessage());
		} catch (FrameworkCheckedException e) {
			throw new FrameworkCheckedException(e.getMessage());
		} catch (Exception e) {
			throw new FrameworkCheckedException(e.getMessage());
		}		
		
		if(fetchTitle) {

			logger.info("Agent Web > Fetch Title > APP USER" + appUserModel.getUsername());
			
			fetchTitle(bankPin, transactionCodeModel, productModel);
		}
	}
	
	
	/**
	 * @param PIN
	 * @return
	 * @throws CommandException
	 */
	public void fetchTitle(AppUserModel appUserModel) throws FrameworkCheckedException {
				
   	  	try {
   	  		
			Boolean titleFetched = getCommonCommandManager().titleFetch(appUserModel);
			logger.info("titleFetched : "+titleFetched);
			
		} catch (FrameworkCheckedException e) {
			logger.error(e);
			throw new FrameworkCheckedException(e.getLocalizedMessage());
			
		}	
	}
	
	
	/**
	 * @param bankPin
	 * @param transactionCodeModel
	 * @param productModel
	 * @throws CommandException
	 */
	protected void fetchTitle(String bankPin, TransactionCodeModel transactionCodeModel, ProductModel productModel) throws CommandException {
				
   	  	try {
   	  		
   	  		Boolean titleFetched = getCommonCommandManager().titleFetch(ThreadLocalAppUser.getAppUserModel());
			
   	  		if(!titleFetched) {
				
				throw new FrameworkCheckedException("Could Not Fetch Title for "+ThreadLocalAppUser.getAppUserModel().getFirstName());
			}   	  		
   	  		
   	  		/*
   	  		AccountInfoModel accountInfoModel = getCommonCommandManager().titleFetch(bankPin, transactionCodeModel, productModel);
			
   	  		if(accountInfoModel == null || StringUtil.isNullOrEmpty(accountInfoModel.getAccountNo())) {
				
				throw new FrameworkCheckedException("Could Not Fetch Title for "+ThreadLocalAppUser.getAppUserModel().getFirstName());
			}
			*/
		} catch (WorkFlowException e) {

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
			
		} catch (FrameworkCheckedException e) {
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
			
		} catch (Exception e) {
			logger.error(e);
			throw new CommandException("Your account cannot be contacted. Please try again later.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
		}	
	}

	
	
	private ActionLogManager actionLogManager = null;
	
	protected Boolean logActionLogModel() {
		
		if(actionLogManager == null) {
			actionLogManager = (ActionLogManager) this.getBean("actionLogManager");
		}
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
	
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		
		try {
		
			baseWrapperActionLog = actionLogManager.createOrUpdateActionLog(baseWrapperActionLog);
			
		} catch (FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {
			
			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();
			
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * @param appUserModel
	 * @return appUserModel
	 */
	public AppUserModel getAppUserModelByQuery(AppUserModel _appUserModel) {
	
		AppUserModel appUserModel = null;

		String mobileNo = _appUserModel.getMobileNo();
		Long appUserTypeId = _appUserModel.getAppUserTypeId();
		String nic = _appUserModel.getNic();
		
		if(logger.isDebugEnabled()){
			logger.debug("mobileNo : "+mobileNo+" appUserTypeId : "+appUserTypeId);
		}
		
		appUserModel = this.getAppUserDAO().loadAppUserByQuery(mobileNo, appUserTypeId);		
		
		return appUserModel;
	}
	
	
	private AppUserDAO userDao = null;
	
	protected AppUserDAO getAppUserDAO() {
	
		if(userDao == null) {
			userDao = (AppUserDAO) getBean(APP_USER_SEC_DAO_BEAN);
		}
		
		return userDao;
	}
	
	public static final String TOKEN_KEY = "_synchronizerToken";
    
    public static synchronized Boolean isTokenValid(HttpServletRequest request) {
    	
       HttpSession session = request.getSession();
        String sessionToken = (String)session.getAttribute(TOKEN_KEY);
        String requestToken = request.getParameter(TOKEN_KEY);
        
        Boolean isTokenValid = Boolean.FALSE;
        
        if (requestToken != null && sessionToken != null) {
        
	        if (sessionToken.equals(requestToken)) {
	        	
	            session.setAttribute(TOKEN_KEY, nextToken());
	            isTokenValid = Boolean.TRUE;;
	        }
        }
        
        if(!isTokenValid) {
        	
        	request.setAttribute("errors", "Invalid Page Submission Error.");
        }
        
        return isTokenValid;
    }
	
    public static String nextToken() {
    	
        long seed = System.currentTimeMillis(); 
        java.util.Random random = new java.util.Random();
        random.setSeed(seed);
        
        return Long.toString(seed) + Long.toString(Math.abs(random.nextLong()));
    }    
    
 
    public void removeTheadLocals() {
    	
		ThreadLocalAppUser.remove();
		ThreadLocalActionLog.remove();
		ThreadLocalUserDeviceAccounts.remove();
		ThreadLocalAccountInfo.remove();
		
		logger.info("Removed ThreadLocalAppUser, ThreadLocalActionLog, ThreadLocalUserDeviceAccounts, ThreadLocalAccountInfo");    	
    }
    
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}