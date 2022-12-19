package com.inov8.microbank.fonepay.controller;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.TaxRegimeModelVO;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.fonepay.model.CustomerVO;
import com.inov8.microbank.fonepay.model.VirtualCardEnableDisableVO;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ActivateDeactivateWebServiceController extends AjaxController {

	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private FonePayManager fonePayManager;
	@Autowired
	private AppUserManager userManager;
	


	@SuppressWarnings("null")
	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String error = null;
		boolean isActivateWebService = false;
		String isActivated = "Activated";
		AppUserModel appUserModel = null;
		
			String appUserId = ServletRequestUtils.getStringParameter(request, "appUserId");
			String ActivateDeactivateWebService = ServletRequestUtils.getStringParameter(request, "webServiceEnabled");
			if(ActivateDeactivateWebService !=null && ActivateDeactivateWebService.equals("true")){
				isActivateWebService = true;
				isActivated="Deactivated";
			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			try{
			if(appUserId != null || appUserId.length() !=0){
				appUserModel =	userManager.loadAppUser(Long.parseLong(appUserId));
				
				CustomerModel custModel = null;
				custModel = this.customerManager.findByPrimaryKey(appUserModel.getCustomerId());
				
					if(null != custModel){
						CustomerVO customerVO = new CustomerVO();
						
						if((isActivateWebService && custModel.getWebServiceEnabled() == true) || (!isActivateWebService && custModel.getWebServiceEnabled() == false)){
	                        if(isActivateWebService){
	                        	customerVO.setIsWebServiceEnabled(false);
								customerVO.setCustomerId(custModel.getCustomerId().toString());
								customerVO.setMobileNo(custModel.getMobileNo());
								customerVO.setName(custModel.getName());
								customerVO.setUpdatedOn(new Date());
								customerVO.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId().toString());
								
								baseWrapper.putObject("useCaseId",PortalConstants.DEACTIVATE_WEB_SERVICE_USECASE_ID);
								baseWrapper.setBasePersistableModel(customerVO);
								
								logger.info("<-----------------Activate / Deactivate Customer Web Service------------->");
								logger.info("Going to deactivate Web Service of customer having appUserId : " + appUserId);
								
								this.populateAuthenticationParams(baseWrapper,request,customerVO);
								fonePayManager.updateCustomerModelWithAuthorization(baseWrapper);
							}else{
								customerVO.setIsWebServiceEnabled(true);
								customerVO.setCustomerId(custModel.getCustomerId().toString());
								customerVO.setMobileNo(custModel.getMobileNo());
								customerVO.setName(custModel.getName());
								customerVO.setUpdatedOn(new Date());
								customerVO.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId().toString());
								
								baseWrapper.putObject("useCaseId",PortalConstants.ACTIVATE_WEB_SERVICE_USECASE_ID);
								baseWrapper.setBasePersistableModel(customerVO);
								
								logger.info("<-----------------Activate / Deactivate Customer Web Service------------->");
								logger.info("Going to activate Web Service of customer having appUserId : " + appUserId);
								
								this.populateAuthenticationParams(baseWrapper,request,customerVO);
								fonePayManager.updateCustomerModelWithAuthorization(baseWrapper);
							}
						}else{
							logger.error("Web Service is already " + isActivated );
							super.saveMessage(request, "Web Service is already " + isActivated);
						}

					}else{
						logger.error("Your request can not be processed at the moment. Please try again later");
						super.saveMessage(request,"Your request can not be processed at the moment. Please try again later");
					}
			}else{
				logger.error("Your request can not be processed at the moment. Please try again later");
				super.saveMessage(request,"Your request can not be processed at the moment. Please try again later");
			}
			}catch(Exception exp){
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
                msg = "Web Service " + isActivated + " Successfully.";
            }
            super.saveMessage(request, msg);
		return buffer.append(msg).toString();
	}
	
	 protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

		 	CustomerVO customerVO = (CustomerVO) model;
	        ObjectMapper mapper = new ObjectMapper();
	        Long actionAuthorizationId =null;

	        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
	        mapper.setDateFormat(df);

	        String modelJsonString = null;
	        String initialModelJsonString = null;
	        
	        try {
	            modelJsonString = mapper.writeValueAsString(customerVO);
	            
	            CustomerVO  oldCustomerVO = new CustomerVO();
	            
	            oldCustomerVO.setCustomerId(customerVO.getCustomerId());
	            oldCustomerVO.setMobileNo(customerVO.getMobileNo());
	            oldCustomerVO.setName(customerVO.getName());
	            
	            if(customerVO.getIsWebServiceEnabled() == true)
	            	oldCustomerVO.setIsWebServiceEnabled(false);
	            else
	            	oldCustomerVO.setIsWebServiceEnabled(true);
	            	 
	            	initialModelJsonString = mapper.writeValueAsString(oldCustomerVO);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
	        }

	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, "updateCustomerModelWithAuthorization");
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, CustomerVO.class.getSimpleName());
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, CustomerVO.class.getName());
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,"fonePayManager");
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,customerVO.getCustomerId());
	        

	        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
	        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, baseWrapper.getObject("useCaseId"));
	        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, "p_activatedeactivatewebservice");
	    }

	

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setFonePayManager(FonePayManager fonePayManager) {
		this.fonePayManager = fonePayManager;
	}


	public void setAppUserManager(AppUserManager userManager) {
		this.userManager = userManager;
	}


}
