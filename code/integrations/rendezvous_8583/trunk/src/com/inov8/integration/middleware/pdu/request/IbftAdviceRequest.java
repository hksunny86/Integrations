package com.inov8.integration.middleware.pdu.request;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.DATE_LOCAL_TRANSACTION;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class IbftAdviceRequest extends BasePDU {
    private static final long serialVersionUID = 4288733817973675911L;

    public IbftAdviceRequest() {
        if (getHeader() == null) {
            this.setHeader(new BaseHeader());
        }
    }

    public void assemble() {
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI(getHeader().getMessageType());
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
            msg.set(SETTLEMENT_AMOUNT.getValue(),trimToEmpty(getSettlementAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(CONVERSION_DATE.getValue(),trimToEmpty(getConversionDate()));
            msg.set(NETWORK_IDENTIFIER.getValue(),trimToEmpty(getNetworkIdentifier()));
            msg.set(RRN.getValue(),trimToEmpty(getRrn()));
            msg.set(TERMINAL_ID.getValue(),trimToEmpty(getTerminalId()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(),trimToEmpty(getCardAcceptorNameAndLocation()));
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(), trimToEmpty(getTransactionCurrencyCode()));
            msg.set(SETTLEMENT_CURRENCY_CODE.getValue(),trimToEmpty(getSettlementCurrencyCode()));
            msg.set(Additional_Data.getValue(),trimToEmpty(getPurposeOfPayment()));
            msg.set(ACCOUNCT_NO_1.getValue(), trimToEmpty(getAccountNo1()));
            msg.set(ACCOUNCT_NO_2.getValue(),trimToEmpty(getAccountNo2()));
            msg.set(RECORD_DATA.getValue(),(getRecordData()));
            body = msg.pack();
            logISOMessage(msg);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        super.setRawPdu(ArrayUtils.addAll(header, body));

    }
}
