package com.inov8.integration.middleware.pdu.request;

import com.inov8.integration.middleware.enums.JSBankDefaultsEnum;
import com.inov8.integration.middleware.enums.MessageTypeEnum;
import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;
import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.*;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.ACCOUNCT_NO_2;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class CoreToWalletRequest extends BasePDU {

    private static final long serialVersionUID = -1643749419874072291L;

    public CoreToWalletRequest() {
        if (getHeader() == null) {
            this.setHeader(new BaseHeader());
        }
    }

    public void assemble() {
        byte[] header = null;
        byte[] body = null;
        try {
            getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);

            msg.setMTI(getHeader().getMessageType());
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
            msg.set(SETTLEMENT_AMOUNT.getValue(), trimToEmpty(getSettlementAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SETTLEMENT_CONVERSION_RATE.getValue(), getSettlementConversionRate());
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
            msg.set(TRANSACTION_AMOUNT_FEE.getValue(), trimToEmpty(getTransactionFee()));
            msg.set(SETTLEMENT_AMOUNT_FEE.getValue(), trimToEmpty(getSettlementFee()));
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
            msg.set(MERCHANT_TYPE.getValue(), JSBankDefaultsEnum.MERCHANT_TYPE.getValue());
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));
            msg.set(TERMINAL_ID.getValue(), trimToEmpty(getTerminalId()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), trimToEmpty(getCardAcceptorName()));
            msg.set(SETTLEMENT_CURRENCY_CODE.getValue(), trimToEmpty(getSettlementCurrencyCode()));
            msg.set(ACCOUNCT_NO_1.getValue(), trimToEmpty(getAccountNo1()));
            msg.set(ACCOUNCT_NO_2.getValue(), trimToEmpty(getAccountNo2()));

            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }
}
