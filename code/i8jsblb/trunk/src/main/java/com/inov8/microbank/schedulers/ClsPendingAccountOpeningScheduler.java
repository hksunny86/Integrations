package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.server.dao.configurations.AmaBvsConfigurationsDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsDebitCreditDAO;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;
import org.springframework.web.context.ContextLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ClsPendingAccountOpeningScheduler {

    private static final Logger LOGGER = Logger.getLogger(ClsPendingAccountOpeningScheduler.class);
    BaseWrapper bWrapper = new BaseWrapperImpl();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private CommandManager commandManager;
    private AppUserModel appUserModel = new AppUserModel();
    private AppUserManager appUserManager;
    private Boolean isSmsRequired = false;
    private Boolean isDebitBlockRequired = false;
    private AmaBvsConfigurationsDAO amaBvsConfigurationsDAO;
    private ClsDebitCreditDAO clsDebitCreditDAO;
    private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;
    private CustomerDAO customerDAO;
    private AppUserDAO appUserDAO;
    private ESBAdapter esbAdapter;
    private DebitCardModelDAO debitCardModelDAO;

    private List[] chunks(final List<AppUserModel> pList, final int pSize) {
        if (pList == null || pList.size() == 0 || pSize == 0) return new List[]{};
        if (pSize < 0) return new List[]{pList};
        // Calculate the number of batches
        int numBatches = (pList.size() / pSize) + 1;

        // Create a new array of Lists to hold the return value
        List[] batches = new List[numBatches];

        for (int index = 0; index < numBatches; index++) {
            int count = index + 1;
            int fromIndex = Math.max(((count - 1) * pSize), 0);
            int toIndex = Math.min((count * pSize), pList.size());
            batches[index] = pList.subList(fromIndex, toIndex);
        }

        return batches;
    }


    public void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("ClSPendingAccountOpening Scheduler init");
        int ORACLE_LIMIT = 10000;
        LOGGER.info("*********** Executing ClSPendingAccountOpening Scheduler ***********");
        List<AppUserModel> toBeRenewedList = null;
        String clsPendingAccountApproveSms = MessageUtil.getMessage("clspending.accountrejected.sms");

        try {
            toBeRenewedList = appUserManager.loadPendingAccountOpeningAppUserModel();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for ClSPendingAccountOpening.");
                List<AppUserModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<AppUserModel> chunksList = new ArrayList<>();
                for (List<AppUserModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (AppUserModel model : toBeRenewedList) {
                    try {
                        ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();
                        appUserModel = model;
                        clsPendingAccountOpeningModel.setMobileNo(model.getMobileNo());
                        clsPendingAccountOpeningModel.setAppUserId(model.getAppUserId());
                        clsPendingAccountOpeningModel = clsPendingAccountOpeningDAO.loadExistingPendingAccountOpening(clsPendingAccountOpeningModel);
                        if (clsPendingAccountOpeningModel == null) {

                            CustomerModel customerModel = new CustomerModel();
                            VerisysDataModel verisysDataModel;

                            customerModel = customerDAO.loadCustomerModelByCustomerId(model.getCustomerId());
                            if (customerModel.getSegmentId().equals(Long.parseLong(MessageUtil.getMessage("Minor_segment_id")))) {
                                clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();
                                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
                                Date dateOfBirth = dateFormat.parse(String.valueOf(model.getDob()));
                                String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
                                String stan = String.valueOf((new Random().nextInt(90000000)));
                                requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
                                requestVO.setName(customerModel.getFatherHusbandName());
                                requestVO.setCNIC(customerModel.getFatherCnicNo());
                                requestVO.setDateOfBirth(new SimpleDateFormat("yyyy").format(dateOfBirth));
                                requestVO.setNationality("Pakistan");
                                requestVO.setRequestId(transmissionDateTime + stan);
                                requestVO.setMobileNumber(customerModel.getFatherMotherMobileNo());
                                requestVO.setFatherName(customerModel.getFatherHusbandName());
                                new VerisysDataModel();
                                verisysDataModel = this.getCommonCommandManager().getVerisysDataHibernateDAO().loadVerisysDataModel(this.appUserModel.getAppUserId());
                                if (verisysDataModel != null) {
                                    requestVO.setCity(verisysDataModel.getCurrentAddress());
                                } else {
                                    requestVO.setCity(this.appUserModel.getAddress1());
                                }

                                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                                if (!responseVO.getResponseCode().equals("I8SB-200"))
                                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

                                if (responseVO.getCaseStatus().equalsIgnoreCase("ERROR-999") || responseVO.getCaseStatus().equalsIgnoreCase("ERROR-998")) {
                                    throw new CommandException("Account Opening Request Rejected Due To Compliance ", ErrorCodes.ACCOUNT_OPENING_FAILED, ErrorLevel.MEDIUM, null);
                                }
                                if (!(responseVO.getCaseStatus().equalsIgnoreCase("No Matches") || responseVO.getCaseStatus().
                                        equalsIgnoreCase("Passed By Rule") || responseVO.getCaseStatus().
                                        equalsIgnoreCase("False Positive Match") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("Private-False Positive") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-Passed by Rules") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("Private-Passed by Rules") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("No Match")
                                        || responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules"))) {
                                    model.setVerified(false);
                                    model.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
                                    model.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
                                    clsPendingAccountOpeningModel.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
                                    clsPendingAccountOpeningModel.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
                                    appUserDAO.saveOrUpdate(model);
//                                List<ClsDebitCreditBlockModel> list = clsDebitCreditDAO.loadClsDebitCreditModel();
//                                if (list != null && !list.isEmpty()) {
//                                    for (ClsDebitCreditBlockModel model1 : list) {
//                                        if (model1.getState().equals("DEBIT") && model1.getStatus().equals("1")) {
//                                            customerModel.setClsDebitBlock("1");
//                                        }
//                                        if (model1.getState().equals("CREDIT") && model1.getStatus().equals("1")) {
//                                            customerModel.setClsCreditBlock("1");
//                                        }
//                                    }
//                                    customerModel.setClsResponseCode(responseVO.getCaseStatus());
//                                    customerDAO.saveOrUpdate(customerModel);
//                                }
                                } else if (responseVO.getCaseStatus().equals("True Match") || responseVO.getCaseStatus().equals("True Match-Compliance")) {
                                    model.setVerified(false);
                                    model.setRegistrationStateId(RegistrationStateConstants.REJECTED);
                                    model.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_REJECTED);
                                    clsPendingAccountOpeningModel.setRegistrationStateId(RegistrationStateConstants.REJECTED);
                                    clsPendingAccountOpeningModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_REJECTED);
                                    appUserDAO.saveOrUpdate(model);
                                    bWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(model.getMobileNo(), clsPendingAccountApproveSms));
                                    getCommonCommandManager().sendSMSToUser(bWrapper);
                                    clsPendingAccountOpeningModel.setIsSmsRequired(false);
                                    clsPendingAccountOpeningDAO.saveOrUpdate(clsPendingAccountOpeningModel);

                                } else {
//                                DebitCardModel debitCardModel = debitCardModelDAO.getDebitCradModelByNicAndState(model.getNic(),2l);
//                                if (debitCardModel!=null){
//                                    debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_INTITATED);
//                                    debitCardModelDAO.saveOrUpdate(debitCardModel);
//                                }
                                    model.setVerified(true);
                                    model.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
                                    model.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
                                    clsPendingAccountOpeningModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                    clsPendingAccountOpeningModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                                    appUserDAO.saveOrUpdate(model);
                                    customerModel.setClsCreditBlock("0");
                                    customerModel.setClsDebitBlock("0");
                                    customerModel.setClsResponseCode(responseVO.getCaseStatus());
                                    customerDAO.saveOrUpdate(customerModel);

                                }

                                clsPendingAccountOpeningModel.setMobileNo(customerModel.getFatherMotherMobileNo());
                                clsPendingAccountOpeningModel.setCnic(customerModel.getFatherCnicNo());
                                clsPendingAccountOpeningModel.setConsumerName(customerModel.getName());
                                clsPendingAccountOpeningModel.setFatherHusbandName(customerModel.getFatherHusbandName());
                                clsPendingAccountOpeningModel.setMotherMaidenName(model.getMotherMaidenName());
                                clsPendingAccountOpeningModel.setCaseStatus(responseVO.getCaseStatus());
                                clsPendingAccountOpeningModel.setCaseID(responseVO.getCaseId());
                                clsPendingAccountOpeningModel.setGender(customerModel.getGender());
                                clsPendingAccountOpeningModel.setCnicIssuanceDate(model.getCnicIssuanceDate());
                                clsPendingAccountOpeningModel.setCustomerId(customerModel.getCustomerId());
                                clsPendingAccountOpeningModel.setAppUserId(model.getAppUserId());
                                clsPendingAccountOpeningModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                                clsPendingAccountOpeningModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                                clsPendingAccountOpeningModel.setCreatedOn(new Date());
                                clsPendingAccountOpeningModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                                clsPendingAccountOpeningModel.setUpdatedOn(new Date());
                                clsPendingAccountOpeningModel.setClsComments(null);
                                clsPendingAccountOpeningModel.setAccountOpenedByAma(customerModel.getAccountOpenedByAma());
                                if (clsPendingAccountOpeningModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) {
                                    clsPendingAccountOpeningModel.setClsBotStatus(1);
                                    clsPendingAccountOpeningModel.setIsCompleted("1");
                                }
                                clsPendingAccountOpeningModel = getCommonCommandManager().clsPendingAccountOpening(clsPendingAccountOpeningModel);
                            } else {
                                customerModel = customerDAO.loadCustomerModelByCustomerId(model.getCustomerId());
                                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
                                Date dateOfBirth = dateFormat.parse(String.valueOf(model.getDob()));
                                String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
                                String stan = String.valueOf((new Random().nextInt(90000000)));
                                requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
                                requestVO.setName(model.getFirstName() + model.getLastName());
                                requestVO.setCNIC(model.getNic());
                                requestVO.setDateOfBirth(new SimpleDateFormat("yyyy").format(dateOfBirth));
                                requestVO.setNationality("Pakistan");
                                requestVO.setRequestId(transmissionDateTime + stan);
                                requestVO.setMobileNumber(model.getMobileNo());
                                requestVO.setFatherName(customerModel.getFatherHusbandName());
                                new VerisysDataModel();
                                verisysDataModel = this.getCommonCommandManager().getVerisysDataHibernateDAO().loadVerisysDataModel(this.appUserModel.getAppUserId());
                                if (verisysDataModel != null) {
                                    requestVO.setCity(verisysDataModel.getCurrentAddress());
                                } else {
                                    requestVO.setCity(this.appUserModel.getAddress1());
                                }

                                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                                if (!responseVO.getResponseCode().equals("I8SB-200"))
                                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

                                if (responseVO.getCaseStatus().equalsIgnoreCase("ERROR-999") || responseVO.getCaseStatus().equalsIgnoreCase("ERROR-998")) {
                                    throw new CommandException("Account Opening Request Rejected Due To Compliance ", ErrorCodes.ACCOUNT_OPENING_FAILED, ErrorLevel.MEDIUM, null);
                                }
                                if (!(responseVO.getCaseStatus().equalsIgnoreCase("No Matches") || responseVO.getCaseStatus().
                                        equalsIgnoreCase("Passed By Rule") || responseVO.getCaseStatus().
                                        equalsIgnoreCase("False Positive Match") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("Private-False Positive") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-Passed by Rules") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("Private-Passed by Rules") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("No Match")
                                        || responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
                                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules"))) {
                                    model.setVerified(false);
                                    model.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
                                    model.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
                                    appUserDAO.saveOrUpdate(model);
//                                List<ClsDebitCreditBlockModel> list = clsDebitCreditDAO.loadClsDebitCreditModel();
//                                if (list != null && !list.isEmpty()) {
//                                    for (ClsDebitCreditBlockModel model1 : list) {
//                                        if (model1.getState().equals("DEBIT") && model1.getStatus().equals("1")) {
//                                            customerModel.setClsDebitBlock("1");
//                                        }
//                                        if (model1.getState().equals("CREDIT") && model1.getStatus().equals("1")) {
//                                            customerModel.setClsCreditBlock("1");
//                                        }
//                                    }
//                                    customerModel.setClsResponseCode(responseVO.getCaseStatus());
//                                    customerDAO.saveOrUpdate(customerModel);
//                                }
                                } else if (responseVO.getCaseStatus().equals("True Match") || responseVO.getCaseStatus().equals("True Match-Compliance")) {
                                    model.setVerified(false);
                                    model.setRegistrationStateId(RegistrationStateConstants.REJECTED);
                                    model.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_REJECTED);
                                    appUserDAO.saveOrUpdate(model);
                                    bWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(model.getMobileNo(), clsPendingAccountApproveSms));
                                    getCommonCommandManager().sendSMSToUser(bWrapper);
                                    clsPendingAccountOpeningModel.setIsSmsRequired(false);
                                    clsPendingAccountOpeningDAO.saveOrUpdate(clsPendingAccountOpeningModel);

                                } else {
//                                DebitCardModel debitCardModel = debitCardModelDAO.getDebitCradModelByNicAndState(model.getNic(),2l);
//                                if (debitCardModel!=null){
//                                    debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_INTITATED);
//                                    debitCardModelDAO.saveOrUpdate(debitCardModel);
//                                }
                                    model.setVerified(true);
                                    model.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                    model.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                                    appUserDAO.saveOrUpdate(model);
                                    customerModel.setClsCreditBlock("0");
                                    customerModel.setClsDebitBlock("0");
                                    customerModel.setClsResponseCode(responseVO.getCaseStatus());
                                    customerDAO.saveOrUpdate(customerModel);

                                }

                                clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();
                                clsPendingAccountOpeningModel.setMobileNo(model.getMobileNo());
                                clsPendingAccountOpeningModel.setCnic(model.getNic());
                                clsPendingAccountOpeningModel.setConsumerName(customerModel.getName());
                                clsPendingAccountOpeningModel.setFatherHusbandName(customerModel.getFatherHusbandName());
                                clsPendingAccountOpeningModel.setMotherMaidenName(model.getMotherMaidenName());
                                clsPendingAccountOpeningModel.setCaseStatus(responseVO.getCaseStatus());
                                clsPendingAccountOpeningModel.setCaseID(responseVO.getCaseId());
                                clsPendingAccountOpeningModel.setGender(customerModel.getGender());
                                clsPendingAccountOpeningModel.setCnicIssuanceDate(model.getCnicIssuanceDate());
                                clsPendingAccountOpeningModel.setCustomerId(customerModel.getCustomerId());
                                clsPendingAccountOpeningModel.setAppUserId(model.getAppUserId());
                                clsPendingAccountOpeningModel.setRegistrationStateId(model.getRegistrationStateId());
                                clsPendingAccountOpeningModel.setAccountStateId(model.getAccountStateId());
                                clsPendingAccountOpeningModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                                clsPendingAccountOpeningModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                                clsPendingAccountOpeningModel.setCreatedOn(new Date());
                                clsPendingAccountOpeningModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                                clsPendingAccountOpeningModel.setUpdatedOn(new Date());
                                clsPendingAccountOpeningModel.setClsComments(null);
                                clsPendingAccountOpeningModel.setAccountOpenedByAma(customerModel.getAccountOpenedByAma());
                                if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) {
                                    clsPendingAccountOpeningModel.setClsBotStatus(1);
                                    clsPendingAccountOpeningModel.setIsCompleted("1");
                                }
                                clsPendingAccountOpeningModel = getCommonCommandManager().clsPendingAccountOpening(clsPendingAccountOpeningModel);
                            }
                        } else {
                            LOGGER.info("*********** Your Record Already Pending At CLs Bucket ***********");

                        }
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing ClSPendingAccountOpening.init() :: " + ex.getMessage(), ex);
                    }
                }
            } else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for ClSPendingAccountOpening.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing ClSPendingAccountOpening.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing ClSPendingAccountOpening Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }


    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setAmaBvsConfigurationsDAO(AmaBvsConfigurationsDAO amaBvsConfigurationsDAO) {
        this.amaBvsConfigurationsDAO = amaBvsConfigurationsDAO;
    }

    public void setClsDebitCreditDAO(ClsDebitCreditDAO clsDebitCreditDAO) {
        this.clsDebitCreditDAO = clsDebitCreditDAO;
    }

    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setDebitCardModelDAO(DebitCardModelDAO debitCardModelDAO) {
        this.debitCardModelDAO = debitCardModelDAO;
    }
}
