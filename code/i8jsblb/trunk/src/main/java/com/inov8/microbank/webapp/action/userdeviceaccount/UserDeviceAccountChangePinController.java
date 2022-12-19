package com.inov8.microbank.webapp.action.userdeviceaccount;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;


public class UserDeviceAccountChangePinController extends AjaxController{

	
	private  UserDeviceAccountListViewManager userDeviceAccountListViewManager;
	private AppUserManager appUserManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer buffer = new StringBuffer();
		try{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			// getting parameters from request
			Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "appUserId")));
			String mfsId = new String(ServletRequestUtils.getStringParameter(request, "mfsId"));
			
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(appUserId);
			
			baseWrapper.setBasePersistableModel(appUserModel);
			appUserModel = (AppUserModel) appUserManager.loadAppUser(baseWrapper).getBasePersistableModel();
			baseWrapper.putObject("appUserTypeId", appUserModel.getAppUserTypeId());
			// putting log information into wrapper for further used
			
			baseWrapper.putObject("mfsId", mfsId);
			baseWrapper.putObject("appUserId", appUserId);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE );
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MFS_DEVICE_ACCOUNT_USECASE_ID);
			UserDeviceAccountsModel userDeviceAccountsModel = userDeviceAccountListViewManager.findUserDeviceByAppUserId(appUserId);
			if(null==userDeviceAccountsModel){
				throw new FrameworkCheckedException("user device Account not found");
			}
			baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
			userDeviceAccountListViewManager.changeUserDeviceAccountPin(baseWrapper);
			
			if(appUserModel.getAppUserTypeId().longValue()==12)
				buffer.append("New Login PIN is generated and sent to handler");
			else
				buffer.append("New Login PIN is generated and sent to Agent");
		}
		catch(FrameworkCheckedException ex){
			ex.printStackTrace();
			buffer.append("Operation cannot be performed at the moment");	
		}		
		return buffer.toString();
	}

	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
}

