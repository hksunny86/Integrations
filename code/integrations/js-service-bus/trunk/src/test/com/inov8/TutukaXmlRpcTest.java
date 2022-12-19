package com.inov8;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class TutukaXmlRpcTest {
    @Test
    public void deduct() throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:8090/I8SB/remote/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);


        Object[] params = new Object[]{"0023687248", "923454677743", 2000, "Test Shop              Punjab        PK ", "00", "0260460110520025004ECOM25110MasterCard252010253045590", "185926", new Date(), "3C085840D990DF102AC67648A035AA5C609570E6"};


        try {
            HashMap<String, Object> response = null;
            response = (HashMap<String, Object>) client.execute("Deduct", params);
            System.out.println(response);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void loadAdjustment() throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:9090/I8SB/remote/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);


        Object[] params = new Object[]{"0023687248", "923454677743", 2000, "Test Shop              Punjab        PK ", "0260460110520025004ECOM25110MasterCard252010253045590", "185926", new Date(), "3C085840D990DF102AC67648A035AA5C609570E6", new Date(), "D47E2961C01F4265FFA46F2E0D59009AFC517982"};


        try {
            HashMap<String, Object> response = null;
            response = (HashMap<String, Object>) client.execute("LoadReversal", params);
            System.out.println(response);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }


    }
}
