package com.inov8.microbank.updatecustomername.Controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.portal.digidormancyaccountmodule.DigiDormancyAccountViewModel;
import com.inov8.microbank.updatecustomername.dao.UpdateCustomerNameDAO;
import com.inov8.microbank.updatecustomername.facade.UpdateCustomerNameFacade;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateCustomerNameInfoController extends BaseFormSearchController {

    UpdateCustomerNameDAO updateCustomerNameDAO;
    UpdateCustomerNameFacade updateCustomerNameFacade;

    public UpdateCustomerNameInfoController() {
        setCommandName("updateCustomerNameModel");
        setCommandClass(UpdateCustomerNameModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();
//        List<UpdateCustomerNameModel> updateCustomerNameModelList = updateCustomerNameDAO.getCustomerNames();
//        if(updateCustomerNameModelList!=null) {
//            referenceDataMap.put("customerNamesList", updateCustomerNameModelList);
//        }
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        UpdateCustomerNameModel modelToSearch = (UpdateCustomerNameModel) o;
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel();
        wrapper.setBasePersistableModel(modelToSearch);
        wrapper.setPagingHelperModel( pagingHelperModel );
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if(modelToSearch.getStart()!=null && modelToSearch.getEnd()!=null){
             dateRangeHolderModel = new DateRangeHolderModel("createdOn", modelToSearch.getStart(), modelToSearch.getEnd());
            wrapper.setDateRangeHolderModel(dateRangeHolderModel);
        }
        if (linkedHashMap.isEmpty()) {
            linkedHashMap.put("cnic", SortingOrder.ASC);
        }
        wrapper.setPagingHelperModel(pagingHelperModel);

        wrapper.setSortingOrderMap(linkedHashMap);
        wrapper = updateCustomerNameFacade.searchUpdateCustomerNames(wrapper);
        CustomList<UpdateCustomerNameModel> customList = wrapper.getCustomList();
        List<UpdateCustomerNameModel> updateCustomerNameModelList = null;
        if (customList != null) {
            updateCustomerNameModelList = wrapper.getCustomList().getResultsetList();
        }
        return new ModelAndView(getSuccessView(), "updateCustomerNameModelList", updateCustomerNameModelList);
    }

    public void setUpdateCustomerNameDAO(UpdateCustomerNameDAO updateCustomerNameDAO) {
        this.updateCustomerNameDAO = updateCustomerNameDAO;
    }

    public void setUpdateCustomerNameFacade(UpdateCustomerNameFacade updateCustomerNameFacade) {
        this.updateCustomerNameFacade = updateCustomerNameFacade;
    }
}
