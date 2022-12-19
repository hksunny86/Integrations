package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.middleware.pdu.PermanentCardBlock;

import java.io.Serializable;
import java.util.List;

/**
 * Created by inov8 on 10/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PermanentCardBlockRequest implements Serializable {

    private static final long serialVersionUID = 2134956891427267824L;

    @JsonProperty("Security_Key")
    private String secretKey;

    @JsonProperty("CardBlockList")
    private List<PermanentCardBlock> permanentCardBlockList;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<PermanentCardBlock> getPermanentCardBlockList() {
        return permanentCardBlockList;
    }

    public void setPermanentCardBlockList(List<PermanentCardBlock> permanentCardBlockList) {
        this.permanentCardBlockList = permanentCardBlockList;
    }
}
