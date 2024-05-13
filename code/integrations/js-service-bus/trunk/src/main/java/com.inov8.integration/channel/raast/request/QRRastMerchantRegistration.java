package com.inov8.integration.channel.raast.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mcc",
        "uuid",
        "businessAddress",
        "businessName",
        "city",
        "cnicNumber",
        "cnicExpiry",
        "estimatedMonthlySales",
        "iban",
        "mobileNo",
        "name",
        "province",
        "typeOfBusiness"

})
public class QRRastMerchantRegistration extends Request {
    @JsonProperty("mcc")
    private String mcc;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("businessAddress")
    private String businessAddress;
    @JsonProperty("businessName")
    private String businessName;
    @JsonProperty("city")
    private String city;
    @JsonProperty("cniNumber")
    private String cniNumber;
    @JsonProperty("cnicExpiry")
    private String cnicExpiry;
    @JsonProperty("estimatedMonthlySales")

    private String estimatedMonthlySales;
    @JsonProperty("iban")

    private String iban;
    @JsonProperty("mobileNo")

    private String mobileNo;
    @JsonProperty("name")

    private String name;
    @JsonProperty("province")

    private String province;
    @JsonProperty("typeOfBusiness")

    private String typeOfBusiness;

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCniNumber() {
        return cniNumber;
    }

    public void setCniNumber(String cniNumber) {
        this.cniNumber = cniNumber;
    }

    public String getCnicExpiry() {
        return cnicExpiry;
    }

    public void setCnicExpiry(String cnicExpiry) {
        this.cnicExpiry = cnicExpiry;
    }

    public String getEstimatedMonthlySales() {
        return estimatedMonthlySales;
    }

    public void setEstimatedMonthlySales(String estimatedMonthlySales) {
        this.estimatedMonthlySales = estimatedMonthlySales;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMcc(i8SBSwitchControllerRequestVO.getMerchantCategoryCode());
        this.setUuid(i8SBSwitchControllerRequestVO.getRRN());
        this.setBusinessName(i8SBSwitchControllerRequestVO.getBusinessTitle());
        this.setBusinessAddress(i8SBSwitchControllerRequestVO.getAddress());
        this.setCity(i8SBSwitchControllerRequestVO.getCity());
        this.setCniNumber(i8SBSwitchControllerRequestVO.getCNIC());
        this.setCnicExpiry(i8SBSwitchControllerRequestVO.getCnicExpiry());
        this.setEstimatedMonthlySales(i8SBSwitchControllerRequestVO.getExpectedMonthlyTurnOver());
        this.setIban(i8SBSwitchControllerRequestVO.getIban());
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setName(i8SBSwitchControllerRequestVO.getName());
        this.setProvince(i8SBSwitchControllerRequestVO.getProvince());
        this.setTypeOfBusiness(i8SBSwitchControllerRequestVO.getTypeOfBusiness());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
