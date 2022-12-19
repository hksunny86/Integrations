package com.inov8.microbank.webapp.action.portal.transactiondetail;

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
import com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedTransactionDetailListViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionDetailListViewModel;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

public class TransactionDetailSearchController extends BaseFormSearchController
{

	private TransactionDetailManager transactionDetailManager;

	private ReferenceDataManager referenceDataManager;

	private ProductManager productManager;

	private SmsSender smsSender;
	
	private FinancialIntegrationManager financialIntegrationManager;

	public TransactionDetailSearchController()
	{
		super.setCommandName("extendedTransactionDetailListViewModel");
		super.setCommandClass(ExtendedTransactionDetailListViewModel.class);
	}

	@SuppressWarnings("unchecked")
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception
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
		Map referenceDataMap = new HashMap();
		referenceDataMap.put("supplierModelList", supplierModelList);
		referenceDataMap.put("productModelList", productModelList);

		return referenceDataMap;

	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap sortingOrderMap)
			throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		//     TransactionDetailListViewModel transactionDetailListViewModel = new TransactionDetailListViewModel();
		ExtendedTransactionDetailListViewModel extendedTransactionDetailListViewModel = (ExtendedTransactionDetailListViewModel) model;		
		extendedTransactionDetailListViewModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedTransactionDetailListViewModel
						.getStartDate(), extendedTransactionDetailListViewModel
						.getEndDate());

		//     BeanUtils.copyProperties(transactionDetailListViewModel,extendedTransactionDetailListViewModel);
		searchBaseWrapper
				.setBasePersistableModel((TransactionDetailListViewModel) extendedTransactionDetailListViewModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
        if(sortingOrderMap.isEmpty()){
        	sortingOrderMap.put("createdOn", SortingOrder.DESC );
        }
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		//searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper = this.transactionDetailManager
				.searchTransactionDetail(searchBaseWrapper);

		BankModel bankModel = new BankModel();
		bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
		httpServletRequest.setAttribute("veriflyRequired", veriflyRequired);
		
		return new ModelAndView(super.getSuccessView(),
				"transactionDetailModelList", searchBaseWrapper.getCustomList()
						.getResultsetList());

	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager)
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

	public void setTransactionDetailManager(
			TransactionDetailManager transactionDetailManager)
	{
		this.transactionDetailManager = transactionDetailManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}
	
}
