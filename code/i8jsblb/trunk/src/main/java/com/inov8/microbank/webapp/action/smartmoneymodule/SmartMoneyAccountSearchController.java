package com.inov8.microbank.webapp.action.smartmoneymodule;

import java.util.LinkedHashMap;
import java.util.Map;

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
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.smartmoneymodule.SmartMoneyAccountListViewModel;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */



public class SmartMoneyAccountSearchController
    extends BaseSearchController
{
  private SmartMoneyAccountManager smartMoneyAccountManager;

  public SmartMoneyAccountSearchController()
  {
    super.setFilterSearchCommandClass(SmartMoneyAccountListViewModel.class);
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws Exception
  {
    Long id = ServletRequestUtils.getLongParameter(request,
        "smartMoneyAccountId");
    Integer versionNo = ServletRequestUtils.getIntParameter(request,
        "versionNo");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "id is not null....retrieving object from DB and then updating it");
      }

      BaseWrapper baseWrapper = new BaseWrapperImpl();
      SmartMoneyAccountModel smartMoneyAccountModel = new
          SmartMoneyAccountModel();
      smartMoneyAccountModel.setSmartMoneyAccountId(id);
      smartMoneyAccountModel.setVersionNo(versionNo);
      baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
      baseWrapper = this.smartMoneyAccountManager.loadSmartMoneyAccount(
          baseWrapper);
      //Set the active flag
      smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.
          getBasePersistableModel();
      smartMoneyAccountModel.setActive(activate);
      this.smartMoneyAccountManager.updateSmartMoneyAccount(baseWrapper);
    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(
        "smartmoneyaccountmanagement.html"));
    return modelAndView;

  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    return null;
  }

  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
                                  Object model, HttpServletRequest request,
                                  LinkedHashMap sortingOrderMap) throws
      Exception
  {
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
    SmartMoneyAccountListViewModel smartmoneyAccountListViewModel = (
        SmartMoneyAccountListViewModel) model;
    searchBaseWrapper.setBasePersistableModel(smartmoneyAccountListViewModel);
    if(sortingOrderMap.isEmpty())
        sortingOrderMap.put("smartMoneyAccountName", SortingOrder.ASC); 

    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.smartMoneyAccountManager.searchSmartMoneyAccount(
        searchBaseWrapper);
    return new ModelAndView(getSearchView(), "smartMoneyAccountList",
                            searchBaseWrapper.getCustomList().
                            getResultsetList());

  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

}
