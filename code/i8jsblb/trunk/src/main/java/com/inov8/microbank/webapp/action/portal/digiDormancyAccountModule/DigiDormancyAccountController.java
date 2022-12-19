package com.inov8.microbank.webapp.action.portal.digiDormancyAccountModule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.portal.digidormancyaccountmodule.DigiDormancyAccountViewModel;
import com.inov8.microbank.server.facade.portal.digidormancyaccountmodule.digiDormancyAccountFacade;
import com.inov8.microbank.server.service.portal.digidormancyaccountmodule.digiDormancyAccountManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DigiDormancyAccountController extends BaseFormSearchController {

    private digiDormancyAccountFacade digiDormancyAccountFacade;
    private ReferenceDataManager referenceDataManager;

    public DigiDormancyAccountController() {
        setCommandName("digiDormancyAccountViewModel");
        setCommandClass(DigiDormancyAccountViewModel.class);
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
        long start = System.currentTimeMillis();
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        DigiDormancyAccountViewModel modelToSearch = (DigiDormancyAccountViewModel) o;
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "lastTransactionDate", modelToSearch.getStartDate(),
                modelToSearch.getEndDate());
        wrapper.setBasePersistableModel( modelToSearch );
        wrapper.setPagingHelperModel( pagingHelperModel );
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( linkedHashMap.isEmpty() )
        {
            linkedHashMap.put( "lastTransactionDate", SortingOrder.ASC );
        }
        wrapper.setSortingOrderMap( linkedHashMap );

        wrapper = digiDormancyAccountFacade.searchDigiDormancyAccountsView(wrapper);

        CustomList<DigiDormancyAccountViewModel> customList = wrapper.getCustomList();
        List<DigiDormancyAccountViewModel> digiDormancyAccountViewModelList = null;
        if( customList != null )
        {
            digiDormancyAccountViewModelList = customList.getResultsetList();
        }

        logger.info("\n\n:- File Created "+ ((System.currentTimeMillis() - start)/1000) + ".s\n");

        return new ModelAndView( getFormView(), "digiAccountsViewModelList", digiDormancyAccountViewModelList );
    }

    public void setDigiDormancyAccountFacade(com.inov8.microbank.server.facade.portal.digidormancyaccountmodule.digiDormancyAccountFacade digiDormancyAccountFacade) {
        this.digiDormancyAccountFacade = digiDormancyAccountFacade;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
