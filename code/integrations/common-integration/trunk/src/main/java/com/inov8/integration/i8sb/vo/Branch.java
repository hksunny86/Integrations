package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("Bank")
public class Branch implements Serializable {
    private String branchCode;
    private String swiftCode;
    private String branchName;
    private String city;
    private String country;
    private String address1;
    private String address2;
    private String zipCode;
    private String phone1;
    private String phone2;
    private String faxNumber;
    private String managerName;
    private String managerEmail;

    public String getBranchCode() { return branchCode; }

    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getSwiftCode() { return swiftCode; }

    public void setSwiftCode(String swiftCode) { this.swiftCode = swiftCode; }

    public String getBranchName() { return branchName; }

    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getAddress1() { return address1; }

    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }

    public void setAddress2(String address2) { this.address2 = address2; }

    public String getZipCode() { return zipCode; }

    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getPhone1() { return phone1; }

    public void setPhone1(String phone1) { this.phone1 = phone1; }

    public String getPhone2() { return phone2; }

    public void setPhone2(String phone2) { this.phone2 = phone2; }

    public String getFaxNumber() { return faxNumber; }

    public void setFaxNumber(String faxNumber) { this.faxNumber = faxNumber; }

    public String getManagerName() { return managerName; }

    public void setManagerName(String managerName) { this.managerName = managerName; }

    public String getManagerEmail() { return managerEmail; }

    public void setManagerEmail(String managerEmail) { this.managerEmail = managerEmail; }
}
