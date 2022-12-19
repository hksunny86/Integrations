package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ADDITIONAL_RESPONSE_DATA;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class IbftTitleFetchResponse extends BasePDU {

    private static final long serialVersionUID = 1895396333666913997L;

    public void build() {
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI("0210");
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SETTLEMENT_CONVERSION_RATE.getValue(),trimToEmpty(getSettlementConversionRate()));
            msg.set(CONVERSION_DATE.getValue(),trimToEmpty(getConversionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(),trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(),trimToEmpty(getTransactionLocalDate()));
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
            msg.set(CONVERSION_DATE.getValue(), trimToEmpty(getConversionDate()));
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));
            msg.set(RESPONSE_CODE.getValue(), trimToEmpty(getResponseCode()));
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(), trimToEmpty(getTransactionCurrencyCode()));
            msg.set(SETTLEMENT_CURRENCY_CODE.getValue(),trimToEmpty(getSettlementCurrencyCode()));
            msg.set(ACCOUNCT_NO_1.getValue(),trimToEmpty(getAccountNo1()));
            msg.set(ACCOUNCT_NO_2.getValue(),trimToEmpty(getAccountNo2()));
            msg.set(RECORD_DATA.getValue(),trimToEmpty(getRecordData()));


            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }

        }
