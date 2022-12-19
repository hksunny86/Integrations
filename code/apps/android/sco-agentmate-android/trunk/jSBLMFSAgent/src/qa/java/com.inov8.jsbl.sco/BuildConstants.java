package com.inov8.jsbl.sco;

public class BuildConstants{

    public static final String PROTOCOL = "http://";
//    public static final String BASE_URL = "jsqa.inov8.com.pk/i8Microbank"; // QA
    public static final String BASE_URL = "//jsblb.inov8.com.pk/i8Microbank"; // QA
    public static final String APP_DOWNLOAD_URL = PROTOCOL + "jsblb.inov8.com.pk/downloads/agent.html"; // QA

    public static final String HOSTNAME = "jsblb.inov8.com.pk"; // qa
//    public static final String HOSTNAME = "jsqa.inov8.com.pk"; // qa

//    public static final String NADRA_VERIFICATION_URL = "http://10.0.1.80:9090/nadra-biometric-integration/service/verify";  // QA
//    public static final String NADRA_OTC_URL = "http://10.0.1.80:9090/nadra-biometric-integration/service/verifyOTC";  // QA
    public static final String NADRA_VERIFICATION_URL = "http://jsblb.inov8.com.pk/nadra-biometric-integration/service/verify";  // QA
    public static final String NADRA_OTC_URL = "http://jsblb.inov8.com.pk/nadra-biometric-integration/service/verifyOTC";  // QA
    public static final String PAYSYS_VERIFY_URL = "http://10.0.1.222:9070/js-nadra-integration/services/api/verify"; // Internal
    public static final String PAYSYS_OTC_URL = "http://10.0.1.222:9070/js-nadra-integration/services/api/otc";  // Internal

    public static final String SIGNATURE = "dLNHWokvDb8LX0/XB3/c9llyR8Q=";  // signed
    public static String SECRET_KEY = new String("682ede816988e58fb6d057d9d85605e0");
    public static String SECURITY_KEY = new String("HDJMZ4AnbDK4zhc1wIGAgGBkPMDFjZjP193Dd0h7No0=");   // new certificate added

    public static int cer = R.raw.android1;
    public static final String HANDSHAKE_KEY = "f069b20cb3f7427d819b368afba72165";

    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = true;
    public static boolean isDebuggingMode = true;

}