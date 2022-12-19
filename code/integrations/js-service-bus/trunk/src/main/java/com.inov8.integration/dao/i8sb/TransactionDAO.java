package com.inov8.integration.dao.i8sb;

import com.inov8.integration.dao.i8sb.model.TransactionLog;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionDAO {

    @Autowired
    @Qualifier("I8SBDBJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("I8SBDBTransactionManager")
    private PlatformTransactionManager platformTransactionManager;

    private static Logger logger = LoggerFactory.getLogger(TransactionDAO.class.getSimpleName());

    public int insert(final TransactionLog transactionLog) {
        int count = 0;
        TransactionStatus status = null;
        try {
            status = startTransaction();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO TRANSACTION_LOG ");
            sql.append("(ID, Gateway, Client_ID, Terminal_ID, Channel_ID, RRN, Request_Type, Parent_Request_RRN, request_Date_Time, I8SB_Request) ");
            sql.append("VALUES (transaction_Log_seq.nextval,?,?,?,?,?,?,?,?,?)");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, transactionLog.getGateway());
                    ps.setString(2, transactionLog.getClientID());
                    ps.setString(3, transactionLog.getTerminalID());
                    ps.setString(4, transactionLog.getChannelID());
                    ps.setString(5, transactionLog.getRRN());
                    ps.setString(6, transactionLog.getRequestType());
                    ps.setString(7, transactionLog.getParentRequestRRN());
                    ps.setTimestamp(8, new Timestamp(new Date().getTime()));
                    ps.setString(9, transactionLog.getI8sbRequest());
                }
            });
            commit(status);
        } catch (Exception e) {
            rollback(status);
            logger.error(e.getMessage(), e);
        }
        return count;
    }

    public int update(final TransactionLog transactionLog) {
        int count = 0;
        TransactionStatus status = null;
        try {
            status = startTransaction();
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE TRANSACTION_LOG ");
            sql.append(" SET CHANNEL_Request = ?, CHANNEL_Response = ?, I8SB_Response = ?, Response_Code = ?, Status = ?, Error = ?, Response_Date_Time = ? ");
            sql.append(" WHERE ");
            sql.append(" TRANSACTION_LOG.RRN = ?");
            sql.append(" AND ");
            sql.append(" TRANSACTION_LOG.REQUEST_TYPE= ?");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, transactionLog.getChannelRequest());
                    ps.setString(2, transactionLog.getChannelResponse());
                    ps.setString(3, transactionLog.getI8sbResponse());
                    ps.setString(4, transactionLog.getResponseCode());
                    ps.setString(5, transactionLog.getStatus());
                    ps.setString(6, transactionLog.getError());
                    ps.setTimestamp(7, new Timestamp(new Date().getTime()));
                    ps.setString(8, transactionLog.getRRN());
                    ps.setString(9,transactionLog.getRequestType());
                }
            });
            commit(status);
        } catch (Exception e) {
            rollback(status);
            e.printStackTrace();
        }
        return count;
    }




    public Map<String, Object> getLinkedRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        try {
            return getJdbcTemplate().queryForMap("SELECT * FROM  LINKED_REQUEST WHERE CLIENT_ID =? AND TERMINAL_ID =? AND CHANNEL_ID=?  AND REQUEST_TYPE=?", i8SBSwitchControllerRequestVO.getI8sbClientID(), i8SBSwitchControllerRequestVO.getI8sbClientTerminalID(), i8SBSwitchControllerRequestVO.getI8sbChannelID(), i8SBSwitchControllerRequestVO.getRequestType());
        } catch (DataAccessException e) {
            return Collections.emptyMap();
        }
    }

    public List<Map<String, Object>> loadLinkedRequest() {

        try {
            return getJdbcTemplate().queryForList("SELECT * FROM  LINKED_REQUEST ");
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    public TransactionLog select(String rrn) {
        Object[] paramValues = new Object[]{rrn};
        List<TransactionLog> list = getJdbcTemplate().query("SELECT * FROM TRANSACTION_LOG WHERE RRN=? ", paramValues, new TransactionRowMapper());
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public List<TransactionLog> selectAll() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT * ");
        queryBuilder.append(" FROM TRANSACTION_LOG");
        List<TransactionLog> list = getJdbcTemplate().query(queryBuilder.toString(), new Object[]{}, new TransactionRowMapper());
        return list;
    }

    public int delete(String rrn) {
        Object[] paramValues = new Object[]{rrn};
        int count = getJdbcTemplate().update("DELETE FROM TRANSACTION_LOG WHERE RRN = ?", paramValues);
        return count;
    }

    public int deleteAll() {
        int count = getJdbcTemplate().update("DELETE FROM TRANSACTION_LOG");
        return count;
    }

    private TransactionStatus startTransaction() {
        DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
        return status;
    }

    private void commit(TransactionStatus status) {
        platformTransactionManager.commit(status);
    }

    private void rollback(TransactionStatus status) {
        try {
            platformTransactionManager.rollback(status);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
