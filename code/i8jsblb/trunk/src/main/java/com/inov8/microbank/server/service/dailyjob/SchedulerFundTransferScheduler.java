package com.inov8.microbank.server.service.dailyjob;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferDetailModel;
import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Invo8
 */

//@DisallowConcurrentExecution
public class SchedulerFundTransferScheduler extends QuartzJobBean {

    protected final Log logger = LogFactory.getLog(FundTransferScheduler.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private CommonCommandManager commonCommandManager;
    private CommandManager commandManager;
    List<ScheduleFundsTransferModel> sftList;
    List<ScheduleFundsTransferModel> ftList;
    List<ScheduleFundsTransferDetailModel> updateSftList;

    List<ScheduleFundsTransferDetailModel> scheduleFundsTransferDetailModels = new ArrayList<>();

    Map<String, String> failedTx;
    private ActionLogManager actionLogManager;
    ActionLogModel actionLogModel = null;
    String outputXML;
    protected MessageSource messageSource;
    //    private int failedTx=0;
    private int i = 0;

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

        try {
            int id = (++i);

            logger.info("\n************:-Started Fund Transfer Scheduler ************ " + id);

            long start = System.currentTimeMillis();
            loadActiveScheduledFundsTransferList();
            schedulerDetails();
            logger.info("\n:-Ended Fund Transfer Scheduler Took : " + ((System.currentTimeMillis() - start) / 1000) + " Second(s) " + id +
                    "\n***********************************************************************************************\n\n");
        } catch (CommandException ex) {
            logger.error("[FundTransferScheduler.executeInternal] " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } catch (FrameworkCheckedException e) {
            logger.error("[FundTransferScheduler.executeInternal] " + e.getMessage());
            logger.error(e.getMessage(), e);
        } catch (ParseException e) {
            logger.error("[FundTransferScheduler.executeInternal] " + e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private void loadActiveScheduledFundsTransferList() throws FrameworkCheckedException, ParseException {

        sftList = new ArrayList<>();
        ftList = new ArrayList<>();
        updateSftList = new ArrayList<>();
        failedTx = new HashMap<>();


        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
       /* ScheduleFundsTransferModel  scheduleFundsTransferModel = new ScheduleFundsTransferModel();
        scheduleFundsTransferModel.setIsExpired(Boolean.FALSE);
        searchBaseWrapper.setBasePersistableModel(scheduleFundsTransferModel);*/
//        scheduleFundsTransferDetailModels = this.commonCommandManager.fetchScheduleFundsTransferDetailList(new Date());


        // scheduleFundsTransferDetailModels = searchBaseWrapper.getCustomList().getResultsetList();
        if (CollectionUtils.isNotEmpty(scheduleFundsTransferDetailModels)) {
            processScheduledFT();
        }
    }

    private void processScheduledFT() throws FrameworkCheckedException, ParseException {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Date currentDateTime = date.getTime();

        //   for (ScheduleFundsTransferModel sft: scheduleFundsTransferDetailModels) {
/*
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dateFormat.parse(sft.getFromDate());
                if (!StringUtil.isNullOrEmpty(sft.getToDate())){
                    endDate = dateFormat.parse(sft.getToDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentDateTime.getTime()>=startDate.getTime() && !StringUtil.isNullOrEmpty(sft.getToDate())){
                if (!currentDateTime.after(endDate)) {
                    calculateDateAndNoOfOccurence(sft);
                }
                else {
                    sft.setIsExpired(Boolean.TRUE);
                    sft.setUpdatedOn(new Date());
                    updateSftList.add(sft);
                }
            }
            else {
                if (currentDateTime.getTime()>=startDate.getTime() && Integer.parseInt(sft.getNoOfOccurence())> sft.getOccurenceConsumed()){
                    calculateDateAndNoOfOccurence(sft);
                }
            }
        }*/
        if (!org.springframework.util.CollectionUtils.isEmpty(scheduleFundsTransferDetailModels)) {
            executeFundsTransfer();
        } else {
            logger.info("\n\n********************************************************************************************" +
                    "\n*             No transactions found for FT on Dated:" + new Date() + "                           *" +
                    "\n***********************************************************************************************\n\n\n");
        }
    }

    private void calculateDateAndNoOfOccurence(ScheduleFundsTransferModel sft) throws FrameworkCheckedException, ParseException {

        Date currentDateTime = new Date();
        Date lastTxDate = null;
        try {
            if (!StringUtil.isNullOrEmpty(sft.getLastTxDate())) {
                lastTxDate = dateFormat.parse(sft.getLastTxDate());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (sft.getRecurPatern().equals("Daily")) {
            ftList.add(sft);
        } else if (sft.getRecurPatern().equals("Weekly")) {
            if (!StringUtil.isNullOrEmpty(sft.getLastTxDate())) {
                long diff = currentDateTime.getTime() - lastTxDate.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if (days == 7) {
                    ftList.add(sft);
                }
            } else {
                ftList.add(sft);
            }
        } else if (sft.getRecurPatern().equals("Monthly")) {
            if (!StringUtil.isNullOrEmpty(sft.getLastTxDate())) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(sft.getLastTxDate()));
                cal.add(Calendar.MONTH, 1);

                if (dateFormat.format(currentDateTime).equals(dateFormat.format(cal.getTime()))) {
                    ftList.add(sft);
                }
            } else {
                ftList.add(sft);
            }
        } else if (sft.getRecurPatern().equals("Yearly")) {
            if (!StringUtil.isNullOrEmpty(sft.getLastTxDate())) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(sft.getLastTxDate()));
                cal.add(Calendar.YEAR, 1);

                if (dateFormat.format(currentDateTime).equals(dateFormat.format(cal.getTime()))) {
                    ftList.add(sft);
                }
            } else {
                ftList.add(sft);
            }
        }
    }

    private void executeFundsTransfer() throws FrameworkCheckedException {

        AppUserModel appUserModel = null;
        UserDeviceAccountsModel userDeviceAccountModel;
        logger.info("\n************:-Started Fund Transfer Command ************\n");
        String accountTitle = null;
        for (ScheduleFundsTransferDetailModel sft : scheduleFundsTransferDetailModels) {
            try {
                appUserModel = new AppUserModel();
                appUserModel = this.commonCommandManager.loadAppUserModelByCustomerId(sft.getCustomerId());
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                userDeviceAccountModel = commonCommandManager.
                        getUserDeviceAccountsManager().loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY, appUserModel.getAppUserId());
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountModel);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                accountTitle = EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, sft.getSenderAccountTitle());
                baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, sft.getTransactionAmount());
                baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, sft.getSenderAccountNo()));
                baseWrapper.putObject(CommandFieldConstants.KEY_SENDER_ACCOUNT_TITLE, accountTitle);
                baseWrapper.putObject(CommandFieldConstants.ATTR_ACCOUNT_TYPE_CODE, sft.getSenderAccountTypeCode());
                baseWrapper.putObject(CommandFieldConstants.KEY_SENDER_BANK_ID, BankConstantsInterface.BOP_BANK_ID);
                baseWrapper.putObject(CommandFieldConstants.KEY_CARD_NO, sft.getSenderCardNo());
                baseWrapper.putObject(CommandFieldConstants.KEY_CARD_EXPIRY, EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, sft.getSenderCardExpiry()));
                baseWrapper.putObject(CommandFieldConstants.KEY_RECIEVER_ACCOUNT_CORE, EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, sft.getBeneficiaryModel().getBeneficiaryAccountNo()));
                baseWrapper.putObject(CommandFieldConstants.KEY_RECIEVER_ACCOUNT_TITLE, EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, sft.getBeneficiaryModel().getBeneficiaryTitle()));
                baseWrapper.putObject(CommandFieldConstants.KEY_RECIEVER_BANK_IMD, sft.getBeneficiaryModel().getBeneficiaryBankCode());
                baseWrapper.putObject(CommandFieldConstants.KEY_BENEFICIARY_BANK_NAME, sft.getBeneficiaryModel().getBeneficiaryBankName());
                baseWrapper.putObject(CommandFieldConstants.KEY_BENE_NICK_NAME, sft.getBeneficiaryModel().getBeneficiaryAccountNick());
                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, sft.getProductId());
                baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALL_PAY);
                baseWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND, CommandFieldConstants.CMD_CORE_FT);

                String command = CommandFieldConstants.CMD_CORE_FT;
                actionLogsBeforeCall(sft, command, sft.getTransactionAmount());

                // BaseWrapper baseWrapper = new BaseWrapperImpl();
                outputXML = commandManager.executeCommand(baseWrapper);
                outputXML = null;

                actionLogAfterCall(outputXML);

                /*if (!StringUtil.isNullOrEmpty(sft.getNoOfOccurence())){
                    sft.setOccurenceConsumed(sft.getOccurenceConsumed()+1);
                    sft.setUpdatedOn(new Date());
                    if (Long.parseLong(sft.getNoOfOccurence()) == sft.getOccurenceConsumed()){
                        sft.setIsExpired(Boolean.TRUE);
                    }
                }*/
                sft.setIsExpired(Boolean.TRUE);
                sft.setStatus("executed");
                sft.setUpdatedOn(new Date());
                updateSftList.add(sft);
            } catch (CommandException ex) {


                try {
                    String mobileNo = "92" + appUserModel.getMobileNo().replaceAll("^0*", "");
                    String date = PortalDateUtils.formatDate(sft.getTransactionDate(), "dd/MM/yyyy");
                    String registrationSMS = this.getMessageSource().getMessage("ScheduleCoreToCoreSMS", new Object[]{sft.getTransactionAmount(), accountTitle, sft.getBeneficiaryModel().getBeneficiaryBankName(), date}, null);
                    logger.info("sending SMS for failed funds transfer mobile no " + appUserModel.getMobileNo() + registrationSMS);
                    getCommonCommandManager().sendM3SMS(mobileNo, registrationSMS);
                } catch (Exception e) {

                }

                sft.setStatus("retry");
                sft.setUpdatedOn(new Date());
                updateSftList.add(sft);
                failedTx.put("Schedule_FT_ID ", String.valueOf(sft.getScheduleFundsTransferDetailId()) +
                        "\n>>>Customer Id: " + String.valueOf(appUserModel.getCustomerId() +
                        "\n>>>Failure Reason: " + ex.getMessage()));
                actionLogAfterCall("<msg reqTime=\""
                        + new Date().getTime() + "\"> Failure Reason : " + ex.getMessage() + "</msg>");
            }
        }
        if (CollectionUtils.isNotEmpty(updateSftList)) {
            updateSftList();
            logger.info("\n************:-End Fund Transfer Command ************\n");
        }
    }

    private void updateSftList() throws FrameworkCheckedException {
        logger.info("\n\n\n************:-Going to update Schedule Funds Transfer Model(s) ************");
//        this.commonCommandManager.updateScheduleFundsTransferList(updateSftList);
    }

    private void schedulerDetails() {
        logger.info("\n\n********************************************************************************************" +
                "\n*                                   TX Detail                                                    *" +
                "\n***********************************************************************************************" +
                "\n>>> Total Transactions : " + ftList.size() +
                "\n>>> Failed Transactions : " + failedTx.size() + "\n\n");

        logger.info("\n***********************************************************************************************" +
                "\n*                                   Failed TX Detail                                     *" +
                "\n***********************************************************************************************");
        for (Map.Entry<String, String> entry : failedTx.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            logger.info("\n\n>>>" + key + " : " + value + "\n\n");
        }

    }


    private void actionLogsBeforeCall(ScheduleFundsTransferDetailModel sft, String command, String txAmount) {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + DeviceTypeConstantsInterface.ALL_PAY + "</param>"
                + "<param name=\"TXAM\">" + txAmount + "</param>"
                + "<param name=\"PID\">" + sft.getProductId() + "</param>"
                + "<param name=\"ACCNO\">" + sft.getSenderAccountNo() + "</param>"
                + "<param name=\"ACTYPECODE\">" + sft.getSenderAccountTypeCode() + "</param>"
                + "<param name=\"SENBAID\">" + BankConstantsInterface.BOP_BANK_ID + "</param>"
                + "<param name=\"KEY_CARD_NO\">" + sft.getSenderCardNo() + "</param>"
                + "<param name=\"RECACCOUNTNO\">" + sft.getBeneficiaryModel().getBeneficiaryAccountNo() + "</param>"
                + "<param name=\"RECACCTITLE\">" + sft.getBeneficiaryModel().getBeneficiaryTitle() + "</param>"
                + "<param name=\"BBNAME\">" + sft.getBeneficiaryModel().getBeneficiaryBankName() + "</param>"
                + "<param name=\"RECBBIMD\">" + sft.getBeneficiaryModel().getBeneficiaryBankCode() + "</param>"
                + "<param name=\"BENENICKNAME\">" + sft.getBeneficiaryModel().getBeneficiaryAccountNick() + "</param>"
                + "<param name=\"SENACCTITLE\">" + sft.getSenderAccountTitle() + "</param>"
                + "</params></msg> ";

        actionLogModel = new ActionLogModel();
        actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(requestString, XPathConstants.actionLogInputXMLLocationSteps));
        actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
        actionLogModel.setCommandId(Long.valueOf(command));
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        actionLogModel.setUserName(ThreadLocalAppUser.getAppUserModel().getUsername());
        actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
    }

    private void actionLogAfterCall(String outputXML) {
        actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(outputXML, XPathConstants.actionLogOutputXMLLocationSteps));
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
        actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
        if (ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel() != null) {
            actionLogModel.setDeviceUserId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
            actionLogModel.setUserDeviceAccountsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserDeviceAccountsId());
        }
        insertActionLogRequiresNewTransaction(actionLogModel);
    }

    private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        try {
            baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error("Exception occurred while processing", ex);

        }
        return actionLogModel;
    }

    public CommonCommandManager getCommonCommandManager() {
        return commonCommandManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public ActionLogManager getActionLogManager() {
        return actionLogManager;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}