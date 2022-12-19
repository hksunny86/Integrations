package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.safrepo.VCFileDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.text.DecimalFormat;
import java.util.List;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

public class VCAmountScheduler {
    private static final Logger LOGGER = Logger.getLogger(VCAmountScheduler.class);

    private FinancialIntegrationManager financialIntegrationManager;
    private VCFileDAO vcFileDAO;
    protected SettlementManager settlementManager;
    private ESBAdapter esbAdapter;
    private TransactionCodeDAO transactionCodeDAO;
    private CommandManager commandManager;

    private List[] chunks(final List<VCFileModel> pList, final int pSize) {
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
        stopWatch.start("VCAmountScheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing VCAmountScheduler ***********");
        String response = null;
        Double totalBalance = 0.0;

        BaseWrapper bWrapper = new BaseWrapperImpl();
        List<VCFileModel> toBeProcessedList = null;
        try {
            String transactionType = "Purchase/Auth Completion";
            toBeProcessedList = vcFileDAO.loadAllToBeProcessedRecords(transactionType);
            if (!toBeProcessedList.isEmpty()) {
                for (VCFileModel model : toBeProcessedList) {
                    totalBalance = totalBalance + model.getBillingAmount();
                }
                    totalBalance = roundTwoDecimals(totalBalance);
                    LOGGER.info("Amount to be disbursed is: " + totalBalance);
                    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                    AppUserModel appUserModelMain = new AppUserModel();
                    appUserModelMain.setAppUserId(SCHEDULER_APP_USER_ID);
                    ThreadLocalAppUser.setAppUserModel(appUserModelMain);

                    BaseWrapper dWrapper = new BaseWrapperImpl();
                    dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, totalBalance);
                    dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.VC_TRANSFER_PRODUCT);
                    response = commandManager.executeCommand(dWrapper, CommandFieldConstants.CMD_VC_TRANSFER);

                  if(response != null) {
                      for (VCFileModel model : toBeProcessedList) {
                          model.setIsCompleted(1L);
                          model.setIsMatched(1L);

                          String transactionCode = MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.TRANS_ID_NODEREF);
                          model.setMicrobankTransactionCode(transactionCode);
                          vcFileDAO.updateVCFileModel(model);
                      }
                  }
                LOGGER.info("Total " + toBeProcessedList.size() + " records fetched for VCAmountScheduler.");
            }
            else{
                LOGGER.info("Total " + toBeProcessedList.size() + " records fetched for VCAmountScheduler.");
            }
        }
        catch (CommandException e){
            LOGGER.error("Error while executing VCAmountScheduler.init() :: " + e.getMessage(), e);
        }
        catch (Exception ex){
            LOGGER.error("Error while executing VCAmountScheduler.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing VCAmountScheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
    }

    private Double roundTwoDecimals(Double value) {
        Double roundedValue = new Double(0.0);
        if(value != null){
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            roundedValue =  Double.valueOf(twoDForm.format(value));
        }
        return roundedValue;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setVcFileDAO(VCFileDAO vcFileDAO) {
        this.vcFileDAO = vcFileDAO;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }
    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setTransactionCodeDAO(TransactionCodeDAO transactionCodeDAO) {
        this.transactionCodeDAO = transactionCodeDAO;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
}
