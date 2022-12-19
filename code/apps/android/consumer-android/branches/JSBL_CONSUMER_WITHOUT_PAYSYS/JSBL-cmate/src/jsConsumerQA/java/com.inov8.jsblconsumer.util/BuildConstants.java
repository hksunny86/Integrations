package com.inov8.jsblconsumer.util;

import com.inov8.jsblconsumer.R;

public class BuildConstants {

    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = true;
    public static int cer = R.raw.android;

    public static final String HANDSHAKE_KEY = "";
    public static boolean isDebuggingMode = false;

//    public static final String BASE_URL = "://jsqa.inov8.com.pk/i8Microbank";
//    public static final String BASE_URL = "://192.168.51.82:8080/i8Microbank";
    public static final String BASE_URL = "://jsblb.inov8.com.pk/i8Microbank";


//    public static final String HOST_NAME = "jsqa.inov8.com.pk";
    public static final String HOST_NAME = "jsblb.inov8.com.pk";

    public static final String PROTOCOL = "http";
    public static final String SIGNATURE = "Iwlf2gfMBxsgsv35/jmZe9YUpcM=";  // signed

//    public static final String CUSTOM_PROXY_VERIFY = "http://192.168.50.98:9070/js-nadra-integration/services/api/verify"; // local
    public static final String CUSTOM_PROXY_VERIFY = "http://10.0.1.221:8181/js-nadra-integration/services/api/verify"; // Internal


}