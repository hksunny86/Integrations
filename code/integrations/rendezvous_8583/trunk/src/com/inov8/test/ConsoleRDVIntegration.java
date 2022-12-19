package com.inov8.test;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.integration.middleware.controller.MiddlewareSwitchController;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.vo.MiddlewareMessageVO;

@SuppressWarnings("all")
public class ConsoleRDVIntegration {
	private static Logger logger = LoggerFactory.getLogger(ConsoleRDVIntegration.class);
	private static MiddlewareSwitchController controller = null;
	private static final int MAX_LOGINS = 3;

	// private static String server = "127.0.0.1:8080";

	private static String readLine(String format, Object... args) throws IOException {
		if (System.console() != null) {
			return System.console().readLine(format, args);
		}
		System.out.print(String.format(format, args));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}

	private static char[] readPassword(String format, Object... args) throws IOException {
		if (System.console() != null)
			return System.console().readPassword(format, args);
		return readLine(format, args).toCharArray();
	}

	public static void main(String[] args) {
		String fmt = "%1$4s %2$5s %3$10s%n";

		System.out.println("########################################");
		System.out.println("Welcome to Middleware Integration Simulator.");
		System.out.println("########################################");

		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		boolean incorrectPwd = true;
		int count = 0;
		Console con = System.console();
		try {
			if (con == null) {
				System.out.println("Enter Integration Server IP:Port ");
				String server = bufferRead.readLine();
				if (StringUtils.isEmpty(server)) {
					server = "127.0.0.1:8080";
				}
				getFromProxy(server);
				phoenixInteractive();
			} else {

				while (con != null && incorrectPwd && count++ < MAX_LOGINS) {

					System.out.println("Enter Integration Server IP:Port ");
					String server = bufferRead.readLine();
					char[] pwd = con.readPassword("Enter %s password: ", "");
					String pwdString = new String(pwd);
					// char[] pwdConfirm = {'l','a','h','o','r','e'};
					if (pwdString.equals("inov8Pak123")) {
						incorrectPwd = false;
						getFromProxy(server);
						phoenixInteractive();
					} else {
						System.out.println("Incorrect Password, try again\n");
						incorrectPwd = true;
					}

				}
			}
		} catch (IOException e) {
			logger.error("Exception",e);
		} catch (Exception e) {
			logger.error("Exception",e);
		}
	}

	public static void phoenixInteractive() {

		boolean flag = true;
		while (flag) {
			System.out.println("Transaction Menu: ");
			System.out.println("\t1. Title Fetch");
			System.out.println("\t2. Transfer In");
			System.out.println("\t3. Bill Inquiry ");
			System.out.println("\t4. Bill Payment Advice");
			System.out.println("\t5. Transfer OUT Advice");
			System.out.println("\t6. Acquirer Reversal Advice");
			System.out.println("\t7. Account Balance");
			System.out.println("\t0. Exit");

			int option;
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String str = "";
			try {
				str = bufferRead.readLine();
			} catch (IOException e) {
				logger.error("Exception",e);
			}

			option = Integer.parseInt(str);

			try {
				switch (option) {
				case 0: {
					flag = false;
					bufferRead.close();
					System.out.println("Goodbye...");
					System.exit(1);
				}
				case 1: {
					System.out.println("You are in Title Fetch");
					System.out.print("Enter Acc#: ");
					String fromAccount = bufferRead.readLine();
					for(int i=0;i<=599;i++)
					{
					Thread t=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub

							
							doTitleFetch("144444444421");
							doTransferIN("144444444421", "144444444421", "1111");
							billInquiry("1700000000034", "1700000000034", "144444444421", "1700000000034");
							billPayment("1700000000034", "144444444421", "200", "1700000000034", "1700000000034", "111222", "111", "1700000000034", "11");
							doTransferOUTAdvice("144444444421", "144444444421", "200");
							doAcquirerReversalAdvice("1111", "112365");
							doAccountBalance("144444444421");
						}
					});
					t.start();
					}
					flag = askMore(bufferRead);
				}
					break;
				case 2: {
					System.out.println("Transfer In");
					System.out.print("Enter JSBL Acc#: ");
					String account1 = bufferRead.readLine();

					System.out.print("Enter BB Acc#: ");
					String account2 = bufferRead.readLine();

					System.out.print("Enter Amount: ");
					String amount = bufferRead.readLine();

					doTransferIN(account1, account2, amount);
					flag = askMore(bufferRead);
				}
					break;
				case 3: {

					System.out.println("You are in Utility Bill Inquiry");

					System.out.println("*** ENTER NIC *** ");
					String nicNo = bufferRead.readLine();

					System.out.println("*** ENTER UTILITY COMPANY ID *** ");
					String utilityCompany = bufferRead.readLine();

					System.out.print("Enter Consumer ID: ");
					String consumerNo = bufferRead.readLine();

					System.out.print("Enter Account No: ");
					String accountNo = bufferRead.readLine();

					billInquiry(consumerNo, utilityCompany, accountNo, nicNo);

					flag = askMore(bufferRead);
				}
					break;
				case 4: {

					System.out.println("You are in Bill Payment");

					System.out.print("Enter CNIC: ");
					String cnic = bufferRead.readLine();

					System.out.print("Enter Company Code: ");
					String companyCode = bufferRead.readLine();

					System.out.print("Enter Consumer ID: ");
					String consumerNo = bufferRead.readLine();

					System.out.print("Enter Consumer Name: ");
					String consumerName = bufferRead.readLine();

					System.out.print("Enter Account Number: ");
					String accNo = bufferRead.readLine();

					System.out.print("Enter Amount Paid: ");
					String amount = bufferRead.readLine();

					System.out.print("Enter Due Date: ");
					String dueDate = bufferRead.readLine();

					System.out.print("Enter Aggregator Code: ");
					String aggCode = bufferRead.readLine();
					
					System.out.print("Enter Bill Category Code: ");
					String catCode = bufferRead.readLine();

					billPayment(consumerNo, accNo, amount, companyCode, cnic, dueDate, aggCode, consumerName, catCode);

					flag = askMore(bufferRead);
				}
					break;
				case 5: {

					System.out.println("Transfer Out");

					System.out.print("Enter BB Acc#: ");
					String bbAccount = bufferRead.readLine();

					System.out.print("Enter JSBL Acc#: ");
					String jsblAccount = bufferRead.readLine();

					System.out.print("Enter Amount: ");
					String amount = bufferRead.readLine();

					doTransferOUTAdvice(bbAccount, jsblAccount, amount);

					flag = askMore(bufferRead);
				}
					break;
				case 6: {

					System.out.println("You are in Acquirer Reversal Advice");

					System.out.print("Enter from Reversal STAN: ");
					String stan = bufferRead.readLine();

					System.out.print("Enter to Request Date Time: ");
					String dateTime = bufferRead.readLine();

					doAcquirerReversalAdvice(stan, dateTime);

					flag = askMore(bufferRead);
				}
					break;
				case 7: {

					System.out.println("You are in Account Balance Inquiry");

					System.out.print("Enter Account Number ");
					String accountNumber = bufferRead.readLine();

					doAccountBalance(accountNumber);

					flag = askMore(bufferRead);
				}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				logger.error("Exception",e);
			}

		}

	}

	private static void doAccountBalance(String accountNumber) {
		MiddlewareMessageVO message = new MiddlewareMessageVO();

		message.setAccountNo1(accountNumber);

		MiddlewareMessageVO responseVO = (MiddlewareMessageVO) controller.accountBalanceInquiry(message);

		if (responseVO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR Balance Check *********");
			logger.info("RRN: " + responseVO.getRetrievalReferenceNumber());
			logger.info("Balance: " + responseVO.getAccountBalance());
			logger.info("STAN: "+responseVO.getStan());
		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode());
			logger.info("STAN: "+responseVO.getStan());
		}

	}

	private static void doAcquirerReversalAdvice(String stan, String dateTime) {
		MiddlewareMessageVO message = new MiddlewareMessageVO();

		message.setReversalSTAN(stan);
		message.setReversalRequestTime(dateTime);

		MiddlewareMessageVO responseVO = (MiddlewareMessageVO) controller.acquirerReversalAdvice(message);

		if (responseVO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR ACQUIRER REVERSAL ADVICE *********");
			logger.info("RRN: " + responseVO.getRetrievalReferenceNumber());

		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode());
		}

	}

	private static void doTransferOUTAdvice(String bbAccountNumber, String jsblAccountNumber, String amount) {
		MiddlewareMessageVO message = new MiddlewareMessageVO();

		message.setAccountNo1(bbAccountNumber);
		message.setAccountNo2(jsblAccountNumber);
		message.setTransactionAmount(amount);

		MiddlewareMessageVO responseVO = (MiddlewareMessageVO) controller.fundTransferAdvice(message);

		if (responseVO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR FUND TRANSFER ADVICE *********");
			logger.info("RRN: " + responseVO.getRetrievalReferenceNumber());

		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode());
		}

	}

	public static void doTransferIN(String jsblAccountNumber, String bbAccountNumber, String amount) {
		MiddlewareMessageVO message = new MiddlewareMessageVO();

		message.setAccountNo1(jsblAccountNumber);
		message.setAccountNo2(bbAccountNumber);
		message.setTransactionAmount(amount);

		MiddlewareMessageVO responseVO = (MiddlewareMessageVO) controller.fundTransfer(message);

		if (responseVO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR ACCOUNT FUND TRANSFER *********");
			logger.info("RRN: " + responseVO.getRetrievalReferenceNumber());

		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode());
		}

	}

	public static void doTitleFetch(String accountNumber) {
		MiddlewareMessageVO responseVO = new MiddlewareMessageVO();

		responseVO.setAccountNo1(accountNumber);

		responseVO = (MiddlewareMessageVO) controller.titleFetch(responseVO);

		if (responseVO.getResponseCode().equals("00")) {
			logger.info("******* DEBUG LOGS FOR TITLE FETCH *********");
			logger.info("Account Number: " + responseVO.getAccountNo1());
			logger.info("Account Title: " + responseVO.getAccountTitle());
		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode());
		}
	}

	public static void billInquiry(String consumerNo, String utilityCompany, String accountNo, String nicNo) {
		MiddlewareMessageVO responseVO = new MiddlewareMessageVO();

		responseVO.setCnicNo(nicNo);
		responseVO.setConsumerNo(consumerNo);
		responseVO.setCompnayCode(utilityCompany);
		responseVO.setAccountNo1(accountNo);

		responseVO = (MiddlewareMessageVO) controller.billInquiry(responseVO);

		logger.info("RRN: " + responseVO.getRetrievalReferenceNumber());
		if (responseVO.getResponseCode().equals("00")) {
			logger.info("******* DEBUG LOGS FOR BILL INQUIRY *********");
			logger.info("ResponseCode: " + responseVO.getResponseCode() + responseVO.getConsumerName());
		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode() + responseVO.getConsumerNo());
			
		}
	}

	public static void billPayment(String consumerNo, String accountNumber, String amount, String companyCode, String cnic, String dueDate, String aggCode,
			String consumerName, String catCode) {
		MiddlewareMessageVO responseVO = new MiddlewareMessageVO();

		responseVO.setAccountNo1(accountNumber);
		responseVO.setCnicNo(cnic);
		responseVO.setConsumerNo(consumerNo);
		responseVO.setCompnayCode(companyCode);
		responseVO.setTransactionAmount(amount);
		responseVO.setConsumerName(consumerName);
		responseVO.setBillCategoryId(catCode);
		try {
			responseVO.setBillDueDate(DateTools.stringToDate(dueDate, "yyMMdd"));
		} catch (ParseException e) {
			logger.error("Exception",e);
		}
		responseVO.setBillAggregator(aggCode);

		responseVO = (MiddlewareMessageVO) controller.billPayment(responseVO);

		logger.info("RRN: " + responseVO.getRetrievalReferenceNumber());
		if (responseVO.getResponseCode().equals("00")) {
			logger.info("******* DEBUG LOGS FOR BILL Payment *********");
			logger.info(responseVO.toString());
		} else {
			logger.info("ResponseCode: " + responseVO.getResponseCode());
		}
	}

	public static boolean askMore(BufferedReader bufferRead) throws Exception {
		System.out.println("If you want to continue, enter 1");
		System.out.println("OR");
		System.out.println("enter 0 to exit the System");
		String str = bufferRead.readLine();
		int option = Integer.parseInt(str);
		if (option == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static void getFromContext() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		controller = (MiddlewareSwitchController) context.getBean("middlewareswitch");
	}

	public static void getFromProxy(String server) throws Exception {
		HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
		httpInvokerProxyFactoryBean.setServiceInterface(MiddlewareSwitchController.class);
		httpInvokerProxyFactoryBean.setServiceUrl("http://" + server + "/middleware-integration/ws/middlewareswitch");
		httpInvokerProxyFactoryBean.afterPropertiesSet();
		controller = (MiddlewareSwitchController) httpInvokerProxyFactoryBean.getObject();
	}
}