package com.inov8.microbank.webapp.action.veriflymodule;

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
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.common.model.veriflymodule.VeriflyFormListViewModel;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyModuleManager;

public class VeriflySearchController extends BaseSearchController
{
	private VeriflyModuleManager veriflyManager;

	  public VeriflySearchController()
	  {
	    super.setFilterSearchCommandClass(VeriflyFormListViewModel.class);
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
	    VeriflyFormListViewModel veriflyFormListViewModel = (VeriflyFormListViewModel) model;
	    searchBaseWrapper.setBasePersistableModel(veriflyFormListViewModel);
	    if(sortingOrderMap.isEmpty())
	    sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.veriflyManager.searchVerifly(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "veriflyModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
	  }

	  @Override
	    protected ModelAndView onToggleActivate(HttpServletRequest request,
	                                          HttpServletResponse response,Boolean activate) throws
	          Exception
	      {
	        Long id = ServletRequestUtils.getLongParameter(request, "veriflyId");	        
	        Boolean active = ServletRequestUtils.getBooleanParameter(request, "_setActivate");
	        
	        if (null != id)
	        {
	          if (log.isDebugEnabled())
	          {
	            log.debug("id is not null....retrieving object from DB and then updating it");
	          }

	          BaseWrapper baseWrapper = new BaseWrapperImpl();	          
	          VeriflyModel veriflyModel = new VeriflyModel() ;
	          veriflyModel.setVeriflyId(id);
	                    	          
	          baseWrapper.setBasePersistableModel(veriflyModel);
	          baseWrapper = this.veriflyManager.loadVerifly(baseWrapper);
	         
	          // Set the active flag
	          veriflyModel = (VeriflyModel) baseWrapper.getBasePersistableModel();
	          veriflyModel.setActive(activate);
	          	          
	          try
	          {	          
	        	  this.veriflyManager.createOrUpdateVerifly(baseWrapper);
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

	public void setVeriflyManager(VeriflyModuleManager veriflyManager)
	{
		this.veriflyManager = veriflyManager;
	}


	}
