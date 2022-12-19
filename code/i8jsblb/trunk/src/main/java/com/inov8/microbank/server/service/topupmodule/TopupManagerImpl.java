package com.inov8.microbank.server.service.topupmodule;

import java.util.List;
import java.util.Map;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TopupSchedulerExecModel;
import com.inov8.microbank.server.dao.dailyjob.TopupSchedulerExecDAO;


public class TopupManagerImpl
    implements TopupManager
{
  private TopupSchedulerExecDAO topupSchedulerExecDAO;
  
  

  public SearchBaseWrapper loadTopupExecModelList(SearchBaseWrapper searchBaseWrapper) {
    CustomList<TopupSchedulerExecModel> list = this.topupSchedulerExecDAO.findByExample( (TopupSchedulerExecModel)searchBaseWrapper.getBasePersistableModel(), null,new String[]{});
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }
  
  public boolean saveTopupExecModelRequiresNewTransaction(TopupSchedulerExecModel topupSchedulerExecModel){
	  boolean saved = false;
	  TopupSchedulerExecModel model = topupSchedulerExecDAO.saveOrUpdate(topupSchedulerExecModel);
	  
	  if(model.getTopupSchedulerExecId() != null){
		  saved = true;
	  }
	  
	  return saved;
  }
  
  public boolean saveTopupExecModel(TopupSchedulerExecModel topupSchedulerExecModel){
	  boolean saved = false;
	  TopupSchedulerExecModel model = topupSchedulerExecDAO.saveOrUpdate(topupSchedulerExecModel);
	  
	  if(model.getTopupSchedulerExecId() != null){
		  saved = true;
	  }
	  
	  return saved;
  }
  
  
  /*public SearchBaseWrapper loadAllDisputedPostedTransactionEntries(List<String> responseCodeList) {
	  CustomList<TopupSchedulerExecModel> list = this.topupSchedulerExecDAO.findByExample( (TopupSchedulerExecModel)searchBaseWrapper.getBasePersistableModel(), null,new String[]{});
	  searchBaseWrapper.setCustomList(list);
	  return searchBaseWrapper;
  }*/

  public Map<Long, String> getAllDisputedEntries(List<Long> trxCodesList){
	  return topupSchedulerExecDAO.getAllDisputedEntries(trxCodesList);
  }

public TopupSchedulerExecDAO getTopupSchedulerExecDAO() {
	return topupSchedulerExecDAO;
}


public void setTopupSchedulerExecDAO(TopupSchedulerExecDAO topupSchedulerExecDAO) {
	this.topupSchedulerExecDAO = topupSchedulerExecDAO;
}

}
