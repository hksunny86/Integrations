package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AmaBvsConfigurationsModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.configurations.AmaBvsConfigurationsDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarkAmaAccountDebitBlockScheduler {

    private static final Logger LOGGER = Logger.getLogger(MarkAmaAccountDebitBlockScheduler.class);
    BaseWrapper bWrapper = new BaseWrapperImpl();
    private CommandManager commandManager;
    private AppUserModel appUserModel = new AppUserModel();
    private AppUserManager appUserManager;
    private Boolean isSmsRequired = false;
    private Boolean isDebitBlockRequired = false;
    private AmaBvsConfigurationsDAO amaBvsConfigurationsDAO;


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
        stopWatch.start("MarkAmaAccountDebitBlock Scheduler init");
        int ORACLE_LIMIT = 10000;
        LOGGER.info("*********** Executing MarkAmaAccountDebitBlock Scheduler ***********");
        List<AppUserModel> toBeRenewedList = null;
        String dormantAlertMessage = MessageUtil.getMessage("markAmaUserDebitBlock.beforeBvs");


        try {
            toBeRenewedList = appUserManager.loadAppUserMarkAmaAccountDebitBlock();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for MarkAmaAccontDebitBlock.");
                List<AppUserModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<AppUserModel> chunksList = new ArrayList<>();
                for (List<AppUserModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (AppUserModel model : toBeRenewedList) {
                    try {
                        List<AmaBvsConfigurationsModel> list = amaBvsConfigurationsDAO.loadAmaBvsConfigurationsModel();
                        Date accountCreatedOn = model.getCreatedOn();
                        Date currentDate = new Date();
                        appUserModel=model;
                        long difference = currentDate.getTime() - accountCreatedOn.getTime();
                        float daysBetween = (difference / (1000 * 60 * 60 * 24));
                        if (daysBetween <= list.get(0).getNoOfDays()) {
                            if (daysBetween >= list.get(0).getDaysOfIntimation() && daysBetween <list.get(0).getNoOfDays()) {
                                LOGGER.info("*********** Executing Sms Scheduler ***********");
                                bWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(model.getMobileNo(), dormantAlertMessage));
                                getCommonCommandManager().sendSMSToUser(bWrapper);
                                LOGGER.info("*********** Please visit nearest Agent to Perform bvs within  ***********" + (daysBetween - 70));
                            } else if (daysBetween == list.get(0).getNoOfDays()) {
                                LOGGER.info("*********** Executing Mark Customer Debit UnBlock***********");
                                SmartMoneyAccountModel smartMoneyAccountModel = null;
                                Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                                smartMoneyAccountModel = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(model, paymentModeId);
                                    smartMoneyAccountModel.setIsDebitBlocked(true);
                                    smartMoneyAccountModel.setDebitBlockAmount(list.get(0).getAmaDebitBlockAmount());
                                    smartMoneyAccountModel.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(smartMoneyAccountModel);
                                    this.getCommonCommandManager().updateSmartMoneyAccount(bWrapper);
                            }
                        } else {
                            LOGGER.info("*********** Finished Executing MarkAmaAccountDebitBlock Scheduler ***********");
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing MarkAmaAccontDebitBlock.init() :: " + ex.getMessage(), ex);
                    }
                }
            } else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for MarkAmaAccontDebitBlock.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing MarkAmaAccontDebitBlock.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing MarkAmaAccountDebitBlock Scheduler ***********");
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


}
