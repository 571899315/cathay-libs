package com.cathay.jvm.cache;


import com.cathay.jvm.cache.annotation.Cached;

import java.lang.reflect.Method;

public class ConfigAnnotationReader {
    public static Config getConfig(Method method, Config customerizedConfig) {
        if (customerizedConfig != null) {
            return customerizedConfig;
        }

        Cached cacheConfig = (Cached) method.getDeclaredAnnotation(Cached.class);
        if (cacheConfig != null) {
            return readFromAnnotation(cacheConfig);
        }

        return new Config();
    }

    private static Config readFromAnnotation(Cached cacheConfig) {
        Config config = new Config();
        config.setSoftValues(cacheConfig.softValues());
        config.setMaxSize(cacheConfig.maxSize());
        config.setExpireMilliSeconds(cacheConfig.expireMilliSeconds());
        return config;
    }
}
