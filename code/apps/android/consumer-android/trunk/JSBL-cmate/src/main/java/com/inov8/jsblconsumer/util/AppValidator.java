package com.inov8.jsblconsumer.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;


import java.security.MessageDigest;

/**
 * Created by soofiafa on 4/17/2017.
 */

public class AppValidator {
    private Context context;

    public AppValidator(Context context) {
        this.context = context;
    }


    static {
        System.loadLibrary("keys");
    }

    public native String getScanner9();

    public native String getScanner10();

    public native String getScanner11();

    public native String getScanner12();

    public String validateApp() {
        String message = null;

        if (!verifyAppSignature()) {
            message = AppMessages.INVALID_APP;
            /**
             * Uncomment for Production Build
             */
//        } else if (!verifyInstaller()) {
//            message = AppMessages.INVALID_INSTALLER;
        } else

            if (RootUtil.isDeviceRooted()) {
            message = AppMessages.DEVICE_ROOTED;
        } else if (verifyEmulator()) {
            message = AppMessages.INVALID_ENVIRONMENT;
        } else if (verifyDebuggable()) {
            message = AppMessages.INVALID_DEBUGGABLE;
        }
        return message;
    }

    public boolean verifyAppSignature() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA1");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());

                String currentSignature = Base64.encodeToString(md.digest(), Base64.NO_WRAP);


//                if(!BuildConstants.SIGNATURE.equals("5WP7ivqnp71ed4uOIL10DUU6zwo=")){
//                    String[] a = new String[4];
//                    a[0] = getScanner9();
//                    a[1] = getScanner10();
//                    a[2] = getScanner11();
//                    a[3] = getScanner12();
//                    String sig = new DecodingUtility().decode(a);
//
//
//                }


                //compare signatures
                if ((packageInfo.signatures.length == 1) && (Constants.SIGNATURE).equals(currentSignature)) {
                    return true;
                }
            }
        } catch (Exception e) {
            //assumes an issue in checking signature., but we let the caller decide on what to do.
        }
        return false;
    }

    private static final String PLAY_STORE_APP_ID = "com.inov8.jsblconsumer";

    private boolean verifyInstaller() {
        String packageName = context.getPackageName();
        final String installer = context.getPackageManager().getInstallerPackageName(packageName);
        return ((installer != null) && installer.startsWith(PLAY_STORE_APP_ID));
    }

    private String getSystemProperty(String name) throws Exception {
        Class systemPropertyClazz = Class.forName("android.os.SystemProperties");
        return (String) systemPropertyClazz.getMethod("get", new Class[]{
                String.class}).invoke(systemPropertyClazz, new Object[]{name});
    }

    public boolean verifyEmulator() {
        try {
            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
            boolean qemu = "1".equals(getSystemProperty("ro.kernel.qemu"));
            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");
            if (qemu || goldfish || sdk) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean verifyDebuggable() {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}
