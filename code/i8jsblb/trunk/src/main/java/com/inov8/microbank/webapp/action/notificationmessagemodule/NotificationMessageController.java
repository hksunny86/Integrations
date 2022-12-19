package com.inov8.microbank.webapp.action.notificationmessagemodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.notificationmessagemodule.NotificationMessageListViewModel;
import com.inov8.microbank.server.service.notificationmessagemodule.NotificationMessageManager;


public class NotificationMessageController extends BaseSearchController  
{
	private NotificationMessageManager notificationMessageManager;
	
	public NotificationMessageController()
	  {
	    super.setFilterSearchCommandClass(NotificationMessageListViewModel.class);
	    
	  }
	
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
		
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		NotificationMessageListViewModel notificationMessageListViewModel=(NotificationMessageListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(notificationMessageListViewModel);
		if(linkedHashMap.isEmpty())
		    linkedHashMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		searchBaseWrapper=this.notificationMessageManager.viewNotificationMessages(searchBaseWrapper);
		 	
		return new ModelAndView(getSearchView(), "notificationmessageList", searchBaseWrapper.getCustomList().getResultsetList());
		
		
	}
	
	
	
	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}

	

	
	
	

}
