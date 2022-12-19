package com.inov8.microbank.server.messaging.listener;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.controller.IBFTSwitchController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.util.DebitCardUtill;
import com.inov8.microbank.debitcard.vo.DebitCardReversalVO;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionReportDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Date;

public class ReversalRequestAdviceListener implements MessageListener {

    private static Log logger = LogFactory.getLog(ReversalRequestAdviceListener.class);

    private TransactionReversalManager transactionReversalManager;
    private CommonCommandManager commonCommandManager;
    private PostedTransactionReportDAO postedTransactionReportDAO;
    private IBFTSwitchController ibftSwitchController;
    private TransactionCodeDAO transactionCodeDAO;
    private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
    private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");

    @Override
    public void onMessage(Message message) {
        Object obj = null;
        int retry = 0;
        DebitCardReversalVO reversalVO = null;
        Boolean isProcessed = Boolean.FALSE;
        TransactionDetailMasterModel tempModel = new TransactionDetailMasterModel();
        try
        {
            retry = (int)((ActiveMQObjectMessage) ((ActiveMQObjectMessage) ((ObjectMessage) message))).getRedeliveryCounter();
            if(message instanceof ObjectMessage)
            {
                obj = ((ObjectMessage)message).getObject();
                if(obj instanceof DebitCardReversalVO)
                {
                    reversalVO = (DebitCardReversalVO) obj;
                    logger.info("Debit Card Transaction Reversal Request Received...");
                    MiddlewareMessageVO middlewareMessageVO= new MiddlewareMessageVO();
                    Long[] productIds = new Long[]{ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US,
                            ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US,
                            ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL,
                    ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US,
                    ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL};
                    UserDeviceAccountsModel uda = null;
                    DebitCardModel debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(reversalVO.getCardPan());
                    if(debitCardModel != null)
                    {
                        DebitCardUtill.verifyDebitCard(new WebServiceVO(),debitCardModel);
                        AppUserModel appUserModel = commonCommandManager.loadAppUserByMobileAndType(debitCardModel.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
                        uda = commonCommandManager.getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
                        middlewareMessageVO.setOrignalStan(reversalVO.getOriginalStan());
                        middlewareMessageVO.setOrignalTransactionDateTime(reversalVO.getReversalRequestTime());
                    }

                    PostedTransactionReportModel postedTransactionReportModel= null;
                    Long transactionCodeId = reversalVO.getTransactionCodeId();
                    if(transactionCodeId == null)
                        postedTransactionReportModel = postedTransactionReportDAO.getTransactionCodeIdForReversalByStanAndUserId(middlewareMessageVO,productIds,uda.getUserId());
                    if(postedTransactionReportModel != null && transactionCodeId == null)
                    {
                        transactionCodeId = postedTransactionReportModel.getTransactionCodeId();
                        reversalVO.setTransactionCodeId(transactionCodeId);
                    }
                    else if(transactionCodeId == null)
                    {
                        throw new Exception("No Transaction Found");
                    }
                    if(postedTransactionReportModel != null || transactionCodeId != null)
                    {
                        if(postedTransactionReportModel != null)
                            transactionCodeId = postedTransactionReportModel.getTransactionCodeId();
                        TransactionCodeModel transactionCodeModel = null;
                        if(transactionCodeId != null && !transactionCodeId.equals(""))
                            transactionCodeModel = transactionCodeDAO.findByPrimaryKey(transactionCodeId);
                        reversalVO.setTransactionCode(transactionCodeModel.getCode());
                        tempModel.setTransactionCode(transactionCodeModel.getCode());
                        BaseWrapper wrapper = new BaseWrapperImpl();
                        wrapper.setBasePersistableModel(tempModel);
                        tempModel = transactionReversalManager.loadTDMForReversal(wrapper);
                        if(tempModel != null && tempModel.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.COMPLETED))
                        {
                            tempModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSAL_IN_PROCESS);
                            tempModel.setProcessingStatusName(SupplierProcessingStatusConstants.REVERSAL_IN_PROCESS_NAME);
                            tempModel.setUpdatedOn(new Date());
                            BaseWrapper baseWrapper = new BaseWrapperImpl();
                            baseWrapper.setBasePersistableModel(tempModel);
                            transactionReversalManager.updateTransactionDetailMaster(baseWrapper);
                            tempModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
                            middlewareMessageVO = new MiddlewareMessageVO();
                            middlewareMessageVO.setProductId(tempModel.getProductId());
                            middlewareMessageVO.setReserved1(tempModel.getTransactionCodeId().toString());
                            ibftSwitchController.cashWithDrawalReversal(middlewareMessageVO);
                            logger.info("Transaction with Code ::  " + tempModel.getTransactionCode() + "Reversed Successfully....");
                        }
                        else if(tempModel != null && tempModel.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.REVERSAL_IN_PROCESS)){
                            logger.error("Transaction Code :: " + tempModel.getTransactionCode() + " is already in Reversal Process.");
                        }
                        else
                        {
                            logger.info("Transaction Status is :: " + tempModel.getProcessingStatusName() + " for Transaction Code :: " + tempModel.getTransactionCode());
                            throw new RuntimeException("Transaction Status is :: " + tempModel.getProcessingStatusName() + " for Transaction Code :: " + tempModel.getTransactionCode());
                        }
                        isProcessed = Boolean.TRUE;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            if(tempModel != null && tempModel.getSupProcessingStatusId() != null
                    && tempModel.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.REVERSAL_IN_PROCESS)){
                tempModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
                tempModel.setProcessingStatusName(SupplierProcessingStatusConstants.COMPLETED_NAME);
                tempModel.setUpdatedOn(new Date());
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(tempModel);
                try {
                    transactionReversalManager.updateTransactionDetailMaster(baseWrapper);
                } catch (FrameworkCheckedException e) {
                    logger.error("Error while Reverting Transaction Status in Reversal Queue");
                    e.printStackTrace();
                }
            }
            throw new RuntimeException(ex.getMessage());
        }
        finally {
            if(retry == 2 && !isProcessed)
            {
                if(obj instanceof DebitCardReversalVO) {
                    try {
                        reversalVO.setProductId(reversalVO.getProductId());
                        this.logFailedRecord((DebitCardReversalVO) obj);
                        //throw new Exception("Failed to process Transaction Reversal.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void logFailedRecord(DebitCardReversalVO debitCardReversalVO) throws Exception{
        MiddlewareRetryAdviceModel model = new MiddlewareRetryAdviceModel();
        model.setConsumerNo(debitCardReversalVO.getCardPan());
        model.setProductId(debitCardReversalVO.getProductId());
        model.setTransactionCode(debitCardReversalVO.getTransactionCode());
        model.setTransactionCodeId(debitCardReversalVO.getTransactionCodeId());
        model.setTransactionAmount(debitCardReversalVO.getReversalAmount());
        model.setStan(debitCardReversalVO.getOriginalStan());
        model.setReversalStan(debitCardReversalVO.getReversalStan());
        model.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
        model.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
        model.setCreatedOn(new Date());
        model.setUpdatedOn(new Date());
        model.setStatus("Failed");
        model.setAdviceType(debitCardReversalVO.getAdviceType());
        model.setRequestTime(new Date());
        transactionReversalManager.saveFailedAdviceRequiresNewTransaction(model);
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public void setPostedTransactionReportDAO(PostedTransactionReportDAO postedTransactionReportDAO) {
        this.postedTransactionReportDAO = postedTransactionReportDAO;
    }

    public void setIbftSwitchController(IBFTSwitchController ibftSwitchController) {
        this.ibftSwitchController = ibftSwitchController;
    }

    public void setTransactionCodeDAO(TransactionCodeDAO transactionCodeDAO) {
        this.transactionCodeDAO = transactionCodeDAO;
    }
}
