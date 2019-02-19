package com.cathay.jvm.cache;

import java.util.Arrays;

public class MethodKey {
    private String methodName;
    private Class[] parameterTypes;

    public MethodKey(String methodName, Class[] parameterTypes) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public Class[] getParameterTypes() {
        return this.parameterTypes;
    }


}
