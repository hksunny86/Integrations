package com.inov8.integration.middleware.pdu.request;

import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TERMINAL_ID;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class PosTransactionRequest extends BasePDU {

    public PosTransactionRequest() {
        this.setHeader(new BaseHeader());
        this.getHeader().setMessageType("0200");
        this.setProcessingCode(TransactionCodeEnum.JS_BB_POS_TRANSACTION.getValue());
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
            msg.set(TRANSACTION_AMOUNT.getValue(),trimToEmpty(getTransactionAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(MERCHANT_TYPE.getValue(), trimToEmpty(getMerchantType()));
            msg.set(POS_ENTRY_MODE.getValue(), trimToEmpty(getPosEntryMode()));
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
            msg.set(RRN.getValue(),trimToEmpty(getRrn()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(),trimToEmpty(getCardAcceptorName()));
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(),trimToEmpty(getTransactionCurrencyCode()));
            msg.set(ACCOUNCT_NO_1.getValue(),trimToEmpty(getAccountNo1()));
            msg.set(TERMINAL_ID.getValue(),trimToEmpty(getTerminalId()));

            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));

        return msg;
    }

}
