package com.inov8.agentmate.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.inov8.jsblmfs.BuildConfig;

import java.security.MessageDigest;

public class AppValidator {
    private Context context;

    public AppValidator(Context context) {
        this.context = context;
    }

    public String validateApp() {
        String message = null;

        if (!verifyAppSignature()) {
            message = Constants.Messages.INVALID_APP;
        }
        else if (verifyEmulator()) {
            message = Constants.Messages.INVALID_ENVIRONMENT;
        }
        else if (verifyDebuggable()) {
            if(!BuildConfig.DEBUG)
                message = Constants.Messages.INVALID_DEBUGGABLE;
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

    private static final String PLAY_STORE_APP_ID = "com.inov8.timepey.mfs";

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
