package com.inov8.microbank.webapp.action.appusermodule;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.*;
import com.inov8.microbank.common.model.AppUserMobileHistoryModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.mobilenetworks.model.MobileNetworkModel;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUserMobileHistoryFormController extends AdvanceAuthorizationFormController{
    
	private AppUserManager appUserManager;
    
	public AppUserMobileHistoryFormController() {
		setCommandName("appUserMobileHistoryModel");
		setCommandClass(AppUserMobileHistoryModel.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		CustomList<AppUserMobileHistoryModel> list = null;
		CustomList<AppUserMobileHistoryModel> historyList = new CustomList<AppUserMobileHistoryModel>();
		historyList.setResultsetList(new ArrayList<AppUserMobileHistoryModel>());
		AppUserMobileHistoryModel model = new AppUserMobileHistoryModel();
		if ( !GenericValidator.isBlankOrNull(appUserId) ) {
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();			
			model.setAppUserId(new Long(EncryptionUtil.decryptForAppUserId( appUserId)));
			wrapper.setBasePersistableModel(model);
			wrapper = this.appUserManager.loadMobileHistory(wrapper);
			list = wrapper.getCustomList();
			for(AppUserMobileHistoryModel history : list.getResultsetList()){
				if(history.getMobileNo() != null){
					historyList.getResultsetList().add(history);
				}
			}
			ReferenceDataWrapper dataWrapper = new ReferenceDataWrapperImpl(new MobileNetworkModel());
			dataWrapper = referenceDataManager.getReferenceData(dataWrapper);
			List<MobileNetworkModel> networkModelList = dataWrapper.getReferenceDataList();
			referenceDataMap.put("networkModelList", networkModelList);
			referenceDataMap.put("mobileHistoryList", historyList.getResultsetList());
			return referenceDataMap;
		}
		return referenceDataMap;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String appUserId = ServletRequestUtils.getStringParameter(req,"appUserId");
		String customerId = ServletRequestUtils.getStringParameter(req,"customerId");
		String agentId = ServletRequestUtils.getStringParameter(req,"agentId");
		String handlerId = ServletRequestUtils.getStringParameter(req,"handlerId");
		String mfsId = ServletRequestUtils.getStringParameter(req,"mfsId");
		
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		AppUserMobileHistoryModel model = new AppUserMobileHistoryModel();
		CustomList<AppUserMobileHistoryModel> list = null;
		AppUserMobileHistoryModel appUserMobileHistoryModel = new AppUserMobileHistoryModel();

		if (!GenericValidator.isBlankOrNull(appUserId)) {
			model.setAppUserId(new Long(EncryptionUtil.decryptForAppUserId( appUserId)));
			wrapper.setBasePersistableModel(model);
			wrapper = this.appUserManager.loadMobileHistory(wrapper);
			list = wrapper.getCustomList();
			if(!list.getResultsetList().isEmpty() && list.getResultsetList().size() > 0){
				model = list.getResultsetList().get(0);
			}
			String oldMobileNo = model.getMobileNo();
			req.setAttribute("agentId", agentId);
			req.setAttribute("customerId", customerId);
			req.setAttribute("handlerId", handlerId);
			req.setAttribute("mobileNo", oldMobileNo);
			req.setAttribute("mfsId", mfsId);
			AppUserModel appUserModel = appUserManager.loadAppUser(new Long(EncryptionUtil.decryptForAppUserId( appUserId)));
			if(appUserModel != null)
				appUserMobileHistoryModel.setMobileNetworkId(appUserModel.getMobileNetworkId());
		}
		return appUserMobileHistoryModel;
	}

	@Override
	public ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView( new RedirectView("home.html"));
		return modelAndView;
	}

	@SuppressWarnings("finally")
	@Override
	public ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		String customerId = ServletRequestUtils.getStringParameter(req,"customerId");
		String agentId = ServletRequestUtils.getStringParameter(req,"agentId");
		String handlerId = ServletRequestUtils.getStringParameter(req,"handlerId");
		String mfsId = ServletRequestUtils.getStringParameter(req,"mfsId");
		String oldMobile = ServletRequestUtils.getStringParameter(req,"oldMobile");
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AppUserMobileHistoryModel model = null;
		boolean isError = false;
		try{		
			model = (AppUserMobileHistoryModel) obj;
			baseWrapper.putObject("mobileNo", model.getMobileNo());
			baseWrapper.putObject("appUserId", Long.valueOf(EncryptionUtil.decryptWithDES(model.getAppUserIdAppUserModel().getAppUserId().toString())));
			
			boolean isUniqeMobileNum = appUserManager.isMobileNumberCNICUnique(baseWrapper);
			if(!isUniqeMobileNum){
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
			
			baseWrapper.setBasePersistableModel(model);
			
			Long appUserTypeId = new Long(req.getParameter("appUserIdAppUserModel.appUserTypeId"));
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			if(appUserTypeId.equals(UserTypeConstantsInterface.CUSTOMER)){
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_CUSTOMER_MOBILE_USECASE_ID));
			}else if(appUserTypeId.equals(UserTypeConstantsInterface.RETAILER)){
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_AGENT_MOBILE_USECASE_ID));
			}else if(appUserTypeId.equals(UserTypeConstantsInterface.HANDLER)){
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_HANDLER_MOBILE_USECASE_ID));
			}
			
			model.setAppUserId(Long.valueOf(EncryptionUtil.decryptWithDES(model.getAppUserIdAppUserModel().getAppUserId().toString())));
			baseWrapper.setBasePersistableModel(model);
		    baseWrapper = this.appUserManager.updateMobileNo(baseWrapper);
		    String msg = super.getText("mobilehistory.update.success", req.getLocale());
		    oldMobile = model.getMobileNo();
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException exception){
			model.setAppUserId(Long.valueOf(EncryptionUtil.decryptForAppUserId(model.getAppUserIdAppUserModel().getAppUserId().toString())));
			exception.getInternalException();
			exception.printStackTrace();
			String msg = exception.getMessage();
			if("MobileNumUniqueException".equals(msg)){
				this.saveMessage(req, super.getText("mobilehistory.update.failure.alreadyexist", req.getLocale() ));
			}	
			else if(exception.getErrorCode() == ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION){
				super.saveMessage(req, super.getText("mobilehistory.update.failure.alreadyexist", req.getLocale()));
			}else{
				super.saveMessage(req, super.getText("mobilehistory.update.failure", req.getLocale()));				
			}
			//return super.showForm(req, res, errors);
		} finally {			
			ModelAndView modelAndView = null;
			if(!customerId.isEmpty()){
				modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?customerId="+customerId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
			}else if(!agentId.isEmpty()){
				modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?agentId="+agentId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
			}else if(!handlerId.isEmpty()){
				modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?mfsId="+mfsId+"&handlerId="+handlerId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
			}else{
				modelAndView = new ModelAndView(new RedirectView("home.html"));
			}
			
			return modelAndView;
		}
	}

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest req,
			HttpServletResponse res, Object obj, BindException errors)
			throws Exception {
		
		String customerId = ServletRequestUtils.getStringParameter(req,"customerId");
		String agentId = ServletRequestUtils.getStringParameter(req,"agentId");
		String handlerId = ServletRequestUtils.getStringParameter(req,"handlerId");
		String mfsId = ServletRequestUtils.getStringParameter(req,"mfsId");
		String oldMobile = ServletRequestUtils.getStringParameter(req,"oldMobile");
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AppUserMobileHistoryModel model = null;
		ModelAndView modelAndView = null;
		boolean isError = false;
		
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(req, "resubmitRequest",false);
		Long actionAuthorizationId = null;
		if(resubmitRequest)
			actionAuthorizationId=ServletRequestUtils.getLongParameter(req, "actionAuthorizationId");

		model = (AppUserMobileHistoryModel) obj;
		Long appUserId = Long.valueOf(EncryptionUtil.decryptForAppUserId( model.getAppUserIdAppUserModel().getAppUserId().toString()));
		model.setAppUserId(appUserId);
		baseWrapper.putObject("mobileNo", model.getMobileNo());
		baseWrapper.putObject("appUserId", appUserId);
		
		try{		
			XStream xstream = new XStream();
			
			AppUserMobileHistoryModel appUserMobileHistoryModelAuth = (AppUserMobileHistoryModel) model.clone();
			AppUserMobileHistoryModel oldAppUserMobileHistoryModelAuth = (AppUserMobileHistoryModel) model.clone();
			oldAppUserMobileHistoryModelAuth.setMobileNo(oldMobile);
			
			String refDataModelString= xstream.toXML(appUserMobileHistoryModelAuth);
			String oldRefDataModelString= xstream.toXML(oldAppUserMobileHistoryModelAuth);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(model.getUsecaseId());
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(model.getUsecaseId(),new Long(0));
			
			if (nextAuthorizationLevel.intValue() < 1) {
				baseWrapper.putObject("mobileNo", model.getMobileNo());
				baseWrapper.putObject("appUserId", appUserId);
				
				boolean isUniqeMobileNum = appUserManager.isMobileNumberCNICUnique(baseWrapper);
				if(!isUniqeMobileNum){
					throw new FrameworkCheckedException("MobileNumUniqueException");
				}
				
				baseWrapper.setBasePersistableModel(model);
				
				Long appUserTypeId = new Long(req.getParameter("appUserIdAppUserModel.appUserTypeId"));
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				if(appUserTypeId.equals(UserTypeConstantsInterface.CUSTOMER)){
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_CUSTOMER_MOBILE_USECASE_ID));
				}else if(appUserTypeId.equals(UserTypeConstantsInterface.RETAILER)){
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_AGENT_MOBILE_USECASE_ID));
				}else if(appUserTypeId.equals(UserTypeConstantsInterface.HANDLER)){
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_HANDLER_MOBILE_USECASE_ID));
				}
				
//				model.setAppUserId(Long.valueOf(EncryptionUtil.decryptWithDES(model.getAppUserIdAppUserModel().getAppUserId().toString())));
				baseWrapper.setBasePersistableModel(model);
			    baseWrapper = this.appUserManager.updateMobileNo(baseWrapper);
			    String msg = super.getText("mobilehistory.update.success", req.getLocale());
			    oldMobile = model.getMobileNo();
				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel,actionAuthorizationId,req);
				this.saveMessage(req,"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			
			} else {
				
				boolean isUniqeMobileNum = appUserManager.isMobileNumberCNICUnique(baseWrapper);
				if(!isUniqeMobileNum){
					throw new FrameworkCheckedException("MobileNumUniqueException");
				}

				actionAuthorizationId = createAuthorizationRequest(
						nextAuthorizationLevel, "", refDataModelString,oldRefDataModelString,
						usecaseModel.getUsecaseId(),
						model.getAppUserId().toString(), resubmitRequest,
						actionAuthorizationId, req);
				this.saveMessage(req,
						"Action is pending for approval against reference Action ID : "
								+ actionAuthorizationId);
				if(!customerId.isEmpty()){
					modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?customerId="+customerId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
				}else if(!agentId.isEmpty()){
					modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?agentId="+agentId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
				}else if(!handlerId.isEmpty()){
					modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?mfsId="+mfsId+"&handlerId="+handlerId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
				}else{
					modelAndView = new ModelAndView(new RedirectView("home.html"));
				}
			}

		}catch(FrameworkCheckedException exception){
			//model.setAppUserId(Long.valueOf(EncryptionUtil.decryptWithDES(model.getAppUserIdAppUserModel().getAppUserId().toString())));
			exception.getInternalException();
			exception.printStackTrace();
			String msg = exception.getMessage();
			if("MobileNumUniqueException".equals(msg)){
				this.saveMessage(req, super.getText("mobilehistory.update.failure.alreadyexist", req.getLocale() ));
			}else{
				super.saveMessage(req, msg);				
			}
			//return super.showForm(req, res, errors);
			
		}catch(Exception exception){
			exception.printStackTrace();
			super.saveMessage(req, MessageUtil.getMessage("6075"));				
		}  
		finally {			
			
			if(!customerId.isEmpty()){
				modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?customerId="+customerId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
			}else if(!agentId.isEmpty()){
				modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?agentId="+agentId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
			}else if(!handlerId.isEmpty()){
				modelAndView = new ModelAndView(new RedirectView("p_mobilehistory.html?mfsId="+mfsId+"&handlerId="+handlerId+"&appUserId="+model.getAppUserId().toString()+"&mobileNo="+oldMobile));
			}else{
				modelAndView = new ModelAndView(new RedirectView("home.html"));
			}
			
			
		}
		
		return modelAndView;
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
}