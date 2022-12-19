package com.inov8.microbank.cardconfiguration.vo;

import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.common.model.ProductThresholdChargesModel;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductThresholdChargesVO implements Serializable {
    private static final long serialVersionUID = 6058253636035612722L;

    private List<ProductThresholdChargesModel> productThresholdChargesModelList;
    private Long productId;
    private String productName;

    @SuppressWarnings("unchecked")
    public ProductThresholdChargesVO() {
        super();
        productThresholdChargesModelList = LazyList.decorate(new ArrayList<ProductThresholdChargesModel>(), new Factory() {
            @Override
            public ProductThresholdChargesModel create() {
                return new ProductThresholdChargesModel();
            }
        });
    }

    public ProductThresholdChargesVO(Long productId)
    {
        this();
        this.productId = productId;
    }
    public List<ProductThresholdChargesModel> getProductThresholdChargesModelList() {
        return productThresholdChargesModelList;
    }

    public void setProductThresholdChargesModelList(List<ProductThresholdChargesModel> productThresholdChargesModelList) {
        this.productThresholdChargesModelList = productThresholdChargesModelList;
    }

    public void addProductThresholdChargesModel(ProductThresholdChargesModel productThresholdChargesModel) {
        this.productThresholdChargesModelList.add(productThresholdChargesModel);
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
