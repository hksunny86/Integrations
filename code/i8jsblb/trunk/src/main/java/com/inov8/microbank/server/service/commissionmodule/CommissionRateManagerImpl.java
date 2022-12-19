package com.inov8.microbank.server.service.commissionmodule;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionRateListViewModel;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateListViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

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

public class CommissionRateManagerImpl
    implements CommissionRateManager
{
  private CommissionRateDAO commissionRateDAO;
  private CommissionRateListViewDAO commissionRateListViewDAO;
  private ActionLogManager actionLogManager;

  public CommissionRateManagerImpl()
  {
  }

  public SearchBaseWrapper loadCommissionRate(SearchBaseWrapper
                                              searchBaseWrapper)
  {
    CommissionRateModel commissionRateModel = this.commissionRateDAO.
        findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(commissionRateModel);
    return searchBaseWrapper;
  }

  public BaseWrapper loadCommissionRate(BaseWrapper baseWrapper)
  {
    CommissionRateModel commissionRateModel = this.commissionRateDAO.
        findByPrimaryKey(baseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    baseWrapper.setBasePersistableModel(commissionRateModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchCommissionRate(SearchBaseWrapper
                                                searchBaseWrapper)
  {
    CustomList<CommissionRateListViewModel>
        list = this.commissionRateListViewDAO.findByExample( (
            CommissionRateListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper updateCommissionRate(BaseWrapper baseWrapper)throws FrameworkCheckedException
  {
	  ActionLogModel actionLogModel = this.getActionLogManager().createActionLogRequiresNewTransaction(baseWrapper);
	  ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
	  
	CommissionRateModel commissionRateModel = (CommissionRateModel) baseWrapper.getBasePersistableModel();
    CommissionRateModel newCommissionRateModel = new CommissionRateModel();
    newCommissionRateModel.setCommissionRateId(commissionRateModel.getCommissionRateId());

    /*if( !commissionRateDAO.isCommissionRangeValid(commissionRateModel) ){   
    	baseWrapper.setBasePersistableModel(null);
    	return baseWrapper;
    }*/
    if (commissionRateModel.getActive()) {
    	commissionRateDAO.isCommissionRangeValid(commissionRateModel);
    }
    
    commissionRateModel = this.commissionRateDAO.saveOrUpdate( (CommissionRateModel) baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(commissionRateModel);
    actionLogModel.setCustomField1(commissionRateModel.getCommissionRateId().toString());
    this.getActionLogManager().completeActionLog(actionLogModel);
    return baseWrapper; 
  }

  public BaseWrapper createCommissionRate(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
	ActionLogModel actionLogModel = this.getActionLogManager().createActionLogRequiresNewTransaction(baseWrapper);
	ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
	  
	int insertFlag;
    Date nowDate = new Date();
    CommissionRateModel newCommissionRateModel = new CommissionRateModel();
    CommissionRateModel commissionRateModel = (CommissionRateModel) baseWrapper.
        getBasePersistableModel();

    newCommissionRateModel.setProductId(commissionRateModel.getProductId());
    newCommissionRateModel.setCommissionReasonId(commissionRateModel.
                                                 getCommissionReasonId());
    newCommissionRateModel.setCommissionStakeholderId(commissionRateModel.
            getCommissionStakeholderId());
    newCommissionRateModel.setPaymentModeId(commissionRateModel.
                                            getPaymentModeId());
    newCommissionRateModel.setFromDate(commissionRateModel.getFromDate());
    newCommissionRateModel.setToDate(commissionRateModel.getToDate());
    
    newCommissionRateModel.setCommissionTypeId(commissionRateModel.getCommissionTypeId());
    
    newCommissionRateModel.setRangeStarts(commissionRateModel.getRangeStarts());
    newCommissionRateModel.setRangeEnds(commissionRateModel.getRangeEnds());
    newCommissionRateModel.setSegmentId(commissionRateModel.getSegmentId()); 
    
    //newCommissionRateModel.setActive(true);

//    int recordCount = commissionRateDAO.countByExample(newCommissionRateModel);

    //***Check if Record already exists
    
    	commissionRateDAO.isCommissionRangeValid(commissionRateModel);
   
     insertFlag = Integer.parseInt(baseWrapper.getObject("insertFlag").toString());

    commissionRateModel.setCreatedOn(nowDate);
    commissionRateModel.setUpdatedOn(nowDate);

  /*  if (!recordCount)
    {
    	throw new FrameworkCheckedException("UniqueKeyViolated");
    }
   */ 
    CommissionRateModel cRateModel = this.commissionRateDAO.saveOrUpdate(commissionRateModel);
    baseWrapper.setBasePersistableModel(cRateModel);
    actionLogModel.setCustomField1(commissionRateModel.getCommissionRateId().toString());
    this.getActionLogManager().completeActionLog(actionLogModel);
    return baseWrapper; 
  }

  public void setCommissionRateListViewDAO(CommissionRateListViewDAO
                                           commissionRateListViewDAO)
  {
    this.commissionRateListViewDAO = commissionRateListViewDAO;
  }

  public void setCommissionRateDAO(CommissionRateDAO commissionRateDAO)
  {
    this.commissionRateDAO = commissionRateDAO;
  }

public ActionLogManager getActionLogManager() {
	return actionLogManager;
}

public void setActionLogManager(ActionLogManager actionLogManager) {
	this.actionLogManager = actionLogManager;
}
}
