package com.inov8.integration.middleware.persistance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inov8.integration.server.model.JDBCAccount;
import com.inov8.integration.server.model.JDBCBill;
import com.inov8.integration.server.model.JDBCCard;
import com.inov8.integration.server.model.JDBCStatement;
import com.inov8.integration.server.model.JDBCUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("all")
@Repository
@Transactional
/**
 * Java Branchless Banking Connector(JBBC) Transaction DAO
 * @author JBBC
 *
 */
public class MockTransactionDAO {

	private JdbcTemplate jdbcTemplate;

	private static Logger logger = LoggerFactory.getLogger(MockTransactionDAO.class.getSimpleName());

	public JDBCAccount findAccount(String accountNo) {
		Object[] values = { accountNo };

        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM MOCK_ACCOUNT WHERE ACCOUNT_NUMBER = ? ", values, new RowMapper<JDBCAccount>(){
                        @Override
                        public JDBCAccount mapRow(ResultSet rs, int row)throws SQLException {
                            JDBCAccount account = new JDBCAccount();
                            account.setAccountTitle(rs.getString("ACCOUNT_TITLE"));
                            account.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
                            account.setAccountType(rs.getString("ACCOUNT_TYPE"));
                            account.setAccountStatus(rs.getString("ACCOUNT_STATUS"));
                            account.setAccountCurrency(rs.getString("ACCOUNT_CURRENCY"));
                            account.setAccountBalance(rs.getDouble("ACCOUNT_BALANCE"));
                            account.setTtlRequest(rs.getLong("TTL"));
                            return account;
                        }

                    });
        }catch (EmptyResultDataAccessException e){
            return  null;
        }
	}

	public List<JDBCStatement> getAccountStatement(String accountNo) {
		Object[] values = { accountNo };

		return jdbcTemplate.query(
				"SELECT * FROM MOCK_STATEMENT WHERE ACCOUNT_NUMBER = ? ", values, new RowMapper<JDBCStatement>(){
					@Override
					public JDBCStatement mapRow(ResultSet rs, int row)throws SQLException {
                        JDBCStatement statement = new JDBCStatement();
                        statement.setAccountNo(rs.getString("ACCOUNT_NUMBER"));
                        statement.setDescription(rs.getString("DESCRIPTION"));
                        statement.setStan(rs.getString("STAN"));
                        statement.setDrAmount(rs.getString("AMOUNT_DR"));
                        statement.setCrAmount(rs.getString("AMOUNT_CR"));
                        statement.setBalance(rs.getString("BALACE"));
                        statement.setTransactionDate(rs.getDate("TX_DATE"));
                        return statement;
					}
				});
	}

	public JDBCBill findBill(String consumerNo) {
		Object[] values = { consumerNo };
        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM MOCK_BILL WHERE CONSUMER_NUMBER = ? ", values, new RowMapper<JDBCBill>(){
                        @Override
                        public JDBCBill mapRow(ResultSet rs, int row)throws SQLException {
                            JDBCBill bill = new JDBCBill();

                            bill.setConsumerNumber(rs.getString("CONSUMER_NUMBER"));
                            bill.setSubscriberName(rs.getString("SUBSCRIBER_NAME"));
                            bill.setBillingMonth(rs.getString("BILLING_MONTH"));
                            bill.setDueDatePayableAmount(rs.getString("DUEDATEPAYABLE"));
                            bill.setPaymentDueDate(rs.getString("PAYMENTDUEDATE"));
                            bill.setPaymentAfterDueDate(rs.getString("PAYMENTAFTERDUEDATE"));
                            bill.setBillStatus(rs.getString("BILL_STATUS"));
                            bill.setResponseCode(rs.getString("RESPONSE_CODE"));
                            bill.setBillStatus(rs.getString("BILL_STATUS"));
                            bill.setNetCED(rs.getString("NET_CED"));
                            bill.setNetWithholdingTAX(rs.getString("NET_WITHHOLDING"));
                            bill.setUtilityCompanyId("1233");

                            return bill;
                        }

                    });
        }catch (EmptyResultDataAccessException e){
            return  null;
        }
	}


	public boolean payBill(String consumerNo) {
		String bill = "UPDATE MOCK_BILL SET BILL_STATUS = 'P' WHERE CONSUMER_NUMBER = ? ";

		int cr = jdbcTemplate.update(bill, consumerNo);

		return (cr > 0);
	}

	public boolean fundTransder(String fromAccount, double amount) {
		String debit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance - ? WHERE account_number = ?";

		int deb = jdbcTemplate.update(debit, amount, fromAccount);

		return (deb > 0);
	}
	
	public boolean fundTransderAdvice(String toAccount, double amount) {
		String credit = "UPDATE MOCK_ACCOUNT SET account_balance = account_balance + ? WHERE account_number = ?";

		int cr = jdbcTemplate.update(credit, amount, toAccount);

		return (cr > 0);
	}


	@Autowired
	public MockTransactionDAO(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
