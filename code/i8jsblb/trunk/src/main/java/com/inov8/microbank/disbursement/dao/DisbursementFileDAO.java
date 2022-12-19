package com.inov8.microbank.disbursement.dao;

import com.inov8.microbank.common.util.*;
import com.inov8.microbank.disbursement.util.BatchUtil;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.vo.BDWalkInVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisbursementFileDAO {

    private Logger logger = Logger.getLogger(DisbursementFileDAO.class.getName());
    private Connection connection;
    private PreparedStatement walkInInsertStatement;
    private PreparedStatement appUserInsertStatement;
    private PreparedStatement smaInsertStatement;
    private PreparedStatement accountHolderInsertStatement;
    private PreparedStatement accountInsertStatement;
    private PreparedStatement disbursementStatement;

    private long startedAt = 0;
    private int batchNo = 1;
    private int batchCounter;
    private boolean executed;

    public DisbursementFileDAO(Connection connection) {
        this.connection = connection;
    }

    public ResultSet loadBulkDisbursementsModelForWalkInCreation(Long disbursementFileInfoId) throws Exception {

        PreparedStatement statement = connection.prepareStatement(LOAD_BULK_DISBURSEMENT);
        statement.setLong(1, disbursementFileInfoId);
        statement.setInt(2, DisbursementStatusConstants.STATUS_WALK_IN_CREATION_IN_PROGRESS);
        statement.setBoolean(3, true);
        statement.setBoolean(4, false);

        return statement.executeQuery();
    }

    public List<Long> getWalkInCreationStats(Long findInfoId) throws Exception {
        PreparedStatement statement = connection.prepareStatement(WALK_IN_CREATION_STATS);
        statement.setLong(1, findInfoId);
        statement.setLong(2, findInfoId);
        ResultSet set = statement.executeQuery();

        List<Long> results = new ArrayList<>(2);
        if(set.next()) {
            results.add(set.getLong(1));
            results.add(set.getLong(2));
        }

        set.close();
        return results;
    }

    public int updateDisbursementFileInfo(Long pk, Integer status) throws Exception {
        PreparedStatement statement = connection.prepareStatement(UPDATE_DISBURSEMENT_FILE_STATUS);
        statement.setInt(1, status);
        statement.setLong(2, pk);

        int result =  statement.executeUpdate();
        statement.close();
        connection.commit();

        return result;
    }

    public void addToBatch(BDWalkInVO vo) throws Exception {
        if (batchCounter >= BatchUtil.getDbBatchSize()) {
            // execute existing batch
            executeBatch();

            // reset batch again
            initializeBatch();
        }

        prepareBatch(vo);

        executed = false;
        batchCounter++;
    }

    public void initializeBatch() throws Exception {
        walkInInsertStatement = connection.prepareStatement(WALK_IN_CUSTOMER_INSERT);
        appUserInsertStatement = connection.prepareStatement(APP_USER_INSERT);
        smaInsertStatement = connection.prepareStatement(SMA_INSERT);
        accountHolderInsertStatement = connection.prepareStatement(ACCOUNT_HOLDER_INSERT);
        accountInsertStatement = connection.prepareStatement(ACCOUNT_INSERT);
        disbursementStatement = connection.prepareStatement(DISBURSEMENT_UPDATE);
    }

    private void prepareBatch(BDWalkInVO vo) throws Exception {
        prepareWalkInCustomer(vo);
        prepareAppUser(vo);
        prepareSMA(vo);
        prepareAccountHolder(vo);
        prepareAccount(vo);
        prepareDisbursement(vo);
    }

    private void prepareWalkInCustomer(BDWalkInVO vo) throws Exception {
        int colIndex = 1;
        long id = vo.getSequenceId();
        walkInInsertStatement.setLong(colIndex ++, id);
        walkInInsertStatement.setString(colIndex ++, vo.getCnic());
        walkInInsertStatement.setString(colIndex++, vo.getMobile());
        walkInInsertStatement.setDate(colIndex++, vo.getDate());
        walkInInsertStatement.setDate(colIndex++, vo.getDate());
        walkInInsertStatement.setLong(colIndex++, vo.getLoggedInUser());
        walkInInsertStatement.setLong(colIndex++, vo.getLoggedInUser());
        walkInInsertStatement.setInt(colIndex, 0);

        walkInInsertStatement.addBatch();
    }

    private void prepareAppUser(BDWalkInVO vo) throws Exception {

        int colIndex = 1;
        long id = vo.getSequenceId();
        appUserInsertStatement.setLong(colIndex++, id);
        appUserInsertStatement.setLong(colIndex++, UserTypeConstantsInterface.WALKIN_CUSTOMER);
        appUserInsertStatement.setLong(colIndex++, id);
        appUserInsertStatement.setString(colIndex++, " - ");
        appUserInsertStatement.setString(colIndex++, " - ");
        appUserInsertStatement.setString(colIndex++, String.valueOf(id));
        appUserInsertStatement.setString(colIndex++, String.valueOf(id));
        appUserInsertStatement.setString(colIndex++, vo.getCnic());
        appUserInsertStatement.setString(colIndex++, vo.getMobile());
        appUserInsertStatement.setBoolean(colIndex++, false);

        appUserInsertStatement.setBoolean(colIndex++, false);
        appUserInsertStatement.setBoolean(colIndex++, false);
        appUserInsertStatement.setBoolean(colIndex++, true);
        appUserInsertStatement.setBoolean(colIndex++, true);

        appUserInsertStatement.setDate(colIndex++, vo.getDate());
        appUserInsertStatement.setDate(colIndex++, vo.getDate());
        appUserInsertStatement.setInt(colIndex++, 0);
        appUserInsertStatement.setBoolean(colIndex++, true);
        appUserInsertStatement.setBoolean(colIndex++, false);
        appUserInsertStatement.setBoolean(colIndex++, false);

        appUserInsertStatement.setLong(colIndex++, vo.getLoggedInUser());
        appUserInsertStatement.setLong(colIndex++, vo.getLoggedInUser());
        appUserInsertStatement.setLong(colIndex, RegistrationStateConstants.VERIFIED);

        appUserInsertStatement.addBatch();
    }

    private void prepareSMA(BDWalkInVO vo) throws Exception {
        int colIndex = 1;

        Long id = vo.getSequenceId();
        smaInsertStatement.setLong(colIndex++, id);
        smaInsertStatement.setLong(colIndex++, id);
        smaInsertStatement.setLong(colIndex++, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        smaInsertStatement.setLong(colIndex++, BankConstantsInterface.OLA_BANK_ID);
        smaInsertStatement.setString(colIndex++, "i8_wc_" + vo.getCnic());
        smaInsertStatement.setBoolean(colIndex++, true);
        smaInsertStatement.setBoolean(colIndex++, true);

        smaInsertStatement.setBoolean(colIndex++, true);
        smaInsertStatement.setBoolean(colIndex++, false);

        smaInsertStatement.setDate(colIndex++, vo.getDate());
        smaInsertStatement.setDate(colIndex++, vo.getDate());
        smaInsertStatement.setLong(colIndex++, vo.getLoggedInUser());
        smaInsertStatement.setLong(colIndex++, vo.getLoggedInUser());
        smaInsertStatement.setLong(colIndex, 0L);

        smaInsertStatement.addBatch();
    }

    private void prepareAccountHolder(BDWalkInVO vo) throws Exception {
        int colIndex = 1;

        Long id = vo.getSequenceId();
        accountHolderInsertStatement.setLong(colIndex++, id);
        accountHolderInsertStatement.setString(colIndex++, vo.getCnic()+"-W");
        accountHolderInsertStatement.setString(colIndex++, vo.getMobile());
        accountHolderInsertStatement.setString(colIndex++, vo.getMobile());
        accountHolderInsertStatement.setString(colIndex++, BatchUtil.ENC_DATE_01_01_1970);
        accountHolderInsertStatement.setString(colIndex++, " - ");
        accountHolderInsertStatement.setString(colIndex++, " - ");
        accountHolderInsertStatement.setString(colIndex++, " - ");
        accountHolderInsertStatement.setString(colIndex++, " - ");

        accountHolderInsertStatement.setString(colIndex++, " - ");

        accountHolderInsertStatement.setDate(colIndex++, vo.getDate());
        accountHolderInsertStatement.setDate(colIndex++, vo.getDate());
        accountHolderInsertStatement.setInt(colIndex, 0);

        accountHolderInsertStatement.addBatch();
    }

    private void prepareAccount(BDWalkInVO vo) throws Exception {
        int colIndex = 1;

        Long id = vo.getSequenceId();

        accountInsertStatement.setLong(colIndex++, id);
        accountInsertStatement.setLong(colIndex++, id);
        accountInsertStatement.setString(colIndex++, com.inov8.ola.util.EncryptionUtil.encryptAccountNo(vo.getSequenceId().toString()));
        accountInsertStatement.setLong(colIndex++, CustomerAccountTypeConstants.WALK_IN_CUSTOMER);
        accountInsertStatement.setString(colIndex++, BatchUtil.ENC_ZERO);
        accountInsertStatement.setLong(colIndex++, 1L);

        accountInsertStatement.setDate(colIndex++, vo.getDate());
        accountInsertStatement.setDate(colIndex++, vo.getDate());
        accountInsertStatement.setInt(colIndex, 0);

        accountInsertStatement.addBatch();
    }

    private void prepareDisbursement(BDWalkInVO vo) throws Exception {
        disbursementStatement.setString(1, "Successful");
        disbursementStatement.setLong(2, vo.getLoggedInUser());
        disbursementStatement.setDate(3, vo.getDate());
        disbursementStatement.setLong(4, vo.getDisbursementId());

        disbursementStatement.addBatch();
    }

    private void executeBatch() throws Exception {
        walkInInsertStatement.executeBatch();
        appUserInsertStatement.executeBatch();
        smaInsertStatement.executeBatch();
        accountHolderInsertStatement.executeBatch();
        accountInsertStatement.executeBatch();
        disbursementStatement.executeBatch();

        connection.commit();

        long ended = System.currentTimeMillis();
        logger.info("Batch No. " + batchNo + " Batch size " + batchCounter + " Executed at " + new Date(ended) +
                " Took " + (ended - startedAt)/1000.0d +" seconds.");

        cleanUpBatch();

        executed = true;
        batchCounter = 0;
        batchNo ++;
        startedAt = System.currentTimeMillis();
    }

    private void cleanUpBatch() throws Exception {
        cleanUpStatement(walkInInsertStatement);
        cleanUpStatement(appUserInsertStatement);
        cleanUpStatement(smaInsertStatement);
        cleanUpStatement(accountHolderInsertStatement);
        cleanUpStatement(accountInsertStatement);
        cleanUpStatement(disbursementStatement);
    }

    private void cleanUpStatement(PreparedStatement ps) throws Exception {
        ps.clearBatch();
        ps.close();
    }

    public void closeBatch() throws Exception {
        if(!executed) {
            executeBatch();
        }
    }

    private static final String LOAD_BULK_DISBURSEMENT =
            "SELECT BD.BULK_DISBURSEMENTS_ID, BD.CNIC, BD.MOBILE_NO "+
            "FROM BULK_DISBURSEMENTS BD, BULK_DISBURSEMENTS_FILE_INFO FI " +
            "WHERE BD.BULK_DISB_FILE_INFO_ID = FI.BULK_DISB_FILE_INFO_ID AND BD.BULK_DISB_FILE_INFO_ID = ? "+
            "AND FI.STATUS = ? AND BD.IS_VALID_RECORD = ? AND BD.DELETED = ? AND BD.ACCOUNT_CREATION_STATUS IS NULL ";

    private static final String WALK_IN_CREATION_STATS =
            "select valid_records, created from (" +
            "(select valid_records from bulk_disbursements_file_info where BULK_DISB_FILE_INFO_ID = ?) valid_records)," +
            "(select count(ACCOUNT_CREATION_STATUS) created from BULK_DISBURSEMENTS where BULK_DISB_FILE_INFO_ID = ? and ACCOUNT_CREATION_STATUS is not null) ";

    private static final String UPDATE_DISBURSEMENT_FILE_STATUS =
            "UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = ? WHERE BULK_DISB_FILE_INFO_ID = ?";

    private static final String WALK_IN_CUSTOMER_INSERT =
            "insert into WALK_IN_CUSTOMER "+
            "(WALK_IN_CUSTOMER_ID, CNIC, MOBILE_NO, CREATED_ON, UPDATED_ON, CREATED_BY, UPDATED_BY, VERSION_NO) "+
            "values (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String APP_USER_INSERT =
            "INSERT INTO APP_USER "+
            "(APP_USER_ID,APP_USER_TYPE_ID, WALK_IN_CUSTOMER_ID, FIRST_NAME,LAST_NAME,USERNAME,PASSWORD,NIC,MOBILE_NO,IS_VERIFIED, "+
            "IS_ACCOUNT_ENABLED,IS_ACCOUNT_EXPIRED,IS_ACCOUNT_LOCKED,IS_CREDENTIALS_EXPIRED, "+
            "CREATED_ON,UPDATED_ON,VERSION_NO,IS_PASSWORD_CHANGE_REQUIRED,IS_CLOSED_UNSETTLED,IS_CLOSED_SETTLED, CREATED_BY, UPDATED_BY, REGISTRATION_STATE_ID) "+
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    private static final String SMA_INSERT =
            "insert into SMART_MONEY_ACCOUNT " +
            "(SMART_MONEY_ACCOUNT_ID, WALK_IN_CUSTOMER_ID, PAYMENT_MODE_ID, BANK_ID, NAME, IS_ACTIVE, IS_CHANGE_PIN_REQUIRED, " +
            "IS_DEF_ACCOUNT, IS_DELETED, CREATED_ON , UPDATED_ON, CREATED_BY, UPDATED_BY, VERSION_NO) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String ACCOUNT_HOLDER_INSERT =
            "insert into ACCOUNT_HOLDER (ACCOUNT_HOLDER_ID, CNIC, MOBILE_NUMBER, LANDLINE_NUMBER, DOB, FIRST_NAME, "+
            "LAST_NAME, MIDDLE_NAME, FATHER_NAME, ADDRESS, CREATED_ON, UPDATED_ON, VERSION_NO) "+
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String ACCOUNT_INSERT =
            "insert into ACCOUNT "+
            "(ACCOUNT_ID, ACCOUNT_HOLDER_ID, ACCOUNT_NUMBER, CUSTOMER_ACCOUNT_TYPE_ID, BALANCE, STATUS_ID, CREATED_ON, UPDATED_ON, VERSION_NO) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    private static final String DISBURSEMENT_UPDATE =
            "UPDATE BULK_DISBURSEMENTS SET ACCOUNT_CREATION_STATUS = ?, UPDATED_BY = ?, UPDATED_ON = ? " +
            "WHERE BULK_DISBURSEMENTS_ID = ? ";
}
