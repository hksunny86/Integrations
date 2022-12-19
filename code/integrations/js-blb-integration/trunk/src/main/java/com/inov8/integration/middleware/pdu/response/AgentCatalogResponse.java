package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.vo.CatalogList;
import com.inov8.integration.vo.SegmentList;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentCatalogResponse")
public class AgentCatalogResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "CatalogName")
    @XmlElementWrapper(name = "CatalogNames")
    private List<CatalogList> catalogNames;
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

    public List<CatalogList> getCatalogNames() {
        return catalogNames;
    }

    public void setCatalogNames(List<CatalogList> catalogNames) {
        this.catalogNames = catalogNames;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
