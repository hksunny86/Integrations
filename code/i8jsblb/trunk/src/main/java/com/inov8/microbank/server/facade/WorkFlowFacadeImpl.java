package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;

public class WorkFlowFacadeImpl implements WorkFlowFacade {

    private WorkFlowController workFlowController;
    private WorkFlowExceptionTranslator workflowExceptionTranslator;
    protected TransactionModuleManager txManager;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private static final Log logger = LogFactory.getLog(WorkFlowFacadeImpl.class);
    private MessageSource messageSource;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
    private CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor;
    private ThirdPartyCashOutQueingPreProcessor thirdPartyCashOutQueingPreProcessor;


    public WorkFlowFacadeImpl() {
    }

    public WorkFlowWrapper workflowProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
        boolean isSuccessful = false;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Inside workflowProcess(WorkFlowWrapper workFlowWrapper) method of  WorkFlowFacadeImpl .. ");
                logger.debug("Going to execute workFlowController.workflowProcess(workFlowWrapper)..");
            }
            logger.info("Going to start Transaction Process for Product-Id: " + workFlowWrapper.getProductModel().getProductId() + " at Time :: " + new Date());
            workFlowWrapper = workFlowController.workflowProcess(workFlowWrapper);
            logger.info("Transaction Process completed for Product-Id: " + workFlowWrapper.getProductModel().getProductId() + " at Time :: " + new Date());
            loadAndForwardAccountToQueue(workFlowWrapper);
            logger.info("Response Received from Queue for Product-Id: " + workFlowWrapper.getProductModel().getProductId() + " at Time :: " + new Date());
            ProductModel productModel = workFlowWrapper.getProductModel();
            Long productId = null;
            if (productModel != null && productModel.getProductId() != null)
                productId = productModel.getProductId();
            if (productId != null
                    && (productId.longValue() == ProductConstantsInterface.TRANSFER_OUT
                    || productId.longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT
                    || productId.longValue() == ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT
                    || productId.longValue() == ProductConstantsInterface.RELIEF_FUND_PRODUCT

                    || productId.longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT
                    || UtilityCompanyEnum.contains(productId.toString())
                    || OneBillProductEnum.contains(productId.toString())
                    || OtherThanOneBillProductEnum.contains(productId.toString())
                    || InternetCompanyEnum.contains(productId.toString())
                    || NadraCompanyEnum.contains(productId.toString())
                    || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION)

                    || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT)
                    || productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)

                    || productId.equals(ProductConstantsInterface.CUSTOMER_BALUCHISTAN_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM)
                    || productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)
                    ||productId.equals(ProductConstantsInterface.CUST_IHL_ISLAMABAD_CAMP)
                    ||productId.equals(ProductConstantsInterface.CUST_IHL_Gujranawala_CAMP)
                    ||productId.equals(ProductConstantsInterface.IHL_Gujranawala_CAMP)
                    ||productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)
                    ||productId.equals(ProductConstantsInterface.IHL_ISLAMABAD_CAMP)
                    ||productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)
                    || productModel.getProductId().longValue() == 50056L
                    || productModel.getProductId().longValue() == 50055L
                    || productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)
                    || productId.equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)
            )) {

                loadAndForwardCoreAdviceToQueue(workFlowWrapper);
            }

            isSuccessful = true;
        } catch (Exception e) {
            logger.error("[WorkFlowFacadeImpl.workflowProcess] Exception:" + e.getMessage(), e);

            String reason = this.messageSource.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR, null, null);
            Long failureReasonId = FailureReasonsConstantsInterface.GENERAL_ERROR;
            Long transactionTypeId = workFlowWrapper.getTransactionTypeModel().getTransactionTypeId();
            if ((transactionTypeId.equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)
                    || transactionTypeId.equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX))
                    && !workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.EOBI_CASH_OUT)
                    && !e.getMessage().equalsIgnoreCase("Transaction Id Already Exist")
                    && ((workFlowWrapper.getObject("I8SB_RESPONSE_CODE") != null && (workFlowWrapper.getObject("I8SB_RESPONSE_CODE").equals("I8SB-400")
                    || workFlowWrapper.getObject("I8SB_RESPONSE_CODE").equals("I8SB-500")))
                    || (workFlowWrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRX_INTERNAL_ERROR) != null
                    && workFlowWrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRX_INTERNAL_ERROR).equals("1")))
                    ) {
                loadAndForwardToReversalQueue(workFlowWrapper);
            }

            if (e instanceof WorkFlowException || e instanceof CommandException || e instanceof FrameworkCheckedException) {
                if (StringUtil.isFailureReasonId(e.getMessage())) {
                    failureReasonId = Long.parseLong(e.getMessage());
                    WorkFlowException wfEx = this.workflowExceptionTranslator.translateWorkFlowException(new WorkFlowException(e.getMessage()), FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
                    reason = wfEx.getMessage();
                    this.saveFailureReason(workFlowWrapper, failureReasonId, reason);
                    throw wfEx;
                } else {
                    if (e instanceof FrameworkCheckedException
                            && e.getMessage() != null
                            && e.getMessage().indexOf("Exception") != -1) {
                        this.saveFailureReason(workFlowWrapper, failureReasonId, reason);
                        throw new WorkFlowException(reason);
                    } else if (e instanceof CommandException) {
                        reason = e.getMessage();
                        String isNadraError = null;
                        String errorCounter = null;
                        if (null != workFlowWrapper.getObject("IS_NADRA_ERROR"))
                            isNadraError = (String) workFlowWrapper.getObject("IS_NADRA_ERROR");
                        if (workFlowWrapper.getObject(CommandFieldConstants.KEY_ERROR_COUNTER) != null)
                            errorCounter = (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ERROR_COUNTER);
                        logger.error("Error for CommandException :: " + reason + "\nwith Error Code :: " + isNadraError + " and Error_Counter :: " + errorCounter);
                        if (workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.BISP_CASH_OUT)
                                && null != workFlowWrapper.getObject("IS_NADRA_ERROR")
                                && (workFlowWrapper.getObject(CommandFieldConstants.KEY_ERROR_COUNTER) != null && workFlowWrapper.getObject(CommandFieldConstants.KEY_ERROR_COUNTER).equals("3"))
                                && isNadraError != null && isNadraError.equals(ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString())) {
                            this.saveFailureReason(workFlowWrapper, null, e.getMessage()); // for RDV related errors
                        } else if (isNadraError != null && isNadraError.equals(ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString())) {
                            this.saveFailureReason(workFlowWrapper, null, e.getMessage()); // for RDV related errors
                        } else {
                            this.saveFailureReason(workFlowWrapper, null, e.getMessage());
                        }
                        if (isNadraError == null)
                            isNadraError = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
                        throw new CommandException(reason, Long.valueOf(isNadraError),
                                ErrorLevel.MEDIUM, new Throwable());
                    } else {
                        this.saveFailureReason(workFlowWrapper, null, e.getMessage()); // for RDV related errors
                        throw e;
                    }
                }

            } else {
                String details = "Untranslated Exception Class: " + e.getClass().getName() + " : Error Message: " + e.getMessage();
                logger.error("**** In WorkFlowFacadeImpl: Untranslated Exception Found... saving and throwing General Exception Instead *****\n" + details);
                this.saveFailureReason(workFlowWrapper, failureReasonId, reason);
                throw new WorkFlowException(reason);
            }
        } finally {
            //In-case of Transfer IN if 220 or 404 response is received from RDV, then send Debit Reversal Advice
            ProductModel productModel = workFlowWrapper.getProductModel();
            if (!isSuccessful && productModel != null && productModel.getProductId() != null
                    && (productModel.getProductId().longValue() == ProductConstantsInterface.TRANSFER_IN)) {

                loadAndForwardCoreAdviceToQueue(workFlowWrapper);
            }

        }

        return workFlowWrapper;
    }

    private void loadAndForwardAccountToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    private void loadAndForwardCoreAdviceToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        coreAdviceQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    private void loadAndForwardToReversalQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        thirdPartyCashOutQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    public void setWorkFlowController(WorkFlowController workFlowController) {
        this.workFlowController = workFlowController;
    }

    public void setWorkflowExceptionTranslator(WorkFlowExceptionTranslator workflowExceptionTranslator) {
        this.workflowExceptionTranslator = workflowExceptionTranslator;
    }

    public void setTxManager(TransactionModuleManager txManager) {
        this.txManager = txManager;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    /*
     * Saves TransactionDetailMasterModel(with Failure Reason) in case of Failure of Single-Leg transaction
     * Also saves failure reason in TransactionCodeModel
     */
    private void saveFailureReason(WorkFlowWrapper workFlowWrapper, Long failureReasonId, String reason) {

        boolean saveTrxDetailMaster = true;

        try {
            if (workFlowWrapper.getTransactionDetailMasterModel() != null && workFlowWrapper.getProductModel() != null) {
                if (workFlowWrapper.isLeg2Transaction()) {
                    logger.error("!!!!!!!!!! Skip the saving of TransactionDetailMasterModel for Failed Leg II. Trx ID:" + workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());
                    saveTrxDetailMaster = false;
					/*
					Long currentTrxStatus = workFlowWrapper.getCurrentSupProcessingStatusId();
					if(currentTrxStatus != null 
							&& (SupplierProcessingStatusConstants.IN_PROGRESS.equals(currentTrxStatus)
								|| SupplierProcessingStatusConstants.UNCLAIMED.equals(currentTrxStatus)
								|| SupplierProcessingStatusConstants.REVERSED.equals(currentTrxStatus))){
					
						logger.info("[Leg II] Going to mark TransactionDetailMasterModel.SupProcessingStatus:" + currentTrxStatus + " for Trx ID:"+workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());
						workFlowWrapper.getTransactionDetailMasterModel().setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(currentTrxStatus));
						workFlowWrapper.getTransactionDetailMasterModel().setSupProcessingStatusId(currentTrxStatus);
					}else{
						logger.error("!!!!!!!!!! CurrentSupProcessingStatusId is INVALID --> ["+currentTrxStatus+"] for Leg II of Trx ID:"+workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());
						logger.error("!!!!!!!!!! Skip the saving of TransactionDetailMasterModel for failed Trx ID:"+workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());
						saveTrxDetailMaster = false;
					}
					*/
                } else {
                    logger.info("[Leg I] Going to mark TransactionDetailMasterModel.SupProcessingStatus = Failed for Trx ID:" + workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());
                    // For all single Leg/1st Leg transactions, TransactionDetailId must be null
                    TransactionDetailMasterModel detailMasterModel = workFlowWrapper.getTransactionDetailMasterModel();
                    Long tdmPK = null;
                    if (detailMasterModel != null && detailMasterModel.getPk() != null)
                        tdmPK = detailMasterModel.getPk();
                    workFlowWrapper.getTransactionDetailMasterModel().setPk(tdmPK);
                    workFlowWrapper.getTransactionDetailMasterModel().setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(SupplierProcessingStatusConstants.FAILED));
                    workFlowWrapper.getTransactionDetailMasterModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
                    workFlowWrapper.getTransactionDetailMasterModel().setFailureReasonId(failureReasonId);
                    workFlowWrapper.getTransactionDetailMasterModel().setFailureReason(reason);
                    if (!StringUtil.isNullOrEmpty(workFlowWrapper.getProductModel().getName())) {
                        workFlowWrapper.getTransactionDetailMasterModel().setProductName(workFlowWrapper.getProductModel().getName());
                    }
                    //Just for Collection payment to save consumer # even in faliure case
                    ProductModel productModel = workFlowWrapper.getProductModel();
                    if (productModel != null) {
                        Long productId = productModel.getProductId();
                        if (productId != null && (productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)
                                || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)
                                || productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION)
                                || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT)
                                || productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)
                                || productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)
                                || productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)
                                || productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)
                                || productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)
                                || productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)

                                || productId.equals(ProductConstantsInterface.CUSTOMER_BALUCHISTAN_ET_COLLECTION)
                                || productId.equals(ProductConstantsInterface.CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM)
                                || productId.equals(ProductConstantsInterface.IHL_ISLAMABAD_CAMP)
                                || productId.equals(ProductConstantsInterface.IHL_Gujranawala_CAMP)
                                || productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)
                                || productId.equals(ProductConstantsInterface.CUST_IHL_Gujranawala_CAMP)
                                || productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)
                                || productId.equals(ProductConstantsInterface.CUST_IHL_ISLAMABAD_CAMP)
                                || productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)
                                || productId.equals(50055L) || productId.equals(50056L))) {
                            workFlowWrapper.getTransactionDetailMasterModel().setConsumerNo(workFlowWrapper.getTransactionDetailModel().getConsumerNo());
                        }
                    }
                }

                if (saveTrxDetailMaster) {
                    transactionDetailMasterManager.saveTransactionDetailMaster(workFlowWrapper.getTransactionDetailMasterModel());
                }
            }

        } catch (Exception exp) {
            logger.error("!!!!! Unable to update TransactionDetailMaster status to Failed. Reason:" + exp.getMessage(), exp);
        }


        if (workFlowWrapper.getTransactionCodeModel() != null && workFlowWrapper.getTransactionCodeModel().getTransactionCodeId() > 0) {

            workFlowWrapper.getTransactionCodeModel().setFailureReasonId(failureReasonId);
            workFlowWrapper.getTransactionCodeModel().setReason(reason);

            try {
                txManager.updateTransactionCode(workFlowWrapper);
            } catch (DataIntegrityViolationException dive) {
                workFlowWrapper.getTransactionCodeModel().setFailureReasonId(FailureReasonsConstantsInterface.GENERAL_ERROR);
                workFlowWrapper.getTransactionCodeModel().setReason("Failure Reason not defined: " + failureReasonId);
                try {
                    txManager.updateTransactionCode(workFlowWrapper);
                } catch (Exception e) {
                    logger.error("2nd try Failed... Still Unable to updateTransactionCode for General failure Reason... Exception:", e);
                }
            } catch (Exception ex) {
                logger.error("Unable to updateTransactionCode for failure Reason:" + failureReasonId + " Exception:", ex);
            }
        }
    }

    public void setCreditAccountQueingPreProcessor(
            CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
        this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
    }

    public void setCoreAdviceQueingPreProcessor(
            CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor) {
        this.coreAdviceQueingPreProcessor = coreAdviceQueingPreProcessor;
    }

    public void setThirdPartyCashOutQueingPreProcessor(ThirdPartyCashOutQueingPreProcessor thirdPartyCashOutQueingPreProcessor) {
        this.thirdPartyCashOutQueingPreProcessor = thirdPartyCashOutQueingPreProcessor;
    }
}
