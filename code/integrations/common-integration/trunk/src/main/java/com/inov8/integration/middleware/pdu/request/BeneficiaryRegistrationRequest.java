package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.middleware.pdu.BeneficiaryRegistration;

import java.io.Serializable;
import java.util.List;

/**
 * Created by inov8 on 10/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BeneficiaryRegistrationRequest implements Serializable {

    private static final long serialVersionUID = 2134956891427267824L;

    @JsonProperty("Security_Key")
    private String secretKey;

    @JsonProperty("Beneficiary_Registration")
    private List<BeneficiaryRegistration> beneficiaryRegistrationList;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<BeneficiaryRegistration> getBeneficiaryRegistrationList() {
        return beneficiaryRegistrationList;
    }

    public void setBeneficiaryRegistrationList(List<BeneficiaryRegistration> beneficiaryRegistrationList) {
        this.beneficiaryRegistrationList = beneficiaryRegistrationList;
    }
}

