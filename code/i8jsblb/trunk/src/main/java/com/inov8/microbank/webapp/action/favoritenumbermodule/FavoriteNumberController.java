package com.inov8.microbank.webapp.action.favoritenumbermodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.favoritenumbermodule.FavoriteNumberListViewModel;
import com.inov8.microbank.server.service.favoritenumbermodule.FavoriteNumberManager;


public class FavoriteNumberController extends BaseSearchController 
{
	private FavoriteNumberManager favoriteNumberManager;
	
	
	public FavoriteNumberController()
	{
		super.setFilterSearchCommandClass(FavoriteNumberListViewModel.class);
	}
	

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request,HttpServletResponse response,
	                                          Boolean activate) throws Exception
	{
		return null;
	}
	

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> linkedHashMap) 
	throws Exception 
	{
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		FavoriteNumberListViewModel favoriteNumberListViewModel=(FavoriteNumberListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(favoriteNumberListViewModel);
		if(linkedHashMap.isEmpty())
			linkedHashMap.put("userId", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		searchBaseWrapper=this.favoriteNumberManager.searchFavoriteNumbers(searchBaseWrapper);
		 	
		return new ModelAndView(getSearchView(), "favoriteNumberListViewModelList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setFavoriteNumberManager(FavoriteNumberManager favoriteNumberManager) 
	{
		this.favoriteNumberManager = favoriteNumberManager;
	}

}
