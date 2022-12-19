package com.inov8.microbank.schedulers;

import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.advancesalaryloanmodule.AdvanceSalaryLoanManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvanceSalaryLoanScheduler {

    private static final Logger LOGGER = Logger.getLogger(AdvanceSalaryLoanScheduler.class);

    private CommandManager commandManager;
    private AdvanceSalaryLoanManager advanceSalaryLoanManager;
    private AppUserModel appUserModel = new AppUserModel();
    private AppUserManager appUserManager;
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;

    private List[] chunks(final List<AdvanceSalaryLoanModel> pList, final int pSize) {
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
        stopWatch.start("AdvanceSalaryLoanDeduction Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing AdvanceSalaryLoanDeduction Scheduler ***********");
        List<AdvanceSalaryLoanModel> toBeRenewedList = null;
        try {
            toBeRenewedList = advanceSalaryLoanManager.loadAllAdvanceSalaryLoanData();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for AdvanceSalaryLoanDeduction.");
                List<AdvanceSalaryLoanModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<AdvanceSalaryLoanModel> chunksList = new ArrayList<>();
                for (List<AdvanceSalaryLoanModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (AdvanceSalaryLoanModel model : toBeRenewedList) {
                    try {

                        advanceSalaryLoanManager.advanceLoanDeductionCommand(model);


                    } catch (Exception ex) {
                        LOGGER.error("Error while executing AdvanceSalaryLoanDeduction.init() :: " + ex.getMessage(), ex);
                    }
                }
            }else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for AdvanceSalaryLoanDeduction.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing AdvanceSalaryLoanDeduction.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing AdvanceSalaryLoanDeduction Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }

    public void setAdvanceSalaryLoanManager(AdvanceSalaryLoanManager advanceSalaryLoanManager) {
        this.advanceSalaryLoanManager = advanceSalaryLoanManager;
    }

    public void setAdvanceSalaryLoanDAO(AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

}
