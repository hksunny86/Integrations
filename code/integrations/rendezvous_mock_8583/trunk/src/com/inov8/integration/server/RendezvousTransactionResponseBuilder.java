package com.inov8.integration.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jpos.q2.cli.SLEEP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.integration.middleware.enums.JSBankDefaultsEnum;
import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.response.AccountBalanceInquiryResponse;
import com.inov8.integration.middleware.pdu.response.AcquirerReversalAdviceResponse;
import com.inov8.integration.middleware.pdu.response.BillInquiryResponse;
import com.inov8.integration.middleware.pdu.response.BillPaymentResponse;
import com.inov8.integration.middleware.pdu.response.FundTransferAdviceResponse;
import com.inov8.integration.middleware.pdu.response.FundTransferResponse;
import com.inov8.integration.middleware.pdu.response.TitleFetchResponse;
import com.inov8.integration.middleware.persistance.MockTransactionDAO;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FormatUtils;
import com.inov8.integration.middleware.util.ISO8583Utils;
import com.inov8.integration.server.model.InMemoryAccountsRepositry;
import com.inov8.integration.server.model.JDBCAccount;

@Component
public class RendezvousTransactionResponseBuilder {

	@Autowired
	private MockTransactionDAO transactionDAO;

	private Logger logger = LoggerFactory.getLogger(RendezvousTransactionResponseBuilder.class.getSimpleName());
	private static Map<String, String> bills = new HashMap<String, String>();

	public RendezvousTransactionResponseBuilder() {
		this.loadBills();
	}

	public void loadBills() {

		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bills.csv");
			Reader reader = new InputStreamReader(inputStream);
			CSVReader csv = new CSVReader(reader);

			List list = csv.readAll();
			for (Object object : list) {
				if (object instanceof String[]) {
					String[] line = (String[]) object;
					if (bills.containsKey(line[0])) {
						bills.remove(line[0]);
					}
					bills.put(line[0], line[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FundTransferResponse fundTransferHandler(BasePDU requestPDU) {
		FundTransferResponse response = new FundTransferResponse();

		String accountNo = requestPDU.getAccountNo1();
		JDBCAccount account = transactionDAO.findAccount(accountNo);

		if (account != null) {

			if (account.getTtlRequest() > 0) {
				try {
					Thread.sleep(account.getTtlRequest());
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

			if (account.getAccountStatus().equals("00")) {
				double transactionAmount = Double
						.valueOf(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));

				boolean successful = transactionDAO.fundTransder(accountNo, transactionAmount);

				if (successful) {
					response.setResponseCode("00");
				} else {
					response.setResponseCode("16");
				}

			} else if (account.getAccountStatus().equals("420")) {
				// No Response to be returned
				return null;
			} else {
				response.setResponseCode(account.getAccountStatus());
			}
		} else {
			response.setResponseCode("10");
		}

		response.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		response.setProcessingCode(requestPDU.getProcessingCode());
		response.setStan(requestPDU.getStan());
		response.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		response.setTransactionLocalTime(
				DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		response.setTransactionLocalDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		response.setTransactionDate(requestPDU.getTransactionDate());
		response.setTransactionAmount(requestPDU.getTransactionAmount());
		response.setSettlementAmount(requestPDU.getSettlementAmount());
		response.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		response.setTransactionFee(requestPDU.getTransactionFee());
		response.setAcquirerIdentification(requestPDU.getAcquirerIdentification());

		return response;
	}

	public AcquirerReversalAdviceResponse acquirerReversalAdviceHandler(BasePDU requestPDU) {
		AcquirerReversalAdviceResponse adviceResponse = new AcquirerReversalAdviceResponse();
		adviceResponse.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		adviceResponse.setProcessingCode(requestPDU.getProcessingCode());
		adviceResponse.setStan(requestPDU.getStan());
		adviceResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		adviceResponse.setTransactionLocalTime(
				DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setTransactionLocalDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setTransactionDate(requestPDU.getTransactionDate());

		String accountNo = requestPDU.getAccountNo1();
		JDBCAccount account = transactionDAO.findAccount(accountNo);

//		if(true){
//			return null;
//		}

		if (account != null) {

			if (account.getAccountStatus().equals("00")) {
				try {
					if (account.getTtlRequest() > 0) {
						try {
							Thread.sleep(account.getTtlRequest());
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
					}

				} catch (Exception ex) {
					logger.error("ERROR while transfering balance.", ex);
				}
			} else if (account.getAccountStatus().equals("420")) {
				return null;
			}
		}

		adviceResponse.setResponseCode("00");

		adviceResponse.setTransactionAmount(requestPDU.getTransactionAmount());
		adviceResponse.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
		adviceResponse.setSettlementConversionRate(requestPDU.getSettlementConversionRate());
		adviceResponse.setConversionDate(requestPDU.getConversionDate());
		adviceResponse.setTransactionFee(requestPDU.getTransactionFee());
		adviceResponse.setSettlementFee(requestPDU.getSettlementFee());
		adviceResponse.setRrn(requestPDU.getRrn());
		adviceResponse.setTerminalId(requestPDU.getTerminalId());
		adviceResponse.setCardAcceptorName(requestPDU.getCardAcceptorName());
		adviceResponse.setTransactionCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());
		adviceResponse.setSettlementCurrencyCode(JSBankDefaultsEnum.PKR_CURRENCY.getValue());

		String privateDate = "";
		adviceResponse.setPrivateData(privateDate);
		return adviceResponse;
	}

	public FundTransferAdviceResponse fundTransferAdviceHandler(BasePDU requestPDU) {
		FundTransferAdviceResponse response = new FundTransferAdviceResponse();

		String accountNo = requestPDU.getAccountNo2();
		JDBCAccount account = transactionDAO.findAccount(accountNo);

		try {

			if (account != null) {

				if (account.getTtlRequest() > 0) {
					try {
						Thread.sleep(account.getTtlRequest());
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}

				if (account.getAccountStatus().equals("00")) {
					double transactionAmount = Double
							.valueOf(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));

					boolean successful = transactionDAO.fundTransderAdvice(accountNo, transactionAmount);

					if (successful) {
						response.setResponseCode("00");
					} else {
						response.setResponseCode("16");
					}

				} else if (account.getAccountStatus().equals("420")) {
					return null;
				} else {
					response.setResponseCode(account.getAccountStatus());
				}
			} else {
				response.setResponseCode("10");
			}

		} catch (Exception ex) {
			logger.error("ERROR while transfering balance.", ex);
		}

		response.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		response.setProcessingCode(requestPDU.getProcessingCode());
		response.setStan(requestPDU.getStan());
		response.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		response.setTransactionLocalTime(
				DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		response.setTransactionLocalDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		response.setTransactionDate(requestPDU.getTransactionDate());
		response.setTransactionAmount(requestPDU.getTransactionAmount());
		response.setTransactionFee(requestPDU.getTransactionFee());
		response.setSettlementAmount(requestPDU.getSettlementAmount());
		response.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));

		return response;
	}

	public TitleFetchResponse titleFetchHandler(BasePDU requestPDU) {
		String accountNo = requestPDU.getAccountNo1();
		TitleFetchResponse titleFetchResponse = new TitleFetchResponse();

		JDBCAccount account = transactionDAO.findAccount(accountNo);

		try {

			if (account != null) {

				if (account.getTtlRequest() > 0) {
					try {
						Thread.sleep(account.getTtlRequest());
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}

				if (account.getAccountStatus().equals("00")) {
					titleFetchResponse.setAdditionalResponseData(account.getAccountTitle());
					titleFetchResponse.setResponseCode("00");
				} else if (account.getAccountStatus().equals("420")) {
					return null;
				} else {
					titleFetchResponse.setResponseCode(account.getAccountStatus());
					titleFetchResponse.setAdditionalResponseData("X");
				}
			} else {
				titleFetchResponse.setResponseCode("10");
				titleFetchResponse.setAdditionalResponseData("X");
			}

		} catch (Exception ex) {
			logger.error("ERROR while transfering balance.", ex);
		}

		titleFetchResponse.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		titleFetchResponse.setProcessingCode(requestPDU.getProcessingCode());
		titleFetchResponse.setTransactionAmount(requestPDU.getTransactionAmount());
		titleFetchResponse.setTransactionDate(requestPDU.getTransactionDate());
		titleFetchResponse.setStan(requestPDU.getStan());
		titleFetchResponse.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		titleFetchResponse.setConversionDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		titleFetchResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		titleFetchResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());

		return titleFetchResponse;
	}

	public BillPaymentResponse billPaymentHandler(BasePDU requestPDU) {
		BillPaymentResponse adviceResponse = new BillPaymentResponse();

//		if(true){
//			return null;
//		}

		String accountNo = requestPDU.getAccountNo1();
//		JDBCAccount account = InMemoryAccountsRepositry.getInstance().getAccountByAccountNo(accountNo);
//		try {
//			double avalibleBalance = account.getAccountBalance();
//			double transactionAmount = Double.valueOf(FormatUtils.parseMiddlewareAmount(requestPDU.getTransactionAmount()));
//			if (avalibleBalance >= transactionAmount) {
//				double remaningBalance = avalibleBalance - transactionAmount;
//				account.setAccountBalance(remaningBalance);
//			}
//		} catch (Exception ex) {
//			logger.error("ERROR while transfering balance.", ex);
//		}

		adviceResponse.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		adviceResponse.setProcessingCode(requestPDU.getProcessingCode());
		adviceResponse.setStan(requestPDU.getStan());
		adviceResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		adviceResponse.setTransactionLocalTime(
				DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setTransactionLocalDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setTransactionDate(requestPDU.getTransactionDate());
		adviceResponse.setResponseCode("00");
		adviceResponse.setTransactionAmount(requestPDU.getTransactionAmount());
		adviceResponse.setSettlementAmount(requestPDU.getSettlementAmount());
		adviceResponse.setSettlementConversionRate(JSBankDefaultsEnum.SETTLEMENT_CONVERSION_RATE.getValue());
		adviceResponse.setConversionDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		adviceResponse.setSettlementFee(requestPDU.getSettlementAmount());
		adviceResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
		adviceResponse.setAuthIdResponse("");
		return adviceResponse;
	}

	public BillInquiryResponse billInquiryHandler(BasePDU requestPDU) {
		if (ConfigReader.getInstance().getProperty("reload", "true", false).equals("true")) {
			loadBills();
		}

		BillInquiryResponse inquiryResponse = new BillInquiryResponse();

		inquiryResponse.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		inquiryResponse.setProcessingCode(requestPDU.getProcessingCode());
		inquiryResponse.setStan(requestPDU.getStan());
		inquiryResponse.setTransactionAmount(requestPDU.getTransactionAmount());
		inquiryResponse.setSettlementAmount(requestPDU.getSettlementAmount());
		inquiryResponse.setTransactionDate(requestPDU.getTransactionDate());
		inquiryResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());
		inquiryResponse.setTransactionLocalTime(
				DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		inquiryResponse.setTransactionLocalDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		inquiryResponse.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		inquiryResponse.setConversionDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		inquiryResponse.setTransactionFee(ISO8583Utils.formatISO8583Amount(""));
		inquiryResponse.setSettlementFee(ISO8583Utils.formatISO8583Amount(""));
		inquiryResponse.setAcquirerIdentification(requestPDU.getAcquirerIdentification());
		inquiryResponse.setResponseCode("00");

		// Addtional data
		String privateData[] = requestPDU.getPrivateData().split(Pattern.quote("|"), -1);
		String cnic = privateData[0];
		String consumerNumber = privateData[1];
		String companyCode = privateData[2];

		String additionalData = null;

		// @formatter:off
		if (consumerNumber.equals("1700416100034")) {
			additionalData = "1700416100034|MR MUSHTAQ AHMED|KESC0001|T|1812|181231|+0000000210000|+0000000230000|01|";
		} else if (consumerNumber.equals("1700416100044")) {
			additionalData = "1700416100044|BILAL LASHARI|KESC0001|T|1411|141231|+0000000230000|+0000000250000|01|";
		} else if (consumerNumber.equals("1700416100050")) {
			additionalData = "1700416100050|SAIMA MOHSIN|KESC0001|T|1411|141231|+0000000220000|+0000000220000|01|";
		}
		//creditcard CCPY0001
		else if(consumerNumber.equals("0212653695222222")) {
			additionalData = "0212653695222222 |MEHVISH HIYAT|CCPY0001|T|1408|190306|+0000000790000|+0000000712000|01|";
		}
		// PTCL PTCL0010
		else if (consumerNumber.equals("2542653111")) {
			additionalData = "2542653111 |MEHVISH HIYAT|PTCL0010|T|1408|150228|+0000000790000|+0000000712000|01|";
		}
		// PTCL PTCL EVO Prepaid (PTCL0011)
		else if (consumerNumber.equals("0212653695")) {
			additionalData = "0212653695 |MAHNOOR BALOCH|PTCL0011|T|1408|150228|+0000000790000|+0000000590000|01|";
		}
		// PTCL PTCL0012
		else if (consumerNumber.equals("2222653697")) {
			additionalData = "2222653697 |SALMAN KHAN|PTCL0012|T|1408|150228|+0000000790000|+0000000330000|01|";
		} // PTCL Defaulter (PTCL0013)
		else if (consumerNumber.equals("2102653695")) {
			additionalData = "2102653695|AGHA NADEEM ZIA|PTCL0013|T|1211|160228|+0000000026000|+0000000027000|01|";
		} // PTCL Vfone (PTCL0015)
		else if (consumerNumber.equals("0512653695")) {
			additionalData = "0512653695|ALI KHAN|PTCL0015|T|1408|150228|+0000000790000|+0000000330000|01|";
		}

		else if (consumerNumber.equals("06121112345678")) {// GEPCO Upaid
			additionalData = "06121112345678|IMRAN KHAN|GEPCO001|U|1408|140916|+0000000210000|+0000000230000|01|";
		} else if (consumerNumber.equals("06121110043400")) {// GEPCO Paid
			additionalData = "06121110043400|BILAL AHMED|GEPCO001|P|1408|140912|+0000000230000|+0000000250000|01|";
		} else if (consumerNumber.equals("06121110044300")) {// GEPCO BLOCKED
			additionalData = "06121110044300|MOHSIN ALI|GEPCO001|B|1407|140810|+0000000220000|+0000000220000|01|";
		}

		else if (consumerNumber.equals("04111123456789")) {// LESCO001 Upaid
			additionalData = "04111123456789|AMJAD HUSSAIN|LESCO001|U|1408|140916|+0000000210000|+0000000230000|01|";
		} else if (consumerNumber.equals("04111102565009")) {// LESCO001 Paid
			additionalData = "04111102565009|GULLU BUTT|LESCO001|P|1408|140912|+0000000230000|+0000000250000|01|";
		} else if (consumerNumber.equals("04111102530010")) {// LESCO001 BLOCKED
			additionalData = "04111102530010|TAHIR UL QADRI|LESCO001|B|1407|140810|+0000000220000|+0000000220000|01|";
		}

		else if (consumerNumber.equals("0000123456")) {// SSGC0001 Upaid
			additionalData = "0000123456|ALTAF HUSSAIN|SSGC0001|U|1408|140916|+0000000210000|+0000000230000|01|";
		} else if (consumerNumber.equals("0000401000")) {// SSGC0001 Paid
			additionalData = "0000401000|MIRZA ASLAM|SSGC0001|P|1408|140912|+0000000230000|+0000000250000|01|";
		} else if (consumerNumber.equals("0001410000")) {// SSGC0001 BLOCKED
			additionalData = "0001410000|GHULAM ALI|SSGC0001|B|1407|140810|+0000000220000|+0000000220000|01|";
		}

		else if (consumerNumber.equals("10300012345")) {// SNGPL001 Upaid
			additionalData = "10300012345|NAUMAN SHEIKH|SNGPL001|U|1408|140916|+0000000210000|+0000000230000|01|";
		} else if (consumerNumber.equals("10300075792")) {// SNGPL001 Paid
			additionalData = "10300075792|WAJID ALI|SNGPL001|P|1408|140912|+0000000230000|+0000000250000|01|";
		} else if (consumerNumber.equals("10300075929")) {// SNGPL001 BLOCKED
			additionalData = "10300075929|BEGUM KALSOOM|SNGPL001|B|1407|140810|+0000000220000|+0000000220000|01|";
		}

		// Mobilink Prepay
		else if (consumerNumber.equals("03068911901")) {
			additionalData = "03068911901|PREPAID|MBLINK01|T|1408|      |+0000000000000|+0000000000000|01|";
		}
		// Mobilink Postpay
		else if (consumerNumber.equals("03040122214")) {
			additionalData = "03040122214|POSTPAID|MBLINK02||||||01|";
		}

		// Zong Prepay
		else if (consumerNumber.equals("03152653695")) {
			additionalData = "03152653695|PREPAID|ZONG0001|T|1408|      |+0000000000000|+0000000000000|01|";
		}
		// Zong Postpay
		else if (consumerNumber.equals("03162653695")) {
			additionalData = "03162653695|   |ZONG0002||||||01|";
		}

		// Warid Prepay
		else if (consumerNumber.equals("03213695895")) {
			additionalData = "03213695895|PREPAID|WARID001|T|1408|      |+0000000000000|+0000000000000|01|";
		}
		// Warid Postpay
		else if (consumerNumber.equals("03223695895")) {
			additionalData = "03223695895|   |WARID002||||||01|";
		}

		// Telenor Prepay
		else if (consumerNumber.equals("03452653695")) {
			additionalData = "03452653695|PREPAID|TELNOR01|T|1408|      |+0000000000000|+0000000000000|01|";
		}
		// Telenor Postpay
		else if (consumerNumber.equals("03462653695")) {
			additionalData = "03462653695|   |TELNOR02||||||01|";
		}

		// Ufone Prepay
		else if (consumerNumber.equals("03342653695")) {
			additionalData = "03342653695|PREPAID|UFONE001|T|1408|      |+0000000000000|+0000000000000|01|";
		}
		// Ufone Postpay
		else if (consumerNumber.equals("03352653695")) {
			additionalData = "03352653695|   |UFONE002||||||01|";
		}

		else if (bills.containsKey(consumerNumber)) {
			additionalData = bills.get(consumerNumber);
		}

		else {
			inquiryResponse.setResponseCode("82");
			additionalData = "" + cnic + "|   |" + companyCode + "||||||01|";
		}

		// @formatter:on
		inquiryResponse.setAdditionalResponseData(additionalData);
		return inquiryResponse;
	}

	public AccountBalanceInquiryResponse balanceInquiryHandler(BasePDU requestPDU) {
		String accountNo = requestPDU.getAccountNo1();
		JDBCAccount account = InMemoryAccountsRepositry.getInstance().getAccountByAccountNo(accountNo);

		AccountBalanceInquiryResponse balanceInquiryResponse = new AccountBalanceInquiryResponse();

		balanceInquiryResponse.setPan(requestPDU.getPan() == null ? "1234123412341234" : requestPDU.getPan());
		balanceInquiryResponse.setProcessingCode(requestPDU.getProcessingCode());
		balanceInquiryResponse.setTransactionAmount(requestPDU.getTransactionAmount());
		balanceInquiryResponse.setSettlementAmount(requestPDU.getSettlementAmount());
		balanceInquiryResponse.setTransactionDate(requestPDU.getTransactionDate());
		balanceInquiryResponse.setStan(requestPDU.getStan());
		balanceInquiryResponse.setTransactionLocalTime(
				DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		balanceInquiryResponse.setTransactionLocalDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		balanceInquiryResponse.setSettlementDate(
				DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		balanceInquiryResponse.setMerchantType(requestPDU.getMerchantType());
		balanceInquiryResponse.setNetworkIdentifier(JSBankDefaultsEnum.NETWORK_IDENTIFIER.getValue());

		if (account == null) {
			balanceInquiryResponse.setResponseCode("10");
			// balanceInquiryResponse.setAdditionalAmount(ISO8583Utils.formatISO8583Amount(""));
		} else if (!account.getAccountStatus().equals("00")) {
			balanceInquiryResponse.setResponseCode(account.getAccountStatus());
			// balanceInquiryResponse.setAdditionalAmount(ISO8583Utils.formatISO8583Amount(""));
		} else {
			balanceInquiryResponse.setResponseCode("00");

			String balance = "2002586C" + ISO8583Utils.formatISO8583Amount(account.getAccountBalance().toString());
			if (account.getAccountBalance() < 0.0) {
				balance = "2002586D" + ISO8583Utils.formatISO8583Amount(Math.abs(account.getAccountBalance()) + "");
			} else {
				balance = "2002586C" + ISO8583Utils.formatISO8583Amount(account.getAccountBalance() + "");
			}

			balanceInquiryResponse.setAdditionalAmount(balance);
		}

		return balanceInquiryResponse;
	}

}
