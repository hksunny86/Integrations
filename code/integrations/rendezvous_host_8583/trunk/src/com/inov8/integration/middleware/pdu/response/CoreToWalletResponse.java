package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class CoreToWalletResponse extends BasePDU {

    private static final long serialVersionUID = 5344490566706906192L;

    public CoreToWalletResponse() {
        setHeader(new BaseHeader());
    }

    public void build() {

        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);

            msg.setMTI("0230");
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
            msg.set(SETTLEMENT_AMOUNT.getValue(), trimToEmpty(getSettlementAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
            msg.set(RESPONSE_CODE.getValue(), trimToEmpty(getResponseCode()));

            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }
}
