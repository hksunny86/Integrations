package com.inov8.integration.webservice.raastVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "till",
        "qrText"

})
@JsonIgnoreProperties (ignoreUnknown = true)
public class QrRegistration implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("till")
    private String till;
    @JsonProperty("qrText")
    private String qrText;

    public String getTill() {
        return till;
    }

    public void setTill(String till) {
        this.till = till;
    }

    public String getQrText() {
        return qrText;
    }

    public void setQrText(String qrText) {
        this.qrText = qrText;
    }
}