package com.inov8.integration.server.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inov8.integration.middleware.util.ConfigReader;

import au.com.bytecode.opencsv.CSVReader;

public class InMemoryAccountsRepositry implements Serializable {

	private static final long serialVersionUID = 3120942275517121579L;

	private static Map<String, JDBCAccount> accounts = new HashMap<String, JDBCAccount>();

	private static InMemoryAccountsRepositry _INSTANCE;

	private InMemoryAccountsRepositry() {
		this.loadBankAccounts();
	}

	public static InMemoryAccountsRepositry getInstance() {
		if (_INSTANCE == null)
			_INSTANCE = new InMemoryAccountsRepositry();

		return _INSTANCE;
	}

	static {
		// Special Case

		JDBCAccount account = new JDBCAccount();
		account.setAccountNumber("4444000044440001");
		account.setAccountTitle("NAGINA ZAHOOR");
		account.setAccountStatus("00");
		account.setAccountBalance(261763.00);
		accounts.put("4444000044440000", account);

		account = new JDBCAccount();
		account.setAccountNumber("4444000044440002");
		account.setAccountTitle("NAGINA ZAHOOR");
		account.setAccountStatus("00");
		account.setAccountBalance(261763.00);
		accounts.put("4444000044440002", account);

		account = new JDBCAccount();
		account.setAccountNumber("4444000044440003");
		account.setAccountTitle("NAGINA ZAHOOR");
		account.setAccountStatus("00");
		account.setAccountBalance(261763.00);
		accounts.put("4444000044440003", account);

		account = new JDBCAccount();
		account.setAccountNumber("4444000044440004");
		account.setAccountTitle("NAGINA ZAHOOR");
		account.setAccountStatus("00");
		account.setAccountBalance(261763.00);
		accounts.put("4444000044440004", account);

		// Account 1
		account = new JDBCAccount();
		account.setAccountNumber("11120000000001");
		account.setAccountTitle("NAGINA ZAHOOR");
		account.setAccountStatus("04");
		account.setAccountBalance(261763.00);

		accounts.put("11120000000001", account);

		// Account 2
		account = new JDBCAccount();
		account.setAccountNumber("11120000000002");
		account.setAccountTitle("MALIK HUSSAIN");
		account.setAccountStatus("03");
		account.setAccountBalance(58355.00);

		accounts.put("11120000000002", account);

		// Account 3
		account = new JDBCAccount();
		account.setAccountNumber("11120000000003");
		account.setAccountTitle("MUHAMMAD UMAIR SHAHZAD");
		account.setAccountStatus("00");
		account.setAccountBalance(60562.00);

		accounts.put("11120000000003", account);

		// Account 4
		account = new JDBCAccount();
		account.setAccountNumber("11120000000004");
		account.setAccountTitle("AHMED NAJEEB KHAWAJA");
		account.setAccountStatus("00");
		account.setAccountBalance(205697.00);

		accounts.put("11120000000004", account);

		// Account 5
		account = new JDBCAccount();
		account.setAccountNumber("11120000000005");
		account.setAccountTitle("KHALEEQ AHMED");
		account.setAccountStatus("00");
		account.setAccountBalance(5432724.00);

		accounts.put("11120000000005", account);

		// Account 6
		account = new JDBCAccount();
		account.setAccountNumber("11120000000006");
		account.setAccountTitle("YASIN YUSUF");
		account.setAccountStatus("00");
		account.setAccountBalance(1812945.00);

		accounts.put("11120000000006", account);

		// Account 7
		account = new JDBCAccount();
		account.setAccountNumber("11120000000007");
		account.setAccountTitle("IMRAN KHAN");
		account.setAccountStatus("00");
		account.setAccountBalance(-15001.05);

		accounts.put("11120000000007", account);

		for (int i = 1; i <= 100; i++) {

			String accountNo = "1113000000000" + i;
			account = new JDBCAccount();
			account.setAccountNumber(accountNo);
			account.setAccountTitle("Account tile: " + i);
			account.setAccountStatus("00");

			account.setAccountBalance(500000.00);
			accounts.put(accountNo, account);

		}

		for (int i = 1; i <= 100; i++) {

			String accountNo = "111222333444" + i;
			account = new JDBCAccount();
			account.setAccountNumber(accountNo);
			account.setAccountTitle("Account tile: " + i);
			account.setAccountStatus("00");

			account.setAccountBalance(500000.00);
			accounts.put(accountNo, account);

		}

	}

	public void loadBankAccounts() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("accounts.csv");
		Reader reader = new InputStreamReader(inputStream);
		CSVReader csv = new CSVReader(reader);

		try {
			List list = csv.readAll();
			for (Object object : list) {
				if (object instanceof String[]) {
					String[] line = (String[]) object;
					JDBCAccount account = new JDBCAccount();
					account.setAccountNumber(line[0]);
					account.setAccountTitle(line[1]);
					account.setAccountBalance(Double.parseDouble(line[2]));
					account.setAccountStatus(line[3]);

					if (accounts.containsKey(account.getAccountNumber())) {
						accounts.remove(account.getAccountNumber());
					}

					accounts.put(account.getAccountNumber(), account);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JDBCAccount getAccountByAccountNo(String branchCode, String accountNo) {

		String fullAccountNo = branchCode + accountNo;
		return accounts.get(fullAccountNo);
	}

	public JDBCAccount getAccountByAccountNo(String accountNo) {

		if (ConfigReader.getInstance().getProperty("reload", "true", false).equals("true")) {
			this.loadBankAccounts();
		}

		return accounts.get(accountNo);
	}

}
