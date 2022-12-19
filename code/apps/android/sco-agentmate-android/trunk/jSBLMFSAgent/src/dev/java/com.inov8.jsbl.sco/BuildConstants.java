package com.inov8.jsbl.sco;

public class BuildConstants{

    public static final String PROTOCOL = "http://";
    public static final String BASE_URL =
            "jsqa.inov8.com.pk/i8Microbank"; // QA
//	 "jsstaging.inov8.com.pk/i8Microbank"; // Staging
//	 "uat.inov8.com.pk/i8Microbank"; // UAT HTTP
//   "blb.jsbl.com/i8Microbank"; // Production HTTPS

    public static final String APP_DOWNLOAD_URL = PROTOCOL +
            "jsqa.inov8.com.pk/downloads/agent.html"; // QA
//	"jsstaging.inov8.com.pk/downloads/agent.html"; // Staging
//   "uat.inov8.com.pk/downloads/agent.html"; // UAT HTTP
//	 "blb.jsbl.com/downloads/agent.html"; // Production HTTPS

    public static final String HOSTNAME = "jsqa.inov8.com.pk";

    //                "https://blb.jsbl.com/nadra-biometric-integration/service/verify";  // Production
    public static final String NADRA_VERIFICATION_URL =
//            "http://10.0.1.40:7070/nadra-biometric-integration/service/verify";  // UAT
//        "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verify";  // UAT
//        "http://10.0.1.221:9090/nadra-biometric-integration/service/verify";  // UAT
            "http://10.0.1.100/svn/mobile_apps/jsbl-consumer/android/branches/JSBL-Consumer-180412";

    public static final String NADRA_OTC_URL =
//            "http://10.0.1.40:7070/nadra-biometric-integration/service/verifyOTC";  // UAT
            "http://nadra.inov8.com.pk/nadra-biometric-integration/service/verifyOTC";  // UAT
//    "https://blb.jsbl.com/nadra-biometric-integration/service/verifyOTC";  // Production

    public static final String PAYSYS_VERIFY_URL =
//  "http://10.0.1.221:8181/js-nadra-integration/services/api/verify"; // Internal
            "http://nadra.inov8.com.pk/js-nadra-integration/services/api/verify";  //  UAT
//    "https://blb.jsbl.com/js-nadra-integration/services/api/verify"; // Production
//            "http://nadra.inov8.com.pk/js-nadra-integration/services/api/verify";  //  UAT

    public static final String PAYSYS_OTC_URL =
//  "http://10.0.1.221:8181/js-nadra-integration/services/api/otc";  // Internal
            "http://nadra.inov8.com.pk/js-nadra-integration/services/api/otc";  //  UAT
//   "https://blb.jsbl.com/js-nadra-integration/services/api/otc";  // Production
//    "http://nadra.inov8.com.pk/js-nadra-integration/services/api/otc";  //  UAT

    public static final String SIGNATURE = "32mrCWQ38BwkM5eaOhRER05ioos=";  // debug

    public static String SECRET_KEY = new String("682ede816988e58fb6d057d9d85605e0");

    public static String SECURITY_KEY =
//            new String("xnNgITFF5kesvYqgUFhO3rIxQtra18wS2XIR0NekNXU=");
            new String("HDJMZ4AnbDK4zhc1wIGAgGBkPMDFjZjP193Dd0h7No0=");   // new certificate added

    public static final String HANDSHAKE_KEY = "f069b20cb3f7427d819b368afba72165";
    public static int cer = R.raw.android1;

    public static boolean isDummyFlow = true;
    public static boolean isCustomIP = true;
    public static boolean isDebuggingMode = true;
}