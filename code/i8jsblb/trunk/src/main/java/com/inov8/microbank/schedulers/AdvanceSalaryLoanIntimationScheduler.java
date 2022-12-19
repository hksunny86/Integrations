package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.advancesalaryloanmodule.AdvanceSalaryLoanManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.ClsPendingAccountOpeningManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvanceSalaryLoanIntimationScheduler {
    private static final Logger LOGGER = Logger.getLogger(AdvanceSalaryLoanIntimationScheduler.class);
    private AdvanceSalaryLoanManager advanceSalaryLoanManager;
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;

    private ESBAdapter esbAdapter;


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
        stopWatch.start("AdvanceSalaryLoanIntimation Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing AdvanceSalaryLoanIntimation Scheduler ***********");

        BaseWrapper bWrapper = new BaseWrapperImpl();
        List<AdvanceSalaryLoanModel> toBeIntimatedList = null;
        try {
            toBeIntimatedList = advanceSalaryLoanManager.loadAdvanceSalaryLoanByIsCompleted();
            if (!toBeIntimatedList.isEmpty()) {
                LOGGER.info("Total " + toBeIntimatedList.size() + " records fetched for AdvanceSalaryLoanIntimationScheduler.");
                List<AdvanceSalaryLoanModel>[] chunks = this.chunks(toBeIntimatedList, ORACLE_LIMIT);
                List<AdvanceSalaryLoanModel> chunksList = new ArrayList<>();
                for (List<AdvanceSalaryLoanModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (AdvanceSalaryLoanModel model : toBeIntimatedList) {
                    try {
                        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

                        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareAdvanceSalaryLoanIntimationRequest
                                (I8SBConstants.RequestType_LOANINTIMATION);

                        requestVO.setTransactionAmount(String.valueOf(model.getInstallmentAmount()));
                        requestVO.setMobileNumber(model.getMobileNo());

                        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                        i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        i8sbSwitchWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                        responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                        ESBAdapter.processI8sbResponseCode(responseVO, false);
                        if (!responseVO.getResponseCode().equals("I8SB-200")) {
                            throw new Exception("Error while Intimating Advance Salary Loan info");
                        }

                        model.setIsIntimated(true);
                        model.setUpdatedOn(new Date());
                        model.setUpdatedBy(UserTypeConstantsInterface.SCHEDULER);
                        advanceSalaryLoanDAO.saveOrUpdate(model);
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing AdvanceSalaryLoanIntimationScheduler.init() :: " + ex.getMessage(), ex);
                    }
                }
            }
            else{
                LOGGER.info("Total " + toBeIntimatedList.size() + " records fetched for AdvanceSalaryLoanIntimationScheduler.");
            }
        }
        catch (Exception ex){
            LOGGER.error("Error while executing AdvanceSalaryLoanIntimationScheduler.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing AdvanceSalaryLoanIntimation Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
    }

    public void setAdvanceSalaryLoanManager(AdvanceSalaryLoanManager advanceSalaryLoanManager) {
        this.advanceSalaryLoanManager = advanceSalaryLoanManager;
    }

    public void setAdvanceSalaryLoanDAO(AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
