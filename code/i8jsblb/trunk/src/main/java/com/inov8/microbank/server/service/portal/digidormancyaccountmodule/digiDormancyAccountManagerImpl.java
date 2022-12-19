package com.inov8.microbank.server.service.portal.digidormancyaccountmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.digidormancyaccountmodule.DigiDormancyAccountViewModel;
import com.inov8.microbank.server.dao.portal.digidormancyviewmodule.DigiDormancyAccountViewDAO;
import org.hibernate.criterion.MatchMode;

public class digiDormancyAccountManagerImpl implements digiDormancyAccountManager{
    private DigiDormancyAccountViewDAO digiDormancyAccountViewDAO;

    public SearchBaseWrapper searchDigiDormancyAccountsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
//        ActionLogModel actionLogModel = new ActionLogModel();
//        actionLogModel.setUsecaseId(PortalConstants.KEY_DIGI_DORMANCY_ACCOUNTS_REPORT_USECASE_ID);
//        actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,actionLogModel);
        DigiDormancyAccountViewModel model = (DigiDormancyAccountViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<DigiDormancyAccountViewModel> customList = digiDormancyAccountViewDAO.findByExample( model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), new ExampleConfigHolderModel(false, true, false, MatchMode.EXACT));
        searchBaseWrapper.setCustomList( customList );
        return searchBaseWrapper;
    }

    public void setDigiDormancyAccountViewDAO(DigiDormancyAccountViewDAO digiDormancyAccountViewDAO) {
        this.digiDormancyAccountViewDAO = digiDormancyAccountViewDAO;
    }

}
