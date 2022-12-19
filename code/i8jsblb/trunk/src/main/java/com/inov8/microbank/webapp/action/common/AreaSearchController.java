package com.inov8.microbank.webapp.action.common;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.areamodule.AreaListViewModel;
import com.inov8.microbank.server.service.common.AreaManager;



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
 **/

public class AreaSearchController extends BaseSearchController
{
  private AreaManager areaManager;

  public AreaSearchController()
  {
    super.setFilterSearchCommandClass(AreaListViewModel.class);
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
    AreaListViewModel areaModel = (AreaListViewModel) model;
    searchBaseWrapper.setBasePersistableModel(areaModel);
    if(sortingOrderMap.isEmpty())
        sortingOrderMap.put("name", SortingOrder.ASC);
    
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.areaManager.searchArea(
        searchBaseWrapper);

    return new ModelAndView(getSearchView(), "areaModelList",
                            searchBaseWrapper.getCustomList().getResultsetList());
  }

  @Override
    protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,Boolean activate) throws
          Exception
      {
        Long id = ServletRequestUtils.getLongParameter(request,
                                                       "areaId");
        Integer versionNo = ServletRequestUtils.getIntParameter(request,
                                                       "versionNo");
        if (null != id)
        {
          if (log.isDebugEnabled())
          {
            log.debug("id is not null....retrieving object from DB and then updating it");
          }

          BaseWrapper baseWrapper = new BaseWrapperImpl();
          AreaModel areaModel = new AreaModel();
          areaModel.setAreaId(id);
          areaModel.setVersionNo(versionNo);
          baseWrapper.setBasePersistableModel(areaModel);
          baseWrapper = this.areaManager.loadArea(
              baseWrapper);
          //***This code is implemented to prevent the specific Area to be deactivated if information is defined against it.
           boolean isFound = false;

          //Get the boolean to check that distributor contact is found against area
          isFound = this.areaManager.
              findDistributorContactByAreaId(baseWrapper);

          if (isFound && activate==false)
          {
            this.saveMessage(request, "The Area cannot be deactivated, because the information is defined against it.");
          }
          //update the status of Area
          else
          {
            // Set the active flag
            areaModel = (AreaModel) baseWrapper.getBasePersistableModel();
//            areaModel.setActive(activate);
            this.areaManager.createOrUpdateArea(baseWrapper);
          }
        }
        ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() + ".html"));
        return modelAndView;
  }

  public void setAreaManager(AreaManager areaManager)
  {
    this.areaManager = areaManager;
  }
}
