package com.inov8.microbank.webapp.action.ajax;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abu Turab
 */
public class ServiceProductAjaxController extends AjaxController {

	private ProductManager productManager;

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ProductModel productModel = new ProductModel();
		List<ProductModel> productList = new ArrayList<>();
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

		if(!StringUtil.isNullOrEmpty(ServletRequestUtils.getStringParameter(request, "serviceId"))){
			productModel.setServiceId(ServletRequestUtils.getLongParameter(request, "serviceId"));
			productModel.setActive(Boolean.TRUE);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(productModel);
			productManager.searchProductModels(searchBaseWrapper);
			if (searchBaseWrapper.getCustomList() != null) {
				productList = searchBaseWrapper.getCustomList().getResultsetList();
			}
			ajaxXmlBuilder.addItemAsCData("---Select---", "");
		}else{
			ajaxXmlBuilder.addItemAsCData("---Select---", "");
		}
		ajaxXmlBuilder.addItems( productList, "name", "productId" );
		return ajaxXmlBuilder.toString();
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
}
