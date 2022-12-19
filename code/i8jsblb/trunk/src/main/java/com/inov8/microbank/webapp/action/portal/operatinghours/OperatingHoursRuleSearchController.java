package com.inov8.microbank.webapp.action.portal.operatinghours;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormController;


import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.server.facade.portal.operatinghoursmodule.OperatingHoursRuleFacade;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OperatingHoursRuleSearchController extends BaseFormSearchController {
    private static final Logger LOGGER = Logger.getLogger( OperatingHoursRuleSearchController.class );
    private ReferenceDataManager referenceDataManager;
    private OperatingHoursRuleFacade operatingHoursRuleFacade;
    //private VelocityRuleFacade velocityRuleFacade;
    private DeviceTypeManager deviceTypeManager;

    public OperatingHoursRuleSearchController() {
        super.setCommandName("extendedOperatingHoursRuleModel");
        super.setCommandClass(ExtendedOperatingHoursRuleModel.class);
    }

    @Override
    protected Map<String, List<?>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();
        ReferenceDataWrapper referenceDataWrapper;

        List<ProductModel> productModelList = null;
        ProductModel productModel = new ProductModel();
        productModel.setActive(true);

        List<SegmentModel> segmentModelLsit = null;
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);

        List<DeviceTypeModel> deviceTypeModelList = null;
        List<DistributorModel> distributorModelList = null;
        DistributorModel	distributorModel = new DistributorModel();
        distributorModel.setActive(true);


        List<DistributorLevelModel> distributorLevelModelList = null;
        DistributorLevelModel	distributorLevelModel = new DistributorLevelModel();
        distributorLevelModel.setActive(true);

        //Account Limit
        List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = null;
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        olaCustomerAccountTypeModel.setActive(true);

        referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.DESC);
        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
        }
        if (null != referenceDataWrapper.getReferenceDataList() )
        {
            productModelList = referenceDataWrapper.getReferenceDataList();
        }

        referenceDataMap.put("productList", productModelList);

        ////////////////////////////////////////////////////////////
        referenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.DESC);
        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
        }
        if (null != referenceDataWrapper.getReferenceDataList())
        {
            segmentModelLsit = referenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("segmentList", segmentModelLsit);

        ////////////////////////////////////////////////////////////
        //Load Device Type Reference data
        deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY,
                DeviceTypeConstantsInterface.ALLPAY_WEB,DeviceTypeConstantsInterface.BANKING_MIDDLEWARE, DeviceTypeConstantsInterface.WEB_SERVICE);
        referenceDataMap.put("deviceTypeList", deviceTypeModelList);
        ////////////////////////////////////////////////////////////
        referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.DESC);
        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
        }
        if (null != referenceDataWrapper.getReferenceDataList())
        {
            distributorModelList = referenceDataWrapper.getReferenceDataList();
        }

        referenceDataMap.put("distributorList", distributorModelList);

        //ACccount Type Limit
        referenceDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.DESC);

        try {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1) {
            ex1.printStackTrace();
        }

        if (null != referenceDataWrapper.getReferenceDataList()) {
            olaCustomerAccountTypeModelList = referenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("olaCustomerAccountTypeList", olaCustomerAccountTypeModelList);

        return referenceDataMap;
    }

    protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    Object model,
                                    PagingHelperModel pagingHelperModel,
                                    LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
    {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        ExtendedOperatingHoursRuleModel extendedOperatingHoursRuleModel = (ExtendedOperatingHoursRuleModel) model;

        searchBaseWrapper.setBasePersistableModel( (ExtendedOperatingHoursRuleModel) extendedOperatingHoursRuleModel );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedOperatingHoursRuleModel.getStartDate(),
                extendedOperatingHoursRuleModel.getEndDate());

        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        //sorting order
        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

        CustomList<OperatingHoursRuleModel> list = this.operatingHoursRuleFacade.searchOperatingHoursRule(searchBaseWrapper).getCustomList();

        return new ModelAndView( getSuccessView(), "operatingHoursRuleModelList", list.getResultsetList());
    }
    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setOperatingHoursRuleFacade(OperatingHoursRuleFacade operatingHoursRuleFacade) {
        this.operatingHoursRuleFacade = operatingHoursRuleFacade;
    }

    public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
        this.deviceTypeManager = deviceTypeManager;
    }
}
