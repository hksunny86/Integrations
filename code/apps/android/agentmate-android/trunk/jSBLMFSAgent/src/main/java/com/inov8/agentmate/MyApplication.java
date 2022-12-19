package com.inov8.agentmate;

import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }
}
