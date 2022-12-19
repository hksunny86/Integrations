package com.inov8.integration.middleware.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by inov8 on 3/9/2018.
 */
public class UnicodeUtil {
    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static String escapeUnicode(String input) {
        StringBuilder b = new StringBuilder(input.length());
        java.util.Formatter f = new java.util.Formatter(b);
        for (char c : input.toCharArray()) {
            if (c < 128) {
                b.append(c);
            } else {
                f.format("\\%04x", (int) c);
            }
        }
        return b.toString();
    }
}
