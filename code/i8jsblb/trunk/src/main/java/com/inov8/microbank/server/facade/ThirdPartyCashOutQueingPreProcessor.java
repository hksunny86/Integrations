package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.BISPCustNadraVerificationModel;
import com.inov8.microbank.common.model.SafRepoCashOutModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThirdPartyCashOutAdviceSender;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.safrepo.SafRepoCashOutDao;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import java.util.Date;
import java.util.List;

/**
 * Created by Attique on 9/6/2018.
 */
public class ThirdPartyCashOutQueingPreProcessor {
    private SafRepoCashOutDao safRepoCashOutDao;
    private ThirdPartyCashOutAdviceSender thirdPartyCashOutAdviceSender;
    private  ESBAdapter esbAdapter;
    private static final Log logger = LogFactory.getLog(ThirdPartyCashOutQueingPreProcessor.class);

    public ThirdPartyCashOutQueingPreProcessor(){}

    public void loadAndForwardAdviceToQueue(String transactionCode) throws InterruptedException{
        if(StringUtil.isNullOrEmpty(transactionCode)){
            logger.error("[CoreAdviceQueingPreProcessor.loadAndForwardAdviceToQueue] TransactionCode supplied is Null...");
            return;
        }

        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());
        workFlowWrapper.getTransactionCodeModel().setCode(transactionCode);
        this.startProcessing(workFlowWrapper);
    }

    public void startProcessing(WorkFlowWrapper wrapper){
        new ThirdPartyCashOutQueingThread(wrapper).start();
    }

    private I8SBSwitchControllerRequestVO prepareI8sbRequestVo(SafRepoCashOutModel safRepoCashOutModel
            , I8SBSwitchControllerRequestVO requestVo, Boolean isManualReversal, BISPCustNadraVerificationModel custNadraVerificationModel) throws FrameworkCheckedException {

        try {
            logger.info("[CoreAdviceQueingPreProcessor.pushCoreAdviceQueue] Pushing Core Advice to queue. trx ID:" + safRepoCashOutModel.getTransactionCode());
            if(isManualReversal){
                if(safRepoCashOutModel.getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT)){
                    requestVo.setTransactionId(safRepoCashOutModel.getTransactionId());
                }
                else
                    requestVo.setTransactionId(custNadraVerificationModel.getBaflTransactionNumber());
                requestVo.setSessionId(custNadraVerificationModel.getBaflSessionId());
                requestVo.setWalletAccountId(custNadraVerificationModel.getBaflWalletId());
                requestVo.setSessionIdNadra(custNadraVerificationModel.getNadraSessionId());
            }
            else if(!isManualReversal || safRepoCashOutModel.getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT)){
                logger.info("[CoreAdviceQueingPreProcessor.pushCoreAdviceQueue] Setting BOP trx ID in requestVO:" + safRepoCashOutModel.getTransactionId());
                requestVo.setTransactionId(safRepoCashOutModel.getTransactionId());
                requestVo.setSessionId(safRepoCashOutModel.getSessionId());
            }
            requestVo.setTransactionAmount(String.valueOf(safRepoCashOutModel.getTransactionAmount()));
            requestVo.setTransmissionDateAndTime(String.valueOf( new Date()));
            requestVo.setAgentId(safRepoCashOutModel.getSellerCode());
            requestVo.setConsumerNumber(safRepoCashOutModel.getCustomerAccountNumber());
            requestVo.setTranCode(safRepoCashOutModel.getTransactionCode());
            requestVo.setSenderMobile(safRepoCashOutModel.getAgentAccountNumber());
            requestVo.setCNIC(safRepoCashOutModel.getCustomerCnic());
            requestVo.setProductCode(safRepoCashOutModel.getProjectCode());
            requestVo.setTerminalID(safRepoCashOutModel.getTerminalId());
//            thirdPartyCashOutAdviceSender.send(middlewareAdviceVO);

        } catch (Exception e) {
            logger.error("Failed to push Core Advice to queue. trx ID:" + safRepoCashOutModel.getTransactionCode(), e);
            throw new FrameworkCheckedException("Failed to queue Core Advice");
        }

        return requestVo;
    }


    private SwitchWrapper pushCoreAdviceQueue(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        try {
            thirdPartyCashOutAdviceSender.send(switchWrapper);

        } catch (Exception e) {
            throw new FrameworkCheckedException("Failed to queue Core Advice");
        }

        return switchWrapper;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    class ThirdPartyCashOutQueingThread extends Thread{

        WorkFlowWrapper workFlowWrapper;

        ThirdPartyCashOutQueingThread(WorkFlowWrapper workFlowWrapper){
            this.workFlowWrapper = workFlowWrapper;
        }

        @Override
        public void run() {
            MiddlewareAdviceVO middlewareAdviceVO = null;
            try {
                Boolean isManualReversal = Boolean.FALSE;
                BISPCustNadraVerificationModel custNadraVerificationModel = null;
                SafRepoCashOutModel safRepoCoreModel = new SafRepoCashOutModel();
                safRepoCoreModel.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
                if(workFlowWrapper.getObject("IS_MANUAL_REVERSAL") != null && workFlowWrapper.getObject("IS_MANUAL_REVERSAL").equals("1"))
                {
                    isManualReversal = Boolean.TRUE;
                    custNadraVerificationModel = (BISPCustNadraVerificationModel) workFlowWrapper.getObject("BISP_MODEL");
                }
                else
                    safRepoCoreModel.setStatus(PortalConstants.SAF_STATUS_INITIATED);
                logger.info("[CoreAdviceQueingPreProcessor.run] Loading transactions against trx_code: "+safRepoCoreModel.getTransactionCode());
                ExampleConfigHolderModel config=new ExampleConfigHolderModel();
                config.setMatchMode(MatchMode.EXACT);
                CustomList<SafRepoCashOutModel> customList = safRepoCashOutDao.findByExample(safRepoCoreModel,null,null,config);
                List<SafRepoCashOutModel> safRepoCoreModels = customList.getResultsetList();

                if(safRepoCoreModels != null && safRepoCoreModels.size() > 0) {

                    for (SafRepoCashOutModel safModel : safRepoCoreModels) {
                        I8SBSwitchControllerRequestVO requestVO=new I8SBSwitchControllerRequestVO();
                        if(safModel.getProductId() != null && (safModel.getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT)
                                || safModel.getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT_COVID_19))){
                            requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_CashWithdrawalReversal);
                            logger.info("[CoreAdviceQueingPreProcessor.run] Loading BOP Cash Out transactions against trx_code: "+safRepoCoreModel.getTransactionCode());
                            requestVO = prepareI8sbRequestVo(safModel,requestVO,isManualReversal,custNadraVerificationModel);
                        }
                        else{
                            try{
                                requestVO=esbAdapter.prepareRequestVoForApiGee(I8SBConstants.RequestType_CashWithdrawalReversal);
                                requestVO = prepareI8sbRequestVo(safModel,requestVO,isManualReversal,custNadraVerificationModel);
                            }catch(Exception ee){
                                logger.error("Exception while pushing Core Advice to Queue for transactionCodeId:" + workFlowWrapper.getTransactionCodeModel().getTransactionCodeId() , ee);
                                updateSafRepoCoreStatus(workFlowWrapper.getTransactionCodeModel().getCode(), PortalConstants.SAF_STATUS_FAILED);
                            }
                        }
                        I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();
                        SwitchWrapper sWrapper=new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        pushCoreAdviceQueue(sWrapper);
                        updateSafRepoCoreStatus(workFlowWrapper.getTransactionCodeModel().getCode() , PortalConstants.SAF_STATUS_SUCCESSFUL);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Exception in CoreQueingThread.run()...",e);
            }
        }

    }

    private void updateSafRepoCoreStatus(String trxCode,String status) throws FrameworkCheckedException{
        try{
            SafRepoCashOutModel safRepoCoreModel = new SafRepoCashOutModel();
            safRepoCoreModel.setTransactionCode(trxCode);
            safRepoCoreModel.setStatus(status);
            safRepoCoreModel.setUpdatedOn(new Date());

            safRepoCashOutDao.updateStatus(safRepoCoreModel);
        }catch(Exception ex){
            logger.error("Failed to updateSafRepoCoreStatus to '"+status+"' for  trxCode:" + trxCode, ex);
        }
    }

    public void setSafRepoCashOutDao(SafRepoCashOutDao safRepoCashOutDao) {
        this.safRepoCashOutDao = safRepoCashOutDao;
    }

    public void setThirdPartyCashOutAdviceSender(ThirdPartyCashOutAdviceSender thirdPartyCashOutAdviceSender) {
        this.thirdPartyCashOutAdviceSender = thirdPartyCashOutAdviceSender;
    }

}
