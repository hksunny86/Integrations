package com.inov8.microbank.schedulers;

import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebitCardAnnualFeeDeductionScheduler {
    private static final Logger LOGGER = Logger.getLogger(DebitCardAnnualFeeDeductionScheduler.class);

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
        stopWatch.start("DebitCardFeeDeduction Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing DebitCardAnnualFeeDeduction Scheduler ***********");
        List<DebitCardModel> toBeRenewedList = null;
        try {
            toBeRenewedList = debitCardManager.loadAllCardsOnRenewRequiredForAnnualFee();
            if(!toBeRenewedList.isEmpty())
            {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for DebitCardAnnualFeeDeductionScheduler.");
                List<DebitCardModel>[] chunks = this.chunks(toBeRenewedList,ORACLE_LIMIT);
                List<DebitCardModel> chunksList = new ArrayList<>();
                for(List<DebitCardModel> limit : chunks)
                {
                    chunksList.addAll(limit);
                }
                for(DebitCardModel model : toBeRenewedList)
                {
                    try{
                        debitCardManager.makeFeeDeductionCommand(null,model,
                                ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE, CardConstantsInterface.CARD_FEE_TYPE_ANNUAL, DeviceTypeConstantsInterface.MOBILE);
                        model.setUpdatedOn(new Date());
                        model.setFeeDeductionDate(new Date());
                        model.setFeeDeductionDateAnnual(new Date());
                        if(model.getNoOfInstallments() != null) {
                            debitCardManager.saveOrUpdateDebitCardModelForAnnualFee(model);
                        }
                        else{
                            debitCardManager.saveOrUpdateDebitCardModel(model);
                        }
                    }
                    catch (Exception ex)
                    {
                        LOGGER.error("Error while executing debitCardManager.executeFeeDeductionCommand() :: " + ex.getMessage(),ex);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("Error while executing DebitCardAnnualFeeDeductionScheduler.init() :: " + ex.getMessage(),ex);
        }
        LOGGER.info("*********** Finished Executing DebitCardAnnualFeeDeduction Scheduler ***********");
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
