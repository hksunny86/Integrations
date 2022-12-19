package com.inov8.integration.channel.JSDebitCardApi.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import static com.inov8.integration.enums.DateFormatEnum.EXPIRY_DATE;
import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "traceNo",
        "dateTime",
        "merchantType",
        "messageType",
        "transactionCode",
        "transmissionDateTime",
        "stan",
        "userId",
        "password",
        "channelId",
        "relationshipNum",
        "custTypeCode",
        "branchCode",
        "segmentCode",
        "custStatusCode",
        "fullName",
        "genderCode",
        "nationalityCode",
        "langCode",
        "fullName_Other",
        "firstName",
        "middleName",
        "lastName",
        "surName",
        "suffix",
        "title",
        "companyName",
        "department",
        "businessTitle",
        "primaryEmail",
        "secondaryEmail",
        "motherMaidenName",
        "fatherName",
        "passportNumber",
        "customerNin",
        "taxNumber",
        "placeOfBirth",
        "dateOfBirth",
        "qualification",
        "profession",
        "religionCode",
        "maritalCode",
        "officeAddress1",
        "officeAddress2",
        "officeCity",
        "officeCountry",
        "officeZip_Code",
        "homeAddress1",
        "homeAddress2",
        "homeCity",
        "homeCountry",
        "homeZipCode",
        "tempAddress1",
        "tempAddress2",
        "tempCity",
        "tempCountry",
        "tempZipCode",
        "otherAddress1",
        "otherAddress2",
        "correspondenceFlag",
        "officePhone1",
        "officePhone2",
        "officeFaxNum",
        "residencePhone1",
        "residencePhone2",
        "residenceFax_Num",
        "mobilePhone",
        "limitProfile",
        "whenDeleted",
        "reserved1",
        "reserved2",
        "reserved3",
        "reserved4"
})
public class ImportCustomerRequest extends Request implements Serializable {

    private String username =  PropertyReader.getProperty("jsdebitcatrdapi.username");
    private String pass = PropertyReader.getProperty("jsdebitcatrdapi.password");

    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("traceNo")
    private String traceNo;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("messageType")
    private String messageType;
    @JsonProperty("custTypeCode")
    private String custTypeCode;
    @JsonProperty("fatherName")
    private String fatherName;
    @JsonProperty("fullName_Other ")
    private String fullNameOther;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("officeAddress1")
    private String officeAddress1;
    @JsonProperty("officePhone1")
    private String officePhone1;
    @JsonProperty("officeAddress2")
    private String officeAddress2;
    @JsonProperty("limitProfile")
    private String limitProfile;
    @JsonProperty("suffix")
    private String suffix;
    @JsonProperty("otherAddress1")
    private String otherAddress1;
    @JsonProperty("otherAddress2")
    private String otherAddress2;
    @JsonProperty("correspondenceFlag")
    private String correspondenceFlag;
    @JsonProperty("password")
    private String password;
    @JsonProperty("nationalityCode")
    private String nationalityCode;
    @JsonProperty("tempZipCode")
    private String tempZipCode;
    @JsonProperty("tempAddress1")
    private String tempAddress1;
    @JsonProperty("custStatusCode")
    private String custStatusCode;
    @JsonProperty("businessTitle")
    private String businessTitle;
    @JsonProperty("homeCity")
    private String homeCity;
    @JsonProperty("tempAddress2")
    private String tempAddress2;
    @JsonProperty("reserved3")
    private String reserved3;
    @JsonProperty("reserved2")
    private String reserved2;
    @JsonProperty("reserved1")
    private String reserved1;
    @JsonProperty("primaryEmail")
    private String primaryEmail;
    @JsonProperty("reserved4")
    private String reserved4;
    @JsonProperty("passportNumber")
    private String passportNumber;
    @JsonProperty("profession")
    private String profession;
    @JsonProperty("motherMaidenName")
    private String motherMaidenName;
    @JsonProperty("officeCity")
    private String officeCity;
    @JsonProperty("taxNumber")
    private String taxNumber;
    @JsonProperty("residencePhone1")
    private String residencePhone1;
    @JsonProperty("residencePhone2")
    private String residencePhone2;
    @JsonProperty("transactionCode")
    private String transactionCode;
    @JsonProperty("officeZip_Code")
    private String officeZipCode;
    @JsonProperty("branchCode")
    private String branchCode;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("qualification")
    private String qualification;
    @JsonProperty("genderCode")
    private String genderCode;
    @JsonProperty("tempCountry")
    private String tempCountry;
    @JsonProperty("mobilePhone")
    private String mobilePhone;
    @JsonProperty("customerNin")
    private String customerNin;
    @JsonProperty("officePhone2")
    private String officePhone2;
    @JsonProperty("religionCode")
    private String religionCode;
    @JsonProperty("homeCountry")
    private String homeCountry;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("secondaryEmail")
    private String secondaryEmail;
    @JsonProperty("langCode")
    private String langCode;
    @JsonProperty("officeCountry")
    private String officeCountry;
    @JsonProperty("title")
    private String title;
    @JsonProperty("transmissionDateTime")
    private String transmissionDateTime;
    @JsonProperty("department")
    private String department;
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("placeOfBirth")
    private String placeOfBirth;
    @JsonProperty("officeFaxNum")
    private String officeFaxNum;
    @JsonProperty("surName ")
    private String surName;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("segmentCode")
    private String segmentCode;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("homeZipCode")
    private String homeZipCode;
    @JsonProperty("maritalCode")
    private String maritalCode;
    @JsonProperty("homeAddress2")
    private String homeAddress2;
    @JsonProperty("homeAddress1")
    private String homeAddress1;
    @JsonProperty("relationshipNum")
    private String relationshipNum;
    @JsonProperty("tempCity")
    private String tempCity;
    @JsonProperty("whenDeleted")
    private String whenDeleted;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("residenceFax_Num")
    private String residenceFaxNum;

    public void setCustTypeCode(String custTypeCode) {
        this.custTypeCode = custTypeCode;
    }

    public String getCustTypeCode() {
        return custTypeCode;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFullNameOther(String fullNameOther) {
        this.fullNameOther = fullNameOther;
    }

    public String getFullNameOther() {
        return fullNameOther;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setOfficeAddress1(String officeAddress1) {
        this.officeAddress1 = officeAddress1;
    }

    public String getOfficeAddress1() {
        return officeAddress1;
    }

    public void setOfficePhone1(String officePhone1) {
        this.officePhone1 = officePhone1;
    }

    public String getOfficePhone1() {
        return officePhone1;
    }

    public void setOfficeAddress2(String officeAddress2) {
        this.officeAddress2 = officeAddress2;
    }

    public String getOfficeAddress2() {
        return officeAddress2;
    }

    public void setLimitProfile(String limitProfile) {
        this.limitProfile = limitProfile;
    }

    public String getLimitProfile() {
        return limitProfile;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setOtherAddress1(String otherAddress1) {
        this.otherAddress1 = otherAddress1;
    }

    public String getOtherAddress1() {
        return otherAddress1;
    }

    public void setOtherAddress2(String otherAddress2) {
        this.otherAddress2 = otherAddress2;
    }

    public String getOtherAddress2() {
        return otherAddress2;
    }

    public void setCorrespondenceFlag(String correspondenceFlag) {
        this.correspondenceFlag = correspondenceFlag;
    }

    public String getCorrespondenceFlag() {
        return correspondenceFlag;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setTempZipCode(String tempZipCode) {
        this.tempZipCode = tempZipCode;
    }

    public String getTempZipCode() {
        return tempZipCode;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setTempAddress1(String tempAddress1) {
        this.tempAddress1 = tempAddress1;
    }

    public String getTempAddress1() {
        return tempAddress1;
    }

    public void setCustStatusCode(String custStatusCode) {
        this.custStatusCode = custStatusCode;
    }

    public String getCustStatusCode() {
        return custStatusCode;
    }

    public void setBusinessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
    }

    public String getBusinessTitle() {
        return businessTitle;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setTempAddress2(String tempAddress2) {
        this.tempAddress2 = tempAddress2;
    }

    public String getTempAddress2() {
        return tempAddress2;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setResidencePhone1(String residencePhone1) {
        this.residencePhone1 = residencePhone1;
    }

    public String getResidencePhone1() {
        return residencePhone1;
    }

    public void setResidencePhone2(String residencePhone2) {
        this.residencePhone2 = residencePhone2;
    }

    public String getResidencePhone2() {
        return residencePhone2;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setOfficeZipCode(String officeZipCode) {
        this.officeZipCode = officeZipCode;
    }

    public String getOfficeZipCode() {
        return officeZipCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getQualification() {
        return qualification;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setTempCountry(String tempCountry) {
        this.tempCountry = tempCountry;
    }

    public String getTempCountry() {
        return tempCountry;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setCustomerNin(String customerNin) {
        this.customerNin = customerNin;
    }

    public String getCustomerNin() {
        return customerNin;
    }

    public void setOfficePhone2(String officePhone2) {
        this.officePhone2 = officePhone2;
    }

    public String getOfficePhone2() {
        return officePhone2;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getReligionCode() {
        return religionCode;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setOfficeCountry(String officeCountry) {
        this.officeCountry = officeCountry;
    }

    public String getOfficeCountry() {
        return officeCountry;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setOfficeFaxNum(String officeFaxNum) {
        this.officeFaxNum = officeFaxNum;
    }

    public String getOfficeFaxNum() {
        return officeFaxNum;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSurName() {
        return surName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setHomeZipCode(String homeZipCode) {
        this.homeZipCode = homeZipCode;
    }

    public String getHomeZipCode() {
        return homeZipCode;
    }

    public void setMaritalCode(String maritalCode) {
        this.maritalCode = maritalCode;
    }

    public String getMaritalCode() {
        return maritalCode;
    }

    public void setHomeAddress2(String homeAddress2) {
        this.homeAddress2 = homeAddress2;
    }

    public String getHomeAddress2() {
        return homeAddress2;
    }

    public void setHomeAddress1(String homeAddress1) {
        this.homeAddress1 = homeAddress1;
    }

    public String getHomeAddress1() {
        return homeAddress1;
    }

    public void setRelationshipNum(String relationshipNum) {
        this.relationshipNum = relationshipNum;
    }

    public String getRelationshipNum() {
        return relationshipNum;
    }

    public void setTempCity(String tempCity) {
        this.tempCity = tempCity;
    }

    public String getTempCity() {
        return tempCity;
    }

    public void setWhenDeleted(String whenDeleted) {
        this.whenDeleted = whenDeleted;
    }

    public String getWhenDeleted() {
        return whenDeleted;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getStan() {
        return stan;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setResidenceFaxNum(String residenceFaxNum) {
        this.residenceFaxNum = residenceFaxNum;
    }

    public String getResidenceFaxNum() {
        return residenceFaxNum;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("ImportCustomer");
        this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
        this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setMerchantType("0088");
        this.setMessageType("0200");
        this.setTransactionCode("401");
        this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setUserId(username);
        this.setPassword(pass);
        this.setChannelId("00");
        this.setRelationshipNum(i8SBSwitchControllerRequestVO.getRelationshipNumber());
        this.setCustTypeCode(i8SBSwitchControllerRequestVO.getCustTypeCode());
        this.setBranchCode(i8SBSwitchControllerRequestVO.getBranchCode());
        this.setSegmentCode(i8SBSwitchControllerRequestVO.getSegmentCode());
        this.setCustStatusCode(i8SBSwitchControllerRequestVO.getCustStatusCode());
        this.setFullName(i8SBSwitchControllerRequestVO.getFullName15());
        this.setGenderCode(i8SBSwitchControllerRequestVO.getGenderCode());
        this.setNationalityCode(i8SBSwitchControllerRequestVO.getNationalityCode());
        this.setLangCode(i8SBSwitchControllerRequestVO.getLangCode());
        this.setFullNameOther(i8SBSwitchControllerRequestVO.getFullNameOther());
        this.setFirstName(i8SBSwitchControllerRequestVO.getFirstName());
        this.setMiddleName(i8SBSwitchControllerRequestVO.getMiddleName());
        this.setLastName(i8SBSwitchControllerRequestVO.getLastName());
        this.setSurName(i8SBSwitchControllerRequestVO.getSurName());
        this.setSuffix(i8SBSwitchControllerRequestVO.getSuffix());
        this.setTitle(i8SBSwitchControllerRequestVO.getTitle());
        this.setCompanyName(i8SBSwitchControllerRequestVO.getCompanyName());
        this.setDepartment(i8SBSwitchControllerRequestVO.getDepartment());
        this.setBusinessTitle(i8SBSwitchControllerRequestVO.getBusinessTitle());
        this.setPrimaryEmail(i8SBSwitchControllerRequestVO.getPrimaryEmail());
        this.setSecondaryEmail(i8SBSwitchControllerRequestVO.getSecondaryEmail());
        this.setMotherMaidenName(i8SBSwitchControllerRequestVO.getMotherMaidenName());
        this.setFatherName(i8SBSwitchControllerRequestVO.getFatherName());
        this.setPassportNumber(i8SBSwitchControllerRequestVO.getPassportNumber());
        this.setCustomerNin(i8SBSwitchControllerRequestVO.getCustomerNin());
        this.setTaxNumber(i8SBSwitchControllerRequestVO.getTaxNumber());
        this.setPlaceOfBirth(i8SBSwitchControllerRequestVO.getPlaceOfBirth());
        this.setDateOfBirth(i8SBSwitchControllerRequestVO.getDateOfBirth());
        this.setQualification(i8SBSwitchControllerRequestVO.getQualification());
        this.setProfession(i8SBSwitchControllerRequestVO.getProfession());
        this.setReligionCode(i8SBSwitchControllerRequestVO.getReligionCode());
        this.setMaritalCode(i8SBSwitchControllerRequestVO.getMartialCode());
        this.setOfficeAddress1(i8SBSwitchControllerRequestVO.getOfficeAddress1());
        this.setOfficeAddress2(i8SBSwitchControllerRequestVO.getOfficeAddress2());
        this.setOfficeCity(i8SBSwitchControllerRequestVO.getOfficeCity());
        this.setOfficeZipCode(i8SBSwitchControllerRequestVO.getOfficeZipCode());
        this.setHomeAddress1(i8SBSwitchControllerRequestVO.getHomeAddress1());
        this.setHomeAddress2(i8SBSwitchControllerRequestVO.getHomeAddress2());
        this.setHomeCity(i8SBSwitchControllerRequestVO.getHomeCity());
        this.setHomeCountry(i8SBSwitchControllerRequestVO.getHomeCountry());
        this.setHomeZipCode(i8SBSwitchControllerRequestVO.getHomeZipCode());
        this.setTempAddress1(i8SBSwitchControllerRequestVO.getTempAddress1());
        this.setTempAddress2(i8SBSwitchControllerRequestVO.getTempAddress2());
        this.setTempCity(i8SBSwitchControllerRequestVO.getTempCity());
        this.setTempCountry(i8SBSwitchControllerRequestVO.getTempCity());
        this.setTempZipCode(i8SBSwitchControllerRequestVO.getTempZipCode());
        this.setOtherAddress1(i8SBSwitchControllerRequestVO.getOtherAddress1());
        this.setOtherAddress2(i8SBSwitchControllerRequestVO.getOtherAddress2());
        this.setCorrespondenceFlag(i8SBSwitchControllerRequestVO.getCorrespondenceFlag());
        this.setOfficePhone1(i8SBSwitchControllerRequestVO.getOfficePhone1());
        this.setOfficePhone2(i8SBSwitchControllerRequestVO.getOfficePhone2());
        this.setOfficeFaxNum(i8SBSwitchControllerRequestVO.getOfficeFaxNum());
        this.setResidencePhone1(i8SBSwitchControllerRequestVO.getResidencePhone1());
        this.setResidencePhone2(i8SBSwitchControllerRequestVO.getResidencePhone2());
        this.setResidenceFaxNum(i8SBSwitchControllerRequestVO.getResidenceFaxNum());
        this.setMobilePhone(i8SBSwitchControllerRequestVO.getMobilePhone());
        this.setLimitProfile(i8SBSwitchControllerRequestVO.getLimitProfile());
        this.setWhenDeleted(i8SBSwitchControllerRequestVO.getWhenDeleted());
        this.setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
        this.setReserved2(i8SBSwitchControllerRequestVO.getReserved2());
        this.setReserved3(i8SBSwitchControllerRequestVO.getReserved3());
        this.setReserved1(i8SBSwitchControllerRequestVO.getReserved4());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getProcessingCode())) {
            throw new I8SBValidationException("[Failed] Processing Code:" + this.getProcessingCode());
        }
        if (StringUtils.isEmpty(this.getTraceNo())) {
            throw new I8SBValidationException("[Failed] Trace No:" + this.getTraceNo());
        }
        if (StringUtils.isEmpty(this.getMerchantType())) {
            throw new I8SBValidationException("[Failed] Merchant Type:" + this.getMerchantType());
        }
        if (StringUtils.isEmpty(this.getMessageType())) {
            throw new I8SBValidationException("[Failed] Message Type:" + this.getMessageType());
        }
        if (StringUtils.isEmpty(this.getTransactionCode())) {
            throw new I8SBValidationException("[Failed] Transaction Code:" + this.getTransactionCode());
        }
        if (StringUtils.isEmpty(this.getTransmissionDateTime())) {
            throw new I8SBValidationException("[Failed] Transmission Date Time :" + this.getTransmissionDateTime());
        }
        if (StringUtils.isEmpty(this.getStan())) {
            throw new I8SBValidationException("[Failed] STAN :" + this.getStan());
        }
        if (StringUtils.isEmpty(this.getChannelId())) {
            throw new I8SBValidationException("[Failed] Channel ID:" + this.getChannelId());
        }

        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }

}