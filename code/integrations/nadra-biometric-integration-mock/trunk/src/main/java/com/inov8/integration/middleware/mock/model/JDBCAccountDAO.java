package com.inov8.integration.middleware.mock.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCAccountDAO {
	private Connection connection;

	public JDBCAccountDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean fundTransder(String fromAccount, String toAccount, double amount) {
		String debit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance - ? WHERE account_number = ?";
		String credit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance - ? WHERE account_number = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(debit);
			stmt.setString(1, fromAccount);
			boolean status = stmt.execute();
			System.out.println("Account Debit: " + status);

			stmt = connection.prepareStatement(credit);
			stmt.setString(1, toAccount);
			boolean statusCr = stmt.execute();
			System.out.println("Account Credit: " + statusCr);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public JDBCAccount findAccount(String accountNo) {
		JDBCAccount account = new JDBCAccount();
		String query = "SELECT * FROM MOCK_ACCOUNT WHERE ACCOUNT_NUMBER=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, accountNo);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				account.setAccountTitle(rs.getString("ACCOUNT_TITLE"));
				account.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
				account.setAccountType(rs.getString("ACCOUNT_TYPE"));
				account.setAccountBalance(rs.getDouble("ACCOUNT_BALANCE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}
}
