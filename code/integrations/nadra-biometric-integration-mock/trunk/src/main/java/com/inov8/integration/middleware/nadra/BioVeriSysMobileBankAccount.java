package com.inov8.integration.middleware.nadra;

import com.inov8.integration.middleware.mock.MockService;
import com.inov8.integration.middleware.nadra.biometric.IBioVeriSysMobileBankAccount;
import com.inov8.integration.middleware.nadra.biometric.TemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.jws.WebService;

@WebService(
        targetNamespace = "http://tempuri.org/",
        name = "BioVeriSysMobileBankAccount"
)
@Controller("BioVeriSysMobileBankAccount")
public class BioVeriSysMobileBankAccount implements IBioVeriSysMobileBankAccount {

    @Autowired
    private MockService mockService;


    public String verifyFingerPrints(String franchizeID, String xmlRequestData) {
        return mockService.verifyFingerPrints(xmlRequestData);
    }

    public String getCitizenIdentityDemographicsData(String franchizeID, String xmlRequestData) {

        return mockService.identityDemographics(xmlRequestData);
    }

    public String submitManualVerificationResults(String franchizeID, String xmlRequestData) {

        return mockService.submitManualVerificationResults(xmlRequestData);
    }
    public String getLastVerificationResults(String franchizeID, String xmlRequestData) {

        return mockService.lastVerificationResult(xmlRequestData);
    }
    public String submitBankAccountDetails(String franchizeID, String xmlRequestData) {

        return mockService.submitMobileBankAccountDetails(xmlRequestData);
    }
    public String getCitizenData(String franchizeID, String xmlRequestData) {

        return mockService.citizenData(xmlRequestData);
    }
    @Override
    public String verifyPhotograph(String s, String s1) {
        return null;
    }

    @Override
    public String testService(TemplateType templateType, String s) {
        return null;
    }



}
