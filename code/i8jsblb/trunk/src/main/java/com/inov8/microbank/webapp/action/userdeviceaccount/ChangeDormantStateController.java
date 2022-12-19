package com.inov8.microbank.webapp.action.userdeviceaccount;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.AccountStateConstants;
import com.inov8.microbank.common.util.AccountStateConstantsInterface;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.RegistrationStateConstants;
import com.inov8.microbank.common.vo.AppUserVO;
import com.inov8.microbank.fonepay.model.CustomerVO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ChangeDormantStateController extends AjaxController{

	
	private AppUserManager appUserManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String error = null;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try{
			// getting parameters from request
			Long appUserId = new Long(ServletRequestUtils.getStringParameter(request, "appUserId"));

			AppUserModel appUserModel=appUserManager.loadAppUser(appUserId);
			
			AppUserVO appUserVO = new AppUserVO();
			appUserVO.setMobileNo(appUserModel.getMobileNo());
			appUserVO.setUserName(appUserModel.getUsername());
			appUserVO.setAppUserId(appUserModel.getAppUserId().toString());
			
			if(null!=appUserModel.getAccountStateId()){
				appUserVO.setAccountState(appUserModel.getAccountStateId().toString());
			}
			
			baseWrapper.putObject("useCaseId", PortalConstants.RESTORE_FROM_DORMANCY_USECASE_ID);
			baseWrapper.setBasePersistableModel(appUserVO);
			this.populateAuthenticationParams(baseWrapper, request, appUserVO);
			appUserManager.updateAppUserWithAuthorization(baseWrapper);
			
		}
		catch(Exception exp){
			exp.printStackTrace();
			if(exp != null){
				if(exp.getMessage().contains("authorization request already exist with  Action ID ")){
					error = exp.getMessage();
				}else{
					error = "An error has occurered. Please try again later";
				}
			}else{
				error = "An error has occurered. Please try again later";
			}
		}		
		
		if(error !=null){
		return	buffer.append(error).toString();
		}
		
		String msg = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
		 if (null == msg) {
             buffer.append(MessageUtil.getMessage("genericUpdateSuccessMessage"));
         }else{
        	 buffer.append(msg.toString());
         }
		
		return buffer.toString();
	}

	 protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

		 	AppUserVO appUserVO = (AppUserVO) model;
	        ObjectMapper mapper = new ObjectMapper();
	        Long actionAuthorizationId =null;
	        AppUserVO  oldAppUserVO = new AppUserVO();

	        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
	        mapper.setDateFormat(df);

	        String modelJsonString = null;
	        String initialModelJsonString = null;
	        
	        try {
	        	 oldAppUserVO.setUpdatedOn(appUserVO.getUpdatedOn());
	        	 oldAppUserVO.setAppUserId(appUserVO.getAppUserId());
	        	 oldAppUserVO.setMobileNo(appUserVO.getMobileNo());
	        	 oldAppUserVO.setUserName(appUserVO.getUserName());
	        	 if(appUserVO.getAccountState()!=null){
	        		 oldAppUserVO.setAccountState(AccountStateConstants.ACC_STATE_MAP.get(Long.valueOf(appUserVO.getAccountState())));
	        	 }
	        	 oldAppUserVO.setRegisterationStatus(RegistrationStateConstantsInterface.REG_STATE_MAP.get(RegistrationStateConstantsInterface.DORMANT));
	        	 
	            
	             initialModelJsonString = mapper.writeValueAsString(oldAppUserVO);
	            	
	        	 appUserVO.setUpdatedOn(new Date());
	        	 appUserVO.setRegisterationStatus(RegistrationStateConstantsInterface.REG_STATE_MAP.get(RegistrationStateConstantsInterface.VERIFIED));
	        	 appUserVO.setAccountState(AccountStateConstants.ACC_STATE_MAP.get(AccountStateConstants.ACCOUNT_STATE_COLD));
	        	 modelJsonString = mapper.writeValueAsString(appUserVO);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
	        }

	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, "updateAppUserWithAuthorization");
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, AppUserVO.class.getSimpleName());
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, AppUserVO.class.getName());
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,"userManager");
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,appUserVO.getAppUserId());
	        

	        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
	        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, baseWrapper.getObject("useCaseId"));
	        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, "changedormantstate");
	    }
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
