package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.M3TechIntegrationVO;

import java.io.Serializable;

/**
 * Created by Zeeshan Ahmad on 6/21/2016.
 */
public interface M3TechIntegrationController extends Serializable {

    public M3TechIntegrationVO sendSMS(M3TechIntegrationVO integrationVO) throws Exception;
}
