package com.inov8.microbank.webapp.action.productmodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.DeviceFlowModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductDeviceFlowModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.server.service.productdeviceflowmodule.ProductDeviceFlowManager;

public class ProductDeviceFlowFormController extends AdvanceFormController{
    
	private ProductDeviceFlowManager productDeviceFlowManager;
	private ReferenceDataManager referenceDataManager;
	
	public ProductDeviceFlowFormController() {
	    setCommandName("productDeviceFlowModel");
	    setCommandClass(ProductDeviceFlowModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		Long id = ServletRequestUtils.getLongParameter(request, "productdeviceflowid");
		if(id!=null){
		    ProductDeviceFlowModel productDeviceFlowModel = new ProductDeviceFlowModel();
		    BaseWrapper baseWrapper = new BaseWrapperImpl();
		    productDeviceFlowModel.setProductDeviceFlowId(id);
		    baseWrapper.setBasePersistableModel(productDeviceFlowModel);
		    baseWrapper = this.productDeviceFlowManager.loadProductDeviceFlowByPrimaryKey(baseWrapper);
		    productDeviceFlowModel = (ProductDeviceFlowModel)baseWrapper.getBasePersistableModel();
		    Long suppId = productDeviceFlowModel.getProductIdProductModel().getSupplierIdSupplierModel().getSupplierId();
		    request.setAttribute("suppId", suppId);
		    return productDeviceFlowModel;
		}
		return new ProductDeviceFlowModel();
	}

	@SuppressWarnings( "unchecked" )
    @Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception {
	    /**
	     * code fragment to load reference data  for Supplier
	     */
	    SupplierModel supplierModel = new SupplierModel();
	    supplierModel.setActive(true);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	        supplierModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(supplierModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<SupplierModel> supplierModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      supplierModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    Map<String,Object> referenceDataMap = new HashMap<>();
	    referenceDataMap.put("supplierModelList", supplierModelList);
        
	    /**
	     * code fragment to load reference data  for Products
	     */
	    ProductModel productModel = new ProductModel();
	    Long suppId = (Long)request.getAttribute("suppId");
	    if(suppId != null)
	        productModel.setSupplierId(suppId);
	    else
	    	{
	    		if (supplierModelList.size()>0)
	    		{
	    		productModel.setSupplierId(((SupplierModel)supplierModelList.get(0)).getSupplierId());
	    		}
	    	}
	    productModel.setActive(true);
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	        productModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(productModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ProductModel> productModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      productModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    if (productModelList!=null)
		{	
	    referenceDataMap.put("productModelList", productModelList);
		}
	    /**
	     * code fragment to load reference data  for DeviceFlowModel
	     */
	    DeviceFlowModel deviceFlowModel = new DeviceFlowModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    	 deviceFlowModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(deviceFlowModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<DeviceFlowModel> deviceFlowModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	deviceFlowModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("deviceFlowModelList", deviceFlowModelList);

	    /**
	     * code fragment to load reference data  for DeviceType
	     */
	    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		deviceTypeModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(deviceTypeModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<DeviceTypeModel> deviceTypeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	deviceTypeModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);

	    
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException errors) throws Exception {
		ProductDeviceFlowModel productDeviceFlowModel = (ProductDeviceFlowModel)model;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(productDeviceFlowModel);
		try{
		    this.productDeviceFlowManager.createProductDeviceFlow(baseWrapper);
		    this.saveMessage(request, "Record saved successfully.");
		}
		catch(FrameworkCheckedException fce){
			
			if( fce.getMessage().equalsIgnoreCase("Recordsalreadyexist") )
			{
				super.saveMessage(request, "Product Device Flow with same Product and Device Type already exists");
				return super.showForm(request, response, errors);				
			}	
			else
			{
			logger.error( fce.getMessage(), fce );
			super.saveMessage(request, "Record could not be saved.");
			super.showForm(request, response, errors);
			}
		}
		catch(Exception fce){
			logger.error( fce.getMessage(), fce );
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			super.showForm(request, response, errors);
		}
		
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException errors) throws Exception {
		ProductDeviceFlowModel productDeviceFlowModel = (ProductDeviceFlowModel)model;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(productDeviceFlowModel);
		try{
		    baseWrapper = this.productDeviceFlowManager.updateProductDeviceFlow(baseWrapper);
		}
		catch(FrameworkCheckedException fce){
		    logger.error( fce.getMessage(), fce );
			super.saveMessage(request, "Record could not be saved");
			super.showForm(request, response, errors);
		}
		catch(Exception fce){
			logger.error( fce.getMessage(), fce );
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			super.showForm(request, response, errors);
		}
		this.saveMessage(request, "Record saved successfully.");
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
	}

	public void setProductDeviceFlowManager(
			ProductDeviceFlowManager productDeviceFlowManager) {
		this.productDeviceFlowManager = productDeviceFlowManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
