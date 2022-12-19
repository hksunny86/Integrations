//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.inov8.integration.channel.BOPBLB.service;

import com.inov8.integration.channel.BOPBLB.client.AccountRegisterInquiryResponse;
import com.inov8.integration.channel.BOPBLB.client.AccountRegisterResponse;
import com.inov8.integration.channel.BOPBLB.client.CashOutResponse2;
import com.inov8.integration.channel.BOPBLB.client.CashoutInquiryResponse2;
import com.inov8.integration.channel.BOPBLB.client.ProcessEchallanSoap;
import com.inov8.integration.channel.BOPBLB.client.Proofoflifeinquiryresponse2;
import com.inov8.integration.channel.BOPBLB.client.Proofoflifeverificationresponse2;
import com.inov8.integration.channel.BOPBLB.client.ReverselResponse;
import com.inov8.integration.channel.BOPBLB.mock.BopCashOutMock;
import com.inov8.integration.channel.BOPBLB.request.AccountRegistrationInquiryRequest;
import com.inov8.integration.channel.BOPBLB.request.AccountRegistrationRequest;
import com.inov8.integration.channel.BOPBLB.request.CardIssuanceReissuanceInquiry;
import com.inov8.integration.channel.BOPBLB.request.CardIssuanceReissuanceRequest;
import com.inov8.integration.channel.BOPBLB.request.CashOutInquiryRequest;
import com.inov8.integration.channel.BOPBLB.request.CashOutRequest;
import com.inov8.integration.channel.BOPBLB.request.CashWithdrawlReversalRequest;
import com.inov8.integration.channel.BOPBLB.request.ProofOfLifeVerificationInquiryRequest;
import com.inov8.integration.channel.BOPBLB.request.ProofOfLifeVerificationRequest;
import com.inov8.integration.channel.BOPBLB.response.AccountRegistrationInquiryResponse;
import com.inov8.integration.channel.BOPBLB.response.AccountRegistrationResponse;
import com.inov8.integration.channel.BOPBLB.response.CashOutInquiryResonse;
import com.inov8.integration.channel.BOPBLB.response.CashOutResponse;
import com.inov8.integration.channel.BOPBLB.response.CashWithdrawlReversalResponse;
import com.inov8.integration.channel.BOPBLB.response.ProofOfLifeVerificationInquiryResponse;
import com.inov8.integration.channel.BOPBLB.response.ProofOfLifeVerificationResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BopCashOutService {
    private static Logger logger = LoggerFactory.getLogger(BopCashOutService.class.getSimpleName());
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
    @Value("${bop.blb.username:#{null}}")
    private String userName;
    @Value("${bop.blb.password:#{null}}")
    private String password;
    @Value("${bop.blb.banknemonic:#{null}}")
    private String bankMneonic;
    @Value("${bop.access.username:#{null}}")
    private String bopUserName;
    @Value("${bop.access.password:#{null}}")
    private String bopPassword;
    @Autowired(
            required = false
    )
    private ProcessEchallanSoap processEchallanSoap;
    @Autowired
    BopCashOutMock bopCashOutMock;
    private String mock = PropertyReader.getProperty("i8sb.target.environment");

    public BopCashOutService() {
    }

    public CashOutInquiryResonse cashOutInquiryResonse(CashOutInquiryRequest cashOutInquiryRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        CashOutInquiryResonse cashOutInquiryResonse = new CashOutInquiryResonse();
        CashoutInquiryResponse2 cashoutInquiryResponse2 = new CashoutInquiryResponse2();
        if (this.mock.equalsIgnoreCase("mock")) {
            cashOutInquiryResonse = this.bopCashOutMock.cashOutInquiry(cashOutInquiryRequest);
        } else {
            cashoutInquiryResponse2 = this.processEchallanSoap.cashOutInquiry(this.userName, this.password, this.bankMneonic, cashOutInquiryRequest.getCnic(), cashOutInquiryRequest.getDebitCardNumber(), cashOutInquiryRequest.getMobileNumber(), cashOutInquiryRequest.getSegmentId(), cashOutInquiryRequest.getAmount(), cashOutInquiryRequest.getAgentLocation(), cashOutInquiryRequest.getAgentCity(), cashOutInquiryRequest.getTerminalId(), cashOutInquiryRequest.getTransactionId());
        }

        logger.info("Response Recieve of CashOutInquiry from ApinSnap For RRN: " + rrn);

        if (StringUtils.isNotEmpty(cashoutInquiryResponse2.getResponseCode())) {
            cashOutInquiryResonse.setAmount(cashoutInquiryResponse2.getAmount());
            cashOutInquiryResonse.setResponseCode(cashoutInquiryResponse2.getResponseCode());
            cashOutInquiryResonse.setResponseDescription(cashoutInquiryResponse2.getResponseDescription());
            cashOutInquiryResonse.setBio_Status_Flag(cashoutInquiryResponse2.getBioStatusFlag());
            cashOutInquiryResonse.setTransaction_Fee(cashoutInquiryResponse2.getTransactionFee());
            cashOutInquiryResonse.setStatus(cashoutInquiryResponse2.getStatus());
            cashOutInquiryResonse.setTotal_Amount(cashoutInquiryResponse2.getTotalAmount());
            cashOutInquiryResonse.setTransaction_ID(cashoutInquiryResponse2.getTransactionID());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return cashOutInquiryResonse;
    }

    public CashOutResponse cashOutResponse(CashOutRequest cashOutRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        CashOutResponse cashOutResponse = new CashOutResponse();
        CashOutResponse2 cashoutResponse1 = new CashOutResponse2();
        if (this.mock.equalsIgnoreCase("mock")) {
            cashOutResponse = this.bopCashOutMock.cashOut(cashOutRequest);
        } else {
            cashoutResponse1 = this.processEchallanSoap.cashOut(this.userName, this.password, this.bankMneonic, cashOutRequest.getCnic(), cashOutRequest.getDebitCardNumber(), cashOutRequest.getMobileNo(), cashOutRequest.getSegmentID(), cashOutRequest.getAmount(), cashOutRequest.getRrn(), cashOutRequest.getTrnsactionID(), cashOutRequest.getOtp(), cashOutRequest.getFingerIndex(), cashOutRequest.getFingerTemplate(), cashOutRequest.getTemplateType(), cashOutRequest.getAgentLocation(), cashOutRequest.getAgentCity(), cashOutRequest.getTerminalId());
        }

        logger.info("Response Recieve of CashOut from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(cashoutResponse1.getResponseCode())) {
            cashOutResponse.setResponseCode(cashoutResponse1.getResponseCode());
            cashOutResponse.setResponseDescription(cashoutResponse1.getResponseDescription());
            cashOutResponse.setFingerIndex(cashoutResponse1.getFingerIndex());
            cashOutResponse.setSessionID(cashoutResponse1.getSessionID());
            cashOutResponse.setStatus(cashoutResponse1.getStatus());
            cashOutResponse.setStatusCode(cashoutResponse1.getStatusCode());
            cashOutResponse.setTransactionID(cashoutResponse1.getTransactionID());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return cashOutResponse;
    }

    public CashWithdrawlReversalResponse cashWithdrawlReversalResponse(CashWithdrawlReversalRequest cashWithdrawlReversalRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        CashWithdrawlReversalResponse cashWithdrawlReversalResponse = new CashWithdrawlReversalResponse();
        ReverselResponse reverselResponse = new ReverselResponse();
        if (this.mock.equalsIgnoreCase("mock")) {
            cashWithdrawlReversalResponse = this.bopCashOutMock.cashWithdrawlReversal(cashWithdrawlReversalRequest);
        } else {
            reverselResponse = this.processEchallanSoap.cashWithdrawReversal(this.userName, this.password, this.bankMneonic, cashWithdrawlReversalRequest.getMobileNo(), cashWithdrawlReversalRequest.getRrn(), cashWithdrawlReversalRequest.getTerminalId(), cashWithdrawlReversalRequest.getOrignalRRN(), cashWithdrawlReversalRequest.getOrignalTransactionDateTime(), cashWithdrawlReversalRequest.getTransactionCode());
        }

        logger.info("Response Recieve of CashWithDrawal from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(reverselResponse.getResponseCode())) {
            cashWithdrawlReversalResponse.setResponseCode(reverselResponse.getResponseCode());
            cashWithdrawlReversalResponse.setResponseDescription(reverselResponse.getResponseDescription());
            cashWithdrawlReversalResponse.setRrn(reverselResponse.getRRN());
            cashWithdrawlReversalResponse.setStatus(reverselResponse.getStatus());
            cashWithdrawlReversalResponse.setStatusCode(reverselResponse.getStatusCode());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return cashWithdrawlReversalResponse;
    }

    public AccountRegistrationInquiryResponse accountRegistrationInquiryResponse(AccountRegistrationInquiryRequest accountRegistrationRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        AccountRegistrationInquiryResponse accountRegistrationInquiryResponse = new AccountRegistrationInquiryResponse();
        AccountRegisterInquiryResponse cashoutResponse = new AccountRegisterInquiryResponse();
        if (this.mock.equalsIgnoreCase("mock")) {
            accountRegistrationInquiryResponse = this.bopCashOutMock.accountRegistrationInquiry(accountRegistrationRequest);
        } else {
            cashoutResponse = this.processEchallanSoap.accountRegisterationInquiry(this.userName, this.password, this.bankMneonic, accountRegistrationRequest.getCnic(), accountRegistrationRequest.getMobileNo(), accountRegistrationRequest.getRrn(), accountRegistrationRequest.getDebitCardNumber(), accountRegistrationRequest.getTransactionType(), accountRegistrationRequest.getSegmentID(), accountRegistrationRequest.getAgentLocation(), accountRegistrationRequest.getAgentCity(), accountRegistrationRequest.getTerminalId());
        }

        logger.info("Response Recieve of AccountRegistrationInquiry from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(cashoutResponse.getResponseCode())) {
            accountRegistrationInquiryResponse.setResponseCode(cashoutResponse.getResponseCode());
            accountRegistrationInquiryResponse.setResponseDescription(cashoutResponse.getResponseDescription());
            accountRegistrationInquiryResponse.setRrn(cashoutResponse.getRRN());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return accountRegistrationInquiryResponse;
    }

    public AccountRegistrationInquiryResponse cardissuaneInquiry(CardIssuanceReissuanceInquiry accountRegistrationRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        AccountRegistrationInquiryResponse accountRegistrationInquiryResponse = new AccountRegistrationInquiryResponse();
        AccountRegisterInquiryResponse cashoutResponse = new AccountRegisterInquiryResponse();
        if (this.mock.equalsIgnoreCase("mock")) {
            accountRegistrationInquiryResponse = this.bopCashOutMock.cardIssuanceInquiry(accountRegistrationRequest);
        } else {
            cashoutResponse = this.processEchallanSoap.accountRegisterationInquiry(this.userName, this.password, this.bankMneonic, accountRegistrationRequest.getCnic(), accountRegistrationRequest.getMobileNo(), accountRegistrationRequest.getRrn(), accountRegistrationRequest.getDebitCardNumber(), accountRegistrationRequest.getTransactionType(), accountRegistrationRequest.getSegmentID(), accountRegistrationRequest.getAgentLocation(), accountRegistrationRequest.getAgentCity(), accountRegistrationRequest.getTerminalId());
        }

        logger.info("Response Recieve of CardIssuaneInquiry from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(cashoutResponse.getResponseCode())) {
            accountRegistrationInquiryResponse.setResponseCode(cashoutResponse.getResponseCode());
            accountRegistrationInquiryResponse.setResponseDescription(cashoutResponse.getResponseDescription());
            accountRegistrationInquiryResponse.setRrn(cashoutResponse.getRRN());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return accountRegistrationInquiryResponse;
    }

    public AccountRegistrationResponse accountRegistrationResponse(AccountRegistrationRequest accountRegistrationRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        AccountRegistrationResponse accountRegistrationResponse = new AccountRegistrationResponse();
        AccountRegisterResponse cashoutResponse1 = new AccountRegisterResponse();
        if (this.mock.equalsIgnoreCase("mock")) {
            accountRegistrationResponse = this.bopCashOutMock.accountRegistration(accountRegistrationRequest);
        } else {
            cashoutResponse1 = this.processEchallanSoap.accountRegisteration(this.userName, this.password, this.bankMneonic, accountRegistrationRequest.getCnic(), accountRegistrationRequest.getMobileNo(), accountRegistrationRequest.getDebitCardNumber(), accountRegistrationRequest.getRrn(), accountRegistrationRequest.getSessionId(), accountRegistrationRequest.getOtp(), accountRegistrationRequest.getFingerIndex(), accountRegistrationRequest.getTransactionType(), accountRegistrationRequest.getFingerTemplate(), accountRegistrationRequest.getSegmentID(), accountRegistrationRequest.getTemplateType(), accountRegistrationRequest.getAgentLocation(), accountRegistrationRequest.getAgentCity(), accountRegistrationRequest.getTerminalId());
        }

        logger.info("Response Recieve of AccountRegistration from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(cashoutResponse1.getResponseCode())) {
            accountRegistrationResponse.setResponseCode(cashoutResponse1.getResponseCode());
            accountRegistrationResponse.setResponseDescription(cashoutResponse1.getResponseDescription());
            accountRegistrationResponse.setFingerIndex(cashoutResponse1.getFingerIndex());
            accountRegistrationResponse.setSessionID(cashoutResponse1.getSessionID());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return accountRegistrationResponse;
    }

    public AccountRegistrationResponse cardIssuaneReissuance(CardIssuanceReissuanceRequest cardIssuanceReissuanceRequest) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        AccountRegistrationResponse accountRegistrationResponse = new AccountRegistrationResponse();
        AccountRegisterResponse clientaccountRegisterResponse = new AccountRegisterResponse();
        if (this.mock.equalsIgnoreCase("mock")) {
            accountRegistrationResponse = this.bopCashOutMock.cardIssuaneReissuance(cardIssuanceReissuanceRequest);
        } else {
            clientaccountRegisterResponse = this.processEchallanSoap.accountRegisteration(this.userName, this.password, this.bankMneonic, cardIssuanceReissuanceRequest.getCnic(), cardIssuanceReissuanceRequest.getMobileNo(), cardIssuanceReissuanceRequest.getDebitCardNumber(), cardIssuanceReissuanceRequest.getRrn(), cardIssuanceReissuanceRequest.getSessionId(), cardIssuanceReissuanceRequest.getOtp(), cardIssuanceReissuanceRequest.getFingerIndex(), cardIssuanceReissuanceRequest.getTransactionType(), cardIssuanceReissuanceRequest.getFingerTemplate(), cardIssuanceReissuanceRequest.getSegmentID(), cardIssuanceReissuanceRequest.getTemplateType(), cardIssuanceReissuanceRequest.getAgentLocation(), cardIssuanceReissuanceRequest.getAgentCity(), cardIssuanceReissuanceRequest.getTerminalId());
        }

        logger.info("Response Recieve of CardIssuaneReissuance from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(clientaccountRegisterResponse.getResponseCode())) {

            accountRegistrationResponse.setResponseCode(clientaccountRegisterResponse.getResponseCode());
            accountRegistrationResponse.setResponseDescription(clientaccountRegisterResponse.getResponseDescription());
            accountRegistrationResponse.setFingerIndex(clientaccountRegisterResponse.getFingerIndex());
            accountRegistrationResponse.setSessionID(clientaccountRegisterResponse.getSessionID());
        }

        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return accountRegistrationResponse;
    }

    public ProofOfLifeVerificationInquiryResponse proofOfLifeVerificationInquiry(ProofOfLifeVerificationInquiryRequest request) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        ProofOfLifeVerificationInquiryResponse accountRegistrationResponse = new ProofOfLifeVerificationInquiryResponse();
        Proofoflifeinquiryresponse2 proofoflifeverificationresponse2 = new Proofoflifeinquiryresponse2();
        if (this.mock.equalsIgnoreCase("mock")) {
            accountRegistrationResponse = this.bopCashOutMock.proofOfLifeVerificationInquiry(request);
        } else {
            proofoflifeverificationresponse2 = this.processEchallanSoap.proofOfLifeInquiry(
                    this.userName,
                    this.password,
                    this.bankMneonic,
                    request.getMobileNumber(),
                    request.getCnic(),
                    request.getSegmentID(),
                    request.getRrn(),
                    request.getAgentLocation(),
                    request.getAgentCity(),
                    request.getAgentID(),
                    request.getMachineName(),
                    request.getIpAddress(),
                    request.getMacAddress(),
                    request.getUdid(),
                    request.getAcknowledgedFlag());
        }
        logger.info("Response Recieve of CardIssuaneReissuance from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(proofoflifeverificationresponse2.getResponseCode())) {
            accountRegistrationResponse.setResponseCode(proofoflifeverificationresponse2.getResponseCode());
            accountRegistrationResponse.setResponseDescription(proofoflifeverificationresponse2.getResponseDescription());
            accountRegistrationResponse.setSmsText(proofoflifeverificationresponse2.getSMS());
        }
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return accountRegistrationResponse;
    }

    public ProofOfLifeVerificationResponse proofOfLifeVerification(ProofOfLifeVerificationRequest request) {
        String rrn = this.i8SBSwitchControllerRequestVO.getRRN();
        logger.info("Sending request to ApinSnap server for RRN: " + rrn);
        long start = System.currentTimeMillis();
        ProofOfLifeVerificationResponse proofOfLifeVerificationResponse = new ProofOfLifeVerificationResponse();
        Proofoflifeverificationresponse2 proofoflifeverificationresponse2 = new Proofoflifeverificationresponse2();
        if (this.mock.equalsIgnoreCase("mock")) {
            proofOfLifeVerificationResponse = this.bopCashOutMock.proofOfLifeVerification(request);
        } else {

            proofoflifeverificationresponse2 =
                    processEchallanSoap.proofOfLifeVerification(userName, password, bankMneonic
                            , request.getMobileNo()
                            , request.getCnic()
                            , request.getSegmentId()
                            , request.getRrn()
                            , request.getFingeIndex()
                            , request.getFingerTemplate()
                            , request.getTemplateType()
                            , request.getSessionID()
                            , request.getAgentLocation()
                            , request.getAgentCity()
                            , request.getAgentID()
                            , request.getMachineName()
                            , request.getIpAddress()
                            , request.getMacAddress()
                            , request.getUdid()
                            , request.getAcknowledgmentFlag());
        }
        logger.info("Response Recieve of CardIssuaneReissuance from ApinSnap For RRN: " + rrn);
        if (StringUtils.isNotEmpty(proofoflifeverificationresponse2.getResponseCode())) {
            proofOfLifeVerificationResponse.setResponseCode(proofoflifeverificationresponse2.getResponseCode());
            proofOfLifeVerificationResponse.setResponseDescription(proofoflifeverificationresponse2.getResponseDescription());
            proofOfLifeVerificationResponse.setFingerIndex(proofoflifeverificationresponse2.getFingerIndex());
            proofOfLifeVerificationResponse.setSessionId(proofoflifeverificationresponse2.getSessionID());
        }
        long endTime1 = (new Date()).getTime();
        long difference = endTime1 - start;
        logger.debug("[HOST] **** TRANSACTION REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        return proofOfLifeVerificationResponse;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
