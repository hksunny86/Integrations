package com.inov8.microbank.webapp.action.applicationversionmodule;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.applicationversionmodule.ApplicationVersionManager;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public class ApplicationVersionSearchController extends BaseSearchController
{
	private ApplicationVersionManager applicationVersionManager;

	public ApplicationVersionSearchController()
	{
		super.setFilterSearchCommandClass(AppVersionModel.class);
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request,
			HttpServletResponse response,
			Boolean activate) throws Exception
	{

		Long id = ServletRequestUtils.getLongParameter(request,"appVersionId");
		Integer versionNo = ServletRequestUtils.getIntParameter(request,"versionNo");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB and then updating it");
			}
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			AppVersionModel appVersionModel = new AppVersionModel();
			appVersionModel.setAppVersionId(id);
			appVersionModel.setVersionNo(versionNo);
			baseWrapper.setBasePersistableModel(appVersionModel);
			baseWrapper = this.applicationVersionManager.loadApplicationVersion(baseWrapper);
			appVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel();
			appVersionModel.setActive(activate);
			appVersionModel.setUpdatedOn(new Date());
			appVersionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			this.applicationVersionManager.updateApplicationVersion(baseWrapper);

		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("applicationversionmanagement.html"));
		return modelAndView;

	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		return null;
	}

	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		AppVersionModel appVersionModel = (AppVersionModel) model;
		searchBaseWrapper.setBasePersistableModel(appVersionModel);
		if(sortingOrderMap.isEmpty())
			sortingOrderMap.put("appVersionNumber", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.applicationVersionManager.searchApplicationVersion(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "appVersionList",
				searchBaseWrapper.getCustomList().
				getResultsetList());

	}

	public void setApplicationVersionManager(ApplicationVersionManager applicationVersionManager)
	{
		this.applicationVersionManager = applicationVersionManager;
	}
}
