package com.inov8.integration.channel.zindigi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.enums.DateFormatEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import static com.inov8.integration.enums.DateFormatEnum.CNIC_FORMAT;
import static com.inov8.integration.enums.DateFormatEnum.EXPIRY_DATE;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "FullName",
        "Mobile",
        "CNIC",
        "CNICIssuanceDate",
        "CNICExpiryDate",
        "Gender",
        "DateOfBirth",
        "Address",
        "Email",
        "FatherName",
        "MotherName",
        "MobileNetwork",
        "PlaceOfBirth"
})
public class ZindigiCustomerSyncRequest extends Request {

    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("CNIC")
    private String cnic;
    @JsonProperty("CNICIssuanceDate")
    private String cNICIssuanceDate;
    @JsonProperty("CNICExpiryDate")
    private String cNICExpiryDate;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("FatherName")
    private String fatherName;
    @JsonProperty("MotherName")
    private String motherName;
    @JsonProperty("MobileNetwork")
    private String mobileNetwork;
    @JsonProperty("PlaceOfBirth")
    private String placeOfBirth;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getcNICIssuanceDate() {
        return cNICIssuanceDate;
    }

    public void setcNICIssuanceDate(String cNICIssuanceDate) {
        this.cNICIssuanceDate = cNICIssuanceDate;
    }

    public String getcNICExpiryDate() {
        return cNICExpiryDate;
    }

    public void setcNICExpiryDate(String cNICExpiryDate) {
        this.cNICExpiryDate = cNICExpiryDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMobileNetwork() {
        return mobileNetwork;
    }

    public void setMobileNetwork(String mobileNetwork) {
        this.mobileNetwork = mobileNetwork;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        if (i8SBSwitchControllerRequestVO.getFullName15().isEmpty() || i8SBSwitchControllerRequestVO.getFullName15().equals("null")) {
            this.setFullName("");
        } else {
            this.setFullName(i8SBSwitchControllerRequestVO.getFullName15());
        }
        if (i8SBSwitchControllerRequestVO.getMobileNumber().isEmpty() || i8SBSwitchControllerRequestVO.getMobileNumber().equals("null")) {
            this.setMobile("");
        } else {
            this.setMobile(i8SBSwitchControllerRequestVO.getMobileNumber());
        }
        if (i8SBSwitchControllerRequestVO.getCNIC().isEmpty() || i8SBSwitchControllerRequestVO.getCNIC().equals("null")) {
            this.setCnic("");
        } else {
            this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        }
        if (i8SBSwitchControllerRequestVO.getCnicIssuanceDate().isEmpty() || i8SBSwitchControllerRequestVO.getCnicIssuanceDate().equals("null")) {
            this.setcNICIssuanceDate("");
        } else {
            this.setcNICIssuanceDate(DateUtil.cnicDateFormat(i8SBSwitchControllerRequestVO.getCnicIssuanceDate(), CNIC_FORMAT.getValue()));
        }
        if (i8SBSwitchControllerRequestVO.getCnicExpiry().isEmpty() || i8SBSwitchControllerRequestVO.getCnicExpiry().equals("null")) {
            this.setcNICExpiryDate("");
        } else {
            this.setcNICExpiryDate(DateUtil.cnicDateFormat(i8SBSwitchControllerRequestVO.getCnicExpiry(),CNIC_FORMAT.getValue()));
        }

        if (i8SBSwitchControllerRequestVO.getGenderCode().isEmpty() || i8SBSwitchControllerRequestVO.getGenderCode().equals("null")) {
            this.setGender("");
        } else {
            this.setGender(i8SBSwitchControllerRequestVO.getGenderCode());
        }

        if (i8SBSwitchControllerRequestVO.getDateOfBirth().isEmpty() || i8SBSwitchControllerRequestVO.getDateOfBirth().equals("null")) {
            this.setDateOfBirth("");
        } else {
            this.setDateOfBirth(DateUtil.cnicDateFormat(i8SBSwitchControllerRequestVO.getDateOfBirth(),CNIC_FORMAT.getValue()));
        }
        if (i8SBSwitchControllerRequestVO.getAddress().isEmpty() || i8SBSwitchControllerRequestVO.getAddress().equals("null")) {
            this.setAddress("");
        } else {
            this.setAddress(i8SBSwitchControllerRequestVO.getAddress());
        }

        if (i8SBSwitchControllerRequestVO.getEmail().isEmpty() || i8SBSwitchControllerRequestVO.getEmail().equals("null")) {
            this.setEmail("");
        } else {
            this.setEmail(i8SBSwitchControllerRequestVO.getEmail());
        }

        if (i8SBSwitchControllerRequestVO.getFatherName().isEmpty() || i8SBSwitchControllerRequestVO.getFatherName().equals("null")) {
            this.setFatherName("");
        } else {
            this.setFatherName(i8SBSwitchControllerRequestVO.getFatherName());
        }

        if (i8SBSwitchControllerRequestVO.getMotherMaidenName().isEmpty() || i8SBSwitchControllerRequestVO.getMotherMaidenName().equals("null")) {
            this.setMotherName("");
        } else {

            this.setMotherName(i8SBSwitchControllerRequestVO.getMotherMaidenName());
        }

        if (i8SBSwitchControllerRequestVO.getMobileNetwork().isEmpty() || i8SBSwitchControllerRequestVO.getMobileNetwork().equals("null")) {
            this.setMobileNetwork("");
        } else {

            this.setMobileNetwork(i8SBSwitchControllerRequestVO.getMobileNetwork());
        }

        if (i8SBSwitchControllerRequestVO.getPlaceOfBirth().isEmpty() || i8SBSwitchControllerRequestVO.getPlaceOfBirth().equals("null")) {
            this.setMobileNetwork("");
        } else {

            this.setPlaceOfBirth(i8SBSwitchControllerRequestVO.getPlaceOfBirth());
        }

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
