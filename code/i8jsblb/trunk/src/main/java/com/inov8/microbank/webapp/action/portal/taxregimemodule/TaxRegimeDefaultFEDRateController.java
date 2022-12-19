package com.inov8.microbank.webapp.action.portal.taxregimemodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;

/**
 * @author Abu Turab
 *
 */
public class TaxRegimeDefaultFEDRateController extends BaseSearchController {
		
	private TaxRegimeFacade taxRegimeFacade;

	public TaxRegimeDefaultFEDRateController() {
		super.setFilterSearchCommandClass(TaxRegimeModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception{
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		TaxRegimeModel taxRegimeModel = (TaxRegimeModel) model;

		searchBaseWrapper.setBasePersistableModel(taxRegimeModel);

		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("name", SortingOrder.ASC);
		}
		
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<UsecaseModel> list = this.taxRegimeFacade.searchTaxRegimeDefaultFEDRates(searchBaseWrapper).getCustomList();

		return new ModelAndView( getSearchView(), "taxRegimeModelList", list.getResultsetList());
	}

	public void setTaxRegimeFacade(TaxRegimeFacade taxRegimeFacade) {
		this.taxRegimeFacade = taxRegimeFacade;
	}
	
	
}
