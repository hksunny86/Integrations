package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

/**
 * Created by inov8 on 4/14/2016.
 */
public class CardAndPinVerificationResponse extends AMBITPDU {
    private static final long serialVersionUID = 8192668027703558568L;
    private String responseCode;
    private String cellNumber;
    public CardAndPinVerificationResponse(){
        if(super.getHeader() == null){
            super.setHeader(new AMBITHeader());
        }
    }
    public void build() {

        StringBuilder pduString = new StringBuilder();

        pduString.append(getHeader().build());
        pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
        pduString.append(trimToEmpty(cellNumber)).append(AMBIT_DELIMITER.getValue());
        FieldUtil.appendMessageSizeByte(pduString);

        super.setRawPdu(pduString.toString());
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
}
