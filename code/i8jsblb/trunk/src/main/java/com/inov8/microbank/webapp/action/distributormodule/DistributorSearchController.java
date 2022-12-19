package com.inov8.microbank.webapp.action.distributormodule;

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
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.distributormodule.DistributorListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;

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

public class DistributorSearchController extends BaseSearchController

{
	private DistributorManager distributorManager;

	public DistributorSearchController()
	{
		super.setFilterSearchCommandClass(DistributorListViewModel.class);
	}

	public void setDistributorManager(DistributorManager distributorManager)
	{
		this.distributorManager = distributorManager;
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request, HttpServletResponse response,
			Boolean activate) throws

	Exception
	{
		Long id = ServletRequestUtils.getLongParameter(request, "distributorId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB and then updating it");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setDistributorId(id);
			baseWrapper.setBasePersistableModel(distributorModel);
			distributorModel = (DistributorModel)this.distributorManager.loadDistributor(baseWrapper).getBasePersistableModel() ;
			distributorModel.setUpdatedOn(new Date());
			distributorModel.setUpdatedByAppUserModel(UserUtils
					.getCurrentUser());
			distributorModel.setActive(activate) ;			
			baseWrapper.setBasePersistableModel(distributorModel);
			this.distributorManager.createOrUpdateDistributor(baseWrapper);
		
		}		
		ModelAndView modelAndView = new ModelAndView(new RedirectView("distributormanagement.html"));
		return modelAndView;
	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		return null;
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object,
			HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		DistributorListViewModel distributorListViewModel = (DistributorListViewModel) object;
		searchBaseWrapper.setBasePersistableModel(distributorListViewModel);
		if(sortingOrderMap.isEmpty())
		    sortingOrderMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.distributorManager.searchDistributor(searchBaseWrapper);

		return new ModelAndView(super.getSearchView(), "distributorModelList", searchBaseWrapper
				.getCustomList().getResultsetList());
	}

}
