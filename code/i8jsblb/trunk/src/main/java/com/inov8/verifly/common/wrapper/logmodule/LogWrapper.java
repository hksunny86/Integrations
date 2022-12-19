package com.inov8.verifly.common.wrapper.logmodule;

import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;

public interface LogWrapper extends BaseWrapper
{
  public LogModel getLogModel();
  public void setLogModel(LogModel logModel);
  public LogListViewModel getLogListViewModel();
  public void setLogListViewModel(LogListViewModel logModel);
  public void setLogListViewModelList(List logListViewModelList);
  public List getLogListViewModelList();
  public void setExceptionStatus(boolean exceptionStatus);
  public boolean isExceptionStatus();
}
