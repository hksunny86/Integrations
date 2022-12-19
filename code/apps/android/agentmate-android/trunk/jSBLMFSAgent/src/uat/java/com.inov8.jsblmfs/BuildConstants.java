package com.inov8.jsblmfs;

public class BuildConstants{

    public static final String PROTOCOL = "http://";
    public static final String BASE_URL = "uat.inov8.com.pk/i8Microbank"; // UAT HTTP
    public static final String APP_DOWNLOAD_URL = PROTOCOL +
            "uat.inov8.com.pk/downloads/agent.html"; // UAT HTTP

    public static final String HOSTNAME = "uat.inov8.com.pk"; // UAT HTTP

    public static final String NADRA_VERIFICATION_URL =
//            "https://blb.jsbl.com/nadra-biometric-integration/service/verify";  // Production
        "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verify";  // UAT

    public static final String NADRA_OTC_URL =
//            "https://blb.jsbl.com/nadra-biometric-integration/service/verifyOTC";  // Production
            "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verifyOTC";  // UAT

    public static final String PAYSYS_VERIFY_URL =
//            "https://blb.jsbl.com/js-nadra-integration/services/api/verify"; // Production
        "http://nadra.inov8.com.pk/js-nadra-integration/services/api/verify";  //  UAT

    public static final String PAYSYS_OTC_URL =
//            "https://blb.jsbl.com/js-nadra-integration/services/api/otc";  // Production
        "http://nadra.inov8.com.pk/js-nadra-integration/services/api/otc";  //  UAT

    public static final String SIGNATURE = "dLNHWokvDb8LX0/XB3/c9llyR8Q=";  // signed

    public static String SECRET_KEY = new String("682ede816988e58fb6d057d9d85605e0");

    public static String SECURITY_KEY =
            //new String("xnNgITFF5kesvYqgUFhO3rIxQtra18wS2XIR0NekNXU=");
            new String("HDJMZ4AnbDK4zhc1wIGAgGBkPMDFjZjP193Dd0h7No0=");   // new certificate added

    public static int cer = R.raw.android1;

    public static final String HANDSHAKE_KEY = "f069b20cb3f7427d819b368afba72165";

    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = false;
    public static boolean isDebuggingMode = false;

}