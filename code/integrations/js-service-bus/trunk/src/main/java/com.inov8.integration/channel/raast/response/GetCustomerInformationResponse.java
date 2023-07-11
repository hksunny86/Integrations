package com.inov8.integration.channel.raast.response;

;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uid",
        "documentType",
        "documentNumber",
        "documentValidityDate",
        "name",
        "address",
        "additionalDetails",
        "recordId",
        "status",
        "_links",
        "Response"
})
public class GetCustomerInformationResponse extends Response implements Serializable {

    @JsonProperty("uid")
    private Uid uid;
    @JsonProperty("documentType")
    private String documentType;
    @JsonProperty("documentNumber")
    private String documentNumber;
    @JsonProperty("documentValidityDate")
    private String documentValidityDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("additionalDetails")
    private AdditionalDetails additionalDetails;
    @JsonProperty("recordId")
    private String recordId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("_links")
    private Links links;
    @JsonProperty("Response")
    private CustomerInfoResponse response;
    private String responseCode;

    @JsonProperty("uid")
    public Uid getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(Uid uid) {
        this.uid = uid;
    }

    @JsonProperty("documentType")
    public String getDocumentType() {
        return documentType;
    }

    @JsonProperty("documentType")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @JsonProperty("documentNumber")
    public String getDocumentNumber() {
        return documentNumber;
    }

    @JsonProperty("documentNumber")
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @JsonProperty("documentValidityDate")
    public String getDocumentValidityDate() {
        return documentValidityDate;
    }

    @JsonProperty("documentValidityDate")
    public void setDocumentValidityDate(String documentValidityDate) {
        this.documentValidityDate = documentValidityDate;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonProperty("additionalDetails")
    public AdditionalDetails getAdditionalDetails() {
        return additionalDetails;
    }

    @JsonProperty("additionalDetails")
    public void setAdditionalDetails(AdditionalDetails additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    @JsonProperty("recordId")
    public String getRecordId() {
        return recordId;
    }

    @JsonProperty("recordId")
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonProperty("Response")
    public CustomerInfoResponse getResponse() {
        return response;
    }

    @JsonProperty("Response")
    public void setResponse(CustomerInfoResponse response) {
        this.response = response;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (Objects.requireNonNull(this.getResponseCode()).equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setName(this.getName());
            i8SBSwitchControllerResponseVO.setDocumentType(this.getDocumentType());
            i8SBSwitchControllerResponseVO.setDocumentNumber(this.getDocumentNumber());
            i8SBSwitchControllerResponseVO.setCustomerStatus(this.getStatus());
        } else {
            if (this.getResponse() != null) {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponse().getResponseCode());
                i8SBSwitchControllerResponseVO.setDescription(String.valueOf(Objects.requireNonNull(this.getResponse().getResponseDescription())));
            }
        }


        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class Accounts {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class AddAccount {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class AddAlias {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dba",
        "mcc"
})
class AdditionalDetails {

    @JsonProperty("dba")
    private String dba;
    @JsonProperty("mcc")
    private String mcc;

    @JsonProperty("dba")
    public String getDba() {
        return dba;
    }

    @JsonProperty("dba")
    public void setDba(String dba) {
        this.dba = dba;
    }

    @JsonProperty("mcc")
    public String getMcc() {
        return mcc;
    }

    @JsonProperty("mcc")
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "country",
        "city",
        "stateProvinceRegion",
        "address"
})
class Address {

    @JsonProperty("country")
    private String country;
    @JsonProperty("city")
    private String city;
    @JsonProperty("stateProvinceRegion")
    private String stateProvinceRegion;
    @JsonProperty("address")
    private String address;

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("stateProvinceRegion")
    public String getStateProvinceRegion() {
        return stateProvinceRegion;
    }

    @JsonProperty("stateProvinceRegion")
    public void setStateProvinceRegion(String stateProvinceRegion) {
        this.stateProvinceRegion = stateProvinceRegion;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class Aliases {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class Delete {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self",
        "suspend",
        "update",
        "delete",
        "accounts",
        "aliases",
        "addAccount",
        "addAlias"
})
class Links {

    @JsonProperty("self")
    private Self self;
    @JsonProperty("suspend")
    private Suspend suspend;
    @JsonProperty("update")
    private Update update;
    @JsonProperty("delete")
    private Delete delete;
    @JsonProperty("accounts")
    private Accounts accounts;
    @JsonProperty("aliases")
    private Aliases aliases;
    @JsonProperty("addAccount")
    private AddAccount addAccount;
    @JsonProperty("addAlias")
    private AddAlias addAlias;

    @JsonProperty("self")
    public Self getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(Self self) {
        this.self = self;
    }

    @JsonProperty("suspend")
    public Suspend getSuspend() {
        return suspend;
    }

    @JsonProperty("suspend")
    public void setSuspend(Suspend suspend) {
        this.suspend = suspend;
    }

    @JsonProperty("update")
    public Update getUpdate() {
        return update;
    }

    @JsonProperty("update")
    public void setUpdate(Update update) {
        this.update = update;
    }

    @JsonProperty("delete")
    public Delete getDelete() {
        return delete;
    }

    @JsonProperty("delete")
    public void setDelete(Delete delete) {
        this.delete = delete;
    }

    @JsonProperty("accounts")
    public Accounts getAccounts() {
        return accounts;
    }

    @JsonProperty("accounts")
    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    @JsonProperty("aliases")
    public Aliases getAliases() {
        return aliases;
    }

    @JsonProperty("aliases")
    public void setAliases(Aliases aliases) {
        this.aliases = aliases;
    }

    @JsonProperty("addAccount")
    public AddAccount getAddAccount() {
        return addAccount;
    }

    @JsonProperty("addAccount")
    public void setAddAccount(AddAccount addAccount) {
        this.addAccount = addAccount;
    }

    @JsonProperty("addAlias")
    public AddAlias getAddAlias() {
        return addAlias;
    }

    @JsonProperty("addAlias")
    public void setAddAlias(AddAlias addAlias) {
        this.addAlias = addAlias;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "responseDescription"
})
class CustomerInfoResponse {

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private List<String> responseDescription;

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
        "href"
})
class Self {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class Suspend {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "value"
})
class Uid {

    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
class Update {

    @JsonProperty("href")
    private String href;

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

}