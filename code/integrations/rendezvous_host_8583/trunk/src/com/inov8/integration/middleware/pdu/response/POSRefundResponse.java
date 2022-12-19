package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class POSRefundResponse extends BasePDU {

    public POSRefundResponse() {
        setHeader(new BaseHeader());
    }

    public ISOMsg build() {

        ISOMsg msg = null;
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            msg = packager.createISOMsg();
            msg.setPackager(packager);

            msg.setMTI("0210");
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(MERCHANT_TYPE.getValue(), trimToEmpty(getMerchantType()));
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
            msg.set(RRN.getValue(),trimToEmpty(getRrn()));
            msg.set(TERMINAL_ID.getValue(),trimToEmpty(getTerminalId()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(),trimToEmpty(getCardAcceptorName()));
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(),trimToEmpty(getTransactionCurrencyCode()));
            msg.set(RECIEVIENG_INSTITUTION_CODE.getValue(),trimToEmpty(getRecievingInstitutionCode()));
            msg.set(RESPONSE_CODE.getValue(), trimToEmpty(getResponseCode()));
            msg.set(ADDITIONAL_RESPONSE_DATA.getValue(), trimToEmpty(getAdditionalResponseData()));

            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));

        return msg;
    }

}
