package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bkr on 7/21/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCategoryVO implements Serializable{

    private String name;
    private List<BusinessNameVO> businessNameVOs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BusinessNameVO> getBusinessNameVOs() {
        return businessNameVOs;
    }

    public void setBusinessNameVOs(List<BusinessNameVO> businessNameVOs) {
        this.businessNameVOs = businessNameVOs;
    }
}
