package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.PITBIntegrationVO;
import java.io.Serializable;

/**
 * Created by inov8 on 10/25/2016.
 */
public interface IPITBController extends Serializable {

    public PITBIntegrationVO beneficiaryRegistration(PITBIntegrationVO integrationVO);

    public PITBIntegrationVO transaction(PITBIntegrationVO integrationVO);

    public PITBIntegrationVO permanentCardBlock(PITBIntegrationVO integrationVO);

    public PITBIntegrationVO getBeneficiaries(PITBIntegrationVO integrationVO);

    public PITBIntegrationVO postBeneficiaries(PITBIntegrationVO integrationVO);
}
