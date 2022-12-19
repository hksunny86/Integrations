package com.inov8.microbank.server.service.favoritenumbermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.favoritenumbermodule.FavoriteNumberListViewModel;
import com.inov8.microbank.server.dao.favoritenumbermodule.FavoriteNumberListViewDAO;

public class FavoriteNumberManagerImpl  implements FavoriteNumberManager 
{
	private FavoriteNumberListViewDAO favoriteNumberListViewDAO;

	public SearchBaseWrapper searchFavoriteNumbers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		CustomList<FavoriteNumberListViewModel> list = this.favoriteNumberListViewDAO.findByExample((FavoriteNumberListViewModel) 
				searchBaseWrapper.getBasePersistableModel(), 
				searchBaseWrapper.getPagingHelperModel(), 
				searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public void setFavoriteNumberListViewDAO(FavoriteNumberListViewDAO favoriteNumberListViewDAO)
	{
		this.favoriteNumberListViewDAO = favoriteNumberListViewDAO;
	}




}
