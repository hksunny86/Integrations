package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.ClsPendingAccountOpeningManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.ClsPendingAccountOpeningManagerImpl;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.List;

public class ClsPendingAccountOpeningApproveSmsAlertScheduler {

    private static final Logger LOGGER = Logger.getLogger(ClsPendingAccountOpeningApproveSmsAlertScheduler.class);

    private CommandManager commandManager;
    private ClsPendingAccountOpeningManager clsPendingAccountOpeningManager;
    private AppUserModel appUserModel = new AppUserModel();
    private ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();


    private AppUserManager appUserManager;    BaseWrapper bWrapper = new BaseWrapperImpl();
    private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;

    private List[] chunks(final List<ClsPendingAccountOpeningModel> pList, final int pSize) {
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
        stopWatch.start("ClsPendingAccountApprovedSmsAlter Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing ClsPendingAccountApprovedSmsAlter Scheduler ***********");
        String clsPendingAccountApproveSms = MessageUtil.getMessage("clspending.accountapprove.sms");

        List<ClsPendingAccountOpeningModel> toBeRenewedList = null;
        try {
            toBeRenewedList = clsPendingAccountOpeningManager.loadAllClsPendingAccountOpeningApprovedSmsAlert();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for ClsPendingAccountApprovedSmsAlter.");
                List<ClsPendingAccountOpeningModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<ClsPendingAccountOpeningModel> chunksList = new ArrayList<>();
                for (List<ClsPendingAccountOpeningModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (ClsPendingAccountOpeningModel model : toBeRenewedList) {
                    try {

                        LOGGER.info("*********** Executing Sms Scheduler ***********");
                        bWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(model.getMobileNo(), clsPendingAccountApproveSms));
                        getCommonCommandManager().sendSMSToUser(bWrapper);
                        model.setIsSmsRequired(false);
                        clsPendingAccountOpeningDAO.saveOrUpdate(model);
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing ClsPendingAccountApprovedSmsAlter.init() :: " + ex.getMessage(), ex);
                    }
                }
            } else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for ClsPendingAccountApprovedSmsAlter.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing ClsPendingAccountApprovedSmsAlter.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing ClsPendingAccountApprovedSmsAlter Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }

    public void setClsPendingAccountOpeningManager(ClsPendingAccountOpeningManager clsPendingAccountOpeningManager) {
        this.clsPendingAccountOpeningManager = clsPendingAccountOpeningManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

}
