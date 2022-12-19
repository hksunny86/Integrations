/**
 * 
 */
package com.inov8.microbank.webapp.action.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.AllpayUserInfoListViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.PanGenerator;
import com.inov8.microbank.common.util.PasswordConfigUtil;
import com.inov8.microbank.common.util.PasswordInputDTO;
import com.inov8.microbank.common.util.PasswordResultDTO;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.server.dao.allpaymodule.AllPayUserInfoListViewDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.model.AccountInfoModel;

/**
 * @author kashif.bashir
 *
 */

@ManagedBean(name="handlerManagedBean", eager=true)
@ViewScoped
public class HandlerManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(HandlerManagedBean.class);


	public HandlerManagedBean() {}

	private HandlerBackingBean handlerBackingBean = null;
	@ManagedProperty(value="#{distributorManager}")
	private DistributorManager distributorManager;
	@ManagedProperty(value="#{agentHierarchyManager}")
	private AgentHierarchyManager agentHierarchyManager;
	@ManagedProperty(value="#{appUserDAO}")
	private AppUserDAO appUserDAO;
	@ManagedProperty(value="#{productCatalogManager}")
	private ProductCatalogManager productCatalogManager;
	@ManagedProperty(value="#{encryptionHandler}")
	private EncryptionHandler encryptionHandler;
	@ManagedProperty(value="#{mfsAccountManager}")
	private MfsAccountManager mfsAccountManager;
	@ManagedProperty(value="#{handlerManager}")
	private HandlerManager handlerManager;
	@ManagedProperty(value="#{accountManager}")
	private AccountManager accountManager;
	@ManagedProperty(value="#{retailerContactManager}")
	private RetailerContactManager retailerContactManager;
	@ManagedProperty(value="#{allpaySequenceGenerator}")
	private OracleSequenceGeneratorJdbcDAO allpaySequenceGenerator;
	@ManagedProperty(value="#{olaCustomerAccountTypeDao}")
	private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
	@ManagedProperty(value="#{userDeviceAccountsDAO}")
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	@ManagedProperty(value="#{allpayUserInfoListViewDAO}")
	private AllPayUserInfoListViewDAO allpayUserInfoListViewDAO;
	@ManagedProperty(value = "#{accountControlManager}")
    private AccountControlManager accountControlManager;
	/**
	 * @return
	 */
	public String execute() {
			
		HandlerBackingBean handlerBackingBean = this.getBackingEntity();
		
		try {
			
			Integer created = 3;
			
			Boolean validated = validate();
			
			if(validated) {							
				
				if(handlerBackingBean.getHandlerModel().getHandlerId() == null || handlerBackingBean.getHandlerModel().getHandlerId() == 0) {
					
					this.createUserDeviceAccountsModel(handlerBackingBean);
					this.createAppUserModel(handlerBackingBean);
					this.createSmartMoneyAccountModel(handlerBackingBean);
					this.createAccountInfoModel(handlerBackingBean);
					this.createHandlerModel(handlerBackingBean);					
					created = 1;
				}				
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();			
				baseWrapper.putObject(HandlerBackingBean.BEAN_NAME, handlerBackingBean);
				
				mfsAccountManager.createHandlerAccount(baseWrapper);

				JSFContext.addErrorMessage("Handler "+ handlerBackingBean.getAppUserModel().getUsername() + " has been created "+
						" successfully with Handler Id : "+handlerBackingBean.getUserDeviceAccountsModel().getUserId());
				
				HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				httpServletResponse.sendRedirect("p_addhandler.jsf?handlerId="+handlerBackingBean.getHandlerModel().getHandlerId()+"&created="+created+"&userId="+handlerBackingBean.getUserDeviceAccountsModel().getUserId());
				
				handlerBackingBean = null;
			}
			
		} catch (Exception e) {

			addErrorMessage(null, e.getLocalizedMessage());
			JSFContext.addErrorMessage(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}	
	public String cancel() {
		return "searchHandler.jsf";
	}
	/**
	 * @param handlerBackingBean
	 */
	private void createSmartMoneyAccountModel(HandlerBackingBean handlerBackingBean) {
		
		SmartMoneyAccountModel smartMoneyAccountModel = handlerBackingBean.getSmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(handlerBackingBean.getAppUserModel().getAppUserId());
		smartMoneyAccountModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
		smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		smartMoneyAccountModel.setCreatedOn(new Date());
		smartMoneyAccountModel.setUpdatedOn(new Date());
		smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		smartMoneyAccountModel.setActive(Boolean.TRUE);
		smartMoneyAccountModel.setChangePinRequired(Boolean.TRUE);
		smartMoneyAccountModel.setDefAccount(Boolean.TRUE);
		smartMoneyAccountModel.setDeleted(Boolean.FALSE);
		smartMoneyAccountModel.setName( "i8_hdr_" + handlerBackingBean.getUserDeviceAccountsModel().getUserId());		
	}
	
	
	/**
	 * @param handlerBackingBean
	 * @throws Exception
	 */
	private void createAccountInfoModel(HandlerBackingBean handlerBackingBean) throws Exception {

		String plainPin = RandomUtils.generateRandom(4, Boolean.FALSE, Boolean.TRUE);
		String encPin = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, plainPin);	
		
		AccountInfoModel accountInfoModel = handlerBackingBean.getAccountInfoModel();
		accountInfoModel.setCreatedOn(new Date());
		accountInfoModel.setUpdatedOn(new Date());
		accountInfoModel.setCustomerId(handlerBackingBean.getAppUserModel().getAppUserId());		
		accountInfoModel.setCustomerMobileNo(handlerBackingBean.getAppUserModel().getMobileNo());
		accountInfoModel.setFirstName(handlerBackingBean.getAppUserModel().getFirstName());
		accountInfoModel.setLastName(handlerBackingBean.getAppUserModel().getLastName());
		accountInfoModel.setPaymentModeId(handlerBackingBean.getSmartMoneyAccountModel().getPaymentModeId());
		accountInfoModel.setAccountNo(encryptionHandler.encrypt(handlerBackingBean.getUserDeviceAccountsModel().getUserId()));
		accountInfoModel.setAccountNick(handlerBackingBean.getSmartMoneyAccountModel().getName());		
		accountInfoModel.setDeleted(Boolean.FALSE);			
		accountInfoModel.setActive(Boolean.TRUE);
		//accountInfoModel.setPin(encPin);
		//Added after HSM Integration
		accountInfoModel.setPan(PanGenerator.generatePAN());
		// End HSM Integration Change
		
//		handlerBackingBean.setPlainTransactionPin(plainPin);
	}
	
	
	/**
	 * @param handlerBackingBean
	 */
	private void createUserDeviceAccountsModel(HandlerBackingBean handlerBackingBean) {

		String userId = String.valueOf(this.allpaySequenceGenerator.nextLongValue());
		String plainPin = RandomUtils.generateRandom(4, Boolean.FALSE, Boolean.TRUE);
		String encPin = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, plainPin);		
		
		UserDeviceAccountsModel userDeviceAccountsModel = handlerBackingBean.getUserDeviceAccountsModel();
		userDeviceAccountsModel.setCreatedOn(new java.util.Date());
		userDeviceAccountsModel.setUpdatedOn(new java.util.Date());
		userDeviceAccountsModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		userDeviceAccountsModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		userDeviceAccountsModel.setPinChangeRequired(Boolean.TRUE);
		userDeviceAccountsModel.setCommissioned(Boolean.TRUE);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		userDeviceAccountsModel.setAppUserIdAppUserModel(handlerBackingBean.getAppUserModel());
		userDeviceAccountsModel.setAccountEnabled(Boolean.TRUE);
		userDeviceAccountsModel.setAccountExpired(Boolean.FALSE);
		userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
		userDeviceAccountsModel.setCommissioned(Boolean.FALSE);
		userDeviceAccountsModel.setCredentialsExpired(Boolean.FALSE);
		userDeviceAccountsModel.setPasswordChangeRequired(Boolean.TRUE);
		userDeviceAccountsModel.setUserId(userId);
		userDeviceAccountsModel.setProdCatalogId(handlerBackingBean.getProductCatalogId());	
		userDeviceAccountsModel.setPin(encPin);
		
		handlerBackingBean.setPlainLoginPin(plainPin);
	}

	
	/**
	 * @param handlerBackingBean
	 */
	private void createAppUserModel(HandlerBackingBean handlerBackingBean) {
		String plainPassword = RandomUtils.generateRandom(8, Boolean.FALSE, Boolean.TRUE);
		handlerBackingBean.setPassword(plainPassword);

		AppUserModel appUserModel = handlerBackingBean.getAppUserModel();
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.HANDLER);
		appUserModel.setVerified(Boolean.TRUE);
		appUserModel.setAccountEnabled(Boolean.TRUE);
		appUserModel.setAccountExpired(Boolean.FALSE);
		appUserModel.setCredentialsExpired(Boolean.FALSE);
		appUserModel.setPasswordChangeRequired(Boolean.TRUE);
		appUserModel.setAccountClosedUnsettled(Boolean.FALSE);
		appUserModel.setAccountClosedSettled(Boolean.FALSE);
		appUserModel.setAccountLocked(Boolean.FALSE);		
		appUserModel.setCreatedOn(new java.util.Date());
		appUserModel.setUpdatedOn(new java.util.Date());
		appUserModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setPassword(EncoderUtils.encodeToSha(handlerBackingBean.getPassword()));

		handlerBackingBean.setAppUserModel(appUserModel);
	}

	
	/**
	 * @param handlerBackingBean
	 */
	private void createHandlerModel(HandlerBackingBean handlerBackingBean) {
		
		HandlerModel handlerModel = handlerBackingBean.getHandlerModel();		
		handlerModel.setRetailerContactId(handlerBackingBean.getRetailerContactId());
		handlerModel.setCreatedOn(new java.util.Date());
		handlerModel.setUpdatedOn(new java.util.Date());
		handlerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		handlerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());		
		handlerModel.setAccountTypeId(handlerBackingBean.getCustomerAccountTypeId());
//		handlerModel.setVersionNo(0);
	}
	
	
	@PostConstruct
	public void init() {
		
		logger.info("@PostConstruct init()");
		
		try {				

			handlerBackingBean = getBackingEntity();
			
			/*List<DistributorModel> distributorList = this.distributorManager.findActiveDistributor();
			handlerBackingBean.setDistributorList(distributorList);*/
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			String handlerId = httpServletRequest.getParameter("handlerId");
			logger.info("@PostConstruct init() handlerId ="+handlerId);
				
			List<SelectItem> catalogList = this.getBackingEntity().getProductCatalogList();
			
			SearchBaseWrapper searchBaseWrapper = productCatalogManager.loadAllCatalogs();
			
			List<ProductCatalogModel> resultSetList = searchBaseWrapper.getCustomList().getResultsetList();
			
			for(ProductCatalogModel productCatalogModel : resultSetList) {
				if(!productCatalogModel.getActive().booleanValue())
					continue;
				
				catalogList.add(new SelectItem(productCatalogModel.getProductCatalogId(), productCatalogModel.getName()));
			}
			
			handlerBackingBean.setProductCatalogList(catalogList);

			if(!StringUtil.isNullOrEmpty(handlerId) && StringUtil.isNumeric(handlerId)) {
				populateHandlerData(Long.valueOf(handlerId));
			}
			
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	} 	

	
	private void populateHandlerData(Long handlerId) throws FrameworkCheckedException {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		handlerBackingBean.getHandlerModel().setHandlerId(handlerId);
		baseWrapper.setBasePersistableModel(handlerBackingBean.getHandlerModel());
		this.handlerManager.loadHandler(baseWrapper);
		handlerBackingBean.setHandlerModel((HandlerModel)baseWrapper.getBasePersistableModel());
				
		handlerBackingBean.getAppUserModel().setHandlerId(handlerId);
		AppUserModel appUserModel =  this.appUserDAO.loadAppUserByHandlerByQuery(handlerId);
		handlerBackingBean.setAppUserModel(appUserModel);
		handlerBackingBean.setReadOnly(Boolean.TRUE);

		UserDeviceAccountsModel userDeviceAccountsModel = this.userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());

		handlerBackingBean.setProductCatalogId(userDeviceAccountsModel.getProdCatalogId());
		handlerBackingBean.setRetailerContactId(handlerBackingBean.getHandlerModel().getRetailerContactId());
		
		/*List dataList = this.handlerManager.getRetalerDataMap(handlerBackingBean.getHandlerModel().getRetailerContactId());	
		Object[] array = (Object[])dataList.get(0);
		
		handlerBackingBean.setRetailerContactId(handlerBackingBean.getHandlerModel().getRetailerContactId());
		handlerBackingBean.setDistributorId((Long)array[1]);
		handlerBackingBean.setRegionId((Long)array[2]);*/
		handlerBackingBean.setCustomerAccountTypeId(handlerBackingBean.getHandlerModel().getAccountTypeId());
	/*	onChangeAgentNetwork(null);
		onChangeRegion(null);*/
		populateAgentInfo();
		onChangeAgent();
		
	}
	
	
	public void onChangeAgentNetwork(AjaxBehaviorEvent event){
		
		Long distributorid = this.getBackingEntity().getDistributorId();
		
		this.handlerBackingBean.setRetailerList(new ArrayList<SelectItem>());
		
		List<SelectItem> regionList = this.getBackingEntity().getRegionList();
		
		
		if(distributorid == null || distributorid < 1) {
			return;
		}
		
		try {

			regionList.clear();
			
			SearchBaseWrapper searchBaseWrapper=agentHierarchyManager.findRegionsByDistributorId(distributorid);
			
			if(searchBaseWrapper.getCustomList() != null) {
			
				List<RegionModel> resultSetList = searchBaseWrapper.getCustomList().getResultsetList();
				
				for(RegionModel regionModel : resultSetList) {
					
					regionList.add(new SelectItem(regionModel.getRegionId(), regionModel.getRegionName()));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	

	public void onChangeRegion(AjaxBehaviorEvent event){

		Long regionId = this.getBackingEntity().getRegionId();
		List<SelectItem> retailerList = this.getBackingEntity().getRetailerList();
		
		if(regionId == null && regionId < 1) {
			
			return;
		}
		
		try {
			retailerList.clear();
			
			List<AppUserModel> appUserModelList = appUserDAO.getAppUserModelByRegionId(regionId);
			
			CustomList<UserDeviceAccountsModel> userDeviceAccountsModelList;	
			UserDeviceAccountsModel userDeviceAccountsModel=new UserDeviceAccountsModel();
			
			for(AppUserModel model : appUserModelList){
				retailerList.add(new SelectItem(model.getRetailerContactId(), model.getAppUserIdUserDeviceAccountsModelList().iterator().next().getUserId()+" - "+model.getFirstName() + " "+model.getLastName()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	

	public void onChangeAgent() {

		Long retailerContactId = this.getBackingEntity().getRetailerContactId();		
		
		List<SelectItem> customerAccountTypeList = this.getBackingEntity().getCustomerAccountTypeList();
		
		if(retailerContactId == null || retailerContactId < 1) {
			return;
		}
		
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId(retailerContactId);		
		
		try {
			
			customerAccountTypeList.clear();

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(retailerContactModel);
			baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
			
			retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
			
			//add agent's account type so it could be selected for handler
			OlaCustomerAccountTypeModel agentAccountTypeModel = olaCustomerAccountTypeDao.findByPrimaryKey(retailerContactModel.getOlaCustomerAccountTypeModelId());
			customerAccountTypeList.add(new SelectItem(agentAccountTypeModel.getPrimaryKey(), agentAccountTypeModel.getName()));
			
			OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
			
			if(handlerBackingBean.getReadOnly()) {

				customerAccountTypeModel.setPrimaryKey(handlerBackingBean.getHandlerModel().getAccountTypeId());
				
			} else {

				customerAccountTypeModel.setParentAccountTypeId(retailerContactModel.getOlaCustomerAccountTypeModelId());
			}
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(customerAccountTypeModel);
			
			this.accountManager.searchOlaCustomerAccountTypes(searchBaseWrapper);
			
			CustomList<OlaCustomerAccountTypeModel> customList = searchBaseWrapper.getCustomList();
			
			if(customList != null && customList.getResultsetList() != null) {
				
				for(Object _olaCustomerAccountTypeModel : customList.getResultsetList()) {
					
					OlaCustomerAccountTypeModel accountTypeModel = (OlaCustomerAccountTypeModel)_olaCustomerAccountTypeModel;
					
					customerAccountTypeList.add(new SelectItem(accountTypeModel.getPrimaryKey(), accountTypeModel.getName()));
				}
			}
			
			Boolean smsToAgent = getBackingEntity().getHandlerModel().getSmsToAgent();
			if(null == smsToAgent)
				this.getBackingEntity().getHandlerModel().setSmsToAgent(retailerContactModel.getSmsToAgent());
			
			Boolean smsToHandler = getBackingEntity().getHandlerModel().getSmsToHandler();
			if(null == smsToHandler)
				this.getBackingEntity().getHandlerModel().setSmsToHandler(retailerContactModel.getSmsToHandler());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	public HandlerBackingBean getBackingEntity() {
		
		if(this.getHandlerBackingBean() == null) {
			setHandlerBackingBean(new HandlerBackingBean());
		}
		
		return getHandlerBackingBean();
	}	
	

	public Boolean preValidate(StringBuilder errorBuffer) {
	
		Boolean error = Boolean.TRUE;

		if(handlerBackingBean.getAppUserModel().getDob() == null) {

			errorBuffer.append("Kindly Enter Date of Birth");
			return Boolean.FALSE;
		}
		
		if(handlerBackingBean.getAppUserModel().getNicExpiryDate() == null) {

			errorBuffer.append("Kindly Enter CNIC Expiry Date");
			return Boolean.FALSE;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		calendar.set(Calendar.MILLISECOND, 0);  
		Date currentDate = calendar.getTime(); 
		
		if(handlerBackingBean.getAppUserModel().getDob().after(currentDate)) {
			errorBuffer.append("Date of Birth cannot be greater than today.");
			return Boolean.FALSE;
		}
		
		Integer age = this.calculateMyAge(handlerBackingBean.getAppUserModel().getDob());		
		
		if(age < 18) {
			errorBuffer.append("Handle must be atleast 18 year of age.");
			return Boolean.FALSE;
		}
		
		if(handlerBackingBean.getAppUserModel().getNicExpiryDate().before(currentDate) || currentDate.compareTo(handlerBackingBean.getAppUserModel().getNicExpiryDate()) == 0) {
			errorBuffer.append("CNIC Expiry Date cannot be less than or equal today.");
			return Boolean.FALSE;
		}	
		
		return error;
	}
	
	
	public Boolean validate() {
		
		StringBuilder errorBuffer = new StringBuilder();
		
		Boolean isValidated = preValidate(errorBuffer);
		
		try {
			
			if(isValidated && (handlerBackingBean.getHandlerModel().getHandlerId() == null || handlerBackingBean.getHandlerModel().getHandlerId() == 0)) {
				
				/*if(isValidated && !StringUtil.isNullOrEmpty(this.handlerBackingBean.getAppUserModel().getNic())) {
					isValidated = validateCnic(this.handlerBackingBean.getAppUserModel().getNic(), errorBuffer);
				}
				
				if(isValidated && !StringUtil.isNullOrEmpty(this.handlerBackingBean.getAppUserModel().getMobileNo())) {
					isValidated = validateMobileNumber(this.handlerBackingBean.getAppUserModel().getMobileNo(), errorBuffer);
				}*/
				
				if(isValidated) {
					isValidated = validateMobileNumberCNIC(this.handlerBackingBean.getAppUserModel(), errorBuffer);
				}

				if(isValidated && !StringUtil.isNullOrEmpty(this.handlerBackingBean.getAppUserModel().getUsername())) {
					isValidated = validateUserName(this.handlerBackingBean.getAppUserModel().getUsername(), errorBuffer);
				}
				/* implemented password policy by atif hussain
				if(isValidated && !StringUtil.isNullOrEmpty(this.handlerBackingBean.getPassword())) {
					PasswordInputDTO	passwordInputDTO=new PasswordInputDTO();
					passwordInputDTO.setFirstName(this.handlerBackingBean.getAppUserModel().getFirstName());
					passwordInputDTO.setLastName(this.handlerBackingBean.getAppUserModel().getLastName());
					passwordInputDTO.setUserName(this.handlerBackingBean.getAppUserModel().getUsername());
					passwordInputDTO.setPassword(this.handlerBackingBean.getPassword());
					PasswordResultDTO	passwordResultDTO= PasswordConfigUtil.validatePasswordPolicy(passwordInputDTO, null);
					isValidated=passwordResultDTO.getIsValid();
					
					if(!isValidated){
						errorBuffer.append(passwordResultDTO.getErrorMessages());
					}
				}*/
			}
			
		} catch (Exception e) {
			isValidated = Boolean.FALSE;
			errorBuffer.append(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		if(errorBuffer.length() > 0) {
			isValidated = Boolean.FALSE;
			addErrorMessage(null, errorBuffer.toString());
		}
		
		return isValidated;
	} 
	
	
	public Boolean validateCnic(Object object, StringBuilder errorBuffer) throws Exception {

		Boolean isErrorFree = Boolean.TRUE;

		String cnic = (String) object;

		if(!StringUtil.isNullOrEmpty(cnic)) {
			
			/*AppUserModel appUserModel = appUserDAO.loadAppUserByCNIC(cnic);
			
			if(appUserModel != null) {
				errorBuffer.append("User already exist against CNIC "+cnic);
				isErrorFree = Boolean.FALSE;
			}*/
			
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setNic(cnic);
			try{
				mfsAccountManager.isUniqueCNICMobile(appUserModel, null);
			}catch(Exception e){
				String exception = e.getMessage();
				if(exception.equalsIgnoreCase("NICUniqueException")){
					errorBuffer.append("User already exist against CNIC "+cnic);
					isErrorFree = Boolean.FALSE;
				}else{
					errorBuffer.append("Your request cant not be processed at the moment. Please contact your Service Provider ");
					isErrorFree = Boolean.FALSE;
				}
			}
		
		} else {

			errorBuffer.append("Empty CNIC ");
			isErrorFree = Boolean.FALSE;
		}
		
		return isErrorFree;
	}	
	
	
	public Boolean validateMobileNumber(Object object, StringBuilder errorBuffer) throws Exception {

		Boolean isErrorFree = Boolean.TRUE;

		String mobileNumber = (String) object;

		if(!StringUtil.isNullOrEmpty(mobileNumber)) {
			
			/*AppUserModel appUserModel = appUserDAO.loadAppUserByMobileByQuery(mobileNumber.trim());
			
			if(appUserModel != null) {
				errorBuffer.append("User already exist against Mobile Number "+mobileNumber);
				isErrorFree = Boolean.FALSE;
			}*/
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setMobileNo(mobileNumber);
			try{
				mfsAccountManager.isUniqueCNICMobile(appUserModel, null);
				if(accountControlManager.isCnicBlacklisted(appUserModel.getNic())) {
                    throw new FrameworkCheckedException("BlacklistedCnicException");
                }
			}catch(Exception e){
				String exception = e.getMessage();
				if(exception.equalsIgnoreCase("MobileNumUniqueException")){
					errorBuffer.append("User already exist against Mobile Number "+mobileNumber);
					isErrorFree = Boolean.FALSE;
				}else{
					errorBuffer.append("Your request cant not be processed at the moment. Please contact your Service Provider ");
					isErrorFree = Boolean.FALSE;
				}
			}
		
		} else {

			errorBuffer.append("Empty Mobile Number ");
			isErrorFree = Boolean.FALSE;
		}
		
		return isErrorFree;
	}
	
	public Boolean validateMobileNumberCNIC(AppUserModel appUserModel, StringBuilder errorBuffer){
		
		Boolean isErrorFree = Boolean.TRUE;
		if(!StringUtil.isNullOrEmpty(appUserModel.getMobileNo()) && !StringUtil.isNullOrEmpty(appUserModel.getNic())) {
			try{
				mfsAccountManager.isUniqueCNICMobile(appUserModel, null);
			}catch(Exception e){
				String exception = e.getMessage();
				if(exception.equalsIgnoreCase("MobileNumUniqueException")){
					errorBuffer.append("User already exist against Mobile Number "+appUserModel.getMobileNo());
					isErrorFree = Boolean.FALSE;
				}else if(exception.equalsIgnoreCase("NICUniqueException")){
					errorBuffer.append("User already exist against CNIC "+appUserModel.getNic());
					isErrorFree = Boolean.FALSE;
				}else{
					errorBuffer.append("Your request cant not be processed at the moment. Please contact your Service Provider ");
					isErrorFree = Boolean.FALSE;
				}
			}
			if(accountControlManager.isCnicBlacklisted(appUserModel.getNic())) {
				errorBuffer.append("CNIC is Black Listed");
				isErrorFree = Boolean.FALSE;
            }
		} else {

			errorBuffer.append("Empty Mobile Number or CNIC provided ");
			isErrorFree = Boolean.FALSE;
		}
		
		return isErrorFree;
	}
	
	
	 private static int calculateMyAge(Date dateOfBirth) {  
		 
		  Calendar birthCal = new GregorianCalendar();  
		  birthCal.setTime(dateOfBirth); 
		  Calendar nowCal = new GregorianCalendar();  
		  
		  int age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);  
		  
		  boolean isMonthGreater = birthCal.get(Calendar.MONTH) >= nowCal  
		    .get(Calendar.MONTH);  
		  
		  boolean isMonthSameButDayGreater = birthCal.get(Calendar.MONTH) == nowCal.get(Calendar.MONTH) && birthCal.get(Calendar.DAY_OF_MONTH) > nowCal.get(Calendar.DAY_OF_MONTH);  
		  
		    if (isMonthGreater || isMonthSameButDayGreater) {  
		     age=age-1;  
		    } 
		    
		    return age;  
		 }  	
	
	
	public Boolean validateUserName(Object object, StringBuilder errorBuffer) throws Exception {

		Boolean isErrorFree = Boolean.TRUE;
		
		String userName = (String) object;;
		
		if(!StringUtil.isNullOrEmpty(userName)) {
			
			AppUserModel appUserModel = appUserDAO.loadAppUserByUserName(userName);
			
			if(appUserModel != null) {
				errorBuffer.append("User already exist against User Name "+userName);
				isErrorFree = Boolean.FALSE;
			}
		} else {

			errorBuffer.append("Empty User Name ");
			isErrorFree = Boolean.FALSE;
		}
		
		return isErrorFree;
	}	
	
	
	public static void addErrorMessage(String controlID, String summary) {

		Severity severity = FacesMessage.SEVERITY_ERROR;
		
		FacesMessage errorMessage = new FacesMessage();
		errorMessage.setDetail(summary);
		errorMessage.setSummary(summary);
		errorMessage.setSeverity(severity);

		FacesContext.getCurrentInstance().addMessage(null, errorMessage);
//		throw new ValidatorException(errorMessage);
	}		
	
	
		public void fetchAgentName(ActionEvent event) throws FrameworkCheckedException {
		
		HandlerBackingBean handlerBackingBean = this.getBackingEntity();
		
		AllpayUserInfoListViewModel userInfoListViewModel = new AllpayUserInfoListViewModel();
		userInfoListViewModel.setUserId(handlerBackingBean.getAgentMfsId());
		CustomList<AllpayUserInfoListViewModel> cList = this.allpayUserInfoListViewDAO.findByExample(userInfoListViewModel,null,null,new ExampleConfigHolderModel(false, true, false,MatchMode.EXACT));
		List<AllpayUserInfoListViewModel> list = cList.getResultsetList();
	
		if(null!=list && list.size()>0){
			
			RetailerContactModel retailerContactModel=null;
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setMobileNo(list.get(0).getMobileNo());
			searchBaseWrapper.setBasePersistableModel(appUserModel);
			retailerContactModel = retailerContactManager.findRetailerContactByMobileNumber(searchBaseWrapper);
			
			handlerBackingBean.setAgentName(retailerContactModel.getBusinessName());
			handlerBackingBean.setRetailerContactId(retailerContactModel.getRetailerContactId());
			
			this.onChangeAgent();
			addErrorMessage(null,null);
		}
		else
		{	
			handlerBackingBean.setAgentName(null);
			addErrorMessage(null, "No record found against provided Agent ID.");
		}
	}
		
	private void populateAgentInfo(){
		
		try {
			RetailerContactModel retailerContactModel = new RetailerContactModel(handlerBackingBean.getRetailerContactId());
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(retailerContactModel);
			baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
			
			retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
			
			AppUserModel appUserModel = appUserDAO.loadAppUserByCNIC(retailerContactModel.getCnic());
			
			UserDeviceAccountsModel userDeviceAccountsModel = this.userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
			
			handlerBackingBean.setAgentMfsId(userDeviceAccountsModel.getUserId());
			handlerBackingBean.setAgentName(retailerContactModel.getBusinessName());

		} catch (FrameworkCheckedException e) {
			addErrorMessage(null, "Associated Agent do not found");
			e.printStackTrace();
		}
	}	

	public HandlerBackingBean getHandlerBackingBean() {
		return handlerBackingBean;
	}
	
	public void setHandlerBackingBean(HandlerBackingBean handlerBackingBean) {
		this.handlerBackingBean = handlerBackingBean;
	}
	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}	
	public void setProductCatalogManager(ProductCatalogManager productCatalogManager) {
		this.productCatalogManager = productCatalogManager;
	}
	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}
	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}	
	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}	
	public void setAllpaySequenceGenerator(OracleSequenceGeneratorJdbcDAO allpaySequenceGenerator) {
		this.allpaySequenceGenerator = allpaySequenceGenerator;
	}
	
	public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
		this.olaCustomerAccountTypeDao =olaCustomerAccountTypeDao;
	}
	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}
	public AllPayUserInfoListViewDAO getAllpayUserInfoListViewDAO() {
		return allpayUserInfoListViewDAO;
	}
	public void setAllpayUserInfoListViewDAO(
			AllPayUserInfoListViewDAO allpayUserInfoListViewDAO) {
		this.allpayUserInfoListViewDAO = allpayUserInfoListViewDAO;
	}
	public void setAccountControlManager(AccountControlManager accountControlManager) {
		this.accountControlManager = accountControlManager;
	}
	
	
	
	
}