package com.inov8.integration.middleware.pdu.request;

import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_1;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RECIEVIENG_INSTITUTION_CODE;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class BalanceInquiryRequest extends BasePDU {

    public BalanceInquiryRequest() {
        this.setHeader(new BaseHeader());
        this.getHeader().setMessageType("0200");
        this.setProcessingCode(TransactionCodeEnum.JS_BB_BALANCE_INQUIRY.getValue());

    }

    public ISOMsg assemble() {

        ISOMsg msg = null;
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            msg = packager.createISOMsg();
            msg.setPackager(packager);

            msg.setMTI("0200");
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
            msg.set(MERCHANT_TYPE.getValue(), trimToEmpty(getMerchantType()));
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
            msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), trimToEmpty(getSettlementFee()));
            msg.set(TRANSACTION_AMOUNT_FEE.getValue(), trimToEmpty(getTransactionFee()));
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), trimToEmpty(getCardAcceptorName()));
            msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), trimToEmpty(getSettlementCurrencyCode()));
            msg.set(ACCOUNCT_NO_1.getValue(), trimToEmpty(getAccountNo1()));
            msg.set(TERMINAL_ID.getValue(), trimToEmpty(getTerminalId()));

            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));

        return msg;
    }

}
