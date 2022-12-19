package com.inov8.integration.middleware.queue;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.regex.Pattern;

import com.inov8.integration.middleware.util.FieldUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.enums.TransactionStatus;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.persistance.TransactionDAO;
import com.inov8.integration.middleware.persistance.model.TransactionLogModel;
import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FormatUtils;
import com.inov8.integration.vo.MiddlewareMessageVO;

@Component("TransactionResponsePoolHandler")
public class TransactionResponsePoolHandler {
    private static Logger logger = LoggerFactory.getLogger(TransactionResponsePoolHandler.class);

    @Autowired
    private TransactionResponsePool transactionResponsePool;

    @Autowired
    private TransactionDAO transactionDAO;

    // @Autowired
    // ReloadableResourceBundleMessageSource messageSource;

    String txTimeoutString;
    String sleepTimeString;
    String debug;

    Integer txTimeout;
    Integer sleepTime;

    public TransactionResponsePoolHandler() {
        System.out.println();
    }

    private void loadApplicationConfig() {
        txTimeoutString = ConfigReader.getInstance().getProperty("txtimeout", "70000");
        sleepTimeString = ConfigReader.getInstance().getProperty("sleeptime", "1000", true);
        debug = ConfigReader.getInstance().getProperty("debug", "false");
        txTimeout = Integer.parseInt(txTimeoutString);
        sleepTime = Integer.parseInt(sleepTimeString);
    }

    public MiddlewareMessageVO checkResponsePool(MiddlewareMessageVO responseVO) {
        loadApplicationConfig();
        String rrnKey = responseVO.getRrnKey();
        Long startTime = System.currentTimeMillis();
        while (true) {
            long timeElapsed = System.currentTimeMillis() - startTime.longValue();

            if (timeElapsed >= txTimeout) {

                logger.debug("***** TRANSACTION REQUEST TIMED OUT  *****");
                logger.debug("i8 Transaction Code => " + responseVO.getMicrobankTransactionCode());
                logger.debug("RRN => " + responseVO.getRetrievalReferenceNumber());
                logger.debug("RRN Key => " + rrnKey);
                logger.debug("Elapsed Time => " + timeElapsed + "  :: Timeout Configured => " + txTimeout);
                logger.debug("***** TRANSACTION REQUEST TIMED OUT  *****");

                this.transactionDAO.updateTransactionStatus(rrnKey, TransactionStatus.TIMEOUT.getValue().intValue());

                responseVO.setResponseCode("404");
                logger.debug("Response Code 404 for RRN: " + rrnKey);
                return responseVO;
            }
            // Locate the response from shared resources
            BasePDU basePDU = transactionResponsePool.get(rrnKey);

            if (basePDU != null) {
                // parse from PDU to IntegrationMessageVO
                String transCode = basePDU.getProcessingCode();
                String responseCode = basePDU.getResponseCode();
                String mti = basePDU.getHeader().getMessageType();
                String stan = basePDU.getStan();
                responseVO.setResponseCode(responseCode);
                responseVO.setStan(stan);

                if (mti.equals(MessageTypeEnum.MT_0210.getValue()) || mti.equals(MessageTypeEnum.MT_0230.getValue())) {
                    if (transCode.equals(TransactionCodeEnum.JS_TITLE_FETCH.getValue())) {
                        responseVO.setAccountTitle(basePDU.getAdditionalResponseData());
                    } else if (transCode.equals(TransactionCodeEnum.JS_FUND_TRANSFER.getValue())) {
                        // No WORK
                    } else if (transCode.equals(TransactionCodeEnum.JS_BILL_INQUIRY.getValue())) {
                        String additionalData = basePDU.getAdditionalResponseData();
                        if (StringUtils.isNotEmpty(additionalData)) {
                            String data[] = additionalData.split(Pattern.quote("|"), -1);

                            responseVO.setConsumerName(data[1]);
                            responseVO.setBillStatus(data[3]);
                            responseVO.setBillingMonth(data[4]);
                            try {
                                responseVO.setBillDueDate(DateTools.stringToDate(data[5], "yyMMdd"));
                            } catch (ParseException e) {
                                logger.error("Exception", e);
                            }
                            responseVO.setAmountDueDate(FormatUtils.parseMiddlewareAmount(data[6]));
                            responseVO.setAmountAfterDueDate(FormatUtils.parseMiddlewareAmount(data[7]));
                            responseVO.setBillAggregator(data[8]);
                        }

                    } else if (transCode.equals(TransactionCodeEnum.JS_BILL_PAYMENT.getValue())) {
                        // ADVICE - No WORK
                    } else if (transCode.equals(TransactionCodeEnum.JS_ACQUIRER_REVERSAL_ADVICE.getValue())) {
                        // ADVICE - No WORK
                    } else if (transCode.equals(TransactionCodeEnum.JS_FUND_TRANSFER_ADVICE.getValue())) {
                        // ADVICE - No WORK
                    } else if (transCode.equals(TransactionCodeEnum.JS_ACCOUNT_BALANCE_INQUIRY.getValue())) {
                        if (StringUtils.isNotEmpty(basePDU.getAdditionalAmount())) {
                            String balanceString = basePDU.getAdditionalAmount();
                            try {
                                String acountType = StringUtils.substring(balanceString, 0, 2);
                                String amountType = StringUtils.substring(balanceString, 2, 4);
                                String currencyCode = StringUtils.substring(balanceString, 4, 7);
                                String balanceType = StringUtils.substring(balanceString, 7, 8);
                                String middlewareBalance = StringUtils.substring(balanceString, 9);
                                String balance = FormatUtils.parseMiddlewareAmount(middlewareBalance);

                                if (balanceType.equalsIgnoreCase("D")) {
                                    balance = StringUtils.leftPad(balance, balance.length() + 1, "-");
                                }

                                responseVO.setAcountType(acountType);
                                responseVO.setAmountType(amountType);
                                responseVO.setCurrencyCode(currencyCode);
                                responseVO.setBalanceType(balanceType);
                                responseVO.setAccountBalance(balance);
                            } catch (Exception e) {
                                logger.error("Error Parsing Balance: ", e);
                            }

                        }
                    } else if (transCode.equals(TransactionCodeEnum.JS_IBFT_TITLE_FETCH.getValue())) {
                        logger.debug(">>>> TitleFetchRespone Message found in Response Pool with RRN " + rrnKey);

                        if (responseCode.equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                            if (StringUtils.isNotEmpty(basePDU.getRecordData())) {
                                String recordData = basePDU.getRecordData();

                                if (recordData.length() < 135) {

                                    try {
                                        String accountNo = StringUtils.substring(recordData, 0, 20);
                                        String accountTitle = StringUtils.substring(recordData, 20, 50);
                                        String senderBankImd = StringUtils.substring(recordData, 50, 61);
                                        String benificieryBankImd = StringUtils.substring(recordData, 61, 72);
                                        String crDr = StringUtils.substring(recordData, 72, 73);
                                        String bankName = StringUtils.substring(recordData, 73, 93);
                                        String branchName = StringUtils.substring(recordData, 93, 118);

                                        responseVO.setAccountNo1(accountNo);
                                        responseVO.setAccountTitle(accountTitle);
                                        responseVO.setSenderBankImd(senderBankImd);
                                        responseVO.setBeneficiaryBankImd(benificieryBankImd);
                                        responseVO.setAccountBranchName(branchName);
                                        responseVO.setAccountBankName(bankName);
                                        responseVO.setCrdr(crDr);
                                    } catch (Exception e) {
                                        logger.error("Error Parsing Balance: ", e);
                                    }
                                }else {
                                    String accountNo = StringUtils.substring(recordData, 0, 20);
                                    String accountTitle = StringUtils.substring(recordData, 20, 50);
                                    String senderBankImd = StringUtils.substring(recordData, 50, 61);
                                    String benificieryBankImd = StringUtils.substring(recordData, 61, 72);
                                    String crDr = StringUtils.substring(recordData, 72, 73);
                                    String bankName = StringUtils.substring(recordData, 73, 93);
                                    String branchName = StringUtils.substring(recordData, 93, 118);
                                    String benificiaryIban=StringUtils.substring(recordData,172,196);

                                    responseVO.setAccountNo1(accountNo);
                                    responseVO.setAccountTitle(accountTitle);
                                    responseVO.setSenderBankImd(senderBankImd);
                                    responseVO.setBeneficiaryBankImd(benificieryBankImd);
                                    responseVO.setAccountBranchName(branchName);
                                    responseVO.setAccountBankName(bankName);
                                    responseVO.setCrdr(crDr);
                                    responseVO.setBenificieryIban(benificiaryIban);
                                }
                            }

                        }
                    }else if (transCode.equals(TransactionCodeEnum.JS_IBFT_ADVICE.getValue())) {
                                logger.debug(">>>> InterBankFundTransferResponse Message found in Response Pool with RRN " + rrnKey);
                            }
                }else if (mti.equals(MessageTypeEnum.MT_0430.getValue())) {
                    if (transCode.equals(TransactionCodeEnum.JS_ACQUIRER_REVERSAL_ADVICE.getValue())) {
                        if (responseCode.equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                            this.transactionDAO.updateTransactionStatus(rrnKey, TransactionStatus.REVERSED.getValue().intValue());
                        } else {
                            this.transactionDAO.updateTransactionStatus(rrnKey, TransactionStatus.REVERSAL_FAILED.getValue().intValue());
                        }
                    }
                }

                this.transactionResponsePool.remove(rrnKey);

                TransactionLogModel trx = new TransactionLogModel();
                trx.setRetrievalRefNo(rrnKey);
                trx.setStatus(TransactionStatus.COMPLETED.getValue().longValue());
                // basePDU.assemblePDU();
                try {
                    trx.setPduResponseString(new String(basePDU.getRawPdu(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("Exception", e);
                }
                trx.setPduResponseHEX(Hex.encodeHexString(basePDU.getRawPdu()));
                trx.setResponseCode(responseVO.getResponseCode());
                trx.setProcessedTime(0L);
                this.transactionDAO.update(trx);

                logger.debug("RRN : " + responseVO.getRetrievalReferenceNumber());
                logger.debug("RRN Key being removed from  Transaction Pool: " + rrnKey);
                logger.debug("RRN Key status updated in database as COMPLETED: " + rrnKey);
                return responseVO;
            }
            try {
//				if (debug.equals("true"))
//					logger.debug("**** SLEEPING FOR " + sleepTime + " MILISECONDS TO WAIT FOR RESPONSE ****");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.error("Exception", e);
                logger.error(e.getMessage());
            }
        }


    }

}
