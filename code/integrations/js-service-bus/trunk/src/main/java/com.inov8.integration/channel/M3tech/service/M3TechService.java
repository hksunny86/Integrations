package com.inov8.integration.channel.M3tech.service;

import com.inov8.integration.channel.M3tech.Request.SendSmsRequest;
import com.inov8.integration.channel.M3tech.Response.SendSms;
import com.inov8.integration.channel.M3tech.client.WebService40Soap;
import com.inov8.integration.exception.I8SBValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.soap.SOAPException;

@Service
public class M3TechService {

    private static Logger logger = LoggerFactory.getLogger(M3TechService.class.getSimpleName());
    @Autowired(required = false)
    @Qualifier("M3TechService")
    private WebService40Soap webService40;

    public SendSms sendSms(SendSmsRequest request) throws SOAPException {
        SendSms sendSms = new SendSms();
        String response = null;
        response = webService40.sendSMS(request.getUserId(),
                request.getPassword(),
                request.getMobileNo(),
                request.getMsgId(),
                request.getSms(),
                request.getMsgHeader(),
                request.getSmsType(),
                request.getSmsChannel(),
                request.getTelco(),
                request.getHandsetPort());
        logger.info("Response received from client");
        if (response != null) {
            sendSms.setSendSMSResult(response);
        } else {
            throw new I8SBValidationException("Response Not Recieve from Client");
        }
        return sendSms;
    }

}
