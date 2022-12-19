package com.inov8.microbank.tax.controller;

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
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.tax.dao.WHTTrxViewModelDAO;
import com.inov8.microbank.tax.model.ExtendedWHTTrxViewModel;
import com.inov8.microbank.tax.model.WHTTrxViewModel;
import com.inov8.microbank.tax.service.DailyWhtDeductionManager;
import com.inov8.verifly.common.model.StatusModel;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.microbank.common.util.UserTypeConstantsInterface.CUSTOMER;
import static com.inov8.microbank.common.util.UserTypeConstantsInterface.RETAILER;

/**
 * Created by Malik on 8/11/2016.
 */
public class WHTTrxController extends BaseFormSearchController
{
    ReferenceDataManager referenceDataManager;
    DailyWhtDeductionManager dailyWhtDeductionManager;


    public WHTTrxController()
    {
        setCommandName("extendedWHTTrxViewModel");
        setCommandClass(ExtendedWHTTrxViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
    {
        Map<String,Object> referenceDataMap=new HashMap<>();
        RegistrationStateModel registrationStateModel = new RegistrationStateModel();
        List<RegistrationStateModel> registrationStateModelList = null;
        AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
        List<AppUserTypeModel> appUserTypeModelList = null;
        StatusModel statusModel=new StatusModel();
        List<StatusModel> statusModelList=null;

        ReferenceDataWrapper registrationStateDataWrapper = new ReferenceDataWrapperImpl(registrationStateModel, "name", SortingOrder.ASC);
        registrationStateModelList= (List<RegistrationStateModel>) referenceDataManager.getReferenceData(registrationStateDataWrapper).getReferenceDataList();
        referenceDataMap.put("registrationStateModelList",registrationStateModelList);

        ReferenceDataWrapper appUserTypeDataWrapper = new ReferenceDataWrapperImpl(appUserTypeModel, "name", SortingOrder.ASC);
        appUserTypeModelList= (List<AppUserTypeModel>) referenceDataManager.getReferenceData(appUserTypeDataWrapper,RETAILER,CUSTOMER).getReferenceDataList();
        referenceDataMap.put("appUserTypeModelList",appUserTypeModelList);

        ReferenceDataWrapper statusDataWrapper=new ReferenceDataWrapperImpl(statusModel,"name",SortingOrder.ASC);
        statusModelList= (List<StatusModel>) referenceDataManager.getReferenceData(statusDataWrapper).getReferenceDataList();
        referenceDataMap.put("statusModelList",statusModelList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
    {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        ExtendedWHTTrxViewModel extendedWHTTrxViewModel = (ExtendedWHTTrxViewModel) model;
        searchBaseWrapper.setBasePersistableModel(extendedWHTTrxViewModel);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",extendedWHTTrxViewModel.getStartDate(),extendedWHTTrxViewModel.getEndDate());
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        if (sortingOrderMap.isEmpty()) {
            sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

        CustomList<WHTTrxViewModel> resultList = dailyWhtDeductionManager.loadWHTTrx(searchBaseWrapper);

        return new ModelAndView(getFormView(),"WHTTrxViewModelList", resultList.getResultsetList());
    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }


    public void setDailyWhtDeductionManager(DailyWhtDeductionManager dailyWhtDeductionManager)
    {
        this.dailyWhtDeductionManager = dailyWhtDeductionManager;
    }

}
