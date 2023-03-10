package com.inov8.integration.channel.CLSJS.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.inov8.integration.enums.DateFormatEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@XmlRootElement
public class ImportScreeningRequest extends Request {

    @XmlElement(name = "RequestID")
    private String requestID;
    @XmlElement(name = "CNIC")
    private String cnic;
    @XmlElement(name = "CustomerName")
    private String customerName;
    @XmlElement(name = "FatherName")
    private String fatherName;
    @XmlElement(name = "DateOfBirth")
    private String dateOfBirth;
    @XmlElement(name = "Nationality")
    private String nationality;
    @XmlElement(name = "City")
    private String city;
    @XmlElement(name = "CustomerNumber")
    private String customerNumber;
    @XmlElement(name = "UserId")
    private String userId;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
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

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setRequestID((DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue())) + (DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue())));
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setCustomerName(i8SBSwitchControllerRequestVO.getName());
        this.setFatherName(i8SBSwitchControllerRequestVO.getFatherName());
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

        if (StringUtils.isEmpty(this.getRequestID())) {
            throw new I8SBValidationException("[Failed] Reques ID " + this.getRequestID());
        }

        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] CNIC " + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getFatherName())) {
            throw new I8SBValidationException("[Failed] Father Name " + this.getFatherName());
        }
        if (StringUtils.isEmpty(this.getCustomerName())) {
            throw new I8SBValidationException("[Failed] Customer Name " + this.getCustomerName());
        }

        return true;
    }
}
