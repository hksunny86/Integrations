
package com.inov8.integration.middleware.bop.nadra;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Enums.REMITTANCE_TYPE.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Enums.REMITTANCE_TYPE"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MONENY_TRANSFER_SEND"/&gt;
 *     &lt;enumeration value="MONENY_TRANSFER_RECEIVE"/&gt;
 *     &lt;enumeration value="IBFT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enums.REMITTANCE_TYPE", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel")
@XmlEnum
public enum EnumsREMITTANCETYPE {

    MONENY_TRANSFER_SEND,
    MONENY_TRANSFER_RECEIVE,
    IBFT;

    public String value() {
        return name();
    }

    public static EnumsREMITTANCETYPE fromValue(String v) {
        return valueOf(v);
    }

}
