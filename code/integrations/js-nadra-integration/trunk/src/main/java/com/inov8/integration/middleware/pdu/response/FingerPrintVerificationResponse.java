package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FingerPrintVerificationResponse {

    @JsonProperty("status")
    private Status status;
    @JsonProperty("data")
    private Data data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Status {
        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Data {
        @JsonProperty("session")
        private String session;

        @JsonProperty("identifier")
        private String identifier;
        @JsonProperty("tags")
        private Tags tags;

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public Tags getTags() {
            return tags;
        }

        public void setTags(Tags tags) {
            this.tags = tags;
        }

        public static class Tags {
            @JsonProperty("expiryDate")
            private String expiryDate;
            @JsonProperty("cardExpired")
            private String cardExpired;
            @JsonProperty("gender")
            private String gender;
            @JsonProperty("birthPlace")
            private String birthPlace;
            @JsonProperty("photograph")
            private String photograph;
            @JsonProperty("presentAddress")
            private String presentAddress;
            @JsonProperty("cardType")
            private String cardType;
            @JsonProperty("name")
            private String name;
            @JsonProperty("fatherHusbandName")
            private String fatherHusbandName;
            @JsonProperty("motherName")
            private String motherName;
            @JsonProperty("dateOfBirth")
            private String dateOfBirth;
            @JsonProperty("permanentAddress")
            private String permanentAddress;

            @JsonProperty("fingerIndexes")
            private String fingerIndexes;

            public String getCardExpired() {
                return cardExpired;
            }

            public void setCardExpired(String cardExpired) {
                this.cardExpired = cardExpired;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getMotherName() {
                return motherName;
            }

            public void setMotherName(String motherName) {
                this.motherName = motherName;
            }

            public String getFingerIndexes() {
                return fingerIndexes;
            }

            public void setFingerIndexes(String fingerIndexes) {
                this.fingerIndexes = fingerIndexes;
            }

            public String getExpiryDate() {
                return expiryDate;
            }

            public void setExpiryDate(String expiryDate) {
                this.expiryDate = expiryDate;
            }

            public String getBirthPlace() {
                return birthPlace;
            }

            public void setBirthPlace(String birthPlace) {
                this.birthPlace = birthPlace;
            }

            public String getPhotograph() {
                return photograph;
            }

            public void setPhotograph(String photograph) {
                this.photograph = photograph;
            }

            public String getPresentAddress() {
                return presentAddress;
            }

            public void setPresentAddress(String presentAddress) {
                this.presentAddress = presentAddress;
            }

            public String getCardType() {
                return cardType;
            }

            public void setCardType(String cardType) {
                this.cardType = cardType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFatherHusbandName() {
                return fatherHusbandName;
            }

            public void setFatherHusbandName(String fatherHusbandName) {
                this.fatherHusbandName = fatherHusbandName;
            }

            public String getDateOfBirth() {
                return dateOfBirth;
            }

            public void setDateOfBirth(String dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
            }

            public String getPermanentAddress() {
                return permanentAddress;
            }

            public void setPermanentAddress(String permanentAddress) {
                this.permanentAddress = permanentAddress;
            }
        }


    }

}