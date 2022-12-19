package com.inov8.microbank.webapp.action.portal.clsscreenigmodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingAccountOpeningSafRepoSearchDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class WalletAccountOpeningTrackingController extends BaseFormSearchController {
    private ReferenceDataManager referenceDataManager;
    private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;


    public WalletAccountOpeningTrackingController() {

        super.setCommandName("clsPendingAccountOpeningModel");
        super.setCommandClass(ClsPendingAccountOpeningModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();
        CopyOnWriteArrayList<RegistrationStateModel> modifiedRegistrationStateModelsList = new CopyOnWriteArrayList<>();

        RegistrationStateModel registrationStateModel = new RegistrationStateModel();
        ReferenceDataWrapper registrationStateReferenceDataWrapper = new ReferenceDataWrapperImpl(registrationStateModel, "registrationStateId", SortingOrder.ASC);
        registrationStateReferenceDataWrapper.setBasePersistableModel(registrationStateModel);
        try {
            referenceDataManager.getReferenceData(registrationStateReferenceDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        List<RegistrationStateModel> registrationStateList = null;
        if (registrationStateReferenceDataWrapper.getReferenceDataList() != null) {
            registrationStateList = registrationStateReferenceDataWrapper.getReferenceDataList();
            modifiedRegistrationStateModelsList.addAll(registrationStateList);

            for (RegistrationStateModel regStateModel : modifiedRegistrationStateModelsList) {
                if (regStateModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.CLOSED) {
                    modifiedRegistrationStateModelsList.remove(regStateModel);
                }
            }

        }
        referenceDataMap.put("registrationStateList", modifiedRegistrationStateModelsList);


        AccountStateModel accountStateModel = new AccountStateModel();
        ReferenceDataWrapper accountStateReferenceDataWrapper = new ReferenceDataWrapperImpl(accountStateModel, "accountStateId", SortingOrder.ASC);
        accountStateReferenceDataWrapper.setBasePersistableModel(accountStateModel);
        try {
            referenceDataManager.getReferenceData(accountStateReferenceDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        List<AccountStateModel> accountStateList = null;
        if (accountStateReferenceDataWrapper.getReferenceDataList() != null) {
            accountStateList = accountStateReferenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("accountStateList", accountStateList);


        ClsCaseStatusModel clsCaseStatusModel = new ClsCaseStatusModel();
        ReferenceDataWrapper clsCaseStatusReferenceDataWrapper = new ReferenceDataWrapperImpl(clsCaseStatusModel, "clsCaseStatusId", SortingOrder.ASC);
        clsCaseStatusReferenceDataWrapper.setBasePersistableModel(clsCaseStatusModel);
        try {
            referenceDataManager.getReferenceData(clsCaseStatusReferenceDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        List<ClsCaseStatusModel> caseStatusModelList = null;
        if (clsCaseStatusReferenceDataWrapper.getReferenceDataList() != null) {
            caseStatusModelList = clsCaseStatusReferenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("caseStatusModelList", caseStatusModelList);


        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = (ClsPendingAccountOpeningModel) o;

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", clsPendingAccountOpeningModel.getStart(),
                clsPendingAccountOpeningModel.getEnd());
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        clsPendingAccountOpeningModel.setCaseStatus(clsPendingAccountOpeningModel.getName());

        searchBaseWrapper.setBasePersistableModel(clsPendingAccountOpeningModel);

        if (linkedHashMap.isEmpty()) {
            linkedHashMap.put("createdOn", SortingOrder.DESC);

        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<ClsPendingAccountOpeningModel> list = this.clsPendingAccountOpeningDAO.findByExample(clsPendingAccountOpeningModel,
                searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),
                exampleConfigHolderModel);
        return new ModelAndView(getSuccessView(), "bookMeList", list.getResultsetList());
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }


    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }
}
