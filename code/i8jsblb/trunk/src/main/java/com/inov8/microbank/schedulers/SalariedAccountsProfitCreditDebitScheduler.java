package com.inov8.microbank.schedulers;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SalariedAccountProfitModel;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.salariedaccountprofitcreditdebitmodule.SalariedAccountProfitcreditDebitManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class SalariedAccountsProfitCreditDebitScheduler {

    private static final Logger LOGGER = Logger.getLogger(AdvanceSalaryLoanScheduler.class);

    private CommandManager commandManager;
    private SalariedAccountProfitcreditDebitManager salariedAccountProfitcreditDebitManager;
    private AppUserModel appUserModel = new AppUserModel();

    private List[] chunks(final List<SalariedAccountProfitModel> pList, final int pSize) {
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


    public void init() {StopWatch stopWatch = new StopWatch();
        stopWatch.start("SalariedAccountProfitDebitCredit Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing SalariedAccountProfitDebitCredit Scheduler ***********");
        List<SalariedAccountProfitModel> toBeRenewedList = null;
        try {
            toBeRenewedList = salariedAccountProfitcreditDebitManager.loadAllSalariedAccountProfitData();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for SalariedAccountProfitDebitCredit.");
                List<SalariedAccountProfitModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<SalariedAccountProfitModel> chunksList = new ArrayList<>();
                for (List<SalariedAccountProfitModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (SalariedAccountProfitModel model : toBeRenewedList) {
                    try {

                        salariedAccountProfitcreditDebitManager.salariedAccountProfitDebitCreditDeductionCommand(model);


                    } catch (Exception ex) {
                        LOGGER.error("Error while executing SalariedAccountProfitDebitCredit.init() :: " + ex.getMessage(), ex);
                    }
                }
            }else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for SalariedAccountProfitDebitCredit.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing SalariedAccountProfitDebitCredit.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing SalariedAccountProfitDebitCredit Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }

    public void setSalariedAccountProfitcreditDebitManager(SalariedAccountProfitcreditDebitManager salariedAccountProfitcreditDebitManager) {
        this.salariedAccountProfitcreditDebitManager = salariedAccountProfitcreditDebitManager;
    }


    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

}
