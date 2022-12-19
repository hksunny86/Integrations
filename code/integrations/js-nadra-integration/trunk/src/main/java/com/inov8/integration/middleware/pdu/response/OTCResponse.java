package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OTCResponse {

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

            @JsonProperty("urduName")
            private String urduName;
            @JsonProperty("citizenNumber")
            private String citizenNumber;
            @JsonProperty("secondaryCitizenUrduName")
            private String secondaryCitizenUrduName;
            @JsonProperty("secondaryCitizenNumber")
            private String secondaryCitizenNumber;

            @JsonProperty("fingerIndexes")
            private String fingerIndexes;

            public String getFingerIndexes() {
                return fingerIndexes;
            }

            public void setFingerIndexes(String fingerIndexes) {
                this.fingerIndexes = fingerIndexes;
            }

            public String getUrduName() {
                return urduName;
            }

            public void setUrduName(String urduName) {
                this.urduName = urduName;
            }

            public String getCitizenNumber() {
                return citizenNumber;
            }

            public void setCitizenNumber(String citizenNumber) {
                this.citizenNumber = citizenNumber;
            }

            public String getSecondaryCitizenUrduName() {
                return secondaryCitizenUrduName;
            }

            public void setSecondaryCitizenUrduName(String secondaryCitizenUrduName) {
                this.secondaryCitizenUrduName = secondaryCitizenUrduName;
            }

            public String getSecondaryCitizenNumber() {
                return secondaryCitizenNumber;
            }

            public void setSecondaryCitizenNumber(String secondaryCitizenNumber) {
                this.secondaryCitizenNumber = secondaryCitizenNumber;
            }
        }
    }
}
