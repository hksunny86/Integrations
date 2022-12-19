package com.inov8.microbank.schedulers;

import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.PendingAccountOpeningManager;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingAccountOpeningDAO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class PendingAccountOpeningScheduler {

    private static final Logger LOGGER = Logger.getLogger(PendingAccountOpeningScheduler.class);

    private CommandManager commandManager;
    private PendingAccountOpeningManager pendingAccountOpeningManager;
    private AppUserModel appUserModel = new AppUserModel();
    private AppUserManager appUserManager;
    private PendingAccountOpeningDAO debitCardModelDAO;

    private List[] chunks(final List<AccountOpeningPendingSafRepoModel> pList, final int pSize) {
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
        stopWatch.start("PendingAccountOpening Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing PendingAccountOpening Scheduler ***********");
        List<AccountOpeningPendingSafRepoModel> toBeRenewedList = null;
        try {
            toBeRenewedList = pendingAccountOpeningManager.loadAllAccountOpeningPendingSafRepo();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for PendingAccountOpening.");
                List<DebitCardChargesSafRepoModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<DebitCardChargesSafRepoModel> chunksList = new ArrayList<>();
                for (List<DebitCardChargesSafRepoModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (AccountOpeningPendingSafRepoModel model : toBeRenewedList) {
                    try {

                        pendingAccountOpeningManager.makePendingAccountOpeningCommand(model);

                    } catch (Exception ex) {
                        LOGGER.error("Error while executing PendingAccountOpening.init() :: " + ex.getMessage(), ex);
                    }
                }
            }else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for PendingAccountOpening.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing PendingAccountOpening.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing PendingAccountOpening Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }

    public void setPendingAccountOpeningManager(PendingAccountOpeningManager pendingAccountOpeningManager) {
        this.pendingAccountOpeningManager = pendingAccountOpeningManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


}
