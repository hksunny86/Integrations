package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bkr on 7/21/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryVO implements Serializable{

    private String responseCode;
    private String responseDescription;
    private String name;
    private List<SubCategoryVO> subCategoryVOs;
    private List<BusinessNameVO> businessNameVOs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategoryVO> getSubCategoryVOs() {
        return subCategoryVOs;
    }

    public void setSubCategoryVOs(List<SubCategoryVO> subCategoryVOs) {
        this.subCategoryVOs = subCategoryVOs;
    }

    public List<BusinessNameVO> getBusinessNameVOs() {
        return businessNameVOs;
    }

    public void setBusinessNameVOs(List<BusinessNameVO> businessNameVOs) {
        this.businessNameVOs = businessNameVOs;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
