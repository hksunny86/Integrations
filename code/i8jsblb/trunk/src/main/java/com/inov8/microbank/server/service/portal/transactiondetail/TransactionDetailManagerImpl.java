package com.inov8.microbank.server.service.portal.transactiondetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.minitransactionmodule.MiniTransactionViewModel;
import com.inov8.microbank.common.model.minitransactionmodule.TransactionCodeHistoryViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.CashTransactionViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionCashReversalViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionDetailListViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionSummaryTextViewModel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.minitransactionmodule.MiniTransactionViewDAO;
import com.inov8.microbank.server.dao.portal.sales.SalesDAO;
import com.inov8.microbank.server.dao.portal.transactiondetail.CashTransactionViewDAO;
import com.inov8.microbank.server.dao.portal.transactiondetail.SenderRedeemViewDAO;
import com.inov8.microbank.server.dao.portal.transactiondetail.TransactionAllViewDao;
import com.inov8.microbank.server.dao.portal.transactiondetail.TransactionCashReversalViewDao;
import com.inov8.microbank.server.dao.portal.transactiondetail.TransactionDetailListViewDAO;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeHistoryDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public class TransactionDetailManagerImpl implements TransactionDetailManager
{


	private TransactionDetailListViewDAO transactionDetailListViewDAO;
	private GenericDao genericDAO;
	private SalesDAO salesDAO;
	private TransactionDAO transactionDAO;
	private MiniTransactionViewDAO miniTransactionViewDAO;
	private MiniTransactionDAO miniTransactionDAO;
	private TransactionCodeHistoryDAO transactionCodeHistoryDAO;
	private TransactionAllViewDao transactionAllViewDao;
	private TransactionCashReversalViewDao transactionCashReversalViewDao;
	private SenderRedeemViewDAO senderRedeemViewDAO;
	private CashTransactionViewDAO cashTransactionViewDAO;
	private ActionLogManager actionLogManager;

	public SearchBaseWrapper loadTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws
			FrameworkCheckedException
	{
		IssueModel issueModel = this.salesDAO.findByPrimaryKey(
				searchBaseWrapper.getBasePersistableModel().
						getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(issueModel);
		return searchBaseWrapper;

	}


	public SearchBaseWrapper searchTransactionDetail(SearchBaseWrapper searchBaseWrapper)

	{
		CustomList<TransactionDetailListViewModel>
				list = this.transactionDetailListViewDAO.findByExample( (
						TransactionDetailListViewModel)
						searchBaseWrapper.
								getBasePersistableModel(),
				searchBaseWrapper.
						getPagingHelperModel(),
				searchBaseWrapper.
						getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());



		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;

	}

	@Override
	public SearchBaseWrapper searchTransactionDetail( SearchBaseWrapper searchBaseWrapper,
													  String mfsId ) throws FrameworkCheckedException
	{
		return transactionDetailListViewDAO.getTransactionDetail( searchBaseWrapper, mfsId );
	}

	public BaseWrapper createOrUpdateIssue(BaseWrapper baseWrapper) throws
			FrameworkCheckedException
	{
		//IssueModel issueModel = new IssueModel();
		IssueModel issueModel = (IssueModel) baseWrapper.getBasePersistableModel();

		issueModel = this.salesDAO.saveOrUpdate( (
				IssueModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(issueModel);
		return baseWrapper;

	}

	public BaseWrapper loadTransaction(BaseWrapper baseWrapper)
	{
		TransactionModel transactionModel = (TransactionModel)this.transactionDAO.findByPrimaryKey( (
				baseWrapper.getBasePersistableModel()).getPrimaryKey());
		baseWrapper.setBasePersistableModel(transactionModel);
		return baseWrapper;
	}

	public BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper) throws
			FrameworkCheckedException
	{
		//IssueModel issueModel = new IssueModel();
		TransactionModel transactionModel = (TransactionModel) baseWrapper.getBasePersistableModel();

		transactionModel = this.transactionDAO.saveOrUpdate( (
				TransactionModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(transactionModel);
		return baseWrapper;

	}


	public List<TransactionSummaryTextViewModel> populateBBTransactionSummary(String csvTransIDs) throws FrameworkCheckedException {
		List transList = null;
		List<TransactionSummaryTextViewModel> modelTransList = new ArrayList();

		String transHQL = " select ts.transactionCode,ts.transactionSummaryText from TransactionSummaryTextViewModel ts where ts.transactionCode IN ("+csvTransIDs+")";
		//String transHQL = " from TransactionSummaryTextViewModel ts where ts.transactionCode IN ("+csvTransIDs+")";

		transList = this.genericDAO.findByHQL(transHQL);

		TransactionSummaryTextViewModel summaryTextViewModel = null;
		for (int count = 0; count < transList.size(); count++) {
			summaryTextViewModel = new TransactionSummaryTextViewModel();

			Object obj[] = (Object[]) transList.get(count);
			//summaryTextViewModel.setPrimaryKey((Long) obj[0]);
			summaryTextViewModel.setTransactionCode((String)obj[0]);
			summaryTextViewModel.setTransactionSummaryText((String) obj[1]);
			modelTransList.add(summaryTextViewModel);
		}
		return modelTransList;
	}

	public SearchBaseWrapper searchMiniTransactionViewList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
/*		  BaseWrapper baseWrapper = new BaseWrapperImpl();
		  MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
		  // Mark all previous transactions as expired........
		  MiniTransactionViewModel model = (MiniTransactionViewModel)searchBaseWrapper.getBasePersistableModel();
		  miniTransactionModel.setAppUserId(model.getAppUserId());
		  miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT);

		  baseWrapper.setBasePersistableModel(miniTransactionModel);
		  this.modifyPINSentMiniTransToExpired(baseWrapper);
*/
		searchBaseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_SEARCH_REGENERATE_TRX_CODE_USECASE_ID);
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,null);
		CustomList<MiniTransactionViewModel> list = this.miniTransactionViewDAO.findByExample(
				(MiniTransactionViewModel)searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(),
				searchBaseWrapper.getSortingOrderMap(),
				searchBaseWrapper.getDateRangeHolderModel());

		searchBaseWrapper.setCustomList(list);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,actionLogModel);
		return searchBaseWrapper;
	}
	public SearchBaseWrapper searchTransactionCodeHtrViewModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

		CustomList<TransactionCodeHistoryViewModel> list = this.transactionCodeHistoryDAO.findByExample(
				(TransactionCodeHistoryViewModel)searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(),
				searchBaseWrapper.getSortingOrderMap(),
				searchBaseWrapper.getDateRangeHolderModel());

		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper modifyPINSentMiniTransToExpired(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		MiniTransactionModel miniTransactionModel = (MiniTransactionModel)baseWrapper.getBasePersistableModel();
		CustomList<MiniTransactionModel> miniTransactionModelList = this.miniTransactionDAO.findByExample(miniTransactionModel);
		Iterator<MiniTransactionModel> ite = miniTransactionModelList.getResultsetList().iterator() ;
		while( ite.hasNext() ){
			miniTransactionModel = ite.next() ;


			//Check the 24 hours validity
			long transValidityInMilliSecs = 24 * 60  * 60 * 1000;
			long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();

			if (timeDiff > transValidityInMilliSecs){
				miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.EXPIRED ) ;
				miniTransactionModel.setUpdatedOn(new Date());
				miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
			}
		}
		return baseWrapper;
	}
	@Override
	public BaseWrapper updateMiniTransactionModel(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();
		TransactionCodeHistoryViewModel transactionCodeHtrModel= new TransactionCodeHistoryViewModel();

		miniTransactionModel = this.miniTransactionDAO.saveOrUpdate( (MiniTransactionModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(miniTransactionModel);

		transactionCodeHtrModel.setCode(miniTransactionModel.getRelationTransactionCodeIdTransactionCodeModel().getCode());
		transactionCodeHtrModel.setComments(miniTransactionModel.getComments());
		transactionCodeHtrModel.setCreatedByAppUserModel(miniTransactionModel.getUpdatedByAppUserModel());
		transactionCodeHtrModel.setCreatedOn(miniTransactionModel.getUpdatedOn());
		transactionCodeHtrModel.setUsecaseId(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID);
		transactionCodeHistoryDAO.saveOrUpdate(transactionCodeHtrModel);

		actionLogModel.setCustomField1(miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getTransactionCodeId().toString());
		actionLogModel.setCustomField11(miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getTransactionCodeId().toString());
		this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;

	}
	public MiniTransactionModel LoadMiniTransactionModel(String transactionCode)throws FrameworkCheckedException{
		MiniTransactionModel miniTransactionModel = miniTransactionDAO.LoadMiniTransactionModel(transactionCode);
		return miniTransactionModel;
	}
	public CustomList<TransactionAllViewModel> searchTransactionAllView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,null);
		TransactionAllViewModel model = (TransactionAllViewModel)wrapper.getBasePersistableModel();
		CustomList<TransactionAllViewModel> list = transactionAllViewDao.findByExample( model, wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
		wrapper.setCustomList(list);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
		return list;
	}

	public List<?> findWalkInCustomerTransactions( String cnic,TransactionAllViewModel transactionAllViewModel, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap,DateRangeHolderModel dateRangeHolderModel )
	{
		return transactionAllViewDao.findWalkInCustomerTransactions( cnic,transactionAllViewModel,pagingHelperModel, sortingOrderMap,dateRangeHolderModel );
	}

	public List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException
	{
		return transactionCashReversalViewDao.loadSupplierProcessingStatuses( ids );
	}

	public CustomList<TransactionCashReversalViewModel> searchTransactionCashReversalView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
	{
		TransactionCashReversalViewModel model = (TransactionCashReversalViewModel) wrapper.getBasePersistableModel();
		return transactionCashReversalViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
	}

	public CustomList<SenderRedeemViewModel> searchSenderRedeemView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException{
		SenderRedeemViewModel model = (SenderRedeemViewModel) wrapper.getBasePersistableModel();
		wrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_SENDER_REDEEM_TRX_USECASE_ID);
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,null);
		CustomList<SenderRedeemViewModel> list = senderRedeemViewDAO.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(),
				wrapper.getDateRangeHolderModel() );
		wrapper.setCustomList(list);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
		return list;
	}

	public CustomList<CashTransactionViewModel> searchCashTransactionView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException{
		CashTransactionViewModel model = (CashTransactionViewModel) wrapper.getBasePersistableModel();
		wrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_SEARCH_TRX_REDEEM_USECASE_ID);
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,null);
		CustomList<CashTransactionViewModel> list = cashTransactionViewDAO.findByExample( model, wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
		wrapper.setCustomList(list);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
		return list;
	}


	public void setSalesDAO( SalesDAO salesDAO )
	{
		this.salesDAO = salesDAO;
	}

	public void setTransactionDAO( TransactionDAO transactionDAO )
	{
		this.transactionDAO = transactionDAO;
	}

	public void setTransactionDetailListViewDAO( TransactionDetailListViewDAO transactionDetailListViewDAO )
	{
		this.transactionDetailListViewDAO = transactionDetailListViewDAO;
	}

	public void setGenericDAO( GenericDao genericDAO )
	{
		this.genericDAO = genericDAO;
	}

	public void setMiniTransactionViewDAO( MiniTransactionViewDAO miniTransactionViewDAO )
	{
		this.miniTransactionViewDAO = miniTransactionViewDAO;
	}

	public void setMiniTransactionDAO( MiniTransactionDAO miniTransactionDAO )
	{
		this.miniTransactionDAO = miniTransactionDAO;
	}

	public void setTransactionAllViewDao( TransactionAllViewDao transactionAllViewDao )
	{
		this.transactionAllViewDao = transactionAllViewDao;
	}

	public void setTransactionCashReversalViewDao( TransactionCashReversalViewDao transactionCashReversalViewDao )
	{
		this.transactionCashReversalViewDao = transactionCashReversalViewDao;
	}
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
	public void setTransactionCodeHistoryDAO(TransactionCodeHistoryDAO transactionCodeHistoryDAO) {
		this.transactionCodeHistoryDAO = transactionCodeHistoryDAO;
	}


	public void setSenderRedeemViewDAO(SenderRedeemViewDAO senderRedeemViewDAO) {
		this.senderRedeemViewDAO = senderRedeemViewDAO;
	}


	public void setCashTransactionViewDAO(CashTransactionViewDAO cashTransactionViewDAO) {
		this.cashTransactionViewDAO = cashTransactionViewDAO;
	}


}
