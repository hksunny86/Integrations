package com.inov8.microbank.server.service.pendingaccountopeningmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.app.dao.AppInfoDAO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DebitCardPendingSafRepo;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.schedulers.PendingAccountOpeningScheduler;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingAccountOpeningDAO;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingDebitCardSafRepoDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;

import javax.xml.xpath.XPathExpressionException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class PendingAccountOpeningManagerImpl implements PendingAccountOpeningManager {
    private PendingAccountOpeningDAO pendingAccountOpeningDAO;
    private CommandManager commandManager;
    private PendingDebitCardSafRepoDAO pendingDebitCardSafRepoDAO;
    private AppUserDAO appUserDAO;
    private MessageSource messageSource;
    private AppInfoDAO appInfoDAO;

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PendingAccountOpeningManagerImpl.class);


    @Override
    public List<AccountOpeningPendingSafRepoModel> loadAllAccountOpeningPendingSafRepo() throws FrameworkCheckedException {
        return pendingAccountOpeningDAO.loadAllPendingAccount();
    }

    @Override
    public void makePendingAccountOpeningCommand(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel) throws FrameworkCheckedException {
        LOGGER.info("*********** Executing PendingAccountOpening Record ***********");
        AppUserModel appUserModel = new AppUserModel();
        LOGGER.info("*********** Load AppUserModel Of PendingAccountOpening Record ***********");
        appUserModel = getCommandManager().getCommonCommandManager().getAppUserWithRegistrationState(accountOpeningPendingSafRepoModel.getMobileNo(), accountOpeningPendingSafRepoModel.getCnic(), RegistrationStateConstantsInterface.VERIFIED);
        DebitCardPendingSafRepo debitCardPendingSafRepo = new DebitCardPendingSafRepo();
        ESBAdapter adapter = new ESBAdapter();
        LOGGER.info("*********** Prepare I8sb Request Of PendingAccountOpening Record ***********");
        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setTransmissionDateAndTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        requestVO.setSTAN(String.valueOf((100000 + new Random().nextInt(900000))));
        requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
        requestVO.setRequestId(requestVO.getRRN());
        requestVO.setCNIC(accountOpeningPendingSafRepoModel.getCnic());
        requestVO.setName(appUserModel.getFullName());
        requestVO.setDateOfBirth(String.valueOf(appUserModel.getDob()));
        requestVO.setNationality("");
        requestVO.setCity("");
        requestVO.setMobileNumber(appUserModel.getMobileNo());
        requestVO.setUserId("");
        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        sWrapper = adapter.makeI8SBCall(sWrapper);
        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
        LOGGER.info("*********** Response Received From I8sb Of PendingAccountOpening Record ***********");
        if (responseVO.getResponseCode().equals("I8SB-200")) {
            if (!(responseVO.getCaseStatus().equals("PEP/EDD-Open") || responseVO.getCaseStatus().equals("PEP/EDD-Open|Private-Open") ||
                    responseVO.getCaseStatus().equals("GWL-Open|PEP/EDD-Open|Private-Open") || responseVO.getCaseStatus().equals("Private-Open")
                    || responseVO.getCaseStatus().equals("GWL-Open"))) {
                if (responseVO.getCaseStatus().equals("Revert to Branch") || responseVO.getCaseStatus().equals("True Match-Compliance")) {
                    LOGGER.info("*********** Update AppUserModel and PendingAccountOpening SafRepo Of PendingAccountOpening Record ***********");
                    accountOpeningPendingSafRepoModel.setCompleted("1");
                    accountOpeningPendingSafRepoModel.setClsResponseCode(responseVO.getCaseStatus());
                    pendingAccountOpeningDAO.saveOrUpdate(accountOpeningPendingSafRepoModel);
                    debitCardPendingSafRepo.setMobileNo(accountOpeningPendingSafRepoModel.getMobileNo());
                    debitCardPendingSafRepo.setCnic(accountOpeningPendingSafRepoModel.getCnic());
                    debitCardPendingSafRepo.setProductId(ProductConstantsInterface.DEBIT_CARD_ISSUANCE);
                    debitCardPendingSafRepo.setIsCompleted("0");
                    LOGGER.info("*********** Load  DebitCard Pending SafRepo Record of Existing Pending AccountOpening ***********");
                    debitCardPendingSafRepo = pendingDebitCardSafRepoDAO.loadDebitCardSafRepoByMobileNoAndCnic(debitCardPendingSafRepo);
                    if (debitCardPendingSafRepo != null) {
                        debitCardPendingSafRepo.setIsCompleted("1");
                        debitCardPendingSafRepo.setDebitCardRegectionReason(responseVO.getCaseStatus());
                    } else {
                        LOGGER.info("***********  DebitCard Pending SafRepo Record Not Found Of Existing Pending AccountOpening  ***********");

                    }

                } else {
                    LOGGER.info("*********** Update AppUserModel and PendingAccountOpening SafRepo Of PendingAccountOpening Record ***********");
                    accountOpeningPendingSafRepoModel.setCompleted("1");
                    accountOpeningPendingSafRepoModel.setClsResponseCode(responseVO.getCaseStatus());
                    accountOpeningPendingSafRepoModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);
                    accountOpeningPendingSafRepoModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                    pendingAccountOpeningDAO.saveOrUpdate(accountOpeningPendingSafRepoModel);
                    appUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);
                    appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                    appUserDAO.saveOrUpdate(appUserModel);
                    if (accountOpeningPendingSafRepoModel.getAppId().equals(1L)) {
                        sendSMSToUsers(accountOpeningPendingSafRepoModel);
                    } else {
                        String randomPin = com.inov8.microbank.common.util.EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, appUserModel.getPassword());
                        boolean consumerApp = false;
                        consumerApp = true;
                        sendSMSToUsers(appUserModel.getUsername(), randomPin, consumerApp, appUserModel.getRegistrationStateId());
                    }
                    debitCardPendingSafRepo.setMobileNo(accountOpeningPendingSafRepoModel.getMobileNo());
                    debitCardPendingSafRepo.setCnic(accountOpeningPendingSafRepoModel.getCnic());
                    debitCardPendingSafRepo.setProductId(ProductConstantsInterface.DEBIT_CARD_ISSUANCE);
                    debitCardPendingSafRepo.setIsCompleted("0");
                    LOGGER.info("*********** Load  DebitCard Pending SafRepo Record of Existing Pending AccountOpening ***********");
                    debitCardPendingSafRepo = pendingDebitCardSafRepoDAO.loadDebitCardSafRepoByMobileNoAndCnic(debitCardPendingSafRepo);

                    if (debitCardPendingSafRepo != null) {
                        BaseWrapper baseWrapper = new BaseWrapperImpl();
                        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, debitCardPendingSafRepo.getMobileNo());
                        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, debitCardPendingSafRepo.getCnic());
                        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, debitCardPendingSafRepo.getDeviceTypeId());
                        baseWrapper.putObject("CARD_DESCRIPTION", debitCardPendingSafRepo.getCardDescription());
                        baseWrapper.putObject("MAILING_ADDRESS", debitCardPendingSafRepo.getEmail());
                        if (debitCardPendingSafRepo.getSegmentId() != null) {
                            baseWrapper.putObject(CommandFieldConstants.KEY_SEGMENT_ID, debitCardPendingSafRepo.getSegmentId());
                        }
                        if (debitCardPendingSafRepo.getAgentId() != null) {
                            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_ID, debitCardPendingSafRepo.getAgentId());
                        }
                        if (debitCardPendingSafRepo.getCardTypeId() != null) {
                            baseWrapper.putObject(CommandFieldConstants.KEY_CTYPE, debitCardPendingSafRepo.getCardTypeId());
                        }
                        if (accountOpeningPendingSafRepoModel.getAgentMobileNo() != null) {
                            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOB_NO, accountOpeningPendingSafRepoModel.getAgentMobileNo());
                        }
                        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, debitCardPendingSafRepo.getAppId());
                        try {
                            LOGGER.info("*********** Execute DebitCard Issuance Command***********");
                            String xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_CARD_ISSUANCE);
                            debitCardPendingSafRepo.setIsCompleted("1");
                            debitCardPendingSafRepo.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                            debitCardPendingSafRepo.setDebitCardRegectionReason(responseVO.getCaseStatus());
                            pendingDebitCardSafRepoDAO.saveOrUpdate(debitCardPendingSafRepo);

                        } catch (CommandException e) {
                            LOGGER.info("*********** Update DebitCard Saf Repo RegectionReason Command***********");
                            debitCardPendingSafRepo.setDebitCardRegectionReason(e.getMessage());
                            debitCardPendingSafRepo.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                            pendingDebitCardSafRepoDAO.saveOrUpdate(debitCardPendingSafRepo);

                        }

                    } else {
                        LOGGER.info("***********  DebitCard Pending SafRepo Record Not Found Of Existing Pending AccountOpening  ***********");

                    }

                }
            } else {
                LOGGER.info("*********** Update PendingAccountOpening SafRepo Record in Case of False CaseStatus Received From I8SB ***********");
                accountOpeningPendingSafRepoModel.setClsResponseCode(responseVO.getCaseStatus());
                pendingAccountOpeningDAO.saveOrUpdate(accountOpeningPendingSafRepoModel);

            }


        }


    }


    private void sendSMSToUsers(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel) {
        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            //Message to customer
            String customerSMS;
            if (accountOpeningPendingSafRepoModel.getInitialDeposit() == null || accountOpeningPendingSafRepoModel.getInitialDeposit().trim().equalsIgnoreCase("")) {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_con_clspending", new Object[]{accountOpeningPendingSafRepoModel.getMobileNo(), MessageUtil.getMessage("HELP_LINE_NUMBER")}, null);
            } else {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_con_sms_clspending2",
                        new Object[]{brandName, accountOpeningPendingSafRepoModel.getMobileNo(), Formatter.formatNumbers(Double.valueOf(accountOpeningPendingSafRepoModel.getInitialDeposit()))}, null);
            }

            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(accountOpeningPendingSafRepoModel.getMobileNo(), customerSMS));
            this.getCommandManager().getCommonCommandManager().sendSMSToUser(baseWrapper);

            //Message to agent
            String agentSMS;
            if (accountOpeningPendingSafRepoModel.getInitialDeposit() == null || accountOpeningPendingSafRepoModel.getInitialDeposit().trim().equalsIgnoreCase("")) {
                agentSMS = this.getMessageSource().getMessage("smsCommand.act_sms_clspendig_agent", new Object[]{accountOpeningPendingSafRepoModel.getMobileNo()}, null);
            } else {
                agentSMS = this.getMessageSource().getMessage("smsCommand.act_sms_clspending_agent2",
                        new Object[]{
                                accountOpeningPendingSafRepoModel.getMobileNo(),
                                Formatter.formatNumbers(Double.valueOf(accountOpeningPendingSafRepoModel.getInitialDeposit()))}, null);
            }

            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(accountOpeningPendingSafRepoModel.getAgentMobileNo(), agentSMS));
            this.getCommandManager().getCommonCommandManager().sendSMSToUser(baseWrapper);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }


    private void sendSMSToUsers(String userName, String pin, boolean isConsumerApp, long registrationStateId) {
        try {
            AppInfoModel appInfoModel = new AppInfoModel();
            appInfoModel.setAppId(AppConstants.CONSUMER_APP);
            List<AppInfoModel> appInfoModelList = new ArrayList<>();
            appInfoModelList = appInfoDAO.findByExample(appInfoModel).getResultsetList();
            ArrayList<String> urls = new ArrayList<>();
            if (appInfoModel != null) {
                for (AppInfoModel model : appInfoModelList
                        ) {
                    urls.add(model.getUrl());
                }
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            //Message to customer
            String customerSMS;
            if (isConsumerApp && registrationStateId != RegistrationStateConstants.CLSPENDING) {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app_cls_pending", new Object[]{userName, pin}, null);
            } else {
//					customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay",
//							new Object[] { brandName,urls.get(0),urls.get(1), pin}, null);
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", null, null);
            }

            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
            this.getCommandManager().getCommonCommandManager().sendSMSToUser(baseWrapper);


        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    public void setPendingAccountOpeningDAO(PendingAccountOpeningDAO pendingAccountOpeningDAO) {
        this.pendingAccountOpeningDAO = pendingAccountOpeningDAO;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public CommandManager getCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommandManager) applicationContext.getBean("cmdManager");
    }


    public void setPendingDebitCardSafRepoDAO(PendingDebitCardSafRepoDAO pendingDebitCardSafRepoDAO) {
        this.pendingDebitCardSafRepoDAO = pendingDebitCardSafRepoDAO;
    }

    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public AppInfoDAO getAppInfoDAO() {
        return appInfoDAO;
    }

    public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
        this.appInfoDAO = appInfoDAO;
    }
}
