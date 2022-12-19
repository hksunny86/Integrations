package com.inov8.integration.middleware.nadra;

import com.inov8.integration.middleware.mock.MockService;
import com.inov8.integration.middleware.nadra.biometric.IBioVeriSysMobileBankAccount;
import com.inov8.integration.middleware.nadra.otc.TemplateType;
import com.inov8.integration.middleware.nadra.otc.IBioVeriSysOTC;
import com.inov8.integration.middleware.nadra.otc.RemittanceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.jws.WebService;

@WebService(
        targetNamespace = "http://tempuri.org/",
        name = "IBioVeriSysOTC"
)
@Controller("IBioVeriSysOTC")
public class BioVerifySysOtc implements IBioVeriSysOTC {

    @Autowired
    private MockService mockService;


    public String verifyFingerPrints(String franchizeID, String xmlRequestData) {
        return mockService.otcVerifyFingerPrints(xmlRequestData);
    }

    public String getCitizenIdentityDemographicsData(String franchizeID, String xmlRequestData) {

        return mockService.otcIdentityDemographics(xmlRequestData);
    }

    public String submitManualVerificationResults(String franchizeID, String xmlRequestData) {

        return mockService.otcSubmitManualVerificationResults(xmlRequestData);
    }
    public String getLastVerificationResults(String franchizeID, String xmlRequestData) {

        return mockService.otcLastVerificationResult(xmlRequestData);
    }

    @Override
    public String verifyPhotograph(String s, String s1) {
        return null;
    }
    @Override
    public String testService(TemplateType templateType, RemittanceType remittanceType, String s) {
        return null;
    }


}