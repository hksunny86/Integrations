package com.inov8.microbank.server.webservice;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.UssdMenuModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.vo.ussd.AppUserModelVO;
import com.inov8.microbank.common.vo.ussd.UserDeviceAccountsModelVO;
import com.inov8.microbank.common.vo.ussd.UserState;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.webservice.bean.USSDInputDTO;
import com.inov8.microbank.server.webservice.bean.USSDOutputDTO;
import com.inov8.microbank.ussd.UssdRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.Date;


public class USSDRequestHandlerDelegate {

    protected final Log logger = LogFactory.getLog(getClass());
    private UssdRequestHandler ussdHandler;
    private ActionLogManager actionLogManager;
    Long appUserTypeID;

    public Object handlerUSSDRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerRequestVO.setI8SBSwitchControllerResponseVO(i8SBSwitchControllerResponseVO);
        boolean isFirstCall = false;

        boolean restoreSession = true;
        boolean isValidated = false;
        String errCode = null;
        String validatedStatus = "";
        // validating the mandatory Param

        if (i8SBSwitchControllerRequestVO != null) {
            String keyMSISDN = null;
            UserState userState = null;

            if (i8SBSwitchControllerRequestVO.getuSSDResponseCode() != null && i8SBSwitchControllerRequestVO.getuSSDResponseCode().equals("false")) {

                logger.info("[USSDRequestHandlerDelegate] First Call from Agent");
                // this is for the first call
                keyMSISDN = i8SBSwitchControllerRequestVO.getMobileNumber();
                normalizeMSISDN(i8SBSwitchControllerRequestVO);
                logger.info("[USSDRequestHandlerDelegate] Deleting Existing Agent User State.");
                ussdHandler.getUssdUserStateManager().deleteUserState(i8SBSwitchControllerRequestVO.getMobileNumber());
                isFirstCall = true;

                if (!normalizUSSDRequestString(i8SBSwitchControllerRequestVO)) {
                    i8SBSwitchControllerResponseVO.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
                    i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(new Date().toString());
                    i8SBSwitchControllerResponseVO.setuSSDResponseString("Invalid Short Code.");
                    i8SBSwitchControllerResponseVO.setuSSDAction("end");
                    i8SBSwitchControllerResponseVO.setResponseCode("0");
                    i8SBSwitchControllerResponseVO.setCodingScheme("UTF-8");

                    return i8SBSwitchControllerResponseVO;
                }

            } else {

                try {
                    normalizeMSISDN(i8SBSwitchControllerRequestVO);
                    logger.info("[USSDRequestHandlerDelegate] Going to find existing User State");
                    userState = ussdHandler.getUssdUserStateManager().findUserState(i8SBSwitchControllerRequestVO.getMobileNumber());
                    isFirstCall = false;
                } catch (Exception e) {
                    logger.error(e);//e.printStackTrace();
                }
            }
            this.initialize(userState);

            logger.info(" [USSDRequestHandlerDelegate] Validating the User");
            i8SBSwitchControllerResponseVO = ussdHandler.validateUser(i8SBSwitchControllerRequestVO, i8SBSwitchControllerResponseVO, appUserTypeID);
            validatedStatus = i8SBSwitchControllerResponseVO.getuSSDResponseString();
            // If user is successfully validated...
            if (i8SBSwitchControllerResponseVO.getError() != null && i8SBSwitchControllerResponseVO.getError().equals("false")) {
                logger.info("[USSDRequestHandlerDelegate] User is Successfully Validated...");
                userState = initializeUserStateVO(i8SBSwitchControllerRequestVO, userState, appUserTypeID);

                if (!isFirstCall) {
                    if (userState.getValidInputs() != null) {//general input validation
                        errCode = ussdHandler.validateGeneralInput(i8SBSwitchControllerRequestVO, userState);
                        isValidated = errCode == null;
                    }
                    if (!isValidated && userState.getCutomInput() != null) {//custom input validation

                        errCode = ussdHandler.validateCustomInput(i8SBSwitchControllerRequestVO, userState, actionLogManager, i8SBSwitchControllerResponseVO);
                        isValidated = errCode == null;
                    }
                } else {
                    isValidated = true;
                }

            } else {
                // User is not validated:
                logger.info("[USSDRequestHandlerDelegate] User Could not be validated");
                i8SBSwitchControllerResponseVO.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
                i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(new Date().toString());
                i8SBSwitchControllerResponseVO.setuSSDResponseString(validatedStatus);
                i8SBSwitchControllerResponseVO.setuSSDAction("end");
                i8SBSwitchControllerResponseVO.setResponseCode("0");
                i8SBSwitchControllerResponseVO.setCodingScheme("UTF-8");

                return i8SBSwitchControllerResponseVO;
            }

            if (isValidated) {
                logger.info("[USSDRequestHandlerDelegate] Going to find next menu");
                BaseWrapper searchBaseWrapper = ussdHandler.findNextMenu(i8SBSwitchControllerRequestVO, userState, appUserTypeID, isFirstCall);
                if (searchBaseWrapper != null && searchBaseWrapper.getBasePersistableModel() != null) {
                    UssdMenuModel menuModel = ((UssdMenuModel) searchBaseWrapper.getBasePersistableModel());
                    updateUserStateVO(userState, menuModel);
                    logger.info("[USSDRequestHandlerDelegate] Next menu to be displayed is :" + menuModel.getMenuString());
                    boolean inputRequired = menuModel.getInputRequired().equals("1");
                    if (inputRequired == true)
                        i8SBSwitchControllerResponseVO.setuSSDAction("request");
                    else
                        i8SBSwitchControllerResponseVO.setuSSDAction("end");

                    if ("1".equals(userState.getExecutionRequired())) {
                        logger.info("[USSDRequestHandlerDelegate] USSD Command Execution");
                        String messageStr = ussdHandler.executeCommand(userState, menuModel, i8SBSwitchControllerResponseVO);
                        if (!messageStr.equals("") && messageStr != null) {
                            messageStr = messageStr.trim();
                            ussdHandler.replacePlaceHolders(userState, menuModel);
                            if (i8SBSwitchControllerResponseVO.getError().equals("true")) {
                                i8SBSwitchControllerResponseVO.setuSSDResponseString(messageStr);
                                i8SBSwitchControllerResponseVO.setuSSDAction("end");
                            } else {
                                i8SBSwitchControllerResponseVO.setuSSDResponseString(menuModel.getMenuString());
                            }
                        } else
                            i8SBSwitchControllerResponseVO.setuSSDResponseString(menuModel.getMenuString());

                        if (ussdHandler.getMessageSource().getMessage(
                                "MINI.MultipleTrans", null, null).equals(messageStr)) {
                            searchBaseWrapper = ussdHandler.findMenu(71L);
                            if (searchBaseWrapper != null && searchBaseWrapper.getBasePersistableModel() != null) {
                                menuModel = ((UssdMenuModel) searchBaseWrapper.getBasePersistableModel());
                                updateUserStateVO(userState, menuModel);
                                inputRequired = menuModel.getInputRequired().equals("1");

                                if (inputRequired == true)
                                    i8SBSwitchControllerResponseVO.setuSSDAction("request");
                                else
                                    i8SBSwitchControllerResponseVO.setuSSDAction("end");
                                i8SBSwitchControllerResponseVO.setuSSDResponseString(menuModel.getMenuString());
                                messageStr = menuModel.getMenuString();
                            }
                        } else {
                            i8SBSwitchControllerRequestVO.setuSSDRequestString(messageStr);
                            i8SBSwitchControllerResponseVO.setResponseCode("0");
                            i8SBSwitchControllerResponseVO.setCodingScheme("UTF-8");
                        }

                    } else
                        i8SBSwitchControllerResponseVO.setuSSDResponseString(menuModel.getMenuString());
                } else {
                    i8SBSwitchControllerResponseVO.setuSSDResponseString("Selected Menu does not exist.Please try again latter");
                    i8SBSwitchControllerResponseVO.setResponseCode("0");
                    i8SBSwitchControllerResponseVO.setCodingScheme("UTF-8");
                }

                restoreUserState(userState);
                return i8SBSwitchControllerResponseVO;

            } else {
                // if custom input is not validated:
                i8SBSwitchControllerResponseVO.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
                i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(new Date().toString());
                i8SBSwitchControllerResponseVO.setuSSDResponseString(errCode);
                i8SBSwitchControllerResponseVO.setuSSDAction("end");
                i8SBSwitchControllerResponseVO.setResponseCode("0");
                i8SBSwitchControllerResponseVO.setCodingScheme("UTF-8");
            }
        }


        return i8SBSwitchControllerResponseVO;
    }


    private void updateUserStateVO(UserState userState, UssdMenuModel menuModel) {
        boolean inputRequired = menuModel.getInputRequired().equals("1");
//		retVal.setInputRequired(inputRequired);
        ussdHandler.replacePlaceHolders(userState, menuModel);
        userState.setInputRequired(inputRequired);
        userState.setPreviousScreenCode(userState.getScreenCode());
        userState.setScreenCode(menuModel.getScreenCode());
        userState.setCommandCode(menuModel.getCommandCode());
        userState.setExecutionRequired(menuModel.getExecutionRequired());
        userState.setValidInputs(menuModel.getValidInputs());
        if (menuModel.getCustomInputTypeIdCustomInputTypeModel() != null) {
            userState.setCutomInput(menuModel.getCustomInputTypeIdCustomInputTypeModel().getKeyword());
        } else {
            userState.setCutomInput(null);//clear previous custom input identifier
        }
    }

    //
//
    private UserState initializeUserStateVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState
            userState, Long appUserTypeID) throws Exception {

        if (userState == null) {
            userState = new UserState();
            userState.setUserMsisdn(i8SBSwitchControllerRequestVO.getMobileNumber());
            userState.setScreenCode(0);
            userState.setAppUserTypeID(appUserTypeID);
            userState.setCreationDate(Calendar.getInstance().getTime());
            this.ussdHandler.populateUserStateWithBankInfo(userState);

        }
        return userState;
    }

    private void initialize(UserState userState) {
        if (userState != null) {
            AppUserModel appUserModel = getAppUserModel(userState.getAppUserModel());
            UserDeviceAccountsModel userDeviceAccountsModel = getUserDeviceAccountsModel(userState.getUserDeviceAccountsModel());
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                if (userDeviceAccountsModel != null) {
                    ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);
                }
                userState.setAppUserTypeID(appUserTypeID);
            }

        }
    }

    private void restoreUserState(UserState userState) throws FrameworkCheckedException {
        if (userState != null) {
            AppUserModel userModel = ThreadLocalAppUser.getAppUserModel();
            UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

//			if (userModel != null) {
//				userState.setAppUserModel(getAppUserModelVO(userModel));
//				if (userDeviceAccountsModel != null) {
//					userState.setUserDeviceAccountsModel(getUserDeviceAccountsModelVO(userDeviceAccountsModel));
//				}
//			}
            //userStateService.save(userState);
            ussdHandler.getUssdUserStateManager().saveUserState(userState);
        }
        // we are done now remove the appUserModel from ThreadLocal
        ThreadLocalAppUser.remove();
        ThreadLocalActionLog.remove();
        ThreadLocalUserDeviceAccounts.remove();
    }

    private AppUserModel getAppUserModel(AppUserModelVO param) {
        AppUserModel retVal = null;
        if (param != null) {
            retVal = new AppUserModel();

            retVal.setAppUserId(param.getAppUserId());
            retVal.setAppUserTypeId(param.getAppUserTypeId());
            retVal.setSupplierUserId(param.getSupplierUserId());
            retVal.setRetailerContactId(param.getRetailerContactId());
            retVal.setOperatorUserId(param.getOperatorUserId());
            retVal.setMobileTypeId(param.getMobileTypeId());
            retVal.setMnoUserId(param.getMnoUserId());
            retVal.setDistributorContactId(param.getDistributorContactId());
            retVal.setCustomerId(param.getCustomerId());
            retVal.setBankUserId(param.getBankUserId());

            retVal.setAppUserTypeId(param.getAppUserTypeId());
            retVal.setFirstName(param.getFirstName());
            retVal.setLastName(param.getLastName());
            retVal.setAddress1(param.getAddress1());
            retVal.setAddress2(param.getAddress2());
            retVal.setCity(param.getCity());
            retVal.setState(param.getState());
            retVal.setCountry(param.getCountry());
            retVal.setZip(param.getZip());
            retVal.setNic(param.getNic());
            retVal.setEmail(param.getEmail());
            retVal.setFax(param.getFax());
            retVal.setMotherMaidenName(param.getMotherMaidenName());
            retVal.setUsername(param.getUsername());
            retVal.setPassword(param.getPassword());
            retVal.setMobileNo(param.getMobileNo());
            retVal.setPasswordHint(param.getPasswordHint());
            retVal.setVerified(param.getVerified());
            retVal.setAccountEnabled(param.getAccountEnabled());
            retVal.setAccountExpired(param.getAccountExpired());
            retVal.setAccountLocked(param.getAccountLocked());
            retVal.setCredentialsExpired(param.getCredentialsExpired());
            retVal.setPasswordChangeRequired(param.getPasswordChangeRequired());
            retVal.setCreatedOn(param.getCreatedOn());
            retVal.setUpdatedOn(param.getUpdatedOn());
            retVal.setVersionNo(param.getVersionNo());
            retVal.setDob(param.getDob());
            retVal.setLastLoginAttemptTime(param.getLastLoginAttemptTime());
            retVal.setLoginAttemptCount(param.getLoginAttemptCount());
            retVal.setNicExpiryDate(param.getNicExpiryDate());
            retVal.setAccountClosedSettled(param.getAccountClosedSettled());
            retVal.setAccountClosedUnsettled(param.getAccountClosedUnsettled());

				/*RetailerContactModel retailerContactModel=new RetailerContactModel();
				retailerContactModel.setRso(param.isRso());
				retVal.setRetailerContactIdRetailerContactModel(retailerContactModel);*/
            if (null != retVal.getRetailerContactIdRetailerContactModel())
                retVal.getRetailerContactIdRetailerContactModel().setRso(param.isRso());
        }
        return retVal;
    }

    private AppUserModelVO getAppUserModelVO(AppUserModel param) {
        AppUserModelVO retVal = null;
        if (param != null) {
            retVal = new AppUserModelVO();

            retVal.setAppUserId(param.getAppUserId());
            retVal.setAppUserTypeId(param.getAppUserTypeId());
            retVal.setSupplierUserId(param.getSupplierUserId());
            retVal.setRetailerContactId(param.getRetailerContactId());
            retVal.setOperatorUserId(param.getOperatorUserId());
            retVal.setMobileTypeId(param.getMobileTypeId());
            retVal.setMnoUserId(param.getMnoUserId());
            retVal.setDistributorContactId(param.getDistributorContactId());
            retVal.setCustomerId(param.getCustomerId());
            retVal.setBankUserId(param.getBankUserId());

            retVal.setFirstName(param.getFirstName());
            retVal.setLastName(param.getLastName());
            retVal.setAddress1(param.getAddress1());
            retVal.setAddress2(param.getAddress2());
            retVal.setCity(param.getCity());
            retVal.setState(param.getState());
            retVal.setCountry(param.getCountry());
            retVal.setZip(param.getZip());
            retVal.setNic(param.getNic());
            retVal.setEmail(param.getEmail());
            retVal.setFax(param.getFax());
            retVal.setMotherMaidenName(param.getMotherMaidenName());
            retVal.setUsername(param.getUsername());
            retVal.setPassword(param.getPassword());
            retVal.setMobileNo(param.getMobileNo());
            retVal.setPasswordHint(param.getPasswordHint());
            retVal.setVerified(param.getVerified());
            retVal.setAccountEnabled(param.getAccountEnabled());
            retVal.setAccountExpired(param.getAccountExpired());
            retVal.setAccountLocked(param.getAccountLocked());
            retVal.setCredentialsExpired(param.getCredentialsExpired());
            retVal.setPasswordChangeRequired(param.getPasswordChangeRequired());
            retVal.setCreatedOn(param.getCreatedOn());
            retVal.setUpdatedOn(param.getUpdatedOn());
            retVal.setVersionNo(param.getVersionNo());
            retVal.setDob(param.getDob());
            retVal.setLastLoginAttemptTime(param.getLastLoginAttemptTime());
            retVal.setLoginAttemptCount(param.getLoginAttemptCount());
            retVal.setNicExpiryDate(param.getNicExpiryDate());

            retVal.setAccountClosedSettled(param.getAccountClosedSettled());
            retVal.setAccountClosedUnsettled(param.getAccountClosedUnsettled());
            if (null != param.getRetailerContactIdRetailerContactModel())
                retVal.setRso(param.getRetailerContactIdRetailerContactModel().getRso());
        }
        return retVal;

    }

    private UserDeviceAccountsModel getUserDeviceAccountsModel(UserDeviceAccountsModelVO param) {
        UserDeviceAccountsModel retVal = null;
        if (param != null) {
            retVal = new UserDeviceAccountsModel();
            retVal.setUserDeviceAccountsId(param.getUserDeviceAccountsId());

            retVal.setDeviceTypeId(param.getDeviceTypeId());
            retVal.setAppUserId(param.getAppUserId());

            retVal.setUserId(param.getUserId());
            retVal.setPin(param.getPin());
            retVal.setAccountExpired(param.getAccountExpired());
            retVal.setAccountEnabled(param.getAccountEnabled());
            retVal.setAccountLocked(param.getAccountLocked());
            retVal.setCredentialsExpired(param.getCredentialsExpired());
            retVal.setCreatedOn(param.getCreatedOn());
            retVal.setUpdatedOn(param.getUpdatedOn());
            retVal.setVersionNo(param.getVersionNo());
            retVal.setPinChangeRequired(param.getPinChangeRequired());
            retVal.setLastLoginAttemptTime(param.getLastLoginAttemptTime());
            retVal.setLoginAttemptCount(param.getLoginAttemptCount());
            retVal.setExpiryDate(param.getExpiryDate());
            retVal.setCommissioned(param.getCommissioned());
            retVal.setPassword(param.getPassword());
            retVal.setPasswordChangeRequired(param.getPasswordChangeRequired());
        }
        return retVal;
    }

    private UserDeviceAccountsModelVO getUserDeviceAccountsModelVO(UserDeviceAccountsModel param) {
        UserDeviceAccountsModelVO retVal = null;
        if (param != null) {
            retVal = new UserDeviceAccountsModelVO();
            retVal.setUserDeviceAccountsId(param.getUserDeviceAccountsId());

            retVal.setDeviceTypeId(param.getDeviceTypeId());
            retVal.setAppUserId(param.getAppUserId());

            retVal.setUserId(param.getUserId());
            retVal.setPin(param.getPin());
            retVal.setAccountExpired(param.getAccountExpired());
            retVal.setAccountEnabled(param.getAccountEnabled());
            retVal.setAccountLocked(param.getAccountLocked());
            retVal.setCredentialsExpired(param.getCredentialsExpired());
            retVal.setCreatedOn(param.getCreatedOn());
            retVal.setUpdatedOn(param.getUpdatedOn());
            retVal.setVersionNo(param.getVersionNo());
            retVal.setPinChangeRequired(param.getPinChangeRequired());
            retVal.setLastLoginAttemptTime(param.getLastLoginAttemptTime());
            retVal.setLoginAttemptCount(param.getLoginAttemptCount());
            retVal.setExpiryDate(param.getExpiryDate());
            retVal.setCommissioned(param.getCommissioned());
            retVal.setPassword(param.getPassword());
            retVal.setPasswordChangeRequired(param.getPasswordChangeRequired());
        }
        return retVal;
    }

    private void normalizeMSISDN(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        if (i8SBSwitchControllerRequestVO.getMobileNumber() != null && i8SBSwitchControllerRequestVO.getMobileNumber().contains("92") && !(i8SBSwitchControllerRequestVO.getMobileNumber().indexOf("92") > 1)) {
            i8SBSwitchControllerRequestVO.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber().replaceFirst("92", "0"));
        }
    }

    private boolean normalizUSSDRequestString(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        String agentShortCode = MessageUtil.getMessage("ussd.agent.code");
        String customerShortCode = MessageUtil.getMessage("ussd.customer.code");

        if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(agentShortCode)) {
            i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
            appUserTypeID = 3L;
            return true;

        } else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(customerShortCode)) {
            i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
            appUserTypeID = 2L;
            return true;
        } else
            return false;

    }

    public UssdRequestHandler getUssdHandler() {
        return ussdHandler;
    }

    public void setUssdHandler(UssdRequestHandler ussdHandler) {
        this.ussdHandler = ussdHandler;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }
}