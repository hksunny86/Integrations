
package com.inov8.integration.middleware.easypay;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransactionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OTC"/&gt;
 *     &lt;enumeration value="MA"/&gt;
 *     &lt;enumeration value="ATM"/&gt;
 *     &lt;enumeration value="CC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TransactionType")
@XmlEnum
public enum TransactionType {

    OTC,
    MA,
    ATM,
    CC;

    public String value() {
        return name();
    }

    public static TransactionType fromValue(String v) {
        return valueOf(v);
    }

}
