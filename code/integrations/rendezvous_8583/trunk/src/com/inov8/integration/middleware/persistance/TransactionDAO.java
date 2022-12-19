package com.inov8.integration.middleware.persistance;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.persistance.model.TransactionLogModel;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.DateTools;

@SuppressWarnings("all")
@Repository
/**
 * Java Branchless Banking Connector(JBBC) Transaction DAO
 * @author JBBC
 *
 */
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
			sql.append("(TRANSACTION_LOG_ID, RRN, MESSAGE_CODE, TRANSACTION_TYPE, TRANSACTION_DATE, STATUS_ID, PDU_REQUEST_STRING, PARENT_TRANSACTION_ID, I8_TRANSACTION_CODE,CHANNEL_ID, PDU_REQUEST_HEX, PDU_RESPONSE_HEX) ");
			sql.append("VALUES (transaction_log_seq.nextval,?,?,?,?,?,?,?,?,?,?,?)");

			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, tx.getRetrievalRefNo());
					ps.setString(2, tx.getMessageType());
					ps.setString(3, tx.getTransactionCode());
					ps.setTimestamp(4, new Timestamp(new Date().getTime()));
					ps.setLong(5, tx.getStatus());
					ps.setString(6, tx.getPduRequestString());
					// lobHandler.getLobCreator().setBlobAsBytes(ps, 6,
					// tx.getPduRequestString().getBytes());

					if (tx.getParentTransactionLogId() == null)
						ps.setNull(7, Types.NULL);
					else
						ps.setLong(7, tx.getParentTransactionLogId());
					ps.setString(8, tx.getI8TransactionCode());
					ps.setString(9, tx.getChannelId());
					ps.setString(10, tx.getPduRequestHEX());
					ps.setString(11, tx.getPduResponseHEX());
				}
			});
			commit(status);
		} catch (Exception e) {
			rollback(status);
			logger.error("Exception",e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** save() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return count;
	}

	public int updateRetryCount(final Long transactionLogId) {
		long startTime = new Date().getTime(); // start time
		int count = 0;
		TransactionStatus status = null;
		try {
			status = startTransaction();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TRANSACTION_LOG trx ");
			sql.append(" SET trx.retry_count = trx.retry_count+1 ");
			sql.append(" WHERE ");
			sql.append(" trx.transaction_log_id = ? ");
			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) {
					try {
						ps.setLong(1, transactionLogId);
					} catch (SQLException e) {
						logger.error("Exception",e);
					}
				}
			});
			commit(status);
		} catch (Exception e) {
			logger.error("Exception",e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** updateRetryCount() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
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
			sql.append(" SET trx.STATUS_ID = ? ");
			sql.append(" WHERE ");
			sql.append(" trx.RRN = ? ");
			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) {
					try {
						ps.setLong(1, trasactionStatus);
						ps.setString(2, retrivalRefernceNumber);
					} catch (SQLException e) {
						logger.error("Exception",e);
					}
				}
			});
			commit(status);
		} catch (Exception e) {
			rollback(status);
			logger.error("Exception",e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** updateTransactionStatus() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return count;
	}

	public int updateTransactionStatus(final Long transactionLogId, final Long trasactionStatus) {
		if(true){
			return 1;
		}
		long startTime = new Date().getTime(); // start time
		int count = 0;
		TransactionStatus status = null;
		try {
			status = startTransaction();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TRANSACTION_LOG trx ");
			sql.append(" SET trx.STATUS_ID = ? ");
			sql.append(" WHERE ");
			sql.append(" trx.transaction_log_id = ? ");
			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) {
					try {
						ps.setLong(1, trasactionStatus);
						ps.setLong(2, transactionLogId);
					} catch (SQLException e) {
						logger.error("Exception",e);
					}
				}
			});
			commit(status);
		} catch (Exception e) {
			logger.error("Exception",e);
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
			sql.append(" SET STATUS_ID = ?, RESPONSE_CODE = ?, PDU_RESPONSE_STRING = ?, PROCESSED_TIME = ?, PDU_RESPONSE_HEX = ? ");
			sql.append(" WHERE ");
			sql.append(" TRANSACTION_LOG.RRN = ?");

			count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, tx.getStatus());
					ps.setString(2, tx.getResponseCode());
					ps.setString(3, tx.getPduResponseString());
					// lobHandler.getLobCreator().setBlobAsBytes(ps, 3,
					// tx.getPduResponseString().getBytes());
					ps.setLong(4, tx.getProcessedTime());
					ps.setString(5, tx.getPduResponseHEX());
					ps.setString(6, tx.getRetrievalRefNo());
				}
			});
			commit(status);
		} catch (Exception e) {
			rollback(status);
			logger.error("Exception",e);
		}
		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** update() REQUEST PROCESSED IN ****: " + difference + " milliseconds for RRN:" + tx.getRetrievalRefNo());
		return count;
	}

	public TransactionLogModel select(String rrn) {
		long startTime = new Date().getTime(); // start time
		Object[] paramValues = new Object[] { rrn };
		List<TransactionLogModel> list = getJdbcTemplate().query("SELECT * FROM TRANSACTION_LOG WHERE RRN=? ", paramValues, new TransactionRowMapper());
		if (list != null && list.size() > 0)
			return list.get(0);

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("**** select() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		return null;
	}
	
	public List<TransactionLogModel> selectAll() {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT * ");
		// queryBuilder
		// .append(" TRANSACTION_LOG_ID,RRN,MESSAGE_CODE,TRANSACTION_TYPE,TRANSACTION_DATE,STATUS_ID,RESPONSE_CODE,PDU_REQUEST_STRING, PDU_RESPONSE_STRING,PARENT_TRANSACTION_ID,I8_TRANSACTION_CODE,PROCESSED_TIME");
		queryBuilder.append(" FROM TRANSACTION_LOG");
		List<TransactionLogModel> list = getJdbcTemplate().query(queryBuilder.toString(), new Object[] {}, new TransactionRowMapper());
		return list;
	}

	public List<TransactionLogModel> findReversalTransaction() {
		SCHEDULER_RETRY_COUNT = Integer.parseInt(ConfigReader.getInstance().getProperty("reversal.scheduler.retry", "5", false));
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT * FROM TRANSACTION_LOG trx");
		queryBuilder.append(" WHERE trx.status_id = ? ");
		queryBuilder.append(" AND trx.message_code = ? ");
		queryBuilder.append(" AND trx.transaction_type = ? ");
		queryBuilder.append(" AND trx.response_code is null ");
		queryBuilder.append(" AND trx.retry_count <= ? ");
		queryBuilder.append(" AND trx.PARENT_TRANSACTION_ID IS NULL ");
		queryBuilder.append(" AND trunc(trx.transaction_date) >= trunc(sysdate) ");

		List<TransactionLogModel> list = null;
		try {
			
			Object[] params = {com.inov8.integration.middleware.enums.TransactionStatus.TIMEOUT.getValue(), 
					MessageTypeEnum.MT_0200.getValue(),
					TransactionCodeEnum.JS_FUND_TRANSFER.getValue(), 
					SCHEDULER_RETRY_COUNT };
			
			list = getJdbcTemplate().query(queryBuilder.toString(), params, new TransactionRowMapper());
			System.out.println("");
		} catch (Exception e) {
			logger.error("Exception",e);
		}
		return list;
	}

	

	public String selectFullStatement(String fileName) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT FULL_STMT ");
		queryBuilder.append(" FROM STATEMENT_DATA ");
		queryBuilder.append(" WHERE FILE_NAME = ");
		queryBuilder.append("'" + fileName + "'");

		return getJdbcTemplate().queryForObject(queryBuilder.toString(), String.class);
	}

	public int delete(String rrn) {
		Object[] paramValues = new Object[] { rrn };
		int count = getJdbcTemplate().update("DELETE FROM TRANSACTION_LOG WHERE RRN = ?", paramValues);
		return count;
	}

	public int deleteAll() {
		int count = getJdbcTemplate().update("DELETE FROM TRANSACTION_LOG");
		return count;
	}

	private TransactionStatus startTransaction() {
		try{
			long startTime = new Date().getTime(); // start time
			DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
			TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
			long endTime = new Date().getTime(); // end time
			long difference = endTime - startTime; // check different
			logger.debug("**** startTransaction() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
			return status;
		}catch(Exception e){
			logger.error("Exception",e);
		}
		return null;
	}

	private void commit(TransactionStatus status) {
		try{
			long startTime = new Date().getTime(); // start time
			if(status != null){
				platformTransactionManager.commit(status);
			}else{
				logger.debug(" commit() TransactionStatus is null ");
			}
			long endTime = new Date().getTime(); // end time
			long difference = endTime - startTime; // check different
			logger.debug("**** commit() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		}catch(Exception e){
			logger.error("Exception",e);
		}
		
	}

	private void rollback(TransactionStatus status) {
		try{
			long startTime = new Date().getTime(); // start time
			if(status != null){
				platformTransactionManager.rollback(status);
			}else{
				logger.debug(" rollback() TransactionStatus is null ");
			}
			long endTime = new Date().getTime(); // end time
			long difference = endTime - startTime; // check different
			logger.debug("**** rollback() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
		}catch(Exception e){
			logger.error("Exception",e);
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
