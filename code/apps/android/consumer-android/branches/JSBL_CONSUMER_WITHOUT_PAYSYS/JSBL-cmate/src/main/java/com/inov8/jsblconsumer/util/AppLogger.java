package com.inov8.jsblconsumer.util;

import android.util.Log;

public class AppLogger {

    private static final String TAG = "JS-CONSUMER";

    // -display all logs only when isDebuggingMode = true
    private static boolean isDebuggingMode = BuildConstants.isDebuggingMode;

    public static void setDebuggingMode(boolean isDebuggingMode) {
        AppLogger.isDebuggingMode = isDebuggingMode;
    }

    public static void e(String msg) {
        if (isDebuggingMode)
            Log.e(TAG, msg);
    }

    public static void e(Exception e) {
        if (isDebuggingMode) {
            try {
                Log.e(TAG, e.getMessage());
            } catch (Exception e1) {
                try {
                    Log.e(TAG,
                            "Some Exception occured and unable to print Exception Message");
                    Log.e(TAG, "Cause = " + e1.getStackTrace());
                } catch (Exception e2) {
                    Log.e(TAG,
                            "Some Exception occured and unable to print Exception Cause");
                }
            }
        }
    }

    public static void i(String msg) {
        if (isDebuggingMode)
            Log.i(TAG, msg);
    }
}