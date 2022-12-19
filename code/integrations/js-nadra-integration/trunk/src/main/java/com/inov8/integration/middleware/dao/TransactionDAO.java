package com.inov8.integration.middleware.dao;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@SuppressWarnings("all")
@Repository
public class TransactionDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DefaultLobHandler defaultLobHandler;

    private static Logger logger = LoggerFactory.getLogger(TransactionDAO.class.getSimpleName());

    @Autowired
    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Transactional
    public int save(final TransactionLogModel tx) {
        long startTime = new Date().getTime(); // start time
        int count = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO TRANSACTION_LOG ");
            sql.append("(TRANSACTION_LOG_ID, RRN, MESSAGE_CODE, TRANSACTION_TYPE, TRANSACTION_DATE, STATUS_ID,CHANNEL_ID, I8_TRANSACTION_CODE, REQUEST_XML) ");
            sql.append("VALUES (transaction_log_seq.nextval,?,?,?,?,?,?,?,UNISTR(?))");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, tx.getRetrievalRefNo());
                    ps.setString(2, tx.getMessageType());
                    ps.setString(3, tx.getTransactionCode());
                    ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                    ps.setLong(5, tx.getStatus());
                    ps.setString(6, tx.getChannelId());
                    ps.setString(7, tx.getI8TransactionCode());
                    ps.setString(8, tx.getPduRequestString());
                }
            });
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** save() REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return count;
    }

    @Transactional
    public int update(final TransactionLogModel tx) {
        long startTime = new Date().getTime(); // start time
        int count = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE TRANSACTION_LOG ");
            sql.append(" SET STATUS_ID = ?, RESPONSE_CODE = ?, PROCESSED_TIME = ?, RESPONSE_XML = UNISTR(?) ");
            sql.append(" WHERE ");
            sql.append(" TRANSACTION_LOG.RRN = ?");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setLong(1, tx.getStatus());
                    ps.setString(2, tx.getResponseCode());
                    ps.setLong(3, tx.getProcessedTime());

                    ps.setString(4, tx.getPduResponseString());

                    ps.setString(5, tx.getRetrievalRefNo());
                }
            });
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("**** update() REQUEST PROCESSED IN ****: " + difference + " milliseconds for RRN:" + tx.getRetrievalRefNo());
        return count;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
