package com.inov8.microbank.webapp.action.productmodule;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.productmodule.ProductCatalogListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;

/**
 * <p>Title: The microbank Project</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Abdul Qadeer
 * @version 1.0
 */
public class CatalogSearchController extends BaseSearchController
{
	private ProductCatalogManager catalogManager;

	public CatalogSearchController()
	{
		super.setFilterSearchCommandClass(ProductCatalogListViewModel.class);

	}

	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model,
			HttpServletRequest request, LinkedHashMap sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ProductCatalogListViewModel productCatalogueModel = (ProductCatalogListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(productCatalogueModel);
		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.catalogManager.searchCatalog(searchBaseWrapper);

		trimDescription(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "productCatalogModelList", searchBaseWrapper.getCustomList()
				.getResultsetList());

	}

	/**
	 * trimDescription
	 *
	 * @param searchBaseWrapper SearchBaseWrapper
	 */
	private void trimDescription(SearchBaseWrapper searchBaseWrapper)
	{
		List<ProductCatalogListViewModel> list = searchBaseWrapper.getCustomList().getResultsetList();
		for (int i = 0; i < list.size(); i++)
		{
			ProductCatalogListViewModel model = list.get(i);
			if (model.getDescription() != null)
			{
				if (model.getDescription().length() > 50)
				{
					model.setDescription(model.getDescription().substring(0,
							Math.min(model.getDescription().length(), 50))
							+ "...");
				}

			}

		}
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request, HttpServletResponse response,
			Boolean activate) throws Exception
	{
		Long id = ServletRequestUtils.getLongParameter(request, "productCatalogId");
		Integer versionNo = ServletRequestUtils.getIntParameter(request, "versionNo");
		if (null != id)
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			ProductCatalogModel productCatalogModel = new ProductCatalogModel();
			productCatalogModel.setProductCatalogId(id);
			productCatalogModel.setVersionNo(versionNo);

			baseWrapper.setBasePersistableModel(productCatalogModel);
			baseWrapper = this.catalogManager.loadCatalog(baseWrapper);
			//Set the active flag
			productCatalogModel = (ProductCatalogModel) baseWrapper.getBasePersistableModel();
			productCatalogModel.setActive(activate);
			productCatalogModel.setUpdatedOn(new Date());
			productCatalogModel.setUpdatedBy( UserUtils.getCurrentUser().getAppUserId() );
		
			this.catalogManager.updateCatalogActivateDeActivate(baseWrapper);

		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() + ".html"));
		return modelAndView;
	}

	public void setCatalogManager(ProductCatalogManager productCatalogManager)
	{
		this.catalogManager = productCatalogManager;
	}
}
