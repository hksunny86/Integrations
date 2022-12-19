package com.inov8.integration.channel.AppInSnap.request.LoanManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.AppInSnap.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class LoanRequest extends Request {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("FatherName")
    private String fatherName;
    @JsonProperty("CNIC")
    private String cnic;
    @JsonProperty("AccountNo")
    private String accountNo;
    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("Amount")
    private int amount;

    private String apinSnapUserName = PropertyReader.getProperty("apinsnap.userName");
    private String apinSnapPassword = PropertyReader.getProperty("apinsnap.password");


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setAccountNo(i8SBSwitchControllerRequestVO.getAccountNumber());
        this.setName(i8SBSwitchControllerRequestVO.getConsumerNickName());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setFatherName(i8SBSwitchControllerRequestVO.getFatherName());
        this.setAmount(Integer.parseInt(i8SBSwitchControllerRequestVO.getAmount()));
        this.setUserName(apinSnapUserName);
        this.setPassword(apinSnapPassword);

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getAccountNo())) {
            throw new I8SBValidationException("[Failed AccountNo] " + this.getAccountNo());
        } else if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed CNIC]" + this.getCnic());
        } else if (StringUtils.isEmpty(this.getName())) {
            throw new I8SBValidationException("[Failed Consumer Name]" + this.getName());
        }
        return true;
    }
}
