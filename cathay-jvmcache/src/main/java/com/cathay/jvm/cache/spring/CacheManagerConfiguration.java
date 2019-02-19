package com.cathay.jvm.cache.spring;

import com.google.common.cache.Cache;
import org.springframework.boot.context.properties.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.support.*;
import org.springframework.cache.*;
import com.google.common.cache.*;
import org.springframework.cache.guava.*;

import java.util.*;

import org.springframework.boot.autoconfigure.cache.*;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;

@Configuration
@EnableConfigurationProperties({CacheManagerProperties.class})
@ConditionalOnProperty(prefix = "cathay", name = {"cache-manager"})
public class CacheManagerConfiguration {
    private CacheManagerProperties cacheManagerProperties;

    @Autowired
    public CacheManagerConfiguration(final CacheManagerProperties cacheManagerProperties) {
        this.cacheManagerProperties = cacheManagerProperties;
    }

    private static void checkArguments(final CacheManagerProperties cacheManagerProperties) {
        if (cacheManagerProperties == null || cacheManagerProperties.cacheManager == null || cacheManagerProperties.cacheManager.isEmpty()) {
            throw new IllegalArgumentException("missing '{cache-name}' property");
        }
        if (!cacheManagerProperties.getCacheManager().entrySet().stream().allMatch(entry -> Objects.nonNull(entry.getKey()) && Objects.nonNull(((CacheProperties) entry.getValue()).getSpec()) && Objects.nonNull(((CacheProperties) entry.getValue()).getType()))) {
            throw new IllegalArgumentException("invalid 'type' or 'spec' property");
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        checkArguments(this.cacheManagerProperties);
        final SimpleCacheManager manager = new SimpleCacheManager();
        final List<Cache> caches = new LinkedList<Cache>();
        for (final Map.Entry<String, CacheProperties> entry : this.cacheManagerProperties.getCacheManager().entrySet()) {
            final CacheType type = entry.getValue().getType();
            switch (type) {
                case GUAVA: {
                    final GuavaCache cache = new GuavaCache((String) entry.getKey(), CacheBuilder.from(entry.getValue().getSpec()).build());
                    caches.add((Cache) cache);
                    continue;
                }
                default: {
                    throw new UnsupportedOperationException(String.format("cache type %s not supported", type));
                }
            }
        }
        manager.setCaches((Collection) caches);
        return (CacheManager) manager;
    }
}
