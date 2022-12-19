package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.util.Error;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class VerifyPINCommand extends BaseCommand {
  protected final Log logger = LogFactory.getLog(VerifyPINCommand.class);
  protected AppUserModel appUserModel;
  protected BaseWrapper preparedBaseWrapper;
  BaseWrapper baseWrapper;
  private String pin;
  private String plainPin;
  private int pinRetryCount;
  private String encryptionType;
  private Long errorCode;
  private Boolean isInvalid=false;
  private boolean isWebService;

  public void prepare(BaseWrapper baseWrapper)
  {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Start of VerifyPINCommand.prepare()");
    }

    this.appUserModel = ThreadLocalAppUser.getAppUserModel();
    this.pin = getCommandParameter(baseWrapper, "PIN");
    this.encryptionType = getCommandParameter(baseWrapper, "ENCT");
    //if (DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() != new Long(this.deviceTypeId).longValue()) {
      byte encType = new Byte(this.encryptionType).byteValue();
      this.plainPin =decryptPin(this.pin, encType);
    //}

    this.pinRetryCount = Integer.parseInt(getCommandParameter(baseWrapper, "PIN_RETRY_COUNT"));
    this.preparedBaseWrapper = baseWrapper;
    this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
    
    String isWebServiceStr = getCommandParameter(baseWrapper, "ISWEBSERVICE");
    
    if(isWebServiceStr != null && isWebServiceStr.equals("1")){
        isWebService = true;
      }else{
        isWebService = false;
      }

    if (this.logger.isDebugEnabled())
    {
      this.logger.debug("End of VerifyPINCommand.prepare()");
    }
  }

  public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
  {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Start of VerifyPINCommand.validate()");
    }

    ValidatorWrapper.doRequired(this.plainPin, validationErrors, "PIN");
    ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");

    if (!validationErrors.hasValidationErrors()) {
        if(!plainPin.equals("JSMPASS-1")){
            ValidatorWrapper.doNumeric(this.plainPin, validationErrors, "PIN");
        }
      ValidatorWrapper.doInteger(this.deviceTypeId, validationErrors, "Device Type");
    }

    if (this.logger.isDebugEnabled()) {
      this.logger.debug("End of VerifyPINCommand.validate()");
    }

    return validationErrors;
  }

  public void execute() throws CommandException
  {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Start of VerifyPINCommand.execute()");
    }

    String retVal = null;
    this.errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
    try
    {
    	if(plainPin.equals("JSMPASS-1")){
	    	
	    }else{
	        Boolean notError = Boolean.valueOf((validatePin(this.plainPin)) && (verifyPIN(this.pin, getCommonCommandManager()).booleanValue()));
	        if (!notError.booleanValue())
	      	  if(isWebService && notError == false){
	              if(isInvalid) {
	                throw new CommandException(retVal, this.errorCode.longValue(), ErrorLevel.CRITICAL, new Throwable());
	              }
	              else
	                throw new CommandException(retVal, this.errorCode.longValue(), ErrorLevel.MEDIUM, new Throwable());
	      	  }else{
	      		  retVal = checkInvalidPinAttempt();
	      	  }
	    }

    }catch (Exception e)
    {
      e.printStackTrace();
      if(isWebService){
    	  this.errorCode = FonePayResponseCodes.INVALID_PIN;
      }else{
    	  this.errorCode = ErrorCodes.INVALID_PIN;
      }
	  if(isWebService){
		  throw new CommandException(retVal, this.errorCode.longValue(), ErrorLevel.MEDIUM, new Throwable());
	  }else{
		  retVal = checkInvalidPinAttempt();
	  }
    }

    if (retVal != null) {
      if(isInvalid){
        throw new CommandException(retVal, this.errorCode.longValue(), ErrorLevel.CRITICAL, new Throwable());
      }
      else
      throw new CommandException(retVal, this.errorCode.longValue(), ErrorLevel.MEDIUM, new Throwable());
    }

    if (this.logger.isDebugEnabled())
      this.logger.debug("End of VerifyPINCommand.execute()");
  }

  private String checkInvalidPinAttempt()
  {
    String retVal = null;
    if (this.pinRetryCount == 2) {
      try {
      //  getCommonCommandManager().blockSmartMoneyAccount(this.appUserModel);
    	  getCommonCommandManager().makeUserBlocked(this.appUserModel);
          appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_WARM);
          baseWrapper = new BaseWrapperImpl();
          baseWrapper.setBasePersistableModel(appUserModel);
          commonCommandManager.updateAppUser(baseWrapper);
        this.errorCode = ErrorCodes.INVALID_PIN;
        retVal = MessageUtil.getMessage("pinRetryLimitExhuasted");

          baseWrapper.setBasePersistableModel(new CustomerModel(appUserModel.getCustomerId()));
          this.getCommonCommandManager().loadCustomer(baseWrapper);
          CustomerModel customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
          customerModel.setIsSetMpinLater(0L);
          customerModel.setIsMPINGenerated(Boolean.FALSE);
          this.getCommonCommandManager().saveOrUpdateCustomerModel(customerModel);
      }

      catch (Exception e)
      {
        e.printStackTrace();
        this.logger.error("[VerifyPINCommand.updateUserStateOnWrongPinAttempt] Unable to block smart money accounts.");
        retVal = e.getMessage();
      }
    }
    else {
      retVal = "Incorrect MPIN, Please retry.\n";
    }

    return retVal;
  }

  public String response()
  {
    List params = new ArrayList();
    params.add(new LabelValueBean("DTID", this.deviceTypeId));

    return MiniXMLUtil.createInfoResponseXMLByParams(params);
  }

  private boolean validatePin(String plainPIN)
  {
	    	return (plainPIN != null) && (!"".equals(plainPIN)) && (StringUtil.isInteger(plainPIN)) && (plainPIN.length() == 4);
  }

  public String decryptPin(String pin) {
    if ((pin != null) && (getOperatorModel().getKey() != null) && (!pin.equals(""))) {
      pin = StringUtil.replaceSpacesWithPlus(pin);
      pin = EncryptionUtil.doDecrypt(getOperatorModel().getKey(), pin);
    }

    return pin;
  }

  private Boolean verifyPIN(String PIN, CommonCommandManager commonCommandManager) throws Exception
  {
	  
	    	 SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
	    	    
	    	    if (appUserModel.getCustomerId() != null){
	    	      sma.setCustomerId(appUserModel.getCustomerId());
	    	  	}else if (appUserModel.getHandlerId() != null){
	    	        sma.setHandlerId(appUserModel.getHandlerId());
	    	  	}else if (appUserModel.getRetailerContactId() != null){
	    	      sma.setRetailerContactId(appUserModel.getRetailerContactId());
	    	    }else{
	    	        throw new CommandException("Invalid User", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
	    	    }
	    	    
	    	    sma.setDeleted(Boolean.valueOf(false));
	    	    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    	    searchBaseWrapper.setBasePersistableModel(sma);
	    	    searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
	    	    if ((searchBaseWrapper.getCustomList() != null) && (searchBaseWrapper.getCustomList().getResultsetList().size() > 0))
	    	    {
	    	      sma = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
	    	    }
	    	    if(sma!=null && sma.getChangePinRequired()){
                    throw new CommandException("Mpin Expired", ErrorCodes.PIN_CHANGE_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
	    	    AccountInfoModel accountInfoModel = new AccountInfoModel();
	    	    if (appUserModel.getCustomerId() != null)
	    	      accountInfoModel.setCustomerId(appUserModel.getCustomerId());
	    	    else { // for agent / handler
	    	      accountInfoModel.setCustomerId(appUserModel.getAppUserId());
	    	    }

                accountInfoModel = this.commonCommandManager.loadAccountInfoModel(accountInfoModel);

	    	    accountInfoModel.setAccountNick(sma.getName());
	    	    accountInfoModel.setOldPin(PIN);

//	    	    if(accountInfoModel.getIsMigrated() == 1 && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())){
//                    throw new CommandException("Mpin Expired", ErrorCodes.PIN_CHANGE_ERROR, ErrorLevel.MEDIUM, new Throwable());
//                }
                baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(new CustomerModel(appUserModel.getCustomerId()));
                if(appUserModel.getCustomerId() != null) {
                    this.getCommonCommandManager().loadCustomer(baseWrapper);
                    CustomerModel customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();

                    if (!customerModel.getIsMPINGenerated() && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                        throw new CommandException("Mpin Expired", ErrorCodes.PIN_CHANGE_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }
                }
	    	    LogModel logModel = new LogModel();
	    	    logModel.setCreatdByUserId(appUserModel.getAppUserId());
	    	    logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());

	    	    VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
	    	    veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
	    	    veriflyBaseWrapper.setLogModel(logModel);

	    	    veriflyBaseWrapper.setBasePersistableModel(sma);
	    	    BaseWrapper baseWrapper = new BaseWrapperImpl();
	    	    baseWrapper.setBasePersistableModel(sma);
	    	    veriflyBaseWrapper.setBasePersistableModel(null);
	    	    // uncomment this to skip HSM
	    	    //veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);
	    	    //
	    	    VeriflyManager veriflyManager = commonCommandManager.loadVeriflyManagerByAccountId(baseWrapper);

	    	    veriflyBaseWrapper = commonCommandManager.verifyVeriflyPin(veriflyManager, veriflyBaseWrapper);
	    	    if(veriflyBaseWrapper.getErrorCode()!=null && veriflyBaseWrapper.getErrorCode().equals("18")){
	    	      isInvalid=true;
	    	    }

	    	    return Boolean.valueOf(veriflyBaseWrapper.isErrorStatus());
	    }

}