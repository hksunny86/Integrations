package com.inov8.microbank.webapp.action.portal.reports.blbreports;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.cardconfiguration.model.CardFeeTypeModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.portal.blbreports.DebitCardChargesViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.service.blbreports.BLBReportsManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DebitCardChargesReportController extends BaseFormSearchController {
    private ReferenceDataManager referenceDataManager;
    private CommonFacade commonFacade;
    private BLBReportsManager blbReportsManager;

    public DebitCardChargesReportController()
    {
        super.setCommandName("debitCardChargesViewModel");
        super.setCommandClass(DebitCardChargesViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,Object> refDataMap = new HashMap<>(8);
        ReferenceDataWrapper refDataWrapper = null;
        List<SegmentModel> segmentModelList = null;
        List<OlaCustomerAccountTypeModel> customerAccountTypeModelList = null;
        List<CardFeeTypeModel> cardFeeTypeModelList = null;

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);
        refDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        segmentModelList = refDataWrapper.getReferenceDataList();

        OlaCustomerAccountTypeModel accountTypeModel = new OlaCustomerAccountTypeModel();
        accountTypeModel.setActive(true);
        accountTypeModel.setIsCustomerAccountType(true);
        refDataWrapper = new ReferenceDataWrapperImpl(accountTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceDataByExcluding(refDataWrapper, CustomerAccountTypeConstants.SETTLEMENT, CustomerAccountTypeConstants.WALK_IN_CUSTOMER);
        customerAccountTypeModelList = refDataWrapper.getReferenceDataList();

        CardFeeTypeModel cardFeeTypeModel = new CardFeeTypeModel();
        refDataWrapper = new ReferenceDataWrapperImpl(cardFeeTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceDataByExcluding(refDataWrapper, 4L,7L,8L);
        cardFeeTypeModelList = refDataWrapper.getReferenceDataList();


        refDataMap.put("customerAccountTypeModelList", customerAccountTypeModelList);
        refDataMap.put("segmentModelList", segmentModelList);
        refDataMap.put("cardFeeTypeModelList", cardFeeTypeModelList);

        return refDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        DebitCardChargesViewModel debitCardChargesViewModel = (DebitCardChargesViewModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(debitCardChargesViewModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "transactionDate",
                debitCardChargesViewModel.getStartDate(), debitCardChargesViewModel.getEndDate() );
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        searchBaseWrapper = blbReportsManager.searchDebitCardChargesView( searchBaseWrapper );

        List<DebitCardChargesViewModel> list = null;
        if(searchBaseWrapper.getCustomList() != null) {
            list = searchBaseWrapper.getCustomList().getResultsetList();
        }

        String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
        return new ModelAndView( successView, "debitCardChargesViewModelList", list );
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setCommonFacade(CommonFacade commonFacade) {
        this.commonFacade = commonFacade;
    }

    public void setBlbReportsManager(BLBReportsManager blbReportsManager) {
        this.blbReportsManager = blbReportsManager;
    }
}
