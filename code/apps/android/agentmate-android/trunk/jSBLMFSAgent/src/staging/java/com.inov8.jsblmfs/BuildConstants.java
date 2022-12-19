package com.inov8.jsblmfs;

public class BuildConstants{

    public static final String PROTOCOL = "http://";
    public static final String BASE_URL = "jsstaging3.inov8.com.pk/i8Microbank"; // Staging
    public static final String APP_DOWNLOAD_URL = PROTOCOL +
            "jsstaging3.inov8.com.pk/downloads/agent.html"; // Staging

    public static final String HOSTNAME = "jsstaging3.inov8.com.pk"; // Staging3

    public static final String NADRA_VERIFICATION_URL =
//            "http://10.0.1.80:9090/nadra-biometric-integration/service/verify";  // Staging
    "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verify";  // UAT

    public static final String NADRA_OTC_URL =
//            "http://10.0.1.80:9090/nadra-biometric-integration/service/verifyOTC";  // Staging
    "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verifyOTC";  // UAT

    public static final String PAYSYS_VERIFY_URL =
//            "http://10.0.1.221:8181/js-nadra-integration/services/api/verify"; // Internal
    "http://nadra.inov8.com.pk/js-nadra-integration/services/api/verify";  //  UAT

    public static final String PAYSYS_OTC_URL =
//            "http://10.0.1.221:8181/js-nadra-integration/services/api/otc";  // Internal
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
    public static boolean isDebuggingMode = true;

}