package com.inov8.microbank.webapp.action.retailermodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.retailermodule.RetailerTransactionListViewModel;
import com.inov8.microbank.server.service.retailermodule.RetailerTransactionManager;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened Application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 * @todo Display the Retailer Transaction according to currently logged in Retailer Head
 */

public class RetailerTransactionSearchController
    extends BaseFormSearchController
{
  private ReferenceDataManager referenceDataManager;
  private RetailerTransactionManager retailerTransactionManager;

  public RetailerTransactionSearchController()
  {
    super.setCommandName("retailerTransactionListViewModel");
    super.setCommandClass(RetailerTransactionListViewModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data for DeviceTypeModel
     */
    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
//    deviceTypeModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        deviceTypeModel, "name", SortingOrder.DESC);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex2)
    {
    }
    List<DeviceTypeModel> DeviceTypeModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      DeviceTypeModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("DeviceTypeModelList", DeviceTypeModelList);

    /**
     * code fragment to load reference data for TransactionTypeModel
     */

    PaymentModeModel paymentModeModel = new PaymentModeModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        paymentModeModel, "name", SortingOrder.DESC);
    referenceDataWrapper.setBasePersistableModel(paymentModeModel);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex)
    {
    }
    List<PaymentModeModel> PaymentModeModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      PaymentModeModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("PaymentModeModelList", PaymentModeModelList);

    return referenceDataMap;

  }

  @Override
  protected ModelAndView onSearch(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  PagingHelperModel pagingHelperModel,
                                  LinkedHashMap sortingOrderMap) throws
      Exception
  {
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
    RetailerTransactionListViewModel retailerTransactionListViewModel = (
        RetailerTransactionListViewModel) command;
    searchBaseWrapper.setBasePersistableModel(retailerTransactionListViewModel);
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.retailerTransactionManager.
        searchRetailerTransaction(
            searchBaseWrapper);

    return new ModelAndView(super.getSuccessView(),
                            "retailerTransactionModelList",
                            searchBaseWrapper.getCustomList().getResultsetList());

  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setRetailerTransactionManager(RetailerTransactionManager
                                            retailerTransactionManager)
  {
    this.retailerTransactionManager = retailerTransactionManager;
  }

}
