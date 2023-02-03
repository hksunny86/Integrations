
package com.inov8.integration.middleware.abl.mobapp;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2017-10-11T05:10:37.625-07:00
 * Generated source version: 3.1.6
 * 
 */
public final class IABLMobileAppService_BasicHttpBindingIABLMobileAppService_Client {

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "ABLMobileAppService");

    private IABLMobileAppService_BasicHttpBindingIABLMobileAppService_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = ABLMobileAppService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ABLMobileAppService ss = new ABLMobileAppService(wsdlURL, SERVICE_NAME);
        IABLMobileAppService port = ss.getBasicHttpBindingIABLMobileAppService();  
        
        {
        System.out.println("Invoking login...");
        com.inov8.integration.middleware.abl.mobapp.InputHeader _login_inputHeader = null;
        java.lang.String _login_customerUserName = "";
        java.lang.String _login_subscriptionCheck = "";
        com.inov8.integration.middleware.abl.mobapp.LoginOutputParams _login__return = port.login(_login_inputHeader, _login_customerUserName, _login_subscriptionCheck);
        System.out.println("login.result=" + _login__return);


        }
        {
        System.out.println("Invoking verifyPassword...");
        com.inov8.integration.middleware.abl.mobapp.InputHeader _verifyPassword_inputHeader = null;
        java.lang.String _verifyPassword_customerUserName = "";
        java.lang.String _verifyPassword_passwordPattern = "";
        java.lang.String _verifyPassword_customerPassword = "";
        com.inov8.integration.middleware.abl.mobapp.PasswordOutputParams _verifyPassword__return = port.verifyPassword(_verifyPassword_inputHeader, _verifyPassword_customerUserName, _verifyPassword_passwordPattern, _verifyPassword_customerPassword);
        System.out.println("verifyPassword.result=" + _verifyPassword__return);


        }
        {
        System.out.println("Invoking getAccounts...");
        com.inov8.integration.middleware.abl.mobapp.InputHeader _getAccounts_inputHeader = null;
        java.lang.String _getAccounts_customerUserName = "";
        java.lang.String _getAccounts_cnic = "";
        com.inov8.integration.middleware.abl.mobapp.GetAccountsOutputParams _getAccounts__return = port.getAccounts(_getAccounts_inputHeader, _getAccounts_customerUserName, _getAccounts_cnic);
        System.out.println("getAccounts.result=" + _getAccounts__return);


        }

        System.exit(0);
    }

}