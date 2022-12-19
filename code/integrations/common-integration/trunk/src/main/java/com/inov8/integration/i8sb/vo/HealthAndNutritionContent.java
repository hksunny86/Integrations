package com.inov8.integration.i8sb.vo;


import java.io.Serializable;
import java.util.Date;

public class HealthAndNutritionContent implements Serializable {

    public String CNIC;
    public String name;
    public String contactNo;
    public String amount;
    public String stage_Code;
    public String husbandName;
    public String husbandCNICNo;
    public Date DOB;
    public Long segmentId;
    public String nationality;
    public String visit_Id;
    public String payment_Status;

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStage_Code() {
        return stage_Code;
    }

    public void setStage_Code(String stage_Code) {
        this.stage_Code = stage_Code;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getHusbandCNICNo() {
        return husbandCNICNo;
    }

    public void setHusbandCNICNo(String husbandCNICNo) {
        this.husbandCNICNo = husbandCNICNo;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getVisit_Id() {
        return visit_Id;
    }

    public void setVisit_Id(String visit_Id) {
        this.visit_Id = visit_Id;
    }

    public String getPayment_Status() {
        return payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        this.payment_Status = payment_Status;
    }
}
