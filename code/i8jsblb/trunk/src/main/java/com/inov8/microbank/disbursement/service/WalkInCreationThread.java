package com.inov8.microbank.disbursement.service;

import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.dao.DisbursementFileDAO;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.vo.BDWalkInVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;

/**
 * Created by AtieqRe on 2/16/2017.
 */
public class WalkInCreationThread extends Thread {

    private Logger logger = Logger.getLogger(getClass().getName());
    private DisbursementFileInfoViewModel infoViewModel;
    private Long loggedInUserId;
    private Connection connection;

    public WalkInCreationThread(DisbursementFileInfoViewModel infoViewModel, Long loggedInUserId, Connection connection) {
        this.infoViewModel = infoViewModel;
        this.loggedInUserId = loggedInUserId;
        this.connection = connection;
    }

    @Override
    public void run() {
        DisbursementFileDAO dao = new DisbursementFileDAO(connection);

        try {
            ResultSet set = dao.loadBulkDisbursementsModelForWalkInCreation(infoViewModel.getDisbursementFileInfoId());
            if(set == null || set.isClosed()) {
                logger.info("No record found for walk-in creation.");

                return;
            }

            PreparedStatement bulkOpsSeqStatement = connection.prepareStatement("select BULK_OPS_SEQ.nextval from dual");
            Long sequenceId = -1L;
            Date currentDate = new Date(System.currentTimeMillis());

            logger.info("initializing insertion for batch " + infoViewModel.getBatchNumber());
            dao.initializeBatch();
            long start = System.currentTimeMillis();
            while (set.next()) {
                ResultSet sequence = bulkOpsSeqStatement.executeQuery();
                if (sequence.next()) {
                    sequenceId = sequence.getLong(1);
                }

                dao.addToBatch(new BDWalkInVO(set.getLong(1), set.getString(2), set.getString(3), loggedInUserId, sequenceId, currentDate));
            }

            dao.closeBatch();

            logger.info("Disbursement : Walk-in Customer Created in " + (System.currentTimeMillis() - start) / 1000.0d + " seconds.");

            List<Long> results = dao.getWalkInCreationStats(infoViewModel.getDisbursementFileInfoId());
            if(CollectionUtils.isNotEmpty(results)) {
                long validRecords = results.get(0);
                long created = results.get(1);

                if(validRecords == created) {
                    dao.updateDisbursementFileInfo(infoViewModel.getDisbursementFileInfoId(), DisbursementStatusConstants.STATUS_READY_TO_DISBURSE);
                }
            }
        }

        catch (Exception e) {
            logger.error("Error Occurred in Walk-in creation process. Batch No : " + infoViewModel.getBatchNumber() + " Error Message : " + e.getMessage());

            try {
                dao.updateDisbursementFileInfo(infoViewModel.getDisbursementFileInfoId(), DisbursementStatusConstants.STATUS_WALK_IN_CREATION_PAUSED);
            }
            catch (Exception e1) {
                logger.error("Batch status update failed " + e1.getMessage());
            }

            try{
                connection.rollback();
            }

            catch (Exception sql) {
                logger.error("Rollback failed " + sql.getMessage());
            }

            e.printStackTrace();
        }

        finally {
            try {
                connection.commit();
                connection.close();
            }

            catch (Exception e) {
                logger.error("Exception occurred on commit. " + e.getMessage());

                e.printStackTrace();
            }
        }
    }
}
