package com.inov8.verifly.common.wrapper.logmodule;

import java.util.ArrayList;
import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;

public class LogWrapperImpl extends BaseWrapperImpl implements LogWrapper
{
  private LogModel logModel;
  private LogListViewModel logListViewModel;
  private boolean exceptionStatus = false;
  private List<LogListViewModel> logListViewModelList = new ArrayList<LogListViewModel>();

  public LogModel getLogModel()
  {
    return logModel;
  }

  public void setLogModel(LogModel logModel)
  {
    this.logModel = logModel;
  }

  public void setLogListViewModel(LogListViewModel logListViewModel) {
    this.logListViewModel = logListViewModel;
  }

  public void setExceptionStatus(boolean exceptionStatus) {
    this.exceptionStatus = exceptionStatus;
  }

    public void setLogListViewModelList(List logListViewModelList) {
        this.logListViewModelList = logListViewModelList;
    }

    public LogListViewModel getLogListViewModel() {
    return logListViewModel;
  }

  public boolean isExceptionStatus() {
    return exceptionStatus;
  }

    public List getLogListViewModelList() {
        return logListViewModelList;
    }
}
