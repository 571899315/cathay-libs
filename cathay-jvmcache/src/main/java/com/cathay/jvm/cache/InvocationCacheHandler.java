package com.cathay.jvm.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvocationCacheHandler implements org.springframework.cglib.proxy.InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(InvocationCacheHandler.class);

    private final Class<?> itf;

    private final Object targetObj;

    private Map<MethodKey, LoadingCache<MethodRequestKey, ?>> caches = new ConcurrentHashMap();

    public InvocationCacheHandler(Class<?> itf, Object targetObj) {
        this.itf = itf;
        this.targetObj = targetObj;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        LoadingCache<MethodRequestKey, ?> cache = (LoadingCache) this.caches.get(new MethodKey(method.getName(), method.getParameterTypes()));

        if (cache == null) {
            return method.invoke(this.targetObj, args);
        }

        MethodRequestKey key = new MethodRequestKey(method, args);
        Object cachedObj = cache.get(key);


//     if ((cachedObj instanceof AbstractResponse)) {
//       AbstractResponse response = (AbstractResponse)cachedObj;
//       if (ResponseStatus.EXCEPTION.equals(response.getStatus())) {
//         log.info("response is EXCEPTION, invalid the cached object, method: " + this.itf.getSimpleName() + "." + method
//           .getName());
//         cache.invalidate(key);
//       }
//     }

        return cachedObj;
    }


    public void build(Method method, Config customerizedConfig) {
        CacheBuilder<?, ?> builder_ = CacheBuilder.newBuilder();
        CacheBuilder<MethodRequestKey, Object> builder = builder_;

        builder.expireAfterWrite(customerizedConfig.getExpireMilliSeconds(), TimeUnit.MILLISECONDS);
        builder.maximumSize(customerizedConfig.getMaxSize());
        if (customerizedConfig.isSoftValues()) {
            builder.softValues();
        }
        try {
            Method proxyMethod = this.itf.getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (proxyMethod != null) {
                final String clazzName = this.itf.getSimpleName();
                LoadingCache<MethodRequestKey, ?> cache = builder.build(new CacheLoader() {
                    public Object load(MethodRequestKey key) throws Exception {
                        InvocationCacheHandler.log.info("retrieve from original method, method: " + clazzName + "." + key
                                .getMethod().getName());
                        return key.getMethod().invoke(InvocationCacheHandler.this.targetObj, key.getParams());
                    }


                });
                this.caches.put(new MethodKey(proxyMethod.getName(), proxyMethod.getParameterTypes()), cache);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            log.error("method " + method.toString() + " is not declared in " + this.itf.getName());
            log.error(e.getMessage(), e);
        }
    }
}
