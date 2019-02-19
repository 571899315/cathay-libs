package com.cathay.jvm.cache;


import java.util.*;
import java.lang.reflect.*;


import java.util.concurrent.*;

import com.google.common.cache.*;
import org.slf4j.*;

public class InvocationCacheHandler implements InvocationHandler {
    private static final Logger log;
    private final Class<?> itf;
    private final Object targetObj;
    private Map<MethodKey, LoadingCache<MethodRequestKey, ?>> caches;

    public InvocationCacheHandler(final Class<?> itf, final Object targetObj) {
        this.caches = new ConcurrentHashMap<MethodKey, LoadingCache<MethodRequestKey, ?>>();
        this.itf = itf;
        this.targetObj = targetObj;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final LoadingCache<MethodRequestKey, ?> cache = this.caches.get(new MethodKey(method.getName(), method.getParameterTypes()));
        if (cache == null) {
            return method.invoke(this.targetObj, args);
        }
        final MethodRequestKey key = new MethodRequestKey(method, args);
        final Object cachedObj = cache.get(key);
        return cachedObj;
    }

    public void build(final Method method, final Config customerizedConfig) {
        final CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        builder.expireAfterWrite(customerizedConfig.getExpireMilliSeconds(), TimeUnit.MILLISECONDS);
        builder.maximumSize((long) customerizedConfig.getMaxSize());
        if (customerizedConfig.isSoftValues()) {
            builder.softValues();
        }
        try {
            final Method proxyMethod = this.itf.getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (proxyMethod != null) {
                final String clazzName = this.itf.getSimpleName();
                final LoadingCache<MethodRequestKey, ?> cache = (LoadingCache<MethodRequestKey, ?>) builder.build((CacheLoader) new CacheLoader<MethodRequestKey, Object>() {
                    public Object load(final MethodRequestKey key) throws Exception {
                        InvocationCacheHandler.log.info("retrieve from original method, method: " + clazzName + "." + key.getMethod().getName());
                        return key.getMethod().invoke(InvocationCacheHandler.this.targetObj, key.getParams());
                    }
                });
                this.caches.put(new MethodKey(proxyMethod.getName(), proxyMethod.getParameterTypes()), cache);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            InvocationCacheHandler.log.error("method " + method.toString() + " is not declared in " + this.itf.getName());
            InvocationCacheHandler.log.error(e.getMessage(), (Throwable) e);
        }
    }

    static {
        log = LoggerFactory.getLogger((Class) InvocationCacheHandler.class);
    }
}