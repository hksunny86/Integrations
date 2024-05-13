package com.inov8.integration.channel.raast.service;

import com.inov8.integration.channel.merchant.request.StaticQrRequest;
import com.inov8.integration.channel.merchant.response.StaticQrResponse;
import com.inov8.integration.channel.raast.mock.RaastMock;
import com.inov8.integration.channel.raast.request.*;
import com.inov8.integration.channel.raast.response.*;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

@Service
public class RaastService {

    private static Logger logger = LoggerFactory.getLogger(RaastService.class.getSimpleName());
    private RestTemplate restTemplate = new RestTemplate();
    private String i8sb_target_environment = PropertyReader.getProperty("i8sb.target.environment");
    private String customerAliasAccountIDUrl = PropertyReader.getProperty("raast.CustomerAliasAccountID");
    private String getDefaultAccountByAliasUrl = PropertyReader.getProperty("raast.GetDefaultAccountByAlias");
    private String getCustomerInfoUrl = PropertyReader.getProperty("raast.GetCustomerInfo");
    private String getCustomerAccountsUrl = PropertyReader.getProperty("raast.GetCustomerAccounts");
    private String getCustomerAliasesUrl = PropertyReader.getProperty("raast.GetCustomerAliases");
    private String deleteAccountUrl = PropertyReader.getProperty("raast.DeleteAccount");
    private String deleteAliasUrl = PropertyReader.getProperty("raast.DeleteAlias");
    private String deleteCustomerUrl = PropertyReader.getProperty("raast.DeleteCustomer");

    private String QRRastCustomerUrl = PropertyReader.getProperty("raast.QR.Registration");
    private String QrAccessToken=PropertyReader.getProperty("raast.QR.AccessToken");

    private String accessToken = PropertyReader.getProperty("raast.Access_token");
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public GetDefaultAccountByAliasResponse getDefaultAccountByAliasResponse(GetDefaultAccountByAliasRequest getDefaultAccountByAliasRequest) {

        GetDefaultAccountByAliasResponse getDefaultAccountByAliasResponse = new GetDefaultAccountByAliasResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.getDefaultAccountByAlias();
            getDefaultAccountByAliasResponse = (GetDefaultAccountByAliasResponse) JSONUtil.jsonToObject(response, GetDefaultAccountByAliasResponse.class);
            Objects.requireNonNull(getDefaultAccountByAliasResponse).setResponseCode("200");
            logger.info("Response Code for Get Default Account By Alias Request : " + Objects.requireNonNull(getDefaultAccountByAliasResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.getDefaultAccountByAliasUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(getDefaultAccountByAliasRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Get Default Account By Alias Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Customer Get Default Account By Alias Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Get Default Account By Alias Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Get Default Account By Alias Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    getDefaultAccountByAliasResponse = (GetDefaultAccountByAliasResponse) JSONUtil.jsonToObject(res1.getBody(), GetDefaultAccountByAliasResponse.class);
                    Objects.requireNonNull(getDefaultAccountByAliasResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(getDefaultAccountByAliasResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(getDefaultAccountByAliasResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            getDefaultAccountByAliasResponse = (GetDefaultAccountByAliasResponse) JSONUtil.jsonToObject(result, GetDefaultAccountByAliasResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            getDefaultAccountByAliasResponse = (GetDefaultAccountByAliasResponse) JSONUtil.jsonToObject(result, GetDefaultAccountByAliasResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(getDefaultAccountByAliasResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Get Default Account By Alias Request processed in: " + difference + " millisecond");
        }
        return getDefaultAccountByAliasResponse;
    }

    public CustomerAliasAccountResponse customerAliasAccountResponse(CustomerAliasAccountRequest customerAliasAccountRequest) {

        CustomerAliasAccountResponse customerAliasAccountResponse = new CustomerAliasAccountResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.customerAliasAccountResponse();
            customerAliasAccountResponse = (CustomerAliasAccountResponse) JSONUtil.jsonToObject(response, CustomerAliasAccountResponse.class);
            Objects.requireNonNull(customerAliasAccountResponse).setStatusCode("200");
            logger.info("Response Code for Customer Alias Account Id Request : " + Objects.requireNonNull(customerAliasAccountResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.customerAliasAccountIDUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(customerAliasAccountRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Customer Alias Account Id Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Customer Alias Account Id Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Customer Alias Account Id Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Customer Alias Account Id Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    customerAliasAccountResponse = (CustomerAliasAccountResponse) JSONUtil.jsonToObject(res1.getBody(), CustomerAliasAccountResponse.class);
                    Objects.requireNonNull(customerAliasAccountResponse).setStatusCode(responseCode);
                } else {
                    Objects.requireNonNull(customerAliasAccountResponse).setStatusCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(customerAliasAccountResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            customerAliasAccountResponse = (CustomerAliasAccountResponse) JSONUtil.jsonToObject(result, CustomerAliasAccountResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            customerAliasAccountResponse = (CustomerAliasAccountResponse) JSONUtil.jsonToObject(result, CustomerAliasAccountResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(customerAliasAccountResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Customer Alias Account Id Request processed in: " + difference + " millisecond");
        }
        return customerAliasAccountResponse;
    }

    public GetCustomerInformationResponse getCustomerInformationResponse(GetCustomerInformationRequest getCustomerInformationRequest) {

        GetCustomerInformationResponse getCustomerInformationResponse = new GetCustomerInformationResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.getCustomerInformation();
            getCustomerInformationResponse = (GetCustomerInformationResponse) JSONUtil.jsonToObject(response, GetCustomerInformationResponse.class);
            Objects.requireNonNull(getCustomerInformationResponse).setResponseCode("200");
            logger.info("Response Code for Get Customer Information Request : " + Objects.requireNonNull(getCustomerInformationResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.getCustomerInfoUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(getCustomerInformationRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Get Customer Information Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Get Customer Information Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Get Customer Information Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Get Customer Information Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    getCustomerInformationResponse = (GetCustomerInformationResponse) JSONUtil.jsonToObject(res1.getBody(), GetCustomerInformationResponse.class);
                    Objects.requireNonNull(getCustomerInformationResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(getCustomerInformationResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(getCustomerInformationResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            getCustomerInformationResponse = (GetCustomerInformationResponse) JSONUtil.jsonToObject(result, GetCustomerInformationResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            getCustomerInformationResponse = (GetCustomerInformationResponse) JSONUtil.jsonToObject(result, GetCustomerInformationResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(getCustomerInformationResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Get Customer Information Request processed in: " + difference + " millisecond");
        }
        return getCustomerInformationResponse;
    }

    public GetCustomerAccountsResponse getCustomerAccountsResponse(GetCustomerAccountsRequest getCustomerAccountsRequest) {

        GetCustomerAccountsResponse getCustomerAccountsResponse = new GetCustomerAccountsResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.getCustomerAccounts();
            getCustomerAccountsResponse = (GetCustomerAccountsResponse) JSONUtil.jsonToObject(response, GetCustomerAccountsResponse.class);
            Objects.requireNonNull(getCustomerAccountsResponse).setResponseCode("200");
            logger.info("Response Code for Get Customer Information Request : " + Objects.requireNonNull(getCustomerAccountsResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.getCustomerAccountsUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(getCustomerAccountsRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Get Customer Accounts Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Get Customer Accounts Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Get Customer Accounts Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Get Customer Accounts Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    getCustomerAccountsResponse = (GetCustomerAccountsResponse) JSONUtil.jsonToObject(res1.getBody(), GetCustomerAccountsResponse.class);
                    Objects.requireNonNull(getCustomerAccountsResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(getCustomerAccountsResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(getCustomerAccountsResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            getCustomerAccountsResponse = (GetCustomerAccountsResponse) JSONUtil.jsonToObject(result, GetCustomerAccountsResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            getCustomerAccountsResponse = (GetCustomerAccountsResponse) JSONUtil.jsonToObject(result, GetCustomerAccountsResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(getCustomerAccountsResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Get Customer Accounts Request processed in: " + difference + " millisecond");
        }
        return getCustomerAccountsResponse;
    }

    public GetCustomerAliasesResponse getCustomerAliasesResponse(GetCustomerAliasesRequest getCustomerAliasesRequest) {

        GetCustomerAliasesResponse getCustomerAliasesResponse = new GetCustomerAliasesResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.getCustomerAliases();
            getCustomerAliasesResponse = (GetCustomerAliasesResponse) JSONUtil.jsonToObject(response, GetCustomerAliasesResponse.class);
            Objects.requireNonNull(getCustomerAliasesResponse).setResponseCode("200");
            logger.info("Response Code for Get Customer Information Request : " + Objects.requireNonNull(getCustomerAliasesResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.getCustomerAliasesUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(getCustomerAliasesRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Get Customer Aliases Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Get Customer Aliases Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Get Customer Aliases Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Get Customer Aliases Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    getCustomerAliasesResponse = (GetCustomerAliasesResponse) JSONUtil.jsonToObject(res1.getBody(), GetCustomerAliasesResponse.class);
                    Objects.requireNonNull(getCustomerAliasesResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(getCustomerAliasesResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(getCustomerAliasesResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            getCustomerAliasesResponse = (GetCustomerAliasesResponse) JSONUtil.jsonToObject(result, GetCustomerAliasesResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            getCustomerAliasesResponse = (GetCustomerAliasesResponse) JSONUtil.jsonToObject(result, GetCustomerAliasesResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(getCustomerAliasesResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Get Customer Aliases Request processed in: " + difference + " millisecond");
        }
        return getCustomerAliasesResponse;
    }

    public DeleteAccountResponse deleteAccountResponse(DeleteAccountRequest deleteAccountRequest) {

        DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
        i8SBSwitchControllerRequestVO.setCustomerId(getI8SBSwitchControllerRequestVO().getCustomerId());
        i8SBSwitchControllerRequestVO.setAccountId(getI8SBSwitchControllerRequestVO().getAccountId());

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.deleteAccount();
            deleteAccountResponse = (DeleteAccountResponse) JSONUtil.jsonToObject(response, DeleteAccountResponse.class);
            Objects.requireNonNull(deleteAccountResponse).setResponseCode("200");
            logger.info("Response Code for Delete Account Information Request : " + Objects.requireNonNull(deleteAccountResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.deleteAccountUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(deleteAccountRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Delete Account Information Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Delete Account Information Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Delete Account Information Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Delete Account Information Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    deleteAccountResponse = (DeleteAccountResponse) JSONUtil.jsonToObject(res1.getBody(), DeleteAccountResponse.class);
                    Objects.requireNonNull(deleteAccountResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(deleteAccountResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(deleteAccountResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            deleteAccountResponse = (DeleteAccountResponse) JSONUtil.jsonToObject(result, DeleteAccountResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            deleteAccountResponse = (DeleteAccountResponse) JSONUtil.jsonToObject(result, DeleteAccountResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(deleteAccountResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Delete Account Information Request processed in: " + difference + " millisecond");
        }
        return deleteAccountResponse;
    }

    public DeleteAliasResponse deleteAliasResponse(DeleteAliasRequest deleteAliasRequest) {

        DeleteAliasResponse deleteAliasResponse = new DeleteAliasResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.deleteAlias();
            deleteAliasResponse = (DeleteAliasResponse) JSONUtil.jsonToObject(response, DeleteAliasResponse.class);
            Objects.requireNonNull(deleteAliasResponse).setResponseCode("200");
            logger.info("Response Code for Delete Account Information Request : " + Objects.requireNonNull(deleteAliasResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.deleteAliasUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(deleteAliasRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Delete Account Information Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Delete Account Information Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Delete Account Information Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Delete Account Information Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    deleteAliasResponse = (DeleteAliasResponse) JSONUtil.jsonToObject(res1.getBody(), DeleteAliasResponse.class);
                    Objects.requireNonNull(deleteAliasResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(deleteAliasResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(deleteAliasResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            deleteAliasResponse = (DeleteAliasResponse) JSONUtil.jsonToObject(result, DeleteAliasResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            deleteAliasResponse = (DeleteAliasResponse) JSONUtil.jsonToObject(result, DeleteAliasResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(deleteAliasResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Delete Account Information Request processed in: " + difference + " millisecond");
        }
        return deleteAliasResponse;
    }

    public DeleteCustomerResponse deleteCustomerResponse(DeleteCustomerRequest deleteCustomerRequest) {

        DeleteCustomerResponse deleteCustomerResponse = new DeleteCustomerResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock2")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            RaastMock raastMock = new RaastMock();
            String response = raastMock.deleteCustomer();
            deleteCustomerResponse = (DeleteCustomerResponse) JSONUtil.jsonToObject(response, DeleteCustomerResponse.class);
            Objects.requireNonNull(deleteCustomerResponse).setResponseCode("200");
            logger.info("Response Code for Delete Customer Information Request : " + Objects.requireNonNull(deleteCustomerResponse).getResponseCode());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.accessToken);
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.deleteCustomerUrl);
            logger.info("Requesting URL " + uri.toUriString());
            String requestJSON = JSONUtil.getJSON(deleteCustomerRequest);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestJSON, headers);
            logger.info("Prepared Delete Customer Request HttpEntity " + httpEntity);
            String responseCode;
            try {
                logger.info("Sending Delete Customer Information Request to Client " + httpEntity.getBody().toString());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Code of Delete Customer Information Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Delete Customer Information Request received from client " + res1.getBody());
                responseCode = res1.getStatusCode().toString();
                if (responseCode.equals("200")) {
                    deleteCustomerResponse = (DeleteCustomerResponse) JSONUtil.jsonToObject(res1.getBody(), DeleteCustomerResponse.class);
                    Objects.requireNonNull(deleteCustomerResponse).setResponseCode(responseCode);
                } else {
                    Objects.requireNonNull(deleteCustomerResponse).setResponseCode(res1.getStatusCode().toString());
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    responseCode = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (responseCode) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(deleteCustomerResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            deleteCustomerResponse = (DeleteCustomerResponse) JSONUtil.jsonToObject(result, DeleteCustomerResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            deleteCustomerResponse = (DeleteCustomerResponse) JSONUtil.jsonToObject(result, DeleteCustomerResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(deleteCustomerResponse).setResponseCode("500");
                }
            } catch (Exception e) {
                logger.error(" [ Exception ]" + e.getLocalizedMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Delete Customer Information Request processed in: " + difference + " millisecond");
        }
        return deleteCustomerResponse;
    }



    public QrRastRegistrationResponse qrRastRegistrationResponse(QRRastMerchantRegistration staticQrRequest) {

        QrRastRegistrationResponse staticQrResponse = new QrRastRegistrationResponse();

        long start = System.currentTimeMillis();
        if (this.i8sb_target_environment != null && this.i8sb_target_environment.equalsIgnoreCase("mock1")) {
            logger.info("Preparing request for Request Type : " + i8SBSwitchControllerRequestVO.getRequestType());
            String response = "{\n" +
                    "    \"staticQrResponses\": [\n" +
                    "        {\n" +
                    "            \"QR\": \"000201010211287600321d06bcb1a9b1487aa6c4962d6dbd622e0108JSBLPKKA0224PK27JSBL99999033479111775204581453035865802PK5912SudaisPharma6005Badin62520312SudaisPharma07099462411760819FastFoodRestaurants80230019FastFoodRestaurants63043E42\",\n" +
                    "            \"UUID\": \"300072123020603008123982753109060930\",\n" +
                    "            \"UETR\": \"7658c2b4-5aec-4945-96d7-2fa4c0fa8a56\",\n" +
                    "            \"Amount\": \"0\",\n" +
                    "            \"Expiry\": \"\",\n" +
                    "            \"CreatedDate\": \"07/08/2023 22:06:39\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            staticQrResponse = (QrRastRegistrationResponse) JSONUtil.jsonToObject(response, QrRastRegistrationResponse.class);
            Objects.requireNonNull(staticQrResponse).setResponseCode("200");
            logger.info("Response Code for Static QR Request: " + staticQrResponse.getResponseCode());
        } else {

            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(this.QRRastCustomerUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Access_token", this.QrAccessToken);
            String requestJSON = JSONUtil.getJSON(staticQrRequest);
            HttpEntity<?> httpEntity = new HttpEntity(requestJSON, headers);
            logger.info("Prepared Request HttpEntity " + httpEntity);
            Iterator res = this.restTemplate.getMessageConverters().iterator();

            while (res.hasNext()) {
                HttpMessageConverter endTime = (HttpMessageConverter) res.next();
                if (endTime instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) endTime).setWriteAcceptCharset(false);
                }
            }
            String response;
            try {
                logger.info("Requesting URL " + uri.toUriString());
                logger.info("Static QR Request Sent to Client " + httpEntity.getBody().toString(), httpEntity.getHeaders());
                ResponseEntity<String> res1 = this.restTemplate.postForEntity(uri.build().toUri(), httpEntity, String.class);
                logger.info("Response Entity: " + res1);
                logger.info("Response Code of Static QR Request received from client " + res1.getStatusCode().toString());
                logger.info("Response of Static QR Request received from client " + res1.getBody());
                String responseCode = res1.getStatusCode().toString();
                if (responseCode.equalsIgnoreCase("200")) {
                    staticQrResponse = (QrRastRegistrationResponse) JSONUtil.jsonToObject(res1.getBody(), QrRastRegistrationResponse.class);
                }
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    response = ((HttpStatusCodeException) e).getStatusCode().toString();
                    String result;
                    switch (response) {
                        case "400":
                        case "422":
                        case "500":
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            Objects.requireNonNull(staticQrResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(staticQrResponse).setResponseDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            staticQrResponse = (QrRastRegistrationResponse) JSONUtil.jsonToObject(result, QrRastRegistrationResponse.class);
                            break;
                        default:
                            result = ((HttpStatusCodeException) e).getResponseBodyAsString();
                            logger.info("Negative Response from Client " + result + "\n" + "Status Code received" + ((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(staticQrResponse).setResponseCode(((HttpStatusCodeException) e).getStatusCode().toString());
                            Objects.requireNonNull(staticQrResponse).setResponseDescription((((HttpStatusCodeException) e).getResponseBodyAsString()));
                            staticQrResponse = (QrRastRegistrationResponse) JSONUtil.jsonToObject(result, QrRastRegistrationResponse.class);
                            break;
                    }
                }
                if (e instanceof ResourceAccessException) {
                    String result = e.getMessage();
                    logger.info("ResourceAccessException " + result + "\n" + "Message received" + e.getMessage());
                    Objects.requireNonNull(staticQrResponse).setResponseCode("500");
                    Objects.requireNonNull(staticQrResponse).setResponseDescription(result);
                }
            } catch (Exception e) {
                logger.error("Exception Occurred: " + e.getMessage());
            }

            long endTime = (new Date()).getTime();
            long difference = endTime - start;
            logger.debug("Static QR Request processed in: " + difference + " millisecond");
        }
        return staticQrResponse;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
