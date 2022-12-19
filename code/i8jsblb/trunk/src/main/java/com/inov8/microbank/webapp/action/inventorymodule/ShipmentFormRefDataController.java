package com.inov8.microbank.webapp.action.inventorymodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ShipmentFormRefDataController extends AjaxController
{

	private ReferenceDataManager	referenceDataManager;

	private ShipmentManager			shipmentManager;

	public void setShipmentManager(ShipmentManager shipmentManager)
	{
		this.shipmentManager = shipmentManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ProductModel productModel = new ProductModel();
		List<ProductModel> productModelList = null;
		String actionType = ServletRequestUtils.getRequiredStringParameter(request, "rType");
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			// Long areaId = request.getAttribute(arg0)

			if ("1".equals(actionType))
			{
				productModel.setSupplierId(ServletRequestUtils.getRequiredLongParameter(request, "supplierId"));
				productModel.setActive(true);
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name",
						SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
				if (actionType.equalsIgnoreCase("1"))
				{
					ajaxXmlBuilder.addItemAsCData("---All---", "");
				}
				ajaxXmlBuilder.addItems(productModelList, "name", "productId");
			}

			else if ("2".equals(actionType))
			{

				productModelList = shipmentManager.searchProductModels(ServletRequestUtils.getRequiredLongParameter(
						request, "supplierId"));
				boolean isVariableProduct=false;
				if (productModelList.size() == 0)
				{
					ajaxXmlBuilder.addItemAsCData("", "");
					
				}
				else
				{
					ajaxXmlBuilder.addItems(productModelList, "name", "productId");
					ProductModel product = (ProductModel) productModelList.get(0);
					isVariableProduct = shipmentManager.isVariableProduct(product.getProductId());
					ajaxXmlBuilder.addItem("isVariableProduct", String.valueOf(isVariableProduct));
				}

			}
			else if ("3".equals(actionType))
			{
				productModelList = shipmentManager.searchProductModels(ServletRequestUtils.getRequiredLongParameter(
						request, "supplierId"));
				Long proId = ServletRequestUtils.getRequiredLongParameter(request, "productId");
				boolean isVariableProduct = shipmentManager.isVariableProduct(proId);
				ajaxXmlBuilder.addItem("isVariableProduct", String.valueOf(isVariableProduct));
			}
			else
			{
				ajaxXmlBuilder.addItemAsCData("", "");
			}
		
			return ajaxXmlBuilder.toString();
	}

}
