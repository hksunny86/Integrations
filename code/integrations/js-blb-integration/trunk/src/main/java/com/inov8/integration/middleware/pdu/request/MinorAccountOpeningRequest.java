package com.inov8.integration.middleware.pdu.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MinorAccountOpeningRequest {

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("RRN")
    private String rrn;
    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("AccountTilte")
    private String name;
    @JsonProperty("Cnic")
    private String nic;
    @JsonProperty("IssuanceDate")
    private String issuanceDate;
    @JsonProperty("MobileNumber")
    private String mobileNumber;
    @JsonProperty("MotherMedianName")
    private String motherMedianName;
    @JsonProperty("FatherName")
    private String FatherName;
    @JsonProperty("PlaceOfbirth")
    private String placeOfbirth;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("Address")
    private String Address;
    @JsonProperty("NicExpiry")
    private String nicExpiry;
    @JsonProperty("parentCnicPic")
    private String parentCnicPic;
    @JsonProperty("SnicPic")
    private String SnicPic;
    @JsonProperty("minorCutomerPic")
    private String minorCutomerPic;
    @JsonProperty("fatherMotherMobileNumber")
    private String fatherMotherMobileNumber;
    @JsonProperty("fatherCnic")
    private String fatherCnic;
    @JsonProperty("FatherCnicIssuanceDate")
    private String fatherCnicIssuanceDate;
    @JsonProperty("FatherCnicExpiryDate")
    private String fatherCnicExpiryDate;
    @JsonProperty("motherCnic")
    private String motherCnic;
    @JsonProperty("BFormPic")
    private String BFormPic;
    @JsonProperty("email")
    private String email;
    @JsonProperty("ChannelId")
    private String channelId;
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("Reserved3")
    private String reserved3;
    @JsonProperty("Reserved4")
    private String reserved4;
    @JsonProperty("Reserved5")
    private String reserved5;
    @JsonProperty("Reserved6")
    private String reserved6;
    @JsonProperty("Reserved7")
    private String reserved7;
    @JsonProperty("Reserved8")
    private String reserved8;
    @JsonProperty("Reserved9")
    private String reserved9;
    @JsonProperty("Reserved10")
    private String reserved10;
    @JsonProperty("HashData")
    private String hashData;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMotherMedianName() {
        return motherMedianName;
    }

    public void setMotherMedianName(String motherMedianName) {
        this.motherMedianName = motherMedianName;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getPlaceOfbirth() {
        return placeOfbirth;
    }

    public void setPlaceOfbirth(String placeOfbirth) {
        this.placeOfbirth = placeOfbirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNicExpiry() {
        return nicExpiry;
    }

    public void setNicExpiry(String nicExpiry) {
        this.nicExpiry = nicExpiry;
    }

    public String getParentCnicPic() {
        return parentCnicPic;
    }

    public void setParentCnicPic(String parentCnicPic) {
        this.parentCnicPic = parentCnicPic;
    }

    public String getSnicPic() {
        return SnicPic;
    }

    public void setSnicPic(String snicPic) {
        SnicPic = snicPic;
    }

    public String getMinorCutomerPic() {
        return minorCutomerPic;
    }

    public void setMinorCutomerPic(String minorCutomerPic) {
        this.minorCutomerPic = minorCutomerPic;
    }

    public String getFatherMotherMobileNumber() {
        return fatherMotherMobileNumber;
    }

    public void setFatherMotherMobileNumber(String fatherMotherMobileNumber) {
        this.fatherMotherMobileNumber = fatherMotherMobileNumber;
    }

    public String getFatherCnic() {
        return fatherCnic;
    }

    public void setFatherCnic(String fatherCnic) {
        this.fatherCnic = fatherCnic;
    }

    public String getMotherCnic() {
        return motherCnic;
    }

    public void setMotherCnic(String motherCnic) {
        this.motherCnic = motherCnic;
    }

    public String getBFormPic() {
        return BFormPic;
    }

    public void setBFormPic(String BFormPic) {
        this.BFormPic = BFormPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public String getReserved6() {
        return reserved6;
    }

    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }

    public String getReserved7() {
        return reserved7;
    }

    public void setReserved7(String reserved7) {
        this.reserved7 = reserved7;
    }

    public String getReserved8() {
        return reserved8;
    }

    public void setReserved8(String reserved8) {
        this.reserved8 = reserved8;
    }

    public String getReserved9() {
        return reserved9;
    }

    public void setReserved9(String reserved9) {
        this.reserved9 = reserved9;
    }

    public String getReserved10() {
        return reserved10;
    }

    public void setReserved10(String reserved10) {
        this.reserved10 = reserved10;
    }

    public String getFatherCnicIssuanceDate() {
        return fatherCnicIssuanceDate;
    }

    public void setFatherCnicIssuanceDate(String fatherCnicIssuanceDate) {
        this.fatherCnicIssuanceDate = fatherCnicIssuanceDate;
    }

    public String getFatherCnicExpiryDate() {
        return fatherCnicExpiryDate;
    }

    public void setFatherCnicExpiryDate(String fatherCnicExpiryDate) {
        this.fatherCnicExpiryDate = fatherCnicExpiryDate;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
