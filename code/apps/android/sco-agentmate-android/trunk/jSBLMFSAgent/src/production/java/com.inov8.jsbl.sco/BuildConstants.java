package com.inov8.jsbl.sco;


public class BuildConstants{

    public static final String PROTOCOL = "https://";
    public static final String BASE_URL = "blb.jsbl.com/i8Microbank"; // Production HTTPS
    public static final String APP_DOWNLOAD_URL = PROTOCOL +
        "blb.jsbl.com/downloads/scoagent.html"; // Production HTTPS

    public static final String HOSTNAME = "blb.jsbl.com"; // Production HTTPS

    public static final String NADRA_VERIFICATION_URL =
            "https://blb.jsbl.com/nadra-biometric-integration/service/verify";  // Production

    public static final String NADRA_OTC_URL =
            "https://blb.jsbl.com/nadra-biometric-integration/service/verifyOTC";  // Production

    public static final String PAYSYS_VERIFY_URL =
        "https://blb.jsbl.com/js-nadra-integration/services/api/verify"; // Production

    public static final String PAYSYS_OTC_URL =
        "https://blb.jsbl.com/js-nadra-integration/services/api/otc";  // Production

    public static final String SIGNATURE = "dLNHWokvDb8LX0/XB3/c9llyR8Q=";  // signed
    public static String SECRET_KEY = new String("682ede816988e58fb6d057d9d85605e0");

    public static String SECURITY_KEY = new String("NTFnLRsq1meTTOs7vsUymR4O4KioDXXrd07rHS8soYw=");
    public static final String HANDSHAKE_KEY = "f069b20cb3f7427d819b368afba72165";

    public static int cer = R.raw.android1;

    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = false;
    public static boolean isDebuggingMode = false;

}