package com.inov8.integration.channel.customerdeviceverification.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDeviceVerificationResponse extends Response {

    private static final long serialVersionUID = 1L;
    @JsonProperty("customers")
    public ArrayList<Customers> customers;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;

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

    public ArrayList<Customers> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customers> customers) {
        this.customers = customers;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
//        HashMap<String, ArrayList<com.inov8.integration.i8sb.vo.CustomerDeviceVerification>> list = new HashMap<>();
//        ArrayList<com.inov8.integration.i8sb.vo.CustomerDeviceVerification> data2 = new ArrayList<>();
//
//        if (this.getCustomers() != null) {
//            List<Customers> associatedAccountList = this.getCustomers();
//            associatedAccountList.forEach(data -> {
//                com.inov8.integration.i8sb.vo.CustomerDeviceVerification associatedAccounts = new com.inov8.integration.i8sb.vo.CustomerDeviceVerification();
//                associatedAccounts.setMobileNo(data.getMobileNo());
//                associatedAccounts.setDeviceName(data.getDeviceName());
//                associatedAccounts.setId(data.getId());
//                associatedAccounts.setApprovalStatus(data.getApprovalStatus());
//                associatedAccounts.setRemarks(data.getRemarks());
//                associatedAccounts.setRequestType(data.getRequestType());
//                associatedAccounts.setRequestedDate(data.getRequestedDate());
//                associatedAccounts.setRequestedTime(data.getRequestedTime());
//                associatedAccounts.setUniqueIdentifier(data.getUniqueIdentifier());
//                data2.add(associatedAccounts);
//            });
//            if (this.getResponseCode().equals("00")||this.getResponseCode().equals("404")) {
//                i8SBSwitchControllerResponseVO.setResponseCode("00");
//            }
//            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
//            list.put("asa", data2);
//            i8SBSwitchControllerResponseVO.setCollectionOfList(list);
//        }
        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Customers implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("ID")
    public String id;
    @JsonProperty("MobileNo")
    public String mobileNo;
    @JsonProperty("UniqueIdentifier")
    public String uniqueIdentifier;
    @JsonProperty("DeviceName")
    public String deviceName;
    @JsonProperty("RequestType")
    public String requestType;
    @JsonProperty("ApprovalStatus")
    public String approvalStatus;
    @JsonProperty("Remarks")
    public String remarks;
    @JsonProperty("RequestedDate")
    public String requestedDate;
    @JsonProperty("RequestedTime")
    public String requestedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }
}

