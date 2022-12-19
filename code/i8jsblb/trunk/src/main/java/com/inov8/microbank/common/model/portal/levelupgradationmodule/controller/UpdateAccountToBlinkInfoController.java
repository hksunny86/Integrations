package com.inov8.microbank.common.model.portal.levelupgradationmodule.controller;
/* 
Created by IntelliJ IDEA.
  @Copyright: 1/11/2022 On: 11:36 AM
  @author(Muhammad Aqeel).
  @project:trunk.
*/


import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.Manager.BlinkCustomerModelManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateAccountToBlinkInfoController extends BaseFormSearchController {
    private ReferenceDataManager referenceDataManager;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
    private BlinkCustomerModelManager blinkCustomerModelManager;
    private List<BlinkCustomerModel> blinkCustomerModelList;
    private CommonCommandManager commonCommandManager;

    public UpdateAccountToBlinkInfoController() {
        setCommandName("blinkCustomerModel");
        setCommandClass(BlinkCustomerModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();
        Long customerAccId = null;
        List<BlinkCustomerModel> data = blinkCustomerModelDAO.getAllData();
        if (data != null) {
            referenceDataMap.put("customerModelList", data);
        }
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        BlinkCustomerModel blinkCustomerModel = (BlinkCustomerModel) o;
//        searchBaseWrapper.setBasePersistableModel(blinkCustomerModel);
        if (blinkCustomerModel.getStart() != null && blinkCustomerModel.getEnd() != null) {
            DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", blinkCustomerModel.getStart(), blinkCustomerModel.getEnd());
            searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.TRUE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);

        if (blinkCustomerModel.getBlinkCustomerId() != null) {
            Criterion criterion = null;
            Disjunction disjunction = Restrictions.disjunction();
            criterion = Restrictions.and(disjunction, Restrictions.eq("blinkCustomerId", blinkCustomerModel.getBlinkCustomerId()));
            CustomList<BlinkCustomerModel> blinkCustomerModelList = blinkCustomerModelDAO.findByCriteria(criterion, blinkCustomerModel, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());
            return new ModelAndView(getFormView(), "blinkCustomerModelList", blinkCustomerModelList.getResultsetList());
        } else {
            CustomList<BlinkCustomerModel> blinkCustomerModelList =
                    this.blinkCustomerModelDAO.findByExample(blinkCustomerModel, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), exampleConfigHolderModel);
            return new ModelAndView(getFormView(), "blinkCustomerModelList", blinkCustomerModelList.getResultsetList());
        }
    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }


    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setBlinkCustomerModelManager(BlinkCustomerModelManager blinkCustomerModelManager) {
        this.blinkCustomerModelManager = blinkCustomerModelManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }
}
