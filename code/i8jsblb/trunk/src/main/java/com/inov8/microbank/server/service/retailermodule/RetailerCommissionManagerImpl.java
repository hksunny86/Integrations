package com.inov8.microbank.server.service.retailermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.retailermodule.RetailerCommissionListViewModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerCommissionListViewDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class RetailerCommissionManagerImpl
    implements RetailerCommissionManager
{
  private RetailerCommissionListViewDAO retailerCommissionListViewDAO;

  public SearchBaseWrapper searchRetailerCommission(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    CustomList<RetailerCommissionListViewModel>
        list = this.retailerCommissionListViewDAO.findByExample( (
            RetailerCommissionListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public void setRetailerCommissionListViewDAO(RetailerCommissionListViewDAO
                                               retailerCommissionListViewDAO)
  {
    this.retailerCommissionListViewDAO = retailerCommissionListViewDAO;
  }

}
