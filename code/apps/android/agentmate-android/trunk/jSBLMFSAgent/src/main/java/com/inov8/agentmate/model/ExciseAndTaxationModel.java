package com.inov8.agentmate.model;

import java.io.Serializable;

public class ExciseAndTaxationModel implements Serializable {

    private String vehRegNo;
    private String vehChassisNo;
    private String vehAssNo;
    private String vehRegDate;
    private String makerMake;
    private String vehCat;
    private String vehAssesmentNo;
    private String vehBodyType;
    private String vehEngCapacity;
    private String vehSeats;
    private String vehCylinders;
    private String vehOwnerName;
    private String vehOwnerCNIC;
    private String vehFileerStatus;
    private String taxPaidFrom;
    private String taxPaidUpto;
    private String vehTaxPaidLifetime;
    private String vehStatus;
    private String vehFitnessDate;
    private String vehTaxId;
    private String vehTaxName;
    private String vehAccountHead;
    private String vehTaxAmount;
    private String vehTaxAmountF;
    private String totalAmount;
    private String totalAmountf;
    private String charges;
    private String chargesF;
    private String vehAssesmentDate;

    private static ExciseAndTaxationModel exciseAndTaxationModel = null;

    public ExciseAndTaxationModel(){

    }

    public ExciseAndTaxationModel(String vehRegNo, String vehChassisNo, String vehAssNo, String vehRegDate, String makerMake,
                                  String vehCat, String vehBodyType, String vehEngCapacity, String vehSeats, String vehCylinders,
                                  String vehOwnerName, String vehOwnerCNIC, String vehFileerStatus, String taxPaidFrom, String taxPaidUpto,
                                  String vehTaxPaidLifetime, String vehStatus, String vehFitnessDate, String vehTaxId, String vehTaxName,
                                  String vehAccountHead, String vehTaxAmount, String vehAssesmentDate, String vehAssesmentNo
            , String totalAmount, String charges, String chargesF, String vehTaxAmountF) {

        this.vehRegNo = vehRegNo;
        this.vehChassisNo = vehChassisNo;
        this.vehAssNo = vehAssNo;
        this.vehRegDate = vehRegDate;
        this.makerMake = makerMake;
        this.vehCat = vehCat;
        this.vehBodyType = vehBodyType;
        this.vehEngCapacity = vehEngCapacity;
        this.vehSeats = vehSeats;
        this.vehCylinders = vehCylinders;
        this.vehOwnerName = vehOwnerName;
        this.vehOwnerCNIC = vehOwnerCNIC;
        this.vehFileerStatus = vehFileerStatus;
        this.taxPaidFrom = taxPaidFrom;
        this.taxPaidUpto = taxPaidUpto;
        this.vehTaxPaidLifetime = vehTaxPaidLifetime;
        this.vehStatus = vehStatus;
        this.vehFitnessDate = vehFitnessDate;
        this.vehTaxId = vehTaxId;
        this.vehTaxName = vehTaxName;
        this.vehAccountHead = vehAccountHead;
        this.vehTaxAmount = vehTaxAmount;
        this.vehAssesmentDate = vehAssesmentDate;
        this.vehAssesmentNo = vehAssesmentNo;
        this.totalAmount = totalAmount;
        this.charges = charges;
        this.chargesF = chargesF;
        this.vehTaxAmountF = vehTaxAmountF;
    }

    public static ExciseAndTaxationModel getInstance() {
        if (exciseAndTaxationModel == null) {
            exciseAndTaxationModel = new ExciseAndTaxationModel();
        }
        return exciseAndTaxationModel;
    }

    public String getVehRegNo() {
        return vehRegNo;
    }

    public void setVehRegNo(String vehRegNo) {
        this.vehRegNo = vehRegNo;
    }

    public String getVehChassisNo() {
        return vehChassisNo;
    }

    public void setVehChassisNo(String vehChassisNo) {
        this.vehChassisNo = vehChassisNo;
    }

    public String getVehAssNo() {
        return vehAssNo;
    }

    public void setVehAssNo(String vehAssNo) {
        this.vehAssNo = vehAssNo;
    }

    public String getVehRegDate() {
        return vehRegDate;
    }

    public void setVehRegDate(String vehRegDate) {
        this.vehRegDate = vehRegDate;
    }

    public String getMakerMake() {
        return makerMake;
    }

    public void setMakerMake(String makerMake) {
        this.makerMake = makerMake;
    }

    public String getVehCat() {
        return vehCat;
    }

    public void setVehCat(String vehCat) {
        this.vehCat = vehCat;
    }

    public String getVehBodyType() {
        return vehBodyType;
    }

    public void setVehBodyType(String vehBodyType) {
        this.vehBodyType = vehBodyType;
    }

    public String getVehEngCapacity() {
        return vehEngCapacity;
    }

    public void setVehEngCapacity(String vehEngCapacity) {
        this.vehEngCapacity = vehEngCapacity;
    }

    public String getVehSeats() {
        return vehSeats;
    }

    public void setVehSeats(String vehSeats) {
        this.vehSeats = vehSeats;
    }

    public String getVehCylinders() {
        return vehCylinders;
    }

    public void setVehCylinders(String vehCylinders) {
        this.vehCylinders = vehCylinders;
    }

    public String getVehOwnerName() {
        return vehOwnerName;
    }

    public void setVehOwnerName(String vehOwnerName) {
        this.vehOwnerName = vehOwnerName;
    }

    public String getVehOwnerCNIC() {
        return vehOwnerCNIC;
    }

    public void setVehOwnerCNIC(String vehOwnerCNIC) {
        this.vehOwnerCNIC = vehOwnerCNIC;
    }

    public String getVehFileerStatus() {
        return vehFileerStatus;
    }

    public void setVehFileerStatus(String vehFileerStatus) {
        this.vehFileerStatus = vehFileerStatus;
    }

    public String getTaxPaidFrom() {
        return taxPaidFrom;
    }

    public void setTaxPaidFrom(String taxPaidFrom) {
        this.taxPaidFrom = taxPaidFrom;
    }

    public String getTaxPaidUpto() {
        return taxPaidUpto;
    }

    public void setTaxPaidUpto(String taxPaidUpto) {
        this.taxPaidUpto = taxPaidUpto;
    }

    public String getVehTaxPaidLifetime() {
        return vehTaxPaidLifetime;
    }

    public void setVehTaxPaidLifetime(String vehTaxPaidLifetime) {
        this.vehTaxPaidLifetime = vehTaxPaidLifetime;
    }

    public String getVehStatus() {
        return vehStatus;
    }

    public void setVehStatus(String vehStatus) {
        this.vehStatus = vehStatus;
    }

    public String getVehFitnessDate() {
        return vehFitnessDate;
    }

    public void setVehFitnessDate(String vehFitnessDate) {
        this.vehFitnessDate = vehFitnessDate;
    }

    public String getVehTaxId() {
        return vehTaxId;
    }

    public void setVehTaxId(String vehTaxId) {
        this.vehTaxId = vehTaxId;
    }

    public String getVehTaxName() {
        return vehTaxName;
    }

    public void setVehTaxName(String vehTaxName) {
        this.vehTaxName = vehTaxName;
    }

    public String getVehAccountHead() {
        return vehAccountHead;
    }

    public void setVehAccountHead(String vehAccountHead) {
        this.vehAccountHead = vehAccountHead;
    }

    public String getVehTaxAmount() {
        return vehTaxAmount;
    }

    public void setVehTaxAmount(String vehTaxAmount) {
        this.vehTaxAmount = vehTaxAmount;
    }

    public String getVehAssesmentDate() {
        return vehAssesmentDate;
    }

    public void setVehAssesmentDate(String vehAssesmentDate) {
        this.vehAssesmentDate = vehAssesmentDate;
    }

    public String getVehAssesmentNo() {
        return vehAssesmentNo;
    }

    public void setVehAssesmentNo(String vehAssesmentNo) {
        this.vehAssesmentNo = vehAssesmentNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getTotalAmountf() {
        return totalAmountf;
    }

    public void setTotalAmountf(String totalAmountf) {
        this.totalAmountf = totalAmountf;
    }

    public String getVehTaxAmountF() {
        return vehTaxAmountF;
    }

    public void setVehTaxAmountF(String vehTaxAmountF) {
        this.vehTaxAmountF = vehTaxAmountF;
    }

    public String getChargesF() {
        return chargesF;
    }

    public void setChargesF(String chargesF) {
        this.chargesF = chargesF;
    }
}
