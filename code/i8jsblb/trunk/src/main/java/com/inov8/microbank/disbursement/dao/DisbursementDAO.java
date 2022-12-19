package com.inov8.microbank.disbursement.dao;

import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Date;

import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;


public class DisbursementDAO {

    private Logger logger = Logger.getLogger(DisbursementDAO.class.getName());
    private Connection connection;
    private PreparedStatement preparedStatement;
    private int batchCounter;
    private boolean executed;

    public DisbursementDAO(Connection connection) {
        this.connection = connection;
    }

    public void initializeBatch() throws Exception {

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO BULK_DISBURSEMENTS ( ");

        query.append("BULK_DISBURSEMENTS_ID, EMPLOYEE_NO, NAME, MOBILE_NO, CHEQUE_NO, AMOUNT, PAYMENT_DATE, DESCRIPTION, SERVICE_ID, IS_VALID_RECORD, REASON, PRODUCT_ID, BATCH_NUMBER, DELETED, POSTED, SETTLED, ");
        query.append("CREATION_DATE, UPDATED_ON, CREATED_BY, UPDATED_BY, IS_BIOMETRIC_REQUIRED, LIMIT_APPLICABLE, CNIC, APP_USER_ID, BULK_DISB_FILE_INFO_ID, ACCOUNT_CREATION_STATUS, FAILURE_REASON, ");
        query.append("PAY_CASH_VIA_CNIC, POSTED_ON, SETTLED_ON, TRANSACTION_CODE, VERSION_NO, FED, CHARGES, ISAPPROVED");

        query.append(") VALUES (BULK_DISBURSEMENTS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");

        preparedStatement = connection.prepareStatement(query.toString());
    }

    public void closeBatch() throws Exception {

        if(!executed) {

            executeBatch();
        }
    }

    private void executeBatch() throws Exception {

        ParameterMetaData metaData = preparedStatement.getParameterMetaData();

        preparedStatement.executeBatch();
        preparedStatement.clearBatch();
        preparedStatement.close();
//        if(connection != null)
//            connection.close();
        preparedStatement = null;
        executed = true;
        batchCounter = 0;
    }

    public void runDisbusementBatch(String[] record) throws Exception {

        String[] columns = {"EMPLOYEE_NO", "NAME", "MOBILE_NO", "CHEQUE_NO", "AMOUNT", "PAYMENT_DATE", "DESCRIPTION", "SERVICE_ID", "IS_VALID_RECORD", "REASON",
                "PRODUCT_ID", "BATCH_NUMBER", "DELETED", "POSTED", "SETTLED", "CREATION_DATE", "UPDATED_ON", "CREATED_BY", "UPDATED_BY",
                "IS_BIOMETRIC_REQUIRED", "LIMIT_APPLICABLE", "CNIC", "APP_USER_ID","BULK_DISB_FILE_INFO_ID", "ACCOUNT_CREATION_STATUS", "FAILURE_REASON", "PAY_CASH_VIA_CNIC", "POSTED_ON", "SETTLED_ON",
                "TRANSACTION_CODE", "VERSION_NO", "FED", "CHARGES", "ISAPPROVED"};

        if(batchCounter >= 1000) {
            executeBatch();
            initializeBatch();
        }

        int index = 1;

        for(String value : record) {

            String column = columns[index-1];

            if(column.equalsIgnoreCase("PAYMENT_DATE") || column.equalsIgnoreCase("CREATION_DATE") || column.equalsIgnoreCase("UPDATED_ON") || column.equalsIgnoreCase("POSTED_ON") || column.equalsIgnoreCase("SETTLED_ON") ) {

                if(StringUtil.isNullOrEmpty(value)) {

                    preparedStatement.setNull(index, Types.DATE);
                    index++;

                } else {

                    preparedStatement.setDate(index, convertStringToDate(value));
                    index++;
                }

            } else {

                preparedStatement.setObject(index, value);
                index++;
            }

        }

        preparedStatement.addBatch();
        executed = false;
        batchCounter++;
    }

    public boolean isExecuted() {

        return executed;
    }


    public static java.sql.Date convertStringToDate(String newDate)
    {
        java.sql.Date returnValue = null;
        try
        {
            Date paymentDate = PortalDateUtils.parseStringAsDate(newDate, CSV_DATE_FORMAT);
            returnValue = new java.sql.Date(paymentDate.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnValue;
    }


    public void processBulkDibursementFile(java.util.List<String[]> records, Connection connection) throws Exception {

        logger.info("Process Started.");

        initializeBatch();

        for(String[] record : records) {

            runDisbusementBatch(record);
        }

        if (!isExecuted()) {
            executeBatch();
        }

        closeBatch();

    }
}