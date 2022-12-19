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
import com.inov8.microbank.common.model.tax.FedDetailedReportModel;
import com.inov8.microbank.server.dao.accounttypemodule.AccountTypeDAO;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;
import com.inov8.microbank.server.dao.productmodule.ProductDAO;
import com.inov8.microbank.server.service.portal.taxreportmodule.TaxReportManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Attique on 8/18/2017.
 */
public class FedDetailedReportController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private TaxRegimeDAO taxRegimeDAO;
    private AccountTypeDAO accountTypeDAO;
    private ProductDAO productDAO;
    private TaxReportManager taxReportManager;
    public FedDetailedReportController()
    {
        setCommandName( "fedDetailedReportModel" );
        setCommandClass( FedDetailedReportModel.class );
    }
    @Override
    protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();

        List<SupplierProcessingStatusModel> supplierProcessingStatusModelList = null;
        List<AppUserTypeModel> appUserTypeModelList = null;
        referenceDataMap.put("taxRegimeList",this.taxRegimeDAO.findAll().getResultsetList());
        //referenceDataMap.put("accountTypeList",this.accountTypeDAO.findAll().getResultsetList());
        referenceDataMap.put("productList",this.productDAO.findAll().getResultsetList());
        ReferenceDataWrapper referenceDataWrapper = null;
        referenceDataWrapper = new ReferenceDataWrapperImpl( new SupplierProcessingStatusModel(), "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData(referenceDataWrapper);
        supplierProcessingStatusModelList = referenceDataWrapper.getReferenceDataList();
        referenceDataMap.put("supplierStatus",supplierProcessingStatusModelList);

        referenceDataWrapper = new ReferenceDataWrapperImpl( new AppUserTypeModel(), "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData(referenceDataWrapper);
        appUserTypeModelList = referenceDataWrapper.getReferenceDataList();
        referenceDataMap.put("accountTypeList",appUserTypeModelList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        FedDetailedReportModel fedDetailedReportModel = (FedDetailedReportModel) o;

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "transactionLastUpdatedOn", fedDetailedReportModel.getTransactionStartDate(), fedDetailedReportModel.getTransactionEndDate() );
       ArrayList<DateRangeHolderModel> l1=new ArrayList<>();
        l1.add(dateRangeHolderModel);
        wrapper.setDateRangeHolderModelList(l1);

        wrapper.setBasePersistableModel( fedDetailedReportModel );
        wrapper.setPagingHelperModel( pagingHelperModel );

        if( linkedHashMap.isEmpty() )
        {
            linkedHashMap.put("pk", SortingOrder.DESC );
        }
        wrapper.setSortingOrderMap( linkedHashMap );
        Date transactionDateTime=new Date();
        List<FedDetailedReportModel> fedDetailedReportModelList=new ArrayList<>();

        fedDetailedReportModelList=this.taxReportManager.getFedDetails(wrapper);
            return new ModelAndView( getFormView(),"fedDetailViewModelList", fedDetailedReportModelList );
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setTaxRegimeDAO(TaxRegimeDAO taxRegimeDAO) {
        this.taxRegimeDAO = taxRegimeDAO;
    }

    public void setAccountTypeDAO(AccountTypeDAO accountTypeDAO) {
        this.accountTypeDAO = accountTypeDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }


    public void setTaxReportManager(TaxReportManager taxReportManager) {
        this.taxReportManager = taxReportManager;
    }
}
