package com.inov8.hsm.persistance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.inov8.hsm.persistance.model.TransactionLogModel;
import com.inov8.hsm.util.ConfigReader;
import com.inov8.hsm.util.VersionInfo;

@SuppressWarnings("all")
@Repository
/**
 * Java Branchless Banking Connector(JBBC) Transaction DAO
 * @author JBBC
 *
 */
@VersionInfo(lastModified = "24/12/2014", releaseVersion = "1.0", version = "1.1", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class TransactionDAO {

	@Autowired
	private OracleLobHandler lobHandler;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	private JdbcTemplate jdbcTemplate;

	private int SCHEDULER_RETRY_COUNT = Integer.parseInt(ConfigReader.getInstance().getProperty("reversal.scheduler.retry", "5", false));

	private static Logger logger = LoggerFactory.getLogger(TransactionDAO.class.getSimpleName());

	@Autowired
	public TransactionDAO(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

	public String getNextRRNSequence() {
		long startTime = new Date().getTime(); // start time
		String sequence = getJdbcTemplate().query(" SELECT  STAN_SEQ.nextval AS STANSEQ FROM  DUAL ", new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				String sequence = null;
				while (rs.next()) {
					sequence = rs.getString("STANSEQ");
				}
				return sequence;
			}
		});

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** getNextRRNSequence() REQUEST PROCESSED IN ****: " + difference + " milliseconds");

		sequence = StringUtils.leftPad(sequence, 6, '0');
		return sequence;
	}

	public int save(final TransactionLogModel tx) {
		long startTime = new Date().getTime(); // start time
		int count = 0;
		TransactionStatus status = null;
		try {
			status = startTransaction();
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO TRANSACTION_LOG ");
			sql.append("(TRANSACTION_LOG_ID, COMMAND, REQUEST, REQUEST_TIME, MICROBANK_TXID, UPID) ");
			sql.append("VALUES (TRANSACTION_LOG_SEQ.nextval,?,?,?,?,?)");

			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, tx.getCommand());
					ps.setString(2, tx.getRequestPacket());
					ps.setTimestamp(3, tx.getRequestTime());
					ps.setString(4, tx.getMicrobankTrxId());
					ps.setString(5, tx.getUniquePacketIdentifier());
				}
			});
			commit(status);
		} catch (Exception e) {
			rollback(status);
			logger.error("Exception at INSERT", e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** save() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return count;
	}

	public int updateTransactionStatus(final String retrivalRefernceNumber, final Integer trasactionStatus) {
		long startTime = new Date().getTime(); // start time
		int count = 0;
		TransactionStatus status = null;
		try {
			status = startTransaction();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TRANSACTION_LOG trx ");
			sql.append(" SET trx.RESPONSE_CODE = ? ");
			sql.append(" WHERE ");
			sql.append(" trx.UPID = ? ");
			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) {
					try {
						ps.setLong(1, trasactionStatus);
						ps.setString(2, retrivalRefernceNumber);
					} catch (SQLException e) {
						logger.error("Exception", e);
					}
				}
			});
			commit(status);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** updateTransactionStatus() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return count;
	}

	public int updateTransactionStatus(final Long transactionLogId, final Long trasactionStatus) {
		long startTime = new Date().getTime(); // start time
		int count = 0;
		TransactionStatus status = null;
		try {
			status = startTransaction();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TRANSACTION_LOG trx ");
			sql.append(" SET trx.RESPONSE_CODE = ? ");
			sql.append(" WHERE ");
			sql.append(" trx.transaction_log_id = ? ");
			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) {
					try {
						ps.setLong(1, trasactionStatus);
						ps.setLong(2, transactionLogId);
					} catch (SQLException e) {
						logger.error("Exception", e);
					}
				}
			});
			commit(status);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** updateTransactionStatus() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return count;
	}

	public int update(final TransactionLogModel tx) {
		long startTime = new Date().getTime(); // start time
		int count = 0;
		TransactionStatus status = null;
		try {
			status = startTransaction();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TRANSACTION_LOG ");
			sql.append(" SET RESPONSE_CODE = ?, RESPONSE = ?, RESPONSE_TIME = ? ");
			sql.append(" WHERE ");
			sql.append(" TRANSACTION_LOG.UPID = ?");

			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, tx.getResponseCode());
					ps.setString(2, tx.getResponsePacket());
					ps.setTimestamp(3, tx.getResponseTime());
					ps.setString(4, tx.getUniquePacketIdentifier());
				}
			});
			commit(status);
		} catch (Exception e) {
			rollback(status);
			logger.error("Exception", e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** update() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return count;
	}

	public TransactionLogModel select(String upid) {
		long startTime = new Date().getTime(); // start time
		try{
			Object[] paramValues = new Object[] { upid };
			List<TransactionLogModel> list = getJdbcTemplate().query("SELECT * FROM TRANSACTION_LOG WHERE UPID=? ", paramValues, new TransactionRowMapper());
			if (list != null && list.size() > 0)
				return list.get(0);
		}catch(Exception e){
			logger.error("Exception", e);
		}

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** select() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return null;
	}

	public List<TransactionLogModel> selectAll() {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT * ");
		// queryBuilder
		// .append(" TRANSACTION_LOG_ID,RRN,MESSAGE_CODE,TRANSACTION_TYPE,TRANSACTION_DATE,RESPONSE_CODE,RESPONSE_CODE,PDU_REQUEST_STRING, PDU_RESPONSE_STRING,PARENT_TRANSACTION_ID,I8_TRANSACTION_CODE,PROCESSED_TIME");
		queryBuilder.append(" FROM TRANSACTION_LOG");
		List<TransactionLogModel> list = getJdbcTemplate().query(queryBuilder.toString(), new Object[] {}, new TransactionRowMapper());
		return list;
	}

	private TransactionStatus startTransaction() {
		long startTime = new Date().getTime(); // start time
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** startTransaction() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return status;
	}

	private void commit(TransactionStatus status) {
		long startTime = new Date().getTime(); // start time
		platformTransactionManager.commit(status);
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** commit() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
	}

	private void rollback(TransactionStatus status) {
		long startTime = new Date().getTime(); // start time
		platformTransactionManager.rollback(status);
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** rollback() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
