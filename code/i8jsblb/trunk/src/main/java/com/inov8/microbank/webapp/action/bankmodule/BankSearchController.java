package com.inov8.microbank.webapp.action.bankmodule;

import java.util.Date;
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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.bankmodule.BankListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.bankmodule.BankManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */



public class BankSearchController
    extends BaseSearchController
{

  private BankManager bankManager;

  public BankSearchController()
  {
    super.setFilterSearchCommandClass(BankListViewModel.class);
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws Exception
  {

    Long id = ServletRequestUtils.getLongParameter(request,
        "bankId");
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
      BankModel bankModel = new BankModel();
      bankModel.setBankId(id);
      bankModel.setVersionNo(versionNo);
      baseWrapper.setBasePersistableModel(bankModel);
      baseWrapper = this.bankManager.loadBank(
          baseWrapper);
      //Set the active flag
      
      bankModel = (BankModel) baseWrapper.getBasePersistableModel();
      bankModel.setUpdatedOn(new Date());
      bankModel.setUpdatedByAppUserModel(UserUtils
				.getCurrentUser());
      bankModel.setActive(activate);
      this.bankManager.updateBank(baseWrapper);

    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(
        "bankmanagement.html"));
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
    BankListViewModel bankListViewModel = (BankListViewModel) model;
    searchBaseWrapper.setBasePersistableModel(bankListViewModel);
    if(sortingOrderMap.isEmpty())
    {
    sortingOrderMap.put("bankName", SortingOrder.ASC);
    }
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.bankManager.searchBank(searchBaseWrapper);

    return new ModelAndView(getSearchView(), "bankList",
                            searchBaseWrapper.getCustomList().
                            getResultsetList());

  }

  public void setBankManager(BankManager bankManager)
  {
    this.bankManager = bankManager;
  }

}
