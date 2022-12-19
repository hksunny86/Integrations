package com.inov8.jsblconsumer.util;

import com.inov8.jsblconsumer.R;

public class BuildConstants {


    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = false;
    public static boolean isDebuggingMode = false;

    public static final String BASE_URL = myUrl();
    public static final String HOST_NAME = myHSTN();

    public static int cer = R.raw.android;

    public static final String PROTOCOL = "https";
    public static final String SIGNATURE = "Iwlf2gfMBxsgsv35/jmZe9YUpcM=";  // signed


 //   public static final String HANDSHAKE_KEY = "wrb643gZOircgHdQltYThPWmhPS6RWGafR3MRFO6Vqo=";
    public static final String HANDSHAKE_KEY = "NTFnLRsq1meTTOs7vsUymR4O4KioDXXrd07rHS8soYw=";


    public static final String CUSTOM_PROXY_VERIFY = myUrlNadra();

    static {
        System.loadLibrary("keys");
    }

    public static native String getScanner1();

    public static native String getScanner2();

    public static native String getScanner3();

    public static native String getScanner4();

    public static native String getScanner5();

    public static native String getScanner6();

    public static native String getScanner7();

    public static native String getScanner8();

    public static native String getScanner9();

    public static native String getScanner10();

    public static native String getScanner11();

    public static native String getScanner12();


    public static native String getScanner17();

    public static native String getScanner18();

    public static native String getScanner19();

    public static native String getScanner20();

    public static String myUrl() {
        String[] a = new String[4];
        a[0] = getScanner1();
        a[1] = getScanner2();
        a[2] = getScanner3();
        a[3] = getScanner4();
        return new DecodingUtility().decode(a);
    }

    public static String myHSTN() {
        String[] a = new String[4];
        a[0] = getScanner5();
        a[1] = getScanner6();
        a[2] = getScanner7();
        a[3] = getScanner8();
        return new DecodingUtility().decode(a);
    }

    public static String myUrlNadra() {
        String[] a = new String[4];
        a[0] = getScanner9();
        a[1] = getScanner10();
        a[2] = getScanner11();
        a[3] = getScanner12();
        return new DecodingUtility().decode(a);
    }

    public static String getHandshakeKey() {

        String[] c = new String[4];
        c[0] = getScanner17();
        c[1] = getScanner18();
        c[2] = getScanner19();
        c[3] = getScanner20();
     return  new DecodingUtility().decode(c);

    }
}