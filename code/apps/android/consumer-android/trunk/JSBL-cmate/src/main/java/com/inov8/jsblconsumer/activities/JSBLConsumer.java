package com.inov8.jsblconsumer.activities;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by Rehan on 6/4/2017.
 */

public class JSBLConsumer extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
