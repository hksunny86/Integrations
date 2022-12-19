package com.inov8.verifly.common.wrapper;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;

public interface VeriflyBaseWrapper extends BaseWrapper
{
  public String getErrorCode();


  public String getErrorMessage();

  public boolean isErrorStatus();


  public void setErrorCode(String errorCode);


  public void setErrorStatus(boolean status);

  public void setErrorMessage(String errorMessage);
  public boolean isExceptionStatus();
  public void setExceptionStatus(boolean exceptionStatus);
  public void setAccountInfoModel(AccountInfoModel accountInfoModel);
  public AccountInfoModel getAccountInfoModel();
//  public void setGeneratedPin(String generatedPin);
//  public void setOldPin(String oldPin);
//  public void setNewPin(String newPin);
//  public String getGeneratedPin();
//  public String getNewPin();
//  public String getOldPin();
  public void setEncryptedPin(String encryptedPin);
  public String getEncryptedPin();
  public void setLogListViewModel(LogListViewModel logListViewModel);
  public void setLogModel(LogModel logModel);
  public LogModel getLogModel();
  public LogListViewModel getLogListViewModel();
  public Boolean getSkipPanCheck();
  public void setSkipPanCheck(Boolean skipPanCheck);
}
