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
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.portal.blbreports.AccountDormancyReportViewModel;
import com.inov8.microbank.common.model.portal.blbreports.DormancyReportViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.service.blbreports.BLBReportsManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class AccountDormancyReportController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private CommonFacade commonFacade;
    private BLBReportsManager blbReportsManager;

    public AccountDormancyReportController()
    {
        super.setCommandName("accountDormancyReportViewModel");
        super.setCommandClass(AccountDormancyReportViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> referenceDataMap = new HashMap<>(2);

        SegmentModel segmentModel = new SegmentModel();
        List<SegmentModel> segmentModelList = null;
        ReferenceDataWrapper segmentDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        segmentModelList = (List<SegmentModel>) referenceDataManager.getReferenceData(segmentDataWrapper).getReferenceDataList();
        referenceDataMap.put("segmentModelList", segmentModelList);

        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = null;
        ReferenceDataWrapper olaCustomerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.ASC);
        olaCustomerAccountTypeModelList = (List<OlaCustomerAccountTypeModel>) referenceDataManager.getReferenceData(olaCustomerAccountTypeDataWrapper).getReferenceDataList();
        referenceDataMap.put("olaCustomerAccountTypeModelList", olaCustomerAccountTypeModelList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        AccountDormancyReportViewModel accountDormancyReportViewModel = (AccountDormancyReportViewModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(accountDormancyReportViewModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        ArrayList<DateRangeHolderModel> dateRangeHolderModelList = new ArrayList<DateRangeHolderModel>();
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "dormancyMarkedOn",
                accountDormancyReportViewModel.getStartDate(), accountDormancyReportViewModel.getEndDate() );
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        dateRangeHolderModelList.add(dateRangeHolderModel);

        dateRangeHolderModel = new DateRangeHolderModel( "dormancyRemovedOn", accountDormancyReportViewModel.getDormancyRemovedOnStartDate(),
                accountDormancyReportViewModel.getDormancyRemovedOnEndDate());
        dateRangeHolderModelList.add(dateRangeHolderModel);

        searchBaseWrapper.setDateRangeHolderModelList( dateRangeHolderModelList );

        searchBaseWrapper = blbReportsManager.searchAccountDormancyReport( searchBaseWrapper );

        List<AccountDormancyReportViewModel> list = null;
        if(searchBaseWrapper.getCustomList() != null) {
            list = searchBaseWrapper.getCustomList().getResultsetList();
        }

        String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
        return new ModelAndView( successView, "accountDormancyReportViewModelList", list );
    }

    public void setBlbReportsManager(BLBReportsManager blbReportsManager) {
        this.blbReportsManager = blbReportsManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setCommonFacade(CommonFacade commonFacade) {
        this.commonFacade = commonFacade;
    }
}
