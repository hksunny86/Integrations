package com.inov8.agentmate.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector {
	public static final int MODE = Context.MODE_PRIVATE;
	public static final String PREF_NAME = "agentmate_jsbl_preferences";
	public static final String USER_ID = "userId";
	public static final String CATALOG_VERSION = "cvno";
	public static final String CATALOG_DATA = "catalog";
	public static final String VERSION_USAGE_LEVEL = "versionUsageLevel";
	public static final String APP_VERSION = "appVersion";
	public static final String CUSTOM_IP  = "customIp";
	public static final String IS_ROOTED  = "0";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static void clearPreferences(Context context){
        getEditor(context).clear().commit();
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key,
			boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}
}