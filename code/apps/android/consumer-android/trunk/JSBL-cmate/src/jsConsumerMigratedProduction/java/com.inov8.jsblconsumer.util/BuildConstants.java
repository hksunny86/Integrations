package com.inov8.jsblconsumer.util;

import com.inov8.jsblconsumer.R;

public class BuildConstants {


    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = false;
    public static boolean isDebuggingMode = false;
    public static int cer = R.raw.uat;

    public static final String BASE_URL = "://blbnew.jsbl.com/i8Microbank";

    public static final String HOST_NAME = "blbnew.jsbl.com";

    public static final String PROTOCOL = "https";
    public static final String SIGNATURE = "Iwlf2gfMBxsgsv35/jmZe9YUpcM=";  // signed
    public static final String HANDSHAKE_KEY = "mIji5UtV2Yb6SU0KbSwMNj1Sv10iLEJeOvlu1uZ8VvE=";  // signed

    public static final String CUSTOM_PROXY_VERIFY = "https://blbnew.jsbl.com/js-nadra-integration/services/api/verify";  //  UAT
}