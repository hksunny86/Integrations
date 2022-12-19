package com.inov8.microbank.server.service.portal.transactiondetaili8module;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.BispTransactionDetailViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.DateWiseTxSummaryModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.transactiondetaili8module.*;
import com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate.TransactionDataDownloadDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import org.hibernate.criterion.MatchMode;

import java.util.List;

public class TransactionDetailI8ManagerImpl implements TransactionDetailI8Manager {
    private TransactionDetailPortalListViewDAO transactionDetailPortalListViewDAO;
    private BispTransactionDetailViewDAO bispTransactionDetailViewDAO;
    private DateWiseTxSummaryDAO dateWiseTxSummaryDAO;
    private OFSettlementTransactionListViewDAO ofSettlementTransactionListViewDAO;
    private OFSettlementTransactionDetailDAO ofSettlementTransactionDetailDAO;
    private OFSettlementTransactionDetailListViewDAO ofSettlementTransactionDetailListViewDAO;
    private TransactionDataDownloadDAO transactionDataDownloadDAO;
    private ActionLogManager actionLogManager;


    /**
     * @return
     * @throws FrameworkCheckedException
     */
    public String createZipFile(SearchBaseWrapper searchBaseWrapper) throws Exception {

        return transactionDataDownloadDAO.createZipFile(searchBaseWrapper);
    }

    public SearchBaseWrapper searchTransactionDetailForI8(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        final TransactionDetailPortalListModel model = (TransactionDetailPortalListModel) searchBaseWrapper.getBasePersistableModel();
        AppUserModel appUserModel = UserUtils.getCurrentUser();
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setUsecaseId(1500L);
        CustomList<TransactionDetailPortalListModel> list = null;
        actionLogModel = this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
	/*	list = this.transactionDetailPortalListViewDAO.findByExample( (TransactionDetailPortalListModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModelList());*/
        if (null == appUserModel.getMnoUserIdMnoUserModel()) {
            list = this.transactionDetailPortalListViewDAO.findByExample(model, searchBaseWrapper.
                    getPagingHelperModel(), searchBaseWrapper.
                    getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModelList());
        } else {
            list = this.transactionDetailPortalListViewDAO.searchTransactionDetailForI8(searchBaseWrapper, appUserModel.getMnoUserIdMnoUserModel().getMnoIdMnoModel());
        }
        if (list != null) {
            searchBaseWrapper.setCustomList(list);
			/*try {

			} catch (IOException e) {
				e.printStackTrace();
			}*/
        }
        actionLogModel = this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        return searchBaseWrapper;
    }

    @Override
    public CustomList<TransactionDetailPortalListModel> searchTransactionDetailForI8(TransactionDetailPortalListModel transactionDetailPortalListModel) throws FrameworkCheckedException {
        CustomList<TransactionDetailPortalListModel>
                list = this.transactionDetailPortalListViewDAO.findByExample(transactionDetailPortalListModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        return list;
    }

    public SearchBaseWrapper searchP2PTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setUsecaseId(1500L);
        actionLogModel = this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        CustomList<TransactionDetailPortalListModel>
                list = this.transactionDetailPortalListViewDAO.findByExample((TransactionDetailPortalListModel)
                        searchBaseWrapper.
                                getBasePersistableModel(),
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.
                        getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModelList());
        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        actionLogModel = this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        return searchBaseWrapper;
    }

    @Override
    public SearchBaseWrapper searchOFSettlementTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        SettlementTransactionViewModel settlementTransactionModel = (SettlementTransactionViewModel) searchBaseWrapper.getBasePersistableModel();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(false);
        exampleConfigHolderModel.setIgnoreCase(false);


        CustomList<SettlementTransactionViewModel>
                list = this.ofSettlementTransactionListViewDAO.findByExample(
                settlementTransactionModel,
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.
                        getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), exampleConfigHolderModel);
        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }

    @Override
    public CustomList<SettlementTransactionViewModel> searchOFSettlementTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        SettlementTransactionViewModel model = (SettlementTransactionViewModel) searchBaseWrapper.getBasePersistableModel();
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(false);
        exampleConfigHolderModel.setIgnoreCase(false);

        CustomList<SettlementTransactionViewModel> customList = this.ofSettlementTransactionListViewDAO.findByExample(model, null, searchBaseWrapper.getSortingOrderMap(), exampleConfigHolderModel);
        return customList;
    }


    @Override
    public SearchBaseWrapper searchOFSettlementTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        SettlementTransactionDetailModel settlementTransactionDetailModel = (SettlementTransactionDetailModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<SettlementTransactionDetailModel>
                list = this.ofSettlementTransactionDetailDAO.findByExample(
                settlementTransactionDetailModel,
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.
                        getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());
        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }


    @Override
    public SearchBaseWrapper searchOFSettlementTransactionSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        SettlementTransactionDetailViewModel settlementTransactionDetailViewModel = (SettlementTransactionDetailViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<SettlementTransactionDetailViewModel>
                list = this.ofSettlementTransactionDetailListViewDAO.findByExample(
                settlementTransactionDetailViewModel,
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.
                        getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());
        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }

    @Override
    public List<TransactionDetailPortalListModel> fetchDateWiseSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        return transactionDetailPortalListViewDAO.fetchDateWiseSummary(searchBaseWrapper);
    }

    @Override
    public List<DateWiseTxSummaryModel> loadDateWiseSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        return dateWiseTxSummaryDAO.loadDateWiseSummary(searchBaseWrapper);
    }

    @Override
    public SearchBaseWrapper searchSenderRedeemTransactionReportDetail(SearchBaseWrapper searchBaseWrapper)
            throws FrameworkCheckedException {

        searchBaseWrapper = transactionDetailPortalListViewDAO.findSenderRedeemTransactionDetail(searchBaseWrapper);
        return searchBaseWrapper;

    }

    @Override
    public SearchBaseWrapper searchBispTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        final BispTransactionDetailViewModel model = (BispTransactionDetailViewModel) searchBaseWrapper.getBasePersistableModel();
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setUsecaseId(1500L);
        actionLogModel = this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        CustomList<BispTransactionDetailViewModel> list = this.bispTransactionDetailViewDAO.findByExample(model, searchBaseWrapper.
                getPagingHelperModel(), searchBaseWrapper.
                getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModelList());
        if(list !=null)
            searchBaseWrapper.setCustomList(list);
        searchBaseWrapper.setBasePersistableModel(model);
        this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        return searchBaseWrapper;
    }

    public void setTransactionDetailPortalListViewDAO(TransactionDetailPortalListViewDAO transactionDetailPortalListViewDAO) {
        this.transactionDetailPortalListViewDAO = transactionDetailPortalListViewDAO;
    }

    public void setDateWiseTxSummaryDAO(DateWiseTxSummaryDAO dateWiseTxSummaryDAO) {
        this.dateWiseTxSummaryDAO = dateWiseTxSummaryDAO;
    }

    public void setOfSettlementTransactionListViewDAO(
            OFSettlementTransactionListViewDAO ofSettlementTransactionListViewDAO) {
        this.ofSettlementTransactionListViewDAO = ofSettlementTransactionListViewDAO;
    }

    public void setOfSettlementTransactionDetailDAO(
            OFSettlementTransactionDetailDAO ofSettlementTransactionDetailDAO) {
        this.ofSettlementTransactionDetailDAO = ofSettlementTransactionDetailDAO;
    }

    public void setOfSettlementTransactionDetailListViewDAO(
            OFSettlementTransactionDetailListViewDAO ofSettlementTransactionDetailListViewDAO) {
        this.ofSettlementTransactionDetailListViewDAO = ofSettlementTransactionDetailListViewDAO;
    }

    public void setTransactionDataDownloadDAO(TransactionDataDownloadDAO transactionDataDownloadDAO) {
        this.transactionDataDownloadDAO = transactionDataDownloadDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setBispTransactionDetailViewDAO(BispTransactionDetailViewDAO bispTransactionDetailViewDAO) {
        this.bispTransactionDetailViewDAO = bispTransactionDetailViewDAO;
    }
}
