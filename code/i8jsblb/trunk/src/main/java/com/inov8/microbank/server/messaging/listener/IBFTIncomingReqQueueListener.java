package com.inov8.microbank.server.messaging.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.dao.productmodule.IBFTStatusDAO;
import com.inov8.microbank.server.dao.productmodule.hibernate.IBFTStatusHibernateDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.handler.IBFTRequestHandler;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;

public class IBFTIncomingReqQueueListener implements MessageListener {

private static Log logger = LogFactory.getLog(IBFTIncomingReqQueueListener.class);
	
	private IBFTRequestHandler ibftRequestHandler;
    private TransactionReversalManager transactionReversalManager;
    private IBFTStatusHibernateDAO ibftStatusHibernateDAO;

	@Override
	public void onMessage(Message message) {
		MiddlewareAdviceVO messageVO = null;
		try {
			messageVO = (MiddlewareAdviceVO) ((ObjectMessage) message).getObject();
			
			logger.info("Message Recieved at IBFTIncomingReqQueueListener... mobile no:" + ((messageVO != null)?messageVO.getAccountNo2():""));
			
			boolean isAlreadyDone = transactionReversalManager.checkAlreadySuccessful(messageVO.getStan(),messageVO.getRequestTime(),PortalConstants.IBFT_ADVICE_TYPE);

			if( isAlreadyDone ){
				
				try{
					transactionReversalManager.updateIBFTStatus(messageVO.getStan(),messageVO.getRequestTime(), PortalConstants.IBFT_STATUS_SUCCESS,null);

				}catch(Exception exc){
					// Don't throw the exception to avoid retries as the actual transaction is already performed
					logger.error("[IBFTIncomingReqQueueListener.onMessage] Unable to mark status of IBFT request to Successful as it was already Done" +
							" stan:"+messageVO.getStan()+" , Request Time:" +messageVO.getRequestTime(), exc);
				}
			
			}else{
				
				ibftRequestHandler.makeCreditAdviceRequest(messageVO);


			}
			
		}catch (Exception ex){
			logger.error("Exception occured @ IBFTIncomingReqQueueListener... failed to perform IBFT Transaction for mobile no:" + ((messageVO != null)?messageVO.getAccountNo2():""));
			logger.error(ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	public IBFTRequestHandler getIbftRequestHandler() {
		return ibftRequestHandler;
	}

	public void setIbftRequestHandler(IBFTRequestHandler ibftRequestHandler) {
		this.ibftRequestHandler = ibftRequestHandler;
	}

	public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
		this.transactionReversalManager = transactionReversalManager;
	}

	public void setIbftStatusHibernateDAO(IBFTStatusHibernateDAO ibftStatusHibernateDAO) {
		this.ibftStatusHibernateDAO = ibftStatusHibernateDAO;
	}

}
