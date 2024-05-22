package com.inov8.integration.middleware.dao;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.middleware.util.JSONUtil;
import com.inov8.integration.middleware.util.PortalConstants;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.server.service.integration.vo.CreditPaymentAdviceVO;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@SuppressWarnings("all")
@Repository
public class TransactionDAO {

    private static Logger logger = LoggerFactory.getLogger(TransactionDAO.class.getSimpleName());
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DefaultLobHandler defaultLobHandler;

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
            sql.append("(TRANSACTION_LOG_ID, RRN, MESSAGE_CODE, TRANSACTION_TYPE, TRANSACTION_DATE, STATUS_ID, I8_TRANSACTION_CODE, PDU_REQUEST_LOB,CHANNEL_ID) ");
            sql.append("VALUES (transaction_log_seq.nextval,?,?,?,?,?,?,?,?)");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, tx.getRetrievalRefNo());
                    ps.setString(2, tx.getMessageType());
                    ps.setString(3, tx.getTransactionCode());
                    ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                    ps.setLong(5, tx.getStatus());
                    ps.setString(6, tx.getI8TransactionCode());

                    LobCreator lobCreator = defaultLobHandler.getLobCreator();
                    lobCreator.setClobAsString(ps, 7, tx.getPduRequestHEX());
                    ps.setString(8, tx.getChannelId());
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
    public int saveNewCreditRecord(final CreditPaymentAdviceVO messageVO) {
        long startTime = new Date().getTime(); // start time
        int count = 0;
        logger.info("Save In Credit Payment Adivce: ");

        CrediRetryAdviceModel tx = new CrediRetryAdviceModel();
        tx.setAccountNo(messageVO.getAccountNo1());
        tx.setMobileNo(messageVO.getAccountNo2());
        tx.setTransactionCode(messageVO.getMicrobankTransactionCode());
        tx.setTransactionAmount(Double.parseDouble(messageVO.getTransactionAmount()));
        tx.setCreditInquiryRRN(messageVO.getReserved3());
        tx.setRequestTime(messageVO.getRequestTime());
        tx.setStan(messageVO.getStan());
        tx.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        if (messageVO.getProductId() != null) {
            tx.setProductId(messageVO.getProductId());
        }

        tx.setCreatedOn(new Date());
        tx.setUpdatedOn(new Date());
        tx.setReserved1(messageVO.getReserved1());
        tx.setReserved2(messageVO.getReserved2());
        tx.setReserved3(messageVO.getReserved3());
        tx.setReserved4(messageVO.getReserved4());
        tx.setReserved5(messageVO.getReserved5());
        tx.setReserved6(messageVO.getReserved6());
        tx.setReserved7(messageVO.getReserved7());
        tx.setReserved8(messageVO.getReserved8());
        tx.setReserved9(messageVO.getReserved9());
        tx.setReserved10(messageVO.getReserved10());
        tx.setCurrencyValue(messageVO.getCurrencyValue());
        tx.setWalletAmount(messageVO.getWalletAmount());


//        model.setBankImd(messageVO.getBankIMD());
        tx.setStatus(PortalConstants.IBFT_STATUS_IN_PROGRESS);
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO I8_MICROBANK_JS_PROD.CREDIT_RETRY_ADVICE ");
            sql.append("(CREDIT_RETRY_ADVICE_ID, MOBILE_NO, RRN, ACCOUNT_NO, TRANSACTION_AMOUNT, REQUEST_TIME, STAN, STATUS,TRANSACTION_CODE,BANK_IMD,CREATED_BY,UPDATED_BY,UPDATED_ON,CREATED_ON,VERSION_NO,PRODUCT_ID,CHANNEL_NAME,CREDIT_INQUIRY_RRN,ORIGINAL_TRANSACTION_RRN,RESERVED1,RESERVED2,RESERVED3,RESERVED4,RESERVED5,RESERVED6,RESERVED7,RESERVED8,RESERVED9,RESERVED10,CURRENCY_VALUE,WALLET_AMOUNT) ");
            sql.append("VALUES (I8_MICROBANK_JS_PROD.CREDIT_RETRY_ADVICE_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, tx.getMobileNo());
                    ps.setString(2, tx.getRetrievalReferenceNumber());
                    ps.setString(3, tx.getAccountNo());
                    ps.setDouble(4, tx.getTransactionAmount());
                    ps.setTimestamp(5, new Timestamp(tx.getRequestTime().getTime()));
                    ps.setString(6, tx.getStan());
                    ps.setString(7, tx.getStatus());
                    ps.setString(8, tx.getTransactionCode());
                    ps.setString(9, tx.getBankImd());
                    ps.setLong(10, 3L);
                    ps.setLong(11, 3L);
                    ps.setTimestamp(12, new Timestamp(new Date().getTime()));
                    ps.setTimestamp(13, new Timestamp(new Date().getTime()));
                    ps.setInt(14, 1);
                    ps.setLong(15, tx.getProductId());
                    ps.setString(16, tx.getChannelName());
                    ps.setString(17, tx.getCreditInquiryRRN());
                    ps.setString(18, tx.getOrignalTransactionRRN());
                    ps.setString(19, tx.getReserved1());
                    ps.setString(20, tx.getReserved2());
                    ps.setString(21, tx.getReserved3());
                    ps.setString(22, tx.getReserved4());
                    ps.setString(23, tx.getReserved5());
                    ps.setString(24, tx.getReserved6());
                    ps.setString(25, tx.getReserved7());
                    ps.setString(26, tx.getReserved8());
                    ps.setString(27, tx.getReserved9());
                    ps.setString(28, tx.getReserved10());
                    ps.setString(29, tx.getCurrencyValue());
                    ps.setString(30, tx.getWalletAmount());

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
            sql.append(" SET STATUS_ID = ?, RESPONSE_CODE = ?, PROCESSED_TIME = ?, PDU_RESPONSE_LOB = ? ");
            sql.append(" WHERE ");
            sql.append(" TRANSACTION_LOG.RRN = ?");

            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setLong(1, tx.getStatus());
                    ps.setString(2, tx.getResponseCode());
                    ps.setLong(3, tx.getProcessedTime());

                    LobCreator lobCreator = defaultLobHandler.getLobCreator();
                    lobCreator.setClobAsString(ps, 4, tx.getPduResponseHEX());

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


    public List<CrediRetryAdviceModel> findByExample(CrediRetryAdviceModel cardFeeRuleModel) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM I8_MICROBANK_JS_PROD.CREDIT_RETRY_ADVICE WHERE RRN= '").append(cardFeeRuleModel.getRetrievalReferenceNumber()).append("'");

        sb.append(" AND to_char(REQUEST_TIME,'yyyy-mm-dd')= '").append(new java.sql.Date(cardFeeRuleModel.getRequestTime().getTime())).append("'");
        sb.append(" order by CREDIT_RETRY_ADVICE_ID desc");
        Calendar c = Calendar.getInstance();
        c.setTime(cardFeeRuleModel.getRequestTime());
        c.set(Calendar.MILLISECOND, 0);
        logger.info("Loading Debit Card Fee Rule with Criteria: " + sb.toString());
        List<CrediRetryAdviceModel> list = (List<CrediRetryAdviceModel>) jdbcTemplate.query(sb.toString(), new CrediRetryAdviceModel());


        return list;
    }


    public List<CrediRetryAdviceModel> findCreditInquiryByExample(CrediRetryAdviceModel cardFeeRuleModel) {

        StringBuilder sb = new StringBuilder();


            sb.append("SELECT * FROM I8_MICROBANK_JS_PROD.CREDIT_RETRY_ADVICE WHERE CREDIT_INQUIRY_RRN= '").append(cardFeeRuleModel.getCreditInquiryRRN()).append("'");
            sb.append(" AND to_char(REQUEST_TIME,'yyyy-mm-dd')= '").append(new java.sql.Date(cardFeeRuleModel.getRequestTime().getTime())).append("'");
            sb.append(" order by CREDIT_RETRY_ADVICE_ID desc");
            Calendar c = Calendar.getInstance();
            c.setTime(cardFeeRuleModel.getRequestTime());
            c.set(Calendar.MILLISECOND, 0);


        logger.info("Loading Debit Card Fee Rule with Criteria: " + sb.toString());
        List<CrediRetryAdviceModel> list = (List<CrediRetryAdviceModel>) jdbcTemplate.query(sb.toString(), new CrediRetryAdviceModel());

        return list;
    }

    public Boolean validateApiGeeRRN(WebServiceVO webServiceVO) {
        StringBuilder sqlBuilder = new StringBuilder(400);
        sqlBuilder.append("SELECT COUNT(*) FROM I8_MICROBANK_JS_PROD.FONEPAY_INTEGERATION_LOG ");
        sqlBuilder.append(" WHERE RRN = '").append(webServiceVO.getRetrievalReferenceNumber()).append("'");
        sqlBuilder.append(" AND TRUNC(CREATED_ON) = TRUNC(SYSDATE)");
        logger.info("APIGEE RRN Validation: " + sqlBuilder.toString());
        int result = jdbcTemplate.queryForObject(sqlBuilder.toString(), Integer.class);
        if (result > 0)
            return false;

        return true;
    }

    public void AddToProcessing(String stan, String reqTime) {
        logger.info("Query to validate validate ibft Status IBFTStatusHibernateDAO.AddToProcessing() :: ");
        StringBuilder query = new StringBuilder(140);
        query.append("INSERT INTO I8_MICROBANK_JS_PROD.CREDIT_STATUS (STAN,REQ_TIME) VALUES "
                + " (?, ?)");

        try {
            jdbcTemplate.update(query.toString(), new Object[]{stan, reqTime});
            logger.info("Query to validate validate ibft Status IBFTStatusHibernateDAO.AddToProcessing()2 :: " + query.toString());

        } catch (DataAccessException e) {
            logger.error("Insertion in CREDIT_STATUS Failed for stan: " + stan + "  at ReqTime : " + reqTime);
            e.printStackTrace();
        }
    }


    public boolean CheckIBFTStatus(String stan, String reqTime) {
        boolean processing = false;
        try {
            StringBuilder query = new StringBuilder(140);
            query.append("SELECT COUNT(*) FROM I8_MICROBANK_JS_PROD.CREDIT_STATUS WHERE ");
            query.append("STAN = '").append(stan).append("'");
            query.append(" AND REQ_TIME = '").append(reqTime).append("'");
            logger.info("Query to validate validate ibft Status IBFTStatusHibernateDAO.CheckIBFTStatus() :: " + query.toString());
            int result = jdbcTemplate.queryForObject(query.toString(), Integer.class);
            logger.info("Query Result :: " + result);
            processing = result > 0 ? true : false;
        } catch (DataAccessException e) {
            logger.error("Count Query failed in IBFT_Status Failed for stan: " + stan + "  REQ_TIME : " + reqTime);
            e.printStackTrace();

        }

        return processing;

    }

    public FonePayLogModel validateCreditInquiryRRN(WebServiceVO webServiceVO) {

        StringBuilder sqlBuilder = new StringBuilder(400);
        sqlBuilder.append("SELECT * FROM I8_MICROBANK_JS_PROD.FONEPAY_INTEGERATION_LOG ");
        sqlBuilder.append(" WHERE RRN = '").append(webServiceVO.getReserved3()).append("'");
        sqlBuilder.append(" AND TRUNC(CREATED_ON) = TRUNC(SYSDATE)");
        logger.info("Credit Inquiry APIGEE RRN Validation: " + sqlBuilder.toString());
        List<FonePayLogModel> list = jdbcTemplate.query(sqlBuilder.toString(), new RowMapper<FonePayLogModel>() {

            @Override
            public FonePayLogModel mapRow(ResultSet resultSet, int i) throws SQLException {
                FonePayLogModel fonePayLogModel = new FonePayLogModel();
                fonePayLogModel.setRrn(resultSet.getString("RRN"));
                fonePayLogModel.setInput(resultSet.getString("INPUT"));
                fonePayLogModel.setOutput(resultSet.getString("OUTPUT"));
                return fonePayLogModel;
            }
        });

        FonePayLogModel fonePayLogModel = null;

        if (list != null && !list.isEmpty()) {
            fonePayLogModel = list.get(0);
        }


        return fonePayLogModel;
    }

    public FonePayLogModel saveFonePayIntegrationLogModel(WebServiceVO webServiceVO, String reqType)
            throws Exception {
        int count = 0;
        FonePayLogModel fonePayLogModel = new FonePayLogModel();

        try {
            Date date = new Date();
            Timestamp ts_now = new Timestamp(date.getTime());

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JSONObject json = null;
            String jsonInString = "";

            jsonInString = mapper.writeValueAsString(webServiceVO);


            String inputparam = JSONUtil.getJSON(webServiceVO);
            json = new JSONObject(inputparam);


            if (webServiceVO.getMobilePin() != null && !webServiceVO.getMobilePin().equals("")) {
                json.put("mobilePin", "****");
                jsonInString = String.valueOf(json);
            }

            if (webServiceVO.getOtpPin() != null && !webServiceVO.getOtpPin().equals("")) {
                json.put("otpPin", "****");
                jsonInString = String.valueOf(json);
            }
//        if (!reqType.equals(FonePayConstants.REQ_ACCOUNT_OPENING_L2)) {
//            if (webServiceVO.getReserved2() != null && !webServiceVO.getReserved2().equals("")) {
//                fonePayLogModel.setStan(webServiceVO.getReserved2());
//            } else {
//                String str = webServiceVO.getRetrievalReferenceNumber();
//                StringBuilder strBuilder = new StringBuilder();
//                Integer lengt = str.length();
//                for (int i = 6; i >= 1; i--) {
//                    strBuilder.append(str.charAt(lengt - i));
//                }
//                fonePayLogModel.setStan(strBuilder.toString());
//            }
//        }
//        if (reqType.equalsIgnoreCase(FonePayConstants.REQ_L2_UPGRADE_DISCREPANT) ||
//                reqType.equalsIgnoreCase("GetUpdateAccountDiscrepent") ||
//                reqType.equalsIgnoreCase(FonePayConstants.REQ_L2_UPGRADE) ||
//                reqType.equalsIgnoreCase(FonePayConstants.REQ_MERCHANT_ACCOUNT_UPGRADE)) {
//            jsonInString = "null";
//        }
            fonePayLogModel.setCnic(webServiceVO.getCnicNo());
            fonePayLogModel.setMobile_no(webServiceVO.getMobileNo());
            fonePayLogModel.setRequestType(reqType);
            fonePayLogModel.setResponse_code(webServiceVO.getResponseCode());
            fonePayLogModel.setResponse_description(webServiceVO.getResponseCodeDescription());
            fonePayLogModel.setRrn(webServiceVO.getRetrievalReferenceNumber());
            fonePayLogModel.setTransactionId(webServiceVO.getTransactionId());

            fonePayLogModel.setCreated_on(ts_now);
            fonePayLogModel.setUpdated_on(ts_now);
            fonePayLogModel.setInput(jsonInString);
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO I8_MICROBANK_JS_PROD.FONEPAY_INTEGERATION_LOG ");
            sql.append("(FONPAY_INTEGERATION_LOG_ID, REQUEST_TYPE, RRN, MOBILE_NO, CNIC, RESPONSE_CODE, RESPONSE_DESCRIPTION, TRANSACTION_ID,CREATED_ON,UPDATED_ON,INPUT,OUTPUT,STAN)");
            sql.append("VALUES (I8_MICROBANK_JS_PROD.FONEPAY_INTEGERATION_LOG_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?)");
            count = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, fonePayLogModel.getRequestType());
                    ps.setString(2, fonePayLogModel.getRrn());
                    ps.setString(3, fonePayLogModel.getMobile_no());
                    ps.setString(4, fonePayLogModel.getCnic());
                    ps.setString(5, fonePayLogModel.getResponse_code());
                    ps.setString(6, fonePayLogModel.getResponse_description());
                    ps.setString(7, fonePayLogModel.getTransactionId());
                    ps.setTimestamp(8, new Timestamp(new Date().getTime()));
                    ps.setTimestamp(9, new Timestamp(new Date().getTime()));
                    ps.setString(10, fonePayLogModel.getInput());
                    ps.setString(11, fonePayLogModel.getOutput());
                    ps.setString(12, fonePayLogModel.getStan());
                }
            });

        } catch (Exception e) {
            logger.error("Exception", e);
        }
//        fonePayLogModel = fonePayLogDAO.saveOrUpdate(fonePayLogModel);
        return fonePayLogModel;

    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final class CountToBooleanExtractor implements ResultSetExtractor<Boolean> {
        @Override
        public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count > 0;
        }
    }

}
