package com.inov8.integration.middleware.controller;


import com.inov8.integration.vo.NadraIntegrationVO;

import java.io.Serializable;

/**
 * Created by Zeeshan Ahmad on
 * 3/30/2016.
 */
public interface NadraIntegrationController extends Serializable {


    NadraIntegrationVO fingerPrintVerification(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getSecretIdentityDemographicsData(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getManualVerificationResult(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getLastVerificationResult(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO submitMobileBankAccountDetail(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO otcFingerPrintVerification(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getOtcSecretIdentityDemographicsData(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getOtcManualVerificationResult(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getOtcLastVerificationResult(NadraIntegrationVO messageVO) throws Exception;

    NadraIntegrationVO getCitizenData(NadraIntegrationVO messageVO) throws Exception;


}
