package com.inov8.microbank.webapp.action.operatormodule;

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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.operatormodule.OperatorUserListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.operatormodule.OperatorUserManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class OperatorUserSearchController extends BaseSearchController {

	private OperatorUserManager operatorUserManager;

	private AppUserManager appUserManager;

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public OperatorUserSearchController() {

		super.setFilterSearchCommandClass(OperatorUserListViewModel.class);
	}

	public void setOperatorUserManager(OperatorUserManager operatorUserManager) {
		this.operatorUserManager = operatorUserManager;
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object object, HttpServletRequest arg2,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		OperatorUserListViewModel operatorUserListViewModel = (OperatorUserListViewModel) object;
		searchBaseWrapper.setBasePersistableModel(operatorUserListViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("username", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.operatorUserManager
				.searchOperatorUser(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "operatorUserModelList",
				searchBaseWrapper.getCustomList().getResultsetList());
	}

	protected ModelAndView onToggleActivate(HttpServletRequest request,
			HttpServletResponse response, Boolean activate) throws

	Exception {

		Long id = ServletRequestUtils.getLongParameter(request, "operatorUserId");
		Boolean active = ServletRequestUtils.getBooleanParameter(request,
				"_setActivate");

		if (null != id) {
			if (log.isDebugEnabled()) {
				log
						.debug("id is not null....retrieving object from DB and then updating it");
			}

			// Set the active flag

			try {
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				AppUserModel appUserModel = new AppUserModel();
				appUserModel.setOperatorUserId(id);
				searchBaseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper = appUserManager.searchAppUser(searchBaseWrapper);
				appUserModel = (AppUserModel) baseWrapper
						.getBasePersistableModel();
				appUserModel.setAccountEnabled(active);
				appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				appUserModel.setUpdatedOn(new Date());
				baseWrapper.setBasePersistableModel(appUserModel);
				appUserManager.saveOrUpdateAppUser(baseWrapper);
			}

			catch (FrameworkCheckedException ex) {
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

}
