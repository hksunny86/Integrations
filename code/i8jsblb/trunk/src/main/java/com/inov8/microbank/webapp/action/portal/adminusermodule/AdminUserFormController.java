package com.inov8.microbank.webapp.action.portal.adminusermodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;

public class AdminUserFormController extends AdvanceFormController{

	private ReferenceDataManager	referenceDataManager;
	private UserManagementManager	userManagementManager;
	private PartnerGroupManager partnerGroupManager;
	private String enId = null;
	

	public AdminUserFormController()
	{
		setCommandName("userManagementModel");
		setCommandClass(UserManagementModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception
	{
		 enId = ServletRequestUtils.getStringParameter(req, "appUserId"); 
		
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
			userManagementModel.setIsActive(appUserModel.getAccountEnabled());
			userManagementModel.setUserId(appUserModel.getUsername());
			// for the logging process
			userManagementModel.setActionId(PortalConstants.ACTION_UPDATE);
			userManagementModel.setUsecaseId(PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID);
			userManagementModel.setPartnerGroupId(this.userManagementManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));

			
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
			
			
			/*
			if (AppRoleConstantsInterface.PAYMENT_GATEWAY.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getBankUserIdBankUserModel().getBankId());
			
			else if (AppRoleConstantsInterface.MNO.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getMnoUserIdMnoUserModel().getMnoId());
			
			else if (AppRoleConstantsInterface.CSR.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getOperatorUserIdOperatorUserModel().getOperatorId());
			
			else if (AppRoleConstantsInterface.PRODUCT_SUPPLIER.equals(userManagementModel.getAppUserRoleId())
					|| AppRoleConstantsInterface.PAYMENT_SERVICE.equals(userManagementModel.getAppUserRoleId()))
				userManagementModel.setPartnerId(appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId());
			*/
			
			//userManagementModel.setPassword("");
			//userManagementModel.setConfirmPassword("");
			/**
			 * This property is set in the request, so that when after this method loadFormBackingObject(),
			 * laodReferenceData Method is called, then the appUserTypeId is available,
			 * from request, This is then used to find out what type of reference data
			 * to load, as partner, ie. a banks, mnos, suppliers, operators(like i8) etc.
			 */
//			req.setAttribute("appUserRoleId", userManagementModel.getAppUserRoleId());

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
		
		
		PartnerModel partnerModel = new PartnerModel();
		partnerModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
		if(null!=enId)
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(enId)));
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.userManagementManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
			
			if(appUserModel!=null)
			{	
			partnerModel.setBankId(appUserModel.getBankUserIdBankUserModel().getBankId());
			}
		}
		else
		{
			
			partnerModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
			
		}
		
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
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
			partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
		}
		
		/*PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		partnerGroupModel.setActive(true);
		if(partnerModelList!=null && partnerModelList.size()>0)
		{
		partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
		}
		 referenceDataWrapper = new ReferenceDataWrapperImpl(
				partnerGroupModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(partnerGroupModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PartnerGroupModel> partnerGroupModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			partnerGroupModelList = referenceDataWrapper.getReferenceDataList();
		}*/

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors)
			throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		UserManagementModel userManagementModel = (UserManagementModel) model;
		userManagementModel.setUsecaseId(PortalConstants.PG_ADMINISTRATION_USECASE_ID);
		baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, userManagementModel.getActionId());
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
		try
		{
			baseWrapper = this.userManagementManager.createNewUserByAdmin(baseWrapper);
		}
		catch (FrameworkCheckedException fce)
		{
			String msg = fce.getMessage();
			if ("MobileNumUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("userManagementModule.mobileNumNotUnique", req.getLocale()));
			}
			else if ("PasswordConfirmPasswordMissMatchException".equals(msg))
			{
				this.saveMessage(req, super.getText("userManagementModule.confirmPasswordMissMatch", req.getLocale()));
			}
			else if (PortalConstants.CONSTRAINT_VIOLATION_EXCEPTION.equals(msg) || "DataIntegrityViolationException".equals(msg))
			{
				this.saveMessage(req, super.getText("userManagementModule.userIdUnique", req.getLocale()));
			}
			else
			{
				this.saveMessage(req, super.getText("userManagementModule.recordSaveUnSuccessful", req.getLocale()));
			}
			
			return super.showForm(req, res, errors);
		}
		catch (Exception fce)
		{
			this.saveMessage(req, MessageUtil.getMessage("6075"));
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
		if(enAppUserId != null && enAppUserId.trim().length() > 0){
			userManagementModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(enAppUserId)));
		}
		userManagementModel.setUsecaseId(PortalConstants.PG_ADMINISTRATION_USECASE_ID);
		baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, userManagementModel.getActionId());
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
		try
		{
			baseWrapper = this.userManagementManager.updateUserByAdmin(baseWrapper);
		}
		catch (FrameworkCheckedException fce)
		{
			if ("MobileNumUniqueException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.mobileNumNotUnique", req.getLocale()));
			}
			else if ("PasswordConfirmPasswordMissMatchException".equals(fce.getMessage()))
			{
				this.saveMessage(req, super.getText("userManagementModule.confirmPasswordMissMatch", req.getLocale()));
			}
			else
			{
				this.saveMessage(req, super.getText("userManagementModule.recordSaveUnSuccessful", req.getLocale()));
			}
			return super.showForm(req, res, errors);
		}
		catch (Exception fce)
		{
			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}

		this.saveMessage(req, super.getText("userManagementModule.recordUpdateSuccessful", req.getLocale()));
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setUserManagementManager(UserManagementManager userManagementManager)
	{
		this.userManagementManager = userManagementManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}	
	

}
