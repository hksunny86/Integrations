package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FingerPrintVerification {

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
    @JsonProperty("meta")
    private BiometricVerificationRequest.Forward.META meta;
    @JsonProperty("sdk")
    private BiometricVerificationRequest.Forward.SDK sdk;

    public BiometricVerificationRequest.Forward.META getMeta() {
        return meta;
    }

    public void setMeta(BiometricVerificationRequest.Forward.META meta) {
        this.meta = meta;
    }

    public BiometricVerificationRequest.Forward.SDK getSdk() {
        return sdk;
    }

    public void setSdk(BiometricVerificationRequest.Forward.SDK sdk) {
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
    public static class SDK {

        @JsonProperty("application")
        private String application;

        @JsonProperty("device")
        private String device;

        @JsonProperty("version")
        private String version;

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

