package com.inov8.microbank.webapp.action.portal.forgotveriflypinmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.portal.forgotveriflypinmodule.ForgotVeriflyPinManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Forgot Verifly Change Pin
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Limited
 * </p>
 * 
 * @author Basit Mehr
 * @author imran.sarwar
 * @version 1.0
 */
public class ForgotVeriflyPinChangeController extends AjaxController {
	
	private ForgotVeriflyPinManager forgotVeriflyPinManager;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private ComplaintManager complaintManager;

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer buffer = new StringBuffer();

		// getting parameters from request
		Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "appUserId")));
		String mfsId = new String(ServletRequestUtils.getStringParameter(request, "mfsId"));
		Long smartMoneyAccountId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "smAcId")));
		String mobileNo="";
		if(ServletRequestUtils.getStringParameter(request, "mobileNo") != null){
			mobileNo = new String(ServletRequestUtils.getStringParameter(request, "mobileNo"));
		}
		
		String handlerMobileNo = "";
		if(ServletRequestUtils.getStringParameter(request, "handlerMobileNo") != null){
			handlerMobileNo = new String(ServletRequestUtils.getStringParameter(request, "handlerMobileNo"));
		}
		
		// getting log information from the request
		Long useCaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
		Long actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);

		// putting log information into wrapper for further used
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject("appUserId", appUserId);
		baseWrapper.putObject("mfsId", mfsId);
		baseWrapper.putObject("smartMoneyAccountId", smartMoneyAccountId);
		baseWrapper.putObject("mobileNo", mobileNo);
		baseWrapper.putObject("handlerMobileNo", handlerMobileNo);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);

		Long appUserTypeId = -1L;
		
		try {
			
			AppUserModel appUserModel = forgotVeriflyPinManager.isRetailerOrDistributor(String.valueOf(appUserId));
			appUserTypeId = appUserModel.getAppUserTypeId();
			
			
			if (UserTypeConstantsInterface.DISTRIBUTOR.longValue() == appUserTypeId.longValue() ||
						UserTypeConstantsInterface.RETAILER.longValue() == appUserTypeId.longValue()||
							UserTypeConstantsInterface.HANDLER.longValue() == appUserTypeId.longValue()) {
				
				this.forgotVeriflyPinManager.changeAllPayVeriflyPin(baseWrapper);
				
			} else {
				
				this.forgotVeriflyPinManager.changeVeriflyPin(baseWrapper);
			}
			
			complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_REGENERATE_PIN, appUserId);

			String errorMessage = baseWrapper.getObject("errorMessage") != null ? baseWrapper.getObject("errorMessage").toString() : "";

			if (errorMessage.trim().length() > 0) {
				
				if (errorMessage.equalsIgnoreCase("VeriflyLite")) {
				
					errorMessage = getMessageSourceAccessor().getMessage("forgotveriflyLitepinmodule.new.pin.genrated", request.getLocale());
				}
				
				buffer.append(errorMessage);
				
			} else {
				
				checkUserDeviceCredentials(appUserId);
				
				if(UserTypeConstantsInterface.CUSTOMER.longValue() == appUserTypeId.longValue()) {
					
					buffer.append(getMessageSourceAccessor().getMessage("forgotveriflypinmodule.new.pin.genrated.customer", request.getLocale()));
					
				} else if (UserTypeConstantsInterface.DISTRIBUTOR.longValue() == appUserTypeId.longValue() ||
									UserTypeConstantsInterface.RETAILER.longValue() == appUserTypeId.longValue()) {
					
					buffer.append(getMessageSourceAccessor().getMessage("forgotveriflypinmodule.new.pin.genrated", request.getLocale()));
				
				} else if (UserTypeConstantsInterface.HANDLER.longValue() == appUserTypeId.longValue()) {
					
					buffer.append(getMessageSourceAccessor().getMessage("forgotveriflypinmodule.new.pin.genrated.handler", request.getLocale()));
				}				
			}			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new FrameworkCheckedException("Your request can not be processed at the moment. Please try again later");
		}
		
		return buffer.toString();
	}

	
	/**
	 * @param appUserId
	 */
	private void checkUserDeviceCredentials(Long appUserId)  {
				
		UserDeviceAccountsModel exampleInstance = new UserDeviceAccountsModel();
		exampleInstance.setAppUserId(appUserId);
		CustomList<UserDeviceAccountsModel> customList = userDeviceAccountsDAO.findByExample(exampleInstance, null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		
		List<UserDeviceAccountsModel> modelList = new ArrayList<UserDeviceAccountsModel>(0);
		
		if(customList != null && customList.getResultsetList() != null && !customList.getResultsetList().isEmpty()) {
			
			for(UserDeviceAccountsModel userDeviceAccount : customList.getResultsetList()) {
				
				if(userDeviceAccount.getCredentialsExpired()) {
					
					logger.info("checkUserDeviceCredentials(...) "+userDeviceAccount.getCredentialsExpired());
					
					userDeviceAccount.setCredentialsExpired(Boolean.FALSE);
					modelList.add(userDeviceAccount);
				}
			} 
		}		
		
		userDeviceAccountsDAO.saveOrUpdateCollection(modelList);
	}
	
	/*
	 * @Override protected ModelAndView handleRequestInternal(HttpServletRequest
	 * request, HttpServletResponse response) throws Exception {
	 * 
	 * //getting parameters from request Long appUserId =
	 * ServletRequestUtils.getLongParameter(request,"appUserId"); Long
	 * smartMoneyAccountId =
	 * ServletRequestUtils.getLongParameter(request,"smartMoneyAccountId");
	 * 
	 * //getting log information from the request Long useCaseId =
	 * ServletRequestUtils.getLongParameter(request,PortalConstants.KEY_USECASE_ID);
	 * Long actionId =
	 * ServletRequestUtils.getLongParameter(request,PortalConstants.KEY_ACTION_ID);
	 * 
	 * //putting log information into wrapper for further used BaseWrapper
	 * baseWrapper = new BaseWrapperImpl(); baseWrapper.putObject("appUserId",
	 * appUserId); baseWrapper.putObject("smartMoneyAccountId",
	 * smartMoneyAccountId);
	 * baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
	 * baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
	 * 
	 * this.forgotVeriflyPinManager.changeVeriflyPin(baseWrapper);
	 * 
	 * String errorMessage =
	 * baseWrapper.getObject("errorMessage")!=null?baseWrapper.getObject("errorMessage").toString():"";
	 * 
	 * if(errorMessage.trim().length() > 0){
	 * ControllerUtils.saveMessage(request, errorMessage); }else{
	 * ControllerUtils.saveMessage(request,
	 * getMessageSourceAccessor().getMessage("forgotveriflypinmodule.new.pin.genrated",
	 * request.getLocale())); }
	 * 
	 * return new ModelAndView(new
	 * RedirectView("p_forgotveriflypinmanagement.html"));
	 *  }
	 */
	public void setForgotVeriflyPinManager(ForgotVeriflyPinManager forgotVeriflyPinManager) {
		this.forgotVeriflyPinManager = forgotVeriflyPinManager;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}
}
