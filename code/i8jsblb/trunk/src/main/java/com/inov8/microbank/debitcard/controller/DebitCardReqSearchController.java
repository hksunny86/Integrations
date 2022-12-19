package com.inov8.microbank.debitcard.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.app.model.AppModel;
import com.inov8.microbank.cardconfiguration.model.CardStateModel;
import com.inov8.microbank.cardconfiguration.model.CardStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.debitcard.model.DebitCardViewModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class DebitCardReqSearchController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private DebitCardManager debitCardManager;

    public DebitCardReqSearchController()
    {
        setCommandName("debitCardReqSearchController");
        setCommandClass(DebitCardViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
        List<AppUserModel> bankUserAppUserModelList = null;
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setAppUserTypeId(6L); // For loading Bank AppUserModels
        appUserModel.setAccountClosedSettled(false);
        appUserModel.setAccountClosedUnsettled(false);

        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(appUserModel, "username", SortingOrder.ASC);

        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
            if (referenceDataWrapper.getReferenceDataList() != null)
            {
                bankUserAppUserModelList = referenceDataWrapper.getReferenceDataList();
            }
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
            throw new FrameworkCheckedException(ex1.getMessage());
        }

        List<CardStateModel> cardStateList = debitCardManager.getAllCardStates();
        List<CardStatusModel> cardStatusList = debitCardManager.getAllCardSatus();

        List<AppModel> appList = new ArrayList<>();

        AppModel appModel = new AppModel();
        appModel.setAppId(1L);
        appModel.setAppName("AgentMate");

        appList.add(appModel);

        appModel = new AppModel();
        appModel.setAppId(2L);
        appModel.setAppName("Consumer App");

        appList.add(appModel);

        appModel = new AppModel();
        appModel.setAppId(4L);
        appModel.setAppName("Vision");

        appList.add(appModel);

        referenceDataMap.put( "bankUserAppUserModelList", bankUserAppUserModelList);
        referenceDataMap.put( "cardStateList", cardStateList);
        referenceDataMap.put( "cardStatusList", cardStatusList);
        referenceDataMap.put( "channelList", appList);
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel,
                                    LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        DebitCardViewModel debitCardModel = (DebitCardViewModel) o;
        searchBaseWrapper.setBasePersistableModel(debitCardModel);
        searchBaseWrapper.setSortingOrderMap( linkedHashMap );
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", debitCardModel.getCreatedOnStartDate(),
                debitCardModel.getCreatedOnEndDate());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
        /*//sorting order
        if( linkedHashMap.isEmpty() )
        {
            linkedHashMap.put("createdOn", SortingOrder.DESC);
        }*/

        List<DebitCardViewModel> list = debitCardManager.searchDebitCardData(searchBaseWrapper);
         return new ModelAndView( getSuccessView(), "debitCardList",list);
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }
}
