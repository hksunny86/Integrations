package com.inov8.microbank.webapp.action.userdeviceaccount;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.ajax.ActivateDeactivateAjaxContorller;

public class UserDeviceAccountActivateDeactivateController extends
		ActivateDeactivateAjaxContorller {
	// protected ActivateDeactivateManager activateDeactivateManager;

	private UserDeviceAccountListViewManager userDeviceAccountListViewManager;

	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}

	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ajaxResponseContent = "";
		try {
			ajaxResponseContent = super.getResponseContent(request, response);

			String strId = ServletRequestUtils.getStringParameter(request,
					ActivateDeactivateManager.KEY_ID);
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
			Long id = Long.parseLong(EncryptionUtil.decryptWithDES(strId));
			userDeviceAccountsModel.setUserDeviceAccountsId(id);
			baseWrapperUserDevice
					.setBasePersistableModel(userDeviceAccountsModel);

			baseWrapperUserDevice = userDeviceAccountListViewManager
					.loadUserDeviceAccount(baseWrapperUserDevice);

			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice
					.getBasePersistableModel();

			userDeviceAccountsModel.setUpdatedOn(new Date());
			userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils
					.getCurrentUser());

			baseWrapperUserDevice
					.setBasePersistableModel(userDeviceAccountsModel);

			baseWrapperUserDevice.putObject(PortalConstants.KEY_ACTION_ID,
					PortalConstants.ACTION_UPDATE);
			baseWrapperUserDevice.putObject(PortalConstants.KEY_USECASE_ID,
					PortalConstants.MFS_DEVICE_ACCOUNT_USECASE_ID);

			userDeviceAccountListViewManager
					.updateUserDeviceAccountStatus(baseWrapperUserDevice);
			request.getSession(false).setAttribute(ActivateDeactivateManager.KEY_ID, userDeviceAccountsModel.getUserId());
			request.getSession(false).setAttribute("lckunlckkey", userDeviceAccountsModel.getAccountLocked()==true?"Locked":"Un-Locked");

		} catch (FrameworkCheckedException ex) {
			String msg = ex.getMessage();

				
				if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				throw new FrameworkCheckedException("Record could not be saved.");
				/*super.saveMessage(request, "Record could not be saved.");*/
			}
		}
		return ajaxResponseContent;

	}

}
