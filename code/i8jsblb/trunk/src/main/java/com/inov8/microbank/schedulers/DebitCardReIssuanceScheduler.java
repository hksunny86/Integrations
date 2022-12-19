package com.inov8.microbank.schedulers;
/**
 * Created by Muhammad Aqeel on 12/30/2021.
 */
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class DebitCardReIssuanceScheduler {
    private static final Logger LOGGER = Logger.getLogger(DebitCardReIssuanceScheduler.class);
    private CommandManager commandManager;
    private DebitCardManager debitCardManager;

    private List[] chunks(final List<DebitCardModel> pList, final int pSize)
    {
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};
        if(pSize < 0) return new List[] { pList };
        // Calculate the number of batches
        int numBatches = (pList.size() / pSize) + 1;

        // Create a new array of Lists to hold the return value
        List[] batches = new List[numBatches];

        for(int index = 0; index < numBatches; index++)
        {
            int count = index + 1;
            int fromIndex = Math.max(((count - 1) * pSize), 0);
            int toIndex = Math.min((count * pSize), pList.size());
            batches[index] = pList.subList(fromIndex, toIndex);
        }

        return batches;
    }

    public void init()
    {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("DebitCardReIssuance Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing DebitCardReIssuance Scheduler ***********");
        List<DebitCardModel> toBeReIssuancedList = null;
        try {
            toBeReIssuancedList = debitCardManager.loadAllCardsOnReIssuanceRequired();
            if(!toBeReIssuancedList.isEmpty())
            {
                LOGGER.info("Total " + toBeReIssuancedList.size() + " records fetched for DebitCardReIssuanceScheduler.");
                List<DebitCardModel>[] chunks = this.chunks(toBeReIssuancedList,ORACLE_LIMIT);
                List<DebitCardModel> chunksList = new ArrayList<>();
                for(List<DebitCardModel> limit : chunks)
                {
                    chunksList.addAll(limit);
                }
                for(DebitCardModel model : toBeReIssuancedList)
                {
                    try {
//                        debitCardManager.saveDebitCardImportExportReissuanceSchedulerRequest(model);
                    }
                    catch (Exception ex)
                    {
                        LOGGER.error("Error while executing debitCardManager.saveOrUpdateReIssuanceDebitCardModel() :: " + ex.getMessage(),ex);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("Error while executing DebitCardFeeDeductionScheduler.init() :: " + ex.getMessage(),ex);
        }
        LOGGER.info("*********** Finished Executing DebitCardFeeDeduction Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
    }
    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

}
