package com.inov8.integration.vo;

import java.io.Serializable;

public class SegmentList implements Serializable {

    private String segmentId;
    private String SegmentName;

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getSegmentName() {
        return SegmentName;
    }

    public void setSegmentName(String segmentName) {
        SegmentName = segmentName;
    }
}
