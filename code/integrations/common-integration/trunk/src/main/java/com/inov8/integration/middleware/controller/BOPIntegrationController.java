package com.inov8.integration.middleware.controller;

import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

import java.io.Serializable;

/**
 * Created by Zeeshan Ahmad on 6/21/2016.
 */
public interface BOPIntegrationController extends Serializable {

    public PhoenixIntegrationMessageVO sendSMS(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO checkRegistration(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO channelActivation(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO customerEmailUpdateRequest(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO customerEmailVerificationRequest(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO viewAccountStatementRequest(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO emailAccountStatementRequest(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public PhoenixIntegrationMessageVO IExternalCommunicationRequest(PhoenixIntegrationMessageVO integrationVO) throws Exception;
    public  PhoenixIntegrationMessageVO checkBvsStatusOnNadra(PhoenixIntegrationMessageVO middlewareMessageVO) throws RuntimeException;

}
