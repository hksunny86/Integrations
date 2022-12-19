package com.inov8.jsblconsumer.util;

import com.inov8.jsblconsumer.R;

public class BuildConstants {

    public static boolean isDummyFlow = false;
    public static boolean isCustomIP = false;
    public static boolean isDebuggingMode = false;
    public static int cer = R.raw.uat;

    public static final String BASE_URL = "://blbuat.jsbl.com/i8Microbank";
    public static final String HOST_NAME = "blbuat.jsbl.com";

    public static final String PROTOCOL = "https";
    public static final String SIGNATURE = "Iwlf2gfMBxsgsv35/jmZe9YUpcM=";  // signed
//  public static final String HANDSHAKE_KEY = "86mbmmwFjlBN0mmZGUoOwGCnj+HOYuPgvtHVYE8EFxk=";  // signed
//  public static final String HANDSHAKE_KEY = "NTFnLRsq1meTTOs7vsUymR4O4KioDXXrd07rHS8soYw=";  // signed
//  public static final String HANDSHAKE_KEY = "RQeZkB42znUfsDIIFWIRiYEcKl7nHwNFwWCrnMMJbVc=";  // new certificate 27/10/2021
    public static final String HANDSHAKE_KEY = "k1LF/mZrDtafelAcDvhb/wVQ8ZLdXDI4XVSlaMO6Uvc=";  // new certificate 27/10/2021

    public static final String CUSTOM_PROXY_VERIFY = "https://blbuat.jsbl.com/js-nadra-integration/services/api/verify";  //  UAT
}