
package com.inov8.integration.middleware.abl.client;

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
 * 2018-01-19T16:31:33.015+05:00
 * Generated source version: 3.1.6
 * 
 */
public final class IService1_BasicHttpBindingIService1_Client {

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "Service1");

    private IService1_BasicHttpBindingIService1_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = Service1.WSDL_LOCATION;
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
      
        Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
        IService1 port = ss.getBasicHttpBindingIService1();  
        
        {
        System.out.println("Invoking fundsTransfer...");
        java.lang.String _fundsTransfer_pan = "";
        java.lang.String _fundsTransfer_sFrmAcc = "";
        java.lang.String _fundsTransfer_sFrmAccType = "";
        java.lang.String _fundsTransfer_sToAcc = "";
        java.lang.String _fundsTransfer_sToAccType = "";
        java.lang.String _fundsTransfer_amount = "";
        java.lang.String _fundsTransfer_rrn = "";
        java.lang.String _fundsTransfer_sNarration = "";
        java.lang.String _fundsTransfer_originalTransactionDateTime = "";
        java.lang.String _fundsTransfer_commission = "";
        java.lang.String _fundsTransfer__return = port.fundsTransfer(_fundsTransfer_pan, _fundsTransfer_sFrmAcc, _fundsTransfer_sFrmAccType, _fundsTransfer_sToAcc, _fundsTransfer_sToAccType, _fundsTransfer_amount, _fundsTransfer_rrn, _fundsTransfer_sNarration, _fundsTransfer_originalTransactionDateTime, _fundsTransfer_commission);
        System.out.println("fundsTransfer.result=" + _fundsTransfer__return);


        }
        {
        System.out.println("Invoking ftReversal...");
        java.lang.String _ftReversal_pan = "";
        java.lang.String _ftReversal_sFrmAcc = "";
        java.lang.String _ftReversal_sFrmAccType = "";
        java.lang.String _ftReversal_sToAcc = "";
        java.lang.String _ftReversal_sToAccType = "";
        java.lang.String _ftReversal_amount = "";
        java.lang.String _ftReversal_rrn = "";
        java.lang.String _ftReversal_sNarration = "";
        java.lang.String _ftReversal_originalSTAN = "";
        java.lang.String _ftReversal_currentTransactionDateTime = "";
        java.lang.String _ftReversal_originalTransactionDateTime = "";
        java.lang.String _ftReversal_originalTransmittionDateTime = "";
        java.lang.String _ftReversal_commission = "";
        java.lang.String _ftReversal__return = port.ftReversal(_ftReversal_pan, _ftReversal_sFrmAcc, _ftReversal_sFrmAccType, _ftReversal_sToAcc, _ftReversal_sToAccType, _ftReversal_amount, _ftReversal_rrn, _ftReversal_sNarration, _ftReversal_originalSTAN, _ftReversal_currentTransactionDateTime, _ftReversal_originalTransactionDateTime, _ftReversal_originalTransmittionDateTime, _ftReversal_commission);
        System.out.println("ftReversal.result=" + _ftReversal__return);


        }
        {
        System.out.println("Invoking custAcctDetail...");
        java.lang.String _custAcctDetail_userid = "";
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_cnic = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_custname = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_email = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_mobno = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_iban = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_acctcurreny = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_currenymnemonic = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_availablebalance = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_currentbalance = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_accttype = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_branchcode = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_branchname = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_accoutname = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _custAcctDetail_bankimd = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<com.inov8.integration.middleware.abl.client.CustAcctDetailResponse.ACCNO> _custAcctDetail_accno = new javax.xml.ws.Holder<com.inov8.integration.middleware.abl.client.CustAcctDetailResponse.ACCNO>();
        port.custAcctDetail(_custAcctDetail_userid, _custAcctDetail_cnic, _custAcctDetail_custname, _custAcctDetail_email, _custAcctDetail_mobno, _custAcctDetail_iban, _custAcctDetail_acctcurreny, _custAcctDetail_currenymnemonic, _custAcctDetail_availablebalance, _custAcctDetail_currentbalance, _custAcctDetail_accttype, _custAcctDetail_branchcode, _custAcctDetail_branchname, _custAcctDetail_accoutname, _custAcctDetail_bankimd, _custAcctDetail_accno);

        System.out.println("custAcctDetail._custAcctDetail_cnic=" + _custAcctDetail_cnic.value);
        System.out.println("custAcctDetail._custAcctDetail_custname=" + _custAcctDetail_custname.value);
        System.out.println("custAcctDetail._custAcctDetail_email=" + _custAcctDetail_email.value);
        System.out.println("custAcctDetail._custAcctDetail_mobno=" + _custAcctDetail_mobno.value);
        System.out.println("custAcctDetail._custAcctDetail_iban=" + _custAcctDetail_iban.value);
        System.out.println("custAcctDetail._custAcctDetail_acctcurreny=" + _custAcctDetail_acctcurreny.value);
        System.out.println("custAcctDetail._custAcctDetail_currenymnemonic=" + _custAcctDetail_currenymnemonic.value);
        System.out.println("custAcctDetail._custAcctDetail_availablebalance=" + _custAcctDetail_availablebalance.value);
        System.out.println("custAcctDetail._custAcctDetail_currentbalance=" + _custAcctDetail_currentbalance.value);
        System.out.println("custAcctDetail._custAcctDetail_accttype=" + _custAcctDetail_accttype.value);
        System.out.println("custAcctDetail._custAcctDetail_branchcode=" + _custAcctDetail_branchcode.value);
        System.out.println("custAcctDetail._custAcctDetail_branchname=" + _custAcctDetail_branchname.value);
        System.out.println("custAcctDetail._custAcctDetail_accoutname=" + _custAcctDetail_accoutname.value);
        System.out.println("custAcctDetail._custAcctDetail_bankimd=" + _custAcctDetail_bankimd.value);
        System.out.println("custAcctDetail._custAcctDetail_accno=" + _custAcctDetail_accno.value);

        }
        {
        System.out.println("Invoking billPayment...");
        java.lang.String _billPayment_customerID = "";
        java.lang.String _billPayment_utilityComapanyId = "";
        java.lang.String _billPayment_consumerNo = "";
        java.lang.String _billPayment_amountpaid = "";
        java.lang.String _billPayment_fromAccount = "";
        java.lang.String _billPayment_fromAccountType = "";
        java.lang.String _billPayment_rrn = "";
        java.lang.String _billPayment__return = port.billPayment(_billPayment_customerID, _billPayment_utilityComapanyId, _billPayment_consumerNo, _billPayment_amountpaid, _billPayment_fromAccount, _billPayment_fromAccountType, _billPayment_rrn);
        System.out.println("billPayment.result=" + _billPayment__return);


        }
        {
        System.out.println("Invoking billInquiry...");
        java.lang.String _billInquiry_customerID = "";
        java.lang.String _billInquiry_utilityComapanyId = "";
        java.lang.String _billInquiry_consumerNo = "";
        java.lang.String _billInquiry_rrn = "";
        java.lang.String _billInquiry__return = port.billInquiry(_billInquiry_customerID, _billInquiry_utilityComapanyId, _billInquiry_consumerNo, _billInquiry_rrn);
        System.out.println("billInquiry.result=" + _billInquiry__return);


        }

        System.exit(0);
    }

}
