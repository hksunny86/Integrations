package com.inov8.integration.channel.CLSJS.service;
import com.inov8.integration.channel.CLSJS.client.ScreenMSPortType;
import com.inov8.integration.channel.CLSJS.mock.CLSJSMock;
import com.inov8.integration.channel.CLSJS.request.ImportScreeningRequest;
import com.inov8.integration.channel.CLSJS.response.ScreeningResponse;
import com.inov8.integration.channel.CLSJS.client.ImportScreeningResponse;
import com.inov8.integration.exception.I8SBValidationException;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.soap.SOAPException;


@Service
public class CLSJSService {

    @Autowired(required = false)
    @Qualifier("CLSJSWebService")
    ScreenMSPortType screenMSPortType;

    @Value("${i8sb.target.environment:#{null}}")
    private String i8sb_target_environment;

    private CLSJSMock clsjsMock = new CLSJSMock();
    private static Logger logger = LoggerFactory.getLogger(CLSJSService.class.getSimpleName());

    public ScreeningResponse importScreeningResponse(ImportScreeningRequest request) throws SOAPException {

        ScreeningResponse importScreeningResponse = new ScreeningResponse();
        com.inov8.integration.channel.CLSJS.client.ImportScreening importScreeningRequest = new com.inov8.integration.channel.CLSJS.client.ImportScreening();
        ImportScreeningResponse response = new ImportScreeningResponse();

        importScreeningRequest.setRequestID(request.getRequestID());
        importScreeningRequest.setCNIC(request.getCnic());
        importScreeningRequest.setCustomerName(request.getCustomerName());
        importScreeningRequest.setFatherName(request.getFatherName());
        importScreeningRequest.setDateOfBirth(request.getDateOfBirth());
        importScreeningRequest.setNationality(request.getNationality());
        importScreeningRequest.setCity(request.getCity());
        importScreeningRequest.setCustomerNumber(request.getCustomerNumber());
        importScreeningRequest.setUserId(request.getUserId());

        if (i8sb_target_environment.equals("mock")) {

            importScreeningResponse = clsjsMock.importScreeningResponse(importScreeningRequest);
        } else {

//            HTTPConduit httpConduit=(HTTPConduit) ClientProxy.getClient(SAMPLE_PORT).getConduit();
//            TLSClientParameters tlsCP = new TLSClientParameters();
//            tlsCP.setDisableCNCheck(true);
//            httpConduit.setTlsClientParameters(tlsCP);

            response = screenMSPortType.screenOperation(importScreeningRequest);
            logger.info("Response received from client");
            if (response != null) {
                importScreeningResponse.setRequestID(response.getRequestID());
                importScreeningResponse.setCaseId(response.getCaseId());
                importScreeningResponse.setCaseStatus(response.getCaseStatus());
                importScreeningResponse.setImportStatus(response.getImportStatus());
                importScreeningResponse.setIsHit(response.getIsHit());
                importScreeningResponse.setScreeningStatus(response.getScreeningStatus());
                importScreeningResponse.setTotalCWL(response.getTotalCWL());
                importScreeningResponse.setTotalGWL(response.getTotalGWL());
                importScreeningResponse.setTotalPEPEDD(response.getTotalPEPEDD());
                importScreeningResponse.setTotalPrivate(response.getTotalPrivate());

            } else {
                throw new I8SBValidationException("Response Not Recieve from Client");
            }
        }
        return importScreeningResponse;
    }
}
