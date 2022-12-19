package com.inov8.microbank.disbursement.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.service.BulkDisbursementsFacade;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.servicemodule.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AtieqRe on 2/11/2017.
 */
public class DisbursementFileInfoController extends BaseFormSearchController {

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private BulkDisbursementsFacade bulkDisbursementsFacade;

    public DisbursementFileInfoController() {
        super.setCommandName("disbursementFileInfoViewModel");
        super.setCommandClass(DisbursementFileInfoViewModel.class);
    }
    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> referenceDataMap = new HashMap<>(2);

        referenceDataMap.put("serviceList", serviceManager.getServiceLabels(ServiceConstantsInterface.BULK_DISB_ACC_HOLDER,
                ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER));

        String serviceId = httpServletRequest.getParameter("serviceId");
        if(!StringUtil.isNullOrEmpty(serviceId)) {
            referenceDataMap.put("productList", productManager.getProductLabelsByReferencedClass(ServiceModel.class, Long.parseLong(serviceId)));
        }

        referenceDataMap.put("statusMap", DisbursementStatusConstants.map);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel,
                                    LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        DisbursementFileInfoViewModel vo = (DisbursementFileInfoViewModel) model;
        searchBaseWrapper.setBasePersistableModel(vo);
        searchBaseWrapper.setDateRangeHolderModel(new DateRangeHolderModel("createdOn", vo.getCreatedOnStart(), vo.getCreatedOnEnd()));

        if(sortingOrderMap.isEmpty()) {
          //  sortingOrderMap.put("batchNumber", SortingOrder.DESC);
            sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }

        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
        List<DisbursementFileInfoViewModel> list = this.bulkDisbursementsFacade.findDFIVModelByExample(searchBaseWrapper);

        req.getSession().removeAttribute("messages");

        return new ModelAndView(super.getSuccessView(), "disbursementFileInfoViewModelList", list);
    }

    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        CommonUtils.bindCustomDateEditor(binder);
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
}
