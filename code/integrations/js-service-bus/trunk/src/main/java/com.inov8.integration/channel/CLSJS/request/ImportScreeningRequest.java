package com.inov8.integration.channel.CLSJS.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import static com.inov8.integration.enums.DateFormatEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

//@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ProcessingCode",
        "TransmissionDatetime",
        "SystemsTraceAuditNumber",
        "TimeLocalTransaction",
        "DateLocalTransaction",
        "MerchantType",
        "Cnic",
        "CustomerName",
        "DateOfBirth",
        "Nationality",
        "City",
        "CustomerNumber",
        "UserId"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportScreeningRequest extends Request {

//    @XmlElement(name = "CNIC")
//    private String cnic;
//    @XmlElement(name = "CustomerName")
//    private String customerName;
//    @XmlElement(name = "FatherName")
//    private String fatherName;
//    @XmlElement(name = "DateOfBirth")
//    private String dateOfBirth;
//    @XmlElement(name = "Nationality")
//    private String nationality;
//    @XmlElement(name = "City")
//    private String city;
//    @XmlElement(name = "CustomerNumber")
//    private String customerNumber;
//    @XmlElement(name = "UserId")
//    private String userId;
//    private String isCustomerIndividual;
//    @XmlElement(name = "RequestID")
//    private String requestID;

    @JsonProperty("ProcessingCode")
    private String processingCode;
    @JsonProperty("TransmissionDatetime")
    private String transmissionDatetime;
    @JsonProperty("SystemsTraceAuditNumber")
    private String systemsTraceAuditNumber;
    @JsonProperty("TimeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("DateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("MerchantType")
    private String merchantType;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("CustomerName")
    private String customerName;
//    @JsonProperty("fatherName")
//    private String fatherName;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("Nationality")
    private String nationality;
    @JsonProperty("City")
    private String city;
    @JsonProperty("CustomerNumber")
    private String customerNumber;
    @JsonProperty("UserId")
    private String userId;

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTransmissionDatetime() {
        return transmissionDatetime;
    }

    public void setTransmissionDatetime(String transmissionDatetime) {
        this.transmissionDatetime = transmissionDatetime;
    }

    public String getSystemsTraceAuditNumber() {
        return systemsTraceAuditNumber;
    }

    public void setSystemsTraceAuditNumber(String systemsTraceAuditNumber) {
        this.systemsTraceAuditNumber = systemsTraceAuditNumber;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

//    public String getFatherName() {
//        return fatherName;
//    }
//
//    public void setFatherName(String fatherName) {
//        this.fatherName = fatherName;
//    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("ComplianceLink");
        this.setTransmissionDatetime((DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue())) + (DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue())));
        this.setSystemsTraceAuditNumber(DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue()));
        this.setTimeLocalTransaction(DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue()));
        this.setDateLocalTransaction(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setMerchantType("0098");
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setCustomerName(i8SBSwitchControllerRequestVO.getName());
//        this.setFatherName(i8SBSwitchControllerRequestVO.getFatherName());
//        if (i8SBSwitchControllerRequestVO.getFatherName() != null) {
//            this.setFatherName(i8SBSwitchControllerRequestVO.getFatherName());
//        } else {
//            this.setFatherName("");
//        }
        this.setDateOfBirth(i8SBSwitchControllerRequestVO.getDateOfBirth());
        if (i8SBSwitchControllerRequestVO.getNationality().equalsIgnoreCase("Pakistan")) {
            this.setNationality("Pakistani");
        } else {
            this.setNationality("");
        }
        if (i8SBSwitchControllerRequestVO.getCity() != null) {
            if (i8SBSwitchControllerRequestVO.getCity().isEmpty()) {
                this.setCity("");
            } else {
                this.setCity(i8SBSwitchControllerRequestVO.getCity());
            }
        }
        this.setCustomerNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setUserId("");
    }


//    public static String convert(String s){
//        String array [] = s.split("-");
//        String result = array[1].concat("/"); //Month
//        result = result.concat(array[2]); // Day
//        result =  result.concat("/");
//        result =  result.concat(array[0]); //Year
//
//        return result;
//    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

//        if (StringUtils.isEmpty(this.getRequestID())) {
//            throw new I8SBValidationException("[Failed] Reques ID " + this.getRequestID());
//        }
//
//        if (StringUtils.isEmpty(this.getCnic())) {
//            throw new I8SBValidationException("[Failed] CNIC " + this.getCnic());
//        }
//        if (StringUtils.isEmpty(this.getFatherName())) {
//            throw new I8SBValidationException("[Failed] Father Name " + this.getFatherName());
//        }
//        if (StringUtils.isEmpty(this.getCustomerName())) {
//            throw new I8SBValidationException("[Failed] Customer Name " + this.getCustomerName());
//        }

        return true;
    }
}
