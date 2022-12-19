package com.inov8.microbank.hra.airtimetopup.controller;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.hra.airtimetopup.dao.ConversionRateDAO;
import com.inov8.microbank.hra.airtimetopup.dao.RateTypeDAO;
import com.inov8.microbank.hra.airtimetopup.model.AirTimeTopViewModel;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.hra.airtimetopup.model.RateTypeModel;
import com.inov8.microbank.hra.service.HRAManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

public class ConversionRateController extends BaseSearchController {

        private ReferenceDataManager referenceDataManager;
        private HRAManager hraManager;
//        private ConversionRateDAO conversionRateDAO;

    public ConversionRateController() {
        super.setFilterSearchCommandClass(AirTimeTopViewModel.class);
    }

    @Override
    protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object o, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        System.out.println("Welcome to Update Method");
        AirTimeTopViewModel airTimeTopViewModel = new AirTimeTopViewModel();
        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
                airTimeTopViewModel, "conversionRateId", SortingOrder.DESC);
        referenceDataWrapper.setBasePersistableModel(airTimeTopViewModel);
        referenceDataWrapper = referenceDataManager.getReferenceData(referenceDataWrapper);
        List<AirTimeTopViewModel> airTimeTopViewModelList = referenceDataWrapper.getReferenceDataList();
        ModelAndView modelAndView = new ModelAndView(getSearchView(), "airTimeTopUpViewList",
                airTimeTopViewModelList);
        pagingHelperModel.setTotalRecordsCount(airTimeTopViewModelList.size());
        return modelAndView;
    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setHraManager(HRAManager hraManager) {
        this.hraManager = hraManager;
    }

    /*public void setConversionRateDAO(ConversionRateDAO conversionRateDAO) {
        this.conversionRateDAO = conversionRateDAO;
    }*/
}


