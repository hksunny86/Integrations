package com.inov8.microbank.webapp.action.devicetypemodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.devicetypemodule.DeviceTypeListViewModel;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;

public class DeviceTypeSearchController extends BaseSearchController
{	
	private DeviceTypeManager deviceTypeManager;

	  public DeviceTypeSearchController()
	  {
	    super.setFilterSearchCommandClass(DeviceTypeListViewModel.class);
	  }

	  @Override
	  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
	                                  Object model,
	                                  HttpServletRequest httpServletRequest,
	                                  LinkedHashMap<String, SortingOrder>
	      sortingOrderMap) throws Exception
	  {
	    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);	    
	    DeviceTypeListViewModel deviceTypeListViewModel = (DeviceTypeListViewModel) model;	    
	    searchBaseWrapper.setBasePersistableModel(deviceTypeListViewModel);
	    if(sortingOrderMap.isEmpty())
	    sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.deviceTypeManager.searchDeviceType(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "deviceTypeModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
	  }

	  @Override
	    protected ModelAndView onToggleActivate(HttpServletRequest request,
	                                          HttpServletResponse response,Boolean activate) throws
	          Exception
	      {
	        ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() + ".html"));
	        return modelAndView;	        
	       
	      }        


	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}
	}
