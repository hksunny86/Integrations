package com.inov8.microbank.server.service.tickermodule;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TickerModel;
import com.inov8.microbank.common.model.tickermodule.TickerListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.mfsmodule.TickerDAO;
import com.inov8.microbank.server.dao.tickermodule.TickerListViewDAO;

public class TickerManagerImpl implements TickerManager {
	
	private TickerListViewDAO tickerListViewDAO ;
	private TickerDAO tickerDAO ;
	
	public void setTickerDAO(TickerDAO tickerDAO) {
		this.tickerDAO = tickerDAO;
	}


	public void setTickerListViewDAO(TickerListViewDAO tickerListViewDAO) {
		this.tickerListViewDAO = tickerListViewDAO;
	}

	

	public BaseWrapper loadDefaultTicker(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return this.tickerDAO.loadDefaultTicker();
	}


	public SearchBaseWrapper searchTicker(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		TickerModel tickerModel = (TickerModel)searchBaseWrapper.getBasePersistableModel();
		CustomList<TickerModel> customList = this.tickerDAO.findByExample(tickerModel);
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}


	public SearchBaseWrapper loadTickerUser(SearchBaseWrapper searchBaseWrapper) {
		TickerListViewModel tickerListViewModel = this.tickerListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(tickerListViewModel);
		return searchBaseWrapper;
	}
	
	public BaseWrapper createTickerUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		BaseWrapper baseWrapperDefaultTicker = new BaseWrapperImpl();
		TickerListViewModel tickerListViewModel= (TickerListViewModel)baseWrapper.getBasePersistableModel();
		TickerModel tickerModel = new TickerModel();
		
		tickerModel.setTickerString(tickerListViewModel.getTickerString());
		tickerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		tickerModel.setCreatedOn(new Date());
		tickerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		tickerModel.setUpdatedOn(new Date());
		
		if (tickerListViewModel.getAppUserId()!=null)
		{
			tickerModel.setAppUserId(tickerListViewModel.getAppUserId());	
			
		}
		else
		{
			
			baseWrapperDefaultTicker=this.tickerDAO.loadDefaultTicker();
			TickerModel defTickerModel = (TickerModel)baseWrapperDefaultTicker.getBasePersistableModel();
			if (defTickerModel!=null)
			{
				throw  new FrameworkCheckedException("Default ticker already exits");
				
			}
			
			
			
	
		}
		this.tickerDAO.saveOrUpdate(tickerModel);
		
		return baseWrapper;
	}

	public SearchBaseWrapper searchTickerUser(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		
		CustomList<TickerListViewModel> list = this.tickerListViewDAO
		.findByExample((TickerListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
				searchBaseWrapper.setCustomList(list);
return 	searchBaseWrapper;
		
	}

	public BaseWrapper updateTickerUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		BaseWrapper baseWrapperDefaultTicker = new BaseWrapperImpl();
		TickerListViewModel tickerListViewModel= (TickerListViewModel)baseWrapper.getBasePersistableModel();
		TickerModel tickerModel = new TickerModel();
		tickerModel=this.tickerDAO.findByPrimaryKey(tickerListViewModel.getTickerId());
		if (tickerListViewModel.getAppUserId()!=null)
		{
			tickerModel.setAppUserId(tickerListViewModel.getAppUserId());	
			tickerModel.setTickerString(tickerListViewModel.getTickerString());
			
		}
		else  // default  Ticker string 
		{
			baseWrapperDefaultTicker=this.tickerDAO.loadDefaultTicker();
			TickerModel defTickerModel = (TickerModel)baseWrapperDefaultTicker.getBasePersistableModel();
			if (defTickerModel!=null)
			{
				tickerModel.setTickerString(tickerListViewModel.getTickerString());
				
			}
			tickerModel.setAppUserId(null);
		    tickerModel.setTickerString(tickerListViewModel.getTickerString());	
			
		}
		
		
		tickerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		tickerModel.setUpdatedOn(new Date());
		this.tickerDAO.saveOrUpdate(tickerModel);
		return baseWrapper;
	}
	
	

	


}
