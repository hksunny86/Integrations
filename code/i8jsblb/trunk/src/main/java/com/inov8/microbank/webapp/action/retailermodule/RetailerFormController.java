package com.inov8.microbank.webapp.action.retailermodule;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.PermissionGroupModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.RetailerTypeModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;

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

public class RetailerFormController extends AdvanceFormController {
	private RetailerManager retailerManager;

	private ReferenceDataManager referenceDataManager;

	private ProductCatalogManager catalogManager;

	private Long id;

	public RetailerFormController() {
		setCommandName("retailerModel");
		setCommandClass(RetailerModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) {

		RetailerTypeModel retailerTypeModel = new RetailerTypeModel();

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(retailerTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(retailerTypeModel);
		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException e) {

			e.printStackTrace();
		}
		List<RetailerTypeModel> retailerModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			retailerModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("retailerTypeModelList", retailerModelList);

		AreaModel areaModel = new AreaModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(areaModel);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<AreaModel> areaModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			areaModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("areaModelList", areaModelList);

		ProductCatalogModel catalogModel = new ProductCatalogModel();
		catalogModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(catalogModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(catalogModel);
		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (Exception e) {

		}
		List<ProductCatalogModel> catalogModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			catalogModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("catalogModelList", catalogModelList);

		if (id == null) {
//			DistributorModel distributorModel = new DistributorModel();
//			if (httpServletRequest.getParameter("areaId") != null) {
//				distributorModel.setAreaId(Long.getLong(httpServletRequest.getParameter("areaId")));
//				// distributorModel.setAreaId(
//				// ((AreaModel)areaModelList.get(0)).getAreaId() ) ;
//				distributorModel.setActive(true);
//				// distributorModel.setAreaId(2409L);
//			} else {
//				if (areaModelList.size() > 0) {
//					distributorModel.setAreaId(((AreaModel) areaModelList.get(0)).getAreaId());
//				}
//				distributorModel.setActive(true);
//			}
			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);

			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			List<DistributorModel> distributorModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper.getReferenceDataList();
//				removeNationalDistributor(distributorModelList);
			}

			referenceDataMap.put("distributorModelList", distributorModelList);
			DistributorModel singleDistributorModel = (DistributorModel)distributorModelList.get(0);
			if(singleDistributorModel != null){
				referenceDataMap.put("singleDistributorModel",singleDistributorModel);
			}
		}

		// load permission group for Retailor
		PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
		permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		referenceDataWrapper = new ReferenceDataWrapperImpl(permissionGroupModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(permissionGroupModel);
		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PermissionGroupModel> permissionGroupModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			permissionGroupModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("permissionGroupModelList", permissionGroupModelList);

		return referenceDataMap;
	}

	private void removeNationalDistributor(List<DistributorModel> distributorModelList) {
		if (distributorModelList != null) {
			for (Iterator iter = distributorModelList.iterator(); iter.hasNext();) {
				DistributorModel distributor = (DistributorModel) iter.next();
				if (true == distributor.getNational()) {
					iter.remove();
				}
			}
		}

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "retailerId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerModel retailerModel = new RetailerModel();
			retailerModel.setRetailerId(id);

			searchBaseWrapper.setBasePersistableModel(retailerModel);
			searchBaseWrapper = this.retailerManager.loadRetailer(searchBaseWrapper);

			Long permissionGroupIdInReq = (Long) searchBaseWrapper.getObject("permissionGroupId");
			httpServletRequest.setAttribute("permissionGroupIdInReq", permissionGroupIdInReq.toString());

			retailerModel = (RetailerModel) searchBaseWrapper.getBasePersistableModel();
			// retailerModel.setRelationProductCatalogueIdProductCatalogueModel(new
			// ProductCatalogueModel());

			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setAreaId(retailerModel.getAreaId());
			distributorModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorModel> distributorModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper.getReferenceDataList();
			}
			httpServletRequest.setAttribute("distributorModelList", distributorModelList);

			/*
			 * DistributorModel distributorModel = new DistributorModel();
			 * distributorModel.setAreaId(retailerModel.getAreaId());
			 * ReferenceDataWrapper referenceDataWrapper = new
			 * ReferenceDataWrapperImpl(distributorModel, "name",
			 * SortingOrder.ASC);
			 * referenceDataWrapper.setBasePersistableModel(distributorModel);
			 * 
			 * try {
			 * referenceDataManager.getReferenceData(referenceDataWrapper); }
			 * catch (FrameworkCheckedException e1) { // TODO Auto-generated
			 * catch block e1.printStackTrace(); }
			 * 
			 * List<DistributorModel> distributorModelList = null; if
			 * (referenceDataWrapper.getReferenceDataList() != null) {
			 * distributorModelList =
			 * referenceDataWrapper.getReferenceDataList(); }
			 * 
			 * httpServletRequest.setAttribute("distributorModelList",
			 * distributorModelList);
			 */

			if (log.isDebugEnabled()) {
				log.debug("Catalog id is : " + retailerModel.getProductCatalogueId());
			}
			return retailerModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			long theDate = new Date().getTime();
			RetailerModel retailerModel = new RetailerModel();
			retailerModel.setCreatedOn(new Date(theDate));
			return retailerModel;
		}

	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse, object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse, object, bindException);

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

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			RetailerModel retailerModel = new RetailerModel();
			long theDate = new Date().getTime();
			retailerModel = (RetailerModel) command;
			if (log.isDebugEnabled()) {
				log.debug("The catlog id received is: " + retailerModel.getProductCatalogueId());
			}
			if (null != id) {
				RetailerModel retModel = new RetailerModel();
				retModel.setPrimaryKey(id);
				baseWrapper.setBasePersistableModel(retModel);
				baseWrapper = this.retailerManager.loadRetailer(baseWrapper);
				retModel = (RetailerModel) baseWrapper.getBasePersistableModel();
				retailerModel.setCreatedOn(retModel.getCreatedOn());
				retailerModel.setUpdatedOn(new Date(theDate));
				retailerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			} else {
				Long permissionGroupId = ServletRequestUtils.getLongParameter(request, "permissionGroupId");
				baseWrapper.putObject("permissionGroupId", permissionGroupId);

				retailerModel.setCreatedOn(new Date(theDate));
				retailerModel.setUpdatedOn(new Date(theDate));
				retailerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				retailerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			}

			retailerModel.setActive(retailerModel.getActive() == null ? false : retailerModel.getActive());

			baseWrapper.setBasePersistableModel(retailerModel);
			baseWrapper = this.retailerManager.createOrUpdateRetailer(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel()) { // if not
																	// found

				this.saveMessage(request, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			} else {
				this.saveMessage(request, "Retailer with the same name already exists.");
				return super.showForm(request, response, errors);
			}
		} catch (FrameworkCheckedException ex) {
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode()) {

				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}

			throw ex;
		}
		catch (Exception ex) {
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}

	}

	public void setRetailerManager(RetailerManager retailerManager) {
		this.retailerManager = retailerManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setCatalogManager(ProductCatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	private List loadCatalogs() throws Exception {

		SearchBaseWrapper wrapper = this.catalogManager.loadAllCatalogs();
		return wrapper.getCustomList().getResultsetList();
	}

	public ProductCatalogManager getCatalogManager() {
		return catalogManager;
	}
}
