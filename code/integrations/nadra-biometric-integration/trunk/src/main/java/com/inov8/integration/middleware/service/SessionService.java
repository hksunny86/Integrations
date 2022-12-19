package com.inov8.integration.middleware.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by inov8 on 4/7/2016.
 */
public class SessionService {
    Cache<String, String> SESSION_CACHE = CacheBuilder
            .newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES) // cache will expire after 1 minute of access
            .recordStats().build();

    public  void saveSession(String key, String value) {
        SESSION_CACHE.put(key, value);
    }

    public String getSession(String key) {

        return SESSION_CACHE.getIfPresent(key);

    }
}