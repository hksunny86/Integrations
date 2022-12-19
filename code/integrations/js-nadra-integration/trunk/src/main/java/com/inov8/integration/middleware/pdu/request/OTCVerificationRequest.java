package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OTCVerificationRequest {

    @JsonProperty("data")
    private Data data;

    @JsonProperty("forward")
    private Forward forward;

    @JsonProperty("operation")
    private String operation;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Forward getForward() {
        return forward;
    }

    public void setForward(Forward forward) {
        this.forward = forward;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        @JsonProperty("session")
        private String session;

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forward {

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
        private META meta;
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

        public META getMeta() {
            return meta;
        }

        public void setMeta(META meta) {
            this.meta = meta;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Tags {

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
        public static class META {

            @JsonProperty("cf")
            private String cf;

            @JsonProperty("sz")
            private String sz;

            @JsonProperty("dtc")
            private String dtc;

            @JsonProperty("fq")
            private String fq;

            @JsonProperty("hr")
            private String hr;

            @JsonProperty("lg")
            private String lg;

            @JsonProperty("mlp")
            private String mlp;

            @JsonProperty("nfiq")
            private String nfiq;

            @JsonProperty("or")
            private String or;

            @JsonProperty("sg")
            private String sg;

            public String getCf() {
                return cf;
            }

            public void setCf(String cf) {
                this.cf = cf;
            }

            public String getSz() {
                return sz;
            }

            public void setSz(String sz) {
                this.sz = sz;
            }

            public String getDtc() {
                return dtc;
            }

            public void setDtc(String dtc) {
                this.dtc = dtc;
            }

            public String getFq() {
                return fq;
            }

            public void setFq(String fq) {
                this.fq = fq;
            }

            public String getHr() {
                return hr;
            }

            public void setHr(String hr) {
                this.hr = hr;
            }

            public String getLg() {
                return lg;
            }

            public void setLg(String lg) {
                this.lg = lg;
            }

            public String getMlp() {
                return mlp;
            }

            public void setMlp(String mlp) {
                this.mlp = mlp;
            }

            public String getNfiq() {
                return nfiq;
            }

            public void setNfiq(String nfiq) {
                this.nfiq = nfiq;
            }

            public String getOr() {
                return or;
            }

            public void setOr(String or) {
                this.or = or;
            }

            public String getSg() {
                return sg;
            }

            public void setSg(String sg) {
                this.sg = sg;
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

    }

}
