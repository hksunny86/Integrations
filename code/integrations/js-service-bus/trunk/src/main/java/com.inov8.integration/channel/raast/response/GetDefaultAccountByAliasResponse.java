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
        "id",
        "type",
        "currency",
        "servicer",
        "name",
        "surname",
        "nickName",
        "isDefault",
        "Response"
})
public class GetDefaultAccountByAliasResponse extends Response implements Serializable {

    @JsonProperty("id")
    private Id id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("servicer")
    private Servicer servicer;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("nickName")
    private String nickName;
    @JsonProperty("isDefault")
    private Boolean isDefault;
    @JsonProperty("Response")
    private AliasResponse response;
    private String responseCode;

    @JsonProperty("id")
    public Id getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Id id) {
        this.id = id;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("servicer")
    public Servicer getServicer() {
        return servicer;
    }

    @JsonProperty("servicer")
    public void setServicer(Servicer servicer) {
        this.servicer = servicer;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("nickName")
    public String getNickName() {
        return nickName;
    }

    @JsonProperty("nickName")
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @JsonProperty("isDefault")
    public Boolean getIsDefault() {
        return isDefault;
    }

    @JsonProperty("isDefault")
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonProperty("Response")
    public AliasResponse getResponse() {
        return response;
    }

    @JsonProperty("Response")
    public void setResponse(AliasResponse response) {
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
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponse().getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(String.valueOf(this.getResponse().getResponseDescription()));
            i8SBSwitchControllerResponseVO.setIBAN(this.getId().getIban());
            i8SBSwitchControllerResponseVO.setType(this.getType());
            i8SBSwitchControllerResponseVO.setCurrency(this.getCurrency());
            i8SBSwitchControllerResponseVO.setMemberId(this.getServicer().getMemberId());
            i8SBSwitchControllerResponseVO.setSurName(this.getSurname());
            i8SBSwitchControllerResponseVO.setNickName(this.getNickName());
            i8SBSwitchControllerResponseVO.setDefault(this.getIsDefault());
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
        "iban"
})
class Id {

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
        "responseCode",
        "responseDescription"
})
class AliasResponse {

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
        "memberId"
})
class Servicer {

    @JsonProperty("memberId")
    private String memberId;

    @JsonProperty("memberId")
    public String getMemberId() {
        return memberId;
    }

    @JsonProperty("memberId")
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

}