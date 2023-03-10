package com.inov8.integration.channel.zindigi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cnic",
        "mobileNumber",
        "Selfie",
        "fatherHusbandName",
        "emailAddress",
        "mailingAddress",
        "permanentAddress",
        "purposeOfAccount",
        "sourceOfIncomePic",
        "expectedMonthlyTurnover",
        "proofOfProfession",
        "cnicFrontPic",
        "cnicBackPic",
        "Status",
})
public class L2AccountUpgradeValidationRequest extends Request implements Serializable {

    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("mobileNumber")
    private boolean mobileNumber;
    @JsonProperty("Selfie")
    private boolean selfie;
    @JsonProperty("fatherHusbandName")
    private boolean fatherHusbandName;
    @JsonProperty("emailAddress")
    private boolean emailAddress;
    @JsonProperty("mailingAddress")
    private boolean mailingAddress;
    @JsonProperty("permanentAddress")
    private boolean permanentAddress;
    @JsonProperty("purposeOfAccount")
    private boolean purposeOfAccount;
    @JsonProperty("sourceOfIncomePic")
    private boolean sourceOfIncomePic;
    @JsonProperty("expectedMonthlyTurnover")
    private boolean expectedMonthlyTurnover;
    @JsonProperty("proofOfProfession")
    private boolean proofOfProfession;
    @JsonProperty("cnicFrontPic")
    private boolean cnicFrontPic;
    @JsonProperty("cnicBackPic")
    private boolean cnicBackPic;
    @JsonProperty("SourceOfIncome")
    private Boolean sourceOfIncome;
    @JsonProperty("Signature")
    private Boolean signature;
    @JsonProperty("Status")
    private String status;


    public void setEmailAddress(boolean emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isEmailAddress() {
        return emailAddress;
    }

    public void setCnicFrontPic(boolean cnicFrontPic) {
        this.cnicFrontPic = cnicFrontPic;
    }

    public boolean isCnicFrontPic() {
        return cnicFrontPic;
    }

    public void setMailingAddress(boolean mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public boolean isMailingAddress() {
        return mailingAddress;
    }

    public void setCnicBackPic(boolean cnicBackPic) {
        this.cnicBackPic = cnicBackPic;
    }

    public boolean isCnicBackPic() {
        return cnicBackPic;
    }

    public void setMobileNumber(boolean mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isMobileNumber() {
        return mobileNumber;
    }

    public void setPurposeOfAccount(boolean purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    public boolean isPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setSourceOfIncomePic(boolean sourceOfIncomePic) {
        this.sourceOfIncomePic = sourceOfIncomePic;
    }

    public boolean isSourceOfIncomePic() {
        return sourceOfIncomePic;
    }

    public void setFatherHusbandName(boolean fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    public boolean isFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCnic() {
        return cnic;
    }

    public void setPermanentAddress(boolean permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public boolean isPermanentAddress() {
        return permanentAddress;
    }

    public void setExpectedMonthlyTurnover(boolean expectedMonthlyTurnover) {
        this.expectedMonthlyTurnover = expectedMonthlyTurnover;
    }

    public boolean isExpectedMonthlyTurnover() {
        return expectedMonthlyTurnover;
    }

    public void setProofOfProfession(boolean proofOfProfession) {
        this.proofOfProfession = proofOfProfession;
    }

    public boolean isProofOfProfession() {
        return proofOfProfession;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelfie() {
        return selfie;
    }

    public void setSelfie(boolean selfie) {
        this.selfie = selfie;
    }

    public Boolean getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(Boolean sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public Boolean getSignature() {
        return signature;
    }

    public void setSignature(Boolean signature) {
        this.signature = signature;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setMobileNumber(true);
        if (i8SBSwitchControllerRequestVO.getFatherName().equalsIgnoreCase("Approved")) {
            this.setFatherHusbandName(true);
        } else {
            this.setFatherHusbandName(false);
        }
        if (i8SBSwitchControllerRequestVO.getEmail().equalsIgnoreCase("Approved")) {
            this.setEmailAddress(true);
        } else {
            this.setEmailAddress(false);
        }
        if (i8SBSwitchControllerRequestVO.getMailingAddress().equalsIgnoreCase("Approved")) {
            this.setMailingAddress(true);
        } else {
            this.setMailingAddress(false);
        }
        if (i8SBSwitchControllerRequestVO.getAddress().equalsIgnoreCase("Approved")) {
            this.setPermanentAddress(true);
        } else {
            this.setPermanentAddress(false);
        }
        if (i8SBSwitchControllerRequestVO.getPurposeOfAccount().equalsIgnoreCase("Approved")) {
            this.setPurposeOfAccount(true);
        } else {
            this.setPurposeOfAccount(false);
        }
        if (i8SBSwitchControllerRequestVO.getSourceOfIncomePic().equalsIgnoreCase("Approved")) {
            this.setSourceOfIncomePic(true);
        } else {
            this.setSourceOfIncomePic(false);
        }
        if (i8SBSwitchControllerRequestVO.getExpectedMonthlyTurnOver().equalsIgnoreCase("Approved")) {
            this.setExpectedMonthlyTurnover(true);
        } else {
            this.setExpectedMonthlyTurnover(false);
        }
        if (i8SBSwitchControllerRequestVO.getProofOfProfession().equalsIgnoreCase("Approved")) {
            this.setProofOfProfession(true);
        } else {
            this.setProofOfProfession(false);
        }
        if (i8SBSwitchControllerRequestVO.getCnicFrontPic().equalsIgnoreCase("Approved")) {
            this.setCnicFrontPic(true);
        } else {
            this.setCnicFrontPic(false);
        }
        if (i8SBSwitchControllerRequestVO.getCnicBackPic().equalsIgnoreCase("Approved")) {
            this.setCnicBackPic(true);
        } else {
            this.setCnicBackPic(false);
        }
        this.setStatus(i8SBSwitchControllerRequestVO.getStatus());
        if (i8SBSwitchControllerRequestVO.getCustomerPic().equalsIgnoreCase("Approved")) {
            this.setSelfie(true);
        } else {
            this.setSelfie(false);
        }
        if (i8SBSwitchControllerRequestVO.getSignaturePic().equalsIgnoreCase("Approved")) {
            this.setSignature(true);
        } else {
            this.setSignature(false);
        }
        if (i8SBSwitchControllerRequestVO.getSourceOfIncome().equalsIgnoreCase("Approved")) {
            this.setSourceOfIncome(true);
        } else {
            this.setSourceOfIncome(false);
        }
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] CNIC :" + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getStatus())) {
            throw new I8SBValidationException("[Failed] Status :" + this.getStatus());
        }
        return true;
    }
}