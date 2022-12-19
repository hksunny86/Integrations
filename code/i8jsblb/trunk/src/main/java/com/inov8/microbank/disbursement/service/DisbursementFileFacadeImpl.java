package com.inov8.microbank.disbursement.service;

import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.common.util.DBManager;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.disbursement.dao.DisbursementFileInfoViewDAO;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.dao.BulkDisbursementDAO;
import com.inov8.microbank.disbursement.dao.BulkDisbursementsFileInfoDAO;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by AtieqRe on 2/16/2017.
 */
public class DisbursementFileFacadeImpl implements DisbursementFileFacade {
    private Logger logger = Logger.getLogger(getClass().getName());
    private SmsSender smsSender;
    private BulkDisbursementDAO bulkDisbursementDAO;
    private DisbursementFileInfoViewDAO disbursementFileInfoViewDAO;
    private BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO;

    @Override
    public DisbursementFileInfoViewModel findDisbursementInfoViewModel(Long pk) {
        return disbursementFileInfoViewDAO.findByPrimaryKey(pk);
    }

    @Override
    public Object getDisbursementFileSettlementStatus(String batchNumber) {
        return  bulkDisbursementsFileInfoDAO.getDisbursementFileSettlementStatus(batchNumber);
    }

    public DisbursementFileInfoViewModel processBatch(Long bulkDisbursementFileInfoId) throws Exception {
        DisbursementFileInfoViewModel infoViewModel = disbursementFileInfoViewDAO.findByPrimaryKey(bulkDisbursementFileInfoId);

        boolean proceed = true;
        if(DisbursementStatusConstants.STATUS_PARSED.longValue() == infoViewModel.getStatus()) {

            Integer nextState = infoViewModel.getServiceId().longValue() == ServiceConstantsInterface.BULK_DISB_ACC_HOLDER ?
                    DisbursementStatusConstants.STATUS_READY_TO_DISBURSE :
                    DisbursementStatusConstants.STATUS_WALK_IN_CREATION_IN_PROGRESS;

            int  count = bulkDisbursementsFileInfoDAO.updateDisbursementFileStatus(bulkDisbursementFileInfoId, nextState);
            proceed = (count > 0);
        }

        if(!proceed) {
            //Unable to update status

            return infoViewModel;
        }

        // TODO need to handle multiple execution of sms sending
        // send sms to all account holders in given batch
        if(ServiceConstantsInterface.BULK_DISB_ACC_HOLDER.longValue() == infoViewModel.getServiceId()) {
            initialSMSForAcHolders(bulkDisbursementFileInfoId);
        }

        else {

            // check if a/c creation is still required
            Object obj = getDisbursementFileSettlementStatus(infoViewModel.getBatchNumber());
            if (obj != null) {
                Object[] results = (Object[]) obj;

                BigDecimal totalCreated = (BigDecimal)results[3];
                BigDecimal validRecords = (BigDecimal)results[4];
                if(validRecords.longValue() != totalCreated.longValue()) {
                    saveWalkInCustomers(infoViewModel, UserUtils.getCurrentUser().getAppUserId());
                }

                else {
                    logger.info("Batch No : " + infoViewModel.getBatchNumber() + " All walk-ins are already created.");

                    bulkDisbursementsFileInfoDAO.updateDisbursementFileStatus(bulkDisbursementFileInfoId, DisbursementStatusConstants.STATUS_READY_TO_DISBURSE);
                }
            }
        }

        infoViewModel = disbursementFileInfoViewDAO.findByPrimaryKey(bulkDisbursementFileInfoId);

        return infoViewModel;
    }

    @Override
    public int cancelBatch(Long disbursementFileInfoId) throws Exception {
        return bulkDisbursementsFileInfoDAO.cancelBatch(disbursementFileInfoId);
    }

    @Override
    public int updateDisbursementFileStatus(Long disbursementFileInfoId, Integer status) throws Exception {
        return bulkDisbursementsFileInfoDAO.updateDisbursementFileStatus(disbursementFileInfoId, status);
    }
    public void initialSMSForAcHolders(Long bulkDisbursementFileInfoId) throws Exception {
        DisbursementSMSSender thread = new DisbursementSMSSender(smsSender, bulkDisbursementDAO, bulkDisbursementFileInfoId);
        thread.start();
    }

    public void saveWalkInCustomers(DisbursementFileInfoViewModel infoViewModel, Long loggedInUserId) throws Exception {
        WalkInCreationThread thread = new WalkInCreationThread(infoViewModel, loggedInUserId, DBManager.getConnection());

        thread.start();
    }

    @Override
    public CustomList<BulkDisbursementsModel> findBulkDibursementsByBatchNumber(BulkDisbursementsModel bulkDisbursementsModel) {
        return bulkDisbursementDAO.findByExample(bulkDisbursementsModel);
    }

    public void setDisbursementFileInfoViewDAO(DisbursementFileInfoViewDAO disbursementFileInfoViewDAO) {
        this.disbursementFileInfoViewDAO = disbursementFileInfoViewDAO;
    }

    public void setBulkDisbursementDAO(BulkDisbursementDAO bulkDisbursementDAO) {
        this.bulkDisbursementDAO = bulkDisbursementDAO;
    }

    public void setBulkDisbursementsFileInfoDAO(BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO) {
        this.bulkDisbursementsFileInfoDAO = bulkDisbursementsFileInfoDAO;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
