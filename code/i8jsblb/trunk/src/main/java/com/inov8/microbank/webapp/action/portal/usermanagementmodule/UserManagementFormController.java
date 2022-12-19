package com.inov8.microbank.webapp.action.portal.usermanagementmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementReferenceDataModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PasswordConfigUtil;
import com.inov8.microbank.common.util.PasswordInputDTO;
import com.inov8.microbank.common.util.PasswordResultDTO;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

/**
 * @modifier HassanJavaid
 * @change Action Authorization
 * 
 */
public class UserManagementFormController extends AdvanceAuthorizationFormController
{
	private static final Logger LOGGER = Logger.getLogger( UserManagementFormController.class );
	private ReferenceDataManager	referenceDataManager;
	private UserManagementManager	userManagementManager;
	private AppUserManager appUserManager ;
	private PartnerGroupManager partnerGroupManager;
	@Value("classpath:dictionary")
	private Resource resource;
	

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

	public UserManagementFormController()
	{
		setCommandName("userManagementModel");
		setCommandClass(UserManagementModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception
	{

		String enId = ServletRequestUtils.getStringParameter(req, "appUserId");
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		
		
		/// Added for Resubmit Authorization Request 
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
				throw new FrameworkCheckedException("illegal operation performed");
			}
			
			XStream xstream = new XStream();
			UserManagementModel userManagementModel = (UserManagementModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			userManagementModel.setUsecaseId(actionAuthorizationModel.getUsecaseId());
			
			if(userManagementModel.getUsecaseId().longValue()==PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID){
				userManagementModel.setIsActive(true);
				userManagementModel.setActionId(PortalConstants.ACTION_CREATE);
				req.setAttribute("appUserId",userManagementModel.getAppUserId());
				req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
				req.setAttribute("partnerId", userManagementModel.getPartnerId());
			}
			else{
				userManagementModel.setActionId(PortalConstants.ACTION_UPDATE);
				req.setAttribute("appUserId",userManagementModel.getAppUserId());
				req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
				req.setAttribute("partnerId", userManagementModel.getPartnerId());
			}		
			return userManagementModel;
		}
		///End Added for Resubmit Authorization Request
		
		if (null != enId && enId.trim().length() > 0)
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserManagementModel userManagementModel = new UserManagementModel();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(enId)));
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.userManagementManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

			userManagementModel.setAppUserId(appUserModel.getAppUserId());
			userManagementModel.setEmail(appUserModel.getEmail());
			userManagementModel.setFirstName(appUserModel.getFirstName());
			userManagementModel.setLastName(appUserModel.getLastName());
			userManagementModel.setEmployeeId(appUserModel.getEmployeeId());
			userManagementModel.setDob(appUserModel.getDob());
			userManagementModel.setIsActive(appUserModel.getAccountEnabled());
			userManagementModel.setMobileNo(appUserModel.getMobileNo());
			if(appUserModel.getMobileNo().startsWith("U")) {
				userManagementModel.setMobileNo(null);
			}
			userManagementModel.setMobileTypeId(appUserModel.getMobileTypeId());
			userManagementModel.setUserId(appUserModel.getUsername());
			userManagementModel.setIsPasswordChanged(false);
			userManagementModel.setPassword(appUserModel.getPassword());
			userManagementModel.setConfirmPassword(appUserModel.getPassword());
			userManagementModel.setTellerId(appUserModel.getTellerId());
			//userManagementModel.setPartnerGroupId(this.userManagementManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
//			userManagementModel.setAccessLevelId(
//					UserUtils.determineCurrentAppUserAppRoleModel(appUserModel).getAccessLevelModel().getAccessLevelId()
//				);

			// for the logging process
			userManagementModel.setActionId(PortalConstants.ACTION_UPDATE);
			userManagementModel.setUsecaseId(PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID);

			
			/*Iterator<AppRoleModel> rolesIter = appUserModel.getRoles().iterator();
			if(rolesIter.hasNext())
				userManagementModel.setAppUserRoleId( ((AppRoleModel)rolesIter.next()).getAppRoleId());
			
			else // this should never happen
			{
				req.setAttribute("disable", "disable");
				this.saveMessage(req, "No role is assigned to this user"
						//,super.getText("userManagementModule.recordSaveUnSuccessful", req.getLocale()
						);
				return userManagementModel;
			}*/
			
			
			/*if (AppRoleConstantsInterface.PAYMENT_GATEWAY.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getBankUserIdBankUserModel().getBankId());
			
			else if (AppRoleConstantsInterface.MNO.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getMnoUserIdMnoUserModel().getMnoId());
			
			else if (AppRoleConstantsInterface.CSR.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getOperatorUserIdOperatorUserModel().getOperatorId());
			
			else if (AppRoleConstantsInterface.PRODUCT_SUPPLIER.equals(userManagementModel.getAppUserRoleId())
					|| AppRoleConstantsInterface.PAYMENT_SERVICE.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId());
			*/
			
			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.userManagementManager.searchUserGroup(baseWrapper);
			AppUserPartnerGroupModel appUserPartnerGroupModel = (AppUserPartnerGroupModel)baseWrapper.getBasePersistableModel();
			userManagementModel.setPartnerGroupId(appUserPartnerGroupModel.getPartnerGroupId());
			
			userManagementModel.setAppUserTypeId(appUserModel.getAppUserTypeId());
			
			if (UserTypeConstantsInterface.BANK.equals(userManagementModel.getAppUserTypeId()))
				userManagementModel.setPartnerId(appUserModel.getBankUserIdBankUserModel().getBankId());
			
			else if (UserTypeConstantsInterface.MNO.equals(userManagementModel.getAppUserTypeId()))
				userManagementModel.setPartnerId(appUserModel.getMnoUserIdMnoUserModel().getMnoId());
			
			else if (UserTypeConstantsInterface.INOV8.equals(userManagementModel.getAppUserTypeId()))
				userManagementModel.setPartnerId(appUserModel.getOperatorUserIdOperatorUserModel().getOperatorId());
			
			else if (UserTypeConstantsInterface.SUPPLIER.equals(userManagementModel.getAppUserTypeId()))
				userManagementModel.setPartnerId(appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId());
			
			
			//userManagementModel.setPassword("");
			//userManagementModel.setConfirmPassword("");
			/**
			 * This property is set in the request, so that when after this method loadFormBackingObject(),
			 * laodReferenceData Method is called, then the appUserTypeId is available,
			 * from request, This is then used to find out what type of reference data
			 * to load, as partner, ie. a banks, mnos, suppliers, operators(like i8) etc.
			 */
			req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			req.setAttribute("partnerId", userManagementModel.getPartnerId());

			return userManagementModel;
		}
		else
		{
			UserManagementModel userManagementModel = new UserManagementModel();
			userManagementModel.setIsActive(true);
			userManagementModel.setActionId(PortalConstants.ACTION_CREATE);
			userManagementModel.setUsecaseId(PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID);
			return userManagementModel;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception
	{
		Map referenceDataMap = new HashMap();
		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		/**
		 * Here the appUserRoleId is retrieved from request, In the case if
		 * the request is comming, as if super.showform() is called then, this
		 * property is available as perameter, otherwise, in the case the update case
		 * the property is avaiable as an attribute as it was set in the formBackingObject()
		 * of this class.
		 */
		/*Long appUserTypeId = ServletRequestUtils.getLongParameter(req, "appUserTypeId");
		Long partnerId = ServletRequestUtils.getLongParameter(req, "partnerId");*/
		Long appUserTypeId = null;
		Long partnerId = null;
		if(req.getAttribute("appUserTypeId") != null)
		{
			appUserTypeId = Long.valueOf(req.getAttribute("appUserTypeId").toString());
		}
		if(req.getAttribute("partnerId") != null)
		{
			partnerId = Long.valueOf(req.getAttribute("partnerId").toString());
		}
		if (appUserTypeId == null)
			appUserTypeId = (Long) req.getAttribute("appUserTypeId");

		/**
		 * If appUserRoleId is still null then set AppRoleConstantsInterface.PAYMENT_GATEWAY as the default user type so that
		 * in case of first hit the Bank Partners be selected 
		 */
		if (appUserTypeId == null)
			appUserTypeId = UserTypeConstantsInterface.BANK;

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel, "name",
				SortingOrder.ASC);

		referenceDataWrapper.setBasePersistableModel(mobileTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);

		/*List<AppUserRoleRefModel> appUserRoleTypeRefList = new ArrayList();

		/*AppUserRoleRefModel appUserRoleRefModel = new AppUserRoleRefModel();
		appUserRoleRefModel.setAppUserRoleId(AppRoleConstantsInterface.PAYMENT_GATEWAY);
		appUserRoleRefModel.setName("Bank");
		appUserRoleTypeRefList.add(appUserRoleRefModel);

		appUserRoleRefModel = new AppUserRoleRefModel();
		appUserRoleRefModel.setAppUserRoleId(AppRoleConstantsInterface.CSR);
		appUserRoleRefModel.setName("I8");
		appUserRoleTypeRefList.add(appUserRoleRefModel);

		appUserRoleRefModel = new AppUserRoleRefModel();
		appUserRoleRefModel.setAppUserRoleId(AppRoleConstantsInterface.MNO);
		appUserRoleRefModel.setName("Service Operator");
		appUserRoleTypeRefList.add(appUserRoleRefModel);

		appUserRoleRefModel = new AppUserRoleRefModel();
		appUserRoleRefModel.setAppUserRoleId(AppRoleConstantsInterface.PAYMENT_SERVICE);
		appUserRoleRefModel.setName("Payment Service");
		appUserRoleTypeRefList.add(appUserRoleRefModel);

		appUserRoleRefModel = new AppUserRoleRefModel();
		appUserRoleRefModel.setAppUserRoleId(AppRoleConstantsInterface.PRODUCT_SUPPLIER);
		appUserRoleRefModel.setName("Product Supplier");
		appUserRoleTypeRefList.add(appUserRoleRefModel);*/
		
		AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				appUserTypeModel, "name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<AppUserTypeModel> appUserTypeModelList = null;
		List<AppUserTypeModel> filteredAppUserTypeModelList = new ArrayList<AppUserTypeModel>();
		if (referenceDataWrapper.getReferenceDataList() != null) {
			appUserTypeModelList = referenceDataWrapper.getReferenceDataList();

			// hardcoded for Askari pilot
			for(AppUserTypeModel tModel: appUserTypeModelList){
				if(tModel.getName().equalsIgnoreCase("Bank") 
						|| tModel.getName().equalsIgnoreCase("Operator")
						|| tModel.getName().equalsIgnoreCase("Service Operator")
						|| tModel.getName().equalsIgnoreCase("Supplier")){
					filteredAppUserTypeModelList.add(tModel);
				}
			}

			
		}

		referenceDataMap.put("appUserTypeModelList", filteredAppUserTypeModelList);

		// loading reference data for partner
		PartnerModel partnerModel = new PartnerModel();

		if (null != appUserTypeId)
		{
			UserManagementReferenceDataModel userManagementRefDataModel;
			/**
			 * Loading the reference data for bank, for partner combo
			 */
			if (UserTypeConstantsInterface.BANK.equals(appUserTypeId))
			{
				BankModel bankModel = new BankModel();
				bankModel.setActive(true);
				List<BankModel> bankModelList = null;
				referenceDataWrapper = new ReferenceDataWrapperImpl(bankModel, "name", SortingOrder.ASC);

				referenceDataWrapper.setBasePersistableModel(bankModel);
				try
				{
					referenceDataManager.getReferenceData(referenceDataWrapper);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					bankModelList = referenceDataWrapper.getReferenceDataList();
					List<UserManagementReferenceDataModel> partnerRefDataList = new ArrayList();
					Iterator<BankModel> itrator = bankModelList.iterator();
					while (itrator.hasNext())
					{
						bankModel = itrator.next();
						
						// only core bank need to be listed for user add user screen so filter out any bank other than core
						if(BankConstantsInterface.ASKARI_BANK_ID.longValue() == bankModel.getBankId().longValue()){
							userManagementRefDataModel = new UserManagementReferenceDataModel();
							userManagementRefDataModel.setIdField(bankModel.getBankId());
							userManagementRefDataModel.setNameField(bankModel.getName());
							partnerRefDataList.add(userManagementRefDataModel);
							break;
						}
					}
					
					referenceDataMap.put("partnerRefDataList", partnerRefDataList);
					if(partnerId != null)
					{
						partnerModel.setBankId(partnerId);
					}else
					{
						partnerModel.setBankId(partnerRefDataList.get(0).getIdField());
					}
				}

			}
			/**
			 * Loading the reference data for MNO, for partner combo
			 */
			else if (UserTypeConstantsInterface.MNO.equals(appUserTypeId))
			{
				MnoModel mnoModel = new MnoModel();
				List<MnoModel> mnoModelList = null;
				referenceDataWrapper = new ReferenceDataWrapperImpl(mnoModel, "name", SortingOrder.ASC);

				referenceDataWrapper.setBasePersistableModel(mnoModel);
				try
				{
					referenceDataManager.getReferenceData(referenceDataWrapper);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					mnoModelList = referenceDataWrapper.getReferenceDataList();
					List<UserManagementReferenceDataModel> partnerRefDataList = new ArrayList();
					Iterator<MnoModel> itrator = mnoModelList.iterator();
					while (itrator.hasNext())
					{
						mnoModel = itrator.next();
						userManagementRefDataModel = new UserManagementReferenceDataModel();
						userManagementRefDataModel.setIdField(mnoModel.getMnoId());
						userManagementRefDataModel.setNameField(mnoModel.getName());
						partnerRefDataList.add(userManagementRefDataModel);
					}
					referenceDataMap.put("partnerRefDataList", partnerRefDataList);
					if(partnerId != null)
					{
						partnerModel.setMnoId(partnerId);
					}else
					{
						partnerModel.setMnoId(partnerRefDataList.get(0).getIdField());
					}
				}

			}
			/**
			 * Loading the reference data for operator, for partner combo
			 */
			else if (UserTypeConstantsInterface.INOV8.equals(appUserTypeId))
			{
				OperatorModel operatorModel = new OperatorModel();
				List<OperatorModel> operatorModelList = null;
				referenceDataWrapper = new ReferenceDataWrapperImpl(operatorModel, "name", SortingOrder.ASC);

				referenceDataWrapper.setBasePersistableModel(operatorModel);
				try
				{
					referenceDataManager.getReferenceData(referenceDataWrapper);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					operatorModelList = referenceDataWrapper.getReferenceDataList();
					List<UserManagementReferenceDataModel> partnerRefDataList = new ArrayList();
					Iterator<OperatorModel> itrator = operatorModelList.iterator();
					while (itrator.hasNext())
					{
						operatorModel = itrator.next();
						userManagementRefDataModel = new UserManagementReferenceDataModel();
						userManagementRefDataModel.setIdField(operatorModel.getOperatorId());
						userManagementRefDataModel.setNameField(operatorModel.getName());
						partnerRefDataList.add(userManagementRefDataModel);
					}
					referenceDataMap.put("partnerRefDataList", partnerRefDataList);
					if(partnerId != null)
					{
						partnerModel.setOperatorId(partnerId);
					}else
					{
						partnerModel.setOperatorId(partnerRefDataList.get(0).getIdField());
					}	
				}
			}
			/**
			 * Loading the reference data for supplier, for partner combo
			 */
			else if (UserTypeConstantsInterface.SUPPLIER.equals(appUserTypeId))
			{
				SupplierModel supplierModel = new SupplierModel();
				List<SupplierModel> supplierModelList = null;
				referenceDataWrapper = new ReferenceDataWrapperImpl(supplierModel, "name", SortingOrder.ASC);

				referenceDataWrapper.setBasePersistableModel(supplierModel);
				try
				{
					referenceDataManager.getReferenceData(referenceDataWrapper);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					supplierModelList = referenceDataWrapper.getReferenceDataList();
					List<UserManagementReferenceDataModel> partnerRefDataList = new ArrayList();
					Iterator<SupplierModel> itrator = supplierModelList.iterator();
					while (itrator.hasNext())
					{
						supplierModel = itrator.next();
						userManagementRefDataModel = new UserManagementReferenceDataModel();
						userManagementRefDataModel.setIdField(supplierModel.getSupplierId());
						userManagementRefDataModel.setNameField(supplierModel.getName());
						partnerRefDataList.add(userManagementRefDataModel);
					}
					referenceDataMap.put("partnerRefDataList", partnerRefDataList);
					if(partnerId != null)
					{
						partnerModel.setSupplierId(partnerId);
					}else
					{
						partnerModel.setSupplierId(partnerRefDataList.get(0).getIdField());
					}
				}
			}
		}
		
		//-- load partner group
						
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				partnerModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(partnerModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PartnerModel> partnerModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) 
		{
			partnerModelList = referenceDataWrapper.getReferenceDataList();
		}
						
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		List<PartnerGroupModel> partnerGroupModelList = null;
		if(partnerModelList!=null && partnerModelList.size()>0)
		{
			partnerGroupModel.setActive(true);
			partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
			if (UserTypeConstantsInterface.INOV8.equals(appUserTypeId))
			{
				partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,false);
			}
			else
			{
			partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
			}
		}

		
		referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);

		/*
		AccessLevelModel accessLevelModel = new AccessLevelModel();
		
		List<AccessLevelModel> accessLevelModelList = null;
		referenceDataWrapper = new ReferenceDataWrapperImpl(accessLevelModel, "accessLevelName", SortingOrder.ASC);

		referenceDataWrapper.setBasePersistableModel(accessLevelModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			accessLevelModelList = referenceDataWrapper.getReferenceDataList();
			List<AccessLevelModel> accessLevelRefDataList = new ArrayList();
			Iterator<AccessLevelModel> accessItrator = accessLevelModelList.iterator();
			while (accessItrator.hasNext())
			{
				accessLevelModel = accessItrator.next();
				accessLevelRefDataList.add(accessLevelModel);
			}
			
			referenceDataMap.put("accessLevelRefDataList", accessLevelRefDataList);
		}*/		
		
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors)
			throws Exception
	{
		String serverURL = super.getText("serverURL", req.getLocale());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		UserManagementModel userManagementModel = (UserManagementModel) model;
		baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, userManagementModel.getActionId());
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
		baseWrapper.putObject("serverURL", serverURL);
		
		PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
		PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
		passwordInputDTO.setPassword(userManagementModel.getPassword());
		passwordInputDTO.setFirstName(userManagementModel.getFirstName());
		passwordInputDTO.setLastName(userManagementModel.getLastName());
		passwordInputDTO.setUserName(userManagementModel.getUserId());
		
		try
		{
				passwordResultDTO = PasswordConfigUtil.validatePasswordPolicy(passwordInputDTO, resource.getFile());
				if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0  ){
					this.saveMessage(req, passwordResultDTO.getErrorMessages().get(0));
					return super.showForm(req, res, errors);
				}
				userManagementManager.validateUser(userManagementModel);
				baseWrapper = this.userManagementManager.createNewUser(baseWrapper);
		}
		catch (FrameworkCheckedException fce)
		{
			req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			req.setAttribute("partnerId", userManagementModel.getPartnerId());
			if ("MobileNumUniqueException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.mobileNumNotUnique", req.getLocale()));
			}
			else if ("PasswordConfirmPasswordMissMatchException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.confirmPasswordMissMatch", req.getLocale()));
			}
			else if ("ConstraintViolationException".equals(fce.getMessage()) || "DataIntegrityViolationException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.userIdUnique", req.getLocale()));
			}
			else if (fce.getMessage().contains("Employee ID already exist"))
			{
				this.saveMessage(req,fce.getMessage());
			}	
			else
			{
				this.saveMessage(req,fce.getMessage());
			}
			
			return super.showForm(req, res, errors);
		}
		catch (Exception fce)
		{
			req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			req.setAttribute("partnerId", userManagementModel.getPartnerId());
			this.saveMessage(req,MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
 
		this.saveMessage(req, super.getText("userManagementModule.recordSaveSuccessful", req.getLocale()));
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors)
			throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		UserManagementModel userManagementModel = (UserManagementModel) model;
		String enAppUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
		PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
		passwordInputDTO.setPassword(userManagementModel.getPassword());
		passwordInputDTO.setFirstName(userManagementModel.getFirstName());
		passwordInputDTO.setLastName(userManagementModel.getLastName());
		passwordInputDTO.setUserName(userManagementModel.getUserId());
		List<String> passwordList = appUserManager.getAppUserPreviousThreePasswordsByAppUserId( userManagementModel.getUserId() );
		passwordInputDTO.setHistoryPasswords(passwordList);
		if(enAppUserId != null && enAppUserId.trim().length() > 0){
			userManagementModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(enAppUserId)));
		}
		baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, userManagementModel.getActionId());
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
		try
		{
			if(userManagementModel.getIsPasswordChanged()){
				passwordResultDTO = PasswordConfigUtil.validatePasswordPolicy(passwordInputDTO, resource.getFile());
				if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0  ){
					this.saveMessage(req, passwordResultDTO.getErrorMessages().get(0));
					return super.showForm(req, res, errors);
				}
			}
				userManagementManager.validateUser(userManagementModel);
				baseWrapper = this.userManagementManager.updateUser(baseWrapper);
		}
		catch (FrameworkCheckedException fce)
		{
			req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			req.setAttribute("partnerId", userManagementModel.getPartnerId());
			if ("MobileNumUniqueException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.mobileNumNotUnique", req.getLocale()));
			}
			else if ("PasswordConfirmPasswordMissMatchException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.confirmPasswordMissMatch", req.getLocale()));
			}
			else if ("ConstraintViolationException".equals(fce.getMessage()) || "DataIntegrityViolationException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.userIdUnique", req.getLocale()));
			}
			else if (fce.getMessage().contains("Employee ID already exist"))
			{
				this.saveMessage(req,fce.getMessage());
			}	
			else
			{
				this.saveMessage(req, fce.getMessage());
			}
			return super.showForm(req, res, errors);
		}
		catch (Exception fce)
		{
			req.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			req.setAttribute("partnerId", userManagementModel.getPartnerId());
			this.saveMessage(req,MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}

		this.saveMessage(req, super.getText("userManagementModule.recordUpdateSuccessful", req.getLocale()));
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
	
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		UserManagementModel userManagementModel = (UserManagementModel) command;
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest",false);
		Long actionAuthorizationId = null;
		if(resubmitRequest)
			actionAuthorizationId=ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
		
		String enAppUserId = ServletRequestUtils.getStringParameter(request, "appUserId"); 
		try{
			if(null!=enAppUserId  && enAppUserId.trim().length() > 0){
				if (!resubmitRequest)
					userManagementModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(enAppUserId)));
			
				PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
				PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
				passwordInputDTO.setPassword(userManagementModel.getPassword());
				passwordInputDTO.setFirstName(userManagementModel.getFirstName());
				passwordInputDTO.setLastName(userManagementModel.getLastName());
				passwordInputDTO.setUserName(userManagementModel.getUserId());
				List<String> passwordList = appUserManager.getAppUserPreviousThreePasswordsByAppUserId( userManagementModel.getUserId() );
				passwordInputDTO.setHistoryPasswords(passwordList);
				if(userManagementModel.getIsPasswordChanged()){
					passwordResultDTO = PasswordConfigUtil.validatePasswordPolicy(passwordInputDTO, resource.getFile());
					if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0  ){
						//this.saveMessage(request, passwordResultDTO.getErrorMessages().get(0));
						//return super.showForm(request, response, errors);
						throw new FrameworkCheckedException(passwordResultDTO.getErrorMessages().get(0));
					}
				}	
			}
			else
			{
				PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
				PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
				passwordInputDTO.setPassword(userManagementModel.getPassword());
				passwordInputDTO.setFirstName(userManagementModel.getFirstName());
				passwordInputDTO.setLastName(userManagementModel.getLastName());
				passwordInputDTO.setUserName(userManagementModel.getUserId());	
				
				passwordResultDTO = PasswordConfigUtil.validatePasswordPolicy(passwordInputDTO, resource.getFile());
				if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0  ){
					//this.saveMessage(request, passwordResultDTO.getErrorMessages().get(0));
					//return super.showForm(request, response, errors);
					throw new FrameworkCheckedException(passwordResultDTO.getErrorMessages().get(0));
				}
			}
										
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(userManagementModel);
			String oldRefDataModelString = null;
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(userManagementModel.getUsecaseId());
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(userManagementModel.getUsecaseId(),new Long(0));
			
			if(userManagementModel.getUsecaseId()!=PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID){
				UserManagementModel oldUserManagementModel = userManagementManager.getUserManagementModel(userManagementModel.getAppUserId());
				oldRefDataModelString= xstream.toXML(oldUserManagementModel);
			}
			
			if(nextAuthorizationLevel.intValue()<1){
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
				baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
				if(null!=userManagementModel.getUsecaseId() && userManagementModel.getUsecaseId()==PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID)
				{	
					userManagementManager.validateUser(userManagementModel);
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
					baseWrapper = this.userManagementManager.createNewUser(baseWrapper);
				}
				else
				{	
					userManagementManager.validateUser(userManagementModel);
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper = this.userManagementManager.updateUser(baseWrapper);
				}
				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel,actionAuthorizationId,request);
				this.saveMessage(request,"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			}
			else
			{	
				userManagementManager.validateUser(userManagementModel);
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel.getUsecaseId(),userManagementModel.getUserId(),resubmitRequest,actionAuthorizationId,request);
				this.saveMessage(request,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			request.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			request.setAttribute("partnerId", userManagementModel.getPartnerId());
			if ("MobileNumUniqueException".equals(ex.getMessage()))
			{
				this.saveMessage(request, super.getText("userManagementModule.mobileNumNotUnique2", request.getLocale()));
			}
			else if ("PasswordConfirmPasswordMissMatchException".equals(ex.getMessage()))
			{
				this.saveMessage(request, super.getText("userManagementModule.confirmPasswordMissMatch2", request.getLocale()));
			}
			else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage()))
			{
				this.saveMessage(request, super.getText("userManagementModule.userIdUnique", request.getLocale()));
			}
			else if (ex.getMessage().contains("Employee with same Employee ID already exist."))
			{
				this.saveMessage(request,ex.getMessage());
			}	
			else
			{
				this.saveMessage(request,ex.getMessage());
			}
			if(resubmitRequest)
				return new ModelAndView("redirect:p_i8userformtest.html?isReSubmit=true&authId="+actionAuthorizationId+"&actionId=1"); 
			else
				return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			request.setAttribute("appUserTypeId", userManagementModel.getAppUserTypeId());
			request.setAttribute("partnerId", userManagementModel.getPartnerId());
			
			this.saveMessage(request,MessageUtil.getMessage("6075"));
			
			if(resubmitRequest)
				return new ModelAndView("redirect:p_i8userformtest.html?isReSubmit=true&authId="+actionAuthorizationId+"&actionId=1"); 
			else
				return super.showForm(request, response, errors);
		}
		return new ModelAndView(getSuccessView());
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setUserManagementManager(UserManagementManager userManagementManager)
	{
		this.userManagementManager = userManagementManager;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
