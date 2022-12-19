package com.inov8.integration.middleware.dao;

import com.inov8.integration.vo.TransactionRequest;
import com.inov8.integration.vo.TransactionResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public boolean saveTransactionDetail(TransactionRequest request, TransactionResponse response) {

        int save = 0;
        int counter = jdbcTemplate.queryForInt("Select count(*) from TRANSACTION_DETAIL WHERE CITIZEN_NUMBER = " + request.getCitizenNumber());

        if (counter > 0) {
            String debit = "UPDATE TRANSACTION_DETAIL SET CODE = ?,MESSAGE = ?,CITIZEN_NAME = ?,PRESENT_ADDRESS = ?,BIRTH_PLACE = ?,CARD_EXPIRED = ?,DATE_OF_BIRTH = ?, FINGER_INDEX_AVALIBLE = ?,RELIGION = ?,MOTHER_NAME = ?,NATIVE_LANGUAGE = ?,PHOTOGRAPH  = ?,VERIFICATION_FUNCTIONALITY = ?,RESPONSE_DATE= ? WHERE CITIZEN_NUMBER = ?";
            save = jdbcTemplate.update(debit, new Object[]{response.getResponseCode(), response.getMessage(), response.getCitizenName(), response.getPresentAddress(), response.getBirthPlace(), response.getCardExpired(), response.getDateOfBirth(), response.getFingerIndex(), response.getReligion(), response.getMotherName(), response.getNativeLanguage(), response.getPhotograph(), response.getVerificationFunctionality(), null, response.getCitizenNumber()});
        } else {
            String bill = "Insert into TRANSACTION_DETAIL(DETAIL_ID,FRANCHISE_ID,USERNAME,SESSION_ID,CITIZEN_NUMBER,CONTACT_NUMBER,FINGER_INDEX,FINGER_TEMPLATE,TEMPLATE_TYPE,TRANSACTION_ID,AREA_NAME,VERIFICATION_RESULT,MOBILE_BANK_ACCOUNT_NUMBER,ACCOUNT_LEVEL,REMITTANCE_AMOUNT,REMITTANCE_TYPE,REQUEST_DATE,CODE,MESSAGE,CITIZEN_NAME,PRESENT_ADDRESS,BIRTH_PLACE,CARD_EXPIRED,DATE_OF_BIRTH,FINGER_INDEX_AVALIBLE,RELIGION,MOTHER_NAME,NATIVE_LANGUAGE,PHOTOGRAPH,VERIFICATION_FUNCTIONALITY,RESPONSE_DATE) " +
                    "values(transaction_log_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            save = jdbcTemplate.update(bill, new Object[]{request.getFranchiseeID(), request.getUsername(), request.getSessionId(), request.getCitizenNumber(), request.getContactNumber(), request.getFingerIndex(), request.getFingerTemplate(), request.getTemplateType(), request.getTransactionId(), request.getAreaName(), request.getVerificationResult(), request.getMobileBankAccountNumber(), request.getAccountLevel(), request.getRemittanceAmount(), request.getRemittanceType(), null, response.getResponseCode(), response.getMessage(), response.getCitizenName(), response.getPresentAddress(), response.getBirthPlace(), response.getCardExpired(), response.getDateOfBirth(), response.getFingerIndex(), response.getReligion(), response.getMotherName(), response.getNativeLanguage(), response.getPhotograph(), response.getVerificationFunctionality(), null});
        }


        return (save > 0);
    }


    @Transactional
    public int save(final TransactionLogModel tx) {
        long startTime = new Date().getTime(); // start time
        int count = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO TRANSACTION_LOG ");
            sql.append("(TRANSACTION_LOG_ID, RRN, MESSAGE_CODE, TRANSACTION_TYPE, TRANSACTION_DATE, STATUS_ID, I8_TRANSACTION_CODE, REQUEST_XML) ");
            sql.append("VALUES (transaction_log_seq.nextval,?,?,?,?,?,?,UNISTR(?))");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, tx.getRetrievalRefNo());
                    ps.setString(2, tx.getMessageType());
                    ps.setString(3, tx.getTransactionCode());
                    ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                    ps.setLong(5, tx.getStatus());
                    ps.setString(6, tx.getI8TransactionCode());
                    ps.setString(7, tx.getPduRequestHEX());
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

                    ps.setString(4, tx.getPduResponseHEX());

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

    public int getFingerAttempts(TransactionRequest request) {

        int counter = 0;
        counter = jdbcTemplate.queryForInt("Select count(*) from TRANSACTION_DETAIL WHERE SESSION_ID = '" + request.getSessionId() + "' AND FINGER_INDEX = '" + request.getFingerIndex() + "' AND TRANSACTION_TYPE = '" + request.getTransactionType() + "'");
        return counter;
    }

    public int getTotalAttempts(TransactionRequest request) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        int counter = jdbcTemplate.queryForInt("Select count(*) from TRANSACTION_DETAIL WHERE CITIZEN_NUMBER = '" + request.getCitizenNumber() + "' AND to_char(REQUEST_DATE,'yyyy-MM-dd') = '" + currentDate + "'");
        return counter;
    }

    public int getTotalSession(TransactionRequest request) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        int counter = jdbcTemplate.queryForInt("Select count(DISTINCT SESSION_ID) from TRANSACTION_DETAIL WHERE CITIZEN_NUMBER = '" + request.getCitizenNumber() + "' AND to_char(REQUEST_DATE,'yyyy-MM-dd') = '" + currentDate + "'");
        return counter;
    }

    public int getFingerAttemptsAgainstSession(TransactionRequest request) {

        int counter = 0;
        counter = jdbcTemplate.queryForInt("Select count(*) from TRANSACTION_DETAIL WHERE SESSION_ID = '" + request.getSessionId() + "' AND TRANSACTION_TYPE = '" + request.getTransactionType() + "'");
        return counter;
    }

    @Transactional
    public int saveTransactionDetail(final TransactionRequest request) {

        final int seqNumber = jdbcTemplate.queryForInt("SELECT transaction_log_seq.nextval FROM Dual");

        String INSERT_TRANSACTION_DETAIL = "INSERT INTO TRANSACTION_DETAIL(DETAIL_ID, FRANCHISE_ID, USERNAME, SESSION_ID, CITIZEN_NUMBER, " +
                "CONTACT_NUMBER, FINGER_INDEX, FINGER_TEMPLATE, TEMPLATE_TYPE, TRANSACTION_ID, AREA_NAME, " +
                "VERIFICATION_RESULT, MOBILE_BANK_ACCOUNT_NUMBER, ACCOUNT_LEVEL, REMITTANCE_AMOUNT, REMITTANCE_TYPE, " +
                "REQUEST_DATE, TRANSACTION_TYPE) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        int rows = jdbcTemplate.update(INSERT_TRANSACTION_DETAIL, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, seqNumber);
                ps.setString(2, request.getFranchiseeID());
                ps.setString(3, request.getUsername());
                ps.setString(4, request.getSessionId());
                ps.setString(5, request.getCitizenNumber());
                ps.setString(6, request.getContactNumber());
                ps.setString(7, request.getFingerIndex());

                if (StringUtils.isNotEmpty(request.getFingerTemplate())) {
                    LobCreator lobCreator = defaultLobHandler.getLobCreator();
                    lobCreator.setBlobAsBytes(ps, 8, request.getFingerTemplate().getBytes());
                } else {
                    LobCreator lobCreator = defaultLobHandler.getLobCreator();
                    lobCreator.setBlobAsBytes(ps, 8, null);
                }

                ps.setString(9, request.getTemplateType());
                ps.setString(10, request.getTransactionId());
                ps.setString(11, request.getAreaName());
                ps.setString(12, request.getVerificationResult());
                ps.setString(13, request.getMobileBankAccountNumber());
                ps.setString(14, request.getAccountLevel());
                ps.setString(15, request.getRemittanceAmount());
                ps.setString(16, request.getRemittanceType());
                ps.setTimestamp(17, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(18, request.getTransactionType());

            }
        });

        return rows > 0 ? seqNumber : -1;
    }

    @Transactional
    public boolean updateTransactionDetail(final int id, final TransactionResponse response) {
        String UDPATE_TRANSACTION_DETAIL = "UPDATE TRANSACTION_DETAIL SET CODE = ?,MESSAGE = ?,CITIZEN_NAME = ?,PRESENT_ADDRESS = ?,BIRTH_PLACE = ?,CARD_EXPIRED = ?,DATE_OF_BIRTH = ?, FINGER_INDEX_AVALIBLE = ?,RELIGION = ?,MOTHER_NAME = ?,NATIVE_LANGUAGE = ?,PHOTOGRAPH  = ?,VERIFICATION_FUNCTIONALITY = ?,RESPONSE_DATE= ? , GENDER= ? , SESSION_ID= ? WHERE DETAIL_ID = ?";

        int rows = jdbcTemplate.update(UDPATE_TRANSACTION_DETAIL, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {

                ps.setString(1, response.getResponseCode());
                ps.setString(2, response.getMessage());
                ps.setString(3, response.getCitizenName());
                ps.setString(4, response.getPresentAddress());
                ps.setString(5, response.getBirthPlace());
                ps.setString(6, response.getCardExpired());
                ps.setString(7, response.getDateOfBirth());
                ps.setString(8, response.getFingerIndex());
                ps.setString(9, response.getReligion());
                ps.setString(10, response.getMotherName());
                ps.setString(11, response.getNativeLanguage());

                if (StringUtils.isNotEmpty(response.getPhotograph())) {
                    LobCreator lobCreator = defaultLobHandler.getLobCreator();
                    lobCreator.setBlobAsBytes(ps, 12, response.getPhotograph().getBytes());
                } else {
                    LobCreator lobCreator = defaultLobHandler.getLobCreator();
                    lobCreator.setBlobAsBytes(ps, 12, null);
                }

                ps.setString(12, response.getPhotograph());
                ps.setString(13, response.getVerificationFunctionality());
                ps.setTimestamp(14, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(15, response.getGender());
                ps.setString(16, response.getSessionId());
                ps.setLong(17, id);

            }
        });

        return rows > 0;
    }

    public String getSessionId(TransactionRequest request) {
        String sessionId = null;
        int counter = jdbcTemplate.queryForInt("Select  COUNT(*) from TRANSACTION_DETAIL where CITIZEN_NUMBER = " + request.getCitizenNumber());
        String sql = "Select SESSION_ID from TRANSACTION_DETAIL where CITIZEN_NUMBER = " + request.getCitizenNumber();
        if (counter > 0) {
            sessionId = jdbcTemplate.queryForObject(sql, String.class);
        }


        return sessionId;

    }

    public TransactionResponse getCustomerLastData(TransactionRequest transactionRequest) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        Object[] values = {transactionRequest.getCitizenNumber(), currentDate, transactionRequest.getTransactionType()};

        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM (SELECT SESSION_ID,CODE,RESPONSE_DATE," +
                            " MESSAGE  ,CITIZEN_NAME  ,PRESENT_ADDRESS  ,BIRTH_PLACE  ,CARD_EXPIRED," +
                            " DATE_OF_BIRTH  , FINGER_INDEX_AVALIBLE  ,RELIGION  ,MOTHER_NAME  ,NATIVE_LANGUAGE,VERIFICATION_FUNCTIONALITY ,GENDER ,TRANSACTION_ID  " +
                            " FROM TRANSACTION_DETAIL WHERE CITIZEN_NUMBER = ? AND to_char(REQUEST_DATE,'yyyy-mm-dd') = ? " +
                            " AND TRANSACTION_TYPE = ? " +
                            " Order BY DETAIL_ID DESC)" +
                            " WHERE ROWNUM = 1", values, new RowMapper<TransactionResponse>() {
                        @Override
                        public TransactionResponse mapRow(ResultSet rs, int row) throws SQLException {
                            TransactionResponse user = new TransactionResponse();

                            user.setSessionId(rs.getString("SESSION_ID"));
                            user.setResponseCode(rs.getString("CODE"));
                            user.setResponseDateTime(rs.getString("RESPONSE_DATE"));
                            user.setMessage(rs.getString("MESSAGE"));
                            user.setCitizenName(rs.getString("CITIZEN_NAME"));
                            user.setPresentAddress(rs.getString("PRESENT_ADDRESS"));
                            user.setBirthPlace(rs.getString("BIRTH_PLACE"));
                            user.setCardExpired(rs.getString("CARD_EXPIRED"));
                            user.setDateOfBirth(rs.getString("DATE_OF_BIRTH"));
                            user.setFingerIndex(rs.getString("FINGER_INDEX_AVALIBLE"));
                            user.setReligion(rs.getString("RELIGION"));
                            user.setMotherName(rs.getString("MOTHER_NAME"));
                            user.setVerificationFunctionality(rs.getString("VERIFICATION_FUNCTIONALITY"));
                            user.setNativeLanguage(rs.getString("NATIVE_LANGUAGE"));
                            user.setGender(rs.getString("GENDER"));
                            user.setTransactionId(rs.getString("TRANSACTION_ID"));

                            return user;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public TransactionResponse getCustomerFirstData(TransactionRequest transactionRequest) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        Object[] values = {transactionRequest.getCitizenNumber(), currentDate, transactionRequest.getTransactionType(), transactionRequest.getSessionId()};

        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM (SELECT SESSION_ID,CODE,RESPONSE_DATE," +
                            " MESSAGE  ,CITIZEN_NAME  ,PRESENT_ADDRESS  ,BIRTH_PLACE  ,CARD_EXPIRED," +
                            " DATE_OF_BIRTH  , FINGER_INDEX_AVALIBLE  ,RELIGION  ,MOTHER_NAME  ,NATIVE_LANGUAGE,VERIFICATION_FUNCTIONALITY ,GENDER,TRANSACTION_ID  " +
                            " FROM TRANSACTION_DETAIL WHERE CITIZEN_NUMBER = ? AND to_char(REQUEST_DATE,'yyyy-mm-dd') = ? " +
                            " AND TRANSACTION_TYPE = ? " +
                            " AND SESSION_ID = ? " +
                            " Order BY DETAIL_ID ASC )" +
                            " WHERE ROWNUM = 1", values, new RowMapper<TransactionResponse>() {
                        @Override
                        public TransactionResponse mapRow(ResultSet rs, int row) throws SQLException {
                            TransactionResponse user = new TransactionResponse();

                            user.setSessionId(rs.getString("SESSION_ID"));
                            user.setResponseCode(rs.getString("CODE"));
                            user.setResponseDateTime(rs.getString("RESPONSE_DATE"));
                            user.setMessage(rs.getString("MESSAGE"));
                            user.setCitizenName(rs.getString("CITIZEN_NAME"));
                            user.setPresentAddress(rs.getString("PRESENT_ADDRESS"));
                            user.setBirthPlace(rs.getString("BIRTH_PLACE"));
                            user.setCardExpired(rs.getString("CARD_EXPIRED"));
                            user.setDateOfBirth(rs.getString("DATE_OF_BIRTH"));
                            user.setFingerIndex(rs.getString("FINGER_INDEX_AVALIBLE"));
                            user.setReligion(rs.getString("RELIGION"));
                            user.setMotherName(rs.getString("MOTHER_NAME"));
                            user.setVerificationFunctionality(rs.getString("VERIFICATION_FUNCTIONALITY"));
                            user.setNativeLanguage(rs.getString("NATIVE_LANGUAGE"));
                            user.setGender(rs.getString("GENDER"));
                            user.setTransactionId(rs.getString("TRANSACTION_ID"));

                            return user;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
