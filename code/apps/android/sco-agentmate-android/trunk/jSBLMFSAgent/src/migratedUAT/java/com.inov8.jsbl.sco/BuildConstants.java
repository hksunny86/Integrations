package com.inov8.jsbl.sco;

public class BuildConstants{

    public static final String PROTOCOL = "https://";
    public static final String BASE_URL = "blbuat.jsbl.com/i8Microbank"; // UAT HTTP
    public static final String APP_DOWNLOAD_URL = PROTOCOL +
            "blbuat.jsbl.com/downloads/scoagent.html"; // UAT HTTP

    public static final String HOSTNAME = "blbuat.jsbl.com"; // UAT HTTP

    public static final String NADRA_VERIFICATION_URL =
            "https://blbuat.jsbl.com/nadra-biometric-integration/service/verify";  // Production
//        "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verify";  // UAT

    public static final String NADRA_OTC_URL =
            "https://blbuat.jsbl.com/nadra-biometric-integration/service/verifyOTC";  // Production
//            "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verifyOTC";  // UAT

    public static final String PAYSYS_VERIFY_URL =
            "https://blbuat.jsbl.com/js-nadra-integration/services/api/verify"; // Production
//        "http://nadra.inov8.com.pk/js-nadra-integration/services/api/verify";  //  UAT

    public static final String PAYSYS_OTC_URL =
            "https://blbuat.jsbl.com/js-nadra-integration/services/api/otc";  // Production
//        "http://nadra.inov8.com.pk/js-nadra-integration/services/api/otc";  //  UAT

    public static final String SIGNATURE = "dLNHWokvDb8LX0/XB3/c9llyR8Q=";  // signed

    public static String SECRET_KEY = new String("682ede816988e58fb6d057d9d85605e0");

    public static String SECURITY_KEY = new String("NTFnLRsq1meTTOs7vsUymR4O4KioDXXrd07rHS8soYw="); // new certificate added

    public static final String HANDSHAKE_KEY = "f069b20cb3f7427d819b368afba72165";

    public static int cer = R.raw.uat;
    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = false;
    public static boolean isDebuggingMode = false;

}