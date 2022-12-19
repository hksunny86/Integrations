package com.inov8.microbank.webapp.action.productmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.productmodule.ProductListViewModel;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

/**
 * <p>
 * Title: Microbank
 * </p>
 *
 * <p>
 * Description: Catalog managemetn.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company: Inov8 Limited
 * </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class CatalogFormController extends AdvanceFormController implements
		Cell

{
	private ProductManager productManager;

	private ProductCatalogManager productCatalogManager;

	private Long id;

	private ReferenceDataManager referenceDataManager;

	public CatalogFormController() {

		setCommandName("productCatalogModel");
		setCommandClass(ProductCatalogModel.class);
	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {

		Map refData = new HashMap();
		List<AppUserTypeModel> appUserTypeModelList;
		ReferenceDataWrapper referenceDataWrapper= new ReferenceDataWrapperImpl();
		AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(appUserTypeModel, "name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper, UserTypeConstantsInterface.CUSTOMER,UserTypeConstantsInterface.RETAILER,UserTypeConstantsInterface.HANDLER);
		appUserTypeModelList = referenceDataWrapper.getReferenceDataList();
		refData.put("appUserTypeModelList", appUserTypeModelList);


		return refData;
	}



	private void sortProductList(List<ProductListViewModel> allProductsList) {
		Collections.sort(allProductsList, new ProductsComparator());
	}

	// /////////////////////////////////////////////////////////////////////////////
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {

		ProductCatalogModel productCatalogModel = new ProductCatalogModel();

		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"productCatalogId");
		if (id != null) {
			// do update case handling
			if (log.isDebugEnabled()) {

				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			productCatalogModel = new ProductCatalogModel();
			productCatalogModel.setProductCatalogId(id);
			searchBaseWrapper.setBasePersistableModel(productCatalogModel);
			searchBaseWrapper = this.productCatalogManager
					.loadCatalog(searchBaseWrapper);
			productCatalogModel = (ProductCatalogModel) searchBaseWrapper
					.getBasePersistableModel();
			httpServletRequest.setAttribute("name", productCatalogModel
					.getName());
			httpServletRequest.setAttribute("description", productCatalogModel
					.getDescription());
			httpServletRequest.setAttribute("comments", productCatalogModel
					.getComments());
			httpServletRequest.setAttribute("productCatalogId",
					productCatalogModel.getProductCatalogId());
			httpServletRequest.setAttribute("createdOn", productCatalogModel
					.getCreatedOn());
			httpServletRequest.setAttribute("createdBy", productCatalogModel
					.getCreatedBy());
			httpServletRequest.setAttribute("updatedBy", productCatalogModel
					.getUpdatedBy());
			httpServletRequest.setAttribute("updatedOn", productCatalogModel
					.getUpdatedOn());
			httpServletRequest.setAttribute("versionNo", productCatalogModel
					.getVersionNo());
			httpServletRequest.setAttribute("appUserTypeId",productCatalogModel.getAppUserTypeId());
			searchBaseWrapper.setBasePersistableModel(productCatalogModel);
			return (ProductCatalogModel) searchBaseWrapper
					.getBasePersistableModel();

		} else {
			if (log.isDebugEnabled()) {
				log
						.debug("id is null....creating new instance of productCatlogModel");
			}
			long theDate = new Date().getTime();
			productCatalogModel = new ProductCatalogModel();
			productCatalogModel.setCreatedOn(new Date(theDate));
			productCatalogModel.setUpdatedOn(new Date(theDate));

		}
		return productCatalogModel;

	}

	// /////////////////////////////////////////////////////////////////////////////
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
									HttpServletResponse httpServletResponse, Object object,
									BindException bindException) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("CatalogFormController.onCreate called...");
		}

		if (id != null) {

			return this.updateCatalog(httpServletRequest, httpServletResponse,
					object, bindException);

		} else {

			return this.createCatalog(httpServletRequest, httpServletResponse,
					object, bindException);
		}

	}

	// /////////////////////////////////////////////////////////////////////////////
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
									HttpServletResponse httpServletResponse, Object object,
									BindException bindException) throws Exception {

		return this.updateCatalog(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	// ///////////////////////////////////////////////////////////////////////////
	private ModelAndView createCatalog(HttpServletRequest httpServletRequest,
									   HttpServletResponse response, Object object,
									   BindException bindException) throws Exception {

		// ////////////////Step 1://////////////////////////////////////////////
		// Prepare an array list of product IDs which were selected by
		// the user on the product catalog form.
		Integer prdIndex=0;
		Enumeration parameterNames = httpServletRequest.getParameterNames();
		Map<Integer, Integer> productIdList = new HashMap<Integer, Integer>();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			if (parameterName.startsWith("checkBox")) {
				String parameterValue = httpServletRequest.getParameter(parameterName);

				String prd = parameterName.replaceFirst("checkBox", "");
				int index = prd.indexOf('.');
				int removingIndex = prd.indexOf('_');

				if(index == -1) {
					prdIndex = Integer.valueOf(prd);
				}
				else{
					if(removingIndex!=-1)
						prd = prd.substring(index + 1, removingIndex);
					else
						prd = prd.substring(index + 1);

					prdIndex++;
				}

				Integer prodId = Integer.parseInt(prd);

				productIdList.put(prdIndex, prodId);
			}
		} // end of while

		if(productIdList.isEmpty()) {
			super.saveMessage(httpServletRequest, "Empty Catalog is not allowed. Please select some product(s).");

			return super.showForm(httpServletRequest, response, bindException);
		}

		httpServletRequest.setAttribute("prodIdList", productIdList);
		// Step 1 ends here

		// ////////////////Step 2://////////////////////////////////////////////
		// Put all necessary stuff in a wrapper object and pass this object
		// to the service layer (product catalog manager in this case)
		try {

			ProductCatalogModel productCatalogModel = (ProductCatalogModel) object;

			if (productCatalogModel.getActive() == null)
				productCatalogModel.setActive(false);
			productCatalogModel.setCreatedBy(UserUtils.getCurrentUser()
					.getPrimaryKey());
			productCatalogModel.setUpdatedBy(UserUtils.getCurrentUser()
					.getPrimaryKey());
			productCatalogModel.setCreatedOn(new Date(System
					.currentTimeMillis()));
			productCatalogModel.setUpdatedOn(new Date(System
					.currentTimeMillis()));


			Collection c = productIdList.values();
			Long[] prodIdArr = new Long[c.size()];

			int i = 0;
			for (Iterator iter = c.iterator(); iter.hasNext();) {
				prodIdArr[i] = ((Integer) iter.next()).longValue();
				i++;
			}

			productCatalogModel = populateProductCatalog(productCatalogModel, prodIdArr);

			BaseWrapper catalogProductInfo = new BaseWrapperImpl();
			catalogProductInfo.setBasePersistableModel(productCatalogModel);
			catalogProductInfo = this.productCatalogManager
					.createCatalog(catalogProductInfo);

			this.saveMessage(httpServletRequest, "Record saved successfully");

		}

		catch (FrameworkCheckedException ex) {

			if (ex.getMessage().equalsIgnoreCase(
					"ProductCatalogUniqueException"))

			{
				super.saveMessage(httpServletRequest,
						"Product Catalog with the same name already exists.");
				return super.showForm(httpServletRequest, response,
						bindException);
			}

			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(httpServletRequest,
						"Record could not be saved.");
				return super.showForm(httpServletRequest, response,
						bindException);
			}


		}
		catch (Exception ex) {


			super.saveMessage(httpServletRequest,
					MessageUtil.getMessage("6075"));
			return super.showForm(httpServletRequest, response,
					bindException);
		}
		ModelAndView mav = new ModelAndView(this.getSuccessView());
		return mav;

	}

	/**
	 * @param productCatalogModel
	 * @param prodIdArr
	 * @throws FrameworkCheckedException
	 */
	@SuppressWarnings("null")
	private ProductCatalogModel populateProductCatalog(ProductCatalogModel productCatalogModel, Long[] prodIdArr) throws FrameworkCheckedException {
		int i;
		List<ProductModel> productModelList = productManager.loadProductsByIds("relationCategoryIdCategoryModel", SortingOrder.ASC, prodIdArr);
		i = 0;
		Long catId = 0L;
		int counter = 0;
		try {
			for (ProductModel productModel : productModelList) {
				i++;
				ProductCatalogDetailModel catalogDetail = new ProductCatalogDetailModel();
				catalogDetail.setProductId(productModel.getProductId());
				if(productModel.getCategoryId()!=null){
					catalogDetail.setCategoryId(productModel.getCategoryIdCategoryModel().getCategoryId());
					if (!catId.equals(productModel.getCategoryIdCategoryModel().getCategoryId())) {
						i = 1;
					}
					catId = productModel.getCategoryIdCategoryModel().getCategoryId();
				}
				catalogDetail.setSequenceNo(i);
				catalogDetail.setShowSupplierInMenu(Boolean.FALSE);
				productCatalogModel.addProductCatalogIdProductCatalogDetailModel(catalogDetail);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return productCatalogModel;
	}

	private ModelAndView updateCatalog(HttpServletRequest httpServletRequest,
									   HttpServletResponse response, Object object,
									   BindException bindException) throws Exception {

		// ////////////////Step 1://////////////////////////////////////////////
		// Prepare an array list of product IDs which were selected by
		// the user on the product catalog form.
		Integer prdIndex=0;
		Enumeration parameterNames = httpServletRequest.getParameterNames();
		Map<Integer, Integer> productIdList = new HashMap<Integer, Integer>();
		ArrayList<Integer> prdIndeces = new ArrayList <Integer> ();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			if (parameterName.startsWith("checkBox")) {
				String parameterValue = httpServletRequest.getParameter(parameterName);

				String prd = parameterName.replaceFirst("checkBox", "");
				int index = prd.indexOf('.');
				int removingIndex = prd.indexOf('_');

				if(index == -1) {
					prdIndex = Integer.valueOf(prd);
				}
				else{
					if(removingIndex!=-1)
						prd = prd.substring(index + 1, removingIndex);
					else
						prd = prd.substring(index + 1);

					prdIndex++;
				}

				Integer prodId = Integer.parseInt(prd);

				productIdList.put(prdIndex, prodId);
			}
		} // end of while
		// Step 1 ends here

		if(productIdList.isEmpty()) {
			super.saveMessage(httpServletRequest, "Empty Catalog is not allowed. Please select some product(s).");

			return super.showForm(httpServletRequest, response, bindException);
		}

		// ////////////////Step 2://////////////////////////////////////////////
		// Put all necessary stuff in a wrapper object and pass this object
		// to the service layer (product catalog manager in this case)
		ProductCatalogModel productCatalogModel = (ProductCatalogModel) object;
		Long productCatalogId = productCatalogModel.getProductCatalogId();

		ProductCatalogModel proCatModel = new ProductCatalogModel();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		proCatModel.setProductCatalogId(id);
		baseWrapper.setBasePersistableModel(proCatModel);
		baseWrapper = this.productCatalogManager.loadCatalog(baseWrapper);
		proCatModel = (ProductCatalogModel) baseWrapper.getBasePersistableModel();

		if((proCatModel.getActive()!=null && proCatModel.getActive().booleanValue()==true) &&  productCatalogModel.getActive()==null || !productCatalogModel.getActive())
		{
			if(productCatalogManager.isAssociatedWithAgentOrHandler(productCatalogId))
			{
				Map<String,Object> modelMap = new HashMap<>(1);
				modelMap.put("productCatalogId", productCatalogId);
				this.saveMessage(httpServletRequest, "This product catalog is associated with agent(s)/handler(s), you cannot deactivate this.");
				return new ModelAndView("redirect:productcatalogform.html",modelMap);
			}
		}

		productCatalogModel.setCreatedBy(proCatModel.getCreatedBy());
		productCatalogModel.setCreatedOn(proCatModel.getCreatedOn());
		productCatalogModel.setVersionNo(proCatModel.getVersionNo());
		productCatalogModel.setProductCatalogId(proCatModel.getProductCatalogId());
		if (productCatalogModel.getActive() == null)
			productCatalogModel.setActive(false);

		productCatalogModel.setUpdatedBy(UserUtils.getCurrentUser().getPrimaryKey());
		productCatalogModel.setUpdatedOn(new Date(System.currentTimeMillis()));

		Collection c = productIdList.values();
		Long[] prodIdArr = new Long[c.size()];

		int i = 0;
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			prodIdArr[i] = ((Integer) iter.next()).longValue();
			i++;
		}


		populateProductCatalog(productCatalogModel, prodIdArr);

		BaseWrapper catalogProductInfo = new BaseWrapperImpl();
		catalogProductInfo.setBasePersistableModel(productCatalogModel);
		catalogProductInfo = this.productCatalogManager.updateCatalog(catalogProductInfo);

		this.saveMessage(httpServletRequest, "Record save successfully");
		ModelAndView mav = new ModelAndView(this.getSuccessView());

		return mav;

	}

	private boolean isAMemberOfTheList(ProductListViewModel prod, List<ProductListViewModel> selectedProducts) {
		for(ProductListViewModel plvm: selectedProducts){
			if (!plvm.getProductId().equals(prod.getProductId()))
				continue;

			prod.setSequenceNo(plvm.getSequenceNo());
			return true;
		}

		return false;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setproductCatalogManager(
			ProductCatalogManager productCatalogManager) {
		this.productCatalogManager = productCatalogManager;
	}

	public void setProductCatalogManager(
			ProductCatalogManager productCatalogManager) {
		this.productCatalogManager = productCatalogManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


// /////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////

	public String getExportDisplay(TableModel tableModel, Column column) {
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////////
	private List getAllActiveProducts(HttpServletRequest request)
			throws Exception {

		// Fetch list of all products
		ProductListViewModel prodModel = new ProductListViewModel();
		// Only active products need to be shown. So setting active
		// method of ProductListViewModel so that Hibernate could ultimately
		// translate it to a "where is_active = 1" clause
		prodModel.setActive(true);
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(prodModel);
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		if (request.getSession().getAttribute("sortingOrderMap") == null) {
			// sortingOrderMap.put("supplierName", SortingOrder.ASC);
			sortingOrderMap.put("productName", SortingOrder.ASC);
			wrapper.setSortingOrderMap(sortingOrderMap);
		} else {
			wrapper.setSortingOrderMap((LinkedHashMap) request.getSession()
					.getAttribute("sortingOrderMap"));
			wrapper.setPagingHelperModel((PagingHelperModel) request
					.getSession().getAttribute("pagingHelperModel"));
		}

		// wrapper.setSortingOrderMap();
		SearchBaseWrapper result = productManager.searchProduct(wrapper);
		return result.getCustomList().getResultsetList();

	}

	// /////////////////////////////////////////////////////////////////////////////

	public String getHtmlDisplay(TableModel tableModel, Column column) {
		// The purpose of this method is to generate dynamic html for combo box
		// column of the table on productcatalogform page.

		ProductListViewModel prodListModel = (ProductListViewModel) tableModel
				.getCurrentRowBean();

		HtmlBuilder html = new HtmlBuilder();
		ColumnBuilder columnBuilder = new ColumnBuilder(html, column);
		columnBuilder.tdStart();
		if (null != prodListModel.getChecked() && prodListModel.getChecked()) {
			html.input("hidden")
					.name("checkBox" + prodListModel.getProductId()).value(
					"SELECTED").xclose();
		} else {

			html.input("hidden")
					.name("checkBox" + prodListModel.getProductId()).value(
					"UNSELECTED").xclose();

		}
		html.input("checkbox").name(prodListModel.getProductId().toString());
		html.onclick("setProductSelectionState(this)");
		if (null != prodListModel.getChecked() && prodListModel.getChecked()) {
			html.checked();
		}
		html.xclose();
		columnBuilder.tdEnd();
		return html.toString();

	} // /////////////////////////////////////////////////////////////////////////////
}