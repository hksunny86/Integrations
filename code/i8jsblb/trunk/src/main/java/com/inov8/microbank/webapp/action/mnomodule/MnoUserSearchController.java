package com.inov8.microbank.webapp.action.mnomodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.mnomodule.MnoUserListViewModel;
import com.inov8.microbank.server.service.mnomodule.MnoUserManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class MnoUserSearchController extends BaseSearchController

{
	private MnoUserManager			mnoUserManager;
	private ReferenceDataManager	referenceDataManager;

	public MnoUserSearchController()
	{
		super.setFilterSearchCommandClass(MnoUserListViewModel.class);
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request, HttpServletResponse response, Boolean activate)
			throws

			Exception
	{
		Long id = ServletRequestUtils.getLongParameter(request, "mnoUserId");
        Long appUserId = ServletRequestUtils.getLongParameter(request, "appUserId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB and then updating it");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject("mnoUserId", id);
			baseWrapper.putObject("appUserId", appUserId);
            baseWrapper.putObject("active", activate);
            this.mnoUserManager.activateDeactivateMnoUser(baseWrapper);
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("mnousermanagement" + ".html"));
		return modelAndView;

	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data  for Mno
		 *
		 */

		MnoModel mnoModel = new MnoModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(mnoModel, "name", SortingOrder.DESC);
		referenceDataWrapper.setBasePersistableModel(mnoModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<MnoModel> mnoModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			mnoModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("mnoModelList", mnoModelList);

		return referenceDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object, PagingHelperModel pagingHelperModel, LinkedHashMap sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		MnoUserListViewModel mnoUserListViewModel = (MnoUserListViewModel) object;
		searchBaseWrapper.setBasePersistableModel(mnoUserListViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("username", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.mnoUserManager.searchMnoUser(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "mnoUserModelList", searchBaseWrapper.getCustomList()
				.getResultsetList());

	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object,
			HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		MnoUserListViewModel bankUserListViewModel = (MnoUserListViewModel) object;
		searchBaseWrapper.setBasePersistableModel(bankUserListViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("username", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.mnoUserManager.searchMnoUser(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "mnoUserModelList", searchBaseWrapper.getCustomList()
				.getResultsetList());

	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setMnoUserManager(MnoUserManager mnoUserManager)
	{
		this.mnoUserManager = mnoUserManager;
	}
}
