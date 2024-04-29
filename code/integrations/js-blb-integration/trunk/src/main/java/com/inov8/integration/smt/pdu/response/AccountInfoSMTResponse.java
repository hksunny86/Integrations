package com.inov8.integration.smt.pdu.response;

// @Created On 4/17/2024 : Wednesday
// @Created By muhammad.aqeel


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "AccountTitle",
        "AccountNumber",
        "RegistrationTypeCode",
        "AccountLevelCode",
        "AccountStatusCode",
        "AccountTypeCode",
        "AccountNatureCode",
        "CurrencyCode",
        "Cnic",
        "Name",
        "Segment",
        "Email",
        "Channel",
        "Gender",
        "Address",
        "City",
        "Country",
        "ProvinceState",
        "GlCodeCombination",
        "DateOfBirth",
        "HashData",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfoSMTResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("AccountTitle")
    private String accountTitle;
    @JsonProperty("AccountNumber")
    private String accountNumber;
    @JsonProperty("RegistrationTypeCode")
    private String registrationTypeCode;
    @JsonProperty("AccountLevelCode")
    private String accountLevelCode;
    @JsonProperty("AccountStatusCode")
    private String accountStatusCode;
    @JsonProperty("AccountTypeCode")
    private String accountTypeCode;
    @JsonProperty("AccountNatureCode")
    private String accountNatureCode;
    @JsonProperty("CurrencyCode")
    private String currencyCode;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Segment")
    private String segment;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Channel")
    private String channel;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("City")
    private String city;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("ProvinceState")
    private String provinceState;
    @JsonProperty("GlCodeCombination")
    private String glCodeCombination;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("HashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRegistrationTypeCode() {
        return registrationTypeCode;
    }

    public void setRegistrationTypeCode(String registrationTypeCode) {
        this.registrationTypeCode = registrationTypeCode;
    }

    public String getAccountLevelCode() {
        return accountLevelCode;
    }

    public void setAccountLevelCode(String accountLevelCode) {
        this.accountLevelCode = accountLevelCode;
    }

    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountNatureCode() {
        return accountNatureCode;
    }

    public void setAccountNatureCode(String accountNatureCode) {
        this.accountNatureCode = accountNatureCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public String getGlCodeCombination() {
        return glCodeCombination;
    }

    public void setGlCodeCombination(String glCodeCombination) {
        this.glCodeCombination = glCodeCombination;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}