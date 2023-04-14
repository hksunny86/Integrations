package com.inov8.microbank.debitcard.controller;

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
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class DebitCardManagmentController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private DebitCardManager debitCardManager;

    public DebitCardManagmentController()
    {
        setCommandName("debitCardRequestsViewModel");
        setCommandClass(DebitCardRequestsViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);
        ReferenceDataWrapper segmentReferenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        segmentReferenceDataWrapper.setBasePersistableModel(segmentModel);
        try
        {
            referenceDataManager.getReferenceData(segmentReferenceDataWrapper);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

        List<SegmentModel> segmentList = null;
        if (segmentReferenceDataWrapper.getReferenceDataList() != null)
        {
            segmentList = segmentReferenceDataWrapper.getReferenceDataList();
        }


        List<CardStateModel> cardStateList = debitCardManager.getAllCardStates();
        List<CardStatusModel> cardStatusList = debitCardManager.getAllCardSatus();
        List<CardProdCodeModel> cardProductTypeList = debitCardManager.getAllCardProductTypes();

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
        List<LabelValueBean> riskLevelList = new ArrayList<>();
        LabelValueBean markCheckedValueBean = new LabelValueBean("High", "High");
        riskLevelList.add(markCheckedValueBean);
        markCheckedValueBean = new LabelValueBean("Medium", "Medium");
        riskLevelList.add(markCheckedValueBean);
        markCheckedValueBean = new LabelValueBean("Low", "Low");
        riskLevelList.add(markCheckedValueBean);
        referenceDataMap.put("riskLevelList", riskLevelList);



        referenceDataMap.put("segmentList", segmentList);
        referenceDataMap.put( "cardStateList", cardStateList);
        referenceDataMap.put( "cardStatusList", cardStatusList);
        referenceDataMap.put( "cardProductTypeList", cardProductTypeList);
        referenceDataMap.put( "channelList", appList);
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        DateRangeHolderModel dateRangeHolderModel=new DateRangeHolderModel();
        DebitCardRequestsViewModel debitCardRequestsViewModel = (DebitCardRequestsViewModel) o;
        searchBaseWrapper.setBasePersistableModel(debitCardRequestsViewModel);
        searchBaseWrapper.setSortingOrderMap( linkedHashMap );
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
         dateRangeHolderModel = new DateRangeHolderModel( "createdOn", debitCardRequestsViewModel.getCreatedOnStartDate(),
                debitCardRequestsViewModel.getCreatedOnEndDate());
         dateRangeHolderModel = new DateRangeHolderModel( "reissuanceRequestDate", debitCardRequestsViewModel.getReIssuancecreatedOnStartDate(),
                debitCardRequestsViewModel.getReIssuancecreatedOnEndDate());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        List<DebitCardRequestsViewModel> list = debitCardManager.searchDebitCardRequestsData(searchBaseWrapper);
        return new ModelAndView( getSuccessView(), "debitCardRequestsList",list);
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
