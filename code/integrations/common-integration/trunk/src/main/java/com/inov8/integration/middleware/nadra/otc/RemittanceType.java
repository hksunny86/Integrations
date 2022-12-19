
package com.inov8.integration.middleware.nadra.otc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RemittanceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RemittanceType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MONENY_TRANSFER_SEND"/&gt;
 *     &lt;enumeration value="MONENY_TRANSFER_RECEIVE"/&gt;
 *     &lt;enumeration value="IBFT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RemittanceType", namespace = "http://schemas.datacontract.org/2004/07/NADRA.Biometric.Verification")
@XmlEnum
public enum RemittanceType {

    MONENY_TRANSFER_SEND,
    MONENY_TRANSFER_RECEIVE,
    IBFT;

    public String value() {
        return name();
    }

    public static RemittanceType fromValue(String v) {
        return valueOf(v);
    }

}
