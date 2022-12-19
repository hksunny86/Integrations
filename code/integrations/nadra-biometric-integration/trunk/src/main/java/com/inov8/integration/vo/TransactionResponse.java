package com.inov8.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by inov8 on 10/4/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse implements Serializable {

    private static final long serialVersionUID = -3145395115894566903L;
    private String responseCode;
    private String message;
    private String sessionId;
    private String transactionId;
    private String citizenNumber;
    private String citizenName;
    private String secondaryCitizenNumber;
    private String secondaryCitizenName;
    private String presentAddress;
    private String birthPlace;
    private String cardExpired;
    private String cardExpiry;
    private String dateOfBirth;
    private String fingerIndex;
    private String religion;
    private String motherName;
    private String nativeLanguage;
    private String photograph;
    private String verificationFunctionality;
    private String responseDateTime;
    private String gender;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCardExpired() {return cardExpired;}

    public void setCardExpired(String cardExpired) {this.cardExpired = cardExpired;}

    public String getCardExpiry() {return cardExpiry;}

    public void setCardExpiry(String cardExpiry) {this.cardExpiry = cardExpiry;}

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public String getVerificationFunctionality() {
        return verificationFunctionality;
    }

    public void setVerificationFunctionality(String verificationFunctionality) {
        this.verificationFunctionality = verificationFunctionality;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSecondaryCitizenNumber() {
        return secondaryCitizenNumber;
    }

    public void setSecondaryCitizenNumber(String secondaryCitizenNumber) {
        this.secondaryCitizenNumber = secondaryCitizenNumber;
    }

    public String getSecondaryCitizenName() {
        return secondaryCitizenName;
    }

    public void setSecondaryCitizenName(String secondaryCitizenName) {
        this.secondaryCitizenName = secondaryCitizenName;
    }
}
