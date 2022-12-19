package com.inov8.jsblconsumer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HraRegistrationModel implements Parcelable {

    private String cnic;
    private String mob;
    private String name;
    private String fatherName;
    private String dob;
    private String incomeSource;
    private String occupation;
    private String accountPurpose;
    private String kinName;
    private String kinMob;
    private String kinCnic;
    private String kinRelationship;
    private String org1Location;
    private String org1Relation;
    private String org2Location;
    private String org2Relation;
    private String org3Location;
    private String org3Relation;
    private String org4Location;
    private String org4Relation;
    private String org5Location;
    private String org5Relation;

    public HraRegistrationModel() { }

    @Override
    public int describeContents() {
        return 0;
    }

    protected HraRegistrationModel(Parcel in) {
        cnic = in.readString();
        mob = in.readString();
        name = in.readString();
        fatherName = in.readString();
        dob = in.readString();
        incomeSource = in.readString();
        occupation = in.readString();
        accountPurpose = in.readString();
        kinName = in.readString();
        kinMob = in.readString();
        kinCnic = in.readString();
        kinRelationship = in.readString();
        org1Location = in.readString();
        org1Relation = in.readString();
        org2Location = in.readString();
        org2Relation = in.readString();
        org3Location = in.readString();
        org3Relation = in.readString();
        org4Location = in.readString();
        org4Relation = in.readString();
        org5Location = in.readString();
        org5Relation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cnic);
        dest.writeString(mob);
        dest.writeString(name);
        dest.writeString(fatherName);
        dest.writeString(dob);
        dest.writeString(incomeSource);
        dest.writeString(occupation);
        dest.writeString(accountPurpose);
        dest.writeString(kinName);
        dest.writeString(kinMob);
        dest.writeString(kinCnic);
        dest.writeString(kinRelationship);
        dest.writeString(org1Location);
        dest.writeString(org1Relation);
        dest.writeString(org2Location);
        dest.writeString(org2Relation);
        dest.writeString(org3Location);
        dest.writeString(org3Relation);
        dest.writeString(org4Location);
        dest.writeString(org4Relation);
        dest.writeString(org5Location);
        dest.writeString(org5Relation);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HraRegistrationModel> CREATOR = new Parcelable.Creator<HraRegistrationModel>() {
        @Override
        public HraRegistrationModel createFromParcel(Parcel in) {
            return new HraRegistrationModel(in);
        }

        @Override
        public HraRegistrationModel[] newArray(int size) {
            return new HraRegistrationModel[size];
        }
    };

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIncomeSource() {
        return incomeSource;
    }

    public void setIncomeSource(String incomeSource) {
        this.incomeSource = incomeSource;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(String accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

    public String getKinName() {
        return kinName;
    }

    public void setKinName(String kinName) {
        this.kinName = kinName;
    }

    public String getKinMob() {
        return kinMob;
    }

    public void setKinMob(String kinMob) {
        this.kinMob = kinMob;
    }

    public String getKinCnic() {
        return kinCnic;
    }

    public void setKinCnic(String kinCnic) {
        this.kinCnic = kinCnic;
    }

    public String getKinRelationship() {
        return kinRelationship;
    }

    public void setKinRelationship(String kinRelationship) {
        this.kinRelationship = kinRelationship;
    }

    public String getOrg1Location() {
        return org1Location;
    }

    public void setOrg1Location(String org1Location) {
        this.org1Location = org1Location;
    }

    public String getOrg1Relation() {
        return org1Relation;
    }

    public void setOrg1Relation(String org1Relation) {
        this.org1Relation = org1Relation;
    }

    public String getOrg2Location() {
        return org2Location;
    }

    public void setOrg2Location(String org2Location) {
        this.org2Location = org2Location;
    }

    public String getOrg2Relation() {
        return org2Relation;
    }

    public void setOrg2Relation(String org2Relation) {
        this.org2Relation = org2Relation;
    }

    public String getOrg3Location() {
        return org3Location;
    }

    public void setOrg3Location(String org3Location) {
        this.org3Location = org3Location;
    }

    public String getOrg3Relation() {
        return org3Relation;
    }

    public void setOrg3Relation(String org3Relation) {
        this.org3Relation = org3Relation;
    }

    public String getOrg4Location() {
        return org4Location;
    }

    public void setOrg4Location(String org4Location) {
        this.org4Location = org4Location;
    }

    public String getOrg4Relation() {
        return org4Relation;
    }

    public void setOrg4Relation(String org4Relation) {
        this.org4Relation = org4Relation;
    }

    public String getOrg5Location() {
        return org5Location;
    }

    public void setOrg5Location(String org5Location) {
        this.org5Location = org5Location;
    }

    public String getOrg5Relation() {
        return org5Relation;
    }

    public void setOrg5Relation(String org5Relation) {
        this.org5Relation = org5Relation;
    }
}