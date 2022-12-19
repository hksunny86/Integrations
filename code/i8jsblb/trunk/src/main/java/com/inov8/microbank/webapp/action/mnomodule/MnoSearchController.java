package com.inov8.microbank.webapp.action.mnomodule;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.mnomodule.MnoListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.mnomodule.MnoManager;


/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class MnoSearchController extends BaseSearchController
{
  private MnoManager mnoManager;

  public MnoSearchController()
  {
    
    super.setFilterSearchCommandClass(MnoListViewModel.class);
  }

 
  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                        HttpServletResponse response,Boolean activate) throws
        Exception
    {
      Long id = ServletRequestUtils.getLongParameter(request,
                                                     "mnoId");
      Integer versionNo = ServletRequestUtils.getIntParameter(request,
                                                     "versionNo");
      if (null != id)
      {
        if (log.isDebugEnabled())
        {
          log.debug("id is not null....retrieving object from DB and then updating it");
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        MnoModel mnoModel = new MnoModel();
        mnoModel.setMnoId(id);
        mnoModel.setVersionNo(versionNo);
        baseWrapper.setBasePersistableModel(mnoModel);
        baseWrapper = this.mnoManager.loadMno(
            baseWrapper);
        //Set the active flag
        mnoModel = (MnoModel) baseWrapper.getBasePersistableModel();
        mnoModel.setActive(activate);
        mnoModel.setUpdatedOn(new Date());
        mnoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        
        try{
        	this.mnoManager.createOrUpdateMno(baseWrapper);
        }catch(FrameworkCheckedException fce){
        	this.saveMessage(request, "Record could not be updated.");
        	ModelAndView modelAndView = new ModelAndView(new RedirectView(
        	         getSearchView() + ".html"));
        	      
        	return modelAndView;
        }

      }
      ModelAndView modelAndView = new ModelAndView(new RedirectView(
         getSearchView() + ".html"));
      
     return modelAndView;

  }

  public void setMnoManager(MnoManager mnoManager)
  {
    this.mnoManager = mnoManager;
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    return null;
  }

@Override
protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
        Object object,
        HttpServletRequest httpServletRequest,
        LinkedHashMap<String, SortingOrder>
sortingOrderMap) throws Exception {
	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	   searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	   MnoListViewModel mnoListViewModel = (
	       MnoListViewModel)
	       object;
	   searchBaseWrapper.setBasePersistableModel(mnoListViewModel);
	   if(sortingOrderMap.isEmpty())
	       sortingOrderMap.put("name", SortingOrder.ASC);
	   searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	   searchBaseWrapper = this.mnoManager.searchMno(
	       searchBaseWrapper);

	   return new ModelAndView(super.getSearchView(), "mnoModelList",
	                           searchBaseWrapper.getCustomList().
	                           getResultsetList());
	
}



}
