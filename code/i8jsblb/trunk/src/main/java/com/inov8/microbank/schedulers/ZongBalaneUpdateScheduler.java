package com.inov8.microbank.schedulers;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.ZongBalanceUpdateModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.advancesalaryloan.dao.ZongBalanceUpdateDAO;
import com.inov8.microbank.server.service.advancesalaryloanmodule.AdvanceSalaryLoanManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.zongbalanceupdatemodule.ZongBalanceUpdateManager;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZongBalaneUpdateScheduler {
    private static final Logger LOGGER = Logger.getLogger(ZongBalaneUpdateScheduler.class);
    private ZongBalanceUpdateManager zongBalanceUpdateManager;
    private ZongBalanceUpdateDAO zongBalanceUpdateDAO;

    private ESBAdapter esbAdapter;


    private List[] chunks(final List<ZongBalanceUpdateModel> pList, final int pSize) {
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
        stopWatch.start("Zong Balance Update  Scheduler init");
        int ORACLE_LIMIT = 30;
        LOGGER.info("*********** Executing Zong Balance Update Scheduler ***********");

        BaseWrapper bWrapper = new BaseWrapperImpl();
        List<ZongBalanceUpdateModel> toBeIntimatedList = null;
        try {
            toBeIntimatedList = zongBalanceUpdateManager.loadAllAdvanceSalaryLoanData();
            if (!toBeIntimatedList.isEmpty()) {
                LOGGER.info("Total " + toBeIntimatedList.size() + " records fetched for Zong Balance Update.");
                List<AdvanceSalaryLoanModel>[] chunks = this.chunks(toBeIntimatedList, ORACLE_LIMIT);
                List<AdvanceSalaryLoanModel> chunksList = new ArrayList<>();
                for (List<AdvanceSalaryLoanModel> limit : chunks) {
                    chunksList.addAll(limit);
                }
                for (ZongBalanceUpdateModel model : toBeIntimatedList) {
                    try {
                        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                        String mobileNo = MessageUtil.getMessage("zong.mobile.no");
                        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                        I8SBSwitchControllerRequestVO requestVO = prepareI8sbRequest(mobileNo);

                        i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        i8sbSwitchWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                        responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                        responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                        if(responseVO!=null  && !StringUtil.isNullOrEmpty(responseVO.getRemainingBalance())){
                            model.setBalance(Double.valueOf(responseVO.getRemainingBalance()));
                            model.setMobileNo(mobileNo);
                            model.setUpdatedOn(new Date());

                            zongBalanceUpdateDAO.saveOrUpdate(model);

                        }
                    } catch (Exception ex) {
                        LOGGER.error("Error while executing AdvanceSalaryLoanIntimationScheduler.init() :: " + ex.getMessage(), ex);
                    }
                }
            }
            else{
                ZongBalanceUpdateModel zongBalanceUpdateModel=new ZongBalanceUpdateModel();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                String mobileNo = MessageUtil.getMessage("zong.mobile.no");
                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                I8SBSwitchControllerRequestVO requestVO = prepareI8sbRequest(mobileNo);
                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                if(responseVO!=null  && !StringUtil.isNullOrEmpty(responseVO.getRemainingBalance())){
                    zongBalanceUpdateModel.setBalance(Double.valueOf(responseVO.getRemainingBalance()));
                    zongBalanceUpdateModel.setMobileNo(mobileNo);
                    zongBalanceUpdateModel.setUpdatedOn(new Date());
                    zongBalanceUpdateModel.setCreatedOn(new Date());
                    zongBalanceUpdateManager.createMerchantAccountModel(zongBalanceUpdateModel);

                }
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

    private I8SBSwitchControllerRequestVO prepareI8sbRequest(String mobileNo){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setMobileNumber(mobileNo);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_PUSHNOTIFICATION);
        requestVO.setRequestType(I8SBConstants.RequestType_SendPushNotification);
        return requestVO;
    }

    public void setZongBalanceUpdateManager(ZongBalanceUpdateManager zongBalanceUpdateManager) {
        this.zongBalanceUpdateManager = zongBalanceUpdateManager;
    }

    public void setZongBalanceUpdateDAO(ZongBalanceUpdateDAO zongBalanceUpdateDAO) {
        this.zongBalanceUpdateDAO = zongBalanceUpdateDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
