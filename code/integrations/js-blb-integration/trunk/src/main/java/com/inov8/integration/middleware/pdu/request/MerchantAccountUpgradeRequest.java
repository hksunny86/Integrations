package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "UserName",
        "Password",
        "MobileNo",
        "DateTime",
        "Rrn",
        "ChannelId",
        "TerminalId",
        "Name",
        "CnicNumber",
        "BusinessName",
        "BusinessAddress",
        "City",
        "EstimatedMonthlySales",
        "TypeOfBusiness",
        "ProfilePic",
        "CNICPic",
        "longitude",
        "latitude",
        "IDType",
        "ID-N",
        "TillID",
        "Reserved1",
        "Reserved2",
        "Reserved3",
        "Reserved4",
        "Reserved5",
        "Reserved6",
        "Reserved7",
        "Reserved8",
        "Reserved9",
        "Reserved10",
        "HashData"
})
public class MerchantAccountUpgradeRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("MobileNo")
    private String mobileNo;
    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ChannelId")
    private String channelId;
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("CnicNumber")
    private String cnicNumber;
    @JsonProperty("BusinessName")
    private String businessName;
    @JsonProperty("BusinessAddress")
    private String businessAddress;
    @JsonProperty("City")
    private String city;
    @JsonProperty("EstimatedMonthlySales")
    private String estimatedMonthlySales;
    @JsonProperty("TypeOfBusiness")
    private String typeOfBusiness;
    @JsonProperty("ProfilePic")
    private String profilePic;
    @JsonProperty("CNICPic")
    private String cNICPic;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("IDType")
    private String iDType;
    @JsonProperty("ID-N")
    private String idN;
    @JsonProperty("TillID")
    private String tillID;
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

    @JsonProperty("UserName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("UserName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("MobileNo")
    public String getMobileNo() {
        return mobileNo;
    }

    @JsonProperty("MobileNo")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @JsonProperty("DateTime")
    public String getDateTime() {
        return dateTime;
    }

    @JsonProperty("DateTime")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @JsonProperty("Rrn")
    public String getRrn() {
        return rrn;
    }

    @JsonProperty("Rrn")
    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @JsonProperty("ChannelId")
    public String getChannelId() {
        return channelId;
    }

    @JsonProperty("ChannelId")
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @JsonProperty("TerminalId")
    public String getTerminalId() {
        return terminalId;
    }

    @JsonProperty("TerminalId")
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("CnicNumber")
    public String getCnicNumber() {
        return cnicNumber;
    }

    @JsonProperty("CnicNumber")
    public void setCnicNumber(String cnicNumber) {
        this.cnicNumber = cnicNumber;
    }

    @JsonProperty("BusinessName")
    public String getBusinessName() {
        return businessName;
    }

    @JsonProperty("BusinessName")
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @JsonProperty("BusinessAddress")
    public String getBusinessAddress() {
        return businessAddress;
    }

    @JsonProperty("BusinessAddress")
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("EstimatedMonthlySales")
    public String getEstimatedMonthlySales() {
        return estimatedMonthlySales;
    }

    @JsonProperty("EstimatedMonthlySales")
    public void setEstimatedMonthlySales(String estimatedMonthlySales) {
        this.estimatedMonthlySales = estimatedMonthlySales;
    }

    @JsonProperty("TypeOfBusiness")
    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    @JsonProperty("TypeOfBusiness")
    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    @JsonProperty("ProfilePic")
    public String getProfilePic() {
        return profilePic;
    }

    @JsonProperty("ProfilePic")
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @JsonProperty("CNICPic")
    public String getCNICPic() {
        return cNICPic;
    }

    @JsonProperty("CNICPic")
    public void setCNICPic(String cNICPic) {
        this.cNICPic = cNICPic;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("IDType")
    public String getIDType() {
        return iDType;
    }

    @JsonProperty("IDType")
    public void setIDType(String iDType) {
        this.iDType = iDType;
    }

    @JsonProperty("ID-N")
    public String getIdN() {
        return idN;
    }

    @JsonProperty("ID-N")
    public void setIdN(String idN) {
        this.idN = idN;
    }

    @JsonProperty("TillID")
    public String getTillID() {
        return tillID;
    }

    @JsonProperty("TillID")
    public void setTillID(String tillID) {
        this.tillID = tillID;
    }

    @JsonProperty("Reserved1")
    public String getReserved1() {
        return reserved1;
    }

    @JsonProperty("Reserved1")
    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    @JsonProperty("Reserved2")
    public String getReserved2() {
        return reserved2;
    }

    @JsonProperty("Reserved2")
    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    @JsonProperty("Reserved3")
    public String getReserved3() {
        return reserved3;
    }

    @JsonProperty("Reserved3")
    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    @JsonProperty("Reserved4")
    public String getReserved4() {
        return reserved4;
    }

    @JsonProperty("Reserved4")
    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    @JsonProperty("Reserved5")
    public String getReserved5() {
        return reserved5;
    }

    @JsonProperty("Reserved5")
    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    @JsonProperty("Reserved6")
    public String getReserved6() {
        return reserved6;
    }

    @JsonProperty("Reserved6")
    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }

    @JsonProperty("Reserved7")
    public String getReserved7() {
        return reserved7;
    }

    @JsonProperty("Reserved7")
    public void setReserved7(String reserved7) {
        this.reserved7 = reserved7;
    }

    @JsonProperty("Reserved8")
    public String getReserved8() {
        return reserved8;
    }

    @JsonProperty("Reserved8")
    public void setReserved8(String reserved8) {
        this.reserved8 = reserved8;
    }

    @JsonProperty("Reserved9")
    public String getReserved9() {
        return reserved9;
    }

    @JsonProperty("Reserved9")
    public void setReserved9(String reserved9) {
        this.reserved9 = reserved9;
    }

    @JsonProperty("Reserved10")
    public String getReserved10() {
        return reserved10;
    }

    @JsonProperty("Reserved10")
    public void setReserved10(String reserved10) {
        this.reserved10 = reserved10;
    }

    @JsonProperty("HashData")
    public String getHashData() {
        return hashData;
    }

    @JsonProperty("HashData")
    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

}