package com.inov8.verifly.common.wrapper;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;
public class VeriflyBaseWrapperImpl  extends BaseWrapperImpl implements VeriflyBaseWrapper
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 7472307891512574296L;
	private boolean errorStatus = false;
	private boolean exceptionStatus = false;
	private Boolean skipPanCheck = null;

	private String errorCode;
	private String errorMessage;
//  private String oldPin;
//  private String newPin;
//  private String generatedPin;
	private String encryptedPin;
	private AccountInfoModel accountInfoModel = new AccountInfoModel();
	private LogModel logModel = new LogModel();
	private LogListViewModel logListViewModel = new LogListViewModel();
	public String getErrorCode()
  {
    return errorCode;
  }

  public String getErrorMessage()
  {
    return errorMessage;
  }

  public boolean isErrorStatus() {
    return errorStatus;
  }

  public boolean isExceptionStatus() {
    return exceptionStatus;
  }

  public AccountInfoModel getAccountInfoModel() {
    return accountInfoModel;
  }

//  public String getNewPin() {
//    return newPin;
//  }

//  public String getOldPin() {
//    return oldPin;
//  }

//  public String getGeneratedPin() {
//    return generatedPin;
//  }

  public String getEncryptedPin() {
    return encryptedPin;
  }

  public LogModel getLogModel() {
    return logModel;
  }

  public LogListViewModel getLogListViewModel() {
    return logListViewModel;
  }

  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }

  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  public void setErrorStatus(boolean errorStatus) {
    this.errorStatus = errorStatus;
  }

  public void setExceptionStatus(boolean exceptionStatus) {
    this.exceptionStatus = exceptionStatus;
  }

  public void setAccountInfoModel(AccountInfoModel accountInfoModel) {
    this.accountInfoModel = accountInfoModel;
  }

//  public void setNewPin(String newPin) {
//    this.newPin = newPin;
//  }

//  public void setOldPin(String oldPin) {
//    this.oldPin = oldPin;
//  }

//  public void setGeneratedPin(String generatedPin) {
//    this.generatedPin = generatedPin;
//  }

  public void setEncryptedPin(String encryptedPin) {
    this.encryptedPin = encryptedPin;
  }

  public void setLogModel(LogModel logModel) {
    this.logModel = logModel;
  }

  public void setLogListViewModel(LogListViewModel logListViewModel) {
    this.logListViewModel = logListViewModel;
  }

	public Boolean getSkipPanCheck() {
		return skipPanCheck;
	}

	public void setSkipPanCheck(Boolean skipPanCheck) {
		this.skipPanCheck = skipPanCheck;
	}
}
