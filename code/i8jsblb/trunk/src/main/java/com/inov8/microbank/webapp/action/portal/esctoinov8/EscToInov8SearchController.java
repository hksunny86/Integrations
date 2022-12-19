package com.inov8.microbank.webapp.action.portal.esctoinov8;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.portal.esctoinov8module.EscToInov8ViewModel;
import com.inov8.microbank.common.model.portal.esctoinov8module.ExtendedEscToInov8ViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.esctoinov8.EscToInov8Manager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Escalate To Inov8</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Basit Mehr
 * @version 1.0
 */

public class EscToInov8SearchController extends BaseFormSearchController {

    private EscToInov8Manager escToInov8Manager;
    private ReferenceDataManager referenceDataManager;
    private FinancialIntegrationManager financialIntegrationManager;

    public EscToInov8SearchController() {
        super.setCommandName("extendedEscToInov8ViewModel");
        super.setCommandClass(ExtendedEscToInov8ViewModel.class);
    }

    @SuppressWarnings("unchecked")
	protected Map loadReferenceData(HttpServletRequest request) throws
            Exception {

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = request.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);

		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<SupplierModel> supplierModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			supplierModelList = referenceDataWrapper.getReferenceDataList();
		}

		ProductModel productModel = null;
		List<ProductModel> productModelList = null;

		if (null != strSupplierId && !"".equals(strSupplierId))
		{
			try
			{
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
			}
			catch (Exception ex)
			{
				log.error("Error getting product data in loadReferenceData", ex);
				ex.printStackTrace();
			}
		}
		Map referenceDataMap = new HashMap();
		referenceDataMap.put("supplierModelList", supplierModelList);
		referenceDataMap.put("productModelList", productModelList);

		return referenceDataMap;

    }

    protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    Object object,
                                    PagingHelperModel pagingHelperModel,
                                    LinkedHashMap<String,SortingOrder> linkedHashMap) throws
            Exception {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        ExtendedEscToInov8ViewModel extendedEscToInov8ViewModel = (
        		ExtendedEscToInov8ViewModel) object;
        extendedEscToInov8ViewModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        
        //need for between range for start date and end date
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", extendedEscToInov8ViewModel.getStartDate(),
                extendedEscToInov8ViewModel.getEndDate());
        
        searchBaseWrapper.setBasePersistableModel((EscToInov8ViewModel)extendedEscToInov8ViewModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        
        if(linkedHashMap.isEmpty()){
            linkedHashMap.put("createdOn", SortingOrder.DESC);
        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);

        searchBaseWrapper = this.escToInov8Manager.searchEscToInov8(
                searchBaseWrapper);
        
    	BankModel bankModel = new BankModel();
    	bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
    	BaseWrapper baseWrapperBank = new BaseWrapperImpl();
    	baseWrapperBank.setBasePersistableModel(bankModel);
    	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
    	boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
    	httpServletRequest.setAttribute("veriflyRequired", veriflyRequired);

        
        return new ModelAndView(getSuccessView(),
                                "escToInov8ViewModelList",
                                searchBaseWrapper.getCustomList().
                                getResultsetList());

    }

    public void setEscToInov8Manager(EscToInov8Manager
                                          escToInov8Manager) {
        this.escToInov8Manager = escToInov8Manager;
    }

    public void setReferenceDataManager(ReferenceDataManager
                                        referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}


}
