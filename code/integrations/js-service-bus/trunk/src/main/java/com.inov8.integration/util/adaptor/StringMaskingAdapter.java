package com.inov8.integration.util.adaptor;

import com.inov8.integration.util.CommonUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringMaskingAdapter extends XmlAdapter<String, String> {

    @Override
    public String marshal(String string) throws Exception {
        return maskString(string);
    }

    @Override
    public String unmarshal(String string) throws Exception {
        return string;
    }

    private String maskString(String input) {
        if (input != null && input.length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                if (!Character.isWhitespace(input.charAt(i))) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString();
        }
        return input;
    }
}