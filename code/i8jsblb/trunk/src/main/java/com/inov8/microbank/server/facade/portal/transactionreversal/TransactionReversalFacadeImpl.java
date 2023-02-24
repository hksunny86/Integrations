package com.inov8.microbank.server.facade.portal.transactionreversal;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;

import java.util.Date;
import java.util.List;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 8, 2012 10:29:26 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class TransactionReversalFacadeImpl implements TransactionReversalFacade
{
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    private TransactionReversalManager transactionReversalManager;

    public boolean updateTransaction( Long transactionCodeId, String comments ) throws FrameworkCheckedException
    {
        try
        {
            return transactionReversalManager.updateTransaction( transactionCodeId, comments );
        }
        catch( Exception ex )
        {
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
    }

    public SwitchWrapper updateTransactionReversed( Long transactionCodeId, String comments ) throws FrameworkCheckedException
    {
        try
        {
            return transactionReversalManager.updateTransactionReversed( transactionCodeId, comments );
        }
        catch( Exception ex )
        {
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
    }
    
    public WorkFlowWrapper updateTransactionRedeemed(TransactionReversalVo txReversalVo) throws FrameworkCheckedException{
    	try
        {
            return transactionReversalManager.updateTransactionRedeemed(txReversalVo);
        }
        catch( Exception ex )
        {
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
    }

    public TransactionReversalVo findTransactionReversalDetail( Long transactionCodeId ) throws FrameworkCheckedException
    {
        try
        {
            return transactionReversalManager.findTransactionReversalDetail( transactionCodeId );
        }
        catch( Exception ex )
        {
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
    }

    public void setFrameworkExceptionTranslator( FrameworkExceptionTranslator frameworkExceptionTranslator )
    {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

    public void setTransactionReversalManager( TransactionReversalManager transactionReversalManager )
    {
        this.transactionReversalManager = transactionReversalManager;
    }

	@Override
	public void sendCoreReversalRequiresNewTransaction(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException {
		transactionReversalManager.sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);
	}
	
	@Override
	public void makeCoreCreditAdvice(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException {
		transactionReversalManager.makeCoreCreditAdvice(middlewareAdviceVO);
	}
	

	@Override
	public void saveFailedAdviceRequiresNewTransaction(MiddlewareRetryAdviceModel model) throws FrameworkCheckedException {
		transactionReversalManager.saveFailedAdviceRequiresNewTransaction(model);
	}

	@Override
	public void saveFailedAdviceRequiresNewTransaction(ThirdPartyRetryAdviceModel model) throws FrameworkCheckedException {
		transactionReversalManager.saveFailedAdviceRequiresNewTransaction(model);
	}

	/**
	 *  Added by atif Husssain
	 */
	@Override
	public SearchBaseWrapper findRetryAdviceSummaryListView(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return transactionReversalManager.findRetryAdviceSummaryListView(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	/**
	 *  Added by atif Husssain
	 */
	@Override
	public SearchBaseWrapper findRetryAdviceHistoryListView(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return transactionReversalManager.findRetryAdviceHistoryListView(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	/**
	 *  Added by atif Husssain
	 */
	@Override
	public BaseWrapper loadRetryAdviceSummary(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return transactionReversalManager.loadRetryAdviceSummary(baseWrapper);
		}
		catch (Exception ex)
		{
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}
	
	/**
	 *  Added by atif Husssain
	 */
	@Override
	public BaseWrapper makeCoreRetryAdvice(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
		{
			return transactionReversalManager.makeCoreRetryAdvice(baseWrapper);
		}
		catch (Exception ex)
		{
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public void updateRetryAdviceReportStatus(Long trxCodeId) throws FrameworkCheckedException {
	    try{
            transactionReversalManager.updateRetryAdviceReportStatus( trxCodeId );
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public void saveNewIBFTRecord(MiddlewareAdviceVO model) throws FrameworkCheckedException {
	    try{
			transactionReversalManager.saveNewIBFTRecord(model);
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public boolean checkAlreadySuccessful(String stan, Date requestTime) throws FrameworkCheckedException {
	    try{
			return transactionReversalManager.checkAlreadySuccessful(stan, requestTime );
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public void updateIBFTStatus(String stan, Date requestTime, String status, String transactionCode) throws FrameworkCheckedException {
	    try{
			transactionReversalManager.updateIBFTStatus(stan, requestTime, status, transactionCode);
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public void saveOrUpdateAccountOpeningStatus(String mobileNo, String cnic,Long accountOpeningStatus, Long isValid, String rrn, String responseCode) throws FrameworkCheckedException {
		try{
			transactionReversalManager.saveOrUpdateAccountOpeningStatus(mobileNo, cnic, accountOpeningStatus, isValid, rrn, responseCode);
		}catch( Exception ex ){
			throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
		}
	}

	@Override
	public SearchBaseWrapper findIBFTRetryAdviceListView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
	    try{
			return transactionReversalManager.findIBFTRetryAdviceListView(wrapper);
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public List<IBFTRetryAdviceModel> getIbftRetryAdviceList(String stan, Date requestTime, String status) throws FrameworkCheckedException {
	    try{
			return transactionReversalManager.getIbftRetryAdviceList(stan, requestTime, status);
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}

	@Override
	public BaseWrapper makeIBFTRetryAdvice(Long ibftRetryAdviceId) throws FrameworkCheckedException {
	    try{
			return transactionReversalManager.makeIBFTRetryAdvice(ibftRetryAdviceId);
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}
	
	@Override
	public SearchBaseWrapper findRetryCreditQueueViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException {

		return transactionReversalManager.findRetryCreditQueueViewModel(wrapper);
	}

	@Override
	public void createSaveAccountCreditQueueLogModel(AccountCreditQueueLogModel accountCreditQueueLogModel) {
		transactionReversalManager.createSaveAccountCreditQueueLogModel(accountCreditQueueLogModel);
	}

	@Override
	public AccountCreditQueueLogModel findAccountCreditQueueLogModel(AccountCreditQueueLogModel _accountCreditQueueLogModel) {
		
		return transactionReversalManager.findAccountCreditQueueLogModel(_accountCreditQueueLogModel);
	}

	@Override
	public void makeCoreAdvice(SwitchWrapper switchWrapper, MiddlewareAdviceVO middlewareAdviceVO) throws Exception {
		
		transactionReversalManager.makeCoreAdvice(switchWrapper, middlewareAdviceVO);
		
	}

	@Override
	public void makeThirdPartyCashOutReversalAdvice(SwitchWrapper switchWrapper) throws Exception {

		transactionReversalManager.makeThirdPartyCashOutReversalAdvice(switchWrapper);

	}

	@Override
	public TransactionDetailMasterModel loadTDMForReversal(BaseWrapper baseWrapper) throws Exception {
		return transactionReversalManager.loadTDMForReversal(baseWrapper);
	}

	@Override
	public TransactionDetailMasterModel loadTDMForReversalByTransactionCode(String transactionCode) throws Exception {
		return transactionReversalManager.loadTDMForReversalByTransactionCode(transactionCode);
	}

	@Override
	public void updateTransactionDetailMaster(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		transactionReversalManager.updateTransactionDetailMaster(baseWrapper);
	}

	@Override
	public IBFTRetryAdviceModel findIBFTRetryByStanAndStatus(MiddlewareAdviceVO adviceVO) throws FrameworkCheckedException {
		return transactionReversalManager.findIBFTRetryByStanAndStatus(adviceVO);
	}

	@Override
	public TransactionDetailMasterModel loadTDMbyThridPartyRRN(String rrn) throws Exception {
		return transactionReversalManager.loadTDMForReversalByTransactionCode(rrn);
	}

	@Override
	public TransactionDetailMasterModel loadTDMbyMobileNumber(String mobileNo, String productId) throws FrameworkCheckedException {
		return transactionReversalManager.loadTDMbyMobileNumber(mobileNo, productId);
	}

//	@Override
//	public List<TransactionDetailMasterModel> loadTDMbyMobileandDateRange(String mobileNo, String startDate, String endDate) throws FrameworkCheckedException {
//		return transactionReversalManager.loadTDMbyMobileandDateRange(mobileNo, startDate, endDate);
//	}

	@Override
	public BaseWrapper makeCoreRetryAdviceSkipped(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	    try{
			return transactionReversalManager.makeCoreRetryAdviceSkipped(baseWrapper);
        }catch( Exception ex ){
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.UPDATE_ACTION );
        }
	}	
}
