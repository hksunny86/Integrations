package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.EasyPayIntegrationVO;
import com.inov8.integration.vo.TelenorSMSIntegrationVO;

import java.io.Serializable;

public interface TelenorSMSSwitchController extends Serializable {


    TelenorSMSIntegrationVO getSession(TelenorSMSIntegrationVO integrationVO) throws RuntimeException;
    TelenorSMSIntegrationVO sendMessage(TelenorSMSIntegrationVO integrationVO) throws RuntimeException;
    TelenorSMSIntegrationVO queryMessage(TelenorSMSIntegrationVO integrationVO) throws RuntimeException;
    TelenorSMSIntegrationVO ping(TelenorSMSIntegrationVO integrationVO) throws RuntimeException;

}
