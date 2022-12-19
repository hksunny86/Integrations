package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.BispExtendedTransactionDetailModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.BispTransactionDetailViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ReportConstants;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BISPTransactionDetailSearchController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private TransactionDetailI8Manager transactionDetailI8Manager;

    public BISPTransactionDetailSearchController()
    {
        super.setCommandName("bispExtendedTransactionDetailModel");
        super.setCommandClass(BispExtendedTransactionDetailModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Integer totalRecordsCount = (Integer) httpServletRequest.getAttribute("totalRecordsCount");
        if(totalRecordsCount == null || totalRecordsCount == 0) {
            httpServletRequest.getSession().removeAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR);
        }
        httpServletRequest.setAttribute(ReportConstants.REPORT_ID, ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT);
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();
        SupplierModel supplierModel = new SupplierModel();
        supplierModel.setActive(true);
        try
        {
            ProductModel productModel = null;
            List<ProductModel> productModelList = null;
            productModel = new ProductModel();
            productModel.setActive(true);
            ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
            referenceDataManager.getReferenceData(referenceDataWrapper,new Long[]{ProductConstantsInterface.BISP_CASH_OUT,
                    ProductConstantsInterface.BISP_CASH_OUT_WALLET,ProductConstantsInterface.BOP_CASH_OUT,ProductConstantsInterface.BOP_CASH_OUT_COVID_19});
            if (referenceDataWrapper.getReferenceDataList() != null)
                productModelList = referenceDataWrapper.getReferenceDataList();
            referenceDataMap.put("productModelList", productModelList);
        }
        catch (Exception ex)
        {
            log.error("Error getting product data in loadReferenceData", ex);
            ex.printStackTrace();
        }

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        BispExtendedTransactionDetailModel bispExtendedTransactionDetailModel = (BispExtendedTransactionDetailModel) o;
        String reportType = ServletRequestUtils.getStringParameter( httpServletRequest, "reportType" );
        if( reportType != null )
        {
            Long productId = ReportTypeEnum.valueOf( reportType.toUpperCase() ).getProductId();
            if( productId != null )
                bispExtendedTransactionDetailModel.setProductId( productId );
        }
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", bispExtendedTransactionDetailModel.getStartDate(),
                bispExtendedTransactionDetailModel.getEndDate());
        searchBaseWrapper.setBasePersistableModel((BispTransactionDetailViewModel)bispExtendedTransactionDetailModel);
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModels);
        if(linkedHashMap.isEmpty())
            linkedHashMap.put("createdOn", SortingOrder.DESC);

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        searchBaseWrapper = this.transactionDetailI8Manager.searchBispTransactionDetail(searchBaseWrapper);
        List<TransactionDetailPortalListModel> list = null;
        if(searchBaseWrapper.getCustomList() != null)
            list = searchBaseWrapper.getCustomList().getResultsetList();
        httpServletRequest.getSession().setAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR, new ReportCriteriaSessionObject(searchBaseWrapper));
        Integer totalRecordsCount = 0;
        if(pagingHelperModel != null)
            totalRecordsCount = pagingHelperModel.getTotalRecordsCount();
        httpServletRequest.setAttribute("totalRecordsCount", totalRecordsCount);
        return new ModelAndView( getSuccessView(), "transactionDetailPortalList",list);
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setTransactionDetailI8Manager(TransactionDetailI8Manager transactionDetailI8Manager)
    {
        this.transactionDetailI8Manager = transactionDetailI8Manager;
    }
}
