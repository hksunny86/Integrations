package com.inov8.microbank.server.service.transactionreversal;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;

import java.util.Date;
import java.util.List;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 8, 2012 10:24:01 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface TransactionReversalManager
{
    boolean updateTransaction( Long transactionCodeId, String comments ) throws FrameworkCheckedException;
    SwitchWrapper updateTransactionReversed( Long transactionCodeId, String comments ) throws FrameworkCheckedException;
    WorkFlowWrapper updateTransactionRedeemed(TransactionReversalVo txReversalVo) throws FrameworkCheckedException;

    TransactionReversalVo findTransactionReversalDetail( Long transactionCodeId ) throws FrameworkCheckedException;
    void sendCoreReversalRequiresNewTransaction(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException;
    void makeCoreCreditAdvice(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException;
    void saveFailedAdviceRequiresNewTransaction(MiddlewareRetryAdviceModel model) throws FrameworkCheckedException;
    void saveFailedAdviceRequiresNewTransaction(ThirdPartyRetryAdviceModel model) throws FrameworkCheckedException;
    void updateRetryAdviceReportStatus(Long trxCodeId) throws FrameworkCheckedException;
    
    /*Added by atif hussain*/
    SearchBaseWrapper findRetryAdviceSummaryListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    SearchBaseWrapper findRetryAdviceHistoryListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    BaseWrapper loadRetryAdviceSummary(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper makeCoreRetryAdvice(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper makeCoreRetryAdviceSkipped(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    /*IBFT related stuff*/
    void saveNewIBFTRecord(MiddlewareAdviceVO messageVO) throws FrameworkCheckedException;
    boolean checkAlreadySuccessful(String stan, Date requestTime) throws FrameworkCheckedException;
    void updateIBFTStatus(String stan, Date requestTime, String status, String transactionCode) throws FrameworkCheckedException;
    void saveOrUpdateAccountOpeningStatus(String mobileNo, String cnic, Long accountOpeningStatus, Long isValid, String rrn, String responseCode) throws FrameworkCheckedException;
	public SearchBaseWrapper findIBFTRetryAdviceListView(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	List<IBFTRetryAdviceModel> getIbftRetryAdviceList(String stan, Date requestTime, String status) throws FrameworkCheckedException;
	public BaseWrapper makeIBFTRetryAdvice(Long ibftRetryAdviceId) throws FrameworkCheckedException;
    public SearchBaseWrapper findRetryCreditQueueViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
    public void createSaveAccountCreditQueueLogModel(AccountCreditQueueLogModel accountCreditQueueLogModel);
	public AccountCreditQueueLogModel findAccountCreditQueueLogModel(AccountCreditQueueLogModel _accountCreditQueueLogModel);
	public void makeCoreAdvice(SwitchWrapper switchWrapper, MiddlewareAdviceVO middlewareAdviceVO) throws Exception;
    public void makeThirdPartyCashOutReversalAdvice(SwitchWrapper switchWrapper) throws Exception;

    TransactionDetailMasterModel loadTDMForReversal(BaseWrapper baseWrapper) throws Exception;
    TransactionDetailMasterModel loadTDMForReversalByTransactionCode(String transactionCode) throws Exception;
    void updateTransactionDetailMaster(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    IBFTRetryAdviceModel findIBFTRetryByStanAndStatus(MiddlewareAdviceVO adviceVO) throws FrameworkCheckedException;

    TransactionDetailMasterModel loadTDMbyThridPartyRRN(String rrn) throws Exception;

    TransactionDetailMasterModel loadTDMbyMobileNumber(String mobileNo, String productId) throws FrameworkCheckedException;
    TransactionDetailMasterModel loadTDMbyProductId(String mobileNo, String productId) throws FrameworkCheckedException;

//    List<TransactionDetailMasterModel> loadTDMbyMobileandDateRange(String mobileNo, String startDate, String endDate) throws FrameworkCheckedException;

}
