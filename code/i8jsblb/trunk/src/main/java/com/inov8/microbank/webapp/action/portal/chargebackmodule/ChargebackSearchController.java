package com.inov8.microbank.webapp.action.portal.chargebackmodule;

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
import com.inov8.microbank.common.model.portal.changemobilemodule.ExtendedChargebackListViewModel;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.chargebackmodule.ChargebackManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

public class ChargebackSearchController extends BaseFormSearchController
{
  private ChargebackManager chargebackManager;
  private ReferenceDataManager referenceDataManager;
  private ProductManager productManager;
  private SmsSender smsSender;
  private FinancialIntegrationManager financialIntegrationManager;


  public ChargebackSearchController()
  {
    super.setCommandName("extendedChargebackListViewModel");
    super.setCommandClass(ExtendedChargebackListViewModel.class);
  }

  @SuppressWarnings("unchecked")
protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws
      Exception
  {
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
		Map<String,Object> referenceDataMap = new HashMap<>();
		referenceDataMap.put("supplierModelList", supplierModelList);
		referenceDataMap.put("productModelList", productModelList);

		return referenceDataMap;

  }

  protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object model,
                                  PagingHelperModel pagingHelperModel,
                                  LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
  {
     SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
     searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
//     TransactionDetailListViewModel transactionDetailListViewModel = new TransactionDetailListViewModel();
     ExtendedChargebackListViewModel extendedChargebackListViewModel = (ExtendedChargebackListViewModel) model;
//     extendedChargebackListViewModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());


     DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
           "createdOn", extendedChargebackListViewModel.getStartDate(),
           extendedChargebackListViewModel.getEndDate());

//     BeanUtils.copyProperties(transactionDetailListViewModel,extendedTransactionDetailListViewModel);
     searchBaseWrapper.setBasePersistableModel((ChargebackListViewModel)extendedChargebackListViewModel);
     searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

     if(sortingOrderMap.isEmpty()){
         sortingOrderMap.put("createdOn", SortingOrder.DESC);
     }
     
     searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
     searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
     searchBaseWrapper = this.chargebackManager.searchChargebackTransaction(
         searchBaseWrapper);

	BankModel bankModel = new BankModel();
	bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
	BaseWrapper baseWrapperBank = new BaseWrapperImpl();
	baseWrapperBank.setBasePersistableModel(bankModel);
	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
	boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
	httpServletRequest.setAttribute("veriflyRequired", veriflyRequired);
     
     return new ModelAndView(super.getSuccessView(), "transactionDetailModelList",
                             searchBaseWrapper.getCustomList().getResultsetList());

  }



  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setSmsSender(SmsSender smsSender)
  {
    this.smsSender = smsSender;
  }

  

public void setFinancialIntegrationManager(
		FinancialIntegrationManager financialIntegrationManager) {
	this.financialIntegrationManager = financialIntegrationManager;
}

public void setChargebackManager(ChargebackManager chargebackManager) {
	this.chargebackManager = chargebackManager;
}
}
