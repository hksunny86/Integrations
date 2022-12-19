package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OTCVerification {

    @JsonProperty("finger")
    private String finger;
    @JsonProperty("session")
    private String session;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("institution")
    private String institution;
    @JsonProperty("wsq")
    private String wsq;

    @JsonProperty("tags")
    private Tags tags;

    @JsonProperty("meta")
    private OTCVerificationRequest.Forward.META meta;

    @JsonProperty("sdk")
    private SDK sdk;

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public SDK getSdk() {
        return sdk;
    }

    public void setSdk(SDK sdk) {
        this.sdk = sdk;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getWsq() {
        return wsq;
    }

    public void setWsq(String wsq) {
        this.wsq = wsq;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tags{

        @JsonProperty("secondaryIdentifier")
        private String secondaryIdentifier;
        @JsonProperty("remittanceType")
        private String remittanceType;
        @JsonProperty("contactNumber")
        private String contactNumber;
        @JsonProperty("remittanceAmount")
        private String remittanceAmount;
        @JsonProperty("secondaryContactNumber")
        private String secondaryContactNumber;
        @JsonProperty("accountNumber")
        private String accountNumber;
        @JsonProperty("areaName")
        private String areaName;

        public String getSecondaryIdentifier() {
            return secondaryIdentifier;
        }

        public void setSecondaryIdentifier(String secondaryIdentifier) {
            this.secondaryIdentifier = secondaryIdentifier;
        }

        public String getRemittanceType() {
            return remittanceType;
        }

        public void setRemittanceType(String remittanceType) {
            this.remittanceType = remittanceType;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getRemittanceAmount() {
            return remittanceAmount;
        }

        public void setRemittanceAmount(String remittanceAmount) {
            this.remittanceAmount = remittanceAmount;
        }

        public String getSecondaryContactNumber() {
            return secondaryContactNumber;
        }

        public void setSecondaryContactNumber(String secondaryContactNumber) {
            this.secondaryContactNumber = secondaryContactNumber;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SDK {

        @JsonProperty("application")
        private String application;

        @JsonProperty("device")
        private String device;

        @JsonProperty("version")
        private String version;

        @JsonProperty("applicationVersion")
        private String applicationVersion;

        @JsonProperty("manufacturer")
        private String manufacturer;

        @JsonProperty("model")
        private String model;

        public String getApplicationVersion() {
            return applicationVersion;
        }

        public void setApplicationVersion(String applicationVersion) {
            this.applicationVersion = applicationVersion;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public OTCVerificationRequest.Forward.META getMeta() {
        return meta;
    }

    public void setMeta(OTCVerificationRequest.Forward.META meta) {
        this.meta = meta;
    }
}
