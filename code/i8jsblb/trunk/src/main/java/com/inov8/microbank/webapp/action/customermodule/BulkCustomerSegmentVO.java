package com.inov8.microbank.webapp.action.customermodule;

import com.inov8.microbank.common.model.CustomerModel;

import java.io.Serializable;
import java.util.List;

public class BulkCustomerSegmentVO implements Serializable {

    private CustomerModel customerModel;
    private List<String> errors;
    private Boolean validated;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }
}
