package com.inov8.integration.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.FormatUtils;
import com.inov8.integration.server.model.JDBCAccount;
import com.inov8.integration.server.model.JDBCBill;
import com.inov8.integration.server.model.TransactionDetail;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class RendezvousConnectionManager {
	private static RendezvousConnectionManager INSTANCE;
	// private static Connection connection = null;
	private static BoneCP connectionPool = null;
	private static Logger logger = LoggerFactory.getLogger(RendezvousConnectionManager.class.getSimpleName());

	public static void cleanup() {
		try {
			if (connectionPool != null)
				connectionPool.shutdown();
			System.out.println("Connection Pooling shutdown");
		} catch (Exception e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while shuting down Connection Pooling", e);
		}
	}

	private RendezvousConnectionManager() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// setup the connection pool

			ConfigReader reader = ConfigReader.getInstance();
			String dbserver = reader.getProperty("db.server", "10.0.1.54");
			String dbUsername = reader.getProperty("db.username", "I8_PHOENIX_INTEGRATION_QA");
			String dbPassword = reader.getProperty("db.password", "I8_PHOENIX_INTEGRATION_QA");
			String dbSID = reader.getProperty("db.sid", "DEV");

			BoneCPConfig config = new BoneCPConfig();
			// StringBuilder stringBuilder = new
			// StringBuilder("jdbc:oracle:oci:@dev");

			StringBuilder stringBuilder = new StringBuilder("jdbc:oracle:thin:@");
			stringBuilder.append(dbserver);
			stringBuilder.append(":");
			stringBuilder.append("1521");
			stringBuilder.append(":");
			stringBuilder.append(dbSID);

			System.out.println("Database URL: " + stringBuilder.toString());
			System.out.println("USERNAME: " + dbUsername);
			System.out.println("PASSWORD: " + dbPassword);

			System.out.println("Database URL: " + stringBuilder.toString());
			System.out.println("USERNAME: " + dbUsername);
			System.out.println("PASSWORD: " + dbPassword);

			// config.setJdbcUrl("jdbc:oracle:thin:@10.0.1.54:1521:DEV");
			config.setJdbcUrl(stringBuilder.toString());
			config.setUsername(dbUsername);
			config.setPassword(dbPassword);
			
			// config.setUsername("I8_PHOENIX_INTEGRATION_DEV");
			// config.setPassword("I8_PHOENIX_INTEGRATION_DEV");

			config.setMinConnectionsPerPartition(5);
			config.setMaxConnectionsPerPartition(10);
			config.setPartitionCount(2);
			config.setAcquireIncrement(1);
			// config.setDefaultAutoCommit(true);
			config.setInitSQL("SELECT 1 FROM DUAL");
			// setup the connection pool
			connectionPool = new BoneCP(config);

		} catch (Exception e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while Connection Pooling", e);
		}
	}

	public static RendezvousConnectionManager instance() {
		if (INSTANCE == null) {
			INSTANCE = new RendezvousConnectionManager();
		}
		return INSTANCE;
	}

	public int updateResponseCode(String rrn, String responseCode) {
		String query = "UPDATE RRN_RESPONSE_SEACRH SET response_code = ? WHERE rrn = ?";
		int count = 0;
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, responseCode);
			stmt.setString(2, rrn);
			count = stmt.executeUpdate();
			stmt.clearParameters();
			connection.commit();
		} catch (SQLException e) {
			logger.error("Exception",e);
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Exception",e);
			}
		}
		return count;
	}

	private void close(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("Exception",e);
		}
	}

	public int fundTransder(String fromAccount, String toAccount, double amount) {
		String from_debit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance - ? WHERE account_number = ?";
		String to_credit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance + ? WHERE account_number = ?";
		Connection connection = null;
		try {

			JDBCAccount account = findAccount(fromAccount);
			if (account != null && account.getAccountBalance() < amount) {
				return -1;
			}
			connection = connectionPool.getConnection();

			PreparedStatement stmt = connection.prepareStatement(from_debit);
			stmt.setDouble(1, amount);
			stmt.setString(2, fromAccount);
			stmt.execute();
			System.out.println("Account Debited ");

			stmt = connection.prepareStatement(to_credit);
			stmt.setDouble(1, amount);
			stmt.setString(2, toAccount);
			stmt.execute();
			System.out.println("Account Credited");
			connection.commit();
			return 0;
		} catch (SQLException e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while Funds Transfer", e);
			return -99;
		} finally {
			close(connection);
		}
	}

	public int fundTransder(String fromAccount, String toAccount, double fromAmount, double toAmount) {
		String from_debit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance - ? WHERE account_number = ?";
		String to_credit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance + ? WHERE account_number = ?";
		Connection connection = null;
		try {

			JDBCAccount account = findAccount(fromAccount);
			if (account != null && account.getAccountBalance() < fromAmount) {
				return -1;
			}
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(from_debit);
			stmt.setDouble(1, fromAmount);
			stmt.setString(2, fromAccount);
			stmt.execute();
			System.out.println("Account Debited ");

			stmt = connection.prepareStatement(to_credit);
			stmt.setDouble(1, toAmount);
			stmt.setString(2, toAccount);
			stmt.execute();
			System.out.println("Account Credited");
			connection.commit();
			return 0;
		} catch (SQLException e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while Funds Transfer", e);
			return -1;
		} finally {
			close(connection);
		}
	}

	public JDBCAccount findAccount(String accountNo) {
		JDBCAccount account = null;
		String query = "SELECT * FROM MOCK_ACCOUNT WHERE ACCOUNT_NUMBER=?";
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, accountNo.trim());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				account = new JDBCAccount();
				account.setAccountTitle(rs.getString("ACCOUNT_TITLE"));
				account.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
				account.setAccountType(rs.getString("ACCOUNT_TYPE"));
				account.setAccountBalance(rs.getDouble("ACCOUNT_BALANCE"));
				account.setAccountType(rs.getString("ACCOUNT_TYPE"));
				account.setAccountStatus(rs.getString("ACCOUNT_STATUS"));
				account.setTtlRequest(rs.getLong("TTL"));
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while Fetching Acnt. Details", e);
		} finally {
			close(connection);
		}
		return account;
	}

	public JDBCBill findBill(String consumerNo) {
		JDBCBill bill = null;
		;
		String query = "SELECT * FROM MOCK_BILL ";
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);
			// stmt.setString(1, consumerNo);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				bill = new JDBCBill();
				bill.setConsumerNumber(rs.getString("CONSUMER_NUMBER"));
				bill.setSubscriberName(rs.getString("SUBSCRIBER_NAME"));
				bill.setBillingMonth(rs.getString("BILLING_MONTH"));
				bill.setDueDatePayableAmount(rs.getString("DUEDATEPAYABLE"));
				bill.setPaymentDueDate(rs.getString("PAYMENTDUEDATE"));
				bill.setPaymentAfterDueDate(rs.getString("PAYMENTAFTERDUEDATE"));
				bill.setBillStatus(rs.getString("BILL_STATUS"));
				bill.setPaymentAuthResponseId(rs.getString("PAYMENT_AUTH_RESPONSE_ID"));
				bill.setNetCED(rs.getString("NET_CED"));
				bill.setNetWithholdingTAX(rs.getString("NET_WITHHOLDING"));
				bill.setResponseCode(rs.getString("RESPONSE_CODE"));
				bill.setTtlRequest(rs.getLong("TTL"));
			}
		} catch (Exception e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while Fetching Bill Details", e);
		} finally {
			close(connection);
		}
		return bill;
	}

	public boolean doBillPayment(String consumerNo, String fromAccount, Double amount) {
		String debitAccount = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance - ? WHERE account_number = ?";
		String billUpdate = "UPDATE MOCK_BILL SET BILL_STATUS = 'P' WHERE CONSUMER_NUMBER = ?";
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(debitAccount);
			stmt.setDouble(1, amount);
			stmt.setString(2, fromAccount);
			stmt.execute();
			System.out.println("Account Debit ");

			stmt = connection.prepareStatement(billUpdate);
			stmt.setString(1, consumerNo);
			stmt.execute();
			System.out.println("Bill Paid");
			connection.commit();
			return true;
		} catch (SQLException e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured in Bill Payment", e);
			return false;
		} finally {
			close(connection);
		}
	}

	public String getTransactions(String accountNumber) {
		String sql = "select * from (select TRANSACTION_ID, AMOUNT, STAN, TRANSACTION_TYPE, TRANSACTION_DATE from TRANSACTIONS where ACCOUNT_NUMBER = ? order by TRANSACTION_DATE desc) where rownum < 11 ";
		TransactionDetail transactionDetail = new TransactionDetail();
		Connection connection = null;
		String transactionBlock = "";
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, accountNumber.trim());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				transactionDetail.setId(rs.getString("TRANSACTION_ID"));
				transactionDetail.setAmount(FormatUtils.parseMiddlewareAmount(rs.getString("AMOUNT")));
				transactionDetail.setStan(rs.getString("STAN"));
				transactionDetail.setType(rs.getString("TRANSACTION_TYPE"));
				transactionDetail.setDate(rs.getDate("TRANSACTION_DATE").toString());
				transactionBlock += transactionDetail.toString();
			}
			return transactionBlock;
		} catch (SQLException e) {
			logger.error("[MIDDLEWARE MOCK] Exceptions Occured while getting Tx's", e);
			return null;
		} finally {
			close(connection);
		}
	}

	public static void main(String[] args) throws Exception {
		System.load("D:\\app\\FaisalBa\\product\\11.2.0\\client_1\\BIN\\ocijdbc11.dll");
		System.load("D:\\app\\FaisalBa\\product\\11.2.0\\client_1\\BIN\\heteroxa11.dll");

		RendezvousConnectionManager manager = new RendezvousConnectionManager();
		JDBCBill bill = manager.findBill("99");
		System.out.println(bill.getSubscriberName());
		manager.doBillPayment(bill.getConsumerNumber(), "123", Double.parseDouble(bill.getDueDatePayableAmount()));

		// ExecutorService executor = Executors.newFixedThreadPool(10);
		// for (int i = 0; i < 50000; i++) {
		// Runnable worker = new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// Class.forName("oracle.jdbc.driver.OracleDriver");
		// } catch (ClassNotFoundException e1) {
		// e1.printStackTrace();
		// }
		// try {
		// Connection connection =
		// DriverManager.getConnection("jdbc:oracle:thin:@10.0.1.54:1521:DEV",
		// "I8_PHOENIX_INTEGRATION_QA",
		// "I8_PHOENIX_INTEGRATION_QA");
		// System.out.println("runing..");
		// } catch (SQLException e) {
		// logger.error("Exception",e);
		// }
		//
		// }
		// };
		// executor.execute(worker);
		// }
		// // This will make the executor accept no new threads
		// // and finish all existing threads in the queue
		// executor.shutdown();
		// // Wait until all threads are finish
		// while (!executor.isTerminated()) {
		//
		// }
		System.out.println("Finished all threads");

		// new OracleConnectionManager();
		//
		// StringBuilder sql = new StringBuilder();
		// sql.append("INSERT INTO TRANSACTION_LOG ");
		// sql.append("(TRANSACTION_LOG_ID, TRANSACTION_DATE) ");
		// sql.append("VALUES (transaction_log_seq.nextval,?)");
		//
		// // Statement stmt = connection.createStatement();
		// // int count = stmt.executeUpdate(sql.toString());
		//
		// PreparedStatement pstmt =
		// connection.prepareStatement(sql.toString());
		// pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
		//
		// pstmt.executeUpdate();

		// String sql = "SELECT * FROM TRANSACTION_LOG WHERE RRN=123";
		//
		// Statement stmt = connection.createStatement();
		// ResultSet rs = stmt.executeQuery(sql);
		// Date date = null;
		// while (rs.next()) {
		// date = rs.getTimestamp("TRANSACTION_DATE");
		// }
		// DateTime dateTime = new DateTime(date);
		// DateTime rdateTime = new DateTime();
		// PeriodFormatter formatter = new PeriodFormatterBuilder()
		// .minimumPrintedDigits(2)
		// .printZeroAlways()
		// .appendHours()
		// .appendSeparator(":")
		// .appendMinutes()
		// .appendSeparator(":")
		// .appendSecondsWithMillis()
		// .toFormatter();
		// Interval interval = new Interval(dateTime, rdateTime);
		// System.out.println(dateTime);
		// System.out.println(rdateTime);
		// System.out.println( (interval.toDurationMillis() ) );
		//
		//
		// PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
		// .appendDays()
		// .appendSuffix(" day", " days")
		// .appendSeparator(" and ")
		// .appendMinutes()
		// .appendSuffix(" minute", " minutes")
		// .appendSeparator(" and ")
		// .appendSeconds()
		// .appendSuffix(" second", " seconds")
		// .toFormatter();
		//
		// Period period = new Period(72, 24, 12, 0);
		//
		// System.out.println(daysHoursMinutes.print(period));
		// System.out.println(daysHoursMinutes.print(period.normalizedStandard()));
		//
		// Period p = new Period(dateTime, rdateTime);
		// System.out.println(formatter.print(p));
		//
		//
		// Period period1 = new Period();
		// // prints 00:00:00
		// System.out.println(String.format("%02d:%02d:%02d", period.getHours(),
		// period.getMinutes(), period.getSeconds()));
		// period = period.plusSeconds(60 * 60 * 12);
		// // prints 00:00:43200
		// System.out.println(String.format("%02d:%02d:%02d", period.getHours(),
		// period.getMinutes(), period.getSeconds()));
		// period = period.normalizedStandard();
		// // prints 12:00:00
		// System.out.println(String.format("%02d:%02d:%02d", period.getHours(),
		// period.getMinutes(), period.getSeconds()));
		//
		// PeriodFormatter daysHoursMinutes1 = new PeriodFormatterBuilder()
		// .appendDays()
		// .appendSuffix(" day", " days")
		// .appendSeparator(" and ")
		// .appendMinutes()
		// .appendSuffix(" minute", " minutes")
		// .appendSeparator(" and ")
		// .appendSeconds()
		// .appendSuffix(" second", " seconds")
		// .toFormatter();

		// System.out.println(connection.isClosed());
		// connection.close();
	}
}
