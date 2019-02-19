package com.cathay.jvm.cache.spring;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "cathay")
public class CacheManagerProperties {
    public Map<String, CacheProperties> cacheManager;

    public void setCacheManager(Map<String, CacheProperties> cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Map<String, CacheProperties> getCacheManager() {
        return this.cacheManager;
    }
}
