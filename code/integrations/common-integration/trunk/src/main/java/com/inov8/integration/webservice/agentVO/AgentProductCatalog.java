package com.inov8.integration.webservice.agentVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentProductCatalog implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;


    private Long pk;
    private Long productCatalogId;
    private String productName;
    private Long productId;
    private transient String productIdEncrypt;
    private Long mnoId;
    private Double unitPrice;
    private Long supplierId;
    private Integer sequenceNo;
    private Integer sequenceForConsumerApp;
    private String supplierName;
    private String serviceTypeName;
    private Integer productCatalogVersionNo;
    private Long serviceId;
    private Long serviceTypeId;
    private Long deviceFlowId;
    private String billServiceLabel;
    private Long categoryId;
    private String ext;
    private String categoryName;
    private Boolean showSupplierInMenu;
    private Long deviceTypeId;
    private String consumerLabel;
    private Boolean amtRequired;
    private Double minLimit;
    private Double maxLimit;
    private Boolean doValidate;
    private String consumerInputType;
    private Long multiples;
    private Double minConsumerLength;
    private Double maxConsumerLength;
    private Boolean inquiryRequired;
    private Boolean partialPaymentAllowed;
    private String url;
    private String prodDenom;
    private Boolean denomFlag;
    private String denomString;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Long getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(Long productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductIdEncrypt() {
        return productIdEncrypt;
    }

    public void setProductIdEncrypt(String productIdEncrypt) {
        this.productIdEncrypt = productIdEncrypt;
    }

    public Long getMnoId() {
        return mnoId;
    }

    public void setMnoId(Long mnoId) {
        this.mnoId = mnoId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getSequenceForConsumerApp() {
        return sequenceForConsumerApp;
    }

    public void setSequenceForConsumerApp(Integer sequenceForConsumerApp) {
        this.sequenceForConsumerApp = sequenceForConsumerApp;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public Integer getProductCatalogVersionNo() {
        return productCatalogVersionNo;
    }

    public void setProductCatalogVersionNo(Integer productCatalogVersionNo) {
        this.productCatalogVersionNo = productCatalogVersionNo;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Long getDeviceFlowId() {
        return deviceFlowId;
    }

    public void setDeviceFlowId(Long deviceFlowId) {
        this.deviceFlowId = deviceFlowId;
    }

    public String getBillServiceLabel() {
        return billServiceLabel;
    }

    public void setBillServiceLabel(String billServiceLabel) {
        this.billServiceLabel = billServiceLabel;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getShowSupplierInMenu() {
        return showSupplierInMenu;
    }

    public void setShowSupplierInMenu(Boolean showSupplierInMenu) {
        this.showSupplierInMenu = showSupplierInMenu;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getConsumerLabel() {
        return consumerLabel;
    }

    public void setConsumerLabel(String consumerLabel) {
        this.consumerLabel = consumerLabel;
    }

    public Boolean getAmtRequired() {
        return amtRequired;
    }

    public void setAmtRequired(Boolean amtRequired) {
        this.amtRequired = amtRequired;
    }

    public Double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public Double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Boolean getDoValidate() {
        return doValidate;
    }

    public void setDoValidate(Boolean doValidate) {
        this.doValidate = doValidate;
    }

    public String getConsumerInputType() {
        return consumerInputType;
    }

    public void setConsumerInputType(String consumerInputType) {
        this.consumerInputType = consumerInputType;
    }

    public Long getMultiples() {
        return multiples;
    }

    public void setMultiples(Long multiples) {
        this.multiples = multiples;
    }

    public Double getMinConsumerLength() {
        return minConsumerLength;
    }

    public void setMinConsumerLength(Double minConsumerLength) {
        this.minConsumerLength = minConsumerLength;
    }

    public Double getMaxConsumerLength() {
        return maxConsumerLength;
    }

    public void setMaxConsumerLength(Double maxConsumerLength) {
        this.maxConsumerLength = maxConsumerLength;
    }

    public Boolean getInquiryRequired() {
        return inquiryRequired;
    }

    public void setInquiryRequired(Boolean inquiryRequired) {
        this.inquiryRequired = inquiryRequired;
    }

    public Boolean getPartialPaymentAllowed() {
        return partialPaymentAllowed;
    }

    public void setPartialPaymentAllowed(Boolean partialPaymentAllowed) {
        this.partialPaymentAllowed = partialPaymentAllowed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProdDenom() {
        return prodDenom;
    }

    public void setProdDenom(String prodDenom) {
        this.prodDenom = prodDenom;
    }

    public Boolean getDenomFlag() {
        return denomFlag;
    }

    public void setDenomFlag(Boolean denomFlag) {
        this.denomFlag = denomFlag;
    }

    public String getDenomString() {
        return denomString;
    }

    public void setDenomString(String denomString) {
        this.denomString = denomString;
    }
}
