
package com.inov8.integration.middleware.novatti;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.1.3
 * 2015-10-16T11:28:15.830+05:00
 * Generated source version: 3.1.3
 * 
 */
public final class AgentServiceIF_AgentServiceIFPort_Client {

    private static final QName SERVICE_NAME = new QName("http://soap.api.novatti.com/service", "AgentService");

    private AgentServiceIF_AgentServiceIFPort_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = AgentService.WSDL_LOCATION;
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
      
        AgentService ss = new AgentService(wsdlURL, SERVICE_NAME);
        AgentServiceIF port = ss.getAgentServiceIFPort();  
        
        {
        System.out.println("Invoking getSystemStatus...");
        SoapSystemStatusRequest _getSystemStatus_systemStatusRequest = null;
        SoapSystemStatusResponse _getSystemStatus__return = port.getSystemStatus(_getSystemStatus_systemStatusRequest);
        System.out.println("getSystemStatus.result=" + _getSystemStatus__return);


        }
        {
        System.out.println("Invoking agentChangePassword...");
        SoapAgentChangePasswordRequest _agentChangePassword_agentChangePasswordRequest = null;
        SoapAgentChangePasswordResponse _agentChangePassword__return = port.agentChangePassword(_agentChangePassword_agentChangePasswordRequest);
        System.out.println("agentChangePassword.result=" + _agentChangePassword__return);


        }
        {
        System.out.println("Invoking getAgentStructure...");
        SoapAgentStructureRequest _getAgentStructure_agentStructureRequest = null;
        SoapAgentStructureResponse _getAgentStructure__return = port.getAgentStructure(_getAgentStructure_agentStructureRequest);
        System.out.println("getAgentStructure.result=" + _getAgentStructure__return);


        }
        {
        System.out.println("Invoking getAgentProducts...");
        SoapAgentProductsRequest _getAgentProducts_agentProductsRequest = null;
        SoapAgentProductsResponse _getAgentProducts__return = port.getAgentProducts(_getAgentProducts_agentProductsRequest);
        System.out.println("getAgentProducts.result=" + _getAgentProducts__return);


        }
        {
        System.out.println("Invoking getAgentDocuments...");
        SoapDocumentsInfoRequest _getAgentDocuments_documentsInfoRequest = null;
        SoapDocumentsInfoResponse _getAgentDocuments__return = port.getAgentDocuments(_getAgentDocuments_documentsInfoRequest);
        System.out.println("getAgentDocuments.result=" + _getAgentDocuments__return);


        }
        {
        System.out.println("Invoking agentLogin...");
        SoapAgentLoginRequest _agentLogin_agentLoginReques = null;
        SoapAgentLoginResponse _agentLogin__return = port.agentLogin(_agentLogin_agentLoginReques);
        System.out.println("agentLogin.result=" + _agentLogin__return);


        }
        {
        System.out.println("Invoking getAgentAddresses...");
        SoapAgentAddressesRequest _getAgentAddresses_agentAddressesRequest = null;
        SoapAgentAddressesResponse _getAgentAddresses__return = port.getAgentAddresses(_getAgentAddresses_agentAddressesRequest);
        System.out.println("getAgentAddresses.result=" + _getAgentAddresses__return);


        }
        {
        System.out.println("Invoking getAgentSettlements...");
        SoapAgentSettlementRequest _getAgentSettlements_agentSettlementRequest = null;
        SoapAgentSettlementResponse _getAgentSettlements__return = port.getAgentSettlements(_getAgentSettlements_agentSettlementRequest);
        System.out.println("getAgentSettlements.result=" + _getAgentSettlements__return);


        }
        {
        System.out.println("Invoking agentWalletTransfer...");
        SoapAgentWalletTransferRequest _agentWalletTransfer_agentWalletTransferRequest = null;
        SoapAgentWalletTransferResponse _agentWalletTransfer__return = port.agentWalletTransfer(_agentWalletTransfer_agentWalletTransferRequest);
        System.out.println("agentWalletTransfer.result=" + _agentWalletTransfer__return);


        }
        {
        System.out.println("Invoking agentCreate...");
        SoapAgentCreateRequest _agentCreate_agentCreateRequest = null;
        SoapAgentCreateResponse _agentCreate__return = port.agentCreate(_agentCreate_agentCreateRequest);
        System.out.println("agentCreate.result=" + _agentCreate__return);


        }
        {
        System.out.println("Invoking agentSearch...");
        SoapAgentSearchRequest _agentSearch_agentSearchRequest = null;
        SoapAgentSearchResponse _agentSearch__return = port.agentSearch(_agentSearch_agentSearchRequest);
        System.out.println("agentSearch.result=" + _agentSearch__return);


        }
        {
        System.out.println("Invoking agentUpdate...");
        SoapAgentUpdateRequest _agentUpdate_agentUpdateRequest = null;
        SoapAgentUpdateResponse _agentUpdate__return = port.agentUpdate(_agentUpdate_agentUpdateRequest);
        System.out.println("agentUpdate.result=" + _agentUpdate__return);


        }
        {
        System.out.println("Invoking getAgentTemplates...");
        SoapAgentTemplateRequest _getAgentTemplates_agentTemplateRequest = null;
        SoapAgentTemplateResponse _getAgentTemplates__return = port.getAgentTemplates(_getAgentTemplates_agentTemplateRequest);
        System.out.println("getAgentTemplates.result=" + _getAgentTemplates__return);


        }
        {
        System.out.println("Invoking sessionValidate...");
        SoapSessionValidateRequest _sessionValidate_sessionValidateRequest = null;
        SoapSessionValidateResponse _sessionValidate__return = port.sessionValidate(_sessionValidate_sessionValidateRequest);
        System.out.println("sessionValidate.result=" + _sessionValidate__return);


        }
        {
        System.out.println("Invoking getBulletins...");
        SoapBulletinInfoRequest _getBulletins_bulletinInfoRequest = null;
        SoapBulletinInfoResponse _getBulletins__return = port.getBulletins(_getBulletins_bulletinInfoRequest);
        System.out.println("getBulletins.result=" + _getBulletins__return);


        }
        {
        System.out.println("Invoking agentChangeAuthkey...");
        SoapAgentChangeAuthkeyRequest _agentChangeAuthkey_agentChangeAuthkeyRequest = null;
        SoapAgentChangeAuthkeyResponse _agentChangeAuthkey__return = port.agentChangeAuthkey(_agentChangeAuthkey_agentChangeAuthkeyRequest);
        System.out.println("agentChangeAuthkey.result=" + _agentChangeAuthkey__return);


        }
        {
        System.out.println("Invoking getAgentProfile...");
        SoapAgentProfileRequest _getAgentProfile_agentProfileRequest = null;
        SoapAgentProfileResponse _getAgentProfile__return = port.getAgentProfile(_getAgentProfile_agentProfileRequest);
        System.out.println("getAgentProfile.result=" + _getAgentProfile__return);


        }
        {
        System.out.println("Invoking getAgentInfo...");
        SoapAgentInfoRequest _getAgentInfo_agentInfoRequest = null;
        SoapAgentInfoResponse _getAgentInfo__return = port.getAgentInfo(_getAgentInfo_agentInfoRequest);
        System.out.println("getAgentInfo.result=" + _getAgentInfo__return);


        }

        System.exit(0);
    }

}
