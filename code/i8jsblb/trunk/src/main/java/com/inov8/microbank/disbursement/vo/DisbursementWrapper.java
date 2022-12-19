package com.inov8.microbank.disbursement.vo;

import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtieqRe on 2/19/2017.
 */
public class DisbursementWrapper implements Serializable {
    private Long disbursementFileInfoId;
    private String batchNumber;
    private String sourceAccount;

    private Boolean isCoreSourceAccount;
    private ServiceModel serviceModel;
    private ProductModel productModel;

    private Double amount = 0d;
    private Double charges = 0d;
    private Long serviceId;
    private String serviceName;
    private Long productId;
    private String productName;
    private List<DisbursementVO> disbursementVOList = new ArrayList<>(0);

    public DisbursementWrapper(DisbursementVO vo) {
        this.disbursementFileInfoId = vo.getDisbursementsFileInfoId();
        this.batchNumber = vo.getBatchNumber();
        this.sourceAccount = vo.getSourceACNo();

        this.serviceId = vo.getServiceId();
        this.serviceName = vo.getServiceName();
        this.productId = vo.getProductId();
        this.productName = vo.getProductName();

        serviceModel = new ServiceModel(serviceId, serviceName);
        productModel = new ProductModel(productId, productName);

        productModel.setServiceIdServiceModel(serviceModel);

        this.add(vo);
    }

    public void add(DisbursementVO vo) {
        this.amount += vo.getAmount();
        this.charges += vo.getCharges();
        disbursementVOList.add(vo);
    }

    public Long getDisbursementFileInfoId() {
        return disbursementFileInfoId;
    }

    public void setDisbursementFileInfoId(Long disbursementFileInfoId) {
        this.disbursementFileInfoId = disbursementFileInfoId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Boolean getCoreSourceAccount() {
        return isCoreSourceAccount;
    }

    public void setCoreSourceAccount(Boolean coreSourceAccount) {
        isCoreSourceAccount = coreSourceAccount;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public List<DisbursementVO> getDisbursementVOList() {
        return disbursementVOList;
    }

    public void setDisbursementVOList(List<DisbursementVO> disbursementVOList) {
        this.disbursementVOList = disbursementVOList;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }
}
