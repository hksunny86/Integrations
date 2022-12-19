package com.inov8.microbank.webapp.action.customermodule;

import java.util.Date;
import java.util.LinkedHashMap;

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
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.customermodule.CustomerManager;

/**
 * <p>Title: i8Microbank</p>
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Hassan Javaid
 * @version 1.0
 */

public class CustomerSegmentSearchController extends BaseSearchController
{

	private CustomerManager customerManager;

	public CustomerSegmentSearchController()
	{
		super.setFilterSearchCommandClass(SegmentModel.class);
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request,HttpServletResponse response, Boolean activate) throws Exception
	{
		Long id = ServletRequestUtils.getLongParameter(request, "segmentId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB and then updating it");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setPrimaryKey(id);
			baseWrapper.setBasePersistableModel(segmentModel);
			baseWrapper = this.customerManager.loadCustomerSegment(baseWrapper);
			segmentModel = (SegmentModel) baseWrapper.getBasePersistableModel();
			segmentModel.setIsActive(activate);
			segmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			segmentModel.setUpdatedOn(new Date());
			baseWrapper.setBasePersistableModel(segmentModel);
			this.customerManager.createOrUpdateCustomerSegment(baseWrapper);
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() + ".html"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		SegmentModel segmentModel = (SegmentModel) model;
		searchBaseWrapper.setBasePersistableModel(segmentModel);
		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper = this.customerManager.searchCustomerSegmentList(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "cutomerSegmentModelList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setCustomerManager(CustomerManager customerManager)
	{
		this.customerManager = customerManager;
	}

}
