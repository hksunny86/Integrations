package com.inov8.microbank.webapp.action.inventorymodule;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.model.ShipmentTypeModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;

/**
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backend Application for POS terminals.
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

public class ShipmentFormController extends AdvanceFormController {
	private ShipmentManager shipmentManager;

	private ReferenceDataManager referenceDataManager;

	private Long id;

	public ShipmentFormController() {
		setCommandName("shipmentModel");
		setCommandClass(ShipmentModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws FrameworkCheckedException {
		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data for PaymentType
		 * 
		 */

		PaymentModeModel paymentModeModel = new PaymentModeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				paymentModeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(paymentModeModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<PaymentModeModel> paymentTypeModelList = null;

		if (referenceDataWrapper.getReferenceDataList() != null) {
			paymentTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("paymentTypeModelList", paymentTypeModelList);

		/**
		 * code fragment to load reference data for Supplier
		 */

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(supplierModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(supplierModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<SupplierModel> supplierModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			supplierModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("supplierModelList", supplierModelList);

		/**
		 * code fragment to load reference data for Service Type
		 */

		ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(serviceTypeModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(serviceTypeModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<ServiceTypeModel> serviceTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			serviceTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("serviceTypeModelList", serviceTypeModelList);

		/**
		 * code fragment to load reference data for Service
		 */

		ServiceModel serviceModel = new ServiceModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(serviceModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(serviceModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<ServiceModel> serviceModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			serviceModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("serviceModelList", serviceModelList);

		/**
		 * code fragment to load reference data for Product
		 */
		String _suppId = httpServletRequest.getParameter("supplierId");
		ProductModel productModel = new ProductModel();
		List<ProductModel> productModelList = null;
		if (null == id) // in new record case
		{

			if (_suppId == null)
			// productModel.setSupplierId(
			// ((SupplierModel)supplierModelList.get(0)).getSupplierId() ) ;
			{
				if (supplierModelList.size() > 0) {
					productModelList = shipmentManager
							.searchProductModels(((SupplierModel) supplierModelList
									.get(0)).getSupplierId());
				}
			}

			else {
				productModelList = shipmentManager.searchProductModels(Long
						.valueOf(_suppId));

			}
			/*
			 * productModel.setSupplierId(Long.valueOf(_suppId));
			 * productModel.setActive(true); referenceDataWrapper = new
			 * ReferenceDataWrapperImpl( productModel, "name",
			 * SortingOrder.ASC);
			 * referenceDataWrapper.setBasePersistableModel(productModel);
			 * 
			 * referenceDataManager.getReferenceData(referenceDataWrapper);
			 * //List<ProductModel> productModelList = null; if
			 * (referenceDataWrapper.getReferenceDataList() != null) {
			 * productModelList = referenceDataWrapper.getReferenceDataList(); }
			 * 
			 */
			if (productModelList != null) {
				referenceDataMap.put("productModelList", productModelList);
				if (productModelList.size() > 0) {
					boolean isVariableProduct = shipmentManager
							.isVariableProduct(((ProductModel) productModelList
									.get(0)).getProductId());
					httpServletRequest.setAttribute("isVariableProduct",
							isVariableProduct);
				}

			}

		} else {

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			ShipmentModel shipmentModel = new ShipmentModel();
			shipmentModel.setShipmentId(id);
			searchBaseWrapper.setBasePersistableModel(shipmentModel);
			searchBaseWrapper = this.shipmentManager
					.loadShipment(searchBaseWrapper);

			shipmentModel = (ShipmentModel) searchBaseWrapper
					.getBasePersistableModel();

			productModelList = shipmentManager
					.searchProductModels(shipmentModel.getSupplierId());

			if (productModelList != null) {

				if (productModelList.size() > 0) {
					referenceDataMap.put("productModelList", productModelList);
					// boolean isVariableProduct =
					// shipmentManager.isVariableProduct(((ProductModel)productModelList.get(0)).getProductId());
					// httpServletRequest.setAttribute("isVariableProduct",
					// isVariableProduct);
				}

			}

			// in the update case
			// if(_suppId == null)
			// productModel.setSupplierId(
			// ((SupplierModel)supplierModelList.get(0)).getSupplierId() ) ;
			// else
			/*
			 * productModel.setSupplierId(id); productModel.setActive(true);
			 * referenceDataWrapper = new ReferenceDataWrapperImpl(
			 * productModel, "name", SortingOrder.ASC);
			 * referenceDataWrapper.setBasePersistableModel(productModel);
			 * 
			 * referenceDataManager.getReferenceData(referenceDataWrapper); List<ProductModel>
			 * productModelList = null; if
			 * (referenceDataWrapper.getReferenceDataList() != null) {
			 * productModelList = referenceDataWrapper.getReferenceDataList(); }
			 */
			// referenceDataMap.put("productModelList", productModelList);
		} // end else

		/**
		 * code fragment to load reference data for Service Type
		 */

		ShipmentTypeModel shipmentTypeModel = new ShipmentTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(shipmentTypeModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(shipmentTypeModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<ShipmentTypeModel> shipmentTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			shipmentTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("shipmentTypeModelList", shipmentTypeModelList);

		/**
		 * code fragment to load reference data for Shipment Reference
		 */

		ShipmentModel shipmentModel = new ShipmentModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(shipmentModel,
				"shipmentId", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(shipmentModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<ShipmentModel> shipmentModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			shipmentModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("shipmentModelList", shipmentModelList);

		return referenceDataMap;

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"shipmentId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			ShipmentModel shipmentModel = new ShipmentModel();
			shipmentModel.setShipmentId(id);
			searchBaseWrapper.setBasePersistableModel(shipmentModel);
			searchBaseWrapper = this.shipmentManager
					.loadShipment(searchBaseWrapper);

			shipmentModel = (ShipmentModel) searchBaseWrapper
					.getBasePersistableModel();

			DecimalFormat myFormatter = new DecimalFormat("###.##");
			if (shipmentModel.getPrice() != null) {
				Double price = shipmentModel.getPrice();
				String _price = myFormatter.format(price);
				httpServletRequest.setAttribute("price", _price);
			}
			if (shipmentModel.getCreditAmount() != null) {
				Double creditAmount = shipmentModel.getCreditAmount();
				String _creditAmount = myFormatter.format(creditAmount);
				httpServletRequest.setAttribute("creditAmount", _creditAmount);
			}
			if (shipmentModel.getOutstandingCredit() != null) {
				Double outstandingCredit = shipmentModel.getOutstandingCredit();
				String _outstandingCredit = myFormatter
						.format(outstandingCredit);
				httpServletRequest.setAttribute("outstandingCredit",
						_outstandingCredit);
			}

			ProductModel productModel = new ProductModel();
			productModel.setSupplierId(shipmentModel.getSupplierId());
			productModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					productModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(productModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<ProductModel> productModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				productModelList = referenceDataWrapper.getReferenceDataList();
			}

			boolean isVariableProduct = shipmentManager
					.isVariableProduct(shipmentModel.getProductId());
			httpServletRequest.setAttribute("isVariableProduct",
					isVariableProduct);

			System.out.println("######################----> "
					+ httpServletRequest.getAttribute("isVariableProduct"));

			boolean isCurDateGTExpiryDate = shipmentManager
					.isCurDateGTExpiryDate(shipmentModel.getExpiryDate());

			if (isCurDateGTExpiryDate) {
				httpServletRequest.setAttribute("isCurDateGTExpiryDate",
						isCurDateGTExpiryDate);
			}

			httpServletRequest.setAttribute("productModelList",
					productModelList);

			/**
			 * Now Loading quantity and Outstanding credit form shipment list
			 * view
			 */
			/*
			 * BaseWrapper baseWrapper =
			 * this.shipmentManager.loadShipmentListViewByPrimaryKey(id);
			 * ShipmentListViewModel shipmentListViewModel =
			 * (ShipmentListViewModel)baseWrapper.getBasePersistableModel();
			 * shipmentModel.setQuantity(shipmentListViewModel.getQuantity().longValue());
			 * shipmentModel.setOutstandingCredit(shipmentListViewModel.getOutstandingCredit());
			 */
			return shipmentModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			DecimalFormat myFormat = new DecimalFormat("###.##");
			ShipmentModel model = new ShipmentModel();
			model.setQuantity(new Long(0));
			model.setOutstandingCredit(new Double(0));
			model.setCreditAmount(new Double(0));
			model.setComments("");

			httpServletRequest.setAttribute("outstandingCredit", myFormat
					.format(model.getOutstandingCredit()));
			httpServletRequest.setAttribute("creditAmount", myFormat
					.format(model.getCreditAmount()));
			return model;
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param command
	 *            Object
	 * @param errors
	 *            BindException
	 * @return ModelAndView
	 * @throws Exception
	 */
	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ShipmentModel shipmentModel = (ShipmentModel) command;
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			long theDate = new Date().getTime();

			if (null != id) {
				ShipmentModel shipModel = new ShipmentModel();
				shipModel.setPrimaryKey(id);
				baseWrapper.setBasePersistableModel(shipModel);
				baseWrapper = this.shipmentManager.loadShipment(baseWrapper);
				shipModel = (ShipmentModel) baseWrapper
						.getBasePersistableModel();
				shipmentModel.setCreatedOn(shipModel.getCreatedOn());
				shipmentModel.setUpdatedOn(new Date(theDate));
				shipmentModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
			} else {
				// shipmentModel.setOutstandingCredit(0.0);
				shipmentModel.setUpdatedOn(new Date(theDate));
				shipmentModel.setCreatedOn(new Date(theDate));
				shipmentModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
				shipmentModel.setCreatedByAppUserModel(UserUtils
						.getCurrentUser());
			}

			shipmentModel.setActive(shipmentModel.getActive() == null ? false
					: shipmentModel.getActive());

			baseWrapper.setBasePersistableModel(shipmentModel);
			baseWrapper = this.shipmentManager
					.createOrUpdateShipment(baseWrapper);
			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(getSuccessView()
					+ "?shipmentId="
					+ baseWrapper.getBasePersistableModel().getPrimaryKey());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				String isUpdate = request.getParameter("isUpdate");
				if (isUpdate == null)
					shipmentModel.setShipmentId(null);
				super
						.saveMessage(
								request,
								"Shipment with same Supplier, Product, Shipment Type, Payment Mode and Purchase Date already exists.");

				return super.showForm(request, response, errors);
			}
			throw ex;
		}

	}

	public void setShipmentManager(ShipmentManager shipmentManager) {
		this.shipmentManager = shipmentManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
