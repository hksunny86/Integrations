package com.inov8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inov8.integration.exception.I8SBServiceNotAvailableException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

public class DobilloXmlRpcTest {

    @Test
    public void handleBillPaymentRequestTest() throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://192.168.1.112:8070/I8SB_war_exploded/remote/billPayment/api"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        //Request for bill Inquiry
//        HashMap<String,Object> RequestMap = new HashMap();
//        RequestMap.put("RequestType", "BillInquiry");
//        RequestMap.put("ClientId","dobiloClient");
//        RequestMap.put("TerminalId","dobiloTerminal");
//        RequestMap.put("ChannelId","dobiloChannel");
//        RequestMap.put("RequestDateTime",new Date());
//        RequestMap.put("RRN","12345678");
//        RequestMap.put("UserName","dobilo");
//        RequestMap.put("Password","abcd12345");
//        RequestMap.put("CompanyName","TestCompany");
//        RequestMap.put("ProductId",2510713);
//        RequestMap.put("ConsumerNumber","123456789017759");
//        RequestMap.put("CurrencyCode","USD");
//        RequestMap.put("CheckSum","1223");

        //REquest for bill payment
//        HashMap<String,Object> RequestMap = new HashMap();
//        RequestMap.put("RequestType", "BillPayment");
//        RequestMap.put("ClientId","dobiloClient");
//        RequestMap.put("TerminalId","dobiloTerminal");
//        RequestMap.put("ChannelId","dobiloChannel");
//        RequestMap.put("RequestDateTime",new Date());
//        RequestMap.put("RRN","12345678");
//        RequestMap.put("UserName","dobilo");
//        RequestMap.put("Password","abcd12345");
//        RequestMap.put("CompanyName","TestCompany");
//        RequestMap.put("ProductId",2510713);
//        RequestMap.put("ConsumerNumber","123456789017759");
//        RequestMap.put("CurrencyCode","USD");
//        RequestMap.put("CheckSum","1223");
//        RequestMap.put("BillAmount","400.523");

        // Request for bill status
        HashMap<String,Object> RequestMap = new HashMap();
        RequestMap.put("RequestType", "BillStatus");
        RequestMap.put("ClientId","dobiloClient");
        RequestMap.put("TerminalId","dobiloTerminal");
        RequestMap.put("ChannelId","dobiloChannel");
        RequestMap.put("RequestDateTime",new Date());
        RequestMap.put("RRN","12345678");
        RequestMap.put("UserName","dobilo");
        RequestMap.put("Password","abcd12345");
        RequestMap.put("CompanyName","TestCompany");
        RequestMap.put("ProductId",2510713);    //I need this to query in database
        RequestMap.put("ConsumerNumber","123456789017759");
        RequestMap.put("CurrencyCode","USD");
        RequestMap.put("CheckSum","1223");
        RequestMap.put("DueDate","200312");

        HashMap<String,Object>[] ObjectMap = new HashMap[]{RequestMap};

        try {
            HashMap<String, Object> response = null;
            response = (HashMap<String, Object>) client.execute("handleBillPaymentRequest", ObjectMap);
            System.out.println(response);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }


    }

 /*   public String logCompleteObject(String desc, Object obj) {
        String jsonRequestString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            jsonRequestString = mapper.writeValueAsString(obj);
            logger.info("\n\n***********\n** " + desc + " **\n" + jsonRequestString + "\n***********");
        } catch (Exception e) {
            logger.error("Exception occurred while printing log... Exception details:", e);
        }
        return jsonRequestString;
    }*/

    @Test
    public void SendDoBilloRequestTest() throws Exception
    {
        HashMap<String,Object> RequestMap = new HashMap();
        RequestMap.put("RequestType", "BillPayment");
        RequestMap.put("ClientId","c123");
        RequestMap.put("TerminalId","t123");
        RequestMap.put("ChannelId","c123");
        RequestMap.put("RequestDateTime",new Date());
        RequestMap.put("RRN","12345678");
        RequestMap.put("UserName","test");
        RequestMap.put("Password","pswd");
        RequestMap.put("CompanyName","TestCompany");
        RequestMap.put("ProductID",2510713);
        RequestMap.put("ConsumerNumber","123456789017759");
        RequestMap.put("CurrencyCode","USD");
        RequestMap.put("CheckSum ","1223");
        RequestMap.put("BillAmount  ","2000");

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        i8SBSwitchControllerRequestVO.setCollectionOfList(RequestMap);
        //i8SBSwitchControllerRequestVO.setRequestType("BillInquiry");
        I8SBSwitchController controller;
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO;
        long startTime = new Date().getTime(); // start time
        controller = CommonUtils.getFromProxy("http://172.29.12.83:8080/i8Microbank/ws/i8sb"); //i8Microbank URL
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        //logger.info("microbanUrl"+ "http://172.29.12.88:8080/i8Microbank/ws/i8sb");//dobillo url
        //logger.info("Sending request to Microbank server for RRN: " + rrn);
        try {
            i8SBSwitchControllerRequestVO = (I8SBSwitchControllerRequestVO) controller.invoke(i8SBSwitchControllerRequestVO);
            i8SBSwitchControllerResponseVO = i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO();

            System.out.println(i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO().getCollectionOfList());
        } catch (Exception e) {
          //  logger.debug("Microbank is not connected" + e.getMessage());
            throw new I8SBServiceNotAvailableException("Microbank is not connected");
        }
       // logger.info("Response Received from Microbank server for RRN: " + rrn);
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        //logger.debug("Response time from Microbank is " + difference + " milliseconds");
//        i8SBSwitchControllerResponseVO = i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO();
    }
}
