package com.inov8.microbank.debitcard.dailyjob;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import org.apache.log4j.Logger;

public class DebitCardDataImportExportScheduler {

    private static final Logger LOGGER = Logger.getLogger(DebitCardDataImportExportScheduler.class );

    private DebitCardManager debitCardManager;
    //no argument default constructor
    public DebitCardDataImportExportScheduler(){

    }

    @SuppressWarnings("unchecked")
    public void init() throws FrameworkCheckedException {
        LOGGER.info("*********** Executing DebitCard Data Import_Export Scheduler ***********");
        try
        {
            debitCardManager.saveDebitCardImportExportSchedulerRequest();
        }
        catch(Exception e)
        {
            LOGGER.error(e.getMessage() + ": " + e);
            throw new FrameworkCheckedException(e.getMessage());
        }
        LOGGER.info("*********** Finished Executing Debit Card Import Export Scheduler ***********");
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }
}
