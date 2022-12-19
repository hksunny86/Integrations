package com.inov8.integration.middleware.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.BillInquiryResponse;
import com.inov8.integration.middleware.pdu.response.BillPaymentResponse;
import com.inov8.integration.middleware.pdu.response.IbftAdviceResponse;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.inov8.integration.middleware.client.MiddlewareClient;
import com.inov8.integration.middleware.controller.NetworkInfoBean;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.persistance.model.TransactionLogModel;
import com.inov8.integration.middleware.queue.TransactionResponsePoolHandler;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.vo.MiddlewareMessageVO;
//import com.inov8.integration.vo.apig.response.BillInquiryResponse;
//import com.inov8.integration.vo.apig.response.BillPayementResponse;

@Service("IntegrationService")
public class IntegrationService {

	@Autowired
	private TransactionDAO transactionDAO;
	@Autowired
	private TransactionResponsePoolHandler poolHandler;
	@Autowired
	private MiddlewareClient middlewareClient;
	@Autowired
	private NetworkInfoBean networkInfoBean;
	@Autowired
	private IntegrationTransactionRequestBuilder transactionRequestBuilder;

	private static Logger logger = LoggerFactory.getLogger(IntegrationService.class.getSimpleName());
	private String BASE_URL_INQUIRY = ConfigReader.getInstance().getProperty("api.url.billInquiry", "https://mfahad88-eval-test.apigee.net/db_microbank/");
	private String BASE_URL_PAYMENT = ConfigReader.getInstance().getProperty("api.url.billPayment", "https://mfahad88-eval-test.apigee.net/db_microbank/");

	public MiddlewareMessageVO accountBalanceInquiry(MiddlewareMessageVO messageVO) throws RuntimeException {
		AccountBalanceInquiryRequest accountBalanceInquiry = transactionRequestBuilder.buildAccountBalanceInquiryRequest(messageVO);

		// Build Message
		accountBalanceInquiry.assemble();
		messageVO.setStan(accountBalanceInquiry.getStan());

		if (networkInfoBean.isConnected()) {
			saveTransaction(accountBalanceInquiry, messageVO, true);
			middlewareClient.sendMessage(accountBalanceInquiry);
			poolHandler.checkResponsePool(messageVO);

		} else {
			saveTransaction(accountBalanceInquiry, messageVO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			messageVO.setResponseCode("911");
			return messageVO;
		}

		return messageVO;
	}

	public MiddlewareMessageVO checkBill(MiddlewareMessageVO messageVO) throws RuntimeException {

		RestTemplate restTemplate = new RestTemplate();

		UriComponentsBuilder uri = UriComponentsBuilder.fromUriString("https://mfahad88-eval-test.apigee.net/db_microbank/");
		BillInquiryResponse response = new BillInquiryResponse();
		BillInquiryRequest request = new BillInquiryRequest();

		if (messageVO.getCompanyCode().equalsIgnoreCase("4949")) {
			request.setCompanyCode(messageVO.getCompanyCode());
			request.setConsumerNumber(messageVO.getConsumerNo());
			response = restTemplate.postForObject(uri.build().toUri(), request, BillInquiryResponse.class);
			if (response != null) {
				messageVO.setResponseCode(response.getResponseCode());
				messageVO.setAmountDueDate(response.getBillAmount());
				messageVO.setBillStatus(response.getBillStatus());
				messageVO.setRrnKey(response.getCustomerMobile());
		
				try {
					messageVO.setBillDueDate(DateTools.stringToDate(response.getDueDate(), "yyMMdd"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}
				messageVO.setBillStatus(response.getBillStatus());

			}

		} else {
			BillInquiryRequest bir = transactionRequestBuilder.buildBillInquiryRequest(messageVO);

			// Build Message
			bir.assemble();
			messageVO.setStan(bir.getStan());

			if (networkInfoBean.isConnected()) {
				saveTransaction(bir, messageVO, true);
				middlewareClient.sendMessage(bir);
				poolHandler.checkResponsePool(messageVO);

			} else {
				saveTransaction(bir, messageVO, false);
				logger.debug("No connection found with Middleware Server => Sending 911 response Code");
				messageVO.setResponseCode("911");
				return messageVO;
			}
		}

		return messageVO;
	}

	public MiddlewareMessageVO titleFetch(MiddlewareMessageVO messageVO) throws RuntimeException {

		TitleFetchRequest tfr = transactionRequestBuilder.buildTitleFetchRequest(messageVO);

		// Build Message
		tfr.assemble();
		messageVO.setStan(tfr.getStan());

		if (networkInfoBean.isConnected()) {
			saveTransaction(tfr, messageVO, true);
			middlewareClient.sendMessage(tfr);
			poolHandler.checkResponsePool(messageVO);

		} else {
			saveTransaction(tfr, messageVO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			messageVO.setResponseCode("911");
			return messageVO;
		}
		return messageVO;
	}

	public MiddlewareMessageVO billPayment(MiddlewareMessageVO messageVO) {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(BASE_URL_PAYMENT);
		BillPaymentResponse response = new BillPaymentResponse();
		BillPaymentRequest request = new BillPaymentRequest();

		if (messageVO.getCompanyCode().equalsIgnoreCase("4949")) {
			request.setProductId(messageVO.getCompanyCode());
			request.setConsumerNumber(messageVO.getConsumerNo());
			request.setBillAmount(messageVO.getTransactionAmount());
			request.setCustomerMobile(messageVO.getRrnKey());
			response = restTemplate.postForObject(uri.build().toUri(), request, BillPaymentResponse.class);
			if (response != null) {
				messageVO.setResponseCode(response.getResponseCode());
			}

		} else {
			BillPaymentRequest bpr = transactionRequestBuilder.buildBillPaymentRequest(messageVO);

			// Build Message
			bpr.assemble();
			messageVO.setStan(bpr.getStan());

			if (networkInfoBean.isConnected()) {
				saveTransaction(bpr, messageVO, true);
				middlewareClient.sendMessage(bpr);
				poolHandler.checkResponsePool(messageVO);

			} else {
				saveTransaction(bpr, messageVO, false);
				logger.debug("No connection found with Middleware Server => Sending 911 response Code");
				messageVO.setResponseCode("911");
				return messageVO;
			}
		}

		return messageVO;
	}

	// Transfer-In - Debit Core Account
	public MiddlewareMessageVO fundTransfer(MiddlewareMessageVO messageVO) {
		FundTransferRequest ftr = transactionRequestBuilder.buildFundTransferRequest(messageVO);

		// Build Message
		ftr.assemble();
		messageVO.setStan(ftr.getStan());

		if (networkInfoBean.isConnected()) {
			saveTransaction(ftr, messageVO, true);
			middlewareClient.sendMessage(ftr);
			poolHandler.checkResponsePool(messageVO);

		} else {
			saveTransaction(ftr, messageVO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			messageVO.setResponseCode("911");
			return messageVO;
		}

		return messageVO;
	}

	// Transfer Out - Credit Core Account
	public MiddlewareMessageVO fundTransferAdvice(MiddlewareMessageVO messageVO) {
		FundTransferAdviceRequest ftar = transactionRequestBuilder.buildFundTransferAdviceRequest(messageVO);

		// Build Message
		ftar.assemble();
		messageVO.setStan(ftar.getStan());

		if (networkInfoBean.isConnected()) {
			saveTransaction(ftar, messageVO, true);
			middlewareClient.sendMessage(ftar);
			poolHandler.checkResponsePool(messageVO);

		} else {
			saveTransaction(ftar, messageVO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			messageVO.setResponseCode("911");
			return messageVO;
		}

		return messageVO;
	}

	public MiddlewareMessageVO acquirerReversalAdvice(MiddlewareMessageVO messageVO) {
		AcquirerReversalAdviceRequest reversalAdvice = transactionRequestBuilder.buildAcquirerReversalAdviceRequest(messageVO);

		// Build Message
		reversalAdvice.assemble();
		messageVO.setStan(reversalAdvice.getStan());

		if (networkInfoBean.isConnected()) {
			saveTransaction(reversalAdvice, messageVO, true);
			middlewareClient.sendMessage(reversalAdvice);
			poolHandler.checkResponsePool(messageVO);

		} else {
			saveTransaction(reversalAdvice, messageVO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			messageVO.setResponseCode("911");
			return messageVO;
		}

		return messageVO;
	}

	public MiddlewareMessageVO ibftTitleFetch(MiddlewareMessageVO messageVO) throws RuntimeException {
		IbftTitleFetchRequest ibftTitleFetchRequest = transactionRequestBuilder.buildIbftTitleFetchRequest(messageVO);

		// Build Message
		ibftTitleFetchRequest.assemble();
		messageVO.setStan(ibftTitleFetchRequest.getStan());

		if (networkInfoBean.isConnected()) {
			saveTransaction(ibftTitleFetchRequest, messageVO, true);
			middlewareClient.sendMessage(ibftTitleFetchRequest);
			poolHandler.checkResponsePool(messageVO);

		} else {
			saveTransaction(ibftTitleFetchRequest, messageVO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			messageVO.setResponseCode("911");
			return messageVO;
		}

		return messageVO;
	}


    public MiddlewareMessageVO ibftAdvice(MiddlewareMessageVO messageVO) throws RuntimeException {
        IbftAdviceRequest ibftAdviceRequest = transactionRequestBuilder.buildIbftAdviceRequest(messageVO);

        // Build Message
        ibftAdviceRequest.assemble();
        messageVO.setStan(ibftAdviceRequest.getStan());

        if (networkInfoBean.isConnected()) {
            saveTransaction(ibftAdviceRequest, messageVO, true);
            middlewareClient.sendMessage(ibftAdviceRequest);
            poolHandler.checkResponsePool(messageVO);

        } else {
            saveTransaction(ibftAdviceRequest, messageVO, false);
            logger.debug("No connection found with Middleware Server => Sending 911 response Code");
            messageVO.setResponseCode("911");
            return messageVO;
        }

        return messageVO;
    }

	/*
	 * SAVES TRANSACTION REQUEST PDU INTO THE DATABASE
	 */
	private void saveTransaction(BasePDU pdu, MiddlewareMessageVO messageVO, boolean isConnected) {

		TransactionLogModel transaction = new TransactionLogModel();
		BaseHeader requestHeader = pdu.getHeader();

		transaction.setRetrievalRefNo(messageVO.getRrnKey());
		transaction.setMessageType(requestHeader.getMessageType());
		transaction.setTransactionCode(pdu.getProcessingCode());
		transaction.setTransactionDateTime(new Date());
		transaction.setI8TransactionCode(messageVO.getMicrobankTransactionCode());
		transaction.setParentTransactionLogId(messageVO.getParentTransactionId());

		String requestPDUString;
		try {
			requestPDUString = new String(pdu.getRawPdu(), "UTF-8");
			transaction.setPduRequestString(requestPDUString);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		transaction.setPduRequestHEX(Hex.encodeHexString(pdu.getRawPdu()));

		if (isConnected) {
			transaction.setStatus(TransactionStatus.PROCESSING.getValue().longValue());
		} else {
			transaction.setStatus(TransactionStatus.REJECTED.getValue().longValue());
		}

		try {
			this.transactionDAO.save(transaction);
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		logger.debug("Transaction saved with RRN: " + transaction.getRetrievalRefNo());
		logger.debug("i8 Transaction Code: " + messageVO.getMicrobankTransactionCode());
	}

}
