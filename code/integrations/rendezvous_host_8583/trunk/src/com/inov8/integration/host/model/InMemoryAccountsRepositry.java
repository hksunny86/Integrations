package com.inov8.integration.host.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountsRepositry implements Serializable{

	private static final long serialVersionUID = 3120942275517121579L;
	
	private static Map<String, JDBCAccount> accounts = new HashMap<String, JDBCAccount>();
	
	static{
		
		// Account 1
		JDBCAccount account = new JDBCAccount();
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
	}
	
	public static JDBCAccount getAccountByAccountNo(String branchCode, String accountNo){
		String fullAccountNo = branchCode + accountNo;
		return accounts.get(fullAccountNo);
	}
	
	public static JDBCAccount getAccountByAccountNo(String accountNo){
		return accounts.get(accountNo);
	}

}
