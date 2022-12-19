package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.handler;

import com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.bo.SCOUSSDBO;
import com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.services.RPCHandler;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Service("uSSDRequestHandler")
public class USSDRequestHandler implements RPCHandler {

    private static Logger logger = LoggerFactory.getLogger(USSDRequestHandler.class.getSimpleName());
    @Autowired
    private SCOUSSDBO seoUSSDBO;

    public LinkedHashMap<String, Object> handleUSSDRequest(HashMap<String, Object> hashMap) {

        logger.info("I8SB received request on XMLRPC gateway");
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        String TransactionId = hashMap.get("TransactionId").toString();
        String TransactionTime  = hashMap.get("TransactionTime").toString();
        String MSISDN = hashMap.get("MSISDN").toString();
        String USSDServiceCode = hashMap.get("USSDServiceCode").toString();
        String USSDRequestString = hashMap.get("USSDRequestString").toString();
        String Response = hashMap.get("response").toString();

        logger.info("TransactionID: "+ TransactionId);
        logger.info("TransactionTime: "+ TransactionTime);
        logger.info("MSISDN:"+ MSISDN);
        logger.info("USSDServiceCode:" + USSDServiceCode);
        logger.info("USSDRequestString: "+ USSDRequestString);
//        String Location = hashMap.get("Location").toString();
//        String Vlr = hashMap.get("Vlr").toString()

        i8SBSwitchControllerRequestVO.setTransactionId(TransactionId);
        i8SBSwitchControllerRequestVO.setTransactionDateTime(TransactionTime);
        if(MSISDN.equals("923555494525"))
            i8SBSwitchControllerRequestVO.setMobileNumber("03024292857");
        else
        i8SBSwitchControllerRequestVO.setMobileNumber(MSISDN);
        i8SBSwitchControllerRequestVO.setuSSDRequestString(USSDRequestString);

        i8SBSwitchControllerRequestVO.setuSSDServiceCode(USSDServiceCode);
        i8SBSwitchControllerRequestVO.setuSSDServiceCode("*788#");
        i8SBSwitchControllerRequestVO.setuSSDResponseCode(Response);

//        i8SBSwitchControllerRequestVO.setLocation(Location);
//        i8SBSwitchControllerRequestVO.setVlrNumber(Vlr);

        long startTime = new Date().getTime(); // start time

        try
        {
            logger.info("Executing SEO USSDBO...");
            i8SBSwitchControllerResponseVO = seoUSSDBO.execute(i8SBSwitchControllerRequestVO);

            logger.info("Received Response from  USSDBO...");
            String USSDResposneString = i8SBSwitchControllerResponseVO.getuSSDResponseString();
            String USSDaction = i8SBSwitchControllerResponseVO.getuSSDAction();
            result.put("TransactionId",TransactionId);
            result.put("TransactionTime",new DateTime());
            result.put("USSDResponseString",USSDResposneString);
            result.put("action",USSDaction);

        }

        catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different



        return result;

    }



}
