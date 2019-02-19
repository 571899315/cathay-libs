package com.cathay.jvm.cache;

import java.lang.reflect.Method;
import java.util.Arrays;


public class MethodRequestKey {
    private Method method;
    private Object[] params;

    public Method getMethod() {
        return this.method;
    }

    public Object[] getParams() {
        return this.params;
    }

    public MethodRequestKey(Method method, Object[] params) {
        this.method = method;
        this.params = new Object[params.length];
        System.arraycopy(params, 0, this.params, 0, params.length);
    }


}
