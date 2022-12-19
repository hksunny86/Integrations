package com.inov8.microbank.tax.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.tax.WhtDetailedReportModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.dao.accounttypemodule.AccountTypeDAO;
import com.inov8.microbank.server.dao.productmodule.ProductDAO;
import com.inov8.microbank.server.service.portal.taxreportmodule.TaxReportManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Attique on 8/22/2017.
 */
public class WhtDetailedReportController extends BaseFormSearchController {
    private ProductDAO productDAO;
    private ReferenceDataManager referenceDataManager;
    private AccountTypeDAO accountTypeDAO;
    private TaxReportManager taxReportManager;
    public WhtDetailedReportController()
    {
        setCommandName( "whtDetailedReportModel" );
        setCommandClass( WhtDetailedReportModel.class );
    }
    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();

        List<SupplierProcessingStatusModel> supplierProcessingStatusModelList = null;
        List<AppUserTypeModel> appUserTypeModelList = null;
        //referenceDataMap.put("accountTypeList",this.accountTypeDAO.findAll().getResultsetList());
        referenceDataMap.put("productList",this.productDAO.findAll().getResultsetList());
        ReferenceDataWrapper referenceDataWrapper = null;
        referenceDataWrapper = new ReferenceDataWrapperImpl( new SupplierProcessingStatusModel(), "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData(referenceDataWrapper);
        supplierProcessingStatusModelList = referenceDataWrapper.getReferenceDataList();
        referenceDataMap.put("supplierStatus",supplierProcessingStatusModelList);

        List<LabelValueBean> adjustmentTypeList = new ArrayList<LabelValueBean>();
        LabelValueBean adjustmentType = new LabelValueBean("233/2", "1");
        adjustmentTypeList.add(adjustmentType);
        adjustmentType = new LabelValueBean("231/A", "3");
        adjustmentTypeList.add(adjustmentType);
        adjustmentType = new LabelValueBean("236/P", "2");
        adjustmentTypeList.add(adjustmentType);
        referenceDataMap.put("whtConfigList", adjustmentTypeList);
        List<LabelValueBean> taxPayerTypeList = new ArrayList<LabelValueBean>();
        LabelValueBean taxPayerType = new LabelValueBean("Filer", "Filer");
        taxPayerTypeList.add(taxPayerType);
        taxPayerType = new LabelValueBean("Non-Filer", "Non-Filer");
        taxPayerTypeList.add(taxPayerType);
        referenceDataMap.put("filerList", taxPayerTypeList);


        referenceDataWrapper = new ReferenceDataWrapperImpl( new AppUserTypeModel(), "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData(referenceDataWrapper);
        appUserTypeModelList = referenceDataWrapper.getReferenceDataList();
        referenceDataMap.put("accountTypeList",appUserTypeModelList);


        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        WhtDetailedReportModel fedDetailedReportModel = (WhtDetailedReportModel) o;

        /*DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "updatedOn", fedDetailedReportModel.getTransactionStartDate(), fedDetailedReportModel.getTransactionEndDate() );
        ArrayList<DateRangeHolderModel> dateRangeModel=new ArrayList<>();
        dateRangeModel.add(dateRangeHolderModel);
        wrapper.setDateRangeHolderModel(dateRangeHolderModel);

        wrapper.setBasePersistableModel( fedDetailedReportModel );
        wrapper.setPagingHelperModel( pagingHelperModel );*/

        wrapper.setBasePersistableModel(fedDetailedReportModel);
        wrapper.setPagingHelperModel(pagingHelperModel);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("transactionLastUpdatedOn",  fedDetailedReportModel.getTransactionStartDate(), fedDetailedReportModel.getTransactionEndDate());
        wrapper.setDateRangeHolderModel(dateRangeHolderModel);



        if( linkedHashMap.isEmpty() )
        {
            linkedHashMap.put("pk", SortingOrder.DESC );
        }
        wrapper.setSortingOrderMap( linkedHashMap );
        //Date transactionDateTime=new Date();
        List<WhtDetailedReportModel> fedDetailedReportModelList=new ArrayList<>();

        fedDetailedReportModelList=this.taxReportManager.getWhtReportDetails(wrapper);
        return new ModelAndView( getFormView(),"whtDetailViewModelList", fedDetailedReportModelList );
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setAccountTypeDAO(AccountTypeDAO accountTypeDAO) {
        this.accountTypeDAO = accountTypeDAO;
    }

    public void setTaxReportManager(TaxReportManager taxReportManager) {
        this.taxReportManager = taxReportManager;
    }
}
