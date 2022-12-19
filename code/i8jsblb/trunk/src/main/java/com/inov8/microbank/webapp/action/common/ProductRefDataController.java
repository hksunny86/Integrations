package com.inov8.microbank.webapp.action.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ProductRefDataController extends AjaxController {
    @Autowired
    private ProductManager productManager;

    public ProductRefDataController() {
    }

    @SuppressWarnings("unchecked")
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) {

        try {
            AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
            ajaxXmlBuilder.addItemAsCData("---Select---", "");

            String _serviceId = ServletRequestUtils.getStringParameter(request, "serviceId");
            if (GenericValidator.isBlankOrNull(_serviceId)) {
                return ajaxXmlBuilder.toString();
            }

            Long serviceId = Long.valueOf(_serviceId);
            List<LabelValueBean> labels = productManager.getProductLabelsByReferencedClass(ServiceModel.class, serviceId);

            ajaxXmlBuilder.addItems(labels, "label", "id");
            return ajaxXmlBuilder.toString();
        }

        catch (Exception e) {
            e.printStackTrace();
            String result = new AjaxXmlBuilder().addItemAsCData("---Select---", "").toString();

            return result;
        }
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

}
