package com.inov8.microbank.server.messaging.listener;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkReversalRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.LinkedHashMap;
import java.util.List;

public class BulkReversalQueueListener implements MessageListener {
    private static Log logger = LogFactory.getLog(BulkReversalQueueListener.class);

    private ManualReversalManager manualReversalManager;
    private ManualReversalManager manualReversalFacade;
    private XmlMarshaller<BulkReversalRefDataModel> xmlMarshaller;
    private WorkFlowExceptionTranslator workflowExceptionTranslator;
    private PortalOlaFacade portalOlaFacade;

    @Override
    public void onMessage(Message message) {
        BulkReversalRefDataModel model = null;
        try {
            if(message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String xml = textMessage.getText();
                if(!GenericValidator.isBlankOrNull(xml))
                {
                    model = xmlMarshaller.unmarshal(xml);

                    logger.info("Started processing Batch ID: " + (model == null ? ("null") : model.getBatchID()));
                    logger.info("Started processing Manual Adjustment against the Transaction Code: " + (model == null ? ("Null TRX_CODE") : model.getTrxnId()));
                    if(model != null && (model.getProcessed() == null ||(model.getProcessed() != null && !model.getProcessed())))
                        this.processBulkManualAdjustments(model);
                }
            }

        }catch(Exception ex){
            logger.error("[BulkManualAdjustmentQueueListener.onMessage] Exception Occurred..."+ex.getMessage(), ex);
            if (model != null){
                logger.error("Going to save error desc. for BulkAdjustmentID:" + model.getTrxnId());

                String reason = MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR);
                if(!StringUtil.isNullOrEmpty(ex.getMessage())){
                    reason = ex.getMessage();
                }
                if(StringUtil.isFailureReasonId(ex.getMessage())){
                    Long failureReasonId = Long.parseLong(ex.getMessage());
                    WorkFlowException wfEx = this.workflowExceptionTranslator.translateWorkFlowException(new WorkFlowException(ex.getMessage()), FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
                    reason = wfEx.getMessage();
                }
                model.setErrorDescription(reason);
//                updateErrorMessage(model.getBulkAdjustmentId(), model.getErrorDescription());
            }
        }finally{
            removeThreadLocals();
        }
    }

    public void processBulkManualAdjustments(BulkReversalRefDataModel model) throws Exception{
        BulkAutoReversalModel bulkAutoReversalModel = new BulkAutoReversalModel();
        bulkAutoReversalModel.setTrxnId(model.getTrxnId());
        bulkAutoReversalModel.setCreatedBy(model.getCreatedBy());
        bulkAutoReversalModel.setCreatedOn(model.getCreatedOn());
//        bulkAutoReversalModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
//        bulkAutoReversalModel.setAdjustmentCategory(2L);
//        bulkAutoReversalModel.setAuthorizerId(model.getAuthorizerId());

        ManualReversalVO manualReversalVO = new ManualReversalVO();
        manualReversalVO.setTransactionCode(model.getTrxnId());
        manualReversalVO.setAdjustmentType(1);
        manualReversalVO.setInitiatorAppUserId(UserUtils.getCurrentUser().getAppUserId());
        Integer adjustmentType = 1;
        boolean isReversal = (adjustmentType != null && adjustmentType == 1) ? true : false;
        BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
        bbStatementAllViewModel.setTransactionCode(model.getTrxnId());
        LinkedHashMap<String, SortingOrder> sortingMap = new LinkedHashMap<String,SortingOrder>();
        sortingMap.put("category", SortingOrder.ASC);
        sortingMap.put("ledgerId", SortingOrder.ASC);
        SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
        sbWrapper.setSortingOrderMap(sortingMap);
        sbWrapper.setBasePersistableModel(bbStatementAllViewModel);
        List<BbStatementAllViewModel>  settlementBBStatementList =  portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();

        if(null!=settlementBBStatementList && settlementBBStatementList.size()>0){
            for (BbStatementAllViewModel modelInList : settlementBBStatementList) {
                modelInList.setAccountNumber(com.inov8.ola.util.EncryptionUtil.decryptAccountNo( modelInList.getAccountNumber()));
                boolean isDebit = (modelInList.getDebitAmount() != null && modelInList.getDebitAmount() > 0) ? true : false;
                if(isDebit){
                    modelInList.setAmountStr(isReversal ? com.inov8.microbank.common.util.Formatter.formatDouble(modelInList.getDebitAmount()) : "");
                    modelInList.setCreditAmount(null);
                    modelInList.setTransactionType(isReversal ? TransactionTypeConstants.CREDIT : null); // reverse the action for reversal only
                }else{
                    modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getCreditAmount()) : "");
                    modelInList.setDebitAmount(null);
                    modelInList.setTransactionType(isReversal ? TransactionTypeConstants.DEBIT : null); // reverse the action for reversal only
                }
            }
        }
        manualReversalVO.setFundTransferEntryList(settlementBBStatementList);
        boolean validReversal = this.validateManualReversalForm(manualReversalVO);
        if (validReversal) {
            manualReversalManager.makeReversal(manualReversalVO);
        }
        model.setProcessed(true);
    }

    private boolean validateManualReversalForm(ManualReversalVO manualReversalVO){

        Integer adjustmentType = manualReversalVO.getAdjustmentType();
        if(adjustmentType == null || adjustmentType.intValue() == 0){
            logger.error("Adjustment Type empty");
            return false;
        }

        if(!StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode())){
            TransactionDetailMasterModel tdmModel = null;
            try {
                tdmModel = this.manualReversalFacade.getTransactionDetailMasterModel(manualReversalVO.getTransactionCode());
            } catch (FrameworkCheckedException e) {
                logger.error("Exception while loading TransactionDetailMasterModel for trx ID:"+manualReversalVO.getTransactionCode());
                return false;
            }
            if(tdmModel == null){
                logger.error("Invalid Trx ID provided:"+manualReversalVO.getTransactionCode());
                return false;
            }

            if(SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue() == tdmModel.getSupProcessingStatusId() && adjustmentType.intValue() == 1){
                logger.error("Transaction status is already Reverse Completed for trx ID:"+manualReversalVO.getTransactionCode());
                manualReversalVO.setIsReversed("1");
                return false;
            }

            // 08-MAR-2017 check commented on request of JSBL
			/*if(adjustmentType.intValue() == 1 &&
					(tdmModel.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.BULK_PAYMENT
							) && (tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.COMPLETED ||
							tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.REVERSE_COMPLETED)){

				logger.error("Transaction reversal is not allowed for " + tdmModel.getProductName() +  "  with status completed / reversed-completed");
				super.saveMessage(req,"Transaction reversal is not allowed for " + tdmModel.getProductName() + " with status completed / reversed-completed ");
				return false;
			}*/

        }


        List<BbStatementAllViewModel> entryList = manualReversalVO.getFundTransferEntryList();
        if(entryList==null || entryList.size() == 0){
            logger.error("Entry list is empty");
            return false;
        }

        double totalDebit = 0;
        double totalCredit = 0;
        double fmtAmount = 0;

        for(BbStatementAllViewModel entry: entryList){
            BBAccountsViewModel model = new BBAccountsViewModel();
            if(StringUtil.isNullOrEmpty(entry.getAccountNumber())){
                logger.error("Account Number is empty");
                return false;
            }else{
                try {
                    model.setAccountNumber(com.inov8.ola.util.EncryptionUtil.encryptWithDES(entry.getAccountNumber()));
                    model = manualReversalManager.getBBAccountsViewModel(model);
                    if(model !=null){
                        if(model.getTitle() == null || model.getTitle().equals("null") || model.getTitle().trim().equals("")){
                            logger.error("Account does not exist against the  account number : " + entry.getAccountNumber());
                            return false;
                        }else if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
                            logger.info("Settlement Acc Type loaded against accNumber:"+entry.getAccountNumber()+" ... so SKIPPING account status/active check");
                        }else{
                            if( model.getIsActive()== null || !model.getIsActive() ){
                                logger.error("Account is not active against the account number : " + entry.getAccountNumber());
                                return false;
                            }
                            if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
                                logger.error("Account Status is not active against the account number : " + entry.getAccountNumber() );
                                return false;
                            }
                            if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
                                logger.error("Account is CLOSED against the account number : " + entry.getAccountNumber());
                                return false;
                            }
                        }

                    }

                    else{
                        logger.error(entry.getAccountNumber() + "Account Number does not exist");
                        return false;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(StringUtil.isNullOrEmpty(entry.getAmountStr())){
                logger.error("Amount is empty");
                return false;
            }else{
                try{
                    entry.setAmount(parseAmount(entry.getAmountStr()));
                }catch(NumberFormatException e){
                    logger.error("Manual Reversal: Invalid Amount:"+entry.getAmountStr(),e);
                    return false;
                }
                fmtAmount = formatAmount(entry.getAmount());
                entry.setAmount(fmtAmount);
                if(fmtAmount < 0.01D){
                    logger.error("Manual Reversal: Amount cannot be less than 0.01");
                    return false;
                }
                if(fmtAmount > 999999999999.99D){
                    logger.error("Manual Reversal: Amount cannot be greater than 999999999999.99");
                    return false;
                }
            }
            if(entry.getTransactionType() == null){
                logger.error("Transaction type is empty");
                return false;
            }

            if(entry.getTransactionType().longValue() == TransactionTypeConstants.DEBIT){
                totalDebit += fmtAmount;
            }else{
                totalCredit += fmtAmount;
            }
        }

        if(totalDebit == 0){
            logger.error("Total debit amount is 0");
            return false;
        }else if(totalCredit == 0){
            logger.error("Total credit amount is 0");
            return false;
        }else if(formatAmount(totalDebit).longValue() != formatAmount(totalCredit).longValue()){
            logger.error("Total debit and credit amount is not equal... Total Debit:"+totalDebit+" Total Credit:"+totalCredit);
            return false;
        }

        manualReversalVO.setTotalAmount(totalDebit);

        return true;
    }

    private Double parseAmount(String amountStr){
        if(StringUtil.isNullOrEmpty(amountStr)){
            return 0.0D;
        }else{
            return Double.parseDouble(amountStr);
        }
    }

    private Double formatAmount(Double amount){
        if(amount == null){
            return 0.0D;
        }else{
            String amt = Formatter.formatDouble(amount);
            return Double.parseDouble(amt);
        }
    }

    private void removeThreadLocals(){
        ThreadLocalAppUser.remove();
        ThreadLocalUserDeviceAccounts.remove();
        ThreadLocalActionLog.remove();
    }


    public void setXmlMarshaller(XmlMarshaller<BulkReversalRefDataModel> xmlMarshaller)
    {
        this.xmlMarshaller = xmlMarshaller;
    }

    public void setWorkflowExceptionTranslator(
            WorkFlowExceptionTranslator workflowExceptionTranslator) {
        this.workflowExceptionTranslator = workflowExceptionTranslator;
    }

    public void setManualReversalManager(ManualReversalManager manualReversalManager) {
        this.manualReversalManager = manualReversalManager;
    }

    public void setManualReversalFacade(ManualReversalManager manualReversalFacade) {
        this.manualReversalFacade = manualReversalFacade;
    }

    public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
        this.portalOlaFacade = portalOlaFacade;
    }
}
