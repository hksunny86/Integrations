/**
 * 
 */
package com.inov8.microbank.webapp.action.userdeviceaccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * @author Administrator
 *
 */
public class UserDeviceAccLockStatusController extends AjaxController
{

	private UserDeviceAccountListViewManager userDeviceAccountListViewManager;

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
//		String strId = ServletRequestUtils.getStringParameter(request,
//				ActivateDeactivateManager.KEY_ID);
//		
//		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
//		BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
//		Long id = Long.parseLong(EncryptionUtil.decryptWithDES(strId));
//		userDeviceAccountsModel.setUserDeviceAccountsId(id);
//		baseWrapperUserDevice
//				.setBasePersistableModel(userDeviceAccountsModel);
//
//		baseWrapperUserDevice = userDeviceAccountListViewManager
//				.loadUserDeviceAccount(baseWrapperUserDevice);
//
//		userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice
//				.getBasePersistableModel();
//		
//		return userDeviceAccountsModel.getAccountLocked()==true?"Locked":"Un-locked";
		HttpSession session = request.getSession(false);
		String result="";
		String key = (String) session.getAttribute(ActivateDeactivateManager.KEY_ID);
		session.removeAttribute(ActivateDeactivateManager.KEY_ID);
		String lckUnlck = (String) request.getSession(false).getAttribute("lckunlckkey");
		session.removeAttribute("lckunlckkey");
		if(null!=key && null != lckUnlck)
		{
			result = key + "," +lckUnlck;
		}
		
		return result;
	}

	public void setUserDeviceAccountListViewManager(UserDeviceAccountListViewManager userDeviceAccountListViewManager)
	{
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}


}
