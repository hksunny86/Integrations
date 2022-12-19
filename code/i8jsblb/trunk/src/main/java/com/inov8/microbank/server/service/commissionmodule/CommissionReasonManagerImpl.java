package com.inov8.microbank.server.service.commissionmodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CommissionReasonModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionReasonListViewModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionReasonDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionReasonListViewDAO;

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
 *
 */

public class CommissionReasonManagerImpl
    implements CommissionReasonManager
{
  private CommissionReasonDAO commissionReasonDAO;
  private CommissionReasonListViewDAO commissionReasonListViewDAO;

  public CommissionReasonManagerImpl()
  {
  }

  public SearchBaseWrapper loadCommissionReason(SearchBaseWrapper
                                                searchBaseWrapper)
  {
    CommissionReasonModel commissionReasonModel = this.commissionReasonDAO.
        findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(commissionReasonModel);
    return searchBaseWrapper;
  }

  public SearchBaseWrapper searchCommissionReason(SearchBaseWrapper
                                                  searchBaseWrapper)
  {
    CustomList<CommissionReasonListViewModel>
        list = this.commissionReasonListViewDAO.findByExample( (
            CommissionReasonListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper updateCommissionReason(BaseWrapper baseWrapper)
  {
    CommissionReasonModel commissionReasonModel = (CommissionReasonModel)
        baseWrapper.getBasePersistableModel();
    CommissionReasonModel newCommissionReasonModel = new CommissionReasonModel();
    newCommissionReasonModel.setCommissionReasonId(commissionReasonModel.
        getCommissionReasonId());

    int recordCount = commissionReasonDAO.countByExample(
        newCommissionReasonModel);

    if (recordCount != 0 && commissionReasonModel.getPrimaryKey() != null)
    {
      commissionReasonModel = this.commissionReasonDAO.saveOrUpdate( (
          CommissionReasonModel) baseWrapper.getBasePersistableModel());
      baseWrapper.setBasePersistableModel(commissionReasonModel);
      return baseWrapper;
    }
    else
    {
      baseWrapper.setBasePersistableModel(null);
      return baseWrapper;
    }
  }

  public BaseWrapper createCommissionReason(BaseWrapper baseWrapper)
  {
    int recordCount;
    Date nowDate = new Date();
    CommissionReasonModel newCommissionReasonModel = new CommissionReasonModel();
    CommissionReasonModel commissionReasonModel = (CommissionReasonModel)
        baseWrapper.getBasePersistableModel();
    newCommissionReasonModel.setName(commissionReasonModel.getName());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    recordCount = commissionReasonDAO.countByExample(newCommissionReasonModel,exampleHolder);

    //***Check if Record already exists

     commissionReasonModel.setCreatedOn(nowDate);
    commissionReasonModel.setUpdatedOn(nowDate);

    if (recordCount == 0)
    {
      baseWrapper.setBasePersistableModel(this.commissionReasonDAO.saveOrUpdate(
          commissionReasonModel));
      return baseWrapper;
    }
    else
    {
      baseWrapper.setBasePersistableModel(null);
      return baseWrapper;
    }
  }

  public void setCommissionReasonListViewDAO(CommissionReasonListViewDAO
                                             commissionReasonListViewDAO)
  {
    this.commissionReasonListViewDAO = commissionReasonListViewDAO;
  }

  public void setCommissionReasonDAO(CommissionReasonDAO commissionReasonDAO)
  {
    this.commissionReasonDAO = commissionReasonDAO;
  }

}
