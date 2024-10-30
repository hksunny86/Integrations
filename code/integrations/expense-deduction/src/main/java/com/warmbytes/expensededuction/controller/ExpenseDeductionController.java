package com.warmbytes.expensededuction.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.warmbytes.expensededuction.model.ExpensesTransaction;
import com.warmbytes.expensededuction.service.ExpensesTransactionService;
import com.warmbytes.expensededuction.vo.DebitRequest;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ExpenseDeductionController {

    private static Logger logger = LoggerFactory.getLogger(ExpenseDeductionController.class);

    @Autowired
    private ExpensesTransactionService service;

    @Value("${zindigi.username}")
    private String zindigiUserName;
    @Value("${zindigi.password}")
    private String zindigiPassword;
    @Value("${zindigi.channelid}")
    private String zindigiChannelId;
    @Value("${zindigi.terminalid}")
    private String zindigiTerminalId;
    @Value("${zindigi.baseurl}")
    private String zindigiBaseUrl;
    @Value("${microbank.debit.productId}")
    private String debitProductId;

    private static final String TRANS_STATUS_SUCCESS = "Success";
    private static final String TRANS_STATUS_FAILED = "Failed";

    @GetMapping("/v1/deduction")
    public ResponseEntity<?> deduction(@RequestParam("mobileNo") String mobileNo) {
        Map<String, String> response = new HashMap<>();
        if (StringUtils.isBlank(mobileNo)) {
            response.put("code", "01");
            response.put("message", "Invalid mobile no provided");
        } else {
             response.put("code", "00");
             response.put("message", "Success");
        }
        CompletableFuture.runAsync(() -> {
            processRequest(mobileNo);
        });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void processRequest(String mobileNo) {
        logger.info("Inside processRequest :: "+mobileNo );
        ExpensesTransaction expenseTrans = service.findByMobileNo(mobileNo);
        if (expenseTrans != null) {
            try {

                logger.info("Record Processing :: "+expenseTrans.getExpensesTransactionId()+"\t Amount :: "+expenseTrans.getAmount());
                DebitRequest debitRequest = new DebitRequest();
                String apiUrl = zindigiBaseUrl+"/ws/api/debitRequest";
                String dateTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                String rrn = String.format("%08d", new java.util.Random().nextInt(100000000));
                String pin = "";
                String pinType = "";
                StringBuilder stringText = new StringBuilder()
                        .append(zindigiUserName)
                        .append(zindigiPassword)
                        .append(expenseTrans.getMobileNo())
                        .append(dateTime)
                        .append(rrn)
                        .append(zindigiChannelId)
                        .append(zindigiTerminalId)
                        .append(debitProductId)
                        .append(pin)
                        .append(pinType)
                        .append(expenseTrans.getAmount().toString())
                        .append("NOVA");

                String hashData = DigestUtils.sha256Hex(stringText.toString());
                debitRequest.setPin(pin);
                debitRequest.setPinType(pinType);
                debitRequest.setChannelId(zindigiChannelId);
                debitRequest.setTerminalId(zindigiTerminalId);
                debitRequest.setDateTime(dateTime);
                debitRequest.setPassword(zindigiPassword);
                debitRequest.setUserName(zindigiUserName);
                debitRequest.setProductId(debitProductId);
                debitRequest.setMobileNumber(expenseTrans.getMobileNo());
                debitRequest.setHashData(hashData);
                debitRequest.setRrn(rrn);
                debitRequest.setTransactionAmount(expenseTrans.getAmount().toString());
                debitRequest.setReserved6("NOVA");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String reqJSON = ow.writeValueAsString(debitRequest);
                // Create the ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                logger.info(apiUrl+" Request Body :: "+reqJSON);
                HttpEntity<?> requestEntity = new HttpEntity<>(debitRequest, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
                String jsonResponse = objectMapper.writeValueAsString(response.getBody());
                logger.info("Response from microbank :: "+jsonResponse);
                Map<String, Object> responseBody = objectMapper.readValue(jsonResponse, Map.class);
                if (responseBody != null && responseBody.get("responseCode").equals("00")) {
                    String transId = (String) responseBody.get("transactionId");
                    expenseTrans.setTransactionId(transId);
                    expenseTrans.setUpdatedOn(new Date());
                    expenseTrans.setIsProcess(true);
                    expenseTrans.setTransactionStatus(TRANS_STATUS_SUCCESS);
                    service.save(expenseTrans);
                } else {
                    logger.info("Invalid transaction response");
                    expenseTrans.setUpdatedOn(new Date());
                    expenseTrans.setIsProcess(true);
                    expenseTrans.setFailureReason((String) responseBody.get("responseDescription"));
                    expenseTrans.setTransactionStatus(TRANS_STATUS_FAILED);
                    service.save(expenseTrans);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(ExceptionUtils.getStackTrace(e));
                expenseTrans.setUpdatedOn(new Date());
                expenseTrans.setIsProcess(true);
                expenseTrans.setTransactionStatus(TRANS_STATUS_FAILED);
                expenseTrans.setFailureReason("System error :: "+e.getMessage());
                service.save(expenseTrans);
            }
        } else {
            logger.info("No Record Found Against MobileNO :: "+mobileNo);
        }
    }
}
