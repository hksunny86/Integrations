package com.inov8.microbank.webapp.action.servicemodule;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.productmodule.ServiceListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.servicemodule.ServiceManager;

public class ServiceSearchController extends BaseSearchController
{
	private ServiceManager serviceManager;

	  public ServiceSearchController()
	  {
	    super.setFilterSearchCommandClass(ServiceListViewModel.class);
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
	    ServiceListViewModel serviceListViewModel = (ServiceListViewModel) model;
	    searchBaseWrapper.setBasePersistableModel(serviceListViewModel);
	    if(sortingOrderMap.isEmpty())
	    	sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.serviceManager.searchService(
	        searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "serviceModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
	  }

	  @Override
	    protected ModelAndView onToggleActivate(HttpServletRequest request,
	                                          HttpServletResponse response,Boolean activate) throws
	          Exception
	      {
	        Long id = ServletRequestUtils.getLongParameter(request, "serviceId");
	        Integer versionNo = ServletRequestUtils.getIntParameter(request, "versionNo");
	        Boolean active = ServletRequestUtils.getBooleanParameter(request, "_setActivate");
	        
	        if (null != id)
	        {
	          if (log.isDebugEnabled())
	          {
	            log.debug("id is not null....retrieving object from DB and then updating it");
	          }

	          BaseWrapper baseWrapper = new BaseWrapperImpl();	          
	          ServiceModel serviceModel = new ServiceModel() ;
	          
	          serviceModel.setServiceId(id);
	          serviceModel.setActive(active);
	          	          
	          baseWrapper.setBasePersistableModel(serviceModel);
	          baseWrapper = this.serviceManager.loadService(baseWrapper);
	         
	          // Set the active flag
	          serviceModel = (ServiceModel) baseWrapper.getBasePersistableModel();
	          serviceModel.setUpdatedOn(new Date());
	    	  serviceModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	          serviceModel.setActive(activate);
	          //serviceModel.setVersionNo(versionNo);
	          
	          try
	          {	          
	        	  this.serviceManager.createOrUpdateService(baseWrapper);
	          }
	          
	  	    catch (FrameworkCheckedException ex)
		    {
		      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
		          ex.getErrorCode())
		      {
		        super.saveMessage(request, "Record could not be saved.");		        
		      }
		    }
	        } 
	        ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() + ".html"));
	        return modelAndView;
	        
	       
	      }        


	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
	}
