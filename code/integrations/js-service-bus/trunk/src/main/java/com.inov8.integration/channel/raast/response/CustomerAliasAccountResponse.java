package com.inov8.integration.channel.raast.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ResponseCode",
        "ResponseDescription",
        "IDs"
})
public class CustomerAliasAccountResponse extends Response implements Serializable {

    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("IDs")
    private IDs iDs;

    @JsonProperty("ResponseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("ResponseDescription")
    public String getResponseDescription() {
        return responseDescription;
    }

    @JsonProperty("ResponseDescription")
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @JsonProperty("IDs")
    public IDs getIDs() {
        return iDs;
    }

    @JsonProperty("IDs")
    public void setIDs(IDs iDs) {
        this.iDs = iDs;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (Objects.requireNonNull(this.getResponseCode()).equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
            com.inov8.integration.webservice.raastVO.IDs iDs = new com.inov8.integration.webservice.raastVO.IDs();
            iDs.setAccountID(this.getIDs().getAccountID());
            iDs.setAliasID(this.getIDs().getAliasID());
            iDs.setCustomerID(this.getIDs().getCustomerID());
            i8SBSwitchControllerResponseVO.setiDs(iDs);
            i8SBSwitchControllerResponseVO.setType(this.getIDs().getType());
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        }


        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CustomerID",
        "AliasID",
        "AccountID",
        "Type",
})
class IDs {

    @JsonProperty("CustomerID")
    private String customerID;
    @JsonProperty("AliasID")
    private String aliasID;
    @JsonProperty("AccountID")
    private String accountID;
    @JsonProperty("Type")
    private String type;

    @JsonProperty("CustomerID")
    public String getCustomerID() {
        return customerID;
    }

    @JsonProperty("CustomerID")
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @JsonProperty("AliasID")
    public String getAliasID() {
        return aliasID;
    }

    @JsonProperty("AliasID")
    public void setAliasID(String aliasID) {
        this.aliasID = aliasID;
    }

    @JsonProperty("AccountID")
    public String getAccountID() {
        return accountID;
    }

    @JsonProperty("AccountID")
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }
}