package com.inov8.microbank.webapp.action.userdeviceaccount;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;

public class UserDeviceAccountSearchController extends BaseSearchController {

	private UserDeviceAccountListViewManager userDeviceAccountListViewManager;

	private AppUserManager appUserManager;

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public UserDeviceAccountSearchController() {
		super.setFilterSearchCommandClass(UserDeviceAccountListViewModel.class);
	}

	protected ModelAndView onToggleActivate(HttpServletRequest request,
			HttpServletResponse response, Boolean activate) throws

	Exception {

		Long id = ServletRequestUtils.getLongParameter(request,
				"userDeviceAccountsId");

		Boolean active = ServletRequestUtils.getBooleanParameter(request,
				"_setActivate");

		if (null != id) {

			// Set the active flag

			try {
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();

				UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();

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
				userDeviceAccountsModel.setAccountEnabled(active);

				baseWrapperUserDevice
						.setBasePersistableModel(userDeviceAccountsModel);
				
				baseWrapperUserDevice.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE );
				baseWrapperUserDevice.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MFS_DEVICE_ACCOUNT_USECASE_ID);
				
				userDeviceAccountListViewManager
						.updateUserDeviceAccountStatus(baseWrapperUserDevice);

			}

			catch (FrameworkCheckedException ex) {
				String msg = ex.getMessage();

				 if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
						.getErrorCode()) {
					super.saveMessage(request, "Record could not be saved.");
				}
			}
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView(
				getSearchView() + ".html"));
		return modelAndView;

	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object object, HttpServletRequest arg2,
			LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		UserDeviceAccountListViewModel userDeviceAccountListViewModel = (UserDeviceAccountListViewModel) object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper
				.setBasePersistableModel(userDeviceAccountListViewModel);
		if (linkedHashMap.isEmpty())
			linkedHashMap.put("userId", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		searchBaseWrapper = this.userDeviceAccountListViewManager
				.searchUserDeviceAccount(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "userDeviceAccountList",
				searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}

}
