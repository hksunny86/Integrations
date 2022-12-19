package com.inov8.microbank.server.messaging.listener;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.model.IBFTRetryAdviceModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.messagingmodule.IBFTRetryAdviceDAO;
import com.inov8.microbank.server.handler.CoreToWalletRequestHandler;
import com.inov8.microbank.server.handler.IBFTRequestHandler;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class WalletIncomingReqQueueListener implements MessageListener {

    private static Log logger = LogFactory.getLog(WalletIncomingReqQueueListener.class);

    private CoreToWalletRequestHandler coreToWalletRequestHandler;
    private TransactionReversalManager transactionReversalManager;
    private IBFTRetryAdviceDAO ibftRetryAdviceDAO;

    @Override
    public void onMessage(Message message) {

        MiddlewareAdviceVO messageVO = null;
        try {
            messageVO = (MiddlewareAdviceVO) ((ObjectMessage) message).getObject();

            logger.info("Message Recieved at WalletIncomingReqQueueListener... mobile no:" + ((messageVO != null) ? messageVO.getAccountNo2() : ""));

            boolean isAlreadyDone = transactionReversalManager.checkAlreadySuccessful(messageVO.getStan(), messageVO.getRequestTime(),PortalConstants.IBFT_ADVICE_TYPE);

            if (isAlreadyDone) {

                try {
                    transactionReversalManager.updateIBFTStatus(messageVO.getStan(), messageVO.getRequestTime(), PortalConstants.IBFT_STATUS_SUCCESS, null);
                } catch (Exception exc) {
                    // Don't throw the exception to avoid retries as the actual transaction is already performed
                    logger.error("[WalletIncomingReqQueueListener.onMessage] Unable to mark status of Wallet request to Successful as it was already Done" +
                            " stan:" + messageVO.getStan() + " , Request Time:" + messageVO.getRequestTime(), exc);
                }

            } else {

                coreToWalletRequestHandler.makeCreditAdviceRequest(messageVO);

            }

        } catch (Exception ex) {
            logger.error("Exception occured @ WalletIncomingReqQueueListener... failed to perform Wallet Transaction for mobile no:" + ((messageVO != null) ? messageVO.getAccountNo2() : ""));
            logger.error(ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    private boolean checkAlreadyExists(String stan, Date requestTime) throws FrameworkCheckedException {
        boolean result = false;

        if (StringUtil.isNullOrEmpty(stan) || requestTime == null) {
            throw new FrameworkCheckedException("Invalid Input for IBFT Credit Advice. STAN:" + stan + " , Request Time:" + requestTime);
        }

        IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
        iBFTRetryAdviceModel.setStan(stan);
        iBFTRetryAdviceModel.setRequestTime(requestTime);

        Calendar c = Calendar.getInstance();
        c.setTime(requestTime);
        c.set(Calendar.MILLISECOND, 0);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", c.getTime(), c.getTime());

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("ibftRetryAdviceId", SortingOrder.DESC);


        CustomList<IBFTRetryAdviceModel> customList = ibftRetryAdviceDAO.findByExample(
                iBFTRetryAdviceModel, null, sortingOrderMap, dateRangeHolderModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        List<IBFTRetryAdviceModel> list = customList.getResultsetList();

        if (list != null && list.size() > 0) {
            result = true;
        }

        return result;
    }

    public CoreToWalletRequestHandler getCoreToWalletRequestHandler() {
        return coreToWalletRequestHandler;
    }

    public void setCoreToWalletRequestHandler(CoreToWalletRequestHandler coreToWalletRequestHandler) {
        this.coreToWalletRequestHandler = coreToWalletRequestHandler;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setIbftRetryAdviceDAO(IBFTRetryAdviceDAO ibftRetryAdviceDAO) {
        this.ibftRetryAdviceDAO = ibftRetryAdviceDAO;
    }
}
