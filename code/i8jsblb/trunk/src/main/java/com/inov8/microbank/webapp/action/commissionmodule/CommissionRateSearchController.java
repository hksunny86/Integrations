package com.inov8.microbank.webapp.action.commissionmodule;

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
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionRateListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.commissionmodule.CommissionRateManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class CommissionRateSearchController extends BaseSearchController
{
	private CommissionRateManager commissionRateManager;

	public CommissionRateSearchController()
	{
		super.setFilterSearchCommandClass(CommissionRateListViewModel.class);
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request, HttpServletResponse response,
			Boolean activate) throws Exception
	{
		Long id = ServletRequestUtils.getLongParameter(request, "commissionRateId");
		Integer versionNo = ServletRequestUtils.getIntParameter(request, "versionNo");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB and then updating it");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			CommissionRateModel commissionRateModel = new CommissionRateModel();
			commissionRateModel.setCommissionRateId(id);
			commissionRateModel.setVersionNo(versionNo);
			baseWrapper.setBasePersistableModel(commissionRateModel);
			baseWrapper = this.commissionRateManager.loadCommissionRate(baseWrapper);
			//Set the active flag
			commissionRateModel = (CommissionRateModel) baseWrapper.getBasePersistableModel();
			commissionRateModel.setActive(activate);

			commissionRateModel.setUpdatedOn(new Date());
			commissionRateModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

			this.commissionRateManager.updateCommissionRate(baseWrapper);
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("commissionratemanagement.html"));
		return modelAndView;

	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		return null;
	}

	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model,
			HttpServletRequest request, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		CommissionRateListViewModel commissionRateListViewModel = (CommissionRateListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(commissionRateListViewModel);

		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("productName", SortingOrder.ASC);

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.commissionRateManager.searchCommissionRate(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "commissionRateList", searchBaseWrapper.getCustomList()
				.getResultsetList());

	}

	public void setCommissionRateManager(CommissionRateManager commissionRateManager)
	{
		this.commissionRateManager = commissionRateManager;
	}

}
