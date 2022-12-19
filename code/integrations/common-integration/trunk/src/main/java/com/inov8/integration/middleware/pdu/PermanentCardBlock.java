package com.inov8.integration.middleware.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by inov8 on 10/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PermanentCardBlock implements Serializable {

    private static final long serialVersionUID = 1054088476952196151L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Mobile_No")
    private String mobileNo;

    @JsonProperty("CNIC_No")
    private String cnicNo;

    @JsonProperty("Card_No")
    private String cardNo;

    @JsonProperty("Block_Date_Time")
    private String blockingDateTime;

    @JsonProperty("Segment_Type")
    private String segmentType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBlockingDateTime() {
        return blockingDateTime;
    }

    public void setBlockingDateTime(String blockingDateTime) {
        this.blockingDateTime = blockingDateTime;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }
}

