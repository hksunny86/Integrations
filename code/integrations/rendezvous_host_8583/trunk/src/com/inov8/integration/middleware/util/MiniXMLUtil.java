//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.inov8.integration.middleware.util;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;

public class MiniXMLUtil {
    public static final String CUSTOMER_MOBILE_NUMBER = "//trans/trn[@name='CMOB']";
    public static final String NAME = "//trans/trn[@name='NAME']";
    public static final String CNIC = "//trans/trn[@name='CNIC']";
    public static final String TRANSACTION_AMOUNT = "//trans/trn[@name='TXAM']";
    public static final String TRANSACTION_AMOUNT_FORMATED = "//trans/trn[@name='TXAMF']";
    public static final String COMMISSION_AMOUNT = "//trans/trn[@name='CAMT']";
    public static final String COMMISSION_AMOUNT_FORMATED = "//trans/trn[@name='CAMTF']";
    public static final String TRANSACTION_PROCESSING_AMOUNT = "//trans/trn[@name='TPAM']";
    public static final String TRANSACTION_PROCESSING_AMOUNT_FORMATED = "//trans/trn[@name='TPAMF']";
    public static final String TOTAL_AMOUNT = "//trans/trn[@name='TAMT']";
    public static final String TOTAL_AMOUNT_FORMATED = "//trans/trn[@name='TAMTF']";
    public static final String DATE_FORMATED = "//trans/trn[@name='DATEF']";
    public static final String DATE = "//trans/trn[@name='DATE']";
    public static final String TIME_FORMATED = "//trans/trn[@name='TIMEF']";
    public static final String BALANCE = "//trans/trn[@name='BAL']";
    public static final String BALANCE_FORMATED = "//trans/trn[@name='BALF']";

    public MiniXMLUtil() {
    }

    public static String getTagTextValue(String xmlData, String locationPath) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = new InputSource(new StringReader(xmlData));
        return ((String)xpath.evaluate(locationPath, inputSource, XPathConstants.STRING)).trim();
    }

    public Boolean isTllBalanceRequired(String xmlData) {
        Boolean isTllBalanceRequired = Boolean.FALSE;
        String tbrValue = null;

        try {
            tbrValue = getTagTextValue(xmlData, "/trans/trn");
        } catch (XPathExpressionException var5) {
            ;
        }

        if (tbrValue != null && tbrValue.equals("1")) {
            isTllBalanceRequired = Boolean.TRUE;
        }

        return isTllBalanceRequired;
    }

    public static String createXMLParameterTag(String name, String value) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<").append("trn").append(" ").append("name").append("=").append("\"").append(name).append("\"").append(">").append(value).append("<").append("/").append("trn").append(">");
        return strBuilder.toString();
    }

    public static String createInfoResponseXMLByParams(Map<String, Object> paramsMap) {
        StringBuilder strBuilder = new StringBuilder(1000);
        strBuilder.append("<").append("trans").append(">");
        Iterator var3 = paramsMap.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<String, Object> param = (Entry)var3.next();
            strBuilder.append("<").append("trn").append(" ");
            strBuilder.append("name").append("=").append("\"").append((String)param.getKey()).append("\"").append(">");
            strBuilder.append(param.getValue());
            strBuilder.append("<").append("/").append("trn").append(">");
        }

        strBuilder.append("<").append("/").append("trans").append(">");
        return strBuilder.toString();
    }

    public static String createMessageXML(String message) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<").append("mesgs").append(">").append("<").append("mesg").append(" ").append("level").append("=").append("\"").append("1").append("\"").append(">");
        strBuilder.append(message);
        strBuilder.append("<").append("/").append("mesg").append(">").append("<").append("/").append("mesgs").append(">");
        return strBuilder.toString();
    }

    public static String createXMLTag(String tagName, Map<String, Object> attributes, boolean closingRequired) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<").append(tagName);

        Object value;
        for(Iterator var5 = attributes.entrySet().iterator(); var5.hasNext(); strBuilder.append(StringEscapeUtils.escapeXml(value.toString())).append("\"")) {
            Entry<String, Object> attribute = (Entry)var5.next();
            strBuilder.append(" ").append((String)attribute.getKey()).append("=").append("\"");
            value = attribute.getValue();
            if (value == null) {
                value = "";
            }
        }

        strBuilder.append(">");
        if (closingRequired) {
            strBuilder.append(createXMLTag(tagName, false));
        }

        return strBuilder.toString();
    }

    public static String createXMLTag(String tagName, boolean openingTag) {
        return createXMLTag(tagName, openingTag, "");
    }

    public static String createXMLTag(String tagName, boolean openingTag, String attrs) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<");
        if (!openingTag) {
            strBuilder.append("/");
        }

        strBuilder.append(tagName);
        if (openingTag && !StringUtils.isBlank(attrs)) {
            strBuilder.append(attrs);
        }

        strBuilder.append(">");
        return strBuilder.toString();
    }
}
