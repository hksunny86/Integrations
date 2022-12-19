package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

@XStreamAlias("CustomerDebit")
public class CustomerVO implements Serializable
{
    private String relationshipNo;
    private String customerTypeCode;
    private String branchCode;
    private String segmantCode;
    private String customerStatusCode;
    private String fullName;
    private String genderCode;
    private String nationalityCode;
    private String langCode;
    private String fullNameOther;
    private String firstName;
    private String lastName;
    private String middleName;
    private String surName;
    private String suffix;
    private String titleName;
    private String companyName;
    private String departmentName;
    private String businessTitle;
    private String primaryEmail;
    private String secondaryEmail;
    private String motherMaidenName;
    private String fatherName;
    private String passportNo;
    private String customerCNIC;
    private String customerTaxNo;
    private String  placeOfBirth;
    private String dateOfBirth;
    private String qualification;
    private String profession;
    private String religonCode;
    private String martialCode;
    private String officeAddress1;
    private String officeAddress2;
    private String officeCity;
    private String officeCountry;
    private String officeZipCode;
    private String homeAddress1;
    private String homeAddress2;
    private String homeCity;
    private String homeCountry;
    private String homeZipCode;
    private String tempAddress1;
    private String tempAddress2;
    private String tempCity;
    private String tempCountry;
    private String tempZipCode;
    private String otherAddress1;
    private String otherAddress2;
    private String CorrespondenceFlag;
    private String officePhone1;
    private String officePhone2;
    private String officeFaxNo;
    private String residencePhone1;
    private String residencePhone2;
    private String residenceFaxNo;
    private String mobilePhone;
    private String limitProfile;
    private DateTime deletedOn;

    private String reserverd1;
    private String reserved2;
    private String reserved3;
    private String reserverd4;

    private String cnicExpiry;
    private String mailingAddress;

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getCnicExpiry() {
        return cnicExpiry;
    }

    public void setCnicExpiry(String cnicExpiry) {
        this.cnicExpiry = cnicExpiry;
    }

    public String getRelationshipNo() {
        return relationshipNo;
    }

    public void setRelationshipNo(String relationshipNo) {
        this.relationshipNo = relationshipNo;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getSegmantCode() {
        return segmantCode;
    }

    public void setSegmantCode(String segmantCode) {
        this.segmantCode = segmantCode;
    }

    public String getCustomerStatusCode() {
        return customerStatusCode;
    }

    public void setCustomerStatusCode(String customerStatusCode) {
        this.customerStatusCode = customerStatusCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getFullNameOther() {
        return fullNameOther;
    }

    public void setFullNameOther(String fullNameOther) {
        this.fullNameOther = fullNameOther;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBusinessTitle() {
        return businessTitle;
    }

    public void setBusinessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getCustomerCNIC() {
        return customerCNIC;
    }

    public void setCustomerCNIC(String customerCNIC) {
        this.customerCNIC = customerCNIC;
    }

    public String getCustomerTaxNo() {
        return customerTaxNo;
    }

    public void setCustomerTaxNo(String customerTaxNo) {
        this.customerTaxNo = customerTaxNo;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getReligonCode() {
        return religonCode;
    }

    public void setReligonCode(String religonCode) {
        this.religonCode = religonCode;
    }

    public String getMartialCode() {
        return martialCode;
    }

    public void setMartialCode(String martialCode) {
        this.martialCode = martialCode;
    }

    public String getOfficeAddress1() {
        return officeAddress1;
    }

    public void setOfficeAddress1(String officeAddress1) {
        this.officeAddress1 = officeAddress1;
    }

    public String getOfficeAddress2() {
        return officeAddress2;
    }

    public void setOfficeAddress2(String officeAddress2) {
        this.officeAddress2 = officeAddress2;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeCountry() {
        return officeCountry;
    }

    public void setOfficeCountry(String officeCountry) {
        this.officeCountry = officeCountry;
    }

    public String getOfficeZipCode() {
        return officeZipCode;
    }

    public void setOfficeZipCode(String officeZipCode) {
        this.officeZipCode = officeZipCode;
    }

    public String getHomeAddress1() {
        return homeAddress1;
    }

    public void setHomeAddress1(String homeAddress1) {
        this.homeAddress1 = homeAddress1;
    }

    public String getHomeAddress2() {
        return homeAddress2;
    }

    public void setHomeAddress2(String homeAddress2) {
        this.homeAddress2 = homeAddress2;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public String getHomeZipCode() {
        return homeZipCode;
    }

    public void setHomeZipCode(String homeZipCode) {
        this.homeZipCode = homeZipCode;
    }

    public String getTempAddress1() {
        return tempAddress1;
    }

    public void setTempAddress1(String tempAddress1) {
        this.tempAddress1 = tempAddress1;
    }

    public String getTempAddress2() {
        return tempAddress2;
    }

    public void setTempAddress2(String tempAddress2) {
        this.tempAddress2 = tempAddress2;
    }

    public String getTempCity() {
        return tempCity;
    }

    public void setTempCity(String tempCity) {
        this.tempCity = tempCity;
    }

    public String getTempCountry() {
        return tempCountry;
    }

    public void setTempCountry(String tempCountry) {
        this.tempCountry = tempCountry;
    }

    public String getTempZipCode() {
        return tempZipCode;
    }

    public void setTempZipCode(String tempZipCode) {
        this.tempZipCode = tempZipCode;
    }

    public String getOtherAddress1() {
        return otherAddress1;
    }

    public void setOtherAddress1(String otherAddress1) {
        this.otherAddress1 = otherAddress1;
    }

    public String getOtherAddress2() {
        return otherAddress2;
    }

    public void setOtherAddress2(String otherAddress2) {
        this.otherAddress2 = otherAddress2;
    }

    public String getCorrespondenceFlag() {
        return CorrespondenceFlag;
    }

    public void setCorrespondenceFlag(String correspondenceFlag) {
        CorrespondenceFlag = correspondenceFlag;
    }

    public String getOfficePhone1() {
        return officePhone1;
    }

    public void setOfficePhone1(String officePhone1) {
        this.officePhone1 = officePhone1;
    }

    public String getOfficePhone2() {
        return officePhone2;
    }

    public void setOfficePhone2(String officePhone2) {
        this.officePhone2 = officePhone2;
    }

    public String getOfficeFaxNo() {
        return officeFaxNo;
    }

    public void setOfficeFaxNo(String officeFaxNo) {
        this.officeFaxNo = officeFaxNo;
    }

    public String getResidencePhone1() {
        return residencePhone1;
    }

    public void setResidencePhone1(String residencePhone1) {
        this.residencePhone1 = residencePhone1;
    }

    public String getResidencePhone2() {
        return residencePhone2;
    }

    public void setResidencePhone2(String residencePhone2) {
        this.residencePhone2 = residencePhone2;
    }

    public String getResidenceFaxNo() {
        return residenceFaxNo;
    }

    public void setResidenceFaxNo(String residenceFaxNo) {
        this.residenceFaxNo = residenceFaxNo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getLimitProfile() {
        return limitProfile;
    }

    public void setLimitProfile(String limitProfile) {
        this.limitProfile = limitProfile;
    }

    public DateTime getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(DateTime deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getReserverd1() {
        return reserverd1;
    }

    public void setReserverd1(String reserverd1) {
        this.reserverd1 = reserverd1;
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

    public String getReserverd4() {
        return reserverd4;
    }

    public void setReserverd4(String reserverd4) {
        this.reserverd4 = reserverd4;
    }
}
