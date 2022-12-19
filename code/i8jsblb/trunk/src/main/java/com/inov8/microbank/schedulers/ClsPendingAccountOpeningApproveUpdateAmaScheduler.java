package com.inov8.microbank.schedulers;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.ClsPendingAccountOpeningManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;
import org.springframework.web.context.ContextLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClsPendingAccountOpeningApproveUpdateAmaScheduler {

    private static final Logger LOGGER = Logger.getLogger(ClsPendingAccountOpeningApproveUpdateAmaScheduler.class);

    private AppUserModel appUserModel = new AppUserModel();
    private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;
    private ClsPendingAccountOpeningManager clsPendingAccountOpeningManager;
    private ESBAdapter esbAdapter;

    private List[] chunks(final List<ClsPendingAccountOpeningModel> pList, final int pSize) {
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
        stopWatch.start("ClsPendingAccountApprovedUpdateAma Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing ClsPendingAccountApprovedUpdateAma Scheduler ***********");
        List<ClsPendingAccountOpeningModel> toBeRenewedList = null;
        try {
            toBeRenewedList = clsPendingAccountOpeningManager.loadAllClsPendingAccountOpeningApprovedUpdateAMA();
            if (!toBeRenewedList.isEmpty()) {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for ClsPendingAccountApprovedUpdateAma.");
                List<ClsPendingAccountOpeningModel>[] chunks = this.chunks(toBeRenewedList, ORACLE_LIMIT);
                List<ClsPendingAccountOpeningModel> chunksList = new ArrayList<>();
                for (List<ClsPendingAccountOpeningModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (ClsPendingAccountOpeningModel model : toBeRenewedList) {
                    try {

                        LOGGER.info("*********** Executing Update Ama Scheduler ***********");
                        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                        requestVO = ESBAdapter.prepareUpdateAma(I8SBConstants.RequestType_AMMA_Update_Account);
                        requestVO.setMobileNumber(model.getMobileNo());
                        requestVO.setIsEnabled("1");
                        requestVO.setIsPinSet("0");
                        requestVO.setIsClose("0");
                        requestVO.setIsRegistered("1");

                        if (model.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                            requestVO.setAccountType("L0");
                        } else if (model.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                            requestVO.setAccountType("L1");
                        }
                        requestVO.setCNIC(model.getCnic());
                        requestVO.setAccountTitle(model.getConsumerName());
                        String cellNumber = model.getMobileNo().replaceFirst("0", "92");
                        requestVO.setTraceNo(new SimpleDateFormat("yyMMHH").format(new Date()));
                        requestVO.setMsisdn(cellNumber);
                        requestVO.setReserved1("1");
                        requestVO.setReserved2("1");
                        requestVO.setReserved3("1");
                        requestVO.setReserved4("1");
                        SwitchWrapper sWrapper = new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                        if (!responseVO.getResponseCode().equals("I8SB-200")) {
                            throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                        } else {
                            model.setUpdateAMA(true);
                            clsPendingAccountOpeningDAO.saveOrUpdate(model);
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing ClsPendingAccountApprovedSmsAlter.init() :: " + ex.getMessage(), ex);
                    }
                }
            } else {
                LOGGER.info("Total " + toBeRenewedList.size() + " records fetched for ClsPendingAccountApprovedSmsAlter.");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing ClsPendingAccountApprovedSmsAlter.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing ClsPendingAccountApprovedSmsAlter Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());


    }


    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }


    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }

    public void setClsPendingAccountOpeningManager(ClsPendingAccountOpeningManager clsPendingAccountOpeningManager) {
        this.clsPendingAccountOpeningManager = clsPendingAccountOpeningManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
