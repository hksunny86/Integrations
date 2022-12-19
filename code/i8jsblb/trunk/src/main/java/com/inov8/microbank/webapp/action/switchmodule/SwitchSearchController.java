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
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.model.switchmodule.SwitchListViewModel;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;

public class SwitchSearchController extends BaseSearchController
{
	private SwitchModuleManager switchManager;

	  public SwitchSearchController()
	  {
	    super.setFilterSearchCommandClass(SwitchListViewModel.class);
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
	    SwitchListViewModel switchListViewModel = (SwitchListViewModel) model;
	    searchBaseWrapper.setBasePersistableModel(switchListViewModel);
	    if(sortingOrderMap.isEmpty())
	    sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.switchManager.searchSwitch(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "switchModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
	  }

	  @Override
	    protected ModelAndView onToggleActivate(HttpServletRequest request,
	                                          HttpServletResponse response,Boolean activate) throws
	          Exception
	      {
	        Long id = ServletRequestUtils.getLongParameter(request, "switchId");	        
	        Boolean active = ServletRequestUtils.getBooleanParameter(request, "_setActivate");
	        
	        if (null != id)
	        {
	          if (log.isDebugEnabled())
	          {
	            log.debug("id is not null....retrieving object from DB and then updating it");
	          }

	          BaseWrapper baseWrapper = new BaseWrapperImpl();	          
	          SwitchModel switchModel = new SwitchModel() ;
	          switchModel.setSwitchId(id);
	                    	          
	          baseWrapper.setBasePersistableModel(switchModel);
	          baseWrapper = this.switchManager.loadSwitch(baseWrapper);
	         
	          // Set the active flag
	          switchModel = (SwitchModel) baseWrapper.getBasePersistableModel();
	          switchModel.setActive(activate);
	          	          
	          try
	          {	          
	        	  this.switchManager.createOrUpdateSwitch(baseWrapper);
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

	public void setSwitchManager(SwitchModuleManager switchManager) {
		this.switchManager = switchManager;
	}        

	}
