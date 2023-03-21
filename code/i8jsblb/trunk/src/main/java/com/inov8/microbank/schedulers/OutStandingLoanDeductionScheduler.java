package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.advancesalaryloanmodule.AdvanceSalaryLoanManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OutStandingLoanDeductionScheduler {

    private static final Logger LOGGER = Logger.getLogger(OutStandingLoanDeductionScheduler.class);

    private CommandManager commandManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private AppUserManager appUserManager;
    private ESBAdapter esbAdapter;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;

    private List[] chunks(final List<SmartMoneyAccountModel> pList, final int pSize) {
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
        stopWatch.start("OutStandingLoanDeduction Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing OutStandingLoanDeduction Scheduler ***********");
        List<SmartMoneyAccountModel> toBeProcessedList = null;
        String response = null;
        AppUserModel appUserModel = new AppUserModel();
        try {
            toBeProcessedList = smartMoneyAccountManager.loadSmartMoneyAccountByIsOptasiaDebitBlocked();
            if (!toBeProcessedList.isEmpty()) {
                LOGGER.info("Total " + toBeProcessedList.size() + " records fetched for OutStandingLoanDeduction.");
                List<SmartMoneyAccountModel>[] chunks = this.chunks(toBeProcessedList, ORACLE_LIMIT);
                List<SmartMoneyAccountModel> chunksList = new ArrayList<>();
                for (List<SmartMoneyAccountModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (SmartMoneyAccountModel model : toBeProcessedList) {
                    try {
                        appUserModel = appUserManager.loadAppUserModelByCustomerId(model.getCustomerId());
                        if (appUserModel != null) {
                            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                            requestVO = ESBAdapter.loans(I8SBConstants.RequestType_OPTASIA_OUTSTANDING);
                            requestVO.setIdentityType("customerIdentity");
                            requestVO.setIdentityValue(appUserModel.getShaNic());
                            requestVO.setOrigSource("mobileApp");

                            SwitchWrapper sWrapper = new SwitchWrapperImpl();
                            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                            sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                            ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                            responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                            if (responseVO.getResponseCode().equals("I8SB-200")) {
                                if (responseVO.getTotalGross().equals("0")) {
                                    model.setIsDebitBlocked(false);
                                    model.setDebitBlockAmount(0.0);
                                    model.setIsOptasiaDebitBlocked(false);
                                    model.setDebitBlockReason(null);
                                    model.setUpdatedOn(new Date());
                                    smartMoneyAccountDAO.saveOrUpdate(model);
                                } else {
                                    BaseWrapper dWrapper = new BaseWrapperImpl();
                                    dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, responseVO.getTotalGross());
                                    dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.LOAN_XTRA_CASH);
                                    response = commandManager.executeCommand(dWrapper, CommandFieldConstants.CMD_DEBIT_PAYMENT_API);
                                }
                            }
                        }
                        else{
                            throw new Exception("Customer Not Found");
                        }

                    } catch (Exception ex) {
                        LOGGER.error("Error while executing OutStandingLoanDeduction.init() :: " + ex.getMessage(), ex);
                    }
                }
            }else {
                LOGGER.info("Total " + toBeProcessedList.size() + " records fetched for OutStandingLoanDeduction.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing OutStandingLoanDeduction.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing OutStandingLoanDeduction Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }
}
