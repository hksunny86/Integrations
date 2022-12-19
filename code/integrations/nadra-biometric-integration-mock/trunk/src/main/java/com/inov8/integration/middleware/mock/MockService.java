package com.inov8.integration.middleware.mock;

import com.inov8.integration.middleware.nadra.FingerPrintVerificationService;
import com.inov8.integration.middleware.util.XMLUtil;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.integration.middleware.mock.model.JDBCUser;
import com.inov8.integration.middleware.nadra.pdu.*;

import com.inov8.integration.middleware.prisim.CustomerDetail;
import com.inov8.integration.middleware.prisim.CustomerRequestOutputParams;
import com.inov8.integration.middleware.prisim.InputHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;


@Service
public class MockService implements Serializable {

    @Autowired
    private MockTransactionDAO transactionDAO;
    @Autowired
    FingerPrintVerificationService fingerService;

    private static Logger logger = (Logger) LoggerFactory.getLogger(MockService.class.getSimpleName());


    public boolean saveResponse(NadraIntegrationVO nadraIntegrationVO, String fingerPrintTemplate) {
//       Boolean result= transactionDAO.saveResponse(nadraIntegrationVO,fingerPrintTemplate);
//        return result;
        return false;
    }

    public NadraIntegrationVO getCustomerData(String searchCnic) {
        NadraIntegrationVO nadraIntegrationVO = new NadraIntegrationVO();

        JDBCUser result = transactionDAO.getCustomerData(searchCnic);
        if (result != null) {
            nadraIntegrationVO.setResponseCode(result.getResponseCode());
            nadraIntegrationVO.setResponseDescription(result.getMessage());
            nadraIntegrationVO.setSessionId(result.getSessionId());
            nadraIntegrationVO.setCitizenNumber(result.getCNIC());
            nadraIntegrationVO.setFullName(result.getUsername());
            nadraIntegrationVO.setSecondaryCitizenNumber("");
            nadraIntegrationVO.setSecondaryFullName("");
            nadraIntegrationVO.setPresentAddress(result.getPresentAddress());
            nadraIntegrationVO.setBirthPlace(result.getBirthPlace());
            nadraIntegrationVO.setDateOfBirth(result.getDateOfBirth());
            nadraIntegrationVO.setCardExpire(result.getCardExpired());
            nadraIntegrationVO.setFingerIndex(result.getFinger());
            nadraIntegrationVO.setReligion(result.getReligion());
            nadraIntegrationVO.setMotherName(result.getMotherName());
            nadraIntegrationVO.setNativeLanguage(result.getNativeLanguage());
            nadraIntegrationVO.setPhotograph(result.getPhotograph());
            nadraIntegrationVO.setVerificationFunctionality(result.getVerificationFunctionality());
            nadraIntegrationVO.setCitizenNumber(result.getCNIC());

            return nadraIntegrationVO;


        } else
            return null;
    }
    /////////////////////////////////////////////////////////////////////////////////////
    //                                                                                 //
    //               Verification for Transaction                                     //
    ////////////////////////////////////////////////////////////////////////////////////

    public String verifyFingerPrints(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        String[] indexes = null;
        FingerIndex fingerIndex = new FingerIndex();
        if (jdbcUser != null) {

            if (jdbcUser.getIndexes() != null) {
                indexes = jdbcUser.getIndexes().split(",");
                int i = 0;
                for (String s : indexes
                        ) {
                    fingerIndex.getFINGER().add(i, Integer.valueOf(s));
                    i++;
                }
                if (Integer.valueOf(indexes[0]) == biometricVerification.getRequestData().getFingerIndex()) {
                    if (jdbcUser.getResponseCode().equals("100")) {
                        personalData.setPresentAddress(jdbcUser.getPresentAddress());
                        personalData.setDateOfBirth(jdbcUser.getDateOfBirth());
                        personalData.setCardExpired(jdbcUser.getCardExpired());
                        personalData.setBirthPlace(jdbcUser.getBirthPlace());
                        personalData.setReligion(jdbcUser.getReligion());
                        personalData.setMotherName(jdbcUser.getMotherName());
                        personalData.setGender(jdbcUser.getGender());
                        personalData.setNativeLanguage(jdbcUser.getNativeLanguage());
                        responseData.setUrduName(jdbcUser.getUsername());
                        responseStatus.setCode(jdbcUser.getResponseCode());
                        responseStatus.setMessage("successful");
                        responseData.setCitizenNumber(jdbcUser.getCNIC());

                    } else {
                        responseStatus.setCode(jdbcUser.getResponseCode());
                        responseStatus.setMessage("");
                    }
                } else {
                    responseStatus.setCode("122");
                    responseStatus.setMessage("fingerprint does not matched");
                    responseData.setFingerIndex(fingerIndex);
                }
            } else {
                responseStatus.setCode("122");
                responseStatus.setMessage("fingers are not configured on mock");
                responseData.setFingerIndex(fingerIndex);
            }

        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String identityDemographics(String xmlRequest) {

        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);
        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseData.setSessionId(jdbcUser.getSessionId());
                personalData.setBirthPlace(jdbcUser.getBirthPlace());
                personalData.setReligion(jdbcUser.getReligion());
                personalData.setMotherName(jdbcUser.getMotherName());
                personalData.setNativeLanguage(jdbcUser.getNativeLanguage());
//                     what to do with it ??
//                personalData.getValue().setPhotograph(jdbcUser.getPhotograph());
                responseData.setSessionId(jdbcUser.getSessionId());
                responseStatus.setCode("100");
                responseStatus.setMessage("successful");
            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String submitManualVerificationResults(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {


//            String response = fingerService.verifyFingerPrint(nadraRequest,jdbcUser);

//            if (jdbcUser.getResponseCode().equals("100") && response.equals("100")) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("successful");
                personalData.setPresentAddress(jdbcUser.getPresentAddress());
                personalData.setDateOfBirth(jdbcUser.getDateOfBirth());
                personalData.setCardExpired(jdbcUser.getCardExpired());
                personalData.setBirthPlace(jdbcUser.getBirthPlace());
                personalData.setGender(jdbcUser.getGender());
                personalData.setMotherName(jdbcUser.getMotherName());
                responseData.setUrduName(jdbcUser.getUsername());
                responseData.setCitizenNumber(jdbcUser.getCNIC());
            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String lastVerificationResult(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {


//            String response = fingerService.verifyFingerPrint(nadraRequest,jdbcUser);

//            if (jdbcUser.getResponseCode().equals("100") && response.equals("100")) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("successful");
                personalData.setPresentAddress(jdbcUser.getPresentAddress());
                personalData.setDateOfBirth(jdbcUser.getDateOfBirth());
                personalData.setCardExpired(jdbcUser.getCardExpired());
                personalData.setBirthPlace(jdbcUser.getBirthPlace());
                responseData.setUrduName(jdbcUser.getUsername());
                responseData.setCitizenNumber(jdbcUser.getCNIC());
                responseData.setVerificationFunctionality(jdbcUser.getVerificationFunctionality());

            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String submitMobileBankAccountDetails(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {


//            String response = fingerService.verifyFingerPrint(nadraRequest,jdbcUser);

//            if (jdbcUser.getResponseCode().equals("100") && response.equals("100")) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("successful");

                responseData.setCitizenNumber(jdbcUser.getCNIC());

            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }



    public String citizenData(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {


//            String response = fingerService.verifyFingerPrint(nadraRequest,jdbcUser);

//            if (jdbcUser.getResponseCode().equals("100") && response.equals("100")) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("successful");
                personalData.setPresentAddress(jdbcUser.getPresentAddress());
                personalData.setDateOfBirth(jdbcUser.getDateOfBirth());
                personalData.setCardExpired(jdbcUser.getCardExpired());
                personalData.setBirthPlace(jdbcUser.getBirthPlace());
                personalData.setGender(jdbcUser.getGender());
                personalData.setMotherName(jdbcUser.getMotherName());
                responseData.setUrduName(jdbcUser.getUsername());
                responseData.setCitizenNumber(jdbcUser.getCNIC());
            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    //                                                                                 //
    //               Verification for OTC Transaction                                  //
    ////////////////////////////////////////////////////////////////////////////////////

    public String otcVerifyFingerPrints(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        String[] indexes = null;
        FingerIndex fingerIndex = new FingerIndex();
        if (jdbcUser != null) {

            if (jdbcUser.getIndexes() != null) {
                indexes = jdbcUser.getIndexes().split(",");
                int i = 0;
                for (String s : indexes
                        ) {
                    fingerIndex.getFINGER().add(i, Integer.valueOf(s));
                    i++;
                }
                if (Integer.valueOf(indexes[0]) == biometricVerification.getRequestData().getFingerIndex()) {
                    if (jdbcUser.getResponseCode().equals("100")) {
                        responseData.setUrduName(jdbcUser.getUsername());
                        responseData.setSecondaryCitizenUrduName(jdbcUser.getUsername());
                        responseStatus.setCode(jdbcUser.getResponseCode());
                        responseStatus.setMessage("successful");
                        responseData.setCitizenNumber(jdbcUser.getCNIC());
                        responseData.setSecondaryCitizenNumber(jdbcUser.getCNIC());

//                responseData.setFingerIndex();

                    } else {
                        responseStatus.setCode(jdbcUser.getResponseCode());
                        responseStatus.setMessage("");
                    }
                } else {
                    responseStatus.setCode("122");
                    responseStatus.setMessage("fingerprint does not matched");
                    responseData.setFingerIndex(fingerIndex);
                }
            } else {
                responseStatus.setCode("122");
                responseStatus.setMessage("fingers are not configured on mock");
                responseData.setFingerIndex(fingerIndex);
            }

        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String otcIdentityDemographics(String xmlRequest) {

        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);
        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseData.setSessionId(jdbcUser.getSessionId());
                personalData.setReligion(jdbcUser.getReligion());
                personalData.setMotherName(jdbcUser.getMotherName());
                personalData.setBirthPlace(jdbcUser.getBirthPlace());
                personalData.setNativeLanguage(jdbcUser.getNativeLanguage());
//                     what to do with it ??
//                personalData.getValue().setPhotograph(jdbcUser.getPhotograph());
                responseData.setSessionId(jdbcUser.getSessionId());
                responseStatus.setCode("100");
                responseStatus.setMessage("successful");
            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String otcSubmitManualVerificationResults(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {


//            String response = fingerService.verifyFingerPrint(nadraRequest,jdbcUser);

//            if (jdbcUser.getResponseCode().equals("100") && response.equals("100")) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("successful");
                responseData.setCitizenNumber(jdbcUser.getCNIC());
            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }

    public String otcLastVerificationResult(String xmlRequest) {
        BiometricVerification biometricVerification = XMLUtil.convertRequest(xmlRequest);

        String CNIC = biometricVerification.getRequestData().getCitizenNumber();
        JDBCUser jdbcUser = transactionDAO.getCustomerData(CNIC);
        ResponseData responseData = new ResponseData();
        PersonalData personalData = new PersonalData();
        ResponsStatus responseStatus = new ResponsStatus();
        if (jdbcUser != null) {


//            String response = fingerService.verifyFingerPrint(nadraRequest,jdbcUser);

//            if (jdbcUser.getResponseCode().equals("100") && response.equals("100")) {
            if (jdbcUser.getResponseCode().equals("100")) {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("successful");
                responseData.setCitizenNumber(jdbcUser.getCNIC());
                responseData.setVerificationFunctionality(jdbcUser.getVerificationFunctionality());

            } else {
                responseStatus.setCode(jdbcUser.getResponseCode());
                responseStatus.setMessage("");
            }
        } else {
            responseStatus.setCode("111");
            responseStatus.setMessage("fingerprints does not exist in citizen database");
        }
        if (biometricVerification.getRequestData().getSessionId() == null) {
            responseData.setSessionId(this.randomNumber());
        } else
            responseData.setSessionId(biometricVerification.getRequestData().getSessionId());
        responseData.setPersonalData(personalData);
        responseData.setResponsStatus(responseStatus);
        biometricVerification.setResponseData(responseData);
        String response = XMLUtil.convertResponse(biometricVerification);
        return response;
    }


    private String randomNumber() {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        String s = sb.toString();
        return s;
    }
}
