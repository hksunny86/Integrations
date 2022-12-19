package com.inov8.jsblconsumer.util;

import com.inov8.jsblconsumer.R;

public class BuildConstants {

    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = true;
    public static boolean isDebuggingMode = true;

    public static int cer = R.raw.android;

    public static final String HANDSHAKE_KEY = "";
//    public static final String BASE_URL = "://jsqa.ingov8.com.pk/i8Microbank";
    public static final String BASE_URL = "://jsblb.inov8.com.pk/i8Microbank";

//    public static final String BASE_URL = "://172.29.12.48:8080/i8Microbank";

//    public static final String HOST_NAME = "jsqa.inov8.com.pk";
    public static final String HOST_NAME = "jsblb.inov8.com.pk";

//    public static final String BASE_URL = "://blb.jsbl.com/i8Microbank ";
//    public static final String HOST_NAME = "blb.jsbl.com";

    public static final String PROTOCOL = "http";
    public static final String SIGNATURE = "5WP7ivqnp71ed4uOIL10DUU6zwo=";

    public static final String CUSTOM_PROXY_VERIFY =
 //         "http://10.0.1.221:8060/js-nadra-integration/services/api/verify"; // Internal
            "https://blbuat.jsbl.com/js-nadra-integration/services/api/verify";  //  UAT
//          "https://blb.jsbl.com/js-nadra-integration/services/api/verify"; // Production

}