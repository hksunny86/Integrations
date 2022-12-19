package com.inov8.microbank.schedulers;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.vo.DebitCardStatusVO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.cardconfiguration.model.CardFeeTypeModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.debitcard.service.DebitCardManagerImpl;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebitCardFeeChargesScheduler {
    private static final Logger LOGGER = Logger.getLogger(DebitCardFeeChargesScheduler.class);

    private CommandManager commandManager;
    private DebitCardManager debitCardManager;
    private AppUserModel appUserModel = new AppUserModel();
    private AppUserManager appUserManager;
    private DebitCardModelDAO debitCardModelDAO;

    private List[] chunks(final List<DebitCardChargesSafRepoModel> pList, final int pSize) {
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
        stopWatch.start("DebitCardFailedFeeChargesScheduler Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing DebitCardFailedFeeChargesScheduler Scheduler ***********");
        List<DebitCardChargesSafRepoModel> toBeRenewedList = null;
        try {
            toBeRenewedList = debitCardManager.loadAllDebitCardFeeChargesRequired();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for DebitCardFailedFeeChargesScheduler.");
                List<DebitCardChargesSafRepoModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<DebitCardChargesSafRepoModel> chunksList = new ArrayList<>();
                for (List<DebitCardChargesSafRepoModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (DebitCardChargesSafRepoModel model : toBeRenewedList) {
                    try {

                        debitCardManager.makeFeeDeductionDebitCardFailedCommand(model);

                    } catch (Exception ex) {
                        LOGGER.error("Error while executing DebitCardFailedFeeChargesScheduler.init() :: " + ex.getMessage(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing DebitCardFailedFeeChargesScheduler.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing DebitCardFailedFeeChargesScheduler Scheduler ***********");
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
