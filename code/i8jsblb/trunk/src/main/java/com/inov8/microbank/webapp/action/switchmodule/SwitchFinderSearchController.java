package com.inov8.microbank.webapp.action.switchmodule;

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
import com.inov8.microbank.common.model.switchmodule.SwitchFinderListViewModel;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchFinderManager;


public class SwitchFinderSearchController extends BaseSearchController
{
	private SwitchFinderManager switchFinderManager;

	  public SwitchFinderSearchController()
	  {
	    super.setFilterSearchCommandClass(SwitchFinderListViewModel.class);
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
	    SwitchFinderListViewModel switchFinderListViewModel = (SwitchFinderListViewModel) model;
	    searchBaseWrapper.setBasePersistableModel(switchFinderListViewModel);
	    if(sortingOrderMap.isEmpty())
	    sortingOrderMap.put("switchName", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.switchFinderManager.searchSwitchFinder(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "switchFinderModelList",
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
	          baseWrapper = this.switchFinderManager.loadSwitchFinder(baseWrapper);
	         
	          // Set the active flag
	          serviceModel = (ServiceModel) baseWrapper.getBasePersistableModel();
	          serviceModel.setActive(activate);
	          //serviceModel.setVersionNo(versionNo);
	          
	          try
	          {	          
	        	  this.switchFinderManager.createOrUpdateSwitchFinder(baseWrapper);
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

	public void setSwitchFinderManager(SwitchFinderManager switchFinderManager) {
		this.switchFinderManager = switchFinderManager;
	}        

	}
