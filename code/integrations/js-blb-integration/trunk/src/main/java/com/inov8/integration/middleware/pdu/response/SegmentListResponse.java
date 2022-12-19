package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.vo.CardType;
import com.inov8.integration.vo.SegmentList;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SegmentListResponse")
public class SegmentListResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "SegmentName")
    @XmlElementWrapper(name = "SegmentNames")
    private List<SegmentList> segmentNames;
    @XmlElement(name = "HashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public List<SegmentList> getSegmentNames() {
        return segmentNames;
    }

    public void setSegmentNames(List<SegmentList> segmentNames) {
        this.segmentNames = segmentNames;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
