package com.cathay.jvm.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cathay.jvm.cache.annotation.Cached;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;


public class CachedInvocationProxyBuilder {
    public static <T> T buildProxy(Class<T> itf, T targetObj) {
        return (T) buildProxyFromImpl(itf, targetObj, itf);
    }


    public static <T> T buildProxyFromImpl(Class<T> itf, T targetObj, Class<?> cachedClazz) {
        if (!itf.isAssignableFrom(cachedClazz)) {
            throw new IllegalStateException("itf class must be assignable from cached Class: " + itf.getSimpleName() + ", " + cachedClazz.getSimpleName());
        }

        Map<Method, Config> methodConfigs = getConfigs(cachedClazz, null);

        return (T) build(itf, targetObj, methodConfigs);
    }


    public static <T> T buildProxyFromConfigs(Class<T> itf, T targetObj, Map<String, Config> configs) {
        Map<Method, Config> methodConfigs = getConfigs(itf, configs);

        return (T) build(itf, targetObj, methodConfigs);
    }

    private static <T> T build(Class<T> itf, T targetObj, Map<Method, Config> methodConfigs) {
        if (!itf.isInterface()) {
            throw new IllegalStateException("the class is not an interface: " + itf.getSimpleName());
        }

        if (methodConfigs.size() == 0) {
            return targetObj;
        }

        InvocationCacheHandler invocationWrapper = new InvocationCacheHandler(itf, targetObj);
        for (Map.Entry<Method, Config> entry : methodConfigs.entrySet()) {
            invocationWrapper.build((Method) entry.getKey(), (Config) entry.getValue());
        }

        return (T) enhance(itf, invocationWrapper);
    }

    static Map<Method, Config> getConfigs(Class<?> cachedClazz, Map<String, Config> customerizedConfig) {
        Map<Method, Config> configs = new HashMap();

        Set<Method> methods = new HashSet();
        if ((customerizedConfig != null) && (!customerizedConfig.isEmpty())) {
            methods.addAll(getMethodNeedCachedByConfig(cachedClazz, customerizedConfig));
        }

        methods.addAll(getMethodsHasCachedAnnotation(cachedClazz.getDeclaredMethods()));

        for (Method method : methods) {
            Config methodConfig = customerizedConfig != null ? (Config) customerizedConfig.get(method.getName()) : null;
            methodConfig = ConfigAnnotationReader.getConfig(method, methodConfig);
            configs.put(method, methodConfig);
        }

        return configs;
    }

    private static Set<Method> getMethodNeedCachedByConfig(Class<?> clazz, Map<String, Config> configs) {
        Set<Method> methods = new HashSet();

        Set<String> methodNames = new HashSet();
        for (Method method : clazz.getDeclaredMethods()) {
            if (configs.containsKey(method.getName())) {


                if (methodNames.contains(method.getName())) {
                    throw new IllegalStateException("don't support overloaded method to build cache proxy. " + method.getName());
                }
                methodNames.add(method.getName());


                methods.add(method);
            }
        }

        return methods;
    }

    private static <T> T enhance(final Class<T> itf, final InvocationCacheHandler invocationWrapper) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{itf});
        enhancer.setCallback((Callback) invocationWrapper);
        final T proxy = (T) enhancer.create();
        return proxy;
    }

    private static List<Method> getMethodsHasCachedAnnotation(Method[] methods) {
        List<Method> methodList = new ArrayList();
        for (Method method : methods) {
            Cached cachedAnnotation = (Cached) method.getDeclaredAnnotation(Cached.class);
            if (cachedAnnotation != null) {
                methodList.add(method);
            }
        }
        return methodList;
    }
}
