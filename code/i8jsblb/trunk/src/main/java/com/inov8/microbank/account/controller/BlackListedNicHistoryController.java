package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.account.model.BlackListedNicHistoryViewModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.AccountStateModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlackListedNicHistoryController extends BaseFormSearchController {

    private AccountControlManager accountControlManager;
    private ReferenceDataManager referenceDataManager;

    public BlackListedNicHistoryController()
    {
        setCommandClass(BlackListedNicHistoryViewModel.class);
        setCommandName("blackListedNicHistoryViewModel");
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {

        Map<String,Object> referenceDataMap=new HashMap<>(2);
        AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
        List<AppUserTypeModel> appUserTypeModelList = null;
        ReferenceDataWrapper appUserTypeDataWrapper = new ReferenceDataWrapperImpl(appUserTypeModel, "name", SortingOrder.ASC);
        appUserTypeModelList= (List<AppUserTypeModel>) referenceDataManager.getReferenceData(appUserTypeDataWrapper, UserTypeConstantsInterface.CUSTOMER,UserTypeConstantsInterface.RETAILER,UserTypeConstantsInterface.HANDLER).getReferenceDataList();

        //Segment
        SegmentModel segmentModel = new SegmentModel();
        List<SegmentModel> segmentModelList = null;
        segmentModel.setIsActive(true);
       ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
                segmentModel, "name", SortingOrder.ASC);
        referenceDataManager.getReferenceData(referenceDataWrapper);
        if (referenceDataWrapper.getReferenceDataList() != null) {
            segmentModelList = referenceDataWrapper.getReferenceDataList();
        }
//


        OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
        customerAccountTypeModel.setActive(true);
        customerAccountTypeModel.setIsCustomerAccountType(true); //added by Turab
        ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(customerAccountTypeModel, "name", SortingOrder.ASC);
        customerAccountTypeDataWrapper.setBasePersistableModel(customerAccountTypeModel);
        try
        {
            referenceDataManager.getReferenceData(customerAccountTypeDataWrapper);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
        if (customerAccountTypeDataWrapper.getReferenceDataList() != null)
        {
            customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(customerAccountTypeDataWrapper.getReferenceDataList());
            if(! CollectionUtils.isEmpty(customerAccountTypeList)){
                removeSpecialAccountTypes(customerAccountTypeList);
            }

        }
        referenceDataMap.put("customerAccountTypeList", customerAccountTypeList);

        referenceDataMap.put("appUserTypeModelList",appUserTypeModelList);
        referenceDataMap.put("segmentModelList", segmentModelList);

        return referenceDataMap;
    }

    private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList){

        //Iterator<OlaCustomerAccountTypeModel> it = olaCustomerAccountTypeModelList.iterator();
        //So far only one special account type exists which is SETTLEMENT (id = 3L)
        for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
            if(model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
                    || model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER){
                olaCustomerAccountTypeModelList.remove(model);
            }
        }
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        BlackListedNicHistoryViewModel model = (BlackListedNicHistoryViewModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(model);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "actionDate", model.getActionDate(), model.getActionDateEnd());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
        searchBaseWrapper = accountControlManager.loadBlackListedNICHistory(searchBaseWrapper);
        List<BlackListedNicHistoryViewModel> list = null;
        if(searchBaseWrapper.getCustomList() != null && !searchBaseWrapper.getCustomList().getResultsetList().isEmpty())
        {
            list = searchBaseWrapper.getCustomList().getResultsetList();
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
        }
        else
        {
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
        }
        return new ModelAndView("p_blackListedNicsHistory","blackListedNicHistoryList",list);
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
