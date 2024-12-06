package com.inov8.integration.channel.euronet.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.euronetVO.InAccount;
import com.inov8.integration.webservice.euronetVO.InAuditDS;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardIssuanceRequest extends Request {

    private InAuditDS inAuditDS;
    private String inFunction;
    private String participantID;
    private String inCzID;
    private String inCIFkey;
    private String incardNumber;
    private String inMember;
    private String inBIN;
    private String inCustSchemeCode;
    private String inReqCardType;
    private String inCardType;
    private String inIBAN;
    private String inLanguage;
    private String inSearchName;
    private String inTitle;
    private String inFName;
    private String inMName;
    private String inLName;
    private String inSufx;
    private String inEmbName;
    private String inAddress1;
    private String inAddress2;
    private String inAddress3;
    private String inAddress4;
    private String inCity;
    private String instate;
    private String inCountry;
    private String inZIPCode;
    private String inAddtype;
    private String inHPhone;
    private String inDOB;
    private String inSex;
    private String inEmpFlg;
    private String inCorPer;
    private String issuanceFee;
    private String issuanceFED;
    private List<InAccount> inAccountLits;

    public InAuditDS getInAuditDS() {
        return inAuditDS;
    }

    public void setInAuditDS(InAuditDS inAuditDS) {
        this.inAuditDS = inAuditDS;
    }

    public String getInFunction() {
        return inFunction;
    }

    public void setInFunction(String inFunction) {
        this.inFunction = inFunction;
    }

    public String getParticipantID() {
        return participantID;
    }

    public void setParticipantID(String participantID) {
        this.participantID = participantID;
    }

    public String getInCzID() {
        return inCzID;
    }

    public void setInCzID(String inCzID) {
        this.inCzID = inCzID;
    }

    public String getInCIFkey() {
        return inCIFkey;
    }

    public void setInCIFkey(String inCIFkey) {
        this.inCIFkey = inCIFkey;
    }

    public String getIncardNumber() {
        return incardNumber;
    }

    public void setIncardNumber(String incardNumber) {
        this.incardNumber = incardNumber;
    }

    public String getInMember() {
        return inMember;
    }

    public void setInMember(String inMember) {
        this.inMember = inMember;
    }

    public String getInBIN() {
        return inBIN;
    }

    public void setInBIN(String inBIN) {
        this.inBIN = inBIN;
    }

    public String getInCustSchemeCode() {
        return inCustSchemeCode;
    }

    public void setInCustSchemeCode(String inCustSchemeCode) {
        this.inCustSchemeCode = inCustSchemeCode;
    }

    public String getInReqCardType() {
        return inReqCardType;
    }

    public void setInReqCardType(String inReqCardType) {
        this.inReqCardType = inReqCardType;
    }

    public String getInCardType() {
        return inCardType;
    }

    public void setInCardType(String inCardType) {
        this.inCardType = inCardType;
    }

    public String getInIBAN() {
        return inIBAN;
    }

    public void setInIBAN(String inIBAN) {
        this.inIBAN = inIBAN;
    }

    public String getInLanguage() {
        return inLanguage;
    }

    public void setInLanguage(String inLanguage) {
        this.inLanguage = inLanguage;
    }

    public String getInSearchName() {
        return inSearchName;
    }

    public void setInSearchName(String inSearchName) {
        this.inSearchName = inSearchName;
    }

    public String getInTitle() {
        return inTitle;
    }

    public void setInTitle(String inTitle) {
        this.inTitle = inTitle;
    }

    public String getInFName() {
        return inFName;
    }

    public void setInFName(String inFName) {
        this.inFName = inFName;
    }

    public String getInMName() {
        return inMName;
    }

    public void setInMName(String inMName) {
        this.inMName = inMName;
    }

    public String getInLName() {
        return inLName;
    }

    public void setInLName(String inLName) {
        this.inLName = inLName;
    }

    public String getInSufx() {
        return inSufx;
    }

    public void setInSufx(String inSufx) {
        this.inSufx = inSufx;
    }

    public String getInEmbName() {
        return inEmbName;
    }

    public void setInEmbName(String inEmbName) {
        this.inEmbName = inEmbName;
    }

    public String getInAddress1() {
        return inAddress1;
    }

    public void setInAddress1(String inAddress1) {
        this.inAddress1 = inAddress1;
    }

    public String getInAddress2() {
        return inAddress2;
    }

    public void setInAddress2(String inAddress2) {
        this.inAddress2 = inAddress2;
    }

    public String getInAddress3() {
        return inAddress3;
    }

    public void setInAddress3(String inAddress3) {
        this.inAddress3 = inAddress3;
    }

    public String getInAddress4() {
        return inAddress4;
    }

    public void setInAddress4(String inAddress4) {
        this.inAddress4 = inAddress4;
    }

    public String getInCity() {
        return inCity;
    }

    public void setInCity(String inCity) {
        this.inCity = inCity;
    }

    public String getInstate() {
        return instate;
    }

    public void setInstate(String instate) {
        this.instate = instate;
    }

    public String getInCountry() {
        return inCountry;
    }

    public void setInCountry(String inCountry) {
        this.inCountry = inCountry;
    }

    public String getInZIPCode() {
        return inZIPCode;
    }

    public void setInZIPCode(String inZIPCode) {
        this.inZIPCode = inZIPCode;
    }

    public String getInAddtype() {
        return inAddtype;
    }

    public void setInAddtype(String inAddtype) {
        this.inAddtype = inAddtype;
    }

    public String getInHPhone() {
        return inHPhone;
    }

    public void setInHPhone(String inHPhone) {
        this.inHPhone = inHPhone;
    }

    public String getInDOB() {
        return inDOB;
    }

    public void setInDOB(String inDOB) {
        this.inDOB = inDOB;
    }

    public String getInSex() {
        return inSex;
    }

    public void setInSex(String inSex) {
        this.inSex = inSex;
    }

    public String getInEmpFlg() {
        return inEmpFlg;
    }

    public void setInEmpFlg(String inEmpFlg) {
        this.inEmpFlg = inEmpFlg;
    }

    public String getInCorPer() {
        return inCorPer;
    }

    public void setInCorPer(String inCorPer) {
        this.inCorPer = inCorPer;
    }

    public String getIssuanceFee() {
        return issuanceFee;
    }

    public void setIssuanceFee(String issuanceFee) {
        this.issuanceFee = issuanceFee;
    }

    public String getIssuanceFED() {
        return issuanceFED;
    }

    public void setIssuanceFED(String issuanceFED) {
        this.issuanceFED = issuanceFED;
    }

    public List<InAccount> getInAccountLits() {
        return inAccountLits;
    }

    public void setInAccountLits(List<InAccount> inAccountLits) {
        this.inAccountLits = inAccountLits;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        InAuditDS inAuditDS = i8SBSwitchControllerRequestVO.getInAuditDS();
        inAuditDS.setApplication(PropertyReader.getProperty("euronet.application"));
        inAuditDS.setUserId(inAuditDS.getUserId());
        inAuditDS.setOrganization(PropertyReader.getProperty("euronet.organization"));
        inAuditDS.setServiceID(PropertyReader.getProperty("euronet.serviceID"));
        inAuditDS.setSequence(inAuditDS.getSequence());
        inAuditDS.setSourceDate(inAuditDS.getSourceDate());
        inAuditDS.setSourceTime(inAuditDS.getSourceTime());
        inAuditDS.setTransmissionDate(inAuditDS.getTransmissionDate());
        inAuditDS.setTransmissionTime(inAuditDS.getTransmissionTime());
        InAccount inAccount = i8SBSwitchControllerRequestVO.getInAccount();
        inAccount.setInAccTyp(PropertyReader.getProperty("euronet.inAccTyp"));
        inAccount.setInAccNum(inAccount.getInAccNum());
        inAccount.setInAccDesc(inAccount.getInAccDesc());
        inAccount.setInAccBrch(inAccount.getInAccBrch());
        inAccount.setInAccCurCd(PropertyReader.getProperty("euronet.inAccCurCd"));
        inAccount.setInAccRouting(PropertyReader.getProperty("euronet.inAccRouting"));
        inAccount.setInAccSts(inAccount.getInAccSts());
        inAccount.setInAccWvChrg(PropertyReader.getProperty("euronet.inAccWvChrg"));
        inAccount.setInPriFlag(PropertyReader.getProperty("euronet.inPriFlag"));
        inAccount.setUnspecified(PropertyReader.getProperty("euronet.Unspecified"));
        List<InAccount> list = new ArrayList<>();
        list.add(inAccount);
        this.setInAuditDS(inAuditDS);
        this.setInFunction(PropertyReader.getProperty("euronet.inFunction"));
        this.setParticipantID(PropertyReader.getProperty("euronet.participantID"));
        this.setInCzID(i8SBSwitchControllerRequestVO.getInCzID());
        this.setInCIFkey(i8SBSwitchControllerRequestVO.getInCIFkey());
        this.setIncardNumber(PropertyReader.getProperty("euronet.incardNumber"));
        this.setInMember(PropertyReader.getProperty("euronet.inMember"));
        this.setInBIN(i8SBSwitchControllerRequestVO.getInBIN());
        this.setInCustSchemeCode(PropertyReader.getProperty("euronet.inCustSchemeCode"));
        this.setInReqCardType(PropertyReader.getProperty("euronet.inReqCardType"));
        this.setInCardType(PropertyReader.getProperty("euronet.inCardType"));
        this.setInIBAN(i8SBSwitchControllerRequestVO.getInIBAN());
        this.setInLanguage(PropertyReader.getProperty("euronet.inLanguage"));
        this.setInSearchName(i8SBSwitchControllerRequestVO.getInSearchName());
        this.setInTitle(i8SBSwitchControllerRequestVO.getInTitle());
        this.setInFName(i8SBSwitchControllerRequestVO.getInFName());
        this.setInMName(i8SBSwitchControllerRequestVO.getInMName());
        this.setInLName(i8SBSwitchControllerRequestVO.getInLName());
        this.setInSufx(i8SBSwitchControllerRequestVO.getInSufx());
        this.setInEmbName(i8SBSwitchControllerRequestVO.getInEmbName());
        this.setInAddress1(i8SBSwitchControllerRequestVO.getInAddress1());
        this.setInAddress2(i8SBSwitchControllerRequestVO.getInAddress2());
        this.setInAddress3(i8SBSwitchControllerRequestVO.getInAddress3());
        this.setInAddress4(i8SBSwitchControllerRequestVO.getInAddress4());
        this.setInCity(i8SBSwitchControllerRequestVO.getCity());
        this.setInstate(i8SBSwitchControllerRequestVO.getInstate());
        this.setInCountry(PropertyReader.getProperty("euronet.inCountry"));
        this.setInZIPCode(i8SBSwitchControllerRequestVO.getInZIPCode());
        this.setInAddtype(i8SBSwitchControllerRequestVO.getInAddtype());
        this.setInHPhone(i8SBSwitchControllerRequestVO.getInHPhone());
        this.setInDOB(i8SBSwitchControllerRequestVO.getInDOB());
        this.setInSex(i8SBSwitchControllerRequestVO.getInSex());
        this.setInEmpFlg(PropertyReader.getProperty("euronet.inEmpFlg"));
        this.setInCorPer(PropertyReader.getProperty("euronet.inCorPer"));
        this.setIssuanceFee(i8SBSwitchControllerRequestVO.getIssuanceFee());
        this.setIssuanceFED(i8SBSwitchControllerRequestVO.getIssuanceFED());
        this.setInAccountLits(list);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


