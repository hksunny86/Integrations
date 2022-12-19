package com.inov8.microbank.server.service.workflow.controller;

import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.workflow.factory.WorkFlowTransactionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Title: </p>
 * <p>
 * <p>Description: </p>
 * <p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class WorkFlowControllerImpl implements WorkFlowController {
    WorkFlowTransactionFactory workFlowTransactionFactory;
    protected final transient Log log = LogFactory.getLog(WorkFlowControllerImpl.class);

    public WorkFlowControllerImpl() {

    }

    public WorkFlowWrapper workflowProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
        log.debug("Inside workflowProcess(WorkFlowWrapper workFlowWrapper) method of WorkFlowControllerImpl...");

        TransactionController txController = null;
        try {
            txController = workFlowTransactionFactory.
                    getTransactionProcessor(workFlowWrapper);
            log.debug("Going to execute txController.start(workFlowWrapper)...;");
            workFlowWrapper = txController.start(workFlowWrapper);
            log.debug("Going to execute txController.process(workFlowWrapper)...;");
            workFlowWrapper = txController.process(workFlowWrapper);
            log.debug("Going to execute txController.end(workFlowWrapper)...;");
            workFlowWrapper = txController.end(workFlowWrapper);
            return workFlowWrapper;
        } catch (Exception e) {
            /**
             * The following code rolls back the transaction and was
             * implemented against CRF 019 but is reverted back now
             * should be uncommented and SHOULD BE TESTED if we want to
             * add the rollback functionality at any time in future
             * --- Maqsood Shahzad
             */
            try {
                log.error("Error in WorkFlowControllerImpl :: " + e + " \nException :: " + e.getMessage());
                //for Two leg transactions, if transaction fails in second leg, then donot fail transaction as it can be performed again.
                if (workFlowWrapper.getTransactionDetailMasterModel() != null && workFlowWrapper.getProductModel() != null &&
                        !(workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH
                                || workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER
// Cash Withdrawal is single leg, hence commenting || workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_WITHDRAWAL
                                || workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BULK_PAYMENT)) {
                    TransactionDetailMasterModel transactionDetailMasterModel = workFlowWrapper.getTransactionDetailMasterModel();
                    if (workFlowWrapper.getProductModel() != null) {
                        transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(SupplierProcessingStatusConstants.FAILED));
                        transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
                        transactionDetailMasterModel.setProductId(workFlowWrapper.getProductModel().getProductId());
                        transactionDetailMasterModel.setSupplierId(workFlowWrapper.getProductModel().getSupplierId());
                        workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
                    }
                }

                txController.rollback(workFlowWrapper);
            } catch (Exception ex) {
                log.error("Exception occured during rollback :: " + ex.getMessage(),ex);
            }

            throw e;

        }
        finally {

        }

/*	Commented By OmarButt + Mudassir on 07-05-2015 to avoid connection not found scenario (on BOP)
	finally{
    	**
    	 * Perform Updates after each transaction whether it is successfully performed or rollbacked...
    	 * For example, Transaction Detail Master table updates (used in reporting) 
    	 *
    	txController.postUpdate(workFlowWrapper);
    }*/

    }

    public void setWorkFlowTransactionFactory(WorkFlowTransactionFactory
                                                      workFlowTransactionFactory) {
        this.workFlowTransactionFactory = workFlowTransactionFactory;
    }

}
