
package com.inov8.integration.middleware.bop.nadra;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Enums.Template_Format.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Enums.Template_Format"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ANSI"/&gt;
 *     &lt;enumeration value="ISO_19794_2"/&gt;
 *     &lt;enumeration value="SEGAM_PKMAT"/&gt;
 *     &lt;enumeration value="SAGEM_PKCOMPV2"/&gt;
 *     &lt;enumeration value="SAGEM_CFV"/&gt;
 *     &lt;enumeration value="RAW_IMAGE"/&gt;
 *     &lt;enumeration value="WSQ"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enums.Template_Format", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel")
@XmlEnum
public enum EnumsTemplateFormat {

    ANSI("ANSI"),
    ISO_19794_2("ISO_19794_2"),
    SEGAM_PKMAT("SEGAM_PKMAT"),
    @XmlEnumValue("SAGEM_PKCOMPV2")
    SAGEM_PKCOMPV_2("SAGEM_PKCOMPV2"),
    SAGEM_CFV("SAGEM_CFV"),
    RAW_IMAGE("RAW_IMAGE"),
    WSQ("WSQ");
    private final String value;

    EnumsTemplateFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumsTemplateFormat fromValue(String v) {
        for (EnumsTemplateFormat c: EnumsTemplateFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
