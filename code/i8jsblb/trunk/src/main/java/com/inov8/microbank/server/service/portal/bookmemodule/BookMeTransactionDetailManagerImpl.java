package com.inov8.microbank.server.service.portal.bookmemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.portal.bookmemodule.BookMeTransactionDetailViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.BispTransactionDetailViewModel;
import com.inov8.microbank.server.dao.portal.bookmemodule.BookMeTransactionDetailDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import org.hibernate.criterion.MatchMode;

public class BookMeTransactionDetailManagerImpl implements BookMeTransactionDetailManager {

    private BookMeTransactionDetailDAO bookMeTransactionDetailDAO;
    private ActionLogManager actionLogManager;

    @Override
    public SearchBaseWrapper searchBookMeTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        final BookMeTransactionDetailViewModel model = (BookMeTransactionDetailViewModel) searchBaseWrapper.getBasePersistableModel();
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setUsecaseId(1500L);
        actionLogModel = this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<BookMeTransactionDetailViewModel> list = this.bookMeTransactionDetailDAO.findByExample(model, searchBaseWrapper.
                getPagingHelperModel(), searchBaseWrapper.
                getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModelList());
        if(list !=null)
            searchBaseWrapper.setCustomList(list);
        searchBaseWrapper.setBasePersistableModel(model);
        this.actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper, null, actionLogModel);
        return searchBaseWrapper;
    }
    public void setBookMeTransactionDetailDAO(BookMeTransactionDetailDAO bookMeTransactionDetailDAO) {
        this.bookMeTransactionDetailDAO = bookMeTransactionDetailDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }
}
