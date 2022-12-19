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
import com.inov8.microbank.server.handler.CreditPaymentRequestHandler;
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

public class CreditPaymentReqQueueListener implements MessageListener {

    private static Log logger = LogFactory.getLog(CreditPaymentReqQueueListener.class);

    private CreditPaymentRequestHandler creditPaymentRequestHandler;
    private TransactionReversalManager transactionReversalManager;
    private IBFTRetryAdviceDAO ibftRetryAdviceDAO;

    @Override
    public void onMessage(Message message) {

        MiddlewareAdviceVO messageVO = null;
        try {
            messageVO = (MiddlewareAdviceVO) ((ObjectMessage) message).getObject();

            logger.info("Message Recieved at CreditPaymentReqQueueListener... mobile no:" + ((messageVO != null) ? messageVO.getAccountNo2() : ""));
            boolean isAlreadyExists = this.checkAlreadyExists(messageVO.getStan(), messageVO.getRequestTime());

            if (!isAlreadyExists) {
                transactionReversalManager.saveNewIBFTRecord(messageVO);
            }
            boolean isAlreadyDone = transactionReversalManager.checkAlreadySuccessful(messageVO.getStan(), messageVO.getRequestTime(),PortalConstants.CREDIT_PAYMENT_ADVICE_TYPE);

            if (isAlreadyDone) {

                try {
                    transactionReversalManager.updateIBFTStatus(messageVO.getStan(), messageVO.getRequestTime(), PortalConstants.CREDIT_PAYMENT_ADVICE_TYPE, null);
                } catch (Exception exc) {
                    // Don't throw the exception to avoid retries as the actual transaction is already performed
                    logger.error("[CreditPaymentReqQueueListener.onMessage] Unable to mark status of Wallet request to Successful as it was already Done" +
                            " stan:" + messageVO.getStan() + " , Request Time:" + messageVO.getRequestTime(), exc);
                }

            } else {

                creditPaymentRequestHandler.makeCreditPaymentAdviceRequest(messageVO);

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

    public CreditPaymentRequestHandler getCreditPaymentRequestHandler() {
        return creditPaymentRequestHandler;
    }

    public void setCreditPaymentRequestHandler(CreditPaymentRequestHandler creditPaymentRequestHandler) {
        this.creditPaymentRequestHandler = creditPaymentRequestHandler;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setIbftRetryAdviceDAO(IBFTRetryAdviceDAO ibftRetryAdviceDAO) {
        this.ibftRetryAdviceDAO = ibftRetryAdviceDAO;
    }
}
