package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

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
import com.inov8.microbank.common.model.AccountOpeningMethodModel;
import com.inov8.microbank.common.model.AccountStateModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MinorUserInfoListViewModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchMinorUserInfoController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private MfsAccountManager mfsAccountManager;

    public SearchMinorUserInfoController() {
        setCommandName("minorUserInfoListViewModel");
        setCommandClass(MinorUserInfoListViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest req) throws Exception {
        Map referenceDataMap = new HashMap();
        CopyOnWriteArrayList<RegistrationStateModel> modifiedRegistrationStateModelsList = new CopyOnWriteArrayList<>();

        if (req.getParameter("infoMessage") != null){
            super.saveMessage(req, req.getParameter("infoMessage"));
        }

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


        RegistrationStateModel registrationStateModel = new RegistrationStateModel();
        ReferenceDataWrapper registrationStateReferenceDataWrapper = new ReferenceDataWrapperImpl(registrationStateModel, "registrationStateId", SortingOrder.ASC);
        registrationStateReferenceDataWrapper.setBasePersistableModel(registrationStateModel);
        try
        {
            referenceDataManager.getReferenceData(registrationStateReferenceDataWrapper);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

        List<RegistrationStateModel> registrationStateList = null;
        if (registrationStateReferenceDataWrapper.getReferenceDataList() != null)
        {
            registrationStateList = registrationStateReferenceDataWrapper.getReferenceDataList();
            modifiedRegistrationStateModelsList.addAll(registrationStateList);

            for(RegistrationStateModel regStateModel : modifiedRegistrationStateModelsList ){
                if(regStateModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.CLOSED){
                    modifiedRegistrationStateModelsList.remove(regStateModel);
                }
            }

        }
        referenceDataMap.put("registrationStateList", modifiedRegistrationStateModelsList);

        AccountStateModel accountStateModel = new AccountStateModel();
        ReferenceDataWrapper accountStateReferenceDataWrapper = new ReferenceDataWrapperImpl(accountStateModel, "accountStateId", SortingOrder.ASC);
        accountStateReferenceDataWrapper.setBasePersistableModel(accountStateModel);
        try
        {
            referenceDataManager.getReferenceData(accountStateReferenceDataWrapper);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

        List<AccountStateModel> accountStateList = null;
        if (accountStateReferenceDataWrapper.getReferenceDataList() != null)
        {
            accountStateList = accountStateReferenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("accountStateList", accountStateList);


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
        referenceDataMap.put("segmentList", segmentList);

        AccountOpeningMethodModel accountOpeningMethodModel = new AccountOpeningMethodModel();
        ReferenceDataWrapper accountOpeningMethodReferenceDataWrapper = new ReferenceDataWrapperImpl(accountOpeningMethodModel, "name", SortingOrder.ASC);
        accountOpeningMethodReferenceDataWrapper.setBasePersistableModel(accountOpeningMethodModel);
        try {
            referenceDataManager.getReferenceData(accountOpeningMethodReferenceDataWrapper);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        List<AccountOpeningMethodModel> accountOpeningMethodList = new ArrayList<>();
        if(null != accountOpeningMethodReferenceDataWrapper.getReferenceDataList()) {
            accountOpeningMethodList = accountOpeningMethodReferenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("accountOpeningMethodList", accountOpeningMethodList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        MinorUserInfoListViewModel minorUserInfoListViewModel = (MinorUserInfoListViewModel) model;
//		minorUserInfoListViewModel.setAccountEnabled(true);	// to fix bug # 149
        searchBaseWrapper.setBasePersistableModel(minorUserInfoListViewModel);

        if(sortingOrderMap.isEmpty()){
            sortingOrderMap.put("firstName", SortingOrder.ASC);
            sortingOrderMap.put("lastName", SortingOrder.ASC);
        }
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "accountOpeningDate", minorUserInfoListViewModel.getCreatedStartDate(),
                minorUserInfoListViewModel.getCreatedEndDate());
        DateRangeHolderModel dateRangeHolderModelOnUpdateDate = new DateRangeHolderModel("accountUpdatedOn",
                minorUserInfoListViewModel.getStartDate(),minorUserInfoListViewModel.getEndDate());
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        dateRangeHolderModels.add(dateRangeHolderModelOnUpdateDate);
        searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModels);
        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
        searchBaseWrapper = this.mfsAccountManager.searchMinorUserInfo(searchBaseWrapper);

        List<MinorUserInfoListViewModel> resultList = searchBaseWrapper.getCustomList().getResultsetList();

        checkNameDuplication(resultList);

        return new ModelAndView(getSuccessView(), "minorUserInfoListViewModelList", resultList);
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

    private void checkNameDuplication(List<MinorUserInfoListViewModel> resultList) {

        for(MinorUserInfoListViewModel viewModel : resultList) {

            String fullName = viewModel.getFullName();

            if(fullName != null && !fullName.isEmpty()) {

                String[] names = fullName.trim().split(" ");

                if(names != null && names.length == 2) {

                    String firstName = names[0];
                    String lastName = names[1];

                    if(firstName.equalsIgnoreCase(lastName)) {

                        viewModel.setFullName(firstName);
                    }
                }
            }
        }
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
