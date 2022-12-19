package com.inov8.agentmate.model;

public class SegmentModel {

    private String code;
    private String name;
    private String segmentId;

    public SegmentModel(){

    }

    public SegmentModel(String name, String code) {
        this.code = code;
        this.name = name;
    }
    public SegmentModel(String name, String code, String segmentId) {
        this.code = code;
        this.name = name;
        this.segmentId = segmentId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
