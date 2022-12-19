package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.TransactionReversalModel;
import com.inov8.microbank.common.util.CoreAdviceUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ReversalAdviceSender;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.debitcard.vo.DebitCardReversalVO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionReversalDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class ReversalAdviceQueingPreProcessor {

//    private TransactionReversalDAO transactionReversalDAO;
    private ReversalAdviceSender reversalAdviceSender;
    private static final Log logger = LogFactory.getLog(CoreAdviceQueingPreProcessor.class);

    public ReversalAdviceQueingPreProcessor()
    {

    }

    public void startProcessing(Object wrapper){
        new CoreQueingThread(wrapper).start();
    }

    private Object pushCoreAdviceQueue(Object middlewareAdviceVO) throws FrameworkCheckedException {
        try {
            logger.info("[ReversalAdviceQueingPreProcessor.pushCoreAdviceQueue] Pushing Core Advice to queue.");
            reversalAdviceSender.send(middlewareAdviceVO);

        } catch (Exception e) {
            logger.error("Failed to push Reversal Advice to queue.", e);
            throw new FrameworkCheckedException("Failed to queue Reversal Advice");
        }

        return middlewareAdviceVO;
    }

    class CoreQueingThread extends Thread{

        Object workFlowWrapper;

        CoreQueingThread(Object workFlowWrapper){
            this.workFlowWrapper = workFlowWrapper;
        }

        @Override
        public void run() {
            logger.info("Welcome to Reversal Advice....");
            try {
                TransactionReversalModel reversalModel = new TransactionReversalModel();
                reversalModel.setStatus(PortalConstants.SAF_STATUS_INITIATED);
                logger.info("[CoreAdviceQueingPreProcessor.run] Loading transactions against trx_code: ");
                try{
                    pushCoreAdviceQueue(workFlowWrapper);

                    //updateSafRepoCoreStatus(workFlowWrapper.getTransactionCodeModel().getCode() , PortalConstants.SAF_STATUS_SUCCESSFUL);

                }catch(Exception ee){
                    logger.error("Exception while pushing Core Advice to Queue for transactionCodeId:" , ee);
                    //updateSafRepoCoreStatus(workFlowWrapper.getTransactionCodeModel().getCode(), PortalConstants.SAF_STATUS_FAILED);
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Exception in CoreQueingThread.run()...",e);
            }
        }

    }

    private void updateSafRepoCoreStatus(String trxCode,String status) throws FrameworkCheckedException {
        /*try{
            SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
            safRepoCoreModel.setTransactionCode(trxCode);
            safRepoCoreModel.setStatus(status);
            safRepoCoreModel.setUpdatedOn(new Date());

            safRepoCoreDao.updateStatus(safRepoCoreModel);
        }catch(Exception ex){
            logger.error("Failed to updateSafRepoCoreStatus to '"+status+"' for  trxCode:" + trxCode, ex);
        }*/
    }

    /*public void setTransactionReversalDAO(TransactionReversalDAO transactionReversalDAO) {
        this.transactionReversalDAO = transactionReversalDAO;
    }*/

    public void setReversalAdviceSender(ReversalAdviceSender reversalAdviceSender) {
        this.reversalAdviceSender = reversalAdviceSender;
    }
}
