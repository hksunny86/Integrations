package com.inov8.integration.middleware.scheduler;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.inov8.integration.middleware.controller.NetworkInfoBean;
import com.inov8.integration.middleware.enums.ISOAccountTypeEnum;
import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.parser.ISO8583MessageParser;
import com.inov8.integration.middleware.pdu.request.AcquirerReversalAdviceRequest;
import com.inov8.integration.middleware.pdu.request.FundTransferRequest;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.persistance.model.TransactionLogModel;
import com.inov8.integration.middleware.queue.TransactionResponsePoolHandler;
import com.inov8.integration.middleware.service.IntegrationService;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.vo.MiddlewareMessageVO;

@Service
@SuppressWarnings("all")
public class MiddlewareTransactionReversalSchedular {

	protected static Logger logger = LoggerFactory.getLogger(MiddlewareTransactionReversalSchedular.class.getSimpleName());

	@Autowired
	TransactionDAO transactionDAO;
	@Autowired
	private NetworkInfoBean networkInfoBean;
	
	@Autowired
	private IntegrationService integrationService;
	
	@Value("${txtimeout}")
	private String txtimeout;

	@Scheduled(cron = "${reversal.scheduler.cron}")
	@Async
	public void execute() {

		logger.debug("######### Starting Reversal Schedular #########");
		List<TransactionLogModel> reversalTransactions = transactionDAO.findReversalTransaction();
		for (TransactionLogModel transactionLogModel : reversalTransactions) {
			String requestHex = transactionLogModel.getPduRequestString();
			/*
			// build reversal transaction
			BasePDU fundTransferRequest = (BasePDU) ISO8583MessageParser.parse(requestHex);
			MiddlewareMessageVO messageVO = new MiddlewareMessageVO();
			
			messageVO.setParentTransactionId(transactionLogModel.getTransactionLogId());
			messageVO.setReversalRequestTime(fundTransferRequest.getTransactionDate());
			messageVO.setReversalSTAN(fundTransferRequest.getStan());
			
			logger.debug("Started Reversal of Transaction RRN: "+ fundTransferRequest.getRrn());
			integrationService.acquirerReversalAdvice(messageVO);
			
			// update retry count
			transactionDAO.updateRetryCount(transactionLogModel.getTransactionLogId());
			*/
			
		} 
	}

	@Async
	public Future<Void> prepareReqHeaderAsync() {
		return null;
	}
}
