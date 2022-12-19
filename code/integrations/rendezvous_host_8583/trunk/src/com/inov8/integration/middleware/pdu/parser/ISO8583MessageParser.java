package com.inov8.integration.middleware.pdu.parser;

import java.util.BitSet;

import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.CommonUtils;
import com.inov8.integration.middleware.util.ISO8583Utils;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;

public class ISO8583MessageParser {
    private static Logger logger = LoggerFactory.getLogger(ISO8583MessageParser.class.getSimpleName());

    public static BasePDU parse(byte[] pduMessage) {
        logger.debug("request recieved in Msg0");
        try {
            logger.debug("request recieved in Msg1");
            byte[] pduHeaderBytes = ArrayUtils.subarray(pduMessage, 0, 35);
            byte[] pduBodyBytes = ArrayUtils.subarray(pduMessage, 35, pduMessage.length);
            BasePDU basePDU = new BasePDU();
            basePDU.setHeader(parseHeader(new String(pduHeaderBytes)));
            logger.debug("request recieved in Msg2");
            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg isoMsg = packager.createISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.unpack(pduBodyBytes);

            basePDU.getHeader().setMessageType(isoMsg.getMTI());
            logger.debug("request recieved in Msg3");
            logISOMessage(isoMsg);

            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                logger.debug("request recieved in Msg4");
                if (isoMsg.hasField(i)) {

                    if (PAN.getValue() == i) {
                        basePDU.setPan(isoMsg.getString(i));
                    }
                    if (PROCESSING_CODE.getValue() == i) {
                        basePDU.setProcessingCode(isoMsg.getString(i));
                    }
                    if (TRANSACTION_AMOUNT.getValue() == i) {
                        basePDU.setTransactionAmount(isoMsg.getString(i));
                    }
                    if (SETTLEMENT_AMOUNT.getValue() == i) {
                        basePDU.setSettlementAmount(isoMsg.getString(i));
                    }
                    if (TRANSACTION_DATE.getValue() == i) {
                        basePDU.setTransactionDate(isoMsg.getString(i));
                    }
                    if (SETTLEMENT_CONVERSION_RATE.getValue() == i) {
                        basePDU.setSettlementConversionRate(isoMsg.getString(i));
                    }
                    if (SYSTEMS_TRACE_NO.getValue() == i) {
                        basePDU.setStan(isoMsg.getString(i));
                    }
                    if (TIME_LOCAL_TRANSACTION.getValue() == i) {
                        basePDU.setTransactionLocalTime(isoMsg.getString(i));
                    }
                    if (DATE_LOCAL_TRANSACTION.getValue() == i) {
                        basePDU.setTransactionLocalDate(isoMsg.getString(i));
                    }
                    if (SETTLEMENT_DATE.getValue() == i) {
                        basePDU.setSettlementDate(isoMsg.getString(i));
                    }
                    if (CONVERSION_DATE.getValue() == i) {
                        basePDU.setConversionDate(isoMsg.getString(i));
                    }
                    if (NETWORK_IDENTIFIER.getValue() == i) {
                        basePDU.setNetworkIdentifier(isoMsg.getString(i));
                    }
                    if (TRANSACTION_AMOUNT_FEE.getValue() == i) {
                        basePDU.setTransactionFee(isoMsg.getString(i));
                    }
                    if (SETTLEMENT_AMOUNT_FEE.getValue() == i) {
                        basePDU.setSettlementFee(isoMsg.getString(i));
                    }
                    if (SETTLEMENT_AMOUNT_PROCESSING_FEE.getValue() == i) {
                        basePDU.setSettlementProcessingFee(isoMsg.getString(i));
                    }
                    if (ACQUIRER_IDENTIFICATION.getValue() == i) {
                        basePDU.setAcquirerIdentification(isoMsg.getString(i));
                    }
                    if (RRN.getValue() == i) {
                        basePDU.setRrn(isoMsg.getString(i));
                    }
                    if (AUTH_ID_RESPONSE.getValue() == i) {
                        basePDU.setAuthIdResponse(isoMsg.getString(i));
                    }
                    if (RESPONSE_CODE.getValue() == i) {
                        basePDU.setResponseCode(isoMsg.getString(i));
                    }
                    if (TERMINAL_ID.getValue() == i) {
                        basePDU.setTerminalId(isoMsg.getString(i));
                    }
                    if (CARD_ACCEPTOR_NAME_AND_LOCATION.getValue() == i) {
                        basePDU.setCardAcceptorName(isoMsg.getString(i));
                    }
                    if (ADDITIONAL_RESPONSE_DATA.getValue() == i) {
                        basePDU.setAdditionalResponseData(isoMsg.getString(i));
                    }
                    if (TRANSACTION_CURRENCY_CODE.getValue() == i) {
                        basePDU.setTransactionCurrencyCode(isoMsg.getString(i));
                    }
                    if (SETTLEMENT_CURRENCY_CODE.getValue() == i) {
                        basePDU.setSettlementCurrencyCode(isoMsg.getString(i));
                    }
                    if (PIN_DATA.getValue() == i) {
                        basePDU.setPin(isoMsg.getString(i));
                    }
                    if (ADDITIONAL_AMOUNT.getValue() == i) {
                        basePDU.setAdditionalAmount(isoMsg.getString(i));
                    }
                    if (NETWORK_MANAGEMENT_CODE.getValue() == i) {
                        basePDU.setNetworkManagementCode(isoMsg.getString(i));
                    }
                    if (MESSAGE_SECURITY_CODE.getValue() == i) {
                        basePDU.setMessageSecurityCode(isoMsg.getString(i));
                    }
                    if (ACCOUNCT_NO_1.getValue() == i) {
                        basePDU.setAccountNo1(isoMsg.getString(i));
                    }
                    if (ACCOUNCT_NO_2.getValue() == i) {
                        basePDU.setAccountNo2(isoMsg.getString(i));
                    }
                    if (PRIVATE_DATA.getValue() == i) {
                        basePDU.setPrivateData(isoMsg.getString(i));
                    }
                    if (RECORD_DATA.getValue() == i) {
                        basePDU.setRecordData(isoMsg.getString(i));
                    }
                    if (POS_ENTRY_MODE.getValue() == i) {
                        basePDU.setPosEntryMode(isoMsg.getString(i));
                    }
                    if (TRACK_2_DATA.getValue() == i) {
                        basePDU.setTrack2Data(isoMsg.getString(i));
                    }
                    if (ORIGINAL_DATA_ELEMENTS.getValue() == i) {
                        basePDU.setOriginalDataElement(isoMsg.getString(i));
                    }
                    if (MERCHANT_TYPE.getValue() == i) {
                        basePDU.setMerchantType(isoMsg.getString(i));
                    }
                    if (RECIEVIENG_INSTITUTION_CODE.getValue() == i) {
                        basePDU.setRecievingInstitutionCode(isoMsg.getString(i));
                    }
                    if (AMOUNT_CARD_HOLDER_BILLING.getValue()==i){
                        basePDU.setAmountCardHolderBilling(isoMsg.getString(i));
                    }


                }
            }
            logger.debug("request recieved in Msg5");
            basePDU.setRawPdu(pduMessage);
            return basePDU;
        } catch (Exception ex) {
            logger.debug("request recieved in Msg6");
            logger.error("ERROR: ", ex);
        }
        return null;
    }


    private static void logISOMessage(ISOMsg msg) {
        logger.debug("request recieved in Msg7");
        StringBuilder log = new StringBuilder();
        log.append("\n----ISO MESSAGE-----");
        log.append("\n");
        try {
            logger.debug("request recieved in Msg8");
            BitSet bset = (BitSet) msg.getValue(-1);
            log.append("BitMap : " + org.jpos.iso.ISOUtil.bitSet2String(bset));
            log.append("\n");
            log.append(" MTI : " + msg.getMTI());
            log.append("\n");
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    log.append("    DE-" + i + " : " + msg.getString(i));
                    log.append("\n");
                }
            }
        } catch (Exception e) {
            logger.debug("request recieved in Msg9");
            logger.error("Exception", e);
        } finally {

            log.append("----ISO MESSAGE-----");
            log.append("\n");
        }
        logger.debug("request recieved in Msg10");
        logger.debug(log.toString());
    }

    public static BaseHeader parseHeader(String hexMessage) {
        BaseHeader header = new BaseHeader();

        return header;
    }

}
