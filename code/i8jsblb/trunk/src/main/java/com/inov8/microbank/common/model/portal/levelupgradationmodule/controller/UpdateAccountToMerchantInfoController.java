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
import com.inov8.microbank.common.model.MerchantAccountModel;
import com.inov8.microbank.server.dao.customermodule.MerchantAccountModelDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateAccountToMerchantInfoController extends BaseFormSearchController {
    private ReferenceDataManager referenceDataManager;
    private MerchantAccountModelDAO merchantAccountModelDAO;
    private List<MerchantAccountModel> merchantAccountModelList;
    private CommonCommandManager commonCommandManager;

    public UpdateAccountToMerchantInfoController() {
        setCommandName("merchantAccountModel");
        setCommandClass(MerchantAccountModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
     return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        MerchantAccountModel merchantAccountModel = (MerchantAccountModel) o;
//        searchBaseWrapper.setBasePersistableModel(blinkCustomerModel);
        if (merchantAccountModel.getStart() != null && merchantAccountModel.getEnd() != null) {
            DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", merchantAccountModel.getStart(), merchantAccountModel.getEnd());
            searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.TRUE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);

        if (merchantAccountModel.getMerchanAccountId() != null) {
            Criterion criterion = null;
            Disjunction disjunction = Restrictions.disjunction();
            criterion = Restrictions.and(disjunction, Restrictions.eq("blinkCustomerId", merchantAccountModel.getMerchanAccountId()));
            CustomList<MerchantAccountModel> merchantAccountModelCustomList = merchantAccountModelDAO.findByCriteria(criterion, merchantAccountModel, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());
            return new ModelAndView(getFormView(), "blinkCustomerModelList", merchantAccountModelCustomList.getResultsetList());
        } else {
            CustomList<MerchantAccountModel> merchantAccountModelCustomList =
                    this.merchantAccountModelDAO.findByExample(merchantAccountModel, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), exampleConfigHolderModel);
            return new ModelAndView(getFormView(), "blinkCustomerModelList", merchantAccountModelCustomList.getResultsetList());
        }
    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }


    public void setMerchantAccountModelDAO(MerchantAccountModelDAO merchantAccountModelDAO) {
        this.merchantAccountModelDAO = merchantAccountModelDAO;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }
}
