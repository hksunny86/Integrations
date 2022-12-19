package com.inov8.microbank.disbursement.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.server.dao.servicemodule.ServiceDAO;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BulkPaymentDisbReportSearchController extends BaseFormSearchController {

    @Autowired
    private ReferenceDataManager referenceDataManager;
    @Autowired
    private ServiceDAO serviceDAO;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private BulkDisbursementsManager bulkDisbursementsManager;

    Map<Long, String> serviceDataMap = new HashMap<>(2);
    Map<Long, String> productDataMap = new HashMap<>(5);

    public BulkPaymentDisbReportSearchController() {
        super.setCommandName("bulkDisbursementsVOModel");
        super.setCommandClass(BulkDisbursementsVOModel.class);
    }

    protected Map<String, Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {

        Map<String, Object> referenceDataMap = new HashMap<>(2);
        referenceDataMap.put("serviceModelList", serviceDAO.getServiceLabels(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER, ServiceConstantsInterface.BULK_DISB_ACC_HOLDER));

        String serviceId = httpServletRequest.getParameter("serviceId");
        if(!StringUtil.isNullOrEmpty(serviceId)) {
            referenceDataMap.put("productList", productManager.getProductLabelsByReferencedClass(ServiceModel.class, Long.parseLong(serviceId)));
        }

        BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel) super.getCommand(httpServletRequest);
        bulkDisbursementsVOModel.setBatchNumber(httpServletRequest.getParameter("batchNumber"));

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> map) throws Exception {

        BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel) object;

        BulkDisbursementsModel bulkDisbursementsModel = new BulkDisbursementsModel();
        bulkDisbursementsModel.setServiceId(bulkDisbursementsVOModel.getServiceId());
        bulkDisbursementsModel.setProductId(bulkDisbursementsVOModel.getProductId());
        bulkDisbursementsModel.setValidRecord(bulkDisbursementsVOModel.getValidRecord());
        bulkDisbursementsModel.setBatchNumber(bulkDisbursementsVOModel.getBatchNumber());

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", bulkDisbursementsVOModel.getUploadFromDate(), bulkDisbursementsVOModel.getUploadToDate());

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(bulkDisbursementsModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        if (map != null && map.isEmpty()) {
            map.put("bulkDisbursementsId", SortingOrder.DESC);
        }

        searchBaseWrapper.setSortingOrderMap(map);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        List<BulkDisbursementsModel> bulkDisbursementsModelList = bulkDisbursementsManager.searchBulkDisbursementsModelList(searchBaseWrapper);

        if (serviceDataMap.isEmpty() || productDataMap.isEmpty()) {
            prepData();
        }

        for (BulkDisbursementsModel disbursementsModel : bulkDisbursementsModelList) {

            disbursementsModel.setProductName(productDataMap.get(disbursementsModel.getProductId()));
            disbursementsModel.setServiceName(serviceDataMap.get(disbursementsModel.getServiceId()));
            disbursementsModel.setSourceACNo(disbursementsModel.getFileInfoIdBulkDisbursementsFileInfoModel().getSourceAccountNumber());
        }




        return new ModelAndView(getSuccessView(), "bulkDisbursementsModelList", bulkDisbursementsModelList);
    }

    void prepData() throws FrameworkCheckedException {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setActive(Boolean.TRUE);
        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(serviceModel, "name", SortingOrder.ASC);
        referenceDataWrapper = referenceDataManager.getReferenceData(referenceDataWrapper, ServiceConstantsInterface.BULK_DISB_ACC_HOLDER, ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER);

        List<ServiceModel> serviceModelList = referenceDataWrapper.getReferenceDataList();

        for (ServiceModel model : serviceModelList) {
            serviceDataMap.put(model.getServiceId(), model.getName());
        }

        List<ProductModel> productModelList = productManager.loadProductListByService(Arrays.copyOf(serviceDataMap.keySet().toArray(), serviceDataMap.keySet().toArray().length, Long[].class));

        for (ProductModel productModel : productModelList) {
            productDataMap.put(productModel.getProductId(), productModel.getName());
        }
    }

    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager) {
        this.bulkDisbursementsManager = bulkDisbursementsManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
}