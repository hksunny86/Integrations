package com.inov8.jsbl.sco;

    public class BuildConstants{

        public static final String PROTOCOL = "https://";
        public static final String BASE_URL = "blbstg.jsbl.com:2087/i8Microbank"; // Staging HTTPS
        public static final String APP_DOWNLOAD_URL = PROTOCOL +
                "blbstg.jsbl.com:2087/downloads/scoagent.html"; // Staging HTTPS

        public static final String HOSTNAME = "blbstg.jsbl.com"; // Staging HTTPS

        public static final String NADRA_VERIFICATION_URL =
                "https://blbstg.jsbl.com:2087/nadra-biometric-integration/service/verify";  // Staging

        public static final String NADRA_OTC_URL =
                "https://blbstg.jsbl.com:2087/nadra-biometric-integration/service/verifyOTC";  // Staging

        public static final String PAYSYS_VERIFY_URL =
                "https://blbstg.jsbl.com:2087/js-nadra-integration/services/api/verify"; // Staging

        public static final String PAYSYS_OTC_URL =
                "https://blbstg.jsbl.com:2087/js-nadra-integration/services/api/otc";  // Staging

        public static final String SIGNATURE = "dLNHWokvDb8LX0/XB3/c9llyR8Q=";  // signed

        public static String SECRET_KEY = new String("682ede816988e58fb6d057d9d85605e0");

        public static String SECURITY_KEY = new String("NTFnLRsq1meTTOs7vsUymR4O4KioDXXrd07rHS8soYw=");   // new certificate added

        public static int cer = R.raw.android1;
        public static final String HANDSHAKE_KEY = "f069b20cb3f7427d819b368afba72165";

        public static boolean isDummyFlow = false;
        public static boolean isCustomIP = false;
        public static boolean isDebuggingMode = false;

    }