package com.inov8.integration.channel.raast.response;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "iban"
})
class AccountId {

    @JsonProperty("iban")
    private String iban;

    @JsonProperty("iban")
    public String getIban() {
        return iban;
    }

    @JsonProperty("iban")
    public void setIban(String iban) {
        this.iban = iban;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "currency",
        "servicer",
        "openingDate",
        "closingDate",
        "id",
        "recordId",
        "_links",
        "responseCode",
        "responseDescription"
})
class AccountsDatum {

    @JsonProperty("type")
    private Object type;
    @JsonProperty("currency")
    private Object currency;
    @JsonProperty("servicer")
    private Object servicer;
    @JsonProperty("openingDate")
    private Object openingDate;
    @JsonProperty("closingDate")
    private Object closingDate;
    @JsonProperty("id")
    private AccountId id;
    @JsonProperty("recordId")
    private Object recordId;
    @JsonProperty("_links")
    private Object links;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private List<String> responseDescription;

    @JsonProperty("type")
    public Object getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(Object type) {
        this.type = type;
    }

    @JsonProperty("currency")
    public Object getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    @JsonProperty("servicer")
    public Object getServicer() {
        return servicer;
    }

    @JsonProperty("servicer")
    public void setServicer(Object servicer) {
        this.servicer = servicer;
    }

    @JsonProperty("openingDate")
    public Object getOpeningDate() {
        return openingDate;
    }

    @JsonProperty("openingDate")
    public void setOpeningDate(Object openingDate) {
        this.openingDate = openingDate;
    }

    @JsonProperty("closingDate")
    public Object getClosingDate() {
        return closingDate;
    }

    @JsonProperty("closingDate")
    public void setClosingDate(Object closingDate) {
        this.closingDate = closingDate;
    }

    @JsonProperty("id")
    public AccountId getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(AccountId id) {
        this.id = id;
    }

    @JsonProperty("recordId")
    public Object getRecordId() {
        return recordId;
    }

    @JsonProperty("recordId")
    public void setRecordId(Object recordId) {
        this.recordId = recordId;
    }

    @JsonProperty("_links")
    public Object getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Object links) {
        this.links = links;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("responseDescription")
    public List<String> getResponseDescription() {
        return responseDescription;
    }

    @JsonProperty("responseDescription")
    public void setResponseDescription(List<String> responseDescription) {
        this.responseDescription = responseDescription;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Data"
})
public class GetCustomerAccountsResponse extends Response implements Serializable {

    @JsonProperty("Data")
    private List<AccountsDatum> data;
    private String responseCode;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("Data")
    public List<AccountsDatum> getData() {
        return data;
    }

    @JsonProperty("Data")
    public void setData(List<AccountsDatum> data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getData() != null) {
            if (Objects.requireNonNull(this.getResponseCode()).equals("200")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
                i8SBSwitchControllerResponseVO.setAccountType(String.valueOf(this.data.get(0).getType()));
                i8SBSwitchControllerResponseVO.setCurrency(String.valueOf(this.data.get(0).getCurrency()));
                i8SBSwitchControllerResponseVO.setIBAN(String.valueOf(this.data.get(0).getId().getIban()));
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
                if (this.getData().get(1).getResponseDescription() != null) {
                    i8SBSwitchControllerResponseVO.setDescription(String.valueOf(Objects.requireNonNull(this.data.get(1).getResponseDescription())));
                } else {
                    i8SBSwitchControllerResponseVO.setDescription("Fail");

                }
            }
        }


        return i8SBSwitchControllerResponseVO;
    }

}